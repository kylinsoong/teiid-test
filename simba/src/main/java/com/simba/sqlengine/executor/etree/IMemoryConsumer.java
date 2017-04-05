package com.simba.sqlengine.executor.etree;

public abstract interface IMemoryConsumer
{
  public abstract void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent);
  
  public abstract long assign(long paramLong);
  
  public abstract long getRequiredMemory();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IMemoryConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */