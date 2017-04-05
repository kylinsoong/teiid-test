package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.dataengine.utilities.OrderType;
import com.simba.exceptions.ExceptionConverter;
import com.simba.jdbc.common.SConnection;
import com.simba.jdbc.common.SDatabaseMetaData;
import com.simba.jdbc.common.SStatement;
import com.simba.jdbc.common.utilities.ClientInfoPropertiesMetadataSource;
import com.simba.jdbc.jdbc4.utilities.ClientInfoPropertiesResultSet;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class S4DatabaseMetaData
  extends SDatabaseMetaData
{
  protected static final Map<Long, String> STRING_FUNCTION_NAME_MAP = new LinkedHashMap()
  {
    private static final long serialVersionUID = -8873467930715133555L;
  };
  private static final Map<Long, String> TIME_FUNCTION_NAME_MAP = new LinkedHashMap()
  {
    private static final long serialVersionUID = 3646797488007220487L;
  };
  private static final int JDBC_MINOR_VERSION = 0;
  
  public S4DatabaseMetaData(SConnection paramSConnection, ILogger paramILogger)
    throws SQLException
  {
    super(paramSConnection, paramILogger);
  }
  
  protected synchronized ResultSet createMetaDataResult(MetadataSourceID paramMetadataSourceID, List<String> paramList)
    throws SQLException
  {
    try
    {
      IResultSet localIResultSet = this.m_dataEngine.makeNewMetadataResult(paramMetadataSourceID, (ArrayList)paramList, getSearchStringEscape(), getIdentifierQuoteString(), false, OrderType.JDBC_4);
      S4MetaDataProxy localS4MetaDataProxy = new S4MetaDataProxy(this, localIResultSet, paramMetadataSourceID, this.m_logger);
      this.m_resultSets.add(localS4MetaDataProxy);
      return localS4MetaDataProxy;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  protected synchronized ResultSet createNullMetaDataResult(MetadataSourceID paramMetadataSourceID)
    throws SQLException
  {
    try
    {
      S4MetaDataProxy localS4MetaDataProxy = new S4MetaDataProxy(this, null, paramMetadataSourceID, this.m_logger);
      this.m_resultSets.add(localS4MetaDataProxy);
      return localS4MetaDataProxy;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  protected synchronized ResultSet createUnorderedMetaDataResult(MetadataSourceID paramMetadataSourceID, List<String> paramList)
    throws SQLException
  {
    try
    {
      IResultSet localIResultSet = this.m_dataEngine.makeNewMetadataResult(paramMetadataSourceID, (ArrayList)paramList, getSearchStringEscape(), getIdentifierQuoteString(), false, OrderType.NONE);
      S4MetaDataProxy localS4MetaDataProxy = new S4MetaDataProxy(this, localIResultSet, paramMetadataSourceID, this.m_logger);
      this.m_resultSets.add(localS4MetaDataProxy);
      return localS4MetaDataProxy;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  public int getJDBCMinorVersion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return 0;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getStringFunctions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(104);
      return createListFromMap(STRING_FUNCTION_NAME_MAP, l);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getTimeDateFunctions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(135);
      return createListFromMap(TIME_FUNCTION_NAME_MAP, l);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getClientInfoProperties()
    throws SQLException
  {
    Map localMap = this.m_parentConnection.getConnection().getClientInfoProperties();
    return new ClientInfoPropertiesResultSet((SStatement)this.m_parentConnection.createStatement(), new ClientInfoPropertiesMetadataSource(localMap), this.m_logger);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4DatabaseMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */