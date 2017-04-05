package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETAsciiFn
  extends ETScalarFn
{
  public ETAsciiFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (4 == paramIColumn.getTypeMetadata().getType());
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
      if (0 == str.length())
      {
        paramETDataRequest.getData().setNull();
      }
      else
      {
        int i = str.charAt(0);
        if ((i < 0) || (i > 255)) {
          paramETDataRequest.getData().setNull();
        } else {
          paramETDataRequest.getData().setInteger(i);
        }
      }
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETAsciiFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */