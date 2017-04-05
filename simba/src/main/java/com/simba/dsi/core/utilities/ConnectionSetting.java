package com.simba.dsi.core.utilities;

import java.util.ArrayList;

public class ConnectionSetting
{
  public static final int TYPE_OPTIONAL = 0;
  public static final int TYPE_REQUIRED = 1;
  public static final int TYPE_PROCESSED = 2;
  private int m_status;
  private String m_label;
  private ArrayList<Variant> m_values;
  
  public ConnectionSetting(int paramInt)
  {
    this.m_status = paramInt;
    this.m_values = new ArrayList();
  }
  
  public String getLabel()
  {
    return this.m_label;
  }
  
  public int getStatus()
  {
    return this.m_status;
  }
  
  public ArrayList<Variant> getValues()
  {
    return this.m_values;
  }
  
  public void insertValue(Variant paramVariant)
  {
    this.m_values.add(paramVariant);
  }
  
  public boolean isOptional()
  {
    return 0 == this.m_status;
  }
  
  public boolean isProcessed()
  {
    return 2 == this.m_status;
  }
  
  public boolean isRequired()
  {
    return 1 == this.m_status;
  }
  
  public void setLabel(String paramString)
  {
    this.m_label = paramString;
  }
  
  public void setValues(ArrayList<Variant> paramArrayList)
  {
    this.m_values = paramArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/ConnectionSetting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */