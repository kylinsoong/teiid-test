package com.simba.sqlengine.executor.etree.temptable;

import com.simba.support.exceptions.ErrorException;

public abstract interface IRowBlock
  extends IRowView
{
  public abstract void close();
  
  public abstract boolean moveToNextRow()
    throws ErrorException;
  
  public abstract void reset()
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/IRowBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */