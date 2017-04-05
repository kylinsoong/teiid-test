package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class DefaultSqlDataWrapper
  implements ISqlDataWrapper
{
  private static Map<Integer, Set<Integer>> s_implicitConvMap = new HashMap();
  
  public static ISqlDataWrapper initializeFromSqlType(int paramInt)
  {
    switch (paramInt)
    {
    case -5: 
      return new SqlBigIntDataWrapper();
    case 2: 
    case 3: 
      return new SqlExactNumDataWrapper(paramInt);
    case 6: 
    case 8: 
      return new SqlDoubleDataWrapper(paramInt);
    case 7: 
      return new SqlRealDataWrapper();
    case -4: 
    case -3: 
    case -2: 
      return new SqlBinaryDataWrapper(paramInt);
    case -7: 
    case 16: 
      return new SqlBooleanDataWrapper(paramInt);
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      return new SqlCharDataWrapper(paramInt);
    case 91: 
      return new SqlDateDataWrapper();
    case 92: 
      return new SqlTimeDataWrapper();
    case 93: 
      return new SqlTimestampDataWrapper();
    case -11: 
      return new SqlGuidDataWrapper();
    case 4: 
      return new SqlIntegerDataWrapper();
    case 5: 
      return new SqlSmallIntDataWrapper();
    case -6: 
      return new SqlTinyIntDataWrapper();
    }
    throw new IllegalArgumentException("Unknow data type: " + paramInt);
  }
  
  public static void setDataWrapperFromDataWrapper(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    if (paramISqlDataWrapper1.isNull())
    {
      paramISqlDataWrapper2.setNull();
      return;
    }
    switch (paramISqlDataWrapper1.getType())
    {
    case -5: 
      paramISqlDataWrapper2.setBigInt(paramISqlDataWrapper1.getBigInt());
      break;
    case 2: 
    case 3: 
      paramISqlDataWrapper2.setExactNumber(paramISqlDataWrapper1.getExactNumber());
      break;
    case 6: 
    case 8: 
      paramISqlDataWrapper2.setDouble(paramISqlDataWrapper1.getDouble());
      break;
    case 7: 
      paramISqlDataWrapper2.setReal(paramISqlDataWrapper1.getReal());
      break;
    case -4: 
    case -3: 
    case -2: 
      paramISqlDataWrapper2.setBinary(paramISqlDataWrapper1.getBinary());
      break;
    case -7: 
    case 16: 
      paramISqlDataWrapper2.setBoolean(paramISqlDataWrapper1.getBoolean());
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      paramISqlDataWrapper2.setChar(paramISqlDataWrapper1.getChar());
      break;
    case 91: 
      paramISqlDataWrapper2.setDate(paramISqlDataWrapper1.getDate());
      break;
    case 92: 
      paramISqlDataWrapper2.setTime(paramISqlDataWrapper1.getTime());
      break;
    case 93: 
      paramISqlDataWrapper2.setTimestamp(paramISqlDataWrapper1.getTimestamp());
      break;
    case -11: 
      paramISqlDataWrapper2.setGuid(paramISqlDataWrapper1.getGuid());
      break;
    case 4: 
      paramISqlDataWrapper2.setInteger(paramISqlDataWrapper1.getInteger());
      break;
    case 5: 
      paramISqlDataWrapper2.setSmallInt(paramISqlDataWrapper1.getSmallInt());
      break;
    case -6: 
      paramISqlDataWrapper2.setTinyInt(paramISqlDataWrapper1.getTinyInt());
      break;
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    default: 
      throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + paramISqlDataWrapper1.getType());
    }
  }
  
  public static boolean isImplicitConvSupported(IColumn paramIColumn1, IColumn paramIColumn2)
    throws ErrorException
  {
    Set localSet = (Set)s_implicitConvMap.get(Integer.valueOf(paramIColumn1.getTypeMetadata().getType()));
    if (!localSet.contains(Integer.valueOf(paramIColumn2.getTypeMetadata().getType()))) {
      return false;
    }
    return isCompatibleMeta(paramIColumn1, paramIColumn2);
  }
  
  private static boolean isCompatibleMeta(IColumn paramIColumn1, IColumn paramIColumn2)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata1 = paramIColumn1.getTypeMetadata();
    TypeMetadata localTypeMetadata2 = paramIColumn2.getTypeMetadata();
    if ((localTypeMetadata1.isCharacterType()) && (localTypeMetadata2.isCharacterType())) {
      return paramIColumn2.getColumnLength() >= paramIColumn1.getColumnLength();
    }
    if ((localTypeMetadata1.isBinaryType()) && (localTypeMetadata2.isBinaryType())) {
      return paramIColumn2.getColumnLength() >= paramIColumn1.getColumnLength();
    }
    int i;
    if ((localTypeMetadata1.isIntegerType()) && (localTypeMetadata2.isIntegerType()))
    {
      i = integralTypeCompareTo(localTypeMetadata1.getType(), localTypeMetadata2.getType());
      if (0 == i) {
        return localTypeMetadata1.isSigned() == localTypeMetadata2.isSigned();
      }
      if (0 > i) {
        return (localTypeMetadata1.isSigned() == localTypeMetadata2.isSigned()) || (localTypeMetadata2.isSigned());
      }
      return false;
    }
    if ((localTypeMetadata1.isExactNumericType()) && (localTypeMetadata2.isExactNumericType()))
    {
      i = localTypeMetadata1.getScale();
      int j = localTypeMetadata2.getScale();
      int k = localTypeMetadata1.getPrecision() - i;
      int m = localTypeMetadata2.getPrecision() - j;
      return (m >= k) && (j >= i);
    }
    if ((localTypeMetadata1.getType() == 93) && (localTypeMetadata2.getType() == 93)) {
      return localTypeMetadata2.getPrecision() >= localTypeMetadata1.getPrecision();
    }
    if ((localTypeMetadata1.getType() == 92) && (localTypeMetadata2.getType() == 92)) {
      return localTypeMetadata2.getPrecision() >= localTypeMetadata1.getPrecision();
    }
    return true;
  }
  
  private static int integralTypeCompareTo(short paramShort1, short paramShort2)
    throws ErrorException
  {
    switch (paramShort1)
    {
    case -6: 
      if (-6 == paramShort2) {
        return 0;
      }
      return -1;
    case 5: 
      if (5 == paramShort2) {
        return 0;
      }
      if (-6 == paramShort2) {
        return 1;
      }
      return -1;
    case 4: 
      if (4 == paramShort2) {
        return 0;
      }
      if (-5 == paramShort2) {
        return -1;
      }
      return 1;
    case -5: 
      if (-5 == paramShort2) {
        return 0;
      }
      return 1;
    }
    throw SQLEngineExceptionFactory.featureNotImplementedException("Unknown type");
  }
  
  public BigInteger getBigInt()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-5);
  }
  
  public byte[] getBinary()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-2);
  }
  
  public boolean getBoolean()
    throws ErrorException, NullPointerException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(16);
  }
  
  public String getChar()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(1);
  }
  
  public Date getDate()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(91);
  }
  
  public double getDouble()
    throws ErrorException, NullPointerException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(8);
  }
  
  public BigDecimal getExactNumber()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(3);
  }
  
  public UUID getGuid()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-11);
  }
  
  public long getInteger()
    throws ErrorException, NullPointerException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(4);
  }
  
  public Object getInterval()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(10);
  }
  
  public float getReal()
    throws ErrorException, NullPointerException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(7);
  }
  
  public int getSmallInt()
    throws ErrorException, NullPointerException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(5);
  }
  
  public Time getTime()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(92);
  }
  
  public Timestamp getTimestamp()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(93);
  }
  
  public short getTinyInt()
    throws ErrorException, NullPointerException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-6);
  }
  
  public void setBigInt(BigInteger paramBigInteger)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-5);
  }
  
  public void setBinary(byte[] paramArrayOfByte)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-6);
  }
  
  public void setBoolean(boolean paramBoolean)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(16);
  }
  
  public void setChar(String paramString)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(1);
  }
  
  public void setDate(Date paramDate)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(91);
  }
  
  public void setDouble(double paramDouble)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(8);
  }
  
  public void setExactNumber(BigDecimal paramBigDecimal)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(3);
  }
  
  public void setGuid(UUID paramUUID)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-11);
  }
  
  public void setInteger(long paramLong)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(4);
  }
  
  public void setInterval(Object paramObject)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(10);
  }
  
  public void setReal(float paramFloat)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(6);
  }
  
  public void setSmallInt(int paramInt)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(5);
  }
  
  public void setTime(Time paramTime)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(92);
  }
  
  public void setTimestamp(Timestamp paramTimestamp)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(93);
  }
  
  public void setTinyInt(short paramShort)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(-6);
  }
  
  static
  {
    s_implicitConvMap.put(Integer.valueOf(-2), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-2), Integer.valueOf(-3), Integer.valueOf(-4) })));
    s_implicitConvMap.put(Integer.valueOf(-3), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-2), Integer.valueOf(-3), Integer.valueOf(-4) })));
    s_implicitConvMap.put(Integer.valueOf(-4), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-2), Integer.valueOf(-3), Integer.valueOf(-4) })));
    s_implicitConvMap.put(Integer.valueOf(1), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) })));
    s_implicitConvMap.put(Integer.valueOf(12), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) })));
    s_implicitConvMap.put(Integer.valueOf(-1), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) })));
    s_implicitConvMap.put(Integer.valueOf(-8), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) })));
    s_implicitConvMap.put(Integer.valueOf(-9), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) })));
    s_implicitConvMap.put(Integer.valueOf(-10), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(12), Integer.valueOf(-1), Integer.valueOf(-8), Integer.valueOf(-9), Integer.valueOf(-10) })));
    s_implicitConvMap.put(Integer.valueOf(-7), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-7), Integer.valueOf(-5), Integer.valueOf(8), Integer.valueOf(16), Integer.valueOf(6), Integer.valueOf(4), Integer.valueOf(7), Integer.valueOf(5), Integer.valueOf(-6) })));
    s_implicitConvMap.put(Integer.valueOf(16), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(16), Integer.valueOf(-5), Integer.valueOf(8), Integer.valueOf(-7), Integer.valueOf(6), Integer.valueOf(4), Integer.valueOf(7), Integer.valueOf(5), Integer.valueOf(-6) })));
    s_implicitConvMap.put(Integer.valueOf(-6), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-6), Integer.valueOf(5), Integer.valueOf(4), Integer.valueOf(-5), Integer.valueOf(8), Integer.valueOf(6), Integer.valueOf(7) })));
    s_implicitConvMap.put(Integer.valueOf(5), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(5), Integer.valueOf(4), Integer.valueOf(-5), Integer.valueOf(8), Integer.valueOf(6), Integer.valueOf(7) })));
    s_implicitConvMap.put(Integer.valueOf(4), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(4), Integer.valueOf(-5), Integer.valueOf(8), Integer.valueOf(6) })));
    s_implicitConvMap.put(Integer.valueOf(-5), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-5) })));
    s_implicitConvMap.put(Integer.valueOf(2), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(3) })));
    s_implicitConvMap.put(Integer.valueOf(3), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(3) })));
    s_implicitConvMap.put(Integer.valueOf(7), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(6) })));
    s_implicitConvMap.put(Integer.valueOf(8), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(8), Integer.valueOf(6) })));
    s_implicitConvMap.put(Integer.valueOf(6), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(8), Integer.valueOf(6) })));
    s_implicitConvMap.put(Integer.valueOf(91), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(91) })));
    s_implicitConvMap.put(Integer.valueOf(92), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(92) })));
    s_implicitConvMap.put(Integer.valueOf(93), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(93) })));
    s_implicitConvMap.put(Integer.valueOf(-11), new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(-11) })));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/DefaultSqlDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */