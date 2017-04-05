package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.HashMap;
import java.util.Map;

public enum SqlTypes
{
  SQL_BINARY(-2),  SQL_VARBINARY(-3),  SQL_LONGVARBINARY(-4),  SQL_CHAR(1),  SQL_VARCHAR(12),  SQL_LONGVARCHAR(-1),  SQL_WCHAR(-8),  SQL_WVARCHAR(-9),  SQL_WLONGVARCHAR(-10),  SQL_BIT(-7),  SQL_TINYINT(-6),  SQL_SMALLINT(5),  SQL_INTEGER(4),  SQL_BIGINT(-5),  SQL_NUMERIC(2),  SQL_DECIMAL(3),  SQL_REAL(7),  SQL_FLOAT(6),  SQL_DOUBLE(8),  SQL_DATE(91),  SQL_TIMESTAMP(93),  SQL_TIME(92),  SQL_BOOLEAN(16),  SQL_NULL(0),  SQL_GUID(-11);
  
  private static Map<Integer, SqlTypes> s_sqlTypeToValue;
  private final int m_sqlType;
  
  private SqlTypes(int paramInt)
  {
    this.m_sqlType = paramInt;
  }
  
  public int getSqlType()
  {
    return this.m_sqlType;
  }
  
  public boolean isNumber()
  {
    return false;
  }
  
  public boolean isExactNum()
  {
    return false;
  }
  
  public boolean isInterval()
  {
    return false;
  }
  
  public boolean isInteger()
  {
    return false;
  }
  
  public boolean isChar()
  {
    return false;
  }
  
  public boolean isWChar()
  {
    return false;
  }
  
  public boolean isBinary()
  {
    return false;
  }
  
  public boolean isDateTime()
  {
    return false;
  }
  
  public boolean isApproximateNum()
  {
    return false;
  }
  
  public static SqlTypes getValueOf(int paramInt)
    throws ErrorException
  {
    if (!s_sqlTypeToValue.containsKey(Integer.valueOf(paramInt))) {
      throw SQLEngineExceptionFactory.unsupportedTypesException("" + paramInt);
    }
    return (SqlTypes)s_sqlTypeToValue.get(Integer.valueOf(paramInt));
  }
  
  static
  {
    s_sqlTypeToValue = new HashMap();
    for (SqlTypes localSqlTypes : values()) {
      s_sqlTypeToValue.put(Integer.valueOf(localSqlTypes.getSqlType()), localSqlTypes);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/SqlTypes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */