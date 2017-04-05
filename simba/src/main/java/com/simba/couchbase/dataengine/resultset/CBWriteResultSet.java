/*      */ package com.simba.couchbase.dataengine.resultset;
/*      */ 
/*      */ import com.fasterxml.jackson.databind.JsonNode;
/*      */ import com.fasterxml.jackson.databind.ObjectMapper;
/*      */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*      */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*      */ import com.simba.couchbase.client.N1QLQueryRow;
/*      */ import com.simba.couchbase.client.N1QLRowCountSet;
/*      */ import com.simba.couchbase.core.CBClient;
/*      */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*      */ import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
/*      */ import com.simba.couchbase.schemamap.metadata.CBSchemaMetadata;
/*      */ import com.simba.couchbase.schemamap.metadata.CBTableMetadata;
/*      */ import com.simba.couchbase.schemamap.parser.CBAttribute;
/*      */ import com.simba.couchbase.schemamap.parser.CBName;
/*      */ import com.simba.couchbase.utils.CBQueryUtils;
/*      */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*      */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*      */ import com.simba.dsi.dataengine.utilities.Updatable;
/*      */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet.DMLType;
/*      */ import com.simba.support.ILogger;
/*      */ import com.simba.support.LogUtilities;
/*      */ import com.simba.support.Pair;
/*      */ import com.simba.support.exceptions.ErrorException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CBWriteResultSet
/*      */   extends CBResultSet
/*      */ {
/*      */   private Map<String, Map<String, List<Integer>>> m_deleteInfoMap;
/*      */   private List<Pair<String, List<CachedDataInfo>>> m_dataCache;
/*      */   private String m_dmlQuery;
/*      */   private DSIExtJResultSet.DMLType m_dmlType;
/*      */   private ILogger m_log;
/*      */   private List<CachedDataInfo> m_rowCache;
/*      */   
/*      */   private class CachedDataInfo
/*      */   {
/*      */     private String key;
/*      */     private String value;
/*      */     private int sqlType;
/*      */     private boolean isJson;
/*      */     private String jdbcName;
/*      */     private CBName sourceName;
/*      */     
/*      */     private CachedDataInfo() {}
/*      */     
/*      */     public boolean isJson()
/*      */     {
/*   60 */       return this.isJson;
/*      */     }
/*      */     
/*      */     public String getKey() {
/*   64 */       return this.key;
/*      */     }
/*      */     
/*      */     public String getName() {
/*   68 */       return this.jdbcName;
/*      */     }
/*      */     
/*      */     public int getSQLType() {
/*   72 */       return this.sqlType;
/*      */     }
/*      */     
/*      */     public CBName getSourceName() {
/*   76 */       return this.sourceName;
/*      */     }
/*      */     
/*      */     public String getValue() {
/*   80 */       return this.value;
/*      */     }
/*      */     
/*      */     public String getValueNoQuote() {
/*   84 */       return this.value.substring(1, this.value.length() - 1);
/*      */     }
/*      */     
/*      */     public void setIsJson(boolean isJson) {
/*   88 */       this.isJson = isJson;
/*      */     }
/*      */     
/*      */     public void setKey(String key) {
/*   92 */       this.key = key;
/*      */     }
/*      */     
/*      */     public void setName(String jdbcName) {
/*   96 */       this.jdbcName = jdbcName;
/*      */     }
/*      */     
/*      */     public void setSQLType(int sqlType) {
/*  100 */       this.sqlType = sqlType;
/*      */     }
/*      */     
/*      */     public void setSourceName(CBName sourceName) {
/*  104 */       this.sourceName = sourceName;
/*      */     }
/*      */     
/*      */     public void setValue(String value) {
/*  108 */       this.value = value;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CBWriteResultSet(CBSchemaMetadata schemaMeta, CBTableMetadata tableMeta, ArrayList<CBColumnMetadata> columnMetadata, CBClient client, ILogger log)
/*      */     throws ErrorException
/*      */   {
/*  171 */     super(schemaMeta, tableMeta, columnMetadata, client, log);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  177 */     this.m_log = log;
/*  178 */     this.m_dataCache = new ArrayList();
/*  179 */     this.m_deleteInfoMap = new HashMap();
/*  180 */     this.m_rowCache = new ArrayList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void appendRow()
/*      */     throws ErrorException
/*      */   {
/*  196 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  197 */     this.m_dmlType = DSIExtJResultSet.DMLType.INSERT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void deleteRow()
/*      */     throws ErrorException
/*      */   {
/*  210 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  211 */     String pk = null;
/*  212 */     for (int keyIndex = 0; keyIndex < this.m_keyColumns.size(); keyIndex++)
/*      */     {
/*      */ 
/*  215 */       CBTableColumn currentKeyColumn = (CBTableColumn)this.m_keyColumns.get(keyIndex);
/*  216 */       CBName keySrcName = currentKeyColumn.getSourceName();
/*  217 */       if ((keySrcName.isSpecialIdentifier()) && (keySrcName.getParentName().isAttribute()))
/*      */       {
/*      */ 
/*  220 */         pk = this.m_currentRowData.get(currentKeyColumn.getSelectAlias()).asText();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  225 */     if (this.m_isVirtualTable)
/*      */     {
/*  227 */       if (null == this.m_deleteInfoMap.get(pk))
/*      */       {
/*  229 */         this.m_deleteInfoMap.put(pk, new HashMap());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  235 */       String srcKey = "";
/*  236 */       int indexValue = 0;
/*  237 */       boolean isAttrArray = false;
/*  238 */       boolean isSkipRow = false;
/*      */       
/*      */ 
/*  241 */       CBName updateAttr = this.m_tableSourceNameList;
/*  242 */       if (updateAttr.isArrayDimension())
/*      */       {
/*  244 */         for (int keyIndex = 0; keyIndex < this.m_keyColumns.size(); keyIndex++)
/*      */         {
/*  246 */           CBTableColumn currentKeyColumn = (CBTableColumn)this.m_keyColumns.get(keyIndex);
/*      */           
/*  248 */           CBName arrayCBName = currentKeyColumn.getSourceName().getParentName();
/*  249 */           if (updateAttr.isEqual(arrayCBName))
/*      */           {
/*      */ 
/*  252 */             indexValue = this.m_currentRowData.get(currentKeyColumn.getSelectAlias()).asInt();
/*  253 */             isAttrArray = true;
/*      */           }
/*      */         }
/*  256 */         updateAttr = updateAttr.getParentName();
/*      */       }
/*      */       
/*  259 */       while (null != updateAttr.getParentName())
/*      */       {
/*  261 */         if (updateAttr.isAttribute())
/*      */         {
/*  263 */           if ((srcKey.length() != 0) && (srcKey.charAt(0) == '`'))
/*      */           {
/*  265 */             srcKey = "." + srcKey;
/*      */           }
/*  267 */           srcKey = "`" + updateAttr.getAsAttribute().getUnquotedName() + "`" + srcKey;
/*      */         }
/*  269 */         else if (updateAttr.isArrayDimension())
/*      */         {
/*  271 */           for (int keyIndex = 0; keyIndex < this.m_keyColumns.size(); keyIndex++)
/*      */           {
/*  273 */             CBTableColumn currentKeyCol = (CBTableColumn)this.m_keyColumns.get(keyIndex);
/*      */             
/*  275 */             CBName arrayCBName = currentKeyCol.getSourceName().getParentName();
/*  276 */             if (updateAttr.isEqual(arrayCBName))
/*      */             {
/*  278 */               Iterator<Map.Entry<CBTableColumn, String>> detectArrayItr = this.m_detectArrayAliasMap.entrySet().iterator();
/*      */               
/*  280 */               while (detectArrayItr.hasNext())
/*      */               {
/*  282 */                 Map.Entry<CBTableColumn, String> nextPair = (Map.Entry)detectArrayItr.next();
/*  283 */                 JsonNode checkingNode = this.m_currentRowData.get((String)nextPair.getValue());
/*      */                 
/*  285 */                 isSkipRow |= !checkingNode.asBoolean();
/*      */               }
/*      */               
/*  288 */               if ((srcKey.length() != 0) && (srcKey.charAt(0) == '`'))
/*      */               {
/*  290 */                 srcKey = "." + srcKey;
/*      */               }
/*  292 */               srcKey = "[" + this.m_currentRowData.get(currentKeyCol.getSelectAlias()) + "]" + srcKey;
/*      */             }
/*      */           }
/*      */         }
/*  296 */         updateAttr = updateAttr.getParentName();
/*      */       }
/*  298 */       if (!isSkipRow)
/*      */       {
/*      */ 
/*  301 */         List<Integer> indexList = (List)((Map)this.m_deleteInfoMap.get(pk)).get(srcKey.toString());
/*  302 */         if ((null != indexList) && (isAttrArray))
/*      */         {
/*  304 */           indexList.add(Integer.valueOf(indexValue));
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*  309 */         else if (isAttrArray)
/*      */         {
/*  311 */           indexList = new ArrayList();
/*  312 */           indexList.add(Integer.valueOf(indexValue));
/*  313 */           ((Map)this.m_deleteInfoMap.get(pk)).put(srcKey.toString(), indexList);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  320 */       this.m_deleteInfoMap.put(pk, null);
/*      */     }
/*  322 */     this.m_dmlType = DSIExtJResultSet.DMLType.DELETE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean detectArrayDimension()
/*      */   {
/*  334 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  335 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDMLResultSet()
/*      */   {
/*  344 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  345 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onFinishDMLBatch()
/*      */     throws ErrorException
/*      */   {
/*  359 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     
/*  361 */     if (null == this.m_dmlType)
/*      */     {
/*  363 */       return;
/*      */     }
/*  365 */     switch (this.m_dmlType)
/*      */     {
/*      */ 
/*      */     case DELETE: 
/*  369 */       if (this.m_isVirtualTable)
/*      */       {
/*  371 */         StringBuilder deleteQueryBuilder = new StringBuilder();
/*  372 */         Iterator<Map.Entry<String, Map<String, List<Integer>>>> deleteMapIte = this.m_deleteInfoMap.entrySet().iterator();
/*      */         
/*  374 */         while (deleteMapIte.hasNext())
/*      */         {
/*  376 */           Map.Entry<String, Map<String, List<Integer>>> pkPair = (Map.Entry)deleteMapIte.next();
/*      */           
/*  378 */           String unsetListStr = "";
/*  379 */           String setListStr = "";
/*  380 */           String PKValue = (String)pkPair.getKey();
/*  381 */           Map<String, List<Integer>> indexMap = (Map)pkPair.getValue();
/*  382 */           Iterator<Map.Entry<String, List<Integer>>> idxItr = indexMap.entrySet().iterator();
/*      */           
/*  384 */           while (idxItr.hasNext())
/*      */           {
/*  386 */             Map.Entry<String, List<Integer>> idxPair = (Map.Entry)idxItr.next();
/*  387 */             List<Integer> indexList = (List)idxPair.getValue();
/*  388 */             if (null == indexList)
/*      */             {
/*      */ 
/*  391 */               if (unsetListStr.length() != 0)
/*      */               {
/*  393 */                 unsetListStr = unsetListStr + ", ";
/*      */               }
/*  395 */               unsetListStr = unsetListStr + (String)idxPair.getKey();
/*      */             }
/*      */             else
/*      */             {
/*  399 */               int listSize = indexList.size();
/*      */               
/*  401 */               if (setListStr.length() != 0)
/*      */               {
/*  403 */                 setListStr = setListStr + ", ";
/*      */               }
/*  405 */               boolean isDeleteAll = true;
/*      */               
/*  407 */               for (int idx = 0; idx < listSize; idx++)
/*      */               {
/*  409 */                 if (((Integer)indexList.get(idx)).intValue() != idx)
/*      */                 {
/*  411 */                   isDeleteAll = false;
/*  412 */                   break;
/*      */                 }
/*      */               }
/*  415 */               if (isDeleteAll)
/*      */               {
/*  417 */                 setListStr = setListStr + (String)idxPair.getKey() + " = " + (String)idxPair.getKey() + "[" + indexList.size() + ":];";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*  428 */                 String spliceStr = "";
/*  429 */                 List<Pair<Integer, Integer>> spliceIdxList = new ArrayList();
/*      */                 
/*  431 */                 if (listSize == 1)
/*      */                 {
/*  433 */                   int startIdx = ((Integer)indexList.get(0)).intValue();
/*  434 */                   spliceIdxList.add(new Pair(Integer.valueOf(startIdx), Integer.valueOf(startIdx + 1)));
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*  439 */                   for (int idx = 0; idx < listSize; idx++)
/*      */                   {
/*  441 */                     if (idx == 0)
/*      */                     {
/*      */ 
/*  444 */                       spliceIdxList.add(new Pair(Integer.valueOf(0), indexList.get(0)));
/*      */                     }
/*      */                     
/*  447 */                     if (idx < listSize - 1)
/*      */                     {
/*  449 */                       int startIdx = ((Integer)indexList.get(idx)).intValue();
/*  450 */                       int endIdx = ((Integer)indexList.get(idx + 1)).intValue();
/*  451 */                       if ((startIdx + 1 == endIdx) && (endIdx - 1 == startIdx))
/*      */                       {
/*  453 */                         if (idx == listSize - 2)
/*      */                         {
/*      */ 
/*  456 */                           spliceIdxList.add(new Pair(Integer.valueOf(endIdx + 1), Integer.valueOf(-1)));
/*      */ 
/*      */                         }
/*      */                         
/*      */ 
/*      */ 
/*      */                       }
/*      */                       else
/*      */                       {
/*  465 */                         spliceIdxList.add(new Pair(Integer.valueOf(startIdx + 1), Integer.valueOf(endIdx)));
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*  472 */                 for (int spliceIdx = 0; spliceIdx < spliceIdxList.size(); spliceIdx++)
/*      */                 {
/*  474 */                   Pair<Integer, Integer> spliceItr = (Pair)spliceIdxList.get(spliceIdx);
/*  475 */                   if (spliceStr.length() != 0)
/*      */                   {
/*  477 */                     spliceStr = "ARRAY_CONCAT(" + spliceStr + ", " + (String)idxPair.getKey() + "[" + spliceItr.key() + ":" + spliceItr.value() + "])";
/*      */                     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  488 */                     if (spliceIdx == spliceIdxList.size() - 1)
/*      */                     {
/*      */ 
/*  491 */                       spliceStr = "ARRAY_CONCAT(" + spliceStr + ", " + (String)idxPair.getKey() + "[" + (((Integer)spliceItr.value()).intValue() + 1) + ":])";
/*      */ 
/*      */ 
/*      */                     }
/*      */                     
/*      */ 
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/*      */ 
/*      */ 
/*  503 */                     String leftParam = "";
/*  504 */                     String rightParam = "";
/*      */                     
/*  506 */                     if (spliceIdxList.size() > 1)
/*      */                     {
/*  508 */                       leftParam = (String)idxPair.getKey() + "[" + spliceItr.key() + ":" + spliceItr.value() + "]";
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  516 */                       spliceIdx++;
/*  517 */                       spliceItr = (Pair)spliceIdxList.get(spliceIdx);
/*      */                       
/*  519 */                       if (((Integer)spliceItr.value()).intValue() == -1)
/*      */                       {
/*  521 */                         rightParam = (String)idxPair.getKey() + "[" + spliceItr.key() + ":]";
/*      */ 
/*      */ 
/*      */                       }
/*      */                       else
/*      */                       {
/*      */ 
/*      */ 
/*  529 */                         rightParam = (String)idxPair.getKey() + "[" + spliceItr.key() + ":" + spliceItr.value() + "]";
/*      */                       }
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  539 */                       spliceStr = "ARRAY_CONCAT(" + leftParam + ", " + rightParam + ")";
/*      */ 
/*      */ 
/*      */ 
/*      */                     }
/*  544 */                     else if (0 == ((Integer)spliceItr.key()).intValue())
/*      */                     {
/*      */ 
/*  547 */                       spliceStr = (String)idxPair.getKey() + "[0:" + spliceItr.value() + "]";
/*      */ 
/*      */ 
/*      */ 
/*      */                     }
/*      */                     else
/*      */                     {
/*      */ 
/*      */ 
/*  556 */                       leftParam = (String)idxPair.getKey() + "[0:" + spliceItr.key() + "]";
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  562 */                       rightParam = (String)idxPair.getKey() + "[" + spliceItr.value() + ":]";
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  568 */                       spliceStr = "ARRAY_CONCAT(" + leftParam + ", " + rightParam + ")";
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 
/*  573 */                 setListStr = setListStr + (String)idxPair.getKey() + " = " + spliceStr;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  578 */           deleteQueryBuilder.append("UPDATE `").append(this.m_schemaName).append("` USE KEYS '").append(PKValue).append("'");
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  585 */           if (setListStr.length() != 0)
/*      */           {
/*  587 */             deleteQueryBuilder.append(" SET ").append(setListStr);
/*      */           }
/*      */           
/*      */ 
/*  591 */           if (unsetListStr.length() != 0)
/*      */           {
/*  593 */             deleteQueryBuilder.append(" UNSET ").append(unsetListStr);
/*      */           }
/*      */           
/*      */ 
/*  597 */           this.m_dmlQuery = deleteQueryBuilder.toString();
/*  598 */           execute();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  603 */         StringBuilder deleteQueryBuilder = new StringBuilder();
/*  604 */         deleteQueryBuilder.append("DELETE FROM `").append(this.m_schemaName).append("`");
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  610 */         deleteQueryBuilder.append(" USE KEYS [");
/*  611 */         Iterator<Map.Entry<String, Map<String, List<Integer>>>> delPKItr = this.m_deleteInfoMap.entrySet().iterator();
/*      */         
/*  613 */         boolean comma = false;
/*  614 */         while (delPKItr.hasNext())
/*      */         {
/*  616 */           Map.Entry<String, Map<String, List<Integer>>> pkInfo = (Map.Entry)delPKItr.next();
/*  617 */           if (comma)
/*      */           {
/*  619 */             deleteQueryBuilder.append(", ");
/*      */           }
/*  621 */           comma = true;
/*  622 */           deleteQueryBuilder.append("'").append((String)pkInfo.getKey()).append("'");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  627 */         deleteQueryBuilder.append("]");
/*  628 */         this.m_dmlQuery = deleteQueryBuilder.toString();
/*  629 */         execute();
/*      */       }
/*  631 */       break;
/*      */     
/*      */ 
/*      */     case INSERT: 
/*  635 */       StringBuilder insertQueryBuilder = new StringBuilder();
/*  636 */       for (int dataCacheIdx = 0; dataCacheIdx < this.m_dataCache.size(); dataCacheIdx++)
/*      */       {
/*  638 */         Pair<String, List<CachedDataInfo>> cachePair = (Pair)this.m_dataCache.get(dataCacheIdx);
/*      */         
/*  640 */         if (this.m_isVirtualTable)
/*      */         {
/*  642 */           String setKey = ((CachedDataInfo)((List)cachePair.value()).get(0)).getKey();
/*  643 */           insertQueryBuilder.append("UPDATE `").append(this.m_schemaName).append("` USE KEYS ").append((String)cachePair.key()).append(" SET ").append(setKey).append(" = ARRAY_CONCAT(IFMISSINGORNULL(").append(setKey).append(", []) , ").append(((CachedDataInfo)((List)cachePair.value()).get(0)).getValue()).append(") RETURNING META(`").append(this.m_schemaName).append("`).id AS ").append("PK");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  661 */           insertQueryBuilder.append("INSERT INTO `").append(this.m_schemaName).append("` (KEY, VALUE) VALUES (").append((String)cachePair.key()).append(", ").append(((CachedDataInfo)((List)cachePair.value()).get(0)).getValue()).append(") RETURNING META(`").append(this.m_schemaName).append("`).id AS ").append("PK");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  674 */       this.m_dmlQuery = insertQueryBuilder.toString();
/*  675 */       execute();
/*  676 */       break;
/*      */     
/*      */ 
/*      */     case UPDATE: 
/*  680 */       for (int dataCacheIdx = 0; dataCacheIdx < this.m_dataCache.size(); dataCacheIdx++)
/*      */       {
/*      */ 
/*  683 */         Pair<String, List<CachedDataInfo>> cachePair = (Pair)this.m_dataCache.get(dataCacheIdx);
/*  684 */         StringBuilder updateQueryBuilder = new StringBuilder();
/*  685 */         updateQueryBuilder.append("UPDATE `").append(this.m_schemaName).append("` USE KEYS '").append((String)cachePair.key()).append("' SET ");
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  692 */         List<CachedDataInfo> currentValueList = (List)cachePair.value();
/*  693 */         StringBuilder setListBuilder = new StringBuilder();
/*  694 */         for (int valueIdx = 0; valueIdx < currentValueList.size(); valueIdx++)
/*      */         {
/*  696 */           CachedDataInfo currentCacheValue = (CachedDataInfo)currentValueList.get(valueIdx);
/*  697 */           if (setListBuilder.length() != 0)
/*      */           {
/*  699 */             setListBuilder.append(", ");
/*      */           }
/*  701 */           setListBuilder.append(currentCacheValue.getKey()).append(" = ");
/*      */           
/*      */ 
/*  704 */           if (currentCacheValue.isJson)
/*      */           {
/*  706 */             setListBuilder.append(currentCacheValue.getValue());
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  711 */             setListBuilder.append(currentCacheValue.getValue());
/*      */           }
/*      */         }
/*      */         
/*  715 */         updateQueryBuilder.append(setListBuilder);
/*      */         
/*  717 */         this.m_dmlQuery = updateQueryBuilder.toString();
/*  718 */         execute();
/*      */       }
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onFinishRowUpdate()
/*      */     throws ErrorException
/*      */   {
/*  733 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     
/*  735 */     if (null == this.m_dmlType)
/*      */     {
/*  737 */       return;
/*      */     }
/*  739 */     switch (this.m_dmlType)
/*      */     {
/*      */ 
/*      */     case INSERT: 
/*  743 */       String primaryKey = null;
/*  744 */       Stack<String> srcNameStack = new Stack();
/*  745 */       ObjectMapper mapper = new ObjectMapper();
/*  746 */       List<CachedDataInfo> insertDataList = new ArrayList();
/*      */       
/*  748 */       if (this.m_isVirtualTable)
/*      */       {
/*  750 */         ArrayNode mainArrayNode = mapper.createArrayNode();
/*      */         
/*  752 */         ObjectNode mainObjectNode = mapper.createObjectNode();
/*  753 */         List<CachedDataInfo> cachedIdxList = new ArrayList();
/*  754 */         for (int rowIndex = 0; rowIndex < this.m_rowCache.size(); rowIndex++)
/*      */         {
/*  756 */           CachedDataInfo currentCacheData = (CachedDataInfo)this.m_rowCache.get(rowIndex);
/*      */           
/*  758 */           if ((currentCacheData.getSourceName().isSpecialIdentifier()) && (currentCacheData.getSourceName().getParentName().isAttribute()))
/*      */           {
/*      */ 
/*      */ 
/*  762 */             primaryKey = ((CachedDataInfo)this.m_rowCache.get(rowIndex)).getValue();
/*      */           }
/*  764 */           else if ((currentCacheData.getSourceName().isSpecialIdentifier()) && (currentCacheData.getSourceName().getParentName().isArrayDimension()))
/*      */           {
/*      */ 
/*  767 */             cachedIdxList.add(currentCacheData);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  773 */           else if (null != currentCacheData.getSourceName().getParentName())
/*      */           {
/*  775 */             CBName srcName = currentCacheData.getSourceName();
/*  776 */             if (srcName == this.m_tableSourceNameList)
/*      */             {
/*  778 */               setNodeValue(mainArrayNode, currentCacheData);
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  783 */               while (!srcName.isEqual(this.m_tableSourceNameList))
/*      */               {
/*  785 */                 if (srcName.isAttribute())
/*      */                 {
/*  787 */                   srcNameStack.push(srcName.getAsAttribute().getUnquotedName());
/*      */                 }
/*  789 */                 srcName = srcName.getParentName();
/*      */               }
/*      */               
/*  792 */               ObjectNode currentObjPtr = mainObjectNode;
/*      */               
/*  794 */               while (!srcNameStack.empty())
/*      */               {
/*  796 */                 String curSrcName = (String)srcNameStack.pop();
/*      */                 
/*  798 */                 if (srcNameStack.empty())
/*      */                 {
/*      */ 
/*  801 */                   currentCacheData.setKey(curSrcName);
/*  802 */                   if (!mainObjectNode.has(curSrcName))
/*      */                   {
/*  804 */                     setObjectNodeValue(currentObjPtr, currentCacheData);
/*  805 */                     break;
/*      */ 
/*      */                   }
/*      */                   
/*      */ 
/*      */                 }
/*  811 */                 else if (!currentObjPtr.has(curSrcName))
/*      */                 {
/*      */ 
/*  814 */                   currentObjPtr = mapper.createObjectNode();
/*  815 */                   mainObjectNode.put(curSrcName, currentObjPtr);
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*  820 */                   currentObjPtr = (ObjectNode)mainObjectNode.get(curSrcName);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  829 */         if (mainObjectNode.size() > 0)
/*      */         {
/*  831 */           mainArrayNode.add(mainObjectNode);
/*      */         }
/*      */         
/*  834 */         String setKey = "";
/*  835 */         CBName updateAttr = this.m_tableSourceNameList;
/*  836 */         if (updateAttr.isArrayDimension())
/*      */         {
/*      */ 
/*  839 */           updateAttr = updateAttr.getParentName();
/*      */         }
/*  841 */         while (null != updateAttr.getParentName())
/*      */         {
/*  843 */           if (updateAttr.isAttribute())
/*      */           {
/*  845 */             if ((setKey.length() != 0) && (setKey.charAt(0) == '`'))
/*      */             {
/*  847 */               setKey = "." + setKey;
/*      */             }
/*  849 */             setKey = "`" + updateAttr.getAsAttribute().getUnquotedName() + "`" + setKey;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  855 */           else if (updateAttr.isArrayDimension())
/*      */           {
/*  857 */             for (int rowIndex = 0; rowIndex < cachedIdxList.size(); rowIndex++)
/*      */             {
/*      */ 
/*  860 */               CachedDataInfo currentIdx = (CachedDataInfo)cachedIdxList.get(rowIndex);
/*  861 */               CBName cacheCBName = currentIdx.getSourceName().getParentName();
/*  862 */               if (updateAttr.isEqual(cacheCBName))
/*      */               {
/*  864 */                 if ((null == currentIdx.getValue()) || (currentIdx.getValue().length() == 0))
/*      */                 {
/*      */ 
/*  867 */                   ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_MISSING_COLUMN.name(), new String[0]);
/*      */                   
/*      */ 
/*  870 */                   throw err;
/*      */                 }
/*  872 */                 if ((setKey.length() != 0) && (setKey.charAt(0) == '`'))
/*      */                 {
/*  874 */                   setKey = "." + setKey;
/*      */                 }
/*  876 */                 setKey = "[" + currentIdx.getValue() + "]" + setKey;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  884 */           updateAttr = updateAttr.getParentName();
/*      */         }
/*      */         
/*      */ 
/*  888 */         if (null == primaryKey)
/*      */         {
/*  890 */           ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_MISSING_PRIMARYKEY.name(), new String[0]);
/*      */           
/*      */ 
/*  893 */           throw err;
/*      */         }
/*      */         
/*  896 */         CachedDataInfo insertCache = new CachedDataInfo(null);
/*  897 */         insertCache.setKey(setKey.toString());
/*  898 */         insertCache.setValue(mainArrayNode.toString());
/*  899 */         insertDataList.add(insertCache);
/*      */         
/*  901 */         this.m_dataCache.add(new Pair(primaryKey, insertDataList));
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  907 */         ObjectNode mainObjectNode = mapper.createObjectNode();
/*      */         
/*  909 */         boolean isTypeSelected = false;
/*      */         
/*  911 */         for (int rowIndex = 0; rowIndex < this.m_rowCache.size(); rowIndex++)
/*      */         {
/*  913 */           CachedDataInfo currentCacheData = (CachedDataInfo)this.m_rowCache.get(rowIndex);
/*  914 */           if ((currentCacheData.getSourceName().isSpecialIdentifier()) && (currentCacheData.getSourceName().getParentName().isAttribute()))
/*      */           {
/*      */ 
/*      */ 
/*  918 */             primaryKey = ((CachedDataInfo)this.m_rowCache.get(rowIndex)).getValue();
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  923 */           else if (null != currentCacheData.getSourceName().getParentName())
/*      */           {
/*  925 */             CBName srcName = currentCacheData.getSourceName();
/*  926 */             while (null != srcName)
/*      */             {
/*  928 */               if (srcName.isAttribute())
/*      */               {
/*  930 */                 srcNameStack.push(srcName.getAsAttribute().getUnquotedName());
/*      */               }
/*  932 */               srcName = srcName.getParentName();
/*      */             }
/*      */             
/*  935 */             srcNameStack.pop();
/*      */             
/*  937 */             ObjectNode currentObjPtr = mainObjectNode;
/*  938 */             while (!srcNameStack.empty())
/*      */             {
/*  940 */               String curSrcName = (String)srcNameStack.pop();
/*      */               
/*  942 */               if (srcNameStack.empty())
/*      */               {
/*  944 */                 currentCacheData.setKey(curSrcName);
/*  945 */                 if (currentObjPtr.has(curSrcName))
/*      */                   break;
/*  947 */                 if ((null != this.m_tableDifferentiatorPair) && (curSrcName.equals(((CBTableColumn)this.m_tableDifferentiatorPair.key()).getColumnDSIIName())))
/*      */                 {
/*      */ 
/*  950 */                   if (!currentCacheData.getValueNoQuote().equals(this.m_tableDifferentiatorPair.value())) {
/*      */                     break;
/*      */                   }
/*  953 */                   setObjectNodeValue(currentObjPtr, currentCacheData);
/*  954 */                   isTypeSelected = true; break;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*  959 */                 setObjectNodeValue(currentObjPtr, currentCacheData); break;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  967 */               if (!currentObjPtr.has(curSrcName))
/*      */               {
/*      */ 
/*  970 */                 currentObjPtr = mapper.createObjectNode();
/*  971 */                 mainObjectNode.put(curSrcName, currentObjPtr);
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*  976 */                 currentObjPtr = (ObjectNode)mainObjectNode.get(curSrcName);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  984 */         if (null == primaryKey)
/*      */         {
/*  986 */           ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_MISSING_PRIMARYKEY.name(), new String[0]);
/*      */           
/*      */ 
/*  989 */           throw err;
/*      */         }
/*  991 */         if ((null != this.m_tableDifferentiatorPair) && (!isTypeSelected))
/*      */         {
/*  993 */           mainObjectNode.put(((CBTableColumn)this.m_tableDifferentiatorPair.key()).getColumnDSIIName(), (String)this.m_tableDifferentiatorPair.value());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  999 */         CachedDataInfo insertCache = new CachedDataInfo(null);
/* 1000 */         insertCache.setKey("VALUE");
/* 1001 */         insertCache.setValue(mainObjectNode.toString());
/* 1002 */         insertDataList.add(insertCache);
/*      */         
/* 1004 */         this.m_dataCache.add(new Pair(primaryKey, insertDataList));
/*      */       }
/*      */       
/* 1007 */       break;
/*      */     
/*      */ 
/*      */     case UPDATE: 
/* 1011 */       List<CachedDataInfo> cachedIdxList = new ArrayList();
/* 1012 */       boolean isSkipRow = false;
/* 1013 */       Pair<String, List<CachedDataInfo>> setValuePair = null;
/* 1014 */       for (int idx = 0; idx < this.m_keyColumns.size(); idx++)
/*      */       {
/* 1016 */         CBTableColumn currentKeyCol = (CBTableColumn)this.m_keyColumns.get(idx);
/* 1017 */         CBName srcName = currentKeyCol.getSourceName();
/* 1018 */         if ((srcName.isSpecialIdentifier()) && (srcName.getParentName().isAttribute()))
/*      */         {
/*      */ 
/*      */ 
/* 1022 */           String pkAlias = currentKeyCol.getRowDocumentAlias();
/* 1023 */           String pkValue = this.m_currentRowData.get(pkAlias).textValue();
/* 1024 */           setValuePair = new Pair(pkValue, new ArrayList());
/*      */ 
/*      */ 
/*      */         }
/* 1028 */         else if ((srcName.isSpecialIdentifier()) && (srcName.getParentName().isArrayDimension()))
/*      */         {
/*      */ 
/* 1031 */           Iterator<Map.Entry<CBTableColumn, String>> detectArrayItr = this.m_detectArrayAliasMap.entrySet().iterator();
/*      */           
/* 1033 */           while (detectArrayItr.hasNext())
/*      */           {
/* 1035 */             Map.Entry<CBTableColumn, String> nextPair = (Map.Entry)detectArrayItr.next();
/* 1036 */             JsonNode checkingNode = this.m_currentRowData.get((String)nextPair.getValue());
/*      */             
/* 1038 */             isSkipRow |= !checkingNode.asBoolean();
/*      */           }
/*      */           
/*      */ 
/* 1042 */           String idxFieldName = currentKeyCol.getSelectAlias();
/* 1043 */           String index = this.m_currentRowData.get(idxFieldName).asText();
/*      */           
/* 1045 */           CachedDataInfo newCache = new CachedDataInfo(null);
/* 1046 */           newCache.setKey(currentKeyCol.getColumnDSIIName());
/* 1047 */           newCache.setValue(index);
/* 1048 */           newCache.setSQLType(4);
/* 1049 */           newCache.setSourceName(currentKeyCol.getSourceName());
/* 1050 */           cachedIdxList.add(newCache);
/*      */         }
/*      */       }
/*      */       
/* 1054 */       if (!isSkipRow)
/*      */       {
/*      */ 
/* 1057 */         for (int cacheIdx = 0; cacheIdx < this.m_rowCache.size(); cacheIdx++)
/*      */         {
/*      */ 
/* 1060 */           Stack<CBName> srcNameStack = new Stack();
/* 1061 */           String setKey = "";
/* 1062 */           CachedDataInfo curCache = (CachedDataInfo)this.m_rowCache.get(cacheIdx);
/* 1063 */           CBName updateAttr = curCache.getSourceName();
/* 1064 */           boolean foundTableSrc = updateAttr.isEqual(this.m_tableSourceNameList);
/* 1065 */           while (null != updateAttr.getParentName())
/*      */           {
/* 1067 */             if (updateAttr.isEqual(this.m_tableSourceNameList))
/*      */             {
/* 1069 */               foundTableSrc = true;
/*      */             }
/* 1071 */             if (foundTableSrc)
/*      */             {
/*      */ 
/* 1074 */               if (updateAttr.isAttribute())
/*      */               {
/* 1076 */                 if ((setKey.length() != 0) && (setKey.charAt(0) == '`'))
/*      */                 {
/* 1078 */                   setKey = "." + setKey;
/*      */                 }
/* 1080 */                 setKey = "`" + updateAttr.getAsAttribute().getUnquotedName() + "`" + setKey;
/*      */               }
/* 1082 */               else if (updateAttr.isArrayDimension())
/*      */               {
/* 1084 */                 for (int idx = 0; idx < cachedIdxList.size(); idx++)
/*      */                 {
/* 1086 */                   CachedDataInfo currentCache = (CachedDataInfo)cachedIdxList.get(idx);
/* 1087 */                   if (updateAttr.isEqual(currentCache.getSourceName().getParentName()))
/*      */                   {
/*      */ 
/* 1090 */                     if ((setKey.length() != 0) && (setKey.charAt(0) == '`'))
/*      */                     {
/* 1092 */                       setKey = "." + setKey;
/*      */                     }
/* 1094 */                     setKey = "[" + currentCache.getValue() + "]" + setKey;
/*      */                   }
/*      */                   
/*      */                 }
/*      */               }
/*      */             }
/*      */             else {
/* 1101 */               srcNameStack.push(updateAttr);
/*      */             }
/* 1103 */             updateAttr = updateAttr.getParentName();
/*      */           }
/*      */           
/* 1106 */           if (!srcNameStack.empty())
/*      */           {
/* 1108 */             boolean isGenerateJson = (srcNameStack.size() > 1) || (this.m_isVirtualTable);
/*      */             
/* 1110 */             if (!isGenerateJson)
/*      */             {
/*      */ 
/* 1113 */               String setKeyCache = setKey.toString();
/* 1114 */               if (setKey.length() != 0)
/*      */               {
/* 1116 */                 setKey = setKey + ".";
/*      */               }
/* 1118 */               setKey = "`" + ((CBName)srcNameStack.pop()).getAsAttribute().getUnquotedName() + "`";
/* 1119 */               curCache.setKey(setKey);
/*      */             }
/*      */             else
/*      */             {
/* 1123 */               CBName srcNameCursor = (CBName)srcNameStack.pop();
/* 1124 */               String lookUpKey = null;
/* 1125 */               if (this.m_isVirtualTable)
/*      */               {
/* 1127 */                 lookUpKey = setKey;
/*      */ 
/*      */ 
/*      */               }
/* 1131 */               else if (srcNameCursor.isAttribute())
/*      */               {
/* 1133 */                 lookUpKey = "`" + srcNameCursor.getAsAttribute().getUnquotedName() + "`";
/*      */               }
/*      */               
/*      */ 
/* 1137 */               if (0 == srcNameStack.size())
/*      */               {
/* 1139 */                 String updateItemName = srcNameCursor.getAsAttribute().getUnquotedName();
/* 1140 */                 curCache.setKey(setKey + "." + updateItemName);
/*      */ 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/* 1147 */                 while ((0 < srcNameStack.size()) && 
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1152 */                   (!srcNameStack.empty())) {}
/*      */               }
/*      */               
/*      */             }
/*      */             
/*      */           }
/* 1158 */           else if (setKey.length() != 0)
/*      */           {
/* 1160 */             curCache.setKey(setKey);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1165 */         ((List)setValuePair.value()).addAll(0, this.m_rowCache);
/* 1166 */         this.m_dataCache.add(setValuePair);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1172 */         this.m_dataCache.remove(0);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onStartRowUpdate()
/*      */   {
/* 1188 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 1189 */     this.m_dmlType = DSIExtJResultSet.DMLType.UPDATE;
/* 1190 */     this.m_rowCache.clear();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean shouldSelectRowSourceValue()
/*      */   {
/* 1201 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 1202 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean writeData(int column, DataWrapper data, long offset, boolean isDefault)
/*      */     throws ErrorException
/*      */   {
/* 1230 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     
/* 1232 */     CBTableColumn tempCol = (CBTableColumn)this.m_tableColumns.get(column);
/*      */     
/* 1234 */     switch (this.m_dmlType)
/*      */     {
/*      */ 
/*      */     case INSERT: 
/*      */       try
/*      */       {
/* 1240 */         if (!isDefault)
/*      */         {
/* 1242 */           CachedDataInfo cacheValue = new CachedDataInfo(null);
/* 1243 */           cacheValue.setKey(tempCol.getColumnDSIIName());
/* 1244 */           cacheValue.setSourceName(tempCol.getSourceName());
/* 1245 */           if (cacheValue.getSourceName().isSpecialIdentifier())
/*      */           {
/* 1247 */             if (this.m_keyColumns.isEmpty())
/*      */             {
/*      */ 
/* 1250 */               return false;
/*      */             }
/*      */             
/*      */ 
/* 1254 */             int keyIndex = 0; if (keyIndex < this.m_keyColumns.size())
/*      */             {
/* 1256 */               if (tempCol == this.m_keyColumns.get(keyIndex))
/*      */               {
/*      */ 
/*      */ 
/* 1260 */                 this.m_keyColumns.remove(keyIndex);
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1265 */                 return false;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1271 */           if (null == data)
/*      */           {
/* 1273 */             cacheValue.setSQLType(0);
/*      */           }
/*      */           else
/*      */           {
/* 1277 */             cacheValue.setSQLType(tempCol.getTypeMetadata().getType());
/*      */           }
/*      */           
/* 1280 */           cacheValue.setValue(setCacheValue(data));
/* 1281 */           cacheValue.setName(tempCol.getName());
/* 1282 */           this.m_rowCache.add(cacheValue);
/*      */         }
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 1287 */         ex.printStackTrace();
/*      */       }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case UPDATE: 
/* 1294 */       String colName = tempCol.getName();
/* 1295 */       if (Updatable.READ_ONLY == tempCol.getUpdatable())
/*      */       {
/* 1297 */         ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_READONLY_COLUMN.name(), new String[0]);
/*      */         
/*      */ 
/* 1300 */         throw err;
/*      */       }
/* 1302 */       CachedDataInfo newCache = new CachedDataInfo(null);
/* 1303 */       newCache.setKey(colName);
/* 1304 */       newCache.setValue(setCacheValue(data));
/* 1305 */       newCache.setIsJson(false);
/* 1306 */       newCache.setSourceName(tempCol.getSourceName());
/* 1307 */       if (null == data)
/*      */       {
/* 1309 */         newCache.setSQLType(0);
/*      */       }
/*      */       else
/*      */       {
/* 1313 */         newCache.setSQLType(tempCol.getTypeMetadata().getType());
/*      */       }
/* 1315 */       this.m_rowCache.add(newCache);
/* 1316 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1323 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void execute()
/*      */     throws ErrorException
/*      */   {
/* 1332 */     N1QLRowCountSet dmlResult = this.m_client.executeUpdate(this.m_dmlQuery, null, null);
/*      */     
/*      */ 
/*      */ 
/* 1336 */     if (DSIExtJResultSet.DMLType.INSERT == this.m_dmlType)
/*      */     {
/* 1338 */       ArrayList<N1QLQueryRow> returnData = dmlResult.allRowsRawData();
/* 1339 */       if ((null == returnData) || (returnData.size() == 0))
/*      */       {
/* 1341 */         ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_FAIL_WRITEBACK.name(), new String[0]);
/*      */         
/*      */ 
/* 1344 */         throw err;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String setCacheValue(DataWrapper data)
/*      */   {
/* 1356 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     try
/*      */     {
/* 1359 */       int type = data.getType();
/* 1360 */       switch (type)
/*      */       {
/*      */ 
/*      */       case -8: 
/*      */       case 1: 
/*      */       case 12: 
/* 1366 */         return "'" + data.getVarChar() + "'";
/*      */       
/*      */ 
/*      */       case 16: 
/* 1370 */         Boolean result = data.getBoolean();
/* 1371 */         if (result.booleanValue())
/*      */         {
/* 1373 */           return "true";
/*      */         }
/*      */         
/*      */ 
/* 1377 */         return "false";
/*      */       
/*      */ 
/*      */ 
/*      */       case 4: 
/* 1382 */         return String.valueOf(data.getInteger());
/*      */       
/*      */ 
/*      */       case -5: 
/* 1386 */         return String.valueOf(data.getBigInt());
/*      */       
/*      */ 
/*      */       case 3: 
/*      */       case 8: 
/* 1391 */         return String.valueOf(data.getDouble());
/*      */       
/*      */ 
/*      */       case 93: 
/* 1395 */         return String.valueOf(data.getTimestamp());
/*      */       
/*      */ 
/*      */       case 0: 
/* 1399 */         return "NULL";
/*      */       }
/*      */       
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1405 */       LogUtilities.logError(ex, this.m_log);
/*      */     }
/* 1407 */     return "NULL";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setNodeValue(JsonNode node, CachedDataInfo dataCache)
/*      */   {
/* 1416 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     try
/*      */     {
/* 1419 */       if ((node instanceof ObjectNode))
/*      */       {
/* 1421 */         setObjectNodeValue((ObjectNode)node, dataCache);
/*      */       }
/* 1423 */       else if ((node instanceof ArrayNode))
/*      */       {
/* 1425 */         setArrayNodeValue((ArrayNode)node, dataCache);
/*      */       }
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1430 */       LogUtilities.logError(ex, this.m_log);
/*      */     }
/*      */   }
/*      */   
/*      */   private void setObjectNodeValue(ObjectNode objectNode, CachedDataInfo dataCache)
/*      */   {
/* 1436 */     int dataType = dataCache.getSQLType();
/* 1437 */     switch (dataType)
/*      */     {
/*      */ 
/*      */     case -8: 
/*      */     case 1: 
/*      */     case 12: 
/* 1443 */       objectNode.put(dataCache.getKey(), dataCache.getValueNoQuote());
/* 1444 */       break;
/*      */     
/*      */ 
/*      */     case 16: 
/* 1448 */       objectNode.put(dataCache.getKey(), Boolean.valueOf(dataCache.getValue()));
/* 1449 */       break;
/*      */     
/*      */ 
/*      */     case 4: 
/* 1453 */       objectNode.put(dataCache.getKey(), Integer.valueOf(dataCache.getValue()));
/* 1454 */       break;
/*      */     
/*      */ 
/*      */     case -5: 
/* 1458 */       objectNode.put(dataCache.getKey(), Long.valueOf(dataCache.getValue()));
/* 1459 */       break;
/*      */     
/*      */ 
/*      */     case 3: 
/*      */     case 8: 
/* 1464 */       objectNode.put(dataCache.getKey(), Double.valueOf(dataCache.getValue()));
/* 1465 */       break;
/*      */     
/*      */ 
/*      */     case 93: 
/* 1469 */       objectNode.put(dataCache.getKey(), dataCache.getValue());
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */   private void setArrayNodeValue(ArrayNode arrayNode, CachedDataInfo dataCache)
/*      */   {
/* 1477 */     int dataType = dataCache.getSQLType();
/* 1478 */     switch (dataType)
/*      */     {
/*      */ 
/*      */     case -8: 
/*      */     case 1: 
/*      */     case 12: 
/* 1484 */       arrayNode.add(dataCache.getValueNoQuote());
/* 1485 */       break;
/*      */     
/*      */ 
/*      */     case 16: 
/* 1489 */       arrayNode.add(Boolean.valueOf(dataCache.getValue()));
/* 1490 */       break;
/*      */     
/*      */ 
/*      */     case 4: 
/* 1494 */       arrayNode.add(Integer.valueOf(dataCache.getValue()));
/* 1495 */       break;
/*      */     
/*      */ 
/*      */     case -5: 
/* 1499 */       arrayNode.add(Long.valueOf(dataCache.getValue()));
/* 1500 */       break;
/*      */     
/*      */ 
/*      */     case 3: 
/*      */     case 8: 
/* 1505 */       arrayNode.add(Double.valueOf(dataCache.getValue()));
/* 1506 */       break;
/*      */     
/*      */ 
/*      */     case 93: 
/* 1510 */       arrayNode.add(dataCache.getValueNoQuote());
/*      */     }
/*      */     
/*      */   }
/*      */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/resultset/CBWriteResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */