package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETLocateFn
  extends ETScalarFn
{
  private static final int NEEDLE_ARG = 0;
  private static final int HAYSTACK_ARG = 1;
  private static final int START_ARG = 2;
  private final boolean m_hasStartArgument;
  
  public ETLocateFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((2 == paramList.size()) || (3 == paramList.size()));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (((IColumn)paramList1.get(1)).getTypeMetadata().isCharacterType());
    this.m_hasStartArgument = (3 == paramList.size());
    assert ((!this.m_hasStartArgument) || (((IColumn)paramList1.get(2)).getTypeMetadata().isIntegerType()));
    assert (paramIColumn.getTypeMetadata().isIntegerType());
  }
  
  public String getLogString()
  {
    return "ETLocateFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    long l1 = 0L;
    if (this.m_hasStartArgument)
    {
      localISqlDataWrapper1 = getArgumentData(2);
      if (localISqlDataWrapper1.isNull())
      {
        paramETDataRequest.getData().setNull();
        return false;
      }
      long l2 = localISqlDataWrapper1.getInteger();
      if (1L < l2) {
        l1 = l2 - 1L;
      }
    }
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(1);
    assert (!hasMoreData(1)) : "Failed to fetch all data.";
    if (localISqlDataWrapper1.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str1 = localISqlDataWrapper1.getChar();
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(0, 0L, str1.length() * 2L);
    if (localISqlDataWrapper2.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str2 = localISqlDataWrapper2.getChar();
    if ((0 < str2.length()) && (!hasMoreData(0)))
    {
      int i = str1.indexOf(str2, (int)Math.min(l1, str1.length()));
      if (0 <= i)
      {
        paramETDataRequest.getData().setInteger(i + 1);
        return false;
      }
    }
    paramETDataRequest.getData().setInteger(0L);
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETLocateFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */