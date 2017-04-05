package com.simba.dsi.core.utilities;

import java.util.ArrayList;
import java.util.List;

public class ConnectionSettingInfo
{
  private String m_key;
  private boolean m_isRequired = false;
  private int m_valueType = 0;
  private List<Variant> m_values = new ArrayList();
  private boolean m_isSensitive = false;
  
  public ConnectionSettingInfo(String paramString, int paramInt)
  {
    this.m_key = paramString;
    this.m_valueType = paramInt;
  }
  
  public String getKey()
  {
    return this.m_key;
  }
  
  public boolean isRequired()
  {
    return this.m_isRequired;
  }
  
  public boolean isSensitive()
  {
    return this.m_isSensitive;
  }
  
  public List<Variant> getValues()
  {
    return this.m_values;
  }
  
  public int getValueType()
  {
    return this.m_valueType;
  }
  
  public void setIsRequired(boolean paramBoolean)
  {
    this.m_isRequired = paramBoolean;
  }
  
  public void setIsSensitive(boolean paramBoolean)
  {
    this.m_isSensitive = paramBoolean;
  }
  
  public void setValues(List<Variant> paramList)
  {
    this.m_values = paramList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/ConnectionSettingInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */