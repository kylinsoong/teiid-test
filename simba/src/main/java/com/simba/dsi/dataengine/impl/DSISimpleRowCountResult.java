package com.simba.dsi.dataengine.impl;

import com.simba.dsi.dataengine.interfaces.IRowCountResult;
import com.simba.support.exceptions.ErrorException;

public class DSISimpleRowCountResult
  implements IRowCountResult
{
  private long m_rowCount;
  
  public DSISimpleRowCountResult(long paramLong)
  {
    this.m_rowCount = paramLong;
  }
  
  public void close() {}
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_rowCount;
  }
  
  public boolean hasRowCount()
  {
    return true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSISimpleRowCountResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */