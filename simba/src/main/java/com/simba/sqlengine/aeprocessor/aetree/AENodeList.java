package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.support.exceptions.ErrorException;

public final class AENodeList<N extends IAENode>
  extends AbstractAENodeList<N>
{
  public AENodeList() {}
  
  public AENodeList(AbstractAENodeList<? extends N> paramAbstractAENodeList)
  {
    super(paramAbstractAENodeList);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AENodeList<N> copy()
  {
    return new AENodeList(this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AENodeList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */