/*     */ package com.simba.couchbase.utils;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeType;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.dsi.dataengine.utilities.ColumnMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.Nullable;
/*     */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.TypeUtilities;
/*     */ import com.simba.dsi.exceptions.NumericOverflowException;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class CBDataTypeUtils
/*     */ {
/*  44 */   private static ArrayList<ColumnMetadata> m_columnMdList = new ArrayList();
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
/*     */   public static ArrayList<ColumnMetadata> createFlattenColumn(String catalog, String schema, String tableName, JsonNode queryContext, ILogger log)
/*     */     throws ErrorException
/*     */   {
/*  70 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/*     */     try
/*     */     {
/*  73 */       return buildFlattenColumn(catalog, schema, tableName, queryContext);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (ErrorException ex)
/*     */     {
/*     */ 
/*     */ 
/*  81 */       throw ex;
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
/*     */ 
/*     */   public static ArrayList<ColumnMetadata> createColumnFromSignature(String catalog, JsonNode jsonNode, ILogger log)
/*     */     throws ErrorException
/*     */   {
/* 102 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/*     */     try
/*     */     {
/* 105 */       return buildFlattenColumn(catalog, null, "signature", jsonNode);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (ErrorException ex)
/*     */     {
/*     */ 
/*     */ 
/* 113 */       throw ex;
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
/*     */   public static boolean convertColumnToData(long offset, long maxSize, DataWrapper retrievedData, Short columnType, JsonNode columnValue, ILogger log)
/*     */     throws ErrorException
/*     */   {
/* 143 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 148 */       if ((null == columnType) || (null == columnValue) || (columnValue.isNull()))
/*     */       {
/* 150 */         retrievedData.setNull(columnType.shortValue());
/* 151 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 155 */       switch (columnType.shortValue())
/*     */       {
/*     */ 
/*     */ 
/*     */       case -8: 
/*     */       case 1: 
/*     */       case 12: 
/* 162 */         return DSITypeUtilities.outputVarCharStringData(columnValue.asText(), retrievedData, offset, maxSize);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 16: 
/* 170 */         retrievedData.setBoolean(columnValue.booleanValue());
/* 171 */         return false;
/*     */       
/*     */ 
/*     */       case 5: 
/* 175 */         retrievedData.setSmallInt(columnValue.shortValue());
/* 176 */         return false;
/*     */       
/*     */ 
/*     */       case 4: 
/* 180 */         retrievedData.setInteger(columnValue.intValue());
/* 181 */         return false;
/*     */       
/*     */ 
/*     */       case -5: 
/* 185 */         retrievedData.setBigInt(columnValue.longValue());
/* 186 */         return false;
/*     */       
/*     */ 
/*     */       case 3: 
/*     */       case 8: 
/* 191 */         retrievedData.setDouble(columnValue.doubleValue());
/* 192 */         return false;
/*     */       
/*     */ 
/*     */ 
/*     */       case 93: 
/* 197 */         return DSITypeUtilities.outputVarCharStringData(columnValue.toString(), retrievedData, offset, maxSize);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 2003: 
/* 206 */         return DSITypeUtilities.outputVarCharStringData(columnValue.toString(), retrievedData, offset, maxSize);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 0: 
/* 214 */         retrievedData.setNull(columnType.shortValue());
/* 215 */         return false;
/*     */       }
/*     */       
/*     */       
/* 219 */       throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_DATANOTSUPPORT_ERR.name(), new String[] { columnValue.toString(), "Data type from json not support" });
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (ErrorException err)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 229 */       throw err;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 233 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.DATA_FETCH_INIT_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 236 */       err.initCause(ex);
/* 237 */       throw err;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList<ColumnMetadata> buildColumn(String catalog, String schema, String tableName, JsonNode rootNode)
/*     */     throws ErrorException
/*     */   {
/* 262 */     ArrayList<ColumnMetadata> columnMdList = new ArrayList();
/*     */     
/* 264 */     JsonNode pkNode = rootNode.path("PK");
/*     */     
/*     */     try
/*     */     {
/* 268 */       columnMdList.add(getPKColumnMetadata(catalog, schema, tableName, pkNode));
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (NumericOverflowException ex)
/*     */     {
/*     */ 
/*     */ 
/* 276 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_GENERATE_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */       err.initCause(ex);
/* 283 */       throw err;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 289 */       JsonNode tableNode = rootNode.path(tableName);
/*     */       
/* 291 */       Iterator<Map.Entry<String, JsonNode>> tableItr = tableNode.fields();
/*     */       
/*     */ 
/* 294 */       while (tableItr.hasNext())
/*     */       {
/*     */ 
/* 297 */         Map.Entry<String, JsonNode> nextColumn = (Map.Entry)tableItr.next();
/*     */         
/* 299 */         String columnName = (String)nextColumn.getKey();
/* 300 */         JsonNode columnValue = (JsonNode)nextColumn.getValue();
/* 301 */         JsonNodeType jsonDataType = columnValue.getNodeType();
/*     */         
/*     */ 
/* 304 */         ColumnMetadata columnMd = null;
/* 305 */         TypeMetadata typeMeta = null;
/* 306 */         String typeName = null;
/* 307 */         int columnLength = 0;
/*     */         short sqlType;
/*     */         short sqlType;
/* 310 */         switch (jsonDataType)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */         case ARRAY: 
/* 316 */           typeName = "ARRAY";
/*     */           
/* 318 */           sqlType = 12;
/* 319 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 320 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case NUMBER: 
/* 325 */           if ((columnValue.isInt()) || (columnValue.isLong()))
/*     */           {
/*     */ 
/* 328 */             typeName = "BIGINT";
/* 329 */             sqlType = -5;
/* 330 */             typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 331 */             columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/*     */           }
/* 333 */           else if (columnValue.isDouble())
/*     */           {
/*     */ 
/* 336 */             typeName = "DOUBLE";
/* 337 */             short sqlType = 8;
/* 338 */             typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 339 */             columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 344 */             typeName = "NUMERIC";
/* 345 */             sqlType = 2;
/* 346 */             typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 347 */             columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/*     */           }
/* 349 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case STRING: 
/* 354 */           typeName = "VARCHAR";
/* 355 */           sqlType = 12;
/* 356 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/*     */           
/* 358 */           columnLength = 255;
/* 359 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case BOOLEAN: 
/* 364 */           typeName = "BOOLEAN";
/* 365 */           sqlType = 16;
/* 366 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 367 */           columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/* 368 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case OBJECT: 
/* 373 */           typeName = "OBJECT";
/* 374 */           sqlType = 12;
/* 375 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 376 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case NULL: 
/* 381 */           typeName = "NULL";
/* 382 */           sqlType = 0;
/* 383 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 384 */           columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/* 385 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case MISSING: 
/* 390 */           typeName = "MISSING";
/* 391 */           sqlType = 12;
/* 392 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 393 */           break;
/*     */         
/*     */ 
/*     */         default: 
/* 397 */           throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_DATANOTSUPPORT_ERR.name(), new String[] { jsonDataType.toString(), "Data type from json not support" });
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */         if (null != typeMeta)
/*     */         {
/* 409 */           columnMd = new ColumnMetadata(typeMeta);
/*     */           
/* 411 */           typeMeta.setTypeName(typeName);
/* 412 */           columnMd.setCatalogName(catalog);
/* 413 */           columnMd.setSchemaName(schema);
/* 414 */           columnMd.setTableName(tableName);
/* 415 */           columnMd.setName(columnName);
/* 416 */           columnMd.setLabel(columnName);
/* 417 */           columnMd.setColumnLength(columnLength);
/* 418 */           columnMd.setNullable(Nullable.NULLABLE);
/* 419 */           columnMdList.add(columnMd);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (ErrorException err)
/*     */     {
/* 426 */       throw err;
/*     */     }
/*     */     catch (NumericOverflowException ex)
/*     */     {
/* 430 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_GENERATE_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 436 */       err.initCause(ex);
/* 437 */       throw err;
/*     */     }
/*     */     
/* 440 */     return columnMdList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList<ColumnMetadata> buildFlattenColumn(String catalog, String schema, String tableName, JsonNode rootNode)
/*     */     throws ErrorException
/*     */   {
/* 463 */     if (m_columnMdList.size() > 0)
/*     */     {
/* 465 */       m_columnMdList.clear();
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
/* 489 */     JsonNode tableNode = rootNode.path(tableName);
/*     */     
/*     */ 
/* 492 */     if (tableNode.isMissingNode())
/*     */     {
/* 494 */       tableNode = rootNode;
/*     */     }
/*     */     
/* 497 */     Iterator<Map.Entry<String, JsonNode>> tableItr = tableNode.fields();
/*     */     
/* 499 */     if (!tableItr.hasNext())
/*     */     {
/* 501 */       String columnName = "$1";
/* 502 */       JsonNode columnValue = tableNode;
/* 503 */       JsonNodeType jsonDataType = columnValue.getNodeType();
/*     */       
/* 505 */       flattenArrayHelper(jsonDataType, catalog, schema, tableName, columnName, columnValue, rootNode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 517 */       while (tableItr.hasNext())
/*     */       {
/*     */ 
/* 520 */         Map.Entry<String, JsonNode> nextColumn = (Map.Entry)tableItr.next();
/*     */         
/* 522 */         String columnName = (String)nextColumn.getKey();
/* 523 */         JsonNode columnValue = (JsonNode)nextColumn.getValue();
/* 524 */         JsonNodeType jsonDataType = columnValue.getNodeType();
/*     */         
/* 526 */         flattenArrayHelper(jsonDataType, catalog, schema, tableName, columnName, columnValue, rootNode);
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
/* 537 */     return m_columnMdList;
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
/*     */ 
/*     */   private static ColumnMetadata getPKColumnMetadata(String catalog, String schema, String tableName, JsonNode pkNode)
/*     */     throws ErrorException, NumericOverflowException
/*     */   {
/* 557 */     TypeMetadata pkTypeMeta = TypeMetadata.createTypeMetadata(12);
/* 558 */     String pkTypeName = "VARCHAR";
/* 559 */     int pkColumnLength = 2048;
/* 560 */     String pkColumnName = "PK";
/*     */     
/*     */ 
/* 563 */     ColumnMetadata pkColumnMd = new ColumnMetadata(pkTypeMeta);
/* 564 */     pkTypeMeta.setTypeName(pkTypeName);
/* 565 */     pkColumnMd.setCatalogName(catalog);
/* 566 */     pkColumnMd.setSchemaName(schema);
/* 567 */     pkColumnMd.setTableName(tableName);
/* 568 */     pkColumnMd.setName(pkColumnName);
/* 569 */     pkColumnMd.setLabel(pkColumnName);
/* 570 */     pkColumnMd.setColumnLength(pkColumnLength);
/* 571 */     pkColumnMd.setNullable(Nullable.NULLABLE);
/*     */     
/* 573 */     return pkColumnMd;
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
/*     */ 
/*     */ 
/*     */   private static void flattenArray(String catalog, String schema, String table, String baseName, ArrayNode arrayNode)
/*     */     throws ErrorException
/*     */   {
/* 594 */     int numElements = arrayNode.size();
/* 595 */     for (int i = 0; i < numElements; i++)
/*     */     {
/*     */ 
/*     */ 
/* 599 */       StringBuilder name = new StringBuilder();
/* 600 */       name.append(baseName).append("[").append(i).append("]");
/*     */       
/*     */ 
/* 603 */       if (arrayNode.get(i).isArray())
/*     */       {
/* 605 */         flattenArray(catalog, schema, table, name.toString(), (ArrayNode)arrayNode.get(i));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 612 */       else if (arrayNode.get(i).isObject())
/*     */       {
/* 614 */         flattenObject(catalog, schema, table, name.toString(), (ObjectNode)arrayNode.get(i));
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 623 */         addColumnMetadata(catalog, schema, table, name.toString(), arrayNode.get(i));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void flattenObject(String catalog, String schema, String table, String baseName, ObjectNode objectNode)
/*     */     throws ErrorException
/*     */   {
/* 651 */     Iterator<Map.Entry<String, JsonNode>> objectItr = objectNode.fields();
/* 652 */     while (objectItr.hasNext())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 657 */       Map.Entry<String, JsonNode> object = (Map.Entry)objectItr.next();
/* 658 */       StringBuilder name = new StringBuilder();
/* 659 */       name.append(baseName).append(".").append((String)object.getKey());
/* 660 */       JsonNode value = (JsonNode)object.getValue();
/*     */       
/*     */ 
/* 663 */       if (value.isArray())
/*     */       {
/* 665 */         flattenArray(catalog, schema, table, name.toString(), (ArrayNode)value);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 672 */       else if (value.isObject())
/*     */       {
/* 674 */         flattenObject(catalog, schema, table, name.toString(), (ObjectNode)value);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 683 */         addColumnMetadata(catalog, schema, table, name.toString(), value);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addColumnMetadata(String catalog, String schema, String table, String columnName, JsonNode columnValue)
/*     */     throws ErrorException
/*     */   {
/* 712 */     ColumnMetadata columnMd = null;
/* 713 */     TypeMetadata typeMeta = null;
/* 714 */     String typeName = null;
/* 715 */     int columnLength = 0;
/*     */     
/*     */ 
/* 718 */     JsonNodeType jsonDataType = columnValue.getNodeType();
/*     */     try {
/*     */       short sqlType;
/* 721 */       switch (jsonDataType)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */       case NUMBER: 
/* 727 */         if ((columnValue.isInt()) || (columnValue.isLong()))
/*     */         {
/*     */ 
/* 730 */           typeName = "BIGINT";
/* 731 */           short sqlType = -5;
/* 732 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 733 */           columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/*     */         }
/* 735 */         else if (columnValue.isDouble())
/*     */         {
/*     */ 
/* 738 */           typeName = "DOUBLE";
/* 739 */           short sqlType = 8;
/* 740 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 741 */           columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 746 */           typeName = "NUMERIC";
/* 747 */           sqlType = 2;
/* 748 */           typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 749 */           columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/*     */         }
/* 751 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case STRING: 
/* 756 */         typeName = "VARCHAR";
/* 757 */         sqlType = 12;
/* 758 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/*     */         
/* 760 */         columnLength = 255;
/* 761 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case BOOLEAN: 
/* 766 */         typeName = "BOOLEAN";
/* 767 */         sqlType = 16;
/* 768 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 769 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/* 770 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case NULL: 
/* 775 */         typeName = "NULL";
/* 776 */         sqlType = 0;
/* 777 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 778 */         columnLength = (int)TypeUtilities.getColumnSize(typeMeta, typeMeta.getPrecision());
/* 779 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case MISSING: 
/* 784 */         typeName = "MISSING";
/* 785 */         sqlType = 12;
/* 786 */         typeMeta = TypeMetadata.createTypeMetadata(sqlType);
/* 787 */         break;
/*     */       
/*     */       case OBJECT: 
/*     */       default: 
/* 791 */         throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_DATANOTSUPPORT_ERR.name(), new String[] { jsonDataType.toString(), "Data type from json not support" });
/*     */       }
/*     */       
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 799 */       if (null != typeMeta)
/*     */       {
/* 801 */         columnMd = new ColumnMetadata(typeMeta);
/*     */         
/* 803 */         typeMeta.setTypeName(typeName);
/* 804 */         columnMd.setCatalogName(catalog);
/* 805 */         columnMd.setSchemaName(schema);
/* 806 */         columnMd.setTableName(table);
/* 807 */         columnMd.setName(columnName);
/* 808 */         columnMd.setLabel(columnName);
/* 809 */         columnMd.setColumnLength(columnLength);
/* 810 */         columnMd.setNullable(Nullable.NULLABLE);
/* 811 */         m_columnMdList.add(columnMd);
/*     */       }
/*     */     }
/*     */     catch (ErrorException err)
/*     */     {
/* 816 */       throw err;
/*     */     }
/*     */     catch (NumericOverflowException ex)
/*     */     {
/* 820 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.METADATA_GENERATE_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 823 */       err.initCause(ex);
/* 824 */       throw err;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void flattenArrayHelper(JsonNodeType jsonDataType, String catalog, String schema, String tableName, String columnName, JsonNode columnValue, JsonNode rootNode)
/*     */     throws ErrorException
/*     */   {
/* 851 */     switch (jsonDataType)
/*     */     {
/*     */ 
/*     */     case ARRAY: 
/* 855 */       flattenArray(catalog, schema, tableName, columnName, (ArrayNode)columnValue);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 861 */       break;
/*     */     
/*     */ 
/*     */     case OBJECT: 
/* 865 */       flattenObject(catalog, schema, tableName, columnName, (ObjectNode)columnValue);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 871 */       break;
/*     */     
/*     */ 
/*     */     default: 
/* 875 */       addColumnMetadata(catalog, schema, tableName, columnName, columnValue);
/*     */     }
/*     */     
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/utils/CBDataTypeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */