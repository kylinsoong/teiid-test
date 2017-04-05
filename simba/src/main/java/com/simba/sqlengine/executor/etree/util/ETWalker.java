package com.simba.sqlengine.executor.etree.util;

import com.simba.sqlengine.executor.etree.IETNode;
import java.util.Iterator;

public class ETWalker
{
  public static void walk(IETNode paramIETNode, Action<?> paramAction)
  {
    paramAction.act(paramIETNode);
    if (paramIETNode == null) {
      return;
    }
    Iterator localIterator = paramIETNode.getChildItr();
    while (localIterator.hasNext()) {
      walk((IETNode)localIterator.next(), paramAction);
    }
  }
  
  public static abstract interface Action<T>
  {
    public abstract void act(IETNode paramIETNode);
    
    public abstract T getResult();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/util/ETWalker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */