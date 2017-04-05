package com.simba.support;

public class ArgValidator
{
  public static boolean isNullOrEmpty(char[] paramArrayOfChar)
  {
    return (null == paramArrayOfChar) || (0 == paramArrayOfChar.length);
  }
  
  public static boolean isNullOrEmpty(String paramString)
  {
    return (null == paramString) || (0 == paramString.length());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/ArgValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */