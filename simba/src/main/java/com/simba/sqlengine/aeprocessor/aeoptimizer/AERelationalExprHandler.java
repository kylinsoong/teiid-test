package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker.Action;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEAggregate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEBinaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESort;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETableConstructor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETop;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnion;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.sqlengine.dsiext.dataengine.DSIExtOperationHandlerFactory;
import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

public class AERelationalExprHandler
  extends AEDefaultVisitor<AERelationalExpr>
{
  DSIExtOperationHandlerFactory m_opHandlerFactory;
  
  public AERelationalExpr visit(AEProject paramAEProject)
  {
    passdownOperand(paramAEProject);
    return paramAEProject;
  }
  
  public AERelationalExpr visit(AETable paramAETable)
  {
    return paramAETable;
  }
  
  public AERelationalExpr visit(AESelect paramAESelect)
  {
    if (!passdownOperand(paramAESelect)) {
      return paramAESelect;
    }
    assert ((paramAESelect.getOperand() instanceof AETable));
    AETable localAETable = (AETable)paramAESelect.getOperand();
    IBooleanExprHandler localIBooleanExprHandler = this.m_opHandlerFactory.createFilterHandler(localAETable.getTable());
    if (null == localIBooleanExprHandler) {
      return null;
    }
    Pair localPair = new AEPassdownFilter(localIBooleanExprHandler).passdown(paramAESelect.getCondition());
    if (localPair.key() != null) {
      localAETable.setTable((DSIExtJResultSet)localPair.key());
    }
    if (localPair.value() == null) {
      return localAETable;
    }
    paramAESelect.setSelectCond((AEBooleanExpr)localPair.value());
    return paramAESelect;
  }
  
  public AERelationalExpr visit(AETop paramAETop)
  {
    passdownOperand(paramAETop);
    return paramAETop;
  }
  
  public AERelationalExpr visit(AESort paramAESort)
  {
    passdownOperand(paramAESort);
    return paramAESort;
  }
  
  public AERelationalExpr visit(AECrossJoin paramAECrossJoin)
  {
    passdownOperand(paramAECrossJoin);
    return paramAECrossJoin;
  }
  
  public AERelationalExpr visit(AEJoin paramAEJoin)
  {
    if (!passdownOperand(paramAEJoin)) {
      return paramAEJoin;
    }
    return new AEPassdownJoin(this.m_opHandlerFactory).passdown(paramAEJoin);
  }
  
  public AERelationalExpr visit(AEAggregate paramAEAggregate)
  {
    passdownOperand(paramAEAggregate);
    return paramAEAggregate;
  }
  
  public AERelationalExpr visit(AESubQuery paramAESubQuery)
  {
    if ((paramAESubQuery.isInFromClause()) && (!paramAESubQuery.isCorrelated()))
    {
      AERelationalExpr localAERelationalExpr1 = paramAESubQuery.getOperand();
      AERelationalExpr localAERelationalExpr2 = passdown(localAERelationalExpr1);
      if (null != localAERelationalExpr2)
      {
        if ((localAERelationalExpr2 instanceof AETable))
        {
          AETable localAETable = (AETable)localAERelationalExpr2;
          localAETable.setCorrelationName(paramAESubQuery.getCorrelationName());
          updateColumns(paramAESubQuery, (AETable)localAERelationalExpr2);
          return localAERelationalExpr2;
        }
        paramAESubQuery.setOperand(localAERelationalExpr2);
      }
    }
    return paramAESubQuery;
  }
  
  public AERelationalExpr visit(AETableConstructor paramAETableConstructor)
  {
    return paramAETableConstructor;
  }
  
  public AERelationalExpr visit(AEUnion paramAEUnion)
  {
    passdownOperand(paramAEUnion);
    return paramAEUnion;
  }
  
  public AERelationalExprHandler(DSIExtOperationHandlerFactory paramDSIExtOperationHandlerFactory)
  {
    this.m_opHandlerFactory = paramDSIExtOperationHandlerFactory;
  }
  
  public AERelationalExpr passdown(AERelationalExpr paramAERelationalExpr)
  {
    try
    {
      return (AERelationalExpr)paramAERelationalExpr.acceptVisitor(this);
    }
    catch (ErrorException localErrorException)
    {
      throw SQLEngineExceptionFactory.runtimeException(localErrorException);
    }
  }
  
  protected AERelationalExpr defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidAETreeException();
  }
  
  private boolean passdownOperand(AEUnaryRelationalExpr paramAEUnaryRelationalExpr)
  {
    AERelationalExpr localAERelationalExpr1 = paramAEUnaryRelationalExpr.getOperand();
    if ((localAERelationalExpr1 instanceof AETable)) {
      return true;
    }
    AERelationalExpr localAERelationalExpr2 = passdown(localAERelationalExpr1);
    if (null != localAERelationalExpr2)
    {
      paramAEUnaryRelationalExpr.getOperand().setParent(null);
      paramAEUnaryRelationalExpr.setOperand(localAERelationalExpr2);
    }
    return localAERelationalExpr2 instanceof AETable;
  }
  
  private boolean passdownOperand(AEBinaryRelationalExpr paramAEBinaryRelationalExpr)
  {
    AERelationalExpr localAERelationalExpr1 = paramAEBinaryRelationalExpr.getLeftOperand();
    AERelationalExpr localAERelationalExpr2 = paramAEBinaryRelationalExpr.getRightOperand();
    AERelationalExpr localAERelationalExpr3 = null;
    if (!(localAERelationalExpr1 instanceof AETable))
    {
      localAERelationalExpr3 = passdown(localAERelationalExpr1);
      if (null != localAERelationalExpr3) {
        paramAEBinaryRelationalExpr.setLeftOperand(localAERelationalExpr3);
      }
    }
    if (!(localAERelationalExpr2 instanceof AETable))
    {
      localAERelationalExpr3 = passdown(localAERelationalExpr2);
      if (null != localAERelationalExpr3) {
        paramAEBinaryRelationalExpr.setRightOperand(localAERelationalExpr3);
      }
    }
    return ((paramAEBinaryRelationalExpr.getLeftOperand() instanceof AETable)) && ((paramAEBinaryRelationalExpr.getRightOperand() instanceof AETable));
  }
  
  private void updateColumns(final AENamedRelationalExpr paramAENamedRelationalExpr1, final AENamedRelationalExpr paramAENamedRelationalExpr2)
  {
    for (IAENode localIAENode = paramAENamedRelationalExpr1.getParent(); (localIAENode.getParent() != null) && (!(localIAENode instanceof AESubQuery)); localIAENode = localIAENode.getParent()) {}
    try
    {
      AETreeWalker.walk(localIAENode, new AETreeWalker.Action()
      {
        public void act(IAENode paramAnonymousIAENode)
          throws ErrorException
        {
          if (paramAnonymousIAENode == paramAENamedRelationalExpr1) {
            skipChildren();
          }
          if ((paramAnonymousIAENode instanceof AEColumnReference))
          {
            AEColumnReference localAEColumnReference = (AEColumnReference)paramAnonymousIAENode;
            if (localAEColumnReference.getNamedRelationalExpr() == paramAENamedRelationalExpr1) {
              localAEColumnReference.setNamedRelationalExpr(paramAENamedRelationalExpr2);
            }
          }
        }
      });
    }
    catch (ErrorException localErrorException)
    {
      throw SQLEngineExceptionFactory.runtimeException(localErrorException);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/AERelationalExprHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */