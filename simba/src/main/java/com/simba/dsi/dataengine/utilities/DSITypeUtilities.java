package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.exceptions.IncorrectTypeException;

public class DSITypeUtilities
{
  public static boolean outputCharStringData(String paramString, DataWrapper paramDataWrapper, long paramLong1, long paramLong2)
  {
    try
    {
      return outputString(paramString, paramDataWrapper, paramLong1, paramLong2, 1);
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return true;
  }
  
  public static boolean outputVarCharStringData(String paramString, DataWrapper paramDataWrapper, long paramLong1, long paramLong2)
  {
    try
    {
      return outputString(paramString, paramDataWrapper, paramLong1, paramLong2, 12);
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return true;
  }
  
  public static boolean outputLongVarCharStringData(String paramString, DataWrapper paramDataWrapper, long paramLong1, long paramLong2)
  {
    try
    {
      return outputString(paramString, paramDataWrapper, paramLong1, paramLong2, -1);
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return true;
  }
  
  public static boolean outputBinary(byte[] paramArrayOfByte, DataWrapper paramDataWrapper, long paramLong1, long paramLong2)
  {
    try
    {
      return outputBytes(paramArrayOfByte, paramDataWrapper, paramLong1, paramLong2, -2);
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return true;
  }
  
  public static boolean outputVarBinary(byte[] paramArrayOfByte, DataWrapper paramDataWrapper, long paramLong1, long paramLong2)
  {
    try
    {
      return outputBytes(paramArrayOfByte, paramDataWrapper, paramLong1, paramLong2, -3);
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return true;
  }
  
  public static boolean outputLongVarBinary(byte[] paramArrayOfByte, DataWrapper paramDataWrapper, long paramLong1, long paramLong2)
  {
    try
    {
      return outputBytes(paramArrayOfByte, paramDataWrapper, paramLong1, paramLong2, -4);
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return true;
  }
  
  public static boolean outputString(String paramString, DataWrapper paramDataWrapper, long paramLong1, long paramLong2, int paramInt)
    throws IncorrectTypeException
  {
    assert ((1 == paramInt) || (12 == paramInt) || (-1 == paramInt) || (-8 == paramInt) || (-9 == paramInt) || (-10 == paramInt));
    if (paramString != null)
    {
      String str = paramString.substring((int)paramLong1);
      if (paramLong2 == -1L)
      {
        paramDataWrapper.setData(paramInt, str);
        return false;
      }
      int i = Math.min((int)paramLong2, str.length());
      paramDataWrapper.setData(paramInt, str.substring(0, i));
      return i < str.length();
    }
    paramDataWrapper.setNull(paramInt);
    return false;
  }
  
  public static boolean outputBytes(byte[] paramArrayOfByte, DataWrapper paramDataWrapper, long paramLong1, long paramLong2, int paramInt)
    throws IncorrectTypeException
  {
    assert ((-2 == paramInt) || (-3 == paramInt) || (-4 == paramInt));
    assert (0L <= paramLong1);
    if (paramArrayOfByte != null)
    {
      long l1 = paramArrayOfByte.length;
      long l2 = l1 - paramLong1;
      byte[] arrayOfByte;
      boolean bool;
      if ((-1L == paramLong2) || (l2 <= paramLong2))
      {
        arrayOfByte = new byte[(int)l2];
        System.arraycopy(paramArrayOfByte, (int)paramLong1, arrayOfByte, 0, (int)l2);
        bool = false;
      }
      else
      {
        arrayOfByte = new byte[(int)paramLong2];
        System.arraycopy(paramArrayOfByte, (int)paramLong1, arrayOfByte, 0, (int)paramLong2);
        bool = true;
      }
      paramDataWrapper.setData(paramInt, arrayOfByte);
      return bool;
    }
    paramDataWrapper.setNull(paramInt);
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/DSITypeUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */