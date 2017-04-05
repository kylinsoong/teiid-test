package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class OptionalFeatureNotImplementedException
  extends ErrorException
{
  private static final long serialVersionUID = -7744849359086725296L;
  
  public OptionalFeatureNotImplementedException(String paramString, int paramInt)
  {
    super(DiagState.DIAG_OPTL_FEAT_NOT_IMPLD, paramString, paramInt);
  }
  
  public OptionalFeatureNotImplementedException(int paramInt, String paramString1, String paramString2)
  {
    super(DiagState.DIAG_OPTL_FEAT_NOT_IMPLD, paramInt, paramString1, new String[] { paramString2 });
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/OptionalFeatureNotImplementedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */