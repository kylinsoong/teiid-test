package com.simba.exceptions;

import com.simba.support.ILogger;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.FailedPropertiesReason;
import java.sql.SQLException;
import java.util.Map;

public abstract class ExceptionConverter
{
  private static ExceptionConverter s_converter = null;
  
  public static ExceptionConverter getInstance()
  {
    return s_converter;
  }
  
  public static void setInstance(ExceptionConverter paramExceptionConverter)
  {
    if (null != paramExceptionConverter) {
      s_converter = paramExceptionConverter;
    }
  }
  
  public abstract SQLException toSQLException(Exception paramException, IWarningListener paramIWarningListener);
  
  public SQLException toSQLException(Exception paramException, IWarningListener paramIWarningListener, ILogger paramILogger)
  {
    SQLException localSQLException = toSQLException(paramException, paramIWarningListener);
    LogUtilities.logError(localSQLException, paramILogger);
    return localSQLException;
  }
  
  public SQLException toSQLException(JDBCMessageKey paramJDBCMessageKey, IWarningListener paramIWarningListener, ExceptionType paramExceptionType, Object... paramVarArgs)
  {
    return toSQLException(paramJDBCMessageKey, paramIWarningListener, paramExceptionType, null, paramVarArgs);
  }
  
  public SQLException toSQLException(JDBCMessageKey paramJDBCMessageKey, IWarningListener paramIWarningListener, ExceptionType paramExceptionType, Map<String, FailedPropertiesReason> paramMap, Object... paramVarArgs)
  {
    IMessageSource localIMessageSource = paramIWarningListener.getMessageSource();
    String str = localIMessageSource.loadMessage(paramIWarningListener.getLocale(), 1, paramJDBCMessageKey.name(), paramVarArgs);
    int i = 0;
    int j = str.indexOf('(');
    int k = str.indexOf(')');
    if ((j < k) && (-1 != j))
    {
      localObject = str.substring(++j, k);
      try
      {
        i = Integer.valueOf((String)localObject).intValue();
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    Object localObject = Thread.currentThread().getStackTrace();
    StackTraceElement[] arrayOfStackTraceElement = new StackTraceElement[localObject.length - 2];
    System.arraycopy(localObject, 2, arrayOfStackTraceElement, 0, arrayOfStackTraceElement.length);
    return createSQLException(paramJDBCMessageKey.getSQLState(), str, i, arrayOfStackTraceElement, paramExceptionType, paramMap);
  }
  
  public SQLException toSQLException(String paramString1, String paramString2, int paramInt, ExceptionType paramExceptionType)
  {
    return toSQLException(paramString1, paramString2, paramInt, paramExceptionType, null);
  }
  
  public SQLException toSQLException(String paramString1, String paramString2, int paramInt, ExceptionType paramExceptionType, Map<String, FailedPropertiesReason> paramMap)
  {
    StackTraceElement[] arrayOfStackTraceElement1 = Thread.currentThread().getStackTrace();
    StackTraceElement[] arrayOfStackTraceElement2 = new StackTraceElement[arrayOfStackTraceElement1.length - 2];
    System.arraycopy(arrayOfStackTraceElement1, 2, arrayOfStackTraceElement2, 0, arrayOfStackTraceElement2.length);
    return createSQLException(paramString1, paramString2, paramInt, arrayOfStackTraceElement2, paramExceptionType, paramMap);
  }
  
  protected abstract SQLException createSQLException(String paramString1, String paramString2, int paramInt, StackTraceElement[] paramArrayOfStackTraceElement, ExceptionType paramExceptionType, Map<String, FailedPropertiesReason> paramMap);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/exceptions/ExceptionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */