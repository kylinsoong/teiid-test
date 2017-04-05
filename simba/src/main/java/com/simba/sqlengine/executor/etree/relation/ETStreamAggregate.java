package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.List;

public class ETStreamAggregate
  extends ETAggregate
{
  private final ETValueExprList m_aggregateList;
  private final List<? extends IColumn> m_metadata;
  private final boolean m_hasGroupBy;
  private boolean m_hasMoved = false;
  
  public ETStreamAggregate(ETRelationalExpr paramETRelationalExpr, ETValueExprList paramETValueExprList, List<? extends IColumn> paramList, boolean paramBoolean, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean, paramETRelationalExpr);
    this.m_aggregateList = paramETValueExprList;
    this.m_metadata = paramList;
    this.m_hasGroupBy = paramBoolean;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_metadata.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_aggregateList.getNumChildren();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return -1L;
  }
  
  public ETDistinctMove.ETRowListener getRowListener()
  {
    return new UpdateRowListener(null);
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    super.open(paramCursorType);
    this.m_aggregateList.open();
    this.m_hasMoved = false;
  }
  
  public void close()
  {
    super.close();
    this.m_aggregateList.close();
  }
  
  public void reset()
    throws ErrorException
  {
    super.reset();
    this.m_aggregateList.reset();
    this.m_hasMoved = false;
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return getOperand();
    }
    if (1 == paramInt) {
      return this.m_aggregateList;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_aggregateList.retrieveData(paramInt, paramETDataRequest);
  }
  
  protected final void updateRow()
    throws ErrorException
  {
    Iterator localIterator = this.m_aggregateList.iterator();
    while (localIterator.hasNext())
    {
      ETValueExpr localETValueExpr = (ETValueExpr)localIterator.next();
      localETValueExpr.update();
    }
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    this.m_aggregateList.reset();
    if (this.m_hasGroupBy) {
      return getOperand().move();
    }
    if (!this.m_hasMoved)
    {
      while (getOperand().move()) {
        updateRow();
      }
      this.m_hasMoved = true;
      return true;
    }
    return false;
  }
  
  private class UpdateRowListener
    implements ETDistinctMove.ETRowListener
  {
    private UpdateRowListener() {}
    
    public void onNewRow()
      throws ErrorException
    {
      ETStreamAggregate.this.updateRow();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETStreamAggregate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */