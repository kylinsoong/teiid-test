package com.simba.support.conv;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeConverter
{
  public static int calculateFractionalSeconds(int paramInt1, int paramInt2)
  {
    return paramInt1 - paramInt1 % ConverterConstants.FRACTIONAL_MOD[paramInt2];
  }
  
  public static String toChar(Date paramDate, long paramLong, Calendar paramCalendar, ConversionResult paramConversionResult)
  {
    paramCalendar.clear();
    paramCalendar.set(1, 0, 1, 0, 0, 0);
    paramCalendar.set(14, 0);
    long l = paramCalendar.getTimeInMillis();
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    localSimpleDateFormat.setTimeZone(paramCalendar.getTimeZone());
    String str = localSimpleDateFormat.format(paramDate);
    if (paramDate.getTime() < l) {
      str = '-' + str;
    }
    if (str.length() > paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
      str = str.substring(0, (int)paramLong);
    }
    else
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return str;
  }
  
  public static String toChar(Timestamp paramTimestamp, long paramLong, int paramInt, Calendar paramCalendar, ConversionResult paramConversionResult)
  {
    char[] arrayOfChar = { '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '-', '0', '0', '-', '0', '0', ' ', '0', '0', ':', '0', '0', ':', '0', '0', '.', '0', '0', '0', '0', '0', '0', '0', '0', '0' };
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramTimestamp.getTime());
    int i = paramCalendar.get(1);
    int j = paramCalendar.get(2) + 1;
    int k = paramCalendar.get(5);
    int m = paramCalendar.get(11);
    int n = paramCalendar.get(12);
    int i1 = paramCalendar.get(13);
    int i2;
    if (i <= 9999)
    {
      i2 = 6;
      arrayOfChar[6] = ((char)(48 + i / 1000));
    }
    else if (i <= 999999)
    {
      if (i <= 99999)
      {
        i2 = 5;
        arrayOfChar[5] = ((char)(48 + i / 10000 % 10));
        arrayOfChar[6] = ((char)(48 + i / 1000 % 10));
      }
      else
      {
        i2 = 4;
        arrayOfChar[4] = ((char)(48 + i / 100000 % 10));
        arrayOfChar[5] = ((char)(48 + i / 10000 % 10));
        arrayOfChar[6] = ((char)(48 + i / 1000 % 10));
      }
    }
    else if (i <= 9999999)
    {
      i2 = 3;
      arrayOfChar[3] = ((char)(48 + i / 1000000 % 10));
      arrayOfChar[4] = ((char)(48 + i / 100000 % 10));
      arrayOfChar[5] = ((char)(48 + i / 10000 % 10));
      arrayOfChar[6] = ((char)(48 + i / 1000 % 10));
    }
    else if (i <= 99999999)
    {
      i2 = 2;
      arrayOfChar[2] = ((char)(48 + i / 10000000 % 10));
      arrayOfChar[3] = ((char)(48 + i / 1000000 % 10));
      arrayOfChar[4] = ((char)(48 + i / 100000 % 10));
      arrayOfChar[5] = ((char)(48 + i / 10000 % 10));
      arrayOfChar[6] = ((char)(48 + i / 1000 % 10));
    }
    else
    {
      i2 = 1;
      arrayOfChar[1] = ((char)(48 + i / 100000000 % 10));
      arrayOfChar[2] = ((char)(48 + i / 10000000 % 10));
      arrayOfChar[3] = ((char)(48 + i / 1000000 % 10));
      arrayOfChar[4] = ((char)(48 + i / 100000 % 10));
      arrayOfChar[5] = ((char)(48 + i / 10000 % 10));
      arrayOfChar[6] = ((char)(48 + i / 1000 % 10));
    }
    arrayOfChar[7] = ((char)(48 + i / 100 % 10));
    arrayOfChar[8] = ((char)(48 + i / 10 % 10));
    arrayOfChar[9] = ((char)(48 + i % 10));
    arrayOfChar[11] = ((char)(48 + j / 10));
    arrayOfChar[12] = ((char)(48 + j % 10));
    arrayOfChar[14] = ((char)(48 + k / 10));
    arrayOfChar[15] = ((char)(48 + k % 10));
    arrayOfChar[17] = ((char)(48 + m / 10));
    arrayOfChar[18] = ((char)(48 + m % 10));
    arrayOfChar[20] = ((char)(48 + n / 10));
    arrayOfChar[21] = ((char)(48 + n % 10));
    arrayOfChar[23] = ((char)(48 + i1 / 10));
    arrayOfChar[24] = ((char)(48 + i1 % 10));
    paramCalendar.clear();
    paramCalendar.set(1, 0, 1, 0, 0, 0);
    paramCalendar.set(14, 0);
    long l = paramCalendar.getTimeInMillis();
    if (paramTimestamp.getTime() < l)
    {
      i2--;
      arrayOfChar[i2] = '-';
    }
    int i3 = 25 - i2;
    if (0 < paramInt)
    {
      int i4 = paramTimestamp.getNanos();
      arrayOfChar[26] = ((char)(48 + i4 / 100000000));
      arrayOfChar[27] = ((char)(48 + i4 / 10000000 % 10));
      arrayOfChar[28] = ((char)(48 + i4 / 1000000 % 10));
      arrayOfChar[29] = ((char)(48 + i4 / 100000 % 10));
      arrayOfChar[30] = ((char)(48 + i4 / 10000 % 10));
      arrayOfChar[31] = ((char)(48 + i4 / 1000 % 10));
      arrayOfChar[32] = ((char)(48 + i4 / 100 % 10));
      arrayOfChar[33] = ((char)(48 + i4 / 10 % 10));
      arrayOfChar[34] = ((char)(48 + i4 % 10));
      i3 = 26 + paramInt - i2;
    }
    if (i3 > paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
      i3 = (int)paramLong;
    }
    else
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    String str = new String(arrayOfChar, i2, i3);
    return str;
  }
  
  public static String toChar(Time paramTime, long paramLong, int paramInt, Calendar paramCalendar, ConversionResult paramConversionResult)
  {
    assert (paramInt >= 0) : "Negative source precision.";
    char[] arrayOfChar = { '0', '0', ':', '0', '0', ':', '0', '0', '.', '0', '0', '0' };
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramTime.getTime());
    paramCalendar.set(1970, 0, 1);
    int i = paramCalendar.get(11);
    int j = paramCalendar.get(12);
    int k = paramCalendar.get(13);
    int m = paramCalendar.get(14);
    arrayOfChar[0] = ((char)(48 + i / 10));
    arrayOfChar[1] = ((char)(48 + i % 10));
    arrayOfChar[3] = ((char)(48 + j / 10));
    arrayOfChar[4] = ((char)(48 + j % 10));
    arrayOfChar[6] = ((char)(48 + k / 10));
    arrayOfChar[7] = ((char)(48 + k % 10));
    for (int n = 11; n > 8; n--)
    {
      arrayOfChar[n] = ((char)(48 + m % 10));
      m /= 10;
    }
    if (0 >= paramInt) {
      paramInt = -1;
    } else if (3 < paramInt) {
      paramInt = 3;
    }
    n = 9 + paramInt;
    if (n <= paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    else
    {
      n = (int)paramLong;
      paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
    }
    String str = new String(arrayOfChar, 0, n);
    return str;
  }
  
  public static Date toDate(Timestamp paramTimestamp, Calendar paramCalendar, ConversionResult paramConversionResult)
  {
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramTimestamp.getTime());
    paramCalendar.set(10, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
    paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    return new Date(paramCalendar.getTimeInMillis());
  }
  
  public static Time toTime(Timestamp paramTimestamp, Calendar paramCalendar, int paramInt, ConversionResult paramConversionResult)
  {
    assert (paramInt >= 0) : "negative precision";
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramTimestamp.getTime());
    if ((paramCalendar instanceof GregorianCalendar)) {
      paramCalendar.set(0, 1);
    }
    paramCalendar.set(1970, 0, 1);
    int i = paramTimestamp.getNanos();
    int j = i % ConverterConstants.FRACTIONAL_MOD[Math.min(paramInt, 3)];
    if (0 != j) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    paramCalendar.set(14, (i - j) / 1000000);
    return new Time(paramCalendar.getTimeInMillis());
  }
  
  public static Time toTime(Time paramTime, ConversionResult paramConversionResult, int paramInt, Calendar paramCalendar)
  {
    assert (paramInt >= 0);
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramTime.getTime());
    paramCalendar.set(1970, 0, 1);
    int i = paramCalendar.get(14);
    int j = i - i % ConverterConstants.MILLIS_MOD[Math.min(paramInt, 3)];
    paramCalendar.set(14, j);
    if (i != j) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    Time localTime = new Time(paramCalendar.getTimeInMillis());
    return localTime;
  }
  
  public static Timestamp toTimestamp(Date paramDate, ConversionResult paramConversionResult, Calendar paramCalendar)
  {
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramDate.getTime());
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
    Timestamp localTimestamp = new Timestamp(paramCalendar.getTimeInMillis());
    localTimestamp.setNanos(0);
    paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    return localTimestamp;
  }
  
  public static Timestamp toTimestamp(Timestamp paramTimestamp, ConversionResult paramConversionResult, int paramInt)
  {
    assert (paramInt >= 0);
    int i = paramTimestamp.getNanos();
    int j = calculateFractionalSeconds(i, paramInt);
    if (i != j) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    Timestamp localTimestamp = new Timestamp(paramTimestamp.getTime());
    localTimestamp.setNanos(j);
    return localTimestamp;
  }
  
  public static Timestamp toTimestamp(Time paramTime, ConversionResult paramConversionResult, Calendar paramCalendar, int paramInt)
  {
    assert (paramInt >= 0);
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(paramTime.getTime());
    paramCalendar.set(1, 1970);
    paramCalendar.set(2, 0);
    paramCalendar.set(5, 1);
    Timestamp localTimestamp = new Timestamp(paramCalendar.getTimeInMillis());
    int i = localTimestamp.getNanos();
    int j = calculateFractionalSeconds(i, paramInt);
    if (i != j) {
      paramConversionResult.setState(ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN);
    } else {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    return localTimestamp;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/DateTimeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */