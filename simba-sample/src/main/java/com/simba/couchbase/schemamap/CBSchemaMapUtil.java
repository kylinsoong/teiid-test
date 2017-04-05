package com.simba.couchbase.schemamap;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.simba.couchbase.client.N1QLQueryResult;
import com.simba.couchbase.core.CBClient;
import com.simba.couchbase.core.CBConnectionSettings;
import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.schema.map.nodes.SMCatalog;
import com.simba.schema.map.nodes.SMDefinition;
import com.simba.schema.map.nodes.SMSchema;
import com.simba.schema.map.nodes.SMSchemaMap;
import com.simba.schemamap.helper.SchemaMapDeserializer;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CBSchemaMapUtil {

    private static final String SCHEMA_MAP_META_ID = "~~~SchemaMap";
   
    public static CBSMSchemaMap generateSchema(CBClient client, CBConnectionSettings settings, ILogger log) throws ErrorException {
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        CBSchemaMapSampler sampler = new CBSchemaMapSampler();
        CBSMSchemaMap schemaMap = sampler.generateSMSchemaMap(client, settings, log);
        return schemaMap;
    }
    
    public static void importSchemaToDatabase(String source, CBClient client, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        if (source.isEmpty()) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MISSING_FILE_ERROR.name(), new String[0]);    
        }
        
        try {
            SMSchemaMap schemaMapFromFile = loadSchemaMap(source, log);
            String version = schemaMapFromFile.getVersion();
            String standard = schemaMapFromFile.getStandard();
            String delimiteter = schemaMapFromFile.getDelimiter();
            SMDefinition definition = schemaMapFromFile.getDefinition();
            
            Set<SMCatalog> catalogs = schemaMapFromFile.getCatalogs();
            for (Iterator<SMCatalog> it = catalogs.iterator(); it.hasNext();) {
                SMCatalog currCatalog = (SMCatalog)it.next();
                Map<String, String> attributes = currCatalog.getAttributes();
                List<SMSchema> catalogSchemas = currCatalog.getSchemas();
                for (SMSchema currSchema : catalogSchemas) {
                    SMSchemaMap currSchemaMap = new SMSchemaMap(version, standard, delimiteter, definition);
                    SMCatalog currSMCatalog = new SMCatalog(currCatalog.getName(), currCatalog.getDefinition(), currSchemaMap);
                    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
                        currSMCatalog.setAttribute((String)attribute.getKey(), (String)attribute.getValue());    
                    }
                    
                    currSchemaMap.addCatalog(currSMCatalog);
                    currSchemaMap.addSchema(currSchema);
                    currSMCatalog.addSchema(currSchema);
                    
                    String query = "UPSERT INTO `" + currCatalog.getName() + "`:`" + currSchema.getName() + "` VALUES (\"" + SCHEMA_MAP_META_ID + "\"," + currSchemaMap.serialize() + ")";
                    
                    try {
                        client.executeUpdate(query, null, null);
                    } catch (Exception ex) {
                        if ((!ex.getMessage().equals("QUERY_UPDATE_NO_KEYSPACE_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_NO_PRIMARY_INDEX_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_AUTHORIZATION_ERR"))) {
                            throw ex; 
                        }
                    } 
                } 
            } 
        } catch (Exception ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_FAILED_TO_IMPORT_ERROR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;
        }
    }
    
    public static void exportSchemaFromDatabase(CBConnectionSettings settings, CBClient client, ILogger log) throws ErrorException {

        LogUtilities.logFunctionEntrance(log, new Object[0]);

        SMSchemaMap schemaMapFromDatabase = loadSchemaMap(client, log);
        writeSchemaMapToFile(settings, schemaMapFromDatabase, log);
    }
    
    public static SMSchemaMap loadSchemaMap(CBClient client, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        ArrayList<String> catalogs = client.getCatalogs();
        SMSchemaMap schemaMap = null; 
        boolean isFirst = true;
        for (Iterator<String> it = catalogs.iterator(); it.hasNext();) {
            String currCatalog = (String)it.next();
            ArrayList<String> schemas = client.getSchemas(currCatalog);
            
            for (String currSchema : schemas) {
                
                String query = "SELECT * FROM `" + currCatalog + "`:`" + currSchema + "` where meta(`" + currSchema + "`).id=\"" + SCHEMA_MAP_META_ID + "\"";
                try {
                     N1QLQueryResult result = client.executeStatement(query, null, null);
                    if (null != result) {
                        if (0 != result.allRowsRawJson().size()) {
                            JsonNode schemaMapResult = result.allRowsRawJson().get(0).path(currSchema);
                            if (null != schemaMapResult) {
                                SMSchemaMap currSchemaMap = SchemaMapDeserializer.deserializeSchemaMap(schemaMapResult);
                                Set<SMCatalog> currSchemaMapCatalogs = currSchemaMap.getCatalogs();
                                if (!currSchemaMapCatalogs.isEmpty()) {
                                    if (currSchemaMapCatalogs.size() > 1) { 
                                        ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_CATALOGS_ERROR.name(), new String[] { "Invalid. Current schema map contains more than one catalogs." });
                                        throw err;
                                    }
                                List<SMSchema> currCatalogSchemas = ((SMCatalog)currSchemaMapCatalogs.iterator().next()).getSchemas();
                                if (currCatalogSchemas.size() > 1) {
                                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_SCHEMAS_ERROR.name(), new String[] { "Invalid. Current catalog contains more than one schema." });
                                    throw err;
                                }
                                
                                if (isFirst) {
                                    schemaMap = currSchemaMap;
                                    isFirst = false;
                                } else {
                                    SMSchema onlySchema = (SMSchema)currCatalogSchemas.iterator().next();
                                    for (SMCatalog currDatabaseCatalog : schemaMap.getCatalogs()) {
                                        if (currDatabaseCatalog.getName().equals(currCatalog)) {
                                            currDatabaseCatalog.addSchema(onlySchema);
                                        }
                                    }
                                    schemaMap.addSchema(onlySchema);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                if (ex.getMessage().equals("SCHEMAMAP_MULTIPLE_CATALOGS_ERROR")) {
                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_CATALOGS_ERROR.name(), new String[] { "Invalid. Current schema map contains more than one catalogs." });
                    throw err;
                }
                if (ex.getMessage().equals("SCHEMAMAP_MULTIPLE_SCHEMAS_ERROR")) {
                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_SCHEMAS_ERROR.name(), new String[] { "Invalid. Current catalog contains more than one schema." });
                    throw err;
                }
                if ((!ex.getMessage().equals("QUERY_EXE_NO_KEYSPACE_ERR")) && (!ex.getMessage().equals("QUERY_EXE_NO_PRIMARY_INDEX_ERR")) && (!ex.getMessage().equals("QUERY_EXE_AUTHORIZATION_ERR"))) {
                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_FROM_DATABASE_ERROR.name(), new String[] { ex.getMessage() });
                    err.initCause(ex);
                    throw err;
                }
            }
          } 
        }
        return schemaMap;
    }

    public static SMSchemaMap loadSchemaMap(String source, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        SMSchemaMap schemaMapresult = null;
        
        if (source.isEmpty()) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MISSING_FILE_ERROR.name(), new String[0]);
        }
        
        try {
            File jsonFile = new File(source);
            if (jsonFile.exists()) {
                FileInputStream fin = new FileInputStream(jsonFile);
                byte[] content = new byte[(int)jsonFile.length()];
                fin.read(content);
                fin.close();
                schemaMapresult = readSchemaMap(content);
            } else {
                throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_ERROR.name(), new String[0]);
            }
        } catch (Exception ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_ERROR.name(), new String[0]);
        }
        return schemaMapresult;
    }
    
    public static void writeSchemaMapToFile(CBConnectionSettings settings, SMSchemaMap schemaMap, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        String json = schemaMap.serialize();
        PrintWriter writer = null;
        
        try {
            writer = new PrintWriter(settings.m_localSchemaFile);
            writer.write(json);
        } catch (FileNotFoundException ex) {
            throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MISSING_FILE_ERROR.name(), new String[] { ex.getMessage() });
        } finally {
            if (writer != null) { 
                writer.close();
            }
        }
    }
    
    public static void deleteDatabaseSchemaMap(CBClient client, ILogger log) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(log, new Object[0]);
        
        ArrayList<String> catalogs = client.getCatalogs();
        for (Iterator<String> it = catalogs.iterator(); it.hasNext();) {
            String currCatalog = (String)it.next();
            ArrayList<String> schemas = client.getSchemas(currCatalog);
            for (String currSchema : schemas) {
                String query = "DELETE FROM `" + currCatalog + "`:`" + currSchema + "` where meta(`" + currSchema + "`).id=\"" + SCHEMA_MAP_META_ID + "\"";
                try {
                    client.executeUpdate(query, null, null);
                } catch (Exception ex) {
                    if ((!ex.getMessage().equals("QUERY_UPDATE_NO_KEYSPACE_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_NO_PRIMARY_INDEX_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_AUTHORIZATION_ERR"))) {
                        ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_DELETE_ERROR.name(), new String[] { ex.getMessage() });
                        err.initCause(ex);
                        throw err;
                    }
                }
            }
        }
    }

    private static SMSchemaMap readSchemaMap(byte[] content) throws Exception { 
        return SchemaMapDeserializer.deserializeSchemaMap(content);
    }
    
    public static CBColumnMetadata toColumnMetadata(String catalogName, String schemaName, String tableName, String columnSourceName, String columnDSIIName, short sqlType) throws ErrorException {

        try {
            CBColumnMetadata columnMd = null;
            TypeMetadata typeMeta = null;
            String typeName = null;
            int columnLength = 0;
            switch (sqlType) {
            case 12: 
/* 539 */         typeName = "VARCHAR";
/* 540 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 541 */         columnLength = 2048;
/* 542 */         break;
       
 
       case 4: 
/* 546 */         typeName = "INTEGER";
/* 547 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 548 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
         
/* 550 */         break;
       
 
       case -5: 
/* 554 */         typeName = "BIGINT";
/* 555 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 556 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
         
/* 558 */         break;
       
 
       case 8: 
/* 562 */         typeName = "DOUBLE";
/* 563 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 564 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
         
/* 566 */         break;
       
 
       case 2: 
/* 570 */         typeName = "NUMERIC";
/* 571 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 572 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
         
/* 574 */         break;
       
 
       case 16: 
/* 578 */         typeName = "BOOLEAN";
/* 579 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 580 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
         
/* 582 */         break;
       
 
       case -7: 
/* 586 */         typeName = "BIT";
/* 587 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 588 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
         
         break;
       
 
       case 0: 
         typeName = "NULL";
         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
         columnLength = 0;
         break;
       case -6: case -4: case -3: case -2: case -1: case 1: 
       case 3: case 5: case 6: case 7: case 9: case 10: 
       case 11: case 13: case 14: case 15: default: 
         throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_DATANOTSUPPORT_ERR.name(), new String[] { String.valueOf(sqlType), "Data type from json not support" });
       }
       
       
 
 
 
 
      if (null != typeMeta)
       {
         columnMd = new CBColumnMetadata(columnSourceName, columnDSIIName, typeMeta);
         
 
 
         typeMeta.setTypeName(typeName);
         columnMd.setCatalogName(catalogName);
         columnMd.setSchemaName(schemaName);
         columnMd.setTableName(tableName);
         columnMd.setName(columnDSIIName);
         columnMd.setLabel(columnDSIIName);
         columnMd.setColumnLength(columnLength);
        columnMd.setNullable(Nullable.NULLABLE);
       }
       return columnMd;
     }
     catch (Exception ex)
     {
       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_DATANOTSUPPORT_ERR.name(), new String[] { ex.getMessage(), "Data type from json not support" });
     }
   }
}
