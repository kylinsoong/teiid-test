package com.simba.dsi.dataengine.interfaces;

import com.simba.support.exceptions.ErrorException;

public abstract interface IRowCountResult
{
  public abstract boolean hasRowCount();
  
  public abstract long getRowCount()
    throws ErrorException;
  
  public abstract void close();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IRowCountResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */