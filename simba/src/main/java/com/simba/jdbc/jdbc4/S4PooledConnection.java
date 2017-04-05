package com.simba.jdbc.jdbc4;

import com.simba.jdbc.common.SConnection;
import com.simba.jdbc.common.SPooledConnection;
import java.sql.SQLException;

public class S4PooledConnection
  extends SPooledConnection
{
  public S4PooledConnection(SConnection paramSConnection)
    throws SQLException
  {
    super(paramSConnection);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4PooledConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */