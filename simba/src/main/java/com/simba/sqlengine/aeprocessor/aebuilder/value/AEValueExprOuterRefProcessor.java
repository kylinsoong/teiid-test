package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope.ClauseType;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprOuterRefProcessor;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AENodeList;
import com.simba.sqlengine.aeprocessor.aetree.AESemantics;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEBinaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEUnaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEValueExprOuterRefProcessor
{
  public static void process(AEValueExprList paramAEValueExprList, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    AEValueExprProcessor localAEValueExprProcessor = new AEValueExprProcessor(paramAEQueryScope);
    Iterator localIterator = paramAEValueExprList.getChildItr();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)localIterator.next();
      paramAEValueExprList.replaceNode((IAENode)localAEValueExpr.acceptVisitor(localAEValueExprProcessor), i);
    }
  }
  
  public static AEValueExpr process(AEValueExpr paramAEValueExpr, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    return (AEValueExpr)paramAEValueExpr.acceptVisitor(new AEValueExprProcessor(paramAEQueryScope));
  }
  
  private static class AEValueExprProcessor
    extends AEDefaultVisitor<AEValueExpr>
  {
    private final AEQueryScope m_scope;
    
    AEValueExprProcessor(AEQueryScope paramAEQueryScope)
    {
      this.m_scope = paramAEQueryScope;
    }
    
    public AEValueExpr visit(AEAdd paramAEAdd)
      throws ErrorException
    {
      return processBinary(paramAEAdd);
    }
    
    public AEValueExpr visit(AEColumnReference paramAEColumnReference)
      throws ErrorException
    {
      AEQueryScope localAEQueryScope = paramAEColumnReference.getResolvedQueryScope();
      if ((isOuterScope(localAEQueryScope)) && (mustBeGroupingExpr(localAEQueryScope)))
      {
        AEValueExpr localAEValueExpr = localAEQueryScope.proxyToAggregateExpr(paramAEColumnReference);
        if (null == localAEValueExpr) {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.NON_GROUPING_COLUMN_IN_SEL_LIST.name(), new String[] { paramAEColumnReference.getLogString() });
        }
        return localAEValueExpr;
      }
      return paramAEColumnReference;
    }
    
    public AEValueExpr visit(AECountStarAggrFn paramAECountStarAggrFn)
      throws ErrorException
    {
      return processAggregate(paramAECountStarAggrFn);
    }
    
    public AEValueExpr visit(AEConcat paramAEConcat)
      throws ErrorException
    {
      return processBinary(paramAEConcat);
    }
    
    public AEValueExpr visit(AEDivide paramAEDivide)
      throws ErrorException
    {
      return processBinary(paramAEDivide);
    }
    
    public AEValueExpr visit(AEGeneralAggrFn paramAEGeneralAggrFn)
      throws ErrorException
    {
      return processAggregate(paramAEGeneralAggrFn);
    }
    
    public AEValueExpr visit(AELiteral paramAELiteral)
      throws ErrorException
    {
      return paramAELiteral;
    }
    
    public AEValueExpr visit(AEMultiply paramAEMultiply)
      throws ErrorException
    {
      return processBinary(paramAEMultiply);
    }
    
    public AEValueExpr visit(AENegate paramAENegate)
      throws ErrorException
    {
      return processUnary(paramAENegate);
    }
    
    public AEValueExpr visit(AENull paramAENull)
      throws ErrorException
    {
      return paramAENull;
    }
    
    public AEValueExpr visit(AEParameter paramAEParameter)
      throws ErrorException
    {
      return paramAEParameter;
    }
    
    public AEValueExpr visit(AERename paramAERename)
      throws ErrorException
    {
      return processUnary(paramAERename);
    }
    
    public AEValueExpr visit(AEScalarFn paramAEScalarFn)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAEScalarFn);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      AEValueExprOuterRefProcessor.process(paramAEScalarFn.getArguments(), this.m_scope);
      return paramAEScalarFn;
    }
    
    public AEValueExpr visit(AESearchedCase paramAESearchedCase)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAESearchedCase);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      paramAESearchedCase.setElseClause((AEValueExpr)paramAESearchedCase.getElseClause().acceptVisitor(this));
      AENodeList localAENodeList = paramAESearchedCase.getWhenClauseList();
      Iterator localIterator = localAENodeList.getChildItr();
      while (localIterator.hasNext())
      {
        AESearchedWhenClause localAESearchedWhenClause = (AESearchedWhenClause)localIterator.next();
        localAESearchedWhenClause.setThenExpression((AEValueExpr)localAESearchedWhenClause.getThenExpression().acceptVisitor(this));
        AEBooleanExprOuterRefProcessor.process(localAESearchedWhenClause.getWhenCondition(), this.m_scope);
      }
      return paramAESearchedCase;
    }
    
    public AEValueExpr visit(AESimpleCase paramAESimpleCase)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAESimpleCase);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      paramAESimpleCase.setCaseOperand((AEValueExpr)paramAESimpleCase.getCaseOperand().acceptVisitor(this));
      paramAESimpleCase.setElseOperand((AEValueExpr)paramAESimpleCase.getElseOperand().acceptVisitor(this));
      Iterator localIterator = paramAESimpleCase.getWhenClauseList().getChildItr();
      while (localIterator.hasNext())
      {
        AESimpleWhenClause localAESimpleWhenClause = (AESimpleWhenClause)localIterator.next();
        localAESimpleWhenClause.setWhenExpression((AEValueExpr)localAESimpleWhenClause.getWhenExpression().acceptVisitor(this));
        localAESimpleWhenClause.setThenExpression((AEValueExpr)localAESimpleWhenClause.getThenExpression().acceptVisitor(this));
      }
      return paramAESimpleCase;
    }
    
    public AEValueExpr visit(AESubtract paramAESubtract)
      throws ErrorException
    {
      return processBinary(paramAESubtract);
    }
    
    public AEValueExpr visit(AEValueSubQuery paramAEValueSubQuery)
    {
      return paramAEValueSubQuery;
    }
    
    protected AEValueExpr defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.NON_GROUPING_COLUMN_IN_SEL_LIST.name(), new String[] { paramIAENode.getLogString() });
    }
    
    private AEValueExpr processAggregate(AEAggrFn paramAEAggrFn)
      throws ErrorException
    {
      AEQueryScope localAEQueryScope = AESemantics.findUniqueQueryScope(paramAEAggrFn, true);
      if (isOuterScope(localAEQueryScope))
      {
        assert ((AEQueryScope.ClauseType.HAVING == localAEQueryScope.getCurrentClause()) || (AEQueryScope.ClauseType.SELECT_LIST == localAEQueryScope.getCurrentClause()));
        return localAEQueryScope.addAggregateFunction(paramAEAggrFn);
      }
      return paramAEAggrFn;
    }
    
    private AEValueExpr processBinary(AEBinaryValueExpr paramAEBinaryValueExpr)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAEBinaryValueExpr);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      paramAEBinaryValueExpr.setLeftOperand((AEValueExpr)paramAEBinaryValueExpr.getLeftOperand().acceptVisitor(this));
      paramAEBinaryValueExpr.setRightOperand((AEValueExpr)paramAEBinaryValueExpr.getRightOperand().acceptVisitor(this));
      return paramAEBinaryValueExpr;
    }
    
    private AEValueExpr processUnary(AEUnaryValueExpr paramAEUnaryValueExpr)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAEUnaryValueExpr);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      paramAEUnaryValueExpr.setOperand((AEValueExpr)paramAEUnaryValueExpr.getOperand().acceptVisitor(this));
      return paramAEUnaryValueExpr;
    }
    
    private AEValueExpr createProxy(AEValueExpr paramAEValueExpr)
      throws ErrorException
    {
      AEQueryScope localAEQueryScope = AESemantics.findUniqueQueryScope(paramAEValueExpr, false);
      if ((isOuterScope(localAEQueryScope)) && (mustBeGroupingExpr(localAEQueryScope))) {
        return localAEQueryScope.proxyToAggregateExpr(paramAEValueExpr);
      }
      return null;
    }
    
    private boolean isOuterScope(AEQueryScope paramAEQueryScope)
    {
      return (null != paramAEQueryScope) && (this.m_scope != paramAEQueryScope);
    }
    
    private boolean mustBeGroupingExpr(AEQueryScope paramAEQueryScope)
    {
      AEQueryScope.ClauseType localClauseType = paramAEQueryScope.getCurrentClause();
      return (AEQueryScope.ClauseType.HAVING == localClauseType) || ((AEQueryScope.ClauseType.SELECT_LIST == localClauseType) && (paramAEQueryScope.hasAggregate()));
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEValueExprOuterRefProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */