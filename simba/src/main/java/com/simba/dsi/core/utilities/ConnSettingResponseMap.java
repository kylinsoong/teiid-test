package com.simba.dsi.core.utilities;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class ConnSettingResponseMap
{
  private TreeMap<String, ConnectionSetting> m_map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
  
  public Iterator<String> getKeysIterator()
  {
    return this.m_map.keySet().iterator();
  }
  
  public ConnectionSetting getProperty(String paramString)
  {
    return (ConnectionSetting)this.m_map.get(paramString);
  }
  
  public void setProperty(String paramString, ConnectionSetting paramConnectionSetting)
  {
    this.m_map.put(paramString, paramConnectionSetting);
  }
  
  public int size()
  {
    return this.m_map.size();
  }
  
  public String toString()
  {
    return this.m_map.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/ConnSettingResponseMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */