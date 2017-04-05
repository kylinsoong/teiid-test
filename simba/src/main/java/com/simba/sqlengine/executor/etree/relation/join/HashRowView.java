package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

class HashRowView
  implements IRowView
{
  private final int[] m_columns;
  private IRowView m_wrapped;
  private final IColumn[] m_metadata;
  private final int[] m_hashColumns;
  
  public HashRowView(IColumn[] paramArrayOfIColumn, int[] paramArrayOfInt, IRowView paramIRowView)
  {
    this.m_columns = paramArrayOfInt;
    this.m_wrapped = paramIRowView;
    this.m_metadata = paramArrayOfIColumn;
    this.m_hashColumns = new int[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      this.m_hashColumns[i] = i;
    }
  }
  
  public int[] getHashColumns()
  {
    return this.m_hashColumns;
  }
  
  public void setRow(IRowView paramIRowView)
  {
    this.m_wrapped = paramIRowView;
  }
  
  public boolean isNull(int paramInt)
  {
    return this.m_wrapped.isNull(this.m_columns[paramInt]);
  }
  
  public long getBigInt(int paramInt)
  {
    return this.m_wrapped.getBigInt(this.m_columns[paramInt]);
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    return this.m_wrapped.getExactNumber(this.m_columns[paramInt]);
  }
  
  public double getDouble(int paramInt)
  {
    return this.m_wrapped.getDouble(this.m_columns[paramInt]);
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    return this.m_wrapped.getFileMarker(this.m_columns[paramInt]);
  }
  
  public float getReal(int paramInt)
  {
    return this.m_wrapped.getReal(this.m_columns[paramInt]);
  }
  
  public boolean getBoolean(int paramInt)
  {
    return this.m_wrapped.getBoolean(this.m_columns[paramInt]);
  }
  
  public String getString(int paramInt)
  {
    return this.m_wrapped.getString(this.m_columns[paramInt]);
  }
  
  public Date getDate(int paramInt)
  {
    return this.m_wrapped.getDate(this.m_columns[paramInt]);
  }
  
  public Time getTime(int paramInt)
  {
    return this.m_wrapped.getTime(this.m_columns[paramInt]);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    return this.m_wrapped.getTimestamp(this.m_columns[paramInt]);
  }
  
  public UUID getGuid(int paramInt)
  {
    return this.m_wrapped.getGuid(this.m_columns[paramInt]);
  }
  
  public int getInteger(int paramInt)
  {
    return this.m_wrapped.getInteger(this.m_columns[paramInt]);
  }
  
  public short getSmallInt(int paramInt)
  {
    return this.m_wrapped.getSmallInt(this.m_columns[paramInt]);
  }
  
  public byte getTinyInt(int paramInt)
  {
    return this.m_wrapped.getTinyInt(this.m_columns[paramInt]);
  }
  
  public byte[] getBytes(int paramInt)
  {
    return this.m_wrapped.getBytes(this.m_columns[paramInt]);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_metadata[paramInt];
  }
  
  public IRowView getWrapped()
  {
    return this.m_wrapped;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/HashRowView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */