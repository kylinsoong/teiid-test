package com.simba.sqlengine.exceptions;

public class SQLEngineRuntimeException
  extends RuntimeException
{
  private static final long serialVersionUID = -8344955073364182712L;
  
  public SQLEngineRuntimeException() {}
  
  public SQLEngineRuntimeException(Exception paramException)
  {
    super(paramException);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/exceptions/SQLEngineRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */