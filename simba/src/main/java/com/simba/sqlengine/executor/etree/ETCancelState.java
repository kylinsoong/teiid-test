package com.simba.sqlengine.executor.etree;

import com.simba.dsi.exceptions.OperationCanceledException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;

public class ETCancelState
{
  private volatile boolean m_isCanceled = false;
  
  public boolean isCanceled()
  {
    return this.m_isCanceled;
  }
  
  public void cancel()
  {
    this.m_isCanceled = true;
  }
  
  public void checkCancel()
    throws OperationCanceledException
  {
    if (this.m_isCanceled) {
      throw SQLEngineExceptionFactory.operationCanceledException();
    }
  }
  
  public void clearCancel()
  {
    this.m_isCanceled = false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETCancelState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */