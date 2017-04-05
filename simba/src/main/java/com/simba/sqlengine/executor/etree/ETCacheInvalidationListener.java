package com.simba.sqlengine.executor.etree;

public class ETCacheInvalidationListener
{
  private boolean m_isCacheValid = true;
  
  public ETCacheInvalidationListener(boolean paramBoolean)
  {
    this.m_isCacheValid = paramBoolean;
  }
  
  public void invalidateCache()
  {
    this.m_isCacheValid = false;
  }
  
  public boolean isCacheValid()
  {
    return this.m_isCacheValid;
  }
  
  public void resetCache()
  {
    this.m_isCacheValid = true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETCacheInvalidationListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */