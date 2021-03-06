package com.simba.support.exceptions;

public enum DiagState
{
  DIAG_WRONG_NUM_PARAMS("07001"),  DIAG_WRONG_COUNT_FIELD("07002"),  DIAG_PREPD_STMT_NOT_CURSOR_SPEC("07005"),  DIAG_RESTRICTED_DATA_TYPE_ATTR_VIOLATION("07006"),  DIAG_INVALID_DSCPTR_INDEX("07009"),  DIAG_INVALID_DFLT_PARAM("07S01"),  DIAG_CLIENT_CANT_CONNECT("08001"),  DIAG_CONN_NAME_IN_USE("08002"),  DIAG_CONN_DOES_NOT_EXIST("08003"),  DIAG_SERVER_REJECTED_CONN("08004"),  DIAG_CONN_FAILURE_IN_TRANSACTION("08007"),  DIAG_COMM_LINK_FAILURE("08S01"),  DIAG_CARDINALITY_VIOLATION_MORE_THAN_ONE_ROW("21000"),  DIAG_INSERT_VAL_LIST_COL_LIST_MISMATCH("21S01"),  DIAG_DEGREE_DERIVED_TABLE_COL_LIST_MISMATCH("21S02"),  DIAG_STR_RIGHT_TRUNC_ERR("22001"),  DIAG_INDICATOR_VAR_NOT_SUPPLIED("22002"),  DIAG_NUM_VAL_OUT_OF_RANGE("22003"),  DIAG_INVALID_DATETIME_FMAT("22007"),  DIAG_DATETIME_OVERFLOW("22008"),  DIAG_DIV_BY_ZERO("22012"),  DIAG_INTERVAL_OVERFLOW("22015"),  DIAG_INVALID_CHAR_VAL_FOR_CAST("22018"),  DIAG_INVALID_ESC_CHAR("22019"),  DIAG_INVALID_ESC_SQNCE("22025"),  DIAG_STR_LEN_MISMATCH("22026"),  DIAG_INTEG_CONSTRAINT_VIOLATION("23000"),  DIAG_INVALID_CURSOR_STATE("24000"),  DIAG_INVALID_TRANSACTION_STATE("25000"),  DIAG_TRANSACTION_STATE_UNKNOWN("25S01"),  DIAG_TRANSACTION_STILL_ACTIVE("25S02"),  DIAG_TRANSACTION_ROLLED_BACK("25S03"),  DIAG_INVALID_AUTH_SPEC("28000"),  DIAG_INVALID_CURSOR_NAME("34000"),  DIAG_DUPLICATE_CURSOR_NAME("3C000"),  DIAG_INVALID_CATALOG_NAME("3D000"),  DIAG_INVALID_SCHEMA_NAME("3F000"),  DIAG_TRANSACTION_SERIALIZATION_FAILURE("40001"),  DIAG_TRANSACTION_INTEG_CONSTRAINT_VIOLATION("40002"),  DIAG_STMT_COMPLETION_UNKNOWN("40003"),  DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION("42000"),  DIAG_BASE_TABLE_OR_VIEW_EXISTS("42S01"),  DIAG_BASE_TABLE_OR_VIEW_MISSING("42S02"),  DIAG_INDEX_EXISTS("42S11"),  DIAG_INDEX_MISSING("42S12"),  DIAG_COLUMN_EXISTS("42S21"),  DIAG_COLUMN_MISSING("42S22"),  DIAG_WITH_CHECK_OPTION_VIOLATION("44000"),  DIAG_GENERAL_ERROR("HY000"),  DIAG_MEM_ALLOC_ERR("HY001"),  DIAG_INVALID_APP_BUFFER_TYPE("HY003"),  DIAG_INVALID_SQL_DATA_TYPE("HY004"),  DIAG_STMT_NOT_PREPARED("HY007"),  DIAG_OPER_CANCELED("HY008"),  DIAG_INVALID_NULL_PTR("HY009"),  DIAG_FUNC_SQNCE_ERR("HY010"),  DIAG_ATTR_CANT_BE_SET("HY011"),  DIAG_INVALID_TRANSACTION_OPCODE("HY012"),  DIAG_MEM_MGMT_ERR("HY013"),  DIAG_TOO_MANY_HANDLES("HY014"),  DIAG_NO_CURSOR_NAME("HY015"),  DIAG_CANT_MODIFY_ROW_DSCPTR("HY016"),  DIAG_INVALID_USE_AUTO_DSCPTR_HANDLE("HY017"),  DIAG_SERVER_DECLINED_CANCEL("HY018"),  DIAG_NON_CHAR_NON_BINARY_SENT_IN_PIECES("HY019"),  DIAG_NULL_CONCAT("HY020"),  DIAG_INCONSISTENT_DSCPTR_INFO("HY021"),  DIAG_INVALID_ATTR_VAL("HY024"),  DIAG_INVALID_STR_OR_BUFFER_LENGTH("HY090"),  DIAG_INVALID_DSCPTR_FIELD_IDENT("HY091"),  DIAG_INVALID_ATTR_OPT_IDENT("HY092"),  DIAG_FUNC_TYPE_OUT_OF_RANGE("HY095"),  DIAG_INVALID_INFO_TYPE("HY096"),  DIAG_COL_TYPE_OUT_OF_RANGE("HY097"),  DIAG_SCOPE_TYPE_OUT_OF_RANGE("HY098"),  DIAG_NULLABLE_TYPE_OUT_OF_RANGE("HY099"),  DIAG_UNIQUENESS_OPT_OUT_OF_RANGE("HY100"),  DIAG_ACCURACY_OPT_OUT_OF_RANGE("HY101"),  DIAG_INVALID_RETRIEVAL_CODE("HY103"),  DIAG_INVALID_PRECISION_OR_SCALE("HY104"),  DIAG_INVALID_PARAM_TYPE("HY105"),  DIAG_FETCH_TYPE_OUT_OF_RANGE("HY106"),  DIAG_ROW_VAL_OUT_OF_RANGE("HY107"),  DIAG_INVALID_CURSOR_POS("HY109"),  DIAG_INVALID_DRVR_COMPLETION("HY110"),  DIAG_INVALID_BOOKMARK_VALUE("HY111"),  DIAG_OPTL_FEAT_NOT_IMPLD("HYC00"),  DIAG_TIMEOUT_EXPIRED("HYT00"),  DIAG_CONN_TIMEOUT_EXPIRED("HYT01"),  DIAG_FRACTIONAL_TRUNC("01S07");
  
  public static final int NO_ROW_NUMBER = -1;
  public static final int NO_COLUMN_NUMBER = -1;
  public static final int ROW_NUMBER_UNKNOWN = -2;
  public static final int COLUMN_NUMBER_UNKNOWN = -2;
  private final String m_sqlState;
  
  private DiagState(String paramString)
  {
    this.m_sqlState = paramString;
  }
  
  public String getSqlState()
  {
    return this.m_sqlState;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/DiagState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */