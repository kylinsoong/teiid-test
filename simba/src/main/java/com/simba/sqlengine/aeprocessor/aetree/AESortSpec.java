package com.simba.sqlengine.aeprocessor.aetree;

public final class AESortSpec
{
  private final int m_colNumber;
  private final boolean m_isAscending;
  
  public AESortSpec(int paramInt, boolean paramBoolean)
  {
    this.m_colNumber = paramInt;
    this.m_isAscending = paramBoolean;
  }
  
  public int hashCode()
  {
    int i = 1;
    i = 31 * i + this.m_colNumber;
    i = 31 * i + (this.m_isAscending ? 1231 : 1237);
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof AESortSpec)) {
      return false;
    }
    AESortSpec localAESortSpec = (AESortSpec)paramObject;
    if (this.m_colNumber != localAESortSpec.m_colNumber) {
      return false;
    }
    return this.m_isAscending == localAESortSpec.m_isAscending;
  }
  
  public int getColumnNumber()
  {
    return this.m_colNumber;
  }
  
  public boolean isAscending()
  {
    return this.m_isAscending;
  }
  
  public String toString()
  {
    return "SESortSpec(" + this.m_colNumber + ", " + (this.m_isAscending ? "ASC" : "DESC") + ")";
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AESortSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */