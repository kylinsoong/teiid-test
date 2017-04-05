package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public abstract interface IHasher
{
  public abstract long hash(IRowView paramIRowView, int[] paramArrayOfInt);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/IHasher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */