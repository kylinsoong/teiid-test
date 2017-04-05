package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.CharConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.UUID;

public class SqlCharTypeConverter
  implements ISqlConverter
{
  private final short m_tgtPrecision;
  private final short m_tgtScale;
  private final boolean m_tgtIsSigned;
  private final IColumn m_targetMetadata;
  
  public SqlCharTypeConverter(IColumn paramIColumn1, IColumn paramIColumn2)
  {
    assert (TypeUtilities.isCharacterType(paramIColumn1.getTypeMetadata().getType()));
    this.m_targetMetadata = paramIColumn2;
    TypeMetadata localTypeMetadata = paramIColumn2.getTypeMetadata();
    this.m_tgtPrecision = localTypeMetadata.getPrecision();
    this.m_tgtScale = localTypeMetadata.getScale();
    this.m_tgtIsSigned = localTypeMetadata.isSigned();
  }
  
  public ConversionResult convert(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    ConversionResult localConversionResult = new ConversionResult();
    if (paramISqlDataWrapper1.isNull())
    {
      paramISqlDataWrapper2.setNull();
      localConversionResult.setState(ConversionResult.TypeConversionState.SUCCESS);
    }
    else
    {
      String str = paramISqlDataWrapper1.getChar();
      Object localObject1;
      boolean bool;
      Object localObject2;
      switch (paramISqlDataWrapper2.getType())
      {
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        localObject1 = CharConverter.toChar(str, this.m_targetMetadata.getColumnLength(), localConversionResult);
        paramISqlDataWrapper2.setChar((String)localObject1);
        break;
      case -5: 
        localObject1 = CharConverter.toBigInt(str, localConversionResult, this.m_tgtIsSigned);
        paramISqlDataWrapper2.setBigInt((BigInteger)localObject1);
        break;
      case 16: 
        bool = CharConverter.toBoolean(str, localConversionResult);
        paramISqlDataWrapper2.setBoolean(bool);
        break;
      case -7: 
        bool = CharConverter.toBit(str, localConversionResult);
        paramISqlDataWrapper2.setBoolean(bool);
        break;
      case 91: 
        Date localDate = CharConverter.toDate(str, localConversionResult, new GregorianCalendar());
        paramISqlDataWrapper2.setDate(localDate);
        break;
      case 6: 
      case 8: 
        double d = CharConverter.toDouble(str, localConversionResult);
        paramISqlDataWrapper2.setDouble(d);
        break;
      case -11: 
        UUID localUUID = CharConverter.toGUID(str, localConversionResult);
        paramISqlDataWrapper2.setGuid(localUUID);
        break;
      case 4: 
        long l = CharConverter.toInteger(str, localConversionResult, this.m_tgtIsSigned);
        paramISqlDataWrapper2.setInteger(l);
        break;
      case 7: 
        float f = CharConverter.toReal(str, localConversionResult);
        paramISqlDataWrapper2.setReal(f);
        break;
      case 5: 
        int i = CharConverter.toSmallint(str, localConversionResult, this.m_tgtIsSigned);
        paramISqlDataWrapper2.setSmallInt(i);
        break;
      case 92: 
        Time localTime = CharConverter.toTime(str, localConversionResult, this.m_tgtPrecision, new GregorianCalendar());
        paramISqlDataWrapper2.setTime(localTime);
        break;
      case -6: 
        short s = CharConverter.toTinyint(str, localConversionResult, this.m_tgtIsSigned);
        paramISqlDataWrapper2.setTinyInt(s);
        break;
      case 93: 
        localObject2 = CharConverter.toTimestamp(str, localConversionResult, this.m_tgtPrecision, new GregorianCalendar());
        paramISqlDataWrapper2.setTimestamp((Timestamp)localObject2);
        break;
      case 2: 
      case 3: 
        localObject2 = CharConverter.toExactNum(str, localConversionResult, this.m_tgtPrecision, this.m_tgtScale);
        paramISqlDataWrapper2.setExactNumber((BigDecimal)localObject2);
        break;
      default: 
        throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
      }
    }
    return localConversionResult;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlCharTypeConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */