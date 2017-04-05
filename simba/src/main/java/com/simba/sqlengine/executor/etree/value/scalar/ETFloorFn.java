package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.util.List;

public final class ETFloorFn
  extends ETScalarFn
{
  private final int m_resultType;
  
  public ETFloorFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    int i = paramIColumn.getTypeMetadata().getType();
    assert (TypeUtilities.isNumberType(i));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().getType() == i);
    this.m_resultType = i;
  }
  
  public String getLogString()
  {
    return "ETFloorFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull()) {
      paramETDataRequest.getData().setNull();
    } else {
      switch (this.m_resultType)
      {
      case -6: 
        paramETDataRequest.getData().setTinyInt(localISqlDataWrapper.getTinyInt());
        break;
      case 5: 
        paramETDataRequest.getData().setSmallInt(localISqlDataWrapper.getSmallInt());
        break;
      case 4: 
        paramETDataRequest.getData().setInteger(localISqlDataWrapper.getInteger());
        break;
      case -5: 
        paramETDataRequest.getData().setBigInt(localISqlDataWrapper.getBigInt());
        break;
      case 2: 
      case 3: 
        BigDecimal localBigDecimal = localISqlDataWrapper.getExactNumber();
        paramETDataRequest.getData().setExactNumber(localBigDecimal.setScale(0, 3));
        break;
      case 7: 
        paramETDataRequest.getData().setReal((float)Math.floor(localISqlDataWrapper.getReal()));
        break;
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 0: 
      case 1: 
      case 6: 
      default: 
        paramETDataRequest.getData().setDouble(Math.floor(localISqlDataWrapper.getDouble()));
      }
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETFloorFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */