package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IDriver;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.GeneralException;

public class DSIDriverFactory
{
  private static String s_driverClassName = null;
  
  public static IDriver createDriver()
    throws ErrorException
  {
    if (null == s_driverClassName) {
      throw new GeneralException("Error creating DriverFactory, Driver class name not set.", 0);
    }
    try
    {
      return (IDriver)Class.forName(s_driverClassName).newInstance();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      throw new GeneralException("Error creating Driver, Driver class name incorrect.", 0);
    }
  }
  
  public static synchronized void setDriverClassName(String paramString)
  {
    if (null == s_driverClassName) {
      s_driverClassName = paramString;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/impl/DSIDriverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */