package com.simba.sqlengine.dsiext.dataengine;

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

public abstract class DSIExtAbstractBooleanExprHandler
  implements IBooleanExprHandler
{
  public boolean passdown(AEBooleanExpr paramAEBooleanExpr)
  {
    assert (paramAEBooleanExpr != null);
    return new DSIExtBooleanExprPassdownVisitor(this).passDown(paramAEBooleanExpr);
  }
  
  protected boolean passdownAnd(AEAnd paramAEAnd)
  {
    return false;
  }
  
  protected boolean passdownComparison(AEComparison paramAEComparison)
  {
    return false;
  }
  
  protected boolean passdownExistsPredicate(AEExistsPredicate paramAEExistsPredicate)
  {
    return false;
  }
  
  protected boolean passdownInPredicate(AEInPredicate paramAEInPredicate)
  {
    return false;
  }
  
  protected boolean passdownLikePredicate(AELikePredicate paramAELikePredicate)
  {
    return false;
  }
  
  protected boolean passdownNot(AENot paramAENot)
  {
    return false;
  }
  
  protected boolean passdownNullPredicate(AENullPredicate paramAENullPredicate)
  {
    return false;
  }
  
  protected boolean passdownOr(AEOr paramAEOr)
  {
    return false;
  }
  
  protected boolean passdownQuantifiedComparison(AEQuantifiedComparison paramAEQuantifiedComparison)
  {
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtAbstractBooleanExprHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */