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

public class ETRightFn
  extends ETScalarFn
{
  public ETRightFn(IColumn paramIColumn, ArrayList<ETValueExpr> paramArrayList, List<IColumn> paramList)
    throws ErrorException
  {
    super(paramIColumn, paramArrayList, paramList);
    assert (((IColumn)paramList.get(0)).getTypeMetadata().isCharacterType());
    assert (4 == ((IColumn)paramList.get(1)).getTypeMetadata().getType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0);
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(1);
    if (hasMoreData(0)) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("RIGHT", 1);
    }
    if ((localISqlDataWrapper1.isNull()) || (localISqlDataWrapper2.isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str1 = localISqlDataWrapper1.getChar();
    long l = localISqlDataWrapper2.getInteger();
    if (l > str1.length()) {
      l = str1.length();
    } else if (0L > l) {
      l = 0L;
    }
    String str2 = str1.substring((int)(str1.length() - l));
    paramETDataRequest.getData().setChar(str2);
    return DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETRightFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */