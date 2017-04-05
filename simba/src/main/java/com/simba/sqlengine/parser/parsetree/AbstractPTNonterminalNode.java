package com.simba.sqlengine.parser.parsetree;

import java.util.Iterator;

public abstract class AbstractPTNonterminalNode
  implements IPTNode
{
  public abstract Iterator<IPTNode> getChildItr();
  
  public boolean isEmptyNode()
  {
    return false;
  }
  
  public final boolean isTerminalNode()
  {
    return false;
  }
  
  public abstract int numChildren();
  
  public abstract String toString();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/AbstractPTNonterminalNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */