/*    */ package com.simba.couchbase.client;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonNode;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class N1QLQueryRow
/*    */ {
/*    */   private JsonNode m_rowValue;
/*    */   
/*    */   public N1QLQueryRow(JsonNode rowValue)
/*    */   {
/* 36 */     this.m_rowValue = rowValue;
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
/*    */   public String toString()
/*    */   {
/* 50 */     return this.m_rowValue.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonNode value()
/*    */   {
/* 59 */     return this.m_rowValue;
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/client/N1QLQueryRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */