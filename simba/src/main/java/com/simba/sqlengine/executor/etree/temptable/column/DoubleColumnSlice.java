package com.simba.sqlengine.executor.etree.temptable.column;

public final class DoubleColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 40172364764569598L;
  private double[] m_data;
  private byte[] m_nullInd;
  private final int m_columnNumber;
  
  public DoubleColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new double[paramInt1];
    this.m_nullInd = new byte[(paramInt1 + 7) / 8];
    this.m_columnNumber = paramInt2;
  }
  
  public int columnNumber()
  {
    return this.m_columnNumber;
  }
  
  public boolean isNull(int paramInt)
  {
    return BitsUtil.isSet(this.m_nullInd, paramInt);
  }
  
  public void setNull(int paramInt)
  {
    BitsUtil.setBit(this.m_nullInd, paramInt);
  }
  
  public double getDouble(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.DOUBLE;
  }
  
  public void setDouble(int paramInt, double paramDouble)
  {
    this.m_data[paramInt] = paramDouble;
    BitsUtil.clearBit(this.m_nullInd, paramInt);
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    DoubleColumnSlice localDoubleColumnSlice = (DoubleColumnSlice)paramIColumnSlice;
    System.arraycopy(localDoubleColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
    BitsUtil.copy(this.m_nullInd, paramInt3, paramInt2, localDoubleColumnSlice.m_nullInd, paramInt1);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getDoubleSize() + paramJavaSize.getByteSize() / 8.0D;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/DoubleColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */