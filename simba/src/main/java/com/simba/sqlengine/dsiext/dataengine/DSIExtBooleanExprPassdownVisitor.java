package com.simba.sqlengine.dsiext.dataengine;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class DSIExtBooleanExprPassdownVisitor
  extends AEDefaultVisitor<Void>
{
  DSIExtAbstractBooleanExprHandler m_pdFilterDispatcher;
  boolean m_isPassedDown;
  
  public DSIExtBooleanExprPassdownVisitor(DSIExtAbstractBooleanExprHandler paramDSIExtAbstractBooleanExprHandler)
  {
    this.m_pdFilterDispatcher = paramDSIExtAbstractBooleanExprHandler;
  }
  
  public boolean passDown(AEBooleanExpr paramAEBooleanExpr)
  {
    this.m_isPassedDown = false;
    try
    {
      paramAEBooleanExpr.acceptVisitor(this);
    }
    catch (ErrorException localErrorException)
    {
      throw SQLEngineExceptionFactory.runtimeException(localErrorException);
    }
    return this.m_isPassedDown;
  }
  
  protected Void defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidOperationException("Unexpected node visited" + paramIAENode.getLogString());
  }
  
  public Void visit(AEAnd paramAEAnd)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownAnd(paramAEAnd);
    return null;
  }
  
  public Void visit(AEComparison paramAEComparison)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownComparison(paramAEComparison);
    return null;
  }
  
  public Void visit(AEExistsPredicate paramAEExistsPredicate)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownExistsPredicate(paramAEExistsPredicate);
    return null;
  }
  
  public Void visit(AEInPredicate paramAEInPredicate)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownInPredicate(paramAEInPredicate);
    return null;
  }
  
  public Void visit(AELikePredicate paramAELikePredicate)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownLikePredicate(paramAELikePredicate);
    return null;
  }
  
  public Void visit(AENot paramAENot)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownNot(paramAENot);
    return null;
  }
  
  public Void visit(AENullPredicate paramAENullPredicate)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownNullPredicate(paramAENullPredicate);
    return null;
  }
  
  public Void visit(AEOr paramAEOr)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownOr(paramAEOr);
    return null;
  }
  
  public Void visit(AEQuantifiedComparison paramAEQuantifiedComparison)
  {
    this.m_isPassedDown = this.m_pdFilterDispatcher.passdownQuantifiedComparison(paramAEQuantifiedComparison);
    return null;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtBooleanExprPassdownVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */