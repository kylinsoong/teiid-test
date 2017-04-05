package com.simba.sqlengine.executor.etree;

import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ETResourceManager
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree";
  private static final String CLASS_NAME = "ETResourceManager";
  private List<IETResource> m_resources = new LinkedList();
  private boolean m_allocated = false;
  private ILogger m_logger;
  
  public ETResourceManager(ILogger paramILogger)
  {
    this.m_logger = paramILogger;
  }
  
  public void registerResource(IETResource paramIETResource)
    throws ErrorException
  {
    if (this.m_allocated) {
      paramIETResource.allocate();
    }
    this.m_resources.add(paramIETResource);
  }
  
  public void allocate()
    throws ErrorException
  {
    Iterator localIterator = this.m_resources.iterator();
    while (localIterator.hasNext())
    {
      IETResource localIETResource = (IETResource)localIterator.next();
      localIETResource.allocate();
    }
    this.m_allocated = true;
  }
  
  public void free()
  {
    Iterator localIterator = this.m_resources.iterator();
    while (localIterator.hasNext())
    {
      IETResource localIETResource = (IETResource)localIterator.next();
      try
      {
        localIETResource.free();
      }
      catch (ErrorException localErrorException)
      {
        if (this.m_logger != null) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree", "ETResourceManager", "free", localErrorException);
        }
      }
      catch (Exception localException)
      {
        if (this.m_logger != null) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree", "ETResourceManager", "free", localException.getLocalizedMessage());
        }
      }
      catch (Error localError)
      {
        if (this.m_logger != null) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree", "ETResourceManager", "free", localError.getLocalizedMessage());
        }
      }
    }
    this.m_allocated = false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETResourceManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */