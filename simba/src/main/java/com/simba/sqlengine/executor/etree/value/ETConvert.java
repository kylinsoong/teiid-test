package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.conversions.ISqlConverter;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public final class ETConvert
  extends ETUnaryValueExpr
{
  private final ETDataRequest m_sourceData;
  private final ISqlConverter m_converter;
  private final boolean m_errorOnTruncation;
  
  public ETConvert(ETValueExpr paramETValueExpr, IColumn paramIColumn, ISqlConverter paramISqlConverter)
    throws ErrorException
  {
    this(paramETValueExpr, paramIColumn, paramISqlConverter, false);
  }
  
  public ETConvert(ETValueExpr paramETValueExpr, IColumn paramIColumn, ISqlConverter paramISqlConverter, boolean paramBoolean)
    throws ErrorException
  {
    super(paramETValueExpr);
    this.m_sourceData = new ETDataRequest(paramIColumn);
    this.m_sourceData.setMaxBytes(-1L);
    this.m_sourceData.setOffset(0L);
    this.m_converter = paramISqlConverter;
    this.m_errorOnTruncation = paramBoolean;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    boolean bool = getOperand().retrieveData(this.m_sourceData);
    if (bool) {
      throw new IllegalStateException("Has more data after retrieve all data");
    }
    return ConversionUtil.doConvert(paramETDataRequest, this.m_sourceData.getData(), this.m_converter, getWarningListener(), this.m_errorOnTruncation);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETConvert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */