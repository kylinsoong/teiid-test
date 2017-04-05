package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.SConnection;
import com.simba.jdbc.common.SConnectionHandle;
import com.simba.jdbc.common.SPooledConnection;
import com.simba.support.exceptions.ExceptionType;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public class S4ConnectionHandle
  extends SConnectionHandle
{
  public S4ConnectionHandle(SConnection paramSConnection, SPooledConnection paramSPooledConnection)
    throws SQLException
  {
    super(paramSConnection, paramSPooledConnection);
  }
  
  public Properties getClientInfo()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return ((S4Connection)this.m_connection).getClientInfo();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public String getClientInfo(String paramString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return ((S4Connection)this.m_connection).getClientInfo(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public PreparedStatement prepareStatement(String paramString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      PreparedStatement localPreparedStatement = this.m_connection.prepareStatement(paramString);
      return new S4PreparedStatementHandle((S4PooledConnection)this.m_pooledConnection, (S4PreparedStatement)localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      PreparedStatement localPreparedStatement = this.m_connection.prepareStatement(paramString, paramInt);
      return new S4PreparedStatementHandle((S4PooledConnection)this.m_pooledConnection, (S4PreparedStatement)localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      PreparedStatement localPreparedStatement = this.m_connection.prepareStatement(paramString, paramInt1, paramInt2);
      return new S4PreparedStatementHandle((S4PooledConnection)this.m_pooledConnection, (S4PreparedStatement)localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      PreparedStatement localPreparedStatement = this.m_connection.prepareStatement(paramString, paramInt1, paramInt2, paramInt3);
      return new S4PreparedStatementHandle((S4PooledConnection)this.m_pooledConnection, (S4PreparedStatement)localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfInt)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      PreparedStatement localPreparedStatement = this.m_connection.prepareStatement(paramString, paramArrayOfInt);
      return new S4PreparedStatementHandle((S4PooledConnection)this.m_pooledConnection, (S4PreparedStatement)localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      PreparedStatement localPreparedStatement = this.m_connection.prepareStatement(paramString, paramArrayOfString);
      return new S4PreparedStatementHandle((S4PooledConnection)this.m_pooledConnection, (S4PreparedStatement)localPreparedStatement);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setClientInfo(Properties paramProperties)
    throws SQLClientInfoException
  {
    if (this.m_isClosed) {
      throw ((SQLClientInfoException)ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_connection.getWarningListener(), ExceptionType.CLIENT_INFO, new Object[0]));
    }
    try
    {
      ((S4Connection)this.m_connection).setClientInfo(paramProperties);
    }
    catch (SQLClientInfoException localSQLClientInfoException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLClientInfoException);
      }
      throw localSQLClientInfoException;
    }
  }
  
  public void setClientInfo(String paramString1, String paramString2)
    throws SQLClientInfoException
  {
    if (this.m_isClosed) {
      throw ((SQLClientInfoException)ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_connection.getWarningListener(), ExceptionType.CLIENT_INFO, new Object[0]));
    }
    try
    {
      ((S4Connection)this.m_connection).setClientInfo(paramString1, paramString2);
    }
    catch (SQLClientInfoException localSQLClientInfoException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLClientInfoException);
      }
      throw localSQLClientInfoException;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4ConnectionHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */