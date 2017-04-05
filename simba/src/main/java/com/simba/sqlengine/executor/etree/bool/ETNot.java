package com.simba.sqlengine.executor.etree.bool;

import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class ETNot
  extends ETUnaryBoolExpr
{
  public ETNot(ETBooleanExpr paramETBooleanExpr)
  {
    super(paramETBooleanExpr);
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public ETBoolean evaluate()
    throws ErrorException
  {
    return this.m_operand.evaluate().not();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETNot.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */