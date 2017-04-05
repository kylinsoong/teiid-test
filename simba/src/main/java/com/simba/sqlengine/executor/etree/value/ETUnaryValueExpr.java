package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETUnaryNode;

public abstract class ETUnaryValueExpr
  extends ETValueExpr
  implements IETUnaryNode<ETValueExpr>
{
  private ETValueExpr m_operand;
  
  protected ETUnaryValueExpr(ETValueExpr paramETValueExpr)
  {
    this.m_operand = paramETValueExpr;
  }
  
  public void close()
  {
    this.m_operand.close();
  }
  
  public boolean isOpen()
  {
    return this.m_operand.isOpen();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public ETValueExpr getOperand()
  {
    return this.m_operand;
  }
  
  public void open()
  {
    this.m_operand.open();
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return this.m_operand;
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETUnaryValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */