package com.simba.sqlengine.executor.etree;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractETNodeList<T extends IETNode>
  implements IETNode, Iterable<T>
{
  private ArrayList<T> m_nodeList = new ArrayList();
  
  public Iterator<T> getChildItr()
  {
    return iterator();
  }
  
  public int getNumChildren()
  {
    return this.m_nodeList.size();
  }
  
  public void addNode(T paramT)
  {
    if (null == paramT) {
      throw new NullPointerException("The child cannot be null.");
    }
    this.m_nodeList.add(paramT);
  }
  
  public Iterator<T> iterator()
  {
    return this.m_nodeList.iterator();
  }
  
  public T replaceNode(T paramT, int paramInt)
  {
    if (null == paramT) {
      throw new NullPointerException("The child cannot be null.");
    }
    return (IETNode)this.m_nodeList.set(paramInt, paramT);
  }
  
  public T getChild(int paramInt)
  {
    return (IETNode)this.m_nodeList.get(paramInt);
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/AbstractETNodeList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */