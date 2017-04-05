package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETATan2Fn
  extends ETScalarFn
{
  public ETATan2Fn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((2 == paramList.size()) && (2 == paramList1.size()));
    assert (8 == ((IColumn)paramList1.get(0)).getTypeMetadata().getType());
    assert (8 == ((IColumn)paramList1.get(1)).getTypeMetadata().getType());
    assert (8 == paramIColumn.getTypeMetadata().getType());
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0);
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(1);
    if ((localISqlDataWrapper1.isNull()) || (localISqlDataWrapper2.isNull()))
    {
      paramETDataRequest.getData().setNull();
    }
    else
    {
      double d1 = localISqlDataWrapper1.getDouble();
      double d2 = localISqlDataWrapper2.getDouble();
      paramETDataRequest.getData().setDouble(Math.atan2(d1, d2));
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETATan2Fn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */