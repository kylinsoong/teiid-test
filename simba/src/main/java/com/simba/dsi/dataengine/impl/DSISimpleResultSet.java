package com.simba.dsi.dataengine.impl;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;

public abstract class DSISimpleResultSet
  implements IResultSet
{
  private int m_fetchSize;
  private boolean m_hasStartedFetch = false;
  private int m_currentRow = 0;
  private IWarningListener m_warningListener = null;
  
  public void appendRow()
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  public void close() {}
  
  public void closeCursor()
    throws ErrorException
  {
    this.m_hasStartedFetch = false;
    this.m_currentRow = 0;
    doCloseCursor();
  }
  
  public void deleteRow()
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  public int getFetchSize()
    throws ErrorException
  {
    return this.m_fetchSize;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    if (!this.m_hasStartedFetch)
    {
      this.m_hasStartedFetch = true;
      this.m_currentRow = 0;
    }
    else
    {
      this.m_currentRow += 1;
    }
    return doMoveToNextRow();
  }
  
  public void onFinishRowUpdate()
    throws ErrorException
  {}
  
  public void onStartRowUpdate() {}
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_warningListener = paramIWarningListener;
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
  
  public void setCursorType(CursorType paramCursorType)
    throws ErrorException
  {
    if (CursorType.FORWARD_ONLY != paramCursorType) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(paramCursorType));
    }
  }
  
  public void setFetchSize(int paramInt)
    throws ErrorException
  {
    this.m_fetchSize = paramInt;
  }
  
  public boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean)
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  public boolean supportsHasMoreRows()
  {
    return true;
  }
  
  protected abstract void doCloseCursor()
    throws ErrorException;
  
  protected abstract boolean doMoveToNextRow()
    throws ErrorException;
  
  protected int getCurrentRow()
  {
    return this.m_currentRow;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSISimpleResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */