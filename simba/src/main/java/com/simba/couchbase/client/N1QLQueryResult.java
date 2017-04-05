/*     */ package com.simba.couchbase.client;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.dsi.dataengine.utilities.ColumnMetadata;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class N1QLQueryResult
/*     */   implements IN1QLResultSet
/*     */ {
/*  41 */   private Map<String, Integer> m_arrayElementMaxValues = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JsonNode m_currentQueryContent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean m_isFirstRow;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean m_hasTypeIdentifier;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public N1QLQueryResult(JsonNode rootNode)
/*     */   {
/*  67 */     this.m_currentQueryContent = rootNode;
/*  68 */     this.m_hasTypeIdentifier = false;
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
/*     */   public ArrayList<N1QLQueryRow> allRowsRawData()
/*     */   {
/*  81 */     JsonNode results = this.m_currentQueryContent.path("results");
/*  82 */     ArrayList<N1QLQueryRow> dataList = new ArrayList();
/*  83 */     for (int nodeIndex = 0; nodeIndex < results.size(); nodeIndex++)
/*     */     {
/*  85 */       N1QLQueryRow currentRow = new N1QLQueryRow(results.get(nodeIndex));
/*  86 */       dataList.add(currentRow);
/*     */     }
/*  88 */     return dataList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<N1QLQueryRow> allRowsTableRowData(String tableName)
/*     */   {
/*  99 */     JsonNode results = this.m_currentQueryContent.path("results");
/* 100 */     ArrayList<N1QLQueryRow> dataList = new ArrayList();
/* 101 */     for (int nodeIndex = 0; nodeIndex < results.size(); nodeIndex++)
/*     */     {
/* 103 */       JsonNode tempRowcontent = results.get(nodeIndex).path(tableName);
/* 104 */       N1QLQueryRow currentRow = new N1QLQueryRow(tempRowcontent);
/* 105 */       dataList.add(currentRow);
/*     */     }
/* 107 */     return dataList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode allRowsRawJson()
/*     */   {
/* 117 */     return this.m_currentQueryContent.path("results");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<JsonNode> getErrorList()
/*     */   {
/* 127 */     JsonNode errorResults = this.m_currentQueryContent.path("errors");
/* 128 */     ArrayList<JsonNode> errorList = new ArrayList();
/* 129 */     for (int nodeIndex = 0; nodeIndex < errorResults.size(); nodeIndex++)
/*     */     {
/* 131 */       JsonNode tempRowcontent = errorResults.get(nodeIndex);
/* 132 */       errorList.add(tempRowcontent);
/*     */     }
/* 134 */     return errorList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getMetrics()
/*     */   {
/* 144 */     return this.m_currentQueryContent.path("metrics");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getRowCount()
/*     */   {
/* 154 */     JsonNode metricsBlock = getMetrics();
/* 155 */     long count = metricsBlock.get("resultCount").asLong();
/* 156 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getRequestID()
/*     */   {
/* 165 */     return this.m_currentQueryContent.path("requestID");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public N1QLQueryRow getFirstRow()
/*     */   {
/* 174 */     ArrayList<N1QLQueryRow> currentResult = allRowsRawData();
/* 175 */     if (0 < currentResult.size())
/*     */     {
/* 177 */       return (N1QLQueryRow)currentResult.get(0);
/*     */     }
/*     */     
/*     */ 
/* 181 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSignature()
/*     */   {
/* 191 */     return this.m_currentQueryContent.path("signature");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getStatus()
/*     */   {
/* 200 */     return this.m_currentQueryContent.path("status");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 210 */     return this.m_currentQueryContent.toString();
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
/*     */   public ArrayList<JsonNode[]> initColumnsInRow(ArrayList<ColumnMetadata> metadataColumns, int rowSize, Map<String, Short> queryOriginalIndexPair, Map<String, Short> queryOriginalIndexPairComplete)
/*     */     throws ErrorException
/*     */   {
/* 228 */     ArrayList<N1QLQueryRow> m_allRows = allRowsRawData();
/* 229 */     ArrayList<JsonNode[]> allRowDataStructure = new ArrayList();
/*     */     
/* 231 */     for (N1QLQueryRow row : m_allRows)
/*     */     {
/* 233 */       JsonNode[] currentRow = new JsonNode[rowSize];
/*     */       try
/*     */       {
/* 236 */         JsonNode queryContext = row.value();
/*     */         
/* 238 */         Iterator<Map.Entry<String, JsonNode>> rowItr = queryContext.fields();
/* 239 */         int colCount = 0;
/* 240 */         while (rowItr.hasNext())
/*     */         {
/* 242 */           Map.Entry<String, JsonNode> nextColumn = (Map.Entry)rowItr.next();
/* 243 */           String columnName = (String)nextColumn.getKey();
/* 244 */           if (null != columnName)
/*     */           {
/* 246 */             JsonNode columnValue = (JsonNode)nextColumn.getValue();
/* 247 */             Short columnIndex = getColumnOriginalIndex(columnName, queryOriginalIndexPair, queryOriginalIndexPairComplete);
/*     */             
/*     */ 
/*     */ 
/* 251 */             if (null != columnIndex)
/*     */             {
/* 253 */               currentRow[columnIndex.shortValue()] = columnValue;
/*     */             }
/*     */             else
/*     */             {
/* 257 */               currentRow[colCount] = null;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 262 */             currentRow[colCount] = null;
/*     */           }
/*     */           
/* 265 */           colCount++;
/*     */         }
/* 267 */         allRowDataStructure.add(currentRow);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 271 */         ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.DATA_FETCH_INIT_ERR.name(), new String[] { "initialization error" });
/*     */         
/*     */ 
/* 274 */         err.initCause(ex);
/* 275 */         throw err;
/*     */       }
/*     */     }
/*     */     
/* 279 */     return allRowDataStructure;
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
/*     */   public ArrayList<JsonNode[]> initFlattenColumnsInRow(ArrayList<ColumnMetadata> metadataColumns, int rowSize, Map<String, Short> queryOriginalIndexPair)
/*     */     throws ErrorException
/*     */   {
/* 295 */     ArrayList<N1QLQueryRow> m_allRows = allRowsRawData();
/* 296 */     ArrayList<JsonNode[]> allRowDataStructure = new ArrayList();
/*     */     
/* 298 */     this.m_isFirstRow = true;
/*     */     
/* 300 */     for (N1QLQueryRow row : m_allRows)
/*     */     {
/* 302 */       JsonNode[] currentRow = new JsonNode[rowSize];
/*     */       try
/*     */       {
/* 305 */         JsonNode queryContext = row.value();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 316 */         if (!queryContext.path(((ColumnMetadata)metadataColumns.get(0)).getTableName()).isMissingNode())
/*     */         {
/* 318 */           queryContext = queryContext.path(((ColumnMetadata)metadataColumns.get(0)).getTableName());
/*     */         }
/*     */         
/*     */ 
/* 322 */         int rowCount = 0;
/* 323 */         if ((rowSize > 0) && (!queryContext.fields().hasNext()))
/*     */         {
/*     */ 
/*     */ 
/* 327 */           for (int rowIndex = 0; rowIndex < rowSize; rowIndex++)
/*     */           {
/* 329 */             String columnName = "$1";
/*     */             
/* 331 */             flattenColumnNamesHelper(rowCount, columnName, queryContext, currentRow, queryOriginalIndexPair);
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 341 */           Iterator<Map.Entry<String, JsonNode>> rowItr = queryContext.fields();
/* 342 */           while (rowItr.hasNext())
/*     */           {
/* 344 */             Map.Entry<String, JsonNode> nextColumn = (Map.Entry)rowItr.next();
/* 345 */             String columnName = (String)nextColumn.getKey();
/* 346 */             JsonNode columnValue = (JsonNode)nextColumn.getValue();
/*     */             
/* 348 */             flattenColumnNamesHelper(rowCount, columnName, columnValue, currentRow, queryOriginalIndexPair);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 357 */         allRowDataStructure.add(currentRow);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 361 */         ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.DATA_FETCH_INIT_ERR.name(), new String[] { "initialization error" });
/*     */         
/*     */ 
/* 364 */         err.initCause(ex);
/* 365 */         throw err;
/*     */       }
/*     */       
/* 368 */       this.m_isFirstRow = false;
/*     */     }
/*     */     
/* 371 */     return allRowDataStructure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasTypeIdentifier()
/*     */   {
/* 383 */     return this.m_hasTypeIdentifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHasTypeIdentifier(boolean hasTypeIdentifier)
/*     */   {
/* 391 */     this.m_hasTypeIdentifier = hasTypeIdentifier;
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
/*     */   private Map<String, JsonNode> flattenColumnNames(String baseName, JsonNode column)
/*     */   {
/* 408 */     Map<String, JsonNode> flattenedColumns = new HashMap();
/*     */     
/* 410 */     if (column.isArray())
/*     */     {
/* 412 */       flattenedColumns = flattenArrayName(baseName, (ArrayNode)column);
/*     */     }
/* 414 */     else if (column.isObject())
/*     */     {
/* 416 */       flattenedColumns = flattenObjectName(baseName, (ObjectNode)column);
/*     */     }
/*     */     else
/*     */     {
/* 420 */       flattenedColumns.put(baseName, column);
/*     */     }
/*     */     
/* 423 */     return flattenedColumns;
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
/*     */   private Map<String, JsonNode> flattenArrayName(String baseName, ArrayNode array)
/*     */   {
/* 436 */     Map<String, JsonNode> flattenedArray = new HashMap();
/*     */     
/* 438 */     int numElements = 0;
/* 439 */     if (this.m_isFirstRow)
/*     */     {
/*     */ 
/*     */ 
/* 443 */       numElements = array.size();
/* 444 */       this.m_arrayElementMaxValues.put(baseName, Integer.valueOf(numElements));
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 450 */       if (!this.m_arrayElementMaxValues.containsKey(baseName))
/*     */       {
/* 452 */         this.m_arrayElementMaxValues.put(baseName, Integer.valueOf(0));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 457 */       numElements = array.size() < ((Integer)this.m_arrayElementMaxValues.get(baseName)).intValue() ? array.size() : ((Integer)this.m_arrayElementMaxValues.get(baseName)).intValue();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 462 */     for (int i = 0; i < numElements; i++)
/*     */     {
/*     */ 
/*     */ 
/* 466 */       StringBuilder name = new StringBuilder();
/* 467 */       name.append(baseName).append("[").append(i).append("]");
/*     */       
/* 469 */       if (array.get(i).isArray())
/*     */       {
/* 471 */         flattenedArray.putAll(flattenArrayName(name.toString(), (ArrayNode)array.get(i)));
/*     */ 
/*     */       }
/* 474 */       else if (array.get(i).isObject())
/*     */       {
/* 476 */         flattenedArray.putAll(flattenObjectName(name.toString(), (ObjectNode)array.get(i)));
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 481 */         flattenedArray.put(name.toString(), array.get(i));
/*     */       }
/*     */     }
/*     */     
/* 485 */     return flattenedArray;
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
/*     */   private Map<String, JsonNode> flattenObjectName(String baseName, ObjectNode object)
/*     */   {
/* 498 */     Map<String, JsonNode> flattenedObject = new HashMap();
/*     */     
/* 500 */     Iterator<Map.Entry<String, JsonNode>> objectItr = object.fields();
/* 501 */     while (objectItr.hasNext())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 506 */       Map.Entry<String, JsonNode> objectEntry = (Map.Entry)objectItr.next();
/* 507 */       String name = baseName + "." + (String)objectEntry.getKey();
/* 508 */       JsonNode value = (JsonNode)objectEntry.getValue();
/*     */       
/* 510 */       if (value.isArray())
/*     */       {
/* 512 */         flattenedObject.putAll(flattenArrayName(name, (ArrayNode)value));
/*     */       }
/* 514 */       else if (value.isObject())
/*     */       {
/* 516 */         flattenedObject.putAll(flattenObjectName(name, (ObjectNode)value));
/*     */       }
/*     */       else
/*     */       {
/* 520 */         flattenedObject.put(name, value);
/*     */       }
/*     */     }
/*     */     
/* 524 */     return flattenedObject;
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
/*     */   private Short getColumnOriginalIndex(String columnName, Map<String, Short> queryOriginalIndexPair, Map<String, Short> queryOriginalIndexPairComplete)
/*     */   {
/* 540 */     Short index = (Short)queryOriginalIndexPair.get(columnName);
/* 541 */     if (null != index)
/*     */     {
/* 543 */       return index;
/*     */     }
/*     */     
/*     */ 
/* 547 */     Short indexFromFullColumnName = (Short)queryOriginalIndexPairComplete.get(columnName);
/* 548 */     if (null != indexFromFullColumnName)
/*     */     {
/* 550 */       return indexFromFullColumnName;
/*     */     }
/*     */     
/*     */ 
/* 554 */     return null;
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
/*     */   private void flattenColumnNamesHelper(int rowCount, String columnName, JsonNode columnValue, JsonNode[] currentRow, Map<String, Short> queryOriginalIndexPair)
/*     */   {
/* 577 */     Map<String, JsonNode> flattenedColumns = flattenColumnNames(columnName, columnValue);
/*     */     
/*     */ 
/* 580 */     for (Map.Entry<String, JsonNode> entry : flattenedColumns.entrySet())
/*     */     {
/* 582 */       String colName = (String)entry.getKey();
/* 583 */       JsonNode colValue = (JsonNode)entry.getValue();
/*     */       
/* 585 */       if (null != colName)
/*     */       {
/* 587 */         Short columnIndex = (Short)queryOriginalIndexPair.get(colName);
/* 588 */         if (null != columnIndex)
/*     */         {
/* 590 */           currentRow[((Short)queryOriginalIndexPair.get(colName)).shortValue()] = colValue;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 595 */         currentRow[rowCount] = null;
/*     */       }
/* 597 */       rowCount++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/client/N1QLQueryResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */