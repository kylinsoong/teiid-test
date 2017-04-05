package com.simba.sqlengine.executor.etree.temptable.column;

import java.util.UUID;

public final class GuidColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = -3523299136239815995L;
  private UUID[] m_data;
  private final int m_columnNumber;
  
  public GuidColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new UUID[paramInt1];
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
  
  public UUID getGuid(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.GUID;
  }
  
  public void setGuid(int paramInt, UUID paramUUID)
  {
    this.m_data[paramInt] = paramUUID;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    GuidColumnSlice localGuidColumnSlice = (GuidColumnSlice)paramIColumnSlice;
    System.arraycopy(localGuidColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectShellSize() + 2 * paramJavaSize.getLongSize();
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/GuidColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */