/*    */ package com.simba.couchbase.jdbc4;
/*    */ 
/*    */ import com.simba.couchbase.core.CBJDBCDriver;
/*    */ import com.simba.couchbase.utils.CBCoreUtils;
/*    */ import com.simba.jdbc.common.AbstractDriver;
/*    */ import com.simba.jdbc.common.JDBCObjectFactory;
/*    */ import com.simba.jdbc.jdbc4.JDBC4AbstractDriver;
/*    */ import java.io.PrintStream;
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
/*    */ public class Driver
/*    */   extends JDBC4AbstractDriver
/*    */ {
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 33 */       AbstractDriver.initialize(new Driver(), CBJDBCDriver.class.getName());
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 37 */       System.err.println(e.getLocalizedMessage());
/*    */     }
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
/* 55 */     return "couchbase";
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
/* 74 */     return CBCoreUtils.parseSubName(subname, properties);
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
/* 91 */     return new CBJDBC4ObjectFactory();
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/jdbc4/Driver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */