package com.simba.dsi.utilities;

import com.simba.dsi.core.utilities.PromptType;
import com.simba.dsi.dataengine.utilities.ExecutionContextStatus;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.OrderType;
import com.simba.dsi.dataengine.utilities.ParameterType;
import com.simba.support.IMessageSource;
import java.util.Locale;

public class CPPUtilities
{
  public static String MessageSourceVarArgsUnpacker(IMessageSource paramIMessageSource, Locale paramLocale, int paramInt, String paramString, Object[] paramArrayOfObject)
  {
    assert (paramIMessageSource != null);
    return paramIMessageSource.loadMessage(paramLocale, paramInt, paramString, paramArrayOfObject);
  }
  
  public static ParameterType getParameterTypeByName(String paramString)
  {
    return (ParameterType)Enum.valueOf(ParameterType.class, paramString);
  }
  
  public static PromptType getPromptTypeByName(String paramString)
  {
    return (PromptType)Enum.valueOf(PromptType.class, paramString);
  }
  
  public static Nullable getNullableByName(String paramString)
  {
    return (Nullable)Enum.valueOf(Nullable.class, paramString);
  }
  
  public static ExecutionContextStatus getExecutionContextStatusByName(String paramString)
  {
    return (ExecutionContextStatus)Enum.valueOf(ExecutionContextStatus.class, paramString);
  }
  
  public static OrderType getODBCOrderType()
  {
    return OrderType.ODBC;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/utilities/CPPUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */