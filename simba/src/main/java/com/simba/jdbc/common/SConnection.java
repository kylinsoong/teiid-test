package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.impl.DSILogger;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.PropertyUtilities;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Warning;
import com.simba.support.exceptions.ClientInfoException;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.FailedPropertiesReason;
import com.simba.support.exceptions.GeneralException;
import com.simba.utilities.FunctionID;
import java.lang.ref.WeakReference;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class SConnection
  implements Connection
{
  protected int m_maxVarcharSize = 256;
  protected int m_maxVarbinarySize = 256;
  protected ILogger m_logger = null;
  private volatile boolean m_isClosed = false;
  protected final Object m_closingLock = new Object();
  private String m_URL = "";
  private List<WeakReference<SStatement>> m_stmtReferences = new ArrayList();
  private SDatabaseMetaData m_databaseMetaData = null;
  private final Object m_databaseMetadataLock = new Object();
  protected IConnection m_connection = null;
  protected SWarningListener m_warningListener = null;
  private boolean m_isInTransaction = false;
  private Stack<SSavepoint> m_savepoints = new Stack();
  private int m_savepointId = 1;
  private boolean m_allowTransactionCallbacks = true;
  protected final Object m_transactionLock = new Object();
  private boolean m_areMaxSizesAreSet = false;
  
  protected SConnection(IConnection paramIConnection, String paramString)
    throws SQLException
  {
    try
    {
      this.m_logger = paramIConnection.getConnectionLog();
      if (null == this.m_logger) {
        this.m_logger = new DSILogger("SimbaJDBC_Connection.log");
      }
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramIConnection, paramString });
      this.m_URL = paramString;
      this.m_connection = paramIConnection;
      this.m_connection.registerTransactionStateListener(new STransactionStateListener(this));
      this.m_warningListener = new SWarningListener(this.m_connection.getMessageSource(), FunctionID.CONNECTION_UPDATE_SETTINGS);
      this.m_warningListener.setLocale(paramIConnection.getLocale());
      Iterator localIterator = this.m_connection.getWarningListener().getWarnings().iterator();
      while (localIterator.hasNext())
      {
        Warning localWarning = (Warning)localIterator.next();
        this.m_warningListener.postWarning(localWarning);
      }
      this.m_connection.registerWarningListener(this.m_warningListener);
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
  
  public void close()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    synchronized (this.m_closingLock)
    {
      if (!this.m_isClosed)
      {
        abortInternal();
        this.m_isClosed = true;
      }
    }
  }
  
  public void commit()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (getAutoCommit())
        {
          if (isDataSourceReadOnly())
          {
            this.m_allowTransactionCallbacks = true;
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.COMMIT_AUTOCOMMIT, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (!this.m_isInTransaction)
        {
          this.m_allowTransactionCallbacks = true;
          return;
        }
        this.m_allowTransactionCallbacks = false;
        this.m_savepoints.clear();
        handleTransactionBehavior(37);
        this.m_connection.commit();
        this.m_isInTransaction = false;
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
      finally
      {
        this.m_allowTransactionCallbacks = true;
      }
    }
  }
  
  public Statement createStatement()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      IStatement localIStatement = this.m_connection.createStatement();
      SStatement localSStatement = JDBCObjectFactory.getInstance().createStatement(localIStatement, this, 1007);
      synchronized (this.m_stmtReferences)
      {
        this.m_stmtReferences.add(new WeakReference(localSStatement));
      }
      return localSStatement;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Statement createStatement(int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    checkIfOpen();
    Object localObject1;
    if ((1003 != paramInt1) || (getConcurrency() < paramInt2) || ((paramInt2 != 1007) && (paramInt2 != 1008)))
    {
      localObject1 = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
      LogUtilities.logError((Exception)localObject1, this.m_logger);
      throw ((Throwable)localObject1);
    }
    try
    {
      localObject1 = this.m_connection.createStatement();
      SStatement localSStatement = JDBCObjectFactory.getInstance().createStatement((IStatement)localObject1, this, paramInt2);
      synchronized (this.m_stmtReferences)
      {
        this.m_stmtReferences.add(new WeakReference(localSStatement));
      }
      return localSStatement;
    }
    catch (ErrorException localErrorException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localErrorException, this.m_warningListener);
    }
  }
  
  public Statement createStatement(int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
    checkIfOpen();
    if (getHoldability() == paramInt3) {
      return createStatement(paramInt1, paramInt2);
    }
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Object getAttribute(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (!this.m_connection.isCustomProperty(paramInt)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ATTRIBUTE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      }
      Variant localVariant = this.m_connection.getCustomProperty(paramInt);
      switch (localVariant.getType())
      {
      case 5: 
        return Short.valueOf(localVariant.getShort());
      case 2: 
        return Character.valueOf(localVariant.getChar());
      case 6: 
        return Integer.valueOf(localVariant.getInt());
      case 3: 
      case 7: 
        return Long.valueOf(localVariant.getLong());
      case 4: 
        return localVariant.getBigInteger();
      }
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean getAutoCommit()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      Variant localVariant = this.m_connection.getProperty(19);
      return 1L == localVariant.getLong();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getCatalog()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (!PropertyUtilities.hasCatalogSupport(this.m_connection)) {
        return null;
      }
      Variant localVariant = this.m_connection.getProperty(22);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public IConnection getDSIConnection()
  {
    return this.m_connection;
  }
  
  public IConnection getConnection()
  {
    return this.m_connection;
  }
  
  public int getHoldability()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      long l = -1L;
      Variant localVariant = this.m_connection.getProperty(37);
      l = localVariant.getLong();
      if ((1L == l) || (0L == l)) {
        return 2;
      }
      if (2L == l) {
        return 1;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_HOLDABILITY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(l) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public DatabaseMetaData getMetaData()
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 4	com/simba/jdbc/common/SConnection:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_0
    //   5: anewarray 6	java/lang/Object
    //   8: invokestatic 29	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   11: aload_0
    //   12: invokevirtual 50	com/simba/jdbc/common/SConnection:checkIfOpen	()V
    //   15: aload_0
    //   16: getfield 14	com/simba/jdbc/common/SConnection:m_databaseMetadataLock	Ljava/lang/Object;
    //   19: dup
    //   20: astore_1
    //   21: monitorenter
    //   22: aconst_null
    //   23: aload_0
    //   24: getfield 13	com/simba/jdbc/common/SConnection:m_databaseMetaData	Lcom/simba/jdbc/common/SDatabaseMetaData;
    //   27: if_acmpne +18 -> 45
    //   30: aload_0
    //   31: invokestatic 62	com/simba/jdbc/common/JDBCObjectFactory:getInstance	()Lcom/simba/jdbc/common/JDBCObjectFactory;
    //   34: aload_0
    //   35: aload_0
    //   36: getfield 4	com/simba/jdbc/common/SConnection:m_logger	Lcom/simba/support/ILogger;
    //   39: invokevirtual 99	com/simba/jdbc/common/JDBCObjectFactory:createDatabaseMetaData	(Lcom/simba/jdbc/common/SConnection;Lcom/simba/support/ILogger;)Lcom/simba/jdbc/common/SDatabaseMetaData;
    //   42: putfield 13	com/simba/jdbc/common/SConnection:m_databaseMetaData	Lcom/simba/jdbc/common/SDatabaseMetaData;
    //   45: aload_0
    //   46: getfield 13	com/simba/jdbc/common/SConnection:m_databaseMetaData	Lcom/simba/jdbc/common/SDatabaseMetaData;
    //   49: aload_1
    //   50: monitorexit
    //   51: areturn
    //   52: astore_2
    //   53: aload_1
    //   54: monitorexit
    //   55: aload_2
    //   56: athrow
    //   57: astore_1
    //   58: invokestatic 48	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   61: aload_1
    //   62: aload_0
    //   63: getfield 16	com/simba/jdbc/common/SConnection:m_warningListener	Lcom/simba/jdbc/common/SWarningListener;
    //   66: aload_0
    //   67: getfield 4	com/simba/jdbc/common/SConnection:m_logger	Lcom/simba/support/ILogger;
    //   70: invokevirtual 49	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   73: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	this	SConnection
    //   57	5	1	localException	Exception
    //   52	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   22	51	52	finally
    //   52	55	52	finally
    //   0	51	57	java/lang/Exception
    //   52	57	57	java/lang/Exception
  }
  
  public int getTransactionIsolation()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      Variant localVariant = this.m_connection.getProperty(26);
      long l = localVariant.getLong();
      if (0L == l) {
        return 0;
      }
      if (2L == l) {
        return 2;
      }
      if (1L == l) {
        return 1;
      }
      if (4L == l) {
        return 4;
      }
      if (8L == l) {
        return 8;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_ISOLATION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(l) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Map<String, Class<?>> getTypeMap()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return new HashMap();
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
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
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public boolean isClosed()
  {
    return this.m_isClosed;
  }
  
  public boolean isReadOnly()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      Variant localVariant = this.m_connection.getProperty(16);
      return localVariant.getLong() == Long.valueOf(1L).longValue();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String nativeSQL(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      return this.m_connection.toNativeSQL(paramString);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public CallableStatement prepareCall(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      if (!PropertyUtilities.hasStoredProcedureSupport(this.m_connection))
      {
        localObject1 = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
        LogUtilities.logError((Exception)localObject1, this.m_logger);
        throw ((Throwable)localObject1);
      }
      beginTransaction();
      Object localObject1 = this.m_connection.createStatement();
      SCallableStatement localSCallableStatement = JDBCObjectFactory.getInstance().createCallableStatement(paramString, (IStatement)localObject1, this, 1007);
      synchronized (this.m_stmtReferences)
      {
        this.m_stmtReferences.add(new WeakReference(localSCallableStatement));
      }
      return localSCallableStatement;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    checkIfOpen();
    Object localObject1;
    if (!PropertyUtilities.hasStoredProcedureSupport(this.m_connection))
    {
      localObject1 = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
      LogUtilities.logError((Exception)localObject1, this.m_logger);
      throw ((Throwable)localObject1);
    }
    if ((1003 != paramInt1) || (getConcurrency() < paramInt2) || ((paramInt2 != 1007) && (paramInt2 != 1008)))
    {
      localObject1 = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
      LogUtilities.logError((Exception)localObject1, this.m_logger);
      throw ((Throwable)localObject1);
    }
    try
    {
      beginTransaction();
      localObject1 = this.m_connection.createStatement();
      SCallableStatement localSCallableStatement = JDBCObjectFactory.getInstance().createCallableStatement(paramString, (IStatement)localObject1, this, paramInt2);
      synchronized (this.m_stmtReferences)
      {
        this.m_stmtReferences.add(new WeakReference(localSCallableStatement));
      }
      return localSCallableStatement;
    }
    catch (ErrorException localErrorException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localErrorException, this.m_warningListener);
    }
  }
  
  public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
    checkIfOpen();
    if (!PropertyUtilities.hasStoredProcedureSupport(this.m_connection))
    {
      localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    if (getHoldability() == paramInt3) {
      return prepareCall(paramString, paramInt1, paramInt2);
    }
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public PreparedStatement prepareStatement(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      beginTransaction();
      IStatement localIStatement = this.m_connection.createStatement();
      SPreparedStatement localSPreparedStatement = JDBCObjectFactory.getInstance().createPreparedStatement(paramString, localIStatement, this, 1007);
      synchronized (this.m_stmtReferences)
      {
        this.m_stmtReferences.add(new WeakReference(localSPreparedStatement));
      }
      return localSPreparedStatement;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
      checkIfOpen();
      if ((paramInt == 2) || (!SPreparedStatement.isInsertStatement(paramString))) {
        return prepareStatement(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    checkIfOpen();
    Object localObject1;
    if ((1003 != paramInt1) || (getConcurrency() < paramInt2) || ((paramInt2 != 1007) && (paramInt2 != 1008)))
    {
      localObject1 = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
      LogUtilities.logError((Exception)localObject1, this.m_logger);
      throw ((Throwable)localObject1);
    }
    try
    {
      beginTransaction();
      localObject1 = this.m_connection.createStatement();
      SPreparedStatement localSPreparedStatement = JDBCObjectFactory.getInstance().createPreparedStatement(paramString, (IStatement)localObject1, this, paramInt2);
      synchronized (this.m_stmtReferences)
      {
        this.m_stmtReferences.add(new WeakReference(localSPreparedStatement));
      }
      return localSPreparedStatement;
    }
    catch (ErrorException localErrorException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localErrorException, this.m_warningListener);
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
    checkIfOpen();
    if (getHoldability() == paramInt3) {
      return prepareStatement(paramString, paramInt1, paramInt2);
    }
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfInt });
      checkIfOpen();
      if (!SPreparedStatement.isInsertStatement(paramString)) {
        return prepareStatement(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfString });
      checkIfOpen();
      if (!SPreparedStatement.isInsertStatement(paramString)) {
        return prepareStatement(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void releaseSavepoint(Savepoint paramSavepoint)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSavepoint });
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (getAutoCommit())
        {
          if (isDataSourceReadOnly()) {
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.ROLLBACK_AUTOCOMMIT, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (!this.m_savepoints.contains(paramSavepoint)) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_SAVEPOINT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        if (((SSavepoint)paramSavepoint).isNamed()) {
          this.m_connection.releaseSavepoint(paramSavepoint.getSavepointName());
        } else {
          this.m_connection.releaseSavepoint(String.valueOf(paramSavepoint.getSavepointId()));
        }
        while (this.m_savepoints.pop() != paramSavepoint) {}
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
    }
  }
  
  public void rollback()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (getAutoCommit())
        {
          if (isDataSourceReadOnly())
          {
            this.m_allowTransactionCallbacks = true;
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.ROLLBACK_AUTOCOMMIT, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (!this.m_isInTransaction)
        {
          this.m_allowTransactionCallbacks = true;
          return;
        }
        this.m_allowTransactionCallbacks = false;
        this.m_savepoints.clear();
        handleTransactionBehavior(38);
        this.m_connection.rollback();
        this.m_isInTransaction = false;
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
      finally
      {
        this.m_allowTransactionCallbacks = true;
      }
    }
  }
  
  public void rollback(Savepoint paramSavepoint)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSavepoint });
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (getAutoCommit())
        {
          if (isDataSourceReadOnly())
          {
            this.m_allowTransactionCallbacks = true;
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.ROLLBACK_AUTOCOMMIT, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        this.m_allowTransactionCallbacks = false;
        if (!this.m_savepoints.contains(paramSavepoint)) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_SAVEPOINT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        assert (this.m_isInTransaction);
        handleTransactionBehavior(38);
        if (((SSavepoint)paramSavepoint).isNamed()) {
          this.m_connection.rollback(paramSavepoint.getSavepointName());
        } else {
          this.m_connection.rollback(String.valueOf(paramSavepoint.getSavepointId()));
        }
        while (this.m_savepoints.pop() != paramSavepoint) {}
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
      finally
      {
        this.m_allowTransactionCallbacks = true;
      }
    }
  }
  
  public void setAttribute(int paramInt, Object paramObject)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (!this.m_connection.isCustomProperty(paramInt)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ATTRIBUTE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      }
      int i = this.m_connection.getCustomPropertyType(paramInt);
      this.m_connection.setCustomProperty(paramInt, new Variant(i, paramObject));
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void setAutoCommit(boolean paramBoolean)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Boolean.valueOf(paramBoolean) });
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if ((!paramBoolean) && (!getMetaData().supportsTransactions()))
        {
          if (isDataSourceReadOnly())
          {
            this.m_allowTransactionCallbacks = true;
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
        }
        this.m_allowTransactionCallbacks = false;
        Long localLong = null;
        if (paramBoolean)
        {
          localLong = Long.valueOf(1L);
          if (this.m_isInTransaction)
          {
            handleTransactionBehavior(37);
            this.m_connection.commit();
            this.m_isInTransaction = false;
          }
        }
        else
        {
          localLong = Long.valueOf(0L);
          this.m_isInTransaction = false;
        }
        Variant localVariant = new Variant(3, localLong);
        this.m_warningListener.clearAndSetFunction(FunctionID.CONNECTION_SET_PROPERTY);
        this.m_connection.setProperty(19, localVariant);
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
      finally
      {
        this.m_allowTransactionCallbacks = true;
      }
    }
  }
  
  public void setCatalog(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      if (!PropertyUtilities.hasCatalogSupport(this.m_connection)) {
        return;
      }
      Variant localVariant = null;
      if (null == paramString) {
        localVariant = new Variant(0, "");
      } else {
        localVariant = new Variant(0, paramString);
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.CONNECTION_SET_PROPERTY);
      this.m_connection.setProperty(22, localVariant);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void setHoldability(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      if (!getMetaData().supportsResultSetHoldability(paramInt)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNSUPPORTED_HOLDABILITY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      }
      Character localCharacter = null;
      switch (paramInt)
      {
      case 2: 
        localCharacter = Character.valueOf('\001');
        break;
      case 1: 
        localCharacter = Character.valueOf('\002');
        break;
      default: 
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_HOLDABILITY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      }
      Variant localVariant = new Variant(2, localCharacter);
      this.m_warningListener.clearAndSetFunction(FunctionID.CONNECTION_SET_PROPERTY);
      this.m_connection.setProperty(37, localVariant);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void setReadOnly(boolean paramBoolean)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Boolean.valueOf(paramBoolean) });
      checkIfOpen();
      if (this.m_isInTransaction) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SETREADONLY_IN_TRANSACTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      Variant localVariant;
      if (paramBoolean) {
        localVariant = new Variant(3, Long.valueOf(1L));
      } else {
        localVariant = new Variant(3, Long.valueOf(0L));
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.CONNECTION_SET_PROPERTY);
      this.m_connection.setProperty(16, localVariant);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Savepoint setSavepoint()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (getAutoCommit()) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_AUTOCOMMIT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        if ((!this.m_isInTransaction) && (this.m_connection.getProperty(1009).getInt() == 0))
        {
          this.m_connection.beginTransaction();
          this.m_isInTransaction = true;
        }
        this.m_connection.createSavepoint(String.valueOf(this.m_savepointId));
        this.m_isInTransaction = true;
        SSavepoint localSSavepoint = new SSavepoint(this.m_savepointId, this.m_logger, this.m_warningListener);
        this.m_savepointId += 1;
        this.m_savepoints.push(localSSavepoint);
        return localSSavepoint;
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
    }
  }
  
  public Savepoint setSavepoint(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    checkIfOpen();
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (getAutoCommit()) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_AUTOCOMMIT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        if ((!this.m_isInTransaction) && (this.m_connection.getProperty(1009).getInt() == 0))
        {
          this.m_connection.beginTransaction();
          this.m_isInTransaction = true;
        }
        this.m_connection.createSavepoint(paramString);
        this.m_isInTransaction = true;
        SSavepoint localSSavepoint = new SSavepoint(paramString, this.m_logger, this.m_warningListener);
        this.m_savepointId += 1;
        this.m_savepoints.push(localSSavepoint);
        return localSSavepoint;
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
    }
  }
  
  public void setTransactionIsolation(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      Long localLong = null;
      DatabaseMetaData localDatabaseMetaData = getMetaData();
      switch (paramInt)
      {
      case 1: 
        if (localDatabaseMetaData.supportsTransactionIsolationLevel(1)) {
          localLong = Long.valueOf(1L);
        }
        break;
      case 2: 
        if (localDatabaseMetaData.supportsTransactionIsolationLevel(2)) {
          localLong = Long.valueOf(2L);
        }
        break;
      case 4: 
        if (localDatabaseMetaData.supportsTransactionIsolationLevel(4)) {
          localLong = Long.valueOf(4L);
        }
        break;
      case 8: 
        if (localDatabaseMetaData.supportsTransactionIsolationLevel(8))
        {
          localLong = Long.valueOf(8L);
          break label211;
        }
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNSUPPORTED_TXN_ISOLATION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_ISOLATION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      label211:
      Variant localVariant = new Variant(3, localLong);
      this.m_warningListener.clearAndSetFunction(FunctionID.CONNECTION_SET_PROPERTY);
      this.m_connection.setProperty(26, localVariant);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void setTypeMap(Map<String, Class<?>> paramMap)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramMap });
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Array createArrayOf(String paramString, Object[] paramArrayOfObject)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfObject });
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Blob createBlob()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Clob createClob()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public NClob createNClob()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public SQLXML createSQLXML()
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Struct createStruct(String paramString, Object[] paramArrayOfObject)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfObject });
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, this.m_logger);
    throw localSQLException;
  }
  
  public Properties getClientInfo()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    Properties localProperties = new Properties();
    Set localSet = this.m_connection.getClientInfoProperties().keySet();
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      try
      {
        String str2 = this.m_connection.getClientInfo(str1);
        if (null != str2) {
          localProperties.setProperty(str1, str2);
        }
      }
      catch (ErrorException localErrorException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localErrorException, this.m_warningListener, this.m_logger);
      }
    }
    return localProperties;
  }
  
  public String getClientInfo(String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    try
    {
      return this.m_connection.getClientInfo(paramString);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isValid(int paramInt)
    throws SQLException
  {
    if (paramInt < 0) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_TIMEOUT, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), String.valueOf(0), String.valueOf(Double.POSITIVE_INFINITY) });
    }
    if (0 == paramInt) {
      return (!isClosed()) && (this.m_connection.isAlive());
    }
    bool1 = !isClosed();
    if (bool1)
    {
      ExecutorService localExecutorService = Executors.newSingleThreadExecutor();
      try
      {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new Handler(this.m_connection));
        try
        {
          List localList = localExecutorService.invokeAll(localArrayList, paramInt, TimeUnit.SECONDS);
          if ((null != localList) && (localList.size() > 0))
          {
            Future localFuture = (Future)localList.get(0);
            boolean bool3;
            if (localFuture.isCancelled())
            {
              bool3 = false;
              return bool3;
            }
            if (localFuture.isDone()) {
              try
              {
                bool3 = (!isClosed()) && (((Boolean)localFuture.get()).booleanValue());
                return bool3;
              }
              catch (ExecutionException localExecutionException)
              {
                BaseConnectionFactory.s_Messages.createGeneralException(localExecutionException.getMessage(), localExecutionException.getCause());
              }
            }
          }
          boolean bool2 = false;
          return bool2;
        }
        catch (InterruptedException localInterruptedException)
        {
          BaseConnectionFactory.s_Messages.createGeneralException(localInterruptedException.getMessage(), localInterruptedException.getCause());
        }
        return bool1;
      }
      finally
      {
        localExecutorService.shutdown();
      }
    }
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public void setClientInfo(Properties paramProperties)
    throws SQLClientInfoException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    try
    {
      checkIfOpen();
    }
    catch (SQLException localSQLException)
    {
      throw ((SQLClientInfoException)ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_warningListener, ExceptionType.CLIENT_INFO, new Object[0]));
    }
    Map localMap = this.m_connection.getClientInfoProperties();
    Object localObject1 = localMap.entrySet().iterator();
    Object localObject2;
    Object localObject3;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localObject3 = (String)((Map.Entry)localObject2).getKey();
      if ((null != paramProperties) && (paramProperties.containsKey(localObject3)))
      {
        setClientInfo((String)localObject3, paramProperties.getProperty((String)localObject3));
        paramProperties.remove(localObject3);
      }
      else
      {
        setClientInfo((String)localObject3, null);
      }
    }
    if ((null != paramProperties) && (0 != paramProperties.size()))
    {
      localObject1 = new HashMap();
      localObject2 = paramProperties.keySet().iterator();
      if (((Iterator)localObject2).hasNext())
      {
        localObject3 = ((Iterator)localObject2).next();
        ((Map)localObject1).put(localObject3.toString(), FailedPropertiesReason.UNKNOWN_PROPERTY);
        throw ((SQLClientInfoException)ExceptionConverter.getInstance().toSQLException(DSIDriver.s_DSIMessages.createClientInfoException(DSIMessageKey.INVALID_PROPKEY.name(), localObject3.toString(), (Map)localObject1), this.m_warningListener, this.m_logger));
      }
    }
  }
  
  public void setClientInfo(String paramString1, String paramString2)
    throws SQLClientInfoException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    try
    {
      checkIfOpen();
    }
    catch (SQLException localSQLException)
    {
      throw ((SQLClientInfoException)ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_warningListener, ExceptionType.CLIENT_INFO, new Object[0]));
    }
    try
    {
      this.m_connection.setClientInfoProperty(paramString1, paramString2);
    }
    catch (ClientInfoException localClientInfoException)
    {
      throw ((SQLClientInfoException)ExceptionConverter.getInstance().toSQLException(localClientInfoException, this.m_warningListener, this.m_logger));
    }
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  void beginTransaction()
    throws SQLException
  {
    synchronized (this.m_transactionLock)
    {
      if ((!getAutoCommit()) && (!this.m_isInTransaction)) {
        try
        {
          this.m_connection.beginTransaction();
          this.m_isInTransaction = true;
        }
        catch (Exception localException)
        {
          throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
        }
      }
    }
  }
  
  void closeChildObjects()
  {
    synchronized (this.m_transactionLock)
    {
      if (this.m_isInTransaction) {
        try
        {
          rollback();
        }
        catch (SQLException localSQLException)
        {
          LogUtilities.logError(localSQLException, this.m_logger);
        }
      }
    }
    closeChildStatements();
    synchronized (this.m_databaseMetadataLock)
    {
      if (null != this.m_databaseMetaData)
      {
        try
        {
          this.m_databaseMetaData.closeMetaData();
        }
        catch (Exception localException)
        {
          LogUtilities.logError(localException, this.m_logger);
        }
        this.m_databaseMetaData = null;
      }
    }
  }
  
  String getURL()
  {
    return this.m_URL;
  }
  
  /* Error */
  boolean isInTransaction()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 23	com/simba/jdbc/common/SConnection:m_transactionLock	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 17	com/simba/jdbc/common/SConnection:m_isInTransaction	Z
    //   11: aload_1
    //   12: monitorexit
    //   13: ireturn
    //   14: astore_2
    //   15: aload_1
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	19	0	this	SConnection
    //   5	11	1	Ljava/lang/Object;	Object
    //   14	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	13	14	finally
    //   14	17	14	finally
  }
  
  void notifyBeginTransaction()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (!this.m_allowTransactionCallbacks) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_CALLBACKS_NOT_ALLOWED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (this.m_isInTransaction) {
          ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_ALREADY_STARTED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (getAutoCommit())
        {
          Variant localVariant = new Variant(3, Long.valueOf(0L));
          this.m_warningListener.clearAndSetFunction(FunctionID.CONNECTION_SET_PROPERTY);
          this.m_connection.setProperty(19, localVariant);
        }
        this.m_isInTransaction = true;
      }
      catch (SQLException localSQLException)
      {
        throw new GeneralException(localSQLException.getLocalizedMessage(), localSQLException.getErrorCode(), localSQLException);
      }
      catch (Exception localException)
      {
        throw new GeneralException(localException.getLocalizedMessage(), 0, localException);
      }
    }
  }
  
  void notifyCommit()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (!this.m_allowTransactionCallbacks) {
          ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_CALLBACKS_NOT_ALLOWED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (getAutoCommit())
        {
          if (isDataSourceReadOnly()) {
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.COMMIT_AUTOCOMMIT, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        assert (this.m_isInTransaction);
        this.m_savepoints.clear();
        handleTransactionBehavior(37);
        this.m_connection.beginTransaction();
      }
      catch (SQLException localSQLException)
      {
        throw new GeneralException(localSQLException.getLocalizedMessage(), localSQLException.getErrorCode(), localSQLException);
      }
    }
  }
  
  void notifyCreateSavepoint(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (!this.m_allowTransactionCallbacks) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_CALLBACKS_NOT_ALLOWED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (getAutoCommit()) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_AUTOCOMMIT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        assert (this.m_isInTransaction);
        this.m_savepoints.push(new SSavepoint(paramString, this.m_logger, this.m_warningListener));
        this.m_savepointId += 1;
      }
      catch (SQLException localSQLException)
      {
        throw new GeneralException(localSQLException.getLocalizedMessage(), localSQLException.getErrorCode(), localSQLException);
      }
    }
  }
  
  void notifyReleaseSavepoint(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (!this.m_allowTransactionCallbacks) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_CALLBACKS_NOT_ALLOWED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (getAutoCommit()) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_AUTOCOMMIT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        assert (this.m_isInTransaction);
        int i = getSavepointIndex(paramString);
        while (this.m_savepoints.size() > i) {
          this.m_savepoints.pop();
        }
      }
      catch (SQLException localSQLException)
      {
        throw new GeneralException(localSQLException.getLocalizedMessage(), localSQLException.getErrorCode(), localSQLException);
      }
    }
  }
  
  void notifyRollback()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (!this.m_allowTransactionCallbacks) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_CALLBACKS_NOT_ALLOWED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (getAutoCommit())
        {
          if (isDataSourceReadOnly()) {
            return;
          }
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.COMMIT_AUTOCOMMIT, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        assert (this.m_isInTransaction);
        this.m_savepoints.clear();
        handleTransactionBehavior(38);
        this.m_connection.beginTransaction();
      }
      catch (SQLException localSQLException)
      {
        throw new GeneralException(localSQLException.getLocalizedMessage(), localSQLException.getErrorCode(), localSQLException);
      }
    }
  }
  
  void notifyRollbackSavepoint(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    synchronized (this.m_transactionLock)
    {
      try
      {
        if (!this.m_allowTransactionCallbacks) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.TRANSACTION_CALLBACKS_NOT_ALLOWED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
        }
        if (getAutoCommit()) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.SAVEPOINT_AUTOCOMMIT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
        }
        assert (this.m_isInTransaction);
        int i = getSavepointIndex(paramString);
        handleTransactionBehavior(38);
        while (this.m_savepoints.size() > i) {
          this.m_savepoints.pop();
        }
      }
      catch (SQLException localSQLException)
      {
        throw new GeneralException(localSQLException.getLocalizedMessage(), localSQLException.getErrorCode(), localSQLException);
      }
    }
  }
  
  void removeStatement(SStatement paramSStatement)
  {
    synchronized (this.m_stmtReferences)
    {
      for (int i = this.m_stmtReferences.size() - 1; i >= 0; i--)
      {
        WeakReference localWeakReference = (WeakReference)this.m_stmtReferences.get(i);
        if (null != localWeakReference)
        {
          SStatement localSStatement = (SStatement)localWeakReference.get();
          if ((null == localSStatement) || (localSStatement == paramSStatement)) {
            this.m_stmtReferences.remove(i);
          }
        }
      }
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    close();
  }
  
  protected void setMaxTypeValues()
    throws SQLException
  {
    if (this.m_areMaxSizesAreSet) {
      return;
    }
    try
    {
      Variant localVariant = this.m_connection.getProperty(1004);
      this.m_maxVarbinarySize = localVariant.getInt();
      localVariant = this.m_connection.getProperty(1005);
      this.m_maxVarcharSize = localVariant.getInt();
    }
    catch (Exception localException)
    {
      this.m_maxVarbinarySize = 0;
      this.m_maxVarcharSize = 0;
    }
    if ((0 == this.m_maxVarbinarySize) || (0 == this.m_maxVarcharSize))
    {
      ResultSet localResultSet = getMetaData().getTypeInfo();
      try
      {
        while (localResultSet.next())
        {
          int i = localResultSet.getInt(2);
          int j;
          if (12 == i)
          {
            j = localResultSet.getInt(3);
            if (!localResultSet.wasNull()) {
              this.m_maxVarcharSize = j;
            }
            if (0 != this.m_maxVarbinarySize) {
              break;
            }
          }
          else if (-3 == i)
          {
            j = localResultSet.getInt(3);
            if (!localResultSet.wasNull()) {
              this.m_maxVarbinarySize = j;
            }
            if (0 != this.m_maxVarcharSize) {
              break;
            }
          }
        }
      }
      finally
      {
        localResultSet.close();
      }
    }
    this.m_areMaxSizesAreSet = true;
  }
  
  protected void checkIfOpen()
    throws SQLException
  {
    if (isClosed()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
    }
  }
  
  protected synchronized void abortInternal()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    try
    {
      closeChildObjects();
    }
    finally
    {
      closeConnection();
    }
  }
  
  protected synchronized void markConnectionClosed()
  {
    this.m_isClosed = true;
  }
  
  private void closeChildStatements()
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject1;
    synchronized (this.m_stmtReferences)
    {
      localObject1 = this.m_stmtReferences.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        WeakReference localWeakReference = (WeakReference)((Iterator)localObject1).next();
        if (null != localWeakReference)
        {
          SStatement localSStatement = (SStatement)localWeakReference.get();
          if (null != localSStatement) {
            localArrayList.add(localSStatement);
          }
        }
      }
      this.m_stmtReferences.clear();
    }
    ??? = localArrayList.iterator();
    while (((Iterator)???).hasNext())
    {
      localObject1 = (SStatement)((Iterator)???).next();
      try
      {
        ((SStatement)localObject1).close();
      }
      catch (Exception localException) {}
    }
  }
  
  private void closeConnection()
    throws SQLException
  {
    if (null != this.m_connection) {
      try
      {
        this.m_connection.disconnect();
      }
      catch (ErrorException localErrorException)
      {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONNECTION_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
      }
      finally
      {
        this.m_connection.close();
        this.m_connection = null;
      }
    }
  }
  
  private int getConcurrency()
    throws SQLException
  {
    try
    {
      Variant localVariant = this.m_connection.getProperty(1001);
      if (0L != localVariant.getLong()) {
        return 1008;
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
    return 1007;
  }
  
  private int getSavepointIndex(String paramString)
    throws SQLException
  {
    for (int i = this.m_savepoints.size() - 1; i >= 0; i--)
    {
      SSavepoint localSSavepoint = (SSavepoint)this.m_savepoints.get(i);
      if (((localSSavepoint.isNamed()) && (paramString.equals(localSSavepoint.getSavepointName()))) || ((!localSSavepoint.isNamed()) && (paramString.equals(String.valueOf(localSSavepoint.getSavepointId()))))) {
        return i;
      }
    }
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_SAVEPOINT, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
  }
  
  private void handleTransactionBehavior(int paramInt)
    throws SQLException
  {
    try
    {
      Variant localVariant = this.m_connection.getProperty(paramInt);
      long l = localVariant.getLong();
      int i;
      WeakReference localWeakReference;
      SStatement localSStatement;
      if (1L == l) {
        synchronized (this.m_stmtReferences)
        {
          for (i = this.m_stmtReferences.size() - 1; i >= 0; i--)
          {
            localWeakReference = (WeakReference)this.m_stmtReferences.get(i);
            if (null != localWeakReference)
            {
              localSStatement = (SStatement)localWeakReference.get();
              if (null != localSStatement) {
                localSStatement.closeResults();
              } else {
                this.m_stmtReferences.remove(i);
              }
            }
          }
        }
      } else if (0L == l) {
        synchronized (this.m_stmtReferences)
        {
          for (i = this.m_stmtReferences.size() - 1; i >= 0; i--)
          {
            localWeakReference = (WeakReference)this.m_stmtReferences.get(i);
            if (null != localWeakReference)
            {
              localSStatement = (SStatement)localWeakReference.get();
              if (null != localSStatement)
              {
                localSStatement.closeResults();
                localSStatement.closeIfPrepared();
              }
              else
              {
                this.m_stmtReferences.remove(i);
              }
            }
          }
        }
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private boolean isDataSourceReadOnly()
  {
    try
    {
      Variant localVariant = this.m_connection.getProperty(40);
      return localVariant.getString().equals("Y");
    }
    catch (ErrorException localErrorException)
    {
      LogUtilities.logError(localErrorException, this.m_logger);
    }
    return true;
  }
  
  protected static class Handler
    implements Callable<Boolean>
  {
    private IConnection m_connection;
    
    public Handler(IConnection paramIConnection)
    {
      this.m_connection = paramIConnection;
    }
    
    public Boolean call()
      throws Exception
    {
      return Boolean.valueOf(this.m_connection.isAlive());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */