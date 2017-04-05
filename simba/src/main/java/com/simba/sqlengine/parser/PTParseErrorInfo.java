package com.simba.sqlengine.parser;

import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;

public final class PTParseErrorInfo
{
  private static final String SYNTAX_ERROR_MARKER = "<<< ??? >>>";
  
  private PTParseErrorInfo()
  {
    throw new UnsupportedOperationException("Not instantiable");
  }
  
  public static SQLEngineException makeSyntaxError(String paramString, int paramInt)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.SYNTAX_ERROR.name(), new String[] { String.valueOf(paramInt), errorPositionString(paramString, paramInt) });
    return localSQLEngineException;
  }
  
  public static SQLEngineException makeInvalidSqlTypeError(String paramString1, String paramString2, int paramInt)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_SQL_TYPE.name(), new String[] { String.valueOf(paramInt), paramString2, errorPositionString(paramString1, paramInt) });
    return localSQLEngineException;
  }
  
  private static String errorPositionString(String paramString, int paramInt)
  {
    paramInt = Math.max(Math.min(paramInt, paramString.length()), 0);
    return paramString.substring(0, paramInt) + "<<< ??? >>>" + paramString.substring(paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/PTParseErrorInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */