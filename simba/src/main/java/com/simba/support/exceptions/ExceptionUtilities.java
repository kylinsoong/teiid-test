package com.simba.support.exceptions;

import java.util.Locale;

public class ExceptionUtilities
{
  public static Locale createLocale(String paramString)
  {
    if (null == paramString) {
      return Locale.getDefault();
    }
    int i = paramString.indexOf("-");
    if (-1 == i) {
      return new Locale(paramString);
    }
    try
    {
      return new Locale(paramString.substring(0, i), paramString.substring(i + 1, i + 3));
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
    return Locale.getDefault();
  }
  
  public static String localeToString(Locale paramLocale)
  {
    String str1 = paramLocale.getLanguage();
    if ("".equals(str1)) {
      return "en-US";
    }
    String str2 = paramLocale.getCountry();
    if ("".equals(str2)) {
      return str1;
    }
    return str1 + "-" + str2;
  }
  
  public static String getPackageName(Class<?> paramClass)
  {
    String str1 = "";
    String str2 = paramClass.getCanonicalName();
    if (null != str2)
    {
      int i = str2.lastIndexOf(".");
      if (-1 != i) {
        str1 = str2.substring(0, str2.lastIndexOf("."));
      }
    }
    return str1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/ExceptionUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */