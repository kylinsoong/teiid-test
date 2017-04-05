package com.simba.exceptions;

import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;

public class JDBCMessageBuilder
{
  public static String getMessage(JDBCMessageKey paramJDBCMessageKey, IWarningListener paramIWarningListener, boolean paramBoolean1, boolean paramBoolean2, Object... paramVarArgs)
  {
    return paramIWarningListener.getMessageSource().loadMessage(paramIWarningListener.getLocale(), 1, paramJDBCMessageKey.name(), paramBoolean1, paramBoolean2, paramVarArgs);
  }
  
  public static String getMessage(JDBCMessageKey paramJDBCMessageKey, IWarningListener paramIWarningListener, Object... paramVarArgs)
  {
    return paramIWarningListener.getMessageSource().loadMessage(paramIWarningListener.getLocale(), 1, paramJDBCMessageKey.name(), paramVarArgs);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/exceptions/JDBCMessageBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */