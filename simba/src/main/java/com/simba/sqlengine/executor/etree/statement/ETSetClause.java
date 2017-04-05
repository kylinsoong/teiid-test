package com.simba.sqlengine.executor.etree.statement;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.ETUnaryValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;

public final class ETSetClause
  extends ETUnaryValueExpr
{
  private final int m_columnIndex;
  
  public ETSetClause(ETValueExpr paramETValueExpr, int paramInt)
  {
    super(paramETValueExpr);
    this.m_columnIndex = paramInt;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return getOperand().retrieveData(paramETDataRequest);
  }
  
  public int getColumnIndex()
  {
    return this.m_columnIndex;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/ETSetClause.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */