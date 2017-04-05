package com.simba.dsi.dataengine.utilities;

public class MetadataConstants
{
  public static final short INITIALLY_DEFFERRED = 5;
  public static final short INITIALLY_IMMEDIATE = 6;
  public static final short NOT_DEFFERRABLE = 7;
  public static final short NULLABLE_FALSE = 0;
  public static final short NULLABLE_TRUE = 1;
  public static final short NULLABLE_UNKNOWN = 2;
  public static final short SCOPE_CURROW = 0;
  public static final short SCOPE_TRANSACTION = 1;
  public static final short SCOPE_SESSION = 2;
  public static final short PC_UNKNOWN = 0;
  public static final short PC_NON_PSEUDO = 1;
  public static final short PC_PSEUDO = 2;
  public static final short DUPLICATES_ALLOWED = 1;
  public static final short DUPLICATES_DISALLOWED = 0;
  public static final short TABLE_STAT = 0;
  public static final short INDEX_CLUSTERED = 1;
  public static final short INDEX_HASHED = 2;
  public static final short INDEX_OTHER = 3;
  public static final short PT_UNKNOWN = 0;
  public static final short PT_PROCEDURE = 1;
  public static final short PT_FUNCTION = 2;
  public static final short PCT_IN_PARAM = 1;
  public static final short PCT_INOUT_PARAM = 2;
  public static final short PCT_OUT_PARAM = 4;
  public static final short PCT_RESULT_COLUMN = 3;
  public static final short PCT_RETURN_VALUE = 5;
  public static final short PCT_UNKNOWN = 0;
  public static final short CASE_SENSITIVE = 1;
  public static final short CASE_INSENSITIVE = 0;
  public static final short SEARCHABLE_PRED_NONE = 0;
  public static final short SEARCHABLE_PRED_BASIC = 2;
  public static final short SEARCHABLE_PRED_CHAR = 1;
  public static final short SEARCHABLE_ALL = 3;
  public static final short UNSIGNED_TYPE = 1;
  public static final short SIGNED_TYPE = 0;
  public static final short PRECISION_SCALE_FIXED = 1;
  public static final short PRECISION_SCALE_NOT_FIXED = 0;
  public static final short AUTO_UNIQUE = 1;
  public static final short NOT_AUTO_UNIQUE = 0;
  public static final short UDT_STANDARD_SQL_TYPE = 0;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/MetadataConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */