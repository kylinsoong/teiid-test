package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETLTrim
  extends ETScalarFn
{
  public ETLTrim(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == getNumChildren());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (hasMoreData(0)) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("LTRIM", 1);
    }
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str = localISqlDataWrapper.getChar();
    int i = 0;
    for (int j = 0; (j < str.length()) && (Character.isWhitespace(str.charAt(j))); j++) {
      i++;
    }
    str = str.substring(i);
    paramETDataRequest.getData().setChar(str);
    return DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETLTrim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */