package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.core.utilities.AttributeDataMap;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.exceptions.OptionalFeatureNotImplementedException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.security.ICredentials;
import javax.naming.OperationNotSupportedException;

public abstract class DSIEnvironment
  implements IEnvironment
{
  private AttributeDataMap m_settings = new AttributeDataMap();
  private IDriver m_parentDriver = null;
  private IWarningListener m_warningListener = null;
  
  public DSIEnvironment(IDriver paramIDriver)
    throws ErrorException
  {
    this.m_parentDriver = paramIDriver;
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { paramIDriver });
    try
    {
      loadProperties();
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.DEFAULT);
    }
  }
  
  public IConnection createConnection(ICredentials paramICredentials)
    throws ErrorException, OperationNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { paramICredentials });
    throw new OperationNotSupportedException();
  }
  
  public IMessageSource getMessageSource()
  {
    return this.m_parentDriver.getMessageSource();
  }
  
  public AttributeDataMap getAllProperties()
  {
    return this.m_settings;
  }
  
  public Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt) });
    if (null == this.m_settings) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    if (this.m_settings.isProperty(paramInt)) {
      return this.m_settings.getProperty(paramInt);
    }
    throw new BadPropertyKeyException(2, DSIMessageKey.INVALID_PROPKEY.name(), String.valueOf(paramInt));
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_warningListener = paramIWarningListener;
  }
  
  public void setProperty(int paramInt, Variant paramVariant)
    throws OptionalFeatureNotImplementedException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
    if (null == this.m_settings) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    if ((1 == paramInt) && (paramVariant.getType() == 6)) {
      try
      {
        if (paramVariant.getLong() == 0L) {
          throw new OptionalFeatureNotImplementedException(2, DSIMessageKey.OPTIONAL_FEAT_NOT_IMPL.name(), String.valueOf(paramInt));
        }
      }
      catch (IncorrectTypeException localIncorrectTypeException)
      {
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.DEFAULT);
      }
      catch (NumericOverflowException localNumericOverflowException)
      {
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.DEFAULT);
      }
    }
    this.m_settings.setProperty(paramInt, paramVariant);
  }
  
  public ILogger getLog()
  {
    return this.m_parentDriver.getDriverLog();
  }
  
  public IDriver getParentDriver()
  {
    return this.m_parentDriver;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  private void loadProperties()
    throws IncorrectTypeException, NumericOverflowException
  {
    this.m_settings.setProperty(1, 3, Long.valueOf(1L));
    this.m_settings.setProperty(2, 2, Character.valueOf('\000'));
    this.m_settings.setProperty(3, 6, Integer.valueOf(3));
    this.m_settings.setProperty(4, 6, Integer.valueOf(1));
    this.m_settings.setProperty(5, 0, "");
    this.m_settings.setProperty(6, 0, "");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/impl/DSIEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */