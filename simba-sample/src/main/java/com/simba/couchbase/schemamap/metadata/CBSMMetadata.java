package com.simba.couchbase.schemamap.metadata;

import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.schemamap.CBSchemaMapUtil;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.schema.map.nodes.SMCatalog;
import com.simba.schema.map.nodes.SMColumn;
import com.simba.schema.map.nodes.SMSchema;
import com.simba.schema.map.nodes.SMSchemaMap;
import com.simba.schema.map.nodes.SMTable;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashMap;

public class CBSMMetadata {

    private ArrayList<String> m_catalogList;
    private HashMap<String, CBCatalogMetadata> m_catalogMetadataMap;
    private ILogger m_log;
    private SMSchemaMap m_schemaMap;
  
    public CBSMMetadata(SMSchemaMap preparedSchemaMap, ILogger log) {
        this.m_log = log;
        this.m_catalogList = new ArrayList<>();
        this.m_catalogMetadataMap = new HashMap<>();
        this.m_schemaMap = preparedSchemaMap;
        getAllMetadata();
    }
    
    public ArrayList<String> getCatalogs() {
        return this.m_catalogList;
    }
    
    public CBCatalogMetadata getCatalogMeta(String catalogName) {
        return (CBCatalogMetadata)this.m_catalogMetadataMap.get(catalogName);
    }
    
    public ArrayList<String> getSchemaNameList(String catalogName) {
        CBCatalogMetadata catalogMeta = (CBCatalogMetadata)this.m_catalogMetadataMap.get(catalogName);
        if (null == catalogMeta) {
            return new ArrayList<>();
        }
        return catalogMeta.getSchemaNameList();
    }
    
    public ArrayList<String> getTableNameList(String catalogName, String schemaName) {

        CBCatalogMetadata catalogMeta = (CBCatalogMetadata)this.m_catalogMetadataMap.get(catalogName);
        if (null != catalogMeta) {
            CBSchemaMetadata schemaMeta = catalogMeta.getSchemaMeta(schemaName);
            if (null != schemaMeta) {
                return schemaMeta.getTableNameList();
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
    
    public ArrayList<CBColumnMetadata> getColumnMetaList(String catalogName, String schemaName, String tableName) {
        CBCatalogMetadata catalogMeta = (CBCatalogMetadata)this.m_catalogMetadataMap.get(catalogName);
        if (null != catalogMeta) {
            CBSchemaMetadata schemaMeta = catalogMeta.getSchemaMeta(schemaName);
            if (null != schemaMeta) {
                CBTableMetadata tableMeta = schemaMeta.getTableMeta(tableName);
                if (null != tableMeta) {
                    return tableMeta.getColumnMetadataList();
                }
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
    
    public SMSchemaMap getSchemaMap() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_schemaMap;
    }
    
    private void getAllMetadata() {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    
        try {
            for (SMCatalog catalog : this.m_schemaMap.getCatalogs()) {
                String catalogName = catalog.getName();
                this.m_catalogList.add(catalogName);
        
                CBCatalogMetadata catalogMeta = new CBCatalogMetadata(catalogName, this.m_log);
                for (SMSchema schema : catalog.getSchemas()) {
                    String schemaDSIIName = schema.getName();
                    catalogMeta.addSchemaName(schemaDSIIName);
                    CBSchemaMetadata schemaMeta = new CBSchemaMetadata(schemaDSIIName, this.m_log);

                    for (SMTable table : schema.getTables()) {
                        try {
                            String tableSourceName = (String)table.getAttributes().get("sourceName");
                            String tableDSIIName = (String)table.getAttributes().get("dsiiName");
                            String tableTypeColumnName = (String)table.getAttributes().get("typeName");
                            String tableTypeColumnValue = (String)table.getAttributes().get("typeValue");
                            
                            CBTableMetadata tableMeta = new CBTableMetadata(tableDSIIName, tableSourceName);
                            tableMeta.setColumnIdentifierName(tableTypeColumnName);
                            tableMeta.setColumnIdentifierValue(tableTypeColumnValue);
                            
                            for (SMColumn column : table.getColumns()) {
                                try {
                                    String columnSourceName = (String)column.getAttributes().get("sourceName");
                                    String columnDSIIName = (String)column.getAttributes().get("dsiiName");
                                    short sqlType = Short.parseShort((String)column.getAttributes().get("dsiiType"));
                                    CBColumnMetadata columnMeta = CBSchemaMapUtil.toColumnMetadata(catalogName, schemaDSIIName, tableDSIIName, columnSourceName, columnDSIIName, sqlType);
                                    if (columnMeta.getName().equals("PK")) {
                                        columnMeta.setPKColumn(true);
                                    }
                                    
                                    tableMeta.addColumnMetadata(columnMeta);
                                    tableMeta.mapColumnMetadata(columnDSIIName, columnMeta);
                                } catch (Exception ex) {
                                    throw ex;
                                }
                            }
                            
                            schemaMeta.addTableName(tableDSIIName);
                            schemaMeta.addTableMeta(tableDSIIName, tableMeta);
                        } catch (Exception ex) {
                            throw ex;
                        }
                    }
                    catalogMeta.mapSchemaMeta(schemaDSIIName, schemaMeta);
                }
                
                this.m_catalogMetadataMap.put(catalogName, catalogMeta);
            }
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_ERROR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
        }
    }
}