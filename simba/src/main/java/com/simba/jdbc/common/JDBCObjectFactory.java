package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.ILogger;
import com.simba.support.exceptions.GeneralException;
import java.sql.SQLException;
import javax.sql.PooledConnection;

public abstract class JDBCObjectFactory
{
  private static JDBCObjectFactory s_factory = null;
  
  static JDBCObjectFactory getInstance()
  {
    return s_factory;
  }
  
  static void setInstance(JDBCObjectFactory paramJDBCObjectFactory)
    throws GeneralException
  {
    if (null == paramJDBCObjectFactory) {
      throw new GeneralException(1, JDBCMessageKey.INITIALIZE_FACTORY.name());
    }
    s_factory = paramJDBCObjectFactory;
  }
  
  protected abstract SCallableStatement createCallableStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException;
  
  protected abstract SConnection createConnection(IConnection paramIConnection, String paramString)
    throws SQLException;
  
  protected abstract SConnectionHandle createConnectionHandle(SConnection paramSConnection, SPooledConnection paramSPooledConnection)
    throws SQLException;
  
  protected abstract SDatabaseMetaData createDatabaseMetaData(SConnection paramSConnection, ILogger paramILogger)
    throws SQLException;
  
  protected abstract SPreparedStatement createPreparedStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException;
  
  protected abstract PooledConnection createPooledConnection(SConnection paramSConnection)
    throws SQLException;
  
  protected abstract SStatement createStatement(IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/JDBCObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */