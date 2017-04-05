package com.simba.support;

public class ObjectWrapper<V>
{
  private V m_value = null;
  
  public ObjectWrapper(V paramV)
  {
    this.m_value = paramV;
  }
  
  public V getValue()
  {
    return (V)this.m_value;
  }
  
  public void setValue(V paramV)
  {
    this.m_value = paramV;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/ObjectWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */