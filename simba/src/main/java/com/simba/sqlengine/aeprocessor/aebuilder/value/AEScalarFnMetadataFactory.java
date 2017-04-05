package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aetree.ScalarFunctionID;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionProperties;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionProperties.Builder;
import com.simba.sqlengine.aeprocessor.metadatautil.AEMetadataCoercionHandler;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataColumnInfo;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataUtilities;
import com.simba.sqlengine.aeprocessor.metadatautil.SqlTypes;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.dsiext.dataengine.utils.ScalarFunctionArgType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.conv.CharConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AEScalarFnMetadataFactory
{
  private final ICoercionHandler m_coercionHandler = new AEMetadataCoercionHandler();
  private AECoercionProperties m_coercionProperties = new AECoercionProperties.Builder().build();
  
  public ScalarFnMetadata createMetadata(SqlDataEngineContext paramSqlDataEngineContext, ScalarFunctionID paramScalarFunctionID, String paramString, List<? extends IColumnInfo> paramList)
    throws ErrorException
  {
    ScalarFnMetadata localScalarFnMetadata;
    Iterator localIterator;
    switch (paramScalarFunctionID)
    {
    case ASCII: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createAsciiMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case CHAR: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCharMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case CONCAT: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createConcatMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case INSERT: 
      if (4 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localIterator = paramList.iterator();
      localScalarFnMetadata = createInsertMetadata(paramSqlDataEngineContext, (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next());
      break;
    case LCASE: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLCaseMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case LEFT: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLeftMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case LENGTH: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLengthMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case LOCATE2: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLocateMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case LOCATE3: 
      if (3 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localIterator = paramList.iterator();
      localScalarFnMetadata = createLocateMetadata(paramSqlDataEngineContext, (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next());
      break;
    case LTRIM: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLTrimMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case REPEAT: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRepeatMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case REPLACE: 
      if (3 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localIterator = paramList.iterator();
      localScalarFnMetadata = createReplaceMetadata(paramSqlDataEngineContext, (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next());
      break;
    case RIGHT: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRightMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case RTRIM: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRTrimMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case SOUNDEX: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSoundexMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case SPACE: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSpaceMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case SUBSTRING3: 
      if (3 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localIterator = paramList.iterator();
      localScalarFnMetadata = createSubstringMetadata(paramSqlDataEngineContext, (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next());
      break;
    case SUBSTRING2: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSubstringMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case UCASE: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createUCaseMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case CURDATE: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurDateMetadata(paramSqlDataEngineContext);
      break;
    case CURTIME: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurTimeMetadata(paramSqlDataEngineContext);
      break;
    case CURRENT_DATE: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurrentDateMetadata(paramSqlDataEngineContext);
      break;
    case CURRENT_TIME: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurrentTimeMetadata(paramSqlDataEngineContext);
      break;
    case CURRENT_TIME1: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurrentTimeMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case CURRENT_TIMESTAMP: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurrentTimestampMetadata(paramSqlDataEngineContext);
      break;
    case CURRENT_TIMESTAMP1: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCurrentTimestampMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case DAYNAME: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createDayNameMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case DAYOFWEEK: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createDayOfWeekMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case DAYOFMONTH: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createDayOfMonthMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case DAYOFYEAR: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createDayOfYearMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case HOUR: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createHourMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case MINUTE: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createMinuteMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case MONTH: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createMonthMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case MONTHNAME: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createMonthNameMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case NOW: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createNowMetadata(paramSqlDataEngineContext);
      break;
    case QUARTER: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createQuarterMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case SECOND: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSecondMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case TIMESTAMPADD: 
      if (3 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localIterator = paramList.iterator();
      localScalarFnMetadata = createTimestampAddMetadata(paramSqlDataEngineContext, (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next());
      break;
    case TIMESTAMPDIFF: 
      if (3 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localIterator = paramList.iterator();
      localScalarFnMetadata = createTimestampDiffMetadata(paramSqlDataEngineContext, (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next(), (IColumnInfo)localIterator.next());
      break;
    case WEEK: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createWeekMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case WEEK_ISO: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createWeekISOMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case YEAR: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createYearMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case ABS: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createAbsMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case ACOS: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createACosMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case ASIN: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createASinMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case ATAN: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createATanMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case ATAN2: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createATanMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case CEILING: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCeilingMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case COS: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCosMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case COT: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCotMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case DEGREES: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createDegreesMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case EXP: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createExpMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case FLOOR: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createFloorMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case LOG: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLogMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case LOG10: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLog10Metadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case MOD: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createModMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case PI: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createPiMetadata(paramSqlDataEngineContext);
      break;
    case POWER: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createPowerMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case RADIANS: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRadiansMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case RAND0: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRandMetadata(paramSqlDataEngineContext);
      break;
    case RAND1: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRandMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case ROUND: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createRoundMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case SIGN: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSignMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case SIN: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSinMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case SQRT: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createSqrtMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case TAN: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createTanMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case TRUNCATE: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createTruncateMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case DATABASE: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createDatabaseMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case IFNULL: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createIfNullMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case NULL: 
      if (0 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createNullMetadata(paramSqlDataEngineContext);
      break;
    case USER: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createUserMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case CONVERT: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createConvertMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case CAST: 
      if (2 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createCastMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0), (IColumnInfo)paramList.get(1));
      break;
    case LOWER: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createLowerMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    case UPPER: 
      if (1 != paramList.size()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localScalarFnMetadata = createUpperMetadata(paramSqlDataEngineContext, (IColumnInfo)paramList.get(0));
      break;
    default: 
      localScalarFnMetadata = null;
    }
    if (null == localScalarFnMetadata) {
      throw SQLEngineExceptionFactory.featureNotImplementedException(paramScalarFunctionID.toString());
    }
    return localScalarFnMetadata;
  }
  
  public void validateMetadata(SqlDataEngineContext paramSqlDataEngineContext, ScalarFunctionID paramScalarFunctionID, String paramString, List<? extends IColumnInfo> paramList)
    throws ErrorException
  {
    validateArgTypes(paramSqlDataEngineContext, paramScalarFunctionID.getName(), paramScalarFunctionID.getArguments(), paramList);
  }
  
  private ICoercionHandler getCoercionHandler()
  {
    return this.m_coercionHandler;
  }
  
  public ScalarFnMetadata createAsciiMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createCharMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(1L), Arrays.asList(new ColumnMetadata[] { createIntMetadata() }));
  }
  
  public ScalarFnMetadata createConcatMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata1 = createStringMetadata(paramIColumnInfo1);
    ColumnMetadata localColumnMetadata2 = createStringMetadata(paramIColumnInfo2);
    ICoercionHandler localICoercionHandler = getCoercionHandler();
    IColumn localIColumn = localICoercionHandler.coerceConcatColumns(new MetadataColumnInfo(localColumnMetadata1, paramIColumnInfo1.getColumnType()), new MetadataColumnInfo(localColumnMetadata2, paramIColumnInfo2.getColumnType()));
    return new ScalarFnMetadata(localIColumn, Arrays.asList(new IColumn[] { localColumnMetadata1, localColumnMetadata2 }));
  }
  
  public ScalarFnMetadata createInsertMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumnInfo paramIColumnInfo3, IColumnInfo paramIColumnInfo4)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata1 = createStringMetadata(paramIColumnInfo1);
    ColumnMetadata localColumnMetadata2 = createStringMetadata(paramIColumnInfo4);
    ICoercionHandler localICoercionHandler = getCoercionHandler();
    IColumn localIColumn = localICoercionHandler.coerceConcatColumns(new MetadataColumnInfo(localColumnMetadata1, paramIColumnInfo1.getColumnType()), new MetadataColumnInfo(localColumnMetadata2, paramIColumnInfo4.getColumnType()));
    return new ScalarFnMetadata(ColumnMetadata.copyOf(localIColumn), Arrays.asList(new IColumn[] { localColumnMetadata1, createIntMetadata(), createIntMetadata(), localColumnMetadata2 }));
  }
  
  public ScalarFnMetadata createLCaseMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createLeftMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo1), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo1), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createLengthMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createLocateMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata1 = createStringMetadata(paramIColumnInfo1);
    ColumnMetadata localColumnMetadata2 = createStringMetadata(paramIColumnInfo2);
    long l = Math.max(localColumnMetadata1.getColumnLength(), localColumnMetadata2.getColumnLength());
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createStringMetadata(l), createStringMetadata(l) }));
  }
  
  public ScalarFnMetadata createLocateMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumnInfo paramIColumnInfo3)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata1 = createStringMetadata(paramIColumnInfo1);
    ColumnMetadata localColumnMetadata2 = createStringMetadata(paramIColumnInfo2);
    long l = Math.max(localColumnMetadata1.getColumnLength(), localColumnMetadata2.getColumnLength());
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createStringMetadata(l), createStringMetadata(l), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createLTrimMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createRepeatMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    long l1 = -1L;
    if (IColumnInfo.ColumnType.LITERAL == paramIColumnInfo2.getColumnType())
    {
      Long localLong = parseIntegerLiteral(paramIColumnInfo2);
      if (null != localLong) {
        l1 = Math.max(localLong.longValue(), 0L);
      }
    }
    long l2;
    if ((0L == l1) || ((0L < l1) && (this.m_coercionProperties.getMaxWvarcharLength() / l1 > paramIColumnInfo1.getColumnLength()))) {
      l2 = paramIColumnInfo1.getColumnLength() * l1;
    } else {
      l2 = Math.max(this.m_coercionProperties.getMaxVarcharlength(), paramIColumnInfo1.getColumnLength());
    }
    ColumnMetadata localColumnMetadata = createStringMetadata(paramIColumnInfo1.getType(), l2);
    return new ScalarFnMetadata(localColumnMetadata, Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo1), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createReplaceMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumnInfo paramIColumnInfo3)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata1 = createStringMetadata(paramIColumnInfo1);
    ColumnMetadata localColumnMetadata2 = createStringMetadata(paramIColumnInfo2);
    ColumnMetadata localColumnMetadata3 = createStringMetadata(paramIColumnInfo3);
    ColumnMetadata localColumnMetadata4 = createStringMetadata(localColumnMetadata1.getColumnLength() * Math.max(localColumnMetadata3.getColumnLength(), 1L));
    return new ScalarFnMetadata(ColumnMetadata.copyOf(localColumnMetadata4), Arrays.asList(new IColumn[] { localColumnMetadata1, localColumnMetadata2, localColumnMetadata3 }));
  }
  
  public ScalarFnMetadata createRightMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo1), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo1), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createRTrimMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createSoundexMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(4L), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createSpaceMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    long l = -1L;
    if (IColumnInfo.ColumnType.LITERAL == paramIColumnInfo.getColumnType())
    {
      Long localLong = parseIntegerLiteral(paramIColumnInfo);
      if (null != localLong) {
        l = Math.max(localLong.longValue(), 0L);
      }
    }
    if ((0L > l) || (l > this.m_coercionProperties.getMaxVarcharlength())) {
      l = this.m_coercionProperties.getMaxVarcharlength();
    }
    return new ScalarFnMetadata(createStringMetadata(l), Arrays.asList(new ColumnMetadata[] { createIntMetadata() }));
  }
  
  public ScalarFnMetadata createSubstringMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumnInfo paramIColumnInfo3)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo1), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo1), createIntMetadata(), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createSubstringMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo1), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo1), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createUCaseMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createCurDateMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDateMetadata(), Collections.emptyList());
  }
  
  public ScalarFnMetadata createCurTimeMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(createTimeMetadata((short)0), Collections.emptyList());
  }
  
  public ScalarFnMetadata createCurrentDateMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return createCurDateMetadata(paramSqlDataEngineContext);
  }
  
  public ScalarFnMetadata createCurrentTimeMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return createCurTimeMetadata(paramSqlDataEngineContext);
  }
  
  public ScalarFnMetadata createCurrentTimeMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createTimeMetadata(this.m_coercionProperties.getMaxTimePrecision()), Arrays.asList(new ColumnMetadata[] { createIntMetadata() }));
  }
  
  public ScalarFnMetadata createCurrentTimestampMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(createTimestampMetadata(), Collections.emptyList());
  }
  
  public ScalarFnMetadata createCurrentTimestampMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createTimestampMetadata(), Arrays.asList(new ColumnMetadata[] { createIntMetadata() }));
  }
  
  public ScalarFnMetadata createDayNameMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(32L), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createDayOfMonthMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createDayOfWeekMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createDayOfYearMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createHourMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createTimeMetadata(0) }));
  }
  
  public ScalarFnMetadata createMinuteMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createTimeMetadata(0) }));
  }
  
  public ScalarFnMetadata createMonthMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createMonthNameMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(32L), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createNowMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(createTimestampMetadata(), Collections.emptyList());
  }
  
  public ScalarFnMetadata createQuarterMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createSecondMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createTimeMetadata(0) }));
  }
  
  public ScalarFnMetadata createTimestampAddMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumnInfo paramIColumnInfo3)
    throws ErrorException
  {
    return null;
  }
  
  public ScalarFnMetadata createTimestampDiffMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumnInfo paramIColumnInfo3)
    throws ErrorException
  {
    return null;
  }
  
  public ScalarFnMetadata createWeekMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createWeekISOMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createYearMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createDateMetadata() }));
  }
  
  public ScalarFnMetadata createAbsMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata = createNumericMetadata(paramIColumnInfo);
    if (localColumnMetadata.getTypeMetadata().isIntegerType()) {
      localColumnMetadata.getTypeMetadata().setSigned(false);
    }
    return new ScalarFnMetadata(localColumnMetadata, Arrays.asList(new ColumnMetadata[] { createNumericMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createACosMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createASinMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createATanMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createATanMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata(), createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createCeilingMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata = createNumericMetadata(paramIColumnInfo);
    if (localColumnMetadata.getTypeMetadata().isExactNumericType())
    {
      TypeMetadata localTypeMetadata = localColumnMetadata.getTypeMetadata();
      localTypeMetadata.setPrecision((short)(localTypeMetadata.getPrecision() + 1));
      localTypeMetadata.setScale((short)0);
    }
    return new ScalarFnMetadata(localColumnMetadata, Arrays.asList(new ColumnMetadata[] { createNumericMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createCosMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createCotMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createDegreesMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createExpMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createFloorMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata = createNumericMetadata(paramIColumnInfo);
    if (localColumnMetadata.getTypeMetadata().isExactNumericType())
    {
      TypeMetadata localTypeMetadata = localColumnMetadata.getTypeMetadata();
      localTypeMetadata.setPrecision((short)(localTypeMetadata.getPrecision() + 1));
      localTypeMetadata.setScale((short)0);
    }
    return new ScalarFnMetadata(localColumnMetadata, Arrays.asList(new ColumnMetadata[] { createNumericMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createLogMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createLog10Metadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createModMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList(2);
    ColumnMetadata localColumnMetadata1;
    if ((IColumnInfo.ColumnType.PARAMETER_UNSET == paramIColumnInfo1.getColumnType()) && (IColumnInfo.ColumnType.PARAMETER_UNSET == paramIColumnInfo2.getColumnType()))
    {
      localColumnMetadata1 = createBigIntMetadata();
      localArrayList.add(createBigIntMetadata());
      localArrayList.add(createBigIntMetadata());
    }
    else
    {
      ColumnMetadata localColumnMetadata2 = createNumericMetadata(paramIColumnInfo1);
      ColumnMetadata localColumnMetadata3 = createNumericMetadata(paramIColumnInfo2);
      IColumn localIColumn = getCoercionHandler().coerceUnionColumns(new MetadataColumnInfo(localColumnMetadata2, paramIColumnInfo1.getColumnType()), new MetadataColumnInfo(localColumnMetadata3, paramIColumnInfo2.getColumnType()));
      localColumnMetadata1 = ColumnMetadata.copyOf(localIColumn);
      localArrayList.add(ColumnMetadata.copyOf(localIColumn));
      localArrayList.add(ColumnMetadata.copyOf(localIColumn));
    }
    return new ScalarFnMetadata(localColumnMetadata1, localArrayList);
  }
  
  public ScalarFnMetadata createPiMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Collections.emptyList());
  }
  
  public ScalarFnMetadata createPowerMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata(), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createRadiansMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createRandMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Collections.emptyList());
  }
  
  public ScalarFnMetadata createRandMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createIntMetadata() }));
  }
  
  public ScalarFnMetadata createRoundMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    Object localObject;
    if (TypeUtilities.isExactNumericType(paramIColumnInfo1.getType())) {
      localObject = this.m_coercionHandler.coercePlusColumns(paramIColumnInfo1, paramIColumnInfo1);
    } else {
      localObject = createNumericMetadata(paramIColumnInfo1);
    }
    return new ScalarFnMetadata((IColumn)localObject, Arrays.asList(new ColumnMetadata[] { createNumericMetadata(paramIColumnInfo1), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createSignMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createIntMetadata(), Arrays.asList(new ColumnMetadata[] { createNumericMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createSinMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createSqrtMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createTanMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createDoubleMetadata(), Arrays.asList(new ColumnMetadata[] { createDoubleMetadata() }));
  }
  
  public ScalarFnMetadata createTruncateMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return new ScalarFnMetadata(createNumericMetadata(paramIColumnInfo1), Arrays.asList(new ColumnMetadata[] { createNumericMetadata(paramIColumnInfo1), createIntMetadata() }));
  }
  
  public ScalarFnMetadata createDatabaseMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createIfNullMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    IColumn localIColumn = getCoercionHandler().coerceUnionColumns(new MetadataColumnInfo(MetadataUtilities.createColumnMetadata(paramIColumnInfo1), paramIColumnInfo1.getColumnType()), new MetadataColumnInfo(MetadataUtilities.createColumnMetadata(paramIColumnInfo2), paramIColumnInfo2.getColumnType()));
    return new ScalarFnMetadata(ColumnMetadata.copyOf(localIColumn), Arrays.asList(new IColumn[] { ColumnMetadata.copyOf(localIColumn), ColumnMetadata.copyOf(localIColumn) }));
  }
  
  public ScalarFnMetadata createNullMetadata(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return new ScalarFnMetadata(new AENull().getColumn(), Collections.emptyList());
  }
  
  public ScalarFnMetadata createUserMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createConvertMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    if (IColumnInfo.ColumnType.LITERAL != paramIColumnInfo2.getColumnType()) {
      throw SQLEngineExceptionFactory.invalidSecondArgumentToConvertException();
    }
    String str = paramIColumnInfo2.getLiteralString();
    int i;
    try
    {
      i = SqlTypes.valueOf(str.toUpperCase()).getSqlType();
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw SQLEngineExceptionFactory.invalidSecondArgumentToConvertException();
    }
    ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(i));
    if (localColumnMetadata.getTypeMetadata().isCharacterType()) {
      try
      {
        setOutputCharLength(localColumnMetadata, paramIColumnInfo1.getDisplaySize());
      }
      catch (NumericOverflowException localNumericOverflowException) {}
    }
    return new ScalarFnMetadata(localColumnMetadata, Arrays.asList(new ColumnMetadata[] { localColumnMetadata, createStringMetadata(paramIColumnInfo2) }));
  }
  
  public ScalarFnMetadata createCastMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    if (IColumnInfo.ColumnType.LITERAL != paramIColumnInfo2.getColumnType()) {
      throw SQLEngineExceptionFactory.invalidSecondArgumentToCastException();
    }
    String str = paramIColumnInfo2.getLiteralString();
    int i = paramSqlDataEngineContext.getSqlTypeForTypeName(str);
    if (0 == i) {
      try
      {
        i = SqlTypes.valueOf(str.toUpperCase()).getSqlType();
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        throw SQLEngineExceptionFactory.invalidSecondArgumentToCastException();
      }
    }
    ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(i));
    if (localColumnMetadata.getTypeMetadata().isCharacterType()) {
      try
      {
        setOutputCharLength(localColumnMetadata, paramIColumnInfo1.getDisplaySize());
      }
      catch (NumericOverflowException localNumericOverflowException) {}
    }
    return new ScalarFnMetadata(localColumnMetadata, Arrays.asList(new ColumnMetadata[] { localColumnMetadata, createStringMetadata(paramIColumnInfo2) }));
  }
  
  public ScalarFnMetadata createLowerMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  public ScalarFnMetadata createUpperMetadata(SqlDataEngineContext paramSqlDataEngineContext, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    return new ScalarFnMetadata(createStringMetadata(paramIColumnInfo), Arrays.asList(new ColumnMetadata[] { createStringMetadata(paramIColumnInfo) }));
  }
  
  static AEScalarFnMetadataFactory getInstance()
  {
    return InstanceHolder.INSTANCE;
  }
  
  private ColumnMetadata createDoubleMetadata()
  {
    try
    {
      return new ColumnMetadata(TypeMetadata.createTypeMetadata(8));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createBigIntMetadata()
  {
    try
    {
      return new ColumnMetadata(TypeMetadata.createTypeMetadata(-5));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createIntMetadata()
  {
    try
    {
      return new ColumnMetadata(TypeMetadata.createTypeMetadata(4));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createUnknownMetadata()
  {
    try
    {
      return new ColumnMetadata(TypeMetadata.createTypeMetadata(0));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createStringMetadata(long paramLong)
  {
    try
    {
      ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(12));
      localColumnMetadata.setColumnLength(paramLong);
      return localColumnMetadata;
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
  }
  
  private ColumnMetadata createStringMetadata(IColumnInfo paramIColumnInfo)
  {
    if ((IColumnInfo.ColumnType.PARAMETER_UNSET == paramIColumnInfo.getColumnType()) || (!TypeUtilities.isCharacterType(paramIColumnInfo.getType()))) {
      return createStringMetadata(255L);
    }
    try
    {
      ColumnMetadata localColumnMetadata = new ColumnMetadata(MetadataUtilities.createTypeMetadata(paramIColumnInfo));
      localColumnMetadata.setColumnLength(paramIColumnInfo.getColumnLength());
      return localColumnMetadata;
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createStringMetadata(int paramInt, long paramLong)
    throws ErrorException
  {
    ColumnMetadata localColumnMetadata;
    if (SqlTypes.getValueOf(paramInt).isWChar())
    {
      if (this.m_coercionProperties.getMaxWvarcharLength() < paramLong) {
        localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-10));
      } else if (this.m_coercionProperties.getMaxWcharlength() < paramLong) {
        localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-9));
      } else {
        localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-8));
      }
    }
    else if (this.m_coercionProperties.getMaxVarcharlength() < paramLong) {
      localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-1));
    } else if (this.m_coercionProperties.getMaxCharLength() < paramLong) {
      localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(12));
    } else {
      localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(1));
    }
    try
    {
      localColumnMetadata.setColumnLength(paramLong);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
    return localColumnMetadata;
  }
  
  private ColumnMetadata createTimeMetadata(short paramShort)
  {
    try
    {
      TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(92);
      localTypeMetadata.setPrecision(paramShort);
      localTypeMetadata.setScale(paramShort);
      return new ColumnMetadata(localTypeMetadata);
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createTimestampMetadata()
  {
    try
    {
      return new ColumnMetadata(TypeMetadata.createTypeMetadata(93));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createDateMetadata()
  {
    try
    {
      return new ColumnMetadata(TypeMetadata.createTypeMetadata(91));
    }
    catch (ErrorException localErrorException)
    {
      throw new AssertionError(localErrorException);
    }
  }
  
  private ColumnMetadata createNumericMetadata(IColumnInfo paramIColumnInfo)
  {
    if (IColumnInfo.ColumnType.PARAMETER_UNSET == paramIColumnInfo.getColumnType()) {
      return createUnknownMetadata();
    }
    if (AEUtils.isTypeNumeric(paramIColumnInfo.getType())) {
      try
      {
        return new ColumnMetadata(MetadataUtilities.createTypeMetadata(paramIColumnInfo));
      }
      catch (ErrorException localErrorException)
      {
        throw new AssertionError(localErrorException);
      }
    }
    return createDoubleMetadata();
  }
  
  private boolean isValidTypeMetadata(SqlDataEngineContext paramSqlDataEngineContext, ScalarFunctionArgType paramScalarFunctionArgType, short paramShort)
    throws ErrorException
  {
    return MetadataUtilities.isConversionLegal(paramShort, paramScalarFunctionArgType);
  }
  
  private Long parseIntegerLiteral(IColumnInfo paramIColumnInfo)
  {
    if (IColumnInfo.ColumnType.LITERAL != paramIColumnInfo.getColumnType()) {
      throw new IllegalArgumentException();
    }
    ConversionResult localConversionResult = new ConversionResult();
    long l = CharConverter.toInteger(paramIColumnInfo.getLiteralString(), localConversionResult, paramIColumnInfo.isSigned());
    switch (localConversionResult.getState())
    {
    case FRAC_TRUNCATION_ROUNDED_DOWN: 
    case FRAC_TRUNCATION_ROUNDED_UP: 
    case SUCCESS: 
      return Long.valueOf(l);
    }
    return null;
  }
  
  private void setOutputCharLength(ColumnMetadata paramColumnMetadata, long paramLong)
    throws NumericOverflowException
  {
    assert (paramColumnMetadata.getTypeMetadata().isCharacterType());
    if (4294967294L < paramLong) {
      throw new NumericOverflowException();
    }
    switch (paramColumnMetadata.getTypeMetadata().getType())
    {
    case 1: 
      paramColumnMetadata.setColumnLength(Math.min(paramLong, this.m_coercionProperties.getMaxCharLength()));
      break;
    case 12: 
      paramColumnMetadata.setColumnLength(Math.min(paramLong, this.m_coercionProperties.getMaxVarcharlength()));
      break;
    case -1: 
      paramColumnMetadata.setColumnLength(paramLong);
      break;
    case -8: 
      paramColumnMetadata.setColumnLength(Math.min(paramLong, this.m_coercionProperties.getMaxWcharlength()));
      break;
    case -9: 
      paramColumnMetadata.setColumnLength(Math.min(paramLong, this.m_coercionProperties.getMaxWvarcharLength()));
      break;
    case -10: 
      paramColumnMetadata.setColumnLength(paramLong);
      break;
    default: 
      throw new IllegalArgumentException();
    }
  }
  
  private void validateTypeMetadata(SqlDataEngineContext paramSqlDataEngineContext, String paramString, int paramInt, ScalarFunctionArgType paramScalarFunctionArgType, IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    if (!isValidTypeMetadata(paramSqlDataEngineContext, paramScalarFunctionArgType, paramIColumnInfo.getType())) {
      throw SQLEngineExceptionFactory.invalidScalarFnArgumentTypeException(paramString, paramInt, paramIColumnInfo.getType());
    }
  }
  
  private void validateArgTypes(SqlDataEngineContext paramSqlDataEngineContext, String paramString, List<ScalarFunctionArgType> paramList, List<? extends IColumnInfo> paramList1)
    throws ErrorException
  {
    if ((paramString.equalsIgnoreCase("USER")) || (paramString.equalsIgnoreCase("DATABASE")))
    {
      if ((0 != paramList.size()) || (1 != paramList1.size())) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
    }
    else if (paramList.size() != paramList1.size()) {
      throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
    }
    for (int i = 0; i < paramList.size(); i++) {
      validateTypeMetadata(paramSqlDataEngineContext, paramString, i + 1, (ScalarFunctionArgType)paramList.get(i), (IColumnInfo)paramList1.get(i));
    }
  }
  
  private static final class InstanceHolder
  {
    public static final AEScalarFnMetadataFactory INSTANCE = new AEScalarFnMetadataFactory(null);
  }
  
  public static final class ScalarFnMetadata
  {
    private final IColumn m_columnMetadata;
    private final List<IColumn> m_expectedArgumentMetadata;
    
    public ScalarFnMetadata(IColumn paramIColumn, List<? extends IColumn> paramList)
    {
      if (null == paramIColumn) {
        throw new NullPointerException("columnMetadata");
      }
      this.m_columnMetadata = paramIColumn;
      this.m_expectedArgumentMetadata = new ArrayList(paramList);
    }
    
    public IColumn getColumnMetadata()
    {
      return this.m_columnMetadata;
    }
    
    public List<IColumn> getExpectedArgumentMetadata()
    {
      return this.m_expectedArgumentMetadata;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEScalarFnMetadataFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */