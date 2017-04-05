package com.simba.sqlengine.executor.etree.util;

import com.simba.support.conv.ConverterConstants;
import java.math.BigInteger;

public class CompressionUtil
{
  public static BigInteger getlongAsBigInteger(long paramLong, boolean paramBoolean)
  {
    if (!paramBoolean) {
      return BigInteger.valueOf(paramLong).and(ConverterConstants.UNSIGNED_BIGINT_MAX);
    }
    return BigInteger.valueOf(paramLong);
  }
  
  public static long getIntAsLong(int paramInt, boolean paramBoolean)
  {
    if (!paramBoolean) {
      return 0xFFFFFFFF & paramInt;
    }
    return paramInt;
  }
  
  public static int getSmallIntAsInteger(short paramShort, boolean paramBoolean)
  {
    if (!paramBoolean) {
      return 0xFFFF & paramShort;
    }
    return paramShort;
  }
  
  public static short getTinyIntAsShort(byte paramByte, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      int i = 0xFF & paramByte;
      return (short)i;
    }
    return (short)paramByte;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/util/CompressionUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */