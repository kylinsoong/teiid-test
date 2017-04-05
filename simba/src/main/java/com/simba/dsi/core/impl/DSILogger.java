package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.LogLevel;
import com.simba.support.SettingReader;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.ExceptionUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DSILogger
  implements ILogger
{
  public static final String LOG_PRINT_WRITER_KEY = "LogPrintWriter";
  private static final String LOG_FILE_EXTENSION = ".log";
  private LogLevel m_level = LogLevel.OFF;
  private String m_fileName;
  private Locale m_locale;
  private String m_package = "";
  private PrintWriter m_logWriter = null;
  private List<String> m_logCreationErrors = new ArrayList();
  
  public DSILogger(String paramString)
  {
    prepareSettings(paramString);
    this.m_locale = ExceptionUtilities.createLocale(SettingReader.readSetting("DriverLocale"));
  }
  
  public synchronized String getFileName()
  {
    if (null != this.m_fileName)
    {
      int i = this.m_fileName.lastIndexOf(File.separator);
      if (-1 != i) {
        return this.m_fileName.substring(i + 1);
      }
    }
    return this.m_fileName;
  }
  
  public synchronized Locale getLocale()
  {
    return this.m_locale;
  }
  
  public synchronized LogLevel getLogLevel()
  {
    return this.m_level;
  }
  
  public synchronized boolean isEnabled()
  {
    return this.m_level != LogLevel.OFF;
  }
  
  public void logDebug(String paramString1, String paramString2, String paramString3, ErrorException paramErrorException)
  {
    logDebug(paramString1, paramString2, paramString3, getExceptionMessage(paramErrorException));
  }
  
  public void logDebug(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = formatLogLine(LogLevel.DEBUG, paramString1, paramString2, paramString3, paramString4);
    if (null != str) {
      writeLogLine(str);
    }
  }
  
  public void logError(String paramString1, String paramString2, String paramString3, ErrorException paramErrorException)
  {
    logError(paramString1, paramString2, paramString3, getExceptionMessage(paramErrorException));
  }
  
  public void logError(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = formatLogLine(LogLevel.ERROR, paramString1, paramString2, paramString3, paramString4);
    if (null != str) {
      writeLogLine(str);
    }
  }
  
  public void logFatal(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = formatLogLine(LogLevel.FATAL, paramString1, paramString2, paramString3, paramString4);
    if (null != str) {
      writeLogLine(str);
    }
  }
  
  public void logFunctionEntrance(String paramString1, String paramString2, String paramString3)
  {
    String str = formatLogLine(LogLevel.TRACE, paramString1, paramString2, paramString3, "+++++ enter +++++");
    if (null != str) {
      writeLogLine(str);
    }
  }
  
  public void logInfo(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = formatLogLine(LogLevel.INFO, paramString1, paramString2, paramString3, paramString4);
    if (null != str) {
      writeLogLine(str);
    }
  }
  
  public void logTrace(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = formatLogLine(LogLevel.TRACE, paramString1, paramString2, paramString3, paramString4);
    if (null != str)
    {
      writeLogLine(str);
      Throwable localThrowable = new Throwable();
      StackTraceElement[] arrayOfStackTraceElement = localThrowable.getStackTrace();
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 1; i < arrayOfStackTraceElement.length; i++)
      {
        localStringBuffer.append("   at ");
        localStringBuffer.append(arrayOfStackTraceElement[i].toString());
        localStringBuffer.append("\n");
      }
      writeLogLine(localStringBuffer.toString());
    }
  }
  
  public void logWarning(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = formatLogLine(LogLevel.WARNING, paramString1, paramString2, paramString3, paramString4);
    if (null != str) {
      writeLogLine(str);
    }
  }
  
  public synchronized void prepareSettings(String paramString)
  {
    String str1 = "";
    String str2 = SettingReader.readSetting("LogLevel");
    this.m_level = LogLevel.getLogLevel(str2);
    str1 = SettingReader.readSetting("LogPath");
    str1 = resolveDirectory(str1);
    if (!paramString.contains(".")) {
      paramString = paramString + ".log";
    }
    this.m_fileName = (str1 + paramString);
    this.m_package = SettingReader.readSetting("LogNamespace");
    if (this.m_package == null) {
      this.m_package = "";
    }
    prepareLogWriter();
  }
  
  public synchronized void setLocale(Locale paramLocale)
  {
    this.m_locale = paramLocale;
  }
  
  public synchronized void setLogLevel(LogLevel paramLogLevel)
  {
    this.m_level = paramLogLevel;
    if (LogLevel.OFF != paramLogLevel) {
      prepareLogWriter();
    }
  }
  
  public synchronized void setLogPackage(String paramString)
  {
    this.m_package = paramString;
  }
  
  private String formatLogLine(LogLevel paramLogLevel, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (null == this.m_logWriter) {
      return null;
    }
    String str = null;
    if ((isInLevel(paramLogLevel)) && (isInPackage(paramString1)))
    {
      StringBuffer localStringBuffer = new StringBuffer();
      SimpleDateFormat localSimpleDateFormat = null;
      localSimpleDateFormat = new SimpleDateFormat("MMM dd HH:mm:ss.SSS");
      localSimpleDateFormat.format(new Date(), localStringBuffer, new FieldPosition(0));
      localStringBuffer.append(" ");
      switch (paramLogLevel)
      {
      case FATAL: 
        localStringBuffer.append("FATAL");
        break;
      case ERROR: 
        localStringBuffer.append("ERROR");
        break;
      case WARNING: 
        localStringBuffer.append("WARN ");
        break;
      case INFO: 
        localStringBuffer.append("INFO ");
        break;
      case DEBUG: 
        localStringBuffer.append("DEBUG");
        break;
      case TRACE: 
        localStringBuffer.append("TRACE");
        break;
      default: 
        localStringBuffer.append("LEVEL");
      }
      localStringBuffer.append(" ");
      localStringBuffer.append(Thread.currentThread().getId()).append(" ");
      localStringBuffer.append(paramString1);
      localStringBuffer.append(".");
      localStringBuffer.append(paramString2);
      localStringBuffer.append(".");
      localStringBuffer.append(paramString3);
      localStringBuffer.append(": ");
      localStringBuffer.append(paramString4);
      str = localStringBuffer.toString();
    }
    return str;
  }
  
  private String getExceptionMessage(ErrorException paramErrorException)
  {
    paramErrorException.loadMessage(DSIDriverSingleton.getInstance().getMessageSource(), this.m_locale);
    StringWriter localStringWriter = new StringWriter();
    paramErrorException.printStackTrace(new PrintWriter(localStringWriter));
    return paramErrorException.getMessage() + "\n" + localStringWriter.toString();
  }
  
  private boolean isInLevel(LogLevel paramLogLevel)
  {
    return paramLogLevel.ordinal() <= this.m_level.ordinal();
  }
  
  private boolean isInPackage(String paramString)
  {
    if (null == paramString) {
      return false;
    }
    return paramString.startsWith(this.m_package);
  }
  
  private void prepareLogWriter()
  {
    this.m_logWriter = ((PrintWriter)SettingReader.readAdditionalSetting("LogPrintWriter"));
    if ((isEnabled()) && (null == this.m_logWriter))
    {
      Object localObject1;
      Object localObject2;
      if ((null != this.m_fileName) && (0 != this.m_fileName.length()) && (this.m_logCreationErrors.isEmpty())) {
        try
        {
          int i = this.m_fileName.lastIndexOf(File.separator);
          if (-1 != i)
          {
            localObject1 = this.m_fileName.substring(0, i);
            localObject2 = new File((String)localObject1);
            if ((!((File)localObject2).exists()) && (!((File)localObject2).mkdir())) {
              throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CANNOT_CREATE_LOGGING_PATH.name(), (String)localObject1, ExceptionType.DEFAULT);
            }
          }
          localObject1 = new FileOutputStream(this.m_fileName, true);
          this.m_logWriter = new PrintWriter((OutputStream)localObject1);
          return;
        }
        catch (Exception localException)
        {
          this.m_logCreationErrors.add(localException.getLocalizedMessage());
        }
      }
      this.m_logWriter = new PrintWriter(System.out);
      Iterator localIterator = this.m_logCreationErrors.iterator();
      while (localIterator.hasNext())
      {
        localObject1 = (String)localIterator.next();
        localObject2 = DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DEFAULT_LOGGING.name(), (String)localObject1, ExceptionType.DEFAULT);
        ((ErrorException)localObject2).loadMessage(DSIDriverSingleton.getInstance().getMessageSource(), DSIDriverSingleton.getInstance().getLocale());
        this.m_logWriter.println(((ErrorException)localObject2).getMessage());
      }
    }
  }
  
  private String resolveDirectory(String paramString)
  {
    String str = "";
    try
    {
      if (null != paramString)
      {
        File localFile = new File(paramString);
        str = localFile.getCanonicalPath();
        if (!str.endsWith(File.separator)) {
          str = str + File.separator;
        }
      }
    }
    catch (Exception localException)
    {
      this.m_logCreationErrors.add(localException.getLocalizedMessage());
    }
    return str;
  }
  
  private synchronized void writeLogLine(String paramString)
  {
    this.m_logWriter.println(paramString);
    this.m_logWriter.flush();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/impl/DSILogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */