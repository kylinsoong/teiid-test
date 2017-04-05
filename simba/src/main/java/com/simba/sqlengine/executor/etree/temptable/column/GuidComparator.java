package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import java.util.UUID;

public final class GuidComparator
  extends ColumnComparator
{
  public GuidComparator(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean1, paramBoolean2);
  }
  
  protected int compareValue(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    UUID localUUID1 = paramIRowView1.getGuid(this.m_colNum);
    UUID localUUID2 = paramIRowView2.getGuid(this.m_colNum);
    int i = unsignedCmp(localUUID1.getMostSignificantBits(), localUUID2.getMostSignificantBits());
    if (0 == i) {
      i = unsignedCmp(localUUID1.getLeastSignificantBits(), localUUID2.getLeastSignificantBits());
    }
    return i;
  }
  
  private static int unsignedCmp(long paramLong1, long paramLong2)
  {
    if ((paramLong1 < 0L ? 1 : 0) == (paramLong2 < 0L ? 1 : 0))
    {
      if (paramLong1 == paramLong2) {
        return 0;
      }
      return paramLong1 < paramLong2 ? -1 : 1;
    }
    return paramLong1 < 0L ? 1 : -1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/GuidComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */