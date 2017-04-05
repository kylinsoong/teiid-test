package com.simba.dsi;

public abstract class CppClassWrapper
{
  private long m_objRef = 0L;
  
  public CppClassWrapper(long paramLong)
  {
    this.m_objRef = paramLong;
    assert (0L != paramLong);
  }
  
  public final long getObjRef()
  {
    return this.m_objRef;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/CppClassWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */