package com.simba.support.conv;

public final class ConversionResult
{
  private TypeConversionState m_state = null;
  
  public TypeConversionState getState()
  {
    return this.m_state;
  }
  
  public void setState(TypeConversionState paramTypeConversionState)
  {
    this.m_state = paramTypeConversionState;
  }
  
  public static enum TypeConversionState
  {
    SUCCESS,  DATETIME_OVERFLOW,  NUMERIC_OUT_OF_RANGE_TOO_LARGE,  NUMERIC_OUT_OF_RANGE_TOO_SMALL,  STRING_RIGHT_TRUNCATION,  FRAC_TRUNCATION_ROUNDED_UP,  FRAC_TRUNCATION_ROUNDED_DOWN,  RESTRICTED_DATA_TYPE_ATTR_VIOLATION,  INTERVAL_OVERFLOW_TOO_LARGE,  INTERVAL_OVERFLOW_TOO_SMALL,  INVALID_CHAR_VAL_FOR_CAST,  INVALID_DATA,  INTEGRAL_PRECISION_LOSS;
    
    private TypeConversionState() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/ConversionResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */