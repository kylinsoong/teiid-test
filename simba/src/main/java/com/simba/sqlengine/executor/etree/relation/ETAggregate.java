package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.support.exceptions.ErrorException;

public abstract class ETAggregate
  extends ETRelationalExpr
{
  private final ETRelationalExpr m_operand;
  
  protected ETAggregate(boolean[] paramArrayOfBoolean, ETRelationalExpr paramETRelationalExpr)
  {
    super(paramArrayOfBoolean);
    this.m_operand = paramETRelationalExpr;
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
  
  public void close()
  {
    getOperand().close();
  }
  
  public void reset()
    throws ErrorException
  {
    getOperand().reset();
  }
  
  protected ETRelationalExpr getOperand()
  {
    return this.m_operand;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETAggregate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */