package com.simba.sqlengine.executor.etree;

import com.simba.support.exceptions.ErrorException;

public abstract interface IETExpr
  extends IETNode
{
  public abstract void close();
  
  public abstract boolean isOpen();
  
  public abstract void reset()
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IETExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */