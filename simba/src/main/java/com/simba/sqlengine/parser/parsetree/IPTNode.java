package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;

public abstract interface IPTNode
{
  public abstract <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException;
  
  public abstract boolean isTerminalNode();
  
  public abstract boolean isEmptyNode();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/IPTNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */