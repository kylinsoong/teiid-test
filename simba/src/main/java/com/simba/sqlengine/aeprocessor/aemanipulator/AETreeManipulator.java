package com.simba.sqlengine.aeprocessor.aemanipulator;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEBinaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnaryRelationalExpr;
import com.simba.sqlengine.dsiext.dataengine.PassdownInformation;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AETreeManipulator
{
  public static void pushDownFilter(PassdownInformation paramPassdownInformation, AEBooleanExpr paramAEBooleanExpr, AERelationalExpr paramAERelationalExpr)
    throws ErrorException
  {
    if ((null == paramAEBooleanExpr) || (null == paramAERelationalExpr)) {
      throw new NullPointerException("Cannot pass null parameters to pushDownFilter.");
    }
    paramAEBooleanExpr.setIsOptimized(true);
    switch (PushDownType.getNodeType(paramAERelationalExpr))
    {
    case JOIN: 
      pushToJoin(paramAEBooleanExpr, (AEJoin)paramAERelationalExpr);
      break;
    case CROSSJOIN: 
      pushToCrossJoin(paramAEBooleanExpr, (AECrossJoin)paramAERelationalExpr);
      break;
    case TABLE: 
      pushToTable(paramAEBooleanExpr, (AETable)paramAERelationalExpr, paramPassdownInformation);
      break;
    case SELECT: 
      pushToSelect(paramAEBooleanExpr, (AESelect)paramAERelationalExpr);
      break;
    case TABLE_SUBQUERY: 
      pushToTableSubquery(paramAEBooleanExpr, (AESubQuery)paramAERelationalExpr);
      break;
    case PROJECT: 
      insertSelectFilterInUnaryRelExpr(paramAEBooleanExpr, (AEProject)paramAERelationalExpr);
    }
  }
  
  public static void convertJoinToCrossJoin(AEJoin paramAEJoin)
  {
    if (null == paramAEJoin) {
      throw new NullPointerException("join parameter cannot be null.");
    }
    IAENode localIAENode = paramAEJoin.getParent();
    AECrossJoin localAECrossJoin = new AECrossJoin(paramAEJoin.getLeftOperand(), paramAEJoin.getRightOperand());
    localAECrossJoin.setParent(localIAENode);
    replaceRelExprOperand(localIAENode, localAECrossJoin, paramAEJoin);
  }
  
  public static void convertCrossJoinToInnerJoin(AECrossJoin paramAECrossJoin, AESelect paramAESelect)
  {
    if ((null == paramAECrossJoin) || (null == paramAESelect)) {
      throw new NullPointerException("Cannot pass null parameters to convertCrossJoinToInnerJoin.");
    }
    IAENode localIAENode = paramAESelect.getParent();
    AEJoin localAEJoin = new AEJoin(AEJoin.AEJoinType.INNER_JOIN, paramAECrossJoin.getLeftOperand(), paramAECrossJoin.getRightOperand());
    localAEJoin.setJoinCondition(paramAESelect.getCondition());
    localAEJoin.setParent(localIAENode);
    replaceRelExprOperand(localIAENode, localAEJoin, paramAESelect);
  }
  
  public static void removeSelect(AESelect paramAESelect)
  {
    assert ((paramAESelect.getCondition() instanceof AEBooleanTrue));
    replaceRelExprOperand(paramAESelect.getParent(), paramAESelect.getOperand(), paramAESelect);
  }
  
  private static void replaceRelExprOperand(IAENode paramIAENode, AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2)
  {
    if ((paramIAENode instanceof AEUnaryRelationalExpr))
    {
      ((AEUnaryRelationalExpr)paramIAENode).setOperand(paramAERelationalExpr1);
    }
    else if ((paramIAENode instanceof AEBinaryRelationalExpr))
    {
      AEBinaryRelationalExpr localAEBinaryRelationalExpr = (AEBinaryRelationalExpr)paramIAENode;
      if (localAEBinaryRelationalExpr.getLeftOperand() == paramAERelationalExpr2) {
        localAEBinaryRelationalExpr.setLeftOperand(paramAERelationalExpr1);
      } else {
        localAEBinaryRelationalExpr.setRightOperand(paramAERelationalExpr1);
      }
    }
    else
    {
      throw new IllegalStateException("Unexpected error in AETree manipulation.");
    }
  }
  
  private static void pushToJoin(AEBooleanExpr paramAEBooleanExpr, AEJoin paramAEJoin)
    throws ErrorException
  {
    assert ((null != paramAEBooleanExpr) && (null != paramAEJoin));
    AEBooleanExpr localAEBooleanExpr = paramAEJoin.getJoinCondition();
    if ((paramAEJoin.getJoinCondition() == paramAEBooleanExpr) || (subtreeContainsReference(localAEBooleanExpr, paramAEBooleanExpr))) {
      return;
    }
    if (localAEBooleanExpr.isEquivalent(paramAEBooleanExpr))
    {
      detachFilter(paramAEBooleanExpr);
      return;
    }
    Object localObject;
    if (paramAEJoin.isOuterJoin())
    {
      localObject = paramAEJoin.getParent();
      if ((localObject instanceof AEUnaryRelationalExpr)) {
        insertSelectFilterInUnaryRelExpr(paramAEBooleanExpr, (AEUnaryRelationalExpr)localObject);
      } else if ((localObject instanceof AEBinaryRelationalExpr)) {
        insertSelectFilterInBinaryRelExpr(paramAEBooleanExpr, (AEBinaryRelationalExpr)localObject, paramAEJoin);
      }
    }
    else
    {
      localObject = new AEAnd(detachFilter(paramAEBooleanExpr), localAEBooleanExpr);
      paramAEJoin.setJoinCondition((AEBooleanExpr)localObject);
    }
  }
  
  private static void pushToCrossJoin(AEBooleanExpr paramAEBooleanExpr, AECrossJoin paramAECrossJoin)
    throws ErrorException
  {
    assert ((null != paramAEBooleanExpr) && (null != paramAECrossJoin));
    IAENode localIAENode = paramAECrossJoin.getParent();
    assert (null != localIAENode);
    if ((localIAENode instanceof AESelect))
    {
      AEBooleanExpr localAEBooleanExpr = ((AESelect)localIAENode).getCondition();
      if ((localAEBooleanExpr == paramAEBooleanExpr) || (subtreeContainsReference(localAEBooleanExpr, paramAEBooleanExpr))) {
        return;
      }
    }
    switch (PushDownType.getNodeType(localIAENode))
    {
    case SELECT: 
      if (!paramAEBooleanExpr.isEquivalent(((AESelect)localIAENode).getCondition())) {
        pushToSelect(paramAEBooleanExpr, (AESelect)localIAENode);
      }
      break;
    case PROJECT: 
      insertSelectFilterInUnaryRelExpr(paramAEBooleanExpr, (AEUnaryRelationalExpr)localIAENode);
      break;
    case JOIN: 
    case CROSSJOIN: 
      insertSelectFilterInBinaryRelExpr(paramAEBooleanExpr, (AEBinaryRelationalExpr)localIAENode, paramAECrossJoin);
      break;
    case TABLE: 
    case TABLE_SUBQUERY: 
    default: 
      throw new IllegalStateException();
    }
  }
  
  private static void pushToTable(AEBooleanExpr paramAEBooleanExpr, AETable paramAETable, PassdownInformation paramPassdownInformation)
    throws ErrorException
  {
    IAENode localIAENode = paramAETable.getParent();
    switch (PushDownType.getNodeType(localIAENode))
    {
    case JOIN: 
    case CROSSJOIN: 
      int i = 0;
      if (paramPassdownInformation.canHandlePassdown(paramAEBooleanExpr)) {
        i = 1;
      } else if (paramPassdownInformation.canHandlePassdown((AERelationalExpr)localIAENode)) {
        i = 1;
      }
      if (i != 0) {
        insertSelectFilterInBinaryRelExpr(paramAEBooleanExpr, (AEBinaryRelationalExpr)localIAENode, paramAETable);
      }
      break;
    case SELECT: 
      pushToSelect(paramAEBooleanExpr, (AESelect)localIAENode);
      break;
    case PROJECT: 
      insertSelectFilterInUnaryRelExpr(paramAEBooleanExpr, (AEUnaryRelationalExpr)localIAENode);
      break;
    case TABLE: 
    case TABLE_SUBQUERY: 
    default: 
      throw new IllegalStateException();
    }
  }
  
  private static void pushToTableSubquery(AEBooleanExpr paramAEBooleanExpr, AESubQuery paramAESubQuery)
    throws ErrorException
  {
    IAENode localIAENode = paramAESubQuery.getParent();
    if ((localIAENode instanceof AEUnaryRelationalExpr)) {
      insertSelectFilterInUnaryRelExpr(paramAEBooleanExpr, (AEUnaryRelationalExpr)localIAENode);
    } else if ((localIAENode instanceof AEBinaryRelationalExpr)) {
      insertSelectFilterInBinaryRelExpr(paramAEBooleanExpr, (AEBinaryRelationalExpr)localIAENode, paramAESubQuery);
    }
  }
  
  private static void pushToSelect(AEBooleanExpr paramAEBooleanExpr, AESelect paramAESelect)
    throws ErrorException
  {
    AEAnd localAEAnd = new AEAnd(detachFilter(paramAEBooleanExpr), paramAESelect.getCondition());
    paramAESelect.setSelectCond(localAEAnd);
  }
  
  private static void insertSelectFilterInUnaryRelExpr(AEBooleanExpr paramAEBooleanExpr, AEUnaryRelationalExpr paramAEUnaryRelationalExpr)
    throws ErrorException
  {
    AERelationalExpr localAERelationalExpr = paramAEUnaryRelationalExpr.getOperand();
    paramAEUnaryRelationalExpr.setOperand(new AESelect(localAERelationalExpr, detachFilter(paramAEBooleanExpr)));
  }
  
  private static void insertSelectFilterInBinaryRelExpr(AEBooleanExpr paramAEBooleanExpr, AEBinaryRelationalExpr paramAEBinaryRelationalExpr, AERelationalExpr paramAERelationalExpr)
    throws ErrorException
  {
    if (paramAEBinaryRelationalExpr.getLeftOperand() == paramAERelationalExpr) {
      paramAEBinaryRelationalExpr.setLeftOperand(new AESelect(paramAERelationalExpr, detachFilter(paramAEBooleanExpr)));
    } else {
      paramAEBinaryRelationalExpr.setRightOperand(new AESelect(paramAERelationalExpr, detachFilter(paramAEBooleanExpr)));
    }
  }
  
  private static AEBooleanExpr detachFilter(AEBooleanExpr paramAEBooleanExpr)
    throws ErrorException
  {
    assert (null != paramAEBooleanExpr);
    AEBooleanExpr localAEBooleanExpr = null;
    IAENode localIAENode = paramAEBooleanExpr.getParent();
    if (null != localIAENode)
    {
      if ((localIAENode instanceof AEAnd)) {
        localAEBooleanExpr = extractFromAnd(paramAEBooleanExpr, (AEAnd)localIAENode);
      } else if ((localIAENode instanceof AERelationalExpr)) {
        localAEBooleanExpr = extractFromRelationalExpr(paramAEBooleanExpr, (AERelationalExpr)localIAENode);
      } else {
        throw new IllegalStateException("Logic error: Filter push down cannot be completed.");
      }
    }
    else {
      localAEBooleanExpr = paramAEBooleanExpr;
    }
    return localAEBooleanExpr;
  }
  
  private static AEBooleanExpr extractFromAnd(AEBooleanExpr paramAEBooleanExpr, AEAnd paramAEAnd)
  {
    if ((null == paramAEAnd) || (null == paramAEBooleanExpr)) {
      throw new NullPointerException("Null parameters are not allowed.");
    }
    AEBooleanExpr localAEBooleanExpr = null;
    if (paramAEAnd.getLeftOperand() == paramAEBooleanExpr)
    {
      localAEBooleanExpr = paramAEAnd.setLeftOperand(null);
      extractAnd(paramAEAnd);
    }
    else
    {
      localAEBooleanExpr = paramAEAnd.setRightOperand(null);
      extractAnd(paramAEAnd);
    }
    return localAEBooleanExpr;
  }
  
  private static void extractAnd(AEAnd paramAEAnd)
  {
    AEBooleanExpr localAEBooleanExpr = null;
    if (null == paramAEAnd.getLeftOperand()) {
      localAEBooleanExpr = paramAEAnd.setRightOperand(null);
    } else {
      localAEBooleanExpr = paramAEAnd.setLeftOperand(null);
    }
    IAENode localIAENode = paramAEAnd.getParent();
    Object localObject;
    if ((localIAENode instanceof AESelect))
    {
      localObject = (AESelect)localIAENode;
      ((AESelect)localObject).setSelectCond(localAEBooleanExpr);
    }
    else if ((localIAENode instanceof AEJoin))
    {
      localObject = (AEJoin)localIAENode;
      ((AEJoin)localObject).setJoinCondition(localAEBooleanExpr);
    }
    else if ((localIAENode instanceof AEAnd))
    {
      localObject = (AEAnd)localIAENode;
      if (((AEAnd)localObject).getLeftOperand() == paramAEAnd) {
        ((AEAnd)localObject).setLeftOperand(localAEBooleanExpr);
      } else {
        ((AEAnd)localObject).setRightOperand(localAEBooleanExpr);
      }
    }
    else
    {
      throw new IllegalStateException("Logic error: Filter push down cannot be completed: " + paramAEAnd + " with parent " + localIAENode);
    }
  }
  
  private static AEBooleanExpr extractFromRelationalExpr(AEBooleanExpr paramAEBooleanExpr, AERelationalExpr paramAERelationalExpr)
    throws ErrorException
  {
    AEBooleanExpr localAEBooleanExpr = null;
    Object localObject;
    if (PushDownType.SELECT == PushDownType.getNodeType(paramAERelationalExpr))
    {
      localObject = (AESelect)paramAERelationalExpr;
      localAEBooleanExpr = ((AESelect)localObject).setSelectCond(new AEBooleanTrue());
    }
    else if (PushDownType.JOIN == PushDownType.getNodeType(paramAERelationalExpr))
    {
      localObject = (AEJoin)paramAERelationalExpr;
      localAEBooleanExpr = ((AEJoin)localObject).getJoinCondition();
      ((AEJoin)localObject).setJoinCondition(new AEBooleanTrue());
    }
    else
    {
      throw new IllegalStateException("Logic error: Filter push down cannot be completed.");
    }
    return localAEBooleanExpr;
  }
  
  private static boolean subtreeContainsReference(AEBooleanExpr paramAEBooleanExpr1, AEBooleanExpr paramAEBooleanExpr2)
    throws ErrorException
  {
    AEDefaultVisitor local1 = new AEDefaultVisitor()
    {
      protected Boolean defaultVisit(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        Iterator localIterator = paramAnonymousIAENode.getChildItr();
        if (paramAnonymousIAENode == this.val$filter) {
          return Boolean.valueOf(true);
        }
        while (localIterator.hasNext()) {
          if (((Boolean)((IAENode)localIterator.next()).acceptVisitor(this)).booleanValue()) {
            return Boolean.valueOf(true);
          }
        }
        return Boolean.valueOf(false);
      }
    };
    return ((Boolean)paramAEBooleanExpr1.acceptVisitor(local1)).booleanValue();
  }
  
  private static enum PushDownType
  {
    JOIN,  CROSSJOIN,  SELECT,  PROJECT,  TABLE,  TABLE_SUBQUERY;
    
    private PushDownType() {}
    
    public static PushDownType getNodeType(IAENode paramIAENode)
      throws ErrorException
    {
      if ((paramIAENode instanceof AEJoin)) {
        return JOIN;
      }
      if ((paramIAENode instanceof AECrossJoin)) {
        return CROSSJOIN;
      }
      if ((paramIAENode instanceof AESelect)) {
        return SELECT;
      }
      if ((paramIAENode instanceof AEProject)) {
        return PROJECT;
      }
      if ((paramIAENode instanceof AETable)) {
        return TABLE;
      }
      if ((paramIAENode instanceof AESubQuery)) {
        return TABLE_SUBQUERY;
      }
      throw SQLEngineExceptionFactory.featureNotImplementedException("Attempt to push down filter on unknown node type: " + paramIAENode.getClass().getName());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aemanipulator/AETreeManipulator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */