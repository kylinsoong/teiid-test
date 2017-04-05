package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETLeftFn
  extends ETScalarFn
{
  private final long m_columnLength;
  
  public ETLeftFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (2 == paramList.size());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (((IColumn)paramList1.get(1)).getTypeMetadata().isIntegerType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    this.m_columnLength = paramIColumn.getColumnLength();
  }
  
  public String getLogString()
  {
    return "ETLeftFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(1);
    if (localISqlDataWrapper1.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    long l1 = paramETDataRequest.getOffset();
    long l2 = paramETDataRequest.getMaxSize();
    int i = -1L == l2 ? 1 : 0;
    long l3 = localISqlDataWrapper1.getInteger();
    long l5;
    long l4;
    if ((0L >= l3) || (l1 / 2L >= l3))
    {
      l4 = l5 = 0L;
    }
    else
    {
      l4 = Math.min(this.m_columnLength, l3 - l1 / 2L);
      l5 = 2L * l4;
      if ((i == 0) && (l2 < l5)) {
        l5 = l2;
      }
    }
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(0, l1, l5);
    if (localISqlDataWrapper2.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str = localISqlDataWrapper2.getChar();
    paramETDataRequest.getData().setChar(str);
    assert (str.length() <= l4) : "Incorrect length";
    return (i == 0) && (l5 < 2L * l4) && (hasMoreData(0));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETLeftFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */