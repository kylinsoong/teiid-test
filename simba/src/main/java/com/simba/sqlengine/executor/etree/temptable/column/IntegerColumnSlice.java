package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.util.CompressionUtil;

public final class IntegerColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = -3626818452823112157L;
  private int[] m_data;
  private byte[] m_nullInd;
  private final int m_columnNumber;
  private final boolean m_isSigned;
  
  public IntegerColumnSlice(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.m_data = new int[paramInt1];
    this.m_nullInd = new byte[(paramInt1 + 8 - 1) / 8];
    this.m_columnNumber = paramInt2;
    this.m_isSigned = paramBoolean;
  }
  
  public int columnNumber()
  {
    return this.m_columnNumber;
  }
  
  public boolean isNull(int paramInt)
  {
    return BitsUtil.isSet(this.m_nullInd, paramInt);
  }
  
  public int getInteger(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public long getBigInt(int paramInt)
  {
    return CompressionUtil.getIntAsLong(this.m_data[paramInt], this.m_isSigned);
  }
  
  public double getDouble(int paramInt)
  {
    return CompressionUtil.getIntAsLong(this.m_data[paramInt], this.m_isSigned);
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.INTEGER;
  }
  
  public void setInteger(int paramInt1, int paramInt2)
  {
    this.m_data[paramInt1] = paramInt2;
    BitsUtil.clearBit(this.m_nullInd, paramInt1);
  }
  
  public void setNull(int paramInt)
  {
    BitsUtil.setBit(this.m_nullInd, paramInt);
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!(paramIColumnSlice instanceof IntegerColumnSlice)) {
      throw new IllegalArgumentException("Unknown column slice type");
    }
    IntegerColumnSlice localIntegerColumnSlice = (IntegerColumnSlice)paramIColumnSlice;
    System.arraycopy(localIntegerColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
    BitsUtil.copy(this.m_nullInd, paramInt3, paramInt2, localIntegerColumnSlice.m_nullInd, paramInt1);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getIntSize() + paramJavaSize.getByteSize() / 8.0D;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/IntegerColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */