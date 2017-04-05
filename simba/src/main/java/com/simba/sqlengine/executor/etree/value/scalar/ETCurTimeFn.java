package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.sql.Time;
import java.util.GregorianCalendar;
import java.util.List;

public final class ETCurTimeFn
  extends ETScalarFn
{
  private int m_timePrecision;
  private long m_timeInMillis;
  
  public ETCurTimeFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((paramList.isEmpty()) && (paramList1.isEmpty()));
    this.m_timePrecision = Math.min(paramIColumn.getTypeMetadata().getPrecision(), 3);
  }
  
  public void open()
  {
    super.open();
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.set(1970, 0, 1);
    long l = localGregorianCalendar.getTimeInMillis();
    l -= com.simba.support.conv.ConverterConstants.MILLIS_MOD[this.m_timePrecision];
    this.m_timeInMillis = l;
  }
  
  public String getLogString()
  {
    return "ETCurTimeFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (isOpen());
    paramETDataRequest.getData().setTime(new Time(this.m_timeInMillis));
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETCurTimeFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */