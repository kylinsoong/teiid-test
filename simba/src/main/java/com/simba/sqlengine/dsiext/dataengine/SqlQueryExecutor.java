package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.impl.DSISimpleRowCountResult;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.ExecutionContext;
import com.simba.dsi.dataengine.utilities.ExecutionContexts;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.ParameterType;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.BadDefaultParamException;
import com.simba.dsi.exceptions.DefaultParamException;
import com.simba.dsi.exceptions.ExecutingException;
import com.simba.dsi.exceptions.InvalidArgumentException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.exceptions.OperationCanceledException;
import com.simba.dsi.exceptions.ParamAlreadyPushedException;
import com.simba.dsi.exceptions.ParsingException;
import com.simba.sqlengine.aeprocessor.aeoptimizer.AEPassdownOpOptimizer;
import com.simba.sqlengine.aeprocessor.aeoptimizer.AETreeOptimizer;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEQuery;
import com.simba.sqlengine.aeprocessor.aetree.statement.AERowCountStatement;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEStatements;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.exceptions.SQLEngineRuntimeException;
import com.simba.sqlengine.executor.IStatementExecutor;
import com.simba.sqlengine.executor.materializer.ETStatementMaterializer;
import com.simba.sqlengine.executor.queryplan.ETQueryPlan;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SqlQueryExecutor
  implements IQueryExecutor
{
  protected AEStatements m_aeStatements;
  protected ILogger m_log;
  private SqlQueryExecutorContext m_context;
  private ExecutionResults m_results;
  private Map<Integer, Map<Integer, ArrayList<ParameterInputValue>>> m_unprocessedPushedValues;
  private Map<Integer, ParameterInputValue[]> m_pushedValues;
  private ArrayList<ParameterMetadata> m_parameterMetadata = null;
  private SqlDataEngineContext m_dataEngineContext;
  private DSIExtOperationHandlerFactory m_opHandlerFactory;
  private IStatementExecutor m_executor = null;
  private Object m_cancelLock = new Object();
  
  public SqlQueryExecutor(AEStatements paramAEStatements, SqlDataEngineContext paramSqlDataEngineContext, ILogger paramILogger)
    throws ErrorException
  {
    this.m_log = paramILogger;
    this.m_context = new SqlQueryExecutorContext(paramSqlDataEngineContext, this);
    this.m_unprocessedPushedValues = new HashMap();
    this.m_dataEngineContext = paramSqlDataEngineContext;
    this.m_aeStatements = new AEStatements();
    AETreeOptimizer localAETreeOptimizer = new AETreeOptimizer(this.m_context);
    Iterator localIterator = paramAEStatements.getStatementItr();
    assert (localIterator.hasNext());
    while (localIterator.hasNext())
    {
      IAEStatement localIAEStatement = (IAEStatement)localIterator.next();
      localAETreeOptimizer.optimize(localIAEStatement);
      this.m_aeStatements.addStatement(localIAEStatement);
    }
    prepareResults();
    this.m_opHandlerFactory = this.m_dataEngineContext.getDataEngine().createOperationHandlerFactory();
  }
  
  public void prepareResults()
    throws ErrorException
  {
    this.m_results = createMetadataResults(this.m_aeStatements);
  }
  
  public void cancelExecute()
    throws ErrorException
  {
    synchronized (this.m_cancelLock)
    {
      this.m_context.setIsCanceled(true);
      if (this.m_executor != null) {
        this.m_executor.cancelExecution();
      }
    }
  }
  
  public void clearCancel()
  {
    synchronized (this.m_cancelLock)
    {
      this.m_context.setIsCanceled(false);
    }
  }
  
  public void close() {}
  
  public void execute(ExecutionContexts paramExecutionContexts, IWarningListener paramIWarningListener)
    throws BadDefaultParamException, ParsingException, ExecutingException, OperationCanceledException, ErrorException
  {
    try
    {
      if ((this.m_aeStatements.size() > 1) && (paramExecutionContexts.getCount() != 0)) {
        throw new IllegalStateException("Statement and execution context mismatch.");
      }
      this.m_results = new ExecutionResults();
      this.m_context.setWarningListener(paramIWarningListener);
      Iterator localIterator = this.m_aeStatements.getStatementItr();
      while (localIterator.hasNext())
      {
        IAEStatement localIAEStatement = (IAEStatement)localIterator.next();
        if (((localIAEStatement instanceof AEQuery)) && (paramExecutionContexts.getCount() != 1)) {
          throw SQLEngineExceptionFactory.featureNotImplementedException("Query with batch parameter");
        }
        if (this.m_opHandlerFactory != null)
        {
          localIAEStatement = (IAEStatement)localIAEStatement.copy();
          if (paramExecutionContexts.getCount() == 1) {
            pushParameterToAETree((ExecutionContext)paramExecutionContexts.contextIterator().next(), localIAEStatement);
          }
          this.m_opHandlerFactory.setParameterSetCount(paramExecutionContexts.getCount());
        }
        ETQueryPlan localETQueryPlan = new ETQueryPlan(doPassdownOptimize(localIAEStatement));
        synchronized (this.m_cancelLock)
        {
          if (this.m_context.isCanceled()) {
            throw SQLEngineExceptionFactory.operationCanceledException();
          }
          this.m_executor = new ETStatementMaterializer(this.m_context.getDataEngineContext().getDataEngine().createSqlConverterGenerator(), paramIWarningListener, this.m_dataEngineContext, this.m_log).materialize(localETQueryPlan);
        }
        try
        {
          this.m_executor.registerWarningListener(paramIWarningListener);
          this.m_executor.startBatch();
          ??? = paramExecutionContexts.contextIterator();
          for (int i = 1; ((Iterator)???).hasNext(); i++)
          {
            ExecutionContext localExecutionContext = (ExecutionContext)((Iterator)???).next();
            if (this.m_pushedValues != null)
            {
              localObject2 = (ParameterInputValue[])this.m_pushedValues.get(Integer.valueOf(i));
              if (localObject2 != null) {
                for (int j = 1; j < localObject2.length; j++) {
                  if (localObject2[j] != null) {
                    localExecutionContext.setInputParam(j - 1, localObject2[j]);
                  }
                }
              }
            }
            Object localObject2 = this.m_executor.execute(localExecutionContext);
            this.m_results.addExecutionResult((ExecutionResult)localObject2);
          }
          this.m_executor.endBatch();
        }
        finally
        {
          this.m_executor.close();
          synchronized (this.m_cancelLock)
          {
            this.m_executor = null;
          }
        }
      }
    }
    catch (SQLEngineRuntimeException localSQLEngineRuntimeException)
    {
      throw SQLEngineExceptionFactory.convertRuntimeException(localSQLEngineRuntimeException);
    }
    finally
    {
      clearPushedParamData();
    }
  }
  
  private void pushParameterToAETree(ExecutionContext paramExecutionContext, IAEStatement paramIAEStatement)
    throws SQLEngineException, OperationCanceledException
  {
    Iterator localIterator = paramIAEStatement.getDynamicParameters().iterator();
    while (localIterator.hasNext())
    {
      AEParameter localAEParameter = (AEParameter)localIterator.next();
      ParameterInputValue localParameterInputValue = (ParameterInputValue)paramExecutionContext.getInputs().get(localAEParameter.getIndex() - 1);
      if (!localParameterInputValue.isDefaultValue()) {
        try
        {
          if (localParameterInputValue.isPushed()) {
            localAEParameter.setInputData(getPushedParam(1, localAEParameter.getIndex()).getData());
          } else {
            localAEParameter.setInputData(localParameterInputValue.getData());
          }
        }
        catch (ParamAlreadyPushedException localParamAlreadyPushedException)
        {
          throw new IllegalStateException("Pushed parameter constructed.");
        }
        catch (DefaultParamException localDefaultParamException)
        {
          if (!$assertionsDisabled) {
            throw new AssertionError();
          }
        }
      }
    }
  }
  
  public void clearPushedParamData()
    throws ErrorException
  {
    this.m_pushedValues = null;
    this.m_unprocessedPushedValues = new HashMap();
  }
  
  public void pushParamData(int paramInt, ParameterInputValue paramParameterInputValue)
    throws BadDefaultParamException, ErrorException
  {
    try
    {
      Object localObject = (Map)getUnprocessedPushedValues().get(Integer.valueOf(paramInt));
      if (null == localObject)
      {
        localObject = new HashMap();
        getUnprocessedPushedValues().put(Integer.valueOf(paramInt), localObject);
      }
      Integer localInteger = Integer.valueOf(paramParameterInputValue.getMetadata().getParameterNumber());
      ArrayList localArrayList = (ArrayList)((Map)localObject).get(localInteger);
      if (null == localArrayList)
      {
        localArrayList = new ArrayList();
        ((Map)localObject).put(localInteger, localArrayList);
      }
      localArrayList.add(paramParameterInputValue);
    }
    catch (Exception localException)
    {
      throw new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, localException.getLocalizedMessage());
    }
  }
  
  public void finalizePushedParamData()
    throws ErrorException
  {
    if ((getNumParams() == 0) || (this.m_unprocessedPushedValues.size() == 0)) {
      return;
    }
    ArrayList localArrayList1 = getMetadataForParameters();
    HashMap localHashMap = new HashMap();
    try
    {
      Iterator localIterator1 = getUnprocessedPushedValues().entrySet().iterator();
      while (localIterator1.hasNext())
      {
        Map.Entry localEntry1 = (Map.Entry)localIterator1.next();
        Map localMap = (Map)localEntry1.getValue();
        Integer localInteger = (Integer)localEntry1.getKey();
        ParameterInputValue[] arrayOfParameterInputValue = new ParameterInputValue[getNumParams() + 1];
        localHashMap.put(localInteger, arrayOfParameterInputValue);
        Iterator localIterator2 = localMap.entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry2 = (Map.Entry)localIterator2.next();
          int i = ((Integer)localEntry2.getKey()).intValue();
          ArrayList localArrayList2 = (ArrayList)localEntry2.getValue();
          if (localArrayList2.size() <= 1)
          {
            if (localArrayList2.size() == 1)
            {
              arrayOfParameterInputValue[i] = ((ParameterInputValue)localArrayList2.get(0));
              arrayOfParameterInputValue[i].setPushed(false);
            }
          }
          else
          {
            DataWrapper localDataWrapper1 = new DataWrapper();
            StringBuilder localStringBuilder = new StringBuilder(8000);
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(8000);
            boolean bool = TypeUtilities.isCharacterType(((ParameterInputValue)localArrayList2.get(0)).getMetadata().getTypeMetadata().getType());
            Iterator localIterator3 = localArrayList2.iterator();
            while (localIterator3.hasNext())
            {
              ParameterInputValue localParameterInputValue = (ParameterInputValue)localIterator3.next();
              DataWrapper localDataWrapper2 = localParameterInputValue.getData();
              Object localObject = localDataWrapper2.getObject();
              if (bool) {
                localStringBuilder.append((String)localParameterInputValue.getData().getObject());
              } else {
                localByteArrayOutputStream.write((byte[])localObject);
              }
            }
            if (bool) {
              switch (((ParameterMetadata)localArrayList1.get(i - 1)).getTypeMetadata().getType())
              {
              case 1: 
                localDataWrapper1.setChar(localStringBuilder.toString());
                break;
              case 12: 
                localDataWrapper1.setVarChar(localStringBuilder.toString());
                break;
              case -1: 
                localDataWrapper1.setLongVarChar(localStringBuilder.toString());
                break;
              case -8: 
                localDataWrapper1.setWChar(localStringBuilder.toString());
                break;
              case -9: 
                localDataWrapper1.setWVarChar(localStringBuilder.toString());
                break;
              case -10: 
                localDataWrapper1.setWLongVarChar(localStringBuilder.toString());
                break;
              default: 
                throw new IllegalStateException("Unknow type.");
              }
            } else {
              switch (((ParameterMetadata)localArrayList1.get(i - 1)).getTypeMetadata().getType())
              {
              case -2: 
                localDataWrapper1.setBinary(localByteArrayOutputStream.toByteArray());
                break;
              case -3: 
                localDataWrapper1.setVarBinary(localByteArrayOutputStream.toByteArray());
                break;
              case -4: 
                localDataWrapper1.setLongVarBinary(localByteArrayOutputStream.toByteArray());
                break;
              default: 
                throw new IllegalStateException("Unknow type.");
              }
            }
            arrayOfParameterInputValue[i] = new ParameterInputValue((ParameterMetadata)localArrayList1.get(i - 1), localDataWrapper1);
          }
        }
      }
      if (this.m_context.isCanceled()) {
        throw SQLEngineExceptionFactory.operationCanceledException();
      }
      this.m_pushedValues = localHashMap;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, localException.getLocalizedMessage());
    }
  }
  
  public SqlQueryExecutorContext getContext()
  {
    return this.m_context;
  }
  
  public ArrayList<ParameterMetadata> getMetadataForParameters()
    throws ErrorException
  {
    if (null == this.m_parameterMetadata)
    {
      ArrayList localArrayList = new ArrayList();
      List localList = ((IAEStatement)this.m_aeStatements.getStatementItr().next()).getDynamicParameters();
      if ((localList.size() != 0) && (this.m_aeStatements.size() > 1)) {
        throw new IllegalStateException("Multiple statements with parameter.");
      }
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        AEParameter localAEParameter = (AEParameter)localIterator.next();
        IColumn localIColumn = localAEParameter.getInferredOrSetColumn();
        ParameterMetadata localParameterMetadata = new ParameterMetadata(localAEParameter.getIndex(), ParameterType.INPUT, localIColumn.getTypeMetadata(), localIColumn.getColumnLength(), localIColumn.getName(), localIColumn.isCaseSensitive(), localIColumn.getNullable());
        localArrayList.add(localParameterMetadata);
      }
      this.m_parameterMetadata = localArrayList;
    }
    return this.m_parameterMetadata;
  }
  
  public ParameterInputValue getPushedParam(int paramInt1, int paramInt2)
    throws SQLEngineException, OperationCanceledException, IllegalStateException
  {
    ParameterInputValue[] arrayOfParameterInputValue = (ParameterInputValue[])getPushedParameter().get(Integer.valueOf(paramInt1));
    if (null == arrayOfParameterInputValue) {
      throw new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_PARAM_SET.name(), new String[] { String.valueOf(paramInt1) });
    }
    if ((1 > paramInt2) || (paramInt2 > arrayOfParameterInputValue.length)) {
      throw new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_PARAM_NUMBER.name(), new String[] { String.valueOf(paramInt2) });
    }
    ParameterInputValue localParameterInputValue = arrayOfParameterInputValue[paramInt2];
    assert (null != localParameterInputValue);
    return localParameterInputValue;
  }
  
  public int getNumParams()
    throws ErrorException
  {
    List localList = ((IAEStatement)this.m_aeStatements.getStatementItr().next()).getDynamicParameters();
    if ((localList.size() != 0) && (this.m_aeStatements.size() > 1)) {
      throw new IllegalStateException("Multiple statements with parameter.");
    }
    return localList.size();
  }
  
  public ExecutionResults getResults()
    throws ErrorException
  {
    return this.m_results;
  }
  
  protected IAEStatement doPassdownOptimize(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    if (this.m_opHandlerFactory != null)
    {
      AEPassdownOpOptimizer localAEPassdownOpOptimizer = new AEPassdownOpOptimizer(this.m_opHandlerFactory);
      paramIAEStatement = (IAEStatement)paramIAEStatement.copy();
      localAEPassdownOpOptimizer.optimize(paramIAEStatement);
    }
    return paramIAEStatement;
  }
  
  public void pushMappedParamTypes(Map<Integer, TypeMetadata> paramMap)
    throws ErrorException
  {
    List localList = ((IAEStatement)this.m_aeStatements.getStatementItr().next()).getDynamicParameters();
    if ((localList.size() != 0) && (this.m_aeStatements.size() > 1)) {
      throw new IllegalStateException("Multiple statements with parameter.");
    }
    if (0 < localList.size())
    {
      for (int i = 0; i < localList.size(); i++)
      {
        TypeMetadata localTypeMetadata = (TypeMetadata)paramMap.get(Integer.valueOf(i));
        if (null == localTypeMetadata)
        {
          localTypeMetadata = ((AEParameter)localList.get(i)).getInferredOrSetColumn().getTypeMetadata();
          if (0 == localTypeMetadata.getType()) {
            localTypeMetadata = TypeMetadata.createTypeMetadata(1);
          }
        }
        ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
        if ((localTypeMetadata.isCharacterType()) || (localTypeMetadata.isBinaryType())) {
          try
          {
            localColumnMetadata.setColumnLength(4294967295L);
          }
          catch (NumericOverflowException localNumericOverflowException)
          {
            throw new SQLEngineException(SQLEngineMessageKey.INVALID_ARGUMENT.name(), localNumericOverflowException);
          }
        }
        ((AEParameter)localList.get(i)).setColumn(localColumnMetadata);
      }
      this.m_aeStatements.reprocessMetadata();
      this.m_parameterMetadata = null;
    }
  }
  
  private Map<Integer, Map<Integer, ArrayList<ParameterInputValue>>> getUnprocessedPushedValues()
    throws OperationCanceledException
  {
    if (this.m_context.isCanceled()) {
      throw SQLEngineExceptionFactory.operationCanceledException();
    }
    return this.m_unprocessedPushedValues;
  }
  
  private Map<Integer, ParameterInputValue[]> getPushedParameter()
    throws OperationCanceledException, IllegalStateException
  {
    if (this.m_context.isCanceled()) {
      throw SQLEngineExceptionFactory.operationCanceledException();
    }
    if (null == this.m_pushedValues) {
      throw new IllegalStateException("Request for pushed parameter data before finalizePushedParamData() has been called.");
    }
    return this.m_pushedValues;
  }
  
  private static ExecutionResults createMetadataResults(AEStatements paramAEStatements)
    throws ErrorException
  {
    if (0 == paramAEStatements.size()) {
      throw new InvalidArgumentException(7, "statements");
    }
    if (1 != paramAEStatements.size()) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("multiple statements");
    }
    ExecutionResults localExecutionResults = new ExecutionResults();
    Iterator localIterator = paramAEStatements.getStatementItr();
    while (localIterator.hasNext())
    {
      IAEStatement localIAEStatement = (IAEStatement)localIterator.next();
      ExecutionResult localExecutionResult;
      if ((localIAEStatement instanceof AEQuery))
      {
        localExecutionResult = createMetadataResult((AEQuery)localIAEStatement);
        localExecutionResults.addExecutionResult(localExecutionResult);
      }
      else if ((localIAEStatement instanceof AERowCountStatement))
      {
        localExecutionResult = createMetadataResult((AERowCountStatement)localIAEStatement);
        localExecutionResults.addExecutionResult(localExecutionResult);
      }
      else
      {
        throw SQLEngineExceptionFactory.invalidAETreeException();
      }
    }
    return localExecutionResults;
  }
  
  private static ExecutionResult createMetadataResult(AEQuery paramAEQuery)
  {
    return new ExecutionResult(new DSIExtMetadataOnlyResultSet(paramAEQuery.createResultSetColumns()));
  }
  
  private static ExecutionResult createMetadataResult(AERowCountStatement paramAERowCountStatement)
  {
    return new ExecutionResult(new DSISimpleRowCountResult(0L));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/SqlQueryExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */