package com.simba.sqlengine.dsiext.dataengine.utils;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import java.math.BigInteger;

public class Variants
{
  public static Variant makeInt16(short paramShort)
  {
    return makeVariant(5, Short.valueOf(paramShort));
  }
  
  public static Variant makeInt32(int paramInt)
  {
    return makeVariant(6, Integer.valueOf(paramInt));
  }
  
  public static Variant makeInt64(long paramLong)
  {
    return makeVariant(7, Long.valueOf(paramLong));
  }
  
  public static Variant makeUInt16(char paramChar)
  {
    return makeVariant(2, Character.valueOf(paramChar));
  }
  
  public static Variant makeUInt32(long paramLong)
    throws NumericOverflowException
  {
    return makeVariantCheckOverflow(3, Long.valueOf(paramLong));
  }
  
  public static Variant makeUInt64(long paramLong)
    throws NumericOverflowException
  {
    return makeVariantCheckOverflow(4, Long.valueOf(paramLong));
  }
  
  public static Variant makeUInt64(BigInteger paramBigInteger)
    throws NumericOverflowException
  {
    return makeVariantCheckOverflow(4, paramBigInteger);
  }
  
  public static Variant makeNull()
  {
    return makeVariant(8, null);
  }
  
  public static Variant makeWString(String paramString)
  {
    return makeVariant(0, paramString);
  }
  
  private static Variant makeVariantCheckOverflow(int paramInt, Object paramObject)
    throws NumericOverflowException
  {
    try
    {
      return new Variant(paramInt, paramObject);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new AssertionError(localIncorrectTypeException);
    }
  }
  
  private static Variant makeVariant(int paramInt, Object paramObject)
  {
    try
    {
      return new Variant(paramInt, paramObject);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new AssertionError(localIncorrectTypeException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/utils/Variants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */