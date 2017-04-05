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
   
 
 
 
 
 
 
 
 
 
 
 
 
   public static void importSchemaToDatabase(String source, CBClient client, ILogger log)
     throws ErrorException
   {
/*  92 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
     
/*  94 */     if (source.isEmpty())
     {
/*  96 */       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MISSING_FILE_ERROR.name(), new String[0]);
     }
     
 
 
 
     try
     {
/* 104 */       SMSchemaMap schemaMapFromFile = loadSchemaMap(source, log);
       
 
/* 107 */       String version = schemaMapFromFile.getVersion();
/* 108 */       String standard = schemaMapFromFile.getStandard();
/* 109 */       String delimiteter = schemaMapFromFile.getDelimiter();
/* 110 */       SMDefinition definition = schemaMapFromFile.getDefinition();
       
/* 112 */       Set<SMCatalog> catalogs = schemaMapFromFile.getCatalogs();
       
       
/* 115 */       for (Iterator it = catalogs.iterator(); it.hasNext();) { 
         
         SMCatalog currCatalog = (SMCatalog)it.next(); 
/* 118 */         Map<String, String> attributes = currCatalog.getAttributes();
         
/* 120 */         List<SMSchema> catalogSchemas = currCatalog.getSchemas();
         
/* 122 */         for (SMSchema currSchema : catalogSchemas)
         {
 
/* 125 */           SMSchemaMap currSchemaMap = new SMSchemaMap(version, standard, delimiteter, definition);
           
/* 127 */           SMCatalog currSMCatalog = new SMCatalog(currCatalog.getName(), currCatalog.getDefinition(), currSchemaMap);
           
 
 
 
/* 132 */           for (Map.Entry<String, String> attribute : attributes.entrySet())
           {
/* 134 */             currSMCatalog.setAttribute((String)attribute.getKey(), (String)attribute.getValue());
           }
           
/* 137 */           currSchemaMap.addCatalog(currSMCatalog);
           
 
/* 140 */           currSchemaMap.addSchema(currSchema);
/* 141 */           currSMCatalog.addSchema(currSchema);
           
 
/* 144 */           String query = "UPSERT INTO `" + currCatalog.getName() + "`:`" + currSchema.getName() + "` VALUES (\"" + "~~~SchemaMap" + "\"," + currSchemaMap.serialize() + ")";
           
 
 
 
 
 
 
 
           try
           {
/* 155 */             client.executeUpdate(query, null, null);
 
 
           }
           catch (Exception ex)
           {
 
 
/* 163 */             if ((!ex.getMessage().equals("QUERY_UPDATE_NO_KEYSPACE_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_NO_PRIMARY_INDEX_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_AUTHORIZATION_ERR")))
             {
 
 
/* 167 */               throw ex; }
           } } } } catch (Exception ex) { String version;
       String standard;
       String delimiteter;
       SMDefinition definition;
       Iterator i$;
       SMCatalog currCatalog;
       Map<String, String> attributes;
/* 175 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_FAILED_TO_IMPORT_ERROR.name(), new String[] { ex.getMessage() });
       
 
/* 178 */       err.initCause(ex);
/* 179 */       throw err;
     }
   }
   
 
 
 
 
 
 
 
 
 
 
 
   public static void exportSchemaFromDatabase(CBConnectionSettings settings, CBClient client, ILogger log)
     throws ErrorException
   {
/* 197 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
     
/* 199 */     SMSchemaMap schemaMapFromDatabase = loadSchemaMap(client, log);
/* 200 */     writeSchemaMapToFile(settings, schemaMapFromDatabase, log);
   }
   
 
 
 
 
 
 
 
   public static SMSchemaMap loadSchemaMap(CBClient client, ILogger log)
     throws ErrorException
   {
/* 213 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
     
/* 215 */     ArrayList<String> catalogs = client.getCatalogs();
     
 
/* 218 */     SMSchemaMap schemaMap = null;
     
 
/* 221 */     boolean isFirst = true;
     
/* 223 */     for (Iterator it = catalogs.iterator(); it.hasNext();) { 
    
                String currCatalog = (String)it.next();
       
 
/* 226 */       ArrayList<String> schemas = client.getSchemas(currCatalog);
       
/* 228 */       for (String currSchema : schemas)
       {
 
/* 231 */         String query = "SELECT * FROM `" + currCatalog + "`:`" + currSchema + "` where meta(`" + currSchema + "`).id=\"" + "~~~SchemaMap" + "\"";

         try
         {
/* 244 */           N1QLQueryResult result = client.executeStatement(query, null, null);
           
 
 
 
/* 249 */           if (null != result)
           {
/* 251 */             if (0 != result.allRowsRawJson().size())
             {
/* 253 */               JsonNode schemaMapResult = result.allRowsRawJson().get(0).path(currSchema);
               
/* 255 */               if (null != schemaMapResult)
               {
/* 257 */                 SMSchemaMap currSchemaMap = SchemaMapDeserializer.deserializeSchemaMap(schemaMapResult);
                 
 
/* 260 */                 Set<SMCatalog> currSchemaMapCatalogs = currSchemaMap.getCatalogs();
                 
 
/* 263 */                 if (!currSchemaMapCatalogs.isEmpty())
                 {
 
/* 266 */                   if (currSchemaMapCatalogs.size() > 1)
                   {
 
/* 269 */                     ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_CATALOGS_ERROR.name(), new String[] { "Invalid. Current schema map contains more than one catalogs." });
                     
 
/* 272 */                     throw err;
                   }
                   
 
 
/* 277 */                   List<SMSchema> currCatalogSchemas = ((SMCatalog)currSchemaMapCatalogs.iterator().next()).getSchemas();
/* 278 */                   if (currCatalogSchemas.size() > 1)
                   {
 
/* 281 */                     ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_SCHEMAS_ERROR.name(), new String[] { "Invalid. Current catalog contains more than one schema." });
                     
 
/* 284 */                     throw err;
                   }
                   
 
/* 288 */                   if (isFirst)
                   {
 
/* 291 */                     schemaMap = currSchemaMap;
/* 292 */                     isFirst = false;
 
                   }
                   else
                   {
/* 297 */                     SMSchema onlySchema = (SMSchema)currCatalogSchemas.iterator().next();
                     
/* 299 */                     for (SMCatalog currDatabaseCatalog : schemaMap.getCatalogs())
                     {
/* 301 */                       if (currDatabaseCatalog.getName().equals(currCatalog))
                       {
/* 303 */                         currDatabaseCatalog.addSchema(onlySchema);
                       }
                     }
/* 306 */                     schemaMap.addSchema(onlySchema);
                   }
                   
                 }
                 
               }
             }
           }
         }
         catch (Exception ex)
         {
/* 317 */           if (ex.getMessage().equals("SCHEMAMAP_MULTIPLE_CATALOGS_ERROR"))
           {
/* 319 */             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_CATALOGS_ERROR.name(), new String[] { "Invalid. Current schema map contains more than one catalogs." });
             
 
/* 322 */             throw err;
           }
/* 324 */           if (ex.getMessage().equals("SCHEMAMAP_MULTIPLE_SCHEMAS_ERROR"))
           {
/* 326 */             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MULTIPLE_SCHEMAS_ERROR.name(), new String[] { "Invalid. Current catalog contains more than one schema." });
             
 
/* 329 */             throw err;
           }
           
/* 332 */           if ((!ex.getMessage().equals("QUERY_EXE_NO_KEYSPACE_ERR")) && (!ex.getMessage().equals("QUERY_EXE_NO_PRIMARY_INDEX_ERR")) && (!ex.getMessage().equals("QUERY_EXE_AUTHORIZATION_ERR")))
           {
 
 
/* 336 */             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_FROM_DATABASE_ERROR.name(), new String[] { ex.getMessage() });
             
 
/* 339 */             err.initCause(ex);
/* 340 */             throw err;
           }
         }
       } }
     String currCatalog;
/* 345 */     return schemaMap;
   }
   
 
 
 
 
 
 
 
   public static SMSchemaMap loadSchemaMap(String source, ILogger log)
     throws ErrorException
   {
/* 358 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/* 359 */     SMSchemaMap schemaMapresult = null;
     
/* 361 */     if (source.isEmpty())
     {
/* 363 */       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MISSING_FILE_ERROR.name(), new String[0]);
     }
     
 
 
 
     try
     {
/* 371 */       File jsonFile = new File(source);
/* 372 */       if (jsonFile.exists())
       {
/* 374 */         FileInputStream fin = new FileInputStream(jsonFile);
/* 375 */         byte[] content = new byte[(int)jsonFile.length()];
/* 376 */         fin.read(content);
/* 377 */         fin.close();
         
 
/* 380 */         schemaMapresult = readSchemaMap(content);
       }
       else
       {
/* 384 */         throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_ERROR.name(), new String[0]);
       }
       
 
     }
     catch (Exception ex)
     {
/* 391 */       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_LOADING_ERROR.name(), new String[0]);
     }
     
 
 
 
/* 397 */     return schemaMapresult;
   }
   
 
 
 
 
 
 
 
 
 
 
 
   public static void writeSchemaMapToFile(CBConnectionSettings settings, SMSchemaMap schemaMap, ILogger log)
     throws ErrorException
   {
/* 414 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
     
/* 416 */     String json = schemaMap.serialize();
/* 417 */     PrintWriter writer = null;
     
     try
     {
/* 421 */       writer = new PrintWriter(settings.m_localSchemaFile);
/* 422 */       writer.write(json);
     }
     catch (FileNotFoundException ex)
     {
/* 426 */       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_MISSING_FILE_ERROR.name(), new String[] { ex.getMessage() });
 
     }
     finally
     {
 
/* 432 */       if (writer != null) { writer.close();
       }
     }
   }
   
 
 
 
 
 
 
 
   public static void deleteDatabaseSchemaMap(CBClient client, ILogger log)
     throws ErrorException
   {
/* 447 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
     
/* 449 */     ArrayList<String> catalogs = client.getCatalogs();
     
/* 451 */     for (Iterator it = catalogs.iterator(); it.hasNext();) { 
       
       String currCatalog = (String)it.next();
/* 454 */       ArrayList<String> schemas = client.getSchemas(currCatalog);
       
/* 456 */       for (String currSchema : schemas)
       {
/* 458 */         String query = "DELETE FROM `" + currCatalog + "`:`" + currSchema + "` where meta(`" + currSchema + "`).id=\"" + "~~~SchemaMap" + "\"";
         
 
 
 
 
 
 
 
         try
         {
/* 469 */           client.executeUpdate(query, null, null);
 
 
         }
         catch (Exception ex)
         {
 
 
/* 477 */           if ((!ex.getMessage().equals("QUERY_UPDATE_NO_KEYSPACE_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_NO_PRIMARY_INDEX_ERR")) && (!ex.getMessage().equals("QUERY_UPDATE_AUTHORIZATION_ERR")))
           {
 
 
/* 481 */             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_DELETE_ERROR.name(), new String[] { ex.getMessage() });
             
 
/* 484 */             err.initCause(ex);
/* 485 */             throw err;
           }
         }
       }
     }
     
 
 
     String currCatalog;
   }
   
 
 
 
   private static SMSchemaMap readSchemaMap(byte[] content)
     throws Exception
   {
/* 502 */     return SchemaMapDeserializer.deserializeSchemaMap(content);
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   public static CBColumnMetadata toColumnMetadata(String catalogName, String schemaName, String tableName, String columnSourceName, String columnDSIIName, short sqlType)
     throws ErrorException
   {
     try
     {
/* 530 */       CBColumnMetadata columnMd = null;
/* 531 */       TypeMetadata typeMeta = null;
/* 532 */       String typeName = null;
/* 533 */       int columnLength = 0;
       
/* 535 */       switch (sqlType)
       {
 
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
