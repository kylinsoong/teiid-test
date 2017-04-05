package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.utilities.CalendarSetter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class TimestampTz
  extends Timestamp
{
  private static final long serialVersionUID = -5521593264075626189L;
  private Calendar m_timezoneCal;
  
  public TimestampTz(Timestamp paramTimestamp, Calendar paramCalendar)
  {
    super(paramTimestamp.getTime());
    setNanos(paramTimestamp.getNanos());
    if (null == paramCalendar) {
      this.m_timezoneCal = Calendar.getInstance();
    } else {
      this.m_timezoneCal = paramCalendar;
    }
  }
  
  public TimestampTz(long paramLong, Calendar paramCalendar)
  {
    super(paramLong);
    if (null == paramCalendar) {
      this.m_timezoneCal = Calendar.getInstance();
    } else {
      this.m_timezoneCal = paramCalendar;
    }
  }
  
  public TimestampTz(long paramLong, int paramInt, Calendar paramCalendar)
  {
    super(paramLong);
    setNanos(paramInt);
    if (null == paramCalendar) {
      this.m_timezoneCal = Calendar.getInstance();
    } else {
      this.m_timezoneCal = paramCalendar;
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!super.equals(paramObject)) {
      return false;
    }
    if ((paramObject instanceof TimestampTz))
    {
      TimestampTz localTimestampTz = (TimestampTz)paramObject;
      return this.m_timezoneCal.getTimeZone().getOffset(getTime()) == localTimestampTz.getTimezoneCalendar().getTimeZone().getOffset(localTimestampTz.getTime());
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() + 7 * this.m_timezoneCal.getTimeZone().getOffset(getTime());
  }
  
  public synchronized Timestamp getAdjustedTimestamp()
  {
    return CalendarSetter.getTimestamp(this, Calendar.getInstance(), this.m_timezoneCal);
  }
  
  public Calendar getTimezoneCalendar()
  {
    return this.m_timezoneCal;
  }
  
  public Object clone()
  {
    TimestampTz localTimestampTz = (TimestampTz)super.clone();
    if (this.m_timezoneCal != null) {
      localTimestampTz.m_timezoneCal = ((Calendar)this.m_timezoneCal.clone());
    }
    return localTimestampTz;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/TimestampTz.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */