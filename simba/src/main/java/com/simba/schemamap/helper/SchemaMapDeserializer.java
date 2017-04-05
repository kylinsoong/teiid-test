/*     */ package com.simba.schemamap.helper;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.simba.schema.map.nodes.SMAttribute;
/*     */ import com.simba.schema.map.nodes.SMCatalog;
/*     */ import com.simba.schema.map.nodes.SMColumn;
/*     */ import com.simba.schema.map.nodes.SMDefinition;
/*     */ import com.simba.schema.map.nodes.SMObject;
/*     */ import com.simba.schema.map.nodes.SMSchema;
/*     */ import com.simba.schema.map.nodes.SMSchemaMap;
/*     */ import com.simba.schema.map.nodes.SMTable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaMapDeserializer
/*     */ {
/*     */   public static SMSchemaMap deserializeSchemaMap(Object schemaMapObject)
/*     */     throws Exception
/*     */   {
/*  45 */     SMSchemaMap schemaMap = null;
/*  46 */     JsonNode rootNode = null;
/*     */     
/*  48 */     if ((schemaMapObject instanceof JsonNode))
/*     */     {
/*  50 */       rootNode = (JsonNode)schemaMapObject;
/*     */     }
/*  52 */     else if ((schemaMapObject instanceof byte[]))
/*     */     {
/*  54 */       ObjectMapper mapper = new ObjectMapper();
/*  55 */       rootNode = mapper.readTree((byte[])schemaMapObject);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  60 */       return null;
/*     */     }
/*     */     
/*  63 */     JsonNode versionNode = rootNode.get("SchemaMapVersion");
/*  64 */     JsonNode standard = rootNode.get("Standard");
/*  65 */     JsonNode delimiter = rootNode.get("Delimiter");
/*     */     
/*     */ 
/*  68 */     SMDefinition schemaMapDefinition = null;
/*  69 */     JsonNode definition = rootNode.get("SchemaMapDefinition");
/*  70 */     if (null != definition)
/*     */     {
/*  72 */       schemaMapDefinition = readDefinition(definition);
/*     */     }
/*     */     
/*  75 */     schemaMap = new SMSchemaMap(versionNode.textValue(), standard.textValue(), delimiter.textValue(), schemaMapDefinition);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     if (rootNode.has("Catalogs"))
/*     */     {
/*  83 */       JsonNode catalogs = rootNode.get("Catalogs");
/*  84 */       readCatalogs(catalogs, schemaMap, schemaMap);
/*     */     }
/*  86 */     else if (rootNode.has("Schemas"))
/*     */     {
/*  88 */       JsonNode schemas = rootNode.get("Schemas");
/*  89 */       readSchemas(schemas, schemaMap, schemaMap);
/*     */     }
/*  91 */     else if (rootNode.has("Tables"))
/*     */     {
/*  93 */       JsonNode tables = rootNode.get("Tables");
/*  94 */       readTables(tables, schemaMap, schemaMap);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     return schemaMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SMDefinition readDefinition(JsonNode jsonDefinition)
/*     */   {
/* 120 */     SMDefinition definition = new SMDefinition();
/*     */     
/*     */ 
/*     */ 
/* 124 */     JsonNode catalogsDefinition = jsonDefinition.get("CatalogDefinition");
/* 125 */     if (null != jsonDefinition.get("CatalogDefinition"))
/*     */     {
/* 127 */       definition.setSupportsCatalogs(true);
/*     */       
/* 129 */       Iterator<Map.Entry<String, JsonNode>> it = catalogsDefinition.fields();
/* 130 */       Map.Entry<String, JsonNode> currentAttribute = null;
/* 131 */       while (it.hasNext())
/*     */       {
/* 133 */         currentAttribute = (Map.Entry)it.next();
/* 134 */         definition.addCatalogAttribute((String)currentAttribute.getKey(), readAttribute((String)currentAttribute.getKey(), (JsonNode)currentAttribute.getValue()));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */     JsonNode schemasDefinition = jsonDefinition.get("SchemaDefinition");
/* 143 */     if (null != jsonDefinition.get("SchemaDefinition"))
/*     */     {
/* 145 */       definition.setSupportsSchemas(true);
/*     */       
/* 147 */       Iterator<Map.Entry<String, JsonNode>> it = schemasDefinition.fields();
/* 148 */       Map.Entry<String, JsonNode> currentAttribute = null;
/* 149 */       while (it.hasNext())
/*     */       {
/* 151 */         currentAttribute = (Map.Entry)it.next();
/* 152 */         definition.addSchemaAttribute((String)currentAttribute.getKey(), readAttribute((String)currentAttribute.getKey(), (JsonNode)currentAttribute.getValue()));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 160 */     JsonNode tablesDefinition = jsonDefinition.get("TableDefinition");
/* 161 */     if (null != tablesDefinition)
/*     */     {
/* 163 */       Iterator<Map.Entry<String, JsonNode>> it = tablesDefinition.fields();
/* 164 */       Map.Entry<String, JsonNode> currentAttribute = null;
/* 165 */       while (it.hasNext())
/*     */       {
/* 167 */         currentAttribute = (Map.Entry)it.next();
/* 168 */         definition.addTableAttribute((String)currentAttribute.getKey(), readAttribute((String)currentAttribute.getKey(), (JsonNode)currentAttribute.getValue()));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */     JsonNode columnsDefinition = jsonDefinition.get("ColumnDefinition");
/* 181 */     if (null != tablesDefinition)
/*     */     {
/* 183 */       Iterator<Map.Entry<String, JsonNode>> it = columnsDefinition.fields();
/* 184 */       Map.Entry<String, JsonNode> currentAttribute = null;
/* 185 */       while (it.hasNext())
/*     */       {
/* 187 */         currentAttribute = (Map.Entry)it.next();
/* 188 */         definition.addColumnAttribute((String)currentAttribute.getKey(), readAttribute((String)currentAttribute.getKey(), (JsonNode)currentAttribute.getValue()));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */     return definition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SMAttribute readAttribute(String attributeName, JsonNode jsonAttribute)
/*     */   {
/* 209 */     SMAttribute attribute = new SMAttribute();
/*     */     
/*     */ 
/* 212 */     JsonNode type = jsonAttribute.get("Type");
/* 213 */     if (null != type)
/*     */     {
/* 215 */       attribute.addField("Type", type.textValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */     if ((null != attributeName) && (attributeName.equals("dsiiName")))
/*     */     {
/* 227 */       attribute.setRequired(true);
/*     */     }
/*     */     else
/*     */     {
/* 231 */       JsonNode required = jsonAttribute.get("Required");
/* 232 */       if (null != required)
/*     */       {
/* 234 */         attribute.setRequired(required.booleanValue());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 239 */     if ((null != attributeName) && (attributeName.equals("dsiiName")))
/*     */     {
/* 241 */       attribute.setDistinct(true);
/*     */     }
/*     */     else
/*     */     {
/* 245 */       JsonNode distinct = jsonAttribute.get("Distinct");
/* 246 */       if (null != distinct)
/*     */       {
/* 248 */         attribute.setDistinct(distinct.booleanValue());
/*     */       }
/*     */     }
/*     */     
/* 252 */     JsonNode hidden = jsonAttribute.get("Hidden");
/* 253 */     if (null != hidden)
/*     */     {
/* 255 */       attribute.setHidden(hidden.booleanValue());
/*     */     }
/*     */     
/* 258 */     JsonNode readOnly = jsonAttribute.get("Read-Only");
/* 259 */     if (null != readOnly)
/*     */     {
/* 261 */       attribute.setReadOnly(readOnly.booleanValue());
/*     */     }
/*     */     
/* 264 */     JsonNode enumValues = jsonAttribute.get("EnumValues");
/* 265 */     if (null != enumValues)
/*     */     {
/* 267 */       Iterator<JsonNode> it = enumValues.elements();
/* 268 */       JsonNode currentValue = null;
/* 269 */       while (it.hasNext())
/*     */       {
/* 271 */         currentValue = (JsonNode)it.next();
/* 272 */         Iterator<Map.Entry<String, JsonNode>> enums = currentValue.fields();
/* 273 */         Map.Entry<String, JsonNode> currentEnum = null;
/* 274 */         while (enums.hasNext())
/*     */         {
/* 276 */           currentEnum = (Map.Entry)enums.next();
/* 277 */           if (null != currentEnum.getValue())
/*     */           {
/* 279 */             attribute.addEnum((String)currentEnum.getKey(), ((JsonNode)currentEnum.getValue()).asText());
/*     */           }
/*     */           else
/*     */           {
/* 283 */             attribute.addEnum((String)currentEnum.getKey(), null);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 289 */     return attribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SMTable readTable(String tableName, JsonNode jsonTable, SMSchemaMap schemaMap, SMObject parent)
/*     */   {
/* 307 */     SMTable table = new SMTable(tableName, schemaMap.getDefinition(), parent);
/*     */     
/*     */ 
/*     */ 
/* 311 */     addAttributes(table, jsonTable);
/*     */     
/*     */ 
/*     */ 
/* 315 */     if (jsonTable.has("Columns"))
/*     */     {
/* 317 */       readColumns(jsonTable.get("Columns"), table, schemaMap);
/*     */     }
/*     */     
/* 320 */     return table;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SMColumn readColum(String columnName, JsonNode jsonColumn, SMDefinition schemaMapDefinition, SMTable table)
/*     */   {
/* 338 */     SMColumn column = new SMColumn(columnName, schemaMapDefinition, table);
/*     */     
/*     */ 
/*     */ 
/* 342 */     for (String currentAttribute : column.getAttributes().keySet())
/*     */     {
/* 344 */       JsonNode curr = jsonColumn.get(currentAttribute);
/* 345 */       if (null != curr)
/*     */       {
/* 347 */         column.addAttribute(currentAttribute, curr.textValue());
/*     */       }
/*     */     }
/*     */     
/* 351 */     return column;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void readTables(JsonNode tables, SMObject parent, SMSchemaMap schemaMap)
/*     */   {
/* 365 */     Iterator<JsonNode> tablesIt = tables.elements();
/* 366 */     JsonNode currentTable = null;
/* 367 */     while (tablesIt.hasNext())
/*     */     {
/* 369 */       currentTable = (JsonNode)tablesIt.next();
/*     */       
/* 371 */       Iterator<Map.Entry<String, JsonNode>> it = currentTable.fields();
/* 372 */       Map.Entry<String, JsonNode> tableInfo = null;
/* 373 */       while (it.hasNext())
/*     */       {
/* 375 */         tableInfo = (Map.Entry)it.next();
/* 376 */         String tableName = ((String)tableInfo.getKey()).replace(schemaMap.getDelimiter(), "").replace("Table-", "");
/*     */         
/*     */ 
/* 379 */         SMTable table = readTable(tableName, (JsonNode)tableInfo.getValue(), schemaMap, parent);
/*     */         
/* 381 */         schemaMap.addTable(table);
/*     */         
/* 383 */         if ((parent instanceof SMCatalog))
/*     */         {
/* 385 */           ((SMCatalog)parent).addTable(table);
/*     */         }
/* 387 */         else if ((parent instanceof SMSchema))
/*     */         {
/* 389 */           ((SMSchema)parent).addTable(table);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void readSchemas(JsonNode schemas, SMObject parent, SMSchemaMap schemaMap)
/*     */   {
/* 406 */     Iterator<JsonNode> schemasIt = schemas.elements();
/* 407 */     JsonNode currentSchema = null;
/* 408 */     while (schemasIt.hasNext())
/*     */     {
/* 410 */       currentSchema = (JsonNode)schemasIt.next();
/*     */       
/* 412 */       Iterator<Map.Entry<String, JsonNode>> it = currentSchema.fields();
/* 413 */       Map.Entry<String, JsonNode> schemaInfo = null;
/* 414 */       while (it.hasNext())
/*     */       {
/* 416 */         schemaInfo = (Map.Entry)it.next();
/* 417 */         String schemaName = ((String)schemaInfo.getKey()).replace(schemaMap.getDelimiter(), "").replace("Schema-", "");
/*     */         
/*     */ 
/* 420 */         SMSchema schema = readSchema(schemaName, (JsonNode)schemaInfo.getValue(), schemaMap, parent);
/*     */         
/* 422 */         schemaMap.addSchema(schema);
/*     */         
/* 424 */         if ((parent instanceof SMCatalog))
/*     */         {
/* 426 */           ((SMCatalog)parent).addSchema(schema);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SMSchema readSchema(String schemaName, JsonNode jsonSchema, SMSchemaMap schemaMap, SMObject parent)
/*     */   {
/* 447 */     SMSchema schema = new SMSchema(schemaName, schemaMap.getDefinition(), parent);
/* 448 */     addAttributes(schema, jsonSchema);
/*     */     
/*     */ 
/* 451 */     if (jsonSchema.has("Tables"))
/*     */     {
/* 453 */       readTables(jsonSchema.get("Tables"), schema, schemaMap);
/*     */     }
/*     */     
/* 456 */     return schema;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void readCatalogs(JsonNode catalogs, SMObject parent, SMSchemaMap schemaMap)
/*     */   {
/* 470 */     Iterator<JsonNode> catalogsIt = catalogs.elements();
/* 471 */     JsonNode currentCatalog = null;
/* 472 */     while (catalogsIt.hasNext())
/*     */     {
/* 474 */       currentCatalog = (JsonNode)catalogsIt.next();
/*     */       
/* 476 */       Iterator<Map.Entry<String, JsonNode>> it = currentCatalog.fields();
/* 477 */       Map.Entry<String, JsonNode> catalogInfo = null;
/* 478 */       while (it.hasNext())
/*     */       {
/* 480 */         catalogInfo = (Map.Entry)it.next();
/* 481 */         String catalogName = ((String)catalogInfo.getKey()).replace(schemaMap.getDelimiter(), "").replace("Catalog-", "");
/*     */         
/*     */ 
/* 484 */         SMCatalog catalog = readCatalog(catalogName, (JsonNode)catalogInfo.getValue(), schemaMap, schemaMap);
/*     */         
/*     */ 
/*     */ 
/* 488 */         schemaMap.addCatalog(catalog);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SMCatalog readCatalog(String catalogName, JsonNode jsonCatalog, SMSchemaMap schemaMap, SMObject parent)
/*     */   {
/* 508 */     SMCatalog catalog = new SMCatalog(catalogName, schemaMap.getDefinition(), parent);
/* 509 */     addAttributes(catalog, jsonCatalog);
/*     */     
/*     */ 
/* 512 */     if (jsonCatalog.has("Schemas"))
/*     */     {
/* 514 */       readSchemas(jsonCatalog.get("Schemas"), catalog, schemaMap);
/*     */     }
/*     */     
/*     */ 
/* 518 */     if (jsonCatalog.has("Tables"))
/*     */     {
/* 520 */       readTables(jsonCatalog.get("Tables"), catalog, schemaMap);
/*     */     }
/*     */     
/* 523 */     return catalog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addAttributes(SMObject smObject, JsonNode node)
/*     */   {
/* 536 */     for (String currentAttribute : smObject.getAttributes().keySet())
/*     */     {
/* 538 */       JsonNode curr = node.get(currentAttribute);
/* 539 */       smObject.addAttribute(currentAttribute, curr.textValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void readColumns(JsonNode columns, SMTable table, SMSchemaMap schemaMap)
/*     */   {
/* 554 */     SMDefinition schemaMapDefinition = schemaMap.getDefinition();
/* 555 */     Iterator<JsonNode> columnsIt = columns.elements();
/* 556 */     JsonNode currentTable = null;
/* 557 */     while (columnsIt.hasNext())
/*     */     {
/* 559 */       currentTable = (JsonNode)columnsIt.next();
/*     */       
/* 561 */       Iterator<Map.Entry<String, JsonNode>> it = currentTable.fields();
/* 562 */       Map.Entry<String, JsonNode> columnInfo = null;
/* 563 */       while (it.hasNext())
/*     */       {
/* 565 */         columnInfo = (Map.Entry)it.next();
/* 566 */         String columnName = ((String)columnInfo.getKey()).replace(schemaMap.getDelimiter(), "").replace("Column-", "");
/*     */         
/*     */ 
/*     */ 
/* 570 */         SMColumn column = readColum(columnName, (JsonNode)columnInfo.getValue(), schemaMapDefinition, table);
/*     */         
/*     */ 
/* 573 */         table.addColumn(column);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/schemamap/helper/SchemaMapDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */