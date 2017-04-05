package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.ExecutionContexts;
import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.ParameterType;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.streams.IStream;
import com.simba.streams.parametersoutput.CharacterParameterDataStream;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.FunctionID;
import com.simba.utilities.TypeNames;
import com.simba.utilities.conversion.TypeConverter;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class SCallableStatement
  extends SPreparedStatement
  implements CallableStatement
{
  private boolean m_wasLastParameterNull = false;
  private List<DataWrapper> m_parameterOutputValues = new ArrayList();
  protected List<Boolean> m_parameterRegistered = new ArrayList();
  protected Map<Integer, Integer> m_parameterScale = new HashMap();
  private IStream m_currentStream = null;
  
  protected SCallableStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException
  {
    super(paramString, paramIStatement, paramSConnection, paramInt);
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramIStatement, paramSConnection });
    resetRegisteredParameters();
  }
  
  public synchronized void clearParameters()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    super.clearParameters();
    if (null != this.m_parameterRegistered) {
      resetRegisteredParameters();
    }
  }
  
  public synchronized boolean execute()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (!allOutputParametersRegistered())
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NOT_OUTPUT_REGISTERED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return super.execute();
  }
  
  public synchronized int[] executeBatch()
    throws SQLException, BatchUpdateException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (this.m_parameterRegistered.contains(Boolean.TRUE)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      return super.executeBatch();
    }
    catch (SQLException localSQLException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localSQLException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized ResultSet executeQuery()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (!allOutputParametersRegistered())
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NOT_OUTPUT_REGISTERED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return super.executeQuery();
  }
  
  public Array getArray(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Array getArray(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getArray(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized BigDecimal getBigDecimal(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 40	com/simba/utilities/conversion/TypeConverter:toBigDecimal	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)Ljava/math/BigDecimal;
    //   36: astore_3
    //   37: aload_0
    //   38: getfield 9	com/simba/jdbc/common/SCallableStatement:m_parameterScale	Ljava/util/Map;
    //   41: iload_1
    //   42: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   45: invokeinterface 41 2 0
    //   50: checkcast 42	java/lang/Integer
    //   53: astore 4
    //   55: aconst_null
    //   56: aload 4
    //   58: if_acmpeq +16 -> 74
    //   61: aload_3
    //   62: aload 4
    //   64: invokevirtual 43	java/lang/Integer:intValue	()I
    //   67: getstatic 44	java/math/RoundingMode:HALF_UP	Ljava/math/RoundingMode;
    //   70: invokevirtual 45	java/math/BigDecimal:setScale	(ILjava/math/RoundingMode;)Ljava/math/BigDecimal;
    //   73: astore_3
    //   74: aload_0
    //   75: aload_2
    //   76: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   79: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   82: aload_3
    //   83: areturn
    //   84: astore_3
    //   85: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   88: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   91: aload_0
    //   92: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   95: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   98: iconst_2
    //   99: anewarray 12	java/lang/Object
    //   102: dup
    //   103: iconst_0
    //   104: iload_1
    //   105: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   108: aastore
    //   109: dup
    //   110: iconst_1
    //   111: iconst_2
    //   112: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   115: aastore
    //   116: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   119: athrow
    //   120: astore_2
    //   121: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   124: aload_2
    //   125: aload_0
    //   126: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   129: aload_0
    //   130: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   133: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   136: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	SCallableStatement
    //   0	137	1	paramInt	int
    //   27	49	2	localDataWrapper	DataWrapper
    //   120	5	2	localException	Exception
    //   36	47	3	localBigDecimal	BigDecimal
    //   84	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   53	10	4	localInteger	Integer
    // Exception table:
    //   from	to	target	type
    //   28	83	84	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	83	120	java/lang/Exception
    //   84	120	120	java/lang/Exception
  }
  
  /**
   * @deprecated
   */
  public synchronized BigDecimal getBigDecimal(int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    BigDecimal localBigDecimal = getBigDecimal(paramInt1);
    if (null != localBigDecimal) {
      return localBigDecimal.setScale(paramInt2, RoundingMode.HALF_UP);
    }
    return null;
  }
  
  public synchronized BigDecimal getBigDecimal(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBigDecimal(getParameterIndex(paramString));
  }
  
  public synchronized Blob getBlob(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public synchronized Blob getBlob(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBlob(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized boolean getBoolean(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 55	com/simba/utilities/conversion/TypeConverter:toBoolean	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)Z
    //   36: istore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: iload_3
    //   46: ireturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: bipush 16
    //   76: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   79: aastore
    //   80: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   83: athrow
    //   84: astore_2
    //   85: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   88: aload_2
    //   89: aload_0
    //   90: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: aload_0
    //   94: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   97: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   100: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	this	SCallableStatement
    //   0	101	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   84	5	2	localException	Exception
    //   36	10	3	bool	boolean
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	84	java/lang/Exception
    //   47	84	84	java/lang/Exception
  }
  
  public synchronized boolean getBoolean(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBoolean(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized byte getByte(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 57	com/simba/utilities/conversion/TypeConverter:toByte	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)B
    //   36: istore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: iload_3
    //   46: ireturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: bipush -6
    //   76: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   79: aastore
    //   80: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   83: athrow
    //   84: astore_2
    //   85: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   88: aload_2
    //   89: aload_0
    //   90: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: aload_0
    //   94: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   97: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   100: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	this	SCallableStatement
    //   0	101	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   84	5	2	localException	Exception
    //   36	10	3	b	byte
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	84	java/lang/Exception
    //   47	84	84	java/lang/Exception
  }
  
  public synchronized byte getByte(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getByte(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized byte[] getBytes(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: invokestatic 59	com/simba/utilities/conversion/TypeConverter:toBytes	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;)[B
    //   32: astore_3
    //   33: aload_0
    //   34: aload_2
    //   35: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   38: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   41: aload_3
    //   42: areturn
    //   43: astore_3
    //   44: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   47: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   50: aload_0
    //   51: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   54: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   57: iconst_2
    //   58: anewarray 12	java/lang/Object
    //   61: dup
    //   62: iconst_0
    //   63: iload_1
    //   64: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   67: aastore
    //   68: dup
    //   69: iconst_1
    //   70: bipush -2
    //   72: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   75: aastore
    //   76: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   79: athrow
    //   80: astore_2
    //   81: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   84: aload_2
    //   85: aload_0
    //   86: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   89: aload_0
    //   90: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   93: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   96: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	97	0	this	SCallableStatement
    //   0	97	1	paramInt	int
    //   27	8	2	localDataWrapper	DataWrapper
    //   80	5	2	localException	Exception
    //   32	10	3	arrayOfByte	byte[]
    //   43	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	42	43	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	42	80	java/lang/Exception
    //   43	80	80	java/lang/Exception
  }
  
  public synchronized byte[] getBytes(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getBytes(getParameterIndex(paramString));
  }
  
  public synchronized Clob getClob(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public synchronized Clob getClob(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getClob(getParameterIndex(paramString));
  }
  
  public synchronized Date getDate(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    return getDate(paramInt, null);
  }
  
  /* Error */
  public synchronized Date getDate(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_3
    //   28: aload_3
    //   29: aload_2
    //   30: aload_0
    //   31: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   34: invokestatic 63	com/simba/utilities/conversion/TypeConverter:toDate	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Ljava/util/Calendar;Lcom/simba/support/IWarningListener;)Ljava/sql/Date;
    //   37: astore 4
    //   39: aload_0
    //   40: aload_3
    //   41: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   44: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   47: aload 4
    //   49: areturn
    //   50: astore 4
    //   52: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   55: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   58: aload_0
    //   59: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   62: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   65: iconst_2
    //   66: anewarray 12	java/lang/Object
    //   69: dup
    //   70: iconst_0
    //   71: iload_1
    //   72: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   75: aastore
    //   76: dup
    //   77: iconst_1
    //   78: bipush 91
    //   80: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   83: aastore
    //   84: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   87: athrow
    //   88: astore_3
    //   89: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   92: aload_3
    //   93: aload_0
    //   94: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   97: aload_0
    //   98: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   101: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   104: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	SCallableStatement
    //   0	105	1	paramInt	int
    //   0	105	2	paramCalendar	Calendar
    //   27	14	3	localDataWrapper	DataWrapper
    //   88	5	3	localException	Exception
    //   37	11	4	localDate	Date
    //   50	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	49	50	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	49	88	java/lang/Exception
    //   50	88	88	java/lang/Exception
  }
  
  public synchronized Date getDate(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getDate(getParameterIndex(paramString));
  }
  
  public synchronized Date getDate(String paramString, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramCalendar });
    return getDate(getParameterIndex(paramString), paramCalendar);
  }
  
  /* Error */
  public synchronized double getDouble(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 65	com/simba/utilities/conversion/TypeConverter:toDouble	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)D
    //   36: dstore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: dload_3
    //   46: dreturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: bipush 8
    //   76: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   79: aastore
    //   80: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   83: athrow
    //   84: astore_2
    //   85: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   88: aload_2
    //   89: aload_0
    //   90: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: aload_0
    //   94: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   97: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   100: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	this	SCallableStatement
    //   0	101	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   84	5	2	localException	Exception
    //   36	10	3	d	double
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	84	java/lang/Exception
    //   47	84	84	java/lang/Exception
  }
  
  public synchronized double getDouble(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getDouble(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized float getFloat(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 67	com/simba/utilities/conversion/TypeConverter:toFloat	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)F
    //   36: fstore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: fload_3
    //   46: freturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: bipush 7
    //   76: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   79: aastore
    //   80: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   83: athrow
    //   84: astore_2
    //   85: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   88: aload_2
    //   89: aload_0
    //   90: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: aload_0
    //   94: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   97: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   100: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	this	SCallableStatement
    //   0	101	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   84	5	2	localException	Exception
    //   36	10	3	f	float
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	84	java/lang/Exception
    //   47	84	84	java/lang/Exception
  }
  
  public synchronized float getFloat(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getFloat(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized int getInt(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 69	com/simba/utilities/conversion/TypeConverter:toInt	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)I
    //   36: istore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: iload_3
    //   46: ireturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: iconst_4
    //   75: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   78: aastore
    //   79: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   82: athrow
    //   83: astore_2
    //   84: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   87: aload_2
    //   88: aload_0
    //   89: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   92: aload_0
    //   93: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   96: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   99: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	100	0	this	SCallableStatement
    //   0	100	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   83	5	2	localException	Exception
    //   36	10	3	i	int
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	83	java/lang/Exception
    //   47	83	83	java/lang/Exception
  }
  
  public synchronized int getInt(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getInt(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized long getLong(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 71	com/simba/utilities/conversion/TypeConverter:toLong	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)J
    //   36: lstore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: lload_3
    //   46: lreturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: bipush -5
    //   76: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   79: aastore
    //   80: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   83: athrow
    //   84: astore_2
    //   85: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   88: aload_2
    //   89: aload_0
    //   90: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   93: aload_0
    //   94: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   97: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   100: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	this	SCallableStatement
    //   0	101	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   84	5	2	localException	Exception
    //   36	10	3	l	long
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	84	java/lang/Exception
    //   47	84	84	java/lang/Exception
  }
  
  public synchronized long getLong(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getLong(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized Object getObject(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_0
    //   29: getfield 73	com/simba/jdbc/common/SCallableStatement:m_parameterMetadata	Ljava/util/ArrayList;
    //   32: iload_1
    //   33: iconst_1
    //   34: isub
    //   35: invokevirtual 74	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   38: checkcast 75	com/simba/dsi/dataengine/utilities/ParameterMetadata
    //   41: astore_3
    //   42: aload_2
    //   43: aload_3
    //   44: invokevirtual 76	com/simba/dsi/dataengine/utilities/ParameterMetadata:getTypeMetadata	()Lcom/simba/dsi/dataengine/utilities/TypeMetadata;
    //   47: aload_0
    //   48: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   51: invokestatic 77	com/simba/utilities/conversion/TypeConverter:toObject	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/lang/Object;
    //   54: astore 4
    //   56: aload_0
    //   57: aload_2
    //   58: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   61: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   64: aload 4
    //   66: areturn
    //   67: astore_3
    //   68: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   71: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   74: aload_0
    //   75: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   78: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   81: iconst_2
    //   82: anewarray 12	java/lang/Object
    //   85: dup
    //   86: iconst_0
    //   87: iload_1
    //   88: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   91: aastore
    //   92: dup
    //   93: iconst_1
    //   94: sipush 2000
    //   97: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   100: aastore
    //   101: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   104: athrow
    //   105: astore_2
    //   106: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   109: aload_2
    //   110: aload_0
    //   111: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   114: aload_0
    //   115: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   118: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   121: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	122	0	this	SCallableStatement
    //   0	122	1	paramInt	int
    //   27	31	2	localDataWrapper	DataWrapper
    //   105	5	2	localException	Exception
    //   41	3	3	localParameterMetadata	ParameterMetadata
    //   67	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   54	11	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   28	66	67	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	66	105	java/lang/Exception
    //   67	105	105	java/lang/Exception
  }
  
  public synchronized Object getObject(int paramInt, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramMap });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public synchronized Object getObject(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getObject(getParameterIndex(paramString));
  }
  
  public synchronized Object getObject(String paramString, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramMap });
    return getObject(getParameterIndex(paramString), paramMap);
  }
  
  public synchronized Ref getRef(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public synchronized Ref getRef(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getRef(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized short getShort(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_0
    //   30: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   33: invokestatic 81	com/simba/utilities/conversion/TypeConverter:toShort	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/support/IWarningListener;)S
    //   36: istore_3
    //   37: aload_0
    //   38: aload_2
    //   39: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   42: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   45: iload_3
    //   46: ireturn
    //   47: astore_3
    //   48: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   51: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   54: aload_0
    //   55: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   58: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   61: iconst_2
    //   62: anewarray 12	java/lang/Object
    //   65: dup
    //   66: iconst_0
    //   67: iload_1
    //   68: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: iconst_5
    //   75: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   78: aastore
    //   79: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   82: athrow
    //   83: astore_2
    //   84: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   87: aload_2
    //   88: aload_0
    //   89: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   92: aload_0
    //   93: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   96: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   99: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	100	0	this	SCallableStatement
    //   0	100	1	paramInt	int
    //   27	12	2	localDataWrapper	DataWrapper
    //   83	5	2	localException	Exception
    //   36	10	3	s	short
    //   47	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	46	47	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	46	83	java/lang/Exception
    //   47	83	83	java/lang/Exception
  }
  
  public synchronized short getShort(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getShort(getParameterIndex(paramString));
  }
  
  /* Error */
  public synchronized String getString(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_2
    //   28: aload_0
    //   29: getfield 73	com/simba/jdbc/common/SCallableStatement:m_parameterMetadata	Ljava/util/ArrayList;
    //   32: iload_1
    //   33: iconst_1
    //   34: isub
    //   35: invokevirtual 74	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   38: checkcast 75	com/simba/dsi/dataengine/utilities/ParameterMetadata
    //   41: astore_3
    //   42: aload_2
    //   43: aload_3
    //   44: invokevirtual 76	com/simba/dsi/dataengine/utilities/ParameterMetadata:getTypeMetadata	()Lcom/simba/dsi/dataengine/utilities/TypeMetadata;
    //   47: invokestatic 83	com/simba/utilities/conversion/TypeConverter:toString	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;)Ljava/lang/String;
    //   50: astore 4
    //   52: aload_0
    //   53: aload_2
    //   54: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   57: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   60: aload 4
    //   62: areturn
    //   63: astore_3
    //   64: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   67: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   70: aload_0
    //   71: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   74: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   77: iconst_2
    //   78: anewarray 12	java/lang/Object
    //   81: dup
    //   82: iconst_0
    //   83: iload_1
    //   84: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   87: aastore
    //   88: dup
    //   89: iconst_1
    //   90: bipush 12
    //   92: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   95: aastore
    //   96: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   99: athrow
    //   100: astore_2
    //   101: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   104: aload_2
    //   105: aload_0
    //   106: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   109: aload_0
    //   110: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   113: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   116: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	117	0	this	SCallableStatement
    //   0	117	1	paramInt	int
    //   27	27	2	localDataWrapper	DataWrapper
    //   100	5	2	localException	Exception
    //   41	3	3	localParameterMetadata	ParameterMetadata
    //   63	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   50	11	4	str	String
    // Exception table:
    //   from	to	target	type
    //   28	62	63	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	62	100	java/lang/Exception
    //   63	100	100	java/lang/Exception
  }
  
  public synchronized String getString(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getString(getParameterIndex(paramString));
  }
  
  public synchronized Time getTime(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    return getTime(paramInt, null);
  }
  
  /* Error */
  public synchronized Time getTime(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_3
    //   28: aload_3
    //   29: aload_2
    //   30: aload_0
    //   31: getfield 73	com/simba/jdbc/common/SCallableStatement:m_parameterMetadata	Ljava/util/ArrayList;
    //   34: iload_1
    //   35: iconst_1
    //   36: isub
    //   37: invokevirtual 74	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   40: checkcast 75	com/simba/dsi/dataengine/utilities/ParameterMetadata
    //   43: invokevirtual 76	com/simba/dsi/dataengine/utilities/ParameterMetadata:getTypeMetadata	()Lcom/simba/dsi/dataengine/utilities/TypeMetadata;
    //   46: aload_0
    //   47: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   50: invokestatic 86	com/simba/utilities/conversion/TypeConverter:toTime	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Ljava/util/Calendar;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Time;
    //   53: astore 4
    //   55: aload_0
    //   56: aload_3
    //   57: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   60: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   63: aload 4
    //   65: areturn
    //   66: astore 4
    //   68: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   71: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   74: aload_0
    //   75: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   78: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   81: iconst_2
    //   82: anewarray 12	java/lang/Object
    //   85: dup
    //   86: iconst_0
    //   87: iload_1
    //   88: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   91: aastore
    //   92: dup
    //   93: iconst_1
    //   94: bipush 92
    //   96: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   99: aastore
    //   100: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   103: athrow
    //   104: astore_3
    //   105: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   108: aload_3
    //   109: aload_0
    //   110: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   113: aload_0
    //   114: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   117: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	SCallableStatement
    //   0	121	1	paramInt	int
    //   0	121	2	paramCalendar	Calendar
    //   27	30	3	localDataWrapper	DataWrapper
    //   104	5	3	localException	Exception
    //   53	11	4	localTime	Time
    //   66	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	65	66	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	65	104	java/lang/Exception
    //   66	104	104	java/lang/Exception
  }
  
  public synchronized Time getTime(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getTime(getParameterIndex(paramString));
  }
  
  public synchronized Time getTime(String paramString, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramCalendar });
    return getTime(getParameterIndex(paramString), paramCalendar);
  }
  
  public synchronized Timestamp getTimestamp(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    return getTimestamp(paramInt, null);
  }
  
  /* Error */
  public synchronized Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 12	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 33	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 13	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 24	com/simba/jdbc/common/SCallableStatement:checkIfOpen	()V
    //   22: aload_0
    //   23: iload_1
    //   24: invokevirtual 39	com/simba/jdbc/common/SCallableStatement:getOutputParameterValue	(I)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   27: astore_3
    //   28: aload_3
    //   29: aload_2
    //   30: aload_0
    //   31: getfield 73	com/simba/jdbc/common/SCallableStatement:m_parameterMetadata	Ljava/util/ArrayList;
    //   34: iload_1
    //   35: iconst_1
    //   36: isub
    //   37: invokevirtual 74	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   40: checkcast 75	com/simba/dsi/dataengine/utilities/ParameterMetadata
    //   43: invokevirtual 76	com/simba/dsi/dataengine/utilities/ParameterMetadata:getTypeMetadata	()Lcom/simba/dsi/dataengine/utilities/TypeMetadata;
    //   46: aload_0
    //   47: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   50: invokestatic 89	com/simba/utilities/conversion/TypeConverter:toTimestamp	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Ljava/util/Calendar;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Timestamp;
    //   53: astore 4
    //   55: aload_0
    //   56: aload_3
    //   57: invokevirtual 46	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   60: putfield 2	com/simba/jdbc/common/SCallableStatement:m_wasLastParameterNull	Z
    //   63: aload 4
    //   65: areturn
    //   66: astore 4
    //   68: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   71: getstatic 48	com/simba/exceptions/JDBCMessageKey:INVALID_PARAM_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   74: aload_0
    //   75: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   78: getstatic 49	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   81: iconst_2
    //   82: anewarray 12	java/lang/Object
    //   85: dup
    //   86: iconst_0
    //   87: iload_1
    //   88: invokestatic 50	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   91: aastore
    //   92: dup
    //   93: iconst_1
    //   94: bipush 93
    //   96: invokestatic 51	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   99: aastore
    //   100: invokevirtual 21	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   103: athrow
    //   104: astore_3
    //   105: invokestatic 17	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   108: aload_3
    //   109: aload_0
    //   110: getfield 19	com/simba/jdbc/common/SCallableStatement:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   113: aload_0
    //   114: getfield 11	com/simba/jdbc/common/SCallableStatement:m_logger	Lcom/simba/support/ILogger;
    //   117: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	SCallableStatement
    //   0	121	1	paramInt	int
    //   0	121	2	paramCalendar	Calendar
    //   27	30	3	localDataWrapper	DataWrapper
    //   104	5	3	localException	Exception
    //   53	11	4	localTimestamp	Timestamp
    //   66	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   28	65	66	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	65	104	java/lang/Exception
    //   66	104	104	java/lang/Exception
  }
  
  public synchronized Timestamp getTimestamp(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getTimestamp(getParameterIndex(paramString));
  }
  
  public synchronized Timestamp getTimestamp(String paramString, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramCalendar });
    return getTimestamp(getParameterIndex(paramString), paramCalendar);
  }
  
  public synchronized URL getURL(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public synchronized URL getURL(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getURL(getParameterIndex(paramString));
  }
  
  public Reader getCharacterStream(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      closeCurrentStream();
      DataWrapper localDataWrapper = getOutputParameterValue(paramInt);
      if (TypeConverter.canConvertStreamFrom(localDataWrapper.getType(), -1))
      {
        if (localDataWrapper.isNull()) {
          return null;
        }
        CharacterParameterDataStream localCharacterParameterDataStream = new CharacterParameterDataStream(localDataWrapper.getLongVarChar().getBytes("UTF-16"));
        this.m_currentStream = localCharacterParameterDataStream;
        return localCharacterParameterDataStream;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), "CharacterStream" });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Reader getCharacterStream(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getCharacterStream(getParameterIndex(paramString));
  }
  
  public Reader getNCharacterStream(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Reader getNCharacterStream(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getNCharacterStream(getParameterIndex(paramString));
  }
  
  public NClob getNClob(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public NClob getNClob(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getNClob(getParameterIndex(paramString));
  }
  
  public String getNString(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public String getNString(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getNString(getParameterIndex(paramString));
  }
  
  public RowId getRowId(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public RowId getRowId(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getRowId(getParameterIndex(paramString));
  }
  
  public SQLXML getSQLXML(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public SQLXML getSQLXML(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return getSQLXML(getParameterIndex(paramString));
  }
  
  public synchronized void registerOutParameter(int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    checkIfOpen();
    checkValidParameterIndex(paramInt1);
    registerOutParameter(paramInt1, paramInt2, 0);
    if (TypeUtilities.isExactNumericType(paramInt2)) {
      this.m_parameterScale.remove(Integer.valueOf(paramInt1));
    }
  }
  
  public synchronized void registerOutParameter(int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      checkTypeSupported(paramInt2);
      if (0 > paramInt3) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_SCALE, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt3), "0" });
      }
      if (ParameterType.INPUT == this.m_parameterTypes.get(paramInt1 - 1)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_REGISTER_TYPE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt1) });
      }
      ParameterMetadata localParameterMetadata = (ParameterMetadata)this.m_parameterMetadata.get(paramInt1 - 1);
      int i = localParameterMetadata.getTypeMetadata().getType();
      if (!TypeConverter.canConvert(i, paramInt2)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt1), TypeNames.getTypeName(paramInt2) });
      }
      if (TypeUtilities.isExactNumericType(paramInt2)) {
        this.m_parameterScale.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt3));
      }
      int j = getOutputParameterOnlyIndex(paramInt1);
      this.m_parameterRegistered.set(j - 1, Boolean.TRUE);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void registerOutParameter(int paramInt1, int paramInt2, String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramString });
    checkIfOpen();
    checkValidParameterIndex(paramInt1);
    registerOutParameter(paramInt1, paramInt2, 0);
    if (TypeUtilities.isExactNumericType(paramInt2)) {
      this.m_parameterScale.remove(Integer.valueOf(paramInt1));
    }
  }
  
  public synchronized void registerOutParameter(String paramString, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
    registerOutParameter(getParameterIndex(paramString), paramInt);
  }
  
  public synchronized void registerOutParameter(String paramString, int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    registerOutParameter(getParameterIndex(paramString), paramInt1, paramInt2);
  }
  
  public synchronized void registerOutParameter(String paramString1, int paramInt, String paramString2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, Integer.valueOf(paramInt), paramString2 });
    registerOutParameter(getParameterIndex(paramString1), paramInt, paramString2);
  }
  
  public synchronized void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Integer.valueOf(paramInt) });
    setAsciiStream(getParameterIndex(paramString), paramInputStream, paramInt);
  }
  
  public synchronized void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramBigDecimal });
    setBigDecimal(getParameterIndex(paramString), paramBigDecimal);
  }
  
  public synchronized void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Integer.valueOf(paramInt) });
    setBinaryStream(getParameterIndex(paramString), paramInputStream, paramInt);
  }
  
  public synchronized void setBoolean(String paramString, boolean paramBoolean)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Boolean.valueOf(paramBoolean) });
    setBoolean(getParameterIndex(paramString), paramBoolean);
  }
  
  public synchronized void setByte(String paramString, byte paramByte)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Byte.valueOf(paramByte) });
    setByte(getParameterIndex(paramString), paramByte);
  }
  
  public synchronized void setBytes(String paramString, byte[] paramArrayOfByte)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfByte });
    setBytes(getParameterIndex(paramString), paramArrayOfByte);
  }
  
  public synchronized void setCharacterStream(String paramString, Reader paramReader, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Integer.valueOf(paramInt) });
    setCharacterStream(getParameterIndex(paramString), paramReader, paramInt);
  }
  
  public synchronized void setDate(String paramString, Date paramDate)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramDate });
    setDate(getParameterIndex(paramString), paramDate);
  }
  
  public synchronized void setDate(String paramString, Date paramDate, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramDate, paramCalendar });
    setDate(getParameterIndex(paramString), paramDate, paramCalendar);
  }
  
  public synchronized void setDouble(String paramString, double paramDouble)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Double.valueOf(paramDouble) });
    setDouble(getParameterIndex(paramString), paramDouble);
  }
  
  public synchronized void setFloat(String paramString, float paramFloat)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Float.valueOf(paramFloat) });
    setFloat(getParameterIndex(paramString), paramFloat);
  }
  
  public synchronized void setInt(String paramString, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
    setInt(getParameterIndex(paramString), paramInt);
  }
  
  public synchronized void setLong(String paramString, long paramLong)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Long.valueOf(paramLong) });
    setLong(getParameterIndex(paramString), paramLong);
  }
  
  public synchronized void setNull(String paramString, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
    setNull(getParameterIndex(paramString), paramInt);
  }
  
  public synchronized void setNull(String paramString1, int paramInt, String paramString2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, Integer.valueOf(paramInt), paramString2 });
    setNull(getParameterIndex(paramString1), paramInt, paramString2);
  }
  
  public synchronized void setObject(String paramString, Object paramObject)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramObject });
    setObject(getParameterIndex(paramString), paramObject);
  }
  
  public synchronized void setObject(String paramString, Object paramObject, int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramObject, Integer.valueOf(paramInt) });
    setObject(getParameterIndex(paramString), paramObject, paramInt);
  }
  
  public synchronized void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramObject, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    setObject(getParameterIndex(paramString), paramObject, paramInt1, paramInt2);
  }
  
  public synchronized void setShort(String paramString, short paramShort)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Short.valueOf(paramShort) });
    setShort(getParameterIndex(paramString), paramShort);
  }
  
  public synchronized void setString(String paramString1, String paramString2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2 });
    setString(getParameterIndex(paramString1), paramString2);
  }
  
  public synchronized void setTime(String paramString, Time paramTime)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramTime });
    setTime(getParameterIndex(paramString), paramTime);
  }
  
  public synchronized void setTime(String paramString, Time paramTime, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramTime, paramCalendar });
    setTime(getParameterIndex(paramString), paramTime, paramCalendar);
  }
  
  public synchronized void setTimestamp(String paramString, Timestamp paramTimestamp)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramTimestamp });
    setTimestamp(getParameterIndex(paramString), paramTimestamp);
  }
  
  public synchronized void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramTimestamp, paramCalendar });
    setTimestamp(getParameterIndex(paramString), paramTimestamp, paramCalendar);
  }
  
  public synchronized void setURL(String paramString, URL paramURL)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramURL });
    setURL(getParameterIndex(paramString), paramURL);
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    setAsciiStream(getParameterIndex(paramString), paramInputStream);
  }
  
  public void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    setAsciiStream(getParameterIndex(paramString), paramInputStream, paramLong);
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream });
    setBinaryStream(getParameterIndex(paramString), paramInputStream);
  }
  
  public void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream });
    setBinaryStream(getParameterIndex(paramString), paramInputStream, paramLong);
  }
  
  public void setBlob(String paramString, Blob paramBlob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramBlob });
    setBlob(getParameterIndex(paramString), paramBlob);
  }
  
  public void setBlob(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream });
    setBlob(getParameterIndex(paramString), paramInputStream);
  }
  
  public void setBlob(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Long.valueOf(paramLong) });
    setBlob(getParameterIndex(paramString), paramInputStream, paramLong);
  }
  
  public void setCharacterStream(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader });
    setCharacterStream(getParameterIndex(paramString), paramReader);
  }
  
  public void setCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    setCharacterStream(getParameterIndex(paramString), paramReader, paramLong);
  }
  
  public void setClob(String paramString, Clob paramClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramClob });
    checkIfOpen();
    getParameterIndex(paramString);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public void setClob(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader });
    setClob(getParameterIndex(paramString), paramReader);
  }
  
  public void setClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    setClob(getParameterIndex(paramString), paramReader, paramLong);
  }
  
  public void setNCharacterStream(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader });
    setNCharacterStream(getParameterIndex(paramString), paramReader);
  }
  
  public void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    setNCharacterStream(getParameterIndex(paramString), paramReader, paramLong);
  }
  
  public void setNClob(String paramString, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramNClob });
    setNClob(getParameterIndex(paramString), paramNClob);
  }
  
  public void setNClob(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader });
    setNClob(getParameterIndex(paramString), paramReader);
  }
  
  public void setNClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    setNClob(getParameterIndex(paramString), paramReader, paramLong);
  }
  
  public void setNString(String paramString1, String paramString2)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2 });
    setNString(getParameterIndex(paramString1), paramString2);
  }
  
  public void setRowId(String paramString, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramRowId });
    setRowId(getParameterIndex(paramString), paramRowId);
  }
  
  public void setSQLXML(String paramString, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramSQLXML });
    setSQLXML(getParameterIndex(paramString), paramSQLXML);
  }
  
  public boolean wasNull()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    return this.m_wasLastParameterNull;
  }
  
  protected boolean allInputParameterValuesPopulated()
  {
    int i = this.m_parameterInputStreamData.size() + this.m_parameterInputValues.size();
    int j = 0;
    Iterator localIterator = this.m_parameterMetadata.iterator();
    while (localIterator.hasNext())
    {
      ParameterMetadata localParameterMetadata = (ParameterMetadata)localIterator.next();
      if ((ParameterType.OUTPUT == localParameterMetadata.getParameterType()) || (ParameterType.RETURN_VALUE == localParameterMetadata.getParameterType())) {
        j++;
      }
    }
    return i + j == this.m_parameterMetadata.size();
  }
  
  protected void checkParameters(List<ParameterMetadata> paramList)
    throws SQLException
  {}
  
  protected ExecutionResults executeWithParams(ArrayList<ParameterMetadata> paramArrayList, ArrayList<ArrayList<ParameterInputValue>> paramArrayList1, SStatement.ThrowCondition paramThrowCondition)
    throws SQLException
  {
    try
    {
      synchronized (this.m_cancelLock)
      {
        this.m_isInCancelableFunction = true;
      }
      checkCondition(this.m_preparedSql, paramThrowCondition);
      ??? = new ExecutionContexts(paramArrayList, paramArrayList1);
      synchronized (this.m_cancelLock)
      {
        if (this.m_isCanceled) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.OPERATION_CANCELED, this.m_warningListener, ExceptionType.TRANSIENT, new Object[0]);
        }
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      this.m_queryExecutor.execute((ExecutionContexts)???, this.m_warningListener);
      ??? = ((ExecutionContexts)???).outputIterator();
      if (((Iterator)???).hasNext()) {
        this.m_parameterOutputValues = ((List)((Iterator)???).next());
      }
      ExecutionResults localExecutionResults = this.m_queryExecutor.getResults();
      return localExecutionResults;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
    finally
    {
      synchronized (this.m_cancelLock)
      {
        this.m_isInCancelableFunction = false;
        this.m_isCanceled = false;
      }
    }
  }
  
  protected int getOutputParameterOnlyIndex(int paramInt)
    throws SQLException
  {
    checkValidParameterIndex(paramInt);
    if (ParameterType.INPUT == this.m_parameterTypes.get(paramInt - 1)) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_RETRIEVE_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
    }
    int i = 0;
    for (int j = 0; j < paramInt; j++) {
      if (ParameterType.INPUT != this.m_parameterTypes.get(j)) {
        i++;
      }
    }
    if (0 == i) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_OUTPUT_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
    }
    return i;
  }
  
  protected DataWrapper getOutputParameterValue(int paramInt)
    throws SQLException
  {
    int i = getOutputParameterOnlyIndex(paramInt);
    return (DataWrapper)this.m_parameterOutputValues.get(i - 1);
  }
  
  protected int getParameterIndex(String paramString)
    throws SQLException
  {
    for (int i = 0; i < this.m_parameterMetadata.size(); i++)
    {
      ParameterMetadata localParameterMetadata = (ParameterMetadata)this.m_parameterMetadata.get(i);
      if (localParameterMetadata.getName().equals(paramString)) {
        return i + 1;
      }
    }
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_NAME, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { paramString });
  }
  
  private boolean allOutputParametersRegistered()
  {
    return !this.m_parameterRegistered.contains(Boolean.FALSE);
  }
  
  private void resetRegisteredParameters()
  {
    this.m_parameterRegistered.clear();
    for (int i = 0; i < this.m_numParameters; i++) {
      if (ParameterType.INPUT != this.m_parameterTypes.get(i)) {
        this.m_parameterRegistered.add(Boolean.FALSE);
      }
    }
    this.m_parameterScale.clear();
  }
  
  private void closeCurrentStream()
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
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SCallableStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */