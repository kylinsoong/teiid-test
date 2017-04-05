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

public final class ETCurrentTime1Fn
  extends ETScalarFn
{
  private int m_outputTimePrecision;
  private long m_timeInMillis;
  
  public ETCurrentTime1Fn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((1 == paramList.size()) && (1 == paramList1.size()));
    this.m_outputTimePrecision = Math.min(paramIColumn.getTypeMetadata().getPrecision(), 3);
  }
  
  public void open()
  {
    super.open();
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.set(1970, 0, 1);
    long l = localGregorianCalendar.getTimeInMillis();
    this.m_timeInMillis = (l - l % com.simba.support.conv.ConverterConstants.MILLIS_MOD[this.m_outputTimePrecision]);
  }
  
  public String getLogString()
  {
    return "ETCurrentTime1Fn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (isOpen());
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
    }
    else
    {
      int i = Math.max((int)localISqlDataWrapper.getInteger(), 0);
      long l = this.m_timeInMillis;
      if (i < this.m_outputTimePrecision) {
        l -= l % com.simba.support.conv.ConverterConstants.MILLIS_MOD[i];
      }
      paramETDataRequest.getData().setTime(new Time(l));
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETCurrentTime1Fn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */