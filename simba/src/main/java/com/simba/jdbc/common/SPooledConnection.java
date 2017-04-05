package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

public abstract class SPooledConnection
  implements PooledConnection
{
  private List<ConnectionEventListener> m_listeners = new ArrayList();
  protected SConnectionHandle m_connectionHandle;
  protected ILogger m_logger;
  private List<StatementEventListener> m_statementlisteners = new ArrayList();
  
  public SPooledConnection(SConnection paramSConnection)
    throws SQLException
  {
    this.m_logger = paramSConnection.getConnection().getConnectionLog();
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSConnection });
    this.m_connectionHandle = JDBCObjectFactory.getInstance().createConnectionHandle(paramSConnection, this);
  }
  
  public void addConnectionEventListener(ConnectionEventListener paramConnectionEventListener)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramConnectionEventListener });
    if (null != paramConnectionEventListener) {
      synchronized (this.m_listeners)
      {
        this.m_listeners.add(paramConnectionEventListener);
      }
    }
  }
  
  public void close()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    this.m_connectionHandle.forceClose();
  }
  
  public Connection getConnection()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    this.m_connectionHandle.open();
    return this.m_connectionHandle;
  }
  
  public void removeConnectionEventListener(ConnectionEventListener paramConnectionEventListener)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramConnectionEventListener });
    if (null != paramConnectionEventListener) {
      synchronized (this.m_listeners)
      {
        this.m_listeners.remove(paramConnectionEventListener);
      }
    }
  }
  
  public void onHandleError(SQLException paramSQLException)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    synchronized (this.m_listeners)
    {
      Iterator localIterator = this.m_listeners.iterator();
      while (localIterator.hasNext())
      {
        ConnectionEventListener localConnectionEventListener = (ConnectionEventListener)localIterator.next();
        localConnectionEventListener.connectionErrorOccurred(new ConnectionEvent(this, paramSQLException));
      }
    }
  }
  
  public void addStatementEventListener(StatementEventListener paramStatementEventListener)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramStatementEventListener });
    if (null != paramStatementEventListener) {
      synchronized (this.m_statementlisteners)
      {
        this.m_statementlisteners.add(paramStatementEventListener);
      }
    }
  }
  
  public void removeStatementEventListener(StatementEventListener paramStatementEventListener)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramStatementEventListener });
    if (null != paramStatementEventListener) {
      synchronized (this.m_statementlisteners)
      {
        this.m_statementlisteners.remove(paramStatementEventListener);
      }
    }
  }
  
  public void onHandleStatementClose(PreparedStatement paramPreparedStatement)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    Iterator localIterator = this.m_statementlisteners.iterator();
    while (localIterator.hasNext())
    {
      StatementEventListener localStatementEventListener = (StatementEventListener)localIterator.next();
      localStatementEventListener.statementClosed(new StatementEvent(this, paramPreparedStatement));
    }
  }
  
  public void onHandleStatementError(PreparedStatement paramPreparedStatement, SQLException paramSQLException)
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    Iterator localIterator = this.m_statementlisteners.iterator();
    while (localIterator.hasNext())
    {
      StatementEventListener localStatementEventListener = (StatementEventListener)localIterator.next();
      localStatementEventListener.statementErrorOccurred(new StatementEvent(this, paramPreparedStatement, paramSQLException));
    }
  }
  
  void onHandleClose()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    synchronized (this.m_listeners)
    {
      Iterator localIterator = this.m_listeners.iterator();
      while (localIterator.hasNext())
      {
        ConnectionEventListener localConnectionEventListener = (ConnectionEventListener)localIterator.next();
        localConnectionEventListener.connectionClosed(new ConnectionEvent(this));
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SPooledConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */