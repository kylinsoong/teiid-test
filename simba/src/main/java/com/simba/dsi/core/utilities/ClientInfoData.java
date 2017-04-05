package com.simba.dsi.core.utilities;

public class ClientInfoData
{
  private String m_name;
  private int m_maxLength;
  private String m_defaultValue;
  private String m_value;
  private String m_description;
  
  public ClientInfoData(String paramString1, int paramInt, String paramString2, String paramString3)
  {
    this.m_name = paramString1;
    this.m_maxLength = paramInt;
    this.m_defaultValue = paramString2;
    this.m_value = this.m_defaultValue;
    this.m_description = paramString3;
  }
  
  public ClientInfoData(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
  {
    this.m_name = paramString1;
    this.m_maxLength = paramInt;
    this.m_defaultValue = paramString2;
    this.m_description = paramString4;
    this.m_value = paramString3;
  }
  
  public String getDefaultValue()
  {
    return this.m_defaultValue;
  }
  
  public String getDescription()
  {
    return this.m_description;
  }
  
  public int getMaxLength()
  {
    return this.m_maxLength;
  }
  
  public String getName()
  {
    return this.m_name;
  }
  
  public String getValue()
  {
    return this.m_value;
  }
  
  public void setValue(String paramString)
  {
    this.m_value = paramString;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/ClientInfoData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */