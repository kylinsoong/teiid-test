package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.impl.DSISimpleRowCountResult;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.dsi.dataengine.interfaces.IErrorResult;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.dataengine.interfaces.IRowCountResult;
import com.simba.dsi.dataengine.interfaces.IStreamQueryExecutor;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.ExecutionContexts;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.dsi.dataengine.utilities.ExecutionResultType;
import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.ParameterType;
import com.simba.dsi.dataengine.utilities.TimeTz;
import com.simba.dsi.dataengine.utilities.TimestampTz;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.BadDefaultParamException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.streams.parameters.AbstractParameterStream;
import com.simba.streams.parameters.AsciiParameterStream;
import com.simba.streams.parameters.BinaryParameterStream;
import com.simba.streams.parameters.CharacterParameterStream;
import com.simba.streams.parameters.UnicodeParameterStream;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.conv.ExactNumConverter;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.FunctionID;
import com.simba.utilities.ReferenceEqualityWrapper;
import com.simba.utilities.conversion.TypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public abstract class SPreparedStatement
  extends SStatement
  implements PreparedStatement
{
  private static final Map<Integer, Set<Integer>> s_stringRepCompatibilityLookup = initializeStringRepCompatibilityLookup();
  protected ArrayList<ParameterMetadata> m_parameterMetadata;
  protected final int m_numParameters;
  protected List<ParameterType> m_parameterTypes;
  protected Map<Integer, DataWrapper> m_parameterInputValues = new TreeMap();
  protected Map<Integer, AbstractParameterStream> m_parameterInputStreamData = new TreeMap();
  protected boolean m_hasParameterBatch = false;
  protected List<Map<Integer, DataWrapper>> m_batchParameterInputValues = new ArrayList();
  protected List<Map<Integer, AbstractParameterStream>> m_batchParameterInputStreamData = new ArrayList();
  protected String m_preparedSql = "";
  protected SResultSetMetaData m_metadata = null;
  protected SParameterMetaData m_openParamMetaData = null;
  private List<DataWrapper> m_cachedDataWrappers = null;
  private boolean m_supportStreamingBatches = false;
  private HashSet<ReferenceEqualityWrapper> m_currExecutionStreamSet = new HashSet();
  private HashSet<ReferenceEqualityWrapper> m_lastStreamsExecuted = new HashSet();
  private int m_nextParamSetForStream = 0;
  protected HashMap<Integer, TypeMetadata> m_setMetadata;
  protected boolean m_isSetMetadataFinal = false;
  private final int UNKNOWN_TYPE = 2000;
  
  protected SPreparedStatement(String paramString, IStatement paramIStatement, SConnection paramSConnection, int paramInt)
    throws SQLException
  {
    super(paramIStatement, paramSConnection, paramInt);
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramIStatement, paramSConnection });
      this.m_preparedSql = paramString;
      checkIfNullSQL(paramString);
      IDataEngine localIDataEngine = this.m_statement.createDataEngine();
      if ((this.m_escapeProcessingEnabled) && (DSIDriverSingleton.getInstance().getProperty(10).getInt() == 1)) {
        paramString = paramSConnection.nativeSQL(paramString);
      }
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_PREPARE);
      this.m_queryExecutor = localIDataEngine.prepare(paramString);
      localIDataEngine.close();
      this.m_parameterMetadata = this.m_queryExecutor.getMetadataForParameters();
      if (null == this.m_parameterMetadata) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NULL_PARAM_METADATA, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      this.m_numParameters = this.m_parameterMetadata.size();
      this.m_setMetadata = new HashMap();
      this.m_cachedDataWrappers = new ArrayList(this.m_numParameters);
      this.m_parameterTypes = new ArrayList(this.m_numParameters);
      int i = 1;
      for (int j = 0; j < this.m_numParameters; j++)
      {
        ParameterMetadata localParameterMetadata = (ParameterMetadata)this.m_parameterMetadata.get(j);
        if ((i != 0) && (localParameterMetadata.getTypeMetadata().isCharacterOrBinaryType()))
        {
          i = 0;
          this.m_parentConnection.setMaxTypeValues();
        }
        this.m_parameterTypes.add(localParameterMetadata.getParameterType());
        this.m_cachedDataWrappers.add(new DataWrapper());
      }
      checkParameters(this.m_parameterMetadata);
      clearParameters();
      if (this.m_parentConnection.getConnection().getProperty(1006).getString().equals("Y")) {
        if (!(this.m_queryExecutor instanceof IStreamQueryExecutor)) {
          getLogger().logWarning("com.simba.jdbc.common", "SPreparedStatement", "SPreparedStatement", "ConnPropertyKey.DSI_SUPPORTS_STREAMED_BATCH_PREPARED_STMTS is set to 'Y', but IStreamQueryExecutor interface is not implemented. Batched parameters will not be streamed.");
        } else {
          this.m_supportStreamingBatches = true;
        }
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void addBatch()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_queryExecutor.clearCancel();
      if (!allInputParameterValuesPopulated()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_NUMBER_PARAMS, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      updateParameterMetadata();
      this.m_cachedDataWrappers.clear();
      for (int i = 0; i < this.m_numParameters; i++) {
        this.m_cachedDataWrappers.add(new DataWrapper());
      }
      this.m_hasParameterBatch = true;
      if (!this.m_supportStreamingBatches)
      {
        this.m_batchParameterInputValues.add(this.m_parameterInputValues);
        this.m_batchParameterInputStreamData.add(this.m_parameterInputStreamData);
        this.m_parameterInputValues = new TreeMap(this.m_parameterInputValues);
        this.m_parameterInputStreamData = new TreeMap();
      }
      else
      {
        IStreamQueryExecutor localIStreamQueryExecutor = (IStreamQueryExecutor)this.m_queryExecutor;
        if (0 == this.m_nextParamSetForStream)
        {
          this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
          this.m_parentConnection.beginTransaction();
          clearResults();
          this.m_nextParamSetForStream += 1;
        }
        localIStreamQueryExecutor.startParamSet(getInputParameterValues(this.m_parameterInputValues, this.m_parameterInputStreamData), this.m_warningListener);
        pushClearSingleBatchSetStreams();
        localIStreamQueryExecutor.finalizeParamSet(this.m_warningListener);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void addBatch(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      this.m_isSetMetadataFinal = false;
      this.m_setMetadata.clear();
      for (int i = 0; i < this.m_batchParameterInputValues.size(); i++) {
        clearParameters((Map)this.m_batchParameterInputValues.get(i), (Map)this.m_batchParameterInputStreamData.get(i));
      }
      clearParameters(this.m_parameterInputValues, this.m_parameterInputStreamData);
      if (null != this.m_queryExecutor) {
        if (this.m_supportStreamingBatches)
        {
          IStreamQueryExecutor localIStreamQueryExecutor = (IStreamQueryExecutor)this.m_queryExecutor;
          this.m_nextParamSetForStream = 0;
          localIStreamQueryExecutor.clearBatch();
        }
        else
        {
          this.m_queryExecutor.clearPushedParamData();
          this.m_batchParameterInputStreamData.clear();
          this.m_batchParameterInputValues.clear();
        }
      }
      this.m_hasParameterBatch = false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void clearParameters()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_isSetMetadataFinal = false;
      this.m_setMetadata.clear();
      clearParameters(this.m_parameterInputValues, this.m_parameterInputStreamData);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void close()
    throws SQLException
  {
    try
    {
      clearBatch();
    }
    catch (SQLException localSQLException) {}
    super.close();
    if (null != this.m_queryExecutor)
    {
      this.m_queryExecutor.close();
      this.m_queryExecutor = null;
    }
    if (null != this.m_metadata) {
      this.m_metadata = null;
    }
    if (null != this.m_openParamMetaData) {
      this.m_openParamMetaData.close();
    }
  }
  
  public synchronized boolean execute()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_queryExecutor.clearCancel();
      if (this.m_hasParameterBatch) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.BATCH_NOT_EMPTY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      if (!allInputParameterValuesPopulated()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_NUMBER_PARAMS, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      if (!this.m_lastStreamsExecuted.isEmpty()) {
        throw new InputOutputException(1, JDBCMessageKey.STREAM_REUSED.name());
      }
      updateParameterMetadata();
      this.m_parentConnection.beginTransaction();
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      pushParameterStreams();
      clearResults();
      ExecutionResults localExecutionResults = executeWithParams(this.m_parameterMetadata, getInputParameterValues(), SStatement.ThrowCondition.None);
      this.m_resultIterator = localExecutionResults.getResultItr();
      if (!this.m_resultIterator.hasNext())
      {
        boolean bool1 = false;
        return bool1;
      }
      ExecutionResult localExecutionResult = (ExecutionResult)this.m_resultIterator.next();
      addResultPair(createResultPair(localExecutionResult));
      SStatement.ResultContext localResultContext = (SStatement.ResultContext)this.m_resultSets.get(0);
      if ((ExecutionResultType.ERROR_RESULT_SET == localResultContext.m_resultType) || (ExecutionResultType.ERROR_ROW_COUNT == localResultContext.m_resultType)) {
        throw ((IErrorResult)localResultContext.m_result).getError();
      }
      boolean bool2 = (ExecutionResultType.RESULT_SET == localResultContext.m_resultType) || (ExecutionResultType.ERROR_RESULT_SET == localResultContext.m_resultType);
      return bool2;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
    finally
    {
      this.m_isSetMetadataFinal = false;
    }
  }
  
  public synchronized boolean execute(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      if (!this.m_lastStreamsExecuted.isEmpty()) {
        throw new InputOutputException(1, JDBCMessageKey.STREAM_REUSED.name());
      }
      this.m_queryExecutor.clearCancel();
      if (!this.m_supportStreamingBatches)
      {
        this.m_parentConnection.beginTransaction();
        this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
        if ((!this.m_hasParameterBatch) && (0 < this.m_numParameters))
        {
          clearResults();
          addResultPair(new SStatement.ResultContext(new DSISimpleRowCountResult(0L)));
          ??? = new int[0];
          return (int[])???;
        }
        pushBatchParameterStreams();
        ExecutionContexts localExecutionContexts = new ExecutionContexts(this.m_parameterMetadata, getBatchInputParameterValues());
        clearResults();
        synchronized (this.m_cancelLock)
        {
          if (this.m_isCanceled) {
            throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.OPERATION_CANCELED, this.m_warningListener, ExceptionType.TRANSIENT, new Object[0]);
          }
        }
        this.m_queryExecutor.execute(localExecutionContexts, this.m_warningListener);
      }
      else
      {
        this.m_currExecutionStreamSet.clear();
        this.m_queryExecutor.finalizePushedParamData();
        ??? = (IStreamQueryExecutor)this.m_queryExecutor;
        if ((!this.m_hasParameterBatch) && (0 < this.m_numParameters))
        {
          clearResults();
          addResultPair(new SStatement.ResultContext(new DSISimpleRowCountResult(0L)));
          ??? = new int[0];
          return (int[])???;
        }
        synchronized (this.m_cancelLock)
        {
          if (this.m_isCanceled) {
            throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.OPERATION_CANCELED, this.m_warningListener, ExceptionType.TRANSIENT, new Object[0]);
          }
        }
        ((IStreamQueryExecutor)???).execute(this.m_warningListener);
      }
      ??? = processBatchResults(this.m_queryExecutor.getResults());
      return (int[])???;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
    finally
    {
      synchronized (this.m_cancelLock)
      {
        this.m_isCanceled = false;
        this.m_isInCancelableFunction = false;
      }
      clearBatch();
    }
  }
  
  public synchronized ResultSet executeQuery()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_queryExecutor.clearCancel();
      if (this.m_hasParameterBatch) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.BATCH_NOT_EMPTY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      if (!allInputParameterValuesPopulated()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_NUMBER_PARAMS, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      if (!this.m_lastStreamsExecuted.isEmpty()) {
        throw new InputOutputException(1, JDBCMessageKey.STREAM_REUSED.name());
      }
      updateParameterMetadata();
      this.m_parentConnection.beginTransaction();
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      pushParameterStreams();
      clearResults();
      ExecutionResults localExecutionResults = executeWithParams(this.m_parameterMetadata, getInputParameterValues(), SStatement.ThrowCondition.SingleResult);
      ExecutionResult localExecutionResult = (ExecutionResult)localExecutionResults.getResultItr().next();
      if (ExecutionResultType.ERROR_RESULT_SET == localExecutionResult.getType())
      {
        addResultPair(createResultPair(localExecutionResult));
        throw ((IErrorResult)localExecutionResult.getResult()).getError();
      }
      ResultSet localResultSet1 = createResultSet(localExecutionResult);
      ((SForwardResultSet)localResultSet1).initializeColumnNameMap();
      addResultSet(localResultSet1);
      ResultSet localResultSet2 = localResultSet1;
      return localResultSet2;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
    finally
    {
      this.m_isSetMetadataFinal = false;
    }
  }
  
  public synchronized ResultSet executeQuery(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized int executeUpdate()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      this.m_queryExecutor.clearCancel();
      if (this.m_hasParameterBatch) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.BATCH_NOT_EMPTY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      if (!allInputParameterValuesPopulated()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_NUMBER_PARAMS, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
      }
      if (!this.m_lastStreamsExecuted.isEmpty()) {
        throw new InputOutputException(1, JDBCMessageKey.STREAM_REUSED.name());
      }
      updateParameterMetadata();
      this.m_parentConnection.beginTransaction();
      this.m_warningListener.clearAndSetFunction(FunctionID.STATEMENT_EXECUTE);
      pushParameterStreams();
      clearResults();
      ExecutionResults localExecutionResults = executeWithParams(this.m_parameterMetadata, getInputParameterValues(), SStatement.ThrowCondition.SingleRowCount);
      ExecutionResult localExecutionResult = (ExecutionResult)localExecutionResults.getResultItr().next();
      if (ExecutionResultType.ERROR_ROW_COUNT == localExecutionResult.getType())
      {
        addResultPair(createResultPair(localExecutionResult));
        throw ((IErrorResult)localExecutionResult.getResult()).getError();
      }
      IRowCountResult localIRowCountResult = (IRowCountResult)localExecutionResult.getResult();
      addResultPair(createResultPair(localExecutionResult));
      if (localIRowCountResult.hasRowCount())
      {
        long l = localIRowCountResult.getRowCount();
        int j = l > 2147483647L ? -2 : (int)l;
        return j;
      }
      int i = 0;
      return i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
    finally
    {
      this.m_isSetMetadataFinal = false;
    }
  }
  
  public synchronized int executeUpdate(String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
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
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_STMT_ACTION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public abstract ResultSetMetaData getMetaData()
    throws SQLException;
  
  public SResultSetMetaData getResultSetMetaData()
  {
    return this.m_metadata;
  }
  
  public abstract ParameterMetaData getParameterMetaData()
    throws SQLException;
  
  public synchronized void setArray(int paramInt, Array paramArray)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramArray });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramInputStream, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-1));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1))).getType(), -1)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-1));
      }
      if (null == paramInputStream)
      {
        setNull(paramInt1, ((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1))).getType());
      }
      else
      {
        AsciiParameterStream localAsciiParameterStream = new AsciiParameterStream(paramInputStream, paramInt2);
        setParameterInputStream(paramInt1, localAsciiParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramBigDecimal });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      TypeMetadata localTypeMetadata = createExactNumMetadata(paramBigDecimal, 2);
      if (null == paramBigDecimal)
      {
        setNull(paramInt, 2);
      }
      else
      {
        DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
        localDataWrapper.setNumeric(paramBigDecimal);
        setParameterInputData(paramInt, localDataWrapper);
      }
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), localTypeMetadata);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramInputStream, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-4));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1))).getType(), -4)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-4));
      }
      if (null == paramInputStream)
      {
        setNull(paramInt1, -4);
      }
      else
      {
        BinaryParameterStream localBinaryParameterStream = new BinaryParameterStream(paramInputStream, paramInt2);
        setParameterInputStream(paramInt1, localBinaryParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setBlob(int paramInt, Blob paramBlob)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramBlob });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setBoolean(int paramInt, boolean paramBoolean)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(16));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      localDataWrapper.setBoolean(paramBoolean);
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setByte(int paramInt, byte paramByte)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Byte.valueOf(paramByte) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-6, true));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      localDataWrapper.setTinyInt((short)paramByte);
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramArrayOfByte });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-4));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      if (null == paramArrayOfByte) {
        localDataWrapper.setNull(-4);
      } else if (paramArrayOfByte.length > this.m_parentConnection.m_maxVarbinarySize) {
        localDataWrapper.setLongVarBinary(paramArrayOfByte);
      } else {
        localDataWrapper.setVarBinary(paramArrayOfByte);
      }
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramReader, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-1));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1))).getType(), -1)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-1));
      }
      if (null == paramReader)
      {
        setNull(paramInt1, -1);
      }
      else
      {
        CharacterParameterStream localCharacterParameterStream = new CharacterParameterStream(paramReader, paramInt2);
        setParameterInputStream(paramInt1, localCharacterParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setClob(int paramInt, Clob paramClob)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramClob });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setDate(int paramInt, Date paramDate)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramDate });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(91));
      }
      if (null == paramDate)
      {
        setNull(paramInt, 91);
      }
      else
      {
        DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
        localDataWrapper.setDate(paramDate);
        setParameterInputData(paramInt, localDataWrapper);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramDate });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    try
    {
      if ((null == paramCalendar) || (null == paramDate))
      {
        setDate(paramInt, paramDate);
      }
      else
      {
        paramCalendar.clear();
        paramCalendar.setTimeInMillis(paramDate.getTime());
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.clear();
        localCalendar.set(paramCalendar.get(1), paramCalendar.get(2), paramCalendar.get(5), 0, 0, 0);
        localCalendar.set(14, 0);
        setDate(paramInt, new Date(localCalendar.getTimeInMillis()));
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setDouble(int paramInt, double paramDouble)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Double.valueOf(paramDouble) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(8));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      localDataWrapper.setDouble(paramDouble);
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setFloat(int paramInt, float paramFloat)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Float.valueOf(paramFloat) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(7));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      localDataWrapper.setReal(paramFloat);
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setInt(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(4, true));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt1 - 1);
      localDataWrapper.setInteger(paramInt2);
      setParameterInputData(paramInt1, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setLong(int paramInt, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-5, true));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      localDataWrapper.setBigInt(BigInteger.valueOf(paramLong));
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setNull(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      checkTypeSupported(paramInt2);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), createMetadataForSqlType(paramInt2));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt1 - 1);
      localDataWrapper.setNull(paramInt2);
      setParameterInputData(paramInt1, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setNull(int paramInt1, int paramInt2, String paramString)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramString });
    setNull(paramInt1, paramInt2);
  }
  
  public synchronized void setObject(int paramInt, Object paramObject)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramObject });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      if (null == paramObject)
      {
        localDataWrapper.setNull(2000);
      }
      else
      {
        int i = TypeConverter.getSqlType(paramObject);
        localDataWrapper.setData(i, paramObject);
        if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1))))
        {
          TypeMetadata localTypeMetadata;
          if (((paramObject instanceof BigDecimal)) && ((2 == i) || (3 == i))) {
            localTypeMetadata = createExactNumMetadata((BigDecimal)paramObject, i);
          } else {
            localTypeMetadata = createMetadataForSqlType(i);
          }
          this.m_setMetadata.put(Integer.valueOf(paramInt - 1), localTypeMetadata);
        }
      }
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARAM_OBJECT_MISMATCH, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramObject) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramObject, Integer.valueOf(paramInt2) });
      int i = 0;
      if ((paramObject instanceof BigDecimal)) {
        i = createExactNumMetadata((BigDecimal)paramObject, 2).getScale();
      }
      setObject(paramInt1, paramObject, paramInt2, i);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt1), paramObject, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt1 - 1);
      if (null == paramObject)
      {
        setNull(paramInt1, paramInt2);
      }
      else
      {
        try
        {
          int i = TypeConverter.getSqlType(paramObject);
          localDataWrapper.setData(i, paramObject);
          if (paramInt2 != i) {
            if (((paramObject instanceof BigDecimal)) && ((2 == paramInt2) || (3 == paramInt2))) {
              localDataWrapper = TypeConverter.toType(localDataWrapper, createExactNumMetadata((BigDecimal)paramObject, paramInt2), getWarningListener());
            } else {
              localDataWrapper = TypeConverter.toType(localDataWrapper, paramInt2, getWarningListener());
            }
          }
          TypeMetadata localTypeMetadata = null;
          BigDecimal localBigDecimal;
          switch (paramInt2)
          {
          case 2: 
            localBigDecimal = localDataWrapper.getNumeric().setScale(paramInt3, 4);
            localTypeMetadata = createExactNumMetadata(localBigDecimal, paramInt2);
            localDataWrapper.setNumeric(localBigDecimal);
            break;
          case 3: 
            localBigDecimal = localDataWrapper.getDecimal().setScale(paramInt3, 4);
            localTypeMetadata = createExactNumMetadata(localBigDecimal, paramInt2);
            localDataWrapper.setDecimal(localBigDecimal);
            break;
          default: 
            localTypeMetadata = createMetadataForSqlType(paramInt2);
          }
          if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
            this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), localTypeMetadata);
          }
        }
        catch (IncorrectTypeException localIncorrectTypeException)
        {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARAM_OBJECT_MISMATCH, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramObject) });
        }
        setParameterInputData(paramInt1, localDataWrapper);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public void setParameterInputStream(int paramInt, AbstractParameterStream paramAbstractParameterStream)
    throws SQLException
  {
    ParameterMetadata localParameterMetadata = (ParameterMetadata)this.m_parameterMetadata.get(paramInt - 1);
    if (ParameterType.OUTPUT == localParameterMetadata.getParameterType()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_SET_TYPE, getWarningListener(), ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
    }
    this.m_parameterInputValues.remove(Integer.valueOf(paramInt));
    AbstractParameterStream localAbstractParameterStream = (AbstractParameterStream)this.m_parameterInputStreamData.get(Integer.valueOf(paramInt));
    if (null != localAbstractParameterStream)
    {
      this.m_lastStreamsExecuted.remove(localAbstractParameterStream.getEqualityStreamWrapper());
      localAbstractParameterStream.close();
    }
    this.m_parameterInputStreamData.put(Integer.valueOf(paramInt), paramAbstractParameterStream);
  }
  
  public void setResultSetMetadata(SResultSetMetaData paramSResultSetMetaData)
  {
    this.m_metadata = paramSResultSetMetaData;
  }
  
  public synchronized void setRef(int paramInt, Ref paramRef)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramRef });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setShort(int paramInt, short paramShort)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Short.valueOf(paramShort) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(5, true));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      localDataWrapper.setSmallInt(paramShort);
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setString(int paramInt, String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramString });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      int i;
      if ((null == paramString) || (paramString.length() <= this.m_parentConnection.m_maxVarcharSize)) {
        i = 12;
      } else {
        i = -1;
      }
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(i));
      }
      DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
      if (null == paramString) {
        localDataWrapper.setNull(12);
      } else if (-1 == i) {
        localDataWrapper.setLongVarChar(paramString);
      } else {
        localDataWrapper.setVarChar(paramString);
      }
      setParameterInputData(paramInt, localDataWrapper);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setTime(int paramInt, Time paramTime)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTime });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), createTimeMetadata());
      }
      if (null == paramTime)
      {
        setNull(paramInt, 92);
      }
      else
      {
        DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
        localDataWrapper.setTime(paramTime);
        setParameterInputData(paramInt, localDataWrapper);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTime });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), createTimeMetadata());
      }
      if (null == paramTime)
      {
        setNull(paramInt, 92);
      }
      else
      {
        DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
        localDataWrapper.setTime(new TimeTz(paramTime, paramCalendar));
        setParameterInputData(paramInt, localDataWrapper);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTimestamp });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), createTimestampMetadata());
      }
      if (null == paramTimestamp)
      {
        setNull(paramInt, 93);
      }
      else
      {
        DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
        localDataWrapper.setTimestamp(paramTimestamp);
        setParameterInputData(paramInt, localDataWrapper);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTimestamp, paramCalendar });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), createTimestampMetadata());
      }
      if (null == paramTimestamp)
      {
        setNull(paramInt, 93);
      }
      else
      {
        DataWrapper localDataWrapper = (DataWrapper)this.m_cachedDataWrappers.get(paramInt - 1);
        localDataWrapper.setTimestamp(new TimestampTz(paramTimestamp, paramCalendar));
        setParameterInputData(paramInt, localDataWrapper);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /**
   * @deprecated
   */
  public synchronized void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramInputStream, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkValidParameterIndex(paramInt1);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-1));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt1 - 1))).getType(), -1)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt1 - 1), TypeMetadata.createTypeMetadata(-1));
      }
      if (null == paramInputStream)
      {
        setNull(paramInt1, -1);
      }
      else
      {
        UnicodeParameterStream localUnicodeParameterStream = new UnicodeParameterStream(paramInputStream, paramInt2);
        setParameterInputStream(paramInt1, localUnicodeParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void setURL(int paramInt, URL paramURL)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramURL });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    setAsciiStream(paramInt, paramInputStream, -1);
  }
  
  public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-1));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt - 1))).getType(), -1)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-1));
      }
      if (null == paramInputStream)
      {
        setNull(paramInt, -1);
      }
      else
      {
        AsciiParameterStream localAsciiParameterStream = new AsciiParameterStream(paramInputStream, paramLong);
        setParameterInputStream(paramInt, localAsciiParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    setBinaryStream(paramInt, paramInputStream, -1);
  }
  
  public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-4));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt - 1))).getType(), -4)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-4));
      }
      if (null == paramInputStream)
      {
        setNull(paramInt, -4);
      }
      else
      {
        BinaryParameterStream localBinaryParameterStream = new BinaryParameterStream(paramInputStream, paramLong);
        setParameterInputStream(paramInt, localBinaryParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    setCharacterStream(paramInt, paramReader, -1);
  }
  
  public void setCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
      checkIfOpen();
      checkValidParameterIndex(paramInt);
      if ((!this.m_isSetMetadataFinal) || (null == this.m_setMetadata.get(Integer.valueOf(paramInt - 1)))) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-1));
      } else if (TypeConverter.canConvertStreamTo(((TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt - 1))).getType(), -1)) {
        this.m_setMetadata.put(Integer.valueOf(paramInt - 1), TypeMetadata.createTypeMetadata(-1));
      }
      if (null == paramReader)
      {
        setNull(paramInt, -1);
      }
      else
      {
        CharacterParameterStream localCharacterParameterStream = new CharacterParameterStream(paramReader, paramLong);
        setParameterInputStream(paramInt, localCharacterParameterStream);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public void setClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setNClob(int paramInt, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramNClob });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setNClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setNString(int paramInt, String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramString });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setRowId(int paramInt, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramRowId });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void setSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramSQLXML });
    checkIfOpen();
    checkValidParameterIndex(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  void closeIfPrepared()
    throws SQLException
  {
    close();
  }
  
  protected boolean allInputParameterValuesPopulated()
  {
    return this.m_parameterInputStreamData.size() + this.m_parameterInputValues.size() == this.m_numParameters;
  }
  
  protected void checkParameters(List<ParameterMetadata> paramList)
    throws SQLException
  {
    for (int i = 0; i < paramList.size(); i++) {
      if (ParameterType.INPUT != this.m_parameterTypes.get(i)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_INPUT_ONLY, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(((ParameterMetadata)paramList.get(i)).getParameterNumber()) });
      }
    }
  }
  
  protected void checkTypeSupported(int paramInt)
    throws SQLException
  {
    switch (paramInt)
    {
    case -11: 
    case -7: 
    case -6: 
    case -5: 
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 12: 
    case 16: 
    case 91: 
    case 92: 
    case 93: 
    case 1111: 
    case 2000: 
      break;
    default: 
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
  }
  
  protected void checkValidParameterIndex(int paramInt)
    throws SQLException
  {
    if ((1 > paramInt) || (this.m_numParameters < paramInt)) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
    }
  }
  
  protected void clearParameters(Map<Integer, DataWrapper> paramMap, Map<Integer, AbstractParameterStream> paramMap1)
  {
    paramMap.clear();
    Iterator localIterator = paramMap1.values().iterator();
    while (localIterator.hasNext())
    {
      AbstractParameterStream localAbstractParameterStream = (AbstractParameterStream)localIterator.next();
      if (null != localAbstractParameterStream)
      {
        this.m_lastStreamsExecuted.remove(localAbstractParameterStream.getEqualityStreamWrapper());
        localAbstractParameterStream.close();
      }
    }
    paramMap1.clear();
  }
  
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
      this.m_warningListener.setCurrentFunction(FunctionID.STATEMENT_EXECUTE);
      this.m_queryExecutor.execute((ExecutionContexts)???, this.m_warningListener);
      this.m_metadata = null;
      ??? = this.m_queryExecutor.getResults();
      return (ExecutionResults)???;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
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
  
  protected SParameterMetaData getOpenParamMetaData()
  {
    return this.m_openParamMetaData;
  }
  
  protected ArrayList<ParameterMetadata> getParameterMetadataList()
  {
    return this.m_parameterMetadata;
  }
  
  protected void setOpenParamMetaData(SParameterMetaData paramSParameterMetaData)
  {
    this.m_openParamMetaData = paramSParameterMetaData;
  }
  
  private DataWrapper convertValueToTypeAsString(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata)
    throws IncorrectTypeException, SQLException
  {
    if (TypeUtilities.isCharacterType(paramDataWrapper.getType())) {
      return paramDataWrapper;
    }
    if (!stringRepresentationsCompatible(paramDataWrapper.getType(), paramTypeMetadata.getType())) {
      paramDataWrapper = TypeConverter.toType(paramDataWrapper, paramTypeMetadata, this.m_warningListener);
    }
    paramDataWrapper.setVarChar(TypeConverter.toString(paramDataWrapper, paramTypeMetadata));
    return paramDataWrapper;
  }
  
  private void setParameterInputData(int paramInt, DataWrapper paramDataWrapper)
    throws SQLException
  {
    if (ParameterType.OUTPUT == this.m_parameterTypes.get(paramInt - 1)) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
    }
    Integer localInteger = Integer.valueOf(paramInt);
    AbstractParameterStream localAbstractParameterStream = (AbstractParameterStream)this.m_parameterInputStreamData.get(localInteger);
    if (null != localAbstractParameterStream)
    {
      this.m_lastStreamsExecuted.remove(localAbstractParameterStream.getEqualityStreamWrapper());
      localAbstractParameterStream.close();
      this.m_parameterInputStreamData.remove(localInteger);
    }
    this.m_parameterInputValues.put(localInteger, paramDataWrapper);
  }
  
  private ParameterInputValue createConvertedParameterInputValue(DataWrapper paramDataWrapper, ParameterMetadata paramParameterMetadata)
    throws SQLException
  {
    assert (null != paramDataWrapper);
    assert (null != paramParameterMetadata);
    try
    {
      TypeMetadata localTypeMetadata = paramParameterMetadata.getTypeMetadata();
      if ((Nullable.NO_NULLS == paramParameterMetadata.getNullable()) && (paramDataWrapper.isNull())) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARAM_NOT_NULLABLE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramParameterMetadata.getParameterNumber()) });
      }
      if ((2000 == paramDataWrapper.getType()) && (paramDataWrapper.isNull())) {
        paramDataWrapper.setNull(localTypeMetadata.getType());
      }
      if (paramParameterMetadata.shouldConvertInputToString()) {
        paramDataWrapper = convertValueToTypeAsString(paramDataWrapper, localTypeMetadata);
      } else {
        paramDataWrapper = TypeConverter.toType(paramDataWrapper, localTypeMetadata, this.m_warningListener);
      }
      return new ParameterInputValue(paramParameterMetadata, paramDataWrapper);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERSION_ERROR_INPUT_PARAM, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramParameterMetadata.getParameterNumber()) });
    }
  }
  
  private ParameterMetadata extractParameterMetadataForStream(int paramInt)
    throws SQLException
  {
    int i = ((ParameterMetadata)this.m_parameterMetadata.get(paramInt)).getTypeMetadata().getType();
    TypeMetadata localTypeMetadata = (TypeMetadata)this.m_setMetadata.get(Integer.valueOf(paramInt));
    int j;
    if (null != localTypeMetadata) {
      j = localTypeMetadata.getType();
    } else {
      j = 2000;
    }
    if (!TypeConverter.canConvertStreamTo(j, i)) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_SET_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt + 1) });
    }
    return (ParameterMetadata)this.m_parameterMetadata.get(paramInt);
  }
  
  private ArrayList<ArrayList<ParameterInputValue>> getBatchInputParameterValues()
    throws SQLException
  {
    int i = this.m_batchParameterInputValues.size();
    ArrayList localArrayList1 = new ArrayList(i);
    for (int j = 0; j < i; j++)
    {
      ArrayList localArrayList2 = getInputParameterValues((Map)this.m_batchParameterInputValues.get(j), (Map)this.m_batchParameterInputStreamData.get(j));
      localArrayList1.add(localArrayList2);
    }
    return localArrayList1;
  }
  
  private ArrayList<ArrayList<ParameterInputValue>> getInputParameterValues()
    throws SQLException
  {
    ArrayList localArrayList1 = getInputParameterValues(this.m_parameterInputValues, this.m_parameterInputStreamData);
    ArrayList localArrayList2 = new ArrayList(1);
    localArrayList2.add(localArrayList1);
    return localArrayList2;
  }
  
  private ArrayList<ParameterInputValue> getInputParameterValues(Map<Integer, DataWrapper> paramMap, Map<Integer, AbstractParameterStream> paramMap1)
    throws SQLException
  {
    ArrayList localArrayList = new ArrayList(this.m_numParameters);
    for (int i = 0; i < this.m_numParameters; i++) {
      if ((ParameterType.OUTPUT != this.m_parameterTypes.get(i)) && (ParameterType.RETURN_VALUE != this.m_parameterTypes.get(i)))
      {
        Integer localInteger = Integer.valueOf(i + 1);
        Object localObject1;
        Object localObject2;
        if (paramMap.containsKey(localInteger))
        {
          localObject1 = (DataWrapper)paramMap.get(localInteger);
          localObject2 = createConvertedParameterInputValue((DataWrapper)localObject1, (ParameterMetadata)this.m_parameterMetadata.get(i));
          localArrayList.add(localObject2);
        }
        else
        {
          localObject1 = (AbstractParameterStream)paramMap1.get(localInteger);
          localObject2 = ((AbstractParameterStream)localObject1).getMetadata();
          if (null == localObject2) {
            localObject2 = extractParameterMetadataForStream(i);
          }
          localArrayList.add(new ParameterInputValue((ParameterMetadata)localObject2, true, null));
        }
      }
    }
    return localArrayList;
  }
  
  private void pushBatchParameterStreams()
    throws SQLException
  {
    try
    {
      int i = this.m_batchParameterInputStreamData.size();
      for (int j = 0; j < i; j++) {
        pushParameterStreams(j + 1, (Map)this.m_batchParameterInputStreamData.get(j));
      }
      this.m_queryExecutor.finalizePushedParamData();
      this.m_currExecutionStreamSet.clear();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private void pushClearSingleBatchSetStreams()
    throws SQLException
  {
    try
    {
      Iterator localIterator = this.m_parameterInputStreamData.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        AbstractParameterStream localAbstractParameterStream = (AbstractParameterStream)localEntry.getValue();
        int i = ((Integer)localEntry.getKey()).intValue();
        if (!this.m_currExecutionStreamSet.add(localAbstractParameterStream.getEqualityStreamWrapper()))
        {
          this.m_currExecutionStreamSet.clear();
          throw new InputOutputException(1, JDBCMessageKey.STREAM_REUSED.name());
        }
        if (null == localAbstractParameterStream.getMetadata())
        {
          ParameterMetadata localParameterMetadata = extractParameterMetadataForStream(i - 1);
          localAbstractParameterStream.setParameterMetadata(localParameterMetadata);
        }
        while (localAbstractParameterStream.hasMoreData()) {
          this.m_queryExecutor.pushParamData(this.m_nextParamSetForStream, localAbstractParameterStream.getNextValue());
        }
        this.m_lastStreamsExecuted.remove(localAbstractParameterStream.getEqualityStreamWrapper());
        localAbstractParameterStream.close();
      }
      this.m_parameterInputStreamData.clear();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
    finally
    {
      this.m_nextParamSetForStream += 1;
    }
  }
  
  private void pushParameterStreams()
    throws SQLException
  {
    try
    {
      pushParameterStreams(1, this.m_parameterInputStreamData);
      this.m_queryExecutor.finalizePushedParamData();
      this.m_currExecutionStreamSet.clear();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private void pushParameterStreams(int paramInt, Map<Integer, AbstractParameterStream> paramMap)
    throws ErrorException, IOException, BadDefaultParamException, SQLException
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      AbstractParameterStream localAbstractParameterStream = (AbstractParameterStream)localEntry.getValue();
      int i = ((Integer)localEntry.getKey()).intValue();
      if (!this.m_currExecutionStreamSet.add(localAbstractParameterStream.getEqualityStreamWrapper()))
      {
        this.m_currExecutionStreamSet.clear();
        throw new InputOutputException(1, JDBCMessageKey.STREAM_REUSED.name());
      }
      if (null == localAbstractParameterStream.getMetadata())
      {
        int j = ((ParameterMetadata)this.m_parameterMetadata.get(i - 1)).getTypeMetadata().getType();
        TypeMetadata localTypeMetadata = (TypeMetadata)this.m_setMetadata.get(Integer.valueOf(i - 1));
        int k;
        if (null != localTypeMetadata) {
          k = localTypeMetadata.getType();
        } else {
          k = 2000;
        }
        if (!TypeConverter.canConvertStreamTo(k, j)) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_PARAM_SET_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(i) });
        }
        localAbstractParameterStream.setParameterMetadata((ParameterMetadata)this.m_parameterMetadata.get(i - 1));
      }
      this.m_lastStreamsExecuted.add(localAbstractParameterStream.getEqualityStreamWrapper());
      while (localAbstractParameterStream.hasMoreData()) {
        this.m_queryExecutor.pushParamData(paramInt, localAbstractParameterStream.getNextValue());
      }
    }
  }
  
  private void updateParameterMetadata()
    throws ErrorException
  {
    if (!this.m_isSetMetadataFinal)
    {
      this.m_openParamMetaData = null;
      HashMap localHashMap = new HashMap();
      Iterator localIterator = this.m_setMetadata.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localHashMap.put(localEntry.getKey(), TypeMetadata.copyOf((TypeMetadata)localEntry.getValue()));
      }
      this.m_queryExecutor.pushMappedParamTypes(localHashMap);
      this.m_parameterMetadata = this.m_queryExecutor.getMetadataForParameters();
      assert (this.m_numParameters == this.m_parameterMetadata.size());
      this.m_isSetMetadataFinal = true;
    }
  }
  
  private boolean stringRepresentationsCompatible(int paramInt1, int paramInt2)
  {
    Set localSet = (Set)s_stringRepCompatibilityLookup.get(Integer.valueOf(paramInt1));
    if (null == localSet) {
      return false;
    }
    return localSet.contains(Integer.valueOf(paramInt2));
  }
  
  private TypeMetadata createExactNumMetadata(BigDecimal paramBigDecimal, int paramInt)
    throws ErrorException
  {
    if ((2 != paramInt) && (3 != paramInt)) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.DATA_TYPE_UNSUPPORTED_NUM.name(), String.valueOf(paramInt), ExceptionType.DATA);
    }
    if (null == paramBigDecimal) {
      return TypeMetadata.createTypeMetadata(paramInt, true);
    }
    ConversionResult localConversionResult = new ConversionResult();
    Pair localPair = ExactNumConverter.calculateSQLPrecisionScale(paramBigDecimal, localConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS != localConversionResult.getState()) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NUMERIC_OVERFLOW.name(), String.valueOf(paramInt), ExceptionType.DATA);
    }
    return TypeMetadata.createTypeMetadata(paramInt, ((Short)localPair.key()).shortValue(), ((Short)localPair.value()).shortValue(), ((Short)localPair.key()).shortValue(), true);
  }
  
  private TypeMetadata createMetadataForSqlType(int paramInt)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata;
    if (TypeUtilities.isIntegerType(paramInt)) {
      localTypeMetadata = TypeMetadata.createTypeMetadata(paramInt, true);
    } else if (92 == paramInt) {
      localTypeMetadata = createTimeMetadata();
    } else if (93 == paramInt) {
      localTypeMetadata = createTimestampMetadata();
    } else {
      localTypeMetadata = TypeMetadata.createTypeMetadata(paramInt);
    }
    return localTypeMetadata;
  }
  
  private TypeMetadata createTimestampMetadata()
    throws ErrorException
  {
    return TypeMetadata.createTypeMetadata(93, (short)9, (short)0, 0, false);
  }
  
  private TypeMetadata createTimeMetadata()
    throws ErrorException
  {
    return TypeMetadata.createTypeMetadata(92, (short)3, (short)0, 0, false);
  }
  
  private static Map<Integer, Set<Integer>> initializeStringRepCompatibilityLookup()
  {
    List localList = Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) });
    HashMap localHashMap = new HashMap();
    Iterator localIterator1 = TypeUtilities.getSupportedSqlTypes().iterator();
    while (localIterator1.hasNext())
    {
      Integer localInteger1 = (Integer)localIterator1.next();
      HashSet localHashSet = new HashSet(localList);
      localHashSet.add(localInteger1);
      Iterator localIterator2 = TypeUtilities.getSupportedSqlTypes().iterator();
      while (localIterator2.hasNext())
      {
        Integer localInteger2 = (Integer)localIterator2.next();
        if (((TypeUtilities.isBinaryType(localInteger1.intValue())) && (TypeUtilities.isBinaryType(localInteger2.intValue()))) || ((TypeUtilities.isIntegerType(localInteger1.intValue())) && ((TypeUtilities.isIntegerType(localInteger2.intValue())) || (TypeUtilities.isNumberType(localInteger2.intValue())))) || (((!TypeUtilities.isApproximateNumericType(localInteger1.intValue())) && (!TypeUtilities.isExactNumericType(localInteger1.intValue()))) || ((TypeUtilities.isApproximateNumericType(localInteger2.intValue())) || (TypeUtilities.isExactNumericType(localInteger2.intValue())) || (((-7 == localInteger1.intValue()) || (16 == localInteger1.intValue())) && ((-7 == localInteger2.intValue()) || (16 == localInteger2.intValue())))))) {
          localHashSet.add(localInteger2);
        }
      }
      localHashMap.put(localInteger1, localHashSet);
    }
    return localHashMap;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SPreparedStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */