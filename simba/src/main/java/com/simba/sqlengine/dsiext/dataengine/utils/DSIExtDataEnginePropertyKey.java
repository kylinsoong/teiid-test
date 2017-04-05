package com.simba.sqlengine.dsiext.dataengine.utils;

public enum DSIExtDataEnginePropertyKey
{
  DSIEXT_DATAENGINE_NULL_EQUALS_EMPTY_STRING(0);
  
  private int m_enumValue;
  
  private DSIExtDataEnginePropertyKey(int paramInt)
  {
    this.m_enumValue = paramInt;
  }
  
  public int getEnumValue()
  {
    return this.m_enumValue;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/utils/DSIExtDataEnginePropertyKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */