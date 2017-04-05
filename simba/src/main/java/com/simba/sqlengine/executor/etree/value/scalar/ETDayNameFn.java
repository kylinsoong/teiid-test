package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.text.DateFormatSymbols;
import java.util.List;

public final class ETDayNameFn
  extends ETScalarFn
{
  private final int m_columnLength;
  private String[] m_weekDays;
  
  public ETDayNameFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    if ((1 != paramList.size()) || (1 != paramList1.size())) {
      throw new IllegalArgumentException("Invalid Number of arguments for DAYNAME scalar function.");
    }
    int i = ((IColumn)paramList1.get(0)).getTypeMetadata().getType();
    if (91 != i) {
      throw new IllegalArgumentException();
    }
    this.m_columnLength = ((int)Math.min(paramIColumn.getColumnLength(), 2147483647L));
  }
  
  public String getLogString()
  {
    return "ETDayNameFn";
  }
  
  public void open()
  {
    super.open();
    this.m_weekDays = new DateFormatSymbols().getWeekdays();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String[] arrayOfString = this.m_weekDays;
    String str = arrayOfString[(1 + localISqlDataWrapper.getDate().getDay())];
    paramETDataRequest.getData().setChar(str);
    return DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize(), this.m_columnLength, getWarningListener());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETDayNameFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */