package com.simba.jdbc.common.utilities;

import com.simba.exceptions.ExceptionConverter;
import com.simba.support.exceptions.ExceptionType;
import java.sql.SQLException;

public final class WrapperUtilities
{
  public static boolean isWrapperFor(Class<?> paramClass, Object paramObject)
  {
    return paramClass.isInstance(paramObject);
  }
  
  public static <T> T unwrap(Class<T> paramClass, Object paramObject)
    throws SQLException
  {
    try
    {
      return (T)paramClass.cast(paramObject);
    }
    catch (Exception localException)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException("HY000", localException.getMessage(), 0, ExceptionType.DEFAULT);
      localSQLException.setStackTrace(localException.getStackTrace());
      throw localSQLException;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/utilities/WrapperUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */