package com.simba.couchbase.dataengine.resultset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.simba.couchbase.client.N1QLQueryRow;
import com.simba.couchbase.client.N1QLRowCountSet;
import com.simba.couchbase.core.CBClient;
import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
import com.simba.couchbase.schemamap.metadata.CBSchemaMetadata;
import com.simba.couchbase.schemamap.metadata.CBTableMetadata;
import com.simba.couchbase.schemamap.parser.CBName;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CBWriteResultSet extends CBResultSet {

    private Map<String, Map<String, List<Integer>>> m_deleteInfoMap;
    private List<Pair<String, List<CachedDataInfo>>> m_dataCache;
    private String m_dmlQuery;
    private DSIExtJResultSet.DMLType m_dmlType;
    private ILogger m_log;
    private List<CachedDataInfo> m_rowCache;

    @SuppressWarnings("unused")
    private class CachedDataInfo {
        
        private String key;
        private String value;
        private int sqlType;
        private boolean isJson;
        private String jdbcName;
        private CBName sourceName;
        
        private CachedDataInfo() {
        }
        
        public boolean isJson() {
            return this.isJson;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getName() {
            return this.jdbcName;
        }
        
        public int getSQLType() {
            return this.sqlType;
        }
        
        public CBName getSourceName() {
            return this.sourceName;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public String getValueNoQuote() {
            return this.value.substring(1, this.value.length() - 1);
        }
        
        public void setIsJson(boolean isJson) {
            this.isJson = isJson;
        }
        
        public void setKey(String key) {
            this.key = key;
        }
        
        public void setName(String jdbcName) {
            this.jdbcName = jdbcName;
        }
        
        public void setSQLType(int sqlType) {
            this.sqlType = sqlType;
        }
        
        public void setSourceName(CBName sourceName) {
            this.sourceName = sourceName;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
    }

    
    public CBWriteResultSet(CBSchemaMetadata schemaMeta, CBTableMetadata tableMeta, ArrayList<CBColumnMetadata> columnMetadata, CBClient client, ILogger log) throws ErrorException {

        super(schemaMeta, tableMeta, columnMetadata, client, log);
        this.m_log = log;
        this.m_dataCache = new ArrayList<>();
        this.m_deleteInfoMap = new HashMap<>();
        this.m_rowCache = new ArrayList<>();
    }
    
    public void appendRow() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_dmlType = DSIExtJResultSet.DMLType.INSERT;
    }
    
    public void deleteRow() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        String pk = null;
        for (int keyIndex = 0; keyIndex < this.m_keyColumns.size(); keyIndex++) {
            CBTableColumn currentKeyColumn = (CBTableColumn)this.m_keyColumns.get(keyIndex);
            CBName keySrcName = currentKeyColumn.getSourceName();
            if ((keySrcName.isSpecialIdentifier()) && (keySrcName.getParentName().isAttribute())) {
                pk = this.m_currentRowData.get(currentKeyColumn.getSelectAlias()).asText();
            }
        }
        
        if (this.m_isVirtualTable) {
            if (null == this.m_deleteInfoMap.get(pk)) {
                this.m_deleteInfoMap.put(pk, new HashMap<>());
            }
            
            String srcKey = "";
            int indexValue = 0;
            boolean isAttrArray = false;
            boolean isSkipRow = false;
            
            CBName updateAttr = this.m_tableSourceNameList;
            if (updateAttr.isArrayDimension()) {
                for (int keyIndex = 0; keyIndex < this.m_keyColumns.size(); keyIndex++) {
                    CBTableColumn currentKeyColumn = (CBTableColumn)this.m_keyColumns.get(keyIndex);
                    CBName arrayCBName = currentKeyColumn.getSourceName().getParentName();
                    if (updateAttr.isEqual(arrayCBName)) {
                        indexValue = this.m_currentRowData.get(currentKeyColumn.getSelectAlias()).asInt();
                        isAttrArray = true;
                    }
                }
                
                updateAttr = updateAttr.getParentName();
            }
            
            while (null != updateAttr.getParentName()) {
                if (updateAttr.isAttribute()) {
                    if ((srcKey.length() != 0) && (srcKey.charAt(0) == '`')) {
                        srcKey = "." + srcKey;
                    }
                    srcKey = "`" + updateAttr.getAsAttribute().getUnquotedName() + "`" + srcKey;
                } else if (updateAttr.isArrayDimension()) {
                    
                    for (int keyIndex = 0; keyIndex < this.m_keyColumns.size(); keyIndex++) {
                        CBTableColumn currentKeyCol = (CBTableColumn)this.m_keyColumns.get(keyIndex);
                        CBName arrayCBName = currentKeyCol.getSourceName().getParentName();
                        if (updateAttr.isEqual(arrayCBName)) {
                            Iterator<Map.Entry<CBTableColumn, String>> detectArrayItr = this.m_detectArrayAliasMap.entrySet().iterator();
                            while (detectArrayItr.hasNext()) {
                                Map.Entry<CBTableColumn, String> nextPair = detectArrayItr.next();
                                JsonNode checkingNode = this.m_currentRowData.get((String)nextPair.getValue());
                                isSkipRow |= !checkingNode.asBoolean();
                            }
                            
                            if ((srcKey.length() != 0) && (srcKey.charAt(0) == '`')) {
                                srcKey = "." + srcKey;
                            }
                            
                            srcKey = "[" + this.m_currentRowData.get(currentKeyCol.getSelectAlias()) + "]" + srcKey;
                        }
                    }
                }
                
                updateAttr = updateAttr.getParentName();
            }
            
            if (!isSkipRow) {
                List<Integer> indexList = this.m_deleteInfoMap.get(pk).get(srcKey.toString());
                if ((null != indexList) && (isAttrArray)) {
                    indexList.add(Integer.valueOf(indexValue));
                } else if (isAttrArray) {
                    indexList = new ArrayList<>();
                    indexList.add(Integer.valueOf(indexValue));
                    this.m_deleteInfoMap.get(pk).put(srcKey.toString(), indexList);
                }
            }
        } else {
            this.m_deleteInfoMap.put(pk, null);
        }
        this.m_dmlType = DSIExtJResultSet.DMLType.DELETE;
    }
  
    public boolean detectArrayDimension() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return true;
    }
    
    public boolean isDMLResultSet() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return true;
    }
    
    public void onFinishDMLBatch() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        if (null == this.m_dmlType) {
            return;
        }
        
        switch (this.m_dmlType) {
        
        case DELETE:
            if (this.m_isVirtualTable) {
/*  371 */         StringBuilder deleteQueryBuilder = new StringBuilder();
/*  372 */         Iterator<Map.Entry<String, Map<String, List<Integer>>>> deleteMapIte = this.m_deleteInfoMap.entrySet().iterator();
        
/*  374 */         while (deleteMapIte.hasNext())
        {
/*  376 */           Map.Entry<String, Map<String, List<Integer>>> pkPair = (Map.Entry)deleteMapIte.next();
          
/*  378 */           String unsetListStr = "";
/*  379 */           String setListStr = "";
/*  380 */           String PKValue = (String)pkPair.getKey();
/*  381 */           Map<String, List<Integer>> indexMap = (Map)pkPair.getValue();
/*  382 */           Iterator<Map.Entry<String, List<Integer>>> idxItr = indexMap.entrySet().iterator();
          
/*  384 */           while (idxItr.hasNext())
          {
/*  386 */             Map.Entry<String, List<Integer>> idxPair = (Map.Entry)idxItr.next();
/*  387 */             List<Integer> indexList = (List)idxPair.getValue();
/*  388 */             if (null == indexList)
            {

/*  391 */               if (unsetListStr.length() != 0)
              {
/*  393 */                 unsetListStr = unsetListStr + ", ";
              }
/*  395 */               unsetListStr = unsetListStr + (String)idxPair.getKey();
            }
            else
            {
/*  399 */               int listSize = indexList.size();
              
/*  401 */               if (setListStr.length() != 0)
              {
/*  403 */                 setListStr = setListStr + ", ";
              }
/*  405 */               boolean isDeleteAll = true;
              
/*  407 */               for (int idx = 0; idx < listSize; idx++)
              {
/*  409 */                 if (((Integer)indexList.get(idx)).intValue() != idx)
                {
/*  411 */                   isDeleteAll = false;
/*  412 */                   break;
                }
              }
/*  415 */               if (isDeleteAll)
              {
/*  417 */                 setListStr = setListStr + (String)idxPair.getKey() + " = " + (String)idxPair.getKey() + "[" + indexList.size() + ":];";




              }
              else
              {



/*  428 */                 String spliceStr = "";
/*  429 */                 List<Pair<Integer, Integer>> spliceIdxList = new ArrayList();
                
/*  431 */                 if (listSize == 1)
                {
/*  433 */                   int startIdx = ((Integer)indexList.get(0)).intValue();
/*  434 */                   spliceIdxList.add(new Pair(Integer.valueOf(startIdx), Integer.valueOf(startIdx + 1)));

                }
                else
                {
/*  439 */                   for (int idx = 0; idx < listSize; idx++)
                  {
/*  441 */                     if (idx == 0)
                    {

/*  444 */                       spliceIdxList.add(new Pair(Integer.valueOf(0), indexList.get(0)));
                    }
                    
/*  447 */                     if (idx < listSize - 1)
                    {
/*  449 */                       int startIdx = ((Integer)indexList.get(idx)).intValue();
/*  450 */                       int endIdx = ((Integer)indexList.get(idx + 1)).intValue();
/*  451 */                       if ((startIdx + 1 == endIdx) && (endIdx - 1 == startIdx))
                      {
/*  453 */                         if (idx == listSize - 2)
                        {

/*  456 */                           spliceIdxList.add(new Pair(Integer.valueOf(endIdx + 1), Integer.valueOf(-1)));

                        }
                        


                      }
                      else
                      {
/*  465 */                         spliceIdxList.add(new Pair(Integer.valueOf(startIdx + 1), Integer.valueOf(endIdx)));
                      }
                    }
                  }
                }
                

/*  472 */                 for (int spliceIdx = 0; spliceIdx < spliceIdxList.size(); spliceIdx++)
                {
/*  474 */                   Pair<Integer, Integer> spliceItr = (Pair)spliceIdxList.get(spliceIdx);
/*  475 */                   if (spliceStr.length() != 0)
                  {
/*  477 */                     spliceStr = "ARRAY_CONCAT(" + spliceStr + ", " + (String)idxPair.getKey() + "[" + spliceItr.key() + ":" + spliceItr.value() + "])";
                    









/*  488 */                     if (spliceIdx == spliceIdxList.size() - 1)
                    {

/*  491 */                       spliceStr = "ARRAY_CONCAT(" + spliceStr + ", " + (String)idxPair.getKey() + "[" + (((Integer)spliceItr.value()).intValue() + 1) + ":])";


                    }
                    


                  }
                  else
                  {


/*  503 */                     String leftParam = "";
/*  504 */                     String rightParam = "";
                    
/*  506 */                     if (spliceIdxList.size() > 1)
                    {
/*  508 */                       leftParam = (String)idxPair.getKey() + "[" + spliceItr.key() + ":" + spliceItr.value() + "]";
                      






/*  516 */                       spliceIdx++;
/*  517 */                       spliceItr = (Pair)spliceIdxList.get(spliceIdx);
                      
/*  519 */                       if (((Integer)spliceItr.value()).intValue() == -1)
                      {
/*  521 */                         rightParam = (String)idxPair.getKey() + "[" + spliceItr.key() + ":]";


                      }
                      else
                      {


/*  529 */                         rightParam = (String)idxPair.getKey() + "[" + spliceItr.key() + ":" + spliceItr.value() + "]";
                      }
                      







/*  539 */                       spliceStr = "ARRAY_CONCAT(" + leftParam + ", " + rightParam + ")";



                    }
/*  544 */                     else if (0 == ((Integer)spliceItr.key()).intValue())
                    {

/*  547 */                       spliceStr = (String)idxPair.getKey() + "[0:" + spliceItr.value() + "]";



                    }
                    else
                    {


/*  556 */                       leftParam = (String)idxPair.getKey() + "[0:" + spliceItr.key() + "]";
                      




/*  562 */                       rightParam = (String)idxPair.getKey() + "[" + spliceItr.value() + ":]";
                      




/*  568 */                       spliceStr = "ARRAY_CONCAT(" + leftParam + ", " + rightParam + ")";
                    }
                  }
                }
                
/*  573 */                 setListStr = setListStr + (String)idxPair.getKey() + " = " + spliceStr;
              }
            }
          }
          
/*  578 */           deleteQueryBuilder.append("UPDATE `").append(this.m_schemaName).append("` USE KEYS '").append(PKValue).append("'");
          





/*  585 */           if (setListStr.length() != 0)
          {
/*  587 */             deleteQueryBuilder.append(" SET ").append(setListStr);
          }
          

/*  591 */           if (unsetListStr.length() != 0)
          {
/*  593 */             deleteQueryBuilder.append(" UNSET ").append(unsetListStr);
          }
          

/*  597 */           this.m_dmlQuery = deleteQueryBuilder.toString();
/*  598 */           execute();
        }
            } else {
                StringBuilder deleteQueryBuilder = new StringBuilder();
                deleteQueryBuilder.append("DELETE FROM `").append(this.m_schemaName).append("`");
                deleteQueryBuilder.append(" USE KEYS [");
                
                Iterator<Map.Entry<String, Map<String, List<Integer>>>> delPKItr = this.m_deleteInfoMap.entrySet().iterator();
                boolean comma = false;
                while (delPKItr.hasNext()) {
                    Map.Entry<String, Map<String, List<Integer>>> pkInfo = delPKItr.next();
                    if (comma) {
                        deleteQueryBuilder.append(", ");
                    }
                    comma = true;
                    deleteQueryBuilder.append("'").append((String)pkInfo.getKey()).append("'");
                }
                
                deleteQueryBuilder.append("]");
                this.m_dmlQuery = deleteQueryBuilder.toString();
                execute();
            }
            break;
    

        case INSERT:
            StringBuilder insertQueryBuilder = new StringBuilder();
            for (int dataCacheIdx = 0; dataCacheIdx < this.m_dataCache.size(); dataCacheIdx++) {
                Pair<String, List<CachedDataInfo>> cachePair = this.m_dataCache.get(dataCacheIdx);
                if (this.m_isVirtualTable) {
                    String setKey = cachePair.value().get(0).getKey();
                    insertQueryBuilder.append("UPDATE `").append(this.m_schemaName).append("` USE KEYS ").append((String)cachePair.key()).append(" SET ").append(setKey).append(" = ARRAY_CONCAT(IFMISSINGORNULL(").append(setKey).append(", []) , ").append(((CachedDataInfo)((List)cachePair.value()).get(0)).getValue()).append(") RETURNING META(`").append(this.m_schemaName).append("`).id AS ").append("PK"); 
                } else {
                    insertQueryBuilder.append("INSERT INTO `").append(this.m_schemaName).append("` (KEY, VALUE) VALUES (").append((String)cachePair.key()).append(", ").append(((CachedDataInfo)((List)cachePair.value()).get(0)).getValue()).append(") RETURNING META(`").append(this.m_schemaName).append("`).id AS ").append("PK");
                }
            }
            
            this.m_dmlQuery = insertQueryBuilder.toString();
            execute();
            break;
            
        case UPDATE:
            for (int dataCacheIdx = 0; dataCacheIdx < this.m_dataCache.size(); dataCacheIdx++) {
                Pair<String, List<CachedDataInfo>> cachePair = this.m_dataCache.get(dataCacheIdx);
                StringBuilder updateQueryBuilder = new StringBuilder();
                updateQueryBuilder.append("UPDATE `").append(this.m_schemaName).append("` USE KEYS '").append((String)cachePair.key()).append("' SET ");
                List<CachedDataInfo> currentValueList = cachePair.value();
                StringBuilder setListBuilder = new StringBuilder();
                for (int valueIdx = 0; valueIdx < currentValueList.size(); valueIdx++) {
                    CachedDataInfo currentCacheValue = currentValueList.get(valueIdx);
                    if (setListBuilder.length() != 0) {
                        setListBuilder.append(", ");
                    }
                    setListBuilder.append(currentCacheValue.getKey()).append(" = ");
                    if (currentCacheValue.isJson) {
                        setListBuilder.append(currentCacheValue.getValue());
                    } else {
                        setListBuilder.append(currentCacheValue.getValue());
                    }
                }
                
                updateQueryBuilder.append(setListBuilder);
                this.m_dmlQuery = updateQueryBuilder.toString();
                execute();
            }
                
        default:
            break;
        }
    }
  
    public void onFinishRowUpdate() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        if (null == this.m_dmlType) {
            return;
        }
        
        switch (this.m_dmlType) {
        
        case INSERT:
            String primaryKey = null;
            Stack<String> srcNameStack = new Stack<>();
            ObjectMapper mapper = new ObjectMapper();
            List<CachedDataInfo> insertDataList = new ArrayList<>();
            
            if (this.m_isVirtualTable) {
                ArrayNode mainArrayNode = mapper.createArrayNode();
                ObjectNode mainObjectNode = mapper.createObjectNode();
                List<CachedDataInfo> cachedIdxList = new ArrayList<>();
                
                for (int rowIndex = 0; rowIndex < this.m_rowCache.size(); rowIndex++) {
                    CachedDataInfo currentCacheData = (CachedDataInfo)this.m_rowCache.get(rowIndex);
                    if ((currentCacheData.getSourceName().isSpecialIdentifier()) && (currentCacheData.getSourceName().getParentName().isAttribute())) {
                        primaryKey = ((CachedDataInfo)this.m_rowCache.get(rowIndex)).getValue();
                    } else if ((currentCacheData.getSourceName().isSpecialIdentifier()) && (currentCacheData.getSourceName().getParentName().isArrayDimension())) {
                        cachedIdxList.add(currentCacheData);
                    } else if (null != currentCacheData.getSourceName().getParentName()) {
                        CBName srcName = currentCacheData.getSourceName();
                        if (srcName == this.m_tableSourceNameList) {
                            setNodeValue(mainArrayNode, currentCacheData);
                        } else {
                            
                            while (!srcName.isEqual(this.m_tableSourceNameList)) {
                                if (srcName.isAttribute()) {
                                    srcNameStack.push(srcName.getAsAttribute().getUnquotedName());
                                }
                                srcName = srcName.getParentName();
                            }
                            
                            ObjectNode currentObjPtr = mainObjectNode;
                            
                            while (!srcNameStack.empty()) {
                                String curSrcName = (String)srcNameStack.pop();
                                if (srcNameStack.empty()) {
                                    currentCacheData.setKey(curSrcName);
                                    if (!mainObjectNode.has(curSrcName)) {
                                        setObjectNodeValue(currentObjPtr, currentCacheData);
                                        break;
                                    }
                                } else if (!currentObjPtr.has(curSrcName)) {
                                    currentObjPtr = mapper.createObjectNode();
                                    mainObjectNode.put(curSrcName, currentObjPtr);
                                } else {
                                    currentObjPtr = (ObjectNode)mainObjectNode.get(curSrcName);
                                }
                            }
                        }
                    }
                }
                
                if (mainObjectNode.size() > 0) {
                    mainArrayNode.add(mainObjectNode);
                }
                
                String setKey = "";
                CBName updateAttr = this.m_tableSourceNameList;
                if (updateAttr.isArrayDimension()) {
                    updateAttr = updateAttr.getParentName();
                }
                
                while (null != updateAttr.getParentName()) {
                    if (updateAttr.isAttribute()) {
                        if ((setKey.length() != 0) && (setKey.charAt(0) == '`')) {
                            setKey = "." + setKey;
                        }
                        setKey = "`" + updateAttr.getAsAttribute().getUnquotedName() + "`" + setKey;
                    } else if (updateAttr.isArrayDimension()) {
                        for (int rowIndex = 0; rowIndex < cachedIdxList.size(); rowIndex++) {
                            CachedDataInfo currentIdx = (CachedDataInfo)cachedIdxList.get(rowIndex);
                            CBName cacheCBName = currentIdx.getSourceName().getParentName();
                            
                            if (updateAttr.isEqual(cacheCBName)) {
                                
                                if ((null == currentIdx.getValue()) || (currentIdx.getValue().length() == 0)) {
                                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_MISSING_COLUMN.name(), new String[0]);
                                    throw err;
                                }
                                
                                if ((setKey.length() != 0) && (setKey.charAt(0) == '`')) {
                                    setKey = "." + setKey;
                                }
                                
                                setKey = "[" + currentIdx.getValue() + "]" + setKey;
                            }
                        }
                    }
          
                    updateAttr = updateAttr.getParentName();
                }
                
                if (null == primaryKey) {
                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_MISSING_PRIMARYKEY.name(), new String[0]);
                    throw err;
                }
                
                CachedDataInfo insertCache = new CachedDataInfo();
                insertCache.setKey(setKey.toString());
                insertCache.setValue(mainArrayNode.toString());
                insertDataList.add(insertCache);
                this.m_dataCache.add(new Pair<>(primaryKey, insertDataList));
            } else {
                ObjectNode mainObjectNode = mapper.createObjectNode();
                boolean isTypeSelected = false;
                for (int rowIndex = 0; rowIndex < this.m_rowCache.size(); rowIndex++) {
                    CachedDataInfo currentCacheData = (CachedDataInfo)this.m_rowCache.get(rowIndex);
                    if ((currentCacheData.getSourceName().isSpecialIdentifier()) && (currentCacheData.getSourceName().getParentName().isAttribute())) {
                        primaryKey = ((CachedDataInfo)this.m_rowCache.get(rowIndex)).getValue();
                    } else if (null != currentCacheData.getSourceName().getParentName()) {
                        CBName srcName = currentCacheData.getSourceName();
                        while (null != srcName) {
                            if (srcName.isAttribute()) {
                                srcNameStack.push(srcName.getAsAttribute().getUnquotedName());
                            }
                            srcName = srcName.getParentName();
                        }
                        
                        srcNameStack.pop();
                        
                        ObjectNode currentObjPtr = mainObjectNode;
                        while (!srcNameStack.empty()) {
                            String curSrcName = (String)srcNameStack.pop();
                            
                            if (srcNameStack.empty()) {
                                currentCacheData.setKey(curSrcName);
                                
                                if (currentObjPtr.has(curSrcName)) {
                                    break;
                                }
                                
                                if ((null != this.m_tableDifferentiatorPair) && (curSrcName.equals(((CBTableColumn)this.m_tableDifferentiatorPair.key()).getColumnDSIIName()))) {
                                    if (!currentCacheData.getValueNoQuote().equals(this.m_tableDifferentiatorPair.value())) {
                                        break;
                                    }
                                    setObjectNodeValue(currentObjPtr, currentCacheData);
                                    isTypeSelected = true; 
                                    break;
                                }
                                
                                setObjectNodeValue(currentObjPtr, currentCacheData); 
                                break;  
                            }
                            
                            if (!currentObjPtr.has(curSrcName)) {
                                currentObjPtr = mapper.createObjectNode();
                                mainObjectNode.put(curSrcName, currentObjPtr);
                            } else {
                                currentObjPtr = (ObjectNode)mainObjectNode.get(curSrcName);
                            }
                        }
                    }
                }
                
                if (null == primaryKey) {
                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_MISSING_PRIMARYKEY.name(), new String[0]);
                    throw err;
                }
                
                if ((null != this.m_tableDifferentiatorPair) && (!isTypeSelected)) {
                    mainObjectNode.put(((CBTableColumn)this.m_tableDifferentiatorPair.key()).getColumnDSIIName(), (String)this.m_tableDifferentiatorPair.value());
                }
                
                CachedDataInfo insertCache = new CachedDataInfo();
                insertCache.setKey("VALUE");
                insertCache.setValue(mainObjectNode.toString());
                insertDataList.add(insertCache);
                this.m_dataCache.add(new Pair<>(primaryKey, insertDataList));
            }
            
            break;
            
        case UPDATE: 
/* 1011 */       List<CachedDataInfo> cachedIdxList = new ArrayList();
/* 1012 */       boolean isSkipRow = false;
/* 1013 */       Pair<String, List<CachedDataInfo>> setValuePair = null;
/* 1014 */       for (int idx = 0; idx < this.m_keyColumns.size(); idx++)
      {
/* 1016 */         CBTableColumn currentKeyCol = (CBTableColumn)this.m_keyColumns.get(idx);
/* 1017 */         CBName srcName = currentKeyCol.getSourceName();
/* 1018 */         if ((srcName.isSpecialIdentifier()) && (srcName.getParentName().isAttribute()))
        {


/* 1022 */           String pkAlias = currentKeyCol.getRowDocumentAlias();
/* 1023 */           String pkValue = this.m_currentRowData.get(pkAlias).textValue();
/* 1024 */           setValuePair = new Pair(pkValue, new ArrayList());


        }
/* 1028 */         else if ((srcName.isSpecialIdentifier()) && (srcName.getParentName().isArrayDimension()))
        {

/* 1031 */           Iterator<Map.Entry<CBTableColumn, String>> detectArrayItr = this.m_detectArrayAliasMap.entrySet().iterator();
          
/* 1033 */           while (detectArrayItr.hasNext())
          {
/* 1035 */             Map.Entry<CBTableColumn, String> nextPair = (Map.Entry)detectArrayItr.next();
/* 1036 */             JsonNode checkingNode = this.m_currentRowData.get((String)nextPair.getValue());
            
/* 1038 */             isSkipRow |= !checkingNode.asBoolean();
          }
          

/* 1042 */           String idxFieldName = currentKeyCol.getSelectAlias();
/* 1043 */           String index = this.m_currentRowData.get(idxFieldName).asText();
          
/* 1045 */           CachedDataInfo newCache = new CachedDataInfo();
/* 1046 */           newCache.setKey(currentKeyCol.getColumnDSIIName());
/* 1047 */           newCache.setValue(index);
/* 1048 */           newCache.setSQLType(4);
/* 1049 */           newCache.setSourceName(currentKeyCol.getSourceName());
/* 1050 */           cachedIdxList.add(newCache);
        }
      }
      
/* 1054 */       if (!isSkipRow)
      {

/* 1057 */         for (int cacheIdx = 0; cacheIdx < this.m_rowCache.size(); cacheIdx++)
        {

/* 1060 */           Stack<CBName> srcCBNameStack = new Stack();
/* 1061 */           String setKey = "";
/* 1062 */           CachedDataInfo curCache = (CachedDataInfo)this.m_rowCache.get(cacheIdx);
/* 1063 */           CBName updateAttr = curCache.getSourceName();
/* 1064 */           boolean foundTableSrc = updateAttr.isEqual(this.m_tableSourceNameList);
/* 1065 */           while (null != updateAttr.getParentName())
          {
/* 1067 */             if (updateAttr.isEqual(this.m_tableSourceNameList))
            {
/* 1069 */               foundTableSrc = true;
            }
/* 1071 */             if (foundTableSrc)
            {

/* 1074 */               if (updateAttr.isAttribute())
              {
/* 1076 */                 if ((setKey.length() != 0) && (setKey.charAt(0) == '`'))
                {
/* 1078 */                   setKey = "." + setKey;
                }
/* 1080 */                 setKey = "`" + updateAttr.getAsAttribute().getUnquotedName() + "`" + setKey;
              }
/* 1082 */               else if (updateAttr.isArrayDimension())
              {
/* 1084 */                 for (int idx = 0; idx < cachedIdxList.size(); idx++)
                {
/* 1086 */                   CachedDataInfo currentCache = (CachedDataInfo)cachedIdxList.get(idx);
/* 1087 */                   if (updateAttr.isEqual(currentCache.getSourceName().getParentName()))
                  {

/* 1090 */                     if ((setKey.length() != 0) && (setKey.charAt(0) == '`'))
                    {
/* 1092 */                       setKey = "." + setKey;
                    }
/* 1094 */                     setKey = "[" + currentCache.getValue() + "]" + setKey;
                  }
                  
                }
              }
            }
            else {
/* 1101 */               srcCBNameStack.push(updateAttr);
            }
/* 1103 */             updateAttr = updateAttr.getParentName();
          }
          
/* 1106 */           if (!srcCBNameStack.empty())
          {
/* 1108 */             boolean isGenerateJson = (srcCBNameStack.size() > 1) || (this.m_isVirtualTable);
            
/* 1110 */             if (!isGenerateJson)
            {

/* 1113 */               String setKeyCache = setKey.toString();
/* 1114 */               if (setKey.length() != 0)
              {
/* 1116 */                 setKey = setKey + ".";
              }
/* 1118 */               setKey = "`" + ((CBName)srcCBNameStack.pop()).getAsAttribute().getUnquotedName() + "`";
/* 1119 */               curCache.setKey(setKey);
            }
            else
            {
/* 1123 */               CBName srcNameCursor = (CBName)srcCBNameStack.pop();
/* 1124 */               String lookUpKey = null;
/* 1125 */               if (this.m_isVirtualTable)
              {
/* 1127 */                 lookUpKey = setKey;


              }
/* 1131 */               else if (srcNameCursor.isAttribute())
              {
/* 1133 */                 lookUpKey = "`" + srcNameCursor.getAsAttribute().getUnquotedName() + "`";
              }
              

/* 1137 */               if (0 == srcCBNameStack.size())
              {
/* 1139 */                 String updateItemName = srcNameCursor.getAsAttribute().getUnquotedName();
/* 1140 */                 curCache.setKey(setKey + "." + updateItemName);


              }
              else
              {

/* 1147 */                 while ((0 < srcCBNameStack.size()) && 
                



/* 1152 */                   (!srcCBNameStack.empty())) {}
              }
              
            }
            
          }
/* 1158 */           else if (setKey.length() != 0)
          {
/* 1160 */             curCache.setKey(setKey);
          }
        }
        

/* 1165 */         ((List)setValuePair.value()).addAll(0, this.m_rowCache);
/* 1166 */         this.m_dataCache.add(setValuePair);

      }
      else
      {

/* 1172 */         this.m_dataCache.remove(0);
      }
      



      break;
    }
    
  }

    public void onStartRowUpdate() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_dmlType = DSIExtJResultSet.DMLType.UPDATE;
        this.m_rowCache.clear();
    }
    
    public boolean shouldSelectRowSourceValue() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return true;
    }
  
    public boolean writeData(int column, DataWrapper data, long offset, boolean isDefault) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        CBTableColumn tempCol = (CBTableColumn)this.m_tableColumns.get(column);
        
        switch (this.m_dmlType) {
        
        case INSERT: 
            try {
                if (!isDefault) {
                    CachedDataInfo cacheValue = new CachedDataInfo();
                    cacheValue.setKey(tempCol.getColumnDSIIName());
                    cacheValue.setSourceName(tempCol.getSourceName());
                    if (cacheValue.getSourceName().isSpecialIdentifier()) {
                        if (this.m_keyColumns.isEmpty()) {
                            return false;
                        }
                        
                        int keyIndex = 0; 
                        if (keyIndex < this.m_keyColumns.size()) {
                            if (tempCol == this.m_keyColumns.get(keyIndex)) {
                                this.m_keyColumns.remove(keyIndex);
                            } else {
                                return false;
                            }
                        }
                    }
                    
                    if (null == data) {
                        cacheValue.setSQLType(0);
                    } else {
                        cacheValue.setSQLType(tempCol.getTypeMetadata().getType());
                    }
                    
                    cacheValue.setValue(setCacheValue(data));
                    cacheValue.setName(tempCol.getName());
                    this.m_rowCache.add(cacheValue);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            break;
            
        case UPDATE: 
            String colName = tempCol.getName();
            if (Updatable.READ_ONLY == tempCol.getUpdatable()) {
                ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_READONLY_COLUMN.name(), new String[0]);
                throw err;
            }
            CachedDataInfo newCache = new CachedDataInfo();
            newCache.setKey(colName);
            newCache.setValue(setCacheValue(data));
            newCache.setIsJson(false);
            newCache.setSourceName(tempCol.getSourceName());
            
            if (null == data) {
                newCache.setSQLType(0);
            } else {
                newCache.setSQLType(tempCol.getTypeMetadata().getType());
            }
            this.m_rowCache.add(newCache);
            break;
            
        default:
            break;
        }
        
        return false;
    }
    
    private void execute() throws ErrorException {
        
        System.out.println(this.m_dmlQuery);
        
        N1QLRowCountSet dmlResult = this.m_client.executeUpdate(this.m_dmlQuery, null, null);
        
        if (DSIExtJResultSet.DMLType.INSERT == this.m_dmlType) {
            ArrayList<N1QLQueryRow> returnData = dmlResult.allRowsRawData();
            if ((null == returnData) || (returnData.size() == 0)) {
                ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.WRITE_BACK_ERROR_FAIL_WRITEBACK.name(), new String[0]);
                throw err;
            }
        }
    }
    
    private String setCacheValue(DataWrapper data) {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    
        try {
            int type = data.getType();
            switch (type) {
            case -8:
            case 1:     
            case 12:
                return "'" + data.getVarChar() + "'";
                
            case 16:
                Boolean result = data.getBoolean();
                if (result.booleanValue()) {
                    return "true";
                }
                return "false";
                
            case 4:
                return String.valueOf(data.getInteger());
                
            case -5:
                return String.valueOf(data.getBigInt());
                
            case 3:     
            case 8: 
                return String.valueOf(data.getDouble());
                
            case 93:
                return String.valueOf(data.getTimestamp());
                
            case 0:
                return "NULL";    
            }    
        } catch (Exception ex) {
            LogUtilities.logError(ex, this.m_log);
        }
        return "NULL";
    }
    
    private void setNodeValue(JsonNode node, CachedDataInfo dataCache) {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    
        try {
            if ((node instanceof ObjectNode)) {
                setObjectNodeValue((ObjectNode)node, dataCache);
            } else if ((node instanceof ArrayNode)) {
                setArrayNodeValue((ArrayNode)node, dataCache);
            }
        } catch (Exception ex) {
            LogUtilities.logError(ex, this.m_log);
        }
    }
    
    private void setObjectNodeValue(ObjectNode objectNode, CachedDataInfo dataCache) {
        
        int dataType = dataCache.getSQLType();
        
        switch (dataType) {
        
        case -8: 
        case 1: 
        case 12: 
            objectNode.put(dataCache.getKey(), dataCache.getValueNoQuote());
            break;
            
        case 16: 
            objectNode.put(dataCache.getKey(), Boolean.valueOf(dataCache.getValue()));
            break;
            
        case 4:
            objectNode.put(dataCache.getKey(), Integer.valueOf(dataCache.getValue()));
            break;
            
        case -5:
            objectNode.put(dataCache.getKey(), Long.valueOf(dataCache.getValue()));
            break;
            
        case 3: 
        case 8:
            objectNode.put(dataCache.getKey(), Double.valueOf(dataCache.getValue()));
            break;
            
        case 93:
            objectNode.put(dataCache.getKey(), dataCache.getValue());
        }
    }
    
    private void setArrayNodeValue(ArrayNode arrayNode, CachedDataInfo dataCache) {
        
        int dataType = dataCache.getSQLType();
        
        switch (dataType) {
        case -8: 
        case 1: 
        case 12:
            arrayNode.add(dataCache.getValueNoQuote());
            break;
            
        case 16:
            arrayNode.add(Boolean.valueOf(dataCache.getValue()));
            break;
            
        case 4:
            arrayNode.add(Integer.valueOf(dataCache.getValue()));
            break;
            
        case -5:
            arrayNode.add(Long.valueOf(dataCache.getValue()));
            break;
            
        case 3: 
        case 8:
            arrayNode.add(Double.valueOf(dataCache.getValue()));
            break;
            
        case 93:
            arrayNode.add(dataCache.getValueNoQuote());
        }
    }
}