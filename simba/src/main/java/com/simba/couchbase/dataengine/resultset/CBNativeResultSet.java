/*     */ package com.simba.couchbase.dataengine.resultset;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.simba.couchbase.client.N1QLQueryResult;
/*     */ import com.simba.couchbase.utils.CBDataTypeUtils;
/*     */ import com.simba.dsi.dataengine.utilities.ColumnMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*     */ import com.simba.jdbc.common.CommonResultSet;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
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
/*     */ public class CBNativeResultSet
/*     */   extends CommonResultSet
/*     */ {
/*     */   private ArrayList<JsonNode[]> m_allRowDataStructure;
/*     */   private int m_currentFetchedRows;
/*     */   private ILogger m_log;
/*     */   private ArrayList<ColumnMetadata> m_columnMetadata;
/*     */   private long m_rowCount;
/*     */   private boolean m_isN1QLMode;
/*     */   
/*     */   public CBNativeResultSet(boolean isN1QLMode, N1QLQueryResult queryResult, ArrayList<ColumnMetadata> metadataColumns, Map<String, Short> queryOriginalIndexPair, Map<String, Short> queryOriginalIndexPairComplete, boolean isPrepareOnly, boolean hasData, ILogger logger)
/*     */     throws ErrorException
/*     */   {
/*  92 */     this.m_log = logger;
/*  93 */     this.m_currentFetchedRows = 0;
/*  94 */     this.m_isN1QLMode = isN1QLMode;
/*     */     
/*     */ 
/*     */ 
/*  98 */     if (metadataColumns.size() > 0)
/*     */     {
/* 100 */       setSelectColumns(metadataColumns);
/*     */     }
/*     */     
/* 103 */     if (hasData)
/*     */     {
/* 105 */       if (!isPrepareOnly)
/*     */       {
/* 107 */         this.m_rowCount = queryResult.getRowCount();
/* 108 */         if (this.m_isN1QLMode)
/*     */         {
/* 110 */           this.m_allRowDataStructure = queryResult.initFlattenColumnsInRow(metadataColumns, metadataColumns.size(), queryOriginalIndexPair);
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 117 */           this.m_allRowDataStructure = queryResult.initColumnsInRow(metadataColumns, metadataColumns.size(), queryOriginalIndexPair, queryOriginalIndexPairComplete);
/*     */         }
/*     */       }
/*     */     }
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
/*     */   public boolean getData(int column, long offset, long maxSize, DataWrapper retrievedData)
/*     */     throws ErrorException
/*     */   {
/* 184 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 186 */     Short sqlType = Short.valueOf(((ColumnMetadata)this.m_columnMetadata.get(column)).getTypeMetadata().getType());
/*     */     
/* 188 */     JsonNode nodeValue = ((JsonNode[])this.m_allRowDataStructure.get(this.m_currentFetchedRows - 1))[column];
/*     */     
/* 190 */     return CBDataTypeUtils.convertColumnToData(offset, maxSize, retrievedData, sqlType, nodeValue, this.m_log);
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
/*     */   public long getRowCount()
/*     */     throws ErrorException
/*     */   {
/* 212 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 216 */     return this.m_rowCount;
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
/*     */   public ArrayList<ColumnMetadata> getSelectColumns()
/*     */     throws ErrorException
/*     */   {
/* 230 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "Column Metadata: " + this.m_columnMetadata });
/* 231 */     return this.m_columnMetadata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMoreRows()
/*     */     throws ErrorException
/*     */   {
/* 242 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 246 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasRowCount()
/*     */   {
/* 255 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 259 */     return false;
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
/*     */   protected void doCloseCursor()
/*     */     throws ErrorException
/*     */   {
/* 274 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
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
/*     */   public void close()
/*     */   {
/* 293 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 294 */     super.close();
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
/*     */   protected boolean doMoveToNextRow()
/*     */     throws ErrorException
/*     */   {
/* 313 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 315 */     if (this.m_currentFetchedRows < this.m_rowCount)
/*     */     {
/* 317 */       this.m_currentFetchedRows += 1;
/* 318 */       return true;
/*     */     }
/*     */     
/* 321 */     return false;
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
/*     */   public boolean supportsHasMoreRows()
/*     */   {
/* 334 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectColumns(ArrayList<ColumnMetadata> columns)
/*     */   {
/* 344 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { columns });
/*     */     
/* 346 */     this.m_columnMetadata = columns;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/resultset/CBNativeResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */