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

public final class ETSubstringFn
  extends ETScalarFn
{
  private final long m_columnLength;
  private final boolean m_hasLengthArg;
  
  public ETSubstringFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((2 == getNumChildren()) || (3 == getNumChildren()));
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    this.m_columnLength = paramIColumn.getColumnLength();
    this.m_hasLengthArg = (2 < getNumChildren());
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    long l1 = paramETDataRequest.getOffset() / 2L;
    long l2 = paramETDataRequest.getMaxSize();
    long l3 = this.m_columnLength - l1;
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(1);
    if (localISqlDataWrapper1.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    long l4 = Math.max(localISqlDataWrapper1.getInteger(), 1L) - 1L + l1;
    long l5 = (l4 + this.m_columnLength) * 2L;
    long l6 = 0L;
    if (this.m_hasLengthArg)
    {
      localISqlDataWrapper2 = getArgumentData(2);
      if (localISqlDataWrapper2.isNull())
      {
        paramETDataRequest.getData().setNull();
        return false;
      }
      l6 = Math.max(localISqlDataWrapper2.getInteger(), 0L) - l1;
      if (this.m_columnLength > l6) {
        l5 = (l4 + l6) * 2L;
      }
    }
    if ((-1L != l2) && (l5 - 2L * l4 > l2)) {
      l5 = 2L * l4 + l2;
    }
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(0, 0L, l5);
    if (localISqlDataWrapper2.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str = localISqlDataWrapper2.getChar();
    l4 = Math.min(l4, str.length());
    str = str.substring((int)l4);
    paramETDataRequest.getData().setChar(str);
    if (hasMoreData(0)) {
      if ((str.length() >= l3) && ((!this.m_hasLengthArg) || (l3 < l6))) {
        getWarningListener().postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
      } else if ((!this.m_hasLengthArg) || (str.length() < l6)) {
        return true;
      }
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETSubstringFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */