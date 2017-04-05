package com.simba.dsi.exceptions;

import com.simba.support.IMessageSource;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.io.IOException;
import java.util.Locale;

public class InputOutputException
  extends IOException
{
  private static final long serialVersionUID = 8964924464604877510L;
  private ErrorException m_exception;
  private int m_errorCode = 0;
  
  public InputOutputException(int paramInt, String paramString)
  {
    this.m_exception = new ErrorException(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString);
  }
  
  public InputOutputException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    this.m_exception = new ErrorException(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString);
  }
  
  public InputOutputException(String paramString, int paramInt)
  {
    this.m_exception = new ErrorException(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt);
  }
  
  public int getErrorCode()
  {
    return this.m_errorCode;
  }
  
  public String getLocalizedMessage()
  {
    return this.m_exception.getLocalizedMessage();
  }
  
  public String getMessage()
  {
    return this.m_exception.getMessage();
  }
  
  public void loadMessage(IMessageSource paramIMessageSource, Locale paramLocale)
  {
    if (this.m_exception.hasPreformattedMessage()) {
      return;
    }
    this.m_exception.loadMessage(paramIMessageSource, paramLocale);
    this.m_errorCode = this.m_exception.getNativeErrorCode(null, null);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/InputOutputException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */