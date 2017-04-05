package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.support.exceptions.ErrorException;

public class ETSearchedWhenClause
  extends ETValueExpr
{
  private ETBooleanExpr m_condition;
  private ETValueExpr m_result;
  
  public ETSearchedWhenClause(ETBooleanExpr paramETBooleanExpr, ETValueExpr paramETValueExpr)
  {
    this.m_condition = paramETBooleanExpr;
    this.m_result = paramETValueExpr;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    this.m_condition.close();
    this.m_result.close();
  }
  
  public boolean evaluateCondition()
    throws ErrorException
  {
    ETBoolean localETBoolean = this.m_condition.evaluate();
    return ETBoolean.SQL_BOOLEAN_TRUE == localETBoolean;
  }
  
  public boolean isOpen()
  {
    return (this.m_condition.isOpen()) && (this.m_result.isOpen());
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public void open()
  {
    this.m_condition.open();
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
    if (paramInt == 0) {
      return this.m_condition;
    }
    if (paramInt == 1) {
      return this.m_result;
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETSearchedWhenClause.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */