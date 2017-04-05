package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETExpr;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;

public abstract class ETValueExpr
  implements IETExpr, IWarningSource
{
  private IWarningListener m_listner = null;
  
  public Iterator<? extends IETNode> getChildItr()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        return ETValueExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return ETValueExpr.this.getNumChildren();
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_listner;
  }
  
  public abstract void open();
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_listner = paramIWarningListener;
  }
  
  public void reset() {}
  
  public abstract boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException;
  
  public void update()
    throws ErrorException
  {}
  
  protected abstract IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */