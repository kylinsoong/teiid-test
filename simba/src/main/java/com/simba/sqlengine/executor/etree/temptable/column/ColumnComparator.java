package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import java.util.Comparator;

public abstract class ColumnComparator
  implements Comparator<IRowView>
{
  protected final int m_colNum;
  private final boolean m_isAscending;
  private final int m_nullCmp;
  
  protected ColumnComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.m_colNum = paramInt;
    this.m_isAscending = paramBoolean1;
    this.m_nullCmp = (paramBoolean2 ? -1 : 1);
  }
  
  public final int compare(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    int i = this.m_colNum;
    if (paramIRowView1.isNull(i)) {
      return paramIRowView2.isNull(i) ? 0 : this.m_nullCmp;
    }
    if (paramIRowView2.isNull(i)) {
      return -this.m_nullCmp;
    }
    return this.m_isAscending ? compareValue(paramIRowView1, paramIRowView2) : compareValue(paramIRowView2, paramIRowView1);
  }
  
  protected abstract int compareValue(IRowView paramIRowView1, IRowView paramIRowView2);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/ColumnComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */