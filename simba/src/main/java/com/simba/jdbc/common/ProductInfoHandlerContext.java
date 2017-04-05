/*    */ package com.simba.jdbc.common;
/*    */ 
/*    */ import com.simba.support.MessageSourceImpl;
/*    */ import com.simba.support.exceptions.ExceptionBuilder;
/*    */ import com.simba.support.exceptions.ExceptionUtilities;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProductInfoHandlerContext
/*    */ {
/*    */   private static final int LICENSE_COMPONENT_ID = 201;
/* 32 */   private static String s_component_name = "ProductInfoHandler";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static final String RESOURCE_NAME = "licenseMessages";
/*    */   
/*    */ 
/*    */ 
/* 41 */   public static final ExceptionBuilder s_messages = new ExceptionBuilder(201);
/*    */   
/*    */ 
/* 44 */   protected static MessageSourceImpl s_defaultMsgSrc = new MessageSourceImpl(true, true);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static
/*    */   {
/* 55 */     registerMessages();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static MessageSourceImpl getDefaultMsgSource()
/*    */   {
/* 66 */     return s_defaultMsgSrc;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void setDefaultMsgSource(MessageSourceImpl messageSource, String componentName)
/*    */   {
/* 77 */     s_defaultMsgSrc = messageSource;
/* 78 */     s_component_name = componentName;
/*    */     
/* 80 */     registerMessages();
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
/*    */   private static void registerMessages()
/*    */   {
/* 93 */     String packageName = ExceptionUtilities.getPackageName(ProductInfoHandlerContext.class);
/*    */     
/* 95 */     StringBuilder sqlEngineMessageFile = new StringBuilder(packageName);
/* 96 */     sqlEngineMessageFile.append(".");
/* 97 */     sqlEngineMessageFile.append("licenseMessages");
/*    */     
/* 99 */     s_defaultMsgSrc.registerMessages(sqlEngineMessageFile.toString(), 201, s_component_name);
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/ProductInfoHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */