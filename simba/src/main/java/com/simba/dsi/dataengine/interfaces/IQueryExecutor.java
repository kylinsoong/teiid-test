package com.simba.dsi.dataengine.interfaces;

import com.simba.dsi.dataengine.utilities.ExecutionContexts;
import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.BadDefaultParamException;
import com.simba.dsi.exceptions.ExecutingException;
import com.simba.dsi.exceptions.OperationCanceledException;
import com.simba.dsi.exceptions.ParsingException;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Map;

public abstract interface IQueryExecutor
{
  public abstract void cancelExecute()
    throws ErrorException;
  
  public abstract void clearCancel();
  
  public abstract void clearPushedParamData()
    throws ErrorException;
  
  public abstract void close();
  
  public abstract void execute(ExecutionContexts paramExecutionContexts, IWarningListener paramIWarningListener)
    throws BadDefaultParamException, ParsingException, ExecutingException, OperationCanceledException, ErrorException;
  
  public abstract void finalizePushedParamData()
    throws ErrorException;
  
  public abstract ArrayList<ParameterMetadata> getMetadataForParameters()
    throws ErrorException;
  
  public abstract int getNumParams()
    throws ErrorException;
  
  public abstract ExecutionResults getResults()
    throws ErrorException;
  
  public abstract void pushParamData(int paramInt, ParameterInputValue paramParameterInputValue)
    throws BadDefaultParamException, ErrorException;
  
  public abstract void pushMappedParamTypes(Map<Integer, TypeMetadata> paramMap)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IQueryExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */