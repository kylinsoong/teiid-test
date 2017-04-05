package com.simba.dsi.dataengine.interfaces;

import com.simba.support.exceptions.ErrorException;

public abstract interface IArray
{
  public abstract Object createArray(long paramLong, int paramInt)
    throws ErrorException;
  
  public abstract IResultSet createResultSet(long paramLong, int paramInt);
  
  public abstract void free();
  
  public abstract IColumn getBaseColumn();
  
  public abstract String getStringRepresentation();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */