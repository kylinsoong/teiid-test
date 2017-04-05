package com.simba.sqlengine.parser.parsetree;

public abstract class AbstractPTTerminalNode
  implements IPTNode
{
  public boolean isEmptyNode()
  {
    return false;
  }
  
  public final boolean isTerminalNode()
  {
    return true;
  }
  
  public abstract String toString();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/AbstractPTTerminalNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */