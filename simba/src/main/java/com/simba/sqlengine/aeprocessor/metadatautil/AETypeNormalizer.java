package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

class AETypeNormalizer
{
  private static Map<SqlTypes, SqlTypes[]> s_normalizeSequence;
  private Set<SqlTypes> m_disabledSet = EnumSet.noneOf(SqlTypes.class);
  private AECoercionProperties m_properties;
  
  public AETypeNormalizer(AECoercionProperties paramAECoercionProperties)
  {
    this.m_properties = paramAECoercionProperties;
  }
  
  public int normalizeType(int paramInt)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramInt);
    SqlTypes localSqlTypes2 = normalizeType(localSqlTypes1);
    if (localSqlTypes2 == null) {
      throw SQLEngineExceptionFactory.invalidOperationException("Can not normalize type: all available coercion type is disabled.");
    }
    return localSqlTypes2.getSqlType();
  }
  
  public SqlTypes normalizeType(SqlTypes paramSqlTypes)
    throws ErrorException
  {
    if (paramSqlTypes == null) {
      throw new NullPointerException("Normalize sql type failed.");
    }
    if (!this.m_disabledSet.contains(paramSqlTypes)) {
      return paramSqlTypes;
    }
    assert (s_normalizeSequence.containsKey(paramSqlTypes));
    for (SqlTypes localSqlTypes : (SqlTypes[])s_normalizeSequence.get(paramSqlTypes)) {
      if (!this.m_disabledSet.contains(localSqlTypes)) {
        return localSqlTypes;
      }
    }
    return null;
  }
  
  public SqlTypes fitBinaryOrCharType(SqlTypes paramSqlTypes, long paramLong)
    throws ErrorException
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("Wrong column length: " + paramLong);
    }
    if (paramSqlTypes.isBinary()) {
      return fitBinaryType(paramSqlTypes, paramLong);
    }
    if (paramSqlTypes.isChar()) {
      return fitCharType(paramSqlTypes, paramLong);
    }
    if (paramSqlTypes.isWChar()) {
      return fitWCharType(paramSqlTypes, paramLong);
    }
    throw new IllegalArgumentException("Only binary or char type is acceptable: actual type: " + paramSqlTypes.name());
  }
  
  private SqlTypes fitCharType(SqlTypes paramSqlTypes, long paramLong)
    throws ErrorException
  {
    assert ((paramSqlTypes.isChar()) && (paramLong >= 0L));
    switch (paramSqlTypes)
    {
    case SQL_LONGVARCHAR: 
      return paramSqlTypes;
    case SQL_VARCHAR: 
      return upBinaryOrCharType(new SqlTypes[] { SqlTypes.SQL_VARCHAR, SqlTypes.SQL_LONGVARCHAR }, paramLong);
    case SQL_CHAR: 
      return upBinaryOrCharType(new SqlTypes[] { SqlTypes.SQL_CHAR, SqlTypes.SQL_VARCHAR, SqlTypes.SQL_LONGVARCHAR }, paramLong);
    }
    throw new IllegalArgumentException("Invalid operation for upWCharType: type: " + paramSqlTypes.name() + "column length: " + paramLong);
  }
  
  private SqlTypes fitWCharType(SqlTypes paramSqlTypes, long paramLong)
    throws ErrorException
  {
    assert ((paramSqlTypes.isWChar()) && (paramLong >= 0L));
    switch (paramSqlTypes)
    {
    case SQL_WLONGVARCHAR: 
      return paramSqlTypes;
    case SQL_WVARCHAR: 
      return upBinaryOrCharType(new SqlTypes[] { SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WLONGVARCHAR }, paramLong);
    case SQL_WCHAR: 
      return upBinaryOrCharType(new SqlTypes[] { SqlTypes.SQL_WCHAR, SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WLONGVARCHAR }, paramLong);
    }
    throw new IllegalArgumentException("Invalid operation for upWCharType: type: " + paramSqlTypes.name() + "column length: " + paramLong);
  }
  
  private SqlTypes fitBinaryType(SqlTypes paramSqlTypes, long paramLong)
    throws ErrorException
  {
    assert ((paramSqlTypes.isBinary()) && (paramLong >= 0L));
    switch (paramSqlTypes)
    {
    case SQL_LONGVARBINARY: 
      return paramSqlTypes;
    case SQL_VARBINARY: 
      return upBinaryOrCharType(new SqlTypes[] { SqlTypes.SQL_VARBINARY, SqlTypes.SQL_LONGVARBINARY }, paramLong);
    case SQL_BINARY: 
      return upBinaryOrCharType(new SqlTypes[] { SqlTypes.SQL_BINARY, SqlTypes.SQL_VARBINARY, SqlTypes.SQL_LONGVARBINARY }, paramLong);
    }
    throw new IllegalArgumentException("Invalid operation for upWCharType: type: " + paramSqlTypes.name() + "column length: " + paramLong);
  }
  
  public boolean isTypeDisabled(SqlTypes paramSqlTypes)
  {
    return this.m_disabledSet.contains(paramSqlTypes);
  }
  
  public void disableType(int paramInt)
    throws ErrorException
  {
    SqlTypes localSqlTypes = SqlTypes.getValueOf(paramInt);
    this.m_disabledSet.add(localSqlTypes);
  }
  
  private SqlTypes upBinaryOrCharType(SqlTypes[] paramArrayOfSqlTypes, long paramLong)
    throws ErrorException
  {
    assert (paramArrayOfSqlTypes.length >= 1);
    SqlTypes localSqlTypes = null;
    for (int i = 0; i < paramArrayOfSqlTypes.length; i++) {
      if (!isTypeDisabled(paramArrayOfSqlTypes[i]))
      {
        localSqlTypes = paramArrayOfSqlTypes[i];
        if (getMaxBinaryOrCharLen(localSqlTypes) >= paramLong) {
          return localSqlTypes;
        }
      }
    }
    if (localSqlTypes == null) {
      throw SQLEngineExceptionFactory.invalidOperationException("Can not normalize type: all available coercion type is disabled.");
    }
    return localSqlTypes;
  }
  
  private long getMaxBinaryOrCharLen(SqlTypes paramSqlTypes)
  {
    switch (paramSqlTypes)
    {
    case SQL_BINARY: 
      return this.m_properties.getMaxBinaryLength();
    case SQL_VARBINARY: 
      return this.m_properties.getMaxVarbinaryLength();
    case SQL_LONGVARBINARY: 
      return Long.MAX_VALUE;
    case SQL_CHAR: 
      return this.m_properties.getMaxCharLength();
    case SQL_VARCHAR: 
      return this.m_properties.getMaxVarcharlength();
    case SQL_LONGVARCHAR: 
      return Long.MAX_VALUE;
    case SQL_WCHAR: 
      return this.m_properties.getMaxWcharlength();
    case SQL_WVARCHAR: 
      return this.m_properties.getMaxWvarcharLength();
    case SQL_WLONGVARCHAR: 
      return Long.MAX_VALUE;
    }
    throw new IllegalArgumentException("Invalid argument passed in getMaxBinaryOrCharLen: " + paramSqlTypes.name());
  }
  
  static
  {
    s_normalizeSequence = new EnumMap(SqlTypes.class);
    s_normalizeSequence.put(SqlTypes.SQL_BINARY, new SqlTypes[] { SqlTypes.SQL_VARBINARY, SqlTypes.SQL_LONGVARBINARY });
    s_normalizeSequence.put(SqlTypes.SQL_VARBINARY, new SqlTypes[] { SqlTypes.SQL_LONGVARBINARY, SqlTypes.SQL_BINARY });
    s_normalizeSequence.put(SqlTypes.SQL_LONGVARBINARY, new SqlTypes[] { SqlTypes.SQL_VARBINARY, SqlTypes.SQL_BINARY });
    s_normalizeSequence.put(SqlTypes.SQL_CHAR, new SqlTypes[] { SqlTypes.SQL_VARCHAR, SqlTypes.SQL_LONGVARCHAR, SqlTypes.SQL_WCHAR, SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WLONGVARCHAR });
    s_normalizeSequence.put(SqlTypes.SQL_VARCHAR, new SqlTypes[] { SqlTypes.SQL_LONGVARCHAR, SqlTypes.SQL_CHAR, SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WLONGVARCHAR, SqlTypes.SQL_WCHAR });
    s_normalizeSequence.put(SqlTypes.SQL_LONGVARCHAR, new SqlTypes[] { SqlTypes.SQL_VARCHAR, SqlTypes.SQL_CHAR, SqlTypes.SQL_WLONGVARCHAR, SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WCHAR });
    s_normalizeSequence.put(SqlTypes.SQL_WCHAR, new SqlTypes[] { SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WLONGVARCHAR });
    s_normalizeSequence.put(SqlTypes.SQL_WVARCHAR, new SqlTypes[] { SqlTypes.SQL_WLONGVARCHAR, SqlTypes.SQL_WCHAR });
    s_normalizeSequence.put(SqlTypes.SQL_WLONGVARCHAR, new SqlTypes[] { SqlTypes.SQL_WVARCHAR, SqlTypes.SQL_WCHAR });
    s_normalizeSequence.put(SqlTypes.SQL_BIT, new SqlTypes[] { SqlTypes.SQL_TINYINT, SqlTypes.SQL_SMALLINT, SqlTypes.SQL_INTEGER, SqlTypes.SQL_BIGINT });
    s_normalizeSequence.put(SqlTypes.SQL_TINYINT, new SqlTypes[] { SqlTypes.SQL_SMALLINT, SqlTypes.SQL_INTEGER, SqlTypes.SQL_BIGINT });
    s_normalizeSequence.put(SqlTypes.SQL_SMALLINT, new SqlTypes[] { SqlTypes.SQL_INTEGER, SqlTypes.SQL_BIGINT, SqlTypes.SQL_TINYINT });
    s_normalizeSequence.put(SqlTypes.SQL_INTEGER, new SqlTypes[] { SqlTypes.SQL_BIGINT, SqlTypes.SQL_SMALLINT, SqlTypes.SQL_TINYINT });
    s_normalizeSequence.put(SqlTypes.SQL_BIGINT, new SqlTypes[] { SqlTypes.SQL_INTEGER, SqlTypes.SQL_SMALLINT, SqlTypes.SQL_TINYINT });
    s_normalizeSequence.put(SqlTypes.SQL_NUMERIC, new SqlTypes[] { SqlTypes.SQL_DECIMAL });
    s_normalizeSequence.put(SqlTypes.SQL_DECIMAL, new SqlTypes[] { SqlTypes.SQL_NUMERIC });
    s_normalizeSequence.put(SqlTypes.SQL_REAL, new SqlTypes[] { SqlTypes.SQL_FLOAT, SqlTypes.SQL_DOUBLE });
    s_normalizeSequence.put(SqlTypes.SQL_FLOAT, new SqlTypes[] { SqlTypes.SQL_DOUBLE, SqlTypes.SQL_REAL });
    s_normalizeSequence.put(SqlTypes.SQL_DOUBLE, new SqlTypes[] { SqlTypes.SQL_FLOAT, SqlTypes.SQL_REAL });
    s_normalizeSequence.put(SqlTypes.SQL_DATE, new SqlTypes[] { SqlTypes.SQL_TIMESTAMP });
    s_normalizeSequence.put(SqlTypes.SQL_TIMESTAMP, new SqlTypes[0]);
    s_normalizeSequence.put(SqlTypes.SQL_TIME, new SqlTypes[0]);
    s_normalizeSequence.put(SqlTypes.SQL_GUID, new SqlTypes[0]);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AETypeNormalizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */