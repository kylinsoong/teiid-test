package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.utilities.CalendarSetter;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeTz
  extends Time
{
  private static final long serialVersionUID = 617671935649092790L;
  private Calendar m_timezoneCal;
  
  public TimeTz(Time paramTime, Calendar paramCalendar)
  {
    super(paramTime.getTime());
    if (null == paramCalendar) {
      this.m_timezoneCal = Calendar.getInstance();
    } else {
      this.m_timezoneCal = paramCalendar;
    }
  }
  
  public TimeTz(long paramLong, Calendar paramCalendar)
  {
    super(paramLong);
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
    if ((paramObject instanceof TimeTz))
    {
      TimeTz localTimeTz = (TimeTz)paramObject;
      return this.m_timezoneCal.getTimeZone().getOffset(getTime()) == localTimeTz.getTimezoneCalendar().getTimeZone().getOffset(localTimeTz.getTime());
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() + 7 * this.m_timezoneCal.getTimeZone().getOffset(getTime());
  }
  
  public synchronized Time getAdjustedTime()
  {
    return CalendarSetter.getTime(this, Calendar.getInstance(), this.m_timezoneCal);
  }
  
  public Calendar getTimezoneCalendar()
  {
    return this.m_timezoneCal;
  }
  
  public String toString()
  {
    String str1 = super.toString();
    int i = (int)(getTime() % 1000L);
    String str2 = String.format("%03d", new Object[] { Integer.valueOf(i) });
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(str1);
    localStringBuilder.append(".");
    localStringBuilder.append(str2);
    return localStringBuilder.toString();
  }
  
  public Object clone()
  {
    TimeTz localTimeTz = (TimeTz)super.clone();
    if (this.m_timezoneCal != null) {
      localTimeTz.m_timezoneCal = ((Calendar)this.m_timezoneCal.clone());
    }
    return localTimeTz;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/TimeTz.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */