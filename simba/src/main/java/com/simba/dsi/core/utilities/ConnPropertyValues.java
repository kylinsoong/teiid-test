package com.simba.dsi.core.utilities;

public class ConnPropertyValues
{
  public static final long DSI_PROP_CD_FALSE = 0L;
  public static final long DSI_PROP_CD_TRUE = 1L;
  public static final long DSI_PROP_AA_TRUE = 1L;
  public static final long DSI_PROP_AA_FALSE = 0L;
  public static final long DSI_PROP_AUTOCOMMIT_ON = 1L;
  public static final long DSI_PROP_AUTOCOMMIT_OFF = 0L;
  public static final long DSI_PROP_AUTO_IPD_ON = 1L;
  public static final long DSI_PROP_AUTO_IPD_OFF = 0L;
  public static final long DSI_AF_ALL = 64L;
  public static final long DSI_AF_AVG = 1L;
  public static final long DSI_AF_COUNT = 2L;
  public static final long DSI_AF_DISTINCT = 32L;
  public static final long DSI_AF_MAX = 4L;
  public static final long DSI_AF_MIN = 8L;
  public static final long DSI_AF_SUM = 16L;
  public static final long DSI_AD_ADD_DOMAIN_CONSTRAINT = 2L;
  public static final long DSI_AD_ADD_DOMAIN_DEFAULT = 8L;
  public static final long DSI_AD_CONSTRAINT_NAME_DEFINITION = 1L;
  public static final long DSI_AD_DROP_DOMAIN_CONSTRAINT = 4L;
  public static final long DSI_AD_DROP_DOMAIN_DEFAULT = 16L;
  public static final long DSI_AD_ADD_CONSTRAINT_DEFERRABLE = 128L;
  public static final long DSI_AD_ADD_CONSTRAINT_NON_DEFERRABLE = 256L;
  public static final long DSI_AD_ADD_CONSTRAINT_INITIALLY_DEFERRED = 32L;
  public static final long DSI_AD_ADD_CONSTRAINT_INITIALLY_IMMEDIATE = 64L;
  public static final long DSI_AT_ADD_COLUMN_COLLATION = 128L;
  public static final long DSI_AT_ADD_COLUMN_DEFAULT = 64L;
  public static final long DSI_AT_ADD_COLUMN_SINGLE = 32L;
  public static final long DSI_AT_ADD_CONSTRAINT = 8L;
  public static final long DSI_AT_ADD_TABLE_CONSTRAINT = 4096L;
  public static final long DSI_AT_CONSTRAINT_NAME_DEFINITION = 32768L;
  public static final long DSI_AT_DROP_COLUMN_CASCADE = 1024L;
  public static final long DSI_AT_DROP_COLUMN_DEFAULT = 512L;
  public static final long DSI_AT_DROP_COLUMN_RESTRICT = 2048L;
  public static final long DSI_AT_DROP_TABLE_CONSTRAINT_CASCADE = 8192L;
  public static final long DSI_AT_DROP_TABLE_CONSTRAINT_RESTRICT = 16384L;
  public static final long DSI_AT_SET_COLUMN_DEFAULT = 256L;
  public static final long DSI_AT_CONSTRAINT_INITIALLY_DEFERRED = 65536L;
  public static final long DSI_AT_CONSTRAINT_INITIALLY_IMMEDIATE = 131072L;
  public static final long DSI_AT_CONSTRAINT_DEFERRABLE = 262144L;
  public static final long DSI_AT_CONSTRAINT_NON_DEFERRABLE = 524288L;
  public static final long DSI_BP_CLOSE = 1L;
  public static final long DSI_BP_DELETE = 2L;
  public static final long DSI_BP_DROP = 4L;
  public static final long DSI_BP_SCROLL = 64L;
  public static final long DSI_BP_TRANSACTION = 8L;
  public static final long DSI_BP_UPDATE = 16L;
  public static final long DSI_BP_OTHER_HSTMT = 32L;
  public static final long DSI_CL_START = 1L;
  public static final long DSI_CL_END = 2L;
  public static final long DSI_CU_DML_STATEMENTS = 1L;
  public static final long DSI_CU_PROCEDURE_INVOCATION = 2L;
  public static final long DSI_CU_TABLE_DEFINITION = 4L;
  public static final long DSI_CU_INDEX_DEFINITION = 8L;
  public static final long DSI_CU_PRIVILEGE_DEFINITION = 16L;
  public static final char DSI_CURSOR_STATIC = '\002';
  public static final char DSI_CURSOR_DYNAMIC = '\004';
  public static final char DSI_CURSOR_KEYSET = '\b';
  public static final char DSI_CURSOR_MIXED = '\020';
  public static final long DSI_CB_NULL = 0L;
  public static final long DSI_CB_NON_NULL = 1L;
  public static final long DSI_PROP_MODE_READ_ONLY = 1L;
  public static final long DSI_PROP_MODE_READ_WRITE = 0L;
  public static final long DSI_CVT_BIGINT = 16384L;
  public static final long DSI_CVT_BINARY = 1024L;
  public static final long DSI_CVT_BIT = 4096L;
  public static final long DSI_CVT_CHAR = 1L;
  public static final long DSI_CVT_GUID = 16777216L;
  public static final long DSI_CVT_DATE = 32768L;
  public static final long DSI_CVT_DECIMAL = 4L;
  public static final long DSI_CVT_DOUBLE = 128L;
  public static final long DSI_CVT_FLOAT = 32L;
  public static final long DSI_CVT_INTEGER = 8L;
  public static final long DSI_CVT_INTERVAL_YEAR_MONTH = 524288L;
  public static final long DSI_CVT_INTERVAL_DAY_TIME = 1048576L;
  public static final long DSI_CVT_LONGVARBINARY = 262144L;
  public static final long DSI_CVT_LONGVARCHAR = 512L;
  public static final long DSI_CVT_NUMERIC = 2L;
  public static final long DSI_CVT_REAL = 64L;
  public static final long DSI_CVT_SMALLINT = 16L;
  public static final long DSI_CVT_TIME = 65536L;
  public static final long DSI_CVT_TIMESTAMP = 131072L;
  public static final long DSI_CVT_TINYINT = 8192L;
  public static final long DSI_CVT_VARBINARY = 2048L;
  public static final long DSI_CVT_VARCHAR = 256L;
  public static final long DSI_CVT_WCHAR = 2097152L;
  public static final long DSI_CVT_WLONGVARCHAR = 4194304L;
  public static final long DSI_CVT_WVARCHAR = 8388608L;
  public static final long DSI_FN_CVT_CONVERT = 1L;
  public static final long DSI_FN_CVT_CAST = 2L;
  public static final long DSI_CN_ANY = 2L;
  public static final long DSI_CN_NONE = 0L;
  public static final long DSI_CN_DIFFERENT = 1L;
  public static final long DSI_CA_CREATE_ASSERTION = 1L;
  public static final long DSI_CA_CONSTRAINT_INITIALLY_DEFERRED = 16L;
  public static final long DSI_CA_CONSTRAINT_INITIALLY_IMMEDIATE = 32L;
  public static final long DSI_CA_CONSTRAINT_DEFERRABLE = 64L;
  public static final long DSI_CA_CONSTRAINT_NON_DEFERRABLE = 128L;
  public static final long DSI_CCS_CREATE_CHARACTER_SET = 1L;
  public static final long DSI_CCS_COLLATE_CLAUSE = 2L;
  public static final long DSI_CCS_LIMITED_COLLATION = 4L;
  public static final long DSI_CCOL_CREATE_COLLATION = 1L;
  public static final long DSI_CDO_CREATE_DOMAIN = 1L;
  public static final long DSI_CDO_CONSTRAINT_NAME_DEFINITION = 16L;
  public static final long DSI_CDO_DEFAULT = 2L;
  public static final long DSI_CDO_CONSTRAINT = 4L;
  public static final long DSI_CDO_COLLATION = 8L;
  public static final long DSI_CDO_CONSTRAINT_INITIALLY_DEFERRED = 32L;
  public static final long DSI_CDO_CONSTRAINT_INITIALLY_IMMEDIATE = 64L;
  public static final long DSI_CDO_CONSTRAINT_DEFERRABLE = 128L;
  public static final long DSI_CDO_CONSTRAINT_NON_DEFERRABLE = 256L;
  public static final long DSI_CS_CREATE_SCHEMA = 1L;
  public static final long DSI_CS_AUTHORIZATION = 2L;
  public static final long DSI_CS_DEFAULT_CHARACTER_SET = 4L;
  public static final long DSI_CT_CREATE_TABLE = 1L;
  public static final long DSI_CT_TABLE_CONSTRAINT = 4096L;
  public static final long DSI_CT_CONSTRAINT_NAME_DEFINITION = 8192L;
  public static final long DSI_CT_COMMIT_PRESERVE = 2L;
  public static final long DSI_CT_COMMIT_DELETE = 4L;
  public static final long DSI_CT_GLOBAL_TEMPORARY = 8L;
  public static final long DSI_CT_LOCAL_TEMPORARY = 16L;
  public static final long DSI_CT_COLUMN_CONSTRAINT = 512L;
  public static final long DSI_CT_COLUMN_DEFAULT = 1024L;
  public static final long DSI_CT_COLUMN_COLLATION = 2048L;
  public static final long DSI_CT_CONSTRAINT_INITIALLY_DEFERRED = 32L;
  public static final long DSI_CT_CONSTRAINT_INITIALLY_IMMEDIATE = 64L;
  public static final long DSI_CT_CONSTRAINT_DEFERRABLE = 64L;
  public static final long DSI_CT_CONSTRAINT_NON_DEFERRABLE = 256L;
  public static final long DSI_CTR_CREATE_TRANSLATION = 1L;
  public static final long DSI_CV_CREATE_VIEW = 1L;
  public static final long DSI_CV_CHECK_OPTION = 2L;
  public static final long DSI_CV_CASCADED = 4L;
  public static final long DSI_CV_LOCAL = 8L;
  public static final long DSI_CB_DELETE = 0L;
  public static final long DSI_CB_CLOSE = 1L;
  public static final long DSI_CB_PRESERVE = 2L;
  public static final long DSI_INSENSITIVE = 1L;
  public static final long DSI_UNSPECIFIED = 0L;
  public static final long DSI_SENSITIVE = 2L;
  public static final long SQL_DL_SQL92_DATE = 1L;
  public static final long SQL_DL_SQL92_TIME = 2L;
  public static final long SQL_DL_SQL92_TIMESTAMP = 4L;
  public static final long SQL_DL_SQL92_INTERVAL_YEAR = 8L;
  public static final long SQL_DL_SQL92_INTERVAL_MONTH = 16L;
  public static final long SQL_DL_SQL92_INTERVAL_DAY = 32L;
  public static final long SQL_DL_SQL92_INTERVAL_HOUR = 64L;
  public static final long SQL_DL_SQL92_INTERVAL_MINUTE = 128L;
  public static final long SQL_DL_SQL92_INTERVAL_SECOND = 256L;
  public static final long SQL_DL_SQL92_INTERVAL_YEAR_TO_MONTH = 512L;
  public static final long SQL_DL_SQL92_INTERVAL_DAY_TO_HOUR = 1024L;
  public static final long SQL_DL_SQL92_INTERVAL_DAY_TO_MINUTE = 2048L;
  public static final long SQL_DL_SQL92_INTERVAL_DAY_TO_SECOND = 4096L;
  public static final long SQL_DL_SQL92_INTERVAL_HOUR_TO_MINUTE = 8192L;
  public static final long SQL_DL_SQL92_INTERVAL_HOUR_TO_SECOND = 16384L;
  public static final long SQL_DL_SQL92_INTERVAL_MINUTE_TO_SECOND = 32768L;
  public static final long DSI_DI_CREATE_INDEX = 1L;
  public static final long DSI_DI_DROP_INDEX = 2L;
  public static final long DSI_TXN_READ_UNCOMMITTED = 1L;
  public static final long DSI_TXN_READ_COMMITTED = 2L;
  public static final long DSI_TXN_REPEATABLE_READ = 4L;
  public static final long DSI_TXN_SERIALIZABLE = 8L;
  public static final long DSI_DA_DROP_ASSERTION = 1L;
  public static final long DSI_DCS_DROP_CHARACTER_SET = 1L;
  public static final long DSI_DC_DROP_COLLATION = 1L;
  public static final long DSI_DD_DROP_DOMAIN = 1L;
  public static final long DSI_DD_CASCADE = 4L;
  public static final long DSI_DD_RESTRICT = 2L;
  public static final long DSI_DS_DROP_SCHEMA = 1L;
  public static final long DSI_DS_CASCADE = 4L;
  public static final long DSI_DS_RESTRICT = 2L;
  public static final long DSI_DT_DROP_TABLE = 1L;
  public static final long DSI_DT_CASCADE = 4L;
  public static final long DSI_DT_RESTRICT = 2L;
  public static final long DSI_DTR_DROP_TRANSLATION = 1L;
  public static final long DSI_DV_DROP_VIEW = 1L;
  public static final long DSI_DV_CASCADE = 4L;
  public static final long DSI_DV_RESTRICT = 2L;
  public static final long DSI_GB_COLLATE = 4L;
  public static final long DSI_GB_NOT_SUPPORTED = 0L;
  public static final long DSI_GB_GROUP_BY_EQUALS_SELECT = 1L;
  public static final long DSI_GB_GROUP_BY_CONTAINS_SELECT = 2L;
  public static final long DSI_GB_NO_RELATION = 3L;
  public static final char DSI_IC_UPPER = '\001';
  public static final char DSI_IC_LOWER = '\002';
  public static final char DSI_IC_SENSITIVE = '\003';
  public static final char DSI_IC_MIXED = '\004';
  public static final long DSI_IK_NONE = 0L;
  public static final long DSI_IK_ASC = 1L;
  public static final long DSI_IK_DESC = 2L;
  public static final long DSI_IK_ALL = 3L;
  public static final long DSI_IS_INSERT_LITERALS = 1L;
  public static final long DSI_IS_INSERT_SEARCHED = 2L;
  public static final long DSI_IS_SELECT_INTO = 4L;
  public static final long DSI_NNC_NULL = 0L;
  public static final long DSI_NNC_NON_NULL = 1L;
  public static final long DSI_NC_END = 4L;
  public static final long DSI_NC_HIGH = 0L;
  public static final long DSI_NC_LOW = 1L;
  public static final long DSI_NC_START = 2L;
  public static final long DSI_FN_NUM_ABS = 1L;
  public static final long DSI_FN_NUM_ACOS = 2L;
  public static final long DSI_FN_NUM_ASIN = 4L;
  public static final long DSI_FN_NUM_ATAN = 8L;
  public static final long DSI_FN_NUM_ATAN2 = 16L;
  public static final long DSI_FN_NUM_CEILING = 32L;
  public static final long DSI_FN_NUM_COS = 64L;
  public static final long DSI_FN_NUM_COT = 128L;
  public static final long DSI_FN_NUM_DEGREES = 262144L;
  public static final long DSI_FN_NUM_EXP = 256L;
  public static final long DSI_FN_NUM_FLOOR = 512L;
  public static final long DSI_FN_NUM_LOG = 1024L;
  public static final long DSI_FN_NUM_LOG10 = 524288L;
  public static final long DSI_FN_NUM_MOD = 2048L;
  public static final long DSI_FN_NUM_PI = 65536L;
  public static final long DSI_FN_NUM_POWER = 1048576L;
  public static final long DSI_FN_NUM_RADIANS = 2097152L;
  public static final long DSI_FN_NUM_RAND = 131072L;
  public static final long DSI_FN_NUM_ROUND = 4194304L;
  public static final long DSI_FN_NUM_SIGN = 4096L;
  public static final long DSI_FN_NUM_SIN = 8192L;
  public static final long DSI_FN_NUM_SQRT = 16384L;
  public static final long DSI_FN_NUM_TAN = 32768L;
  public static final long DSI_FN_NUM_TRUNCATE = 8388608L;
  public static final long DSI_OJ_LEFT = 1L;
  public static final long DSI_OJ_RIGHT = 2L;
  public static final long DSI_OJ_FULL = 4L;
  public static final long DSI_OJ_NESTED = 8L;
  public static final long DSI_OJ_NOT_ORDERED = 16L;
  public static final long DSI_OJ_INNER = 32L;
  public static final long DSI_OJ_ALL_COMPARISON_OPS = 64L;
  public static final long DSI_SU_DML_STATEMENTS = 1L;
  public static final long DSI_SU_PROCEDURE_INVOCATION = 2L;
  public static final long DSI_SU_TABLE_DEFINITION = 4L;
  public static final long DSI_SU_INDEX_DEFINITION = 8L;
  public static final long DSI_SU_PRIVILEGE_DEFINITION = 16L;
  public static final long DSI_SC_SQL92_ENTRY = 1L;
  public static final long DSI_SC_FIPS127_2_TRANSITIONAL = 2L;
  public static final long DSI_SC_SQL92_FULL = 8L;
  public static final long DSI_SC_SQL92_INTERMEDIATE = 4L;
  public static final long DSI_FN_STR_ASCII = 8192L;
  public static final long DSI_FN_STR_BIT_LENGTH = 524288L;
  public static final long DSI_FN_STR_CHAR = 16384L;
  public static final long DSI_FN_STR_CHAR_LENGTH = 1048576L;
  public static final long DSI_FN_STR_CHARACTER_LENGTH = 2097152L;
  public static final long DSI_FN_STR_CONCAT = 1L;
  public static final long DSI_FN_STR_DIFFERENCE = 32768L;
  public static final long DSI_FN_STR_INSERT = 2L;
  public static final long DSI_FN_STR_LCASE = 64L;
  public static final long DSI_FN_STR_LEFT = 4L;
  public static final long DSI_FN_STR_LENGTH = 16L;
  public static final long DSI_FN_STR_LOCATE = 32L;
  public static final long DSI_FN_STR_LOCATE_2 = 65536L;
  public static final long DSI_FN_STR_LTRIM = 8L;
  public static final long DSI_FN_STR_OCTET_LENGTH = 4194304L;
  public static final long DSI_FN_STR_POSITION = 8388608L;
  public static final long DSI_FN_STR_REPEAT = 128L;
  public static final long DSI_FN_STR_REPLACE = 256L;
  public static final long DSI_FN_STR_RIGHT = 512L;
  public static final long DSI_FN_STR_RTRIM = 1024L;
  public static final long DSI_FN_STR_SOUNDEX = 131072L;
  public static final long DSI_FN_STR_SPACE = 262144L;
  public static final long DSI_FN_STR_SUBSTRING = 2048L;
  public static final long DSI_FN_STR_UCASE = 4096L;
  public static final long DSI_SQ_CORRELATED_SUBQUERIES = 16L;
  public static final long DSI_SQ_COMPARISON = 1L;
  public static final long DSI_SQ_EXISTS = 2L;
  public static final long DSI_SQ_IN = 4L;
  public static final long DSI_SQ_QUANTIFIED = 8L;
  public static final long DSI_FN_SYS_DBNAME = 2L;
  public static final long DSI_FN_SYS_IFNULL = 4L;
  public static final long DSI_FN_SYS_USERNAME = 1L;
  public static final long DSI_FN_TSI_FRAC_SECOND = 1L;
  public static final long DSI_FN_TSI_SECOND = 2L;
  public static final long DSI_FN_TSI_MINUTE = 4L;
  public static final long DSI_FN_TSI_HOUR = 8L;
  public static final long DSI_FN_TSI_DAY = 16L;
  public static final long DSI_FN_TSI_WEEK = 32L;
  public static final long DSI_FN_TSI_MONTH = 64L;
  public static final long DSI_FN_TSI_QUARTER = 128L;
  public static final long DSI_FN_TSI_YEAR = 256L;
  public static final long DSI_FN_TD_CURRENT_DATE = 131072L;
  public static final long DSI_FN_TD_CURRENT_TIME = 262144L;
  public static final long DSI_FN_TD_CURRENT_TIMESTAMP = 524288L;
  public static final long DSI_FN_TD_CURDATE = 2L;
  public static final long DSI_FN_TD_CURTIME = 512L;
  public static final long DSI_FN_TD_DAYNAME = 32768L;
  public static final long DSI_FN_TD_DAYOFMONTH = 4L;
  public static final long DSI_FN_TD_DAYOFWEEK = 8L;
  public static final long DSI_FN_TD_DAYOFYEAR = 16L;
  public static final long DSI_FN_TD_EXTRACT = 1048576L;
  public static final long DSI_FN_TD_HOUR = 1024L;
  public static final long DSI_FN_TD_MINUTE = 2048L;
  public static final long DSI_FN_TD_MONTH = 32L;
  public static final long DSI_FN_TD_MONTHNAME = 65536L;
  public static final long DSI_FN_TD_NOW = 1L;
  public static final long DSI_FN_TD_QUARTER = 64L;
  public static final long DSI_FN_TD_SECOND = 4096L;
  public static final long DSI_FN_TD_TIMESTAMPADD = 8192L;
  public static final long DSI_FN_TD_TIMESTAMPDIFF = 16384L;
  public static final long DSI_FN_TD_WEEK = 128L;
  public static final long DSI_FN_TD_YEAR = 256L;
  public static final char DSI_TC_NONE = '\000';
  public static final char DSI_TC_DML = '\001';
  public static final char DSI_TC_DDL_COMMIT = '\003';
  public static final char DSI_TC_DDL_IGNORE = '\004';
  public static final char DSI_TC_ALL = '\002';
  public static final long DSI_U_UNION = 1L;
  public static final long DSI_U_UNION_ALL = 2L;
  public static final short DSI_OSC_MINIMUM = 0;
  public static final short DSI_OSC_CORE = 1;
  public static final short DSI_OSC_EXTENDED = 2;
  public static final long DSI_PARC_BATCH = 1L;
  public static final long DSI_PARC_NO_BATCH = 2L;
  public static final long DSI_BRC_EXPLICIT = 2L;
  public static final long DSI_BRC_PROCEDURES = 1L;
  public static final long DSI_BRC_ROLLED_UP = 4L;
  public static final long DSI_BS_ROW_COUNT_EXPLICIT = 2L;
  public static final long DSI_BS_ROW_COUNT_PROC = 8L;
  public static final long DSI_BS_SELECT_EXPLICIT = 1L;
  public static final long DSI_BS_SELECT_PROC = 4L;
  public static final long DSI_CA1_NEXT = 1L;
  public static final long DSI_CA1_ABSOLUTE = 2L;
  public static final long DSI_CA1_RELATIVE = 4L;
  public static final long DSI_CA1_BOOKMARK = 8L;
  public static final long DSI_CA1_EXCLUSIVE = 128L;
  public static final long DSI_CA1_NO_CHANGE = 64L;
  public static final long DSI_CA1_UNLOCK = 256L;
  public static final long DSI_CA1_POSITION = 512L;
  public static final long DSI_CA1_UPDATE = 1024L;
  public static final long DSI_CA1_DELETE = 2048L;
  public static final long DSI_CA1_REFRESH = 4096L;
  public static final long DSI_CA1_POSITIONED_UPDATE = 8192L;
  public static final long DSI_CA1_POSITIONED_DELETE = 16384L;
  public static final long DSI_CA1_SELECT_FOR_UPDATE = 32768L;
  public static final long DSI_CA1_BULK_ADD = 65536L;
  public static final long DSI_CA1_BULK_UPDATE_BY_BOOKMARK = 131072L;
  public static final long DSI_CA1_BULK_DELETE_BY_BOOKMARK = 262144L;
  public static final long DSI_CA1_BULK_FETCH_BY_BOOKMARK = 524288L;
  public static final long DSI_CA2_READ_ONLY_CONCURRENCY = 1L;
  public static final long DSI_CA2_LOCK_CONCURRENCY = 2L;
  public static final long DSI_CA2_OPT_ROWVER_CONCURRENCY = 4L;
  public static final long DSI_CA2_OPT_VALUES_CONCURRENCY = 8L;
  public static final long DSI_CA2_SENSITIVITY_ADDITIONS = 16L;
  public static final long DSI_CA2_SENSITIVITY_DELETIONS = 32L;
  public static final long DSI_CA2_SENSITIVITY_UPDATES = 64L;
  public static final long DSI_CA2_MAX_ROWS_SELECT = 128L;
  public static final long DSI_CA2_MAX_ROWS_INSERT = 256L;
  public static final long DSI_CA2_MAX_ROWS_DELETE = 512L;
  public static final long DSI_CA2_MAX_ROWS_UPDATE = 1024L;
  public static final long DSI_CA2_MAX_ROWS_CATALOG = 2048L;
  public static final long DSI_CA2_MAX_ROWS_AFFECTS_ALL = 3968L;
  public static final long DSI_CA2_CRC_EXACT = 4096L;
  public static final long DSI_CA2_CRC_APPROXIMATE = 8192L;
  public static final long DSI_CA2_SIMULATE_NON_UNIQUE = 16384L;
  public static final long DSI_CA2_SIMULATE_TRY_UNIQUE = 32768L;
  public static final long DSI_CA2_SIMULATE_UNIQUE = 65536L;
  public static final long DSI_ISV_ASSERTIONS = 1L;
  public static final long DSI_ISV_CHARACTER_SETS = 2L;
  public static final long DSI_ISV_CHECK_CONSTRAINTS = 4L;
  public static final long DSI_ISV_COLLATIONS = 8L;
  public static final long DSI_ISV_COLUMN_DOMAIN_USE = 16L;
  public static final long DSI_ISV_COLUMN_PRIVILEGES = 32L;
  public static final long DSI_ISV_COLUMNS = 64L;
  public static final long DSI_ISV_CONSTRAINT_COLUMN_USAGE = 128L;
  public static final long DSI_ISV_CONSTRAINT_TABLE_USAGE = 256L;
  public static final long DSI_ISV_DOMAIN_CONSTRAINTS = 512L;
  public static final long DSI_ISV_DOMAINS = 1024L;
  public static final long DSI_ISV_KEY_COLUMN_USAGE = 2048L;
  public static final long DSI_ISV_REFERENTIAL_CONSTRAINTS = 4096L;
  public static final long DSI_ISV_SCHEMATA = 8192L;
  public static final long DSI_ISV_SQL_LANGUAGES = 16384L;
  public static final long DSI_ISV_TABLE_CONSTRAINTS = 32768L;
  public static final long DSI_ISV_TABLE_PRIVILEGES = 65536L;
  public static final long DSI_ISV_TABLES = 131072L;
  public static final long DSI_ISV_TRANSLATIONS = 262144L;
  public static final long DSI_ISV_USAGE_PRIVILEGES = 524288L;
  public static final long DSI_ISV_VIEW_COLUMN_USAGE = 1048576L;
  public static final long DSI_ISV_VIEW_TABLE_USAGE = 2097152L;
  public static final long DSI_ISV_VIEWS = 4194304L;
  public static final long DSI_PAS_BATCH = 1L;
  public static final long DSI_PAS_NO_BATCH = 2L;
  public static final long DSI_PAS_NO_SELECT = 3L;
  public static final long DSI_SDF_CURRENT_DATE = 1L;
  public static final long DSI_SDF_CURRENT_TIME = 2L;
  public static final long DSI_SDF_CURRENT_TIMESTAMP = 4L;
  public static final long DSI_SFKD_CASCADE = 1L;
  public static final long DSI_SFKD_NO_ACTION = 2L;
  public static final long DSI_SFKD_SET_DEFAULT = 4L;
  public static final long DSI_SFKD_SET_NULL = 8L;
  public static final long DSI_SFKU_CASCADE = 1L;
  public static final long DSI_SFKU_NO_ACTION = 2L;
  public static final long DSI_SFKU_SET_DEFAULT = 4L;
  public static final long DSI_SFKU_SET_NULL = 8L;
  public static final long DSI_SNVF_BIT_LENGTH = 1L;
  public static final long DSI_SNVF_CHAR_LENGTH = 2L;
  public static final long DSI_SNVF_CHARACTER_LENGTH = 4L;
  public static final long DSI_SNVF_EXTRACT = 8L;
  public static final long DSI_SNVF_OCTET_LENGTH = 16L;
  public static final long DSI_SNVF_POSITION = 32L;
  public static final long DSI_SP_BETWEEN = 2048L;
  public static final long DSI_SP_COMPARISON = 4096L;
  public static final long DSI_SP_EXISTS = 1L;
  public static final long DSI_SP_IN = 1024L;
  public static final long DSI_SP_ISNOTNULL = 2L;
  public static final long DSI_SP_ISNULL = 4L;
  public static final long DSI_SP_LIKE = 512L;
  public static final long DSI_SP_MATCH_FULL = 8L;
  public static final long DSI_SP_MATCH_PARTIAL = 16L;
  public static final long DSI_SP_MATCH_UNIQUE_FULL = 32L;
  public static final long DSI_SP_MATCH_UNIQUE_PARTIAL = 64L;
  public static final long DSI_SP_OVERLAPS = 128L;
  public static final long DSI_SP_QUANTIFIED_COMPARISON = 8192L;
  public static final long DSI_SP_UNIQUE = 256L;
  public static final long DSI_SG_DELETE_TABLE = 32L;
  public static final long DSI_SG_INSERT_COLUMN = 128L;
  public static final long DSI_SG_INSERT_TABLE = 64L;
  public static final long DSI_SG_REFERENCES_TABLE = 256L;
  public static final long DSI_SG_REFERENCES_COLUMN = 512L;
  public static final long DSI_SG_SELECT_TABLE = 1024L;
  public static final long DSI_SG_UPDATE_COLUMN = 4096L;
  public static final long DSI_SG_UPDATE_TABLE = 2048L;
  public static final long DSI_SG_USAGE_ON_DOMAIN = 1L;
  public static final long DSI_SG_USAGE_ON_CHARACTER_SET = 2L;
  public static final long DSI_SG_USAGE_ON_COLLATION = 4L;
  public static final long DSI_SG_USAGE_ON_TRANSLATION = 8L;
  public static final long DSI_SG_WITH_GRANT_OPTION = 16L;
  public static final long DSI_SRJO_CORRESPONDING_CLAUSE = 1L;
  public static final long DSI_SRJO_CROSS_JOIN = 2L;
  public static final long DSI_SRJO_EXCEPT_JOIN = 4L;
  public static final long DSI_SRJO_FULL_OUTER_JOIN = 8L;
  public static final long DSI_SRJO_INNER_JOIN = 16L;
  public static final long DSI_SRJO_INTERSECT_JOIN = 32L;
  public static final long DSI_SRJO_LEFT_OUTER_JOIN = 64L;
  public static final long DSI_SRJO_NATURAL_JOIN = 128L;
  public static final long DSI_SRJO_RIGHT_OUTER_JOIN = 256L;
  public static final long DSI_SRJO_UNION_JOIN = 256L;
  public static final long DSI_SR_CASCADE = 32L;
  public static final long DSI_SR_DELETE_TABLE = 128L;
  public static final long DSI_SR_GRANT_OPTION_FOR = 16L;
  public static final long DSI_SR_INSERT_COLUMN = 512L;
  public static final long DSI_SR_INSERT_TABLE = 256L;
  public static final long DSI_SR_REFERENCES_COLUMN = 2048L;
  public static final long DSI_SR_REFERENCES_TABLE = 1024L;
  public static final long DSI_SR_RESTRICT = 64L;
  public static final long DSI_SR_SELECT_TABLE = 4096L;
  public static final long DSI_SR_UPDATE_COLUMN = 16384L;
  public static final long DSI_SR_UPDATE_TABLE = 8192L;
  public static final long DSI_SR_USAGE_ON_DOMAIN = 1L;
  public static final long DSI_SR_USAGE_ON_CHARACTER_SET = 2L;
  public static final long DSI_SR_USAGE_ON_COLLATION = 4L;
  public static final long DSI_SR_USAGE_ON_TRANSLATION = 8L;
  public static final long DSI_SRVC_VALUE_EXPRESSION = 1L;
  public static final long DSI_SRVC_NULL = 2L;
  public static final long DSI_SRVC_DEFAULT = 4L;
  public static final long DSI_SRVC_ROW_SUBQUERY = 8L;
  public static final long DSI_SSF_CONVERT = 1L;
  public static final long DSI_SSF_LOWER = 2L;
  public static final long DSI_SSF_UPPER = 4L;
  public static final long DSI_SSF_SUBSTRING = 8L;
  public static final long DSI_SSF_TRANSLATE = 16L;
  public static final long DSI_SSF_TRIM_BOTH = 32L;
  public static final long DSI_SSF_TRIM_LEADING = 64L;
  public static final long DSI_SSF_TRIM_TRAILING = 128L;
  public static final long DSI_SVE_CASE = 1L;
  public static final long DSI_SVE_CAST = 2L;
  public static final long DSI_SVE_COALESCE = 4L;
  public static final long DSI_SVE_NULLIF = 8L;
  public static final short DSI_SUPPORTS_SAVEPOINTS_FALSE = 0;
  public static final short DSI_SUPPORTS_SAVEPOINTS_TRUE = 1;
  public static final long DSI_SUPPORTS_URS_NONE = 0L;
  public static final long DSI_SUPPORTS_URS_DELETE = 1L;
  public static final long DSI_SUPPORTS_URS_INSERT = 2L;
  public static final long DSI_SUPPORTS_URS_UPDATE = 4L;
  public static final long DSI_RS_CD_NONE = 0L;
  public static final long DSI_RS_CD_DELETE_FORWARD = 1L;
  public static final long DSI_RS_CD_INSERT_FORWARD = 2L;
  public static final long DSI_RS_CD_UPDATE_FORWARD = 4L;
  public static final long DSI_RS_CD_DELETE_INSENSITIVE = 8L;
  public static final long DSI_RS_CD_INSERT_INSENSITIVE = 16L;
  public static final long DSI_RS_CD_UPDATE_INSENSITIVE = 32L;
  public static final long DSI_RS_CD_DELETE_SENSITIVE = 64L;
  public static final long DSI_RS_CD_INSERT_SENSITIVE = 128L;
  public static final long DSI_RS_CD_UPDATE_SENSITIVE = 256L;
  public static final short DSI_SUPPORTS_STORED_FUNCTIONS_USING_CALL_SYNTAX_FALSE = 0;
  public static final short DSI_SUPPORTS_STORED_FUNCTIONS_USING_CALL_SYNTAX_TRUE = 1;
  public static final int DSI_DEFAULT_STREAM_BUFFER_SIZE = 32000;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/ConnPropertyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */