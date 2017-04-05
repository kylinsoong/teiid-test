package com.simba.dsi.utilities;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class CalendarSetter
{
  public static Date getDate(Date paramDate, Calendar paramCalendar)
  {
    if (null == paramDate) {
      return null;
    }
    long l = 0L;
    if (null == paramCalendar)
    {
      paramCalendar = Calendar.getInstance();
      l = paramDate.getTime();
    }
    else
    {
      l = convertTimeMillis(paramDate.getTime(), paramCalendar, Calendar.getInstance());
    }
    paramCalendar.clear();
    paramCalendar.setTimeInMillis(l);
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
    return new Date(paramCalendar.getTimeInMillis());
  }
  
  public static Time getTime(Time paramTime, Calendar paramCalendar)
  {
    if (null == paramTime) {
      return null;
    }
    if (null == paramCalendar) {
      paramCalendar = Calendar.getInstance();
    }
    return getTime(paramTime, paramCalendar, Calendar.getInstance());
  }
  
  public static Time getTime(Time paramTime, Calendar paramCalendar1, Calendar paramCalendar2)
  {
    if (null == paramTime) {
      return null;
    }
    if ((null == paramCalendar1) || (null == paramCalendar2)) {
      throw new NullPointerException("Calendar cannot be null.");
    }
    long l1 = paramTime.getTime();
    long l2 = 0L;
    paramCalendar1.clear();
    paramCalendar2.clear();
    if (paramCalendar1.equals(paramCalendar2)) {
      l2 = paramTime.getTime();
    } else {
      l2 = convertTimeMillis(l1, paramCalendar1, paramCalendar2);
    }
    paramCalendar1.clear();
    paramCalendar1.setTimeInMillis(l2);
    paramCalendar1.set(1, 1970);
    paramCalendar1.set(2, 0);
    paramCalendar1.set(5, 1);
    return new Time(paramCalendar1.getTimeInMillis());
  }
  
  public static Timestamp getTimestamp(Timestamp paramTimestamp, Calendar paramCalendar)
  {
    if (null == paramTimestamp) {
      return null;
    }
    if (null == paramCalendar) {
      paramCalendar = Calendar.getInstance();
    }
    return getTimestamp(paramTimestamp, paramCalendar, Calendar.getInstance());
  }
  
  public static Timestamp getTimestamp(Timestamp paramTimestamp, Calendar paramCalendar1, Calendar paramCalendar2)
  {
    if (null == paramTimestamp) {
      return null;
    }
    if ((null == paramCalendar1) || (null == paramCalendar2)) {
      throw new NullPointerException("Calendar cannot be null.");
    }
    paramCalendar1.clear();
    paramCalendar2.clear();
    if (paramCalendar1.equals(paramCalendar2)) {
      return paramTimestamp;
    }
    Timestamp localTimestamp = new Timestamp(convertTimeMillis(paramTimestamp.getTime(), paramCalendar1, paramCalendar2));
    localTimestamp.setNanos(paramTimestamp.getNanos());
    return localTimestamp;
  }
  
  private static long convertTimeMillis(long paramLong, Calendar paramCalendar1, Calendar paramCalendar2)
  {
    paramCalendar2.setTimeInMillis(paramLong);
    paramCalendar1.set(paramCalendar2.get(1), paramCalendar2.get(2), paramCalendar2.get(5), paramCalendar2.get(11), paramCalendar2.get(12), paramCalendar2.get(13));
    paramCalendar1.set(14, paramCalendar2.get(14));
    paramCalendar1.set(0, paramCalendar2.get(0));
    return paramCalendar1.getTimeInMillis();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/utilities/CalendarSetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */