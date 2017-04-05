package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class ETSimpleWhenClause
  extends ETValueExpr
{
  private ETValueExpr m_operand;
  private ETValueExpr m_result;
  
  public ETSimpleWhenClause(ETValueExpr paramETValueExpr1, ETValueExpr paramETValueExpr2)
  {
    this.m_operand = paramETValueExpr1;
    this.m_result = paramETValueExpr2;
  }
  
  public void close()
  {
    this.m_operand.close();
    this.m_result.close();
  }
  
  public boolean isOpen()
  {
    return (this.m_operand.isOpen()) && (this.m_result.isOpen());
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
  
  public ETValueExpr getWhenOperand()
  {
    return this.m_operand;
  }
  
  public void open()
  {
    this.m_operand.open();
    this.m_result.open();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_result.retrieveData(paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return this.m_operand;
    }
    if (1 == paramInt) {
      return this.m_result;
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETSimpleWhenClause.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */