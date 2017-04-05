/*     */ package com.simba.couchbase.dataengine.operationhandler;
/*     */ 
/*     */ import com.simba.couchbase.dataengine.CBSQLDataEngine;
/*     */ import com.simba.couchbase.dataengine.resultset.CBResultSet;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
/*     */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
/*     */ import com.simba.sqlengine.dsiext.dataengine.DSIExtOperationHandlerFactory;
/*     */ import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
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
/*     */ public class CBOperationHandlerFactory
/*     */   implements DSIExtOperationHandlerFactory
/*     */ {
/*     */   private CBSQLDataEngine m_dataEngine;
/*     */   private ILogger m_log;
/*     */   
/*     */   public CBOperationHandlerFactory(CBSQLDataEngine dataEngineRefe, ILogger log)
/*     */   {
/*  44 */     this.m_dataEngine = dataEngineRefe;
/*  45 */     this.m_log = log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameterSetCount(int paramSetCount) {}
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
/*     */   public IBooleanExprHandler createFilterHandler(DSIExtJResultSet table)
/*     */   {
/*  91 */     if (null == table)
/*     */     {
/*     */ 
/*  94 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  99 */     if (((CBResultSet)table).isDMLResultSet())
/*     */     {
/* 101 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 107 */       return new CBFilterHandler(table, this.m_log);
/*     */ 
/*     */     }
/*     */     catch (ErrorException ex)
/*     */     {
/*     */ 
/* 113 */       LogUtilities.logError(ex, this.m_log);
/*     */     }
/*     */     
/*     */ 
/* 117 */     return null;
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
/*     */   public IBooleanExprHandler createJoinHandler(DSIExtJResultSet table1, DSIExtJResultSet table2, AEJoin.AEJoinType joinType)
/*     */   {
/* 152 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/operationhandler/CBOperationHandlerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */