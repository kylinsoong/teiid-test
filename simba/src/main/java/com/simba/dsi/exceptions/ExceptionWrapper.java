package com.simba.dsi.exceptions;

import com.simba.support.IMessageSource;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

class ExceptionWrapper
{
  private Exception m_exception;
  private Locale m_locale;
  private IMessageSource m_messageSource;
  
  protected ExceptionWrapper(Exception paramException, IMessageSource paramIMessageSource, Locale paramLocale)
    throws NullPointerException
  {
    if (null == paramException) {
      throw new NullPointerException();
    }
    this.m_exception = paramException;
    this.m_locale = paramLocale;
    this.m_messageSource = paramIMessageSource;
  }
  
  protected DiagState getDiagState()
  {
    if (isErrorException()) {
      return ((ErrorException)this.m_exception).getDiagState();
    }
    return DiagState.DIAG_GENERAL_ERROR;
  }
  
  protected String getMessage()
  {
    if (isErrorException()) {
      return ((ErrorException)this.m_exception).getMessage(this.m_messageSource, this.m_locale);
    }
    return this.m_exception.getLocalizedMessage();
  }
  
  protected String getFullMessage()
  {
    StringWriter localStringWriter = new StringWriter();
    PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
    this.m_exception.printStackTrace(localPrintWriter);
    return getMessage() + "\n" + localStringWriter.toString();
  }
  
  protected int getNativeErrorCode()
  {
    if (isErrorException()) {
      return ((ErrorException)this.m_exception).getNativeErrorCode(this.m_messageSource, this.m_locale);
    }
    return -1;
  }
  
  protected int getRowNumber()
  {
    if (isErrorException()) {
      return ((ErrorException)this.m_exception).getRowNumber();
    }
    return -1;
  }
  
  protected int getColumnNumber()
  {
    if (isErrorException()) {
      return ((ErrorException)this.m_exception).getColumnNumber();
    }
    return -1;
  }
  
  protected ExceptionWrapper getCause()
  {
    try
    {
      if (null == this.m_exception.getCause()) {
        return null;
      }
      return new ExceptionWrapper((Exception)this.m_exception.getCause(), this.m_messageSource, this.m_locale);
    }
    catch (Exception localException) {}
    return null;
  }
  
  private boolean isErrorException()
  {
    return this.m_exception instanceof ErrorException;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/ExceptionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */