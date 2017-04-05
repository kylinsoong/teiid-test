package com.simba.sqlengine.executor.etree.temptable.column;

public final class CharColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 2677650540629864998L;
  private String[] m_data;
  private final int m_columnNumber;
  
  public CharColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new String[paramInt1];
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
  
  public String getString(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.CHAR;
  }
  
  public void setString(int paramInt, String paramString)
  {
    this.m_data[paramInt] = paramString;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!(paramIColumnSlice instanceof CharColumnSlice)) {
      throw new IllegalArgumentException("Unknown column slice type");
    }
    CharColumnSlice localCharColumnSlice = (CharColumnSlice)paramIColumnSlice;
    System.arraycopy(localCharColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize, int paramInt)
  {
    return paramJavaSize.estimateStringSize(paramInt);
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/CharColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */