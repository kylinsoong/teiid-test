package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.conv.IntegralConverter;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SqlIntegralConverter
  implements ISqlConverter
{
  private final IColumn m_sourceMetadata;
  private final IColumn m_targetMetadata;
  
  public SqlIntegralConverter(IColumn paramIColumn1, IColumn paramIColumn2)
  {
    assert ((paramIColumn1.getTypeMetadata().isIntegerType()) || (paramIColumn1.getTypeMetadata().isBooleanType()));
    this.m_sourceMetadata = paramIColumn1;
    this.m_targetMetadata = paramIColumn2;
  }
  
  public ConversionResult convert(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    ConversionResult localConversionResult = new ConversionResult();
    if (-5 == this.m_sourceMetadata.getTypeMetadata().getType()) {
      convertBigInt(paramISqlDataWrapper1, paramISqlDataWrapper2, localConversionResult);
    } else if (this.m_sourceMetadata.getTypeMetadata().isBooleanType()) {
      convertBool(paramISqlDataWrapper1, paramISqlDataWrapper2, localConversionResult);
    } else {
      convertInteger(paramISqlDataWrapper1, paramISqlDataWrapper2, localConversionResult);
    }
    return localConversionResult;
  }
  
  private void convertBool(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, ConversionResult paramConversionResult)
    throws ErrorException
  {
    if (paramISqlDataWrapper1.isNull())
    {
      paramISqlDataWrapper2.setNull();
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return;
    }
    boolean bool = paramISqlDataWrapper1.getBoolean();
    Object localObject;
    switch (paramISqlDataWrapper2.getType())
    {
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      localObject = null;
      if (16 == this.m_sourceMetadata.getTypeMetadata().getType()) {
        localObject = IntegralConverter.booleanToChar(bool, this.m_targetMetadata.getColumnLength(), paramConversionResult);
      } else {
        localObject = IntegralConverter.bitToChar(bool, this.m_targetMetadata.getColumnLength(), paramConversionResult);
      }
      paramISqlDataWrapper2.setChar((String)localObject);
      break;
    case 2: 
    case 3: 
      localObject = IntegralConverter.toBigDecimal(bool, this.m_targetMetadata.getTypeMetadata().getPrecision(), this.m_targetMetadata.getTypeMetadata().getScale(), paramConversionResult);
      paramISqlDataWrapper2.setExactNumber((BigDecimal)localObject);
      break;
    case -7: 
    case -6: 
    case -5: 
    case -4: 
    case -3: 
    case -2: 
    case 0: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
  }
  
  private void convertInteger(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, ConversionResult paramConversionResult)
    throws ErrorException
  {
    if (paramISqlDataWrapper1.isNull())
    {
      paramISqlDataWrapper2.setNull();
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return;
    }
    long l1 = paramISqlDataWrapper1.getInteger();
    int i;
    switch (paramISqlDataWrapper2.getType())
    {
    case -5: 
      BigInteger localBigInteger = IntegralConverter.toBigInt(l1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setBigInt(localBigInteger);
      break;
    case 4: 
      long l2 = IntegralConverter.toInteger(l1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setInteger(l2);
      break;
    case 5: 
      i = IntegralConverter.toSmallInt(l1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setSmallInt(i);
      break;
    case -6: 
      i = IntegralConverter.toTinyInt(l1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setTinyInt(i);
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      String str = IntegralConverter.toChar(l1, this.m_targetMetadata.getColumnLength(), paramConversionResult);
      paramISqlDataWrapper2.setChar(str);
      break;
    case -7: 
    case 16: 
      boolean bool = IntegralConverter.toBit(l1, paramConversionResult);
      paramISqlDataWrapper2.setBoolean(bool);
      break;
    case 2: 
    case 3: 
      BigDecimal localBigDecimal = IntegralConverter.toBigDecimal(l1, this.m_targetMetadata.getTypeMetadata().getPrecision(), this.m_targetMetadata.getTypeMetadata().getScale(), paramConversionResult);
      paramISqlDataWrapper2.setExactNumber(localBigDecimal);
      break;
    case 7: 
      float f = IntegralConverter.toFloat(l1, paramConversionResult);
      paramISqlDataWrapper2.setReal(f);
      break;
    case -4: 
    case -3: 
    case -2: 
    case 0: 
    case 6: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
  }
  
  private void convertBigInt(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, ConversionResult paramConversionResult)
    throws ErrorException
  {
    if (paramISqlDataWrapper1.isNull())
    {
      paramISqlDataWrapper2.setNull();
      paramConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return;
    }
    BigInteger localBigInteger1 = paramISqlDataWrapper1.getBigInt();
    int i;
    switch (paramISqlDataWrapper2.getType())
    {
    case -5: 
      BigInteger localBigInteger2 = IntegralConverter.toBigInt(localBigInteger1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setBigInt(localBigInteger2);
      break;
    case 4: 
      long l = IntegralConverter.toInteger(localBigInteger1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setInteger(l);
      break;
    case 5: 
      i = IntegralConverter.toSmallInt(localBigInteger1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setSmallInt(i);
      break;
    case -6: 
      i = IntegralConverter.toTinyInt(localBigInteger1, this.m_targetMetadata.getTypeMetadata().isSigned(), paramConversionResult);
      paramISqlDataWrapper2.setTinyInt(i);
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      String str = IntegralConverter.toChar(localBigInteger1, this.m_targetMetadata.getColumnLength(), paramConversionResult);
      paramISqlDataWrapper2.setChar(str);
      break;
    case -7: 
    case 16: 
      boolean bool = IntegralConverter.toBit(localBigInteger1, paramConversionResult);
      paramISqlDataWrapper2.setBoolean(bool);
      break;
    case 2: 
    case 3: 
      BigDecimal localBigDecimal = IntegralConverter.toBigDecimal(localBigInteger1, this.m_targetMetadata.getTypeMetadata().getPrecision(), this.m_targetMetadata.getTypeMetadata().getScale(), paramConversionResult);
      paramISqlDataWrapper2.setExactNumber(localBigDecimal);
      break;
    case 6: 
    case 8: 
      double d = IntegralConverter.toDouble(localBigInteger1, paramConversionResult);
      paramISqlDataWrapper2.setDouble(d);
      break;
    case 7: 
      float f = IntegralConverter.toFloat(localBigInteger1, paramConversionResult);
      paramISqlDataWrapper2.setReal(f);
      break;
    case -4: 
    case -3: 
    case -2: 
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlIntegralConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */