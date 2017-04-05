package com.simba.sqlengine.executor.etree.hash;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public abstract interface IRowBinaryPredicate
{
  public abstract boolean apply(IRowView paramIRowView1, IRowView paramIRowView2);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/hash/IRowBinaryPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */