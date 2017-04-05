package com.simba.dsi.dataengine.impl;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.ArrayList;
import java.util.Iterator;

class DSIArrayResultSet
  implements IResultSet
{
  private static final String INDEX_COLUMN_NAME = "INDEX";
  private static final String VALUE_COLUMN_NAME = "VALUE";
  private final long m_startIndex;
  private final long m_maxSize;
  private long m_currentRow = -1L;
  private int m_fetchSize;
  private IWarningListener m_warningListener;
  private Iterator<?> m_iterator;
  private Object m_data;
  private ArrayList<? extends IColumn> m_selectColumns;
  private TypeMetadata m_typeMetadata;
  
  DSIArrayResultSet(Iterator<?> paramIterator, IColumn paramIColumn, long paramLong, int paramInt)
  {
    if (null == paramIterator) {
      throw new NullPointerException("iterator");
    }
    if (0L > paramLong) {
      throw new IndexOutOfBoundsException("offset: " + paramLong);
    }
    if ((0 > paramInt) && (-1 != paramInt)) {
      throw new IllegalArgumentException("count: " + paramInt);
    }
    this.m_iterator = paramIterator;
    this.m_startIndex = (paramLong + 1L);
    this.m_maxSize = paramInt;
    this.m_selectColumns = initializeSelectColumns(paramIColumn);
    this.m_typeMetadata = paramIColumn.getTypeMetadata();
  }
  
  public void appendRow()
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  public void close() {}
  
  public void closeCursor()
    throws ErrorException
  {
    this.m_iterator = null;
    this.m_currentRow = -1L;
  }
  
  public void deleteRow()
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  public boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    if (0L > this.m_currentRow) {
      throw new IllegalStateException();
    }
    switch (paramInt)
    {
    case 0: 
      paramDataWrapper.setBigInt(this.m_startIndex + this.m_currentRow);
      return false;
    case 1: 
      int i = this.m_typeMetadata.getType();
      if (null == this.m_data)
      {
        paramDataWrapper.setNull(i);
        return false;
      }
      try
      {
        Object localObject;
        if (TypeUtilities.isCharacterType(i))
        {
          localObject = (String)this.m_data;
          long l1 = paramLong1 / 2L;
          long l2;
          if (-1L != paramLong2) {
            l2 = paramLong2 / 2L;
          } else {
            l2 = paramLong2;
          }
          return DSITypeUtilities.outputString((String)localObject, paramDataWrapper, l1, l2, i);
        }
        if (TypeUtilities.isBinaryType(i))
        {
          localObject = (byte[])this.m_data;
          return DSITypeUtilities.outputBytes((byte[])localObject, paramDataWrapper, paramLong1, paramLong2, i);
        }
        paramDataWrapper.setData(i, this.m_data);
        return false;
      }
      catch (ClassCastException localClassCastException)
      {
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_MISMATCH.name(), new String[] { String.valueOf(i), this.m_data.getClass().getName() });
      }
      catch (IncorrectTypeException localIncorrectTypeException)
      {
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_MISMATCH.name(), new String[] { String.valueOf(i), this.m_data.getClass().getName() });
      }
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_COLNUM.name(), String.valueOf(paramInt + 1), ExceptionType.DATA);
  }
  
  public int getFetchSize()
    throws ErrorException
  {
    return this.m_fetchSize;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return -1L;
  }
  
  public ArrayList<? extends IColumn> getSelectColumns()
    throws ErrorException
  {
    return this.m_selectColumns;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    if (null == this.m_iterator) {
      return false;
    }
    if ((-1L != this.m_maxSize) && (this.m_maxSize <= this.m_currentRow)) {
      return false;
    }
    return this.m_iterator.hasNext();
  }
  
  public boolean hasRowCount()
  {
    return false;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    if (hasMoreRows())
    {
      this.m_currentRow += 1L;
      this.m_data = this.m_iterator.next();
      return true;
    }
    return false;
  }
  
  public void onFinishRowUpdate() {}
  
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
  
  public boolean supportsHasMoreRows()
  {
    return true;
  }
  
  public boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean)
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
  private static ArrayList<ColumnMetadata> initializeSelectColumns(IColumn paramIColumn)
  {
    ArrayList localArrayList = new ArrayList(2);
    ColumnMetadata localColumnMetadata1;
    try
    {
      localColumnMetadata1 = new ColumnMetadata(TypeMetadata.createTypeMetadata(-5, false));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
    localColumnMetadata1.setName("INDEX");
    localColumnMetadata1.setLabel("INDEX");
    localArrayList.add(localColumnMetadata1);
    ColumnMetadata localColumnMetadata2 = ColumnMetadata.copyOf(paramIColumn);
    localColumnMetadata2.setName("VALUE");
    localColumnMetadata2.setLabel("VALUE");
    localArrayList.add(localColumnMetadata2);
    return localArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIArrayResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */