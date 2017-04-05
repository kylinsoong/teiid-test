package com.simba.sqlengine.executor.etree;

import com.simba.support.exceptions.ErrorException;

public abstract interface IETResource
{
  public abstract void allocate()
    throws ErrorException;
  
  public abstract void free()
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IETResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */