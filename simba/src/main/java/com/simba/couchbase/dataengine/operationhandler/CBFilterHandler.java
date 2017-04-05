/*     */ package com.simba.couchbase.dataengine.operationhandler;
/*     */ 
/*     */ import com.simba.couchbase.dataengine.querygeneration.CBBooleanExprBuilder;
/*     */ import com.simba.couchbase.dataengine.querygeneration.CBParamValue;
/*     */ import com.simba.couchbase.dataengine.resultset.CBResultSet;
/*     */ import com.simba.couchbase.dataengine.resultset.CBTableColumn;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
/*     */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
/*     */ import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
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
/*     */ public class CBFilterHandler
/*     */   implements IBooleanExprHandler
/*     */ {
/*     */   private ILogger m_log;
/*     */   private Map<String, CBTableColumn> m_newLetClauses;
/*     */   private List<String> m_newWhereClauses;
/*     */   private List<CBParamValue> m_newParamValues;
/*     */   private CBResultSet m_resultSet;
/*     */   private Map<DSIExtJResultSet, CBResultSet> m_tableCopyMap;
/*     */   
/*     */   public CBFilterHandler(DSIExtJResultSet table, ILogger log)
/*     */     throws ErrorException
/*     */   {
/*  81 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/*  82 */     this.m_log = log;
/*  83 */     this.m_newWhereClauses = new Stack();
/*  84 */     this.m_newLetClauses = new HashMap();
/*  85 */     this.m_newParamValues = new ArrayList();
/*  86 */     this.m_tableCopyMap = new HashMap();
/*     */     
/*  88 */     CBResultSet copyTable = new CBResultSet((CBResultSet)table, this.m_log);
/*  89 */     this.m_tableCopyMap.put(table, copyTable);
/*  90 */     this.m_resultSet = copyTable;
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
/*     */   public boolean canHandleMoreClauses()
/*     */   {
/* 118 */     return true;
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
/*     */   public boolean passdown(AEBooleanExpr node)
/*     */   {
/* 139 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 141 */     String whereClause = new CBBooleanExprBuilder(this.m_resultSet.getDataEngineRef(), this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(node);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */     if (null == whereClause)
/*     */     {
/* 150 */       return false;
/*     */     }
/*     */     
/* 153 */     this.m_newWhereClauses.add(whereClause);
/*     */     
/* 155 */     return true;
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
/*     */   public DSIExtJResultSet takeResult()
/*     */   {
/* 203 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "Pass down taken success" });
/* 204 */     if (this.m_newWhereClauses.size() == 0)
/*     */     {
/*     */ 
/* 207 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 211 */     this.m_resultSet.addLetClauses(this.m_newLetClauses);
/* 212 */     this.m_resultSet.giveWhereClauses(this.m_newWhereClauses);
/* 213 */     this.m_resultSet.giveParameterValues(this.m_newParamValues);
/*     */     
/* 215 */     return this.m_resultSet;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/operationhandler/CBFilterHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */