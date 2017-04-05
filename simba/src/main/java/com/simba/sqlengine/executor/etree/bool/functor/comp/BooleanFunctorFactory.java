package com.simba.sqlengine.executor.etree.bool.functor.comp;

import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.util.HashMap;
import java.util.Map;

public class BooleanFunctorFactory
{
  private static Map<FunctorMapKey, IBooleanCompFunctor> s_functorMap = new HashMap();
  
  public static IBooleanCompFunctor getBoolCompFunctor(AEComparisonType paramAEComparisonType, TypeMetadata paramTypeMetadata)
    throws ErrorException
  {
    FunctorMapKey localFunctorMapKey = new FunctorMapKey(paramAEComparisonType, paramTypeMetadata.getType());
    if (s_functorMap.containsKey(localFunctorMapKey)) {
      return (IBooleanCompFunctor)s_functorMap.get(localFunctorMapKey);
    }
    throw new SQLEngineException(SQLEngineMessageKey.UNSUPPORT_COMP_OP.name(), new String[] { "Operation: " + paramAEComparisonType.name() + " type: " + paramTypeMetadata.getTypeName() });
  }
  
  private static void registerFunctors(int[] paramArrayOfInt, IBooleanCompFunctor paramIBooleanCompFunctor1, IBooleanCompFunctor paramIBooleanCompFunctor2, IBooleanCompFunctor paramIBooleanCompFunctor3, IBooleanCompFunctor paramIBooleanCompFunctor4, IBooleanCompFunctor paramIBooleanCompFunctor5, IBooleanCompFunctor paramIBooleanCompFunctor6)
  {
    for (int k : paramArrayOfInt) {
      registerFunctors(k, paramIBooleanCompFunctor1, paramIBooleanCompFunctor2, paramIBooleanCompFunctor3, paramIBooleanCompFunctor4, paramIBooleanCompFunctor5, paramIBooleanCompFunctor6);
    }
  }
  
  private static void registerFunctors(int paramInt, IBooleanCompFunctor paramIBooleanCompFunctor1, IBooleanCompFunctor paramIBooleanCompFunctor2, IBooleanCompFunctor paramIBooleanCompFunctor3, IBooleanCompFunctor paramIBooleanCompFunctor4, IBooleanCompFunctor paramIBooleanCompFunctor5, IBooleanCompFunctor paramIBooleanCompFunctor6)
  {
    if (null != paramIBooleanCompFunctor1) {
      s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, paramInt), paramIBooleanCompFunctor1);
    }
    if (null != paramIBooleanCompFunctor2) {
      s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, paramInt), paramIBooleanCompFunctor2);
    }
    if (null != paramIBooleanCompFunctor3) {
      s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, paramInt), paramIBooleanCompFunctor3);
    }
    if (null != paramIBooleanCompFunctor4) {
      s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, paramInt), paramIBooleanCompFunctor4);
    }
    if (null != paramIBooleanCompFunctor5) {
      s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, paramInt), paramIBooleanCompFunctor5);
    }
    if (null != paramIBooleanCompFunctor6) {
      s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, paramInt), paramIBooleanCompFunctor6);
    }
  }
  
  static
  {
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -8), new CharBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -8), new CharBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -8), new CharBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -8), new CharBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -8), new CharBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -8), new CharBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -9), new CharBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -9), new CharBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -9), new CharBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -9), new CharBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -9), new CharBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -9), new CharBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -10), new CharBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -10), new CharBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -10), new CharBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -10), new CharBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -10), new CharBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -10), new CharBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -1), new CharBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -1), new CharBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -1), new CharBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -1), new CharBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -1), new CharBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -1), new CharBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 12), new CharBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 12), new CharBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 12), new CharBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 12), new CharBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 12), new CharBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 12), new CharBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 1), new CharBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 1), new CharBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 1), new CharBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 1), new CharBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 1), new CharBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 1), new CharBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -5), new BigIntBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -5), new BigIntBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -5), new BigIntBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -5), new BigIntBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -5), new BigIntBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -5), new BigIntBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 4), new IntegerBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 4), new IntegerBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 4), new IntegerBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 4), new IntegerBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 4), new IntegerBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 4), new IntegerBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 5), new SmallIntBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 5), new SmallIntBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 5), new SmallIntBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 5), new SmallIntBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 5), new SmallIntBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 5), new SmallIntBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -6), new TinyIntBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -6), new TinyIntBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -6), new TinyIntBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -6), new TinyIntBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -6), new TinyIntBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -6), new TinyIntBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -7), new BitBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -7), new BitBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, -7), new BitBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, -7), new BitBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, -7), new BitBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, -7), new BitBoolGeFunctor());
    registerFunctors(new int[] { 8, 6 }, new DoubleBoolEqFunctor(), new DoubleBoolNeFunctor(), new DoubleBoolLtFunctor(), new DoubleBoolLeFunctor(), new DoubleBoolGtFunctor(), new DoubleBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 7), new RealBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 7), new RealBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 7), new RealBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 7), new RealBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 7), new RealBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 7), new RealBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 3), new DecimalBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 3), new DecimalBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 3), new DecimalBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 3), new DecimalBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 3), new DecimalBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 3), new DecimalBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 2), new DecimalBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 2), new DecimalBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 2), new DecimalBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 2), new DecimalBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 2), new DecimalBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 2), new DecimalBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 93), new TimestampBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 93), new TimestampBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 93), new TimestampBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 93), new TimestampBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 93), new TimestampBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 93), new TimestampBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 91), new DateBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 91), new DateBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 91), new DateBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 91), new DateBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 91), new DateBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 91), new DateBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, 92), new TimeBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, 92), new TimeBoolNeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN, 92), new TimeBoolLtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.LESS_THAN_OR_EQUAL, 92), new TimeBoolLeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN, 92), new TimeBoolGtFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.GREATER_THAN_OR_EQUAL, 92), new TimeBoolGeFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.EQUAL, -11), new GuidBoolEqFunctor());
    s_functorMap.put(new FunctorMapKey(AEComparisonType.NOT_EQUAL, -11), new GuidBoolNeFunctor());
    registerFunctors(16, new BooleanBoolEqFunctor(), new BooleanBoolNeFunctor(), new BooleanBoolLtFunctor(), new BooleanBoolLeFunctor(), new BooleanBoolGtFunctor(), new BooleanBoolGeFunctor());
  }
  
  private static class FunctorMapKey
  {
    private AEComparisonType m_opType;
    private int m_sqlType;
    
    public FunctorMapKey(AEComparisonType paramAEComparisonType, int paramInt)
    {
      this.m_opType = paramAEComparisonType;
      this.m_sqlType = paramInt;
    }
    
    public int hashCode()
    {
      int i = 1;
      i = 31 * i + (this.m_opType == null ? 0 : this.m_opType.hashCode());
      i = 31 * i + this.m_sqlType;
      return i;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      FunctorMapKey localFunctorMapKey = (FunctorMapKey)paramObject;
      if (this.m_opType != localFunctorMapKey.m_opType) {
        return false;
      }
      return this.m_sqlType == localFunctorMapKey.m_sqlType;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/BooleanFunctorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */