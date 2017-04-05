package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

public final class ETCurDateFn
  extends ETScalarFn
{
  private long m_dateInMillis;
  
  public ETCurDateFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((paramList.isEmpty()) && (paramList1.isEmpty()));
  }
  
  public void open()
  {
    super.open();
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.set(11, 0);
    localGregorianCalendar.set(12, 0);
    localGregorianCalendar.set(13, 0);
    localGregorianCalendar.set(14, 0);
    this.m_dateInMillis = localGregorianCalendar.getTimeInMillis();
  }
  
  public String getLogString()
  {
    return "ETCurDateFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (isOpen());
    paramETDataRequest.getData().setDate(new Date(this.m_dateInMillis));
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETCurDateFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */