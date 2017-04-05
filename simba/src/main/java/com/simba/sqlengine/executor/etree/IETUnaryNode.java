package com.simba.sqlengine.executor.etree;

public abstract interface IETUnaryNode<T extends IETNode>
{
  public abstract T getOperand();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IETUnaryNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */