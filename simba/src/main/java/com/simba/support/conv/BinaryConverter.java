package com.simba.support.conv;

import java.util.Arrays;

public class BinaryConverter
{
  public static byte[] toBinary(byte[] paramArrayOfByte, long paramLong, ConversionResult paramConversionResult)
  {
    if (paramArrayOfByte.length <= paramLong)
    {
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length);
    }
    paramConversionResult.setState(ConversionResult.TypeConversionState.STRING_RIGHT_TRUNCATION);
    return Arrays.copyOf(paramArrayOfByte, (int)paramLong);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/conv/BinaryConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */