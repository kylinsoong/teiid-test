package com.simba.support.conv;

import java.math.BigInteger;
import java.util.regex.Pattern;

public class ConverterConstants
{
  public static final String POS_INF_STR = "INFINITY";
  public static final String NEG_INF_STR = "-INFINITY";
  public static final String NAN_STR = "NaN";
  public static final BigInteger UNSIGNED_INT_MAX = BigInteger.valueOf(4294967295L);
  public static final BigInteger SIGNED_INT_MAX = BigInteger.valueOf(2147483647L);
  public static final BigInteger SIGNED_INT_MIN = BigInteger.valueOf(-2147483648L);
  public static final BigInteger UNSIGNED_SMALLINT_MAX = BigInteger.valueOf(65535L);
  public static final BigInteger SIGNED_SMALLINT_MAX = BigInteger.valueOf(32767L);
  public static final BigInteger SIGNED_SMALLINT_MIN = BigInteger.valueOf(-32768L);
  public static final BigInteger UNSIGNED_TINYINT_MAX = BigInteger.valueOf(255L);
  public static final BigInteger SIGNED_TINYINT_MAX = BigInteger.valueOf(127L);
  public static final BigInteger SIGNED_TINYINT_MIN = BigInteger.valueOf(-128L);
  public static final BigInteger SIGNED_BIGINT_MAX = BigInteger.valueOf(Long.MAX_VALUE);
  public static final BigInteger SIGNED_BIGINT_MIN = BigInteger.valueOf(Long.MIN_VALUE);
  public static final BigInteger UNSIGNED_BIGINT_MAX = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2L)).add(BigInteger.ONE);
  public static final BigInteger MAX_INTEGRAL_IN_DOUBLE_INVARIANT = BigInteger.valueOf(9007199254740992L);
  public static final BigInteger MIN_INTEGRAL_IN_DOUBLE_INVARIANT = BigInteger.valueOf(-9007199254740992L);
  public static final BigInteger MAX_INTEGRAL_IN_FLOAT_INVARIANT = BigInteger.valueOf(16777216L);
  public static final BigInteger MIN_INTEGRAL_IN_FLOAT_INVARIANT = BigInteger.valueOf(-16777216L);
  public static final Pattern TIMESTAMP_PATTERN = Pattern.compile("(-?\\d{1,9})-(\\d{1,2})-(\\d{1,2})(?:\\s+(\\d{1,2}):(\\d{1,2}):(\\d{1,2})(?:\\.(\\d{1,9}))?)?");
  public static final Pattern TIME_PATTERN = Pattern.compile("(?:(-?\\d{1,9})-(\\d{1,2})-(\\d{1,2})\\s+)?(\\d{1,2}):(\\d{1,2}):(\\d{1,2})(?:\\.(\\d{1,9}))?");
  public static final int UUID_LENGTH = 36;
  public static final int[] MILLIS_MOD = { 1000, 100, 10, 1 };
  public static final int[] FRACTIONAL_MOD = { 1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1 };
  public static final String s_dateFormat = "yyyy-MM-dd";
  public static final int MAX_TINYINT_DIGITS = 3;
  public static final int MAX_SMALLINT_DIGITS = 5;
  public static final int MAX_INT_DIGITS = 10;
  public static final int MAX_BIGINT_DIGITS = 20;
  public static final int[] MONTHS_DAY = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/ConverterConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */