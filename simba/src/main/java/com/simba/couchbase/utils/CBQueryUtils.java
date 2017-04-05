/*     */ package com.simba.couchbase.utils;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.simba.couchbase.core.CBJDBCDriver;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import com.simba.support.exceptions.ExceptionBuilder;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBQueryUtils
/*     */ {
/*     */   public static final String CB_CONNECTION_QUERY = "SELECT * from system:keyspaces";
/*     */   public static final String CB_CATALOGFUNCTION_CATALOG_QUERY = "SELECT DISTINCT namespace_id FROM system:keyspaces";
/*     */   public static final String CB_CATALOGFUNCTION_CATALOG_KEY = "namespace_id";
/*     */   public static final String CB_CATALOGFUNCTION_SCHEMA_QUERY_HEADER = "SELECT name, namespace_id, store_id FROM system:keyspaces";
/*     */   public static final String CB_CATALOGFUNCTION_SCHEMA_KEY = "name";
/*     */   public static final String CB_CATALOGFUNCTION_TABLE_QUERY_HEADER = "SELECT DISTINCT ";
/*     */   public static final String CB_CATALOGFUNCTION_COLUMN_QUERY_HEADER = "SELECT * FROM ";
/*     */   public static final String CB_PIMARY_KCOLUMN_NAME = "PK";
/*     */   public static final String CB_COLUMN_IDENTIFIER_NAME = "type";
/*     */   public static final String CB_SCHEMAMAP_KEY = "Delimiter";
/*     */   public static final String CB_SCHEMAMAP_KEY_NAME = "~~~SchemaMap";
/*  85 */   public static HashSet<String> m_consistencyLevelVariableMap = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  92 */     m_consistencyLevelVariableMap.add("not_bounded");
/*  93 */     m_consistencyLevelVariableMap.add("at_plus");
/*  94 */     m_consistencyLevelVariableMap.add("request_plus");
/*  95 */     m_consistencyLevelVariableMap.add("statement_plus");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ErrorException buildServerErrorMessage(List<JsonNode> errorList, String cbErrorKey, String errorClientMessage)
/*     */   {
/* 111 */     JsonNode errorInfo = (JsonNode)errorList.get(0);
/* 112 */     String errorCode = String.valueOf(errorInfo.get("code").intValue());
/* 113 */     String errorServerMessage = errorInfo.get("msg").textValue();
/* 114 */     ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(cbErrorKey, new String[] { errorCode, errorServerMessage, errorClientMessage });
/*     */     
/*     */ 
/* 117 */     return err;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ErrorException buildRegularErrorMessage(String cbErrorKey, String[] errorClientMessage)
/*     */   {
/* 132 */     ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(cbErrorKey, errorClientMessage);
/*     */     
/*     */ 
/* 135 */     return err;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/utils/CBQueryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */