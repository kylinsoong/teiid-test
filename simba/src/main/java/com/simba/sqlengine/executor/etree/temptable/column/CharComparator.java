package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;

public final class CharComparator
  extends ColumnComparator
{
  public CharComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    return DataRetrievalUtil.rtrim(paramIRowView1.getString(this.m_colNum)).compareTo(DataRetrievalUtil.rtrim(paramIRowView2.getString(this.m_colNum)));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/CharComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */