package com.simba.support;

import java.util.ArrayList;

public enum LogLevel
{
  OFF,  FATAL,  ERROR,  WARNING,  INFO,  DEBUG,  TRACE;
  
  private static ArrayList<String> m_names;
  
  private LogLevel() {}
  
  public static LogLevel getLogLevel(int paramInt)
  {
    switch (paramInt)
    {
    case 1: 
      return FATAL;
    case 2: 
      return ERROR;
    case 3: 
      return WARNING;
    case 4: 
      return INFO;
    case 5: 
      return DEBUG;
    case 6: 
      return TRACE;
    }
    return OFF;
  }
  
  public static LogLevel getLogLevel(String paramString)
  {
    LogLevel localLogLevel = OFF;
    if ((null == paramString) || (paramString.equals(""))) {
      return localLogLevel;
    }
    if (paramString.equalsIgnoreCase("OFF")) {
      localLogLevel = OFF;
    }
    if (paramString.equalsIgnoreCase("FATAL")) {
      localLogLevel = FATAL;
    } else if (paramString.equalsIgnoreCase("ERROR")) {
      localLogLevel = ERROR;
    } else if (paramString.equalsIgnoreCase("WARNING")) {
      localLogLevel = WARNING;
    } else if (paramString.equalsIgnoreCase("INFO")) {
      localLogLevel = INFO;
    } else if (paramString.equalsIgnoreCase("DEBUG")) {
      localLogLevel = DEBUG;
    } else if (paramString.equalsIgnoreCase("TRACE")) {
      localLogLevel = TRACE;
    } else {
      try
      {
        localLogLevel = getLogLevel(Integer.parseInt(paramString));
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    return localLogLevel;
  }
  
  static
  {
    m_names = new ArrayList();
    m_names.add("OFF");
    m_names.add("FATAL");
    m_names.add("ERROR");
    m_names.add("WARNING");
    m_names.add("INFO");
    m_names.add("DEBUG");
    m_names.add("TRACE");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/LogLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */