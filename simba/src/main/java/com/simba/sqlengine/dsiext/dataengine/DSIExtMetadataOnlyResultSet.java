package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.util.ArrayList;

public final class DSIExtMetadataOnlyResultSet
  implements IResultSet
{
  private ArrayList<? extends IColumn> m_columns;
  
  DSIExtMetadataOnlyResultSet(ArrayList<? extends IColumn> paramArrayList)
  {
    this.m_columns = paramArrayList;
  }
  
  public void appendRow() {}
  
  public void close() {}
  
  public void closeCursor()
    throws ErrorException
  {}
  
  public void deleteRow()
    throws ErrorException
  {}
  
  public boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    return false;
  }
  
  public int getFetchSize()
    throws ErrorException
  {
    return 0;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return 0L;
  }
  
  public ArrayList<? extends IColumn> getSelectColumns()
    throws ErrorException
  {
    return this.m_columns;
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    return false;
  }
  
  public boolean hasRowCount()
  {
    return false;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    return false;
  }
  
  public void onFinishRowUpdate() {}
  
  public void onStartRowUpdate() {}
  
  public void registerWarningListener(IWarningListener paramIWarningListener) {}
  
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
      throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(paramCursorType));
    }
  }
  
  public void setFetchSize(int paramInt)
    throws ErrorException
  {}
  
  public boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean)
    throws ErrorException
  {
    throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  public final boolean supportsHasMoreRows()
  {
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtMetadataOnlyResultSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */