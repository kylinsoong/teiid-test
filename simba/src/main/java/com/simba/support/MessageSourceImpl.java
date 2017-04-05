package com.simba.support;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageSourceImpl
  implements IMessageSource
{
  private Map<Integer, String> m_componentToFileName = new HashMap();
  private Map<Integer, String> m_componentIDToName = new HashMap();
  private boolean m_isConcatComponentName;
  private boolean m_isConcatVendorString;
  private String m_vendorName = "";
  
  public MessageSourceImpl(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.m_isConcatVendorString = paramBoolean1;
    this.m_isConcatComponentName = paramBoolean2;
    setVendorName("Simba");
  }
  
  public String loadMessage(Locale paramLocale, int paramInt, String paramString)
  {
    return getFormattedMessage(paramLocale, paramInt, paramString, this.m_isConcatVendorString, this.m_isConcatComponentName, new Object[0]);
  }
  
  public String loadMessage(Locale paramLocale, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    return getFormattedMessage(paramLocale, paramInt, paramString, paramBoolean1, paramBoolean2, new Object[0]);
  }
  
  public String loadMessage(Locale paramLocale, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2, Object... paramVarArgs)
  {
    return getFormattedMessage(paramLocale, paramInt, paramString, paramBoolean1, paramBoolean2, paramVarArgs);
  }
  
  public String loadMessage(Locale paramLocale, int paramInt, String paramString, Object... paramVarArgs)
  {
    return getFormattedMessage(paramLocale, paramInt, paramString, this.m_isConcatVendorString, this.m_isConcatComponentName, paramVarArgs);
  }
  
  public synchronized void registerMessages(String paramString1, int paramInt, String paramString2)
  {
    this.m_componentToFileName.put(Integer.valueOf(paramInt), paramString1);
    this.m_componentIDToName.put(Integer.valueOf(paramInt), paramString2);
  }
  
  public void setVendorName(String paramString)
  {
    synchronized (this)
    {
      this.m_vendorName = ("[" + paramString + "]");
    }
  }
  
  private String getFormattedMessage(Locale paramLocale, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2, Object... paramVarArgs)
  {
    try
    {
      String str1 = (String)this.m_componentToFileName.get(Integer.valueOf(paramInt));
      if (null == str1) {
        return "Component not found: " + paramInt;
      }
      ResourceBundle localResourceBundle = ResourceBundle.getBundle(str1, paramLocale);
      String str2 = localResourceBundle.getString(paramString);
      if (null != paramVarArgs) {
        str2 = MessageFormat.format(str2, paramVarArgs);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      if (paramBoolean1) {
        synchronized (this)
        {
          localStringBuilder.append(this.m_vendorName);
        }
      }
      if (paramBoolean2)
      {
        ??? = (String)this.m_componentIDToName.get(Integer.valueOf(paramInt));
        if (null != ???)
        {
          localStringBuilder.append("[");
          localStringBuilder.append((String)???);
          localStringBuilder.append("]");
        }
      }
      localStringBuilder.append(str2);
      return localStringBuilder.toString();
    }
    catch (MissingResourceException localMissingResourceException)
    {
      return "Error message not found: " + paramString + ". " + localMissingResourceException.getLocalizedMessage();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/MessageSourceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */