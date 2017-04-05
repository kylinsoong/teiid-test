package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public final class SignedBigIntComparator
  extends ColumnComparator
{
  public SignedBigIntComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    long l1 = paramIRowView1.getBigInt(this.m_colNum);
    long l2 = paramIRowView2.getBigInt(this.m_colNum);
    if (l1 == l2) {
      return 0;
    }
    return l1 < l2 ? -1 : 1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/SignedBigIntComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */