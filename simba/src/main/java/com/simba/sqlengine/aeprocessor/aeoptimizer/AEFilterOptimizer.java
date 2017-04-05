package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.aemanipulator.AEDeMorgansProcessor;
import com.simba.sqlengine.aeprocessor.aemanipulator.AETreeManipulator;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker.Action;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEAggregate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEBinaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsert;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEQuery;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.dsiext.dataengine.PassdownInformation;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.support.exceptions.ErrorException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AEFilterOptimizer
  implements IAEOptimizer
{
  private SqlDataEngineContext m_context;
  private PassdownInformation m_passdownInfo;
  private IAENode m_currentRoot;
  
  public AEFilterOptimizer(SqlDataEngineContext paramSqlDataEngineContext, PassdownInformation paramPassdownInformation)
  {
    this.m_context = paramSqlDataEngineContext;
    this.m_passdownInfo = paramPassdownInformation;
  }
  
  public void optimize(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    if (null == paramIAEStatement) {
      throw new NullPointerException("statement cannot be null.");
    }
    this.m_currentRoot = paramIAEStatement;
    AEDeMorgansProcessor.apply(this.m_context, paramIAEStatement);
    clearOptimizationFlags(paramIAEStatement);
    convertOuterJoin(paramIAEStatement);
    doOptimize(paramIAEStatement);
    postProcess(paramIAEStatement);
  }
  
  private void doOptimize(IAENode paramIAENode)
    throws ErrorException
  {
    assert (null != paramIAENode);
    final AEDefaultVisitor local1 = new AEDefaultVisitor()
    {
      public Boolean visit(AEProxyColumn paramAnonymousAEProxyColumn)
      {
        return Boolean.valueOf(true);
      }
      
      protected Boolean defaultVisit(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        Iterator localIterator = paramAnonymousIAENode.getChildItr();
        while (localIterator.hasNext())
        {
          IAENode localIAENode = (IAENode)localIterator.next();
          if (((Boolean)localIAENode.acceptVisitor(this)).booleanValue()) {
            return Boolean.valueOf(true);
          }
        }
        return Boolean.valueOf(false);
      }
    };
    for (;;)
    {
      if (!((Boolean)AETreeWalker.walk(paramIAENode, new AETreeWalker.Action()
      {
        private boolean m_optimized = false;
        
        public void act(IAENode paramAnonymousIAENode)
          throws ErrorException
        {
          if ((paramAnonymousIAENode instanceof AESubQuery))
          {
            skipChildren();
            localObject = this.this$0.m_currentRoot;
            this.this$0.m_currentRoot = paramAnonymousIAENode;
            this.this$0.doOptimize(((AESubQuery)paramAnonymousIAENode).getOperand());
            this.this$0.m_currentRoot = ((IAENode)localObject);
            return;
          }
          if ((paramAnonymousIAENode instanceof AEValueSubQuery))
          {
            skipChildren();
            localObject = this.this$0.m_currentRoot;
            this.this$0.m_currentRoot = paramAnonymousIAENode;
            this.this$0.doOptimize(((AEValueSubQuery)paramAnonymousIAENode).getQueryExpression());
            this.this$0.m_currentRoot = ((IAENode)localObject);
            return;
          }
          if (!(paramAnonymousIAENode instanceof AEBooleanExpr)) {
            return;
          }
          Object localObject = (AEBooleanExpr)paramAnonymousIAENode;
          if ((((AEBooleanExpr)localObject).isOptimized()) || ((localObject instanceof AEBooleanTrue)))
          {
            skipChildren();
            return;
          }
          if (!((Boolean)((AEBooleanExpr)localObject).acceptVisitor(local1)).booleanValue())
          {
            if (this.this$0.pushDownFilter((AEBooleanExpr)localObject, false))
            {
              this.m_optimized = true;
              skipAll();
            }
          }
          else {
            this.this$0.setFilterOptimized((AEBooleanExpr)localObject);
          }
        }
        
        public Boolean getResult()
        {
          return Boolean.valueOf(this.m_optimized);
        }
      })).booleanValue()) {
        break;
      }
    }
  }
  
  private boolean doPushDownFilter(AEBooleanExpr paramAEBooleanExpr, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramAEBooleanExpr);
    assert (null != this.m_currentRoot);
    Set localSet = extractRelationalExprs(paramAEBooleanExpr);
    if (0 == localSet.size())
    {
      setFilterOptimized(paramAEBooleanExpr);
      return false;
    }
    IAENode localIAENode1 = paramAEBooleanExpr.getParent();
    if (!(localIAENode1 instanceof AERelationalExpr)) {
      while ((!(localIAENode1 instanceof AERelationalExpr)) && (localIAENode1 != this.m_currentRoot)) {
        localIAENode1 = localIAENode1.getParent();
      }
    }
    AETreeWalker localAETreeWalker = new AETreeWalker(localIAENode1);
    Object localObject = null;
    while (localAETreeWalker.hasNext())
    {
      IAENode localIAENode2 = localAETreeWalker.next();
      if (((localIAENode2 instanceof AEJoin)) || ((localIAENode2 instanceof AECrossJoin)))
      {
        AEBinaryRelationalExpr localAEBinaryRelationalExpr = (AEBinaryRelationalExpr)localIAENode2;
        PushdownInfo localPushdownInfo = new PushdownInfo(localAEBinaryRelationalExpr, new HashSet(localSet), paramBoolean);
        boolean bool2 = processJoin(localPushdownInfo);
        if (!bool2) {
          break;
        }
        if (localPushdownInfo.m_isInLeftSubTree)
        {
          localObject = localPushdownInfo.m_joinNode.getLeftOperand();
          localPushdownInfo.m_inOuterJoinCond = false;
          localAETreeWalker = new AETreeWalker(localAEBinaryRelationalExpr.getLeftOperand());
        }
        else if (!localPushdownInfo.m_foundRelExprInLeft)
        {
          localObject = localPushdownInfo.m_joinNode.getRightOperand();
          localPushdownInfo.m_inOuterJoinCond = false;
          localAETreeWalker = new AETreeWalker(localAEBinaryRelationalExpr.getRightOperand());
        }
        else if (((localPushdownInfo.m_joinNode instanceof AECrossJoin)) || (!((AEJoin)localPushdownInfo.m_joinNode).isOuterJoin()))
        {
          localObject = localPushdownInfo.m_joinNode;
        }
      }
    }
    boolean bool1 = false;
    if (null != localObject)
    {
      AETreeManipulator.pushDownFilter(this.m_passdownInfo, paramAEBooleanExpr, (AERelationalExpr)localObject);
      bool1 = true;
    }
    else
    {
      paramAEBooleanExpr.setIsOptimized(true);
    }
    return bool1;
  }
  
  private boolean pushDownFilter(AEBooleanExpr paramAEBooleanExpr, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramAEBooleanExpr);
    if (paramAEBooleanExpr.isOptimized()) {
      return false;
    }
    if (childOfSearchedWhenClause(paramAEBooleanExpr))
    {
      setFilterOptimized(paramAEBooleanExpr);
      return false;
    }
    IAENode localIAENode1 = paramAEBooleanExpr.getParent();
    boolean bool = paramBoolean;
    Object localObject;
    if ((!bool) && ((localIAENode1 instanceof AEJoin)))
    {
      localObject = (AEJoin)localIAENode1;
      if (((AEJoin)localObject).isOuterJoin()) {
        bool = true;
      }
    }
    switch (paramAEBooleanExpr.getType())
    {
    case COMPARISON: 
    case LIKE_PRED: 
    case NULL_PRED: 
    case NOT: 
      return doPushDownFilter(paramAEBooleanExpr, bool);
    case OR: 
      return pushDownOr((AEOr)paramAEBooleanExpr, bool);
    case AND: 
      return pushDownAnd((AEAnd)paramAEBooleanExpr, bool);
    case IN_PRED: 
      localObject = (AEInPredicate)paramAEBooleanExpr;
      IAENode localIAENode2 = (IAENode)((AEInPredicate)localObject).getRightOperand().getChildItr().next();
      if (((localIAENode2 instanceof AESubQuery)) || ((localIAENode2 instanceof AEValueSubQuery)))
      {
        setFilterOptimized((AEBooleanExpr)localObject);
        doOptimize(localIAENode2);
        return true;
      }
      return doPushDownFilter((AEBooleanExpr)localObject, paramBoolean);
    case EXISTS_PRED: 
      localObject = (AEExistsPredicate)paramAEBooleanExpr;
      setFilterOptimized((AEBooleanExpr)localObject);
      doOptimize(((AEExistsPredicate)localObject).getOperand());
      return true;
    case QUANITIFIED_COMPARISON: 
      localObject = (AEQuantifiedComparison)paramAEBooleanExpr;
      setFilterOptimized((AEBooleanExpr)localObject);
      doOptimize(((AEQuantifiedComparison)localObject).getRightOperand());
      return true;
    }
    paramAEBooleanExpr.setIsOptimized(true);
    return false;
  }
  
  private boolean pushDownAnd(AEAnd paramAEAnd, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramAEAnd);
    AEBooleanExpr localAEBooleanExpr1 = paramAEAnd.getLeftOperand();
    AEBooleanExpr localAEBooleanExpr2 = paramAEAnd.getRightOperand();
    if ((localAEBooleanExpr1.isOptimized()) && (localAEBooleanExpr2.isOptimized()))
    {
      paramAEAnd.setIsOptimized(true);
      return false;
    }
    boolean bool = false;
    if (!localAEBooleanExpr1.isOptimized()) {
      bool = pushDownFilter(localAEBooleanExpr1, paramBoolean);
    }
    if ((null != localAEBooleanExpr2) && (!localAEBooleanExpr2.isOptimized())) {
      bool = (bool) || (pushDownFilter(localAEBooleanExpr2, paramBoolean));
    }
    return bool;
  }
  
  private boolean pushDownOr(AEOr paramAEOr, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramAEOr);
    boolean bool = doPushDownFilter(paramAEOr, paramBoolean);
    setFilterOptimized(paramAEOr);
    return bool;
  }
  
  private boolean processJoin(final PushdownInfo paramPushdownInfo)
    throws ErrorException
  {
    AEBinaryRelationalExpr localAEBinaryRelationalExpr = paramPushdownInfo.m_joinNode;
    assert (((localAEBinaryRelationalExpr instanceof AEJoin)) || ((localAEBinaryRelationalExpr instanceof AECrossJoin)));
    AEJoin.AEJoinType localAEJoinType = null;
    int i = 0;
    if ((localAEBinaryRelationalExpr instanceof AEJoin)) {
      localAEJoinType = ((AEJoin)localAEBinaryRelationalExpr).getJoinType();
    } else {
      i = 1;
    }
    if (AEJoin.AEJoinType.FULL_OUTER_JOIN == localAEJoinType) {
      return false;
    }
    paramPushdownInfo.m_isInLeftSubTree = true;
    if ((i != 0) || (AEJoin.AEJoinType.INNER_JOIN == localAEJoinType) || ((!paramPushdownInfo.m_inOuterJoinCond) && (AEJoin.AEJoinType.LEFT_OUTER_JOIN == localAEJoinType)) || ((paramPushdownInfo.m_inOuterJoinCond) && (AEJoin.AEJoinType.RIGHT_OUTER_JOIN == localAEJoinType))) {
      AETreeWalker.walk(localAEBinaryRelationalExpr.getLeftOperand(), new AETreeWalker.Action()
      {
        public void act(IAENode paramAnonymousIAENode)
          throws ErrorException
        {
          if ((paramAnonymousIAENode instanceof AENamedRelationalExpr))
          {
            String str = ((AENamedRelationalExpr)paramAnonymousIAENode).getQTableName().toString();
            if (paramPushdownInfo.m_foundRelExprs.remove(str)) {
              paramPushdownInfo.m_foundRelExprInLeft = true;
            }
            if ((paramAnonymousIAENode instanceof AESubQuery)) {
              skipChildren();
            }
          }
        }
      });
    }
    if (0 == paramPushdownInfo.m_foundRelExprs.size()) {
      return true;
    }
    paramPushdownInfo.m_isInLeftSubTree = false;
    if ((i != 0) || (AEJoin.AEJoinType.INNER_JOIN == localAEJoinType) || ((!paramPushdownInfo.m_inOuterJoinCond) && (AEJoin.AEJoinType.RIGHT_OUTER_JOIN == localAEJoinType)) || ((paramPushdownInfo.m_inOuterJoinCond) && (AEJoin.AEJoinType.LEFT_OUTER_JOIN == localAEJoinType))) {
      AETreeWalker.walk(localAEBinaryRelationalExpr.getRightOperand(), new AETreeWalker.Action()
      {
        public void act(IAENode paramAnonymousIAENode)
          throws ErrorException
        {
          if ((paramAnonymousIAENode instanceof AENamedRelationalExpr))
          {
            String str = ((AENamedRelationalExpr)paramAnonymousIAENode).getQTableName().toString();
            paramPushdownInfo.m_foundRelExprs.remove(str);
          }
          if ((paramAnonymousIAENode instanceof AESubQuery)) {
            skipChildren();
          }
        }
      });
    }
    return 0 == paramPushdownInfo.m_foundRelExprs.size();
  }
  
  private void clearOptimizationFlags(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    AETreeWalker.walk(paramIAEStatement, new AETreeWalker.Action()
    {
      public void act(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        if ((paramAnonymousIAENode instanceof AEBooleanExpr)) {
          ((AEBooleanExpr)paramAnonymousIAENode).setIsOptimized(false);
        }
      }
    });
  }
  
  private boolean convertOuterJoin(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    if ((paramIAEStatement instanceof AEQuery)) {
      return convertOuterJoin(((AEQuery)paramIAEStatement).getOperand(), new HashSet());
    }
    if ((paramIAEStatement instanceof AEInsert)) {
      return convertOuterJoin(((AEInsert)paramIAEStatement).getRelationalExpr(), new HashSet());
    }
    return false;
  }
  
  private boolean convertOuterJoin(AERelationalExpr paramAERelationalExpr, Set<AERelationalExpr> paramSet)
    throws ErrorException
  {
    if ((paramAERelationalExpr instanceof AESelect)) {
      return convertOuterJoinForSelectNode((AESelect)paramAERelationalExpr, paramSet);
    }
    if ((paramAERelationalExpr instanceof AEJoin)) {
      return convertOuterJoinForJoinNode((AEJoin)paramAERelationalExpr, paramSet);
    }
    if ((paramAERelationalExpr instanceof AECrossJoin))
    {
      AECrossJoin localAECrossJoin = (AECrossJoin)paramAERelationalExpr;
      return convertOuterJoin(localAECrossJoin.getLeftOperand(), new HashSet(paramSet)) | convertOuterJoin(localAECrossJoin.getRightOperand(), paramSet);
    }
    if ((paramAERelationalExpr instanceof AEAggregate)) {
      return false;
    }
    if ((paramAERelationalExpr instanceof AEUnaryRelationalExpr)) {
      return convertOuterJoin(((AEUnaryRelationalExpr)paramAERelationalExpr).getOperand(), paramSet);
    }
    return false;
  }
  
  private boolean convertOuterJoinForSelectNode(AESelect paramAESelect, Set<AERelationalExpr> paramSet)
    throws ErrorException
  {
    AEBooleanExpr localAEBooleanExpr = paramAESelect.getCondition();
    paramSet.addAll(extractNullRejected(localAEBooleanExpr));
    return convertOuterJoin(paramAESelect.getOperand(), paramSet);
  }
  
  private boolean convertOuterJoinForJoinNode(AEJoin paramAEJoin, Set<AERelationalExpr> paramSet)
    throws ErrorException
  {
    Object localObject;
    switch (paramAEJoin.getJoinType())
    {
    case INNER_JOIN: 
      paramSet.addAll(extractNullRejected(paramAEJoin.getJoinCondition()));
      return convertOuterJoin(paramAEJoin.getLeftOperand(), new HashSet(paramSet)) | convertOuterJoin(paramAEJoin.getRightOperand(), paramSet);
    case LEFT_OUTER_JOIN: 
      if (paramSet.contains(paramAEJoin.getRightOperand()))
      {
        localObject = convertJoinType(AEJoin.AEJoinType.INNER_JOIN, paramAEJoin);
        if (null != localObject)
        {
          convertOuterJoinForJoinNode((AEJoin)localObject, paramSet);
          return true;
        }
      }
      localObject = new HashSet(paramSet);
      ((Set)localObject).addAll(extractNullRejected(paramAEJoin.getJoinCondition()));
      return convertOuterJoin(paramAEJoin.getLeftOperand(), paramSet) | convertOuterJoin(paramAEJoin.getRightOperand(), (Set)localObject);
    case RIGHT_OUTER_JOIN: 
      if (paramSet.contains(paramAEJoin.getLeftOperand()))
      {
        localObject = convertJoinType(AEJoin.AEJoinType.INNER_JOIN, paramAEJoin);
        if (null != localObject)
        {
          convertOuterJoinForJoinNode((AEJoin)localObject, paramSet);
          return true;
        }
      }
      localObject = new HashSet(paramSet);
      ((Set)localObject).addAll(extractNullRejected(paramAEJoin.getJoinCondition()));
      return convertOuterJoin(paramAEJoin.getLeftOperand(), (Set)localObject) | convertOuterJoin(paramAEJoin.getRightOperand(), paramSet);
    case FULL_OUTER_JOIN: 
      localObject = null;
      if (paramSet.contains(paramAEJoin.getLeftOperand()))
      {
        if (paramSet.contains(paramAEJoin.getRightOperand())) {
          localObject = convertJoinType(AEJoin.AEJoinType.INNER_JOIN, paramAEJoin);
        } else {
          localObject = convertJoinType(AEJoin.AEJoinType.LEFT_OUTER_JOIN, paramAEJoin);
        }
      }
      else if (paramSet.contains(paramAEJoin.getRightOperand())) {
        localObject = convertJoinType(AEJoin.AEJoinType.RIGHT_OUTER_JOIN, paramAEJoin);
      }
      if (null != localObject)
      {
        convertOuterJoinForJoinNode((AEJoin)localObject, paramSet);
        return true;
      }
      return convertOuterJoin(paramAEJoin.getLeftOperand(), new HashSet(paramSet)) | convertOuterJoin(paramAEJoin.getRightOperand(), paramSet);
    }
    return false;
  }
  
  private AEJoin convertJoinType(AEJoin.AEJoinType paramAEJoinType, AEJoin paramAEJoin)
  {
    switch (paramAEJoin.getJoinType())
    {
    case LEFT_OUTER_JOIN: 
    case RIGHT_OUTER_JOIN: 
      if (AEJoin.AEJoinType.INNER_JOIN != paramAEJoinType) {
        throw new IllegalArgumentException();
      }
    case FULL_OUTER_JOIN: 
      IAENode localIAENode = paramAEJoin.getParent();
      Object localObject;
      AEJoin localAEJoin;
      if ((localIAENode instanceof AEUnaryRelationalExpr))
      {
        localObject = (AEUnaryRelationalExpr)localIAENode;
        localAEJoin = new AEJoin(paramAEJoinType, paramAEJoin.getLeftOperand(), paramAEJoin.getRightOperand(), paramAEJoin.getJoinCondition());
        ((AEUnaryRelationalExpr)localObject).setOperand(localAEJoin);
        localAEJoin.setParent((IAENode)localObject);
        return localAEJoin;
      }
      if ((localIAENode instanceof AEBinaryRelationalExpr))
      {
        localObject = (AEBinaryRelationalExpr)localIAENode;
        localAEJoin = new AEJoin(paramAEJoinType, paramAEJoin.getLeftOperand(), paramAEJoin.getRightOperand(), paramAEJoin.getJoinCondition());
        if (((AEBinaryRelationalExpr)localObject).getLeftOperand() == paramAEJoin)
        {
          ((AEBinaryRelationalExpr)localObject).setLeftOperand(localAEJoin);
        }
        else
        {
          assert (((AEBinaryRelationalExpr)localObject).getRightOperand() == paramAEJoin) : "JOIN node must be a LEFT or RIGHT child of its binary parent";
          ((AEBinaryRelationalExpr)localObject).setRightOperand(localAEJoin);
        }
        localAEJoin.setParent((IAENode)localObject);
        return localAEJoin;
      }
      return null;
    }
    throw new IllegalArgumentException();
  }
  
  private void postProcess(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    AETreeWalker localAETreeWalker = new AETreeWalker(paramIAEStatement);
    while (localAETreeWalker.hasNext())
    {
      IAENode localIAENode = localAETreeWalker.next();
      Object localObject;
      if ((localIAENode instanceof AEJoin))
      {
        localObject = (AEJoin)localIAENode;
        if ((!((AEJoin)localObject).isOuterJoin()) && ((((AEJoin)localObject).getJoinCondition() instanceof AEBooleanTrue)))
        {
          AETreeManipulator.convertJoinToCrossJoin((AEJoin)localObject);
          localAETreeWalker = new AETreeWalker(paramIAEStatement);
        }
      }
      else if ((localIAENode instanceof AESelect))
      {
        localObject = (AESelect)localIAENode;
        if ((((AESelect)localObject).getCondition() instanceof AEBooleanTrue))
        {
          AETreeManipulator.removeSelect((AESelect)localObject);
          localAETreeWalker = new AETreeWalker(paramIAEStatement);
        }
        else if ((((AESelect)localObject).getOperand() instanceof AECrossJoin))
        {
          AETreeManipulator.convertCrossJoinToInnerJoin((AECrossJoin)((AESelect)localObject).getOperand(), (AESelect)localObject);
          localAETreeWalker = new AETreeWalker(paramIAEStatement);
        }
      }
    }
  }
  
  private boolean childOfSearchedWhenClause(AEBooleanExpr paramAEBooleanExpr)
  {
    for (IAENode localIAENode = paramAEBooleanExpr.getParent(); (localIAENode instanceof AEBooleanExpr); localIAENode = localIAENode.getParent()) {}
    return localIAENode instanceof AESearchedWhenClause;
  }
  
  private Set<String> extractRelationalExprs(AEBooleanExpr paramAEBooleanExpr)
    throws ErrorException
  {
    assert (null != paramAEBooleanExpr);
    final HashSet localHashSet = new HashSet();
    AEDefaultVisitor local5 = new AEDefaultVisitor()
    {
      public Void visit(AEColumnReference paramAnonymousAEColumnReference)
      {
        localHashSet.add(paramAnonymousAEColumnReference.getNamedRelationalExpr().getQTableName().toString());
        return null;
      }
      
      protected Void defaultVisit(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        Iterator localIterator = paramAnonymousIAENode.getChildItr();
        while (localIterator.hasNext()) {
          ((IAENode)localIterator.next()).acceptVisitor(this);
        }
        return null;
      }
    };
    paramAEBooleanExpr.acceptVisitor(local5);
    return localHashSet;
  }
  
  private Set<AERelationalExpr> extractNullRejected(AEBooleanExpr paramAEBooleanExpr)
    throws ErrorException
  {
    return (Set)paramAEBooleanExpr.acceptVisitor(new NullRejectVisitor());
  }
  
  private void setFilterOptimized(AEBooleanExpr paramAEBooleanExpr)
    throws ErrorException
  {
    AETreeWalker.walk(paramAEBooleanExpr, new AETreeWalker.Action()
    {
      public void act(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        if ((paramAnonymousIAENode instanceof AEBooleanExpr)) {
          ((AEBooleanExpr)paramAnonymousIAENode).setIsOptimized(true);
        } else if (((paramAnonymousIAENode instanceof AESubQuery)) || ((paramAnonymousIAENode instanceof AEValueSubQuery))) {
          skipChildren();
        }
      }
    });
  }
  
  private class PushdownInfo
  {
    public AEBinaryRelationalExpr m_joinNode;
    public boolean m_foundRelExprInLeft;
    public boolean m_isInLeftSubTree;
    public boolean m_inOuterJoinCond;
    public Set<String> m_foundRelExprs;
    
    public PushdownInfo(Set<String> paramSet, boolean paramBoolean)
    {
      this.m_joinNode = paramSet;
      this.m_foundRelExprInLeft = false;
      this.m_isInLeftSubTree = true;
      this.m_foundRelExprs = paramBoolean;
      boolean bool;
      this.m_inOuterJoinCond = bool;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/AEFilterOptimizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */