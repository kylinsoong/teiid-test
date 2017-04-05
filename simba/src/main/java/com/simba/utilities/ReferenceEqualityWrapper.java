package com.simba.utilities;

public class ReferenceEqualityWrapper
{
  private final Object m_wrappedObj;
  
  public ReferenceEqualityWrapper(Object paramObject)
  {
    this.m_wrappedObj = paramObject;
  }
  
  public Object getWrappedObject()
  {
    return this.m_wrappedObj;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof ReferenceEqualityWrapper)) {
      return false;
    }
    ReferenceEqualityWrapper localReferenceEqualityWrapper = (ReferenceEqualityWrapper)paramObject;
    return localReferenceEqualityWrapper.getWrappedObject() == this.m_wrappedObj;
  }
  
  public int hashCode()
  {
    if (this.m_wrappedObj == null) {
      return 0;
    }
    return this.m_wrappedObj.hashCode();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/utilities/ReferenceEqualityWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */