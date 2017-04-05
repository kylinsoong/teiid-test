package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriverFactory;
import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.utilities.Variant;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.LogLevel;
import com.simba.support.Pair;
import com.simba.support.exceptions.ExceptionType;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

public abstract class AbstractDataSource
  extends BaseConnectionFactory
  implements DataSource, ConnectionPoolDataSource
{
  private static boolean s_isInitialized = false;
  private String m_url = null;
  private String m_password = null;
  private String m_userID = null;
  private LogLevel m_logLevel = LogLevel.OFF;
  private String m_logDirectory = "";
  private Map<String, String> m_customProperties = new HashMap();
  
  public static void initialize(String paramString)
  {
    DSIDriverFactory.setDriverClassName(paramString);
    s_isInitialized = true;
  }
  
  public Connection getConnection()
    throws SQLException
  {
    return getSimbaConnection();
  }
  
  public Connection getConnection(String paramString1, String paramString2)
    throws SQLException
  {
    this.m_userID = paramString1;
    this.m_password = paramString2;
    return getConnection();
  }
  
  public String getCustomProperty(String paramString)
  {
    return (String)this.m_customProperties.get(paramString);
  }
  
  public String getLogDirectory()
  {
    return this.m_logDirectory;
  }
  
  public int getLoginTimeout()
  {
    return DriverManager.getLoginTimeout();
  }
  
  public LogLevel getLogLevel()
  {
    return this.m_logLevel;
  }
  
  public PrintWriter getLogWriter()
  {
    return DriverManager.getLogWriter();
  }
  
  public String getPassword()
  {
    return this.m_password;
  }
  
  public PooledConnection getPooledConnection()
    throws SQLException
  {
    SConnection localSConnection = getSimbaConnection();
    if (null != localSConnection) {
      return JDBCObjectFactory.getInstance().createPooledConnection(localSConnection);
    }
    SWarningListener localSWarningListener = new SWarningListener(DSIDriverSingleton.getInstance().getMessageSource(), null);
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_REFUSED, localSWarningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
  }
  
  public PooledConnection getPooledConnection(String paramString1, String paramString2)
    throws SQLException
  {
    this.m_userID = paramString1;
    this.m_password = paramString2;
    return getPooledConnection();
  }
  
  public String getUserID()
  {
    return this.m_userID;
  }
  
  public void removeCustomProperty(String paramString)
  {
    this.m_customProperties.remove(paramString);
  }
  
  public void setCustomProperty(String paramString1, String paramString2)
  {
    this.m_customProperties.put(paramString1, paramString2);
  }
  
  public void setLogDirectory(String paramString)
  {
    this.m_logDirectory = paramString;
  }
  
  public void setLoginTimeout(int paramInt)
  {
    DriverManager.setLoginTimeout(paramInt);
  }
  
  public void setLogLevel(String paramString)
  {
    this.m_logLevel = LogLevel.getLogLevel(paramString);
  }
  
  public void setLogWriter(PrintWriter paramPrintWriter)
  {
    DriverManager.setLogWriter(paramPrintWriter);
  }
  
  public void setPassword(String paramString)
  {
    this.m_password = paramString;
  }
  
  public void setURL(String paramString)
  {
    this.m_url = paramString;
  }
  
  public void setUserID(String paramString)
  {
    this.m_userID = paramString;
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  protected Properties getProperties()
  {
    Properties localProperties = new Properties();
    String str1 = "USER";
    String str2 = "PASSWORD";
    IDriver localIDriver = DSIDriverSingleton.getInstance();
    assert (null != localIDriver);
    try
    {
      Variant localVariant = localIDriver.getProperty(1000);
      if (1 == localVariant.getShort())
      {
        str1 = "UID";
        str2 = "PWD";
      }
    }
    catch (Exception localException) {}
    if (null != this.m_userID) {
      localProperties.put(str1, this.m_userID);
    } else {
      localProperties.put(str1, "");
    }
    if (null != this.m_password) {
      localProperties.put(str2, this.m_password);
    } else {
      localProperties.put(str2, "");
    }
    if (null != this.m_logLevel) {
      localProperties.put("LogLevel", this.m_logLevel.name());
    }
    if (null != this.m_logDirectory) {
      localProperties.put("LogPath", this.m_logDirectory);
    }
    localProperties.putAll(this.m_customProperties);
    return localProperties;
  }
  
  protected abstract String getSubProtocol();
  
  private void doInitialize()
    throws SQLException
  {
    if (!s_isInitialized) {
      ExceptionConverter.getInstance().toSQLException("HY000", "Class has not been initialized. Please initialize by calling AbstractDriver.initialize()", 0, ExceptionType.DEFAULT);
    } else if (null == DSIDriverSingleton.getInstance()) {
      try
      {
        DSIDriverSingleton.setInstance(DSIDriverFactory.createDriver());
      }
      catch (Exception localException)
      {
        SQLException localSQLException = new SQLException(localException.getLocalizedMessage());
        localSQLException.initCause(localException);
        throw localSQLException;
      }
    }
  }
  
  private SConnection getSimbaConnection()
    throws SQLException
  {
    doInitialize();
    Properties localProperties = getProperties();
    if (!acceptsURL(getURL(), localProperties)) {
      return null;
    }
    Pair localPair = getConnection(localProperties);
    return doConnect(localPair, getURL());
  }
  
  private String getURL()
    throws SQLException
  {
    if (null != this.m_url) {
      return this.m_url;
    }
    StringBuffer localStringBuffer = new StringBuffer("jdbc");
    localStringBuffer.append(":").append(getSubProtocol());
    return localStringBuffer.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/AbstractDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */