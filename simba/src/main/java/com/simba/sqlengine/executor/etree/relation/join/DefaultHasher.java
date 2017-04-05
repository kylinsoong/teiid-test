package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class DefaultHasher
  implements IHasher
{
  private final long m_modulus;
  private final long m_seed;
  
  public DefaultHasher(long paramLong1, long paramLong2)
  {
    this.m_modulus = paramLong1;
    this.m_seed = paramLong2;
  }
  
  public long hash(IRowView paramIRowView, int[] paramArrayOfInt)
  {
    long l1 = -8663945395140668459L;
    long l2 = 5545529020109919103L;
    long l3 = this.m_seed;
    long l4 = this.m_seed;
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      l5 = getColumnHash(paramIRowView, paramArrayOfInt[i], paramIRowView.getColumn(paramArrayOfInt[i]));
      long l6 = (int)l5;
      l6 *= l1;
      l6 = l6 << 31 | l6 >>> 33;
      l6 *= l2;
      l3 ^= l6;
      l3 = l3 << 31 | l3 >>> 33;
      l3 += l4;
      l3 = l3 * 5L + 1390208809L;
      long l7 = l5 >>> 32;
      l7 *= l2;
      l7 = l7 << 33 | l7 >>> 31;
      l7 *= l1;
      l4 ^= l7;
      l4 = l4 << 31 | l4 >>> 33;
      l4 += l3;
      l4 = l4 * 5L + 944331445L;
    }
    long l5 = i * 8;
    l3 ^= l5;
    l4 ^= l5;
    l3 += l4;
    l4 += l3;
    l3 = mix64(l3);
    l4 = mix64(l4);
    l3 += l4;
    l4 += l3;
    if (l3 < 0L) {
      l3 = -l3;
    }
    return l3 % this.m_modulus;
  }
  
  private static long getColumnHash(IRowView paramIRowView, int paramInt, IColumn paramIColumn)
  {
    if (paramIRowView.isNull(paramInt)) {
      return 0L;
    }
    switch (paramIColumn.getTypeMetadata().getType())
    {
    case 4: 
      return paramIRowView.getInteger(paramInt);
    case -5: 
      return paramIRowView.getBigInt(paramInt);
    case 5: 
      return paramIRowView.getSmallInt(paramInt);
    case -6: 
      return paramIRowView.getTinyInt(paramInt);
    case 6: 
    case 8: 
      return hash(paramIRowView.getDouble(paramInt));
    case 7: 
      return hash(paramIRowView.getReal(paramInt));
    case -7: 
    case 16: 
      return hash(paramIRowView.getBoolean(paramInt));
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      return DataRetrievalUtil.rtrim(paramIRowView.getString(paramInt)).hashCode();
    case 92: 
      return paramIRowView.getTime(paramInt).hashCode();
    case 93: 
      return paramIRowView.getTimestamp(paramInt).hashCode();
    case 91: 
      return paramIRowView.getDate(paramInt).hashCode();
    case -11: 
      return paramIRowView.getGuid(paramInt).hashCode();
    case 2: 
    case 3: 
      return paramIRowView.getExactNumber(paramInt).hashCode();
    }
    throw new IllegalStateException(String.format("Attempt to hash on type %s.", new Object[] { paramIColumn.getTypeMetadata().getTypeName() }));
  }
  
  private static long hash(boolean paramBoolean)
  {
    long l;
    if (paramBoolean) {
      l = 0L;
    } else {
      l = 1L;
    }
    return l;
  }
  
  private static long hash(float paramFloat)
  {
    return Float.floatToIntBits(paramFloat);
  }
  
  private static long hash(double paramDouble)
  {
    if (0.0D == paramDouble) {
      return 0L;
    }
    return Double.doubleToLongBits(paramDouble);
  }
  
  private static long mix64(long paramLong)
  {
    paramLong ^= paramLong >>> 33;
    paramLong *= -49064778989728563L;
    paramLong ^= paramLong >>> 33;
    paramLong *= -4265267296055464877L;
    paramLong ^= paramLong >>> 33;
    return paramLong;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/DefaultHasher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */