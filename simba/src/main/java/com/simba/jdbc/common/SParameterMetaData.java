package com.simba.jdbc.common;

import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.TypeNames;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class SParameterMetaData
  implements ParameterMetaData
{
  private ArrayList<ParameterMetadata> m_parameterMetadata = new ArrayList();
  private boolean m_isOpen = false;
  private ILogger m_logger = null;
  private IWarningListener m_warningListener = null;
  
  protected SParameterMetaData(ArrayList<ParameterMetadata> paramArrayList, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramArrayList, paramILogger });
    this.m_warningListener = paramIWarningListener;
    this.m_isOpen = true;
    this.m_parameterMetadata = paramArrayList;
  }
  
  public String getParameterClassName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      int i = getParameterType(paramInt);
      return TypeNames.getTypeClassName(i);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getParameterCount()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_parameterMetadata.size();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getParameterMode(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      switch (getParameterMetadata(paramInt).getParameterType())
      {
      case INPUT: 
        return 1;
      case OUTPUT: 
        return 4;
      case INPUT_OUTPUT: 
        return 2;
      }
      return 0;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getParameterType(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return localTypeMetadata.getType();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getParameterTypeName(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
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
      checkIfOpen();
      ParameterMetadata localParameterMetadata = getParameterMetadata(paramInt);
      TypeMetadata localTypeMetadata = localParameterMetadata.getTypeMetadata();
      int i = localTypeMetadata.getType();
      if (localTypeMetadata.isCharacterOrBinaryType())
      {
        long l = localParameterMetadata.getColumnLength();
        return l > 2147483647L ? Integer.MAX_VALUE : (int)l;
      }
      if ((91 == i) || (92 == i) || (93 == i)) {
        return (int)TypeUtilities.getDisplaySize(localTypeMetadata, 0L);
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
      checkIfOpen();
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return localTypeMetadata.getScale();
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
      checkIfOpen();
      ParameterMetadata localParameterMetadata = getParameterMetadata(paramInt);
      switch (localParameterMetadata.getNullable())
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
  
  public boolean isSigned(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      TypeMetadata localTypeMetadata = getTypeMetadata(paramInt);
      return localTypeMetadata.isSigned();
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
  
  protected synchronized void close()
  {
    this.m_isOpen = false;
  }
  
  private synchronized void checkIfOpen()
    throws SQLException
  {
    if (!this.m_isOpen)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARAMMETA_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  private ParameterMetadata getParameterMetadata(int paramInt)
    throws SQLException
  {
    if (!isValidIndex(paramInt))
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return (ParameterMetadata)this.m_parameterMetadata.get(paramInt - 1);
  }
  
  private TypeMetadata getTypeMetadata(int paramInt)
    throws SQLException
  {
    ParameterMetadata localParameterMetadata = getParameterMetadata(paramInt);
    return localParameterMetadata.getTypeMetadata();
  }
  
  private boolean isValidIndex(int paramInt)
  {
    return (0 < paramInt) && (this.m_parameterMetadata.size() >= paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SParameterMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */