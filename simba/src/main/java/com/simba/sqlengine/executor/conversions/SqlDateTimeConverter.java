package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TimeTz;
import com.simba.dsi.dataengine.utilities.TimestampTz;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.conv.DateTimeConverter;
import com.simba.support.exceptions.ErrorException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class SqlDateTimeConverter
  implements ISqlConverter
{
  private final IColumn m_targetMeta;
  private final IColumn m_sourceMeta;
  
  public SqlDateTimeConverter(IColumn paramIColumn1, IColumn paramIColumn2)
  {
    this.m_targetMeta = paramIColumn2;
    this.m_sourceMeta = paramIColumn1;
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
      switch (paramISqlDataWrapper1.getType())
      {
      case 91: 
        convertFromDate(paramISqlDataWrapper1.getDate(), localConversionResult, paramISqlDataWrapper1, paramISqlDataWrapper2);
        break;
      case 92: 
        convertFromTime(paramISqlDataWrapper1.getTime(), localConversionResult, paramISqlDataWrapper1, paramISqlDataWrapper2);
        break;
      case 93: 
        convertFromTimestamp(paramISqlDataWrapper1.getTimestamp(), localConversionResult, paramISqlDataWrapper1, paramISqlDataWrapper2);
        break;
      default: 
        throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
      }
    }
    return localConversionResult;
  }
  
  private void convertFromTime(Time paramTime, ConversionResult paramConversionResult, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    Calendar localCalendar = null;
    TimeTz localTimeTz = null;
    if ((paramTime instanceof TimeTz))
    {
      localTimeTz = (TimeTz)paramTime;
      localCalendar = localTimeTz.getTimezoneCalendar();
    }
    else
    {
      localCalendar = Calendar.getInstance();
    }
    Object localObject;
    switch (paramISqlDataWrapper2.getType())
    {
    case 92: 
      localObject = DateTimeConverter.toTime(paramTime, paramConversionResult, this.m_targetMeta.getTypeMetadata().getPrecision(), localCalendar);
      if (null != localTimeTz) {
        localObject = new TimeTz((Time)localObject, localCalendar);
      }
      paramISqlDataWrapper2.setTime((Time)localObject);
      break;
    case 93: 
      localObject = DateTimeConverter.toTimestamp(paramTime, paramConversionResult, localCalendar, this.m_targetMeta.getTypeMetadata().getPrecision());
      if (null != localTimeTz) {
        localObject = new TimestampTz((Timestamp)localObject, localCalendar);
      }
      paramISqlDataWrapper2.setTimestamp((Timestamp)localObject);
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      localObject = DateTimeConverter.toChar(paramTime, this.m_targetMeta.getColumnLength(), this.m_sourceMeta.getTypeMetadata().getPrecision(), localCalendar, paramConversionResult);
      paramISqlDataWrapper2.setChar((String)localObject);
      break;
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
  }
  
  private void convertFromTimestamp(Timestamp paramTimestamp, ConversionResult paramConversionResult, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    Object localObject1;
    Object localObject2;
    switch (paramISqlDataWrapper2.getType())
    {
    case 93: 
      localObject1 = DateTimeConverter.toTimestamp(paramTimestamp, paramConversionResult, this.m_targetMeta.getTypeMetadata().getPrecision());
      if ((paramTimestamp instanceof TimestampTz)) {
        localObject1 = new TimestampTz((Timestamp)localObject1, ((TimestampTz)paramTimestamp).getTimezoneCalendar());
      }
      paramISqlDataWrapper2.setTimestamp((Timestamp)localObject1);
      break;
    case 91: 
      localObject1 = null;
      if ((paramTimestamp instanceof TimestampTz)) {
        localObject1 = ((TimestampTz)paramTimestamp).getTimezoneCalendar();
      } else {
        localObject1 = Calendar.getInstance();
      }
      localObject2 = DateTimeConverter.toDate(paramTimestamp, (Calendar)localObject1, paramConversionResult);
      paramISqlDataWrapper2.setDate((Date)localObject2);
      break;
    case 92: 
      localObject1 = null;
      if ((paramTimestamp instanceof TimestampTz)) {
        localObject1 = ((TimestampTz)paramTimestamp).getTimezoneCalendar();
      } else {
        localObject1 = Calendar.getInstance();
      }
      localObject2 = DateTimeConverter.toTime(paramTimestamp, (Calendar)localObject1, this.m_targetMeta.getTypeMetadata().getPrecision(), paramConversionResult);
      paramISqlDataWrapper2.setTime((Time)localObject2);
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      localObject1 = null;
      if ((paramTimestamp instanceof TimestampTz)) {
        localObject1 = ((TimestampTz)paramTimestamp).getTimezoneCalendar();
      } else {
        localObject1 = Calendar.getInstance();
      }
      localObject2 = DateTimeConverter.toChar(paramTimestamp, this.m_targetMeta.getColumnLength(), this.m_sourceMeta.getTypeMetadata().getPrecision(), (Calendar)localObject1, paramConversionResult);
      paramISqlDataWrapper2.setChar((String)localObject2);
      break;
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
  }
  
  private void convertFromDate(Date paramDate, ConversionResult paramConversionResult, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    Object localObject;
    switch (paramISqlDataWrapper2.getType())
    {
    case 93: 
      localObject = DateTimeConverter.toTimestamp(paramDate, paramConversionResult, Calendar.getInstance());
      paramISqlDataWrapper2.setTimestamp((Timestamp)localObject);
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      localObject = DateTimeConverter.toChar(paramISqlDataWrapper1.getDate(), this.m_targetMeta.getColumnLength(), Calendar.getInstance(), paramConversionResult);
      paramISqlDataWrapper2.setChar((String)localObject);
      break;
    default: 
      throw SQLEngineExceptionFactory.conversionNotSupported(TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper1.getType()), TypeUtilities.sqlTypeToString((short)paramISqlDataWrapper2.getType()));
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlDateTimeConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */