package com.simba.jdbc.common;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.TypeNames;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SResultSetMetaData
  implements ResultSetMetaData
{
  private List<? extends IColumn> m_columnMetaData = new ArrayList();
  private ILogger m_logger = null;
  private IWarningListener m_warningListener = null;
  
  protected SResultSetMetaData(List<? extends IColumn> paramList, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramList, paramILogger });
    this.m_warningListener = paramIWarningListener;
    this.m_columnMetaData = paramList;
  }
  
  public String getCatalogName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getCatalogName();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getColumnClassName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return TypeNames.getTypeClassName(localTypeMetadata.getType());
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getColumnCount()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      return this.m_columnMetaData.size();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getColumnDisplaySize(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      long l = localIColumn.getDisplaySize();
      return l > 2147483647L ? Integer.MAX_VALUE : (int)l;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getColumnLabel(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getLabel();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getColumnName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getName();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getColumnType(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return TypeUtilities.mapDataTypes(localTypeMetadata.getType());
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getColumnTypeName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return localTypeMetadata.getTypeName();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getPrecision(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      TypeMetadata localTypeMetadata = localIColumn.getTypeMetadata();
      int i = localTypeMetadata.getType();
      if (localTypeMetadata.isCharacterOrBinaryType())
      {
        long l = localIColumn.getColumnLength();
        return l > 2147483647L ? Integer.MAX_VALUE : (int)l;
      }
      if ((91 == i) || (92 == i) || (93 == i)) {
        return (int)localIColumn.getDisplaySize();
      }
      if (localTypeMetadata.isIntervalType()) {
        return localTypeMetadata.getIntervalPrecision();
      }
      return localTypeMetadata.getPrecision();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getScale(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return localTypeMetadata.getScale();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getSchemaName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getSchemaName();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getTableName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getTableName();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isAutoIncrement(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.isAutoUnique();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isCaseSensitive(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.isCaseSensitive();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isCurrency(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      return getColumnMetadata(paramInt).getTypeMetadata().isCurrency();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isDefinitelyWritable(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      return getColumnMetadata(paramInt).isDefinitelyWritable();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int isNullable(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      switch (localIColumn.getNullable())
      {
      case NO_NULLS: 
        return 0;
      case NULLABLE: 
        return 1;
      }
      return 2;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isReadOnly(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return (localIColumn.getUpdatable() == Updatable.READ_ONLY) || (localIColumn.getUpdatable() == Updatable.UNKNOWN);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isSearchable(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getSearchable() != Searchable.PREDICATE_NONE;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isSigned(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return localTypeMetadata.isSigned();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isWritable(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      IColumn localIColumn = getColumnMetadata(paramInt);
      return localIColumn.getUpdatable() == Updatable.WRITE;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  private IColumn getColumnMetadata(int paramInt)
    throws SQLException
  {
    if (!isValidIndex(paramInt))
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return (IColumn)this.m_columnMetaData.get(paramInt - 1);
  }
  
  private TypeMetadata getTypeMetadata(int paramInt)
    throws SQLException
  {
    IColumn localIColumn = getColumnMetadata(paramInt);
    return localIColumn.getTypeMetadata();
  }
  
  private boolean isValidIndex(int paramInt)
  {
    return (0 < paramInt) && (this.m_columnMetaData.size() >= paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SResultSetMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */