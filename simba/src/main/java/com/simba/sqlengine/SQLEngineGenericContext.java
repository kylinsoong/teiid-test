package com.simba.sqlengine;

import com.simba.support.MessageSourceImpl;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionUtilities;

public class SQLEngineGenericContext
{
  public static final int UNKNOWN_COLUMN_NUMBER = -1;
  public static final int UNKNOWN_ROW_NUMBER = -1;
  public static final ExceptionBuilder s_SQLEngineMessages = new ExceptionBuilder(7);
  protected static MessageSourceImpl s_defaultMsgSrc = new MessageSourceImpl(true, true);
  
  public static MessageSourceImpl getDefaultMsgSource()
  {
    return s_defaultMsgSrc;
  }
  
  public static void setDefaultMsgSource(MessageSourceImpl paramMessageSourceImpl)
  {
    s_defaultMsgSrc = paramMessageSourceImpl;
    registerMessages();
  }
  
  private static void registerMessages()
  {
    String str = ExceptionUtilities.getPackageName(SQLEngineGenericContext.class);
    StringBuilder localStringBuilder = new StringBuilder(str);
    localStringBuilder.append(".");
    localStringBuilder.append("SQLEngineMessages");
    s_defaultMsgSrc.registerMessages(localStringBuilder.toString(), 7, "JSQLEngine");
  }
  
  static
  {
    registerMessages();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/SQLEngineGenericContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */