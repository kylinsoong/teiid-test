package com.simba.sqlengine.dsiext.dataengine.utils;

public enum DSIStmtPropertyKey
{
  private int m_enumValue;
  
  private DSIStmtPropertyKey(int paramInt)
  {
    this.m_enumValue = paramInt;
  }
  
  public int getEnumValue()
  {
    return this.m_enumValue;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/utils/DSIStmtPropertyKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */