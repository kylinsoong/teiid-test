package com.simba.sqlengine.executor;

import com.simba.dsi.dataengine.interfaces.IRowCountResult;
import com.simba.support.exceptions.ErrorException;

public class ETRowCountResult
  implements IRowCountResult
{
  long m_rowCount = -1L;
  
  public boolean hasRowCount()
  {
    return this.m_rowCount != -1L;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_rowCount;
  }
  
  public void close() {}
  
  public void setRowCount(long paramLong)
  {
    if ((paramLong < 0L) && (paramLong != -1L)) {
      throw new IllegalArgumentException("Invalid row count");
    }
    this.m_rowCount = paramLong;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/ETRowCountResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */