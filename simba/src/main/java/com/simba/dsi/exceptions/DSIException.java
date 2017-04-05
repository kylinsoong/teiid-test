package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public class DSIException
  extends ErrorException
{
  private static final long serialVersionUID = 5840522976809607230L;
  
  public DSIException(DiagState paramDiagState, String paramString)
  {
    super(paramDiagState, 2, paramString);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, int paramInt)
  {
    super(paramDiagState, paramString, paramInt, -1, -1);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2)
  {
    super(paramDiagState, 2, paramString, paramInt1, paramInt2);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramDiagState, paramString, paramInt1, paramInt2, paramInt3);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramDiagState, paramString, paramInt1, paramInt2, paramInt3, paramThrowable);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, Throwable paramThrowable)
  {
    super(paramDiagState, 2, paramString, paramInt1, paramInt2, paramThrowable);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, int paramInt, Throwable paramThrowable)
  {
    super(paramDiagState, paramString, paramInt, -1, -1, paramThrowable);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, String[] paramArrayOfString)
  {
    super(paramDiagState, 2, paramString, paramArrayOfString);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    super(paramDiagState, 2, paramString, paramArrayOfString, paramInt1, paramInt2);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, Throwable paramThrowable)
  {
    super(paramDiagState, 2, paramString, paramArrayOfString, paramInt1, paramInt2, paramThrowable);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, String[] paramArrayOfString, Throwable paramThrowable)
  {
    super(paramDiagState, 2, paramString, paramArrayOfString, paramThrowable);
  }
  
  public DSIException(DiagState paramDiagState, String paramString, Throwable paramThrowable)
  {
    super(paramDiagState, 2, paramString, paramThrowable);
  }
  
  public DSIException(String paramString1, String paramString2)
  {
    super(paramString1, 2, paramString2);
  }
  
  public DSIException(String paramString1, String paramString2, int paramInt)
  {
    super(paramString1, paramString2, paramInt, -1, -1);
  }
  
  public DSIException(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    super(paramString1, 2, paramString2, paramInt1, paramInt2);
  }
  
  public DSIException(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramString1, paramString2, paramInt1, paramInt2, paramInt3);
  }
  
  public DSIException(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable)
  {
    super(paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramThrowable);
  }
  
  public DSIException(String paramString1, String paramString2, int paramInt1, int paramInt2, Throwable paramThrowable)
  {
    super(paramString1, 2, paramString2, paramInt1, paramInt2, paramThrowable);
  }
  
  public DSIException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
  {
    super(paramString1, paramString2, paramInt, -1, -1, paramThrowable);
  }
  
  public DSIException(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    super(paramString1, 2, paramString2, paramArrayOfString);
  }
  
  public DSIException(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    super(paramString1, 2, paramString2, paramArrayOfString, paramInt1, paramInt2);
  }
  
  public DSIException(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, int paramInt2, Throwable paramThrowable)
  {
    super(paramString1, 2, paramString2, paramArrayOfString, paramInt1, paramInt2, paramThrowable);
  }
  
  public DSIException(String paramString1, String paramString2, String[] paramArrayOfString, Throwable paramThrowable)
  {
    super(paramString1, 2, paramString2, paramArrayOfString, paramThrowable);
  }
  
  public DSIException(String paramString1, String paramString2, Throwable paramThrowable)
  {
    super(paramString1, 2, paramString2, paramThrowable);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/DSIException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */