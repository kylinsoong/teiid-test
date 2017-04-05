package com.simba.sqlengine.executor;

import com.simba.dsi.dataengine.utilities.ExecutionContext;
import com.simba.dsi.dataengine.utilities.ExecutionResult;
import com.simba.support.exceptions.ErrorException;

public abstract interface IStatementExecutor
  extends IWarningSource
{
  public abstract ExecutionResult execute(ExecutionContext paramExecutionContext)
    throws ErrorException;
  
  public abstract void startBatch()
    throws ErrorException;
  
  public abstract void endBatch()
    throws ErrorException;
  
  public abstract void close();
  
  public abstract void cancelExecution();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/IStatementExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */