package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public final class UnsignedSmallIntComparator
  extends ColumnComparator
{
  public UnsignedSmallIntComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    int i = paramIRowView1.getSmallInt(this.m_colNum);
    int j = paramIRowView2.getSmallInt(this.m_colNum);
    if ((i < 0 ? 1 : 0) == (j < 0 ? 1 : 0)) {
      return i - j;
    }
    return i < 0 ? 1 : -1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/UnsignedSmallIntComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */