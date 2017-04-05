package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.LongDataStore;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceArray;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceBuilder;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice.ColumnSliceType;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

class HHJoinDataSource
  extends ETRelationalExpr
  implements IRowView
{
  private final boolean m_isRowFile;
  private final LongDataStore m_longDataStore;
  private ETRelationalExpr m_relation = null;
  private final IColumn[] m_columns;
  private RowFile m_file = null;
  private boolean m_isOpen;
  private ColumnSliceArray m_row;
  private int m_maxDataSize;
  private boolean[] m_isLongData;
  
  public HHJoinDataSource(ETRelationalExpr paramETRelationalExpr, LongDataStore paramLongDataStore, boolean[] paramArrayOfBoolean, IColumn[] paramArrayOfIColumn, int paramInt)
  {
    super(paramArrayOfBoolean);
    this.m_isRowFile = false;
    this.m_longDataStore = paramLongDataStore;
    this.m_relation = paramETRelationalExpr;
    this.m_columns = paramArrayOfIColumn;
    this.m_isOpen = false;
    this.m_maxDataSize = paramInt;
    this.m_row = ColumnSliceBuilder.buildColumnSliceArray(paramArrayOfIColumn, paramArrayOfBoolean, 1, this.m_maxDataSize);
    setIsLongData();
  }
  
  public HHJoinDataSource(LongDataStore paramLongDataStore, boolean[] paramArrayOfBoolean, IColumn[] paramArrayOfIColumn, int paramInt, File paramFile, ILogger paramILogger)
    throws ErrorException
  {
    super(paramArrayOfBoolean);
    this.m_isRowFile = true;
    this.m_file = new RowFile(paramArrayOfIColumn, paramFile, paramILogger, paramInt, paramArrayOfBoolean);
    this.m_columns = paramArrayOfIColumn;
    this.m_longDataStore = paramLongDataStore;
    this.m_isOpen = false;
    this.m_maxDataSize = paramInt;
    setIsLongData();
  }
  
  public void close()
  {
    this.m_isOpen = false;
    if (this.m_isRowFile) {
      this.m_file.reset();
    }
  }
  
  public void destroy()
  {
    this.m_isOpen = false;
    if (this.m_isRowFile) {
      this.m_file.close();
    } else {
      close();
    }
  }
  
  public boolean isOpen()
  {
    return this.m_isOpen;
  }
  
  public void reset()
    throws ErrorException
  {
    if (this.m_isRowFile) {
      this.m_file.reset();
    } else {
      this.m_relation.reset();
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    throw new IllegalStateException();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isNull(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.isNull(paramInt);
    }
    return this.m_row.get(paramInt).isNull(0);
  }
  
  public long getBigInt(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getBigInt(paramInt);
    }
    return this.m_row.get(paramInt).getBigInt(0);
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getExactNumber(paramInt);
    }
    return this.m_row.get(paramInt).getExactNum(0);
  }
  
  public double getDouble(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getDouble(paramInt);
    }
    return this.m_row.get(paramInt).getDouble(0);
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getFileMarker(paramInt);
    }
    return this.m_row.get(paramInt).getFileMarker(0);
  }
  
  public float getReal(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getReal(paramInt);
    }
    return this.m_row.get(paramInt).getReal(0);
  }
  
  public boolean getBoolean(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getBoolean(paramInt);
    }
    return this.m_row.get(paramInt).getBoolean(0);
  }
  
  public String getString(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getString(paramInt);
    }
    return this.m_row.get(paramInt).getString(0);
  }
  
  public Date getDate(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getDate(paramInt);
    }
    return this.m_row.get(paramInt).getDate(0);
  }
  
  public Time getTime(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getTime(paramInt);
    }
    return this.m_row.get(paramInt).getTime(0);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getTimestamp(paramInt);
    }
    return this.m_row.get(paramInt).getTimestamp(0);
  }
  
  public UUID getGuid(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getGuid(paramInt);
    }
    return this.m_row.get(paramInt).getGuid(0);
  }
  
  public int getInteger(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getInteger(paramInt);
    }
    return this.m_row.get(paramInt).getInteger(0);
  }
  
  public short getSmallInt(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getSmallInt(paramInt);
    }
    return this.m_row.get(paramInt).getSmallInt(0);
  }
  
  public byte getTinyInt(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getTinyInt(paramInt);
    }
    return this.m_row.get(paramInt).getTinyInt(0);
  }
  
  public byte[] getBytes(int paramInt)
  {
    if (this.m_isRowFile) {
      return this.m_file.getBytes(paramInt);
    }
    return this.m_row.get(paramInt).getBytes(0);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_columns[paramInt];
  }
  
  public int getColumnCount()
  {
    return this.m_columns.length;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    if (this.m_isRowFile) {
      return this.m_file.getRowCount();
    }
    return this.m_relation.getRowCount();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_isOpen = true;
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (this.m_isRowFile) {
      return DataRetrievalUtil.retrieveFromRowView(paramInt, this.m_isLongData[paramInt], paramETDataRequest, this.m_file, this.m_longDataStore);
    }
    return this.m_relation.retrieveData(paramInt, paramETDataRequest);
  }
  
  public void writeRows(InMemTable paramInMemTable, List<Integer> paramList, int paramInt)
    throws ErrorException
  {
    if (this.m_isRowFile) {
      this.m_file.writeRows(paramInMemTable, paramList, paramInt);
    } else {
      throw new IllegalStateException();
    }
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    if (this.m_isRowFile) {
      return this.m_file.moveToNextRow();
    }
    boolean bool = this.m_relation.move();
    if (bool) {
      loadRow();
    }
    return bool;
  }
  
  private void loadRow()
    throws ErrorException
  {
    Iterator localIterator = this.m_row.iterator();
    while (localIterator.hasNext())
    {
      IColumnSlice localIColumnSlice = (IColumnSlice)localIterator.next();
      ETDataRequest localETDataRequest = new ETDataRequest(0L, this.m_maxDataSize, this.m_columns[localIColumnSlice.columnNumber()]);
      if (IColumnSlice.ColumnSliceType.FILE_MARKER == localIColumnSlice.getType())
      {
        TemporaryFile.FileMarker localFileMarker = this.m_longDataStore.put(localIColumnSlice.columnNumber(), this.m_relation);
        if (null == localFileMarker) {
          localIColumnSlice.setNull(0);
        } else {
          localIColumnSlice.setFileMarker(0, localFileMarker);
        }
      }
      else
      {
        this.m_relation.retrieveData(localIColumnSlice.columnNumber(), localETDataRequest);
        if (localETDataRequest.getData().isNull()) {
          localIColumnSlice.setNull(0);
        } else {
          switch (localIColumnSlice.getType())
          {
          case BIGINT: 
            localIColumnSlice.setBigInt(0, localETDataRequest.getData().getBigInt().longValue());
            break;
          case BINARY: 
            localIColumnSlice.setBytes(0, localETDataRequest.getData().getBinary());
            break;
          case BOOLEAN: 
            localIColumnSlice.setBoolean(0, localETDataRequest.getData().getBoolean());
            break;
          case CHAR: 
            localIColumnSlice.setString(0, localETDataRequest.getData().getChar());
            break;
          case DATE: 
            localIColumnSlice.setDate(0, localETDataRequest.getData().getDate());
            break;
          case DOUBLE: 
            localIColumnSlice.setDouble(0, localETDataRequest.getData().getDouble());
            break;
          case EXACT_NUM: 
            localIColumnSlice.setExactNum(0, localETDataRequest.getData().getExactNumber());
            break;
          case GUID: 
            localIColumnSlice.setGuid(0, localETDataRequest.getData().getGuid());
            break;
          case INTEGER: 
            localIColumnSlice.setInteger(0, (int)localETDataRequest.getData().getInteger());
            break;
          case REAL: 
            localIColumnSlice.setReal(0, localETDataRequest.getData().getReal());
            break;
          case SMALLINT: 
            localIColumnSlice.setSmallInt(0, (short)localETDataRequest.getData().getSmallInt());
            break;
          case TIME: 
            localIColumnSlice.setTime(0, localETDataRequest.getData().getTime());
            break;
          case TIMESTAMP: 
            localIColumnSlice.setTimestamp(0, localETDataRequest.getData().getTimestamp());
            break;
          case TINYINT: 
            localIColumnSlice.setTinyInt(0, (byte)localETDataRequest.getData().getTinyInt());
            break;
          default: 
            throw new IllegalStateException("Unknown ColumnSliceType.");
          }
        }
      }
    }
  }
  
  private void setIsLongData()
  {
    this.m_isLongData = new boolean[this.m_columns.length];
    for (int i = 0; i < this.m_columns.length; i++)
    {
      boolean bool = ColumnSizeCalculator.isLongData(this.m_columns[i], this.m_maxDataSize);
      this.m_isLongData[i] = bool;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/HHJoinDataSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */