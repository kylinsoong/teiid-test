package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;
import java.util.Random;

public final class ETRandFn
  extends ETScalarFn
{
  public ETRandFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    assert (paramIColumn.getTypeMetadata().getType() == 8);
  }
  
  public String getLogString()
  {
    return "ETRandFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull()) {
      paramETDataRequest.getData().setNull();
    } else {
      paramETDataRequest.getData().setDouble(new Random(localISqlDataWrapper.getInteger()).nextDouble());
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETRandFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */