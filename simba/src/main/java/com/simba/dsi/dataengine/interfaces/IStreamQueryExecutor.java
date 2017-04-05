package com.simba.dsi.dataengine.interfaces;

import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.exceptions.BadDefaultParamException;
import com.simba.dsi.exceptions.ExecutingException;
import com.simba.dsi.exceptions.OperationCanceledException;
import com.simba.dsi.exceptions.ParsingException;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public abstract interface IStreamQueryExecutor
  extends IQueryExecutor
{
  public abstract void startParamSet(List<ParameterInputValue> paramList, IWarningListener paramIWarningListener)
    throws BadDefaultParamException, ParsingException, OperationCanceledException, ErrorException;
  
  public abstract void finalizeParamSet(IWarningListener paramIWarningListener)
    throws BadDefaultParamException, ParsingException, OperationCanceledException, ErrorException;
  
  public abstract void execute(IWarningListener paramIWarningListener)
    throws BadDefaultParamException, ParsingException, ExecutingException, OperationCanceledException, ErrorException;
  
  public abstract void clearBatch();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IStreamQueryExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */