package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.support.exceptions.ErrorException;

class ETJoinedUnitWrapper
  extends ETRelationalExpr
{
  IColumn[] m_columns;
  IJoinUnit m_joinUnit;
  
  public ETJoinedUnitWrapper(ETRelationalExpr paramETRelationalExpr, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    this.m_columns = new IColumn[paramETRelationalExpr.getColumnCount()];
    for (int i = 0; i < this.m_columns.length; i++) {
      this.m_columns[i] = paramETRelationalExpr.getColumn(i);
    }
  }
  
  public void setJoinUnit(IJoinUnit paramIJoinUnit)
  {
    this.m_joinUnit = paramIJoinUnit;
  }
  
  public void close() {}
  
  public boolean isOpen()
  {
    return true;
  }
  
  public void reset()
    throws ErrorException
  {}
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    throw new IllegalStateException("visitor is not supported by ETJoinedUnitWrapper.");
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_columns[paramInt];
  }
  
  public int getColumnCount()
  {
    return this.m_columns.length;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return -1L;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {}
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_joinUnit.retrieveData(paramInt, paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    throw new IllegalStateException("doMove should not be called on ETJoinedUnitWrapper");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/ETJoinedUnitWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */