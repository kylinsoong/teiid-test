package com.simba.sqlengine.executor.etree.bool;

import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.support.exceptions.ErrorException;

public abstract class ETUnaryBoolExpr
  extends ETBooleanExpr
{
  protected ETBooleanExpr m_operand;
  
  protected ETUnaryBoolExpr(ETBooleanExpr paramETBooleanExpr)
  {
    this.m_operand = paramETBooleanExpr;
  }
  
  public void close()
  {
    this.m_operand.close();
  }
  
  public boolean isOpen()
  {
    return this.m_operand.isOpen();
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_operand.reset();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (paramInt == 0) {
      return this.m_operand;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  public void open()
  {
    this.m_operand.open();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETUnaryBoolExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */