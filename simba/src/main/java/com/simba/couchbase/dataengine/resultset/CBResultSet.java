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
import com.simba.couchbase.schemamap.parser.CBArrayDimension;
import com.simba.couchbase.schemamap.parser.CBAttribute;
import com.simba.couchbase.schemamap.parser.CBName;
import com.simba.couchbase.schemamap.parser.generated.Parser;
import com.simba.couchbase.utils.CBDataTypeUtils;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
  








  public void addLetClauses(Map<String, CBTableColumn> newLetClauses)
  {
/*  338 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  339 */     this.m_letClauses.putAll(newLetClauses);
  }
  







  public void appendRow()
    throws ErrorException
  {
/*  352 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  353 */     throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  











  public void close()
  {
/*  369 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  









  public void closeCursor()
    throws ErrorException
  {
/*  384 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  








  public boolean detectArrayDimension()
  {
/*  397 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  398 */     return false;
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
  




  public boolean isDMLResultSet()
  {
/*  698 */     return false;
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
  










  public void setCursorType(CursorType cursorType)
    throws ErrorException
  {
/*  752 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  753 */     if (CursorType.FORWARD_ONLY != cursorType)
    {
/*  755 */       throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(cursorType));
    }
    


/*  760 */     fixNames();
/*  761 */     buildQuery();
  }
  



  public void setLimitExpression(String limitExpr)
  {
/*  769 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  







  public boolean shouldSelectRowSourceValue()
  {
/*  781 */     return false;
  }
  




















  public boolean writeData(int column, DataWrapper data, long offset, boolean isDefault)
    throws ErrorException
  {
/*  807 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  808 */     throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
  }
  










  public void reset()
    throws ErrorException
  {
/*  824 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
  }
  








  private void buildQuery()
    throws ErrorException
  {
/*  838 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    

/*  841 */     StringBuilder parseDownBuilder = new StringBuilder();
/*  842 */     parseDownBuilder.append("SELECT ");
/*  843 */     boolean hasTypeColumn = false;
    


/*  847 */     StringBuilder PKAliasCache = null;
/*  848 */     CBTableColumn PKColumnPtrCache = null;
/*  849 */     boolean hasPKInProjection = false;
    

/*  852 */     Map<DSIExtJResultSet, CBResultSet> emptyTableCopyMap = new HashMap();
/*  853 */     if (null != this.m_tableDifferentiatorPair)
    {
/*  855 */       hasTypeColumn = true;
    }
    



/*  861 */     boolean comma = false;
/*  862 */     Set<String> selectAliases = new HashSet();
/*  863 */     int neededColumnCount = 0;
/*  864 */     for (int columnIndex = 0; columnIndex < this.m_tableColumns.size(); columnIndex++)
    {

/*  867 */       if (((CBTableColumn)this.m_tableColumns.get(columnIndex)).isPKColumn())
      {
/*  869 */         if (null == ((CBTableColumn)this.m_tableColumns.get(columnIndex)).getQueryString())
        {
/*  871 */           PKColumnPtrCache = (CBTableColumn)this.m_tableColumns.get(columnIndex);
/*  872 */           PKAliasCache = new StringBuilder(this.m_client.getDataEngineRef().getColumnAliasGenerator().addAlias());

        }
        else
        {
/*  877 */           PKAliasCache = new StringBuilder(((CBTableColumn)this.m_tableColumns.get(columnIndex)).getQueryString());
          
/*  879 */           hasPKInProjection = true;
        }
      }
      



/*  886 */       if ((isDMLResultSet()) || (getDataNeeded(columnIndex)))
      {




/*  892 */         neededColumnCount++;
/*  893 */         CBTableColumn neededColumn = (CBTableColumn)this.m_tableColumns.get(columnIndex);
        

/*  896 */         String colExpr = new CBValueExprBuilder(this.m_client.getDataEngineRef(), this.m_letClauses, emptyTableCopyMap, this.m_paramValues, this.m_log).buildColumnExpression(neededColumn);
        





/*  903 */         String selectAlias = neededColumn.getSelectAlias();
        

/*  906 */         if (selectAliases.isEmpty())
        {
/*  908 */           if (neededColumn.isQueryStringAnAlias())
          {
/*  910 */             neededColumn.setSelectAlias(colExpr);
/*  911 */             if (selectAliases.contains(colExpr))
            {
              continue;
            }
            

          }
          else
          {
/*  920 */             String colName = neededColumn.getName();
/*  921 */             selectAlias = this.m_client.getDataEngineRef().getColumnAliasGenerator().addAlias(colName);
            

/*  924 */             neededColumn.setSelectAlias(selectAlias);
          }
          
        }
        else {
/*  929 */           if (selectAliases.contains(colExpr)) {
            continue;
          }
        }
        




/*  938 */         if (comma)
        {
/*  940 */           parseDownBuilder.append(",");
        }
/*  942 */         comma = true;
        
/*  944 */         if (neededColumn.isQueryStringAnAlias())
        {
/*  946 */           if (!colExpr.equals(selectAlias)) {}
          





/*  953 */           parseDownBuilder.append(colExpr);
        }
        else
        {
/*  957 */           parseDownBuilder.append(colExpr).append(" ").append(selectAlias);
        }
      }
    }
    





/*  967 */     if (shouldSelectRowSourceValue())
    {

/*  970 */       parseDownBuilder.append(", ").append(this.m_rowSourceAlias);
    }
    



/*  976 */     if (neededColumnCount == 0)
    {
/*  978 */       parseDownBuilder.append(" 1 ");
    }
    

/*  982 */     if (detectArrayDimension())
    {
/*  984 */       CBAliasGenerator arrayAliasGenerator = new CBAliasGenerator("$sb_isArray");
/*  985 */       for (int keyColIdx = 0; keyColIdx < this.m_keyColumns.size(); keyColIdx++)
      {
/*  987 */         if ((keyColIdx != 0) && (keyColIdx != this.m_keyColumns.size() - 1))
        {



/*  992 */           String alias = arrayAliasGenerator.addAlias();
/*  993 */           String trimAlias = alias.substring(1, alias.length() - 1);
/*  994 */           CBTableColumn currentKeyColumn = (CBTableColumn)this.m_keyColumns.get(keyColIdx);
/*  995 */           this.m_detectArrayAliasMap.put(currentKeyColumn, trimAlias);
          
/*  997 */           String sourceNameToCheck = "";
/*  998 */           CBName srcNamePtr = currentKeyColumn.getSourceName();
          


/* 1002 */           while (null != srcNamePtr.getParentName())
          {
/* 1004 */             if (srcNamePtr.isAttribute())
            {
/* 1006 */               if ((sourceNameToCheck.length() != 0) && (sourceNameToCheck.charAt(0) == '`'))
              {
/* 1008 */                 sourceNameToCheck = "." + sourceNameToCheck;
              }
/* 1010 */               sourceNameToCheck = srcNamePtr.getAsAttribute().getAttributeName() + sourceNameToCheck;
            }
            
/* 1013 */             srcNamePtr = srcNamePtr.getParentName();
          }
          
/* 1016 */           sourceNameToCheck = srcNamePtr.getAlias() + "." + sourceNameToCheck;
/* 1017 */           parseDownBuilder.append(", ISARRAY(").append(sourceNameToCheck).append(") ").append(alias);
        }
      }
    }
    





/* 1027 */     parseDownBuilder.append(" FROM ");
    
/* 1029 */     CBName stackPtr = null;
/* 1030 */     Stack<CBName> tableNameParts = new Stack();
/* 1031 */     stackPtr = this.m_tableSourceNameList;
/* 1032 */     while (null != stackPtr.getParentName())
    {
/* 1034 */       tableNameParts.push(stackPtr);
/* 1035 */       stackPtr = stackPtr.getParentName();
    }
/* 1037 */     tableNameParts.push(stackPtr);
    
/* 1039 */     boolean isBase = true;
/* 1040 */     String previousTableAlias = null;
/* 1041 */     while (tableNameParts.size() > 0)
    {
/* 1043 */       CBName currentNode = (CBName)tableNameParts.pop();
      

/* 1046 */       if (isBase)
      {
/* 1048 */         parseDownBuilder.append(currentNode.getAsAttribute().getAttributeName()).append(" ").append(currentNode.getAlias());
        


/* 1052 */         previousTableAlias = currentNode.getAlias();
      }
      

/* 1056 */       if ((!isBase) && (currentNode.isAttribute()) && (null != currentNode.getAlias()))
      {


/* 1060 */         parseDownBuilder.append(" UNNEST ").append(previousTableAlias).append(".").append(currentNode.getAsAttribute().getAttributeName()).append(" ").append(currentNode.getAlias());
        





/* 1067 */         previousTableAlias = currentNode.getAlias();
      }
      
/* 1070 */       if ((isBase) || (!currentNode.isArrayDimension()) || (currentNode.getAsArrayDimension().getDimension() != 1))
      {




/* 1076 */         if ((!isBase) && (currentNode.isArrayDimension()) && (currentNode.getAsArrayDimension().getDimension() > 1))
        {


/* 1080 */           parseDownBuilder.append(" UNNEST ").append(previousTableAlias).append(" ").append(currentNode.getAlias());
          



/* 1085 */           previousTableAlias = currentNode.getAlias();
        }
/* 1087 */         isBase = false;
      }
    }
    
/* 1091 */     if (neededColumnCount > 0)
    {
/* 1093 */       StringBuilder typeColumnAlias = new StringBuilder();
      
/* 1095 */       buildLetClauses(typeColumnAlias, parseDownBuilder, PKAliasCache, hasTypeColumn, hasPKInProjection, PKColumnPtrCache);
      







/* 1104 */       parseDownBuilder.append(" WHERE ");
      

/* 1107 */       parseDownBuilder.append("(").append(PKAliasCache).append("!='").append("~~~SchemaMap").append("')");
      





/* 1114 */       if (null != this.m_whereClauses)
      {
/* 1116 */         parseDownBuilder.append("AND");
        
/* 1118 */         for (int index = 0; index < this.m_whereClauses.size(); index++)
        {
/* 1120 */           parseDownBuilder.append((String)this.m_whereClauses.get(index));
        }
      }
      
/* 1124 */       if (hasTypeColumn)
      {

/* 1127 */         if (null != this.m_tableDifferentiatorPair)
        {

/* 1130 */           parseDownBuilder.append("AND(").append(typeColumnAlias).append(" = '").append((String)this.m_tableDifferentiatorPair.value()).append("'").append(")");


        }
        

      }
      


    }
    else
    {


/* 1145 */       StringBuilder typeColumnAlias = new StringBuilder();
      
/* 1147 */       buildLetClauses(typeColumnAlias, parseDownBuilder, PKAliasCache, hasTypeColumn, hasPKInProjection, PKColumnPtrCache);
      






/* 1155 */       if (hasTypeColumn)
      {

/* 1158 */         if (this.m_isVirtualTable)
        {

/* 1161 */           if (null != this.m_tableMeta.getColumnIdentifierName())
          {
/* 1163 */             CBName srcNamePtr = this.m_tableSourceNameList;
            
/* 1165 */             while (null != srcNamePtr.getParentName())
            {
/* 1167 */               srcNamePtr = srcNamePtr.getParentName();
            }
/* 1169 */             parseDownBuilder.append(" where ").append(srcNamePtr.getAlias()).append(".`").append(this.m_tableMeta.getColumnIdentifierName()).append("` = '").append(this.m_tableMeta.getColumnIdentifierValue()).append("'");
            






/* 1177 */             if (null != this.m_whereClauses)
            {
/* 1179 */               parseDownBuilder.append(" AND ");
              
/* 1181 */               for (int index = 0; index < this.m_whereClauses.size(); index++)
              {
/* 1183 */                 parseDownBuilder.append((String)this.m_whereClauses.get(index));
              }
              
            }
            
          }
/* 1189 */           else if (null != this.m_whereClauses)
          {
/* 1191 */             parseDownBuilder.append(" where ");
            
/* 1193 */             for (int index = 0; index < this.m_whereClauses.size(); index++)
            {
/* 1195 */               parseDownBuilder.append((String)this.m_whereClauses.get(index));
            }
            
          }
          

        }
/* 1202 */         else if (null != this.m_tableMeta.getColumnIdentifierName())
        {
/* 1204 */           parseDownBuilder.append(" where `").append(this.m_tableMeta.getColumnIdentifierName()).append("` = '").append(this.m_tableMeta.getColumnIdentifierValue()).append("'");
          




/* 1210 */           if (null != this.m_whereClauses)
          {
/* 1212 */             parseDownBuilder.append(" AND ");
            
/* 1214 */             for (int index = 0; index < this.m_whereClauses.size(); index++)
            {
/* 1216 */               parseDownBuilder.append((String)this.m_whereClauses.get(index));
            }
            
          }
          
        }
/* 1222 */         else if (null != this.m_whereClauses)
        {
/* 1224 */           parseDownBuilder.append(" where ");
          
/* 1226 */           for (int index = 0; index < this.m_whereClauses.size(); index++)
          {
/* 1228 */             parseDownBuilder.append((String)this.m_whereClauses.get(index));

          }
          

        }
        

      }
/* 1237 */       else if (null != this.m_whereClauses)
      {
/* 1239 */         parseDownBuilder.append(" where ");
        
/* 1241 */         for (int index = 0; index < this.m_whereClauses.size(); index++)
        {
/* 1243 */           parseDownBuilder.append((String)this.m_whereClauses.get(index));
        }
      }
    }
    


/* 1250 */     LogUtilities.logInfo(parseDownBuilder.toString(), this.m_log);
/* 1251 */     fetchNeededData(parseDownBuilder);
  }
  

















  private void buildLetClauses(StringBuilder typeColumnAlias, StringBuilder parseDownBuilder, StringBuilder PKAliasCache, boolean hasTypeColumn, boolean hasPKInProjection, CBTableColumn PKColumnPtrCache)
    throws ErrorException
  {
/* 1274 */     boolean comma = false;
/* 1275 */     if (this.m_letClauses.size() > 0)
    {
/* 1277 */       parseDownBuilder.append(" LET ");
/* 1278 */       Iterator<Map.Entry<String, CBTableColumn>> letMapItr = this.m_letClauses.entrySet().iterator();
/* 1279 */       while (letMapItr.hasNext())
      {
/* 1281 */         Map.Entry<String, CBTableColumn> pair = (Map.Entry)letMapItr.next();
/* 1282 */         if (comma)
        {
/* 1284 */           parseDownBuilder.append(",");
        }
/* 1286 */         comma = true;
/* 1287 */         parseDownBuilder.append((String)pair.getKey()).append(" = ").append(((CBTableColumn)pair.getValue()).getValueReferenceString());
      }
      



/* 1293 */       if (hasTypeColumn)
      {
/* 1295 */         if (comma)
        {
/* 1297 */           parseDownBuilder.append(",");
        }
/* 1299 */         typeColumnAlias.append(this.m_client.getDataEngineRef().getColumnAliasGenerator().addAlias("TableType"));
        




/* 1305 */         String coltypeColumnExpr = ((CBTableColumn)this.m_tableDifferentiatorPair.key()).getValueReferenceString();
        


/* 1309 */         parseDownBuilder.append(typeColumnAlias).append(" = ").append(coltypeColumnExpr);
      }
      





/* 1317 */       if (!hasPKInProjection)
      {
/* 1319 */         parseDownBuilder.append(",").append(PKAliasCache).append(" = ").append(PKColumnPtrCache.getValueReferenceString());
      }
    }
  }
  









  private void fetchNeededData(StringBuilder query)
    throws ErrorException
  {
/* 1336 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    
/* 1338 */     HashMap<String, String> namedParamValue = null;
    

/* 1341 */     if (!this.m_paramValues.isEmpty())
    {
/* 1343 */       namedParamValue = new HashMap();
      
/* 1345 */       for (CBParamValue param : this.m_paramValues)
      {
/* 1347 */         namedParamValue.put(param.getParamName(), param.getParamValue());
      }
    }
    
    try
    {
/* 1353 */       N1QLQueryResult queryResult = this.m_client.executeStatement(query.toString(), null, namedParamValue);
      


/* 1357 */       this.m_rowCount = queryResult.getRowCount();
/* 1358 */       JsonNode rowResult = queryResult.allRowsRawJson();
      
/* 1360 */       if (null != rowResult)
      {
/* 1362 */         this.m_allRowDataStructure = new JsonNode[rowResult.size()];
/* 1363 */         for (int rowIndex = 0; rowIndex < rowResult.size(); rowIndex++)
        {
/* 1365 */           this.m_allRowDataStructure[rowIndex] = rowResult.get(rowIndex);
        }
      }
    }
    catch (ErrorException ex)
    {
/* 1371 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.DATA_FETCH_INIT_ERR.name(), new String[] { "initialization error" });
      

/* 1374 */       err.initCause(ex);
/* 1375 */       throw err;
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