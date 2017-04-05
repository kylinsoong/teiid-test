package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.dataengine.interfaces.IErrorResult;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.interfaces.IRowCountResult;

public final class ExecutionResult
{
  private ExecutionResultType m_type;
  private Object m_result;
  
  public ExecutionResult(IErrorResult paramIErrorResult, boolean paramBoolean)
  {
    if (paramBoolean) {
      this.m_type = ExecutionResultType.ERROR_RESULT_SET;
    } else {
      this.m_type = ExecutionResultType.ERROR_ROW_COUNT;
    }
    this.m_result = paramIErrorResult;
  }
  
  public ExecutionResult(IResultSet paramIResultSet)
  {
    this.m_type = ExecutionResultType.RESULT_SET;
    this.m_result = paramIResultSet;
  }
  
  public ExecutionResult(IRowCountResult paramIRowCountResult)
  {
    this.m_type = ExecutionResultType.ROW_COUNT;
    this.m_result = paramIRowCountResult;
  }
  
  public Object getResult()
  {
    return this.m_result;
  }
  
  public ExecutionResultType getType()
  {
    return this.m_type;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ExecutionResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */