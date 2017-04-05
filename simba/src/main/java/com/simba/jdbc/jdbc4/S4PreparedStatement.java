package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.dataengine.impl.DSIEmptyResultSet;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.dsi.dataengine.utilities.ExecutionResultType;
import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.SConnection;
import com.simba.jdbc.common.SPreparedStatement;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.JDBCVersion;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class S4PreparedStatement
  extends SPreparedStatement
{
  public S4PreparedStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException
  {
    super(paramString, paramIStatement, paramSConnection, paramInt);
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
  
  public synchronized ResultSetMetaData getMetaData()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      if (null == getResultSetMetaData())
      {
        ExecutionResults localExecutionResults = getQueryExecutor().getResults();
        Iterator localIterator = localExecutionResults.getResultItr();
        if (!localIterator.hasNext()) {
          return null;
        }
        ExecutionResult localExecutionResult = (ExecutionResult)localIterator.next();
        if (localIterator.hasNext()) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_NUMBER_METADATA, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
        }
        if (ExecutionResultType.RESULT_SET == localExecutionResult.getType())
        {
          IResultSet localIResultSet = (IResultSet)localExecutionResult.getResult();
          ArrayList localArrayList = localIResultSet.getSelectColumns();
          if (null == localArrayList) {
            return null;
          }
          S4ResultSetMetaData localS4ResultSetMetaData = null;
          localS4ResultSetMetaData = new S4ResultSetMetaData(localArrayList, getLogger(), getWarningListener());
          setResultSetMetadata(localS4ResultSetMetaData);
        }
      }
      return getResultSetMetaData();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public synchronized ParameterMetaData getParameterMetaData()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      if (null == getOpenParamMetaData())
      {
        S4ParameterMetaData localS4ParameterMetaData = null;
        localS4ParameterMetaData = new S4ParameterMetaData(getParameterMetadataList(), getLogger(), getWarningListener());
        setOpenParamMetaData(localS4ParameterMetaData);
      }
      return getOpenParamMetaData();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4PreparedStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */