package com.simba.dsi.dataengine.impl;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.filters.IFilter;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IMetadataSource;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataColumn;
import com.simba.dsi.dataengine.utilities.MetadataColumnFactory;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.dataengine.utilities.OrderType;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DSIMetadataResultSet
  implements IResultSet
{
  protected ILogger m_logger;
  private IMetadataSource m_metadataSource;
  private List<IFilter> m_filters;
  protected List<MetadataColumn> m_columns;
  protected List<MetadataSourceColumnTag> m_sortOrder;
  private IWarningListener m_warningListener = null;
  private boolean m_performFiltering = true;
  protected List<MetadataRow> m_rows = null;
  private int m_currentRowIndex = -1;
  protected final OrderType m_orderType;
  protected final boolean m_isODBC2;
  
  public DSIMetadataResultSet(IStatement paramIStatement, MetadataSourceID paramMetadataSourceID, IMetadataSource paramIMetadataSource, List<IFilter> paramList, OrderType paramOrderType)
    throws ErrorException
  {
    this(paramIStatement, paramMetadataSourceID, paramIMetadataSource, paramList, paramOrderType, false);
  }
  
  public DSIMetadataResultSet(IStatement paramIStatement, MetadataSourceID paramMetadataSourceID, IMetadataSource paramIMetadataSource, List<IFilter> paramList, OrderType paramOrderType, boolean paramBoolean)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(paramIStatement.getLog(), new Object[] { paramIStatement, paramMetadataSourceID, paramIMetadataSource, paramList });
    assert (null != paramIMetadataSource);
    this.m_logger = paramIStatement.getLog();
    this.m_filters = paramList;
    this.m_metadataSource = paramIMetadataSource;
    this.m_orderType = paramOrderType;
    this.m_isODBC2 = paramBoolean;
    this.m_columns = MetadataColumnFactory.createMetadataColumns(paramIStatement, paramMetadataSourceID);
    this.m_sortOrder = MetadataColumnFactory.getSortOrder(paramIStatement.getLog(), paramMetadataSourceID, paramList, paramOrderType);
    try
    {
      Variant localVariant = DSIDriverSingleton.getInstance().getProperty(22);
      this.m_performFiltering = (1L == localVariant.getLong());
    }
    catch (Exception localException1)
    {
      LogUtilities.logError(localException1, this.m_logger);
    }
    if (OrderType.NONE != paramOrderType) {
      try
      {
        sortData();
      }
      catch (Exception localException2)
      {
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.SORTING_ERROR.name(), localException2);
      }
    } else {
      initializeRows();
    }
  }
  
  protected DSIMetadataResultSet(IStatement paramIStatement, MetadataSourceID paramMetadataSourceID, List<IFilter> paramList, OrderType paramOrderType)
    throws ErrorException
  {
    this(paramIStatement, paramMetadataSourceID, paramList, paramOrderType, false);
  }
  
  protected DSIMetadataResultSet(IStatement paramIStatement, MetadataSourceID paramMetadataSourceID, List<IFilter> paramList, OrderType paramOrderType, boolean paramBoolean)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(paramIStatement.getLog(), new Object[] { paramIStatement, paramMetadataSourceID, paramList });
    this.m_logger = paramIStatement.getLog();
    this.m_filters = paramList;
    this.m_orderType = paramOrderType;
    this.m_isODBC2 = paramBoolean;
    this.m_columns = MetadataColumnFactory.createMetadataColumns(paramIStatement, paramMetadataSourceID);
    this.m_sortOrder = MetadataColumnFactory.getSortOrder(paramIStatement.getLog(), paramMetadataSourceID, paramList, paramOrderType);
    try
    {
      Variant localVariant = DSIDriverSingleton.getInstance().getProperty(22);
      this.m_performFiltering = (1L == localVariant.getLong());
    }
    catch (Exception localException)
    {
      LogUtilities.logError(localException, this.m_logger);
    }
  }
  
  public void appendRow()
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void close()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null != this.m_metadataSource)
    {
      this.m_metadataSource.close();
      this.m_metadataSource = null;
    }
  }
  
  public void closeCursor()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null != this.m_metadataSource) {
      this.m_metadataSource.closeCursor();
    }
  }
  
  public void deleteRow()
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), paramDataWrapper });
    DataWrapper localDataWrapper = (DataWrapper)((MetadataRow)this.m_rows.get(this.m_currentRowIndex)).m_rowData.get(paramInt);
    try
    {
      if (null != localDataWrapper.getObject()) {
        paramDataWrapper.setData(localDataWrapper.getType(), localDataWrapper.getObject());
      } else {
        paramDataWrapper.setNull(localDataWrapper.getType());
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_MISMATCH.name(), new String[] { String.valueOf(localDataWrapper.getType()), localDataWrapper.getObject().getClass().getName() });
    }
    return false;
  }
  
  public int getFetchSize()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return 0;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return -1L;
  }
  
  public ArrayList<? extends IColumn> getSelectColumns()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return (ArrayList)this.m_columns;
  }
  
  public List<MetadataSourceColumnTag> getSortOrder()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return this.m_sortOrder;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return this.m_currentRowIndex < this.m_rows.size();
  }
  
  public boolean hasRowCount()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return false;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return ++this.m_currentRowIndex < this.m_rows.size();
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
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramCursorType });
    if (CursorType.FORWARD_ONLY != paramCursorType) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(paramCursorType), ExceptionType.DEFAULT);
    }
  }
  
  public void setFetchSize(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
  }
  
  public boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean)
    throws ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  protected boolean getUnsortedData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), paramDataWrapper });
    if (paramInt >= this.m_columns.size()) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_COLNUM.name(), String.valueOf(paramInt), ExceptionType.DATA);
    }
    if (((MetadataColumn)this.m_columns.get(paramInt)).isNullColumn())
    {
      paramDataWrapper.setNull(((MetadataColumn)this.m_columns.get(paramInt)).getTypeMetadata().getType());
      return false;
    }
    MetadataSourceColumnTag localMetadataSourceColumnTag = ((MetadataColumn)this.m_columns.get(paramInt)).getColumnTag();
    boolean bool = this.m_metadataSource.getMetadata(localMetadataSourceColumnTag, paramLong1, paramLong2, paramDataWrapper);
    if ((this.m_isODBC2) && (MetadataSourceColumnTag.DATA_TYPE == localMetadataSourceColumnTag))
    {
      int i = 0;
      try
      {
        i = paramDataWrapper.getSmallInt().intValue();
      }
      catch (IncorrectTypeException localIncorrectTypeException) {}
      switch (i)
      {
      case 91: 
        paramDataWrapper.setSmallInt(9);
        break;
      case 92: 
        paramDataWrapper.setSmallInt(10);
        break;
      case 93: 
        paramDataWrapper.setSmallInt(11);
      }
    }
    return bool;
  }
  
  protected boolean hasMoreUnsortedRows()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return this.m_metadataSource.hasMoreRows();
  }
  
  protected void initializeRows()
    throws ErrorException
  {
    Pair[] arrayOfPair = new Pair[this.m_sortOrder.size()];
    int j;
    Object localObject;
    for (int i = 0; i < this.m_sortOrder.size(); i++) {
      for (j = 0; j < this.m_columns.size(); j++)
      {
        localObject = ((MetadataColumn)this.m_columns.get(j)).getColumnTag();
        if (localObject == this.m_sortOrder.get(i)) {
          arrayOfPair[i] = new Pair(Integer.valueOf(j), localObject);
        }
      }
    }
    if (null == this.m_rows) {
      this.m_rows = new ArrayList();
    }
    while (moveToNextUnsortedRow())
    {
      ArrayList localArrayList = new ArrayList(this.m_columns.size());
      for (j = 0; j < this.m_columns.size(); j++)
      {
        localObject = new DataWrapper();
        try
        {
          getUnsortedData(j, 0L, -1L, (DataWrapper)localObject);
        }
        catch (ErrorException localErrorException)
        {
          if (((MetadataColumn)this.m_columns.get(j)).getColumnTag() == MetadataSourceColumnTag.USER_DATA_TYPE) {
            LogUtilities.logError(localErrorException, this.m_logger);
          } else {
            throw localErrorException;
          }
        }
        localArrayList.add(localObject);
      }
      this.m_rows.add(new MetadataRow(arrayOfPair, localArrayList, this.m_orderType));
    }
  }
  
  protected boolean moveToNextUnsortedRow()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (!this.m_performFiltering) {
      return this.m_metadataSource.moveToNextRow();
    }
    while (this.m_metadataSource.moveToNextRow()) {
      if (checkRow()) {
        return true;
      }
    }
    return false;
  }
  
  protected void sortData()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    initializeRows();
    Collections.sort(this.m_rows);
  }
  
  private boolean checkRow()
    throws ErrorException
  {
    DataWrapper localDataWrapper = new DataWrapper();
    Iterator localIterator = this.m_filters.iterator();
    while (localIterator.hasNext())
    {
      IFilter localIFilter = (IFilter)localIterator.next();
      this.m_metadataSource.getMetadata(localIFilter.getColumnTag(), 0L, -1L, localDataWrapper);
      if (!localIFilter.filter(localDataWrapper)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean supportsHasMoreRows()
  {
    return true;
  }
  
  protected static class MetadataRow
    implements Comparable<MetadataRow>
  {
    private final Pair<Integer, MetadataSourceColumnTag>[] m_columnsSortIndex;
    private final List<DataWrapper> m_rowData;
    private final OrderType m_orderType;
    
    public MetadataRow(Pair<Integer, MetadataSourceColumnTag>[] paramArrayOfPair, List<DataWrapper> paramList, OrderType paramOrderType)
    {
      this.m_columnsSortIndex = paramArrayOfPair;
      this.m_rowData = paramList;
      this.m_orderType = paramOrderType;
    }
    
    public int compareTo(MetadataRow paramMetadataRow)
    {
      for (Pair localPair : this.m_columnsSortIndex)
      {
        DataWrapper localDataWrapper1 = (DataWrapper)paramMetadataRow.m_rowData.get(((Integer)localPair.key()).intValue());
        DataWrapper localDataWrapper2 = (DataWrapper)this.m_rowData.get(((Integer)localPair.key()).intValue());
        if (localDataWrapper1.getType() != localDataWrapper2.getType()) {
          throw new ClassCastException("Unable to compare rows. Data type mismatch.");
        }
        int k = doCompareTo(localDataWrapper2, localDataWrapper1, (MetadataSourceColumnTag)localPair.value());
        if (0 != k) {
          return k;
        }
      }
      return 0;
    }
    
    private int doCompareTo(DataWrapper paramDataWrapper1, DataWrapper paramDataWrapper2, MetadataSourceColumnTag paramMetadataSourceColumnTag)
    {
      try
      {
        int i = null == paramDataWrapper1.getObject() ? 1 : 0;
        int j = null == paramDataWrapper2.getObject() ? 1 : 0;
        if ((i != 0) || (j != 0))
        {
          if ((i != 0) && (j != 0)) {
            return 0;
          }
          if (i != 0) {
            return -1;
          }
          return 1;
        }
        if (paramMetadataSourceColumnTag == MetadataSourceColumnTag.PROCEDURE_COLUMN_TYPE) {
          return compareProcedureColumnType(paramDataWrapper1.getSmallInt().intValue(), paramDataWrapper2.getSmallInt().intValue());
        }
        if (TypeUtilities.isIntegerType(paramDataWrapper1.getType()))
        {
          Long localLong1 = getIntegerValue(paramDataWrapper1);
          Long localLong2 = getIntegerValue(paramDataWrapper2);
          if ((paramMetadataSourceColumnTag == MetadataSourceColumnTag.DATA_TYPE) && (OrderType.ODBC != this.m_orderType))
          {
            localLong1 = Long.valueOf(TypeUtilities.mapDataTypes(localLong1.intValue()));
            localLong2 = Long.valueOf(TypeUtilities.mapDataTypes(localLong2.intValue()));
          }
          return localLong1.compareTo(localLong2);
        }
        return getStringValue(paramDataWrapper1).compareTo(getStringValue(paramDataWrapper2));
      }
      catch (IncorrectTypeException localIncorrectTypeException)
      {
        throw new ClassCastException(localIncorrectTypeException.getMessage());
      }
    }
    
    private int compareProcedureColumnType(int paramInt1, int paramInt2)
    {
      int i = getProcedureColumnTypeComparisonKey(paramInt1);
      int j = getProcedureColumnTypeComparisonKey(paramInt2);
      return i - j;
    }
    
    private static int getProcedureColumnTypeComparisonKey(int paramInt)
    {
      if (5 == paramInt) {
        return 0;
      }
      if ((1 == paramInt) || (2 == paramInt) || (4 == paramInt)) {
        return 1;
      }
      if (3 == paramInt) {
        return 2;
      }
      assert (0 == paramInt) : ("Unknown column type " + paramInt);
      return 3;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Iterator localIterator = this.m_rowData.iterator();
      while (localIterator.hasNext())
      {
        DataWrapper localDataWrapper = (DataWrapper)localIterator.next();
        if (null != localDataWrapper.getObject()) {
          localStringBuilder.append(localDataWrapper.getObject().toString());
        } else {
          localStringBuilder.append("null");
        }
        localStringBuilder.append("-");
      }
      return localStringBuilder.toString();
    }
    
    private Long getIntegerValue(DataWrapper paramDataWrapper)
      throws IncorrectTypeException
    {
      int i = paramDataWrapper.getType();
      if (-6 == i) {
        return Long.valueOf(paramDataWrapper.getTinyInt().shortValue());
      }
      if (5 == i) {
        return Long.valueOf(paramDataWrapper.getSmallInt().intValue());
      }
      if (4 == i) {
        return paramDataWrapper.getInteger();
      }
      return null;
    }
    
    private String getStringValue(DataWrapper paramDataWrapper)
      throws IncorrectTypeException
    {
      switch (paramDataWrapper.getType())
      {
      case -8: 
      case 1: 
        return paramDataWrapper.getChar();
      case -9: 
      case 12: 
        return paramDataWrapper.getVarChar();
      case -10: 
      case -1: 
        return paramDataWrapper.getLongVarChar();
      }
      return null;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIMetadataResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */