package com.simba.dsi.core.utilities;

public class ClientInfoPropertyKey
{
  public static final String APPLICATION_NAME = "APPLICATIONNAME";
  public static final String CLIENT_USER = "CLIENTUSER";
  public static final String CLIENT_HOSTNAME = "CLIENTHOSTNAME";
  
  public static boolean isStandardClientInfoPropertyKey(String paramString)
  {
    String str = paramString.toUpperCase();
    return (str.equals("APPLICATIONNAME")) || (str.equals("CLIENTUSER")) || (str.equals("CLIENTHOSTNAME"));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/ClientInfoPropertyKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */