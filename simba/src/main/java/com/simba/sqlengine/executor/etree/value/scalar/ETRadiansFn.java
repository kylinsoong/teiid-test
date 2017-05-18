package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETRadiansFn
  extends ETScalarFn
{
  private static final double DEGREES_TO_RAD_RATIO = 0.017453292519943295D;
  
  public ETRadiansFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().getType() == 8);
    assert (paramIColumn.getTypeMetadata().getType() == 8);
  }
  
  public String getLogString()
  {
    return "ETRadiansFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
    }
    else
    {
      double d = localISqlDataWrapper.getDouble() * 0.017453292519943295D;
      paramETDataRequest.getData().setDouble(d);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETRadiansFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */