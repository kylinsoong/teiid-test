package com.simba.sqlengine.executor.etree.bool.functor.comp;

class StringUtil
{
  public static String rightTrim(String paramString)
  {
    for (int i = paramString.length() - 1; (i >= 0) && (Character.isWhitespace(paramString.charAt(i))); i--) {}
    return paramString.substring(0, i + 1);
  }
  
  public static String rightTrim(String paramString, char paramChar)
  {
    for (int i = paramString.length() - 1; (i >= 0) && (paramChar == paramString.charAt(i)); i--) {}
    return paramString.substring(0, i + 1);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/StringUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */