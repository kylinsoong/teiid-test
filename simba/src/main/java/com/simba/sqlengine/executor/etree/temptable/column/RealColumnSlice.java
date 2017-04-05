package com.simba.sqlengine.executor.etree.temptable.column;

public final class RealColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = -1486808935311312641L;
  private float[] m_data;
  private byte[] m_nullInd;
  private final int m_columnNumber;
  
  public RealColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new float[paramInt1];
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
  
  public float getReal(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public double getDouble(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.REAL;
  }
  
  public void setReal(int paramInt, float paramFloat)
  {
    this.m_data[paramInt] = paramFloat;
    BitsUtil.clearBit(this.m_nullInd, paramInt);
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    RealColumnSlice localRealColumnSlice = (RealColumnSlice)paramIColumnSlice;
    System.arraycopy(localRealColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
    BitsUtil.copy(this.m_nullInd, paramInt3, paramInt2, localRealColumnSlice.m_nullInd, paramInt1);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getFloatSize() + paramJavaSize.getByteSize() / 8.0D;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/RealColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */