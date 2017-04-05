package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.impl.DSILogger;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.core.utilities.ConnSettingRequestMap;
import com.simba.dsi.core.utilities.ConnSettingResponseMap;
import com.simba.dsi.core.utilities.ConnectionSetting;
import com.simba.dsi.core.utilities.Variant;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageBuilder;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.SettingReader;
import com.simba.support.Warning;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.ExceptionUtilities;
import com.simba.support.security.ICredentialFactory;
import com.simba.utilities.FunctionID;
import com.simba.utilities.PropertyTypeConverter;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public abstract class BaseConnectionFactory
{
  public static final String URL_SEPARATOR = ":";
  public static final String CONNECTION_LOCALE_KEY = "Locale";
  public static ExceptionBuilder s_Messages = new ExceptionBuilder(1);
  
  protected boolean acceptsSubProtocol(String paramString)
  {
    String str = getSubProtocol();
    if (null == str) {
      return false;
    }
    return str.equals(paramString);
  }
  
  protected final boolean acceptsURL(String paramString, Properties paramProperties)
  {
    try
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
      String str1 = localStringTokenizer.nextToken();
      String str2 = localStringTokenizer.nextToken();
      if (("jdbc".equals(str1)) && (acceptsSubProtocol(str2)))
      {
        int i = paramString.indexOf(str2) + str2.length() + 1;
        String str3 = "";
        if (i < paramString.length()) {
          str3 = paramString.substring(i);
        }
        SettingReader.clearAllSettings();
        if (SettingReader.isSuccessfulLoad()) {
          paramProperties.putAll(SettingReader.retrieveAllSettings());
        }
        return parseSubName(str3, paramProperties);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return false;
  }
  
  protected abstract JDBCObjectFactory createJDBCObjectFactory();
  
  protected SConnection doConnect(Pair<IConnection, ConnSettingRequestMap> paramPair, String paramString)
    throws SQLException
  {
    assert (null != paramPair);
    IConnection localIConnection = (IConnection)paramPair.key();
    Object localObject1 = localIConnection.getWarningListener();
    try
    {
      if (null == JDBCObjectFactory.getInstance()) {
        JDBCObjectFactory.setInstance(createJDBCObjectFactory());
      }
      ConnSettingResponseMap localConnSettingResponseMap = null;
      if (null == localObject1)
      {
        localObject1 = new SWarningListener(localIConnection.getMessageSource(), FunctionID.CONNECTION_UPDATE_SETTINGS);
        localIConnection.registerWarningListener((IWarningListener)localObject1);
      }
      localConnSettingResponseMap = localIConnection.updateConnectionSettings((ConnSettingRequestMap)paramPair.value());
      checkResponseMap(localConnSettingResponseMap, (IWarningListener)localObject1);
      localIConnection.connect((ConnSettingRequestMap)paramPair.value());
      localObject2 = ((IWarningListener)localObject1).getWarnings().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Warning localWarning = (Warning)((Iterator)localObject2).next();
        LogUtilities.logWarning(localWarning.getMessage(), DSIDriverSingleton.getInstance().getDriverLog());
      }
      return JDBCObjectFactory.getInstance().createConnection(localIConnection, paramString);
    }
    catch (Exception localException1)
    {
      Object localObject2 = null;
      try
      {
        localObject2 = ((IConnection)paramPair.key()).getConnectionLog();
      }
      catch (Exception localException2) {}
      if (null == localObject2) {
        throw ExceptionConverter.getInstance().toSQLException(localException1, (IWarningListener)localObject1);
      }
      throw ExceptionConverter.getInstance().toSQLException(localException1, (IWarningListener)localObject1, (ILogger)localObject2);
    }
  }
  
  protected Pair<IConnection, ConnSettingRequestMap> getConnection(Properties paramProperties)
    throws SQLException
  {
    assert (null != paramProperties);
    IDriver localIDriver = DSIDriverSingleton.getInstance();
    SWarningListener localSWarningListener = new SWarningListener(localIDriver.getMessageSource(), FunctionID.BEFORE_CONNECTION);
    try
    {
      mapJDBCtoODBCAuthentication(paramProperties);
      saveLoggingAttributes(paramProperties);
      PrintWriter localPrintWriter = DriverManager.getLogWriter();
      if (null != localPrintWriter) {
        SettingReader.storeAdditionalSetting("LogPrintWriter", localPrintWriter);
      }
      ILogger localILogger = localIDriver.getDriverLog();
      if ((localILogger instanceof DSILogger)) {
        ((DSILogger)localILogger).prepareSettings(((DSILogger)localILogger).getFileName());
      }
      IConnection localIConnection = null;
      try
      {
        long l = localIDriver.getProperty(20).getLong();
        IEnvironment localIEnvironment = localIDriver.createEnvironment();
        localIEnvironment.registerWarningListener(localSWarningListener);
        if ((l & 1L) != 0L)
        {
          ICredentialFactory localICredentialFactory = localIDriver.createCredentialFactory();
          if (localICredentialFactory != null) {
            localIConnection = localIEnvironment.createConnection(localICredentialFactory.getLocalCredentials());
          } else {
            localIConnection = localIEnvironment.createConnection();
          }
        }
        else
        {
          localIConnection = localIEnvironment.createConnection();
        }
      }
      catch (Exception localException2)
      {
        try
        {
          throw ExceptionConverter.getInstance().toSQLException(localException2, localSWarningListener, localILogger);
        }
        catch (Exception localException3)
        {
          throw localException2;
        }
      }
      ConnSettingRequestMap localConnSettingRequestMap = PropertyTypeConverter.toConnSettingRequestMap(paramProperties);
      SettingReader.storeSettings(paramProperties);
      if (localConnSettingRequestMap.containsKey("Locale"))
      {
        Variant localVariant = localConnSettingRequestMap.getProperty("Locale");
        localIConnection.setLocale(ExceptionUtilities.createLocale(localVariant.getString()));
      }
      return new Pair(localIConnection, localConnSettingRequestMap);
    }
    catch (Exception localException1)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException1, localSWarningListener);
    }
  }
  
  protected abstract String getSubProtocol();
  
  protected abstract boolean parseSubName(String paramString, Properties paramProperties);
  
  private void checkResponseMap(ConnSettingResponseMap paramConnSettingResponseMap, IWarningListener paramIWarningListener)
    throws SQLException
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    ArrayList localArrayList3 = new ArrayList();
    Iterator localIterator = paramConnSettingResponseMap.getKeysIterator();
    while (localIterator.hasNext())
    {
      localObject = (String)localIterator.next();
      ConnectionSetting localConnectionSetting = paramConnSettingResponseMap.getProperty((String)localObject);
      if (localConnectionSetting.isRequired()) {
        localArrayList1.add(localObject);
      } else if (localConnectionSetting.isOptional()) {
        localArrayList2.add(localObject);
      } else {
        localArrayList3.add(localObject);
      }
    }
    if (localArrayList1.isEmpty()) {
      return;
    }
    Object localObject = new StringBuilder(JDBCMessageBuilder.getMessage(JDBCMessageKey.REQUIRED_KEYS_MSG, paramIWarningListener, new Object[] { Boolean.valueOf(false), Boolean.valueOf(false) }));
    ((StringBuilder)localObject).append(": ");
    localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      ((StringBuilder)localObject).append((String)localIterator.next());
      if (localIterator.hasNext()) {
        ((StringBuilder)localObject).append(", ");
      }
    }
    if (!localArrayList2.isEmpty())
    {
      ((StringBuilder)localObject).append("; ");
      ((StringBuilder)localObject).append(JDBCMessageBuilder.getMessage(JDBCMessageKey.OPTIONAL_KEYS_MSG, paramIWarningListener, new Object[] { Boolean.valueOf(false), Boolean.valueOf(false) }));
      ((StringBuilder)localObject).append(": ");
      localIterator = localArrayList2.iterator();
      while (localIterator.hasNext())
      {
        ((StringBuilder)localObject).append((String)localIterator.next());
        if (localIterator.hasNext()) {
          ((StringBuilder)localObject).append(", ");
        }
      }
    }
    if (!localArrayList3.isEmpty())
    {
      ((StringBuilder)localObject).append("; ");
      ((StringBuilder)localObject).append(JDBCMessageBuilder.getMessage(JDBCMessageKey.PROCESSED_KEYS_MSG, paramIWarningListener, new Object[] { Boolean.valueOf(false), Boolean.valueOf(false) }));
      ((StringBuilder)localObject).append(": ");
      localIterator = localArrayList3.iterator();
      while (localIterator.hasNext())
      {
        ((StringBuilder)localObject).append((String)localIterator.next());
        if (localIterator.hasNext()) {
          ((StringBuilder)localObject).append(", ");
        }
      }
    }
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_REFUSED, paramIWarningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[] { ((StringBuilder)localObject).toString() });
  }
  
  private void mapJDBCtoODBCAuthentication(Properties paramProperties)
  {
    IDriver localIDriver = DSIDriverSingleton.getInstance();
    assert (null != localIDriver);
    try
    {
      Variant localVariant = localIDriver.getProperty(1000);
      if (1 != localVariant.getShort()) {
        return;
      }
    }
    catch (Exception localException)
    {
      return;
    }
    Enumeration localEnumeration = paramProperties.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str1 = (String)localEnumeration.nextElement();
      String str2;
      if (str1.equalsIgnoreCase("USER"))
      {
        str2 = paramProperties.getProperty(str1);
        paramProperties.remove(str1);
        paramProperties.setProperty("UID", str2);
      }
      else if (str1.equalsIgnoreCase("PASSWORD"))
      {
        str2 = paramProperties.getProperty(str1);
        paramProperties.remove(str1);
        paramProperties.setProperty("PWD", str2);
      }
    }
  }
  
  private void saveLoggingAttributes(Properties paramProperties)
  {
    Enumeration localEnumeration = paramProperties.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (str.regionMatches(true, 0, "log", 0, 3)) {
        SettingReader.storeSetting(str, paramProperties.get(str).toString());
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/BaseConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */