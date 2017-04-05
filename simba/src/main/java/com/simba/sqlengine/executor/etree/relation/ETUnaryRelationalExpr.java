package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETUnaryNode;
import com.simba.support.exceptions.ErrorException;

public abstract class ETUnaryRelationalExpr
  extends ETRelationalExpr
  implements IETUnaryNode<ETRelationalExpr>
{
  private ETRelationalExpr m_operand;
  
  ETUnaryRelationalExpr(ETRelationalExpr paramETRelationalExpr, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    this.m_operand = paramETRelationalExpr;
  }
  
  public void close()
  {
    this.m_operand.close();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public ETRelationalExpr getOperand()
  {
    return this.m_operand;
  }
  
  public boolean isOpen()
  {
    return this.m_operand.isOpen();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_operand.open(paramCursorType);
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_operand.reset();
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETUnaryRelationalExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */