package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETCotFn
  extends ETScalarFn
{
  public ETCotFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    assert (8 == ((IColumn)paramList1.get(0)).getTypeMetadata().getType());
    assert (8 == paramIColumn.getTypeMetadata().getType());
  }
  
  public String getLogString()
  {
    return "ETCotFn";
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
      double d = Math.tan(localISqlDataWrapper.getDouble());
      if (0.0D == d) {
        throw SQLEngineExceptionFactory.divByZeroException();
      }
      paramETDataRequest.getData().setDouble(1.0D / d);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETCotFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */