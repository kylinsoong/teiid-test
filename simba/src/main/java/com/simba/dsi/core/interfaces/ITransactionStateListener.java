package com.simba.dsi.core.interfaces;

import com.simba.support.exceptions.ErrorException;

public abstract interface ITransactionStateListener
{
  public abstract void notifyBeginTransaction()
    throws ErrorException;
  
  public abstract void notifyCommit()
    throws ErrorException;
  
  public abstract void notifyCreateSavepoint(String paramString)
    throws ErrorException;
  
  public abstract void notifyReleaseSavepoint(String paramString)
    throws ErrorException;
  
  public abstract void notifyRollback()
    throws ErrorException;
  
  public abstract void notifyRollbackSavepoint(String paramString)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/interfaces/ITransactionStateListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */