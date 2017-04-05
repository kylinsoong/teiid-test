package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.dataengine.impl.DSIEmptyResultSet;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.exceptions.ExceptionConverter;
import com.simba.jdbc.common.SConnection;
import com.simba.jdbc.common.SStatement;
import com.simba.support.LogUtilities;
import com.simba.utilities.JDBCVersion;
import java.sql.ResultSet;
import java.sql.SQLException;

public class S4Statement
  extends SStatement
{
  public S4Statement(IStatement paramIStatement, SConnection paramSConnection, int paramInt)
  {
    super(paramIStatement, paramSConnection, paramInt);
    this.m_jdbcVersion = JDBCVersion.JDBC4;
  }
  
  protected ResultSet createResultSet(ExecutionResult paramExecutionResult)
    throws SQLException
  {
    Object localObject = null;
    if (createsUpdatableResults()) {
      localObject = new S4UpdatableForwardResultSet(this, (IResultSet)paramExecutionResult.getResult(), getLogger());
    } else {
      localObject = new S4ForwardResultSet(this, (IResultSet)paramExecutionResult.getResult(), getLogger());
    }
    ((ResultSet)localObject).setFetchSize(getFetchSize());
    return (ResultSet)localObject;
  }
  
  public ResultSet getGeneratedKeys()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      S4ForwardResultSet localS4ForwardResultSet = null;
      localS4ForwardResultSet = new S4ForwardResultSet(this, new DSIEmptyResultSet(), getLogger());
      return localS4ForwardResultSet;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4Statement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */