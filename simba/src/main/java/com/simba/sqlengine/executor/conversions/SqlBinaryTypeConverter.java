package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.BinaryConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.exceptions.ErrorException;

public class SqlBinaryTypeConverter
  implements ISqlConverter
{
  private final long m_targetColLen;
  
  public SqlBinaryTypeConverter(IColumn paramIColumn1, IColumn paramIColumn2)
  {
    if ((!paramIColumn1.getTypeMetadata().isBinaryType()) || (!paramIColumn2.getTypeMetadata().isBinaryType())) {
      throw new IllegalArgumentException("Unsupported source to target type conversion: source type: " + paramIColumn1.getTypeMetadata().getTypeName() + " , " + "target type: " + paramIColumn2.getTypeMetadata().getTypeName());
    }
    this.m_targetColLen = paramIColumn2.getColumnLength();
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
      byte[] arrayOfByte1 = paramISqlDataWrapper1.getBinary();
      switch (paramISqlDataWrapper2.getType())
      {
      case -4: 
      case -3: 
      case -2: 
        byte[] arrayOfByte2 = BinaryConverter.toBinary(arrayOfByte1, this.m_targetColLen, localConversionResult);
        paramISqlDataWrapper2.setBinary(arrayOfByte2);
        break;
      default: 
        throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
      }
    }
    return localConversionResult;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlBinaryTypeConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */