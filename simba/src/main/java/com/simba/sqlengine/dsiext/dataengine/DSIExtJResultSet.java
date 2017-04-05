package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.BitSet;

public abstract class DSIExtJResultSet
  implements IResultSet
{
  private BitSet m_needData;
  
  public abstract String getCatalogName();
  
  public boolean getDataNeeded(int paramInt)
  {
    if (null != this.m_needData) {
      return this.m_needData.get(paramInt);
    }
    return false;
  }
  
  public int getFetchSize()
    throws ErrorException
  {
    return 0;
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("IResult.hasMoreRows()");
  }
  
  public abstract String getSchemaName();
  
  public abstract String getTableName();
  
  public void onFinishDMLBatch()
    throws ErrorException
  {}
  
  public void onFinishRowUpdate()
    throws ErrorException
  {}
  
  public void onStartDMLBatch(DMLType paramDMLType, long paramLong)
    throws ErrorException
  {}
  
  public void onStartRowUpdate() {}
  
  public abstract void reset()
    throws ErrorException;
  
  public int resolveColumn(DSIExtJResultSet paramDSIExtJResultSet, int paramInt)
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public boolean rowDeleted()
  {
    return false;
  }
  
  public boolean rowInserted()
  {
    return false;
  }
  
  public boolean rowUpdated()
  {
    return false;
  }
  
  public void setDataNeeded(int paramInt)
    throws ErrorException
  {
    if (null == this.m_needData) {
      this.m_needData = new BitSet(paramInt);
    }
    this.m_needData.set(paramInt);
  }
  
  public void setFetchSize(int paramInt)
    throws ErrorException
  {}
  
  public final boolean supportsHasMoreRows()
  {
    return false;
  }
  
  public static enum DMLType
  {
    INSERT,  UPDATE,  UPSERT,  DELETE;
    
    private DMLType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtJResultSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */