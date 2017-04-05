package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETCharFn
  extends ETScalarFn
{
  public ETCharFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isIntegerType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
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
      long l = localISqlDataWrapper.getInteger();
      if ((l > 255L) || (l < 0L)) {
        paramETDataRequest.getData().setNull();
      } else {
        paramETDataRequest.getData().setChar(String.valueOf((char)(int)l));
      }
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETCharFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */