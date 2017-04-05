package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETInsertFn
  extends ETScalarFn
{
  private final long m_columnLength;
  
  public ETInsertFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (4 == paramList.size());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (((IColumn)paramList1.get(1)).getTypeMetadata().isIntegerType());
    assert (((IColumn)paramList1.get(2)).getTypeMetadata().isIntegerType());
    assert (((IColumn)paramList1.get(3)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    this.m_columnLength = paramIColumn.getColumnLength();
  }
  
  public String getLogString()
  {
    return "ETInsertFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0);
    ISqlDataWrapper localISqlDataWrapper2;
    ISqlDataWrapper localISqlDataWrapper3;
    ISqlDataWrapper localISqlDataWrapper4;
    if ((localISqlDataWrapper1.isNull()) || ((localISqlDataWrapper2 = getArgumentData(1)).isNull()) || ((localISqlDataWrapper3 = getArgumentData(2)).isNull()) || ((localISqlDataWrapper4 = getArgumentData(3)).isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str1 = localISqlDataWrapper1.getChar();
    long l1 = localISqlDataWrapper2.getInteger();
    long l2 = localISqlDataWrapper3.getInteger();
    String str2 = localISqlDataWrapper4.getChar();
    StringBuilder localStringBuilder = new StringBuilder(str1.length() + Math.min(str2.length(), Integer.MAX_VALUE - str1.length()));
    int i = str1.length();
    if (l1 <= i) {
      i = (int)Math.max(l1, 1L) - 1;
    }
    localStringBuilder.append(str1, 0, i);
    localStringBuilder.append(str2);
    if (l2 > 0L)
    {
      int j = (int)Math.min(str1.length() - i, l2);
      i += j;
    }
    localStringBuilder.append(str1, i, str1.length());
    ISqlDataWrapper localISqlDataWrapper5 = paramETDataRequest.getData();
    localISqlDataWrapper5.setChar(localStringBuilder.toString());
    return DataRetrievalUtil.retrieveCharData(localISqlDataWrapper5, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize(), this.m_columnLength, getWarningListener());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETInsertFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */