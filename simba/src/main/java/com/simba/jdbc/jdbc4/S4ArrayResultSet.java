package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ExceptionType;
import java.sql.SQLException;

public class S4ArrayResultSet
  extends S4ForwardResultSet
{
  private S4Array m_parentArray;
  
  public S4ArrayResultSet(S4Array paramS4Array, IResultSet paramIResultSet, ILogger paramILogger)
    throws SQLException
  {
    super(null, paramIResultSet, paramILogger);
    this.m_parentArray = paramS4Array;
    initializeColumnNameMap();
  }
  
  protected void checkIfOpen()
    throws SQLException
  {
    synchronized (this)
    {
      if (!this.m_isOpen) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULTSET_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
    }
  }
  
  protected IConnection getParentConnection()
    throws SQLException
  {
    return this.m_parentArray.getParentConnection();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4ArrayResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */