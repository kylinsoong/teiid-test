package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;

public final class ColumnSizeCalculator
{
  public static final JavaSize JAVA_SIZE = ;
  
  public static double getColumnSizePerRow(IColumn paramIColumn, boolean paramBoolean, int paramInt)
  {
    if (!paramBoolean) {
      return 0.0D;
    }
    TypeMetadata localTypeMetadata = paramIColumn.getTypeMetadata();
    JavaSize localJavaSize = JAVA_SIZE;
    long l;
    switch (localTypeMetadata.getType())
    {
    case -5: 
      return BigIntColumnSlice.estimateRowSize(localJavaSize);
    case -4: 
    case -3: 
    case -2: 
      l = paramIColumn.getColumnLength();
      if (!isLongData(paramIColumn, paramInt)) {
        return BinaryColumnSlice.estimateRowSize(localJavaSize, (int)l);
      }
      return FileMarkerColumnSlice.estimateRowSize(localJavaSize);
    case -7: 
    case 16: 
      return BooleanColumnSlice.estimateRowSize(localJavaSize);
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      l = paramIColumn.getColumnLength();
      if (!isLongData(paramIColumn, paramInt)) {
        return CharColumnSlice.estimateRowSize(localJavaSize, (int)l);
      }
      return FileMarkerColumnSlice.estimateRowSize(localJavaSize);
    case 91: 
      return DateColumnSlice.estimateRowSize(localJavaSize);
    case 2: 
    case 3: 
      return ExactNumColumnSlice.estimateRowSize(localJavaSize);
    case 6: 
    case 8: 
      return DoubleColumnSlice.estimateRowSize(localJavaSize);
    case 4: 
      return IntegerColumnSlice.estimateRowSize(localJavaSize);
    case 7: 
      return RealColumnSlice.estimateRowSize(localJavaSize);
    case 5: 
      return SmallIntColumnSlice.estimateRowSize(localJavaSize);
    case 92: 
      return TimeColumnSlice.estimateRowSize(localJavaSize);
    case 93: 
      return TimestampColumnSlice.estimateRowSize(localJavaSize);
    case -6: 
      return TinyIntColumnSlice.estimateRowSize(localJavaSize);
    case -11: 
      return GuidColumnSlice.estimateRowSize(localJavaSize);
    }
    throw new UnsupportedOperationException(localTypeMetadata.getTypeName() + " (" + localTypeMetadata.getType() + ")");
  }
  
  public static double getOverHeadPerRow(IColumn paramIColumn, boolean paramBoolean, int paramInt)
  {
    if (!paramBoolean) {
      return 0.0D;
    }
    TypeMetadata localTypeMetadata = paramIColumn.getTypeMetadata();
    JavaSize localJavaSize = JAVA_SIZE;
    switch (localTypeMetadata.getType())
    {
    case -5: 
      return BigIntColumnSlice.estimateRowOverhead(localJavaSize);
    case -4: 
    case -3: 
    case -2: 
      if (!isLongData(paramIColumn, paramInt)) {
        return BinaryColumnSlice.estimateRowOverhead(localJavaSize);
      }
      return FileMarkerColumnSlice.estimateRowOverhead(localJavaSize);
    case -7: 
    case 16: 
      return BooleanColumnSlice.estimateRowOverhead(localJavaSize);
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      if (!isLongData(paramIColumn, paramInt)) {
        return CharColumnSlice.estimateRowOverhead(localJavaSize);
      }
      return FileMarkerColumnSlice.estimateRowOverhead(localJavaSize);
    case 91: 
      return DateColumnSlice.estimateRowOverhead(localJavaSize);
    case 2: 
    case 3: 
      return ExactNumColumnSlice.estimateRowOverhead(localJavaSize);
    case 6: 
    case 8: 
      return DoubleColumnSlice.estimateRowOverhead(localJavaSize);
    case 4: 
      return IntegerColumnSlice.estimateRowOverhead(localJavaSize);
    case 7: 
      return RealColumnSlice.estimateRowOverhead(localJavaSize);
    case 5: 
      return SmallIntColumnSlice.estimateRowOverhead(localJavaSize);
    case 92: 
      return TimeColumnSlice.estimateRowOverhead(localJavaSize);
    case 93: 
      return TimestampColumnSlice.estimateRowOverhead(localJavaSize);
    case -6: 
      return TinyIntColumnSlice.estimateRowOverhead(localJavaSize);
    case -11: 
      return GuidColumnSlice.estimateRowOverhead(localJavaSize);
    }
    throw new UnsupportedOperationException(localTypeMetadata.getTypeName() + " (" + localTypeMetadata.getType() + ")");
  }
  
  public static boolean isLongData(IColumn paramIColumn, int paramInt)
  {
    if (!paramIColumn.getTypeMetadata().isCharacterOrBinaryType()) {
      return false;
    }
    long l = paramIColumn.getColumnLength();
    if (paramIColumn.getTypeMetadata().isCharacterType()) {
      l *= 2L;
    }
    return l > paramInt;
  }
  
  private static JavaSize init32VmSize()
  {
    JavaSize localJavaSize = new JavaSize(null);
    localJavaSize.m_objectShellSize = 8;
    localJavaSize.m_objectRefSize = 8;
    localJavaSize.m_longSize = 8;
    localJavaSize.m_intSize = 4;
    localJavaSize.m_shortSize = 2;
    localJavaSize.m_charSize = 2;
    localJavaSize.m_byteSize = 1;
    localJavaSize.m_booleanSize = 1;
    localJavaSize.m_doubleSize = 8;
    localJavaSize.m_floatSize = 4;
    return localJavaSize;
  }
  
  public static class JavaSize
  {
    private int m_objectShellSize;
    private int m_objectRefSize;
    private int m_booleanSize;
    private int m_charSize;
    private int m_byteSize;
    private int m_shortSize;
    private int m_intSize;
    private int m_longSize;
    private int m_floatSize;
    private int m_doubleSize;
    
    public int getObjectShellSize()
    {
      return this.m_objectShellSize;
    }
    
    public int getObjectRefSize()
    {
      return this.m_objectRefSize;
    }
    
    public int getBooleanSize()
    {
      return this.m_booleanSize;
    }
    
    public int getCharSize()
    {
      return this.m_charSize;
    }
    
    public int getByteSize()
    {
      return this.m_byteSize;
    }
    
    public int getShortSize()
    {
      return this.m_shortSize;
    }
    
    public int getIntSize()
    {
      return this.m_intSize;
    }
    
    public int getLongSize()
    {
      return this.m_longSize;
    }
    
    public int getFloatSize()
    {
      return this.m_floatSize;
    }
    
    public int getDoubleSize()
    {
      return this.m_doubleSize;
    }
    
    public int estimateStringSize(int paramInt)
    {
      assert (0 <= paramInt);
      int i = getObjectShellSize();
      i += 3 * getIntSize();
      i += getObjectRefSize();
      i += estimateArraySize(getCharSize(), paramInt);
      return i;
    }
    
    public int estimateArraySize(int paramInt1, int paramInt2)
    {
      assert (0 <= paramInt2);
      return getObjectShellSize() + getObjectRefSize() + getIntSize() + paramInt2 * paramInt1;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/ColumnSizeCalculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */