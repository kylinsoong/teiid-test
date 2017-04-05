package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.support.exceptions.ErrorException;

public abstract interface IAggregatorFactory
{
  public abstract IAggregator createAggregator()
    throws ErrorException;
  
  public abstract boolean requiresDistinct();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/IAggregatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */