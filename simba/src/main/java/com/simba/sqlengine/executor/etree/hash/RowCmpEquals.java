package com.simba.sqlengine.executor.etree.hash;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import java.util.Comparator;

public final class RowCmpEquals
  implements IRowBinaryPredicate
{
  private final Comparator<IRowView> m_comparator;
  
  public RowCmpEquals(Comparator<IRowView> paramComparator)
  {
    this.m_comparator = paramComparator;
  }
  
  public boolean apply(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    return 0 == this.m_comparator.compare(paramIRowView1, paramIRowView2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/hash/RowCmpEquals.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */