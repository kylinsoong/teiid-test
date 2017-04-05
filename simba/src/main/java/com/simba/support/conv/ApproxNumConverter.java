package com.simba.support.conv;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ApproxNumConverter
{
  private static final double SMALLEST_DOUBLE_GREATER_THAN_NEGATIVE_ONE = -1.0D + Math.ulp(1.0D);
  private static final double SBIGINT_MIN = ConverterConstants.SIGNED_BIGINT_MIN.doubleValue();
  private static final double SBIGINT_MAX_PLUS_ONE = ConverterConstants.SIGNED_BIGINT_MAX.doubleValue();
  private static final double UBIGINT_MAX_PLUS_ONE = ConverterConstants.UNSIGNED_BIGINT_MAX.doubleValue();
  
  private ApproxNumConverter()
  {
    throw new UnsupportedOperationException();
  }
  
  public static BigDecimal toBigDecimal(double paramDouble, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
  {
    if (Double.isNaN(paramDouble))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_DATA);
      return null;
    }
    if (Double.POSITIVE_INFINITY == paramDouble)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return null;
    }
    if (Double.NEGATIVE_INFINITY == paramDouble)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return null;
    }
    return ExactNumConverter.setPrecScale(new BigDecimal(paramDouble), paramShort1, paramShort2, paramConversionResult);
  }
  
  public static BigInteger toBigInt(double paramDouble, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    if (Double.isNaN(paramDouble))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_DATA);
      return null;
    }
    double d = paramBoolean ? SBIGINT_MIN : SMALLEST_DOUBLE_GREATER_THAN_NEGATIVE_ONE;
    if (paramDouble >= SBIGINT_MAX_PLUS_ONE)
    {
      if ((!paramBoolean) && (paramDouble < UBIGINT_MAX_PLUS_ONE))
      {
        paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
        BigDecimal localBigDecimal = new BigDecimal(paramDouble);
        return localBigDecimal.toBigIntegerExact();
      }
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return null;
    }
    if (paramDouble < d)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return null;
    }
    long l = paramDouble;
    if (l == paramDouble) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(0.0D < paramDouble ? ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN : ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
    }
    return BigInteger.valueOf(l);
  }
  
  public static String toChar(double paramDouble, long paramLong, ConversionResult paramConversionResult)
  {
    String str;
    if (Double.POSITIVE_INFINITY == paramDouble) {
      str = "INFINITY";
    } else if (Double.NEGATIVE_INFINITY == paramDouble) {
      str = "-INFINITY";
    } else {
      str = Double.toString(paramDouble);
    }
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
  
  public static float toFloat(double paramDouble, ConversionResult paramConversionResult)
  {
    paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    return (float)paramDouble;
  }
  
  public static long toInteger(double paramDouble, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    long l = 0L;
    if (Double.isNaN(paramDouble))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_DATA);
      return l;
    }
    double d1;
    double d2;
    if (paramBoolean)
    {
      d1 = -2.147483648E9D;
      d2 = 2.147483647E9D;
    }
    else
    {
      d1 = 0.0D;
      d2 = 4.294967295E9D;
    }
    l = paramDouble;
    if (paramDouble > d2)
    {
      if (d2 == l) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      }
    }
    else if (paramDouble < d1)
    {
      if (d1 == l) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      }
    }
    else if (l == paramDouble) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(0.0D < paramDouble ? ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN : ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
    }
    return l;
  }
  
  public static int toSmallInt(double paramDouble, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    int i = 0;
    if (Double.isNaN(paramDouble))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_DATA);
      return i;
    }
    double d1;
    double d2;
    if (paramBoolean)
    {
      d1 = -32768.0D;
      d2 = 32767.0D;
    }
    else
    {
      d1 = 0.0D;
      d2 = 65535.0D;
    }
    i = (int)paramDouble;
    if (paramDouble > d2)
    {
      if ((int)d2 == i) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      }
    }
    else if (paramDouble < d1)
    {
      if ((int)d1 == i) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      }
    }
    else if (i == paramDouble) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(0.0D < paramDouble ? ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN : ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
    }
    return i;
  }
  
  public static short toTinyInt(double paramDouble, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    int i = 0;
    if (Double.isNaN(paramDouble))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_DATA);
      return (short)i;
    }
    double d1;
    double d2;
    if (paramBoolean)
    {
      d1 = -128.0D;
      d2 = 127.0D;
    }
    else
    {
      d1 = 0.0D;
      d2 = 255.0D;
    }
    i = (int)paramDouble;
    if (paramDouble > d2)
    {
      if ((int)d2 == i) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      }
    }
    else if (paramDouble < d1)
    {
      if ((int)d1 == i) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      }
    }
    else if (i == paramDouble) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(0.0D < paramDouble ? ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN : ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
    }
    return (short)i;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/ApproxNumConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */