package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.InvalidOperationException;
import com.simba.sqlengine.executor.datawrapper.DefaultSqlDataWrapper;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.conv.ConversionResult;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public class ConversionUtil
{
  public static final void checkForErrorOnly(ConversionResult paramConversionResult, boolean paramBoolean, int paramInt1, int paramInt2)
    throws ErrorException
  {
    switch (paramConversionResult.getState())
    {
    case DATETIME_OVERFLOW: 
      if (paramBoolean) {
        throw new ErrorException(DiagState.DIAG_DATETIME_OVERFLOW, 7, SQLEngineMessageKey.DATETIME_OVERFLOW.name(), paramInt2, paramInt1);
      }
      break;
    case FRAC_TRUNCATION_ROUNDED_UP: 
    case FRAC_TRUNCATION_ROUNDED_DOWN: 
      if (paramBoolean) {
        throw new ErrorException(DiagState.DIAG_FRACTIONAL_TRUNC, 7, SQLEngineMessageKey.FRAC_TRUNCATION.name(), paramInt2, paramInt1);
      }
      break;
    case INTERVAL_OVERFLOW_TOO_LARGE: 
    case INTERVAL_OVERFLOW_TOO_SMALL: 
      throw new ErrorException(DiagState.DIAG_INTERVAL_OVERFLOW, 7, SQLEngineMessageKey.INTERVAL_FIELD_OVERFLOW.name(), paramInt2, paramInt1);
    case INVALID_CHAR_VAL_FOR_CAST: 
      throw new ErrorException(DiagState.DIAG_INVALID_CHAR_VAL_FOR_CAST, 7, SQLEngineMessageKey.INVALID_CHAR_FOR_CAST.name(), paramInt2, paramInt1);
    case INVALID_DATA: 
      throw new ErrorException(DiagState.DIAG_GENERAL_ERROR, 7, SQLEngineMessageKey.CONV_LOGIC_ERROR.name(), paramInt2, paramInt1);
    case NUMERIC_OUT_OF_RANGE_TOO_LARGE: 
    case NUMERIC_OUT_OF_RANGE_TOO_SMALL: 
      throw new ErrorException(DiagState.DIAG_NUM_VAL_OUT_OF_RANGE, 7, SQLEngineMessageKey.CONV_NUMERIC_OUT_OF_RANGE.name(), paramInt2, paramInt1);
    case RESTRICTED_DATA_TYPE_ATTR_VIOLATION: 
      throw new ErrorException(DiagState.DIAG_RESTRICTED_DATA_TYPE_ATTR_VIOLATION, 7, SQLEngineMessageKey.INVALID_MULTI_FIELD_INTERVAL_CONV.name(), paramInt2, paramInt1);
    case STRING_RIGHT_TRUNCATION: 
      if (paramBoolean) {
        throw new ErrorException(DiagState.DIAG_STR_RIGHT_TRUNC_ERR, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), paramInt2, paramInt1);
      }
      break;
    }
  }
  
  public static final void checkResult(ConversionResult paramConversionResult, boolean paramBoolean1, boolean paramBoolean2, IWarningListener paramIWarningListener, int paramInt1, int paramInt2)
    throws ErrorException
  {
    switch (paramConversionResult.getState())
    {
    case SUCCESS: 
      break;
    case DATETIME_OVERFLOW: 
      if (paramBoolean1) {
        throw new ErrorException(DiagState.DIAG_DATETIME_OVERFLOW, 7, SQLEngineMessageKey.DATETIME_OVERFLOW.name(), paramInt2, paramInt1);
      }
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 7, SQLEngineMessageKey.DATETIME_OVERFLOW.name(), paramInt2, paramInt1));
      }
      break;
    case FRAC_TRUNCATION_ROUNDED_UP: 
    case FRAC_TRUNCATION_ROUNDED_DOWN: 
      if (paramBoolean1) {
        throw new ErrorException(DiagState.DIAG_FRACTIONAL_TRUNC, 7, SQLEngineMessageKey.FRAC_TRUNCATION.name(), paramInt2, paramInt1);
      }
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 7, SQLEngineMessageKey.FRAC_TRUNCATION.name(), paramInt2, paramInt1));
      }
      break;
    case INTERVAL_OVERFLOW_TOO_LARGE: 
    case INTERVAL_OVERFLOW_TOO_SMALL: 
      throw new ErrorException(DiagState.DIAG_INTERVAL_OVERFLOW, 7, SQLEngineMessageKey.INTERVAL_FIELD_OVERFLOW.name(), paramInt2, paramInt1);
    case INVALID_CHAR_VAL_FOR_CAST: 
      throw new ErrorException(DiagState.DIAG_INVALID_CHAR_VAL_FOR_CAST, 7, SQLEngineMessageKey.INVALID_CHAR_FOR_CAST.name(), paramInt2, paramInt1);
    case INVALID_DATA: 
      throw new ErrorException(DiagState.DIAG_GENERAL_ERROR, 7, SQLEngineMessageKey.CONV_LOGIC_ERROR.name(), paramInt2, paramInt1);
    case NUMERIC_OUT_OF_RANGE_TOO_LARGE: 
    case NUMERIC_OUT_OF_RANGE_TOO_SMALL: 
      throw new ErrorException(DiagState.DIAG_NUM_VAL_OUT_OF_RANGE, 7, SQLEngineMessageKey.CONV_NUMERIC_OUT_OF_RANGE.name(), paramInt2, paramInt1);
    case RESTRICTED_DATA_TYPE_ATTR_VIOLATION: 
      throw new ErrorException(DiagState.DIAG_RESTRICTED_DATA_TYPE_ATTR_VIOLATION, 7, SQLEngineMessageKey.INVALID_MULTI_FIELD_INTERVAL_CONV.name(), paramInt2, paramInt1);
    case STRING_RIGHT_TRUNCATION: 
      if (paramBoolean2) {
        throw new ErrorException(DiagState.DIAG_STR_RIGHT_TRUNC_ERR, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), paramInt2, paramInt1);
      }
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), paramInt2, paramInt1));
      }
      break;
    case INTEGRAL_PRECISION_LOSS: 
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.GENERAL_WARNING, 7, SQLEngineMessageKey.INTEGRAL_PRECISION_LOSS.name(), paramInt2, paramInt1));
      }
      break;
    default: 
      throw new InvalidOperationException();
    }
  }
  
  public static final void checkResult(ConversionResult paramConversionResult, IWarningListener paramIWarningListener, int paramInt1, int paramInt2)
    throws ErrorException
  {
    checkResult(paramConversionResult, false, false, paramIWarningListener, paramInt1, paramInt2);
  }
  
  public static boolean doConvert(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper, ISqlConverter paramISqlConverter, IWarningListener paramIWarningListener, boolean paramBoolean)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
    ConversionResult localConversionResult = paramISqlConverter.convert(paramISqlDataWrapper, localISqlDataWrapper);
    checkResult(localConversionResult, false, paramBoolean, paramIWarningListener, -1, -1);
    boolean bool = false;
    if (!localISqlDataWrapper.isNull())
    {
      if (paramETDataRequest.getMetadata().isBinaryType()) {
        bool = DataRetrievalUtil.retrieveBinaryData(localISqlDataWrapper, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
      }
      if (paramETDataRequest.getMetadata().isCharacterType()) {
        bool = DataRetrievalUtil.retrieveCharData(localISqlDataWrapper, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
      }
    }
    return bool;
  }
  
  public static ISqlConverter createConverter(SqlConverterGenerator paramSqlConverterGenerator, IColumn paramIColumn1, IColumn paramIColumn2)
    throws ErrorException
  {
    ISqlConverter localISqlConverter = paramSqlConverterGenerator.createCustomConverter(paramIColumn1, paramIColumn2);
    if (localISqlConverter != null) {
      return localISqlConverter;
    }
    if (DefaultSqlDataWrapper.isImplicitConvSupported(paramIColumn1, paramIColumn2)) {
      return null;
    }
    return paramSqlConverterGenerator.createConverter(paramIColumn1, paramIColumn2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/ConversionUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */