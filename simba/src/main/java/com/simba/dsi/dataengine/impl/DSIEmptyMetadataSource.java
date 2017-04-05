package com.simba.dsi.dataengine.impl;

import com.simba.dsi.dataengine.interfaces.IMetadataSource;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;

public class DSIEmptyMetadataSource
  implements IMetadataSource
{
  private ILogger m_logger;
  
  public DSIEmptyMetadataSource(ILogger paramILogger)
  {
    LogUtilities.logFunctionEntrance(paramILogger, new Object[] { paramILogger });
    this.m_logger = paramILogger;
  }
  
  public void close()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
  }
  
  public void closeCursor()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
  }
  
  public boolean getMetadata(MetadataSourceColumnTag paramMetadataSourceColumnTag, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramMetadataSourceColumnTag, Long.valueOf(paramLong1), Long.valueOf(paramLong2), paramDataWrapper });
    paramDataWrapper.setNull(paramDataWrapper.getType());
    return false;
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return false;
  }
  
  public boolean moveToNextRow()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIEmptyMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */