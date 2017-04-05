package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.streams.IStream;
import com.simba.streams.resultset.AsciiStream;
import com.simba.streams.resultset.BinaryStream;
import com.simba.streams.resultset.CharacterStream;
import com.simba.streams.resultset.UnicodeStream;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.CursorPosition;
import com.simba.utilities.FunctionID;
import com.simba.utilities.JDBCVersion;
import com.simba.utilities.conversion.TypeConverter;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class SForwardResultSet
  implements ResultSet
{
  protected ILogger m_logger = null;
  protected SResultSetMetaData m_resultMetaData = null;
  protected int m_currentRow = 0;
  protected IStream m_currentStream = null;
  protected boolean m_isOpen = true;
  protected IResultSet m_resultSet = null;
  protected List<? extends IColumn> m_resultSetColumns = null;
  protected SWarningListener m_warningListener = null;
  protected boolean m_wasLastValueNull = false;
  protected List<DataWrapper> m_cachedDataWrappers = null;
  private CursorPosition m_cursorPosition = CursorPosition.BEFORE_FIRST;
  private List<String> m_columnNameIndexes = null;
  private int m_numColumns;
  private final boolean m_canCallHasMoreRows;
  protected SStatement m_parentStatement = null;
  protected JDBCVersion m_jdbcVersion;
  protected int m_streamBufferSize;
  protected final int m_maxRows;
  
  protected SForwardResultSet(SStatement paramSStatement, IResultSet paramIResultSet, ILogger paramILogger)
    throws SQLException
  {
    try
    {
      this.m_logger = paramILogger;
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSStatement, paramIResultSet, paramILogger });
      this.m_isOpen = true;
      this.m_parentStatement = paramSStatement;
      this.m_resultSet = paramIResultSet;
      this.m_warningListener = new SWarningListener(DSIDriverSingleton.getInstance().getMessageSource(), null);
      if (null != paramSStatement)
      {
        this.m_streamBufferSize = getStreamBufferSize();
        this.m_warningListener.setLocale(paramSStatement.getParentConnection().getDSIConnection().getLocale());
        this.m_maxRows = paramSStatement.getMaxRows();
      }
      else
      {
        this.m_maxRows = 0;
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      if (null != this.m_resultSet)
      {
        this.m_resultSet.registerWarningListener(this.m_warningListener);
        this.m_canCallHasMoreRows = paramIResultSet.supportsHasMoreRows();
      }
      else
      {
        this.m_canCallHasMoreRows = false;
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean absolute(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      if (1003 == getType()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void afterLast()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (1003 == getType()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void beforeFirst()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (1003 == getType()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void cancelRowUpdates()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void clearWarnings()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_warningListener.clear();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void close()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    this.m_isOpen = false;
    if (null != this.m_parentStatement)
    {
      this.m_parentStatement.markResultSetClosed(this);
      this.m_parentStatement = null;
    }
    if (null != this.m_resultSet)
    {
      this.m_resultSet.close();
      this.m_resultSet = null;
    }
    if (null != this.m_resultMetaData) {
      this.m_resultMetaData = null;
    }
  }
  
  public void deleteRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int findColumn(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      int i = this.m_columnNameIndexes.indexOf(paramString.toUpperCase(Locale.ENGLISH));
      if (-1 == i) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_NAME, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { paramString });
      }
      return i + 1;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean first()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (1003 == getType()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public Array getArray(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_2
    //   36: invokevirtual 58	com/simba/dsi/dataengine/utilities/DataWrapper:getArray	()Lcom/simba/dsi/dataengine/interfaces/IArray;
    //   39: astore_3
    //   40: aload_0
    //   41: aload_2
    //   42: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   45: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   48: aload_2
    //   49: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   52: ifeq +5 -> 57
    //   55: aconst_null
    //   56: areturn
    //   57: aload_0
    //   58: aload_3
    //   59: invokevirtual 60	com/simba/jdbc/common/SForwardResultSet:createArrayResult	(Lcom/simba/dsi/dataengine/interfaces/IArray;)Lcom/simba/jdbc/common/SArray;
    //   62: areturn
    //   63: astore_3
    //   64: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   67: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   70: aload_0
    //   71: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   74: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   77: iconst_2
    //   78: anewarray 16	java/lang/Object
    //   81: dup
    //   82: iconst_0
    //   83: iload_1
    //   84: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   87: aastore
    //   88: dup
    //   89: iconst_1
    //   90: sipush 2003
    //   93: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   96: aastore
    //   97: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   100: athrow
    //   101: astore_2
    //   102: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   105: aload_2
    //   106: aload_0
    //   107: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   110: aload_0
    //   111: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   114: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   117: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	118	0	this	SForwardResultSet
    //   0	118	1	paramInt	int
    //   34	15	2	localDataWrapper	DataWrapper
    //   101	5	2	localException	Exception
    //   39	20	3	localIArray	IArray
    //   63	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	56	63	com/simba/dsi/exceptions/IncorrectTypeException
    //   57	62	63	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	56	101	java/lang/Exception
    //   57	62	101	java/lang/Exception
    //   63	101	101	java/lang/Exception
  }
  
  public Array getArray(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getArray(findColumn(paramString));
  }
  
  public InputStream getAsciiStream(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      closeCurrentStream();
      DataWrapper localDataWrapper = getData(paramInt, 0L);
      if (TypeConverter.canConvertStreamFrom(localDataWrapper.getType(), -1))
      {
        this.m_wasLastValueNull = localDataWrapper.isNull();
        if (localDataWrapper.isNull()) {
          return null;
        }
        AsciiStream localAsciiStream = new AsciiStream(this.m_resultSet, paramInt - 1, this.m_streamBufferSize);
        this.m_currentStream = localAsciiStream;
        return localAsciiStream;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), "AsciiStream" });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public InputStream getAsciiStream(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getAsciiStream(findColumn(paramString));
  }
  
  /* Error */
  public BigDecimal getBigDecimal(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 74	com/simba/utilities/conversion/TypeConverter:toBigDecimal	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)Ljava/math/BigDecimal;
    //   51: areturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: iconst_2
    //   80: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   83: aastore
    //   84: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   87: athrow
    //   88: astore_2
    //   89: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   92: aload_2
    //   93: aload_0
    //   94: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   97: aload_0
    //   98: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   101: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   104: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	SForwardResultSet
    //   0	105	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   88	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	88	java/lang/Exception
    //   52	88	88	java/lang/Exception
  }
  
  /**
   * @deprecated
   */
  public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    BigDecimal localBigDecimal = getBigDecimal(paramInt1);
    if (null != localBigDecimal) {
      return localBigDecimal.setScale(paramInt2, RoundingMode.HALF_UP);
    }
    return null;
  }
  
  public BigDecimal getBigDecimal(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBigDecimal(findColumn(paramString));
  }
  
  /**
   * @deprecated
   */
  public BigDecimal getBigDecimal(String paramString, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
    return getBigDecimal(findColumn(paramString), paramInt);
  }
  
  public InputStream getBinaryStream(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      closeCurrentStream();
      DataWrapper localDataWrapper = getData(paramInt, 0L);
      if (TypeConverter.canConvertStreamFrom(localDataWrapper.getType(), -4))
      {
        this.m_wasLastValueNull = localDataWrapper.isNull();
        if (localDataWrapper.isNull()) {
          return null;
        }
        BinaryStream localBinaryStream = new BinaryStream(this.m_resultSet, paramInt - 1, this.m_streamBufferSize);
        this.m_currentStream = localBinaryStream;
        return localBinaryStream;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), "BinaryStream" });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public InputStream getBinaryStream(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBinaryStream(findColumn(paramString));
  }
  
  public Blob getBlob(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Blob getBlob(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBlob(findColumn(paramString));
  }
  
  /* Error */
  public boolean getBoolean(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 85	com/simba/utilities/conversion/TypeConverter:toBoolean	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)Z
    //   51: ireturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: bipush 16
    //   81: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   84: aastore
    //   85: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   88: athrow
    //   89: astore_2
    //   90: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   93: aload_2
    //   94: aload_0
    //   95: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   98: aload_0
    //   99: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   102: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   105: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	106	0	this	SForwardResultSet
    //   0	106	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   89	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	89	java/lang/Exception
    //   52	89	89	java/lang/Exception
  }
  
  public boolean getBoolean(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBoolean(findColumn(paramString));
  }
  
  /* Error */
  public byte getByte(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 87	com/simba/utilities/conversion/TypeConverter:toByte	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)B
    //   51: ireturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: bipush -6
    //   81: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   84: aastore
    //   85: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   88: athrow
    //   89: astore_2
    //   90: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   93: aload_2
    //   94: aload_0
    //   95: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   98: aload_0
    //   99: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   102: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   105: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	106	0	this	SForwardResultSet
    //   0	106	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   89	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	89	java/lang/Exception
    //   52	89	89	java/lang/Exception
  }
  
  public byte getByte(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getByte(findColumn(paramString));
  }
  
  /* Error */
  public byte[] getBytes(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: invokestatic 89	com/simba/utilities/conversion/TypeConverter:toBytes	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;)[B
    //   47: areturn
    //   48: astore_3
    //   49: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   52: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   55: aload_0
    //   56: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   59: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   62: iconst_2
    //   63: anewarray 16	java/lang/Object
    //   66: dup
    //   67: iconst_0
    //   68: iload_1
    //   69: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   72: aastore
    //   73: dup
    //   74: iconst_1
    //   75: bipush -2
    //   77: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   80: aastore
    //   81: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   84: athrow
    //   85: astore_2
    //   86: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   89: aload_2
    //   90: aload_0
    //   91: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   94: aload_0
    //   95: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   98: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   101: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	102	0	this	SForwardResultSet
    //   0	102	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   85	5	2	localException	Exception
    //   48	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	47	48	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	47	85	java/lang/Exception
    //   48	85	85	java/lang/Exception
  }
  
  public byte[] getBytes(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBytes(findColumn(paramString));
  }
  
  public Reader getCharacterStream(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      closeCurrentStream();
      DataWrapper localDataWrapper = getData(paramInt, 0L);
      if (TypeConverter.canConvertStreamFrom(localDataWrapper.getType(), -1))
      {
        this.m_wasLastValueNull = localDataWrapper.isNull();
        if (localDataWrapper.isNull()) {
          return null;
        }
        CharacterStream localCharacterStream = new CharacterStream(this.m_resultSet, paramInt - 1, this.m_streamBufferSize);
        this.m_currentStream = localCharacterStream;
        return localCharacterStream;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), "CharacterStream" });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Reader getCharacterStream(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getCharacterStream(findColumn(paramString));
  }
  
  public Clob getClob(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Clob getClob(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getClob(findColumn(paramString));
  }
  
  public int getConcurrency()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return 1007;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getCursorName()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_parentStatement.getStatement().getCursorName();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public Date getDate(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_2
    //   36: aload_0
    //   37: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   40: invokestatic 98	com/simba/utilities/conversion/TypeConverter:toDate	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)Ljava/sql/Date;
    //   43: astore_3
    //   44: aload_0
    //   45: aload_2
    //   46: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   49: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   52: aload_3
    //   53: areturn
    //   54: astore_3
    //   55: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   58: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   61: aload_0
    //   62: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   65: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   68: iconst_2
    //   69: anewarray 16	java/lang/Object
    //   72: dup
    //   73: iconst_0
    //   74: iload_1
    //   75: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   78: aastore
    //   79: dup
    //   80: iconst_1
    //   81: bipush 91
    //   83: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   86: aastore
    //   87: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   90: athrow
    //   91: astore_2
    //   92: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   95: aload_2
    //   96: aload_0
    //   97: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   100: aload_0
    //   101: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   104: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   107: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	108	0	this	SForwardResultSet
    //   0	108	1	paramInt	int
    //   34	12	2	localDataWrapper	DataWrapper
    //   91	5	2	localException	Exception
    //   43	10	3	localDate	Date
    //   54	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	53	54	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	53	91	java/lang/Exception
    //   54	91	91	java/lang/Exception
  }
  
  /* Error */
  public Date getDate(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_2
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: dup
    //   16: iconst_1
    //   17: aload_2
    //   18: aastore
    //   19: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   22: aload_0
    //   23: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   26: aload_0
    //   27: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   30: aload_0
    //   31: iload_1
    //   32: ldc2_w 55
    //   35: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   38: astore_3
    //   39: aload_3
    //   40: aload_2
    //   41: aload_0
    //   42: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   45: invokestatic 99	com/simba/utilities/conversion/TypeConverter:toDate	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Ljava/util/Calendar;Lcom/simba/support/IWarningListener;)Ljava/sql/Date;
    //   48: astore 4
    //   50: aload_0
    //   51: aload_3
    //   52: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   55: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   58: aload 4
    //   60: areturn
    //   61: astore 4
    //   63: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   66: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   69: aload_0
    //   70: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   73: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   76: iconst_2
    //   77: anewarray 16	java/lang/Object
    //   80: dup
    //   81: iconst_0
    //   82: iload_1
    //   83: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   86: aastore
    //   87: dup
    //   88: iconst_1
    //   89: bipush 91
    //   91: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   94: aastore
    //   95: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   98: athrow
    //   99: astore_3
    //   100: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   103: aload_3
    //   104: aload_0
    //   105: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   108: aload_0
    //   109: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   112: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   115: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	116	0	this	SForwardResultSet
    //   0	116	1	paramInt	int
    //   0	116	2	paramCalendar	Calendar
    //   38	14	3	localDataWrapper	DataWrapper
    //   99	5	3	localException	Exception
    //   48	11	4	localDate	Date
    //   61	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   39	60	61	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	60	99	java/lang/Exception
    //   61	99	99	java/lang/Exception
  }
  
  public Date getDate(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getDate(findColumn(paramString));
  }
  
  public Date getDate(String paramString, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramCalendar });
    return getDate(findColumn(paramString), paramCalendar);
  }
  
  /* Error */
  public double getDouble(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 102	com/simba/utilities/conversion/TypeConverter:toDouble	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)D
    //   51: dreturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: bipush 8
    //   81: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   84: aastore
    //   85: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   88: athrow
    //   89: astore_2
    //   90: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   93: aload_2
    //   94: aload_0
    //   95: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   98: aload_0
    //   99: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   102: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   105: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	106	0	this	SForwardResultSet
    //   0	106	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   89	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	89	java/lang/Exception
    //   52	89	89	java/lang/Exception
  }
  
  public double getDouble(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getDouble(findColumn(paramString));
  }
  
  public int getFetchDirection()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return 1000;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getFetchSize()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_resultSet.getFetchSize();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public float getFloat(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 105	com/simba/utilities/conversion/TypeConverter:toFloat	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)F
    //   51: freturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: bipush 7
    //   81: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   84: aastore
    //   85: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   88: athrow
    //   89: astore_2
    //   90: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   93: aload_2
    //   94: aload_0
    //   95: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   98: aload_0
    //   99: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   102: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   105: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	106	0	this	SForwardResultSet
    //   0	106	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   89	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	89	java/lang/Exception
    //   52	89	89	java/lang/Exception
  }
  
  public float getFloat(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getFloat(findColumn(paramString));
  }
  
  /* Error */
  public int getInt(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 107	com/simba/utilities/conversion/TypeConverter:toInt	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)I
    //   51: ireturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: iconst_4
    //   80: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   83: aastore
    //   84: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   87: athrow
    //   88: astore_2
    //   89: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   92: aload_2
    //   93: aload_0
    //   94: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   97: aload_0
    //   98: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   101: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   104: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	SForwardResultSet
    //   0	105	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   88	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	88	java/lang/Exception
    //   52	88	88	java/lang/Exception
  }
  
  public int getInt(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getInt(findColumn(paramString));
  }
  
  public ILogger getLogger()
  {
    return this.m_logger;
  }
  
  /* Error */
  public long getLong(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 109	com/simba/utilities/conversion/TypeConverter:toLong	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)J
    //   51: lreturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: bipush -5
    //   81: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   84: aastore
    //   85: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   88: athrow
    //   89: astore_2
    //   90: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   93: aload_2
    //   94: aload_0
    //   95: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   98: aload_0
    //   99: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   102: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   105: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	106	0	this	SForwardResultSet
    //   0	106	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   89	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	89	java/lang/Exception
    //   52	89	89	java/lang/Exception
  }
  
  public long getLong(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getLong(findColumn(paramString));
  }
  
  public abstract ResultSetMetaData getMetaData()
    throws SQLException;
  
  /* Error */
  public Object getObject(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: getfield 8	com/simba/jdbc/common/SForwardResultSet:m_resultSetColumns	Ljava/util/List;
    //   39: iload_1
    //   40: iconst_1
    //   41: isub
    //   42: invokeinterface 111 2 0
    //   47: checkcast 112	com/simba/dsi/dataengine/interfaces/IColumn
    //   50: astore_3
    //   51: sipush 2003
    //   54: aload_3
    //   55: invokeinterface 113 1 0
    //   60: invokevirtual 114	com/simba/dsi/dataengine/utilities/TypeMetadata:getType	()S
    //   63: if_icmpne +9 -> 72
    //   66: aload_0
    //   67: iload_1
    //   68: invokevirtual 67	com/simba/jdbc/common/SForwardResultSet:getArray	(I)Ljava/sql/Array;
    //   71: areturn
    //   72: aload_2
    //   73: aload_3
    //   74: invokeinterface 113 1 0
    //   79: aload_0
    //   80: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   83: invokestatic 115	com/simba/utilities/conversion/TypeConverter:toObject	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/lang/Object;
    //   86: astore 4
    //   88: aload_0
    //   89: aload_2
    //   90: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   93: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   96: aload 4
    //   98: areturn
    //   99: astore_3
    //   100: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   103: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   106: aload_0
    //   107: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   110: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   113: iconst_2
    //   114: anewarray 16	java/lang/Object
    //   117: dup
    //   118: iconst_0
    //   119: iload_1
    //   120: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   123: aastore
    //   124: dup
    //   125: iconst_1
    //   126: sipush 2000
    //   129: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   132: aastore
    //   133: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   136: athrow
    //   137: astore_2
    //   138: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   141: aload_2
    //   142: aload_0
    //   143: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   146: aload_0
    //   147: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   150: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   153: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	154	0	this	SForwardResultSet
    //   0	154	1	paramInt	int
    //   34	56	2	localDataWrapper	DataWrapper
    //   137	5	2	localException	Exception
    //   50	24	3	localIColumn	IColumn
    //   99	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   86	11	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   35	71	99	com/simba/dsi/exceptions/IncorrectTypeException
    //   72	98	99	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	71	137	java/lang/Exception
    //   72	98	137	java/lang/Exception
    //   99	137	137	java/lang/Exception
  }
  
  public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramMap });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Object getObject(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getObject(findColumn(paramString));
  }
  
  public Object getObject(String paramString, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramMap });
    return getObject(findColumn(paramString), paramMap);
  }
  
  public Ref getRef(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Ref getRef(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getRef(findColumn(paramString));
  }
  
  protected List<? extends IColumn> getResultSetColumns()
  {
    return this.m_resultSetColumns;
  }
  
  protected SResultSetMetaData getResultSetMetaData()
  {
    return this.m_resultMetaData;
  }
  
  public int getRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if ((CursorPosition.AFTER_LAST == this.m_cursorPosition) || (CursorPosition.BEFORE_FIRST == this.m_cursorPosition)) {
        return 0;
      }
      return this.m_currentRow;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public long getRowCount()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_resultSet.getRowCount();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public short getShort(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_2
    //   44: aload_0
    //   45: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   48: invokestatic 121	com/simba/utilities/conversion/TypeConverter:toShort	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)S
    //   51: ireturn
    //   52: astore_3
    //   53: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   56: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   59: aload_0
    //   60: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   63: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   66: iconst_2
    //   67: anewarray 16	java/lang/Object
    //   70: dup
    //   71: iconst_0
    //   72: iload_1
    //   73: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   76: aastore
    //   77: dup
    //   78: iconst_1
    //   79: iconst_5
    //   80: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   83: aastore
    //   84: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   87: athrow
    //   88: astore_2
    //   89: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   92: aload_2
    //   93: aload_0
    //   94: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   97: aload_0
    //   98: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   101: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   104: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	SForwardResultSet
    //   0	105	1	paramInt	int
    //   34	10	2	localDataWrapper	DataWrapper
    //   88	5	2	localException	Exception
    //   52	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	51	52	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	51	88	java/lang/Exception
    //   52	88	88	java/lang/Exception
  }
  
  public short getShort(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getShort(findColumn(paramString));
  }
  
  public Statement getStatement()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_parentStatement;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public String getString(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_0
    //   44: getfield 8	com/simba/jdbc/common/SForwardResultSet:m_resultSetColumns	Ljava/util/List;
    //   47: iload_1
    //   48: iconst_1
    //   49: isub
    //   50: invokeinterface 111 2 0
    //   55: checkcast 112	com/simba/dsi/dataengine/interfaces/IColumn
    //   58: astore_3
    //   59: aload_2
    //   60: aload_3
    //   61: invokeinterface 113 1 0
    //   66: invokestatic 123	com/simba/utilities/conversion/TypeConverter:toString	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;)Ljava/lang/String;
    //   69: areturn
    //   70: astore_3
    //   71: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   74: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   77: aload_0
    //   78: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   81: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   84: iconst_2
    //   85: anewarray 16	java/lang/Object
    //   88: dup
    //   89: iconst_0
    //   90: iload_1
    //   91: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   94: aastore
    //   95: dup
    //   96: iconst_1
    //   97: bipush 12
    //   99: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   102: aastore
    //   103: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   106: athrow
    //   107: astore_2
    //   108: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   111: aload_2
    //   112: aload_0
    //   113: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   116: aload_0
    //   117: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   120: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   123: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	124	0	this	SForwardResultSet
    //   0	124	1	paramInt	int
    //   34	26	2	localDataWrapper	DataWrapper
    //   107	5	2	localException	Exception
    //   58	3	3	localIColumn	IColumn
    //   70	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	69	70	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	69	107	java/lang/Exception
    //   70	107	107	java/lang/Exception
  }
  
  public String getString(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getString(findColumn(paramString));
  }
  
  /* Error */
  public Time getTime(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: getfield 8	com/simba/jdbc/common/SForwardResultSet:m_resultSetColumns	Ljava/util/List;
    //   39: iload_1
    //   40: iconst_1
    //   41: isub
    //   42: invokeinterface 111 2 0
    //   47: checkcast 112	com/simba/dsi/dataengine/interfaces/IColumn
    //   50: astore_3
    //   51: aload_2
    //   52: aload_3
    //   53: invokeinterface 113 1 0
    //   58: aload_0
    //   59: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   62: invokestatic 125	com/simba/utilities/conversion/TypeConverter:toTime	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Time;
    //   65: astore 4
    //   67: aload_0
    //   68: aload_2
    //   69: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   72: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   75: aload 4
    //   77: areturn
    //   78: astore_3
    //   79: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   82: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   85: aload_0
    //   86: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   89: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   92: iconst_2
    //   93: anewarray 16	java/lang/Object
    //   96: dup
    //   97: iconst_0
    //   98: iload_1
    //   99: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   102: aastore
    //   103: dup
    //   104: iconst_1
    //   105: bipush 92
    //   107: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   110: aastore
    //   111: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   114: athrow
    //   115: astore_2
    //   116: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   119: aload_2
    //   120: aload_0
    //   121: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   124: aload_0
    //   125: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   128: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   131: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	132	0	this	SForwardResultSet
    //   0	132	1	paramInt	int
    //   34	35	2	localDataWrapper	DataWrapper
    //   115	5	2	localException	Exception
    //   50	3	3	localIColumn	IColumn
    //   78	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   65	11	4	localTime	Time
    // Exception table:
    //   from	to	target	type
    //   35	77	78	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	77	115	java/lang/Exception
    //   78	115	115	java/lang/Exception
  }
  
  /* Error */
  public Time getTime(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_3
    //   35: aload_0
    //   36: getfield 8	com/simba/jdbc/common/SForwardResultSet:m_resultSetColumns	Ljava/util/List;
    //   39: iload_1
    //   40: iconst_1
    //   41: isub
    //   42: invokeinterface 111 2 0
    //   47: checkcast 112	com/simba/dsi/dataengine/interfaces/IColumn
    //   50: astore 4
    //   52: aload_3
    //   53: aload_2
    //   54: aload 4
    //   56: invokeinterface 113 1 0
    //   61: aload_0
    //   62: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   65: invokestatic 126	com/simba/utilities/conversion/TypeConverter:toTime	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Ljava/util/Calendar;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Time;
    //   68: astore 5
    //   70: aload_0
    //   71: aload_3
    //   72: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   75: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   78: aload 5
    //   80: areturn
    //   81: astore 4
    //   83: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   86: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   89: aload_0
    //   90: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   96: iconst_2
    //   97: anewarray 16	java/lang/Object
    //   100: dup
    //   101: iconst_0
    //   102: iload_1
    //   103: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   106: aastore
    //   107: dup
    //   108: iconst_1
    //   109: bipush 92
    //   111: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   114: aastore
    //   115: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   118: athrow
    //   119: astore_3
    //   120: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   123: aload_3
    //   124: aload_0
    //   125: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   128: aload_0
    //   129: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   132: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   135: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	136	0	this	SForwardResultSet
    //   0	136	1	paramInt	int
    //   0	136	2	paramCalendar	Calendar
    //   34	38	3	localDataWrapper	DataWrapper
    //   119	5	3	localException	Exception
    //   50	5	4	localIColumn	IColumn
    //   81	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   68	11	5	localTime	Time
    // Exception table:
    //   from	to	target	type
    //   35	80	81	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	80	119	java/lang/Exception
    //   81	119	119	java/lang/Exception
  }
  
  public Time getTime(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getTime(findColumn(paramString));
  }
  
  public Time getTime(String paramString, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramCalendar });
    return getTime(findColumn(paramString), paramCalendar);
  }
  
  /* Error */
  public Timestamp getTimestamp(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: ldc2_w 55
    //   31: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   34: astore_2
    //   35: aload_0
    //   36: aload_2
    //   37: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   40: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   43: aload_0
    //   44: getfield 8	com/simba/jdbc/common/SForwardResultSet:m_resultSetColumns	Ljava/util/List;
    //   47: iload_1
    //   48: iconst_1
    //   49: isub
    //   50: invokeinterface 111 2 0
    //   55: checkcast 112	com/simba/dsi/dataengine/interfaces/IColumn
    //   58: astore_3
    //   59: aload_2
    //   60: aload_3
    //   61: invokeinterface 113 1 0
    //   66: aload_0
    //   67: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   70: invokestatic 129	com/simba/utilities/conversion/TypeConverter:toTimestamp	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Timestamp;
    //   73: areturn
    //   74: astore_3
    //   75: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   78: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   81: aload_0
    //   82: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   85: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   88: iconst_2
    //   89: anewarray 16	java/lang/Object
    //   92: dup
    //   93: iconst_0
    //   94: iload_1
    //   95: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   98: aastore
    //   99: dup
    //   100: iconst_1
    //   101: bipush 93
    //   103: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   106: aastore
    //   107: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   110: athrow
    //   111: astore_2
    //   112: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   115: aload_2
    //   116: aload_0
    //   117: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   120: aload_0
    //   121: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   124: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   127: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	128	0	this	SForwardResultSet
    //   0	128	1	paramInt	int
    //   34	26	2	localDataWrapper	DataWrapper
    //   111	5	2	localException	Exception
    //   58	3	3	localIColumn	IColumn
    //   74	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   35	73	74	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	73	111	java/lang/Exception
    //   74	111	111	java/lang/Exception
  }
  
  /* Error */
  public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_2
    //   5: anewarray 16	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 38	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: dup
    //   16: iconst_1
    //   17: aload_2
    //   18: aastore
    //   19: invokestatic 17	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   22: aload_0
    //   23: invokevirtual 39	com/simba/jdbc/common/SForwardResultSet:checkIfOpen	()V
    //   26: aload_0
    //   27: invokevirtual 54	com/simba/jdbc/common/SForwardResultSet:closeCurrentStream	()V
    //   30: aload_0
    //   31: iload_1
    //   32: ldc2_w 55
    //   35: invokevirtual 57	com/simba/jdbc/common/SForwardResultSet:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   38: astore_3
    //   39: aload_0
    //   40: aload_3
    //   41: invokevirtual 59	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   44: putfield 10	com/simba/jdbc/common/SForwardResultSet:m_wasLastValueNull	Z
    //   47: aload_0
    //   48: getfield 8	com/simba/jdbc/common/SForwardResultSet:m_resultSetColumns	Ljava/util/List;
    //   51: iload_1
    //   52: iconst_1
    //   53: isub
    //   54: invokeinterface 111 2 0
    //   59: checkcast 112	com/simba/dsi/dataengine/interfaces/IColumn
    //   62: astore 4
    //   64: aload_3
    //   65: aload_2
    //   66: aload 4
    //   68: invokeinterface 113 1 0
    //   73: aload_0
    //   74: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   77: invokestatic 130	com/simba/utilities/conversion/TypeConverter:toTimestamp	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Ljava/util/Calendar;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Timestamp;
    //   80: areturn
    //   81: astore 4
    //   83: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   86: getstatic 62	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   89: aload_0
    //   90: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: getstatic 63	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   96: iconst_2
    //   97: anewarray 16	java/lang/Object
    //   100: dup
    //   101: iconst_0
    //   102: iload_1
    //   103: invokestatic 64	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   106: aastore
    //   107: dup
    //   108: iconst_1
    //   109: bipush 93
    //   111: invokestatic 65	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   114: aastore
    //   115: invokevirtual 43	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   118: athrow
    //   119: astore_3
    //   120: invokestatic 36	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   123: aload_3
    //   124: aload_0
    //   125: getfield 9	com/simba/jdbc/common/SForwardResultSet:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   128: aload_0
    //   129: getfield 2	com/simba/jdbc/common/SForwardResultSet:m_logger	Lcom/simba/support/ILogger;
    //   132: invokevirtual 37	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   135: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	136	0	this	SForwardResultSet
    //   0	136	1	paramInt	int
    //   0	136	2	paramCalendar	Calendar
    //   38	27	3	localDataWrapper	DataWrapper
    //   119	5	3	localException	Exception
    //   62	5	4	localIColumn	IColumn
    //   81	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   39	80	81	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	80	119	java/lang/Exception
    //   81	119	119	java/lang/Exception
  }
  
  public Timestamp getTimestamp(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getTimestamp(findColumn(paramString));
  }
  
  public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramCalendar });
    return getTimestamp(findColumn(paramString), paramCalendar);
  }
  
  public int getType()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return 1003;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /**
   * @deprecated
   */
  public InputStream getUnicodeStream(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      closeCurrentStream();
      DataWrapper localDataWrapper = getData(paramInt, 0L);
      if (TypeConverter.canConvertStreamFrom(localDataWrapper.getType(), -1))
      {
        this.m_wasLastValueNull = localDataWrapper.isNull();
        if (localDataWrapper.isNull()) {
          return null;
        }
        UnicodeStream localUnicodeStream = new UnicodeStream(this.m_resultSet, paramInt - 1, this.m_streamBufferSize);
        this.m_currentStream = localUnicodeStream;
        return localUnicodeStream;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), "UnicodeStream" });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /**
   * @deprecated
   */
  public InputStream getUnicodeStream(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getUnicodeStream(findColumn(paramString));
  }
  
  public URL getURL(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public URL getURL(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getURL(findColumn(paramString));
  }
  
  public SQLWarning getWarnings()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_warningListener.getSQLWarnings();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public SWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public void insertRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isAfterLast()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return CursorPosition.AFTER_LAST == this.m_cursorPosition;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isBeforeFirst()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return CursorPosition.BEFORE_FIRST == this.m_cursorPosition;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isFirst()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return (CursorPosition.AT_FIRST == this.m_cursorPosition) || (CursorPosition.AT_FIRST_AT_LAST == this.m_cursorPosition);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isLast()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    if (!this.m_canCallHasMoreRows) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    return (CursorPosition.AT_LAST == this.m_cursorPosition) || (CursorPosition.AT_FIRST_AT_LAST == this.m_cursorPosition);
  }
  
  public boolean last()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (1003 == getType()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void moveToCurrentRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void moveToInsertRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean next()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (null == this.m_resultSet) {
        return false;
      }
      boolean bool = (isBelowRowLimit()) && (this.m_resultSet.moveToNextRow());
      updateCursorPosition(bool);
      this.m_currentRow += 1;
      this.m_warningListener.clear();
      return bool;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean previous()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (1003 == getType()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void refreshRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean relative(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (getType() == 1003) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean rowDeleted()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if ((CursorPosition.BEFORE_FIRST == this.m_cursorPosition) || (CursorPosition.AFTER_LAST == this.m_cursorPosition)) {
        return false;
      }
      return this.m_resultSet.rowDeleted();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean rowInserted()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if ((CursorPosition.BEFORE_FIRST == this.m_cursorPosition) || (CursorPosition.AFTER_LAST == this.m_cursorPosition)) {
        return false;
      }
      return this.m_resultSet.rowInserted();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean rowUpdated()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if ((CursorPosition.BEFORE_FIRST == this.m_cursorPosition) || (CursorPosition.AFTER_LAST == this.m_cursorPosition)) {
        return false;
      }
      return this.m_resultSet.rowUpdated();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void setFetchDirection(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      switch (paramInt)
      {
      case 1000: 
      case 1001: 
      case 1002: 
        break;
      default: 
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_FETCH_DIRECTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
      }
      if ((1003 == getType()) && (1000 != paramInt)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FORWARDONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void setFetchSize(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      int i = null == getStatement() ? 0 : getStatement().getMaxRows();
      int j = 0 <= paramInt ? 1 : 0;
      if (0 != i) {
        j = (j != 0) && (i >= paramInt) ? 1 : 0;
      }
      if (j == 0) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FETCH_SIZE, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt), String.valueOf(0), String.valueOf(0 == i ? Double.POSITIVE_INFINITY : i) });
      }
      this.m_resultSet.setFetchSize(paramInt);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateArray(int paramInt, Array paramArray)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramArray });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateArray(String paramString, Array paramArray)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArray });
      updateArray(findColumn(paramString), paramArray);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramInputStream, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt1);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Integer.valueOf(paramInt) });
    updateAsciiStream(findColumn(paramString), paramInputStream, paramInt);
  }
  
  public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramBigDecimal });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramBigDecimal });
    updateBigDecimal(findColumn(paramString), paramBigDecimal);
  }
  
  public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt1);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Integer.valueOf(paramInt) });
    updateBinaryStream(findColumn(paramString), paramInputStream, paramInt);
  }
  
  public void updateBlob(int paramInt, Blob paramBlob)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramBlob });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBlob(String paramString, Blob paramBlob)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramBlob });
    updateBlob(findColumn(paramString), paramBlob);
  }
  
  public void updateBoolean(int paramInt, boolean paramBoolean)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBoolean(String paramString, boolean paramBoolean)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Boolean.valueOf(paramBoolean) });
    updateBoolean(findColumn(paramString), paramBoolean);
  }
  
  public void updateByte(int paramInt, byte paramByte)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Byte.valueOf(paramByte) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateByte(String paramString, byte paramByte)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Byte.valueOf(paramByte) });
    updateByte(findColumn(paramString), paramByte);
  }
  
  public void updateBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramArrayOfByte });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBytes(String paramString, byte[] paramArrayOfByte)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfByte });
    updateBytes(findColumn(paramString), paramArrayOfByte);
  }
  
  public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramReader, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt1);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Integer.valueOf(paramInt) });
    updateCharacterStream(findColumn(paramString), paramReader, paramInt);
  }
  
  public void updateClob(int paramInt, Clob paramClob)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramClob });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateClob(String paramString, Clob paramClob)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramClob });
    updateClob(findColumn(paramString), paramClob);
  }
  
  public void updateDate(int paramInt, Date paramDate)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramDate });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateDate(String paramString, Date paramDate)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramDate });
    updateDate(findColumn(paramString), paramDate);
  }
  
  public void updateDouble(int paramInt, double paramDouble)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Double.valueOf(paramDouble) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateDouble(String paramString, double paramDouble)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Double.valueOf(paramDouble) });
    updateDouble(findColumn(paramString), paramDouble);
  }
  
  public void updateFloat(int paramInt, float paramFloat)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Float.valueOf(paramFloat) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateFloat(String paramString, float paramFloat)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Float.valueOf(paramFloat) });
    updateFloat(findColumn(paramString), paramFloat);
  }
  
  public void updateInt(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt1);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateInt(String paramString, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
    updateInt(findColumn(paramString), paramInt);
  }
  
  public void updateLong(int paramInt, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateLong(String paramString, long paramLong)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Long.valueOf(paramLong) });
    updateLong(findColumn(paramString), paramLong);
  }
  
  public void updateNull(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateNull(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    updateNull(findColumn(paramString));
  }
  
  public void updateObject(int paramInt, Object paramObject)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramObject });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramObject, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt1);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateObject(String paramString, Object paramObject)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramObject });
    updateObject(findColumn(paramString), paramObject);
  }
  
  public void updateObject(String paramString, Object paramObject, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramObject, Integer.valueOf(paramInt) });
    updateObject(findColumn(paramString), paramObject, paramInt);
  }
  
  public void updateRef(int paramInt, Ref paramRef)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramRef });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateRef(String paramString, Ref paramRef)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramRef });
    updateRef(findColumn(paramString), paramRef);
  }
  
  public void updateRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateShort(int paramInt, short paramShort)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Short.valueOf(paramShort) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateShort(String paramString, short paramShort)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Short.valueOf(paramShort) });
    updateShort(findColumn(paramString), paramShort);
  }
  
  public void updateString(int paramInt, String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramString });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateString(String paramString1, String paramString2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2 });
    updateString(findColumn(paramString1), paramString2);
  }
  
  public void updateTime(int paramInt, Time paramTime)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTime });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateTime(String paramString, Time paramTime)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramTime });
    updateTime(findColumn(paramString), paramTime);
  }
  
  public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTimestamp });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateTimestamp(String paramString, Timestamp paramTimestamp)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramTimestamp });
    updateTimestamp(findColumn(paramString), paramTimestamp);
  }
  
  public boolean wasNull()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_wasLastValueNull;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getHoldability()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      return this.m_parentStatement.getResultSetHoldability();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public Reader getNCharacterStream(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public Reader getNCharacterStream(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getNCharacterStream(findColumn(paramString));
  }
  
  public NClob getNClob(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public NClob getNClob(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getNClob(findColumn(paramString));
  }
  
  public String getNString(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public String getNString(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getNString(findColumn(paramString));
  }
  
  public RowId getRowId(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public RowId getRowId(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getRowId(findColumn(paramString));
  }
  
  public SQLXML getSQLXML(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public SQLXML getSQLXML(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getSQLXML(findColumn(paramString));
  }
  
  public boolean isClosed()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    return !this.m_isOpen;
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
  
  public void updateAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramInputStream });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream });
    updateAsciiStream(findColumn(paramString), paramInputStream);
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Long.valueOf(paramLong) });
    updateAsciiStream(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramInputStream });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream });
    updateBinaryStream(findColumn(paramString), paramInputStream);
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Long.valueOf(paramLong) });
    updateBinaryStream(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateBlob(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateBlob(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramInputStream });
    updateBlob(findColumn(paramString), paramInputStream);
  }
  
  public void updateBlob(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramInputStream, Long.valueOf(paramLong) });
    updateBlob(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramReader });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader });
    updateCharacterStream(findColumn(paramString), paramReader);
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateCharacterStream(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateClob(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader });
    updateClob(findColumn(paramString), paramReader);
  }
  
  public void updateClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateClob(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader });
    updateNCharacterStream(findColumn(paramString), paramReader);
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateNCharacterStream(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNClob(int paramInt, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramNClob });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNClob(String paramString, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramNClob });
    updateNClob(findColumn(paramString), paramNClob);
  }
  
  public void updateNClob(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader });
    updateNClob(findColumn(paramString), paramReader);
  }
  
  public void updateNClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateNClob(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNString(int paramInt, String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramString });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNString(String paramString1, String paramString2)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString1, paramString2 });
    updateNString(findColumn(paramString1), paramString2);
  }
  
  public void updateRowId(int paramInt, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramRowId });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateRowId(String paramString, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramRowId });
    updateRowId(findColumn(paramString), paramRowId);
  }
  
  public void updateSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramSQLXML });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_READONLY_ACTION, getWarningListener(), ExceptionType.DEFAULT, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateSQLXML(String paramString, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramSQLXML });
    updateSQLXML(findColumn(paramString), paramSQLXML);
  }
  
  protected void initializeResultSetColumns()
    throws ErrorException
  {
    this.m_resultSetColumns = this.m_resultSet.getSelectColumns();
  }
  
  protected void setResultSetMetadata(SResultSetMetaData paramSResultSetMetaData)
  {
    this.m_resultMetaData = paramSResultSetMetaData;
  }
  
  protected void initializeColumnNameMap()
    throws SQLException
  {
    if (null == this.m_columnNameIndexes)
    {
      this.m_columnNameIndexes = new ArrayList();
      this.m_cachedDataWrappers = new ArrayList();
      this.m_numColumns = getMetaData().getColumnCount();
      for (int i = 0; i < this.m_numColumns; i++)
      {
        this.m_columnNameIndexes.add(((IColumn)this.m_resultSetColumns.get(i)).getName().toUpperCase(Locale.ENGLISH));
        this.m_cachedDataWrappers.add(new DataWrapper());
      }
    }
  }
  
  protected synchronized void checkIfOpen()
    throws SQLException
  {
    if (!this.m_isOpen) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULTSET_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
    if (null == this.m_parentStatement)
    {
      close();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARENT_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
  }
  
  protected void checkIfValidColumnNumber(int paramInt)
    throws SQLException
  {
    if ((1 > paramInt) || (this.m_numColumns < paramInt)) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
    }
  }
  
  protected void checkIfValidRowNumber()
    throws SQLException
  {
    if (0 == this.m_currentRow) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CURSOR_BEFORE_FIRST_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    if (CursorPosition.AFTER_LAST == this.m_cursorPosition) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CURSOR_AFTER_LAST_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
  }
  
  protected void closeCurrentStream()
    throws SQLException
  {
    if (null == this.m_currentStream) {
      return;
    }
    try
    {
      this.m_currentStream.close();
      this.m_currentStream = null;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  protected SArray createArrayResult(IArray paramIArray)
    throws SQLException
  {
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.FEATURE_NOT_SUPPORTED, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[] { "getArray" });
  }
  
  protected void finalize()
    throws Throwable
  {
    close();
  }
  
  protected DataWrapper getData(int paramInt, long paramLong)
    throws SQLException
  {
    checkIfValidColumnNumber(paramInt);
    checkIfValidRowNumber();
    paramInt--;
    try
    {
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt);
      this.m_resultSet.getData(paramInt, 0L, paramLong, localDataWrapper);
      return localDataWrapper;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private void updateCursorPosition(boolean paramBoolean)
    throws SQLException, ErrorException
  {
    HAS_MORE_ROWS localHAS_MORE_ROWS = null;
    if (!paramBoolean)
    {
      this.m_cursorPosition = CursorPosition.AFTER_LAST;
      return;
    }
    if (this.m_canCallHasMoreRows)
    {
      if (this.m_resultSet.hasMoreRows()) {
        localHAS_MORE_ROWS = HAS_MORE_ROWS.TRUE;
      } else {
        localHAS_MORE_ROWS = HAS_MORE_ROWS.FALSE;
      }
    }
    else {
      localHAS_MORE_ROWS = HAS_MORE_ROWS.UNKNOWN;
    }
    switch (this.m_cursorPosition)
    {
    case BEFORE_FIRST: 
      if (HAS_MORE_ROWS.FALSE != localHAS_MORE_ROWS) {
        this.m_cursorPosition = CursorPosition.AT_FIRST;
      } else {
        this.m_cursorPosition = CursorPosition.AT_FIRST_AT_LAST;
      }
      break;
    case AT_FIRST: 
      if (HAS_MORE_ROWS.TRUE == localHAS_MORE_ROWS) {
        this.m_cursorPosition = CursorPosition.DURING;
      } else {
        this.m_cursorPosition = CursorPosition.UNKNOWN;
      }
      break;
    case UNKNOWN: 
    case DURING: 
      if (HAS_MORE_ROWS.FALSE == localHAS_MORE_ROWS) {
        this.m_cursorPosition = CursorPosition.AT_LAST;
      }
      break;
    case AT_LAST: 
    case AT_FIRST_AT_LAST: 
      if (HAS_MORE_ROWS.TRUE == localHAS_MORE_ROWS) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_CURSOR_TRANSITION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      this.m_cursorPosition = CursorPosition.AFTER_LAST;
      break;
    case AFTER_LAST: 
      break;
    default: 
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_CURSOR_TRANSITION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { this.m_cursorPosition.toString() });
    }
  }
  
  protected IConnection getParentConnection()
    throws SQLException
  {
    return this.m_parentStatement.getParentConnection().getConnection();
  }
  
  protected int getStreamBufferSize()
    throws SQLException
  {
    try
    {
      IConnection localIConnection = getParentConnection();
      int i = localIConnection.getProperty(1007).getInt();
      return i > 0 ? i : 32000;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  private boolean isBelowRowLimit()
  {
    return (this.m_maxRows == 0) || (this.m_currentRow < this.m_maxRows);
  }
  
  private static enum HAS_MORE_ROWS
  {
    TRUE,  FALSE,  UNKNOWN;
    
    private HAS_MORE_ROWS() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SForwardResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */