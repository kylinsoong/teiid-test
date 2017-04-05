package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public final class BooleanComparator
  extends ColumnComparator
{
  public BooleanComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    boolean bool1 = paramIRowView1.getBoolean(this.m_colNum);
    boolean bool2 = paramIRowView2.getBoolean(this.m_colNum);
    return bool1 ? 1 : bool1 == bool2 ? 0 : -1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/BooleanComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */