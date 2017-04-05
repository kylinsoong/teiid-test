package com.simba.sqlengine.executor.etree;

import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public abstract interface IETNode
{
  public abstract <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException;
  
  public abstract Iterator<? extends IETNode> getChildItr();
  
  public abstract String getLogString();
  
  public abstract int getNumChildren();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IETNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */