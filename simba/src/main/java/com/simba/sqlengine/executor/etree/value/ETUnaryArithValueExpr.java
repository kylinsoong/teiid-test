package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.functor.arithmetic.IUnaryArithmeticFunctor;
import com.simba.support.exceptions.ErrorException;

public class ETUnaryArithValueExpr
  extends ETUnaryValueExpr
{
  private ETDataRequest m_childData;
  private IUnaryArithmeticFunctor m_functor;
  
  public ETUnaryArithValueExpr(IColumn paramIColumn, ETValueExpr paramETValueExpr, IUnaryArithmeticFunctor paramIUnaryArithmeticFunctor)
    throws ErrorException
  {
    super(paramETValueExpr);
    if ((paramIColumn == null) || (paramETValueExpr == null) || (paramIUnaryArithmeticFunctor == null)) {
      throw new NullPointerException("ETUnaryArithValueExpr does not take null input.");
    }
    this.m_childData = new ETDataRequest(paramIColumn);
    this.m_functor = paramIUnaryArithmeticFunctor;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    getOperand().retrieveData(this.m_childData);
    return this.m_functor.execute(paramETDataRequest, this.m_childData.getData());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETUnaryArithValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */