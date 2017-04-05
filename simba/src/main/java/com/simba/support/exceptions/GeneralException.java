package com.simba.support.exceptions;

public class GeneralException
  extends ErrorException
{
  private static final long serialVersionUID = 6876617258587742935L;
  
  public GeneralException(int paramInt, String paramString)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, -1, -1);
  }
  
  public GeneralException(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramInt2, paramInt3);
  }
  
  public GeneralException(int paramInt1, String paramString, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramInt2, paramInt3, paramThrowable);
  }
  
  public GeneralException(int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString, -1, -1);
  }
  
  public GeneralException(int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3);
  }
  
  public GeneralException(int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3, paramThrowable);
  }
  
  public GeneralException(int paramInt, String paramString, String[] paramArrayOfString, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString, -1, -1, paramThrowable);
  }
  
  public GeneralException(int paramInt, String paramString, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, -1, -1, paramThrowable);
  }
  
  public GeneralException(String paramString, int paramInt)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt, -1, -1);
  }
  
  public GeneralException(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public GeneralException(String paramString, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt1, paramInt2, paramInt3, paramThrowable);
  }
  
  public GeneralException(String paramString, int paramInt, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt, -1, -1, paramThrowable);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/GeneralException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */