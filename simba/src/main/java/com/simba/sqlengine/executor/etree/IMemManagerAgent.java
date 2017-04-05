package com.simba.sqlengine.executor.etree;

public abstract interface IMemManagerAgent
{
  public abstract void recycleMemory(long paramLong);
  
  public abstract void unregisterConsumer();
  
  public abstract long require(long paramLong1, long paramLong2);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IMemManagerAgent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */