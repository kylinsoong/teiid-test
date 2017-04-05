 package com.simba.couchbase.schemamap;
 
 import com.fasterxml.jackson.databind.JsonNode;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.JsonNodeType;
 import com.fasterxml.jackson.databind.util.EmptyIterator;
 import com.simba.couchbase.client.N1QLQueryResult;
 import com.simba.couchbase.client.N1QLQueryRow;
 import com.simba.couchbase.core.CBClient;
 import com.simba.couchbase.core.CBConnectionSettings;
 import com.simba.couchbase.exceptions.CBJDBCMessageKey;
 import com.simba.couchbase.utils.CBQueryUtils;
 import com.simba.schema.map.nodes.SMCatalog;
 import com.simba.schema.map.nodes.SMDefinition;
 import com.simba.schema.map.nodes.SMSchema;
 import com.simba.support.ILogger;
 import com.simba.support.LogUtilities;
 import com.simba.support.exceptions.ErrorException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;

 public class CBSchemaMapSampler {
     
     public static final String COLUMN_ARRAY_POSTFIX = "[]";
     public static int currentBucketNum = 0;
     public static int currentTotalBucketNum;
     
     private Set<String> m_tableNameList = new HashSet<>();
     private Map<String, CBSMTable> m_tableValueMap = new HashMap<>();
     
     private String CURRENT_PK_SOURCE_NAME;
     
     private final String DSII_COLUMNNAME_SEPARATOR = "_";
     private final String SOURCE_COLUMNNAME_SEPARATOR = ".";
   

    /**
     *  To generate schema
     * @param couchClient
     * @param settings
     * @param log
     * @return
     * @throws ErrorException
     */
    public CBSMSchemaMap generateSMSchemaMap(CBClient couchClient, CBConnectionSettings settings, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);

        int sampleSize = settings.m_sampleSize;
        
        CBSMSchemaMap couchbaseSchemaMap = new CBSMSchemaMap();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            ArrayList<String> dataSrcCatalogList = couchClient.getCatalogs();
            
            for (String catalogName : dataSrcCatalogList) {    
                
                SMCatalog couchbaseSMCatalog = new SMCatalog(catalogName, couchbaseSchemaMap.getDefinition(), couchbaseSchemaMap);
                couchbaseSMCatalog.setAttribute((String)CBSMSchemaMap.DSII_NAME.key(), catalogName);
                couchbaseSchemaMap.addCatalog(couchbaseSMCatalog);
         
                ArrayList<String> dataSrcSchemaList = couchClient.getSchemas(catalogName);
                currentTotalBucketNum = dataSrcSchemaList.size();
                
                try {
                    for (String schemaName : dataSrcSchemaList) {
                        
                        SMSchema couchbaseSMSchema = new SMSchema(schemaName, couchbaseSchemaMap.getDefinition(), couchbaseSMCatalog);
                        couchbaseSMSchema.setAttribute((String)CBSMSchemaMap.DSII_NAME.key(), schemaName);
                        String bucketTypeName = (String)settings.m_bucketLookupTypeName.get(schemaName);

                        boolean bucketTypeNameHasBackTick = false;
                        if (null != bucketTypeName) {
                            couchbaseSMSchema.setAttribute((String)CBSMSchemaMap.COLUMN_DIFF_NAME.key(), bucketTypeName);
                        } else {
                            StringBuilder schemaNameBuilder = new StringBuilder();
                            schemaNameBuilder.append("`").append(schemaName).append("`");
                            bucketTypeName = (String)settings.m_bucketLookupTypeName.get(schemaNameBuilder.toString());
                            if (null != bucketTypeName) {
                                couchbaseSMSchema.setAttribute((String)CBSMSchemaMap.COLUMN_DIFF_NAME.key(), bucketTypeName);
                                bucketTypeNameHasBackTick = true;
                            } else {
                                bucketTypeName = "type";    
                            }    
                        }
                        
                        StringBuilder pk_sourceName = new StringBuilder();
                        pk_sourceName.append("`").append(schemaName).append("`").append("$").append("PK");
                        
                        this.CURRENT_PK_SOURCE_NAME = pk_sourceName.toString();
                        
                        ArrayList<String> dataSrcTableList = new ArrayList<>();
                        N1QLQueryResult dataSrcTableResult = null;
                        
                        try {
                            dataSrcTableResult = couchClient.getTables(bucketTypeName, catalogName, schemaName);
                            if (bucketTypeNameHasBackTick) {
                                bucketTypeName = bucketTypeName.substring(1, bucketTypeName.length() - 1);    
                            }
                            
                            ArrayList<N1QLQueryRow> tableRowList = dataSrcTableResult.allRowsRawData();
                            if ((null != tableRowList) && (tableRowList.size() == 1)) {
                                int typeSize = ((N1QLQueryRow)tableRowList.get(0)).value().size();
                                if (typeSize == 0) {
                                    dataSrcTableList.add(schemaName);    
                                } else {
                                    dataSrcTableList.add(((N1QLQueryRow)tableRowList.get(0)).value().get(bucketTypeName).textValue());    
                                }    
                            } else {
                                for (N1QLQueryRow tableRow : tableRowList) { 
                                    if (null != tableRow.value().get(bucketTypeName)) {
                                        String tableName = tableRow.value().get(bucketTypeName).textValue();
                                        dataSrcTableList.add(tableName);    
                                    }    
                                }    
                            }    
                        } catch (Exception ex) {
                            LogUtilities.logError(ex, log);    
                        }
                        
                        try {
                            for (String tableName : dataSrcTableList) {
                                this.m_tableNameList.add(tableName);
                                StringBuilder schemaNameBuilder = new StringBuilder();
                                schemaNameBuilder.append("`").append(schemaName).append("`");
                                CBSMTable couchbaseSMTable = new CBSMTable(schemaNameBuilder.toString(), tableName, pk_sourceName.toString(), couchbaseSchemaMap.getDefinition(), couchbaseSMSchema, false);
                 
                                couchbaseSMTable.addAttribute((String)CBSMSchemaMap.TYPECOLUMN_NAME.key(), bucketTypeName);
                                couchbaseSMTable.addAttribute((String)CBSMSchemaMap.TYPECOLUMN_VALUE.key(), tableName);
                                
                                this.m_tableValueMap.put(tableName, couchbaseSMTable);
                 
                                boolean hasTypeIdentifier = false;
                                if (null != dataSrcTableResult) {
                                    hasTypeIdentifier = dataSrcTableResult.hasTypeIdentifier();    
                                }
                                
                                N1QLQueryResult sampleResult = couchClient.getTableSample(catalogName, schemaName, tableName, bucketTypeName, sampleSize, hasTypeIdentifier);
                                
                                try {
                                    for (N1QLQueryRow currentRow : sampleResult.allRowsRawData()) {
                                        JsonNode rootNode = mapper.readTree(currentRow.toString());
                                        JsonNode currentRowJson = rootNode.path(schemaName);
                                        
                                        JsonNodeType currentRowType = currentRowJson.getNodeType();
                                        if (JsonNodeType.ARRAY == currentRowType) {
                                            StringBuilder newTableSrcName = new StringBuilder();
                                            newTableSrcName.append(schemaNameBuilder.toString()).append("[]");
                                            couchbaseSMTable.addAttribute((String)CBSMSchemaMap.SOURCE_NAME.key(), newTableSrcName.toString());    
                                        }
                                        
                                        scanRow(couchbaseSchemaMap, couchbaseSMSchema, couchbaseSMTable, currentRowJson, schemaName, tableName, mapper, Boolean.valueOf(false), log);    
                                    }    
                                } catch (ErrorException err) {
                                    LogUtilities.logError(err, log);    
                                }
                                
                                couchbaseSMSchema.addTable(couchbaseSMTable);    
                            }    
                        } catch (ErrorException err) {
                            LogUtilities.logError(err, log);    
                        }
                        
                        couchbaseSchemaMap.addSchema(couchbaseSMSchema);
                        couchbaseSMCatalog.addSchema(couchbaseSMSchema);
                        currentBucketNum += 1;
                        if (settings.m_displaySchemaMapProcess == 1) {
                            String displayPercent = String.valueOf(currentBucketNum / currentTotalBucketNum * 100.0D);
                            if (displayPercent.length() > 4) {
                                displayPercent = displayPercent.substring(0, 4);    
                            }
                            System.out.println("Current Catalog: " + catalogName + ", Current bucket: " + schemaName + "\n schemamap generated at " + displayPercent + "%");    
                        }    
                    }    
                } catch (Exception err) {
                    LogUtilities.logError(err, log);    
                }

                buildTable(couchbaseSMCatalog, settings);    
            }    
        } catch (Exception ex) {
            LogUtilities.logError(ex, log);    
        }
        
        return couchbaseSchemaMap;    
    }
    
    
    private void buildTable(SMCatalog couchbaseSMSCatalog, CBConnectionSettings settings) {
        
        for (int schemaIdx = 0; schemaIdx < couchbaseSMSCatalog.getSchemas().size(); schemaIdx++) {
            
            SMSchema couchSchema = (SMSchema)couchbaseSMSCatalog.getSchemas().get(schemaIdx);
            for (int tableIndex = 0; tableIndex < couchSchema.getTables().size(); tableIndex++) {
                CBSMTable couchTable = (CBSMTable)couchSchema.getTables().get(tableIndex);
                Iterator<String> columnItr = couchTable.getColumnNameList().iterator();
                while (columnItr.hasNext()) {
                    String columnName = (String)columnItr.next();
                    CBSMColumn columnContent = (CBSMColumn)couchTable.getColumnValueList().get(columnName);
                    couchTable.addColumn(columnContent);    
                }    
            }    
        }
        
        if (settings.m_displaySchemaMapProcess == 1) {
            System.out.println("SchemaMap generation finished");    
        }    
    }
    
    private void scanRow(CBSMSchemaMap couchbaseSchemaMap, SMSchema couchbaseSMSchema, CBSMTable couchbaseSMTable, JsonNode currentRootNode, String currentColumnSourceName, String currentColumnDSIIName, ObjectMapper mapper, Boolean isVirtualTable, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[] { "Current table SourceName: " + currentColumnSourceName, "Current table DSIIName: " + currentColumnDSIIName, "Is table virtual: " + isVirtualTable });
        
        try {
            Iterator<Map.Entry<String, JsonNode>> currentRowItr = currentRootNode.fields();
            if (isObjectJsonType(currentRowItr)) {
                goThroughObjectRow(currentRowItr, couchbaseSchemaMap, couchbaseSMSchema, couchbaseSMTable, currentColumnSourceName, currentColumnDSIIName, mapper, isVirtualTable, log);    
            } else if (isArrayJsonType(currentRowItr)) {
                goThroughArrayRow(couchbaseSchemaMap, couchbaseSMSchema, couchbaseSMTable, currentRootNode, mapper, isVirtualTable, log);    
            }    
        } catch (ErrorException err) {
            throw err;    
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_GEN_ERROR.name(), new String[] { ex.getMessage() });    
        }    
    }
    
    
    private String buildVirtualTableName(String parentTableName, String currentTableName, String splitToken) {
        return parentTableName + splitToken + currentTableName;    
    }
    
    
    private CBSMTable createBasicVirtualTable(SMSchema couchbaseSMSchema, CBSMTable parentSMTable, String tableSourceName, String tableDSIIName, SMDefinition smDefinition, boolean isVirtualTable) {
        
        CBSMTable couchObjectVT = new CBSMTable(tableSourceName, tableDSIIName, this.CURRENT_PK_SOURCE_NAME, smDefinition, couchbaseSMSchema, isVirtualTable);
        couchObjectVT.addAttribute((String)CBSMSchemaMap.TYPECOLUMN_NAME.key(), (String)parentSMTable.getAttributes().get(CBSMSchemaMap.TYPECOLUMN_NAME.key()));
        couchObjectVT.addAttribute((String)CBSMSchemaMap.TYPECOLUMN_VALUE.key(), (String)parentSMTable.getAttributes().get(CBSMSchemaMap.TYPECOLUMN_VALUE.key()));
        couchObjectVT.creatColumnForVirtual(parentSMTable, tableSourceName, tableDSIIName);
        return couchObjectVT;    
    }
    
    
    private void goThroughObjectRow(Iterator<Map.Entry<String, JsonNode>> currentRootItr, CBSMSchemaMap couchbaseSchemaMap, SMSchema parentSMSchema, CBSMTable parentSMTable, String parentSourceName, String parentDSIIName, ObjectMapper mapper, Boolean isNestedType, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[] { "Go throught object column: " + parentSourceName });
        
        try {
            while (currentRootItr.hasNext()) {
                Map.Entry<String, JsonNode> nextColumn = currentRootItr.next();
                
                String columnName = (String)nextColumn.getKey();
                JsonNode columnValue = (JsonNode)nextColumn.getValue();
                JsonNodeType columnJsonDataType = columnValue.getNodeType();
                
                StringBuilder newSourceNameBuilder = new StringBuilder();
                newSourceNameBuilder.append("`").append(columnName).append("`");
                
                String newDSIIName;
                String newSourceName;
                
                if (!isNestedType.booleanValue()) {
                    newSourceName = newSourceNameBuilder.toString();
                    newDSIIName = columnName;    
                } else {
                    newSourceName = buildVirtualTableName(parentSourceName, newSourceNameBuilder.toString(), SOURCE_COLUMNNAME_SEPARATOR);
                    newDSIIName = buildVirtualTableName(parentDSIIName, columnName, DSII_COLUMNNAME_SEPARATOR);    
                }
                
                switch (columnJsonDataType) {
                
                case ARRAY:
                    StringBuilder newArrayTableSourceName = new StringBuilder();
                    StringBuilder newArrayTableDSIIName = new StringBuilder();
                    
                    newArrayTableSourceName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.SOURCE_NAME.key())).append(".").append(newSourceName).append("[]"); 
                    if (!parentSMTable.isVirtualTable()) {
                        newArrayTableDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key())).append("_").append(columnName);    
                    } else {
                        newArrayTableDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key())).append("_").append(columnName).append("_").append("dim").append(parentSMTable.getTableDimension() + 1);    
                    }
                    
                    CBSMTable virtableToScan = initTableORgetTableReference(parentSMSchema, parentSMTable, newArrayTableSourceName.toString(), newArrayTableDSIIName.toString());
                    scanRow(couchbaseSchemaMap, parentSMSchema, virtableToScan, columnValue, newSourceName, newDSIIName, mapper, Boolean.valueOf(true), log);
                    break;
                    
                case OBJECT:
                    scanRow(couchbaseSchemaMap, parentSMSchema, parentSMTable, columnValue, newSourceName, newDSIIName, mapper, Boolean.valueOf(true), log);
                    break;
                    
                default:
                    String parentTableSourceName = (String)parentSMTable.getAttributes().get(CBSMSchemaMap.SOURCE_NAME.key());
                    if (!isNestedType.booleanValue()) {
                        newSourceName = buildVirtualTableName(parentTableSourceName, newSourceNameBuilder.toString(), ".");    
                    } else {
                        newSourceName = buildVirtualTableName(parentTableSourceName, newSourceName, ".");    
                    }
                    
                    CBSMColumn columnToScan = initColumnORgetColumnReference(parentSMTable, newSourceName, newDSIIName, columnValue);
                    updateVirtualTable(parentSMTable, columnToScan, newSourceName, newDSIIName, columnValue);    
                }    
            }    
        } catch (ErrorException err) {
            throw err;    
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_GEN_ERROR.name(), new String[] { ex.getMessage() });    
        }    
    }
   
    
    private void goThroughArrayRow(CBSMSchemaMap couchbaseSchemaMap, SMSchema parentSMSchema, CBSMTable parentSMTable, JsonNode currentRootNode, ObjectMapper mapper, Boolean isNestedType, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[] { "Go throught array column: " + parentSMTable.toString() });
     
        try {
            if (1 <= currentRootNode.size()) {
                for (int arrayIndex = 0; arrayIndex < currentRootNode.size(); arrayIndex++) {
                    
                    JsonNode currentArrayElement = currentRootNode.get(arrayIndex);
                    JsonNodeType currentElementType = currentArrayElement.getNodeType();
                    
                    if (JsonNodeType.OBJECT == currentElementType) { 
                        Iterator<Map.Entry<String, JsonNode>> currentArrayItr = currentArrayElement.fields();
                        while (currentArrayItr.hasNext()) {
                            Map.Entry<String, JsonNode> nextColumn = currentArrayItr.next();
                            
                            String columnName = (String)nextColumn.getKey();
                            JsonNode columnValue = (JsonNode)nextColumn.getValue();
                            JsonNodeType columnJsonDataType = columnValue.getNodeType();
                            JsonNode newRootNode = mapper.readTree(columnValue.toString());
                            
                            StringBuilder newSourceNameBuilder = new StringBuilder();
                            newSourceNameBuilder.append("`").append(columnName).append("`");
                            
                            StringBuilder newSourceName = new StringBuilder();
                            newSourceName.append(newSourceNameBuilder.toString());
                            
                            StringBuilder newDSIIName = new StringBuilder();
                            newDSIIName.append(columnName);
                            
                            switch (columnJsonDataType) {
                            
                            case ARRAY:
                                StringBuilder newArrayTableSourceName = new StringBuilder();
                                StringBuilder newArrayTableDSIIName = new StringBuilder();
                                newArrayTableSourceName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.SOURCE_NAME.key())).append(".").append(newSourceNameBuilder.toString()).append("[]");
                 
                                if (!parentSMTable.isVirtualTable()) {
                                    newArrayTableDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key())).append("_").append(columnName);    
                                } else {
                                    newArrayTableDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key())).append("_").append(columnName).append("_").append("dim").append(parentSMTable.getTableDimension() + 1);    
                                }
                                
                                CBSMTable virtableToScan = initTableORgetTableReference(parentSMSchema, parentSMTable, newArrayTableSourceName.toString(), newArrayTableDSIIName.toString());
                                scanRow(couchbaseSchemaMap, parentSMSchema, virtableToScan, newRootNode, newArrayTableSourceName.toString(), newArrayTableDSIIName.toString(), mapper, Boolean.valueOf(true), log);
                                break;
                                
                            case OBJECT:
                                scanRow(couchbaseSchemaMap, parentSMSchema, parentSMTable, newRootNode, newSourceName.toString(), newDSIIName.toString(), mapper, Boolean.valueOf(true), log);
                                break;
                                
                            default:
                                StringBuilder newColumnSourceName = new StringBuilder();
                                newColumnSourceName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.SOURCE_NAME.key())).append(".").append(newSourceNameBuilder.toString());
                 
                                StringBuilder newColumnDSIIName = new StringBuilder();
                                newColumnDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key())).append("_").append(columnName);
                                CBSMColumn columnToScan = initColumnORgetColumnReference(parentSMTable, newColumnSourceName.toString(), newColumnDSIIName.toString(), columnValue);
                                updateVirtualTable(parentSMTable, columnToScan, newColumnSourceName.toString(), newColumnDSIIName.toString(), columnValue);    
                            }    
                        }    
                    } else if (JsonNodeType.ARRAY == currentElementType) {
                        StringBuilder newColumnSourceName = new StringBuilder();
                        newColumnSourceName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.SOURCE_NAME.key())).append("[]");
                        StringBuilder newColumnDSIIName = new StringBuilder();
                        newColumnDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key())).append("_").append("dim").append(parentSMTable.getTableDimension() + 1);
             
                        CBSMTable virtableToScan = initTableORgetTableReference(parentSMSchema, parentSMTable, newColumnSourceName.toString(), newColumnDSIIName.toString());
                        scanRow(couchbaseSchemaMap, parentSMSchema, virtableToScan, currentArrayElement, newColumnSourceName.toString(), newColumnDSIIName.toString(), mapper, Boolean.valueOf(true), log);    
                    } else {
                        StringBuilder newColumnSourceName = new StringBuilder();
                        newColumnSourceName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.SOURCE_NAME.key()));
                        
                        StringBuilder newColumnDSIIName = new StringBuilder();
                        newColumnDSIIName.append((String)parentSMTable.getAttributes().get(CBSMSchemaMap.DSII_NAME.key()));
                        
                        CBSMColumn columnToScan = initColumnORgetColumnReference(parentSMTable, newColumnSourceName.toString(), newColumnDSIIName.toString(), currentArrayElement);
                        updateVirtualTable(parentSMTable, columnToScan, newColumnSourceName.toString(), newColumnDSIIName.toString(), currentArrayElement);    
                    }    
                }    
            } else if (0 == currentRootNode.size()) {
                updateVirtualTable(parentSMTable, null, null, null, null);    
            }    
        } catch (ErrorException err) {
            throw err;    
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_GEN_ERROR.name(), new String[] { ex.getMessage() });    
        }    
    }
    
    
    private CBSMColumn initColumnORgetColumnReference(CBSMTable couchbaseSMTable, String sourceColumnName, String dsiiColumnName, JsonNode currentColumnContent) throws ErrorException {
     
        try {
            if (!couchbaseSMTable.getColumnNameList().contains(sourceColumnName)) {
                CBSMColumn currentCouchbaseColumn = new CBSMColumn(sourceColumnName, dsiiColumnName, couchbaseSMTable.getDefinition(), couchbaseSMTable, false);
                currentCouchbaseColumn.initColumnForTable(currentColumnContent);
                return currentCouchbaseColumn;    
            }
            
            return (CBSMColumn)couchbaseSMTable.getColumnValueList().get(sourceColumnName);    
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_COLUMN_GEN_ERROR.name(), new String[] { ex.getMessage() });    
        }    
    }
    
    private CBSMTable initTableORgetTableReference(SMSchema couchbaseSMSchema, CBSMTable couchbaseSMTable, String parentSourceName, String parentDSIIName) throws ErrorException {
     
        try {
            if (!this.m_tableNameList.contains(parentDSIIName)) {
                
                CBSMTable couchArrayVT = createBasicVirtualTable(couchbaseSMSchema, couchbaseSMTable, parentSourceName, parentDSIIName, couchbaseSMSchema.getDefinition(), true);
                couchArrayVT.setTableDimension(couchbaseSMTable.getTableDimension() + 1);
                
                this.m_tableNameList.add(parentDSIIName);
                this.m_tableValueMap.put(parentDSIIName, couchArrayVT);
                couchbaseSMSchema.addTable(couchArrayVT);
                return couchArrayVT;    
            }
            
            return (CBSMTable)this.m_tableValueMap.get(parentDSIIName);    
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_TABLE_GEN_ERROR.name(), new String[] { ex.getMessage() });    
        }    
    }
    
    private boolean isArrayJsonType(Iterator<Map.Entry<String, JsonNode>> currentElement) {
        
        if ((currentElement instanceof EmptyIterator)) {
            return true;    
        }
        
        return false;    
    }
    
    private boolean isObjectJsonType(Iterator<Map.Entry<String, JsonNode>> currentElement) {
        
        if (!(currentElement instanceof EmptyIterator)) {
            return true;    
        }
        
        return false;
   }
    
    private void updateVirtualTable(CBSMTable couchbaseSMTable, CBSMColumn currentCouchbaseColumn, String newSourceName, String newDSIIName, JsonNode columnValue) {
        
        if (null != currentCouchbaseColumn) {
            if (!couchbaseSMTable.getColumnNameList().contains(newSourceName)) {
                couchbaseSMTable.getColumnNameList().add(newSourceName);
                couchbaseSMTable.getColumnValueList().put(newSourceName, currentCouchbaseColumn);    
            } else {
                updateColumn(couchbaseSMTable, currentCouchbaseColumn, newSourceName, newDSIIName, columnValue);    
            }    
        }    
    }
    
    private void updateColumn(CBSMTable couchbaseSMTable, CBSMColumn currentCouchbaseColumn, String newSourceName, String newDSIIName, JsonNode columnValue) {
        
        CBSMColumn columnToModify = (CBSMColumn)couchbaseSMTable.getColumnValueList().get(newSourceName);
        
        String existColumnType = (String)columnToModify.getAttributes().get(CBSMSchemaMap.SOURCE_TYPE.key());
        String updateColumnType = (String)currentCouchbaseColumn.getAttributes().get(CBSMSchemaMap.SOURCE_TYPE.key());
        
        if (existColumnType.equalsIgnoreCase(CBSourceType.NULL.name())) {
            if (!updateColumnType.equalsIgnoreCase(CBSourceType.NULL.name())) {
                columnToModify.updateColumnForTable((JsonNodeType)CBSMSchemaMap.sourceTypeMap.get(updateColumnType.toUpperCase()), columnValue);    
            }    
        } else if (existColumnType.equalsIgnoreCase(CBSourceType.BOOLEAN.name())) {
            if ((!updateColumnType.equalsIgnoreCase(CBSourceType.BOOLEAN.name())) && (!updateColumnType.equalsIgnoreCase(CBSourceType.NULL.name()))) {
                columnToModify.updateColumnForTable((JsonNodeType)CBSMSchemaMap.sourceTypeMap.get(updateColumnType.toUpperCase()), columnValue);    
            }    
        } else if (existColumnType.equalsIgnoreCase(CBSourceType.NUMBER.name())) {
            if ((!updateColumnType.equalsIgnoreCase(CBSourceType.BOOLEAN.name())) && (!updateColumnType.equalsIgnoreCase(CBSourceType.NULL.name())) && (!updateColumnType.equalsIgnoreCase(CBSourceType.NUMBER.name()))) {
                columnToModify.updateColumnForTable((JsonNodeType)CBSMSchemaMap.sourceTypeMap.get(updateColumnType.toUpperCase()), columnValue);    
            } else if (existColumnType.equalsIgnoreCase(CBSourceType.NUMBER.name())) {
                if (null != columnValue) {
                    if (columnValue.isDouble()) {
                        columnToModify.updateColumnForTable((JsonNodeType)CBSMSchemaMap.sourceTypeMap.get(updateColumnType.toUpperCase()), columnValue);    
                    }    
                }    
            }    
        } else if (existColumnType.equalsIgnoreCase(CBSourceType.STRING.name())) {
            if ((updateColumnType.equalsIgnoreCase(CBSourceType.ARRAY.name())) && (updateColumnType.equalsIgnoreCase(CBSourceType.OBJECTS.name()))) {
                columnToModify.updateColumnForTable((JsonNodeType)CBSMSchemaMap.sourceTypeMap.get(updateColumnType.toUpperCase()), columnValue);    
            }    
        } else if (existColumnType.equalsIgnoreCase(CBSourceType.ARRAY.name())) {
            if (updateColumnType.equalsIgnoreCase(CBSourceType.OBJECTS.name())) {
                columnToModify.updateColumnForTable((JsonNodeType)CBSMSchemaMap.sourceTypeMap.get(updateColumnType.toUpperCase()), columnValue);    
            }    
        }    
    }
 }
