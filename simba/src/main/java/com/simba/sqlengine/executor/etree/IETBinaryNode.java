package com.simba.sqlengine.executor.etree;

public abstract interface IETBinaryNode<L extends IETNode, R extends IETNode>
{
  public abstract L getLeftOperand();
  
  public abstract R getRightOperand();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IETBinaryNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */