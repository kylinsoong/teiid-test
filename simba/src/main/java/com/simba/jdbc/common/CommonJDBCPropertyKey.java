/*     */ package com.simba.jdbc.common;
/*     */ 
/*     */ import com.simba.dsi.utilities.DSIPropertyKey;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonJDBCPropertyKey
/*     */   extends DSIPropertyKey
/*     */ {
/*     */   public static final String USERNAME = "UID";
/*     */   public static final String PASSWORD = "PWD";
/*     */   public static final String HOST_KEY = "Host";
/*     */   public static final String PORT_KEY = "Port";
/*     */   public static final String SCHEMA_KEY = "ConnSchema";
/*     */   public static final String AUTH_MECH_KEY = "AuthMech";
/*     */   public static final String KRB_REALM_KEY = "KrbRealm";
/*     */   public static final String KRB_HOST_FQDN_KEY = "KrbHostFQDN";
/*     */   public static final String KRB_SERVICE_NAME_KEY = "KrbServiceName";
/*     */   public static final String SSL_KEYSTORE_KEY = "SSLKeyStore";
/*     */   public static final String SSL_KEYSTORE_PWD_KEY = "SSLKeyStorePwd";
/*     */   
/*     */   public static List<String> getCommonOptionalKeys()
/*     */   {
/*  92 */     List<String> result = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*  96 */     result.add("AuthMech");
/*     */     
/*  98 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> getCommonRequiredKeys()
/*     */   {
/* 108 */     List<String> result = new ArrayList();
/*     */     
/* 110 */     result.add("UID");
/* 111 */     result.add("PWD");
/*     */     
/* 113 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/CommonJDBCPropertyKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */