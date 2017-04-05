package com.simba.jdbc.common;

import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import java.sql.SQLException;
import java.sql.Savepoint;

public class SSavepoint
  implements Savepoint
{
  private int m_id = 0;
  private String m_name = null;
  private ILogger m_logger = null;
  private IWarningListener m_warningListener = null;
  
  protected SSavepoint(int paramInt, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramILogger });
    this.m_warningListener = paramIWarningListener;
    this.m_id = paramInt;
  }
  
  protected SSavepoint(String paramString, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramILogger });
    this.m_warningListener = paramIWarningListener;
    this.m_name = paramString;
  }
  
  public int getSavepointId()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null != this.m_name)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_ID_FROM_NAMED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return this.m_id;
  }
  
  public String getSavepointName()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null == this.m_name)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_ID_FROM_NAMED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return this.m_name;
  }
  
  boolean isNamed()
  {
    return null != this.m_name;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SSavepoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */