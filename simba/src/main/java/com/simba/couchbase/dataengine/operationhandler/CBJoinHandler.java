/*    */ package com.simba.couchbase.dataengine.operationhandler;
/*    */ 
/*    */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
/*    */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
/*    */ import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CBJoinHandler
/*    */   implements IBooleanExprHandler
/*    */ {
/*    */   public CBJoinHandler(DSIExtJResultSet table1, DSIExtJResultSet table2) {}
/*    */   
/*    */   public boolean canHandleMoreClauses()
/*    */   {
/* 21 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean passdown(AEBooleanExpr node)
/*    */   {
/* 28 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DSIExtJResultSet takeResult()
/*    */   {
/* 35 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/operationhandler/CBJoinHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */