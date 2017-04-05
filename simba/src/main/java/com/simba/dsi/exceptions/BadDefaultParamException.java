package com.simba.dsi.exceptions;

import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class BadDefaultParamException
  extends ErrorException
{
  private static final long serialVersionUID = 7200216309316498576L;
  
  public BadDefaultParamException(int paramInt1, int paramInt2, int paramInt3)
  {
    super(DiagState.DIAG_INVALID_DFLT_PARAM, paramInt1, DSIMessageKey.INVALID_PUSHED_DEFAULT_PARAM.name(), paramInt2, paramInt3);
  }
  
  public BadDefaultParamException(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    super(DiagState.DIAG_INVALID_DFLT_PARAM, paramString, paramInt1, paramInt2, paramInt3);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/BadDefaultParamException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */