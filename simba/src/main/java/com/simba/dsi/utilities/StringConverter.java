package com.simba.dsi.utilities;

import com.simba.support.conv.CharConverter;
import com.simba.support.conv.ConversionResult;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

@Deprecated
public class StringConverter
{
  public static final int[] s_fractionalMultiplier = { 0, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1 };
  
  public static Date parseDate(String paramString, GregorianCalendar paramGregorianCalendar)
  {
    ConversionResult localConversionResult = new ConversionResult();
    Date localDate = CharConverter.toDate(paramString, localConversionResult, paramGregorianCalendar);
    switch (localConversionResult.getState())
    {
    case SUCCESS: 
      return localDate;
    }
    return null;
  }
  
  public static int parseInt(String paramString, int paramInt1, int paramInt2)
  {
    assert ((null != paramString) && (paramInt1 <= paramInt2) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 < paramString.length()) && (paramInt2 <= paramString.length()));
    if ((paramInt1 >= paramInt2) || (paramInt2 - paramInt1 > 9)) {
      return -1;
    }
    int i = 0;
    while (paramInt1 < paramInt2)
    {
      int j = paramString.charAt(paramInt1);
      if ((j < 48) || (j > 57)) {
        return -1;
      }
      i *= 10;
      i += j - 48;
      paramInt1++;
    }
    return i;
  }
  
  public static Time parseTime(String paramString, GregorianCalendar paramGregorianCalendar)
  {
    if (null == paramString) {
      throw new IllegalArgumentException();
    }
    ConversionResult localConversionResult = new ConversionResult();
    Time localTime = CharConverter.toTime(paramString, localConversionResult, (short)9, paramGregorianCalendar);
    switch (localConversionResult.getState())
    {
    case SUCCESS: 
    case FRAC_TRUNCATION_ROUNDED_DOWN: 
    case FRAC_TRUNCATION_ROUNDED_UP: 
      return localTime;
    }
    throw new IllegalArgumentException();
  }
  
  public static Timestamp parseTimestamp(String paramString, GregorianCalendar paramGregorianCalendar)
  {
    if (null == paramString) {
      throw new IllegalArgumentException();
    }
    ConversionResult localConversionResult = new ConversionResult();
    Timestamp localTimestamp = CharConverter.tsStrToTimestamp(paramString, localConversionResult, (short)9, paramGregorianCalendar);
    switch (localConversionResult.getState())
    {
    case SUCCESS: 
    case FRAC_TRUNCATION_ROUNDED_DOWN: 
    case FRAC_TRUNCATION_ROUNDED_UP: 
      return localTimestamp;
    }
    throw new IllegalArgumentException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/utilities/StringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */