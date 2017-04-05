package com.simba.support;

import java.util.List;
import java.util.Locale;

public abstract interface IWarningListener
{
  public abstract Locale getLocale();
  
  public abstract IMessageSource getMessageSource();
  
  public abstract void postWarning(Warning paramWarning);
  
  public abstract List<Warning> getWarnings();
  
  public abstract void setLocale(Locale paramLocale);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/IWarningListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */