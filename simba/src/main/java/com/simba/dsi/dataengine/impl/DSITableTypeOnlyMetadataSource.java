package com.simba.dsi.dataengine.impl;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IMetadataSource;
import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.exceptions.InvalidArgumentException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.ArrayList;
import java.util.List;

public class DSITableTypeOnlyMetadataSource
  implements IMetadataSource
{
  private boolean m_isFetching = false;
  private int m_currentIndex = 0;
  List<String> m_tableTypes = new ArrayList();
  private ILogger m_logger;
  
  public DSITableTypeOnlyMetadataSource(ILogger paramILogger)
  {
    LogUtilities.logFunctionEntrance(paramILogger, new Object[] { paramILogger });
    this.m_logger = paramILogger;
    this.m_tableTypes.add("SYSTEM TABLE");
    this.m_tableTypes.add("TABLE");
    this.m_tableTypes.add("VIEW");
  }
  
  public DSITableTypeOnlyMetadataSource(ILogger paramILogger, List<String> paramList)
  {
    LogUtilities.logFunctionEntrance(paramILogger, new Object[] { paramILogger, paramList });
    if (null == paramList) {
      throw new InvalidArgumentException(2, "tableTypes");
    }
    this.m_logger = paramILogger;
    this.m_tableTypes = paramList;
  }
  
  public void close()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    try
    {
      closeCursor();
    }
    catch (ErrorException localErrorException) {}
  }
  
  public void closeCursor()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    this.m_isFetching = false;
  }
  
  public boolean getMetadata(MetadataSourceColumnTag paramMetadataSourceColumnTag, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramMetadataSourceColumnTag, Long.valueOf(paramLong1), Long.valueOf(paramLong2) });
    switch (paramMetadataSourceColumnTag)
    {
    case TABLE_TYPE: 
      return DSITypeUtilities.outputVarCharStringData((String)this.m_tableTypes.get(this.m_currentIndex), paramDataWrapper, paramLong1, paramLong2);
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_COLNUM.name(), new String[] { paramMetadataSourceColumnTag.toString() }, ExceptionType.DEFAULT);
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return this.m_currentIndex + 1 < this.m_tableTypes.size();
  }
  
  public boolean moveToNextRow()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (this.m_isFetching)
    {
      this.m_currentIndex += 1;
    }
    else
    {
      this.m_isFetching = true;
      this.m_currentIndex = 0;
    }
    return this.m_currentIndex < this.m_tableTypes.size();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSITableTypeOnlyMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */