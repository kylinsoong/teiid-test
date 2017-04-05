package com.simba.sqlengine.executor.etree.temptable.column;

import java.sql.Timestamp;

public final class TimestampColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = -7615131358345588142L;
  private Timestamp[] m_data;
  private final int m_columnNumber;
  
  public TimestampColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new Timestamp[paramInt1];
    this.m_columnNumber = paramInt2;
  }
  
  public int columnNumber()
  {
    return this.m_columnNumber;
  }
  
  public boolean isNull(int paramInt)
  {
    return null == this.m_data[paramInt];
  }
  
  public void setNull(int paramInt)
  {
    this.m_data[paramInt] = null;
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.TIMESTAMP;
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp)
  {
    this.m_data[paramInt] = paramTimestamp;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    TimestampColumnSlice localTimestampColumnSlice = (TimestampColumnSlice)paramIColumnSlice;
    System.arraycopy(localTimestampColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectShellSize() + paramJavaSize.getLongSize() + paramJavaSize.getIntSize() + paramJavaSize.getObjectRefSize();
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/TimestampColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */