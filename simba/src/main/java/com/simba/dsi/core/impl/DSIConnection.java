package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.core.interfaces.ITransactionStateListener;
import com.simba.dsi.core.utilities.AttributeDataMap;
import com.simba.dsi.core.utilities.ClientInfoData;
import com.simba.dsi.core.utilities.ConnSettingRequestMap;
import com.simba.dsi.core.utilities.ConnSettingResponseMap;
import com.simba.dsi.core.utilities.ConnectionSetting;
import com.simba.dsi.core.utilities.ConnectionSettingInfo;
import com.simba.dsi.core.utilities.PromptType;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.BadAuthException;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.SettingReader;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ClientInfoException;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.ExceptionUtilities;
import com.simba.support.exceptions.FailedPropertiesReason;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class DSIConnection
  implements IConnection
{
  public static final String REQ_INDICATOR = "?";
  protected ITransactionStateListener m_transactionListener;
  protected AttributeDataMap m_connectionProperties = new AttributeDataMap();
  private IEnvironment m_environment;
  private IWarningListener m_warningListener = null;
  private Locale m_locale = null;
  protected Map<String, ClientInfoData> m_clientInfoProperties = initializeClientInfoProperties();
  protected Map<Long, ConnectionSettingInfo> m_connectionSettingInfo = new HashMap<>();
  
  protected DSIConnection(IEnvironment paramIEnvironment)
    throws ErrorException
  {
    this.m_environment = paramIEnvironment;
    try
    {
      loadProperties();
      this.m_locale = ExceptionUtilities.createLocale(SettingReader.readSetting("DriverLocale"));
      LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramIEnvironment });
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.NON_TRANSIENT_CONNECTION);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.NON_TRANSIENT_CONNECTION);
    }
  }
  
  public void beginTransaction()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[0]);
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void commit()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[0]);
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void createSavepoint(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramString });
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void setClientInfoProperty(ClientInfoData paramClientInfoData)
    throws ErrorException
  {
    this.m_clientInfoProperties.put(paramClientInfoData.getName(), paramClientInfoData);
  }
  
  public String getClientInfo(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramString });
    if (null != this.m_clientInfoProperties.get(paramString)) {
      return ((ClientInfoData)this.m_clientInfoProperties.get(paramString)).getValue();
    }
    return null;
  }
  
  public Map<String, ClientInfoData> getClientInfoProperties()
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[0]);
    return this.m_clientInfoProperties;
  }
  
  public Map<Long, ConnectionSettingInfo> getConnectionSettingInfo()
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[0]);
    return this.m_connectionSettingInfo;
  }
  
  public Variant getCustomProperty(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { Integer.valueOf(paramInt) });
    return null;
  }
  
  public int getCustomPropertyType(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { Integer.valueOf(paramInt) });
    return 0;
  }
  
  public String getDataSourceName()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[0]);
    try
    {
      return getProperty(41).getString();
    }
    catch (BadPropertyKeyException localBadPropertyKeyException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPKEY.name(), "Data Source Name", localBadPropertyKeyException, ExceptionType.DEFAULT);
    }
  }
  
  public Locale getLocale()
  {
    return this.m_locale;
  }
  
  public void setLocale(Locale paramLocale)
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramLocale });
    this.m_locale = paramLocale;
  }
  
  public IMessageSource getMessageSource()
  {
    return this.m_environment.getMessageSource();
  }
  
  public IEnvironment getParentEnvironment()
  {
    return this.m_environment;
  }
  
  public Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { Integer.valueOf(paramInt) });
    if (null == this.m_connectionProperties) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    if (this.m_connectionProperties.isProperty(paramInt)) {
      return this.m_connectionProperties.getProperty(paramInt);
    }
    throw new BadPropertyKeyException(2, DSIMessageKey.INVALID_PROPKEY.name(), String.valueOf(paramInt));
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public boolean isAlive()
  {
    Variant localVariant = this.m_connectionProperties.getProperty(20);
    assert (null != localVariant);
    try
    {
      return localVariant.getLong() != 1L;
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      return false;
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return false;
  }
  
  public boolean isCustomProperty(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { Integer.valueOf(paramInt) });
    return false;
  }
  
  public boolean promptDialog(ConnSettingResponseMap paramConnSettingResponseMap, ConnSettingRequestMap paramConnSettingRequestMap, long paramLong, PromptType paramPromptType)
  {
    return false;
  }
  
  public void registerTransactionStateListener(ITransactionStateListener paramITransactionStateListener)
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramITransactionStateListener });
    this.m_transactionListener = paramITransactionStateListener;
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramIWarningListener });
    this.m_warningListener = paramIWarningListener;
  }
  
  public void releaseSavepoint(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramString });
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void rollback()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[0]);
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void rollback(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramString });
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void setClientInfoProperty(String paramString1, String paramString2)
    throws ClientInfoException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramString1, paramString2 });
    ClientInfoData localClientInfoData = (ClientInfoData)this.m_clientInfoProperties.get(paramString1);
    Object localObject;
    if (null != localClientInfoData)
    {
      if ((null == paramString2) || (paramString2.length() <= localClientInfoData.getMaxLength()))
      {
        localClientInfoData.setValue(paramString2);
      }
      else
      {
        localObject = paramString2.substring(0, localClientInfoData.getMaxLength() - 1);
        localClientInfoData.setValue((String)localObject);
        this.m_warningListener.postWarning(new Warning(WarningCode.GENERAL_WARNING, 2, DSIMessageKey.BAD_ATTR_VAL.name(), new String[] { paramString1, paramString2 }));
      }
    }
    else
    {
      localObject = new HashMap();
      ((Map)localObject).put(paramString1, FailedPropertiesReason.UNKNOWN_PROPERTY);
      throw ((ClientInfoException)DSIDriver.s_DSIMessages.createClientInfoException(DSIMessageKey.INVALID_PROPKEY.name(), paramString1, (Map)localObject));
    }
  }
  
  public void setCustomProperty(int paramInt, Variant paramVariant)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
  }
  
  public void setInvokerAndClassLoader(Object paramObject1, Object paramObject2) {}
  
  public void setProperty(int paramInt, Variant paramVariant)
    throws BadAttrValException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
    if (null == this.m_connectionProperties) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    this.m_connectionProperties.setProperty(paramInt, paramVariant);
  }
  
  public String toNativeSQL(String paramString)
  {
    LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { paramString });
    return String.valueOf(paramString);
  }
  
  protected Variant getOptionalSetting(String paramString, ConnSettingRequestMap paramConnSettingRequestMap)
  {
    return paramConnSettingRequestMap.getProperty(paramString);
  }
  
  protected Variant getRequiredSetting(String paramString, ConnSettingRequestMap paramConnSettingRequestMap)
    throws BadAuthException
  {
    Variant localVariant = paramConnSettingRequestMap.getProperty(paramString);
    if (null == localVariant) {
      throw new BadAuthException(2, DSIMessageKey.REQ_SETTING_NOT_FOUND.name(), paramString);
    }
    return localVariant;
  }
  
  protected void verifyOptionalSetting(String paramString, ConnSettingRequestMap paramConnSettingRequestMap, ConnSettingResponseMap paramConnSettingResponseMap)
  {
    verifyOptionalSetting(paramString, paramString, paramConnSettingRequestMap, paramConnSettingResponseMap);
  }
  
  protected void verifyOptionalSetting(String paramString1, String paramString2, ConnSettingRequestMap paramConnSettingRequestMap, ConnSettingResponseMap paramConnSettingResponseMap)
  {
    if (null == paramConnSettingRequestMap.getProperty(paramString1))
    {
      ConnectionSetting localConnectionSetting = new ConnectionSetting(0);
      localConnectionSetting.setLabel(paramString2);
      try
      {
        localConnectionSetting.insertValue(new Variant(0, "?"));
      }
      catch (Exception localException1)
      {
        try
        {
          LogUtilities.logError(localException1, getConnectionLog());
        }
        catch (Exception localException2) {}
      }
      paramConnSettingResponseMap.setProperty(paramString1, localConnectionSetting);
    }
  }
  
  protected void verifyRequiredSetting(String paramString, ConnSettingRequestMap paramConnSettingRequestMap, ConnSettingResponseMap paramConnSettingResponseMap)
  {
    verifyRequiredSetting(paramString, paramString, paramConnSettingRequestMap, paramConnSettingResponseMap);
  }
  
  protected void verifyRequiredSetting(String paramString1, String paramString2, ConnSettingRequestMap paramConnSettingRequestMap, ConnSettingResponseMap paramConnSettingResponseMap)
  {
    if (null == paramConnSettingRequestMap.getProperty(paramString1))
    {
      ConnectionSetting localConnectionSetting = new ConnectionSetting(1);
      localConnectionSetting.setLabel(paramString2);
      try
      {
        localConnectionSetting.insertValue(new Variant(0, "?"));
      }
      catch (Exception localException1)
      {
        try
        {
          LogUtilities.logError(localException1, getConnectionLog());
        }
        catch (Exception localException2) {}
      }
      paramConnSettingResponseMap.setProperty(paramString1, localConnectionSetting);
    }
  }
  
  private Map<String, ClientInfoData> initializeClientInfoProperties()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("APPLICATIONNAME", new ClientInfoData("APPLICATIONNAME", 25, null, "The name of the application currently utilizing the connection."));
    localHashMap.put("CLIENTUSER", new ClientInfoData("CLIENTUSER", 25, null, "The name of the user that the application using the connection is performing work for."));
    localHashMap.put("CLIENTHOSTNAME", new ClientInfoData("CLIENTHOSTNAME", 25, null, "The hostname of the computer the application using the connection is running on."));
    return localHashMap;
  }
  
  private void loadProperties()
    throws ErrorException, IncorrectTypeException, NumericOverflowException
  {
    this.m_connectionProperties.setProperty(1, 0, "Y");
    this.m_connectionProperties.setProperty(2, 0, "Y");
    this.m_connectionProperties.setProperty(3, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(4, 3, Long.valueOf(127L));
    this.m_connectionProperties.setProperty(5, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(6, 3, Long.valueOf(256L));
    this.m_connectionProperties.setProperty(7, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(8, 2, Character.valueOf('\001'));
    this.m_connectionProperties.setProperty(9, 0, "Y");
    this.m_connectionProperties.setProperty(10, 0, ".");
    this.m_connectionProperties.setProperty(11, 0, "catalog");
    this.m_connectionProperties.setProperty(12, 3, Long.valueOf(31L));
    this.m_connectionProperties.setProperty(13, 0, "");
    this.m_connectionProperties.setProperty(14, 0, "Y");
    this.m_connectionProperties.setProperty(15, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(16, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(17, 6, Integer.valueOf(0));
    this.m_connectionProperties.setProperty(18, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(19, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(21, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(20, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(22, 0, "");
    this.m_connectionProperties.setProperty(26, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(23, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(24, 3, Long.valueOf(1024L));
    this.m_connectionProperties.setProperty(25, 4, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(27, 3, Long.valueOf(3L));
    this.m_connectionProperties.setProperty(28, 2, Character.valueOf('\002'));
    this.m_connectionProperties.setProperty(29, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(30, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(31, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(32, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(33, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(34, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(35, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(36, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(37, 2, Character.valueOf('\001'));
    this.m_connectionProperties.setProperty(38, 2, Character.valueOf('\001'));
    this.m_connectionProperties.setProperty(39, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(40, 0, "Y");
    this.m_connectionProperties.setProperty(43, 3, Long.valueOf(65535L));
    this.m_connectionProperties.setProperty(41, 0, "TEXT");
    this.m_connectionProperties.setProperty(42, 0, "00.00.0000");
    this.m_connectionProperties.setProperty(44, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(45, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(46, 0, "Y");
    this.m_connectionProperties.setProperty(47, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(48, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(49, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(50, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(51, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(52, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(53, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(54, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(55, 0, "Y");
    this.m_connectionProperties.setProperty(56, 2, Character.valueOf('\002'));
    this.m_connectionProperties.setProperty(57, 2, Character.valueOf('\004'));
    this.m_connectionProperties.setProperty(58, 0, "\"");
    this.m_connectionProperties.setProperty(59, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(60, 3, Long.valueOf(7L));
    this.m_connectionProperties.setProperty(61, 0, "N");
    this.m_connectionProperties.setProperty(62, 0, "");
    this.m_connectionProperties.setProperty(63, 0, "Y");
    this.m_connectionProperties.setProperty(64, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(65, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(66, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(67, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(68, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(69, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(70, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(71, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(72, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(73, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(74, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(75, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(76, 2, Character.valueOf('ÿ'));
    this.m_connectionProperties.setProperty(77, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(78, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(79, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(80, 0, "N");
    this.m_connectionProperties.setProperty(81, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(82, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(83, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(84, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(85, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(86, 0, "N");
    this.m_connectionProperties.setProperty(87, 0, "Y");
    this.m_connectionProperties.setProperty(88, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(89, 2, Character.valueOf('\001'));
    this.m_connectionProperties.setProperty(90, 3, Long.valueOf(16777215L));
    this.m_connectionProperties.setProperty(91, 5, Short.valueOf((short)1));
    this.m_connectionProperties.setProperty(92, 3, Long.valueOf(127L));
    this.m_connectionProperties.setProperty(93, 0, "N");
    this.m_connectionProperties.setProperty(94, 0, "F");
    this.m_connectionProperties.setProperty(95, 3, Long.valueOf(2L));
    this.m_connectionProperties.setProperty(96, 0, "procedure");
    this.m_connectionProperties.setProperty(97, 0, "Y");
    this.m_connectionProperties.setProperty(98, 2, Character.valueOf('\003'));
    this.m_connectionProperties.setProperty(99, 0, "schema");
    this.m_connectionProperties.setProperty(100, 3, Long.valueOf(29L));
    this.m_connectionProperties.setProperty(101, 0, "");
    this.m_connectionProperties.setProperty(102, 0, "");
    this.m_connectionProperties.setProperty(103, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(104, 3, Long.valueOf(16220159L));
    this.m_connectionProperties.setProperty(105, 3, Long.valueOf(31L));
    this.m_connectionProperties.setProperty(106, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(107, 3, Long.valueOf(14946049L));
    this.m_connectionProperties.setProperty(108, 3, Long.valueOf(14974975L));
    this.m_connectionProperties.setProperty(109, 3, Long.valueOf(31981567L));
    this.m_connectionProperties.setProperty(110, 3, Long.valueOf(31458049L));
    this.m_connectionProperties.setProperty(111, 3, Long.valueOf(14847745L));
    this.m_connectionProperties.setProperty(112, 3, Long.valueOf(10510847L));
    this.m_connectionProperties.setProperty(113, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(114, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(115, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(116, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(117, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(118, 3, Long.valueOf(14946049L));
    this.m_connectionProperties.setProperty(119, 3, Long.valueOf(31716351L));
    this.m_connectionProperties.setProperty(120, 3, Long.valueOf(10510847L));
    this.m_connectionProperties.setProperty(121, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(122, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(123, 3, Long.valueOf(14880513L));
    this.m_connectionProperties.setProperty(124, 3, Long.valueOf(14913281L));
    this.m_connectionProperties.setProperty(125, 3, Long.valueOf(14712831L));
    this.m_connectionProperties.setProperty(126, 3, Long.valueOf(14946049L));
    this.m_connectionProperties.setProperty(127, 3, Long.valueOf(31981567L));
    this.m_connectionProperties.setProperty(128, 3, Long.valueOf(31981567L));
    this.m_connectionProperties.setProperty(129, 3, Long.valueOf(31716351L));
    this.m_connectionProperties.setProperty(130, 3, Long.valueOf(31981567L));
    this.m_connectionProperties.setProperty(131, 3, Long.valueOf(7L));
    this.m_connectionProperties.setProperty(132, 0, "table");
    this.m_connectionProperties.setProperty(133, 3, Long.valueOf(511L));
    this.m_connectionProperties.setProperty(134, 3, Long.valueOf(511L));
    this.m_connectionProperties.setProperty(135, 3, Long.valueOf(1048575L));
    this.m_connectionProperties.setProperty(136, 2, Character.valueOf('\000'));
    this.m_connectionProperties.setProperty(137, 3, Long.valueOf(1L));
    this.m_connectionProperties.setProperty(138, 3, Long.valueOf(3L));
    this.m_connectionProperties.setProperty(139, 0, "");
    this.m_connectionProperties.setProperty(140, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(141, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(142, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(143, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(144, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(145, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(146, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(147, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(148, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(149, 3, Long.valueOf(3L));
    this.m_connectionProperties.setProperty(150, 0, "N");
    this.m_connectionProperties.setProperty(151, 0, "\\");
    this.m_connectionProperties.setProperty(152, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(153, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(154, 3, Long.valueOf(7L));
    this.m_connectionProperties.setProperty(155, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(156, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(159, 3, Long.valueOf(16L));
    this.m_connectionProperties.setProperty(157, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(158, 3, Long.valueOf(16135L));
    this.m_connectionProperties.setProperty(160, 3, Long.valueOf(346L));
    this.m_connectionProperties.setProperty(161, 3, Long.valueOf(16L));
    this.m_connectionProperties.setProperty(162, 3, Long.valueOf(15L));
    this.m_connectionProperties.setProperty(163, 3, Long.valueOf(6L));
    this.m_connectionProperties.setProperty(164, 3, Long.valueOf(15L));
    this.m_connectionProperties.setProperty(165, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(1000, 5, Short.valueOf((short)0));
    this.m_connectionProperties.setProperty(1001, 3, Long.valueOf(0L));
    this.m_connectionProperties.setProperty(1002, new Variant(3, Long.valueOf(0L)));
    this.m_connectionProperties.setProperty(1003, 5, Short.valueOf((short)0));
    this.m_connectionProperties.setProperty(1004, 6, Integer.valueOf(0));
    this.m_connectionProperties.setProperty(1005, 6, Integer.valueOf(0));
    this.m_connectionProperties.setProperty(1006, 0, "N");
    this.m_connectionProperties.setProperty(1007, 6, Integer.valueOf(32000));
    this.m_connectionProperties.setProperty(1008, 0, "");
    this.m_connectionProperties.setProperty(1009, 6, Integer.valueOf(0));
  }
}
