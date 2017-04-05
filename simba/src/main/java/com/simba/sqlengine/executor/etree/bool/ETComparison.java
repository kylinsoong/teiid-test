package com.simba.sqlengine.executor.etree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.bool.functor.comp.IBooleanCompFunctor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;

public class ETComparison
  extends ETBooleanExpr
{
  private ETValueExpr m_leftOperand;
  private ETValueExpr m_rightOperand;
  private IBooleanCompFunctor m_functor;
  private ETDataRequest m_leftData;
  private ETDataRequest m_rightData;
  
  public ETComparison(IColumn paramIColumn, ETValueExpr paramETValueExpr1, ETValueExpr paramETValueExpr2, IBooleanCompFunctor paramIBooleanCompFunctor)
    throws ErrorException
  {
    if ((paramETValueExpr1 == null) || (paramIColumn == null) || (paramETValueExpr2 == null) || (paramIBooleanCompFunctor == null)) {
      throw new NullPointerException("ETArithmeticValueExpr does not take null input.");
    }
    this.m_leftData = new ETDataRequest(paramIColumn);
    this.m_rightData = new ETDataRequest(paramIColumn);
    this.m_leftOperand = paramETValueExpr1;
    this.m_rightOperand = paramETValueExpr2;
    this.m_functor = paramIBooleanCompFunctor;
  }
  
  public void close()
  {
    this.m_leftOperand.close();
    this.m_rightOperand.close();
  }
  
  public boolean isOpen()
  {
    return (this.m_leftOperand.isOpen()) && (this.m_rightOperand.isOpen());
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_leftOperand.reset();
    this.m_rightOperand.reset();
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public ETBoolean evaluate()
    throws ErrorException
  {
    this.m_leftOperand.retrieveData(this.m_leftData);
    this.m_rightOperand.retrieveData(this.m_rightData);
    return this.m_functor.evaluate(this.m_leftData.getData(), this.m_rightData.getData());
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    switch (paramInt)
    {
    case 0: 
      return this.m_leftOperand;
    case 1: 
      return this.m_rightOperand;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  public void open()
  {
    this.m_leftOperand.open();
    this.m_rightOperand.open();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETComparison.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */