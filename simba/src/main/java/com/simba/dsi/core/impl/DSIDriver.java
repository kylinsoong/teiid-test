package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IEventHandler;
import com.simba.dsi.core.utilities.AttributeDataMap;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.IMessageSource;
import com.simba.support.LogUtilities;
import com.simba.support.SettingReader;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.ExceptionUtilities;
import com.simba.support.security.ICredentialFactory;
import com.simba.support.security.SimbaCredentialFactory;
import java.util.Locale;

public abstract class DSIDriver
  implements IDriver
{
  public static final ExceptionBuilder s_DSIMessages = new ExceptionBuilder(2);
  protected DSIMessageSource m_msgSrc = new DSIMessageSource(true, true);
  private AttributeDataMap m_settings = new AttributeDataMap();
  
  protected DSIDriver()
    throws ErrorException
  {
    try
    {
      loadProperties();
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.DEFAULT);
    }
    registerMessages();
  }
  
  public ICredentialFactory createCredentialFactory()
  {
    return new SimbaCredentialFactory(getDriverLog());
  }
  
  public IEventHandler getEventHandler()
  {
    return null;
  }
  
  public Locale getLocale()
  {
    String str = SettingReader.readSetting("DriverLocale");
    return ExceptionUtilities.createLocale(str);
  }
  
  public IMessageSource getMessageSource()
  {
    return this.m_msgSrc;
  }
  
  public Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getDriverLog(), new Object[] { Integer.valueOf(paramInt) });
    if (null == this.m_settings) {
      throw s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    if (this.m_settings.isProperty(paramInt)) {
      return this.m_settings.getProperty(paramInt);
    }
    throw new BadPropertyKeyException(2, DSIMessageKey.INVALID_PROPKEY.name(), String.valueOf(paramInt));
  }
  
  protected void setProperty(int paramInt, Variant paramVariant)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getDriverLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
    if (null == this.m_settings) {
      throw s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    this.m_settings.setProperty(paramInt, paramVariant);
  }
  
  private void loadProperties()
    throws IncorrectTypeException, NumericOverflowException
  {
    this.m_settings.setProperty(1, 2, Character.valueOf('\000'));
    this.m_settings.setProperty(29, 3, Long.valueOf(1L));
    this.m_settings.setProperty(28, 3, Long.valueOf(1L));
    this.m_settings.setProperty(2, 3, Long.valueOf(2L));
    this.m_settings.setProperty(3, 0, "");
    this.m_settings.setProperty(4, 0, "03.52");
    this.m_settings.setProperty(5, 0, "9.5.13.1016");
    this.m_settings.setProperty(6, 2, Character.valueOf('\000'));
    this.m_settings.setProperty(7, 6, Integer.valueOf(65532));
    this.m_settings.setProperty(9, 6, Integer.valueOf(65532));
    this.m_settings.setProperty(8, 6, Integer.valueOf(65532));
    this.m_settings.setProperty(10, 6, Integer.valueOf(1));
    this.m_settings.setProperty(11, 3, Long.valueOf(1L));
    this.m_settings.setProperty(12, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(13, 3, Long.valueOf(2L));
    this.m_settings.setProperty(16, 0, "1995");
    this.m_settings.setProperty(17, 3, Long.valueOf(0L));
    this.m_settings.setProperty(18, 2, Character.valueOf('\002'));
    this.m_settings.setProperty(19, 3, Long.valueOf(1073741824L));
    this.m_settings.setProperty(20, 3, Long.valueOf(0L));
    this.m_settings.setProperty(21, 3, Long.valueOf(0L));
    this.m_settings.setProperty(22, 3, Long.valueOf(1L));
    this.m_settings.setProperty(1000, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(1001, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(1002, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(1003, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(1004, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(23, 2, Character.valueOf('P'));
    this.m_settings.setProperty(24, 0, null);
    this.m_settings.setProperty(25, 3, Long.valueOf(0L));
    this.m_settings.setProperty(26, 3, Long.valueOf(0L));
    this.m_settings.setProperty(27, 3, Long.valueOf(1L));
    this.m_settings.setProperty(30, 3, Long.valueOf(1L));
    this.m_settings.setProperty(31, 4, Long.valueOf(0L));
    this.m_settings.setProperty(1006, 0, "");
    this.m_settings.setProperty(32, 3, Long.valueOf(0L));
    this.m_settings.setProperty(33, 3, Long.valueOf(0L));
  }
  
  private void registerMessages()
  {
    String str = ExceptionUtilities.getPackageName(DSIDriver.class);
    StringBuilder localStringBuilder1 = new StringBuilder(str);
    localStringBuilder1.append(".");
    localStringBuilder1.append("DSIMessages");
    this.m_msgSrc.registerMessages(localStringBuilder1.toString(), 2, "JDSI");
    StringBuilder localStringBuilder2 = new StringBuilder(str);
    localStringBuilder2.append(".");
    localStringBuilder2.append("JDBCMessages");
    this.m_msgSrc.registerMessages(localStringBuilder2.toString(), 1, "JDBC");
    this.m_msgSrc.registerMessages("com.simba.support.channels.messages", 8, "SupportChannels");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/impl/DSIDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */