package com.simba.support.conv;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharConverter
{
  public static boolean toBoolean(String paramString, ConversionResult paramConversionResult)
  {
    paramString = paramString.trim();
    if ("true".equalsIgnoreCase(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return true;
    }
    if ("false".equalsIgnoreCase(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return false;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
    return false;
  }
  
  public static BigInteger toBigInt(String paramString, ConversionResult paramConversionResult, boolean paramBoolean)
  {
    BigInteger localBigInteger1 = null;
    BigInteger localBigInteger2 = null;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_BIGINT_MAX;
      localBigInteger2 = ConverterConstants.SIGNED_BIGINT_MIN;
    }
    else
    {
      localBigInteger1 = ConverterConstants.UNSIGNED_BIGINT_MAX;
      localBigInteger2 = BigInteger.ZERO;
    }
    return convertInteger(paramString, paramConversionResult, localBigInteger2, localBigInteger1);
  }
  
  public static boolean toBit(String paramString, ConversionResult paramConversionResult)
  {
    paramString = paramString.trim();
    if (paramString.length() == 1)
    {
      if (paramString.charAt(0) == '0')
      {
        paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
        return false;
      }
      if (paramString.charAt(0) == '1')
      {
        paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
        return true;
      }
    }
    BigInteger localBigInteger1 = BigInteger.ONE;
    BigInteger localBigInteger2 = BigInteger.ZERO;
    BigInteger localBigInteger3 = convertInteger(paramString, paramConversionResult, localBigInteger2, localBigInteger1);
    if (null == localBigInteger3)
    {
      assert (ConversionResult.TypeConversionState.SUCCESS != paramConversionResult.getState());
      return false;
    }
    return localBigInteger3.compareTo(BigInteger.ONE) == 0;
  }
  
  public static String toChar(String paramString, long paramLong, ConversionResult paramConversionResult)
  {
    String str = paramString;
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
  
  public static Date toDate(String paramString, ConversionResult paramConversionResult, GregorianCalendar paramGregorianCalendar)
  {
    if (null == paramGregorianCalendar) {
      paramGregorianCalendar = new GregorianCalendar();
    }
    paramGregorianCalendar.clear();
    Matcher localMatcher = ConverterConstants.TIMESTAMP_PATTERN.matcher(paramString.trim());
    int[] arrayOfInt = parseTimestamp(paramGregorianCalendar, localMatcher);
    if (arrayOfInt == null)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    if (0 > arrayOfInt[0])
    {
      paramGregorianCalendar.set(0, 0);
      arrayOfInt[0] *= -1;
    }
    paramGregorianCalendar.set(arrayOfInt[0], arrayOfInt[1] - 1, arrayOfInt[2], 0, 0, 0);
    paramGregorianCalendar.set(14, 0);
    if ((arrayOfInt[3] != 0) || (arrayOfInt[4] != 0) || (arrayOfInt[5] != 0) || (arrayOfInt[6] != 0)) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.DATETIME_OVERFLOW);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return new Date(paramGregorianCalendar.getTimeInMillis());
  }
  
  public static double toDouble(String paramString, ConversionResult paramConversionResult)
  {
    paramString = paramString.trim();
    if (paramString.equalsIgnoreCase("NaN"))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return NaN.0D;
    }
    if (paramString.equalsIgnoreCase("INFINITY"))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return Double.POSITIVE_INFINITY;
    }
    if (paramString.equalsIgnoreCase("-INFINITY"))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return Double.NEGATIVE_INFINITY;
    }
    double d = 0.0D;
    try
    {
      d = Double.parseDouble(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return d;
    }
    if (d == Double.POSITIVE_INFINITY) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    } else if (d == Double.NEGATIVE_INFINITY) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return d;
  }
  
  public static BigDecimal toExactNum(String paramString, ConversionResult paramConversionResult, short paramShort1, short paramShort2)
  {
    paramString = paramString.trim();
    if (isNanStr(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    if (isPosInfStr(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return null;
    }
    if (isNegInfStr(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return null;
    }
    BigDecimal localBigDecimal;
    try
    {
      localBigDecimal = new BigDecimal(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    return ExactNumConverter.setPrecScale(localBigDecimal, paramShort1, paramShort2, paramConversionResult);
  }
  
  public static double toFloat(String paramString, ConversionResult paramConversionResult)
  {
    return toDouble(paramString, paramConversionResult);
  }
  
  public static UUID toGUID(String paramString, ConversionResult paramConversionResult)
  {
    paramString = paramString.trim();
    byte[] arrayOfByte = new byte[16];
    if ((36 != paramString.length()) || (!convertHex(paramString.substring(0, 8), arrayOfByte, 0)) || ('-' != paramString.charAt(8)) || (!convertHex(paramString.substring(9, 13), arrayOfByte, 4)) || ('-' != paramString.charAt(13)) || (!convertHex(paramString.substring(14, 18), arrayOfByte, 6)) || ('-' != paramString.charAt(18)) || (!convertHex(paramString.substring(19, 23), arrayOfByte, 8)) || ('-' != paramString.charAt(23)) || (!convertHex(paramString.substring(24), arrayOfByte, 10)))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    long l1 = 0L;
    for (int i = 0; i < 8; i++) {
      l1 = l1 << 8 | arrayOfByte[i] & 0xFF;
    }
    long l2 = 0L;
    for (int j = 8; j < 16; j++) {
      l2 = l2 << 8 | arrayOfByte[j] & 0xFF;
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    return new UUID(l1, l2);
  }
  
  public static long toInteger(String paramString, ConversionResult paramConversionResult, boolean paramBoolean)
  {
    BigInteger localBigInteger1 = null;
    BigInteger localBigInteger2 = null;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_INT_MAX;
      localBigInteger2 = ConverterConstants.SIGNED_INT_MIN;
    }
    else
    {
      localBigInteger1 = ConverterConstants.UNSIGNED_INT_MAX;
      localBigInteger2 = BigInteger.ZERO;
    }
    BigInteger localBigInteger3 = convertInteger(paramString, paramConversionResult, localBigInteger2, localBigInteger1);
    if (localBigInteger3 == null)
    {
      assert (ConversionResult.TypeConversionState.SUCCESS != paramConversionResult.getState());
      return -1L;
    }
    return localBigInteger3.longValue();
  }
  
  public static float toReal(String paramString, ConversionResult paramConversionResult)
  {
    paramString = paramString.trim();
    if (paramString.equalsIgnoreCase("NaN"))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return NaN.0F;
    }
    if (paramString.equalsIgnoreCase("INFINITY"))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return Float.POSITIVE_INFINITY;
    }
    if (paramString.equalsIgnoreCase("-INFINITY"))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return Float.NEGATIVE_INFINITY;
    }
    float f = 0.0F;
    try
    {
      f = Float.parseFloat(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return f;
    }
    if (f == Float.POSITIVE_INFINITY) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
    } else if (f == Float.NEGATIVE_INFINITY) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return f;
  }
  
  public static int toSmallint(String paramString, ConversionResult paramConversionResult, boolean paramBoolean)
  {
    BigInteger localBigInteger1 = null;
    BigInteger localBigInteger2 = null;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_SMALLINT_MAX;
      localBigInteger2 = ConverterConstants.SIGNED_SMALLINT_MIN;
    }
    else
    {
      localBigInteger1 = ConverterConstants.UNSIGNED_SMALLINT_MAX;
      localBigInteger2 = BigInteger.ZERO;
    }
    BigInteger localBigInteger3 = convertInteger(paramString, paramConversionResult, localBigInteger2, localBigInteger1);
    if (localBigInteger3 == null)
    {
      assert (ConversionResult.TypeConversionState.SUCCESS != paramConversionResult.getState());
      return -1;
    }
    return localBigInteger3.intValue();
  }
  
  public static Time toTime(String paramString, ConversionResult paramConversionResult, short paramShort, GregorianCalendar paramGregorianCalendar)
  {
    if ((paramShort < 0) || (paramShort > 9)) {
      throw new IllegalArgumentException("Invalid precision");
    }
    if (null == paramGregorianCalendar) {
      paramGregorianCalendar = new GregorianCalendar();
    }
    int[] arrayOfInt = parseTime(paramString, paramGregorianCalendar);
    if (arrayOfInt == null)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    paramGregorianCalendar.clear();
    paramGregorianCalendar.set(1970, 0, 1, arrayOfInt[3], arrayOfInt[4], arrayOfInt[5]);
    int i = DateTimeConverter.calculateFractionalSeconds(arrayOfInt[6], Math.min(paramShort, 3));
    paramGregorianCalendar.set(14, i / 1000000);
    Time localTime = new Time(paramGregorianCalendar.getTimeInMillis());
    if (i != arrayOfInt[6]) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    if ((arrayOfInt[0] != 0) || (arrayOfInt[1] != 0) || (arrayOfInt[2] != 0)) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.DATETIME_OVERFLOW);
    }
    return localTime;
  }
  
  public static Timestamp toTimestamp(String paramString, ConversionResult paramConversionResult, short paramShort, GregorianCalendar paramGregorianCalendar)
  {
    Matcher localMatcher = ConverterConstants.TIMESTAMP_PATTERN.matcher(paramString.trim());
    if (localMatcher.matches())
    {
      if ((paramShort < 0) || (paramShort > 9)) {
        throw new IllegalArgumentException("Invalid precision");
      }
      if (null == paramGregorianCalendar) {
        paramGregorianCalendar = new GregorianCalendar();
      }
      return doTsStrToTimestamp(paramConversionResult, paramShort, paramGregorianCalendar, localMatcher);
    }
    return timeStrToTimestamp(paramString, paramConversionResult, paramShort, paramGregorianCalendar);
  }
  
  public static short toTinyint(String paramString, ConversionResult paramConversionResult, boolean paramBoolean)
  {
    BigInteger localBigInteger1 = null;
    BigInteger localBigInteger2 = null;
    if (paramBoolean)
    {
      localBigInteger1 = ConverterConstants.SIGNED_TINYINT_MAX;
      localBigInteger2 = ConverterConstants.SIGNED_TINYINT_MIN;
    }
    else
    {
      localBigInteger1 = ConverterConstants.UNSIGNED_TINYINT_MAX;
      localBigInteger2 = BigInteger.ZERO;
    }
    BigInteger localBigInteger3 = convertInteger(paramString, paramConversionResult, localBigInteger2, localBigInteger1);
    if (localBigInteger3 == null) {
      return -1;
    }
    return localBigInteger3.shortValue();
  }
  
  public static Timestamp tsStrToTimestamp(String paramString, ConversionResult paramConversionResult, short paramShort, GregorianCalendar paramGregorianCalendar)
  {
    if ((paramShort < 0) || (paramShort > 9)) {
      throw new IllegalArgumentException("Invalid precision");
    }
    if (null == paramGregorianCalendar) {
      paramGregorianCalendar = new GregorianCalendar();
    }
    Matcher localMatcher = ConverterConstants.TIMESTAMP_PATTERN.matcher(paramString.trim());
    return doTsStrToTimestamp(paramConversionResult, paramShort, paramGregorianCalendar, localMatcher);
  }
  
  public static Timestamp timeStrToTimestamp(String paramString, ConversionResult paramConversionResult, short paramShort, GregorianCalendar paramGregorianCalendar)
  {
    if ((paramShort < 0) || (paramShort > 9)) {
      throw new IllegalArgumentException("Invalid precision");
    }
    if (null == paramGregorianCalendar) {
      paramGregorianCalendar = new GregorianCalendar();
    }
    int[] arrayOfInt = parseTime(paramString, paramGregorianCalendar);
    if (arrayOfInt == null)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    paramGregorianCalendar.clear();
    paramGregorianCalendar.set(1970, 0, 1, arrayOfInt[3], arrayOfInt[4], arrayOfInt[5]);
    int i = DateTimeConverter.calculateFractionalSeconds(arrayOfInt[6], Math.min(paramShort, 3));
    paramGregorianCalendar.set(14, i / 1000000);
    Timestamp localTimestamp = new Timestamp(paramGregorianCalendar.getTimeInMillis());
    if (i != arrayOfInt[6]) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return localTimestamp;
  }
  
  private static boolean convertHex(CharSequence paramCharSequence, byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramCharSequence.length();
    if (0 != i % 2) {
      return false;
    }
    int j = 0;
    while (j < i)
    {
      int k = Character.digit(paramCharSequence.charAt(j++), 16);
      int m = Character.digit(paramCharSequence.charAt(j++), 16);
      if ((0 > k) || (0 > m)) {
        return false;
      }
      paramArrayOfByte[(paramInt++)] = ((byte)(k << 4 | m));
    }
    return true;
  }
  
  private static BigInteger convertInteger(String paramString, ConversionResult paramConversionResult, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    paramString = paramString.trim();
    if (isNanStr(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    if (isPosInfStr(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return null;
    }
    if (isNegInfStr(paramString))
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return null;
    }
    BigDecimal localBigDecimal = null;
    try
    {
      localBigDecimal = new BigDecimal(paramString.trim());
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    BigInteger localBigInteger = localBigDecimal.toBigInteger();
    if (localBigInteger.compareTo(paramBigInteger1) < 0)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_SMALL);
      return null;
    }
    if (localBigInteger.compareTo(paramBigInteger2) > 0)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.NUMERIC_OUT_OF_RANGE_TOO_LARGE);
      return null;
    }
    if (localBigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0)
    {
      if (localBigDecimal.compareTo(BigDecimal.ZERO) > 0) {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
      } else {
        paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_UP);
      }
    }
    else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return localBigInteger;
  }
  
  private static Timestamp doTsStrToTimestamp(ConversionResult paramConversionResult, short paramShort, GregorianCalendar paramGregorianCalendar, Matcher paramMatcher)
  {
    int[] arrayOfInt = parseTimestamp(paramGregorianCalendar, paramMatcher);
    if (arrayOfInt == null)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.INVALID_CHAR_VAL_FOR_CAST);
      return null;
    }
    paramGregorianCalendar.clear();
    paramGregorianCalendar.set(0, arrayOfInt[0] < 0 ? 0 : 1);
    paramGregorianCalendar.set(Math.abs(arrayOfInt[0]), arrayOfInt[1] - 1, arrayOfInt[2], arrayOfInt[3], arrayOfInt[4], arrayOfInt[5]);
    Timestamp localTimestamp = new Timestamp(paramGregorianCalendar.getTimeInMillis());
    int i = DateTimeConverter.calculateFractionalSeconds(arrayOfInt[6], paramShort);
    localTimestamp.setNanos(i);
    if (i != arrayOfInt[6]) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return localTimestamp;
  }
  
  private static int[] parseTimestamp(GregorianCalendar paramGregorianCalendar, Matcher paramMatcher)
  {
    if (!paramMatcher.matches()) {
      return null;
    }
    int[] arrayOfInt = new int[7];
    for (int i = 0; i < 6; i++)
    {
      String str2 = paramMatcher.group(i + 1);
      arrayOfInt[i] = (str2 == null ? 0 : Integer.parseInt(str2));
    }
    if ((!checkGregorianDate(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], paramGregorianCalendar)) || (!checkTime(arrayOfInt[3], arrayOfInt[4], arrayOfInt[5]))) {
      return null;
    }
    String str1 = paramMatcher.group(7);
    if (str1 == null)
    {
      arrayOfInt[6] = 0;
      return arrayOfInt;
    }
    arrayOfInt[6] = (Integer.parseInt(str1) * ConverterConstants.FRACTIONAL_MOD[str1.length()]);
    return arrayOfInt;
  }
  
  private static int[] parseTime(String paramString, GregorianCalendar paramGregorianCalendar)
  {
    Matcher localMatcher = ConverterConstants.TIME_PATTERN.matcher(paramString.trim());
    if (!localMatcher.matches()) {
      return null;
    }
    String str1 = localMatcher.group(1);
    String str2 = localMatcher.group(2);
    String str3 = localMatcher.group(3);
    int[] arrayOfInt = new int[7];
    if (str1 != null)
    {
      arrayOfInt[0] = Integer.parseInt(str1);
      arrayOfInt[1] = Integer.parseInt(str2);
      arrayOfInt[2] = Integer.parseInt(str3);
      if (!checkGregorianDate(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], paramGregorianCalendar)) {
        return null;
      }
    }
    else
    {
      arrayOfInt[0] = 0;
      arrayOfInt[1] = 0;
      arrayOfInt[2] = 0;
    }
    arrayOfInt[3] = Integer.parseInt(localMatcher.group(4));
    arrayOfInt[4] = Integer.parseInt(localMatcher.group(5));
    arrayOfInt[5] = Integer.parseInt(localMatcher.group(6));
    if (!checkTime(arrayOfInt[3], arrayOfInt[4], arrayOfInt[5])) {
      return null;
    }
    String str4 = localMatcher.group(7);
    if (str4 == null)
    {
      arrayOfInt[6] = 0;
      return arrayOfInt;
    }
    arrayOfInt[6] = (Integer.parseInt(str4) * ConverterConstants.FRACTIONAL_MOD[str4.length()]);
    return arrayOfInt;
  }
  
  private static boolean checkGregorianDate(int paramInt1, int paramInt2, int paramInt3, GregorianCalendar paramGregorianCalendar)
  {
    if (paramInt1 == 0) {
      return false;
    }
    paramInt2--;
    paramGregorianCalendar.clear();
    paramGregorianCalendar.set(0, paramInt1 < 0 ? 0 : 1);
    paramGregorianCalendar.set(Math.abs(paramInt1), paramInt2, paramInt3);
    return (paramGregorianCalendar.get(1) == Math.abs(paramInt1)) && (paramGregorianCalendar.get(2) == paramInt2) && (paramGregorianCalendar.get(5) == paramInt3);
  }
  
  private static boolean checkTime(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 0) || (paramInt1 > 23)) {
      return false;
    }
    if ((paramInt2 < 0) || (paramInt2 > 59)) {
      return false;
    }
    return (paramInt3 >= 0) && (paramInt3 <= 61);
  }
  
  private static boolean isPosInfStr(String paramString)
  {
    return paramString.equalsIgnoreCase("INFINITY");
  }
  
  private static boolean isNegInfStr(String paramString)
  {
    return paramString.equalsIgnoreCase("-INFINITY");
  }
  
  private static boolean isNanStr(String paramString)
  {
    return paramString.equalsIgnoreCase("NaN");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/CharConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */