package com.simba.utilities.conversion;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.utilities.DSIMonthSpan;
import com.simba.dsi.dataengine.utilities.DSITimeSpan;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.InvalidArgumentException;
import com.simba.exceptions.JDBCMessageBuilder;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;

public class IntervalConverter
{
  private static final char DAY_HOUR_SEPARATOR = ' ';
  private static final char HOUR_MINUTE_SECOND_SEPARATOR = ':';
  private static final char SECOND_FRACTION_SEPARATOR = '.';
  private static final char YEAR_MONTH_SEPARATOR = '-';
  private static final char MINUS_SIGN = '-';
  private static final char ZERO_DIGIT = '0';
  private static final int INTERVAL_LEADING_PRECISION = 9;
  private static final short INTERVAL_FRACTIONAL_PRECISION = 9;
  private static boolean s_isLeadingFieldPadded;
  static final String[] ZERO_BUFFER = { "", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000" };
  private static final int MONTHS_PER_YEAR = 12;
  private static final int HOURS_PER_DAY = 24;
  private static final int MINUTES_PER_HOUR = 60;
  private static final int MINUTES_PER_DAY = 1440;
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int SECONDS_PER_HOUR = 3600;
  private static final int SECONDS_PER_DAY = 86400;
  
  public static String convertIntervalDayToHourToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(16);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getDay(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append(' ');
    int i = localDSITimeSpan.getHour();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalDayToMinuteToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(19);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getDay(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append(' ');
    int i = localDSITimeSpan.getHour();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    localStringBuilder.append(':');
    int j = localDSITimeSpan.getMinute();
    localStringBuilder.append((char)(48 + j / 10));
    localStringBuilder.append((char)(48 + j % 10));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalDayToSecondToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(30);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getDay(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append(' ');
    int i = localDSITimeSpan.getHour();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    localStringBuilder.append(':');
    int j = localDSITimeSpan.getMinute();
    localStringBuilder.append((char)(48 + j / 10));
    localStringBuilder.append((char)(48 + j % 10));
    localStringBuilder.append(':');
    int k = localDSITimeSpan.getSecond();
    localStringBuilder.append((char)(48 + k / 10));
    localStringBuilder.append((char)(48 + k % 10));
    formatFractionField(localStringBuilder, localDSITimeSpan.getFraction(), (short)(paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getPrecision() : 9));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalDayToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(10);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getDay(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalHourToMinuteToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(13);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getHour(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append(':');
    int i = localDSITimeSpan.getMinute();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalHourToSecondToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(26);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getHour(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append(':');
    int i = localDSITimeSpan.getMinute();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    localStringBuilder.append(':');
    int j = localDSITimeSpan.getSecond();
    localStringBuilder.append((char)(48 + j / 10));
    localStringBuilder.append((char)(48 + j % 10));
    formatFractionField(localStringBuilder, localDSITimeSpan.getFraction(), (short)(paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getPrecision() : 9));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalHourToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(10);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getHour(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalMinuteToSecondToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(22);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getMinute(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append(':');
    int i = localDSITimeSpan.getSecond();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    formatFractionField(localStringBuilder, localDSITimeSpan.getFraction(), (short)(paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getPrecision() : 9));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalMinuteToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(10);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getMinute(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalMonthToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSIMonthSpan localDSIMonthSpan = (DSIMonthSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(10);
    formatLeadingField(localStringBuilder, localDSIMonthSpan.getMonth(), localDSIMonthSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalSecondToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(20);
    formatLeadingField(localStringBuilder, localDSITimeSpan.getSecond(), localDSITimeSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    formatFractionField(localStringBuilder, localDSITimeSpan.getFraction(), (short)(paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getPrecision() : 9));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    String str = null;
    switch (paramTypeMetadata.getType())
    {
    case 103: 
      str = convertIntervalDayToString(paramObject, paramTypeMetadata);
      break;
    case 108: 
      str = convertIntervalDayToHourToString(paramObject, paramTypeMetadata);
      break;
    case 109: 
      str = convertIntervalDayToMinuteToString(paramObject, paramTypeMetadata);
      break;
    case 110: 
      str = convertIntervalDayToSecondToString(paramObject, paramTypeMetadata);
      break;
    case 104: 
      str = convertIntervalHourToString(paramObject, paramTypeMetadata);
      break;
    case 111: 
      str = convertIntervalHourToMinuteToString(paramObject, paramTypeMetadata);
      break;
    case 112: 
      str = convertIntervalHourToSecondToString(paramObject, paramTypeMetadata);
      break;
    case 105: 
      str = convertIntervalMinuteToString(paramObject, paramTypeMetadata);
      break;
    case 113: 
      str = convertIntervalMinuteToSecondToString(paramObject, paramTypeMetadata);
      break;
    case 102: 
      str = convertIntervalMonthToString(paramObject, paramTypeMetadata);
      break;
    case 106: 
      str = convertIntervalSecondToString(paramObject, paramTypeMetadata);
      break;
    case 101: 
      str = convertIntervalYearToString(paramObject, paramTypeMetadata);
      break;
    case 107: 
      str = convertIntervalYearToMonthToString(paramObject, paramTypeMetadata);
      break;
    default: 
      throw new InvalidArgumentException(1, JDBCMessageKey.INVALID_INTERVAL_DATA_TYPE.name(), null);
    }
    return str;
  }
  
  public static String convertIntervalYearToMonthToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSIMonthSpan localDSIMonthSpan = (DSIMonthSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(13);
    formatLeadingField(localStringBuilder, localDSIMonthSpan.getYear(), localDSIMonthSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    localStringBuilder.append('-');
    int i = localDSIMonthSpan.getMonth();
    localStringBuilder.append((char)(48 + i / 10));
    localStringBuilder.append((char)(48 + i % 10));
    return localStringBuilder.toString();
  }
  
  public static String convertIntervalYearToString(Object paramObject, TypeMetadata paramTypeMetadata)
  {
    DSIMonthSpan localDSIMonthSpan = (DSIMonthSpan)paramObject;
    StringBuilder localStringBuilder = new StringBuilder(10);
    formatLeadingField(localStringBuilder, localDSIMonthSpan.getYear(), localDSIMonthSpan.isNegative(), paramTypeMetadata.isIntervalType() ? paramTypeMetadata.getIntervalPrecision() : 9L);
    return localStringBuilder.toString();
  }
  
  public static Object convertStringToInterval(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    paramString = paramString.trim();
    switch (paramTypeMetadata.getType())
    {
    case 102: 
      return stringToIntervalMonth(paramString, paramTypeMetadata, paramIWarningListener);
    case 101: 
      return stringToIntervalYear(paramString, paramTypeMetadata, paramIWarningListener);
    case 107: 
      return stringToIntervalYearMonth(paramString, paramTypeMetadata, paramIWarningListener);
    case 103: 
      return stringToIntervalDay(paramString, paramTypeMetadata, paramIWarningListener);
    case 108: 
      return stringToIntervalDayToHour(paramString, paramTypeMetadata, paramIWarningListener);
    case 109: 
      return stringToIntervalDayToMinute(paramString, paramTypeMetadata, paramIWarningListener);
    case 110: 
      return stringToIntervalDayToSecond(paramString, paramTypeMetadata, paramIWarningListener);
    case 104: 
      return stringToIntervalHour(paramString, paramTypeMetadata, paramIWarningListener);
    case 111: 
      return stringToIntervalHourToMinute(paramString, paramTypeMetadata, paramIWarningListener);
    case 112: 
      return stringToIntervalHourToSecond(paramString, paramTypeMetadata, paramIWarningListener);
    case 105: 
      return stringToIntervalMinute(paramString, paramTypeMetadata, paramIWarningListener);
    case 113: 
      return stringToIntervalMinuteToSecond(paramString, paramTypeMetadata, paramIWarningListener);
    case 106: 
      return stringToIntervalSecond(paramString, paramTypeMetadata, paramIWarningListener);
    }
    throw new InvalidArgumentException(1, JDBCMessageKey.INVALID_INTERVAL_DATA_TYPE.name(), null);
  }
  
  private static void checkLeadingField(int paramInt1, int paramInt2)
  {
    int i = 0;
    if (paramInt1 < 100000)
    {
      if (paramInt1 < 100) {
        i = paramInt1 < 10 ? 1 : 2;
      } else {
        i = paramInt1 < 10000 ? 4 : paramInt1 < 1000 ? 3 : 5;
      }
    }
    else if (paramInt1 < 10000000) {
      i = paramInt1 < 1000000 ? 6 : 7;
    } else {
      i = paramInt1 < 1000000000 ? 9 : paramInt1 < 100000000 ? 8 : 10;
    }
    if (i > paramInt2) {
      throw new InvalidArgumentException(1, JDBCMessageKey.INTERVAL_FIELD_OVERFLOW.name(), null);
    }
  }
  
  private static IllegalArgumentException createException(JDBCMessageKey paramJDBCMessageKey, String paramString, IWarningListener paramIWarningListener)
  {
    return new IllegalArgumentException(JDBCMessageBuilder.getMessage(paramJDBCMessageKey, paramIWarningListener, new Object[] { paramString }));
  }
  
  private static void formatFractionField(StringBuilder paramStringBuilder, int paramInt, short paramShort)
  {
    if (0 < paramShort)
    {
      paramStringBuilder.append('.');
      int i = paramShort;
      StringBuilder localStringBuilder = new StringBuilder("000000000");
      while (i > 0)
      {
        localStringBuilder.setCharAt(--i, (char)(48 + paramInt % 10));
        paramInt /= 10;
      }
      if (paramInt > 0) {
        throw new InvalidArgumentException(1, JDBCMessageKey.INTERVAL_FIELD_OVERFLOW.name(), null);
      }
      paramStringBuilder.append(localStringBuilder.substring(0, paramShort));
    }
  }
  
  private static void formatLeadingField(StringBuilder paramStringBuilder, int paramInt, boolean paramBoolean, long paramLong)
  {
    if (paramBoolean) {
      paramStringBuilder.append('-');
    }
    if (s_isLeadingFieldPadded)
    {
      String str = Integer.toString(paramInt);
      paramStringBuilder.append(ZERO_BUFFER[((int)paramLong - str.length())]);
      paramStringBuilder.append(str);
    }
    else
    {
      paramStringBuilder.append(paramInt);
    }
  }
  
  private static int getFractionField(String paramString, short paramShort, IWarningListener paramIWarningListener)
  {
    int i = 0;
    if (0 < paramShort)
    {
      short s = paramString.length();
      if (s == paramShort)
      {
        i = Integer.valueOf(paramString).intValue();
      }
      else if (s > paramShort)
      {
        i = Integer.valueOf(paramString.substring(0, paramShort)).intValue();
        if (0 != Integer.valueOf(paramString.substring(paramShort)).intValue()) {
          paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
        }
      }
      else
      {
        int j = paramShort - s;
        i = Integer.valueOf(paramString).intValue();
        while (j-- > 0) {
          i *= 10;
        }
      }
    }
    return i;
  }
  
  private static DaySecondValue stringToDaySecondValue(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener, JDBCMessageKey paramJDBCMessageKey)
  {
    if (0 == paramString.length()) {
      throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
    }
    DaySecondValue localDaySecondValue = new DaySecondValue(null);
    int i = 0;
    int j = paramString.length();
    if ('-' == paramString.charAt(0))
    {
      localDaySecondValue.m_isNegative = true;
      i++;
    }
    while ((i < j) && (Character.isDigit(paramString.charAt(i)))) {
      i++;
    }
    int k = i;
    IntervalFieldType localIntervalFieldType = IntervalFieldType.DAY;
    if (i < j) {
      switch (paramString.charAt(i))
      {
      case ' ': 
        localIntervalFieldType = IntervalFieldType.DAY;
        break;
      case ':': 
        i++;
        while ((i < j) && (Character.isDigit(paramString.charAt(i)))) {
          i++;
        }
        if ((i < j) && ('.' == paramString.charAt(i))) {
          localIntervalFieldType = IntervalFieldType.MINUTE;
        } else {
          localIntervalFieldType = IntervalFieldType.HOUR;
        }
        break;
      case '.': 
        localIntervalFieldType = IntervalFieldType.SECOND;
        break;
      default: 
        throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
      }
    }
    int m = 0;
    for (i = localDaySecondValue.m_isNegative ? 1 : 0; i < j; i++)
    {
      DaySecondValue tmp248_246 = localDaySecondValue;
      tmp248_246.m_numFields = ((short)(tmp248_246.m_numFields + 1));
      switch (localIntervalFieldType)
      {
      case DAY: 
        localDaySecondValue.m_day = Integer.valueOf(paramString.substring(i, k)).intValue();
        m = 1;
        localIntervalFieldType = IntervalFieldType.HOUR;
        i = k;
        if ((i != j) && (' ' != paramString.charAt(i))) {
          throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
        }
        break;
      case HOUR: 
        if (m != 0)
        {
          localDaySecondValue.m_hour = Integer.valueOf(paramString.substring(i, i + 2)).intValue();
          i += 2;
        }
        else
        {
          localDaySecondValue.m_hour = Integer.valueOf(paramString.substring(i, k)).intValue();
          m = 1;
          i = k;
        }
        localIntervalFieldType = IntervalFieldType.MINUTE;
        if ((i != j) && (':' != paramString.charAt(i))) {
          throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
        }
        break;
      case MINUTE: 
        if (m != 0)
        {
          localDaySecondValue.m_minute = Integer.valueOf(paramString.substring(i, i + 2)).intValue();
          i += 2;
        }
        else
        {
          localDaySecondValue.m_minute = Integer.valueOf(paramString.substring(i, k)).intValue();
          m = 1;
          i = k;
        }
        localIntervalFieldType = IntervalFieldType.SECOND;
        if ((i != j) && (':' != paramString.charAt(i))) {
          throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
        }
        break;
      case SECOND: 
        if (m != 0)
        {
          localDaySecondValue.m_second = Integer.valueOf(paramString.substring(i, i + 2)).intValue();
          i += 2;
        }
        else
        {
          localDaySecondValue.m_second = Integer.valueOf(paramString.substring(i, k)).intValue();
          m = 1;
          i = k;
        }
        localIntervalFieldType = IntervalFieldType.FRACTION;
        if ((i != j) && ('.' != paramString.charAt(i))) {
          throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
        }
        break;
      case FRACTION: 
        if (m == 0) {
          throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
        }
        localDaySecondValue.m_fraction = getFractionField(paramString.substring(i), paramTypeMetadata.getPrecision(), paramIWarningListener);
        return localDaySecondValue;
      default: 
        throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
      }
    }
    return localDaySecondValue;
  }
  
  private static DSITimeSpan stringToIntervalDay(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_DAY_FORMAT);
    int i = localDaySecondValue.m_day + localDaySecondValue.m_hour / 24 + localDaySecondValue.m_minute / 1440 + localDaySecondValue.m_second / 86400;
    DSITimeSpan localDSITimeSpan = new DSITimeSpan(103, i, 0, 0, 0, 0, localDaySecondValue.m_isNegative);
    if ((0 != localDaySecondValue.m_hour % 24) || (0 != localDaySecondValue.m_minute % 1440) || (0 != localDaySecondValue.m_second % 86400) || (0 != localDaySecondValue.m_fraction)) {
      paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
    }
    checkLeadingField(localDSITimeSpan.getDay(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalDayToHour(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_DAY_HOUR_FORMAT);
    int i = localDaySecondValue.m_hour + localDaySecondValue.m_minute / 60 + localDaySecondValue.m_second / 3600;
    DSITimeSpan localDSITimeSpan = new DSITimeSpan(108, localDaySecondValue.m_day + i / 24, i % 24, 0, 0, 0, localDaySecondValue.m_isNegative);
    if ((0 != localDaySecondValue.m_minute % 60) || (0 != localDaySecondValue.m_second % 3600) || (0 != localDaySecondValue.m_fraction)) {
      paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
    }
    checkLeadingField(localDSITimeSpan.getDay(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalDayToMinute(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_DAY_MINUTE_FORMAT);
    int i = localDaySecondValue.m_minute + localDaySecondValue.m_second / 60;
    int j = i % 60;
    i = localDaySecondValue.m_hour + i / 60;
    DSITimeSpan localDSITimeSpan = new DSITimeSpan(109, localDaySecondValue.m_day + i / 24, i % 24, j, 0, 0, localDaySecondValue.m_isNegative);
    if ((0 != localDaySecondValue.m_second % 60) || (0 != localDaySecondValue.m_fraction)) {
      paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
    }
    checkLeadingField(localDSITimeSpan.getDay(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalDayToSecond(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_DAY_SECOND_FORMAT);
    checkLeadingField(localDaySecondValue.m_day, paramTypeMetadata.getIntervalPrecision());
    int i = localDaySecondValue.m_minute + localDaySecondValue.m_second / 60;
    int j = i % 60;
    i = localDaySecondValue.m_hour + i / 60;
    return new DSITimeSpan(110, localDaySecondValue.m_day + i / 24, i % 24, j, localDaySecondValue.m_second % 60, localDaySecondValue.m_fraction, localDaySecondValue.m_isNegative);
  }
  
  private static DSITimeSpan stringToIntervalHour(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_HOUR_FORMAT);
    DSITimeSpan localDSITimeSpan = null;
    if (1 == localDaySecondValue.m_numFields)
    {
      localDSITimeSpan = new DSITimeSpan(104, 0, localDaySecondValue.m_day, 0, 0, 0, localDaySecondValue.m_isNegative);
    }
    else
    {
      int i = localDaySecondValue.m_day * 24 + localDaySecondValue.m_hour + localDaySecondValue.m_minute / 60 + localDaySecondValue.m_second / 60;
      localDSITimeSpan = new DSITimeSpan(104, 0, i, 0, 0, 0, localDaySecondValue.m_isNegative);
      if ((0 != localDaySecondValue.m_minute % 60) || (0 != localDaySecondValue.m_second % 60) || (0 != localDaySecondValue.m_fraction)) {
        paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
      }
    }
    checkLeadingField(localDSITimeSpan.getHour(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalHourToMinute(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_HOUR_MINUTE_FORMAT);
    DSITimeSpan localDSITimeSpan = null;
    if (1 == localDaySecondValue.m_numFields)
    {
      localDSITimeSpan = new DSITimeSpan(111, 0, localDaySecondValue.m_day, 0, 0, 0, localDaySecondValue.m_isNegative);
    }
    else
    {
      int i = localDaySecondValue.m_minute + localDaySecondValue.m_second / 60;
      localDSITimeSpan = new DSITimeSpan(111, 0, localDaySecondValue.m_day * 24 + localDaySecondValue.m_hour + i / 60, i % 60, 0, 0, localDaySecondValue.m_isNegative);
      if ((0 != localDaySecondValue.m_second % 60) || (0 != localDaySecondValue.m_fraction)) {
        paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
      }
    }
    checkLeadingField(localDSITimeSpan.getHour(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalHourToSecond(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_HOUR_SECOND_FORMAT);
    DSITimeSpan localDSITimeSpan = null;
    if (1 == localDaySecondValue.m_numFields)
    {
      localDSITimeSpan = new DSITimeSpan(112, 0, localDaySecondValue.m_day, 0, 0, 0, localDaySecondValue.m_isNegative);
    }
    else
    {
      int i = localDaySecondValue.m_minute + localDaySecondValue.m_second / 60;
      localDSITimeSpan = new DSITimeSpan(112, 0, localDaySecondValue.m_day * 24 + localDaySecondValue.m_hour + i / 60, i % 60, localDaySecondValue.m_second % 60, localDaySecondValue.m_fraction, localDaySecondValue.m_isNegative);
    }
    checkLeadingField(localDSITimeSpan.getHour(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalMinute(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_MINUTE_FORMAT);
    DSITimeSpan localDSITimeSpan = null;
    if (1 == localDaySecondValue.m_numFields)
    {
      localDSITimeSpan = new DSITimeSpan(105, 0, 0, localDaySecondValue.m_day, 0, 0, localDaySecondValue.m_isNegative);
    }
    else
    {
      int i = localDaySecondValue.m_day * 1440 + localDaySecondValue.m_hour * 60 + localDaySecondValue.m_minute + localDaySecondValue.m_second / 60;
      localDSITimeSpan = new DSITimeSpan(105, 0, 0, i, 0, 0, localDaySecondValue.m_isNegative);
      if ((0 != localDaySecondValue.m_second % 60) || (0 != localDaySecondValue.m_fraction)) {
        paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
      }
    }
    checkLeadingField(localDSITimeSpan.getMinute(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSITimeSpan stringToIntervalMinuteToSecond(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_MINUTE_SECOND_FORMAT);
    DSITimeSpan localDSITimeSpan = null;
    if (1 == localDaySecondValue.m_numFields)
    {
      localDSITimeSpan = new DSITimeSpan(113, 0, 0, localDaySecondValue.m_day, 0, 0, localDaySecondValue.m_isNegative);
    }
    else if ((2 == localDaySecondValue.m_numFields) && (0 == localDaySecondValue.m_day) && (0 == localDaySecondValue.m_second) && (0 == localDaySecondValue.m_fraction))
    {
      localDSITimeSpan = new DSITimeSpan(113, 0, 0, localDaySecondValue.m_hour + localDaySecondValue.m_minute / 60, localDaySecondValue.m_minute % 60, 0, localDaySecondValue.m_isNegative);
    }
    else
    {
      int i = localDaySecondValue.m_day * 1440 + localDaySecondValue.m_hour * 60 + localDaySecondValue.m_minute + localDaySecondValue.m_second / 60;
      localDSITimeSpan = new DSITimeSpan(113, 0, 0, i, localDaySecondValue.m_second % 60, localDaySecondValue.m_fraction, localDaySecondValue.m_isNegative);
    }
    checkLeadingField(localDSITimeSpan.getMinute(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSIMonthSpan stringToIntervalMonth(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    YearMonthValue localYearMonthValue = stringToYearMonthValue(paramString, JDBCMessageKey.INVALID_INTERVAL_MONTH_FORMAT, paramIWarningListener);
    DSIMonthSpan localDSIMonthSpan = null;
    if (1 == localYearMonthValue.m_numFields) {
      localDSIMonthSpan = new DSIMonthSpan(102, 0, localYearMonthValue.m_first, localYearMonthValue.m_isNegative);
    } else {
      localDSIMonthSpan = new DSIMonthSpan(102, 0, localYearMonthValue.m_first * 12 + localYearMonthValue.m_second, localYearMonthValue.m_isNegative);
    }
    checkLeadingField(localDSIMonthSpan.getMonth(), paramTypeMetadata.getIntervalPrecision());
    return localDSIMonthSpan;
  }
  
  private static DSITimeSpan stringToIntervalSecond(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    DaySecondValue localDaySecondValue = stringToDaySecondValue(paramString, paramTypeMetadata, paramIWarningListener, JDBCMessageKey.INVALID_INTERVAL_SECOND_FORMAT);
    DSITimeSpan localDSITimeSpan = null;
    if (1 == localDaySecondValue.m_numFields)
    {
      localDSITimeSpan = new DSITimeSpan(106, 0, 0, 0, localDaySecondValue.m_day, 0, localDaySecondValue.m_isNegative);
    }
    else if ((2 == localDaySecondValue.m_numFields) && (0 == localDaySecondValue.m_day) && (0 == localDaySecondValue.m_second) && (0 == localDaySecondValue.m_fraction))
    {
      localDSITimeSpan = new DSITimeSpan(106, 0, 0, 0, localDaySecondValue.m_hour * 60 + localDaySecondValue.m_minute, 0, localDaySecondValue.m_isNegative);
    }
    else
    {
      int i = localDaySecondValue.m_day * 86400 + localDaySecondValue.m_hour * 3600 + localDaySecondValue.m_minute * 60 + localDaySecondValue.m_second;
      localDSITimeSpan = new DSITimeSpan(106, 0, 0, 0, i, localDaySecondValue.m_fraction, localDaySecondValue.m_isNegative);
    }
    checkLeadingField(localDSITimeSpan.getSecond(), paramTypeMetadata.getIntervalPrecision());
    return localDSITimeSpan;
  }
  
  private static DSIMonthSpan stringToIntervalYear(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    YearMonthValue localYearMonthValue = stringToYearMonthValue(paramString, JDBCMessageKey.INVALID_INTERVAL_YEAR_FORMAT, paramIWarningListener);
    DSIMonthSpan localDSIMonthSpan = null;
    if (1 == localYearMonthValue.m_numFields)
    {
      localDSIMonthSpan = new DSIMonthSpan(101, localYearMonthValue.m_first, 0, localYearMonthValue.m_isNegative);
    }
    else
    {
      if (0 != localYearMonthValue.m_second % 12) {
        paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
      }
      localDSIMonthSpan = new DSIMonthSpan(101, localYearMonthValue.m_first + localYearMonthValue.m_second / 12, 0, localYearMonthValue.m_isNegative);
    }
    checkLeadingField(localDSIMonthSpan.getYear(), paramTypeMetadata.getIntervalPrecision());
    return localDSIMonthSpan;
  }
  
  private static DSIMonthSpan stringToIntervalYearMonth(String paramString, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
  {
    YearMonthValue localYearMonthValue = stringToYearMonthValue(paramString, JDBCMessageKey.INVALID_INTERVAL_YEAR_MONTH_FORMAT, paramIWarningListener);
    checkLeadingField(localYearMonthValue.m_first, paramTypeMetadata.getIntervalPrecision());
    return new DSIMonthSpan(107, localYearMonthValue.m_first, localYearMonthValue.m_second, localYearMonthValue.m_isNegative);
  }
  
  private static YearMonthValue stringToYearMonthValue(String paramString, JDBCMessageKey paramJDBCMessageKey, IWarningListener paramIWarningListener)
  {
    if (0 == paramString.length()) {
      throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
    }
    YearMonthValue localYearMonthValue = new YearMonthValue(null);
    localYearMonthValue.m_isNegative = ('-' == paramString.charAt(0));
    if (localYearMonthValue.m_isNegative) {
      paramString = paramString.substring(1);
    }
    int i = paramString.lastIndexOf('-');
    try
    {
      if (-1 == i)
      {
        localYearMonthValue.m_first = Integer.parseInt(paramString);
        localYearMonthValue.m_numFields = 1;
      }
      else
      {
        localYearMonthValue.m_first = Integer.parseInt(paramString.substring(0, i));
        localYearMonthValue.m_second = Integer.parseInt(paramString.substring(i + 1));
        localYearMonthValue.m_numFields = 2;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw createException(paramJDBCMessageKey, paramString, paramIWarningListener);
    }
    return localYearMonthValue;
  }
  
  static
  {
    IDriver localIDriver = DSIDriverSingleton.getInstance();
    try
    {
      Variant localVariant = localIDriver.getProperty(21);
      if (1L == localVariant.getLong()) {
        s_isLeadingFieldPadded = true;
      } else {
        s_isLeadingFieldPadded = false;
      }
    }
    catch (Exception localException)
    {
      s_isLeadingFieldPadded = false;
    }
  }
  
  private static class YearMonthValue
  {
    public int m_first;
    public int m_second;
    public short m_numFields;
    public boolean m_isNegative;
  }
  
  private static enum IntervalFieldType
  {
    DAY,  HOUR,  MINUTE,  SECOND,  FRACTION;
    
    private IntervalFieldType() {}
  }
  
  private static class DaySecondValue
  {
    public int m_day;
    public int m_hour;
    public int m_minute;
    public int m_second;
    public int m_fraction;
    public short m_numFields;
    public boolean m_isNegative;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/utilities/conversion/IntervalConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */