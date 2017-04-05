/*     */ package com.simba.couchbase.client;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import java.util.ArrayList;
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
/*     */ public class N1QLRowCountSet
/*     */   implements IN1QLResultSet
/*     */ {
/*     */   private int m_affectRowCount;
/*     */   private JsonNode m_currentQueryContent;
/*  25 */   private final String MUTATION_COUNT_KEY = "mutationCount";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public N1QLRowCountSet(JsonNode rootNode)
/*     */   {
/*  36 */     this.m_currentQueryContent = rootNode;
/*  37 */     this.m_affectRowCount = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<N1QLQueryRow> allRowsRawData()
/*     */   {
/*  46 */     JsonNode results = this.m_currentQueryContent.path("results");
/*  47 */     ArrayList<N1QLQueryRow> dataList = new ArrayList();
/*  48 */     for (int nodeIndex = 0; nodeIndex < results.size(); nodeIndex++)
/*     */     {
/*  50 */       N1QLQueryRow currentRow = new N1QLQueryRow(results.get(nodeIndex));
/*  51 */       dataList.add(currentRow);
/*     */     }
/*  53 */     return dataList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<JsonNode> getErrorList()
/*     */   {
/*  63 */     JsonNode errorResults = this.m_currentQueryContent.path("errors");
/*  64 */     ArrayList<JsonNode> errorList = new ArrayList();
/*  65 */     for (int nodeIndex = 0; nodeIndex < errorResults.size(); nodeIndex++)
/*     */     {
/*  67 */       JsonNode tempRowcontent = errorResults.get(nodeIndex);
/*  68 */       errorList.add(tempRowcontent);
/*     */     }
/*  70 */     return errorList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRowCount()
/*     */   {
/*  78 */     JsonNode inforNode = this.m_currentQueryContent.path("metrics").path("mutationCount");
/*  79 */     this.m_affectRowCount = inforNode.intValue();
/*  80 */     return this.m_affectRowCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSignature()
/*     */   {
/*  89 */     return this.m_currentQueryContent.path("signature");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getStatus()
/*     */   {
/*  98 */     return this.m_currentQueryContent.path("status");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return this.m_currentQueryContent.toString();
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/client/N1QLRowCountSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */