package com.simba.sqlengine.executor.etree.temptable.column;

import java.math.BigDecimal;

public final class BooleanColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 8879043950764226178L;
  private boolean[] m_data;
  private int m_size;
  private final int m_columnNumber;
  
  public BooleanColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_size = paramInt1;
    this.m_data = new boolean[paramInt1 * 2];
    this.m_columnNumber = paramInt2;
  }
  
  public int columnNumber()
  {
    return this.m_columnNumber;
  }
  
  public boolean isNull(int paramInt)
  {
    return this.m_data[(2 * paramInt + 1)];
  }
  
  public void setNull(int paramInt)
  {
    this.m_data[(2 * paramInt + 1)] = true;
  }
  
  public boolean getBoolean(int paramInt)
  {
    return this.m_data[(2 * paramInt)];
  }
  
  public long getBigInt(int paramInt)
  {
    return getBoolean(paramInt) ? 1L : 0L;
  }
  
  public BigDecimal getExactNum(int paramInt)
  {
    return getBoolean(paramInt) ? BigDecimal.ONE : BigDecimal.ZERO;
  }
  
  public double getDouble(int paramInt)
  {
    return getBoolean(paramInt) ? 1.0D : 0.0D;
  }
  
  public float getReal(int paramInt)
  {
    return getBoolean(paramInt) ? 1.0F : 0.0F;
  }
  
  public int getInteger(int paramInt)
  {
    return getBoolean(paramInt) ? 1 : 0;
  }
  
  public short getSmallInt(int paramInt)
  {
    return getBoolean(paramInt) ? 1 : 0;
  }
  
  public byte getTinyInt(int paramInt)
  {
    return getBoolean(paramInt) ? 1 : 0;
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.BOOLEAN;
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    this.m_data[(2 * paramInt)] = paramBoolean;
    this.m_data[(2 * paramInt + 1)] = false;
  }
  
  public int size()
  {
    return this.m_size;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!(paramIColumnSlice instanceof BooleanColumnSlice)) {
      throw new IllegalArgumentException("Unknown column slice type");
    }
    BooleanColumnSlice localBooleanColumnSlice = (BooleanColumnSlice)paramIColumnSlice;
    System.arraycopy(localBooleanColumnSlice.m_data, paramInt1 * 2, this.m_data, paramInt3 * 2, paramInt2 * 2);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 2 * paramJavaSize.getBooleanSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/BooleanColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */