package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.ITransactionStateListener;
import com.simba.support.exceptions.ErrorException;

class STransactionStateListener
  implements ITransactionStateListener
{
  private SConnection m_connection = null;
  
  protected STransactionStateListener(SConnection paramSConnection)
  {
    this.m_connection = paramSConnection;
  }
  
  public void notifyBeginTransaction()
    throws ErrorException
  {
    this.m_connection.notifyBeginTransaction();
  }
  
  public void notifyCommit()
    throws ErrorException
  {
    this.m_connection.notifyCommit();
  }
  
  public void notifyCreateSavepoint(String paramString)
    throws ErrorException
  {
    this.m_connection.notifyCreateSavepoint(paramString);
  }
  
  public void notifyReleaseSavepoint(String paramString)
    throws ErrorException
  {
    this.m_connection.notifyReleaseSavepoint(paramString);
  }
  
  public void notifyRollback()
    throws ErrorException
  {
    this.m_connection.notifyRollback();
  }
  
  public void notifyRollbackSavepoint(String paramString)
    throws ErrorException
  {
    this.m_connection.notifyRollbackSavepoint(paramString);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/STransactionStateListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */