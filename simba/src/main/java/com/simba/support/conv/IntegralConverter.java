package com.simba.support.conv;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntegralConverter
{
  public static String bitToChar(boolean paramBoolean, long paramLong, ConversionResult paramConversionResult)
  {
    return toChar(paramBoolean ? 1 : 0, paramLong, paramConversionResult);
  }
  
  public static String booleanToChar(boolean paramBoolean, long paramLong, ConversionResult paramConversionResult)
  {
    String str = String.valueOf(paramBoolean);
    if (str.length() <= paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    else
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
      str = str.substring(0, (int)paramLong);
    }
    return str;
  }
  
  public static boolean toBit(long paramLong, ConversionResult paramConversionResult)
  {
    if (0L == paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return false;
    }
    if (1L == paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return true;
    }
    if (paramLong < 0L)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return false;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    return false;
  }
  
  public static boolean toBit(BigInteger paramBigInteger, ConversionResult paramConversionResult)
  {
    if (BigInteger.ZERO.equals(paramBigInteger))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return false;
    }
    if (BigInteger.ONE.equals(paramBigInteger))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return true;
    }
    if (paramBigInteger.compareTo(BigInteger.ZERO) < 0)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return false;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    return false;
  }
  
  public static BigDecimal toBigDecimal(long paramLong, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
  {
    return ExactNumConverter.setPrecScale(new BigDecimal(paramLong), paramShort1, paramShort2, paramConversionResult);
  }
  
  public static BigDecimal toBigDecimal(BigInteger paramBigInteger, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
  {
    return ExactNumConverter.setPrecScale(new BigDecimal(paramBigInteger), paramShort1, paramShort2, paramConversionResult);
  }
  
  public static BigDecimal toBigDecimal(boolean paramBoolean, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
  {
    if ((paramShort1 < 1) || (paramShort2 > paramShort1) || (paramShort2 < 0)) {
      throw new IllegalArgumentException("Invalid precision scale");
    }
    if (!paramBoolean)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return BigDecimal.ZERO;
    }
    if (paramShort1 == paramShort2)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return null;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    return BigDecimal.ONE;
  }
  
  public static String toChar(BigInteger paramBigInteger, long paramLong, ConversionResult paramConversionResult)
  {
    String str = paramBigInteger.toString();
    if (str.length() <= paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    else
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
      str = str.substring(0, (int)paramLong);
    }
    return str;
  }
  
  public static String toChar(long paramLong1, long paramLong2, ConversionResult paramConversionResult)
  {
    String str = String.valueOf(paramLong1);
    if (str.length() <= paramLong2)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    else
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
      str = str.substring(0, (int)paramLong2);
    }
    return str;
  }
  
  public static BigInteger toBigInt(long paramLong, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    if ((!paramBoolean) && (0L > paramLong))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return null;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    return BigInteger.valueOf(paramLong);
  }
  
  public static BigInteger toBigInt(BigInteger paramBigInteger, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_BIGINT_MIN;
      localBigInteger2 = ConverterConstants.SIGNED_BIGINT_MAX;
    }
    else
    {
      localBigInteger1 = BigInteger.ZERO;
      localBigInteger2 = ConverterConstants.UNSIGNED_BIGINT_MAX;
    }
    checkRange(paramBigInteger, localBigInteger1, localBigInteger2, paramConversionResult);
    return paramBigInteger;
  }
  
  public static double toDouble(BigInteger paramBigInteger, ConversionResult paramConversionResult)
  {
    int i = paramBigInteger.compareTo(ConverterConstants.MAX_INTEGRAL_IN_DOUBLE_INVARIANT);
    int j = paramBigInteger.compareTo(ConverterConstants.MIN_INTEGRAL_IN_DOUBLE_INVARIANT);
    if ((i <= 0) && (j >= 0))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return paramBigInteger.longValue();
    }
    double d = paramBigInteger.doubleValue();
    checkIntegralToDoubleExact(paramBigInteger, d, paramConversionResult);
    return d;
  }
  
  public static float toFloat(BigInteger paramBigInteger, ConversionResult paramConversionResult)
  {
    int i = paramBigInteger.compareTo(ConverterConstants.MAX_INTEGRAL_IN_FLOAT_INVARIANT);
    int j = paramBigInteger.compareTo(ConverterConstants.MIN_INTEGRAL_IN_FLOAT_INVARIANT);
    if ((i <= 0) && (j >= 0))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return (float)paramBigInteger.longValue();
    }
    float f = (float)paramBigInteger.doubleValue();
    checkIntegralToFloatExact(paramBigInteger, f, paramConversionResult);
    return f;
  }
  
  public static float toFloat(long paramLong, ConversionResult paramConversionResult)
  {
    float f = (float)paramLong;
    if ((paramLong <= ConverterConstants.MAX_INTEGRAL_IN_FLOAT_INVARIANT.longValue()) || (paramLong >= ConverterConstants.MIN_INTEGRAL_IN_FLOAT_INVARIANT.longValue())) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      checkIntegralToFloatExact(BigInteger.valueOf(paramLong), f, paramConversionResult);
    }
    return f;
  }
  
  public static long toInteger(long paramLong, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    long l1;
    long l2;
    if (paramBoolean)
    {
      l1 = -2147483648L;
      l2 = 2147483647L;
    }
    else
    {
      l1 = 0L;
      l2 = 4294967295L;
    }
    checkRange(paramLong, l1, l2, paramConversionResult);
    return paramLong;
  }
  
  public static long toInteger(BigInteger paramBigInteger, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_INT_MIN;
      localBigInteger2 = ConverterConstants.SIGNED_INT_MAX;
    }
    else
    {
      localBigInteger1 = BigInteger.ZERO;
      localBigInteger2 = ConverterConstants.UNSIGNED_INT_MAX;
    }
    checkRange(paramBigInteger, localBigInteger1, localBigInteger2, paramConversionResult);
    return paramBigInteger.longValue();
  }
  
  public static int toSmallInt(long paramLong, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    long l1;
    long l2;
    if (paramBoolean)
    {
      l1 = -32768L;
      l2 = 32767L;
    }
    else
    {
      l1 = 0L;
      l2 = 65535L;
    }
    checkRange(paramLong, l1, l2, paramConversionResult);
    return (int)paramLong;
  }
  
  public static int toSmallInt(BigInteger paramBigInteger, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_SMALLINT_MIN;
      localBigInteger2 = ConverterConstants.SIGNED_SMALLINT_MAX;
    }
    else
    {
      localBigInteger1 = BigInteger.ZERO;
      localBigInteger2 = ConverterConstants.UNSIGNED_SMALLINT_MAX;
    }
    checkRange(paramBigInteger, localBigInteger1, localBigInteger2, paramConversionResult);
    return paramBigInteger.intValue();
  }
  
  public static short toTinyInt(long paramLong, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    long l1;
    long l2;
    if (paramBoolean)
    {
      l1 = -128L;
      l2 = 127L;
    }
    else
    {
      l1 = 0L;
      l2 = 255L;
    }
    checkRange(paramLong, l1, l2, paramConversionResult);
    return (short)(int)paramLong;
  }
  
  public static short toTinyInt(BigInteger paramBigInteger, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_TINYINT_MIN;
      localBigInteger2 = ConverterConstants.SIGNED_TINYINT_MAX;
    }
    else
    {
      localBigInteger1 = BigInteger.ZERO;
      localBigInteger2 = ConverterConstants.UNSIGNED_TINYINT_MAX;
    }
    checkRange(paramBigInteger, localBigInteger1, localBigInteger2, paramConversionResult);
    return paramBigInteger.shortValue();
  }
  
  private static void checkIntegralToDoubleExact(BigInteger paramBigInteger, double paramDouble, ConversionResult paramConversionResult)
  {
    BigDecimal localBigDecimal = new BigDecimal(paramDouble);
    if (paramBigInteger.equals(localBigDecimal.toBigInteger())) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INTEGRAL_PRECISION_LOSS);
    }
  }
  
  private static void checkIntegralToFloatExact(BigInteger paramBigInteger, float paramFloat, ConversionResult paramConversionResult)
  {
    BigDecimal localBigDecimal = new BigDecimal(paramFloat);
    if (paramBigInteger.equals(localBigDecimal.toBigInteger())) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INTEGRAL_PRECISION_LOSS);
    }
  }
  
  private static void checkRange(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, ConversionResult paramConversionResult)
  {
    assert (paramBigInteger3.compareTo(paramBigInteger2) >= 0);
    if (paramBigInteger1.compareTo(paramBigInteger2) < 0) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
    } else if (paramBigInteger1.compareTo(paramBigInteger3) > 0) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
  }
  
  private static void checkRange(long paramLong1, long paramLong2, long paramLong3, ConversionResult paramConversionResult)
  {
    assert (paramLong3 >= paramLong2);
    if (paramLong1 < paramLong2) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
    } else if (paramLong1 > paramLong3) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/IntegralConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */