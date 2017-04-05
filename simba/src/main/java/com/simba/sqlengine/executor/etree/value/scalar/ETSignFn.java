package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class ETSignFn
  extends ETScalarFn
{
  private final int m_argType;
  
  public ETSignFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    this.m_argType = ((IColumn)paramList1.get(0)).getTypeMetadata().getType();
    assert (TypeUtilities.isNumberType(this.m_argType));
    assert (TypeUtilities.isIntegerType(paramIColumn.getTypeMetadata().getType()));
  }
  
  public String getLogString()
  {
    return "ETSignFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
    }
    else
    {
      int i;
      switch (this.m_argType)
      {
      case 2: 
      case 3: 
        i = localISqlDataWrapper.getExactNumber().signum();
        break;
      case -5: 
        i = localISqlDataWrapper.getBigInt().signum();
        break;
      case -6: 
      case 4: 
      case 5: 
        i = Long.signum(localISqlDataWrapper.getInteger());
        break;
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 0: 
      case 1: 
      default: 
        i = (int)Math.signum(localISqlDataWrapper.getDouble());
      }
      paramETDataRequest.getData().setSmallInt(i);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETSignFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */