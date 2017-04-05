package com.simba.support;

import com.simba.support.exceptions.ErrorException;
import java.util.Locale;

public abstract interface ILogger
{
  public abstract Locale getLocale();
  
  public abstract LogLevel getLogLevel();
  
  public abstract boolean isEnabled();
  
  public abstract void logDebug(String paramString1, String paramString2, String paramString3, ErrorException paramErrorException);
  
  public abstract void logDebug(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void logError(String paramString1, String paramString2, String paramString3, ErrorException paramErrorException);
  
  public abstract void logError(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void logFatal(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void logFunctionEntrance(String paramString1, String paramString2, String paramString3);
  
  public abstract void logInfo(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void logTrace(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void logWarning(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void setLocale(Locale paramLocale);
  
  public abstract void setLogLevel(LogLevel paramLogLevel);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/ILogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */