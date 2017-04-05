package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.impl.DSISimpleRowCountResult;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.dsi.dataengine.interfaces.IErrorResult;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.interfaces.IRowCountResult;
import com.simba.dsi.dataengine.utilities.ExecutionContexts;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.dsi.dataengine.utilities.ExecutionResultType;
import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.IMessageSource;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.FunctionID;
import com.simba.utilities.JDBCVersion;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public abstract class SStatement
  implements Statement
{
  private static final int MAX_ROWS_UNLIMITED = 0;
  private static final String INSERT_TOKEN = "insert";
  private static final int RESULT_SET_UPDATE_COUNT = -1;
  protected Iterator<ExecutionResult> m_resultIterator = null;
  protected List<ResultContext> m_resultSets = new ArrayList();
  protected IStatement m_statement = null;
  protected IQueryExecutor m_queryExecutor = null;
  protected SWarningListener m_warningListener = null;
  protected IMessageSource m_messageSource = null;
  protected boolean m_escapeProcessingEnabled = true;
  protected ILogger m_logger = null;
  protected SConnection m_parentConnection = null;
  private int m_currentResultSetIndex = 0;
  private int m_concurrency = 1007;
  private List<String> m_batchSQLStatements = new ArrayList();
  protected JDBCVersion m_jdbcVersion;
  private final Object m_closeLock = new Object();
  protected boolean m_isCanceled = false;
  protected boolean m_isInCancelableFunction = false;
  protected final Object m_cancelLock = new Object();
  private final Object m_resultSetsLock = new Object();
  protected volatile boolean m_closeOnCompletion = false;
  
  static boolean isInsertStatement(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
    String str = localStringTokenizer.nextToken();
    return "insert".equalsIgnoreCase(str);
  }
  
  protected SStatement(IStatement paramIStatement, SConnection paramSConnection, int paramInt)
  {
    this.m_logger = paramSConnection.getConnection().getConnectionLog();
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramIStatement, paramSConnection, Integer.valueOf(paramInt) });
    this.m_parentConnection = paramSConnection;
    this.m_statement = paramIStatement;
    this.m_concurrency = paramInt;
    this.m_messageSource = DSIDriverSingleton.getInstance().getMessageSource();
    this.m_warningListener = new SWarningListener(this.m_messageSource, null);
    this.m_warningListener.setLocale(paramSConnection.getDSIConnection().getLocale());
    this.m_statement.registerWarningListener(this.m_warningListener);
  }
  
  public synchronized void addBatch(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      if ((this.m_escapeProcessingEnabled) && (DSIDriverSingleton.getInstance().getProperty(10).getInt() == 1)) {
        paramString = this.m_parentConnection.nativeSQL(paramString);
      }
      this.m_batchSQLStatements.add(paramString);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void cancel()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    try
    {
      synchronized (this.m_cancelLock)
      {
        if (this.m_isInCancelableFunction) {
          this.m_isCanceled = true;
        }
      }
      ??? = this.m_queryExecutor;
      if (null != ???) {
        ((IQueryExecutor)???).cancelExecute();
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void clearBatch()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_batchSQLStatements.clear();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void clearWarnings()
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
    synchronized (this.m_closeLock)
    {
      try
      {
        LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
        clearResults();
        if (null != this.m_parentConnection)
        {
          this.m_parentConnection.removeStatement(this);
          this.m_parentConnection = null;
        }
        if (null != this.m_statement)
        {
          this.m_statement.close();
          this.m_statement = null;
        }
        replaceQueryExecutor(null);
      }
      catch (Exception localException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
      }
    }
  }
  
  public synchronized boolean execute(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      checkIfNullSQL(paramString);
      if (!this.m_batchSQLStatements.isEmpty()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.BATCH_NOT_EMPTY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      clearResults();
      ExecutionResults localExecutionResults = executeNoParams(paramString, ThrowCondition.None);
      this.m_resultIterator = localExecutionResults.getResultItr();
      if (!this.m_resultIterator.hasNext()) {
        return false;
      }
      addResultPair(createResultPair((ExecutionResult)this.m_resultIterator.next()));
      return (ExecutionResultType.RESULT_SET == ((ResultContext)this.m_resultSets.get(0)).m_resultType) || (ExecutionResultType.ERROR_RESULT_SET == ((ResultContext)this.m_resultSets.get(0)).m_resultType);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized boolean execute(String paramString, int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
      checkIfOpen();
      if ((paramInt == 2) || (!isInsertStatement(paramString))) {
        return execute(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized boolean execute(String paramString, int[] paramArrayOfInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfInt });
      checkIfOpen();
      if (!isInsertStatement(paramString)) {
        return execute(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized boolean execute(String paramString, String[] paramArrayOfString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfString });
      checkIfOpen();
      if (!isInsertStatement(paramString)) {
        return execute(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int[] executeBatch()
    throws SQLException, BatchUpdateException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      synchronized (this.m_cancelLock)
      {
        this.m_isInCancelableFunction = true;
      }
      this.m_parentConnection.beginTransaction();
      ??? = this.m_statement.createDataEngine();
      ((IDataEngine)???).setDirectExecute();
      ((IDataEngine)???).setMetadataNeeded(false);
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_PREPARE);
      replaceQueryExecutor(((IDataEngine)???).prepareBatch(this.m_batchSQLStatements));
      ((IDataEngine)???).close();
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      localArrayList1.add(new ArrayList());
      ExecutionContexts localExecutionContexts = new ExecutionContexts(localArrayList2, localArrayList1);
      clearResults();
      synchronized (this.m_cancelLock)
      {
        if (this.m_isCanceled) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.OPERATION_CANCELED, this.m_warningListener, ExceptionType.TRANSIENT, new Object[0]);
        }
      }
      this.m_warningListener.setCurrentFunction(FunctionID.STATEMENT_EXECUTE);
      this.m_queryExecutor.execute(localExecutionContexts, this.m_warningListener);
      ??? = processBatchResults(this.m_queryExecutor.getResults());
      synchronized (this.m_cancelLock)
      {
        this.m_isCanceled = false;
        this.m_isInCancelableFunction = false;
      }
      this.m_batchSQLStatements.clear();
      return (int[])???;
    }
    catch (Exception localException1) {}finally
    {
      try
      {
        replaceQueryExecutor(null);
        throw ExceptionConverter.getInstance().toSQLException(localException1, this.m_warningListener, this.m_logger);
      }
      catch (Exception localException2)
      {
        LogUtilities.logError(localException2, this.m_logger);
        throw ExceptionConverter.getInstance().toSQLException(localException1, this.m_warningListener, this.m_logger);
      }
      finally
      {
        synchronized (this.m_cancelLock)
        {
          this.m_isCanceled = false;
          this.m_isInCancelableFunction = false;
        }
      }
    }
  }
  
  public synchronized ResultSet executeQuery(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      checkIfNullSQL(paramString);
      if (!this.m_batchSQLStatements.isEmpty()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.BATCH_NOT_EMPTY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      clearResults();
      ExecutionResults localExecutionResults = executeNoParams(paramString, ThrowCondition.SingleResult);
      ExecutionResult localExecutionResult = (ExecutionResult)localExecutionResults.getResultItr().next();
      if (ExecutionResultType.ERROR_RESULT_SET == localExecutionResult.getType())
      {
        addResultPair(createResultPair(localExecutionResult));
        throw ((IErrorResult)localExecutionResult.getResult()).getError();
      }
      ResultSet localResultSet = createResultSet(localExecutionResult);
      ((SForwardResultSet)localResultSet).initializeColumnNameMap();
      addResultSet(localResultSet);
      return localResultSet;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int executeUpdate(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      checkIfNullSQL(paramString);
      if (!this.m_batchSQLStatements.isEmpty()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.BATCH_NOT_EMPTY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      clearResults();
      ExecutionResults localExecutionResults = executeNoParams(paramString, ThrowCondition.SingleRowCount);
      this.m_resultIterator = localExecutionResults.getResultItr();
      ExecutionResult localExecutionResult = (ExecutionResult)this.m_resultIterator.next();
      if (ExecutionResultType.ERROR_ROW_COUNT == localExecutionResult.getType())
      {
        addResultPair(createResultPair(localExecutionResult));
        throw ((IErrorResult)localExecutionResult.getResult()).getError();
      }
      IRowCountResult localIRowCountResult = (IRowCountResult)localExecutionResult.getResult();
      addResultPair(createResultPair(localExecutionResult));
      if (localIRowCountResult.hasRowCount()) {
        return castRowCount(localIRowCountResult.getRowCount());
      }
      return 0;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int executeUpdate(String paramString, int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, Integer.valueOf(paramInt) });
      checkIfOpen();
      if ((paramInt == 2) || (!isInsertStatement(paramString))) {
        return executeUpdate(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int executeUpdate(String paramString, int[] paramArrayOfInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfInt });
      checkIfOpen();
      if (!isInsertStatement(paramString)) {
        return executeUpdate(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int executeUpdate(String paramString, String[] paramArrayOfString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramArrayOfString });
      checkIfOpen();
      if (!isInsertStatement(paramString)) {
        return executeUpdate(paramString);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    close();
  }
  
  public synchronized Object getAttribute(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (!this.m_statement.isCustomProperty(paramInt)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ATTRIBUTE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      }
      Variant localVariant = this.m_statement.getCustomProperty(paramInt);
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
  
  public Connection getConnection()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_parentConnection;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int getFetchDirection()
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
  
  public synchronized int getFetchSize()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_statement.getProperty(6).getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public abstract ResultSet getGeneratedKeys()
    throws SQLException;
  
  protected ILogger getLogger()
  {
    return this.m_logger;
  }
  
  public synchronized int getMaxFieldSize()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return getStatementPropertyInt(1);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int getMaxRows()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      Variant localVariant = this.m_statement.getProperty(2);
      long l = localVariant.getLong();
      if ((2147483647L < l) || (-2147483648L > l)) {
        return 0;
      }
      return (int)l;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized boolean getMoreResults()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return getMoreResults(true);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized boolean getMoreResults(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      switch (paramInt)
      {
      case 1: 
        return getMoreResults(true);
      case 3: 
        closeAllResultsToCurrent();
        return getMoreResults(true);
      case 2: 
        IDriver localIDriver = DSIDriverSingleton.getInstance();
        Variant localVariant = localIDriver.getProperty(1005);
        if (localVariant.getShort() != 1) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
        }
        return getMoreResults(false);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_MORERESULTS_VALUE, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public SConnection getParentConnection()
  {
    return this.m_parentConnection;
  }
  
  public IQueryExecutor getQueryExecutor()
  {
    return this.m_queryExecutor;
  }
  
  public synchronized int getQueryTimeout()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return getStatementPropertyInt(3);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized ResultSet getResultSet()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (this.m_resultSets.size() > this.m_currentResultSetIndex)
      {
        ResultContext localResultContext = (ResultContext)this.m_resultSets.get(this.m_currentResultSetIndex);
        if (ExecutionResultType.ERROR_RESULT_SET == localResultContext.m_resultType) {
          throw ((IErrorResult)localResultContext.m_result).getError();
        }
        if (ExecutionResultType.RESULT_SET == localResultContext.m_resultType)
        {
          ((SForwardResultSet)localResultContext.m_result).initializeColumnNameMap();
          return (ResultSet)localResultContext.m_result;
        }
      }
      return null;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getResultSetConcurrency()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_concurrency;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getResultSetHoldability()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return this.m_parentConnection.getHoldability();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getResultSetType()
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
  
  public IStatement getStatement()
  {
    return this.m_statement;
  }
  
  public synchronized int getUpdateCount()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (this.m_resultSets.size() > this.m_currentResultSetIndex)
      {
        ResultContext localResultContext = (ResultContext)this.m_resultSets.get(this.m_currentResultSetIndex);
        if (ExecutionResultType.ERROR_ROW_COUNT == localResultContext.m_resultType) {
          throw ((IErrorResult)localResultContext.m_result).getError();
        }
        if (ExecutionResultType.ROW_COUNT == localResultContext.m_resultType) {
          return castRowCount(((IRowCountResult)localResultContext.m_result).getRowCount());
        }
      }
      return -1;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  protected SWarningListener getWarningListener()
  {
    return this.m_warningListener;
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
  
  public synchronized void setAttribute(int paramInt, Object paramObject)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (!this.m_statement.isCustomProperty(paramInt)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ATTRIBUTE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramInt) });
      }
      int i = this.m_statement.getCustomPropertyType(paramInt);
      this.m_statement.setCustomProperty(paramInt, new Variant(i, paramObject));
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setCursorName(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setEscapeProcessing(boolean paramBoolean)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Boolean.valueOf(paramBoolean) });
      checkIfOpen();
      this.m_escapeProcessingEnabled = paramBoolean;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setFetchDirection(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      switch (paramInt)
      {
      case 1000: 
        break;
      case 1001: 
      case 1002: 
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
      default: 
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_FETCH_DIRECTION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setFetchSize(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      int i = getMaxRows();
      if (0 == i)
      {
        if (paramInt < 0) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FETCH_SIZE_UNLIMITED_MAX, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
        }
      }
      else if ((paramInt < 0) || (paramInt > i)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FETCH_SIZE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), String.valueOf(0), String.valueOf(i) });
      }
      this.m_statement.setProperty(6, new Variant(4, Long.valueOf(paramInt)));
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setMaxFieldSize(int paramInt)
    throws SQLException
  {
    long l1 = 0L;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    try
    {
      l1 = getStatementPropertyLong(101);
    }
    catch (Exception localException1) {}
    long l2 = 0L;
    try
    {
      l2 = getStatementPropertyLong(100);
    }
    catch (Exception localException2) {}
    try
    {
      if ((l1 > paramInt) || ((0L != l2) && (l2 < paramInt))) {
        throw new Exception();
      }
      setStatementProperty(1, 4, Long.valueOf(paramInt));
    }
    catch (Exception localException3)
    {
      String str1 = String.valueOf(Double.POSITIVE_INFINITY);
      String str2 = String.valueOf(l1);
      if (0L != l2) {
        str1 = String.valueOf(l2);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_FIELD_SIZE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), str2, str1 });
    }
  }
  
  public synchronized void setMaxRows(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    long l1 = 0L;
    try
    {
      l1 = getStatementPropertyLong(103);
    }
    catch (Exception localException1) {}
    long l2 = 0L;
    try
    {
      l2 = getStatementPropertyLong(102);
    }
    catch (Exception localException2) {}
    try
    {
      if ((l1 > paramInt) || ((0L != l2) && (l2 < paramInt))) {
        throw new Exception();
      }
      setStatementProperty(2, 4, Long.valueOf(paramInt));
    }
    catch (Exception localException3)
    {
      String str1 = String.valueOf(Double.POSITIVE_INFINITY);
      String str2 = String.valueOf(l1);
      if (0L != l2) {
        str1 = String.valueOf(l2);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_MAX_ROW, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), str2, str1 });
    }
  }
  
  public synchronized void setQueryTimeout(int paramInt)
    throws SQLException
  {
    long l1 = 0L;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    try
    {
      l1 = getStatementPropertyLong(105);
    }
    catch (Exception localException1) {}
    long l2 = 0L;
    try
    {
      l2 = getStatementPropertyLong(104);
    }
    catch (Exception localException2) {}
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      if ((l1 > paramInt) || ((0L != l2) && (l2 < paramInt))) {
        throw new Exception();
      }
      setStatementProperty(3, 4, Long.valueOf(paramInt));
    }
    catch (Exception localException3)
    {
      String str1 = String.valueOf(Double.POSITIVE_INFINITY);
      String str2 = String.valueOf(l1);
      if (0L != l2) {
        str1 = String.valueOf(l2);
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_TIMEOUT, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), str2, str1 });
    }
  }
  
  public boolean isClosed()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    return (null == this.m_statement) || (null == getParentConnection());
  }
  
  public boolean isPoolable()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    checkIfOpen();
    return false;
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public void setPoolable(boolean paramBoolean)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Boolean.valueOf(paramBoolean) });
    checkIfOpen();
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  void closeIfPrepared()
    throws SQLException
  {}
  
  void closeResults()
    throws SQLException
  {
    for (int i = this.m_resultSets.size() - 1; i >= 0; i--)
    {
      ResultContext localResultContext = (ResultContext)this.m_resultSets.get(i);
      if ((ExecutionResultType.RESULT_SET == localResultContext.m_resultType) && (!localResultContext.m_isResultClosed)) {
        ((ResultSet)localResultContext.m_result).close();
      }
    }
  }
  
  void markResultSetClosed(ResultSet paramResultSet)
    throws SQLException
  {
    int i = 1;
    synchronized (this.m_resultSetsLock)
    {
      Iterator localIterator = this.m_resultSets.iterator();
      while (localIterator.hasNext())
      {
        ResultContext localResultContext = (ResultContext)localIterator.next();
        if (localResultContext.m_result == paramResultSet) {
          localResultContext.m_isResultClosed = true;
        }
        if ((ExecutionResultType.RESULT_SET == localResultContext.m_resultType) && (!localResultContext.m_isResultClosed)) {
          i = 0;
        }
      }
    }
    if ((i != 0) && (this.m_closeOnCompletion)) {
      close();
    }
  }
  
  protected void addResultPair(ResultContext paramResultContext)
  {
    if ((null != this.m_resultSets) && (!this.m_resultSets.contains(paramResultContext))) {
      this.m_resultSets.add(paramResultContext);
    }
  }
  
  protected void addResultSet(ResultSet paramResultSet)
  {
    addResultPair(new ResultContext(paramResultSet));
  }
  
  protected void checkCondition(String paramString, ThrowCondition paramThrowCondition)
    throws ErrorException, SQLException
  {
    if (ThrowCondition.None != paramThrowCondition)
    {
      ExecutionResults localExecutionResults = this.m_queryExecutor.getResults();
      Iterator localIterator = localExecutionResults.getResultItr();
      if (!localIterator.hasNext()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.ONE_RESULT_NOT_RETURNED, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(0), paramString });
      }
      ExecutionResult localExecutionResult = (ExecutionResult)localIterator.next();
      if (localIterator.hasNext()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.ONE_RESULT_NOT_RETURNED, this.m_warningListener, ExceptionType.DATA, new Object[] { "1 <", paramString });
      }
      ExecutionResultType localExecutionResultType = localExecutionResult.getType();
      if ((ThrowCondition.SingleResult == paramThrowCondition) && (ExecutionResultType.RESULT_SET != localExecutionResultType) && (ExecutionResultType.ERROR_RESULT_SET != localExecutionResultType)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NO_RESULTSET_GENERATED, this.m_warningListener, ExceptionType.DATA, new Object[] { paramString });
      }
      if ((ThrowCondition.SingleRowCount == paramThrowCondition) && (ExecutionResultType.ROW_COUNT != localExecutionResultType) && (ExecutionResultType.ERROR_ROW_COUNT != localExecutionResultType)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NO_ROWCOUNT_GENERATED, this.m_warningListener, ExceptionType.DATA, new Object[] { paramString });
      }
    }
  }
  
  protected void checkIfNullSQL(String paramString)
    throws SQLException
  {
    if (null == paramString) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NULL_SQL_STRING, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
  }
  
  protected void checkIfOpen()
    throws SQLException
  {
    int i;
    synchronized (this.m_closeLock)
    {
      if (null == this.m_statement) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STATEMENT_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      i = null == this.m_parentConnection ? 1 : 0;
    }
    if (i != 0)
    {
      close();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARENT_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
  }
  
  protected void clearResults()
  {
    if (null != this.m_resultSets)
    {
      Object localObject = this.m_resultSets.iterator();
      while (((Iterator)localObject).hasNext())
      {
        ResultContext localResultContext = (ResultContext)((Iterator)localObject).next();
        try
        {
          if ((ExecutionResultType.RESULT_SET == localResultContext.m_resultType) && (!localResultContext.m_isResultClosed)) {
            ((ResultSet)localResultContext.m_result).close();
          }
        }
        catch (Exception localException) {}
      }
      this.m_resultSets.clear();
      if (null != this.m_resultIterator)
      {
        while (this.m_resultIterator.hasNext())
        {
          localObject = (ExecutionResult)this.m_resultIterator.next();
          if (ExecutionResultType.RESULT_SET == ((ExecutionResult)localObject).getType()) {
            ((IResultSet)((ExecutionResult)localObject).getResult()).close();
          }
        }
        this.m_resultIterator = null;
      }
    }
    this.m_currentResultSetIndex = 0;
  }
  
  protected ResultContext createResultPair(ExecutionResult paramExecutionResult)
    throws SQLException
  {
    if (ExecutionResultType.ROW_COUNT == paramExecutionResult.getType())
    {
      localObject = (IRowCountResult)paramExecutionResult.getResult();
      return new ResultContext((IRowCountResult)localObject);
    }
    if (ExecutionResultType.RESULT_SET == paramExecutionResult.getType())
    {
      localObject = createResultSet(paramExecutionResult);
      return new ResultContext((ResultSet)localObject);
    }
    if (ExecutionResultType.ERROR_RESULT_SET == paramExecutionResult.getType())
    {
      localObject = (IErrorResult)paramExecutionResult.getResult();
      return new ResultContext((IErrorResult)localObject, true);
    }
    Object localObject = (IErrorResult)paramExecutionResult.getResult();
    return new ResultContext((IErrorResult)localObject, false);
  }
  
  protected abstract ResultSet createResultSet(ExecutionResult paramExecutionResult)
    throws SQLException;
  
  protected boolean createsUpdatableResults()
  {
    return 1007 != this.m_concurrency;
  }
  
  protected int[] processBatchResults(ExecutionResults paramExecutionResults)
    throws ErrorException, SQLException
  {
    this.m_resultIterator = paramExecutionResults.getResultItr();
    ArrayList localArrayList1 = null;
    ArrayList localArrayList2 = new ArrayList();
    long l1 = 0L;
    int i = 0;
    Short localShort = null;
    while (this.m_resultIterator.hasNext())
    {
      localObject1 = (ExecutionResult)this.m_resultIterator.next();
      Object localObject2;
      if (ExecutionResultType.ROW_COUNT == ((ExecutionResult)localObject1).getType())
      {
        localObject2 = (IRowCountResult)((ExecutionResult)localObject1).getResult();
        long l2 = ((IRowCountResult)localObject2).getRowCount();
        if ((-1L == l2) || (2147483647L < l2))
        {
          localArrayList2.add(Integer.valueOf(-2));
          i = 1;
        }
        else
        {
          localArrayList2.add(Integer.valueOf((int)l2));
        }
        if (2147483647L > l1)
        {
          l1 += ((Integer)localArrayList2.get(localArrayList2.size() - 1)).intValue();
          if (2147483647L < l1) {
            l1 = 2147483647L;
          }
        }
      }
      else
      {
        if (null == localArrayList1) {
          localArrayList1 = new ArrayList();
        }
        if (ExecutionResultType.RESULT_SET == ((ExecutionResult)localObject1).getType())
        {
          localObject2 = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULTSET_RETURNED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
          localArrayList1.add(localObject2);
        }
        else
        {
          localObject2 = (IErrorResult)((ExecutionResult)localObject1).getResult();
          localArrayList1.add(ExceptionConverter.getInstance().toSQLException(((IErrorResult)localObject2).getError(), this.m_warningListener));
        }
        if (null == localShort) {
          try
          {
            localObject2 = DSIDriverSingleton.getInstance();
            localShort = Short.valueOf(((IDriver)localObject2).getProperty(1001).getShort());
          }
          catch (Exception localException)
          {
            localShort = Short.valueOf((short)1);
          }
        }
        if (1 == localShort.shortValue()) {
          break;
        }
        localArrayList2.add(Integer.valueOf(-3));
      }
    }
    Object localObject1 = new int[localArrayList2.size()];
    for (int j = 0; j < localArrayList2.size(); j++) {
      localObject1[j] = ((Integer)localArrayList2.get(j)).intValue();
    }
    BatchUpdateException localBatchUpdateException = null;
    if (null != localArrayList1)
    {
      Iterator localIterator = localArrayList1.iterator();
      SQLException localSQLException1 = (SQLException)localIterator.next();
      localBatchUpdateException = new BatchUpdateException(localSQLException1.getMessage(), localSQLException1.getSQLState(), localSQLException1.getErrorCode(), (int[])localObject1);
      SQLException localSQLException2;
      for (Object localObject3 = localBatchUpdateException; localIterator.hasNext(); localObject3 = localSQLException2)
      {
        localSQLException2 = (SQLException)localIterator.next();
        ((SQLException)localObject3).setNextException(localSQLException2);
      }
    }
    if (i != 0) {
      addResultPair(new ResultContext(new DSISimpleRowCountResult(-1L)));
    } else {
      addResultPair(new ResultContext(new DSISimpleRowCountResult(l1)));
    }
    if (null != localBatchUpdateException) {
      throw localBatchUpdateException;
    }
    return (int[])localObject1;
  }
  
  private void closeAllResultsToCurrent()
    throws SQLException
  {
    for (int i = 0; i < this.m_currentResultSetIndex; i++)
    {
      ResultContext localResultContext = (ResultContext)this.m_resultSets.get(i);
      if ((ExecutionResultType.RESULT_SET == localResultContext.m_resultType) && (!localResultContext.m_isResultClosed)) {
        ((ResultSet)localResultContext.m_result).close();
      }
    }
  }
  
  private ExecutionResults executeNoParams(String paramString, ThrowCondition paramThrowCondition)
    throws SQLException
  {
    try
    {
      synchronized (this.m_cancelLock)
      {
        this.m_isInCancelableFunction = true;
      }
      this.m_parentConnection.beginTransaction();
      ??? = this.m_statement.createDataEngine();
      ((IDataEngine)???).setDirectExecute();
      if (ThrowCondition.None == paramThrowCondition) {
        ((IDataEngine)???).setMetadataNeeded(false);
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_PREPARE);
      if ((this.m_escapeProcessingEnabled) && (DSIDriverSingleton.getInstance().getProperty(10).getInt() == 1)) {
        paramString = this.m_parentConnection.nativeSQL(paramString);
      }
      replaceQueryExecutor(((IDataEngine)???).prepare(paramString));
      ((IDataEngine)???).close();
      if (this.m_queryExecutor.getNumParams() != 0) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_PARAM, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      checkCondition(paramString, paramThrowCondition);
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      localArrayList1.add(new ArrayList());
      ExecutionContexts localExecutionContexts = new ExecutionContexts(localArrayList2, localArrayList1);
      synchronized (this.m_cancelLock)
      {
        if (this.m_isCanceled) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.OPERATION_CANCELED, this.m_warningListener, ExceptionType.TRANSIENT, new Object[0]);
        }
      }
      this.m_warningListener.setCurrentFunction(FunctionID.STATEMENT_EXECUTE);
      this.m_queryExecutor.execute(localExecutionContexts, this.m_warningListener);
      ??? = this.m_queryExecutor.getResults();
      synchronized (this.m_cancelLock)
      {
        this.m_isInCancelableFunction = false;
      }
      return (ExecutionResults)???;
    }
    catch (Exception localException1) {}finally
    {
      try
      {
        replaceQueryExecutor(null);
        throw ExceptionConverter.getInstance().toSQLException(localException1, this.m_warningListener, this.m_logger);
      }
      catch (Exception localException2)
      {
        LogUtilities.logError(localException2, this.m_logger);
        throw ExceptionConverter.getInstance().toSQLException(localException1, this.m_warningListener, this.m_logger);
      }
      finally {}
    }
  }
  
  private boolean getMoreResults(boolean paramBoolean)
    throws SQLException
  {
    if (this.m_resultSets.size() > this.m_currentResultSetIndex)
    {
      ResultContext localResultContext1 = (ResultContext)this.m_resultSets.get(this.m_currentResultSetIndex);
      if ((paramBoolean) && (ExecutionResultType.RESULT_SET == localResultContext1.m_resultType) && (!localResultContext1.m_isResultClosed)) {
        ((ResultSet)localResultContext1.m_result).close();
      }
      this.m_currentResultSetIndex += 1;
      if ((null != this.m_resultIterator) && (this.m_resultIterator.hasNext()))
      {
        ResultContext localResultContext2 = createResultPair((ExecutionResult)this.m_resultIterator.next());
        addResultPair(localResultContext2);
      }
      if (this.m_resultSets.size() == this.m_currentResultSetIndex) {
        return false;
      }
      localResultContext1 = (ResultContext)this.m_resultSets.get(this.m_currentResultSetIndex);
      return (ExecutionResultType.RESULT_SET == localResultContext1.m_resultType) || (ExecutionResultType.ERROR_RESULT_SET == localResultContext1.m_resultType);
    }
    return false;
  }
  
  private int getStatementPropertyInt(int paramInt)
    throws SQLException
  {
    try
    {
      Variant localVariant = this.m_statement.getProperty(paramInt);
      return localVariant.getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private long getStatementPropertyLong(int paramInt)
    throws SQLException
  {
    try
    {
      Variant localVariant = this.m_statement.getProperty(paramInt);
      return localVariant.getLong();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private void replaceQueryExecutor(IQueryExecutor paramIQueryExecutor)
  {
    IQueryExecutor localIQueryExecutor = this.m_queryExecutor;
    if (localIQueryExecutor != null) {
      localIQueryExecutor.close();
    }
    this.m_queryExecutor = paramIQueryExecutor;
  }
  
  private void setStatementProperty(int paramInt1, int paramInt2, Object paramObject)
    throws SQLException
  {
    try
    {
      this.m_warningListener.setCurrentFunction(FunctionID.STATEMENT_SET_PROPERTY);
      this.m_statement.setProperty(paramInt1, new Variant(paramInt2, paramObject));
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private int castRowCount(long paramLong)
  {
    return paramLong > 2147483647L ? -2 : (int)paramLong;
  }
  
  protected static enum ThrowCondition
  {
    None,  SingleResult,  SingleRowCount;
    
    private ThrowCondition() {}
  }
  
  protected static class ResultContext
  {
    protected ExecutionResultType m_resultType;
    protected boolean m_isResultClosed = false;
    protected Object m_result = null;
    
    ResultContext(IRowCountResult paramIRowCountResult)
    {
      this.m_resultType = ExecutionResultType.ROW_COUNT;
      this.m_result = paramIRowCountResult;
    }
    
    ResultContext(ResultSet paramResultSet)
    {
      this.m_resultType = ExecutionResultType.RESULT_SET;
      this.m_result = paramResultSet;
    }
    
    ResultContext(IErrorResult paramIErrorResult, boolean paramBoolean)
    {
      if (paramBoolean) {
        this.m_resultType = ExecutionResultType.ERROR_RESULT_SET;
      } else {
        this.m_resultType = ExecutionResultType.ERROR_ROW_COUNT;
      }
      this.m_result = paramIErrorResult;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */