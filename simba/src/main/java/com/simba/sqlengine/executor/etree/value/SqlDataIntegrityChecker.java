package com.simba.sqlengine.executor.etree.value;

import java.math.BigInteger;

public class SqlDataIntegrityChecker
{
  public static final long UNSIGNED_INT_MAX = 4294967295L;
  public static final long SIGNED_INT_MAX = 2147483647L;
  public static final long SIGNED_INT_MIN = -2147483648L;
  public static final int UNSIGNED_SMALLINT_MAX = 65535;
  public static final int SIGNED_SMALLINT_MAX = 32767;
  public static final int SIGNED_SMALLINT_MIN = -32768;
  public static final short UNSIGNED_TINYINT_MAX = 255;
  public static final short SIGNED_TINYINT_MAX = 127;
  public static final short SIGNED_TINYINT_MIN = -128;
  public static final BigInteger UNSIGNED_BIGINT_MAX = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2L)).add(BigInteger.ONE);
  public static final BigInteger SIGNED_BIGINT_MAX = BigInteger.valueOf(Long.MAX_VALUE);
  public static final BigInteger SIGNED_BIGINT_MIN = BigInteger.valueOf(Long.MIN_VALUE);
  
  public static boolean checkInteger(long paramLong, boolean paramBoolean)
  {
    if (paramBoolean) {
      return (paramLong <= 2147483647L) && (paramLong >= -2147483648L);
    }
    return (paramLong >= 0L) && (paramLong <= 4294967295L);
  }
  
  public static boolean checkSmallInt(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      return (paramInt <= 32767) && (paramInt >= 32768);
    }
    return (paramInt >= 0) && (paramInt <= 65535);
  }
  
  public static boolean checkTinyInt(short paramShort, boolean paramBoolean)
  {
    if (paramBoolean) {
      return (paramShort <= 127) && (paramShort >= -128);
    }
    return (paramShort >= 0) && (paramShort <= 255);
  }
  
  public static boolean checkBigInt(BigInteger paramBigInteger, boolean paramBoolean)
  {
    if (paramBoolean) {
      return (paramBigInteger.compareTo(SIGNED_BIGINT_MAX) <= 0) && (paramBigInteger.compareTo(SIGNED_BIGINT_MIN) >= 0);
    }
    return (paramBigInteger.compareTo(BigInteger.ZERO) >= 0) && (paramBigInteger.compareTo(UNSIGNED_BIGINT_MAX) <= 0);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/SqlDataIntegrityChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */