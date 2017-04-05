package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TypeUtilities
{
  public static final int BIT_DISPLAY_SIZE = 1;
  public static final int DOUBLE_FLOAT_DISPLAY_SIZE = 24;
  public static final int REAL_DISPLAY_SIZE = 14;
  public static final int BIGINT_DISPLAY_SIZE = 20;
  public static final int GUID_DISPLAY_SIZE = 36;
  public static final int UINT_DISPLAY_SIZE = 10;
  public static final int SINT_DISPLAY_SIZE = 11;
  public static final int USMALLINT_DISPLAY_SIZE = 5;
  public static final int SSMALLINT_DISPLAY_SIZE = 6;
  public static final int UTINYINT_DISPLAY_SIZE = 3;
  public static final int STINYINT_DISPLAY_SIZE = 4;
  public static final int DATE_DISPLAY_SIZE = 10;
  public static final int TIME_NO_FRAC_PREC_DISPLAY_SIZE = 8;
  public static final int TIME_WITH_FRAC_PREC_DISPLAY_SIZE = 9;
  public static final int TIMESTAMP_NO_FRAC_PREC_DISPLAY_SIZE = 19;
  public static final int TIMESTAMP_WITH_FRAC_PREC_DISPLAY_SIZE = 20;
  public static final int BIT_COLUMN_SIZE = 1;
  public static final int DATE_COLUMN_SIZE = 10;
  public static final int DOUBLE_FLOAT_COLUMN_SIZE = 15;
  public static final int GUID_COLUMN_SIZE = 36;
  public static final int INTEGER_COLUMN_SIZE = 10;
  public static final int REAL_COLUMN_SIZE = 7;
  public static final int SMALLINT_COLUMN_SIZE = 5;
  public static final int TINYINT_COLUMN_SIZE = 3;
  public static final int SBIGINT_COLUMN_SIZE = 19;
  public static final int UBIGINT_COLUMN_SIZE = 20;
  public static final int INTERVAL_YEAR_COLUMN_SIZE = 2;
  public static final int INTERVAL_YEAR_TO_MONTH_COLUMN_SIZE = 5;
  public static final int INTERVAL_MONTH_COLUMN_SIZE = 2;
  public static final int INTERVAL_DAY_COLUMN_SIZE = 2;
  public static final int INTERVAL_DAY_TO_HOUR_COLUMN_SIZE = 5;
  public static final int INTERVAL_DAY_TO_MINUTE_COLUMN_SIZE = 8;
  public static final int INTERVAL_DAY_TO_SECOND_COLUMN_SIZE = 18;
  public static final int INTERVAL_HOUR_COLUMN_SIZE = 2;
  public static final int INTERVAL_HOUR_TO_MINUTE_COLUMN_SIZE = 5;
  public static final int INTERVAL_HOUR_TO_SECOND_COLUMN_SIZE = 15;
  public static final int INTERVAL_MINUTE_COLUMN_SIZE = 2;
  public static final int INTERVAL_MINUTE_TO_SECOND_COLUMN_SIZE = 12;
  public static final int INTERVAL_SECOND_COLUMN_SIZE = 9;
  private static final Map<String, Integer> s_stringToSQLTypeMap = initializeStringToSQLTypeMap();
  private static final Set<Integer> s_supportedSqlTypes = initializeSupportedSqlTypes();
  
  public static long getColumnSize(TypeMetadata paramTypeMetadata, long paramLong)
    throws ErrorException
  {
    int i;
    switch (paramTypeMetadata.getType())
    {
    case -10: 
    case -9: 
    case -8: 
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    case 12: 
      return paramLong;
    case 92: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return 8L;
      }
      return 9 + i;
    case 91: 
      return 10L;
    case 93: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return 19L;
      }
      return 20 + i;
    case 2: 
    case 3: 
      return paramTypeMetadata.getPrecision();
    case 7: 
      return 7L;
    case 6: 
    case 8: 
      return 15L;
    case -11: 
      return 36L;
    case -7: 
    case 16: 
      return 1L;
    case -5: 
      if (paramTypeMetadata.isSigned()) {
        return 19L;
      }
      return 20L;
    case 4: 
      return 10L;
    case 5: 
      return 5L;
    case -6: 
      return 3L;
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
      return paramTypeMetadata.getIntervalPrecision();
    case 106: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision();
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 1;
    case 107: 
    case 108: 
    case 111: 
      return paramTypeMetadata.getIntervalPrecision() + 3;
    case 109: 
      return paramTypeMetadata.getIntervalPrecision() + 6;
    case 110: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 9;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 10;
    case 112: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 6;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 7;
    case 113: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 3;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 4;
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_UNSUPPORTED.name(), new String[] { paramTypeMetadata.getTypeName(), Short.toString(paramTypeMetadata.getType()) }, ExceptionType.DATA);
  }
  
  public static int getSizeInBytes(int paramInt)
  {
    switch (paramInt)
    {
    case 92: 
      return 6;
    case 91: 
      return 6;
    case 93: 
      return 16;
    case 2: 
    case 3: 
      return 40;
    case 7: 
      return 4;
    case 6: 
    case 8: 
      return 8;
    case -11: 
      return 16;
    case -7: 
    case 16: 
      return 1;
    case -5: 
      return 8;
    case 4: 
      return 4;
    case 5: 
      return 2;
    case -6: 
      return 1;
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      return 34;
    }
    if (!$assertionsDisabled) {
      throw new AssertionError();
    }
    return 0;
  }
  
  public static long getDisplaySize(TypeMetadata paramTypeMetadata, long paramLong)
    throws ErrorException
  {
    int i;
    switch (paramTypeMetadata.getType())
    {
    case 2003: 
      return -4L;
    case -4: 
    case -3: 
    case -2: 
      return paramLong * 2L;
    case -7: 
    case 16: 
      return 1L;
    case -5: 
      return 20L;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 0: 
    case 1: 
    case 12: 
      return paramLong;
    case -11: 
      return 36L;
    case 2: 
    case 3: 
      i = paramTypeMetadata.getScale();
      int j = paramTypeMetadata.getPrecision();
      if (j == i) {
        return j + 3;
      }
      if (0 == i) {
        return j + 1;
      }
      return j + 2;
    case 6: 
    case 8: 
      return 24L;
    case 4: 
      if (!paramTypeMetadata.isSigned()) {
        return 10L;
      }
      return 11L;
    case 7: 
      return 14L;
    case 5: 
      if (!paramTypeMetadata.isSigned()) {
        return 5L;
      }
      return 6L;
    case -6: 
      if (!paramTypeMetadata.isSigned()) {
        return 3L;
      }
      return 4L;
    case 91: 
      return 10L;
    case 92: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return 8L;
      }
      return 9 + i;
    case 93: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return 19L;
      }
      return 20 + i;
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
      return paramTypeMetadata.getIntervalPrecision() + 1;
    case 107: 
    case 108: 
    case 111: 
      return paramTypeMetadata.getIntervalPrecision() + 4;
    case 109: 
      return paramTypeMetadata.getIntervalPrecision() + 7;
    case 110: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 10;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 11;
    case 112: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 7;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 8;
    case 113: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 4;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 5;
    case 106: 
      i = paramTypeMetadata.getPrecision();
      if (0 == i) {
        return paramTypeMetadata.getIntervalPrecision() + 1;
      }
      return paramTypeMetadata.getIntervalPrecision() + i + 2;
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_UNSUPPORTED.name(), new String[] { paramTypeMetadata.getTypeName(), Short.toString(paramTypeMetadata.getType()) }, ExceptionType.DATA);
  }
  
  private static Map<String, Integer> initializeStringToSQLTypeMap()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("SQL_CHAR", Integer.valueOf(1));
    localHashMap.put("SQL_VARCHAR", Integer.valueOf(12));
    localHashMap.put("SQL_LONGVARCHAR", Integer.valueOf(-1));
    localHashMap.put("SQL_BINARY", Integer.valueOf(-2));
    localHashMap.put("SQL_VARBINARY", Integer.valueOf(-3));
    localHashMap.put("SQL_LONGVARBINARY", Integer.valueOf(-4));
    localHashMap.put("SQL_BIT", Integer.valueOf(-7));
    localHashMap.put("SQL_BOOLEAN", Integer.valueOf(16));
    localHashMap.put("SQL_TINYINT", Integer.valueOf(-6));
    localHashMap.put("SQL_SMALLINT", Integer.valueOf(5));
    localHashMap.put("SQL_INTEGER", Integer.valueOf(4));
    localHashMap.put("SQL_BIGINT", Integer.valueOf(-5));
    localHashMap.put("SQL_DECIMAL", Integer.valueOf(3));
    localHashMap.put("SQL_NUMERIC", Integer.valueOf(2));
    localHashMap.put("SQL_FLOAT", Integer.valueOf(6));
    localHashMap.put("SQL_DOUBLE", Integer.valueOf(8));
    localHashMap.put("SQL_REAL", Integer.valueOf(7));
    localHashMap.put("SQL_GUID", Integer.valueOf(-11));
    localHashMap.put("SQL_TYPE_DATE", Integer.valueOf(91));
    localHashMap.put("SQL_TYPE_TIME", Integer.valueOf(92));
    localHashMap.put("SQL_TYPE_TIMESTAMP", Integer.valueOf(93));
    localHashMap.put("SQL_DATE", Integer.valueOf(91));
    localHashMap.put("SQL_TIME", Integer.valueOf(92));
    localHashMap.put("SQL_TIMESTAMP", Integer.valueOf(93));
    localHashMap.put("SQL_WCHAR", Integer.valueOf(-8));
    localHashMap.put("SQL_WVARCHAR", Integer.valueOf(-9));
    localHashMap.put("SQL_WLONGVARCHAR", Integer.valueOf(-10));
    localHashMap.put("NULL", Integer.valueOf(0));
    localHashMap.put("SQL_INTERVAL_MONTH", Integer.valueOf(102));
    localHashMap.put("SQL_INTERVAL_YEAR", Integer.valueOf(101));
    localHashMap.put("SQL_INTERVAL_YEAR_TO_MONTH", Integer.valueOf(107));
    localHashMap.put("SQL_INTERVAL_DAY", Integer.valueOf(103));
    localHashMap.put("SQL_INTERVAL_DAY_TO_HOUR", Integer.valueOf(108));
    localHashMap.put("SQL_INTERVAL_DAY_TO_MINUTE", Integer.valueOf(109));
    localHashMap.put("SQL_INTERVAL_DAY_TO_SECOND", Integer.valueOf(110));
    localHashMap.put("SQL_INTERVAL_HOUR", Integer.valueOf(104));
    localHashMap.put("SQL_INTERVAL_HOUR_TO_MINUTE", Integer.valueOf(111));
    localHashMap.put("SQL_INTERVAL_HOUR_TO_SECOND", Integer.valueOf(112));
    localHashMap.put("SQL_INTERVAL_MINUTE", Integer.valueOf(105));
    localHashMap.put("SQL_INTERVAL_MINUTE_TO_SECOND", Integer.valueOf(113));
    localHashMap.put("SQL_INTERVAL_SECOND", Integer.valueOf(106));
    return Collections.unmodifiableMap(localHashMap);
  }
  
  private static Set<Integer> initializeSupportedSqlTypes()
  {
    return Collections.unmodifiableSet(new HashSet(s_stringToSQLTypeMap.values()));
  }
  
  public static short getIntervalCodeFromConciseType(int paramInt)
  {
    switch (paramInt)
    {
    case 91: 
      return 1;
    case 92: 
      return 2;
    case 93: 
      return 3;
    case 103: 
      return 3;
    case 108: 
      return 8;
    case 109: 
      return 9;
    case 110: 
      return 10;
    case 104: 
      return 4;
    case 111: 
      return 11;
    case 112: 
      return 12;
    case 105: 
      return 5;
    case 113: 
      return 13;
    case 102: 
      return 2;
    case 106: 
      return 6;
    case 101: 
      return 1;
    case 107: 
      return 7;
    }
    return 0;
  }
  
  public static Set<Integer> getSupportedSqlTypes()
  {
    return s_supportedSqlTypes;
  }
  
  public static int getVerboseTypeFromConciseType(int paramInt)
  {
    switch (paramInt)
    {
    case 91: 
    case 92: 
    case 93: 
      return 9;
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      return 10;
    }
    return paramInt;
  }
  
  public static boolean isApproximateNumericType(int paramInt)
  {
    return (paramInt == 7) || (paramInt == 6) || (paramInt == 8);
  }
  
  public static boolean isBinaryType(int paramInt)
  {
    return (paramInt == -2) || (paramInt == -3) || (paramInt == -4);
  }
  
  public static boolean isBooleanType(int paramInt)
  {
    return (paramInt == -7) || (paramInt == 16);
  }
  
  public static boolean isCharacterOrBinaryType(int paramInt)
  {
    return (isBinaryType(paramInt)) || (isCharacterType(paramInt));
  }
  
  public static boolean isCharacterType(int paramInt)
  {
    return (paramInt == 12) || (paramInt == 1) || (paramInt == -8) || (paramInt == -9) || (paramInt == -1) || (paramInt == -10);
  }
  
  public static boolean isDateTimeType(int paramInt)
  {
    return (paramInt == 91) || (paramInt == 92) || (paramInt == 93);
  }
  
  public static boolean isExactNumericType(int paramInt)
  {
    return (paramInt == 2) || (paramInt == 3);
  }
  
  public static boolean isNumberType(int paramInt)
  {
    return (paramInt == 2) || (paramInt == 3) || (paramInt == -6) || (paramInt == 5) || (paramInt == 4) || (paramInt == -5) || (paramInt == 8) || (paramInt == 6) || (paramInt == 7);
  }
  
  public static boolean isIntegerType(int paramInt)
  {
    return (paramInt == -6) || (paramInt == 5) || (paramInt == 4) || (paramInt == -5);
  }
  
  public static boolean isIntervalType(int paramInt)
  {
    switch (paramInt)
    {
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      return true;
    }
    return false;
  }
  
  public static boolean isLOBType(int paramInt)
  {
    return (paramInt == -4) || (paramInt == -1) || (paramInt == -10);
  }
  
  public static boolean isTimeType(int paramInt)
  {
    return (paramInt == 93) || (paramInt == 92);
  }
  
  public static int mapDataTypes(int paramInt)
  {
    if (isIntervalType(paramInt)) {
      return 1111;
    }
    if (-8 == paramInt) {
      return 1;
    }
    if (-9 == paramInt) {
      return 12;
    }
    if (-10 == paramInt) {
      return -1;
    }
    if (-11 == paramInt) {
      return 1111;
    }
    return paramInt;
  }
  
  public static Integer getTypeForSqlTypeString(String paramString)
  {
    if (null == paramString) {
      throw new NullPointerException();
    }
    String str = paramString.trim().toUpperCase();
    if (!s_stringToSQLTypeMap.containsKey(str)) {
      return null;
    }
    return (Integer)s_stringToSQLTypeMap.get(str);
  }
  
  public static String sqlTypeToString(short paramShort)
  {
    switch (paramShort)
    {
    case 2003: 
      return "SQL_ARRAY";
    case -5: 
      return "SQL_BIGINT";
    case 3: 
      return "SQL_DECIMAL";
    case 8: 
      return "SQL_DOUBLE";
    case 6: 
      return "SQL_FLOAT";
    case 4: 
      return "SQL_INTEGER";
    case 2: 
      return "SQL_NUMERIC";
    case 0: 
      return "NULL";
    case 7: 
      return "SQL_REAL";
    case 5: 
      return "SQL_SMALLINT";
    case -2: 
      return "SQL_BINARY";
    case -7: 
      return "SQL_BIT";
    case 16: 
      return "SQL_BOOLEAN";
    case 1: 
      return "SQL_CHAR";
    case -4: 
      return "SQL_LONGVARBINARY";
    case -1: 
      return "SQL_LONGVARCHAR";
    case 91: 
      return "SQL_TYPE_DATE";
    case 92: 
      return "SQL_TYPE_TIME";
    case 93: 
      return "SQL_TYPE_TIMESTAMP";
    case -6: 
      return "SQL_TINYINT";
    case -3: 
      return "SQL_VARBINARY";
    case 12: 
      return "SQL_VARCHAR";
    case -11: 
      return "SQL_GUID";
    case -8: 
      return "SQL_WCHAR";
    case -9: 
      return "SQL_WVARCHAR";
    case -10: 
      return "SQL_WLONGVARCHAR";
    case 103: 
      return "SQL_INTERVAL_DAY";
    case 108: 
      return "SQL_INTERVAL_DAY_TO_HOUR";
    case 109: 
      return "SQL_INTERVAL_DAY_TO_MINUTE";
    case 110: 
      return "SQL_INTERVAL_DAY_TO_SECOND";
    case 104: 
      return "SQL_INTERVAL_HOUR";
    case 111: 
      return "SQL_INTERVAL_HOUR_TO_MINUTE";
    case 112: 
      return "SQL_INTERVAL_HOUR_TO_SECOND";
    case 105: 
      return "SQL_INTERVAL_MINUTE";
    case 113: 
      return "SQL_INTERVAL_MINUTE_TO_SECOND";
    case 102: 
      return "SQL_INTERVAL_MONTH";
    case 106: 
      return "SQL_INTERVAL_SECOND";
    case 101: 
      return "SQL_INTERVAL_YEAR";
    case 107: 
      return "SQL_INTERVAL_YEAR_TO_MONTH";
    }
    return null;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/TypeUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */