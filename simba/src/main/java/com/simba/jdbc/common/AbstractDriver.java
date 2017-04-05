package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriverFactory;
import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.utilities.ConnSettingRequestMap;
import com.simba.dsi.core.utilities.ConnSettingResponseMap;
import com.simba.dsi.core.utilities.Variant;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.SettingReader;
import com.simba.support.Warning;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.FunctionID;
import com.simba.utilities.PropertyTypeConverter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public abstract class AbstractDriver
  extends BaseConnectionFactory
  implements Driver
{
  private static final String VERSION_SEPARATOR = ".";
  public static final String URL_PROTOCOL = "jdbc";
  private static boolean s_isInitialized = false;
  private static String s_messageComponentName = "[JDBC Driver]";
  private SWarningListener m_warningListener;
  
  public static String getErrorMessageComponentName()
  {
    return s_messageComponentName;
  }
  
  public static void initialize(AbstractDriver paramAbstractDriver, String paramString)
    throws SQLException
  {
    DSIDriverFactory.setDriverClassName(paramString);
    try
    {
      DriverManager.registerDriver(paramAbstractDriver);
      s_isInitialized = true;
    }
    catch (SQLException localSQLException)
    {
      ExceptionConverter.getInstance().toSQLException("HY000", "A database access error occurred while registering the driver", 0, ExceptionType.DEFAULT);
    }
  }
  
  public static void setErrorMessageComponentName(String paramString)
  {
    s_messageComponentName = "[" + paramString + "]";
  }
  
  public AbstractDriver()
  {
    SettingReader.loadSimbaSettings();
  }
  
  public final boolean acceptsURL(String paramString)
  {
    return acceptsURL(paramString, new Properties());
  }
  
  public Connection connect(String paramString, Properties paramProperties)
    throws SQLException
  {
    Properties localProperties = new Properties();
    if (null != paramProperties) {
      copyProperties(localProperties, paramProperties);
    }
    doInitialize();
    if (!acceptsURL(paramString, localProperties)) {
      return null;
    }
    Pair localPair = getConnection(localProperties);
    return doConnect(localPair, paramString);
  }
  
  public int getMajorVersion()
  {
    int i = 1;
    try
    {
      doInitialize();
      IDriver localIDriver = DSIDriverSingleton.getInstance();
      Variant localVariant = localIDriver.getProperty(5);
      if (null == localVariant.getString()) {
        ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_VERSION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { localVariant.getString() });
      }
      StringTokenizer localStringTokenizer = new StringTokenizer(localVariant.getString(), ".");
      String str = localStringTokenizer.nextToken();
      i = Integer.parseInt(str);
    }
    catch (Throwable localThrowable) {}
    return i;
  }
  
  public int getMinorVersion()
  {
    int i = 0;
    try
    {
      doInitialize();
      IDriver localIDriver = DSIDriverSingleton.getInstance();
      Variant localVariant = localIDriver.getProperty(5);
      if (null == localVariant.getString()) {
        ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_VERSION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { localVariant.getString() });
      }
      StringTokenizer localStringTokenizer = new StringTokenizer(localVariant.getString(), ".");
      localStringTokenizer.nextToken();
      String str = localStringTokenizer.nextToken();
      i = Integer.parseInt(str);
    }
    catch (Throwable localThrowable) {}
    return i;
  }
  
  public DriverPropertyInfo[] getPropertyInfo(String paramString, Properties paramProperties)
    throws SQLException
  {
    Properties localProperties = new Properties();
    if (null != paramProperties) {
      copyProperties(localProperties, paramProperties);
    }
    doInitialize();
    if (!acceptsURL(paramString, localProperties)) {
      ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_URL, this.m_warningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
    }
    try
    {
      Pair localPair = getConnection(localProperties);
      IConnection localIConnection = (IConnection)localPair.key();
      Object localObject = localIConnection.getWarningListener();
      ConnSettingResponseMap localConnSettingResponseMap = null;
      if (null == localObject)
      {
        localObject = new SWarningListener(DSIDriverSingleton.getInstance().getMessageSource(), FunctionID.CONNECTION_UPDATE_SETTINGS);
        localIConnection.registerWarningListener((IWarningListener)localObject);
      }
      localConnSettingResponseMap = localIConnection.updateConnectionSettings((ConnSettingRequestMap)localPair.value());
      Iterator localIterator = ((IWarningListener)localObject).getWarnings().iterator();
      while (localIterator.hasNext())
      {
        Warning localWarning = (Warning)localIterator.next();
        LogUtilities.logWarning(localWarning.getMessage(), DSIDriverSingleton.getInstance().getDriverLog());
      }
      localIConnection.disconnect();
      localIConnection.close();
      SettingReader.clearAllSettings();
      return PropertyTypeConverter.toDriverPropertyInfo(localConnSettingResponseMap);
    }
    catch (Exception localException)
    {
      SettingReader.clearAllSettings();
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  public boolean jdbcCompliant()
  {
    return false;
  }
  
  protected abstract JDBCObjectFactory createJDBCObjectFactory();
  
  private void copyProperties(Properties paramProperties1, Properties paramProperties2)
  {
    Enumeration localEnumeration = paramProperties2.propertyNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      paramProperties1.put(str, paramProperties2.get(str));
    }
  }
  
  private void doInitialize()
    throws SQLException
  {
    if (!s_isInitialized) {
      ExceptionConverter.getInstance().toSQLException("HY000", "Class has not been initialized.Please initialize by calling AbstractDriver.initialize()", 0, ExceptionType.DEFAULT);
    } else if (null == DSIDriverSingleton.getInstance()) {
      try
      {
        DSIDriverSingleton.setInstance(DSIDriverFactory.createDriver());
        this.m_warningListener = new SWarningListener(DSIDriverSingleton.getInstance().getMessageSource(), FunctionID.BEFORE_CONNECTION);
      }
      catch (ErrorException localErrorException)
      {
        SQLException localSQLException = new SQLException(localErrorException.getLocalizedMessage());
        localSQLException.initCause(localErrorException);
        throw localSQLException;
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/AbstractDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */