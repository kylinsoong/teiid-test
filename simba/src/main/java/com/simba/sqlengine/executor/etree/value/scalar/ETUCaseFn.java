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

public class ETUCaseFn
  extends ETScalarFn
{
  public ETUCaseFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == getNumChildren());
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
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
    boolean bool = hasMoreData(0);
    long l1 = paramETDataRequest.getOffset() / 2L;
    long l2 = getResultMetadata().getColumnLength();
    if (l1 + str.length() > l2)
    {
      bool = false;
      int i = (int)(l2 - l1);
      str = str.substring(0, i);
      IWarningListener localIWarningListener = getWarningListener();
      localIWarningListener.postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
    }
    paramETDataRequest.getData().setChar(str.toUpperCase());
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETUCaseFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */