package com.simba.dsi.core.interfaces;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.support.ILogger;
import com.simba.support.IMessageSource;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.security.ICredentialFactory;
import java.util.Locale;

public abstract interface IDriver
{
  public abstract IEnvironment createEnvironment()
    throws ErrorException;
  
  public abstract ILogger getDriverLog();
  
  public abstract IEventHandler getEventHandler();
  
  public abstract Locale getLocale();
  
  public abstract IMessageSource getMessageSource();
  
  public abstract Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException;
  
  public abstract ICredentialFactory createCredentialFactory();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/interfaces/IDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */