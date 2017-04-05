package com.simba.support.exceptions;

import java.util.Map;

public class ExceptionBuilder
{
  private int m_componentId = 0;
  
  public ExceptionBuilder(int paramInt)
  {
    this.m_componentId = paramInt;
  }
  
  public ErrorException createClientInfoException(String paramString, Map<String, FailedPropertiesReason> paramMap)
  {
    return new ClientInfoException(this.m_componentId, paramString, paramMap);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2)
  {
    return createCustomErrorException(paramString1, paramString2, null, null, ExceptionType.DEFAULT);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, ExceptionType paramExceptionType)
  {
    return createCustomErrorException(paramString1, paramString2, null, null, paramExceptionType);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String paramString3)
  {
    return createCustomErrorException(paramString1, paramString2, new String[] { paramString3 }, null, ExceptionType.DEFAULT);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String paramString3, ExceptionType paramExceptionType)
  {
    return createCustomErrorException(paramString1, paramString2, new String[] { paramString3 }, null, paramExceptionType);
  }
  
  public ErrorException createClientInfoException(String paramString1, String paramString2, Map<String, FailedPropertiesReason> paramMap)
  {
    return new ClientInfoException(this.m_componentId, paramString1, new String[] { paramString2 }, paramMap);
  }
  
  public ErrorException createClientInfoException(String paramString, String[] paramArrayOfString, Map<String, FailedPropertiesReason> paramMap)
  {
    return new ClientInfoException(this.m_componentId, paramString, paramArrayOfString, paramMap);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    return createCustomErrorException(paramString1, paramString2, paramArrayOfString, null, ExceptionType.DEFAULT);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String[] paramArrayOfString, ExceptionType paramExceptionType)
  {
    return createCustomErrorException(paramString1, paramString2, paramArrayOfString, null, paramExceptionType);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return createCustomErrorException(paramString1, paramString2, null, paramThrowable, ExceptionType.DEFAULT);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    return createCustomErrorException(paramString1, paramString2, null, paramThrowable, paramExceptionType);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String paramString3, Throwable paramThrowable)
  {
    return createCustomErrorException(paramString1, paramString2, new String[] { paramString3 }, paramThrowable, ExceptionType.DEFAULT);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String paramString3, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    return createCustomErrorException(paramString1, paramString2, new String[] { paramString3 }, paramThrowable, paramExceptionType);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String[] paramArrayOfString, Throwable paramThrowable)
  {
    return createCustomErrorException(paramString1, paramString2, paramArrayOfString, paramThrowable, ExceptionType.DEFAULT);
  }
  
  public ErrorException createCustomException(String paramString1, String paramString2, String[] paramArrayOfString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    return createCustomErrorException(paramString1, paramString2, paramArrayOfString, paramThrowable, paramExceptionType);
  }
  
  public ErrorException createGeneralException(String paramString)
  {
    return createException(paramString, null, null, ExceptionType.DEFAULT);
  }
  
  public ErrorException createGeneralException(String paramString, ExceptionType paramExceptionType)
  {
    return createException(paramString, null, null, paramExceptionType);
  }
  
  public ErrorException createGeneralException(String paramString1, String paramString2)
  {
    return createException(paramString1, new String[] { paramString2 }, null, ExceptionType.DEFAULT);
  }
  
  public ErrorException createGeneralException(String paramString1, String paramString2, ExceptionType paramExceptionType)
  {
    return createException(paramString1, new String[] { paramString2 }, null, paramExceptionType);
  }
  
  public ErrorException createGeneralException(String paramString, String[] paramArrayOfString)
  {
    return createException(paramString, paramArrayOfString, null, ExceptionType.DEFAULT);
  }
  
  public ErrorException createGeneralException(String paramString, String[] paramArrayOfString, ExceptionType paramExceptionType)
  {
    return createException(paramString, paramArrayOfString, null, paramExceptionType);
  }
  
  public ErrorException createGeneralException(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return createException(paramString1, new String[] { paramString2 }, paramThrowable, ExceptionType.DEFAULT);
  }
  
  public ErrorException createGeneralException(String paramString1, String paramString2, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    return createException(paramString1, new String[] { paramString2 }, paramThrowable, paramExceptionType);
  }
  
  public ErrorException createGeneralException(String paramString, String[] paramArrayOfString, Throwable paramThrowable)
  {
    return createException(paramString, paramArrayOfString, paramThrowable, ExceptionType.DEFAULT);
  }
  
  public ErrorException createGeneralException(String paramString, String[] paramArrayOfString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    return createException(paramString, paramArrayOfString, paramThrowable, paramExceptionType);
  }
  
  public ErrorException createGeneralException(String paramString, Throwable paramThrowable)
  {
    return createException(paramString, null, paramThrowable, ExceptionType.DEFAULT);
  }
  
  public ErrorException createGeneralException(String paramString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    return createException(paramString, null, paramThrowable, paramExceptionType);
  }
  
  private ErrorException createException(String paramString, String[] paramArrayOfString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    StackTraceElement[] arrayOfStackTraceElement1 = Thread.currentThread().getStackTrace();
    StackTraceElement[] arrayOfStackTraceElement2 = new StackTraceElement[arrayOfStackTraceElement1.length - 4];
    System.arraycopy(arrayOfStackTraceElement1, 3, arrayOfStackTraceElement2, 0, arrayOfStackTraceElement2.length);
    ErrorException localErrorException = null;
    if (null == paramArrayOfString)
    {
      if (null == paramThrowable) {
        localErrorException = generateException(paramString, paramExceptionType);
      } else {
        localErrorException = generateException(paramString, paramThrowable, paramExceptionType);
      }
    }
    else if (null == paramThrowable) {
      localErrorException = generateException(paramString, paramArrayOfString, paramExceptionType);
    } else {
      localErrorException = generateException(paramString, paramArrayOfString, paramThrowable, paramExceptionType);
    }
    localErrorException.setStackTrace(arrayOfStackTraceElement2);
    return localErrorException;
  }
  
  private ErrorException createCustomErrorException(String paramString1, String paramString2, String[] paramArrayOfString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    StackTraceElement[] arrayOfStackTraceElement1 = Thread.currentThread().getStackTrace();
    StackTraceElement[] arrayOfStackTraceElement2 = new StackTraceElement[arrayOfStackTraceElement1.length - 4];
    System.arraycopy(arrayOfStackTraceElement1, 3, arrayOfStackTraceElement2, 0, arrayOfStackTraceElement2.length);
    ErrorException localErrorException = null;
    if (null == paramArrayOfString)
    {
      if (null == paramThrowable) {
        localErrorException = generateException(paramString1, paramString2, paramExceptionType);
      } else {
        localErrorException = generateException(paramString1, paramString2, paramThrowable, paramExceptionType);
      }
    }
    else if (null == paramThrowable) {
      localErrorException = generateException(paramString1, paramString2, paramArrayOfString, paramExceptionType);
    } else {
      localErrorException = generateException(paramString1, paramString2, paramArrayOfString, paramThrowable, paramExceptionType);
    }
    localErrorException.setStackTrace(arrayOfStackTraceElement2);
    return localErrorException;
  }
  
  private ErrorException generateException(String paramString, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(this.m_componentId, paramString, null);
    case DATA: 
      return new DataException(this.m_componentId, paramString);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(this.m_componentId, paramString);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(this.m_componentId, paramString);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(this.m_componentId, paramString);
    case NON_TRANSIENT: 
      return new NonTransientException(this.m_componentId, paramString);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(this.m_componentId, paramString);
    case RECOVERABLE: 
      return new RecoverableException(this.m_componentId, paramString);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(this.m_componentId, paramString);
    case TIME_OUT: 
      return new TimeOutException(this.m_componentId, paramString);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(this.m_componentId, paramString);
    case TRANSIENT: 
      return new TransientException(this.m_componentId, paramString);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(this.m_componentId, paramString);
    }
    return new GeneralException(this.m_componentId, paramString);
  }
  
  private ErrorException generateException(String paramString, String[] paramArrayOfString, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(this.m_componentId, paramString, paramArrayOfString, null);
    case DATA: 
      return new DataException(this.m_componentId, paramString, paramArrayOfString);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(this.m_componentId, paramString, paramArrayOfString);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(this.m_componentId, paramString, paramArrayOfString);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(this.m_componentId, paramString, paramArrayOfString);
    case NON_TRANSIENT: 
      return new NonTransientException(this.m_componentId, paramString, paramArrayOfString);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(this.m_componentId, paramString, paramArrayOfString);
    case RECOVERABLE: 
      return new RecoverableException(this.m_componentId, paramString, paramArrayOfString);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(this.m_componentId, paramString, paramArrayOfString);
    case TIME_OUT: 
      return new TimeOutException(this.m_componentId, paramString, paramArrayOfString);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(this.m_componentId, paramString, paramArrayOfString);
    case TRANSIENT: 
      return new TransientException(this.m_componentId, paramString, paramArrayOfString);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(this.m_componentId, paramString, paramArrayOfString);
    }
    return new GeneralException(this.m_componentId, paramString, paramArrayOfString);
  }
  
  private ErrorException generateException(String paramString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(this.m_componentId, paramString, paramThrowable, null);
    case DATA: 
      return new DataException(this.m_componentId, paramString, paramThrowable);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(this.m_componentId, paramString, paramThrowable);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(this.m_componentId, paramString, paramThrowable);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(this.m_componentId, paramString, paramThrowable);
    case NON_TRANSIENT: 
      return new NonTransientException(this.m_componentId, paramString, paramThrowable);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(this.m_componentId, paramString, paramThrowable);
    case RECOVERABLE: 
      return new RecoverableException(this.m_componentId, paramString, paramThrowable);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(this.m_componentId, paramString, paramThrowable);
    case TIME_OUT: 
      return new TimeOutException(this.m_componentId, paramString, paramThrowable);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(this.m_componentId, paramString, paramThrowable);
    case TRANSIENT: 
      return new TransientException(this.m_componentId, paramString, paramThrowable);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(this.m_componentId, paramString, paramThrowable);
    }
    return new GeneralException(this.m_componentId, paramString, paramThrowable);
  }
  
  private ErrorException generateException(String paramString, String[] paramArrayOfString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(this.m_componentId, paramString, paramArrayOfString, paramThrowable, null);
    case DATA: 
      return new DataException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case NON_TRANSIENT: 
      return new NonTransientException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case RECOVERABLE: 
      return new RecoverableException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case TIME_OUT: 
      return new TimeOutException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case TRANSIENT: 
      return new TransientException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
    }
    return new GeneralException(this.m_componentId, paramString, paramArrayOfString, paramThrowable);
  }
  
  private ErrorException generateException(String paramString1, String paramString2, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(paramString1, this.m_componentId, paramString2, null);
    case DATA: 
      return new DataException(paramString1, this.m_componentId, paramString2);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(paramString1, this.m_componentId, paramString2);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(paramString1, this.m_componentId, paramString2);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(paramString1, this.m_componentId, paramString2);
    case NON_TRANSIENT: 
      return new NonTransientException(paramString1, this.m_componentId, paramString2);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(paramString1, this.m_componentId, paramString2);
    case RECOVERABLE: 
      return new RecoverableException(paramString1, this.m_componentId, paramString2);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(paramString1, this.m_componentId, paramString2);
    case TIME_OUT: 
      return new TimeOutException(paramString1, this.m_componentId, paramString2);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(paramString1, this.m_componentId, paramString2);
    case TRANSIENT: 
      return new TransientException(paramString1, this.m_componentId, paramString2);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(paramString1, this.m_componentId, paramString2);
    }
    return new ErrorException(paramString1, this.m_componentId, paramString2);
  }
  
  private ErrorException generateException(String paramString1, String paramString2, String[] paramArrayOfString, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(paramString1, this.m_componentId, paramString2, paramArrayOfString, null);
    case DATA: 
      return new DataException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case NON_TRANSIENT: 
      return new NonTransientException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case RECOVERABLE: 
      return new RecoverableException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case TIME_OUT: 
      return new TimeOutException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case TRANSIENT: 
      return new TransientException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
    }
    return new ErrorException(paramString1, this.m_componentId, paramString2, paramArrayOfString);
  }
  
  private ErrorException generateException(String paramString1, String paramString2, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(paramString1, this.m_componentId, paramString2, paramThrowable, null);
    case DATA: 
      return new DataException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case NON_TRANSIENT: 
      return new NonTransientException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case RECOVERABLE: 
      return new RecoverableException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case TIME_OUT: 
      return new TimeOutException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case TRANSIENT: 
      return new TransientException(paramString1, this.m_componentId, paramString2, paramThrowable);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(paramString1, this.m_componentId, paramString2, paramThrowable);
    }
    return new ErrorException(paramString1, this.m_componentId, paramString2, paramThrowable);
  }
  
  private ErrorException generateException(String paramString1, String paramString2, String[] paramArrayOfString, Throwable paramThrowable, ExceptionType paramExceptionType)
  {
    if (null == paramExceptionType) {
      paramExceptionType = ExceptionType.DEFAULT;
    }
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      return new ClientInfoException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable, null);
    case DATA: 
      return new DataException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case FEATURE_NOT_IMPLEMENTED: 
      return new FeatureNotSupportedException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      return new IntegrityConstraintViolationException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case INVALID_AUTHORIZATION: 
      return new InvalidAuthorizationException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case NON_TRANSIENT: 
      return new NonTransientException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case NON_TRANSIENT_CONNECTION: 
      return new NonTransientConnectionException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case RECOVERABLE: 
      return new RecoverableException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case SYNTAX_ERROR: 
      return new SyntaxErrorException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case TIME_OUT: 
      return new TimeOutException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case TRANSACTION_ROLLBACK: 
      return new TransactionRollbackException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case TRANSIENT: 
      return new TransientException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    case TRANSIENT_CONNECTION: 
      return new TransientConnectionException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
    }
    return new ErrorException(paramString1, this.m_componentId, paramString2, paramArrayOfString, paramThrowable);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/ExceptionBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */