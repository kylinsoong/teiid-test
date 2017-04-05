package com.simba.support;

import com.simba.support.exceptions.ErrorException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

public class LogUtilities
{
  public static void logDebug(ErrorException paramErrorException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.DEBUG, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logDebug");
    paramILogger.logDebug(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramErrorException);
  }
  
  public static void logDebug(Exception paramException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.DEBUG, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logDebug");
    paramILogger.logDebug(arrayOfString[0], arrayOfString[1], arrayOfString[2], getExceptionMessage(paramException));
  }
  
  public static void logDebug(String paramString, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.DEBUG, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logDebug");
    paramILogger.logDebug(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramString);
  }
  
  public static void logError(ErrorException paramErrorException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.ERROR, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logError");
    paramILogger.logError(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramErrorException);
  }
  
  public static void logError(Exception paramException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.ERROR, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logError");
    paramILogger.logError(arrayOfString[0], arrayOfString[1], arrayOfString[2], getExceptionMessage(paramException));
  }
  
  public static void logError(String paramString, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.ERROR, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logError");
    paramILogger.logError(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramString);
  }
  
  public static void logFatal(Exception paramException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.FATAL, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logFatal");
    paramILogger.logFatal(arrayOfString[0], arrayOfString[1], arrayOfString[2], getExceptionMessage(paramException));
  }
  
  public static void logFatal(String paramString, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.FATAL, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logFatal");
    paramILogger.logFatal(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramString);
  }
  
  public static void logFunctionEntrance(ILogger paramILogger, Object... paramVarArgs)
  {
    if (!shouldLogLevel(LogLevel.TRACE, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logFunctionEntrance");
    paramILogger.logFunctionEntrance(arrayOfString[0], arrayOfString[1], arrayOfString[2] + formatArguments(paramVarArgs));
  }
  
  public static void logInfo(Exception paramException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.INFO, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logInfo");
    paramILogger.logInfo(arrayOfString[0], arrayOfString[1], arrayOfString[2], getExceptionMessage(paramException));
  }
  
  public static void logInfo(String paramString, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.INFO, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logInfo");
    paramILogger.logInfo(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramString);
  }
  
  public static void logTrace(Exception paramException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.TRACE, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logTrace");
    paramILogger.logTrace(arrayOfString[0], arrayOfString[1], arrayOfString[2], getExceptionMessage(paramException));
  }
  
  public static void logTrace(String paramString, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.TRACE, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logTrace");
    paramILogger.logTrace(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramString);
  }
  
  public static void logWarning(Exception paramException, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.WARNING, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logWarning");
    paramILogger.logWarning(arrayOfString[0], arrayOfString[1], arrayOfString[2], getExceptionMessage(paramException));
  }
  
  public static void logWarning(String paramString, ILogger paramILogger)
  {
    if (!shouldLogLevel(LogLevel.WARNING, paramILogger)) {
      return;
    }
    String[] arrayOfString = getNames("logWarning");
    paramILogger.logWarning(arrayOfString[0], arrayOfString[1], arrayOfString[2], paramString);
  }
  
  public static boolean shouldLogLevel(LogLevel paramLogLevel, ILogger paramILogger)
  {
    return (paramILogger.isEnabled()) && (paramLogLevel.ordinal() <= paramILogger.getLogLevel().ordinal());
  }
  
  private static String formatArguments(Object... paramVarArgs)
  {
    StringBuilder localStringBuilder = new StringBuilder("(");
    if (null == paramVarArgs)
    {
      localStringBuilder.append("null");
    }
    else
    {
      int i = 1;
      for (Object localObject1 : paramVarArgs)
      {
        if (i == 0) {
          localStringBuilder.append(", ");
        }
        i = 0;
        if (null == localObject1)
        {
          localStringBuilder.append("null");
        }
        else if ((localObject1 instanceof String))
        {
          localStringBuilder.append("\"").append(localObject1.toString()).append("\"");
        }
        else if ((localObject1 instanceof Iterable))
        {
          localStringBuilder.append("<");
          i = 1;
          Iterator localIterator = ((Iterable)localObject1).iterator();
          while (localIterator.hasNext())
          {
            if (i == 0) {
              localStringBuilder.append(", ");
            }
            i = 0;
            Object localObject2 = localIterator.next();
            if (null == localObject2) {
              localStringBuilder.append("null");
            } else {
              localStringBuilder.append(localObject2.toString());
            }
          }
          i = 0;
          localStringBuilder.append(">");
        }
        else
        {
          localStringBuilder.append(localObject1.toString());
        }
      }
    }
    return ")";
  }
  
  private static String getExceptionMessage(Exception paramException)
  {
    StringWriter localStringWriter = new StringWriter();
    paramException.printStackTrace(new PrintWriter(localStringWriter));
    return paramException.getLocalizedMessage() + "\n" + localStringWriter.toString();
  }
  
  private static String[] getNames(String paramString)
  {
    StackTraceElement localStackTraceElement = getStackElementAbove(paramString);
    String[] arrayOfString = new String[3];
    arrayOfString[2] = localStackTraceElement.getMethodName();
    try
    {
      Class localClass = Class.forName(localStackTraceElement.getClassName());
      arrayOfString[1] = localClass.getSimpleName();
      arrayOfString[0] = "";
      Package localPackage = localClass.getPackage();
      if (null != localPackage) {
        arrayOfString[0] = localPackage.getName();
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      arrayOfString[0] = "<error>";
      arrayOfString[1] = localStackTraceElement.getClassName();
    }
    if (arrayOfString[2].equals("<init>")) {
      arrayOfString[2] = arrayOfString[1];
    }
    return arrayOfString;
  }
  
  private static StackTraceElement getStackElementAbove(String paramString)
  {
    int i = 0;
    for (StackTraceElement localStackTraceElement : Thread.currentThread().getStackTrace())
    {
      if (i != 0) {
        return localStackTraceElement;
      }
      if (localStackTraceElement.getMethodName().equals(paramString)) {
        i = 1;
      }
    }
    return Thread.currentThread().getStackTrace()[3];
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/LogUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */