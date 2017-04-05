package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprComposer;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBinaryBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class AEBooleanExprProcessor
{
  public static void process(AEBooleanExpr paramAEBooleanExpr, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    paramAEBooleanExpr.acceptVisitor(new AEBoolExprProcessorVisitor(paramAEQueryScope));
  }
  
  private static class AEBoolExprProcessorVisitor
    extends AEDefaultVisitor<Void>
  {
    private AEQueryScope m_queryScope = null;
    
    public AEBoolExprProcessorVisitor(AEQueryScope paramAEQueryScope)
    {
      this.m_queryScope = paramAEQueryScope;
    }
    
    public Void visit(AEAnd paramAEAnd)
      throws ErrorException
    {
      processBinary(paramAEAnd);
      return null;
    }
    
    public Void visit(AEOr paramAEOr)
      throws ErrorException
    {
      processBinary(paramAEOr);
      return null;
    }
    
    public Void visit(AENot paramAENot)
      throws ErrorException
    {
      return (Void)paramAENot.getOperand().acceptVisitor(this);
    }
    
    public Void visit(AEComparison paramAEComparison)
      throws ErrorException
    {
      AEValueExprComposer.compose(paramAEComparison.getLeftOperand(), this.m_queryScope);
      AEValueExprComposer.compose(paramAEComparison.getRightOperand(), this.m_queryScope);
      return null;
    }
    
    public Void visit(AEInPredicate paramAEInPredicate)
      throws ErrorException
    {
      AEValueExprComposer.compose(paramAEInPredicate.getLeftOperand(), this.m_queryScope);
      IAENode localIAENode = paramAEInPredicate.getRightOperand();
      if ((localIAENode instanceof AEValueExprList)) {
        AEValueExprComposer.compose((AEValueExprList)localIAENode, this.m_queryScope);
      }
      return null;
    }
    
    public Void visit(AENullPredicate paramAENullPredicate)
      throws ErrorException
    {
      AEValueExprComposer.compose(paramAENullPredicate.getOperand(), this.m_queryScope);
      return null;
    }
    
    public Void visit(AEBooleanTrue paramAEBooleanTrue)
      throws ErrorException
    {
      return null;
    }
    
    public Void visit(AEExistsPredicate paramAEExistsPredicate)
      throws ErrorException
    {
      return null;
    }
    
    public Void visit(AEQuantifiedComparison paramAEQuantifiedComparison)
      throws ErrorException
    {
      AEValueExprComposer.compose(paramAEQuantifiedComparison.getLeftOperand(), this.m_queryScope);
      return null;
    }
    
    public Void visit(AELikePredicate paramAELikePredicate)
      throws ErrorException
    {
      paramAELikePredicate.setLeftOperand(AEValueExprComposer.composeExpr(paramAELikePredicate.getLeftOperand(), this.m_queryScope));
      paramAELikePredicate.setRightOperand(AEValueExprComposer.composeExpr(paramAELikePredicate.getRightOperand(), this.m_queryScope));
      if (paramAELikePredicate.hasEscapeChar()) {
        paramAELikePredicate.setEscape(AEValueExprComposer.composeExpr(paramAELikePredicate.getEscapeChar(), this.m_queryScope));
      }
      return null;
    }
    
    protected Void defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      throw SQLEngineExceptionFactory.invalidOperationException("Unexpected node visited" + paramIAENode.getLogString());
    }
    
    private void processBinary(AEBinaryBooleanExpr paramAEBinaryBooleanExpr)
      throws ErrorException
    {
      paramAEBinaryBooleanExpr.getLeftOperand().acceptVisitor(this);
      paramAEBinaryBooleanExpr.getRightOperand().acceptVisitor(this);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEBooleanExprProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */