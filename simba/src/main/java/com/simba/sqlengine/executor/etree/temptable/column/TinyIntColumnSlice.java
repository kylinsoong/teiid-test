package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.util.CompressionUtil;

public final class TinyIntColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 6481926827866336384L;
  private byte[] m_data;
  private byte[] m_nullInd;
  private final int m_columnNumber;
  private final boolean m_isSigned;
  
  public TinyIntColumnSlice(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.m_data = new byte[paramInt1];
    this.m_nullInd = new byte[(paramInt1 + 7) / 8];
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
  
  public void setNull(int paramInt)
  {
    BitsUtil.setBit(this.m_nullInd, paramInt);
  }
  
  public byte getTinyInt(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public long getBigInt(int paramInt)
  {
    return CompressionUtil.getTinyIntAsShort(this.m_data[paramInt], this.m_isSigned);
  }
  
  public double getDouble(int paramInt)
  {
    return CompressionUtil.getTinyIntAsShort(this.m_data[paramInt], this.m_isSigned);
  }
  
  public float getReal(int paramInt)
  {
    return CompressionUtil.getTinyIntAsShort(this.m_data[paramInt], this.m_isSigned);
  }
  
  public int getInteger(int paramInt)
  {
    return CompressionUtil.getTinyIntAsShort(this.m_data[paramInt], this.m_isSigned);
  }
  
  public short getSmallInt(int paramInt)
  {
    return CompressionUtil.getTinyIntAsShort(this.m_data[paramInt], this.m_isSigned);
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.TINYINT;
  }
  
  public void setTinyInt(int paramInt, byte paramByte)
  {
    this.m_data[paramInt] = paramByte;
    BitsUtil.clearBit(this.m_nullInd, paramInt);
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    TinyIntColumnSlice localTinyIntColumnSlice = (TinyIntColumnSlice)paramIColumnSlice;
    System.arraycopy(localTinyIntColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
    BitsUtil.copy(this.m_nullInd, paramInt3, paramInt2, localTinyIntColumnSlice.m_nullInd, paramInt1);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 1.125D * paramJavaSize.getByteSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/TinyIntColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */