package com.simba.dsi.dataengine.interfaces;

import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;

public abstract interface IResultSet
{
  public static final long RETRIEVE_ALL_DATA = -1L;
  public static final long ROW_COUNT_UNKNOWN = -1L;
  public static final int CURSOR_POSITION_BEFORE = -1;
  
  public abstract void appendRow()
    throws ErrorException;
  
  public abstract void close();
  
  public abstract void closeCursor()
    throws ErrorException;
  
  public abstract void deleteRow()
    throws ErrorException;
  
  public abstract boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException;
  
  public abstract int getFetchSize()
    throws ErrorException;
  
  public abstract long getRowCount()
    throws ErrorException;
  
  public abstract ArrayList<? extends IColumn> getSelectColumns()
    throws ErrorException;
  
  public abstract boolean hasMoreRows()
    throws ErrorException;
  
  public abstract boolean hasRowCount();
  
  public abstract boolean moveToNextRow()
    throws ErrorException;
  
  public abstract void onFinishRowUpdate()
    throws ErrorException;
  
  public abstract void onStartRowUpdate();
  
  public abstract void registerWarningListener(IWarningListener paramIWarningListener);
  
  public abstract boolean rowDeleted();
  
  public abstract boolean rowInserted();
  
  public abstract boolean rowUpdated();
  
  public abstract void setCursorType(CursorType paramCursorType)
    throws ErrorException;
  
  public abstract void setFetchSize(int paramInt)
    throws ErrorException;
  
  public abstract boolean supportsHasMoreRows();
  
  public abstract boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */