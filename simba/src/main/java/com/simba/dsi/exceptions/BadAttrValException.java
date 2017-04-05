package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class BadAttrValException
  extends ErrorException
{
  private static final long serialVersionUID = -7030180004437025106L;
  
  public BadAttrValException(String paramString, int paramInt)
  {
    super(DiagState.DIAG_INVALID_ATTR_VAL, paramString, paramInt);
  }
  
  public BadAttrValException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_INVALID_ATTR_VAL, paramInt, paramString, paramArrayOfString);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/BadAttrValException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */