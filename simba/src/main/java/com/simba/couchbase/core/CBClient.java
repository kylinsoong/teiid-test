 package com.simba.couchbase.core;
 
 import com.fasterxml.jackson.databind.JsonNode;
 import com.simba.couchbase.client.N1QLClient;
 import com.simba.couchbase.client.N1QLPlan;
 import com.simba.couchbase.client.N1QLQueryResult;
 import com.simba.couchbase.client.N1QLQueryRow;
 import com.simba.couchbase.client.N1QLRequestType;
 import com.simba.couchbase.client.N1QLRowCountSet;
 import com.simba.couchbase.dataengine.CBSQLDataEngine;
 import com.simba.couchbase.dataengine.resultset.CBResultSet;
 import com.simba.couchbase.dataengine.resultset.CBWriteResultSet;
 import com.simba.couchbase.exceptions.CBJDBCMessageKey;
 import com.simba.couchbase.schemamap.metadata.CBCatalogMetadata;
 import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
 import com.simba.couchbase.schemamap.metadata.CBSMMetadata;
 import com.simba.couchbase.schemamap.metadata.CBSchemaMetadata;
 import com.simba.couchbase.schemamap.metadata.CBTableMetadata;
 import com.simba.couchbase.utils.CBQueryUtils;
 import com.simba.dsi.dataengine.utilities.ColumnMetadata;
 import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
 import com.simba.sqlengine.dsiext.dataengine.OpenTableType;
 import com.simba.support.ILogger;
 import com.simba.support.IWarningListener;
 import com.simba.support.LogUtilities;
 import com.simba.support.exceptions.ErrorException;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;

 public class CBClient {
     
     private CBSQLDataEngine m_dataEngine;
     private ILogger m_log;
     private N1QLClient m_N1QLClient;
     private CBConnectionSettings m_settings;
     private CBSMMetadata m_schemaMapMeta;
     private IWarningListener m_warningListener;

     public CBClient(CBConnectionSettings settings, ILogger log, IWarningListener warningListener) throws ErrorException {

         LogUtilities.logFunctionEntrance(log, new Object[0]);

         this.m_N1QLClient = new N1QLClient(log, settings);
         this.m_settings = settings;
         this.m_log = log;
         this.m_warningListener = warningListener;
         
         openSession();    
     }
     
     public N1QLClient getN1QLClient() {
         return this.m_N1QLClient;    
     }
     
     public boolean disconnect() throws ErrorException {
         
         LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
         
         try {
             this.m_N1QLClient.disconnect();    
         } catch (Exception ex) {
             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_DISCONNECT_ERR.name(), new String[] { "disconnect fail" });
             err.initCause(ex);
             throw err;    
         }
         return true;    
     }
     
     public N1QLQueryResult executeStatement(String query, ArrayList<String> posParams, HashMap<String, String> namedParams)
     throws ErrorException
   {
/* 163 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
     try
     {
/* 166 */       N1QLQueryResult executionResult = this.m_N1QLClient.executeStatementDirectly(query, true, posParams, namedParams);
       
 
 
 
 
/* 172 */       ArrayList<JsonNode> executionError = executionResult.getErrorList();
       
/* 174 */       if ((null != executionError) && (executionError.size() != 0))
       {
/* 176 */         for (JsonNode currError : executionError)
         {
/* 178 */           if (currError.get("code").toString().equals("12003"))
           {
/* 180 */             throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_NO_KEYSPACE_ERR.name(), currError.toString());
           }
           
 
 
/* 185 */           if (currError.get("code").toString().equals("4000"))
           {
/* 187 */             throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_NO_PRIMARY_INDEX_ERR.name(), currError.toString());
           }
           
 
 
/* 192 */           if (currError.get("code").toString().equals("10000"))
           {
/* 194 */             throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_AUTHORIZATION_ERR.name(), currError.toString());
           }
         }
         
 
 
/* 200 */         throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_PREPARE_FAIL_ERR.name(), " execute statement fail");
       }
       
 
 
 
/* 206 */       return executionResult;
     }
     catch (ErrorException err)
     {
/* 210 */       throw err;
     }
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
 
   public N1QLQueryResult executePreparedStatement(N1QLPlan plan, ArrayList<String> posParams, HashMap<String, String> namedParams)
     throws ErrorException
   {
/* 230 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { plan });
     
     try
     {
/* 234 */       N1QLQueryResult executePreparePlan = this.m_N1QLClient.executePrepareStatement(plan, true, posParams, namedParams);
       
 
 
 
 
/* 240 */       ArrayList<JsonNode> preapareExeError = executePreparePlan.getErrorList();
/* 241 */       if ((null != preapareExeError) && (preapareExeError.size() != 0))
       {
/* 243 */         throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_PREPARE_FAIL_GEN_ERR.name(), " execute prepare statement fail");
       }
       
 
 
 
/* 249 */       return executePreparePlan;
 
     }
     catch (ErrorException err)
     {
/* 254 */       throw err;
     }
     catch (Exception ex)
     {
/* 258 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_EXE_PREPARE_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 261 */       err.initCause(ex);
/* 262 */       throw err;
     }
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
   public N1QLRowCountSet executeUpdate(String query, ArrayList<String> posParams, HashMap<String, String> namedParams)
     throws ErrorException
   {
/* 281 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
     
     try
     {
/* 285 */       N1QLRowCountSet executeUpdateResult = this.m_N1QLClient.executeUpdate(query, posParams, namedParams);
       
 
 
 
/* 290 */       ArrayList<JsonNode> preapareExeError = executeUpdateResult.getErrorList();
/* 291 */       if ((null != preapareExeError) && (preapareExeError.size() != 0))
       {
/* 293 */         for (JsonNode currError : preapareExeError)
         {
/* 295 */           if (currError.get("code").toString().equals("12003"))
           {
/* 297 */             throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_NO_KEYSPACE_ERR.name(), currError.toString());
           }
           
 
 
/* 302 */           if (currError.get("code").toString().equals("4000"))
           {
/* 304 */             throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_NO_PRIMARY_INDEX_ERR.name(), currError.toString());
           }
           
 
 
/* 309 */           if (currError.get("code").toString().equals("10000"))
           {
/* 311 */             throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_AUTHORIZATION_ERR.name(), currError.toString());
           }
         }
         
 
 
/* 317 */         throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_FAIL_GEN_ERR.name(), "executeupdate fail");
       }
       
 
 
 
/* 323 */       return executeUpdateResult;
     }
     catch (ErrorException err)
     {
/* 327 */       throw err;
     }
     catch (Exception ex)
     {
/* 331 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_EXE_PREPARE_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 334 */       err.initCause(ex);
/* 335 */       throw err;
     }
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
 
   public N1QLPlan preparedStatement(String query, ArrayList<String> posParams, HashMap<String, String> namedParams)
     throws ErrorException
   {
/* 355 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
     
     try
     {
/* 359 */       N1QLPlan executionPlan = this.m_N1QLClient.prepareStatement(query, posParams, namedParams);
       
 
 
 
/* 364 */       ArrayList<JsonNode> preapareError = executionPlan.getErrorList();
/* 365 */       if ((null != preapareError) && (preapareError.size() != 0))
       {
/* 367 */         throw CBQueryUtils.buildServerErrorMessage(preapareError, CBJDBCMessageKey.QUERY_PREPARE_FAIL_ERR.name(), " prepare statement fail");
       }
       
 
 
 
/* 373 */       return executionPlan;
     }
     catch (ErrorException err)
     {
/* 377 */       throw err;
     }
     catch (Exception ex)
     {
/* 381 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_PREPARE_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 384 */       err.initCause(ex);
/* 385 */       throw err;
     }
   }
   
 
 
 
 
 
   public ArrayList<String> getCatalogs()
     throws ErrorException
   {
/* 397 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
     
/* 399 */     ArrayList<String> catalogs = new ArrayList();
     
 
     try
     {
/* 404 */       N1QLQueryResult catalogResult = this.m_N1QLClient.executeStatementDirectly("SELECT DISTINCT namespace_id FROM system:keyspaces", false);
       
 
 
 
/* 409 */       ArrayList<JsonNode> schemaError = catalogResult.getErrorList();
/* 410 */       if ((null != schemaError) && (schemaError.size() != 0))
       {
/* 412 */         throw CBQueryUtils.buildServerErrorMessage(schemaError, CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_ERR.name(), "catalog function fail");
       }
       
 
 
 
 
/* 419 */       ArrayList<N1QLQueryRow> catalogQueryRowList = catalogResult.allRowsRawData();
/* 420 */       for (N1QLQueryRow schmeaRow : catalogQueryRowList)
       {
/* 422 */         String schemaName = schmeaRow.value().get("namespace_id").textValue();
         
/* 424 */         catalogs.add(schemaName);
       }
       
 
/* 428 */       Collections.sort(catalogs);
     }
     catch (ErrorException err)
     {
/* 432 */       throw err;
     }
     catch (Exception ex)
     {
/* 436 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGONLY_FAIL_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 439 */       err.initCause(ex);
/* 440 */       throw err;
     }
     
/* 443 */     return catalogs;
   }
   
 
 
 
 
 
   public ArrayList<String> getCatalogsFromSchemaMap()
     throws ErrorException
   {
/* 454 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
     
     try
     {
/* 458 */       return this.m_schemaMapMeta.getCatalogs();
     }
     catch (Exception ex)
     {
/* 462 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGONLY_FAIL_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 465 */       err.initCause(ex);
/* 466 */       throw err;
     }
   }
   
 
 
 
 
 
 
 
 
 
 
 
   public ArrayList<CBColumnMetadata> getColumnsFromSchemaMap(String catalogName, String schemaName, String tableName)
   {
/* 483 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
     
     try
     {
/* 487 */       return this.m_schemaMapMeta.getColumnMetaList(catalogName, schemaName, tableName);
 
 
 
     }
     catch (Exception ex)
     {
 
 
/* 496 */       LogUtilities.logError(ex, this.m_log); }
/* 497 */     return new ArrayList();
   }
   
 
 
 
 
 
   public CBSQLDataEngine getDataEngineRef()
   {
/* 507 */     return this.m_dataEngine;
   }
   
 
 
 
 
 
 
   public ArrayList<String> getSchemas(String catalog)
     throws ErrorException
   {
/* 519 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "catalog name: " + catalog });
     
/* 521 */     ArrayList<String> schemas = new ArrayList();
/* 522 */     StringBuilder schemaQuery = new StringBuilder("SELECT name, namespace_id, store_id FROM system:keyspaces");
     
     try
     {
/* 526 */       if (null != catalog)
       {
/* 528 */         schemaQuery.append(" where ").append("namespace_id").append("='").append(catalog).append("'");
       }
       
 
 
 
 
 
/* 536 */       N1QLQueryResult schemaResult = this.m_N1QLClient.executeStatementDirectly(schemaQuery.toString(), false);
       
 
 
 
/* 541 */       ArrayList<JsonNode> schemaError = schemaResult.getErrorList();
/* 542 */       if ((null != schemaError) && (schemaError.size() != 0))
       {
/* 544 */         throw CBQueryUtils.buildServerErrorMessage(schemaError, CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_ERR.name(), "catalog function fail");
       }
       
 
 
 
 
/* 551 */       ArrayList<N1QLQueryRow> schemaQueryRowList = schemaResult.allRowsRawData();
/* 552 */       for (N1QLQueryRow schmeaRow : schemaQueryRowList)
       {
/* 554 */         String schemaName = schmeaRow.value().get("name").textValue();
         
/* 556 */         schemas.add(schemaName);
       }
       
/* 559 */       Collections.sort(schemas);
     }
     catch (ErrorException err)
     {
/* 563 */       throw err;
     }
     catch (Exception ex)
     {
/* 567 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 570 */       err.initCause(ex);
/* 571 */       throw err;
     }
     
/* 574 */     return schemas;
   }
   
 
 
 
 
 
 
   public ArrayList<String> getSchemasFromSchemaMap(String catalogName)
     throws ErrorException
   {
/* 586 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
     
     try
     {
/* 590 */       ArrayList<String> schemas = this.m_schemaMapMeta.getSchemaNameList(catalogName);
/* 591 */       Collections.sort(schemas);
/* 592 */       return schemas;
     }
     catch (Exception ex)
     {
/* 596 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 599 */       err.initCause(ex);
/* 600 */       throw err;
     }
   }
   
 
 
 
 
   public CBSMMetadata getSchemaMapMeta()
   {
/* 610 */     return this.m_schemaMapMeta;
   }
   
 
 
 
   public CBConnectionSettings getSettings()
   {
/* 618 */     return this.m_settings;
   }
   

    public N1QLQueryResult getTables(String columnIdentifierName, String catalogName, String schemaName) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "Schema: " + schemaName });
        
        StringBuilder tableQuery = new StringBuilder("SELECT DISTINCT ");
        if ((null != schemaName) && (null != catalogName)) {
            tableQuery.append(columnIdentifierName).append(" FROM `").append(catalogName).append("`:`").append(schemaName).append("`");    
        }
        
        try {
            N1QLQueryResult tableResult = this.m_N1QLClient.executeStatementDirectly(tableQuery.toString(), false);
            
            ArrayList<JsonNode> tableError = tableResult.getErrorList();
            if ((null != tableError) && (tableError.size() != 0)) {
                throw CBQueryUtils.buildServerErrorMessage(tableError, CBJDBCMessageKey.CATALOGFUN_TABLEONLY_FAIL_ERR.name(), "cata function fail");    
            }
            
            ArrayList<N1QLQueryRow> tableRowList = tableResult.allRowsRawData();
            if ((null != tableRowList) && (tableRowList.size() == 1)) {
                int typeSize = ((N1QLQueryRow)tableRowList.get(0)).value().size();
                if (typeSize == 0) {
                    tableResult.setHasTypeIdentifier(false);    
                } else {
                    tableResult.setHasTypeIdentifier(true);    
                }    
            } else {
                tableResult.setHasTypeIdentifier(true);    
            }
            
            return tableResult;    
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_TABLEONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
     }
   }
   
    
   public ArrayList<String> getTablesFromSchemaMap(String catalogName, String schemaName)
     throws ErrorException
   {
/* 711 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "Schema: " + schemaName });
     
     try
     {
/* 715 */       ArrayList<String> tables = this.m_schemaMapMeta.getTableNameList(catalogName, schemaName);
       
 
 
/* 719 */       Collections.sort(tables);
/* 720 */       return tables;
     }
     catch (Exception ex)
     {
/* 724 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_TABLEONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
       
 
/* 727 */       err.initCause(ex);
/* 728 */       throw err;
     }
   }

   
    public N1QLQueryResult getTableSample(String catalogName, String schemaName, String tableName, String columnIdentifierName, int sampleSize, boolean hasTypeIdentifier) throws ErrorException {
        
        StringBuilder columnQuery = new StringBuilder();
        columnQuery.append("SELECT meta(`").append(schemaName).append("`).id as PK, ").append("`").append(schemaName).append("`").append(" FROM `").append(catalogName).append("`:`").append(schemaName).append("`");
  
        if (hasTypeIdentifier) {
            columnQuery.append(" WHERE ").append(columnIdentifierName).append("='").append(tableName).append("'");
            columnQuery.append(" and ").append("meta(`").append(schemaName).append("`).id !='").append("~~~SchemaMap").append("'");    
        } else {
            columnQuery.append(" WHERE ").append("meta(`").append(schemaName).append("`).id !='").append("~~~SchemaMap").append("'");    
        }
        
        columnQuery.append(" LIMIT ").append(sampleSize);
        try {
            N1QLQueryResult sampleResult = this.m_N1QLClient.executeStatementDirectly(columnQuery.toString(), false);
            
            ArrayList<JsonNode> executionError = sampleResult.getErrorList();
            if ((null != executionError) && (executionError.size() != 0)) {
                throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.CATALOGFUN_CATALOGCOLUMN_FAIL_ERR.name(), "catalog function fail");    
            }
            
            return sampleResult;    
        } catch (ErrorException err) {
            throw err;    
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGCOLUMN_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;    
        }    
    }
   
 
 
 
 
 
   public IWarningListener getWarningListener()
   {
/* 836 */     return this.m_warningListener;
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   public DSIExtJResultSet openTable(String catalogName, String schemaName, String tableName, ArrayList<ColumnMetadata> columnListCache, OpenTableType openType)
     throws ErrorException
   {
/* 858 */     if ((null == catalogName) || (catalogName.equalsIgnoreCase("")))
     {
 
/* 861 */       catalogName = this.m_settings.m_catalog;
     }
     
 
/* 865 */     ArrayList<CBColumnMetadata> columns = new ArrayList();
/* 866 */     CBCatalogMetadata catalogMeta = this.m_schemaMapMeta.getCatalogMeta(catalogName);
     
/* 868 */     if (null == catalogMeta)
     {
/* 870 */       return null;
     }
     
/* 873 */     CBTableMetadata tableMeta = null;
/* 874 */     CBSchemaMetadata schemaMeta = null;
/* 875 */     for (String bucketName : catalogMeta.getSchemaNameList())
     {
/* 877 */       schemaMeta = catalogMeta.getSchemaMeta(bucketName);
/* 878 */       if (null != schemaMeta)
       {
/* 880 */         tableMeta = schemaMeta.getTableMeta(tableName);
/* 881 */         if (null != tableMeta)
         {
 
/* 884 */           columns = tableMeta.getColumnMetadataList();
/* 885 */           break;
         }
       }
     }
     
 
/* 891 */     columnListCache.addAll(columns);
     
/* 893 */     if ((null == columns) || (columns.isEmpty()))
     {
/* 895 */       return null;
     }
     
 
/* 899 */     if (OpenTableType.READ_ONLY == openType)
     {
/* 901 */       return new CBResultSet(schemaMeta, tableMeta, columns, this, this.m_log);
     }
     
 
 
 
 
 
 
/* 910 */     return new CBWriteResultSet(schemaMeta, tableMeta, columns, this, this.m_log);
   }
   
 
 
 
 
 
 
 
 
 
   public void setDataEngineRef(CBSQLDataEngine cbEngine)
   {
/* 924 */     this.m_dataEngine = cbEngine;
   }
   
 
 
 
   public void setSchemaMap(CBSMMetadata schemaMapMeta)
   {
/* 932 */     this.m_schemaMapMeta = schemaMapMeta;
   }
   
 
 
 
 
 
 
 
 
 
 
   private boolean openSession()
     throws ErrorException
   {
/* 948 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
     
     try
     {
/* 952 */       N1QLQueryResult authenticationResult = this.m_N1QLClient.connect(this.m_settings.m_host, this.m_settings.m_port, N1QLRequestType.statement.name(), "SELECT * from system:keyspaces");
       
 
 
 
 
 
/* 959 */       boolean isValidSchema = false;
/* 960 */       ArrayList<N1QLQueryRow> queryResult = authenticationResult.allRowsRawData();
/* 961 */       for (N1QLQueryRow queryResultIter : queryResult)
       {
/* 963 */         if (queryResultIter.value().toString().contains("namespace_id\":\"" + this.m_settings.m_catalog))
         {
/* 965 */           isValidSchema = true;
/* 966 */           break;
         }
       }
/* 969 */       if (!isValidSchema)
       {
/* 971 */         LogUtilities.logDebug("User-defined schema was invalid. The \"default\" schema will be used", this.m_log);
/* 972 */         this.m_settings.m_catalog = "default";
       }
       
/* 975 */       ArrayList<JsonNode> error = authenticationResult.getErrorList();
       
/* 977 */       if ((null != error) && (0 != error.size()))
       {
/* 979 */         throw CBQueryUtils.buildServerErrorMessage(error, CBJDBCMessageKey.CONN_FAIL_ERR.name(), "connection fail");
       }
       
 
     }
     catch (ErrorException ex)
     {
 
/* 987 */       throw ex;
     }
     catch (Exception ex)
     {
/* 991 */       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_FAIL_ERR.name(), new String[] { ex.getMessage() });
     }
     
 
 
/* 996 */     return true;
   }
 }
