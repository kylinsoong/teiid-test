package com.simba.support;

public class Pair<K, V>
{
  private K m_key;
  private V m_value;
  
  public Pair(K paramK, V paramV)
  {
    this.m_key = paramK;
    this.m_value = paramV;
  }
  
  public K key()
  {
    return (K)this.m_key;
  }
  
  public void setKey(K paramK)
  {
    this.m_key = paramK;
  }
  
  public void setValue(V paramV)
  {
    this.m_value = paramV;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(null != this.m_key ? this.m_key.toString() : "<null>");
    localStringBuilder.append(":");
    localStringBuilder.append(null != this.m_value ? this.m_value.toString() : "<null>");
    return localStringBuilder.toString();
  }
  
  public V value()
  {
    return (V)this.m_value;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Pair)) {
      return false;
    }
    Pair localPair = (Pair)paramObject;
    boolean bool1;
    if (null == this.m_key) {
      bool1 = null == localPair.m_key;
    } else {
      bool1 = this.m_key.equals(localPair.m_key);
    }
    boolean bool2;
    if (null == this.m_value) {
      bool2 = null == localPair.m_value;
    } else {
      bool2 = this.m_value.equals(localPair.m_value);
    }
    return (bool1) && (bool2);
  }
  
  public int hashCode()
  {
    int i = 0;
    if (null != this.m_key) {
      i += this.m_key.hashCode();
    }
    if (null != this.m_value) {
      i += 2 * this.m_value.hashCode();
    }
    return i;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */