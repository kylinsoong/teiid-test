package com.simba.sqlengine.executor.etree.temptable.column;

import java.math.BigDecimal;

public final class ExactNumColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 5227156909031151909L;
  private BigDecimal[] m_data;
  private final int m_columnNumber;
  
  public ExactNumColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new BigDecimal[paramInt1];
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
  
  public BigDecimal getExactNum(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.EXACT_NUM;
  }
  
  public void setExactNum(int paramInt, BigDecimal paramBigDecimal)
  {
    this.m_data[paramInt] = paramBigDecimal;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    ExactNumColumnSlice localExactNumColumnSlice = (ExactNumColumnSlice)paramIColumnSlice;
    System.arraycopy(localExactNumColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  public static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    int i = paramJavaSize.getObjectShellSize();
    i += paramJavaSize.getIntSize();
    i += 2 * paramJavaSize.getLongSize();
    i += paramJavaSize.getObjectRefSize();
    i += paramJavaSize.getObjectShellSize();
    i += 5 * paramJavaSize.getIntSize();
    i += paramJavaSize.getObjectRefSize();
    i += paramJavaSize.estimateArraySize(paramJavaSize.getIntSize(), 4);
    return i;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/ExactNumColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */