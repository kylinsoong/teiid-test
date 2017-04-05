package com.simba.sqlengine.dsiext.dataengine;

import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;

public abstract class AggregationHandler
{
  public abstract DSIExtJResultSet createResult();
  
  public abstract boolean setGroupingExpr(AEValueExpr paramAEValueExpr);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/AggregationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */