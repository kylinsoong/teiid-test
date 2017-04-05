package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public final class DoubleComparator
  extends ColumnComparator
{
  public DoubleComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    double d1 = paramIRowView1.getDouble(this.m_colNum);
    double d2 = paramIRowView2.getDouble(this.m_colNum);
    if ((0.0D == d1) && (0.0D == d2)) {
      return 0;
    }
    return Double.compare(d1, d2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/DoubleComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */