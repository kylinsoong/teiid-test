package com.simba.dsi.dataengine.utilities;

public class DSIMonthSpan
  implements Cloneable
{
  private int m_intervalType;
  private int m_year = 0;
  private int m_month = 0;
  private boolean m_isNegative = false;
  
  private static void validate(int paramInt1, int paramInt2)
  {
    if ((0 != paramInt1) && ((0 > paramInt2) || (11 < paramInt2))) {
      throw new IllegalArgumentException("Invalid month value.");
    }
  }
  
  public DSIMonthSpan(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    validate(paramInt2, paramInt3);
    this.m_intervalType = paramInt1;
    this.m_year = paramInt2;
    this.m_month = paramInt3;
    this.m_isNegative = paramBoolean;
  }
  
  public int getIntervalType()
  {
    return this.m_intervalType;
  }
  
  public int getMonth()
  {
    return this.m_month;
  }
  
  public int getYear()
  {
    return this.m_year;
  }
  
  public boolean isNegative()
  {
    return this.m_isNegative;
  }
  
  public void setIsNegative(boolean paramBoolean)
  {
    this.m_isNegative = paramBoolean;
  }
  
  public void setMonth(int paramInt)
  {
    validate(this.m_year, paramInt);
    this.m_month = paramInt;
  }
  
  public void setYear(int paramInt)
  {
    validate(paramInt, this.m_month);
    this.m_year = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    if (this.m_isNegative) {
      localStringBuilder.append("-");
    }
    localStringBuilder.append(String.valueOf(this.m_year)).append("-");
    localStringBuilder.append(String.valueOf(this.m_month));
    return localStringBuilder.toString();
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
    i = 31 * i + this.m_intervalType;
    i = 31 * i + (this.m_isNegative ? 1231 : 1237);
    i = 31 * i + this.m_month;
    i = 31 * i + this.m_year;
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DSIMonthSpan)) {
      return false;
    }
    DSIMonthSpan localDSIMonthSpan = (DSIMonthSpan)paramObject;
    return (this.m_isNegative == localDSIMonthSpan.m_isNegative) && (this.m_month == localDSIMonthSpan.m_month) && (this.m_year == localDSIMonthSpan.m_year) && (this.m_intervalType == localDSIMonthSpan.m_intervalType);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/DSIMonthSpan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */