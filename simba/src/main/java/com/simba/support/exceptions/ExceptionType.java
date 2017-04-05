package com.simba.support.exceptions;

public enum ExceptionType
{
  DEFAULT,  DATA,  FEATURE_NOT_IMPLEMENTED,  CLIENT_INFO,  INTEGRITY_CONSTRAINT_VIOLATION,  NON_TRANSIENT_CONNECTION,  INVALID_AUTHORIZATION,  RECOVERABLE,  SYNTAX_ERROR,  TIME_OUT,  TRANSACTION_ROLLBACK,  TRANSIENT_CONNECTION,  TRANSIENT,  NON_TRANSIENT;
  
  private ExceptionType() {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/ExceptionType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */