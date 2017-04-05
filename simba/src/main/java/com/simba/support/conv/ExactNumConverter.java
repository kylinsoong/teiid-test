package com.simba.support.conv;

import com.simba.support.Pair;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class ExactNumConverter
{
  private ExactNumConverter()
  {
    throw new UnsupportedOperationException();
  }
  
  public static Pair<Short, Short> calculateSQLPrecisionScale(BigDecimal paramBigDecimal, ConversionResult paramConversionResult)
  {
    if (paramBigDecimal.compareTo(BigDecimal.ZERO) == 0)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return new Pair(Short.valueOf((short)1), Short.valueOf((short)0));
    }
    int i = paramBigDecimal.scale() < 0 ? 0 : paramBigDecimal.scale();
    int j = paramBigDecimal.scale() < 0 ? paramBigDecimal.precision() - paramBigDecimal.scale() : paramBigDecimal.precision();
    if ((0 > j) || (32767 < j))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    }
    else if (32767 < i)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      i = 32767;
    }
    else
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return new Pair(Short.valueOf((short)j), Short.valueOf((short)i));
  }
  
  public static boolean toBoolean(BigDecimal paramBigDecimal, ConversionResult paramConversionResult)
  {
    int i = paramBigDecimal.compareTo(BigDecimal.ONE);
    int j = paramBigDecimal.compareTo(BigDecimal.ZERO);
    if (0 == i)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return true;
    }
    if (0 == j)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return false;
    }
    if (0 < i)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return true;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
    return true;
  }
  
  public static double toDouble(BigDecimal paramBigDecimal, ConversionResult paramConversionResult)
  {
    double d = paramBigDecimal.doubleValue();
    if (Double.POSITIVE_INFINITY == d) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    } else if (Double.NEGATIVE_INFINITY == d) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return d;
  }
  
  public static float toFloat(BigDecimal paramBigDecimal, ConversionResult paramConversionResult)
  {
    double d = toDouble(paramBigDecimal, paramConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS != paramConversionResult.getState()) {
      return (float)d;
    }
    return ApproxNumConverter.toFloat(d, paramConversionResult);
  }
  
  public static String toChar(BigDecimal paramBigDecimal, long paramLong, ConversionResult paramConversionResult)
  {
    String str = paramBigDecimal.toString();
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
  
  public static BigInteger toBigInt(BigDecimal paramBigDecimal, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger1 = paramBigDecimal.toBigInteger();
    BigInteger localBigInteger2 = IntegralConverter.toBigInt(localBigInteger1, paramBoolean, paramConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS == paramConversionResult.getState())
    {
      int i = paramBigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO);
      if (i > 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else if (i < 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      }
      return localBigInteger2;
    }
    return null;
  }
  
  public static long toInteger(BigDecimal paramBigDecimal, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger = paramBigDecimal.toBigInteger();
    long l = IntegralConverter.toInteger(localBigInteger, paramBoolean, paramConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS == paramConversionResult.getState())
    {
      int i = paramBigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO);
      if (i > 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else if (i < 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      }
    }
    return l;
  }
  
  public static int toSmallInt(BigDecimal paramBigDecimal, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger = paramBigDecimal.toBigInteger();
    int i = IntegralConverter.toSmallInt(localBigInteger, paramBoolean, paramConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS == paramConversionResult.getState())
    {
      int j = paramBigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO);
      if (j > 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else if (j < 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      }
    }
    return i;
  }
  
  public static short toTinyInt(BigDecimal paramBigDecimal, boolean paramBoolean, ConversionResult paramConversionResult)
  {
    BigInteger localBigInteger = paramBigDecimal.toBigInteger();
    short s = IntegralConverter.toTinyInt(localBigInteger, paramBoolean, paramConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS == paramConversionResult.getState())
    {
      int i = paramBigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO);
      if (i > 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else if (i < 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      }
    }
    return s;
  }
  
  public static BigDecimal setPrecScale(BigDecimal paramBigDecimal, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
  {
    if ((paramShort1 < 1) || (paramShort2 > paramShort1) || (paramShort2 < 0)) {
      throw new IllegalArgumentException("Invalid precision scale");
    }
    ConversionResult localConversionResult = new ConversionResult();
    Pair localPair = calculateSQLPrecisionScale(paramBigDecimal, localConversionResult);
    if (ConversionResult.TypeConversionState.SUCCESS != localConversionResult.getState())
    {
      paramConversionResult.setState(localConversionResult.getState());
      return null;
    }
    int i = ((Short)localPair.key()).shortValue() - ((Short)localPair.value()).shortValue();
    int j = paramShort1 - paramShort2;
    if (i > j)
    {
      if (paramBigDecimal.compareTo(BigDecimal.ZERO) < 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      }
      return null;
    }
    if (((Short)localPair.value()).shortValue() <= paramShort2)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return paramBigDecimal;
    }
    BigDecimal localBigDecimal = paramBigDecimal.setScale(paramShort2, RoundingMode.DOWN);
    int k = paramBigDecimal.compareTo(localBigDecimal);
    if (k < 0) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
    } else if (k == 0) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    }
    return localBigDecimal;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/ExactNumConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */