package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IETUnaryNode;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.List;

public class ETTableConstructor
  extends ETRelationalExpr
  implements IETUnaryNode<ETValueExprList>
{
  private ETValueExprList m_operand;
  private boolean m_hasStartedFetch = false;
  private List<IColumn> m_columns;
  
  public ETTableConstructor(ETValueExprList paramETValueExprList, List<IColumn> paramList, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    this.m_operand = paramETValueExprList;
    this.m_columns = paramList;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    this.m_operand.close();
    this.m_hasStartedFetch = false;
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_columns.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_operand.getNumChildren();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return 1L;
  }
  
  public boolean isOpen()
  {
    Iterator localIterator = this.m_operand.getChildItr();
    while (localIterator.hasNext()) {
      if (!((ETValueExpr)localIterator.next()).isOpen()) {
        return false;
      }
    }
    return true;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_operand.open();
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_operand.reset();
    this.m_hasStartedFetch = false;
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_operand.retrieveData(paramInt, paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return this.m_operand;
    }
    throw new IndexOutOfBoundsException();
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    if (this.m_hasStartedFetch) {
      return false;
    }
    this.m_hasStartedFetch = true;
    return true;
  }
  
  public ETValueExprList getOperand()
  {
    return this.m_operand;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETTableConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */