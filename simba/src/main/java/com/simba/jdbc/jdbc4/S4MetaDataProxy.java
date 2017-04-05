package com.simba.jdbc.jdbc4;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.jdbc.common.SMetaDataProxy;
import com.simba.support.ILogger;
import com.simba.utilities.JDBCVersion;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class S4MetaDataProxy
  extends SMetaDataProxy
{
  protected S4MetaDataProxy(DatabaseMetaData paramDatabaseMetaData, IResultSet paramIResultSet, MetadataSourceID paramMetadataSourceID, ILogger paramILogger)
    throws SQLException
  {
    super(paramDatabaseMetaData, paramIResultSet, paramMetadataSourceID, paramILogger, JDBCVersion.JDBC4);
  }
  
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    if (null == this.m_resultMetaData)
    {
      generateMetadataList();
      this.m_resultMetaData = new S4ResultSetMetaData(this.m_resultSetColumns, this.m_logger, this.m_warningListener);
    }
    return this.m_resultMetaData;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4MetaDataProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */