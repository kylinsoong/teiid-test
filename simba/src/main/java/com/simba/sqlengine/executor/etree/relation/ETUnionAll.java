package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETBinaryNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public final class ETUnionAll
  extends ETRelationalExpr
  implements IETBinaryNode<ETRelationalExpr, ETRelationalExpr>
{
  private final ETRelationalExpr m_leftOperand;
  private final ETRelationalExpr m_rightOperand;
  private boolean m_isOnLeftOperand = true;
  
  public ETUnionAll(ETRelationalExpr paramETRelationalExpr1, ETRelationalExpr paramETRelationalExpr2, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    assert (paramETRelationalExpr1.getColumnCount() == paramETRelationalExpr2.getColumnCount());
    this.m_leftOperand = paramETRelationalExpr1;
    this.m_rightOperand = paramETRelationalExpr2;
  }
  
  public void close()
  {
    getLeftOperand().close();
    getRightOperand().close();
  }
  
  public boolean isOpen()
  {
    return (getLeftOperand().isOpen()) && (getRightOperand().isOpen());
  }
  
  public void reset()
    throws ErrorException
  {
    getLeftOperand().reset();
    getRightOperand().reset();
    this.m_isOnLeftOperand = true;
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
  
  public IColumn getColumn(int paramInt)
  {
    return getLeftOperand().getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return getLeftOperand().getColumnCount();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    long l1 = getLeftOperand().getRowCount();
    if (-1L == l1) {
      return l1;
    }
    long l2 = getRightOperand().getRowCount();
    if (-1L == l2) {
      return l2;
    }
    return l1 + l2;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    getLeftOperand().open(paramCursorType);
    getRightOperand().open(paramCursorType);
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return getCurrentOperand().retrieveData(paramInt, paramETDataRequest);
  }
  
  public String getLogString()
  {
    return "ETUnionAll";
  }
  
  public ETRelationalExpr getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return getLeftOperand();
    }
    if (1 == paramInt) {
      return getRightOperand();
    }
    throw new IndexOutOfBoundsException();
  }
  
  public ETRelationalExpr getLeftOperand()
  {
    return this.m_leftOperand;
  }
  
  public ETRelationalExpr getRightOperand()
  {
    return this.m_rightOperand;
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    if (this.m_isOnLeftOperand)
    {
      if (this.m_leftOperand.move()) {
        return true;
      }
      this.m_isOnLeftOperand = false;
    }
    return this.m_rightOperand.move();
  }
  
  private ETRelationalExpr getCurrentOperand()
  {
    return this.m_isOnLeftOperand ? getLeftOperand() : getRightOperand();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETUnionAll.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */