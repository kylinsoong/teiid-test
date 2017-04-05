package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ExactNumConverter;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class ETModFn
  extends ETScalarFn
{
  private final int m_type;
  
  public ETModFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((2 == paramList.size()) && (2 == paramList1.size()));
    int i = paramIColumn.getTypeMetadata().getType();
    assert (TypeUtilities.isNumberType(i));
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().getType() == i);
    assert (((IColumn)paramList1.get(1)).getTypeMetadata().getType() == i);
    this.m_type = i;
  }
  
  public String getLogString()
  {
    return "ETModFn";
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
    Object localObject1;
    Object localObject2;
    Object localObject3;
    switch (this.m_type)
    {
    case 2: 
    case 3: 
      localObject1 = localISqlDataWrapper1.getExactNumber();
      localObject2 = localISqlDataWrapper2.getExactNumber();
      if (0 == ((BigDecimal)localObject2).compareTo(BigDecimal.ZERO)) {
        throw SQLEngineExceptionFactory.divByZeroException();
      }
      localObject3 = getResultMetadata().getTypeMetadata();
      ConversionResult localConversionResult = new ConversionResult();
      BigDecimal localBigDecimal = ExactNumConverter.setPrecScale(((BigDecimal)localObject1).remainder((BigDecimal)localObject2), ((TypeMetadata)localObject3).getPrecision(), ((TypeMetadata)localObject3).getScale(), localConversionResult);
      paramETDataRequest.getData().setExactNumber(localBigDecimal);
      break;
    case -5: 
      localObject1 = localISqlDataWrapper1.getBigInt();
      localObject2 = localISqlDataWrapper2.getBigInt();
      if (((BigInteger)localObject2).equals(BigInteger.ZERO)) {
        throw SQLEngineExceptionFactory.divByZeroException();
      }
      localObject3 = ((BigInteger)localObject1).remainder((BigInteger)localObject2);
      paramETDataRequest.getData().setBigInt((BigInteger)localObject3);
      break;
    case 4: 
      paramETDataRequest.getData().setInteger(mod(localISqlDataWrapper1.getInteger(), localISqlDataWrapper2.getInteger()));
      break;
    case 5: 
      paramETDataRequest.getData().setSmallInt(mod(localISqlDataWrapper1.getSmallInt(), localISqlDataWrapper2.getSmallInt()));
      break;
    case -6: 
      paramETDataRequest.getData().setTinyInt((short)mod(localISqlDataWrapper1.getTinyInt(), localISqlDataWrapper2.getTinyInt()));
      break;
    case 7: 
      paramETDataRequest.getData().setReal((float)mod(localISqlDataWrapper1.getReal(), localISqlDataWrapper2.getReal()));
      break;
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    case 6: 
    default: 
      paramETDataRequest.getData().setDouble(mod(localISqlDataWrapper1.getDouble(), localISqlDataWrapper2.getDouble()));
    }
    return false;
  }
  
  private static int mod(int paramInt1, int paramInt2)
    throws ErrorException
  {
    if (0 == paramInt2) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramInt1 % paramInt2;
  }
  
  private static long mod(long paramLong1, long paramLong2)
    throws ErrorException
  {
    if (0L == paramLong2) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramLong1 % paramLong2;
  }
  
  private static double mod(double paramDouble1, double paramDouble2)
    throws ErrorException
  {
    if (0.0D == paramDouble2) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramDouble1 % paramDouble2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETModFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */