package com.simba.dsi.exceptions;

import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.IMessageSource;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.Locale;

public final class InvalidArgumentException
  extends DSIRuntimeException
{
  private static final long serialVersionUID = 5547653333445247686L;
  private ErrorException m_exception = null;
  private int m_errorCode = 0;
  
  public InvalidArgumentException(int paramInt, String paramString)
  {
    this.m_exception = new ErrorException(DiagState.DIAG_GENERAL_ERROR, paramInt, DSIMessageKey.INVALID_NULL_ARG.name(), new String[] { paramString });
  }
  
  public InvalidArgumentException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    if (null == paramArrayOfString) {
      this.m_exception = new ErrorException(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString);
    } else {
      this.m_exception = new ErrorException(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString);
    }
  }
  
  public InvalidArgumentException(String paramString, int paramInt)
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/InvalidArgumentException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */