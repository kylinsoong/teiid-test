package com.simba.sqlengine.aeprocessor.aetree;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractAENodeList<T extends IAENode>
  implements IAENode, Iterable<T>
{
  private ArrayList<T> m_nodeList;
  private IAENode m_parent;
  
  public AbstractAENodeList()
  {
    this.m_nodeList = new ArrayList();
    this.m_parent = null;
  }
  
  public AbstractAENodeList(AbstractAENodeList<? extends T> paramAbstractAENodeList)
  {
    this.m_nodeList = new ArrayList(paramAbstractAENodeList.getNumChildren());
    this.m_parent = null;
    Iterator localIterator = paramAbstractAENodeList.m_nodeList.iterator();
    while (localIterator.hasNext())
    {
      IAENode localIAENode1 = (IAENode)localIterator.next();
      IAENode localIAENode2 = localIAENode1.copy();
      localIAENode2.setParent(this);
      this.m_nodeList.add(localIAENode2);
    }
  }
  
  public abstract AbstractAENodeList<T> copy();
  
  public IAENode getParent()
  {
    return this.m_parent;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    this.m_parent = paramIAENode;
  }
  
  public Iterator<T> getChildItr()
  {
    return this.m_nodeList.iterator();
  }
  
  public int getNumChildren()
  {
    return this.m_nodeList.size();
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AbstractAENodeList)) {
      return false;
    }
    if (paramIAENode.getNumChildren() != this.m_nodeList.size()) {
      return false;
    }
    Iterator localIterator1 = paramIAENode.getChildItr();
    Iterator localIterator2 = this.m_nodeList.iterator();
    while (localIterator1.hasNext()) {
      if (!((IAENode)localIterator1.next()).isEquivalent((IAENode)localIterator2.next())) {
        return false;
      }
    }
    return true;
  }
  
  public Iterator<T> iterator()
  {
    return getChildItr();
  }
  
  public void addNode(T paramT)
  {
    if (null == paramT) {
      throw new NullPointerException("The child cannot be null.");
    }
    this.m_nodeList.add(paramT);
    paramT.setParent(this);
  }
  
  public T replaceNode(T paramT, int paramInt)
  {
    if (null == paramT) {
      throw new NullPointerException("The child cannot be null.");
    }
    paramT.setParent(this);
    return (IAENode)this.m_nodeList.set(paramInt, paramT);
  }
  
  public int findNode(T paramT)
  {
    int i = 0;
    Iterator localIterator = this.m_nodeList.iterator();
    while (localIterator.hasNext())
    {
      IAENode localIAENode = (IAENode)localIterator.next();
      if (localIAENode.isEquivalent(paramT)) {
        return i;
      }
      i++;
    }
    return -1;
  }
  
  public T getChild(int paramInt)
  {
    return (IAENode)this.m_nodeList.get(paramInt);
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(getLogString());
    Iterator localIterator = getChildItr();
    if (!localIterator.hasNext())
    {
      localStringBuilder.append("[]");
    }
    else
    {
      localStringBuilder.append("[").append(((IAENode)localIterator.next()).getLogString());
      while (localIterator.hasNext()) {
        localStringBuilder.append(", ").append(((IAENode)localIterator.next()).getLogString());
      }
      localStringBuilder.append("]");
    }
    return localStringBuilder.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AbstractAENodeList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */