package com.simba.sqlengine.executor.etree.bool;

import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class ETOr
  extends ETBinaryBoolExpr
{
  public ETOr(ETBooleanExpr paramETBooleanExpr1, ETBooleanExpr paramETBooleanExpr2)
  {
    super(paramETBooleanExpr1, paramETBooleanExpr2);
  }
  
  public ETBoolean evaluate()
    throws ErrorException
  {
    ETBoolean localETBoolean = this.m_leftOperand.evaluate();
    if (localETBoolean == ETBoolean.SQL_BOOLEAN_TRUE) {
      return localETBoolean;
    }
    return localETBoolean.or(this.m_rightOperand.evaluate());
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETOr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */