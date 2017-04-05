package com.simba.dsi.core.utilities;

import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class AttributeDataMap
{
  private Hashtable<Integer, Variant> m_map = new Hashtable();
  
  public Enumeration<Integer> getKeysEnum()
  {
    return this.m_map.keys();
  }
  
  public Iterator<Integer> getKeysIterator()
  {
    return this.m_map.keySet().iterator();
  }
  
  public Variant getProperty(int paramInt)
  {
    return (Variant)this.m_map.get(Integer.valueOf(paramInt));
  }
  
  public boolean isProperty(int paramInt)
  {
    return this.m_map.containsKey(Integer.valueOf(paramInt));
  }
  
  public void setProperty(int paramInt1, int paramInt2, Object paramObject)
    throws IncorrectTypeException, NumericOverflowException
  {
    Variant localVariant = new Variant(paramInt2, paramObject);
    this.m_map.put(Integer.valueOf(paramInt1), localVariant);
  }
  
  public void setProperty(int paramInt, Variant paramVariant)
  {
    this.m_map.put(Integer.valueOf(paramInt), paramVariant);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/AttributeDataMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */