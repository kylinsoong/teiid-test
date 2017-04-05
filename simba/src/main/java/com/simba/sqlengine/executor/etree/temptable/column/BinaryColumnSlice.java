package com.simba.sqlengine.executor.etree.temptable.column;

public final class BinaryColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 2719920700780359307L;
  private byte[][] m_data;
  private final int m_columnNumber;
  
  public BinaryColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new byte[paramInt1][];
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
  
  public byte[] getBytes(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.BINARY;
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
  {
    this.m_data[paramInt] = paramArrayOfByte;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    BinaryColumnSlice localBinaryColumnSlice = (BinaryColumnSlice)paramIColumnSlice;
    System.arraycopy(localBinaryColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize, int paramInt)
  {
    return paramJavaSize.estimateArraySize(paramJavaSize.getByteSize(), paramInt);
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/BinaryColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */