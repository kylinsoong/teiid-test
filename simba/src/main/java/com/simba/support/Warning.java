package com.simba.support;

import java.util.Locale;

public class Warning
{
  private int m_nativeErrorCode = -1;
  private WarningCode m_warningCode;
  private String m_customState;
  private String m_msgKey;
  private String[] m_msgParams = null;
  private String m_message;
  private int m_componentId;
  private int m_rowNumber;
  private int m_columnNumber;
  private boolean m_hasPreformattedMessage = false;
  
  public Warning(WarningCode paramWarningCode, String paramString, int paramInt)
  {
    this(paramWarningCode, paramString, paramInt, -1, -1);
  }
  
  public Warning(WarningCode paramWarningCode, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    this.m_warningCode = paramWarningCode;
    if (null == paramString) {
      this.m_message = "";
    } else {
      this.m_message = paramString;
    }
    this.m_nativeErrorCode = paramInt1;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
  }
  
  public Warning(WarningCode paramWarningCode, int paramInt, String paramString)
  {
    this(paramWarningCode, paramInt, paramString, -1, -1);
  }
  
  public Warning(WarningCode paramWarningCode, int paramInt, String paramString, String[] paramArrayOfString)
  {
    this(paramWarningCode, paramInt, paramString, paramArrayOfString, -1, -1);
  }
  
  public Warning(WarningCode paramWarningCode, int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    this(paramWarningCode, paramInt1, paramString, null, paramInt2, paramInt3);
  }
  
  public Warning(WarningCode paramWarningCode, int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    this.m_warningCode = paramWarningCode;
    this.m_customState = null;
    this.m_componentId = paramInt1;
    this.m_msgKey = paramString;
    this.m_msgParams = paramArrayOfString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
  }
  
  public Warning(WarningCode paramWarningCode, String paramString)
  {
    this(paramWarningCode, paramString, -1, -1);
  }
  
  public Warning(WarningCode paramWarningCode, String paramString, int paramInt1, int paramInt2)
  {
    this.m_warningCode = paramWarningCode;
    if (null == paramString) {
      this.m_message = "";
    } else {
      this.m_message = paramString;
    }
    this.m_hasPreformattedMessage = true;
    this.m_rowNumber = paramInt1;
    this.m_columnNumber = paramInt2;
  }
  
  public Warning(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, -1, -1);
  }
  
  public Warning(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    this.m_warningCode = WarningCode.GENERAL_WARNING;
    this.m_customState = paramString1;
    if (null == paramString2) {
      this.m_message = "";
    } else {
      this.m_message = paramString2;
    }
    this.m_hasPreformattedMessage = true;
    this.m_rowNumber = paramInt1;
    this.m_columnNumber = paramInt2;
  }
  
  public Warning(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3)
  {
    this(paramString1, paramInt1, paramString2, null, paramInt2, paramInt3);
  }
  
  public Warning(String paramString1, int paramInt1, String paramString2, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    this.m_warningCode = WarningCode.GENERAL_WARNING;
    this.m_customState = paramString1;
    this.m_componentId = paramInt1;
    this.m_msgKey = paramString2;
    this.m_msgParams = paramArrayOfString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
  }
  
  public Warning(String paramString1, String paramString2, int paramInt)
  {
    this(paramString1, paramString2, paramInt, -1, -1);
  }
  
  public Warning(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    this.m_warningCode = WarningCode.GENERAL_WARNING;
    this.m_customState = paramString1;
    if (null == paramString2) {
      this.m_message = "";
    } else {
      this.m_message = paramString2;
    }
    this.m_hasPreformattedMessage = true;
    this.m_nativeErrorCode = paramInt1;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
  }
  
  public int getNativeErrorCode()
  {
    return this.m_nativeErrorCode;
  }
  
  public int getColumnNumber()
  {
    return this.m_columnNumber;
  }
  
  public int getComponentId()
  {
    return this.m_componentId;
  }
  
  public String getCustomState()
  {
    return this.m_customState;
  }
  
  public String getMessage()
  {
    return this.m_message;
  }
  
  public String getMessageKey()
  {
    return this.m_msgKey;
  }
  
  public String[] getMessageParams()
  {
    return this.m_msgParams;
  }
  
  public int getRowNumber()
  {
    return this.m_rowNumber;
  }
  
  public WarningCode getWarningCode()
  {
    return this.m_warningCode;
  }
  
  public boolean hasCustomState()
  {
    return null != this.m_customState;
  }
  
  public boolean hasMessageParams()
  {
    return null != this.m_msgParams;
  }
  
  public boolean hasPreformattedMessage()
  {
    return this.m_hasPreformattedMessage;
  }
  
  public void setLocalizedWarningMessage(IMessageSource paramIMessageSource, Locale paramLocale)
  {
    if ((this.m_hasPreformattedMessage) || (null == this.m_msgKey)) {
      return;
    }
    if (hasMessageParams()) {
      this.m_message = paramIMessageSource.loadMessage(paramLocale, this.m_componentId, this.m_msgKey, (Object[])this.m_msgParams);
    } else {
      this.m_message = paramIMessageSource.loadMessage(paramLocale, this.m_componentId, this.m_msgKey);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/Warning.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */