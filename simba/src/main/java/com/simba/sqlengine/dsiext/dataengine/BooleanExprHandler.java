package com.simba.sqlengine.dsiext.dataengine;

public abstract class BooleanExprHandler
{
  public abstract boolean canHandleMoreClauses();
  
  public abstract DSIExtJResultSet takeResult();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/BooleanExprHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */