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
/*    */ public class N1QLPlan
/*    */   extends N1QLQueryResult
/*    */ {
/*    */   public N1QLPlan(JsonNode rootNode)
/*    */   {
/* 27 */     super(rootNode);
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
/*    */   public String getPlanValue()
/*    */   {
/* 40 */     JsonNode planContent = allRowsRawJson().get(0);
/* 41 */     return planContent.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonNode getSignature()
/*    */   {
/* 50 */     return allRowsRawJson().get(0).path("signature");
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/client/N1QLPlan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */