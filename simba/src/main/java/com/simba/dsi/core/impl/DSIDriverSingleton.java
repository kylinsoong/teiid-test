package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IDriver;

public class DSIDriverSingleton
{
  private static IDriver s_driver = null;
  
  public static IDriver getInstance()
  {
    return s_driver;
  }
  
  public static void setInstance(IDriver paramIDriver)
  {
    s_driver = paramIDriver;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/impl/DSIDriverSingleton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */