package com.simba.dsi.core.utilities;

public final class PropertyLimitKeys
{
  private Integer m_propMaxLimitKey;
  private Integer m_propMinLimitKey;
  
  public PropertyLimitKeys(int paramInt1, int paramInt2)
  {
    this.m_propMaxLimitKey = Integer.valueOf(paramInt1);
    this.m_propMinLimitKey = Integer.valueOf(paramInt2);
  }
  
  public Integer getMaxLimitkey()
  {
    return this.m_propMaxLimitKey;
  }
  
  public Integer getMinLimitkey()
  {
    return this.m_propMinLimitKey;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/PropertyLimitKeys.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */