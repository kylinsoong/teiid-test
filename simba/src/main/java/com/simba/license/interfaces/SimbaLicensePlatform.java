package com.simba.license.interfaces;

public class SimbaLicensePlatform
{
  public static final String WINDOWS = "Windows";
  public static final String LINUX = "Linux";
  public static final String MACINTOSH = "Macintosh";
  public static final String SOLARIS = "Solaris";
  public static final String AIX = "AIX";
  public static final String HP_UX = "HP-UX";
  public static final String UNSUPPORTED = "UNKNOW_PLATFORM";
  
  public static String detect()
  {
    String str1 = "UNKNOW_PLATFORM";
    String str2 = System.getProperty("os.name", "generic").toLowerCase();
    if (str2.indexOf("windows") >= 0) {
      str1 = "Windows";
    } else if (str2.indexOf("nux") >= 0) {
      str1 = "Linux";
    } else if ((str2.indexOf("mac") >= 0) || (str2.indexOf("darwin") >= 0)) {
      str1 = "Macintosh";
    } else if ((str2.indexOf("sunos") >= 0) || (str2.indexOf("solaris") >= 0)) {
      str1 = "Solaris";
    } else if (str2.indexOf("aix") >= 0) {
      str1 = "AIX";
    } else if (str2.indexOf("hp-ux") >= 0) {
      str1 = "HP-UX";
    }
    return str1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/SimbaLicensePlatform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */