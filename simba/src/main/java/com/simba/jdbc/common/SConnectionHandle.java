package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;

public abstract class SConnectionHandle
  implements Connection
{
  protected boolean m_isClosed = true;
  protected SConnection m_connection;
  protected SPooledConnection m_pooledConnection;
  
  protected SConnectionHandle(SConnection paramSConnection, SPooledConnection paramSPooledConnection)
    throws SQLException
  {
    this.m_connection = paramSConnection;
    this.m_pooledConnection = paramSPooledConnection;
  }
  
  public void clearWarnings()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.clearWarnings();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void close()
    throws SQLException
  {
    if (this.m_isClosed) {
      return;
    }
    this.m_isClosed = true;
    this.m_connection.closeChildObjects();
    this.m_pooledConnection.onHandleClose();
  }
  
  public void commit()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.commit();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Statement createStatement()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.createStatement();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Statement createStatement(int paramInt1, int paramInt2)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.createStatement(paramInt1, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Statement createStatement(int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.createStatement(paramInt1, paramInt2, paramInt3);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public boolean getAutoCommit()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getAutoCommit();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public String getCatalog()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getCatalog();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getHoldability()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getHoldability();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  protected ILogger getLogger()
  {
    return this.m_connection.m_logger;
  }
  
  public DatabaseMetaData getMetaData()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getMetaData();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getTransactionIsolation()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getTransactionIsolation();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Map<String, Class<?>> getTypeMap()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getTypeMap();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public SQLWarning getWarnings()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.getWarnings();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  protected IWarningListener getWarningListener()
  {
    return this.m_connection.m_warningListener;
  }
  
  public boolean isClosed()
  {
    return (this.m_isClosed) || (this.m_connection.isClosed());
  }
  
  public boolean isReadOnly()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.isReadOnly();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public String nativeSQL(String paramString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.nativeSQL(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public CallableStatement prepareCall(String paramString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.prepareCall(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.prepareCall(paramString, paramInt1, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.prepareCall(paramString, paramInt1, paramInt2, paramInt3);
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
      return this.m_connection.prepareStatement(paramString);
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
      return this.m_connection.prepareStatement(paramString, paramInt);
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
      return this.m_connection.prepareStatement(paramString, paramInt1, paramInt2);
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
      return this.m_connection.prepareStatement(paramString, paramInt1, paramInt2, paramInt3);
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
      return this.m_connection.prepareStatement(paramString, paramArrayOfInt);
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
      return this.m_connection.prepareStatement(paramString, paramArrayOfString);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void releaseSavepoint(Savepoint paramSavepoint)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.releaseSavepoint(paramSavepoint);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void rollback()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.rollback();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void rollback(Savepoint paramSavepoint)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.rollback(paramSavepoint);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setAutoCommit(boolean paramBoolean)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.setAutoCommit(paramBoolean);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setCatalog(String paramString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.setCatalog(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setHoldability(int paramInt)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.setHoldability(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setReadOnly(boolean paramBoolean)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.setReadOnly(paramBoolean);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Savepoint setSavepoint()
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.setSavepoint();
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Savepoint setSavepoint(String paramString)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      return this.m_connection.setSavepoint(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setTransactionIsolation(int paramInt)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.setTransactionIsolation(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setTypeMap(Map<String, Class<?>> paramMap)
    throws SQLException
  {
    checkIfOpen();
    try
    {
      this.m_connection.setTypeMap(paramMap);
    }
    catch (SQLException localSQLException)
    {
      if (!this.m_connection.getConnection().isAlive()) {
        this.m_pooledConnection.onHandleError(localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Array createArrayOf(String paramString, Object[] paramArrayOfObject)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramArrayOfObject });
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public Blob createBlob()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public Clob createClob()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public NClob createNClob()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public SQLXML createSQLXML()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public Struct createStruct(String paramString, Object[] paramArrayOfObject)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramArrayOfObject });
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public boolean isValid(int paramInt)
    throws SQLException
  {
    return (!isClosed()) && (this.m_connection.getDSIConnection().isAlive());
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  void open()
    throws SQLException
  {
    if (!this.m_isClosed) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_OPEN, this.m_connection.getWarningListener(), ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
    }
    this.m_isClosed = false;
  }
  
  void forceClose()
    throws SQLException
  {
    this.m_connection.close();
  }
  
  protected void checkIfOpen()
    throws SQLException
  {
    if (this.m_isClosed) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_connection.getWarningListener(), ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SConnectionHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */