package com.simba.exceptions.jdbc4;

import com.simba.dsi.exceptions.InputOutputException;
import com.simba.dsi.exceptions.InvalidArgumentException;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.AbstractDriver;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ClientInfoException;
import com.simba.support.exceptions.DataException;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.FailedPropertiesReason;
import com.simba.support.exceptions.FeatureNotSupportedException;
import com.simba.support.exceptions.IntegrityConstraintViolationException;
import com.simba.support.exceptions.InvalidAuthorizationException;
import com.simba.support.exceptions.NonTransientConnectionException;
import com.simba.support.exceptions.NonTransientException;
import com.simba.support.exceptions.RecoverableException;
import com.simba.support.exceptions.SyntaxErrorException;
import com.simba.support.exceptions.TimeOutException;
import com.simba.support.exceptions.TransactionRollbackException;
import com.simba.support.exceptions.TransientConnectionException;
import com.simba.support.exceptions.TransientException;
import java.sql.ClientInfoStatus;
import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLNonTransientException;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;
import java.sql.SQLTransientException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JDBC4ExceptionConverter
  extends ExceptionConverter
{
  public SQLException toSQLException(Exception paramException, IWarningListener paramIWarningListener)
  {
    if ((paramException instanceof SQLException)) {
      return (SQLException)paramException;
    }
    IMessageSource localIMessageSource = paramIWarningListener.getMessageSource();
    SQLException localSQLException = null;
    Object localObject;
    if ((paramException instanceof NullPointerException))
    {
      localObject = localIMessageSource.loadMessage(paramIWarningListener.getLocale(), 1, JDBCMessageKey.NULL_ERROR.name());
      localSQLException = new SQLException((String)localObject, JDBCMessageKey.NULL_ERROR.getSQLState(), 0);
    }
    else if ((paramException instanceof InputOutputException))
    {
      localObject = (InputOutputException)paramException;
      ((InputOutputException)localObject).loadMessage(paramIWarningListener.getMessageSource(), paramIWarningListener.getLocale());
      localSQLException = new SQLException(((InputOutputException)localObject).getMessage(), "HY000", ((InputOutputException)localObject).getErrorCode());
    }
    else if ((paramException instanceof InvalidArgumentException))
    {
      localObject = (InvalidArgumentException)paramException;
      ((InvalidArgumentException)localObject).loadMessage(paramIWarningListener.getMessageSource(), paramIWarningListener.getLocale());
      localSQLException = new SQLException(((InvalidArgumentException)localObject).getMessage(), "HY000", ((InvalidArgumentException)localObject).getErrorCode());
    }
    else if ((paramException instanceof ErrorException))
    {
      ErrorException localErrorException = (ErrorException)paramException;
      int i = localErrorException.getNativeErrorCode(localIMessageSource, paramIWarningListener.getLocale());
      if (localErrorException.hasCustomState()) {
        localObject = localErrorException.getCustomState();
      } else {
        localObject = localErrorException.getDiagState().getSqlState();
      }
      if ((paramException instanceof ClientInfoException)) {
        localSQLException = convertToSQLClientInfoException(((ClientInfoException)paramException).getFailedProperties(), localErrorException.getMessage(), (String)localObject, localErrorException.getNativeErrorCode(null, null));
      } else {
        localSQLException = convertToJDBC4Exception(localErrorException.getMessage(), (String)localObject, i, localErrorException.getClass());
      }
    }
    else
    {
      localSQLException = new SQLException(AbstractDriver.getErrorMessageComponentName() + paramException.getLocalizedMessage(), "HY000", 0);
    }
    localSQLException.initCause(paramException);
    localSQLException.setStackTrace(paramException.getStackTrace());
    return localSQLException;
  }
  
  protected SQLException createSQLException(String paramString1, String paramString2, int paramInt, StackTraceElement[] paramArrayOfStackTraceElement, ExceptionType paramExceptionType, Map<String, FailedPropertiesReason> paramMap)
  {
    Object localObject = null;
    switch (paramExceptionType)
    {
    case CLIENT_INFO: 
      localObject = new SQLClientInfoException(paramString2, paramString1, paramInt, convertFailedPropertiesReasonToClientInfoStatus(paramMap));
      break;
    case DATA: 
      localObject = new SQLDataException(paramString2, paramString1, paramInt);
      break;
    case FEATURE_NOT_IMPLEMENTED: 
      localObject = new SQLFeatureNotSupportedException(paramString2, paramString1, paramInt);
      break;
    case INTEGRITY_CONSTRAINT_VIOLATION: 
      localObject = new SQLIntegrityConstraintViolationException(paramString2, paramString1, paramInt);
      break;
    case INVALID_AUTHORIZATION: 
      localObject = new SQLInvalidAuthorizationSpecException(paramString2, paramString1, paramInt);
      break;
    case NON_TRANSIENT: 
      localObject = new SQLNonTransientException(paramString2, paramString1, paramInt);
      break;
    case NON_TRANSIENT_CONNECTION: 
      localObject = new SQLNonTransientConnectionException(paramString2, paramString1, paramInt);
      break;
    case RECOVERABLE: 
      localObject = new SQLRecoverableException(paramString2, paramString1, paramInt);
      break;
    case SYNTAX_ERROR: 
      localObject = new SQLSyntaxErrorException(paramString2, paramString1, paramInt);
      break;
    case TIME_OUT: 
      localObject = new SQLTimeoutException(paramString2, paramString1, paramInt);
      break;
    case TRANSACTION_ROLLBACK: 
      localObject = new SQLTransactionRollbackException(paramString2, paramString1, paramInt);
      break;
    case TRANSIENT: 
      localObject = new SQLTransientException(paramString2, paramString1, paramInt);
      break;
    case TRANSIENT_CONNECTION: 
      localObject = new SQLTransientConnectionException(paramString2, paramString1, paramInt);
      break;
    default: 
      localObject = new SQLException(paramString2, paramString1, paramInt);
    }
    ((SQLException)localObject).setStackTrace(paramArrayOfStackTraceElement);
    return (SQLException)localObject;
  }
  
  private Map<String, ClientInfoStatus> convertFailedPropertiesReasonToClientInfoStatus(Map<String, FailedPropertiesReason> paramMap)
  {
    if (null != paramMap)
    {
      HashMap localHashMap = new HashMap(paramMap.size());
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        ClientInfoStatus localClientInfoStatus = convertFailedPropertyReasonToClientInfoStatus((FailedPropertiesReason)localEntry.getValue());
        localHashMap.put(localEntry.getKey(), localClientInfoStatus);
      }
      return localHashMap;
    }
    return null;
  }
  
  private ClientInfoStatus convertFailedPropertyReasonToClientInfoStatus(FailedPropertiesReason paramFailedPropertiesReason)
  {
    switch (paramFailedPropertiesReason)
    {
    case UNKNOWN: 
      return ClientInfoStatus.REASON_UNKNOWN;
    case UNKNOWN_PROPERTY: 
      return ClientInfoStatus.REASON_UNKNOWN_PROPERTY;
    case VALUE_INVALID: 
      return ClientInfoStatus.REASON_VALUE_INVALID;
    case VALUE_TRUNCATED: 
      return ClientInfoStatus.REASON_VALUE_TRUNCATED;
    }
    return null;
  }
  
  private SQLException convertToJDBC4Exception(String paramString1, String paramString2, int paramInt, Class<? extends ErrorException> paramClass)
  {
    if (DataException.class.isAssignableFrom(paramClass)) {
      return new SQLDataException(paramString1, paramString2, paramInt);
    }
    if (FeatureNotSupportedException.class.isAssignableFrom(paramClass)) {
      return new SQLFeatureNotSupportedException(paramString1, paramString2, paramInt);
    }
    if (IntegrityConstraintViolationException.class.isAssignableFrom(paramClass)) {
      return new SQLIntegrityConstraintViolationException(paramString1, paramString2, paramInt);
    }
    if (InvalidAuthorizationException.class.isAssignableFrom(paramClass)) {
      return new SQLInvalidAuthorizationSpecException(paramString1, paramString2, paramInt);
    }
    if (NonTransientConnectionException.class.isAssignableFrom(paramClass)) {
      return new SQLNonTransientConnectionException(paramString1, paramString2, paramInt);
    }
    if (NonTransientException.class.isAssignableFrom(paramClass)) {
      return new SQLNonTransientException(paramString1, paramString2, paramInt);
    }
    if (RecoverableException.class.isAssignableFrom(paramClass)) {
      return new SQLRecoverableException(paramString1, paramString2, paramInt);
    }
    if (SyntaxErrorException.class.isAssignableFrom(paramClass)) {
      return new SQLSyntaxErrorException(paramString1, paramString2, paramInt);
    }
    if (TimeOutException.class.isAssignableFrom(paramClass)) {
      return new SQLTimeoutException(paramString1, paramString2, paramInt);
    }
    if (TransactionRollbackException.class.isAssignableFrom(paramClass)) {
      return new SQLTransactionRollbackException(paramString1, paramString2, paramInt);
    }
    if (TransientConnectionException.class.isAssignableFrom(paramClass)) {
      return new SQLTransientConnectionException(paramString1, paramString2, paramInt);
    }
    if (TransientException.class.isAssignableFrom(paramClass)) {
      return new SQLTransientException(paramString1, paramString2, paramInt);
    }
    return new SQLException(paramString1, paramString2, paramInt);
  }
  
  private SQLException convertToSQLClientInfoException(Map<String, FailedPropertiesReason> paramMap, String paramString1, String paramString2, int paramInt)
  {
    Map localMap = convertFailedPropertiesReasonToClientInfoStatus(paramMap);
    return new SQLClientInfoException(paramString1, paramString2, paramInt, localMap);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/exceptions/jdbc4/JDBC4ExceptionConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */