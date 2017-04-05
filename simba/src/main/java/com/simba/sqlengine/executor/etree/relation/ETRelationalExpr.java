package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETCacheInvalidationListener;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETExpr;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.HashSet;
import java.util.Iterator;

public abstract class ETRelationalExpr
  implements IETExpr
{
  private HashSet<ETCacheInvalidationListener> m_cacheValidationListeners = new HashSet();
  private boolean m_canReopenAfterClosed = false;
  private boolean m_canBeReset;
  protected boolean[] m_dataNeeded;
  
  public ETRelationalExpr(boolean[] paramArrayOfBoolean)
  {
    this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
  }
  
  public boolean canReopenAfterClosed()
  {
    return this.m_canReopenAfterClosed;
  }
  
  public Iterator<? extends IETNode> getChildItr()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        return ETRelationalExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return ETRelationalExpr.this.getNumChildren();
      }
    }.iterator();
  }
  
  public abstract IColumn getColumn(int paramInt);
  
  public abstract int getColumnCount();
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    return this.m_dataNeeded[paramInt];
  }
  
  public abstract long getRowCount()
    throws ErrorException;
  
  public boolean move()
    throws ErrorException
  {
    if (doMove())
    {
      Iterator localIterator = this.m_cacheValidationListeners.iterator();
      while (localIterator.hasNext())
      {
        ETCacheInvalidationListener localETCacheInvalidationListener = (ETCacheInvalidationListener)localIterator.next();
        localETCacheInvalidationListener.invalidateCache();
      }
      return true;
    }
    return false;
  }
  
  public abstract void open(CursorType paramCursorType)
    throws ErrorException;
  
  public void registerCacheValidationListener(ETCacheInvalidationListener paramETCacheInvalidationListener)
  {
    this.m_cacheValidationListeners.add(paramETCacheInvalidationListener);
  }
  
  public abstract boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException;
  
  public void setCanBeReset(boolean paramBoolean)
  {
    if (this.m_canBeReset != paramBoolean)
    {
      this.m_canBeReset = paramBoolean;
      Iterator localIterator = getChildItr();
      while (localIterator.hasNext())
      {
        IETNode localIETNode = (IETNode)localIterator.next();
        if ((localIETNode instanceof ETRelationalExpr)) {
          ((ETRelationalExpr)localIETNode).setCanBeReset(paramBoolean);
        }
      }
    }
  }
  
  public void setCanReopenAfterClosed()
  {
    if (!this.m_canReopenAfterClosed)
    {
      this.m_canReopenAfterClosed = true;
      Iterator localIterator = getChildItr();
      while (localIterator.hasNext())
      {
        IETNode localIETNode = (IETNode)localIterator.next();
        if ((localIETNode instanceof ETRelationalExpr)) {
          ((ETRelationalExpr)localIETNode).setCanReopenAfterClosed();
        }
      }
    }
  }
  
  protected abstract IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException;
  
  protected abstract boolean doMove()
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETRelationalExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */