package com.simba.jdbc.jdbc4;

import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.exceptions.ExceptionConverter;
import com.simba.jdbc.common.SArray;
import com.simba.jdbc.common.SForwardResultSet;
import com.simba.jdbc.common.SStatement;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.utilities.JDBCVersion;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class S4ForwardResultSet
  extends SForwardResultSet
{
  public S4ForwardResultSet(SStatement paramSStatement, IResultSet paramIResultSet, ILogger paramILogger)
    throws SQLException
  {
    super(paramSStatement, paramIResultSet, paramILogger);
    this.m_jdbcVersion = JDBCVersion.JDBC4;
  }
  
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      if (null == getResultSetMetaData())
      {
        initializeResultSetColumns();
        setResultSetMetadata(new S4ResultSetMetaData(getResultSetColumns(), getLogger(), getWarningListener()));
      }
      return getResultSetMetaData();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  protected SArray createArrayResult(IArray paramIArray)
    throws SQLException
  {
    return new S4Array(paramIArray, getParentConnection(), getLogger(), getWarningListener());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4ForwardResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */