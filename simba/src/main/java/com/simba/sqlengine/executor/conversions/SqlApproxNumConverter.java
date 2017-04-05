package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.ApproxNumConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SqlApproxNumConverter
  implements ISqlConverter
{
  private IColumn m_targetMeta;
  
  public SqlApproxNumConverter(IColumn paramIColumn1, IColumn paramIColumn2)
  {
    assert (paramIColumn1.getTypeMetadata().isApproximateNumericType());
    this.m_targetMeta = paramIColumn2;
  }
  
  public ConversionResult convert(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    ConversionResult localConversionResult = new ConversionResult();
    if (paramISqlDataWrapper1.isNull())
    {
      paramISqlDataWrapper2.setNull();
      localConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return localConversionResult;
    }
    double d = paramISqlDataWrapper1.getDouble();
    Object localObject;
    int i;
    switch (paramISqlDataWrapper2.getType())
    {
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      String str = ApproxNumConverter.toChar(d, this.m_targetMeta.getColumnLength(), localConversionResult);
      paramISqlDataWrapper2.setChar(str);
      break;
    case 7: 
      float f = ApproxNumConverter.toFloat(d, localConversionResult);
      paramISqlDataWrapper2.setReal(f);
      break;
    case 2: 
    case 3: 
      localObject = ApproxNumConverter.toBigDecimal(d, this.m_targetMeta.getTypeMetadata().getPrecision(), this.m_targetMeta.getTypeMetadata().getScale(), localConversionResult);
      paramISqlDataWrapper2.setExactNumber((BigDecimal)localObject);
      break;
    case -5: 
      localObject = ApproxNumConverter.toBigInt(d, this.m_targetMeta.getTypeMetadata().isSigned(), localConversionResult);
      paramISqlDataWrapper2.setBigInt((BigInteger)localObject);
      break;
    case 4: 
      long l = ApproxNumConverter.toInteger(d, this.m_targetMeta.getTypeMetadata().isSigned(), localConversionResult);
      paramISqlDataWrapper2.setInteger(l);
      break;
    case 5: 
      i = ApproxNumConverter.toSmallInt(d, this.m_targetMeta.getTypeMetadata().isSigned(), localConversionResult);
      paramISqlDataWrapper2.setSmallInt(i);
      break;
    case -6: 
      i = ApproxNumConverter.toTinyInt(d, this.m_targetMeta.getTypeMetadata().isSigned(), localConversionResult);
      paramISqlDataWrapper2.setTinyInt(i);
      break;
    case -7: 
    case -4: 
    case -3: 
    case -2: 
    case 0: 
    case 6: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
    return localConversionResult;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlApproxNumConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */