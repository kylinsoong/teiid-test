package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ExactNumConverter;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

public class ETTruncateFn
  extends ETScalarFn
{
  public ETTruncateFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (AEUtils.isTypeNumeric(paramIColumn.getTypeMetadata().getType()));
    assert (2 == getNumChildren());
    assert (AEUtils.isTypeNumeric(((IColumn)paramList1.get(0)).getTypeMetadata().getType()));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().getType() == paramIColumn.getTypeMetadata().getType());
    assert (4 == ((IColumn)paramList1.get(1)).getTypeMetadata().getType());
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
    long l1 = localISqlDataWrapper2.getInteger();
    if ((l1 > 32767L) || (l1 < -32768L)) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("TRUNCATE", 2);
    }
    Object localObject;
    int i;
    BigInteger localBigInteger1;
    BigDecimal localBigDecimal;
    switch (localISqlDataWrapper1.getType())
    {
    case 2: 
    case 3: 
      localObject = truncate(localISqlDataWrapper1.getExactNumber(), (short)(int)l1);
      paramETDataRequest.getData().setExactNumber((BigDecimal)localObject);
      break;
    case -5: 
      localObject = localISqlDataWrapper1.getBigInt();
      if (l1 < 0L) {
        if (-l1 < 20L) {
          localObject = truncate((BigInteger)localObject, (short)(int)l1);
        } else {
          localObject = BigInteger.ZERO;
        }
      }
      paramETDataRequest.getData().setBigInt((BigInteger)localObject);
      break;
    case 4: 
      long l2 = localISqlDataWrapper1.getInteger();
      if (l1 < 0L) {
        if (-l1 < 10L)
        {
          BigInteger localBigInteger2 = truncate(BigInteger.valueOf(l2), (short)(int)l1);
          l2 = localBigInteger2.longValue();
        }
        else
        {
          l2 = 0L;
        }
      }
      paramETDataRequest.getData().setInteger(l2);
      break;
    case 5: 
      i = localISqlDataWrapper1.getSmallInt();
      if (l1 < 0L)
      {
        localBigInteger1 = truncate(BigInteger.valueOf(i), (short)(int)l1);
        i = localBigInteger1.intValue();
      }
      paramETDataRequest.getData().setSmallInt(i);
      break;
    case -6: 
      i = localISqlDataWrapper1.getTinyInt();
      if (l1 < 0L)
      {
        localBigInteger1 = truncate(BigInteger.valueOf(i), (short)(int)l1);
        i = localBigInteger1.shortValue();
      }
      paramETDataRequest.getData().setTinyInt(i);
      break;
    case 6: 
    case 8: 
      localBigDecimal = truncate(new BigDecimal(localISqlDataWrapper1.getDouble()), (short)(int)l1);
      paramETDataRequest.getData().setDouble(localBigDecimal.doubleValue());
      break;
    case 7: 
      localBigDecimal = truncate(new BigDecimal(localISqlDataWrapper1.getReal()), (short)(int)l1);
      paramETDataRequest.getData().setReal((float)localBigDecimal.doubleValue());
      break;
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    default: 
      throw new IllegalArgumentException("Invalid data type to TRUNCATE scalar: " + getArgumentData(0).getType());
    }
    return false;
  }
  
  private BigInteger truncate(BigInteger paramBigInteger, short paramShort)
  {
    if (paramShort < 0)
    {
      BigInteger localBigInteger = paramBigInteger.remainder(BigInteger.TEN.pow(-paramShort));
      paramBigInteger = paramBigInteger.subtract(localBigInteger);
    }
    return paramBigInteger;
  }
  
  private BigDecimal truncate(BigDecimal paramBigDecimal, short paramShort)
    throws ErrorException
  {
    int i = 0;
    if (0 > paramShort)
    {
      i = 1;
      paramShort = 0;
    }
    IColumn localIColumn = getInputMetadata(0);
    int j = localIColumn.getTypeMetadata().getPrecision() - localIColumn.getTypeMetadata().getScale() + paramShort;
    if ((0 > j) || (j > 32767)) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("TRUNCATE", 2);
    }
    ConversionResult localConversionResult = new ConversionResult();
    BigDecimal localBigDecimal = ExactNumConverter.setPrecScale(paramBigDecimal, (short)j, paramShort, localConversionResult);
    ConversionUtil.checkForErrorOnly(localConversionResult, false, -1, -1);
    if (i != 0) {
      localBigDecimal = localBigDecimal.setScale((int)getArgumentData(1).getInteger(), RoundingMode.DOWN);
    }
    return localBigDecimal;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETTruncateFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */