package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETPowerFn
  extends ETScalarFn
{
  public ETPowerFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((2 == paramList.size()) && (2 == paramList1.size()));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().getType() == 8);
    assert (((IColumn)paramList1.get(1)).getTypeMetadata().isIntegerType());
    assert (paramIColumn.getTypeMetadata().getType() == 8);
  }
  
  public String getLogString()
  {
    return "ETPowerFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0);
    if (localISqlDataWrapper1.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(1);
    if (localISqlDataWrapper2.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    paramETDataRequest.getData().setDouble(Math.pow(localISqlDataWrapper1.getDouble(), localISqlDataWrapper2.getInteger()));
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETPowerFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */