package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.util.CompressionUtil;

public final class SmallIntColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 3397388423848434484L;
  private short[] m_data;
  private byte[] m_nullInd;
  private final int m_columnNumber;
  private final boolean m_isSigned;
  
  public SmallIntColumnSlice(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.m_data = new short[paramInt1];
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
  
  public short getSmallInt(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public long getBigInt(int paramInt)
  {
    return CompressionUtil.getSmallIntAsInteger(this.m_data[paramInt], this.m_isSigned);
  }
  
  public double getDouble(int paramInt)
  {
    return CompressionUtil.getSmallIntAsInteger(this.m_data[paramInt], this.m_isSigned);
  }
  
  public float getReal(int paramInt)
  {
    return CompressionUtil.getSmallIntAsInteger(this.m_data[paramInt], this.m_isSigned);
  }
  
  public int getInteger(int paramInt)
  {
    return CompressionUtil.getSmallIntAsInteger(this.m_data[paramInt], this.m_isSigned);
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.SMALLINT;
  }
  
  public void setSmallInt(int paramInt, short paramShort)
  {
    this.m_data[paramInt] = paramShort;
    BitsUtil.clearBit(this.m_nullInd, paramInt);
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    SmallIntColumnSlice localSmallIntColumnSlice = (SmallIntColumnSlice)paramIColumnSlice;
    System.arraycopy(localSmallIntColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
    BitsUtil.copy(this.m_nullInd, paramInt3, paramInt2, localSmallIntColumnSlice.m_nullInd, paramInt1);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return 0.0D;
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getShortSize() + paramJavaSize.getByteSize() / 8.0D;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/SmallIntColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */