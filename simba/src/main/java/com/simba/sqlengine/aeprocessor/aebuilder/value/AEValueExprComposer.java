package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprProcessor;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AENodeList;
import com.simba.sqlengine.aeprocessor.aetree.AESemantics;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEBinaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AECustomScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEValueExprComposer
{
  public static void compose(AEValueExprList paramAEValueExprList, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    Composer localComposer = new Composer(paramAEQueryScope);
    Iterator localIterator = paramAEValueExprList.getChildItr();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)((AEValueExpr)localIterator.next()).acceptVisitor(localComposer);
      paramAEValueExprList.replaceNode(localAEValueExpr, i);
    }
  }
  
  public static AEValueExpr composeExpr(AEValueExpr paramAEValueExpr, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    return (AEValueExpr)paramAEValueExpr.acceptVisitor(new Composer(paramAEQueryScope));
  }
  
  private static class Composer
    extends AEDefaultVisitor<AEValueExpr>
  {
    private AEQueryScope m_queryScope;
    
    public Composer(AEQueryScope paramAEQueryScope)
    {
      this.m_queryScope = paramAEQueryScope;
    }
    
    private AEValueExpr createProxy(AEValueExpr paramAEValueExpr)
      throws ErrorException
    {
      AEQueryScope localAEQueryScope = AESemantics.findUniqueQueryScope(paramAEValueExpr, true);
      if ((null != localAEQueryScope) && (this.m_queryScope == localAEQueryScope)) {
        return localAEQueryScope.proxyToAggregateExpr(paramAEValueExpr);
      }
      return null;
    }
    
    private AEValueExpr composeBinary(AEBinaryValueExpr paramAEBinaryValueExpr)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr1 = createProxy(paramAEBinaryValueExpr);
      if (null != localAEValueExpr1) {
        return localAEValueExpr1;
      }
      AEValueExpr localAEValueExpr2 = (AEValueExpr)paramAEBinaryValueExpr.getLeftOperand().acceptVisitor(this);
      paramAEBinaryValueExpr.setLeftOperand(localAEValueExpr2);
      AEValueExpr localAEValueExpr3 = (AEValueExpr)paramAEBinaryValueExpr.getRightOperand().acceptVisitor(this);
      paramAEBinaryValueExpr.setRightOperand(localAEValueExpr3);
      return paramAEBinaryValueExpr;
    }
    
    public AEValueExpr visit(AEAdd paramAEAdd)
      throws ErrorException
    {
      return composeBinary(paramAEAdd);
    }
    
    public AEValueExpr visit(AEConcat paramAEConcat)
      throws ErrorException
    {
      return composeBinary(paramAEConcat);
    }
    
    public AEValueExpr visit(AEDivide paramAEDivide)
      throws ErrorException
    {
      return composeBinary(paramAEDivide);
    }
    
    public AEValueExpr visit(AEMultiply paramAEMultiply)
      throws ErrorException
    {
      return composeBinary(paramAEMultiply);
    }
    
    public AEValueExpr visit(AESubtract paramAESubtract)
      throws ErrorException
    {
      return composeBinary(paramAESubtract);
    }
    
    public AEValueExpr visit(AEColumnReference paramAEColumnReference)
      throws ErrorException
    {
      if (paramAEColumnReference.isOuterReference()) {
        return paramAEColumnReference;
      }
      AEValueExpr localAEValueExpr = createProxy(paramAEColumnReference);
      if (null == localAEValueExpr) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.NON_GROUPING_COLUMN_IN_SEL_LIST.name(), new String[] { paramAEColumnReference.getLogString() });
      }
      return localAEValueExpr;
    }
    
    public AEValueExpr visit(AEProxyColumn paramAEProxyColumn)
      throws ErrorException
    {
      return paramAEProxyColumn;
    }
    
    public AEValueExpr visit(AELiteral paramAELiteral)
      throws ErrorException
    {
      return paramAELiteral;
    }
    
    public AEValueExpr visit(AEParameter paramAEParameter)
      throws ErrorException
    {
      return paramAEParameter;
    }
    
    public AEValueExpr visit(AENull paramAENull)
      throws ErrorException
    {
      return paramAENull;
    }
    
    public AEValueExpr visit(AERename paramAERename)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)paramAERename.getOperand().acceptVisitor(this);
      paramAERename.setOperand(localAEValueExpr);
      return paramAERename;
    }
    
    public AEValueExpr visit(AEScalarFn paramAEScalarFn)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAEScalarFn);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      AEValueExprList localAEValueExprList = paramAEScalarFn.getArguments();
      if ((null != localAEValueExprList) && (localAEValueExprList.getNumChildren() > 0)) {
        AEValueExprComposer.compose(localAEValueExprList, this.m_queryScope);
      }
      return paramAEScalarFn;
    }
    
    public AEValueExpr visit(AECustomScalarFn paramAECustomScalarFn)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr = createProxy(paramAECustomScalarFn);
      if (null != localAEValueExpr) {
        return localAEValueExpr;
      }
      AEValueExprList localAEValueExprList = paramAECustomScalarFn.getArguments();
      if ((null != localAEValueExprList) && (0 < localAEValueExprList.getNumChildren())) {
        AEValueExprComposer.compose(localAEValueExprList, this.m_queryScope);
      }
      return paramAECustomScalarFn;
    }
    
    public AEValueExpr visit(AECountStarAggrFn paramAECountStarAggrFn)
      throws ErrorException
    {
      return this.m_queryScope.addAggregateFunction(paramAECountStarAggrFn);
    }
    
    public AEValueExpr visit(AEGeneralAggrFn paramAEGeneralAggrFn)
      throws ErrorException
    {
      AEQueryScope localAEQueryScope = AESemantics.findUniqueQueryScope(paramAEGeneralAggrFn, true);
      if (null != localAEQueryScope)
      {
        if (localAEQueryScope != this.m_queryScope) {
          throw new IllegalArgumentException("Processing aggregate function with scope not equal to the current scope.");
        }
      }
      else {
        localAEQueryScope = this.m_queryScope;
      }
      return localAEQueryScope.addAggregateFunction(paramAEGeneralAggrFn);
    }
    
    public AEValueExpr visit(AENegate paramAENegate)
      throws ErrorException
    {
      AEValueExpr localAEValueExpr1 = createProxy(paramAENegate);
      if (null != localAEValueExpr1) {
        return localAEValueExpr1;
      }
      AEValueExpr localAEValueExpr2 = (AEValueExpr)paramAENegate.getOperand().acceptVisitor(this);
      paramAENegate.setOperand(localAEValueExpr2);
      return paramAENegate;
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
        AEBooleanExprProcessor.process(localAESearchedWhenClause.getWhenCondition(), this.m_queryScope);
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
      paramAESimpleCase.setElseOperand((AEValueExpr)paramAESimpleCase.getElseOperand().acceptVisitor(this));
      paramAESimpleCase.setCaseOperand((AEValueExpr)paramAESimpleCase.getCaseOperand().acceptVisitor(this));
      AENodeList localAENodeList = paramAESimpleCase.getWhenClauseList();
      Iterator localIterator = localAENodeList.getChildItr();
      while (localIterator.hasNext())
      {
        AESimpleWhenClause localAESimpleWhenClause = (AESimpleWhenClause)localIterator.next();
        localAESimpleWhenClause.setWhenExpression((AEValueExpr)localAESimpleWhenClause.getWhenExpression().acceptVisitor(this));
        localAESimpleWhenClause.setThenExpression((AEValueExpr)localAESimpleWhenClause.getThenExpression().acceptVisitor(this));
      }
      return paramAESimpleCase;
    }
    
    public AEValueExpr visit(AEValueSubQuery paramAEValueSubQuery)
      throws ErrorException
    {
      return paramAEValueSubQuery;
    }
    
    protected AEValueExpr defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.NON_GROUPING_COLUMN_IN_SEL_LIST.name(), new String[] { paramIAENode.getLogString() });
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEValueExprComposer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */