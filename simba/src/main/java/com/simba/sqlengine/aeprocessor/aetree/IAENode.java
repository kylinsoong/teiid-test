package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public abstract interface IAENode
{
  public abstract <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException;
  
  public abstract IAENode copy();
  
  public abstract Iterator<? extends IAENode> getChildItr();
  
  public abstract String getLogString();
  
  public abstract int getNumChildren();
  
  public abstract IAENode getParent();
  
  public abstract void setParent(IAENode paramIAENode);
  
  public abstract boolean isEquivalent(IAENode paramIAENode);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/IAENode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */