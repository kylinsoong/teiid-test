package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprOuterRefProcessor;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBinaryBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class AEBooleanExprOuterRefProcessor
{
  public static void process(AEBooleanExpr paramAEBooleanExpr, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    paramAEBooleanExpr.acceptVisitor(new AEBooleanExprProcessor(paramAEQueryScope));
  }
  
  private static class AEBooleanExprProcessor
    extends AEDefaultVisitor<Void>
  {
    private final AEQueryScope m_queryScope;
    
    public AEBooleanExprProcessor(AEQueryScope paramAEQueryScope)
    {
      this.m_queryScope = paramAEQueryScope;
    }
    
    public Void visit(AEAnd paramAEAnd)
      throws ErrorException
    {
      return processBinary(paramAEAnd);
    }
    
    public Void visit(AEOr paramAEOr)
      throws ErrorException
    {
      return processBinary(paramAEOr);
    }
    
    public Void visit(AENot paramAENot)
      throws ErrorException
    {
      return (Void)paramAENot.getOperand().acceptVisitor(this);
    }
    
    public Void visit(AEComparison paramAEComparison)
      throws ErrorException
    {
      processValueList(paramAEComparison.getLeftOperand());
      return processValueList(paramAEComparison.getRightOperand());
    }
    
    public Void visit(AEExistsPredicate paramAEExistsPredicate)
    {
      return null;
    }
    
    public Void visit(AELikePredicate paramAELikePredicate)
      throws ErrorException
    {
      paramAELikePredicate.setLeftOperand(processValue(paramAELikePredicate.getLeftOperand()));
      paramAELikePredicate.setRightOperand(processValue(paramAELikePredicate.getRightOperand()));
      if (paramAELikePredicate.hasEscapeChar()) {
        paramAELikePredicate.setEscape(processValue(paramAELikePredicate.getEscapeChar()));
      }
      return null;
    }
    
    public Void visit(AEInPredicate paramAEInPredicate)
      throws ErrorException
    {
      if ((paramAEInPredicate.getRightOperand() instanceof AEValueExprList)) {
        processValueList((AEValueExprList)paramAEInPredicate.getRightOperand());
      }
      return null;
    }
    
    public Void visit(AENullPredicate paramAENullPredicate)
      throws ErrorException
    {
      processValueList(paramAENullPredicate.getOperand());
      return null;
    }
    
    public Void visit(AEQuantifiedComparison paramAEQuantifiedComparison)
      throws ErrorException
    {
      processValueList(paramAEQuantifiedComparison.getLeftOperand());
      return null;
    }
    
    protected Void defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      throw SQLEngineExceptionFactory.invalidOperationException("Unexpected node visited" + paramIAENode.getLogString());
    }
    
    private Void processBinary(AEBinaryBooleanExpr paramAEBinaryBooleanExpr)
      throws ErrorException
    {
      paramAEBinaryBooleanExpr.getLeftOperand().acceptVisitor(this);
      paramAEBinaryBooleanExpr.getRightOperand().acceptVisitor(this);
      return null;
    }
    
    private AEValueExpr processValue(AEValueExpr paramAEValueExpr)
      throws ErrorException
    {
      return AEValueExprOuterRefProcessor.process(paramAEValueExpr, this.m_queryScope);
    }
    
    private Void processValueList(AEValueExprList paramAEValueExprList)
      throws ErrorException
    {
      AEValueExprOuterRefProcessor.process(paramAEValueExprList, this.m_queryScope);
      return null;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEBooleanExprOuterRefProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */