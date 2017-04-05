package com.simba.sqlengine.exceptions;

import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.OperationCanceledException;
import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.EnumSet;

public class SQLEngineExceptionFactory
{
  public static ErrorException featureNotImplementedException(String paramString)
  {
    ErrorException localErrorException = SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(SQLEngineMessageKey.FEATURE_NOT_SUPPORTED.name(), new String[] { paramString }, ExceptionType.FEATURE_NOT_IMPLEMENTED);
    return localErrorException;
  }
  
  public static ErrorException invalidParseTreeException()
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_PARSE_TREE.name());
    return localSQLEngineException;
  }
  
  public static ErrorException invalidAETreeException()
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_AET.name());
    return localSQLEngineException;
  }
  
  public static ErrorException invalidUpsertQueryException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_UPSERT_QUERY.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidSqlTypeForWrapperException(int paramInt)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_SQL_TYPE_FOR_WRAPPER.name(), new String[] { String.valueOf(paramInt) + " (" + TypeUtilities.sqlTypeToString((short)paramInt) + ")" });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidScalarFunctionDataException(String paramString, int paramInt)
  {
    return new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_SCALAR_FN_DATA.name(), new String[] { paramString, String.valueOf(paramInt) });
  }
  
  public static ErrorException invalidScalarFnNameException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_SCALAR_FN_NAME.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidEscapeSequenceException()
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_INVALID_ESC_SQNCE, SQLEngineMessageKey.INVALID_ESC_SQNCE.name());
    return localSQLEngineException;
  }
  
  public static ErrorException aggrFnSemanticsException(SQLEngineMessageKey paramSQLEngineMessageKey)
  {
    if (!EnumSet.of(SQLEngineMessageKey.AGGR_FN_N_SUBQUERY_IN_GROUPBY, new SQLEngineMessageKey[] { SQLEngineMessageKey.AGGR_FN_IN_ON, SQLEngineMessageKey.AGGR_FN_IN_WHERE, SQLEngineMessageKey.AGGR_FN_NOT_IN_SEL_LIST_OR_HAVING, SQLEngineMessageKey.NESTED_AGGR_FN_NOT_ALLOWED, SQLEngineMessageKey.MULTI_COL_IN_AGGR_FN }).contains(paramSQLEngineMessageKey)) {
      throw new IllegalArgumentException("Invalid message key: " + paramSQLEngineMessageKey);
    }
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramSQLEngineMessageKey.name());
    return localSQLEngineException;
  }
  
  public static ErrorException invalidScalarFnArgumentCountException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_SCALAR_FN_ARG_COUNT.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidScalarFnArgumentTypeException(String paramString, int paramInt, short paramShort)
  {
    String str = TypeUtilities.sqlTypeToString(paramShort);
    if (null == str) {
      str = "UNKNOWN (" + paramShort + ")";
    }
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_INVALID_CHAR_VAL_FOR_CAST, SQLEngineMessageKey.INCOMPATIBLE_ARG_TYPE_FOR_SCALAR_FN.name(), new String[] { paramString, String.valueOf(paramInt), str });
    return localSQLEngineException;
  }
  
  public static OperationCanceledException operationCanceledException()
  {
    OperationCanceledException localOperationCanceledException = new OperationCanceledException(7, SQLEngineMessageKey.OPERATION_CANCELED.name());
    return localOperationCanceledException;
  }
  
  public static ErrorException unsupportedTypesException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_INVALID_SQL_DATA_TYPE, SQLEngineMessageKey.DATA_TYPE_NOT_SUPPORTED.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidOperationException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_OPERATION.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException incompatibleTypesException(String paramString1, String paramString2)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INCOMPATIBLE_TYPES.name(), new String[] { paramString1, paramString2 });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidFormatException(String paramString1, String paramString2)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_INVALID_CHAR_VAL_FOR_CAST, SQLEngineMessageKey.INVALID_FORMAT_FOR_LITERAL.name(), new String[] { paramString1, paramString2 });
    return localSQLEngineException;
  }
  
  public static ErrorException numericOverflowException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.NUMERIC_OVERFLOW.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidOrderByExprException()
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_ORDER_BY_EXPR.name(), new String[0]);
    return localSQLEngineException;
  }
  
  public static ErrorException invalidSetArgTypeException(String paramString, int paramInt)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_OPERAND_TYPE_FOR_SET_FN.name(), new String[] { paramString, "" + paramInt });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidOrderByColumnException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_ORDER_BY_COLUMN.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException orderByPositionOutOfRangeException()
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.ORDER_BY_POS_NUM_OUT_OF_RANGE.name(), new String[0]);
    return localSQLEngineException;
  }
  
  public static ErrorException columnReferenceNotUniqueException(String paramString)
  {
    SQLEngineException localSQLEngineException = new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.COLUMN_REF_NOT_UNIQUE.name(), new String[] { paramString });
    return localSQLEngineException;
  }
  
  public static ErrorException invalidSecondArgumentToCastException()
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_SECOND_ARG_FOR_CAST.name());
  }
  
  public static ErrorException invalidSecondArgumentToConvertException()
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_SECOND_ARG_FOR_CONVERT.name());
  }
  
  public static ErrorException incompatibleInsertType(String paramString, short paramShort1, short paramShort2)
  {
    String str1 = TypeUtilities.sqlTypeToString(paramShort1);
    if (null == str1) {
      str1 = "UNKNOWN (" + paramShort1 + ")";
    }
    String str2 = TypeUtilities.sqlTypeToString(paramShort2);
    if (null == str2) {
      str2 = "UNKNOWN (" + paramShort2 + ")";
    }
    return new SQLEngineException(DiagState.DIAG_RESTRICTED_DATA_TYPE_ATTR_VIOLATION, SQLEngineMessageKey.INCOMPATIBLE_INSERT_TYPE.name(), new String[] { paramString, str1, str2 });
  }
  
  public static ErrorException invalidTypeParameterException(String paramString)
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_TYPE_PARAMS.name(), new String[] { paramString });
  }
  
  public static ErrorException numArithOverflowException()
  {
    return new SQLEngineException(DiagState.DIAG_NUM_VAL_OUT_OF_RANGE, SQLEngineMessageKey.ARITHMETIC_ERROR.name(), new String[] { "Overflow" });
  }
  
  public static ErrorException datetimeArithOverflowException()
  {
    return new SQLEngineException(DiagState.DIAG_DATETIME_OVERFLOW, SQLEngineMessageKey.ARITHMETIC_ERROR.name(), new String[] { "Overflow" });
  }
  
  public static ErrorException divByZeroException()
  {
    return new SQLEngineException(DiagState.DIAG_DIV_BY_ZERO, SQLEngineMessageKey.ARITHMETIC_ERROR.name(), new String[] { "Divided by zero" });
  }
  
  public static ErrorException conversionNotSupported(String paramString1, String paramString2)
  {
    return new SQLEngineException(DiagState.DIAG_RESTRICTED_DATA_TYPE_ATTR_VIOLATION, SQLEngineMessageKey.CONVERSION_NOT_SUPPORTED.name(), new String[] { paramString1, paramString2 });
  }
  
  public static ErrorException requestedDataNotSet()
  {
    return new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.REQUESTED_DATA_NOT_SET.name());
  }
  
  public static ErrorException invalidTopLimitValue(String paramString)
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_TOP_LIMIT_VALUE.name(), new String[] { paramString });
  }
  
  public static ErrorException failedToCreateFile(String paramString)
  {
    return new SQLEngineException(DiagState.DIAG_MEM_ALLOC_ERR, SQLEngineMessageKey.FILE_CREATION_ERROR.name(), new String[] { paramString });
  }
  
  public static ErrorException failedToDeleteFile(String paramString)
  {
    return new SQLEngineException(DiagState.DIAG_MEM_ALLOC_ERR, SQLEngineMessageKey.FILE_DELETION_ERROR.name(), new String[] { paramString });
  }
  
  public static ErrorException failedToCreateFile(String paramString1, String paramString2)
  {
    return new SQLEngineException(DiagState.DIAG_MEM_ALLOC_ERR, SQLEngineMessageKey.FILE_CREATION_ERROR.name(), new String[] { paramString1, paramString2 });
  }
  
  public static ErrorException failedToReadData(Exception paramException)
  {
    return new SQLEngineException(DiagState.DIAG_MEM_MGMT_ERR, SQLEngineMessageKey.READ_DATA_ERROR.name(), paramException);
  }
  
  public static ErrorException failedToReadData()
  {
    return new SQLEngineException(DiagState.DIAG_MEM_MGMT_ERR, SQLEngineMessageKey.READ_DATA_ERROR.name());
  }
  
  public static ErrorException sortOnLongData(int paramInt)
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.ATTEMPT_TO_SORT_ON_LONG_DATA.name(), new String[] { String.valueOf(paramInt) });
  }
  
  public static ErrorException joinOnLongData(int paramInt)
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.ATTEMPT_TO_JOIN_ON_LONG_DATA.name(), new String[] { String.valueOf(paramInt) });
  }
  
  public static ErrorException failedToAllocateMemory(String paramString)
  {
    return new SQLEngineException(DiagState.DIAG_MEM_ALLOC_ERR, SQLEngineMessageKey.FAILED_TO_ALLOC_MEM.name(), new String[] { paramString });
  }
  
  public static ErrorException failedToWriteData(Exception paramException)
  {
    return new SQLEngineException(DiagState.DIAG_MEM_MGMT_ERR, SQLEngineMessageKey.WRITE_DATA_ERROR.name(), paramException);
  }
  
  public static SQLEngineRuntimeException runtimeException(Exception paramException)
  {
    return new SQLEngineRuntimeException(paramException);
  }
  
  public static ErrorException convertRuntimeException(Throwable paramThrowable)
  {
    return new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.GENERAL_ERROR.name(), paramThrowable);
  }
  
  public static ErrorException aggregateOnLongData()
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.ATTEMPT_TO_AGGR_ON_LONG_DATA.name());
  }
  
  public static ErrorException invalidConfiguration()
  {
    return new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_CONFIG.name());
  }
  
  public static ErrorException invalidConfiguration(Exception paramException)
  {
    return new SQLEngineException(DiagState.DIAG_GENERAL_ERROR, SQLEngineMessageKey.INVALID_CONFIG.name(), paramException);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/exceptions/SQLEngineExceptionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */