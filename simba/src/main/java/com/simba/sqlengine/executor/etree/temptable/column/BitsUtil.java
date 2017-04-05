package com.simba.sqlengine.executor.etree.temptable.column;

public final class BitsUtil
{
  public static void setBit(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt % 8;
    int j = paramInt / 8;
    paramArrayOfByte[j] = ((byte)(paramArrayOfByte[j] | 1 << i));
  }
  
  public static void clearBit(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt % 8;
    int j = paramInt / 8;
    paramArrayOfByte[j] = ((byte)(paramArrayOfByte[j] & (1 << i ^ 0xFFFFFFFF)));
  }
  
  public static boolean isSet(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt % 8;
    int j = paramInt / 8;
    return (paramArrayOfByte[j] & 1 << i) != 0;
  }
  
  public static void copy(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    for (int i = 0; i < paramInt2; i++)
    {
      if (isSet(paramArrayOfByte2, paramInt3)) {
        setBit(paramArrayOfByte1, paramInt1);
      } else {
        clearBit(paramArrayOfByte1, paramInt1);
      }
      paramInt3++;
      paramInt1++;
    }
  }
  
  private BitsUtil()
  {
    throw new UnsupportedOperationException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/BitsUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */