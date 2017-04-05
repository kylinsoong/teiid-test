package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlCharDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlDoubleDataWrapper;
import com.simba.sqlengine.executor.etree.value.ETConstant;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.scalar.ETACosFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETASinFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETATan2Fn;
import com.simba.sqlengine.executor.etree.value.scalar.ETATanFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETAbsFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETAsciiFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCeilingFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCharFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETConcatFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCosFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCotFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCurDateFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCurTimeFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCurTimestampFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETCurrentTime1Fn;
import com.simba.sqlengine.executor.etree.value.scalar.ETDatabaseFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETDayNameFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETDayOfMonthFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETDayOfWeekFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETDayOfYearFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETDegreesFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETExpFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETFloorFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETHourFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETIfNullFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETInsertFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETLTrim;
import com.simba.sqlengine.executor.etree.value.scalar.ETLeftFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETLengthFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETLocateFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETLog10Fn;
import com.simba.sqlengine.executor.etree.value.scalar.ETLogFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETLowerFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETMinuteFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETModFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETMonthFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETMonthNameFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETPowerFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETQuarterFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETRTrim;
import com.simba.sqlengine.executor.etree.value.scalar.ETRadiansFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETRandFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETRepeatFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETReplaceFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETRightFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETRoundFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSecondFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSignFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSinFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSoundexFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSpaceFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSqrtFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETSubstringFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETTanFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETTruncateFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETUCaseFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETUserFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETWeekFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETWeekIsoFn;
import com.simba.sqlengine.executor.etree.value.scalar.ETYearFn;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ETScalarFnFactory
{
  public static ETValueExpr makeNewScalarFn(AEScalarFn paramAEScalarFn, List<IColumn> paramList, ArrayList<ETValueExpr> paramArrayList, MaterializerContext paramMaterializerContext)
    throws ErrorException
  {
    assert (paramArrayList.size() == paramList.size());
    Object localObject;
    switch (paramAEScalarFn.getScalarFnId())
    {
    case ABS: 
      return new ETAbsFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case ACOS: 
      return new ETACosFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case ASCII: 
      return new ETAsciiFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case ASIN: 
      return new ETASinFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case ATAN: 
      return new ETATanFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case ATAN2: 
      return new ETATan2Fn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CAST: 
    case CONVERT: 
      return (ETValueExpr)paramArrayList.get(0);
    case CEILING: 
      return new ETCeilingFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CHAR: 
      return new ETCharFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CONCAT: 
      return new ETConcatFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case COS: 
      return new ETCosFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case COT: 
      return new ETCotFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CURDATE: 
    case CURRENT_DATE: 
      return new ETCurDateFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CURTIME: 
    case CURRENT_TIME: 
      return new ETCurTimeFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CURRENT_TIME1: 
      return new ETCurrentTime1Fn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case CURRENT_TIMESTAMP: 
    case NOW: 
      assert (paramArrayList.isEmpty());
    case CURRENT_TIMESTAMP1: 
      return new ETCurTimestampFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case DATABASE: 
      return new ETDatabaseFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case DAYNAME: 
      return new ETDayNameFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case DAYOFMONTH: 
      return new ETDayOfMonthFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case DAYOFWEEK: 
      return new ETDayOfWeekFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case DAYOFYEAR: 
      return new ETDayOfYearFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case DEGREES: 
      return new ETDegreesFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case EXP: 
      return new ETExpFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case FLOOR: 
      return new ETFloorFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case HOUR: 
      return new ETHourFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case IFNULL: 
      return new ETIfNullFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case INSERT: 
      return new ETInsertFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LCASE: 
    case LOWER: 
      return new ETLowerFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LEFT: 
      return new ETLeftFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LENGTH: 
      return new ETLengthFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LOG: 
      return new ETLogFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LOG10: 
      return new ETLog10Fn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LOCATE2: 
    case LOCATE3: 
      return new ETLocateFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case LTRIM: 
      return new ETLTrim(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case MINUTE: 
      return new ETMinuteFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case MOD: 
      return new ETModFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case MONTH: 
      return new ETMonthFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case MONTHNAME: 
      return new ETMonthNameFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case NULL: 
      localObject = new SqlCharDataWrapper(1);
      ((SqlCharDataWrapper)localObject).setNull();
      return new ETConstant((ISqlDataWrapper)localObject);
    case PI: 
      localObject = new SqlDoubleDataWrapper(8);
      ((SqlDoubleDataWrapper)localObject).setDouble(3.141592653589793D);
      return new ETConstant((ISqlDataWrapper)localObject);
    case POWER: 
      return new ETPowerFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case QUARTER: 
      return new ETQuarterFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case RADIANS: 
      return new ETRadiansFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case RAND0: 
      localObject = new SqlDoubleDataWrapper(8);
      ((SqlDoubleDataWrapper)localObject).setDouble(new Random().nextDouble());
      return new ETConstant((ISqlDataWrapper)localObject);
    case RAND1: 
      return new ETRandFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case REPEAT: 
      return new ETRepeatFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case REPLACE: 
      return new ETReplaceFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case RIGHT: 
      return new ETRightFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case ROUND: 
      return new ETRoundFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case RTRIM: 
      return new ETRTrim(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SECOND: 
      return new ETSecondFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SIGN: 
      return new ETSignFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SIN: 
      return new ETSinFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SOUNDEX: 
      return new ETSoundexFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SPACE: 
      return new ETSpaceFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SQRT: 
      return new ETSqrtFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case SUBSTRING2: 
    case SUBSTRING3: 
      return new ETSubstringFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case TAN: 
      return new ETTanFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case TRUNCATE: 
      return new ETTruncateFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case UCASE: 
    case UPPER: 
      return new ETUCaseFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case USER: 
      return new ETUserFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case WEEK: 
      return new ETWeekFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case WEEK_ISO: 
      return new ETWeekIsoFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    case YEAR: 
      return new ETYearFn(paramAEScalarFn.getColumn(), paramArrayList, paramList);
    }
    throw SQLEngineExceptionFactory.featureNotImplementedException("scalar function not supported");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETScalarFnFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */