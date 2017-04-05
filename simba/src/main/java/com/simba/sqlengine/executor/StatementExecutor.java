package com.simba.sqlengine.executor;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.interfaces.IRowCountResult;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.ExecutionContext;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.exceptions.DefaultParamException;
import com.simba.dsi.exceptions.ParamAlreadyPushedException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETResourceManager;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.statement.ETQuery;
import com.simba.sqlengine.executor.etree.statement.IETStatement;
import com.simba.sqlengine.executor.etree.statement.RowCountStatement;
import com.simba.sqlengine.executor.etree.util.RegisterWarningListenerUtil;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StatementExecutor
  implements IStatementExecutor
{
  private IETStatement m_etStatement;
  private ETResourceManager m_rscManager;
  private final ETCancelState m_cancelState;
  private Object m_cancelLock;
  private boolean m_isCanceled;
  
  public StatementExecutor(IETStatement paramIETStatement, ETResourceManager paramETResourceManager, ETCancelState paramETCancelState)
  {
    if ((!(paramIETStatement instanceof ETQuery)) && (!(paramIETStatement instanceof RowCountStatement))) {
      throw new IllegalArgumentException("Unknown ETree type.");
    }
    this.m_etStatement = paramIETStatement;
    this.m_rscManager = paramETResourceManager;
    this.m_cancelState = paramETCancelState;
    this.m_isCanceled = false;
    this.m_cancelLock = new Object();
  }
  
  public void close()
  {
    if ((this.m_etStatement instanceof RowCountStatement))
    {
      try
      {
        ((RowCountStatement)this.m_etStatement).close();
      }
      catch (Exception localException1) {}
      this.m_etStatement = null;
      if (null != this.m_rscManager)
      {
        try
        {
          this.m_rscManager.free();
        }
        catch (Exception localException2) {}
        this.m_rscManager = null;
      }
    }
  }
  
  public void cancelExecution()
  {
    synchronized (this.m_cancelLock)
    {
      this.m_isCanceled = true;
      this.m_cancelState.cancel();
    }
  }
  
  public ExecutionResult execute(ExecutionContext paramExecutionContext)
    throws ErrorException
  {
    synchronized (this.m_cancelLock)
    {
      if (this.m_isCanceled) {
        throw SQLEngineExceptionFactory.operationCanceledException();
      }
      this.m_cancelState.clearCancel();
    }
    Object localObject2;
    ExecutionResult localExecutionResult;
    if (this.m_etStatement.isResultSet())
    {
      ??? = (ETQuery)this.m_etStatement;
      if (paramExecutionContext.getInputs().size() != 0)
      {
        if (((ETQuery)???).getOperand().isOpen()) {
          throw SQLEngineExceptionFactory.featureNotImplementedException("Multiple active open result sets is not supported.");
        }
        pushParameters(paramExecutionContext);
      }
      localObject2 = new ETResultSet((ETQuery)???, this.m_rscManager, this.m_cancelState);
      ((ETResultSet)localObject2).setCursorType(CursorType.FORWARD_ONLY);
      localExecutionResult = new ExecutionResult((IResultSet)localObject2);
    }
    else
    {
      try
      {
        pushParameters(paramExecutionContext);
        ??? = (RowCountStatement)this.m_etStatement;
        this.m_rscManager.allocate();
        ((RowCountStatement)???).execute();
        localObject2 = new ETRowCountResult();
        ((ETRowCountResult)localObject2).setRowCount(((RowCountStatement)???).getRowCount());
        localExecutionResult = new ExecutionResult((IRowCountResult)localObject2);
      }
      finally
      {
        this.m_rscManager.free();
      }
    }
    return localExecutionResult;
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    RegisterWarningListenerUtil.registerWarningListener(paramIWarningListener, this.m_etStatement);
  }
  
  private void pushParameters(ExecutionContext paramExecutionContext)
  {
    ArrayList localArrayList = paramExecutionContext.getInputs();
    Iterator localIterator = this.m_etStatement.getInputParameters().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      int i = ((Integer)localEntry.getKey()).intValue();
      if ((i < 0) || (i > localArrayList.size())) {
        throw new IllegalStateException("Parameter index in the ETree does not match execution context.");
      }
      ParameterInputValue localParameterInputValue = (ParameterInputValue)localArrayList.get(i - 1);
      try
      {
        ((ETParameter)localEntry.getValue()).setInputData(localParameterInputValue.getData());
      }
      catch (ParamAlreadyPushedException localParamAlreadyPushedException)
      {
        throw new IllegalStateException("pushed parameter encountered during execution.");
      }
      catch (DefaultParamException localDefaultParamException)
      {
        throw new IllegalStateException("Default parameter encountered during execution.");
      }
    }
  }
  
  public void startBatch()
    throws ErrorException
  {
    if ((this.m_etStatement instanceof RowCountStatement)) {
      ((RowCountStatement)this.m_etStatement).startBatch();
    }
  }
  
  public void endBatch()
    throws ErrorException
  {
    if ((this.m_etStatement instanceof RowCountStatement)) {
      ((RowCountStatement)this.m_etStatement).endBatch();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/StatementExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */