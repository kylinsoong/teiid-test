package com.simba.sqlengine.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public class SQLEngineException
  extends ErrorException
{
  private static final long serialVersionUID = -3128020673411712263L;
  
  public SQLEngineException(DiagState paramDiagState, String paramString)
  {
    super(paramDiagState, 7, paramString);
  }
  
  public SQLEngineException(DiagState paramDiagState, String paramString, Throwable paramThrowable)
  {
    super(paramDiagState, 7, paramString, paramThrowable);
  }
  
  public SQLEngineException(DiagState paramDiagState, String paramString, Throwable paramThrowable, String[] paramArrayOfString)
  {
    super(paramDiagState, 7, paramString, paramArrayOfString, paramThrowable);
  }
  
  public SQLEngineException(DiagState paramDiagState, String paramString, String[] paramArrayOfString)
  {
    super(paramDiagState, 7, paramString, paramArrayOfString);
  }
  
  public SQLEngineException(String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_GENERAL_ERROR, 7, paramString, paramArrayOfString);
  }
  
  public SQLEngineException(String paramString)
  {
    super(DiagState.DIAG_GENERAL_ERROR, 7, paramString);
  }
  
  public SQLEngineException(String paramString, Throwable paramThrowable)
  {
    super(DiagState.DIAG_GENERAL_ERROR, 7, paramString, paramThrowable);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/exceptions/SQLEngineException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */