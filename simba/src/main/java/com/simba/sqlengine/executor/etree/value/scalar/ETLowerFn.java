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

public class ETLowerFn
  extends ETScalarFn
{
  private final long m_columnLength;
  
  public ETLowerFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == getNumChildren());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    this.m_columnLength = paramIColumn.getColumnLength();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str = localISqlDataWrapper.getChar();
    long l = this.m_columnLength - paramETDataRequest.getOffset() / 2L;
    boolean bool;
    if (str.length() > l)
    {
      bool = false;
      str = str.substring(0, (int)l);
      getWarningListener().postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
    }
    else
    {
      bool = hasMoreData(0);
    }
    paramETDataRequest.getData().setChar(str.toLowerCase());
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETLowerFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */