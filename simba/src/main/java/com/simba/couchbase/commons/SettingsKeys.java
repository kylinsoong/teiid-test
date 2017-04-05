/*    */ package com.simba.couchbase.commons;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SettingsKeys
/*    */ {
/*    */   public static final int CB_ERROR = 101;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String CB_COMPONENT_NAME = "CouchbaseJDBCDriver";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String CB_CATALOG = "Couchbase";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String CB_SUBPROTOCAL_NAME = "couchbase";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int AESTATEMENT_TABLE_NOT_FOUND_ERROR_CODE = 12000;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int AESTATEMENT_COLUMN_NOT_FOUND_ERROR_CODE = 12010;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String CB_DEFAULT_BUCKET = "";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String CB_DEFAULT_CATALOG = "default";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 65 */   public static final Boolean CB_DEFAULT_ENABLE_SSL_FLAG = Boolean.valueOf(false);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 70 */   public static final Boolean CB_DEFAULT_USE_REDUNDANCY_FLAG = Boolean.valueOf(false);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 75 */   public static final Boolean CB_DEFAULT_AUTH_MECH_FLAG = Boolean.valueOf(false);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 80 */   public static int CB_DEFAULT_SCHEMA_MAP_OPERATION = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String CB_DEFAULT_LOCAL_SCHEMA_FILE_PATH = "";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int CB_DEFAULT_SCHEMAMAP_SAMPLE_SIZE = 100;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 95 */   public static final Boolean CB_DEFAULT_QUERY_MODE_FLAG = Boolean.valueOf(false);
/*    */   public static final String CB_DEFAULT_SCAN_CONSISTENCY = "request_plus";
/*    */   public static final String CB_RAW_COLUMN_NAME = "$1";
/*    */   public static final String CB_TPYENAME_MATCHER_PATTERN = "([a-zA-Z_]\\w*|(?:`[^`]*`)+):([a-zA-Z_]\\w*|(?:`[^`]*`)+)(?:$|,)";
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/commons/SettingsKeys.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */