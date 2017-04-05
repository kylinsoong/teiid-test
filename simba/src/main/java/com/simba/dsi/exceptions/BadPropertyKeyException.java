package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class BadPropertyKeyException
  extends ErrorException
{
  private static final long serialVersionUID = 1259531785459247757L;
  
  public BadPropertyKeyException(String paramString, int paramInt)
  {
    super(DiagState.DIAG_INVALID_ATTR_OPT_IDENT, paramString, paramInt);
  }
  
  public BadPropertyKeyException(int paramInt, String paramString1, String paramString2)
  {
    super(DiagState.DIAG_INVALID_ATTR_OPT_IDENT, paramInt, paramString1, new String[] { paramString2 });
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/BadPropertyKeyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */