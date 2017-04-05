package com.simba.jdbc.jdbc4;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class S4PreparedStatementHandle
  implements PreparedStatement
{
  private S4PreparedStatement m_preparedStatement;
  private S4PooledConnection m_pooledConnection;
  
  public S4PreparedStatementHandle(S4PooledConnection paramS4PooledConnection, S4PreparedStatement paramS4PreparedStatement)
  {
    this.m_pooledConnection = paramS4PooledConnection;
    this.m_preparedStatement = paramS4PreparedStatement;
  }
  
  public synchronized void addBatch()
    throws SQLException
  {
    this.m_preparedStatement.addBatch();
  }
  
  public synchronized void addBatch(String paramString)
    throws SQLException
  {
    this.m_preparedStatement.addBatch(paramString);
  }
  
  public void cancel()
    throws SQLException
  {
    this.m_preparedStatement.cancel();
  }
  
  public synchronized void clearBatch()
    throws SQLException
  {
    this.m_preparedStatement.clearBatch();
  }
  
  public synchronized void clearParameters()
    throws SQLException
  {
    this.m_preparedStatement.clearParameters();
  }
  
  public void clearWarnings()
    throws SQLException
  {
    this.m_preparedStatement.clearWarnings();
  }
  
  public synchronized void close()
    throws SQLException
  {
    this.m_preparedStatement.close();
    this.m_pooledConnection.onHandleStatementClose(this.m_preparedStatement);
  }
  
  public synchronized boolean execute()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.execute();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized boolean execute(String paramString)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.execute(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized boolean execute(String paramString, int paramInt)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.execute(paramString, paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized boolean execute(String paramString, int[] paramArrayOfInt)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.execute(paramString, paramArrayOfInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized boolean execute(String paramString, String[] paramArrayOfString)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.execute(paramString, paramArrayOfString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int[] executeBatch()
    throws SQLException, BatchUpdateException
  {
    try
    {
      return this.m_preparedStatement.executeBatch();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized ResultSet executeQuery()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeQuery();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized ResultSet executeQuery(String paramString)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeQuery(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int executeUpdate()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeUpdate();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int executeUpdate(String paramString)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeUpdate(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int executeUpdate(String paramString, int paramInt)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeUpdate(paramString, paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int executeUpdate(String paramString, int[] paramArrayOfInt)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeUpdate(paramString, paramArrayOfInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int executeUpdate(String paramString, String[] paramArrayOfString)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.executeUpdate(paramString, paramArrayOfString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public Connection getConnection()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getConnection();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int getFetchDirection()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getFetchDirection();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized int getFetchSize()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getFetchSize();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public ResultSet getGeneratedKeys()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getGeneratedKeys();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getMaxFieldSize()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getMaxFieldSize();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getMaxRows()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getMaxRows();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getMetaData();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public boolean getMoreResults()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getMoreResults();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized boolean getMoreResults(int paramInt)
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getMoreResults(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public ParameterMetaData getParameterMetaData()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getParameterMetaData();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getQueryTimeout()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getQueryTimeout();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public ResultSet getResultSet()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getResultSet();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getResultSetConcurrency()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getResultSetConcurrency();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getResultSetHoldability()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getResultSetHoldability();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getResultSetType()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getResultSetType();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public int getUpdateCount()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getUpdateCount();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public SQLWarning getWarnings()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.getWarnings();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public boolean isClosed()
    throws SQLException
  {
    return this.m_preparedStatement.isClosed();
  }
  
  public boolean isPoolable()
    throws SQLException
  {
    try
    {
      return this.m_preparedStatement.isPoolable();
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return this.m_preparedStatement.isWrapperFor(paramClass);
  }
  
  public synchronized void setArray(int paramInt, Array paramArray)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setArray(paramInt, paramArray);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setAsciiStream(paramInt, paramInputStream);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setAsciiStream(paramInt1, paramInputStream, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setAsciiStream(paramInt, paramInputStream, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setBigDecimal(paramInt, paramBigDecimal);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setBinaryStream(paramInt, paramInputStream);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setBinaryStream(paramInt1, paramInputStream, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setBinaryStream(paramInt, paramInputStream, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setBlob(int paramInt, Blob paramBlob)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setBlob(paramInt, paramBlob);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setBlob(paramInt, paramInputStream);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setBlob(paramInt, paramInputStream, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setBoolean(int paramInt, boolean paramBoolean)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setBoolean(paramInt, paramBoolean);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setByte(int paramInt, byte paramByte)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setByte(paramInt, paramByte);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setBytes(paramInt, paramArrayOfByte);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setCharacterStream(paramInt, paramReader);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setCharacterStream(paramInt1, paramReader, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setCharacterStream(paramInt, paramReader, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setClob(int paramInt, Clob paramClob)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setClob(paramInt, paramClob);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setClob(paramInt, paramReader);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setClob(paramInt, paramReader, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setCursorName(String paramString)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setCursorName(paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setDate(int paramInt, Date paramDate)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setDate(paramInt, paramDate);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setDate(paramInt, paramDate, paramCalendar);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setDouble(int paramInt, double paramDouble)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setDouble(paramInt, paramDouble);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setEscapeProcessing(boolean paramBoolean)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setEscapeProcessing(paramBoolean);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setFetchDirection(int paramInt)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setFetchDirection(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setFetchSize(int paramInt)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setFetchSize(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setFloat(int paramInt, float paramFloat)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setFloat(paramInt, paramFloat);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setInt(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setInt(paramInt1, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setLong(int paramInt, long paramLong)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setLong(paramInt, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setMaxFieldSize(int paramInt)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setMaxFieldSize(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setMaxRows(int paramInt)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setMaxRows(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setNCharacterStream(paramInt, paramReader);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setNCharacterStream(paramInt, paramReader, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setNClob(int paramInt, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setNClob(paramInt, paramNClob);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setNClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setNClob(paramInt, paramReader);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setNClob(paramInt, paramReader, paramLong);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setNString(int paramInt, String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setNString(paramInt, paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setNull(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setNull(paramInt1, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setNull(int paramInt1, int paramInt2, String paramString)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setNull(paramInt1, paramInt2, paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setObject(int paramInt, Object paramObject)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setObject(paramInt, paramObject);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setObject(paramInt1, paramObject, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setObject(paramInt1, paramObject, paramInt2, paramInt3);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setPoolable(boolean paramBoolean)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setPoolable(paramBoolean);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setQueryTimeout(int paramInt)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setQueryTimeout(paramInt);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setRef(int paramInt, Ref paramRef)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setRef(paramInt, paramRef);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setRowId(int paramInt, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setRowId(paramInt, paramRowId);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setShort(int paramInt, short paramShort)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setShort(paramInt, paramShort);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public void setSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      this.m_preparedStatement.setSQLXML(paramInt, paramSQLXML);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setString(int paramInt, String paramString)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setString(paramInt, paramString);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setTime(int paramInt, Time paramTime)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setTime(paramInt, paramTime);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setTime(paramInt, paramTime, paramCalendar);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setTimestamp(paramInt, paramTimestamp);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setTimestamp(paramInt, paramTimestamp, paramCalendar);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  /**
   * @deprecated
   */
  public synchronized void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setUnicodeStream(paramInt1, paramInputStream, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public synchronized void setURL(int paramInt, URL paramURL)
    throws SQLException
  {
    try
    {
      this.m_preparedStatement.setURL(paramInt, paramURL);
    }
    catch (SQLException localSQLException)
    {
      if (this.m_preparedStatement.isClosed()) {
        this.m_pooledConnection.onHandleStatementError(this.m_preparedStatement, localSQLException);
      }
      throw localSQLException;
    }
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)this.m_preparedStatement.unwrap(paramClass);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4PreparedStatementHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */