package com.simba.sqlengine.executor;

import com.simba.support.IWarningListener;

public abstract interface IWarningSource
{
  public abstract void registerWarningListener(IWarningListener paramIWarningListener);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/IWarningSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */