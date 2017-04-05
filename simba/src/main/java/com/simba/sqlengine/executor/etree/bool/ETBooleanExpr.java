package com.simba.sqlengine.executor.etree.bool;

import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.IETExpr;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;

public abstract class ETBooleanExpr
  implements IETExpr
{
  public Iterator<? extends IETNode> getChildItr()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        return ETBooleanExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return ETBooleanExpr.this.getNumChildren();
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public abstract ETBoolean evaluate()
    throws ErrorException;
  
  protected abstract IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException;
  
  public abstract void open();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETBooleanExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */