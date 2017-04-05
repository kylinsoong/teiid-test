package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.conv.ApproxNumConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ExactNumConverter;
import com.simba.support.conv.IntegralConverter;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

public final class ETRoundFn
  extends ETScalarFn
{
  private final int m_numericType;
  
  public ETRoundFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    this.m_numericType = paramIColumn.getTypeMetadata().getType();
    assert (TypeUtilities.isNumberType(this.m_numericType));
    assert (2 == getNumChildren());
    assert (AEUtils.isTypeNumeric(((IColumn)paramList1.get(0)).getTypeMetadata().getType()));
    assert (4 == ((IColumn)paramList1.get(1)).getTypeMetadata().getType());
  }
  
  public String getLogString()
  {
    return "ETRoundFn";
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper1 = getArgumentData(0);
    if (localISqlDataWrapper1.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    ISqlDataWrapper localISqlDataWrapper2 = getArgumentData(1);
    if (localISqlDataWrapper2.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    long l = localISqlDataWrapper2.getInteger();
    if ((l > 32767L) || (l < -32768L)) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("ROUND", 2);
    }
    int i = (int)l;
    Object localObject1;
    Object localObject2;
    Object localObject4;
    ConversionResult localConversionResult1;
    Object localObject3;
    double d;
    switch (this.m_numericType)
    {
    case 2: 
    case 3: 
      localObject1 = getResultMetadata().getTypeMetadata();
      localObject2 = localISqlDataWrapper1.getExactNumber();
      int j = ((BigDecimal)localObject2).scale();
      localObject4 = 0 <= ((BigDecimal)localObject2).compareTo(BigDecimal.ZERO) ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN;
      localObject2 = ((BigDecimal)localObject2).setScale(i, (RoundingMode)localObject4);
      localObject2 = ((BigDecimal)localObject2).setScale(j);
      localConversionResult1 = new ConversionResult();
      BigDecimal localBigDecimal = ExactNumConverter.setPrecScale((BigDecimal)localObject2, ((TypeMetadata)localObject1).getPrecision(), ((TypeMetadata)localObject1).getScale(), localConversionResult1);
      checkError(localConversionResult1);
      paramETDataRequest.getData().setExactNumber(localBigDecimal);
      break;
    case -5: 
      localObject1 = localISqlDataWrapper1.getBigInt();
      if (0 > i) {
        if (-i <= 21)
        {
          localObject2 = BigInteger.valueOf(10L).pow(-i);
          localObject3 = ((BigInteger)localObject1).divideAndRemainder((BigInteger)localObject2);
          localObject4 = localObject3[0];
          localConversionResult1 = localObject3[1];
          int k = 0;
          if (0 <= localConversionResult1.compareTo(BigInteger.ZERO))
          {
            if (0 <= localConversionResult1.shiftLeft(1).compareTo((BigInteger)localObject2))
            {
              localObject4 = ((BigInteger)localObject4).add(BigInteger.ONE);
              k = 1;
            }
          }
          else if (0 < localConversionResult1.negate().shiftLeft(1).compareTo((BigInteger)localObject2))
          {
            localObject4 = ((BigInteger)localObject4).subtract(BigInteger.ONE);
            k = 1;
          }
          localObject1 = ((BigInteger)localObject4).multiply((BigInteger)localObject2);
          if (k != 0)
          {
            ConversionResult localConversionResult2 = new ConversionResult();
            localObject1 = IntegralConverter.toBigInt((BigInteger)localObject1, getResultMetadata().getTypeMetadata().isSigned(), localConversionResult2);
            checkError(localConversionResult2);
          }
        }
        else
        {
          localObject1 = BigInteger.ZERO;
        }
      }
      paramETDataRequest.getData().setBigInt((BigInteger)localObject1);
      break;
    case 4: 
      if (0 > i)
      {
        d = localISqlDataWrapper1.getDouble() * Math.pow(10.0D, i);
        d = Math.round(d) * Math.pow(10.0D, -i);
        paramETDataRequest.getData().setInteger(d);
      }
      else
      {
        paramETDataRequest.getData().setInteger(localISqlDataWrapper1.getInteger());
      }
      break;
    case 5: 
      if (0 > i)
      {
        d = localISqlDataWrapper1.getDouble() * Math.pow(10.0D, i);
        d = Math.round(d) * Math.pow(10.0D, -i);
        paramETDataRequest.getData().setSmallInt((int)d);
      }
      else
      {
        paramETDataRequest.getData().setSmallInt(localISqlDataWrapper1.getSmallInt());
      }
      break;
    case -6: 
      if (0 > i)
      {
        d = localISqlDataWrapper1.getDouble() * Math.pow(10.0D, i);
        d = Math.round(d) * Math.pow(10.0D, -i);
        paramETDataRequest.getData().setTinyInt((short)(int)d);
      }
      else
      {
        paramETDataRequest.getData().setTinyInt(localISqlDataWrapper1.getTinyInt());
      }
      break;
    case 7: 
      d = localISqlDataWrapper1.getDouble();
      if ((Double.isNaN(d)) || (Double.isInfinite(d)))
      {
        paramETDataRequest.getData().setReal((float)d);
        return false;
      }
      localObject3 = d >= 0.0D ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN;
      localObject4 = new BigDecimal(d).setScale(i, (RoundingMode)localObject3);
      localConversionResult1 = new ConversionResult();
      float f = ApproxNumConverter.toFloat(((BigDecimal)localObject4).doubleValue(), localConversionResult1);
      checkError(localConversionResult1);
      paramETDataRequest.getData().setReal(f);
      break;
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    case 6: 
    default: 
      assert ((8 == this.m_numericType) || (6 == this.m_numericType));
      d = localISqlDataWrapper1.getDouble();
      if ((Double.isNaN(d)) || (Double.isInfinite(d)))
      {
        paramETDataRequest.getData().setDouble(d);
        return false;
      }
      localObject3 = d >= 0.0D ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN;
      localObject4 = new BigDecimal(d).setScale(i, (RoundingMode)localObject3);
      paramETDataRequest.getData().setDouble(((BigDecimal)localObject4).doubleValue());
      break;
    }
    return false;
  }
  
  private static void checkError(ConversionResult paramConversionResult)
    throws ErrorException
  {
    ConversionUtil.checkForErrorOnly(paramConversionResult, false, -1, -1);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETRoundFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */