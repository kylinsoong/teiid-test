package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.dataengine.interfaces.IErrorResult;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.interfaces.IRowCountResult;
import java.util.ArrayList;
import java.util.Iterator;

public class ExecutionResults
{
  private ArrayList<ExecutionResult> m_resultsList = new ArrayList();
  
  public void addErrorResult(IErrorResult paramIErrorResult, boolean paramBoolean)
  {
    this.m_resultsList.add(new ExecutionResult(paramIErrorResult, paramBoolean));
  }
  
  public void addResultSet(IResultSet paramIResultSet)
  {
    this.m_resultsList.add(new ExecutionResult(paramIResultSet));
  }
  
  public void addRowCountResult(IRowCountResult paramIRowCountResult)
  {
    this.m_resultsList.add(new ExecutionResult(paramIRowCountResult));
  }
  
  public void addExecutionResult(ExecutionResult paramExecutionResult)
  {
    this.m_resultsList.add(paramExecutionResult);
  }
  
  @Deprecated
  public ArrayList<ExecutionResult> getResults()
  {
    return this.m_resultsList;
  }
  
  public Iterator<ExecutionResult> getResultItr()
  {
    return this.m_resultsList.iterator();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ExecutionResults.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */