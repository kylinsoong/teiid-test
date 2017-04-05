package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class BlockConverter
{
  private InMemTable m_table;
  private int m_curRow;
  
  public BlockConverter(IColumn[] paramArrayOfIColumn, TemporaryTableBuilder.TemporaryTableProperties paramTemporaryTableProperties, ILogger paramILogger, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    int i = (int)(paramTemporaryTableProperties.m_blockSize / paramTemporaryTableProperties.m_rowSize / 10L) + 1;
    this.m_table = new InMemTable(paramArrayOfIColumn, paramTemporaryTableProperties.m_maxDataLen, i, paramArrayOfBoolean, paramILogger);
    this.m_curRow = -1;
  }
  
  public boolean appendRow()
  {
    this.m_curRow = this.m_table.appendRow();
    return this.m_curRow >= 0;
  }
  
  public void setNull(int paramInt)
  {
    this.m_table.setNull(this.m_curRow, paramInt);
  }
  
  public void setBigInt(int paramInt, long paramLong)
  {
    this.m_table.setBigInt(this.m_curRow, paramInt, paramLong);
  }
  
  public void setExactNum(int paramInt, BigDecimal paramBigDecimal)
  {
    this.m_table.setExactNum(this.m_curRow, paramInt, paramBigDecimal);
  }
  
  public void setDouble(int paramInt, double paramDouble)
  {
    this.m_table.setDouble(this.m_curRow, paramInt, paramDouble);
  }
  
  public void setReal(int paramInt, float paramFloat)
  {
    this.m_table.setReal(this.m_curRow, paramInt, paramFloat);
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    this.m_table.setBoolean(this.m_curRow, paramInt, paramBoolean);
  }
  
  public void setString(int paramInt, String paramString)
  {
    this.m_table.setString(this.m_curRow, paramInt, paramString);
  }
  
  public void setDate(int paramInt, Date paramDate)
  {
    this.m_table.setDate(this.m_curRow, paramInt, paramDate);
  }
  
  public void setTime(int paramInt, Time paramTime)
  {
    this.m_table.setTime(this.m_curRow, paramInt, paramTime);
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp)
  {
    this.m_table.setTimestamp(this.m_curRow, paramInt, paramTimestamp);
  }
  
  public void setFileMarker(int paramInt, TemporaryFile.FileMarker paramFileMarker)
  {
    this.m_table.setFileMarker(this.m_curRow, paramInt, paramFileMarker);
  }
  
  public void setGuid(int paramInt, UUID paramUUID)
  {
    this.m_table.setGuid(this.m_curRow, paramInt, paramUUID);
  }
  
  public void setInteger(int paramInt1, int paramInt2)
  {
    this.m_table.setInteger(this.m_curRow, paramInt1, paramInt2);
  }
  
  public void setSmallInt(int paramInt, short paramShort)
  {
    this.m_table.setSmallInt(this.m_curRow, paramInt, paramShort);
  }
  
  public void setTinyInt(int paramInt, byte paramByte)
  {
    this.m_table.setTinyInt(this.m_curRow, paramInt, paramByte);
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
  {
    this.m_table.setBytes(this.m_curRow, paramInt, paramArrayOfByte);
  }
  
  public RowBlock toRowBlock()
  {
    this.m_curRow = -1;
    RowBlock localRowBlock = this.m_table.toRowBlock();
    this.m_table.clear();
    return localRowBlock;
  }
  
  public int getNumRows()
  {
    return this.m_table.getNumRows();
  }
  
  public void setMemLimit(long paramLong)
  {
    this.m_table.setMemLimit(paramLong);
  }
  
  public int getMemOverhead()
  {
    return this.m_table.getMemOverhead();
  }
  
  public void reset()
  {
    this.m_table.clear();
    this.m_table.setMemLimit(0L);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/BlockConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */