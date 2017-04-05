package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class PTWalker
{
  public static void walkBreadthFirst(IPTNode paramIPTNode, IPTVisitor<Void> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTNode) {
      throw new NullPointerException("The tree cannot be null.");
    }
    LinkedList localLinkedList = new LinkedList();
    localLinkedList.add(paramIPTNode);
    while (!localLinkedList.isEmpty())
    {
      IPTNode localIPTNode1 = (IPTNode)localLinkedList.remove();
      localIPTNode1.acceptVisitor(paramIPTVisitor);
      if (!localIPTNode1.isTerminalNode())
      {
        Iterator localIterator = ((AbstractPTNonterminalNode)localIPTNode1).getChildItr();
        while (localIterator.hasNext())
        {
          IPTNode localIPTNode2 = (IPTNode)localIterator.next();
          if (null == localIPTNode2) {
            throw new NullPointerException("Tree nodes cannot be null.");
          }
          localLinkedList.add(localIPTNode2);
        }
      }
    }
  }
  
  public static void walkPreorderDepthFirst(IPTNode paramIPTNode, IPTVisitor<Void> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTNode) {
      throw new NullPointerException("The tree cannot be null.");
    }
    Stack localStack = new Stack();
    localStack.push(paramIPTNode);
    while (!localStack.empty())
    {
      IPTNode localIPTNode1 = (IPTNode)localStack.pop();
      localIPTNode1.acceptVisitor(paramIPTVisitor);
      if (!localIPTNode1.isTerminalNode())
      {
        Iterator localIterator = ((AbstractPTNonterminalNode)localIPTNode1).getChildItr();
        ArrayList localArrayList = new ArrayList();
        while (localIterator.hasNext()) {
          localArrayList.add(localIterator.next());
        }
        for (int i = localArrayList.size() - 1; i >= 0; i--)
        {
          IPTNode localIPTNode2 = (IPTNode)localArrayList.get(i);
          if (null == localIPTNode2) {
            throw new NullPointerException("Tree nodes cannot be null.");
          }
          localStack.push(localIPTNode2);
        }
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTWalker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */