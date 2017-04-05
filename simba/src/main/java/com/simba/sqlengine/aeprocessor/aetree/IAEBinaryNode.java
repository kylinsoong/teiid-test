package com.simba.sqlengine.aeprocessor.aetree;

public abstract interface IAEBinaryNode<L extends IAENode, R extends IAENode>
{
  public abstract L getLeftOperand();
  
  public abstract R getRightOperand();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/IAEBinaryNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */