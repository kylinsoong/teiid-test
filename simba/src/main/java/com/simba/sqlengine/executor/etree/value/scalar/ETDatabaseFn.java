package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETDatabaseFn
  extends ETScalarFn
{
  public ETDatabaseFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == getNumChildren());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
    paramETDataRequest.getData().setChar(localISqlDataWrapper.getChar());
    return hasMoreData(0);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETDatabaseFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */