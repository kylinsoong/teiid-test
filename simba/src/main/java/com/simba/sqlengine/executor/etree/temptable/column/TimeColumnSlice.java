package com.simba.sqlengine.executor.etree.temptable.column;

import java.sql.Time;

public final class TimeColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = -1382490667428191778L;
  private Time[] m_data;
  private final int m_columnNumber;
  
  public TimeColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new Time[paramInt1];
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
  
  public Time getTime(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.TIME;
  }
  
  public void setTime(int paramInt, Time paramTime)
  {
    this.m_data[paramInt] = paramTime;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    TimeColumnSlice localTimeColumnSlice = (TimeColumnSlice)paramIColumnSlice;
    System.arraycopy(localTimeColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectShellSize() + paramJavaSize.getLongSize() + paramJavaSize.getObjectRefSize();
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/TimeColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */