package com.simba.dsi.utilities;

import com.simba.dsi.CppClassWrapper;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CPPWarningListenerWrapper
  extends CppClassWrapper
  implements IWarningListener
{
  private final Object m_lockObj = new Object();
  private boolean m_isValid = true;
  
  public CPPWarningListenerWrapper(long paramLong)
    throws ErrorException
  {
    super(paramLong);
  }
  
  public Locale getLocale()
  {
    synchronized (this.m_lockObj)
    {
      checkValid();
      return getLocaleImpl(getObjRef());
    }
  }
  
  public IMessageSource getMessageSource()
  {
    synchronized (this.m_lockObj)
    {
      checkValid();
      return getMessageSourceImpl(getObjRef());
    }
  }
  
  public List<Warning> getWarnings()
  {
    assert (this.m_isValid);
    return new ArrayList();
  }
  
  public void postWarning(Warning paramWarning)
  {
    synchronized (this.m_lockObj)
    {
      checkValid();
      postWarningImpl(getObjRef(), paramWarning);
    }
  }
  
  public void setLocale(Locale paramLocale)
  {
    synchronized (this.m_lockObj)
    {
      checkValid();
      setLocaleImpl(getObjRef(), paramLocale);
    }
  }
  
  private void checkValid()
    throws IllegalStateException
  {
    if (!this.m_isValid)
    {
      if (!$assertionsDisabled) {
        throw new AssertionError("Object used after release().");
      }
      throw new IllegalStateException("Object used after release().");
    }
  }
  
  private void release()
  {
    synchronized (this.m_lockObj)
    {
      assert (this.m_isValid) : "release() called more than once.";
      if (this.m_isValid)
      {
        destruct(getObjRef());
        this.m_isValid = false;
      }
    }
  }
  
  private static final native void postWarningImpl(long paramLong, Warning paramWarning);
  
  private static final native void setLocaleImpl(long paramLong, Locale paramLocale);
  
  private static final native Locale getLocaleImpl(long paramLong);
  
  private static final native IMessageSource getMessageSourceImpl(long paramLong);
  
  private static final native void destruct(long paramLong);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/utilities/CPPWarningListenerWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */