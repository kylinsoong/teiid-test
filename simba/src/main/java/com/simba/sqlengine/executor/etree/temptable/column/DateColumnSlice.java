package com.simba.sqlengine.executor.etree.temptable.column;

import java.sql.Date;

public final class DateColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 6449117822981500182L;
  private Date[] m_data;
  private final int m_columnNumber;
  
  public DateColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new Date[paramInt1];
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
  
  public Date getDate(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public void setDate(int paramInt, Date paramDate)
  {
    this.m_data[paramInt] = paramDate;
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.DATE;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    DateColumnSlice localDateColumnSlice = (DateColumnSlice)paramIColumnSlice;
    System.arraycopy(localDateColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/DateColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */