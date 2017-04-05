package com.simba.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class SettingReader
{
  private static boolean m_isSuccessfulLoad = false;
  private static String m_loadException;
  private static Properties s_addedSettings;
  private static Map<String, Object> s_objectSettings = new HashMap();
  private static Properties s_settings = new Properties();
  
  public static void clearAllSettings()
  {
    s_addedSettings.clear();
    s_objectSettings.clear();
  }
  
  public static boolean isSuccessfulLoad()
  {
    return m_isSuccessfulLoad;
  }
  
  public static void loadSettings(String paramString1, String paramString2)
  {
    FileInputStream localFileInputStream = null;
    Object localObject;
    try
    {
      String str1 = "";
      String str2 = null;
      if (null != paramString2) {
        str2 = System.getenv(paramString2);
      }
      if (null != str2)
      {
        localObject = new File(str2);
        if (((File)localObject).exists())
        {
          str1 = ((File)localObject).getCanonicalPath();
          if (!str1.endsWith(File.separator)) {
            str1 = str1 + File.separator;
          }
        }
      }
      str1 = str1 + paramString1;
      localFileInputStream = new FileInputStream(str1);
      s_settings.load(localFileInputStream);
      localFileInputStream.close();
    }
    catch (IOException localIOException1)
    {
      m_isSuccessfulLoad = false;
      m_loadException = localIOException1.getLocalizedMessage();
      try
      {
        if (null != localFileInputStream) {
          localFileInputStream.close();
        }
      }
      catch (IOException localIOException2) {}
      return;
    }
    Enumeration localEnumeration = s_settings.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str3 = (String)localEnumeration.nextElement();
      localObject = s_settings.getProperty(str3);
      s_settings.remove(str3);
      s_settings.setProperty(str3.toUpperCase(Locale.ENGLISH), (String)localObject);
    }
    m_isSuccessfulLoad = true;
  }
  
  public static void loadSimbaSettings()
  {
    loadSettings("simba.properties", "SIMBA_PROPERTIES_DIR");
  }
  
  public static Object readAdditionalSetting(String paramString)
  {
    return s_objectSettings.get(paramString.toUpperCase(Locale.ENGLISH));
  }
  
  public static String readSetting(String paramString)
  {
    String str1 = paramString.toUpperCase(Locale.ENGLISH);
    String str2 = s_addedSettings.getProperty(str1);
    if (null == str2) {
      str2 = s_settings.getProperty(str1);
    }
    return str2;
  }
  
  public static Map<String, Object> retrieveAllAdditionalSettings()
  {
    return s_objectSettings;
  }
  
  public static Properties retrieveAllSettings()
  {
    return s_settings;
  }
  
  public static String retrieveLoadErrorMessage()
  {
    return m_loadException;
  }
  
  public static void storeAdditionalSetting(String paramString, Object paramObject)
  {
    s_objectSettings.put(paramString.toUpperCase(Locale.ENGLISH), paramObject);
  }
  
  public static void storeSetting(String paramString1, String paramString2)
  {
    s_addedSettings.setProperty(paramString1.toUpperCase(Locale.ENGLISH), paramString2);
  }
  
  public static void storeSettings(Properties paramProperties)
  {
    Enumeration localEnumeration = paramProperties.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      s_addedSettings.setProperty(str.toUpperCase(Locale.ENGLISH), paramProperties.get(str).toString());
    }
  }
  
  static
  {
    try
    {
      s_addedSettings = new Properties();
      storeSetting("DriverLocale", Locale.getDefault().toString());
    }
    catch (Exception localException)
    {
      storeSetting("DriverLocale", "en");
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/SettingReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */