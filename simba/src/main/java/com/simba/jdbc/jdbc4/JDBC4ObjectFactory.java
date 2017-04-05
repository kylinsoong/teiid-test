package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.jdbc.common.JDBCObjectFactory;
import com.simba.jdbc.common.SCallableStatement;
import com.simba.jdbc.common.SConnection;
import com.simba.jdbc.common.SConnectionHandle;
import com.simba.jdbc.common.SDatabaseMetaData;
import com.simba.jdbc.common.SPooledConnection;
import com.simba.jdbc.common.SPreparedStatement;
import com.simba.jdbc.common.SStatement;
import com.simba.support.ILogger;
import java.sql.SQLException;
import javax.sql.PooledConnection;

public class JDBC4ObjectFactory
  extends JDBCObjectFactory
{
  protected SCallableStatement createCallableStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException
  {
    return new S4CallableStatement(paramString, paramIStatement, paramSConnection, paramInt);
  }
  
  protected SConnection createConnection(IConnection paramIConnection, String paramString)
    throws SQLException
  {
    return new S4Connection(paramIConnection, paramString);
  }
  
  protected SConnectionHandle createConnectionHandle(SConnection paramSConnection, SPooledConnection paramSPooledConnection)
    throws SQLException
  {
    return new S4ConnectionHandle(paramSConnection, paramSPooledConnection);
  }
  
  protected SDatabaseMetaData createDatabaseMetaData(SConnection paramSConnection, ILogger paramILogger)
    throws SQLException
  {
    return new S4DatabaseMetaData(paramSConnection, paramILogger);
  }
  
  protected PooledConnection createPooledConnection(SConnection paramSConnection)
    throws SQLException
  {
    return new S4PooledConnection(paramSConnection);
  }
  
  protected SPreparedStatement createPreparedStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException
  {
    return new S4PreparedStatement(paramString, paramIStatement, paramSConnection, paramInt);
  }
  
  protected SStatement createStatement(IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException
  {
    return new S4Statement(paramIStatement, paramSConnection, paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/JDBC4ObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */