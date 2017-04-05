package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ErrorException;
import java.util.Arrays;
import java.util.List;

public class ETSpaceFn
  extends ETScalarFn
{
  private final long m_columnLength;
  
  public ETSpaceFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (4 == ((IColumn)paramList1.get(0)).getTypeMetadata().getType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    this.m_columnLength = paramIColumn.getColumnLength();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    long l1 = paramETDataRequest.getOffset() / 2L;
    long l2 = paramETDataRequest.getMaxSize();
    long l3 = localISqlDataWrapper.getInteger();
    l3 = 0L > l3 ? 0L : l3 - l1;
    long l4 = this.m_columnLength - l1;
    int i = 0;
    int j = (int)Math.min(2147483647L, l3);
    if (-1L != l2) {
      j = (int)Math.min(j, l2 / 2L);
    }
    if (l4 < j)
    {
      j = (int)l4;
      i = 1;
    }
    char[] arrayOfChar = new char[j];
    Arrays.fill(arrayOfChar, ' ');
    paramETDataRequest.getData().setChar(String.valueOf(arrayOfChar));
    if (i != 0)
    {
      getWarningListener().postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
      return false;
    }
    return j < l3;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETSpaceFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */