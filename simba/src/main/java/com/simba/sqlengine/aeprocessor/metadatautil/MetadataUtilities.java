package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.utils.ScalarFunctionArgType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class MetadataUtilities
{
  private MetadataUtilities()
  {
    throw new UnsupportedOperationException("Not instantiable.");
  }
  
  public static ColumnMetadata defaultColumnMetadata()
  {
    try
    {
      TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(4);
      return new ColumnMetadata(localTypeMetadata);
    }
    catch (ErrorException localErrorException)
    {
      throw new RuntimeException(localErrorException);
    }
  }
  
  public static TypeMetadata createTypeMetadata(IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(paramIColumnInfo.getType(), paramIColumnInfo.isSigned());
    localTypeMetadata.setPrecision(paramIColumnInfo.getPrecision());
    localTypeMetadata.setScale(paramIColumnInfo.getScale());
    localTypeMetadata.setIntervalPrecision(paramIColumnInfo.getIntervalPrecision());
    localTypeMetadata.setIsCurrency(paramIColumnInfo.isCurrency());
    localTypeMetadata.setIsSortable(paramIColumnInfo.isSortable());
    return localTypeMetadata;
  }
  
  public static ColumnMetadata createColumnMetadata(IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata = createTypeMetadata(paramIColumnInfo);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
    localColumnMetadata.setAutoUnique(paramIColumnInfo.isAutoUnique());
    localColumnMetadata.setCaseSensitive(paramIColumnInfo.isCaseSensitive());
    try
    {
      localColumnMetadata.setColumnLength(paramIColumnInfo.getColumnLength());
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw SQLEngineExceptionFactory.numericOverflowException("Column length is too long.");
    }
    localColumnMetadata.setNullable(paramIColumnInfo.getNullable());
    localColumnMetadata.setSearchable(paramIColumnInfo.getSearchable());
    localColumnMetadata.setUpdatable(paramIColumnInfo.getUpdatable());
    return localColumnMetadata;
  }
  
  public static boolean isConversionLegal(int paramInt, ScalarFunctionArgType paramScalarFunctionArgType)
    throws ErrorException
  {
    switch (paramScalarFunctionArgType)
    {
    case FN_ARG_ANY: 
      return true;
    case FN_ARG_DATE: 
      return AEUtils.isAnyConversionLegal(paramInt, 91, new int[] { 93, 1, 12, -8, -9 });
    case FN_ARG_FLOAT: 
      return AEUtils.isAnyConversionLegal(paramInt, 6, new int[] { 7 });
    case FN_ARG_INTEGER: 
      return AEUtils.isAnyConversionLegal(paramInt, 4, new int[] { -6, 5, -5 });
    case FN_ARG_INTERVAL: 
      return AEUtils.isAnyConversionLegal(paramInt, 103, new int[] { 108, 109, 110, 104, 111, 112, 105, 113, 102, 106, 101, 107, 1, 12, -8, -9 });
    case FN_ARG_NUMERIC: 
      return AEUtils.isAnyConversionLegal(paramInt, 2, new int[] { 3 });
    case FN_ARG_SLC: 
      return AEUtils.isAnyConversionLegal(paramInt, 2, new int[] { -6, 5, 4 });
    case FN_ARG_STRING: 
      return AEUtils.isAnyConversionLegal(paramInt, 1, new int[] { 12, -1, -8, -9, -10 });
    case FN_ARG_TIME: 
      return AEUtils.isAnyConversionLegal(paramInt, 92, new int[] { 93, 1, 12, -8, -9 });
    case FN_ARG_TIMESTAMP: 
      return AEUtils.isAnyConversionLegal(paramInt, 93, new int[] { 92, 91, 1, 12, -8, -9 });
    }
    throw new IllegalArgumentException("" + paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/MetadataUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */