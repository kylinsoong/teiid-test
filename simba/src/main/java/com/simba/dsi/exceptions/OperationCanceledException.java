package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class OperationCanceledException
  extends ErrorException
{
  private static final long serialVersionUID = 5489609470389078613L;
  
  public OperationCanceledException(int paramInt, String paramString)
  {
    super(DiagState.DIAG_OPER_CANCELED, paramInt, paramString);
  }
  
  public OperationCanceledException(String paramString, int paramInt)
  {
    super(DiagState.DIAG_OPER_CANCELED, paramString, paramInt);
  }
  
  public OperationCanceledException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_OPER_CANCELED, paramInt, paramString, paramArrayOfString);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/OperationCanceledException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */