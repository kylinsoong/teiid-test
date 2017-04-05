/*    */ package com.simba.couchbase.dataengine.querygeneration;
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
/*    */ public class CBParamValue
/*    */ {
/*    */   private String m_paramName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String m_paramValue;
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
/*    */   public CBParamValue(String paramName, String paramValue)
/*    */   {
/* 37 */     this.m_paramName = paramName;
/* 38 */     this.m_paramValue = paramValue;
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
/*    */   public String getParamName()
/*    */   {
/* 52 */     return this.m_paramName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getParamValue()
/*    */   {
/* 62 */     return this.m_paramValue;
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/querygeneration/CBParamValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */