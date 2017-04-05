package com.simba.sqlengine.exceptions;

import com.simba.support.exceptions.DiagState;

public class InvalidQueryException
  extends SQLEngineException
{
  private static final long serialVersionUID = 1644116957164984386L;
  
  public InvalidQueryException(String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramString, paramArrayOfString);
  }
  
  public InvalidQueryException(String paramString)
  {
    super(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramString);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/exceptions/InvalidQueryException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */