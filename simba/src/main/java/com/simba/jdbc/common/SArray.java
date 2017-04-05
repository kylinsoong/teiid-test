package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ExceptionType;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class SArray
  implements Array
{
  protected static final int RETRIEVE_ALL_DATA = -1;
  private IArray m_array;
  private IConnection m_parentConnection;
  private ILogger m_logger;
  private IWarningListener m_warningListener;
  
  public SArray(IArray paramIArray, IConnection paramIConnection, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    this.m_array = paramIArray;
    this.m_parentConnection = paramIConnection;
    this.m_logger = paramILogger;
    this.m_warningListener = paramIWarningListener;
  }
  
  public Object getArray()
    throws SQLException
  {
    try
    {
      return createArray(1L, -1);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener());
    }
  }
  
  public Object getArray(Map<String, Class<?>> paramMap)
    throws SQLException
  {
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.FEATURE_NOT_SUPPORTED, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[] { "getArray(Map)" });
  }
  
  public Object getArray(long paramLong, int paramInt)
    throws SQLException
  {
    try
    {
      return createArray(paramLong, paramInt);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener());
    }
  }
  
  public Object getArray(long paramLong, int paramInt, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.FEATURE_NOT_SUPPORTED, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[] { "getArray(long,int,Map)" });
  }
  
  public int getBaseType()
    throws SQLException
  {
    try
    {
      return TypeUtilities.mapDataTypes(this.m_array.getBaseColumn().getTypeMetadata().getType());
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public String getBaseTypeName()
    throws SQLException
  {
    return this.m_array.getBaseColumn().getTypeMetadata().getTypeName();
  }
  
  public final int getUnmappedBaseType()
    throws SQLException
  {
    try
    {
      return this.m_array.getBaseColumn().getTypeMetadata().getType();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public ResultSet getResultSet()
    throws SQLException
  {
    try
    {
      return createResultSet(1L, -1);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public ResultSet getResultSet(Map<String, Class<?>> paramMap)
    throws SQLException
  {
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.FEATURE_NOT_SUPPORTED, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[] { "getResultSet(Map)" });
  }
  
  public ResultSet getResultSet(long paramLong, int paramInt)
    throws SQLException
  {
    try
    {
      return createResultSet(paramLong, paramInt);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public ResultSet getResultSet(long paramLong, int paramInt, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.FEATURE_NOT_SUPPORTED, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[] { "getResultSet(long,int,Map)" });
  }
  
  public void free()
    throws SQLException
  {
    try
    {
      this.m_array.free();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public String toString()
  {
    return getDSIArray().getStringRepresentation();
  }
  
  protected abstract Object createArray(long paramLong, int paramInt)
    throws SQLException;
  
  protected abstract ResultSet createResultSet(long paramLong, int paramInt)
    throws SQLException;
  
  protected IArray getDSIArray()
  {
    return this.m_array;
  }
  
  protected ILogger getLogger()
  {
    return this.m_logger;
  }
  
  protected IConnection getParentConnection()
  {
    return this.m_parentConnection;
  }
  
  protected IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */