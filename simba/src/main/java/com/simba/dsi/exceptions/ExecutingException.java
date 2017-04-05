package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class ExecutingException
  extends ErrorException
{
  private static final long serialVersionUID = -1825998272653185878L;
  
  public ExecutingException(DiagState paramDiagState, int paramInt, String paramString)
  {
    super(paramDiagState, paramString, paramInt, -2, -2);
  }
  
  public ExecutingException(DiagState paramDiagState, String paramString, int paramInt)
  {
    super(paramDiagState, paramString, paramInt, -2, -2);
  }
  
  public ExecutingException(DiagState paramDiagState, int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    super(paramDiagState, paramInt1, paramString, paramInt2, paramInt3);
  }
  
  public ExecutingException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramDiagState, paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public ExecutingException(DiagState paramDiagState, int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(paramDiagState, paramInt, paramString, paramArrayOfString, -2, -2);
  }
  
  public ExecutingException(DiagState paramDiagState, int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    super(paramDiagState, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/ExecutingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */