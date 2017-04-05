package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ETYearFn
  extends ETScalarFn
{
  public ETYearFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == getNumChildren());
    assert (91 == ((IColumn)paramList1.get(0)).getTypeMetadata().getType());
    assert (4 == paramIColumn.getTypeMetadata().getType());
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
      Date localDate = localISqlDataWrapper.getDate();
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      localGregorianCalendar.clear();
      localGregorianCalendar.setTime(localDate);
      paramETDataRequest.getData().setInteger(localGregorianCalendar.get(1));
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETYearFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */