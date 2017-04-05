package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import java.math.BigDecimal;

public final class ExactNumComparator
  extends ColumnComparator
{
  public ExactNumComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    return paramIRowView1.getExactNumber(this.m_colNum).compareTo(paramIRowView2.getExactNumber(this.m_colNum));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/ExactNumComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */