package com.simba.sqlengine.aeprocessor.aetree;

public abstract interface IAEUnaryNode<T extends IAENode>
{
  public abstract T getOperand();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/IAEUnaryNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */