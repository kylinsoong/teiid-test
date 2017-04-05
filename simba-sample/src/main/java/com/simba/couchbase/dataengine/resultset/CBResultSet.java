package com.simba.couchbase.dataengine.resultset;

import com.fasterxml.jackson.databind.JsonNode;
import com.simba.couchbase.client.N1QLQueryResult;
import com.simba.couchbase.core.CBClient;
import com.simba.couchbase.dataengine.CBSQLDataEngine;
import com.simba.couchbase.dataengine.querygeneration.CBAliasGenerator;
import com.simba.couchbase.dataengine.querygeneration.CBParamValue;
import com.simba.couchbase.dataengine.querygeneration.CBValueExprBuilder;
import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
import com.simba.couchbase.schemamap.metadata.CBSchemaMetadata;
import com.simba.couchbase.schemamap.metadata.CBTableMetadata;
import com.simba.couchbase.schemamap.parser.CBName;
import com.simba.couchbase.schemamap.parser.generated.Parser;
import com.simba.couchbase.utils.CBDataTypeUtils;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class CBResultSet extends DSIExtJResultSet {
    
    protected JsonNode[] m_allRowDataStructure;
    private String m_catalogName;
    protected CBClient m_client;
    private ArrayList<CBColumnMetadata> m_columnMetadata;
    private int m_currentFetchedRows;
    protected JsonNode m_currentRowData;
    protected Map<CBTableColumn, String> m_detectArrayAliasMap;
    protected boolean m_isVirtualTable;
    public List<CBTableColumn> m_keyColumns;
    private Map<String, CBTableColumn> m_letClauses;
    public List<String> m_lettingClauses;
    private List<CBParamValue> m_paramValues;
    private ILogger m_log;
    private long m_rowCount;
    protected String m_rowSourceAlias;
    protected String m_schemaName;
    private String m_tableName;
    protected ArrayList<CBTableColumn> m_tableColumns;
    protected Pair<CBTableColumn, String> m_tableDifferentiatorPair;
    private CBTableMetadata m_tableMeta;
    protected CBName m_tableSourceNameList;
    private IWarningListener m_warningListener;
    private List<String> m_whereClauses;
  
    public CBResultSet(CBSchemaMetadata schemaMeta, CBTableMetadata tableMeta, ArrayList<CBColumnMetadata> columnMetadata, CBClient client, ILogger log) throws ErrorException {

        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        this.m_client = client;
        this.m_currentFetchedRows = 0;
        this.m_columnMetadata = columnMetadata;
        this.m_tableMeta = tableMeta;
        this.m_log = log;
        this.m_letClauses = new HashMap<>();
        this.m_paramValues = new ArrayList<>();
        this.m_isVirtualTable = false;
        
        this.m_catalogName = ((CBColumnMetadata)this.m_columnMetadata.get(0)).getCatalogName();
        this.m_schemaName = ((CBColumnMetadata)this.m_columnMetadata.get(0)).getSchemaName();
        this.m_tableName = ((CBColumnMetadata)this.m_columnMetadata.get(0)).getTableName();
        
        this.m_tableSourceNameList = Parser.parse(this.m_tableMeta.getSourceName());
        
        this.m_keyColumns = new ArrayList<>();
        this.m_tableColumns = new ArrayList<>();
        this.m_detectArrayAliasMap = new HashMap<>();
        
        for (int columnIndex = 0; columnIndex < this.m_columnMetadata.size(); columnIndex++) {
            CBColumnMetadata columnMetaPtr = (CBColumnMetadata)this.m_columnMetadata.get(columnIndex);
            
            String typeColumnName = this.m_tableMeta.getColumnIdentifierName();
            String typeColumnValue = this.m_tableMeta.getColumnIdentifierValue();
            String columnName = columnMetaPtr.getName();
            CBTableColumn tableColumn = new CBTableColumn(columnMetaPtr, typeColumnValue);
            
            if (columnName.equals(typeColumnName)) {
                this.m_tableDifferentiatorPair = new Pair<>(tableColumn, tableColumn.getTableDifferentiatorValue());
            }
            
            if (tableColumn.getSourceName().isSpecialIdentifier()) {
                this.m_keyColumns.add(tableColumn);
            }
            this.m_tableColumns.add(tableColumn);
        }
    }
    
    public CBResultSet(CBResultSet rs, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        setCatalogName(rs.getCatalogName());
        setSchemaName(rs.getSchemaName());
        setTableName(rs.getTableName());
        
        this.m_client = rs.getClientRef();
        this.m_letClauses = new HashMap<>();
        this.m_paramValues = new ArrayList<>();
        this.m_log = log;
        this.m_tableMeta = rs.getTableMeta();
        this.m_keyColumns = new ArrayList<>();
        this.m_tableSourceNameList = Parser.parse(rs.getTableMeta().getSourceName());
        
        @SuppressWarnings("unchecked")
        ArrayList<CBColumnMetadata> selectedColumnsRef = (ArrayList<CBColumnMetadata>) rs.getSelectColumnsOrgin();
        
        this.m_tableColumns = new ArrayList<>();
        
        for (int columnIndex = 0; columnIndex < selectedColumnsRef.size(); columnIndex++) {
            CBTableColumn tableColumn = new CBTableColumn((CBColumnMetadata)selectedColumnsRef.get(columnIndex), rs.getTableName());
            String columnName = ((CBColumnMetadata)selectedColumnsRef.get(columnIndex)).getName();
            String typeColumnName = rs.getTableMeta().getColumnIdentifierName();
            
            if ((null != typeColumnName) && (typeColumnName.equals(columnName))) {
                this.m_tableDifferentiatorPair = new Pair<>(tableColumn, tableColumn.getTableDifferentiatorValue());
            }
            
            if (tableColumn.getSourceName().isSpecialIdentifier()) {
                this.m_keyColumns.add(tableColumn);
            }
            this.m_tableColumns.add(tableColumn);
        }
    }
    
    public void addLetClauses(Map<String, CBTableColumn> newLetClauses) { 
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_letClauses.putAll(newLetClauses);
    }
    
    public void appendRow() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
    }
    
    public void close() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    }
  









  public void closeCursor()
    throws ErrorException
  {
/*  384 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  
    public boolean detectArrayDimension() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return false;
    }
  








  public void deleteRow()
    throws ErrorException
  {
/*  412 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  413 */     throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  
    public String getCatalogName() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_catalogName;
    }
  




  public CBClient getClientRef()
  {
/*  437 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  438 */     return this.m_client;
  }
  
    public boolean getData(int column, long offset, long maxSize, DataWrapper retrievedData)
    throws ErrorException
  {
/*  491 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  492 */     CBTableColumn neededColumn = (CBTableColumn)this.m_tableColumns.get(column);
/*  493 */     Short sqlType = Short.valueOf(neededColumn.getTypeMetadata().getType());
/*  494 */     String alias = neededColumn.getSelectAlias();
/*  495 */     JsonNode row = this.m_allRowDataStructure[(this.m_currentFetchedRows - 1)];
/*  496 */     JsonNode field = row.get(alias);
    
/*  498 */     return CBDataTypeUtils.convertColumnToData(offset, maxSize, retrievedData, sqlType, field, this.m_log);
  }
  










  public CBSQLDataEngine getDataEngineRef()
  {
/*  513 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  514 */     return this.m_client.getDataEngineRef();
  }
  















  public long getRowCount()
    throws ErrorException
  {
/*  535 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  536 */     return 0L;
  }
  
    public String getSchemaName() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_schemaName;
    }
  
    public ArrayList<? extends IColumn> getSelectColumns() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_tableColumns;
    }
    
    public ArrayList<? extends IColumn> getSelectColumnsOrgin() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_columnMetadata;
    }
    
    public void giveSortClauses() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    }
  

  public Pair<CBTableColumn, String> getTableDifferentorPair()
  {
/*  603 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  604 */     return this.m_tableDifferentiatorPair;
  }
  
    public CBTableMetadata getTableMeta() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_tableMeta;
    }
  
    public String getTableName() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_tableName;
    }
  





  public IWarningListener getWarningListener()
  {
/*  636 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  637 */     return this.m_warningListener;
  }
  



  public void giveAggregationPassDown()
  {
/*  645 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  





  public void giveParameterValues(List<CBParamValue> newParamValues)
  {
/*  655 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  656 */     this.m_paramValues.addAll(newParamValues);
  }
  




  public void giveWhereClauses(List<String> newWhereClauses)
  {
/*  665 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  666 */     this.m_whereClauses = newWhereClauses;
  }
  






  public boolean hasRowCount()
  {
/*  677 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  678 */     return false;
  }
  




  public boolean isJoinPassdownEligible()
  {
/*  687 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    
/*  689 */     return false;
  }
  




    public boolean isDMLResultSet() {
        return false;
    }
  













  public boolean moveToNextRow()
    throws ErrorException
  {
/*  717 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  718 */     if (this.m_currentFetchedRows < this.m_rowCount)
    {
/*  720 */       this.m_currentRowData = this.m_allRowDataStructure[this.m_currentFetchedRows];
/*  721 */       this.m_currentFetchedRows += 1;
/*  722 */       return true;
    }
/*  724 */     return false;
  }
  






  public void registerWarningListener(IWarningListener listener)
  {
/*  735 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  736 */     this.m_warningListener = listener;
  }
  
    public void setCursorType(CursorType cursorType) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        if (CursorType.FORWARD_ONLY != cursorType) {
            throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(cursorType));
        }
        
        fixNames();
        buildQuery();
    }
  



  public void setLimitExpression(String limitExpr)
  {
/*  769 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  







  public boolean shouldSelectRowSourceValue()
  {
/*  781 */     return false;
  }

    public boolean writeData(int column, DataWrapper data, long offset, boolean isDefault) throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
    }
  
    public void reset() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    }
    
    private void buildQuery() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        LogUtilities.logInfo("###############  Build Query Start ##################", this.m_log);
        
        StringBuilder parseDownBuilder = new StringBuilder();
        parseDownBuilder.append("SELECT ");
        
        boolean hasTypeColumn = false;
        StringBuilder PKAliasCache = null;
        CBTableColumn PKColumnPtrCache = null;
        boolean hasPKInProjection = false;
        
        Map<DSIExtJResultSet, CBResultSet> emptyTableCopyMap = new HashMap<>();
        if (null != this.m_tableDifferentiatorPair) {
            hasTypeColumn = true;
        }
        
        boolean comma = false;
        Set<String> selectAliases = new HashSet<>();
        int neededColumnCount = 0;
        for (int columnIndex = 0; columnIndex < this.m_tableColumns.size(); columnIndex++) {
            if (((CBTableColumn)this.m_tableColumns.get(columnIndex)).isPKColumn()) {
                if (null == ((CBTableColumn)this.m_tableColumns.get(columnIndex)).getQueryString()) {
                    PKColumnPtrCache = (CBTableColumn)this.m_tableColumns.get(columnIndex);
                    PKAliasCache = new StringBuilder(this.m_client.getDataEngineRef().getColumnAliasGenerator().addAlias());
                } else {
                    PKAliasCache = new StringBuilder(((CBTableColumn)this.m_tableColumns.get(columnIndex)).getQueryString());
                    hasPKInProjection = true;
                }
            }
            
            if ((isDMLResultSet()) || (getDataNeeded(columnIndex))) {
                neededColumnCount++;
                CBTableColumn neededColumn = (CBTableColumn)this.m_tableColumns.get(columnIndex);
                String colExpr = new CBValueExprBuilder(this.m_client.getDataEngineRef(), this.m_letClauses, emptyTableCopyMap, this.m_paramValues, this.m_log).buildColumnExpression(neededColumn);
 
                String selectAlias = neededColumn.getSelectAlias();
                if (selectAliases.isEmpty()) {
                    if (neededColumn.isQueryStringAnAlias()) {
                        neededColumn.setSelectAlias(colExpr);
                        if (selectAliases.contains(colExpr)) {
                            continue;
                        }
                    } else {
                        String colName = neededColumn.getName();
                        selectAlias = this.m_client.getDataEngineRef().getColumnAliasGenerator().addAlias(colName);
                        neededColumn.setSelectAlias(selectAlias);
                    }
                } else {
                    if (selectAliases.contains(colExpr)) {
                        continue;
                    }
                }
                
                if (comma) {
                    parseDownBuilder.append(",");
                }
                
                comma = true;
                if (neededColumn.isQueryStringAnAlias()) {
                    if (!colExpr.equals(selectAlias)) {}
                    parseDownBuilder.append(colExpr);
                } else {
                    parseDownBuilder.append(colExpr).append(" ").append(selectAlias);
                }
            }
        }
        
        if (shouldSelectRowSourceValue()) {
            parseDownBuilder.append(", ").append(this.m_rowSourceAlias);
        }
        
        if (neededColumnCount == 0) {
            parseDownBuilder.append(" 1 ");
        }
        
        if (detectArrayDimension()) {
            CBAliasGenerator arrayAliasGenerator = new CBAliasGenerator("$sb_isArray");
            for (int keyColIdx = 0; keyColIdx < this.m_keyColumns.size(); keyColIdx++) {
                if ((keyColIdx != 0) && (keyColIdx != this.m_keyColumns.size() - 1)) {
                    String alias = arrayAliasGenerator.addAlias();
                    String trimAlias = alias.substring(1, alias.length() - 1);
                    CBTableColumn currentKeyColumn = (CBTableColumn)this.m_keyColumns.get(keyColIdx);
                    this.m_detectArrayAliasMap.put(currentKeyColumn, trimAlias);
                    
                    String sourceNameToCheck = "";
                    CBName srcNamePtr = currentKeyColumn.getSourceName();
                    
                    while (null != srcNamePtr.getParentName()) {
                        if (srcNamePtr.isAttribute()) {
                            if ((sourceNameToCheck.length() != 0) && (sourceNameToCheck.charAt(0) == '`')) {
                                sourceNameToCheck = "." + sourceNameToCheck;
                            }
                            
                            sourceNameToCheck = srcNamePtr.getAsAttribute().getAttributeName() + sourceNameToCheck;
                        }
                        srcNamePtr = srcNamePtr.getParentName();
                    }
                    sourceNameToCheck = srcNamePtr.getAlias() + "." + sourceNameToCheck;
                    parseDownBuilder.append(", ISARRAY(").append(sourceNameToCheck).append(") ").append(alias);
                }
            }
        }
        
        parseDownBuilder.append(" FROM ");
        
        CBName stackPtr = null;
        Stack<CBName> tableNameParts = new Stack<>();
        stackPtr = this.m_tableSourceNameList;
        
        while (null != stackPtr.getParentName()) {

            tableNameParts.push(stackPtr);
            stackPtr = stackPtr.getParentName();
        }
        
        tableNameParts.push(stackPtr);

        boolean isBase = true;
        String previousTableAlias = null;
        while (tableNameParts.size() > 0) {

            CBName currentNode = (CBName)tableNameParts.pop();
            if (isBase) {
                parseDownBuilder.append(currentNode.getAsAttribute().getAttributeName()).append(" ").append(currentNode.getAlias());
                previousTableAlias = currentNode.getAlias();
            }
            
            if ((!isBase) && (currentNode.isAttribute()) && (null != currentNode.getAlias())) {
                parseDownBuilder.append(" UNNEST ").append(previousTableAlias).append(".").append(currentNode.getAsAttribute().getAttributeName()).append(" ").append(currentNode.getAlias());
                previousTableAlias = currentNode.getAlias();
            }
            
            if ((isBase) || (!currentNode.isArrayDimension()) || (currentNode.getAsArrayDimension().getDimension() != 1)) {
                if ((!isBase) && (currentNode.isArrayDimension()) && (currentNode.getAsArrayDimension().getDimension() > 1)) {
                    parseDownBuilder.append(" UNNEST ").append(previousTableAlias).append(" ").append(currentNode.getAlias());
                    previousTableAlias = currentNode.getAlias();
                }
                isBase = false;
            }
        }
        
        if (neededColumnCount > 0) {
            StringBuilder typeColumnAlias = new StringBuilder();
            buildLetClauses(typeColumnAlias, parseDownBuilder, PKAliasCache, hasTypeColumn, hasPKInProjection, PKColumnPtrCache);
            
            parseDownBuilder.append(" WHERE ");
            parseDownBuilder.append("(").append(PKAliasCache).append("!='").append("~~~SchemaMap").append("')");
            
            if (null != this.m_whereClauses) {
                parseDownBuilder.append("AND");
                for (int index = 0; index < this.m_whereClauses.size(); index++) {
                    parseDownBuilder.append((String)this.m_whereClauses.get(index));
                }
            }
            
            if (hasTypeColumn) {
                if (null != this.m_tableDifferentiatorPair) {
                    parseDownBuilder.append("AND(").append(typeColumnAlias).append(" = '").append((String)this.m_tableDifferentiatorPair.value()).append("'").append(")");
                }
            }
        } else {
            StringBuilder typeColumnAlias = new StringBuilder();
            buildLetClauses(typeColumnAlias, parseDownBuilder, PKAliasCache, hasTypeColumn, hasPKInProjection, PKColumnPtrCache);
            if (hasTypeColumn) {
                if (this.m_isVirtualTable) {
                    if (null != this.m_tableMeta.getColumnIdentifierName()) {
                        CBName srcNamePtr = this.m_tableSourceNameList;
                        while (null != srcNamePtr.getParentName()) {
                            srcNamePtr = srcNamePtr.getParentName();
                        }
                        
                        parseDownBuilder.append(" where ").append(srcNamePtr.getAlias()).append(".`").append(this.m_tableMeta.getColumnIdentifierName()).append("` = '").append(this.m_tableMeta.getColumnIdentifierValue()).append("'");

                        if (null != this.m_whereClauses) {
                            parseDownBuilder.append(" AND ");
                            for (int index = 0; index < this.m_whereClauses.size(); index++) {
                                parseDownBuilder.append((String)this.m_whereClauses.get(index));
                            }
                        }
                    } else if (null != this.m_whereClauses) {
                        parseDownBuilder.append(" where ");
                        for (int index = 0; index < this.m_whereClauses.size(); index++) {
                            parseDownBuilder.append((String)this.m_whereClauses.get(index));
                        }
                    }
                } else if (null != this.m_tableMeta.getColumnIdentifierName()) {
                    parseDownBuilder.append(" where `").append(this.m_tableMeta.getColumnIdentifierName()).append("` = '").append(this.m_tableMeta.getColumnIdentifierValue()).append("'");

                    if (null != this.m_whereClauses) {
                        parseDownBuilder.append(" AND ");
                        for (int index = 0; index < this.m_whereClauses.size(); index++) {
                            parseDownBuilder.append((String)this.m_whereClauses.get(index));
                        }
                    }
                } else if (null != this.m_whereClauses) {
                    parseDownBuilder.append(" where ");
                    for (int index = 0; index < this.m_whereClauses.size(); index++) {
                        parseDownBuilder.append((String)this.m_whereClauses.get(index));
                    }
                }
            } else if (null != this.m_whereClauses) {
                parseDownBuilder.append(" where ");
                for (int index = 0; index < this.m_whereClauses.size(); index++) {
                    parseDownBuilder.append((String)this.m_whereClauses.get(index));
                }
            }
        }

        LogUtilities.logInfo(parseDownBuilder.toString(), this.m_log);
        fetchNeededData(parseDownBuilder);
        
        LogUtilities.logInfo("###############  Build Query End ##################", this.m_log);
        System.out.println(parseDownBuilder.toString());
    }
    
    private void buildLetClauses(StringBuilder typeColumnAlias, StringBuilder sb, StringBuilder PKAliasCache, boolean hasTypeColumn, boolean hasPKInProjection, CBTableColumn PKColumnPtrCache) throws ErrorException {

        boolean comma = false;
        if (this.m_letClauses.size() > 0) {
            sb.append(" LET ");
            Iterator<Map.Entry<String, CBTableColumn>> letMapItr = this.m_letClauses.entrySet().iterator();
            while (letMapItr.hasNext()) {
                Map.Entry<String, CBTableColumn> pair = letMapItr.next();
                if (comma) {
                    sb.append(",");
                }
                comma = true;
                sb.append((String)pair.getKey()).append(" = ").append(((CBTableColumn)pair.getValue()).getValueReferenceString());
            }
            
            if (hasTypeColumn) {
                if (comma) {
                    sb.append(",");
                }
                typeColumnAlias.append(this.m_client.getDataEngineRef().getColumnAliasGenerator().addAlias("TableType"));
                String coltypeColumnExpr = ((CBTableColumn)this.m_tableDifferentiatorPair.key()).getValueReferenceString();
                sb.append(typeColumnAlias).append(" = ").append(coltypeColumnExpr);
            }
            
            if (!hasPKInProjection) {
                sb.append(",").append(PKAliasCache).append(" = ").append(PKColumnPtrCache.getValueReferenceString());
            }
        }
    }
    
    private void fetchNeededData(StringBuilder query) throws ErrorException { 
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        HashMap<String, String> namedParamValue = null;
        if (!this.m_paramValues.isEmpty()) {
            namedParamValue = new HashMap<>();
            for (CBParamValue param : this.m_paramValues) {
                namedParamValue.put(param.getParamName(), param.getParamValue());
            }
        }
        
        try {
            N1QLQueryResult queryResult = this.m_client.executeStatement(query.toString(), null, namedParamValue);
            
            this.m_rowCount = queryResult.getRowCount();
            JsonNode rowResult = queryResult.allRowsRawJson();
            if (null != rowResult) {
                this.m_allRowDataStructure = new JsonNode[rowResult.size()];
                for (int rowIndex = 0; rowIndex < rowResult.size(); rowIndex++) {
                    this.m_allRowDataStructure[rowIndex] = rowResult.get(rowIndex);
                }
            }
        } catch (ErrorException ex) { 
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.DATA_FETCH_INIT_ERR.name(), new String[] { "initialization error" });
            err.initCause(ex);
            throw err;
        }
    }
  




  private void fixNames()
    throws ErrorException
  {
/* 1386 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    


/* 1390 */     CBName baseNodePtr = null;
/* 1391 */     baseNodePtr = this.m_tableSourceNameList;
/* 1392 */     while (null != baseNodePtr.getParentName())
    {

/* 1395 */       if (null == baseNodePtr.getAlias())
      {
/* 1397 */         if (baseNodePtr.isArrayDimension())
        {

/* 1400 */           this.m_isVirtualTable = true;
/* 1401 */           String currentAlias = this.m_client.getDataEngineRef().getTableAliasGenerator().addAlias();
          


/* 1405 */           baseNodePtr.setAlias(currentAlias);
          

/* 1408 */           if (baseNodePtr.getParentName().isAttribute())
          {
/* 1410 */             baseNodePtr.getParentName().setAlias(currentAlias);
          }
          
        }
/* 1414 */         else if ((baseNodePtr.isAttribute()) && (null != baseNodePtr.getParentName()))
        {
/* 1416 */           String currentAlias = this.m_client.getDataEngineRef().getTableAliasGenerator().addAlias();
          


/* 1420 */           baseNodePtr.setAlias(currentAlias);
        }
      }
      
/* 1424 */       baseNodePtr = baseNodePtr.getParentName();
    }
    
/* 1427 */     String baseAlias = this.m_client.getDataEngineRef().getTableAliasGenerator().addAlias();
    


/* 1431 */     baseNodePtr.setAlias(baseAlias);
    


/* 1435 */     for (int columnIndex = 0; columnIndex < this.m_tableColumns.size(); columnIndex++)
    {
/* 1437 */       CBTableColumn tempCol = (CBTableColumn)this.m_tableColumns.get(columnIndex);
      

/* 1440 */       tempCol.rebaseSourceName(this.m_tableSourceNameList);
    }
    
/* 1443 */     if ((null == this.m_tableDifferentiatorPair) && (this.m_isVirtualTable))
    {
/* 1445 */       if (null != this.m_tableMeta.getColumnIdentifierValue())
      {
/* 1447 */         String parentTableName = this.m_tableMeta.getColumnIdentifierValue();
/* 1448 */         String typeColumnName = this.m_tableMeta.getColumnIdentifierName();
/* 1449 */         ArrayList<CBColumnMetadata> tempParentColumnList = this.m_client.getColumnsFromSchemaMap(this.m_catalogName, this.m_schemaName, parentTableName);
        



/* 1454 */         for (int columnIndex = 0; columnIndex < tempParentColumnList.size(); columnIndex++)
        {
/* 1456 */           CBColumnMetadata currentColumn = (CBColumnMetadata)tempParentColumnList.get(columnIndex);
/* 1457 */           if (currentColumn.getColumnDSIIName().equals(typeColumnName))
          {
/* 1459 */             CBTableColumn tableColumn = new CBTableColumn(currentColumn, parentTableName);
            

/* 1462 */             tableColumn.rebaseSourceName(this.m_tableSourceNameList);
/* 1463 */             this.m_tableDifferentiatorPair = new Pair(tableColumn, parentTableName);
            

/* 1466 */             break;
          }
        }
      }
    }
    
/* 1472 */     CBName virtualNamePtr = this.m_tableSourceNameList;
/* 1473 */     while ((null == virtualNamePtr.getAlias()) && (virtualNamePtr.getParentName() != null))
    {
/* 1475 */       virtualNamePtr = virtualNamePtr.getParentName();
    }
/* 1477 */     this.m_rowSourceAlias = virtualNamePtr.getAlias();
  }
  




  private void setCatalogName(String catalogName)
  {
/* 1486 */     this.m_catalogName = catalogName;
  }
  




  private void setSchemaName(String schemaName)
  {
/* 1495 */     this.m_schemaName = schemaName;
  }
  




  private void setTableName(String tableName)
  {
/* 1504 */     this.m_tableName = tableName;
  }
}