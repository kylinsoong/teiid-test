package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;

public abstract interface IJoinUnit
{
  public abstract boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException;
  
  public abstract void close();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/IJoinUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */