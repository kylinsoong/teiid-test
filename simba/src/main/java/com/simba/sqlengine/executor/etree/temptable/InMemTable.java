package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceArray;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceBuilder;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice;
import com.simba.sqlengine.executor.etree.util.CompressionUtil;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class InMemTable
{
  private static final int NUM_ROW_PER_SLICE_DEFAULT = 100;
  private long m_memLimit;
  private int m_numRowPerSlice;
  private int m_numRows;
  private int m_varLenColMemUsage;
  protected ILogger m_logger;
  protected IColumn[] m_columnMetadata;
  protected ArrayList<ColumnSliceArray> m_data;
  protected double m_memOverheadPerRow;
  protected double m_fixLenDataSizePerRow;
  private HashSet<Integer> m_removedRows;
  private int m_curWritingRow = -1;
  private boolean[] m_isVarLenColumns;
  private boolean[] m_isLongDataColumn;
  private int m_maxRowNumber = -1;
  private int m_maxDataLength;
  private long m_maxRowSize;
  private ETDataRequest[] m_requests;
  private boolean[] m_needsData;
  
  public InMemTable(IColumn[] paramArrayOfIColumn, int paramInt1, int paramInt2, boolean[] paramArrayOfBoolean, ILogger paramILogger)
  {
    if ((paramInt1 < 0) || (paramArrayOfIColumn == null)) {
      throw new IllegalArgumentException("Invalid argument for InMemTable Constructor.");
    }
    this.m_numRowPerSlice = paramInt2;
    if (this.m_numRowPerSlice <= 0) {
      this.m_numRowPerSlice = 100;
    }
    this.m_maxDataLength = paramInt1;
    this.m_numRows = 0;
    this.m_varLenColMemUsage = 0;
    this.m_logger = paramILogger;
    this.m_needsData = ((boolean[])paramArrayOfBoolean.clone());
    this.m_columnMetadata = new IColumn[paramArrayOfIColumn.length];
    this.m_isLongDataColumn = new boolean[paramArrayOfIColumn.length];
    this.m_isVarLenColumns = new boolean[paramArrayOfIColumn.length];
    this.m_requests = new ETDataRequest[paramArrayOfIColumn.length];
    for (int i = 0; i < paramArrayOfIColumn.length; i++)
    {
      this.m_columnMetadata[i] = paramArrayOfIColumn[i];
      if (ColumnSizeCalculator.isLongData(this.m_columnMetadata[i], this.m_maxDataLength)) {
        this.m_isLongDataColumn[i] = true;
      } else if (this.m_columnMetadata[i].getTypeMetadata().isCharacterOrBinaryType()) {
        this.m_isVarLenColumns[i] = true;
      }
    }
    this.m_maxRowSize = ExternalAlgorithmUtil.calculateRowSize(Arrays.asList(this.m_columnMetadata), this.m_needsData, this.m_maxDataLength);
    this.m_data = new ArrayList();
    this.m_removedRows = new HashSet();
    this.m_memOverheadPerRow = 0.0D;
    this.m_fixLenDataSizePerRow = 0.0D;
    for (i = 0; i < this.m_columnMetadata.length; i++)
    {
      if (this.m_isVarLenColumns[i] == 0) {
        this.m_fixLenDataSizePerRow += ColumnSizeCalculator.getColumnSizePerRow(this.m_columnMetadata[i], this.m_needsData[i], this.m_maxDataLength);
      }
      this.m_memOverheadPerRow += ColumnSizeCalculator.getOverHeadPerRow(this.m_columnMetadata[i], this.m_needsData[i], this.m_maxDataLength);
      if (this.m_isLongDataColumn[i] == 0) {
        this.m_requests[i] = new ETDataRequest(paramArrayOfIColumn[i]);
      }
    }
  }
  
  public static void setColumn(InMemTable paramInMemTable, IRowView paramIRowView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramIRowView.isNull(paramInt1)) {
      paramInMemTable.setNull(paramInt2, paramInt1);
    } else {
      switch (paramIRowView.getColumn(paramInt1).getTypeMetadata().getType())
      {
      case -5: 
        paramInMemTable.setBigInt(paramInt2, paramInt1, paramIRowView.getBigInt(paramInt1));
        break;
      case -4: 
      case -3: 
      case -2: 
        if (ColumnSizeCalculator.isLongData(paramIRowView.getColumn(paramInt1), paramInt3)) {
          paramInMemTable.setFileMarker(paramInt2, paramInt1, paramIRowView.getFileMarker(paramInt1));
        } else {
          paramInMemTable.setBytes(paramInt2, paramInt1, paramIRowView.getBytes(paramInt1));
        }
        break;
      case -7: 
      case 16: 
        paramInMemTable.setBoolean(paramInt2, paramInt1, paramIRowView.getBoolean(paramInt1));
        break;
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        if (ColumnSizeCalculator.isLongData(paramIRowView.getColumn(paramInt1), paramInt3)) {
          paramInMemTable.setFileMarker(paramInt2, paramInt1, paramIRowView.getFileMarker(paramInt1));
        } else {
          paramInMemTable.setString(paramInt2, paramInt1, paramIRowView.getString(paramInt1));
        }
        break;
      case 91: 
        paramInMemTable.setDate(paramInt2, paramInt1, paramIRowView.getDate(paramInt1));
        break;
      case 6: 
      case 8: 
        paramInMemTable.setDouble(paramInt2, paramInt1, paramIRowView.getDouble(paramInt1));
        break;
      case 2: 
      case 3: 
        paramInMemTable.setExactNum(paramInt2, paramInt1, paramIRowView.getExactNumber(paramInt1));
        break;
      case -11: 
        paramInMemTable.setGuid(paramInt2, paramInt1, paramIRowView.getGuid(paramInt1));
        break;
      case 4: 
        paramInMemTable.setInteger(paramInt2, paramInt1, paramIRowView.getInteger(paramInt1));
        break;
      case 7: 
        paramInMemTable.setReal(paramInt2, paramInt1, paramIRowView.getReal(paramInt1));
        break;
      case 5: 
        paramInMemTable.setSmallInt(paramInt2, paramInt1, paramIRowView.getSmallInt(paramInt1));
        break;
      case 92: 
        paramInMemTable.setTime(paramInt2, paramInt1, paramIRowView.getTime(paramInt1));
        break;
      case 93: 
        paramInMemTable.setTimestamp(paramInt2, paramInt1, paramIRowView.getTimestamp(paramInt1));
        break;
      case -6: 
        paramInMemTable.setTinyInt(paramInt2, paramInt1, paramIRowView.getTinyInt(paramInt1));
        break;
      case 0: 
      case 9: 
      case 10: 
      case 11: 
      case 13: 
      case 14: 
      case 15: 
      case 17: 
      case 18: 
      case 19: 
      case 20: 
      case 21: 
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 26: 
      case 27: 
      case 28: 
      case 29: 
      case 30: 
      case 31: 
      case 32: 
      case 33: 
      case 34: 
      case 35: 
      case 36: 
      case 37: 
      case 38: 
      case 39: 
      case 40: 
      case 41: 
      case 42: 
      case 43: 
      case 44: 
      case 45: 
      case 46: 
      case 47: 
      case 48: 
      case 49: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 54: 
      case 55: 
      case 56: 
      case 57: 
      case 58: 
      case 59: 
      case 60: 
      case 61: 
      case 62: 
      case 63: 
      case 64: 
      case 65: 
      case 66: 
      case 67: 
      case 68: 
      case 69: 
      case 70: 
      case 71: 
      case 72: 
      case 73: 
      case 74: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
      case 87: 
      case 88: 
      case 89: 
      case 90: 
      default: 
        throw new IllegalStateException(String.format("Unknown data type: %d.", new Object[] { Short.valueOf(paramIRowView.getColumn(paramInt1).getTypeMetadata().getType()) }));
      }
    }
  }
  
  public static void setColumn(InMemTable paramInMemTable, ISqlDataWrapper paramISqlDataWrapper, IColumn paramIColumn, int paramInt1, int paramInt2)
    throws ErrorException
  {
    if (paramISqlDataWrapper.isNull()) {
      paramInMemTable.setNull(paramInt2, paramInt1);
    } else {
      switch (paramIColumn.getTypeMetadata().getType())
      {
      case -6: 
        paramInMemTable.setTinyInt(paramInt2, paramInt1, (byte)paramISqlDataWrapper.getTinyInt());
        break;
      case 5: 
        paramInMemTable.setSmallInt(paramInt2, paramInt1, (short)paramISqlDataWrapper.getSmallInt());
        break;
      case 4: 
        paramInMemTable.setInteger(paramInt2, paramInt1, (int)paramISqlDataWrapper.getInteger());
        break;
      case -5: 
        paramInMemTable.setBigInt(paramInt2, paramInt1, paramISqlDataWrapper.getBigInt().longValue());
        break;
      case 7: 
        paramInMemTable.setReal(paramInt2, paramInt1, paramISqlDataWrapper.getReal());
        break;
      case 6: 
      case 8: 
        paramInMemTable.setDouble(paramInt2, paramInt1, paramISqlDataWrapper.getDouble());
        break;
      case 2: 
      case 3: 
        paramInMemTable.setExactNum(paramInt2, paramInt1, paramISqlDataWrapper.getExactNumber());
        break;
      case -7: 
      case 16: 
        paramInMemTable.setBoolean(paramInt2, paramInt1, paramISqlDataWrapper.getBoolean());
        break;
      case 91: 
        paramInMemTable.setDate(paramInt2, paramInt1, paramISqlDataWrapper.getDate());
        break;
      case 92: 
        paramInMemTable.setTime(paramInt2, paramInt1, paramISqlDataWrapper.getTime());
        break;
      case 93: 
        paramInMemTable.setTimestamp(paramInt2, paramInt1, paramISqlDataWrapper.getTimestamp());
        break;
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        paramInMemTable.setString(paramInt2, paramInt1, paramISqlDataWrapper.getChar());
        break;
      case -4: 
      case -3: 
      case -2: 
        paramInMemTable.setBytes(paramInt2, paramInt1, paramISqlDataWrapper.getBinary());
        break;
      case -11: 
        paramInMemTable.setGuid(paramInt2, paramInt1, paramISqlDataWrapper.getGuid());
        break;
      case 0: 
      case 9: 
      case 10: 
      case 11: 
      case 13: 
      case 14: 
      case 15: 
      case 17: 
      case 18: 
      case 19: 
      case 20: 
      case 21: 
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 26: 
      case 27: 
      case 28: 
      case 29: 
      case 30: 
      case 31: 
      case 32: 
      case 33: 
      case 34: 
      case 35: 
      case 36: 
      case 37: 
      case 38: 
      case 39: 
      case 40: 
      case 41: 
      case 42: 
      case 43: 
      case 44: 
      case 45: 
      case 46: 
      case 47: 
      case 48: 
      case 49: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 54: 
      case 55: 
      case 56: 
      case 57: 
      case 58: 
      case 59: 
      case 60: 
      case 61: 
      case 62: 
      case 63: 
      case 64: 
      case 65: 
      case 66: 
      case 67: 
      case 68: 
      case 69: 
      case 70: 
      case 71: 
      case 72: 
      case 73: 
      case 74: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
      case 87: 
      case 88: 
      case 89: 
      case 90: 
      default: 
        throw new UnsupportedOperationException(paramIColumn.getTypeMetadata().getTypeName());
      }
    }
  }
  
  public int appendRow()
  {
    if (!canAppendRow()) {
      return -1;
    }
    if (!this.m_removedRows.isEmpty())
    {
      Iterator localIterator = this.m_removedRows.iterator();
      this.m_curWritingRow = ((Integer)localIterator.next()).intValue();
      localIterator.remove();
      this.m_numRows += 1;
      return this.m_curWritingRow;
    }
    if (getCapacity() == this.m_numRows) {
      addEmptyRows();
    }
    this.m_numRows += 1;
    this.m_curWritingRow = (this.m_numRows - 1);
    if (this.m_curWritingRow > this.m_maxRowNumber) {
      this.m_maxRowNumber = this.m_curWritingRow;
    }
    return this.m_curWritingRow;
  }
  
  public int getMaxRowNumber()
  {
    return this.m_maxRowNumber;
  }
  
  public void removeRow(int paramInt)
  {
    if (this.m_removedRows.contains(Integer.valueOf(paramInt))) {
      throw new IllegalArgumentException("Invalid row number");
    }
    for (int i = 0; i < this.m_columnMetadata.length; i++) {
      if ((this.m_needsData[i] != 0) && (!isNull(paramInt, i)))
      {
        if (this.m_isVarLenColumns[i] != 0)
        {
          assert (this.m_columnMetadata[i].getTypeMetadata().isCharacterOrBinaryType());
          if (this.m_columnMetadata[i].getTypeMetadata().isCharacterType()) {
            this.m_varLenColMemUsage -= getString(paramInt, i).length() * 2;
          } else {
            this.m_varLenColMemUsage -= getBytes(paramInt, i).length;
          }
        }
        setNull(paramInt, i);
      }
    }
    this.m_removedRows.add(Integer.valueOf(paramInt));
    if (paramInt == this.m_curWritingRow) {
      this.m_curWritingRow = -1;
    }
    this.m_numRows -= 1;
  }
  
  public long getMemLimit()
  {
    return this.m_memLimit;
  }
  
  public int getMemOverhead()
  {
    return (int)Math.ceil(this.m_memOverheadPerRow * this.m_numRowPerSlice);
  }
  
  public int getNumRows()
  {
    return this.m_numRows;
  }
  
  public void setMemLimit(long paramLong)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Long.valueOf(paramLong) });
    }
    if (paramLong < 0L) {
      throw new IllegalArgumentException("Setting a negative memory limit.");
    }
    this.m_memLimit = paramLong;
  }
  
  public boolean isNull(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).isNull(getSliceRowNum(paramInt1));
  }
  
  public long getBigInt(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getBigInt(getSliceRowNum(paramInt1));
  }
  
  public BigDecimal getExactNum(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getExactNum(getSliceRowNum(paramInt1));
  }
  
  public double getDouble(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getDouble(getSliceRowNum(paramInt1));
  }
  
  public float getReal(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getReal(getSliceRowNum(paramInt1));
  }
  
  public boolean getBoolean(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getBoolean(getSliceRowNum(paramInt1));
  }
  
  public String getString(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getString(getSliceRowNum(paramInt1));
  }
  
  public Date getDate(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getDate(getSliceRowNum(paramInt1));
  }
  
  public Time getTime(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getTime(getSliceRowNum(paramInt1));
  }
  
  public Timestamp getTimestamp(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getTimestamp(getSliceRowNum(paramInt1));
  }
  
  public UUID getGuid(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getGuid(getSliceRowNum(paramInt1));
  }
  
  public int getInteger(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getInteger(getSliceRowNum(paramInt1));
  }
  
  public short getSmallInt(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getSmallInt(getSliceRowNum(paramInt1));
  }
  
  public byte getTinyInt(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getTinyInt(getSliceRowNum(paramInt1));
  }
  
  public byte[] getBytes(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getBytes(getSliceRowNum(paramInt1));
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt1, int paramInt2)
  {
    return getSlice(paramInt2, paramInt1).getFileMarker(getSliceRowNum(paramInt1));
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_columnMetadata[paramInt];
  }
  
  public void setNull(int paramInt1, int paramInt2)
  {
    getSlice(paramInt2, paramInt1).setNull(getSliceRowNum(paramInt1));
  }
  
  public void setBigInt(int paramInt1, int paramInt2, long paramLong)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setBigInt(getSliceRowNum(paramInt1), paramLong);
  }
  
  public void setExactNum(int paramInt1, int paramInt2, BigDecimal paramBigDecimal)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setExactNum(getSliceRowNum(paramInt1), paramBigDecimal);
  }
  
  public void setDouble(int paramInt1, int paramInt2, double paramDouble)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setDouble(getSliceRowNum(paramInt1), paramDouble);
  }
  
  public void setReal(int paramInt1, int paramInt2, float paramFloat)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setReal(getSliceRowNum(paramInt1), paramFloat);
  }
  
  public void setBoolean(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setBoolean(getSliceRowNum(paramInt1), paramBoolean);
  }
  
  public void setString(int paramInt1, int paramInt2, String paramString)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    assert (paramString != null);
    getSlice(paramInt2, paramInt1).setString(getSliceRowNum(paramInt1), paramString);
    this.m_varLenColMemUsage += paramString.length() * 2;
  }
  
  public void setDate(int paramInt1, int paramInt2, Date paramDate)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setDate(getSliceRowNum(paramInt1), paramDate);
  }
  
  public void setTime(int paramInt1, int paramInt2, Time paramTime)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setTime(getSliceRowNum(paramInt1), paramTime);
  }
  
  public void setTimestamp(int paramInt1, int paramInt2, Timestamp paramTimestamp)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setTimestamp(getSliceRowNum(paramInt1), paramTimestamp);
  }
  
  public void setFileMarker(int paramInt1, int paramInt2, TemporaryFile.FileMarker paramFileMarker)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setFileMarker(getSliceRowNum(paramInt1), paramFileMarker);
  }
  
  public void setGuid(int paramInt1, int paramInt2, UUID paramUUID)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setGuid(getSliceRowNum(paramInt1), paramUUID);
  }
  
  public void setInteger(int paramInt1, int paramInt2, int paramInt3)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setInteger(getSliceRowNum(paramInt1), paramInt3);
  }
  
  public void setSmallInt(int paramInt1, int paramInt2, short paramShort)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setSmallInt(getSliceRowNum(paramInt1), paramShort);
  }
  
  public void setTinyInt(int paramInt1, int paramInt2, byte paramByte)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    getSlice(paramInt2, paramInt1).setTinyInt(getSliceRowNum(paramInt1), paramByte);
  }
  
  public void setBytes(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    assert (paramInt2 < this.m_columnMetadata.length);
    assert (paramArrayOfByte != null);
    getSlice(paramInt2, paramInt1).setBytes(getSliceRowNum(paramInt1), paramArrayOfByte);
    this.m_varLenColMemUsage += paramArrayOfByte.length;
  }
  
  public RowBlock toRowBlock()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    if (this.m_numRows == 0) {
      return null;
    }
    ColumnSliceArray localColumnSliceArray1 = ColumnSliceBuilder.buildColumnSliceArray(this.m_columnMetadata, this.m_needsData, this.m_numRows, this.m_maxDataLength);
    Object localObject = localColumnSliceArray1.iterator();
    while (((Iterator)localObject).hasNext())
    {
      IColumnSlice localIColumnSlice = (IColumnSlice)((Iterator)localObject).next();
      int i = 0;
      int j = 0;
      Iterator localIterator = this.m_data.iterator();
      while (localIterator.hasNext())
      {
        ColumnSliceArray localColumnSliceArray2 = (ColumnSliceArray)localIterator.next();
        if (j == this.m_data.size() - 1)
        {
          int k = this.m_numRows - this.m_numRowPerSlice * (this.m_data.size() - 1);
          localIColumnSlice.copy(localColumnSliceArray2.get(localIColumnSlice.columnNumber()), 0, k, i);
          i += k;
        }
        else
        {
          localIColumnSlice.copy(localColumnSliceArray2.get(localIColumnSlice.columnNumber()), 0, this.m_numRowPerSlice, i);
          i += localColumnSliceArray2.numRows();
        }
        j++;
      }
    }
    if (null != this.m_logger) {
      LogUtilities.logTrace(String.format("Write %d rows to new block", new Object[] { Integer.valueOf(this.m_numRows) }), this.m_logger);
    }
    clear();
    localObject = new RowBlock(localColumnSliceArray1, this.m_columnMetadata, this.m_logger);
    return (RowBlock)localObject;
  }
  
  public void clear()
  {
    for (int i = 0; i < this.m_data.size(); i++) {
      this.m_data.clear();
    }
    this.m_numRows = 0;
    this.m_curWritingRow = -1;
    this.m_maxRowNumber = -1;
    this.m_varLenColMemUsage = 0;
    this.m_removedRows.clear();
  }
  
  private void addEmptyRows()
  {
    ColumnSliceArray localColumnSliceArray = ColumnSliceBuilder.buildColumnSliceArray(this.m_columnMetadata, this.m_needsData, this.m_numRowPerSlice, this.m_maxDataLength);
    this.m_data.add(localColumnSliceArray);
  }
  
  private int getCapacity()
  {
    if (this.m_data.size() == 0) {
      return 0;
    }
    return this.m_data.size() * this.m_numRowPerSlice;
  }
  
  protected IColumnSlice getSlice(int paramInt1, int paramInt2)
  {
    return ((ColumnSliceArray)this.m_data.get(paramInt2 / this.m_numRowPerSlice)).get(paramInt1);
  }
  
  protected int getSliceRowNum(int paramInt)
  {
    return paramInt % this.m_numRowPerSlice;
  }
  
  public int getMemUsage()
  {
    if (this.m_maxRowNumber < 0) {
      return 0;
    }
    return (int)Math.ceil(this.m_memOverheadPerRow * (this.m_maxRowNumber + 1) + this.m_fixLenDataSizePerRow * this.m_numRows) + this.m_varLenColMemUsage;
  }
  
  public int getRowSize(int paramInt)
  {
    int i = 0;
    for (int j = 0; j < this.m_isVarLenColumns.length; j++) {
      if ((this.m_isVarLenColumns[j] != 0) && (this.m_needsData[j] != 0) && (!isNull(paramInt, j))) {
        if (this.m_columnMetadata[j].getTypeMetadata().isCharacterType()) {
          i += getString(paramInt, j).length() * 2;
        } else {
          i += getBytes(paramInt, j).length;
        }
      }
    }
    return i + (int)Math.ceil(this.m_memOverheadPerRow + this.m_fixLenDataSizePerRow);
  }
  
  public int getDataUsage()
  {
    return (int)Math.ceil(this.m_memOverheadPerRow * this.m_numRows + this.m_fixLenDataSizePerRow * this.m_numRows) + this.m_varLenColMemUsage;
  }
  
  protected boolean isOverMemLimit(long paramLong)
  {
    return paramLong > this.m_memLimit;
  }
  
  public boolean canAppendRow()
  {
    return (this.m_numRows == 0) || (!isOverMemLimit(this.m_maxRowSize + getMemUsage()));
  }
  
  public boolean isLongDataColumn(int paramInt)
  {
    return this.m_isLongDataColumn[paramInt];
  }
  
  public boolean retrieveData(int paramInt1, int paramInt2, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (this.m_isLongDataColumn[paramInt2] != 0) {
      throw new IllegalArgumentException("Cannot retrieve data from long data column");
    }
    if (isNull(paramInt1, paramInt2))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
    int i;
    switch (localISqlDataWrapper.getType())
    {
    case -5: 
      long l = getBigInt(paramInt1, paramInt2);
      localISqlDataWrapper.setBigInt(CompressionUtil.getlongAsBigInteger(l, this.m_columnMetadata[paramInt2].getTypeMetadata().isSigned()));
      return false;
    case 2: 
    case 3: 
      localISqlDataWrapper.setExactNumber(getExactNum(paramInt1, paramInt2));
      return false;
    case 6: 
    case 8: 
      localISqlDataWrapper.setDouble(getDouble(paramInt1, paramInt2));
      return false;
    case 7: 
      localISqlDataWrapper.setReal(getReal(paramInt1, paramInt2));
      return false;
    case -7: 
    case 16: 
      localISqlDataWrapper.setBoolean(getBoolean(paramInt1, paramInt2));
      return false;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      localISqlDataWrapper.setChar(getString(paramInt1, paramInt2));
      break;
    case -4: 
    case -3: 
    case -2: 
      localISqlDataWrapper.setBinary(getBytes(paramInt1, paramInt2));
      break;
    case 91: 
      localISqlDataWrapper.setDate(getDate(paramInt1, paramInt2));
      return false;
    case 92: 
      localISqlDataWrapper.setTime(getTime(paramInt1, paramInt2));
      return false;
    case 93: 
      localISqlDataWrapper.setTimestamp(getTimestamp(paramInt1, paramInt2));
      return false;
    case -11: 
      localISqlDataWrapper.setGuid(getGuid(paramInt1, paramInt2));
      return false;
    case 4: 
      i = getInteger(paramInt1, paramInt2);
      localISqlDataWrapper.setInteger(CompressionUtil.getIntAsLong(i, this.m_columnMetadata[paramInt2].getTypeMetadata().isSigned()));
      return false;
    case 5: 
      i = getSmallInt(paramInt1, paramInt2);
      localISqlDataWrapper.setSmallInt(CompressionUtil.getSmallIntAsInteger(i, this.m_columnMetadata[paramInt2].getTypeMetadata().isSigned()));
      return false;
    case -6: 
      byte b = getTinyInt(paramInt1, paramInt2);
      localISqlDataWrapper.setTinyInt(CompressionUtil.getTinyIntAsShort(b, this.m_columnMetadata[paramInt2].getTypeMetadata().isSigned()));
      return false;
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    default: 
      throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + localISqlDataWrapper.getType());
    }
    if (paramETDataRequest.getColumn().getTypeMetadata().isCharacterType()) {
      return DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
    }
    assert (paramETDataRequest.getColumn().getTypeMetadata().isBinaryType());
    return DataRetrievalUtil.retrieveBinaryData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
  }
  
  public void writeData(int paramInt1, int paramInt2, ETRelationalExpr paramETRelationalExpr)
    throws ErrorException
  {
    ETDataRequest localETDataRequest = this.m_requests[paramInt2];
    if (localETDataRequest == null) {
      throw new IllegalArgumentException("Can not write long data.");
    }
    paramETRelationalExpr.retrieveData(paramInt2, localETDataRequest);
    ISqlDataWrapper localISqlDataWrapper = localETDataRequest.getData();
    if (localISqlDataWrapper.isNull())
    {
      setNull(paramInt1, paramInt2);
      return;
    }
    switch (localISqlDataWrapper.getType())
    {
    case -5: 
      setBigInt(paramInt1, paramInt2, localISqlDataWrapper.getBigInt().longValue());
      break;
    case 2: 
    case 3: 
      setExactNum(paramInt1, paramInt2, localISqlDataWrapper.getExactNumber());
      break;
    case 6: 
    case 8: 
      setDouble(paramInt1, paramInt2, localISqlDataWrapper.getDouble());
      break;
    case 7: 
      setReal(paramInt1, paramInt2, localISqlDataWrapper.getReal());
      break;
    case -7: 
    case 16: 
      setBoolean(paramInt1, paramInt2, localISqlDataWrapper.getBoolean());
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      setString(paramInt1, paramInt2, localISqlDataWrapper.getChar());
      break;
    case -4: 
    case -3: 
    case -2: 
      setBytes(paramInt1, paramInt2, localISqlDataWrapper.getBinary());
      break;
    case 91: 
      setDate(paramInt1, paramInt2, localISqlDataWrapper.getDate());
      break;
    case 92: 
      setTime(paramInt1, paramInt2, localISqlDataWrapper.getTime());
      break;
    case 93: 
      setTimestamp(paramInt1, paramInt2, localISqlDataWrapper.getTimestamp());
      break;
    case -11: 
      setGuid(paramInt1, paramInt2, localISqlDataWrapper.getGuid());
      break;
    case 4: 
      setInteger(paramInt1, paramInt2, (int)localISqlDataWrapper.getInteger());
      break;
    case 5: 
      setSmallInt(paramInt1, paramInt2, (short)localISqlDataWrapper.getSmallInt());
      break;
    case -6: 
      setTinyInt(paramInt1, paramInt2, (byte)localISqlDataWrapper.getTinyInt());
      break;
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    default: 
      throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + localISqlDataWrapper.getType());
    }
  }
  
  public long getRowSize()
  {
    return this.m_maxRowSize;
  }
  
  public long reduceMemoryUsage()
  {
    while (this.m_removedRows.contains(Integer.valueOf(this.m_maxRowNumber))) {
      this.m_removedRows.remove(Integer.valueOf(this.m_maxRowNumber--));
    }
    long l = this.m_memLimit;
    this.m_memLimit = getMemUsage();
    l -= this.m_memLimit;
    return l;
  }
  
  public void increaseMemLimit(long paramLong)
  {
    this.m_memLimit += paramLong;
  }
  
  public void writeRow(int paramInt, IRowView paramIRowView)
  {
    for (int i = 0; i < this.m_needsData.length; i++) {
      if (this.m_needsData[i] != 0) {
        if (paramIRowView.isNull(i))
        {
          setNull(paramInt, i);
        }
        else
        {
          IColumnSlice localIColumnSlice = getSlice(i, paramInt);
          switch (localIColumnSlice.getType())
          {
          case BIGINT: 
            localIColumnSlice.setBigInt(getSliceRowNum(paramInt), paramIRowView.getBigInt(i));
            break;
          case BINARY: 
            setBytes(paramInt, i, paramIRowView.getBytes(i));
            break;
          case BOOLEAN: 
            localIColumnSlice.setBoolean(getSliceRowNum(paramInt), paramIRowView.getBoolean(i));
            break;
          case CHAR: 
            setString(paramInt, i, paramIRowView.getString(i));
            break;
          case DATE: 
            localIColumnSlice.setDate(getSliceRowNum(paramInt), paramIRowView.getDate(i));
            break;
          case DOUBLE: 
            localIColumnSlice.setDouble(getSliceRowNum(paramInt), paramIRowView.getDouble(i));
            break;
          case EXACT_NUM: 
            localIColumnSlice.setExactNum(getSliceRowNum(paramInt), paramIRowView.getExactNumber(i));
            break;
          case FILE_MARKER: 
            localIColumnSlice.setFileMarker(getSliceRowNum(paramInt), paramIRowView.getFileMarker(i));
            break;
          case GUID: 
            localIColumnSlice.setGuid(getSliceRowNum(paramInt), paramIRowView.getGuid(i));
            break;
          case INTEGER: 
            localIColumnSlice.setInteger(getSliceRowNum(paramInt), paramIRowView.getInteger(i));
            break;
          case REAL: 
            localIColumnSlice.setReal(getSliceRowNum(paramInt), paramIRowView.getReal(i));
            break;
          case SMALLINT: 
            localIColumnSlice.setSmallInt(getSliceRowNum(paramInt), paramIRowView.getSmallInt(i));
            break;
          case TIME: 
            localIColumnSlice.setTime(getSliceRowNum(paramInt), paramIRowView.getTime(i));
            break;
          case TIMESTAMP: 
            localIColumnSlice.setTimestamp(getSliceRowNum(paramInt), paramIRowView.getTimestamp(i));
            break;
          case TINYINT: 
            localIColumnSlice.setTinyInt(getSliceRowNum(paramInt), paramIRowView.getTinyInt(i));
            break;
          default: 
            throw new IllegalStateException("Unknown column slice type.");
          }
        }
      }
    }
  }
  
  public boolean isRowInTable(int paramInt)
  {
    return (paramInt <= this.m_maxRowNumber) && (!this.m_removedRows.contains(Integer.valueOf(paramInt)));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/InMemTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */