package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class AETreeWalker
  implements Iterator<IAENode>
{
  protected IAENode m_nextNode;
  protected Stack<Iterator<? extends IAENode>> m_parentIters;
  
  public AETreeWalker(IAENode paramIAENode)
  {
    this.m_nextNode = paramIAENode;
    this.m_parentIters = new Stack();
  }
  
  public boolean hasNext()
  {
    return null != this.m_nextNode;
  }
  
  public IAENode peekNext()
  {
    return this.m_nextNode;
  }
  
  public IAENode next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    IAENode localIAENode = this.m_nextNode;
    this.m_parentIters.push(localIAENode.getChildItr());
    findNext();
    return localIAENode;
  }
  
  protected void findNext()
  {
    this.m_nextNode = null;
    while (!this.m_parentIters.empty())
    {
      if (((Iterator)this.m_parentIters.peek()).hasNext())
      {
        this.m_nextNode = ((IAENode)((Iterator)this.m_parentIters.peek()).next());
        break;
      }
      this.m_parentIters.pop();
    }
  }
  
  public void remove()
  {
    throw new UnsupportedOperationException("remove() is not supported.");
  }
  
  public static <T> T walk(IAENode paramIAENode, Action<T> paramAction)
    throws ErrorException
  {
    paramAction.walk(new AETreeWalker(paramIAENode));
    return (T)paramAction.getResult();
  }
  
  public static abstract class Action<T>
  {
    private IAENode m_currentNode;
    private AETreeWalker m_walker;
    
    public abstract void act(IAENode paramIAENode)
      throws ErrorException;
    
    public T getResult()
    {
      return null;
    }
    
    protected void skipChildren()
    {
      if (this.m_currentNode.getNumChildren() != 0)
      {
        this.m_walker.m_parentIters.pop();
        this.m_walker.findNext();
      }
    }
    
    protected void skipAll()
    {
      this.m_walker.m_parentIters.clear();
      this.m_walker.m_nextNode = null;
    }
    
    private void walk(AETreeWalker paramAETreeWalker)
      throws ErrorException
    {
      this.m_walker = paramAETreeWalker;
      while (this.m_walker.hasNext())
      {
        this.m_currentNode = this.m_walker.next();
        act(this.m_currentNode);
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AETreeWalker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */