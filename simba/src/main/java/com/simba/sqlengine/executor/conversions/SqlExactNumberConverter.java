package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.conv.ExactNumConverter;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SqlExactNumberConverter
  implements ISqlConverter
{
  private final short m_tgtPrecision;
  private final short m_tgtScale;
  private final long m_targetColLen;
  private final boolean m_targetIsSigned;
  
  public SqlExactNumberConverter(IColumn paramIColumn1, IColumn paramIColumn2)
  {
    assert (paramIColumn1.getTypeMetadata().isExactNumericType());
    this.m_tgtPrecision = paramIColumn2.getTypeMetadata().getPrecision();
    this.m_tgtScale = paramIColumn2.getTypeMetadata().getScale();
    this.m_targetColLen = paramIColumn2.getColumnLength();
    this.m_targetIsSigned = paramIColumn2.getTypeMetadata().isSigned();
  }
  
  public ConversionResult convert(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    ConversionResult localConversionResult = new ConversionResult();
    BigDecimal localBigDecimal1 = paramISqlDataWrapper1.getExactNumber();
    if (null == localBigDecimal1)
    {
      paramISqlDataWrapper2.setNull();
      localConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
      return localConversionResult;
    }
    int i;
    switch (paramISqlDataWrapper2.getType())
    {
    case 2: 
    case 3: 
      BigDecimal localBigDecimal2 = ExactNumConverter.setPrecScale(localBigDecimal1, this.m_tgtPrecision, this.m_tgtScale, localConversionResult);
      paramISqlDataWrapper2.setExactNumber(localBigDecimal2);
      break;
    case 6: 
    case 8: 
      double d = ExactNumConverter.toDouble(localBigDecimal1, localConversionResult);
      paramISqlDataWrapper2.setDouble(d);
      break;
    case -7: 
    case 16: 
      boolean bool = ExactNumConverter.toBoolean(localBigDecimal1, localConversionResult);
      paramISqlDataWrapper2.setBoolean(bool);
      break;
    case 7: 
      float f = ExactNumConverter.toFloat(localBigDecimal1, localConversionResult);
      paramISqlDataWrapper2.setReal(f);
      break;
    case -5: 
      BigInteger localBigInteger = ExactNumConverter.toBigInt(localBigDecimal1, this.m_targetIsSigned, localConversionResult);
      paramISqlDataWrapper2.setBigInt(localBigInteger);
      break;
    case 4: 
      long l = ExactNumConverter.toInteger(localBigDecimal1, this.m_targetIsSigned, localConversionResult);
      paramISqlDataWrapper2.setInteger(l);
      break;
    case 5: 
      i = ExactNumConverter.toSmallInt(localBigDecimal1, this.m_targetIsSigned, localConversionResult);
      paramISqlDataWrapper2.setSmallInt(i);
      break;
    case -6: 
      i = ExactNumConverter.toTinyInt(localBigDecimal1, this.m_targetIsSigned, localConversionResult);
      paramISqlDataWrapper2.setTinyInt(i);
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      String str = ExactNumConverter.toChar(paramISqlDataWrapper1.getExactNumber(), this.m_targetColLen, localConversionResult);
      paramISqlDataWrapper2.setChar(str);
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
    return localConversionResult;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlExactNumberConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */