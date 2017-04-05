package com.simba.support.exceptions;

public class InvalidOperationException
  extends ErrorException
{
  private static final long serialVersionUID = 6439046145313070136L;
  
  public InvalidOperationException(int paramInt, String paramString, String[] paramArrayOfString, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString, -1, -1, paramThrowable);
  }
  
  public InvalidOperationException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString, -1, -1);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/InvalidOperationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */