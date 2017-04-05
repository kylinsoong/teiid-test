package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class BadAuthException
  extends ErrorException
{
  private static final long serialVersionUID = 5489609470389078613L;
  
  public BadAuthException(int paramInt, String paramString)
  {
    super(DiagState.DIAG_INVALID_AUTH_SPEC, paramInt, paramString);
  }
  
  public BadAuthException(int paramInt, String paramString1, String paramString2)
  {
    super(DiagState.DIAG_INVALID_AUTH_SPEC, paramInt, paramString1, new String[] { paramString2 });
  }
  
  public BadAuthException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_INVALID_AUTH_SPEC, paramInt, paramString, paramArrayOfString);
  }
  
  public BadAuthException(String paramString, int paramInt)
  {
    super(DiagState.DIAG_INVALID_AUTH_SPEC, paramString, paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/BadAuthException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */