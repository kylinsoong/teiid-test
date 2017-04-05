package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public class ETRepeatFn
  extends ETScalarFn
{
  public ETRepeatFn(IColumn paramIColumn, ArrayList<ETValueExpr> paramArrayList, List<IColumn> paramList)
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
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("REPEAT", 1);
    }
    if ((localISqlDataWrapper1.isNull()) || (localISqlDataWrapper2.isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str = localISqlDataWrapper1.getChar();
    long l1 = localISqlDataWrapper2.getInteger();
    if ((0 == str.length()) || (0L >= l1))
    {
      paramETDataRequest.getData().setChar("");
      return false;
    }
    long l2 = l1 * str.length();
    long l3 = paramETDataRequest.getOffset() / 2L;
    long l4 = l3 / str.length();
    if (l2 / str.length() != l1) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("REPEAT", 2);
    }
    int i = (int)(l3 % str.length());
    long l5 = paramETDataRequest.getMaxSize() == -1L ? 2147483647L : paramETDataRequest.getMaxSize() / 2L;
    int j = (int)Math.min(Math.min(l5, 2147483647L), l2 - l3);
    StringBuilder localStringBuilder = new StringBuilder(j);
    localStringBuilder.append(str.substring(i));
    l4 += 1L;
    if (localStringBuilder.length() > j)
    {
      localStringBuilder.setLength(j);
      paramETDataRequest.getData().setChar(localStringBuilder.toString());
      return true;
    }
    int k = (j - localStringBuilder.length()) % str.length();
    int m = j - k;
    while (localStringBuilder.length() < m)
    {
      assert (l4 < l1);
      localStringBuilder.append(str);
      l4 += 1L;
    }
    if (0 < k) {
      localStringBuilder.append(str, 0, k);
    }
    paramETDataRequest.getData().setChar(localStringBuilder.toString());
    return l4 < l1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETRepeatFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */