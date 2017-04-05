package com.simba.jdbc.jdbc4.utilities;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.exceptions.ExceptionConverter;
import com.simba.jdbc.common.SStatement;
import com.simba.jdbc.jdbc4.S4ForwardResultSet;
import com.simba.jdbc.jdbc4.S4ResultSetMetaData;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.utilities.MetaDataFactory;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class ClientInfoPropertiesResultSet
  extends S4ForwardResultSet
{
  public ClientInfoPropertiesResultSet(SStatement paramSStatement, IResultSet paramIResultSet, ILogger paramILogger)
    throws SQLException
  {
    super(paramSStatement, paramIResultSet, paramILogger);
    initializeColumnNameMap();
  }
  
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      if (null == this.m_resultMetaData)
      {
        List localList = MetaDataFactory.createClientInfoPropertiesMetadata();
        this.m_resultSetColumns = localList;
        this.m_resultMetaData = new S4ResultSetMetaData(localList, this.m_logger, this.m_warningListener);
      }
      return this.m_resultMetaData;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/utilities/ClientInfoPropertiesResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */