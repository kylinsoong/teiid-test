package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;

public class ColumnSliceBuilder
{
  public static ColumnSliceArray buildColumnSliceArray(IColumn[] paramArrayOfIColumn, boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = new int[paramArrayOfBoolean.length];
    int i = 0;
    for (int j = 0; j < paramArrayOfBoolean.length; j++) {
      if (paramArrayOfBoolean[j] != 0)
      {
        arrayOfInt[j] = i;
        i++;
      }
      else
      {
        arrayOfInt[j] = -1;
      }
    }
    IColumnSlice[] arrayOfIColumnSlice = new IColumnSlice[i];
    for (int k = 0; k < arrayOfInt.length; k++) {
      if (arrayOfInt[k] >= 0) {
        arrayOfIColumnSlice[arrayOfInt[k]] = createColumnSlice(paramArrayOfIColumn[k], k, paramInt1, paramInt2);
      }
    }
    return new ColumnSliceArray(arrayOfIColumnSlice, arrayOfInt, paramInt1);
  }
  
  private static IColumnSlice createColumnSlice(IColumn paramIColumn, int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramIColumn.getTypeMetadata().getType())
    {
    case -7: 
    case 16: 
      return new BooleanColumnSlice(paramInt2, paramInt1);
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      if (ColumnSizeCalculator.isLongData(paramIColumn, paramInt3)) {
        return new FileMarkerColumnSlice(paramInt2, paramInt1);
      }
      return new CharColumnSlice(paramInt2, paramInt1);
    case -4: 
    case -3: 
    case -2: 
      if (ColumnSizeCalculator.isLongData(paramIColumn, paramInt3)) {
        return new FileMarkerColumnSlice(paramInt2, paramInt1);
      }
      return new BinaryColumnSlice(paramInt2, paramInt1);
    case -5: 
      return new BigIntColumnSlice(paramInt2, paramInt1);
    case 4: 
      return new IntegerColumnSlice(paramInt2, paramInt1, paramIColumn.getTypeMetadata().isSigned());
    case 5: 
      return new SmallIntColumnSlice(paramInt2, paramInt1, paramIColumn.getTypeMetadata().isSigned());
    case -6: 
      return new TinyIntColumnSlice(paramInt2, paramInt1, paramIColumn.getTypeMetadata().isSigned());
    case 7: 
      return new RealColumnSlice(paramInt2, paramInt1);
    case 6: 
    case 8: 
      return new DoubleColumnSlice(paramInt2, paramInt1);
    case -11: 
      return new GuidColumnSlice(paramInt2, paramInt1);
    case 2: 
    case 3: 
      return new ExactNumColumnSlice(paramInt2, paramInt1);
    case 91: 
      return new DateColumnSlice(paramInt2, paramInt1);
    case 92: 
      return new TimeColumnSlice(paramInt2, paramInt1);
    case 93: 
      return new TimestampColumnSlice(paramInt2, paramInt1);
    }
    throw new UnsupportedOperationException("unknown columnslice type to create.");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/ColumnSliceBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */