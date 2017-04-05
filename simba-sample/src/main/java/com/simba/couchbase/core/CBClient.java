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
     
     public N1QLQueryResult executeStatement(String query, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

         LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
     
         try {
             N1QLQueryResult executionResult = this.m_N1QLClient.executeStatementDirectly(query, true, posParams, namedParams);
             
             ArrayList<JsonNode> executionError = executionResult.getErrorList();
             if ((null != executionError) && (executionError.size() != 0)) {
                 for (JsonNode currError : executionError) {
                     if (currError.get("code").toString().equals("12003")) { 
                         throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_NO_KEYSPACE_ERR.name(), currError.toString());
                     }
                     
                     if (currError.get("code").toString().equals("4000")) {
                         throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_NO_PRIMARY_INDEX_ERR.name(), currError.toString());
                     }
                     
                     if (currError.get("code").toString().equals("10000")) {
                         throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_AUTHORIZATION_ERR.name(), currError.toString());
                     }
                 }
                 throw CBQueryUtils.buildServerErrorMessage(executionError, CBJDBCMessageKey.QUERY_EXE_PREPARE_FAIL_ERR.name(), " execute statement fail");
             }
             return executionResult;
         } catch (ErrorException err) {
             throw err;
         }
    }
     
    public N1QLQueryResult executePreparedStatement(N1QLPlan plan, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { plan });
     
        try {
            N1QLQueryResult executePreparePlan = this.m_N1QLClient.executePrepareStatement(plan, true, posParams, namedParams);

            ArrayList<JsonNode> preapareExeError = executePreparePlan.getErrorList();
            if ((null != preapareExeError) && (preapareExeError.size() != 0)) {
                throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_PREPARE_FAIL_GEN_ERR.name(), " execute prepare statement fail");
            }
            return executePreparePlan;
        } catch (ErrorException err) {
            throw err;
        }  catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_EXE_PREPARE_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
    }
   
    public N1QLRowCountSet executeUpdate(String query, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
     
        try {
            N1QLRowCountSet executeUpdateResult = this.m_N1QLClient.executeUpdate(query, posParams, namedParams);
 
            ArrayList<JsonNode> preapareExeError = executeUpdateResult.getErrorList();
            if ((null != preapareExeError) && (preapareExeError.size() != 0)) {
                for (JsonNode currError : preapareExeError) {
                    if (currError.get("code").toString().equals("12003")) {
                        throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_NO_KEYSPACE_ERR.name(), currError.toString());
                    }
                    
                    if (currError.get("code").toString().equals("4000")) { 
                        throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_NO_PRIMARY_INDEX_ERR.name(), currError.toString());
                    }
                    
                    if (currError.get("code").toString().equals("10000")) {
                        throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_AUTHORIZATION_ERR.name(), currError.toString());
                    }
                }
                throw CBQueryUtils.buildServerErrorMessage(preapareExeError, CBJDBCMessageKey.QUERY_UPDATE_FAIL_GEN_ERR.name(), "executeupdate fail");
            }
            return executeUpdateResult;
        } catch (ErrorException err) {
            throw err;
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_EXE_PREPARE_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
    }

    public N1QLPlan preparedStatement(String query, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });

        try {
            N1QLPlan executionPlan = this.m_N1QLClient.prepareStatement(query, posParams, namedParams);
            ArrayList<JsonNode> preapareError = executionPlan.getErrorList();
            if ((null != preapareError) && (preapareError.size() != 0)) {
                throw CBQueryUtils.buildServerErrorMessage(preapareError, CBJDBCMessageKey.QUERY_PREPARE_FAIL_ERR.name(), " prepare statement fail");
            }
            return executionPlan;
        } catch (ErrorException err) {
            throw err;
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_PREPARE_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
    }

    public ArrayList<String> getCatalogs() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        ArrayList<String> catalogs = new ArrayList<>();
        try {
            N1QLQueryResult catalogResult = this.m_N1QLClient.executeStatementDirectly("SELECT DISTINCT namespace_id FROM system:keyspaces", false);
            ArrayList<JsonNode> schemaError = catalogResult.getErrorList();
            if ((null != schemaError) && (schemaError.size() != 0)) {
                throw CBQueryUtils.buildServerErrorMessage(schemaError, CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_ERR.name(), "catalog function fail");
            }
            
            ArrayList<N1QLQueryRow> catalogQueryRowList = catalogResult.allRowsRawData();
            for (N1QLQueryRow schmeaRow : catalogQueryRowList) {
                String schemaName = schmeaRow.value().get("namespace_id").textValue();
                catalogs.add(schemaName);
            }
            Collections.sort(catalogs);
        } catch (ErrorException err) {
            throw err;
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGONLY_FAIL_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
        return catalogs;
    }
   
    public ArrayList<String> getCatalogsFromSchemaMap() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        try {
            return this.m_schemaMapMeta.getCatalogs();
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGONLY_FAIL_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
    }
    
    public ArrayList<CBColumnMetadata> getColumnsFromSchemaMap(String catalogName, String schemaName, String tableName) {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);

        try {
            return this.m_schemaMapMeta.getColumnMetaList(catalogName, schemaName, tableName);
        } catch (Exception ex) {
            LogUtilities.logError(ex, this.m_log); 
        }
        return new ArrayList<>();
    }
    
    public CBSQLDataEngine getDataEngineRef() {
        return this.m_dataEngine;
    }
    
    public ArrayList<String> getSchemas(String catalog) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "catalog name: " + catalog });

        ArrayList<String> schemas = new ArrayList<>();
        StringBuilder schemaQuery = new StringBuilder("SELECT name, namespace_id, store_id FROM system:keyspaces");
        
        try {
            if (null != catalog) {
                schemaQuery.append(" where ").append("namespace_id").append("='").append(catalog).append("'");
            }
            N1QLQueryResult schemaResult = this.m_N1QLClient.executeStatementDirectly(schemaQuery.toString(), false);
            
            ArrayList<JsonNode> schemaError = schemaResult.getErrorList();
            if ((null != schemaError) && (schemaError.size() != 0)) {
                throw CBQueryUtils.buildServerErrorMessage(schemaError, CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_ERR.name(), "catalog function fail");
            }
            
            ArrayList<N1QLQueryRow> schemaQueryRowList = schemaResult.allRowsRawData();
            for (N1QLQueryRow schmeaRow : schemaQueryRowList) {
                String schemaName = schmeaRow.value().get("name").textValue();
                schemas.add(schemaName);
            }
            Collections.sort(schemas);
        } catch (ErrorException err) {
            throw err;
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
        return schemas;
    }
    
    public ArrayList<String> getSchemasFromSchemaMap(String catalogName) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        try {
            ArrayList<String> schemas = this.m_schemaMapMeta.getSchemaNameList(catalogName);
            Collections.sort(schemas);
            return schemas;
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
    }
    
    public CBSMMetadata getSchemaMapMeta() {
        return this.m_schemaMapMeta;
    }
    
    public CBConnectionSettings getSettings() {
        return this.m_settings;
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
   
    
    public ArrayList<String> getTablesFromSchemaMap(String catalogName, String schemaName) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "Schema: " + schemaName });

        try {
            ArrayList<String> tables = this.m_schemaMapMeta.getTableNameList(catalogName, schemaName);
            Collections.sort(tables);
            return tables;
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_TABLEONLY_FAIL_GEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
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
    
    public IWarningListener getWarningListener() {
        return this.m_warningListener;
    }
    
    public DSIExtJResultSet openTable(String catalogName, String schemaName, String tableName, ArrayList<ColumnMetadata> columnListCache, OpenTableType openType) throws ErrorException {

        if ((null == catalogName) || (catalogName.equalsIgnoreCase(""))) {
            catalogName = this.m_settings.m_catalog;
        }
        
        ArrayList<CBColumnMetadata> columns = new ArrayList<>();
        CBCatalogMetadata catalogMeta = this.m_schemaMapMeta.getCatalogMeta(catalogName);
        if (null == catalogMeta) {
            return null;
        }
        
        CBTableMetadata tableMeta = null;
        CBSchemaMetadata schemaMeta = null;
        for (String bucketName : catalogMeta.getSchemaNameList()) {
            schemaMeta = catalogMeta.getSchemaMeta(bucketName);
            if (null != schemaMeta) {
                tableMeta = schemaMeta.getTableMeta(tableName);
                if (null != tableMeta) {
                    columns = tableMeta.getColumnMetadataList();
                    break;
                }
            }
        }
        
        columnListCache.addAll(columns);
        if ((null == columns) || (columns.isEmpty())) {
            return null;
        }
        
        if (OpenTableType.READ_ONLY == openType) {
            return new CBResultSet(schemaMeta, tableMeta, columns, this, this.m_log);
        }
        
        return new CBWriteResultSet(schemaMeta, tableMeta, columns, this, this.m_log);
    }

    public void setDataEngineRef(CBSQLDataEngine cbEngine) {
        this.m_dataEngine = cbEngine;
    }
   
    public void setSchemaMap(CBSMMetadata schemaMapMeta) {
        this.m_schemaMapMeta = schemaMapMeta;
    }

    private boolean openSession() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        try {
            N1QLQueryResult authenticationResult = this.m_N1QLClient.connect(this.m_settings.m_host, this.m_settings.m_port, N1QLRequestType.statement.name(), "SELECT * from system:keyspaces");

            boolean isValidSchema = false;
            ArrayList<N1QLQueryRow> queryResult = authenticationResult.allRowsRawData();
            for (N1QLQueryRow queryResultIter : queryResult) {
                if (queryResultIter.value().toString().contains("namespace_id\":\"" + this.m_settings.m_catalog)) {
                    isValidSchema = true;
                    break;
                }
            }
            
            if (!isValidSchema) {
                LogUtilities.logDebug("User-defined schema was invalid. The \"default\" schema will be used", this.m_log);
                this.m_settings.m_catalog = "default";
            }
            
            ArrayList<JsonNode> error = authenticationResult.getErrorList();
            if ((null != error) && (0 != error.size())) {
                throw CBQueryUtils.buildServerErrorMessage(error, CBJDBCMessageKey.CONN_FAIL_ERR.name(), "connection fail");
            }
        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_FAIL_ERR.name(), new String[] { ex.getMessage() });
        }
        
        return true;
    }
 }
