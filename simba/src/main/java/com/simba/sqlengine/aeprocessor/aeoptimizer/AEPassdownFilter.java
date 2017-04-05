package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aemanipulator.CNFIterator;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
import com.simba.support.Pair;

public class AEPassdownFilter
{
  private IBooleanExprHandler m_handler;
  
  public AEPassdownFilter(IBooleanExprHandler paramIBooleanExprHandler)
  {
    assert (null != paramIBooleanExprHandler);
    this.m_handler = paramIBooleanExprHandler;
  }
  
  public Pair<DSIExtJResultSet, AEBooleanExpr> passdown(AEBooleanExpr paramAEBooleanExpr)
  {
    if (this.m_handler.passdown(paramAEBooleanExpr)) {
      return new Pair(this.m_handler.takeResult(), null);
    }
    if (this.m_handler.canHandleMoreClauses())
    {
      if (!(paramAEBooleanExpr instanceof AEAnd)) {
        return new Pair(this.m_handler.takeResult(), paramAEBooleanExpr);
      }
      CNFIterator localCNFIterator = new CNFIterator(paramAEBooleanExpr);
      while (localCNFIterator.hasNext())
      {
        if (this.m_handler.passdown(localCNFIterator.next())) {
          localCNFIterator.remove();
        }
        if (!this.m_handler.canHandleMoreClauses()) {
          break;
        }
      }
      return new Pair(this.m_handler.takeResult(), localCNFIterator.getExpr());
    }
    return new Pair(this.m_handler.takeResult(), paramAEBooleanExpr);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/AEPassdownFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */