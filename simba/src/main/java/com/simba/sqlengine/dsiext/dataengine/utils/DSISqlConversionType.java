package com.simba.sqlengine.dsiext.dataengine.utils;

import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;

public enum DSISqlConversionType
{
  DSI_CVT_BIGINT,  DSI_CVT_BINARY,  DSI_CVT_BIT,  DSI_CVT_CHAR,  DSI_CVT_GUID,  DSI_CVT_DATE,  DSI_CVT_DECIMAL,  DSI_CVT_DOUBLE,  DSI_CVT_FLOAT,  DSI_CVT_INTEGER,  DSI_CVT_INTERVAL_DAY_SECOND,  DSI_CVT_INTERVAL_YEAR_MONTH,  DSI_CVT_LONGVARBINARY,  DSI_CVT_LONGVARCHAR,  DSI_CVT_NULL,  DSI_CVT_NUMERIC,  DSI_CVT_REAL,  DSI_CVT_SMALLINT,  DSI_CVT_TIME,  DSI_CVT_TIMESTAMP,  DSI_CVT_TINYINT,  DSI_CVT_VARBINARY,  DSI_CVT_VARCHAR,  DSI_CVT_WCHAR,  DSI_CVT_WVARCHAR,  DSI_CVT_WLONGVARCHAR,  DSI_CVT_BOOLEAN;
  
  private DSISqlConversionType() {}
  
  public static DSISqlConversionType fromSqlType(int paramInt)
    throws ErrorException
  {
    switch (paramInt)
    {
    case -2: 
      return DSI_CVT_BINARY;
    case -3: 
      return DSI_CVT_VARBINARY;
    case -4: 
      return DSI_CVT_LONGVARBINARY;
    case 1: 
      return DSI_CVT_CHAR;
    case 12: 
      return DSI_CVT_VARCHAR;
    case -1: 
      return DSI_CVT_LONGVARCHAR;
    case -8: 
      return DSI_CVT_WCHAR;
    case -9: 
      return DSI_CVT_WVARCHAR;
    case -10: 
      return DSI_CVT_WLONGVARCHAR;
    case -7: 
      return DSI_CVT_BIT;
    case 16: 
      return DSI_CVT_BOOLEAN;
    case -6: 
      return DSI_CVT_TINYINT;
    case 5: 
      return DSI_CVT_SMALLINT;
    case 4: 
      return DSI_CVT_INTEGER;
    case -5: 
      return DSI_CVT_BIGINT;
    case 7: 
      return DSI_CVT_REAL;
    case 6: 
      return DSI_CVT_FLOAT;
    case 8: 
      return DSI_CVT_DOUBLE;
    case 3: 
      return DSI_CVT_DECIMAL;
    case 2: 
      return DSI_CVT_NUMERIC;
    case 91: 
      return DSI_CVT_DATE;
    case 92: 
      return DSI_CVT_TIME;
    case 93: 
      return DSI_CVT_TIMESTAMP;
    case 0: 
      return DSI_CVT_NULL;
    case 103: 
    case 104: 
    case 106: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
      return DSI_CVT_INTERVAL_DAY_SECOND;
    case 101: 
    case 102: 
    case 107: 
      return DSI_CVT_INTERVAL_YEAR_MONTH;
    case -11: 
      return DSI_CVT_GUID;
    }
    throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(SQLEngineMessageKey.DATA_TYPE_NOT_SUPPORTED.name(), String.valueOf(paramInt), ExceptionType.DATA);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/utils/DSISqlConversionType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */