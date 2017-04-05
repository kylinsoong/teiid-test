package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public abstract class ETScalarFn
  extends ETValueExpr
{
  private ETDataRequest[] m_argRequests;
  private ETValueExpr[] m_args;
  private IColumn m_resultMeta;
  private BitSet m_argumentHasMoreData = new BitSet();
  
  protected ETScalarFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    if (paramList.size() != paramList1.size()) {
      throw new IllegalArgumentException(String.format("Number of metadata does not match number of arguments: number of argument metadata: %d, number of arguments: %d", new Object[] { Integer.valueOf(paramList1.size()), Integer.valueOf(paramList.size()) }));
    }
    this.m_resultMeta = paramIColumn;
    int i = paramList1.size();
    this.m_argRequests = new ETDataRequest[i];
    this.m_args = new ETValueExpr[i];
    Iterator localIterator1 = paramList1.iterator();
    Iterator localIterator2 = paramList.iterator();
    for (int j = 0; j < this.m_args.length; j++)
    {
      this.m_argRequests[j] = new ETDataRequest((IColumn)localIterator1.next());
      this.m_args[j] = ((ETValueExpr)localIterator2.next());
    }
  }
  
  public void close()
  {
    for (ETValueExpr localETValueExpr : this.m_args) {
      localETValueExpr.close();
    }
  }
  
  public boolean isOpen()
  {
    for (ETValueExpr localETValueExpr : this.m_args) {
      if (!localETValueExpr.isOpen()) {
        return false;
      }
    }
    return true;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return this.m_args.length;
  }
  
  public void open()
  {
    for (ETValueExpr localETValueExpr : this.m_args) {
      localETValueExpr.open();
    }
  }
  
  public abstract boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException;
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if ((paramInt >= this.m_args.length) || (paramInt < 0)) {
      throw new IndexOutOfBoundsException("invalid index: " + paramInt);
    }
    return this.m_args[paramInt];
  }
  
  protected ISqlDataWrapper getArgumentData(int paramInt)
    throws ErrorException
  {
    if ((paramInt < 0) || (paramInt > this.m_argRequests.length)) {
      throw new IndexOutOfBoundsException("index: " + paramInt);
    }
    this.m_argRequests[paramInt].setOffset(0L);
    this.m_argRequests[paramInt].setMaxBytes(-1L);
    boolean bool = this.m_args[paramInt].retrieveData(this.m_argRequests[paramInt]);
    this.m_argumentHasMoreData.set(paramInt, bool);
    return this.m_argRequests[paramInt].getData();
  }
  
  protected ISqlDataWrapper getArgumentData(int paramInt, long paramLong1, long paramLong2)
    throws ErrorException
  {
    if ((paramInt < 0) || (paramInt > this.m_argRequests.length)) {
      throw new IndexOutOfBoundsException("index: " + paramInt);
    }
    this.m_argRequests[paramInt].setOffset(paramLong1);
    this.m_argRequests[paramInt].setMaxBytes(paramLong2);
    boolean bool = this.m_args[paramInt].retrieveData(this.m_argRequests[paramInt]);
    this.m_argumentHasMoreData.set(paramInt, bool);
    return this.m_argRequests[paramInt].getData();
  }
  
  protected IColumn getInputMetadata(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.m_argRequests.length)) {
      throw new IndexOutOfBoundsException("index: " + paramInt);
    }
    return this.m_argRequests[paramInt].getColumn();
  }
  
  protected IColumn getResultMetadata()
  {
    return this.m_resultMeta;
  }
  
  protected final boolean hasMoreData(int paramInt)
  {
    return this.m_argumentHasMoreData.get(paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETScalarFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */