package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.sql.Date;
import java.util.List;

public final class ETMonthFn
  extends ETScalarFn
{
  public ETMonthFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    if ((1 != paramList.size()) || (1 != paramList1.size())) {
      throw new IllegalArgumentException("Invalid Number of arguments for MONTH scalar function.");
    }
    int i = ((IColumn)paramList1.get(0)).getTypeMetadata().getType();
    if (91 != i) {
      throw new IllegalArgumentException();
    }
  }
  
  public String getLogString()
  {
    return "ETMonthFn";
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
      int i = localISqlDataWrapper.getDate().getMonth() + 1;
      assert ((1 <= i) && (i <= 12));
      paramETDataRequest.getData().setSmallInt(i);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETMonthFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */