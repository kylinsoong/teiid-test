/*    */ package com.simba.couchbase.jdbc4;
/*    */ 
/*    */ import com.simba.couchbase.core.CBJDBCDriver;
/*    */ import com.simba.couchbase.utils.CBCoreUtils;
/*    */ import com.simba.jdbc.common.AbstractDataSource;
/*    */ import com.simba.jdbc.common.JDBCObjectFactory;
/*    */ import com.simba.jdbc.jdbc4.JDBC4AbstractDataSource;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataSource
/*    */   extends JDBC4AbstractDataSource
/*    */ {
/*    */   static
/*    */   {
/* 32 */     AbstractDataSource.initialize(CBJDBCDriver.class.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getSubProtocol()
/*    */   {
/* 49 */     return "couchbase";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean parseSubName(String subname, Properties properties)
/*    */   {
/* 68 */     return CBCoreUtils.parseSubName(subname, properties);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected JDBCObjectFactory createJDBCObjectFactory()
/*    */   {
/* 85 */     return new CBJDBC4ObjectFactory();
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/jdbc4/DataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */