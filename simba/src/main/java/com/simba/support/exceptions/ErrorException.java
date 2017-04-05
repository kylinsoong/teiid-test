package com.simba.support.exceptions;

import com.simba.support.IMessageSource;
import java.util.Locale;

public class ErrorException
  extends Exception
{
  public static final int JDBC_ERROR = 1;
  public static final int DSI_ERROR = 2;
  public static final int MESSAGES_ERROR = 5;
  public static final int COMMUNICATIONS_ERROR = 6;
  public static final int SQLENGINE_ERROR = 7;
  public static final int SUPPORT_CHANNELS = 8;
  public static final String JDBC_COMPONENT_NAME = "JDBC";
  public static final String DSI_COMPONENT_NAME = "JDSI";
  public static final String MESSAGES_COMPONENT_NAME = "JMessages";
  public static final String COMMUNICATIONS_COMPONENT_NAME = "JCommunications";
  public static final String SQLENGINE_COMPONENT_NAME = "JSQLEngine";
  public static final String DSI_ERROR_MESSAGES = "DSIMessages";
  public static final String JDBC_ERROR_MESSAGES = "JDBCMessages";
  public static final String MESSAGES_ERROR_MESSAGES = "CSMessages";
  public static final String COMMUNICATIONS_ERROR_MESSAGES = "CommunicationsMessages";
  public static final String SQLENGINE_ERROR_MESSAGES = "SQLEngineMessages";
  public static final String SUPPORT_CHANNELS_NAME = "SupportChannels";
  private static final long serialVersionUID = 4070171084841472995L;
  protected int m_columnNumber;
  protected int m_rowNumber;
  private DiagState m_diagState;
  private String[] m_msgParams;
  private String m_msgKeyOrText = "";
  private String m_customState;
  private int m_componentId;
  private int m_nativeErrorCode = 0;
  private boolean m_hasPreformattedMessage;
  private boolean m_hasCachedMessage;
  
  public ErrorException(DiagState paramDiagState, int paramInt, String paramString)
  {
    this(paramDiagState, paramInt, paramString, -1, -1);
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    this.m_diagState = paramDiagState;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt1, String paramString, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramThrowable);
    this.m_diagState = paramDiagState;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt, String paramString, String[] paramArrayOfString)
  {
    this(paramDiagState, paramInt, paramString, paramArrayOfString, -1, -1);
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    this.m_diagState = paramDiagState;
    this.m_componentId = paramInt1;
    this.m_msgParams = paramArrayOfString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_msgKeyOrText = paramString;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramThrowable);
    this.m_diagState = paramDiagState;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString;
    this.m_msgParams = paramArrayOfString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt, String paramString, String[] paramArrayOfString, Throwable paramThrowable)
  {
    this(paramDiagState, paramInt, paramString, paramArrayOfString, -1, -1, paramThrowable);
  }
  
  public ErrorException(DiagState paramDiagState, int paramInt, String paramString, Throwable paramThrowable)
  {
    this(paramDiagState, paramInt, paramString, -1, -1);
  }
  
  public ErrorException(DiagState paramDiagState, String paramString, int paramInt)
  {
    this(paramDiagState, paramString, paramInt, -1, -1);
  }
  
  public ErrorException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramString);
    this.m_diagState = paramDiagState;
    this.m_customState = null;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasPreformattedMessage = true;
    this.m_msgKeyOrText = paramString;
    this.m_nativeErrorCode = paramInt1;
  }
  
  public ErrorException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
    this.m_diagState = paramDiagState;
    this.m_customState = null;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasPreformattedMessage = true;
    this.m_msgKeyOrText = paramString;
    this.m_nativeErrorCode = paramInt1;
  }
  
  public ErrorException(DiagState paramDiagState, String paramString, int paramInt, Throwable paramThrowable)
  {
    this(paramDiagState, paramString, paramInt, -1, -1, paramThrowable);
  }
  
  public ErrorException(String paramString1, int paramInt, String paramString2)
  {
    this(paramString1, paramInt, paramString2, -1, -1);
  }
  
  public ErrorException(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3)
  {
    this.m_customState = paramString1;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString2;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramThrowable);
    this.m_customState = paramString1;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString2;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString)
  {
    this(paramString1, paramInt, paramString2, paramArrayOfString, -1, -1);
  }
  
  public ErrorException(String paramString1, int paramInt1, String paramString2, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    this.m_customState = paramString1;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString2;
    this.m_msgParams = paramArrayOfString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(String paramString1, int paramInt1, String paramString2, String[] paramArrayOfString, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramThrowable);
    this.m_customState = paramString1;
    this.m_componentId = paramInt1;
    this.m_msgKeyOrText = paramString2;
    this.m_msgParams = paramArrayOfString;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasCachedMessage = false;
  }
  
  public ErrorException(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString, Throwable paramThrowable)
  {
    this(paramString1, paramInt, paramString2, paramArrayOfString, -1, -1, paramThrowable);
  }
  
  public ErrorException(String paramString1, int paramInt, String paramString2, Throwable paramThrowable)
  {
    this(paramString1, paramInt, paramString2, -1, -1, paramThrowable);
  }
  
  public ErrorException(String paramString1, String paramString2, int paramInt)
  {
    this(paramString1, paramString2, paramInt, -1, -1);
  }
  
  public ErrorException(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramString2);
    this.m_diagState = DiagState.DIAG_GENERAL_ERROR;
    this.m_customState = paramString1;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasPreformattedMessage = true;
    this.m_msgKeyOrText = paramString2;
    this.m_nativeErrorCode = paramInt1;
  }
  
  public ErrorException(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramString2, paramThrowable);
    this.m_diagState = DiagState.DIAG_GENERAL_ERROR;
    this.m_customState = paramString1;
    this.m_rowNumber = paramInt2;
    this.m_columnNumber = paramInt3;
    this.m_hasPreformattedMessage = true;
    this.m_msgKeyOrText = paramString2;
    this.m_nativeErrorCode = paramInt1;
  }
  
  public ErrorException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
  {
    this(paramString1, paramString2, paramInt, -1, -1, paramThrowable);
  }
  
  public int getColumnNumber()
  {
    return this.m_columnNumber;
  }
  
  public String getCustomState()
  {
    return this.m_customState;
  }
  
  public DiagState getDiagState()
  {
    return this.m_diagState;
  }
  
  public String getLocalizedMessage()
  {
    return this.m_msgKeyOrText;
  }
  
  public String getMessage()
  {
    assert ((this.m_hasCachedMessage) || (this.m_hasPreformattedMessage));
    return this.m_msgKeyOrText;
  }
  
  public String getMessage(IMessageSource paramIMessageSource, Locale paramLocale)
  {
    if ((this.m_hasCachedMessage) || (this.m_hasPreformattedMessage)) {
      return this.m_msgKeyOrText;
    }
    loadMessage(paramIMessageSource, paramLocale);
    return this.m_msgKeyOrText;
  }
  
  public String getMessageKeyOrText()
  {
    return this.m_msgKeyOrText;
  }
  
  public String[] getMessageParams()
  {
    return this.m_msgParams;
  }
  
  public int getNativeErrorCode(IMessageSource paramIMessageSource, Locale paramLocale)
  {
    if ((this.m_hasCachedMessage) || (this.m_hasPreformattedMessage)) {
      return this.m_nativeErrorCode;
    }
    loadMessage(paramIMessageSource, paramLocale);
    return this.m_nativeErrorCode;
  }
  
  public int getRowNumber()
  {
    return this.m_rowNumber;
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
  
  public void loadMessage(IMessageSource paramIMessageSource, Locale paramLocale)
  {
    if ((this.m_hasPreformattedMessage) || (this.m_hasCachedMessage)) {
      return;
    }
    if (null == paramIMessageSource)
    {
      this.m_msgKeyOrText = "Cannot create a message with a null message source.";
      return;
    }
    String str = null;
    if (hasMessageParams()) {
      str = paramIMessageSource.loadMessage(paramLocale, this.m_componentId, this.m_msgKeyOrText, (Object[])this.m_msgParams);
    } else {
      str = paramIMessageSource.loadMessage(paramLocale, this.m_componentId, this.m_msgKeyOrText);
    }
    this.m_hasCachedMessage = true;
    this.m_msgKeyOrText = str;
    loadNativeErrCode();
  }
  
  public void loadMessage(IMessageSource paramIMessageSource, Locale paramLocale, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((this.m_hasPreformattedMessage) || (this.m_hasCachedMessage)) {
      return;
    }
    if (null == paramIMessageSource)
    {
      str = "Error retrieving the error message for " + this.m_msgKeyOrText;
      this.m_msgKeyOrText = str;
      return;
    }
    String str = null;
    if (hasMessageParams()) {
      str = paramIMessageSource.loadMessage(paramLocale, this.m_componentId, this.m_msgKeyOrText, paramBoolean1, paramBoolean2, (Object[])this.m_msgParams);
    } else {
      str = paramIMessageSource.loadMessage(paramLocale, this.m_componentId, this.m_msgKeyOrText);
    }
    this.m_hasCachedMessage = true;
    this.m_msgKeyOrText = str;
    loadNativeErrCode();
  }
  
  private void loadNativeErrCode()
  {
    assert ((this.m_hasCachedMessage) || (this.m_hasPreformattedMessage));
    int i = this.m_msgKeyOrText.indexOf('(');
    int j = this.m_msgKeyOrText.indexOf(')');
    if ((-1 != i) && (-1 != j) && (j > i))
    {
      String str = this.m_msgKeyOrText.substring(++i, j);
      try
      {
        this.m_nativeErrorCode = Integer.parseInt(str);
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/ErrorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */