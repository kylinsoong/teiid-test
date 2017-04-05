package com.simba.dsi.dataengine.utilities;

import java.util.ArrayList;

public class ExecutionContext
{
  private ArrayList<ParameterInputValue> m_inputs;
  private ArrayList<ParameterOutputValue> m_outputs;
  private ExecutionContextStatus m_status;
  
  protected ExecutionContext(ArrayList<ParameterInputValue> paramArrayList, ArrayList<ParameterOutputValue> paramArrayList1)
  {
    this.m_inputs = paramArrayList;
    this.m_outputs = paramArrayList1;
    this.m_status = ExecutionContextStatus.NOT_EXECUTED;
  }
  
  public ExecutionContextStatus getStatus()
  {
    return this.m_status;
  }
  
  public void setStatus(ExecutionContextStatus paramExecutionContextStatus)
  {
    this.m_status = paramExecutionContextStatus;
  }
  
  public ArrayList<ParameterInputValue> getInputs()
  {
    return this.m_inputs;
  }
  
  public ArrayList<ParameterOutputValue> getOutputs()
  {
    return this.m_outputs;
  }
  
  public void setInputParam(int paramInt, ParameterInputValue paramParameterInputValue)
  {
    this.m_inputs.set(paramInt, paramParameterInputValue);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ExecutionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */