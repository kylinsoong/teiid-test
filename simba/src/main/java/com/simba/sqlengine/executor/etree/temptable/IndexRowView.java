package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public final class IndexRowView
  implements IRowView
{
  private int m_rowNum = -1;
  private final InMemTable m_table;
  
  public IndexRowView(InMemTable paramInMemTable)
  {
    this.m_table = paramInMemTable;
  }
  
  public void setRowNum(int paramInt)
  {
    this.m_rowNum = paramInt;
  }
  
  public boolean isNull(int paramInt)
  {
    return this.m_table.isNull(this.m_rowNum, paramInt);
  }
  
  public long getBigInt(int paramInt)
  {
    return this.m_table.getBigInt(this.m_rowNum, paramInt);
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    return this.m_table.getExactNum(this.m_rowNum, paramInt);
  }
  
  public double getDouble(int paramInt)
  {
    return this.m_table.getDouble(this.m_rowNum, paramInt);
  }
  
  public float getReal(int paramInt)
  {
    return this.m_table.getReal(this.m_rowNum, paramInt);
  }
  
  public boolean getBoolean(int paramInt)
  {
    return this.m_table.getBoolean(this.m_rowNum, paramInt);
  }
  
  public String getString(int paramInt)
  {
    return this.m_table.getString(this.m_rowNum, paramInt);
  }
  
  public Date getDate(int paramInt)
  {
    return this.m_table.getDate(this.m_rowNum, paramInt);
  }
  
  public Time getTime(int paramInt)
  {
    return this.m_table.getTime(this.m_rowNum, paramInt);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    return this.m_table.getTimestamp(this.m_rowNum, paramInt);
  }
  
  public UUID getGuid(int paramInt)
  {
    return this.m_table.getGuid(this.m_rowNum, paramInt);
  }
  
  public int getInteger(int paramInt)
  {
    return this.m_table.getInteger(this.m_rowNum, paramInt);
  }
  
  public short getSmallInt(int paramInt)
  {
    return this.m_table.getSmallInt(this.m_rowNum, paramInt);
  }
  
  public byte getTinyInt(int paramInt)
  {
    return this.m_table.getTinyInt(this.m_rowNum, paramInt);
  }
  
  public byte[] getBytes(int paramInt)
  {
    return this.m_table.getBytes(this.m_rowNum, paramInt);
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    return this.m_table.getFileMarker(this.m_rowNum, paramInt);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_table.getColumn(paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/IndexRowView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */