package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;

public final class RealComparator
  extends ColumnComparator
{
  public RealComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    return Float.compare(paramIRowView1.getReal(this.m_colNum), paramIRowView2.getReal(this.m_colNum));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/RealComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */