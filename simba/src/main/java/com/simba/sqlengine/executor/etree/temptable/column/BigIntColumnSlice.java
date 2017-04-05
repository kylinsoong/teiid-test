package com.simba.sqlengine.executor.etree.temptable.column;

public final class BigIntColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = -4060993706225601688L;
  private long[] m_data;
  private byte[] m_nullIndicators;
  private final int m_columnNumber;
  
  public BigIntColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new long[paramInt1];
    this.m_nullIndicators = new byte[(paramInt1 + 7) / 8];
    this.m_columnNumber = paramInt2;
  }
  
  public boolean isNull(int paramInt)
  {
    return BitsUtil.isSet(this.m_nullIndicators, paramInt);
  }
  
  public void setNull(int paramInt)
  {
    BitsUtil.setBit(this.m_nullIndicators, paramInt);
  }
  
  public long getBigInt(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.BIGINT;
  }
  
  public void setBigInt(int paramInt, long paramLong)
  {
    this.m_data[paramInt] = paramLong;
    BitsUtil.clearBit(this.m_nullIndicators, paramInt);
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!(paramIColumnSlice instanceof BigIntColumnSlice)) {
      throw new IllegalArgumentException("Unknown column slice type");
    }
    BigIntColumnSlice localBigIntColumnSlice = (BigIntColumnSlice)paramIColumnSlice;
    System.arraycopy(localBigIntColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
    BitsUtil.copy(this.m_nullIndicators, paramInt3, paramInt2, localBigIntColumnSlice.m_nullIndicators, paramInt1);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getLongSize() + paramJavaSize.getByteSize() / 8.0D;
  }
  
  public int columnNumber()
  {
    return this.m_columnNumber;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/BigIntColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */