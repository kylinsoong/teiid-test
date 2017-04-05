package com.simba.sqlengine.dsiext.dataengine;

import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;

public abstract interface IBooleanExprHandler
{
  public abstract boolean canHandleMoreClauses();
  
  public abstract boolean passdown(AEBooleanExpr paramAEBooleanExpr);
  
  public abstract DSIExtJResultSet takeResult();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/IBooleanExprHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */