package com.simba.dsi.core.utilities;

import java.util.Iterator;
import java.util.TreeMap;

public class ConnSettingRequestMap
{
  private TreeMap<String, Variant> m_map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  
  public boolean containsKey(String paramString)
  {
    return this.m_map.containsKey(paramString);
  }
  
  public Iterator<String> getKeysIterator()
  {
    return this.m_map.keySet().iterator();
  }
  
  public Variant getProperty(String paramString)
  {
    return (Variant)this.m_map.get(paramString);
  }
  
  public void setProperty(String paramString, Variant paramVariant)
  {
    this.m_map.put(paramString, paramVariant);
  }
  
  public String toString()
  {
    return this.m_map.toString();
  }
}