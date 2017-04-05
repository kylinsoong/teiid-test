package com.simba.support;

public class RebrandingUtilities
{
  private static final String s_packageBranding;
  
  public static String getPackageBranding()
  {
    return s_packageBranding;
  }
  
  static
  {
    String str = RebrandingUtilities.class.getCanonicalName();
    s_packageBranding = str.substring("com.".length(), str.length() - ".support.RebrandingUtilities".length());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/RebrandingUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */