package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;

public final class TypeMetadata
{
  public static final String TN_ARRAY = "SQL_ARRAY";
  public static final String TN_BIGINT = "SQL_BIGINT";
  public static final String TN_BINARY = "SQL_BINARY";
  public static final String TN_BIT = "SQL_BIT";
  public static final String TN_BOOLEAN = "SQL_BOOLEAN";
  public static final String TN_CHAR = "SQL_CHAR";
  public static final String TN_DECIMAL = "SQL_DECIMAL";
  public static final String TN_DOUBLE = "SQL_DOUBLE";
  public static final String TN_FLOAT = "SQL_FLOAT";
  public static final String TN_GUID = "SQL_GUID";
  public static final String TN_INTEGER = "SQL_INTEGER";
  public static final String TN_INTERVAL_DAY = "SQL_INTERVAL_DAY";
  public static final String TN_INTERVAL_DAY_TO_HOUR = "SQL_INTERVAL_DAY_TO_HOUR";
  public static final String TN_INTERVAL_DAY_TO_MINUTE = "SQL_INTERVAL_DAY_TO_MINUTE";
  public static final String TN_INTERVAL_DAY_TO_SECOND = "SQL_INTERVAL_DAY_TO_SECOND";
  public static final String TN_INTERVAL_HOUR = "SQL_INTERVAL_HOUR";
  public static final String TN_INTERVAL_HOUR_TO_MINUTE = "SQL_INTERVAL_HOUR_TO_MINUTE";
  public static final String TN_INTERVAL_HOUR_TO_SECOND = "SQL_INTERVAL_HOUR_TO_SECOND";
  public static final String TN_INTERVAL_MINUTE = "SQL_INTERVAL_MINUTE";
  public static final String TN_INTERVAL_MINUTE_TO_SECOND = "SQL_INTERVAL_MINUTE_TO_SECOND";
  public static final String TN_INTERVAL_MONTH = "SQL_INTERVAL_MONTH";
  public static final String TN_INTERVAL_SECOND = "SQL_INTERVAL_SECOND";
  public static final String TN_INTERVAL_YEAR = "SQL_INTERVAL_YEAR";
  public static final String TN_INTERVAL_YEAR_TO_MONTH = "SQL_INTERVAL_YEAR_TO_MONTH";
  public static final String TN_LONGVARBINARY = "SQL_LONGVARBINARY";
  public static final String TN_LONGVARCHAR = "SQL_LONGVARCHAR";
  public static final String TN_NULL = "NULL";
  public static final String TN_NUMERIC = "SQL_NUMERIC";
  public static final String TN_REAL = "SQL_REAL";
  public static final String TN_SMALLINT = "SQL_SMALLINT";
  public static final String TN_TINYINT = "SQL_TINYINT";
  public static final String TN_TYPE_DATE = "SQL_TYPE_DATE";
  public static final String TN_TYPE_TIME = "SQL_TYPE_TIME";
  public static final String TN_TYPE_TIMESTAMP = "SQL_TYPE_TIMESTAMP";
  public static final String TN_VARBINARY = "SQL_VARBINARY";
  public static final String TN_VARCHAR = "SQL_VARCHAR";
  public static final String TN_WCHAR = "SQL_WCHAR";
  public static final String TN_WLONGVARCHAR = "SQL_WLONGVARCHAR";
  public static final String TN_WVARCHAR = "SQL_WVARCHAR";
  private short m_type;
  private String m_localTypeName;
  private String m_typeName;
  private short m_scale;
  private short m_precision;
  private int m_intervalPrecision;
  private boolean m_isSigned;
  private boolean m_isApproximateNumericType;
  private boolean m_isBinaryType;
  private boolean m_isBooleanType;
  private boolean m_isCharacterType;
  private boolean m_isExactNumericType;
  private boolean m_isIntegerType;
  private boolean m_isIntervalType;
  private boolean m_isCurrency = false;
  private boolean m_isSortable = true;
  
  public static TypeMetadata createTypeMetadata(int paramInt)
    throws ErrorException
  {
    switch (paramInt)
    {
    case -5: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
      return createTypeMetadata(paramInt, true);
    case -11: 
    case -10: 
    case -9: 
    case -8: 
    case -7: 
    case -6: 
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    case 12: 
    case 16: 
    case 91: 
    case 92: 
    case 93: 
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
    case 2003: 
      return createTypeMetadata(paramInt, false);
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_UNSUPPORTED_NUM.name(), String.valueOf(paramInt), ExceptionType.DATA);
  }
  
  public static TypeMetadata createTypeMetadata(int paramInt, boolean paramBoolean)
    throws ErrorException
  {
    switch (paramInt)
    {
    case 2003: 
      TypeMetadata localTypeMetadata = new TypeMetadata((short)paramInt, "SQL_ARRAY".substring(4), (short)0, (short)0, 0, paramBoolean);
      localTypeMetadata.setIsSortable(false);
      return localTypeMetadata;
    case -5: 
      return createTypeMetadata(paramInt, (short)19, (short)0, 19, paramBoolean);
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 1: 
    case 12: 
      return createTypeMetadata(paramInt, (short)1, (short)0, 1, paramBoolean);
    case -7: 
    case 16: 
      return createTypeMetadata(paramInt, (short)1, (short)0, 0, paramBoolean);
    case 2: 
    case 3: 
      return createTypeMetadata(paramInt, (short)38, (short)0, 38, paramBoolean);
    case 6: 
    case 8: 
      return createTypeMetadata(paramInt, (short)15, (short)0, 53, paramBoolean);
    case -11: 
      return createTypeMetadata(paramInt, (short)36, (short)0, 36, paramBoolean);
    case 4: 
      return createTypeMetadata(paramInt, (short)10, (short)0, 0, paramBoolean);
    case 0: 
      return createTypeMetadata(paramInt, (short)0, (short)0, 0, paramBoolean);
    case 7: 
      return createTypeMetadata(paramInt, (short)7, (short)0, 24, paramBoolean);
    case 5: 
      return createTypeMetadata(paramInt, (short)5, (short)0, 5, paramBoolean);
    case -6: 
      return createTypeMetadata(paramInt, (short)3, (short)0, 3, paramBoolean);
    case 91: 
      return createTypeMetadata(paramInt, (short)0, (short)0, 10, paramBoolean);
    case 92: 
      return createTypeMetadata(paramInt, (short)0, (short)0, 8, paramBoolean);
    case 93: 
      return createTypeMetadata(paramInt, (short)6, (short)6, 0, paramBoolean);
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 107: 
    case 108: 
    case 109: 
    case 111: 
      return createTypeMetadata(paramInt, (short)0, (short)0, 2, paramBoolean);
    case 106: 
    case 110: 
    case 112: 
    case 113: 
      return createTypeMetadata(paramInt, (short)6, (short)6, 2, paramBoolean);
    case -10: 
    case -9: 
    case -8: 
      return createTypeMetadata(paramInt, (short)0, (short)0, 0, paramBoolean);
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_UNSUPPORTED_NUM.name(), String.valueOf(paramInt), ExceptionType.DATA);
  }
  
  public static TypeMetadata createTypeMetadata(int paramInt1, short paramShort1, short paramShort2, int paramInt2, boolean paramBoolean)
    throws ErrorException
  {
    String str = TypeUtilities.sqlTypeToString((short)paramInt1);
    if (null == str) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_UNSUPPORTED_NUM.name(), String.valueOf(paramInt1), ExceptionType.DATA);
    }
    if (str.startsWith("SQL_")) {
      str = str.substring(4);
    }
    return new TypeMetadata((short)paramInt1, str, paramShort1, paramShort2, paramInt2, paramBoolean);
  }
  
  public TypeMetadata(short paramShort1, String paramString, short paramShort2, short paramShort3, int paramInt)
  {
    this(paramShort1, paramString, paramShort2, paramShort3, paramInt, false);
  }
  
  public TypeMetadata(short paramShort1, String paramString, short paramShort2, short paramShort3, int paramInt, boolean paramBoolean)
  {
    this.m_type = paramShort1;
    this.m_typeName = paramString;
    this.m_localTypeName = paramString;
    this.m_precision = paramShort2;
    this.m_scale = paramShort3;
    this.m_intervalPrecision = paramInt;
    this.m_isSigned = paramBoolean;
    this.m_isApproximateNumericType = TypeUtilities.isApproximateNumericType(this.m_type);
    this.m_isBinaryType = TypeUtilities.isBinaryType(this.m_type);
    this.m_isCharacterType = TypeUtilities.isCharacterType(this.m_type);
    this.m_isExactNumericType = TypeUtilities.isExactNumericType(this.m_type);
    this.m_isIntegerType = TypeUtilities.isIntegerType(this.m_type);
    this.m_isIntervalType = TypeUtilities.isIntervalType(this.m_type);
    this.m_isBooleanType = TypeUtilities.isBooleanType(this.m_type);
  }
  
  public static TypeMetadata copyOf(TypeMetadata paramTypeMetadata)
  {
    TypeMetadata localTypeMetadata = new TypeMetadata(paramTypeMetadata.getType(), paramTypeMetadata.getTypeName(), paramTypeMetadata.getPrecision(), paramTypeMetadata.getScale(), paramTypeMetadata.getIntervalPrecision(), paramTypeMetadata.isSigned());
    localTypeMetadata.setIsCurrency(paramTypeMetadata.isCurrency());
    localTypeMetadata.setIsSortable(paramTypeMetadata.isSortable());
    return localTypeMetadata;
  }
  
  public int getIntervalPrecision()
  {
    return this.m_intervalPrecision;
  }
  
  public String getLocalTypeName()
  {
    return this.m_localTypeName;
  }
  
  public short getPrecision()
  {
    return this.m_precision;
  }
  
  public short getScale()
  {
    return this.m_scale;
  }
  
  public short getType()
  {
    return this.m_type;
  }
  
  public String getTypeName()
  {
    return this.m_typeName;
  }
  
  public boolean isApproximateNumericType()
  {
    return this.m_isApproximateNumericType;
  }
  
  public boolean isBooleanType()
  {
    return this.m_isBooleanType;
  }
  
  public boolean isBinaryType()
  {
    return this.m_isBinaryType;
  }
  
  public boolean isCharacterOrBinaryType()
  {
    return (this.m_isCharacterType) || (this.m_isBinaryType);
  }
  
  public boolean isCharacterType()
  {
    return this.m_isCharacterType;
  }
  
  public boolean isCurrency()
  {
    return this.m_isCurrency;
  }
  
  public boolean isExactNumericType()
  {
    return this.m_isExactNumericType;
  }
  
  public boolean isIntegerType()
  {
    return this.m_isIntegerType;
  }
  
  public boolean isIntervalType()
  {
    return this.m_isIntervalType;
  }
  
  public boolean isSigned()
  {
    return this.m_isSigned;
  }
  
  public boolean isSortable()
  {
    return this.m_isSortable;
  }
  
  public void setIntervalPrecision(int paramInt)
  {
    this.m_intervalPrecision = paramInt;
  }
  
  public void setIsCurrency(boolean paramBoolean)
  {
    this.m_isCurrency = paramBoolean;
  }
  
  public void setIsSortable(boolean paramBoolean)
  {
    this.m_isSortable = paramBoolean;
  }
  
  public void setLocalTypeName(String paramString)
  {
    this.m_localTypeName = paramString;
  }
  
  public void setPrecision(short paramShort)
  {
    this.m_precision = paramShort;
  }
  
  public void setScale(short paramShort)
  {
    this.m_scale = paramShort;
  }
  
  public void setSigned(boolean paramBoolean)
  {
    this.m_isSigned = paramBoolean;
  }
  
  public void setTypeName(String paramString)
  {
    this.m_typeName = paramString;
  }
  
  public int hashCode()
  {
    int i = 1;
    i = 31 * i + this.m_intervalPrecision;
    i = 31 * i + (this.m_isApproximateNumericType ? 1231 : 1237);
    i = 31 * i + (this.m_isBinaryType ? 1231 : 1237);
    i = 31 * i + (this.m_isBooleanType ? 1231 : 1237);
    i = 31 * i + (this.m_isCharacterType ? 1231 : 1237);
    i = 31 * i + (this.m_isExactNumericType ? 1231 : 1237);
    i = 31 * i + (this.m_isIntegerType ? 1231 : 1237);
    i = 31 * i + (this.m_isIntervalType ? 1231 : 1237);
    i = 31 * i + (this.m_isSigned ? 1231 : 1237);
    i = 31 * i + this.m_precision;
    i = 31 * i + this.m_scale;
    i = 31 * i + this.m_type;
    i = 31 * i + (this.m_typeName == null ? 0 : this.m_typeName.hashCode());
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    TypeMetadata localTypeMetadata = (TypeMetadata)paramObject;
    if (this.m_intervalPrecision != localTypeMetadata.m_intervalPrecision) {
      return false;
    }
    if (this.m_isApproximateNumericType != localTypeMetadata.m_isApproximateNumericType) {
      return false;
    }
    if (this.m_isBinaryType != localTypeMetadata.m_isBinaryType) {
      return false;
    }
    if (this.m_isBooleanType != localTypeMetadata.m_isBooleanType) {
      return false;
    }
    if (this.m_isCharacterType != localTypeMetadata.m_isCharacterType) {
      return false;
    }
    if (this.m_isExactNumericType != localTypeMetadata.m_isExactNumericType) {
      return false;
    }
    if (this.m_isIntegerType != localTypeMetadata.m_isIntegerType) {
      return false;
    }
    if (this.m_isIntervalType != localTypeMetadata.m_isIntervalType) {
      return false;
    }
    if (this.m_isSigned != localTypeMetadata.m_isSigned) {
      return false;
    }
    if (this.m_precision != localTypeMetadata.m_precision) {
      return false;
    }
    if (this.m_scale != localTypeMetadata.m_scale) {
      return false;
    }
    if (this.m_type != localTypeMetadata.m_type) {
      return false;
    }
    if (this.m_typeName == null)
    {
      if (localTypeMetadata.m_typeName != null) {
        return false;
      }
    }
    else if (!this.m_typeName.equals(localTypeMetadata.m_typeName)) {
      return false;
    }
    return true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/TypeMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */