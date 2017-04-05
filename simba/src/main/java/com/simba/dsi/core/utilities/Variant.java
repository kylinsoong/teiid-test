package com.simba.dsi.core.utilities;

import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class Variant
{
  public static final long UINT32_MAX_VALUE = 4294967295L;
  public static final long UINT32_MIN_VALUE = 0L;
  public static final BigInteger UINT64_MAX_VALUE = new BigInteger("18446744073709551615");
  public static final BigInteger UINT64_MIN_VALUE = BigInteger.ZERO;
  private static final BigInteger CHAR_MIN_VALUE_AS_BIGINT = BigInteger.valueOf(0L);
  private static final BigInteger CHAR_MAX_VALUE_AS_BIGINT = BigInteger.valueOf(65535L);
  private static final BigInteger SHORT_MIN_VALUE_AS_BIGINT = BigInteger.valueOf(-2147483648L);
  private static final BigInteger SHORT_MAX_VALUE_AS_BIGINT = BigInteger.valueOf(2147483647L);
  private static final BigInteger INT_MIN_VALUE_AS_BIGINT = BigInteger.valueOf(-2147483648L);
  private static final BigInteger INT_MAX_VALUE_AS_BIGINT = BigInteger.valueOf(2147483647L);
  private static final BigInteger LONG_MIN_VALUE_AS_BIGINT = BigInteger.valueOf(Long.MIN_VALUE);
  private static final BigInteger LONG_MAX_VALUE_AS_BIGINT = BigInteger.valueOf(Long.MAX_VALUE);
  public static final int TYPE_WSTRING = 0;
  public static final int TYPE_UINT16 = 2;
  public static final int TYPE_UINT32 = 3;
  public static final int TYPE_UINT64 = 4;
  public static final int TYPE_INT16 = 5;
  public static final int TYPE_INT32 = 6;
  public static final int TYPE_INT64 = 7;
  public static final int TYPE_NULL = 8;
  private int m_type;
  private Object m_value;
  
  public Variant(int paramInt, Object paramObject)
    throws IncorrectTypeException, NumericOverflowException
  {
    long l;
    switch (paramInt)
    {
    case 0: 
      if ((paramObject != null) && (!(paramObject instanceof String))) {
        throw new IncorrectTypeException();
      }
      break;
    case 5: 
      if (!(paramObject instanceof Short)) {
        throw new IncorrectTypeException();
      }
      break;
    case 2: 
      if (!(paramObject instanceof Character)) {
        throw new IncorrectTypeException();
      }
      break;
    case 6: 
      if (!(paramObject instanceof Integer)) {
        throw new IncorrectTypeException();
      }
      break;
    case 3: 
      if (!(paramObject instanceof Long)) {
        throw new IncorrectTypeException();
      }
      l = ((Long)paramObject).longValue();
      if ((4294967295L < l) || (0L > l)) {
        throw new NumericOverflowException();
      }
      break;
    case 7: 
      if (!(paramObject instanceof Long)) {
        throw new IncorrectTypeException();
      }
      break;
    case 4: 
      if ((paramObject instanceof Long))
      {
        l = ((Long)paramObject).longValue();
        if (0L > l) {
          throw new NumericOverflowException();
        }
        paramObject = BigInteger.valueOf(((Long)paramObject).longValue());
      }
      else if ((paramObject instanceof BigInteger))
      {
        BigInteger localBigInteger = (BigInteger)paramObject;
        if ((UINT64_MAX_VALUE.compareTo(localBigInteger) < 0) || (UINT64_MIN_VALUE.compareTo(localBigInteger) > 0)) {
          throw new NumericOverflowException();
        }
      }
      else
      {
        throw new IncorrectTypeException();
      }
      break;
    case 8: 
      if (null != paramObject) {
        throw new IncorrectTypeException();
      }
      break;
    case 1: 
    default: 
      throw new IncorrectTypeException();
    }
    this.m_type = paramInt;
    this.m_value = paramObject;
  }
  
  public char getChar()
    throws NumericOverflowException, IncorrectTypeException
  {
    switch (this.m_type)
    {
    case 2: 
      return ((Character)this.m_value).charValue();
    case 5: 
      int i = ((Short)this.m_value).intValue();
      if (0 <= i) {
        return (char)i;
      }
      throw new NumericOverflowException();
    case 3: 
      long l1 = ((Long)this.m_value).longValue();
      if (65535L >= l1) {
        return (char)(int)l1;
      }
      throw new NumericOverflowException();
    case 6: 
      int j = ((Integer)this.m_value).intValue();
      if ((65535 >= j) && (0 <= j)) {
        return (char)j;
      }
      throw new NumericOverflowException();
    case 7: 
      long l2 = ((Long)this.m_value).longValue();
      if ((65535L >= l2) && (0L <= l2)) {
        return (char)(int)l2;
      }
      throw new NumericOverflowException();
    case 4: 
      BigInteger localBigInteger = (BigInteger)this.m_value;
      if ((CHAR_MAX_VALUE_AS_BIGINT.compareTo(localBigInteger) < 0) || (CHAR_MIN_VALUE_AS_BIGINT.compareTo(localBigInteger) > 0)) {
        throw new NumericOverflowException();
      }
      return (char)localBigInteger.shortValue();
    case 0: 
      try
      {
        long l3 = Long.parseLong((String)this.m_value);
        if ((65535L >= l3) && (0L <= l3)) {
          return (char)(int)l3;
        }
        throw new NumericOverflowException();
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new IncorrectTypeException();
      }
    case 8: 
      throw new IncorrectTypeException();
    }
    throw new IncorrectTypeException();
  }
  
  public int getInt()
    throws NumericOverflowException, IncorrectTypeException
  {
    long l1;
    switch (this.m_type)
    {
    case 2: 
      return ((Character)this.m_value).charValue();
    case 5: 
      return ((Short)this.m_value).intValue();
    case 3: 
      l1 = ((Long)this.m_value).longValue();
      if (2147483647L >= l1) {
        return (int)l1;
      }
      throw new NumericOverflowException();
    case 6: 
      return ((Integer)this.m_value).intValue();
    case 7: 
      l1 = ((Long)this.m_value).longValue();
      if ((2147483647L >= l1) && (-2147483648L <= l1)) {
        return (short)(int)l1;
      }
      throw new NumericOverflowException();
    case 4: 
      BigInteger localBigInteger = (BigInteger)this.m_value;
      if ((INT_MAX_VALUE_AS_BIGINT.compareTo(localBigInteger) < 0) || (INT_MIN_VALUE_AS_BIGINT.compareTo(localBigInteger) > 0)) {
        throw new NumericOverflowException();
      }
      return localBigInteger.intValue();
    case 0: 
      try
      {
        long l2 = Long.parseLong((String)this.m_value);
        if ((2147483647L >= l2) && (-2147483648L <= l2)) {
          return (int)l2;
        }
        throw new NumericOverflowException();
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new IncorrectTypeException();
      }
    case 8: 
      throw new IncorrectTypeException();
    }
    throw new IncorrectTypeException();
  }
  
  public long getLong()
    throws IncorrectTypeException, NumericOverflowException
  {
    switch (this.m_type)
    {
    case 2: 
      return ((Character)this.m_value).charValue();
    case 5: 
      return ((Short)this.m_value).longValue();
    case 3: 
      return ((Long)this.m_value).longValue();
    case 6: 
      return ((Integer)this.m_value).longValue();
    case 7: 
      return ((Long)this.m_value).longValue();
    case 4: 
      BigInteger localBigInteger = (BigInteger)this.m_value;
      if ((LONG_MAX_VALUE_AS_BIGINT.compareTo(localBigInteger) < 0) || (LONG_MIN_VALUE_AS_BIGINT.compareTo(localBigInteger) > 0)) {
        throw new NumericOverflowException();
      }
      return localBigInteger.longValue();
    case 0: 
      try
      {
        return Long.parseLong((String)this.m_value);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new IncorrectTypeException();
      }
    case 8: 
      throw new IncorrectTypeException();
    }
    throw new IncorrectTypeException();
  }
  
  public BigInteger getBigInteger()
    throws IncorrectTypeException
  {
    switch (this.m_type)
    {
    case 2: 
      return BigInteger.valueOf(((Character)this.m_value).charValue());
    case 5: 
      return BigInteger.valueOf(((Short)this.m_value).shortValue());
    case 3: 
      return BigInteger.valueOf(((Long)this.m_value).longValue());
    case 6: 
      return BigInteger.valueOf(((Integer)this.m_value).intValue());
    case 7: 
      return BigInteger.valueOf(((Long)this.m_value).longValue());
    case 4: 
      return (BigInteger)this.m_value;
    case 0: 
      try
      {
        return new BigInteger((String)this.m_value);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new IncorrectTypeException();
      }
    case 8: 
      throw new IncorrectTypeException();
    }
    throw new IncorrectTypeException();
  }
  
  public short getShort()
    throws NumericOverflowException, IncorrectTypeException
  {
    switch (this.m_type)
    {
    case 2: 
      int i = ((Character)this.m_value).charValue();
      if (32767 >= i) {
        return (short)i;
      }
      throw new NumericOverflowException();
    case 5: 
      return ((Short)this.m_value).shortValue();
    case 3: 
      long l1 = ((Long)this.m_value).longValue();
      if (32767L >= l1) {
        return (short)(int)l1;
      }
      throw new NumericOverflowException();
    case 6: 
      int j = ((Integer)this.m_value).intValue();
      if ((32767 >= j) && (32768 <= j)) {
        return (short)j;
      }
      throw new NumericOverflowException();
    case 7: 
      long l2 = ((Long)this.m_value).longValue();
      if ((32767L >= l2) && (-32768L <= l2)) {
        return (short)(int)l2;
      }
      throw new NumericOverflowException();
    case 4: 
      BigInteger localBigInteger = (BigInteger)this.m_value;
      if ((SHORT_MAX_VALUE_AS_BIGINT.compareTo(localBigInteger) < 0) || (SHORT_MIN_VALUE_AS_BIGINT.compareTo(localBigInteger) > 0)) {
        throw new NumericOverflowException();
      }
      return localBigInteger.shortValue();
    case 0: 
      try
      {
        long l3 = Long.parseLong((String)this.m_value);
        if ((32767L >= l3) && (-32768L <= l3)) {
          return (short)(int)l3;
        }
        throw new NumericOverflowException();
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new IncorrectTypeException();
      }
    case 8: 
      throw new IncorrectTypeException();
    }
    throw new IncorrectTypeException();
  }
  
  public String getString()
  {
    if (null == this.m_value) {
      return null;
    }
    if (2 == this.m_type)
    {
      int i = Character.getNumericValue(((Character)this.m_value).charValue());
      return String.valueOf(i);
    }
    return this.m_value.toString();
  }
  
  public byte[] getStringAsUTF8()
    throws IncorrectTypeException, UnsupportedEncodingException
  {
    if (null == this.m_value) {
      return null;
    }
    return getString().getBytes("UTF-8");
  }
  
  public int getType()
  {
    return this.m_type;
  }
  
  public String toString()
  {
    String str = null;
    switch (this.m_type)
    {
    case 0: 
      str = "TYPE_WSTRING";
      break;
    case 2: 
      str = "TYPE_UINT16";
      break;
    case 3: 
      str = "TYPE_UINT32";
      break;
    case 4: 
      str = "TYPE_UINT64";
      break;
    case 5: 
      str = "TYPE_INT16";
      break;
    case 6: 
      str = "TYPE_INT32";
      break;
    case 7: 
      str = "TYPE_INT64";
      break;
    case 1: 
    default: 
      str = "TYPE_NULL";
    }
    if (null == this.m_value) {
      return "Variant[type: " + str + ", value: null]";
    }
    return "Variant[type: " + str + ", value: " + getString() + "]";
  }
  
  public boolean equals(Object paramObject)
  {
    if ((null == paramObject) || (!(paramObject instanceof Variant))) {
      return false;
    }
    Variant localVariant = (Variant)paramObject;
    if (null == this.m_value) {
      return null == localVariant.m_value;
    }
    return this.m_value.equals(localVariant.m_value);
  }
  
  public int hashCode()
  {
    if (null != this.m_value) {
      return this.m_value.hashCode();
    }
    return 0;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/Variant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */