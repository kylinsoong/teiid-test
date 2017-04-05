package com.simba.sqlengine.dsiext.dataengine;

import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;

public abstract interface DSIExtOperationHandlerFactory
{
  public abstract void setParameterSetCount(int paramInt);
  
  public abstract IBooleanExprHandler createFilterHandler(DSIExtJResultSet paramDSIExtJResultSet);
  
  public abstract IBooleanExprHandler createJoinHandler(DSIExtJResultSet paramDSIExtJResultSet1, DSIExtJResultSet paramDSIExtJResultSet2, AEJoin.AEJoinType paramAEJoinType);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtOperationHandlerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */