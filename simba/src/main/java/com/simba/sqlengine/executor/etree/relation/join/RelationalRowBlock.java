package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.IRowBlock;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceArray;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceBuilder;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice.ColumnSliceType;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.UUID;

public class RelationalRowBlock
  implements IRowBlock
{
  private final ETRelationalExpr m_wrapped;
  private final TemporaryFile m_longDataStore;
  private final ColumnSliceArray m_row;
  private final int m_maxDataSize;
  private final int m_currentRow = 0;
  
  public RelationalRowBlock(ETRelationalExpr paramETRelationalExpr, TemporaryFile paramTemporaryFile, int paramInt, boolean[] paramArrayOfBoolean)
  {
    this.m_wrapped = paramETRelationalExpr;
    this.m_longDataStore = paramTemporaryFile;
    this.m_maxDataSize = paramInt;
    IColumn[] arrayOfIColumn = new IColumn[this.m_wrapped.getColumnCount()];
    for (int i = 0; i < this.m_wrapped.getColumnCount(); i++) {
      arrayOfIColumn[i] = this.m_wrapped.getColumn(i);
    }
    this.m_row = ColumnSliceBuilder.buildColumnSliceArray(arrayOfIColumn, paramArrayOfBoolean, 1, this.m_maxDataSize);
  }
  
  public void close() {}
  
  public boolean isNull(int paramInt)
  {
    return this.m_row.get(paramInt).isNull(0);
  }
  
  public long getBigInt(int paramInt)
  {
    return this.m_row.get(paramInt).getBigInt(0);
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    return this.m_row.get(paramInt).getExactNum(0);
  }
  
  public double getDouble(int paramInt)
  {
    return this.m_row.get(paramInt).getDouble(0);
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    return this.m_row.get(paramInt).getFileMarker(0);
  }
  
  public float getReal(int paramInt)
  {
    return this.m_row.get(paramInt).getReal(0);
  }
  
  public boolean getBoolean(int paramInt)
  {
    return this.m_row.get(paramInt).getBoolean(0);
  }
  
  public String getString(int paramInt)
  {
    return this.m_row.get(paramInt).getString(0);
  }
  
  public Date getDate(int paramInt)
  {
    return this.m_row.get(paramInt).getDate(0);
  }
  
  public Time getTime(int paramInt)
  {
    return this.m_row.get(paramInt).getTime(0);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    return this.m_row.get(paramInt).getTimestamp(0);
  }
  
  public UUID getGuid(int paramInt)
  {
    return this.m_row.get(paramInt).getGuid(0);
  }
  
  public int getInteger(int paramInt)
  {
    return this.m_row.get(paramInt).getInteger(0);
  }
  
  public short getSmallInt(int paramInt)
  {
    return this.m_row.get(paramInt).getSmallInt(0);
  }
  
  public byte getTinyInt(int paramInt)
  {
    return this.m_row.get(paramInt).getTinyInt(0);
  }
  
  public byte[] getBytes(int paramInt)
  {
    return this.m_row.get(paramInt).getBytes(0);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_wrapped.getColumn(paramInt);
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    boolean bool = this.m_wrapped.move();
    if (bool) {
      loadRow();
    }
    return bool;
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_wrapped.reset();
  }
  
  private byte[] getLongData(ISqlDataWrapper paramISqlDataWrapper)
  {
    try
    {
      if (TypeUtilities.isCharacterType(paramISqlDataWrapper.getType())) {
        return DataRetrievalUtil.stringToBytes(paramISqlDataWrapper.getChar());
      }
      return paramISqlDataWrapper.getBinary();
    }
    catch (ErrorException localErrorException)
    {
      throw SQLEngineExceptionFactory.runtimeException(localErrorException);
    }
  }
  
  private void loadRow()
    throws ErrorException
  {
    Iterator localIterator = this.m_row.iterator();
    while (localIterator.hasNext())
    {
      IColumnSlice localIColumnSlice = (IColumnSlice)localIterator.next();
      ETDataRequest localETDataRequest = new ETDataRequest(0L, this.m_maxDataSize, this.m_wrapped.getColumn(localIColumnSlice.columnNumber()));
      if (IColumnSlice.ColumnSliceType.FILE_MARKER == localIColumnSlice.getType())
      {
        long l = 0L;
        boolean bool;
        do
        {
          localETDataRequest.setOffset(l);
          bool = this.m_wrapped.retrieveData(localIColumnSlice.columnNumber(), localETDataRequest);
          if (localETDataRequest.getData().isNull())
          {
            localIColumnSlice.setNull(0);
            break;
          }
          byte[] arrayOfByte = getLongData(localETDataRequest.getData());
          l += arrayOfByte.length;
          this.m_longDataStore.append(arrayOfByte);
        } while (bool);
        if (this.m_longDataStore.isAppending()) {
          localIColumnSlice.setFileMarker(0, this.m_longDataStore.generateFileMarker());
        }
      }
      else
      {
        this.m_wrapped.retrieveData(localIColumnSlice.columnNumber(), localETDataRequest);
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
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/RelationalRowBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */