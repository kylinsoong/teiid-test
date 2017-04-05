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
import java.util.List;

public class ETConcatFn
  extends ETScalarFn
{
  private final long m_columnLength;
  
  public ETConcatFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (2 == getNumChildren());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (((IColumn)paramList1.get(1)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    this.m_columnLength = paramIColumn.getColumnLength();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    long l1 = paramETDataRequest.getOffset();
    long l2 = paramETDataRequest.getMaxSize();
    int i = -1L == l2 ? 1 : 0;
    long l3 = this.m_columnLength * 2L;
    if ((i == 0) && (l3 - l2 > l1)) {
      l3 = l2 + l1;
    }
    assert (l3 >= 0L) : "Overflow.";
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0, 0L, l3);
    if (localISqlDataWrapper1.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str1 = localISqlDataWrapper1.getChar();
    long l4 = l1 / 2L;
    long l5 = str1.length();
    long l6;
    if (l4 >= l5)
    {
      str1 = "";
      l6 = 2L * (l4 - l5);
    }
    else
    {
      str1 = str1.substring((int)l4);
      l6 = 0L;
      l3 -= l5 * 2L;
    }
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(1, l6, l3);
    if (localISqlDataWrapper2.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str2 = 0 == str1.length() ? localISqlDataWrapper2.getChar() : str1.concat(localISqlDataWrapper2.getChar());
    paramETDataRequest.getData().setChar(str2);
    if (((0L == l3) && (hasMoreData(0))) || (hasMoreData(1)))
    {
      if (this.m_columnLength - str2.length() > l4) {
        return true;
      }
      getWarningListener().postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETConcatFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */