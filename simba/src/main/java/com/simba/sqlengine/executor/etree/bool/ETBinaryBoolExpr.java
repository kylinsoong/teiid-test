package com.simba.sqlengine.executor.etree.bool;

import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.support.exceptions.ErrorException;

public abstract class ETBinaryBoolExpr
  extends ETBooleanExpr
{
  protected ETBooleanExpr m_leftOperand;
  protected ETBooleanExpr m_rightOperand;
  
  protected ETBinaryBoolExpr(ETBooleanExpr paramETBooleanExpr1, ETBooleanExpr paramETBooleanExpr2)
  {
    this.m_leftOperand = paramETBooleanExpr1;
    this.m_rightOperand = paramETBooleanExpr2;
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
  
  public int getNumChildren()
  {
    return 2;
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
    throw new IndexOutOfBoundsException("Index out of bound: " + paramInt);
  }
  
  public void open()
  {
    this.m_leftOperand.open();
    this.m_rightOperand.open();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETBinaryBoolExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */