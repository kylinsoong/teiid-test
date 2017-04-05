package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public class ETReplaceFn
  extends ETScalarFn
{
  public ETReplaceFn(IColumn paramIColumn, ArrayList<ETValueExpr> paramArrayList, List<IColumn> paramList)
    throws ErrorException
  {
    super(paramIColumn, paramArrayList, paramList);
    assert (((IColumn)paramList.get(0)).getTypeMetadata().isCharacterType());
    assert (((IColumn)paramList.get(1)).getTypeMetadata().isCharacterType());
    assert (((IColumn)paramList.get(2)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0);
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(1);
    ISqlDataWrapper localISqlDataWrapper3 = getArgumentData(2);
    if ((hasMoreData(0)) || (hasMoreData(1)) || (hasMoreData(2)))
    {
      int i = hasMoreData(0) ? 1 : 2;
      i = (i != 1) && (!hasMoreData(1)) ? 3 : i;
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("REPLACE", i);
    }
    if ((localISqlDataWrapper1.isNull()) || (localISqlDataWrapper2.isNull()) || (localISqlDataWrapper3.isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str1 = localISqlDataWrapper1.getChar();
    String str2 = localISqlDataWrapper2.getChar();
    String str3;
    if ("".equals(str2)) {
      str3 = str1;
    } else {
      str3 = str1.replace(str2, localISqlDataWrapper3.getChar());
    }
    paramETDataRequest.getData().setChar(str3);
    return DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETReplaceFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */