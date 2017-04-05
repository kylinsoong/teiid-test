package com.simba.license.interfaces;

public enum ValidatorsEnum
{
  SIGNATURE(0),  EXPIRY(1),  PRODUCT_NAME(2),  PRODUCT_VERSION(3),  PRODUCT_PLATFORM(4);
  
  private int bit;
  
  private ValidatorsEnum(int paramInt)
  {
    this.bit = paramInt;
  }
  
  public int bitNumber()
  {
    return this.bit;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/ValidatorsEnum.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */