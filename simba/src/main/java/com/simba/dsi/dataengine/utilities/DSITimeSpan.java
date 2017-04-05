package com.simba.dsi.dataengine.utilities;

public class DSITimeSpan
  implements Cloneable
{
  private int m_intervalType;
  private int m_day = 0;
  private int m_hour = 0;
  private int m_minute = 0;
  private int m_second = 0;
  private int m_fraction = 0;
  private boolean m_isNegative = false;
  
  public DSITimeSpan(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
  {
    validate(paramInt2, paramInt3, paramInt4, paramInt5);
    this.m_intervalType = paramInt1;
    this.m_day = paramInt2;
    this.m_hour = paramInt3;
    this.m_minute = paramInt4;
    this.m_second = paramInt5;
    this.m_fraction = paramInt6;
    this.m_isNegative = paramBoolean;
  }
  
  public int getDay()
  {
    return this.m_day;
  }
  
  public int getFraction()
  {
    return this.m_fraction;
  }
  
  public int getHour()
  {
    return this.m_hour;
  }
  
  public int getIntervalType()
  {
    return this.m_intervalType;
  }
  
  public int getMinute()
  {
    return this.m_minute;
  }
  
  public int getSecond()
  {
    return this.m_second;
  }
  
  public boolean isNegative()
  {
    return this.m_isNegative;
  }
  
  public void setDay(int paramInt)
  {
    validate(paramInt, this.m_hour, this.m_minute, this.m_second);
    this.m_day = paramInt;
  }
  
  public void setFraction(int paramInt)
  {
    this.m_fraction = paramInt;
  }
  
  public void setHour(int paramInt)
  {
    validate(this.m_day, paramInt, this.m_minute, this.m_second);
    this.m_hour = paramInt;
  }
  
  public void setIsNegative(boolean paramBoolean)
  {
    this.m_isNegative = paramBoolean;
  }
  
  public void setMinute(int paramInt)
  {
    validate(this.m_day, this.m_hour, paramInt, this.m_second);
    this.m_minute = paramInt;
  }
  
  public void setSecond(int paramInt)
  {
    validate(this.m_day, this.m_hour, this.m_minute, paramInt);
    this.m_second = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    if (this.m_isNegative) {
      localStringBuilder.append("-");
    }
    localStringBuilder.append(String.valueOf(this.m_day)).append(" ");
    localStringBuilder.append(String.valueOf(this.m_hour)).append(":");
    localStringBuilder.append(String.valueOf(this.m_minute)).append(":");
    localStringBuilder.append(String.valueOf(this.m_second)).append(".");
    String str = String.valueOf(this.m_fraction);
    for (int i = 9 - str.length(); i > 0; i--) {
      localStringBuilder.append("0");
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
  
  private void validate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 != 0 ? 1 : 0;
    int j = (i != 0) || (paramInt2 != 0) ? 1 : 0;
    int k = (j != 0) || (paramInt3 != 0) ? 1 : 0;
    if ((i != 0) && ((0 > paramInt2) || (23 < paramInt2))) {
      throw new IllegalArgumentException("Invalid hour value.");
    }
    if ((j != 0) && ((0 > paramInt3) || (59 < paramInt3))) {
      throw new IllegalArgumentException("Invalid minute value.");
    }
    if ((k != 0) && ((0 > paramInt4) || (59 < paramInt4))) {
      throw new IllegalArgumentException("Invalid second value.");
    }
  }
  
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return null;
  }
  
  public int hashCode()
  {
    int i = 1;
    i = 31 * i + this.m_day;
    i = 31 * i + this.m_fraction;
    i = 31 * i + this.m_hour;
    i = 31 * i + this.m_intervalType;
    i = 31 * i + (this.m_isNegative ? 1231 : 1237);
    i = 31 * i + this.m_minute;
    i = 31 * i + this.m_second;
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DSITimeSpan)) {
      return false;
    }
    DSITimeSpan localDSITimeSpan = (DSITimeSpan)paramObject;
    return (this.m_isNegative == localDSITimeSpan.m_isNegative) && (this.m_day == localDSITimeSpan.m_day) && (this.m_hour == localDSITimeSpan.m_hour) && (this.m_minute == localDSITimeSpan.m_minute) && (this.m_second == localDSITimeSpan.m_second) && (this.m_fraction == localDSITimeSpan.m_fraction) && (this.m_intervalType == localDSITimeSpan.m_intervalType);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/DSITimeSpan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */