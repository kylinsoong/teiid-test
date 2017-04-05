package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETLengthFn
  extends ETScalarFn
{
  public ETLengthFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == paramList.size());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isIntegerType());
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
      String str = localISqlDataWrapper.getChar();
      for (int i = str.length() - 1; (i >= 0) && (' ' == str.charAt(i)); i--) {}
      paramETDataRequest.getData().setSmallInt(i + 1);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETLengthFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */