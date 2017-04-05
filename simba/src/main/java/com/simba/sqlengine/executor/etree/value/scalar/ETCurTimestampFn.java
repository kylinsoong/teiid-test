package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

public final class ETCurTimestampFn
  extends ETScalarFn
{
  private final int m_timestampPrecision;
  private final boolean m_isUnary;
  private long m_timeInMillis;
  
  public ETCurTimestampFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((paramList.isEmpty()) || (1 == paramList.size()));
    assert (93 == paramIColumn.getTypeMetadata().getType());
    int i = paramIColumn.getTypeMetadata().getPrecision();
    assert (i >= 0) : "negative precision";
    this.m_isUnary = (!paramList.isEmpty());
    this.m_timestampPrecision = Math.min(i, 3);
  }
  
  public void open()
  {
    super.open();
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    long l = localGregorianCalendar.getTimeInMillis();
    l -= l % com.simba.support.conv.ConverterConstants.MILLIS_MOD[this.m_timestampPrecision];
    this.m_timeInMillis = l;
  }
  
  public String getLogString()
  {
    return "ETCurTimestampFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (isOpen());
    Object localObject;
    if (this.m_isUnary)
    {
      localObject = getArgumentData(0);
      if (((ISqlDataWrapper)localObject).isNull())
      {
        paramETDataRequest.getData().setNull();
      }
      else
      {
        int i = Math.max((int)((ISqlDataWrapper)localObject).getInteger(), 0);
        long l = this.m_timeInMillis;
        if (i < this.m_timestampPrecision) {
          l -= l % com.simba.support.conv.ConverterConstants.MILLIS_MOD[i];
        }
        Timestamp localTimestamp = new Timestamp(l);
        paramETDataRequest.getData().setTimestamp(localTimestamp);
      }
    }
    else
    {
      localObject = new Timestamp(this.m_timeInMillis);
      paramETDataRequest.getData().setTimestamp((Timestamp)localObject);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETCurTimestampFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */