package com.simba.support;

import java.util.Locale;

public abstract interface IMessageSource
{
  public abstract String loadMessage(Locale paramLocale, int paramInt, String paramString);
  
  public abstract String loadMessage(Locale paramLocale, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract String loadMessage(Locale paramLocale, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2, Object... paramVarArgs);
  
  public abstract String loadMessage(Locale paramLocale, int paramInt, String paramString, Object... paramVarArgs);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/IMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */