/*     */ package com.simba.couchbase.dataengine.metadata;
/*     */ 
/*     */ import com.simba.couchbase.core.CBClient;
/*     */ import com.simba.couchbase.core.CBConnectionSettings;
/*     */ import com.simba.couchbase.core.CBJDBCDriver;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.dsi.dataengine.interfaces.IMetadataSource;
/*     */ import com.simba.dsi.dataengine.utilities.ColumnMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
/*     */ import com.simba.dsi.dataengine.utilities.Nullable;
/*     */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.TypeUtilities;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import com.simba.support.exceptions.ExceptionBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public class CBColumnsMetadataSource
/*     */   implements IMetadataSource
/*     */ {
/*     */   private final CBClient m_CBClient;
/*  68 */   private ArrayList<ColumnMetadata> m_columns = null;
/*     */   
/*     */ 
/*     */ 
/*  72 */   private int m_currentIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private int m_currentColumn = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String m_currentSchema;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String m_currentTable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   private boolean m_hasStartedFetch = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ILogger m_logger;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private Map<MetadataSourceColumnTag, String> m_restrictions = null;
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
/*     */   public CBColumnsMetadataSource(ILogger logger, Map<MetadataSourceColumnTag, String> restrictions, CBClient CBClient, CBConnectionSettings settings)
/*     */     throws ErrorException
/*     */   {
/* 123 */     LogUtilities.logFunctionEntrance(logger, new Object[] { logger });
/*     */     
/* 125 */     this.m_logger = logger;
/* 126 */     this.m_CBClient = CBClient;
/* 127 */     this.m_restrictions = restrictions;
/* 128 */     this.m_columns = new ArrayList();
/*     */     
/* 130 */     initializeColumn();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 142 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */     try
/*     */     {
/* 146 */       closeCursor();
/*     */     }
/*     */     catch (ErrorException e) {}
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
/*     */   public void closeCursor()
/*     */     throws ErrorException
/*     */   {
/* 164 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 166 */     this.m_hasStartedFetch = false;
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
/*     */   public boolean getMetadata(MetadataSourceColumnTag columnTag, long offset, long maxSize, DataWrapper data)
/*     */     throws ErrorException
/*     */   {
/* 190 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { columnTag, Long.valueOf(offset), Long.valueOf(maxSize) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 196 */     switch (columnTag)
/*     */     {
/*     */ 
/*     */     case CATALOG_NAME: 
/* 200 */       return DSITypeUtilities.outputVarCharStringData(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getCatalogName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case SCHEMA_NAME: 
/* 208 */       return DSITypeUtilities.outputVarCharStringData(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getSchemaName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case TABLE_NAME: 
/* 216 */       return DSITypeUtilities.outputVarCharStringData(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTableName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case COLUMN_NAME: 
/* 224 */       return DSITypeUtilities.outputVarCharStringData(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case DATA_TYPE: 
/* 232 */       data.setSmallInt(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType());
/*     */       
/* 234 */       return false;
/*     */     
/*     */ 
/*     */     case DATA_TYPE_NAME: 
/* 238 */       return DSITypeUtilities.outputVarCharStringData(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getTypeName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case COLUMN_SIZE: 
/* 246 */       data.setSmallInt((short)(int)((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getColumnLength());
/* 247 */       return false;
/*     */     
/*     */ 
/*     */     case BUFFER_LENGTH: 
/* 251 */       data.setSmallInt(MetadataUtils.getBufferSize(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata(), this.m_logger));
/*     */       
/*     */ 
/* 254 */       return false;
/*     */     
/*     */ 
/*     */     case DECIMAL_DIGITS: 
/* 258 */       data.setSmallInt(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getScale());
/*     */       
/* 260 */       return false;
/*     */     
/*     */ 
/*     */     case NUM_PREC_RADIX: 
/* 264 */       data.setSmallInt(MetadataUtils.getNumPrecRadix(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata()));
/*     */       
/*     */ 
/* 267 */       return false;
/*     */     
/*     */ 
/*     */     case NULLABLE: 
/* 271 */       data.setSmallInt(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getNullable().ordinal());
/* 272 */       return false;
/*     */     
/*     */ 
/*     */     case REMARKS: 
/* 276 */       data.setNull(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType());
/* 277 */       return false;
/*     */     
/*     */ 
/*     */     case COLUMN_DEF: 
/* 281 */       data.setNull(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType());
/* 282 */       return false;
/*     */     
/*     */ 
/*     */     case SQL_DATA_TYPE: 
/* 286 */       int sqlType = ((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType();
/* 287 */       data.setSmallInt(TypeUtilities.getVerboseTypeFromConciseType(sqlType));
/*     */       
/* 289 */       return false;
/*     */     
/*     */ 
/*     */     case SQL_DATETIME_SUB: 
/* 293 */       int sqlType = ((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType();
/* 294 */       short datetimeSub = TypeUtilities.getIntervalCodeFromConciseType(sqlType);
/*     */       
/* 296 */       if (0 == datetimeSub)
/*     */       {
/* 298 */         data.setNull(5);
/*     */       }
/*     */       else
/*     */       {
/* 302 */         data.setSmallInt(datetimeSub);
/*     */       }
/*     */       
/* 305 */       return false;
/*     */     
/*     */ 
/*     */     case CHAR_OCTET_LENGTH: 
/* 309 */       int sqlType = ((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType();
/*     */       
/* 311 */       if (TypeUtilities.isCharacterOrBinaryType(sqlType))
/*     */       {
/* 313 */         data.setInteger(MetadataUtils.getBufferSize(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata(), this.m_logger));
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 318 */         data.setNull(4);
/*     */       }
/*     */       
/* 321 */       return false;
/*     */     
/*     */ 
/*     */     case ORDINAL_POSITION: 
/* 325 */       data.setInteger(this.m_currentColumn);
/*     */       
/* 327 */       return false;
/*     */     
/*     */ 
/*     */     case IS_NULLABLE: 
/* 331 */       return DSITypeUtilities.outputVarCharStringData(nullableToIsNullable(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getNullable()), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case USER_DATA_TYPE: 
/* 339 */       data.setSmallInt(0);
/*     */       
/* 341 */       return false;
/*     */     
/*     */ 
/*     */     case IS_AUTOINCREMENT: 
/* 345 */       data.setNull(((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTypeMetadata().getType());
/* 346 */       return false;
/*     */     
/*     */ 
/*     */     case IS_GENERATEDCOLUMN: 
/* 350 */       return false;
/*     */     }
/*     */     
/*     */     
/* 354 */     throw CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.METADATA_COLUMN_NOT_FOUND.name(), new String[] { columnTag.toString() });
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
/*     */   public boolean hasMoreRows()
/*     */     throws ErrorException
/*     */   {
/* 375 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */ 
/* 378 */     return this.m_currentIndex + 1 < this.m_columns.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeColumn()
/*     */     throws ErrorException
/*     */   {
/* 388 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     try
/*     */     {
/* 391 */       catalogRestriction = MetadataUtils.GetRestriction(this.m_restrictions, MetadataSourceColumnTag.CATALOG_NAME);
/*     */       
/*     */ 
/* 394 */       schemaRestriction = MetadataUtils.GetRestriction(this.m_restrictions, MetadataSourceColumnTag.SCHEMA_NAME);
/*     */       
/*     */ 
/* 397 */       String tableRestriction = MetadataUtils.GetRestriction(this.m_restrictions, MetadataSourceColumnTag.SCHEMA_NAME);
/*     */       
/*     */       Iterator i$;
/*     */       String currentCatalog;
/* 401 */       if ((null == catalogRestriction) || (catalogRestriction.equalsIgnoreCase(""))) {
/*     */         Iterator i$;
/*     */         String currentCatalog;
/* 404 */         if ((null == schemaRestriction) || (schemaRestriction.equalsIgnoreCase(""))) { Iterator i$;
/*     */           String currentCatalog;
/* 406 */           Iterator i$; String currentSchema; if ((null == tableRestriction) || (tableRestriction.equalsIgnoreCase("")))
/*     */           {
/* 408 */             for (i$ = this.m_CBClient.getCatalogs().iterator(); i$.hasNext();) { currentCatalog = (String)i$.next();
/*     */               
/* 410 */               for (i$ = this.m_CBClient.getSchemasFromSchemaMap(currentCatalog).iterator(); i$.hasNext();) { currentSchema = (String)i$.next();
/*     */                 
/* 412 */                 for (String currentTable : this.m_CBClient.getTablesFromSchemaMap(currentCatalog, currentSchema))
/*     */                 {
/*     */ 
/*     */ 
/* 416 */                   for (ColumnMetadata currentColumn : this.m_CBClient.getColumnsFromSchemaMap(currentCatalog, currentSchema, currentTable))
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/* 421 */                     this.m_columns.add(currentColumn);
/*     */                   }
/*     */                   
/*     */                 }
/*     */                 
/*     */               }
/*     */             }
/*     */           } else {
/* 429 */             for (i$ = this.m_CBClient.getCatalogs().iterator(); i$.hasNext();) { currentCatalog = (String)i$.next();
/*     */               
/* 431 */               for (String currentSchema : this.m_CBClient.getSchemasFromSchemaMap(currentCatalog))
/*     */               {
/* 433 */                 for (ColumnMetadata currentColumn : this.m_CBClient.getColumnsFromSchemaMap(currentCatalog, currentSchema, tableRestriction))
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/* 438 */                   this.m_columns.add(currentColumn);
/*     */                 }
/*     */                 
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 447 */           for (i$ = this.m_CBClient.getCatalogs().iterator(); i$.hasNext();) { currentCatalog = (String)i$.next();
/*     */             
/* 449 */             for (String currentTable : this.m_CBClient.getTablesFromSchemaMap(currentCatalog, schemaRestriction))
/*     */             {
/*     */ 
/*     */ 
/* 453 */               for (ColumnMetadata currentColumn : this.m_CBClient.getColumnsFromSchemaMap(currentCatalog, schemaRestriction, currentTable))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/* 458 */                 this.m_columns.add(currentColumn);
/*     */               }
/*     */               
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 467 */         for (String currentTable : this.m_CBClient.getTablesFromSchemaMap(catalogRestriction, schemaRestriction))
/*     */         {
/*     */ 
/*     */ 
/* 471 */           for (ColumnMetadata currentColumn : this.m_CBClient.getColumnsFromSchemaMap(catalogRestriction, schemaRestriction, currentTable))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 476 */             this.m_columns.add(currentColumn);
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (ErrorException ex) {
/*     */       String catalogRestriction;
/*     */       String schemaRestriction;
/* 483 */       throw ex;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 487 */       ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_CATALOGCOLUMN_FAIL_ERR.name(), e.getMessage());
/*     */       
/*     */ 
/* 490 */       err.initCause(e);
/*     */       
/* 492 */       throw err;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean moveToNextRow()
/*     */   {
/* 503 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 505 */     if (this.m_currentIndex >= this.m_columns.size() - 1)
/*     */     {
/* 507 */       return false;
/*     */     }
/*     */     
/* 510 */     this.m_currentIndex += 1;
/*     */     
/*     */ 
/* 513 */     if (((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getSchemaName().equalsIgnoreCase(this.m_currentSchema))
/*     */     {
/*     */ 
/* 516 */       if (((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTableName().equalsIgnoreCase(this.m_currentTable))
/*     */       {
/* 518 */         this.m_currentColumn += 1;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 523 */         this.m_currentTable = ((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getTableName();
/* 524 */         this.m_currentColumn = 1;
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 530 */       this.m_currentSchema = ((ColumnMetadata)this.m_columns.get(this.m_currentIndex)).getSchemaName();
/* 531 */       this.m_currentColumn = 1;
/*     */     }
/*     */     
/* 534 */     if (!this.m_hasStartedFetch)
/*     */     {
/* 536 */       this.m_hasStartedFetch = true;
/*     */     }
/*     */     
/* 539 */     return true;
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
/*     */   private String nullableToIsNullable(Nullable nullable)
/*     */   {
/* 560 */     if (nullable == Nullable.NULLABLE)
/*     */     {
/* 562 */       return "YES";
/*     */     }
/* 564 */     if (nullable == Nullable.NO_NULLS)
/*     */     {
/* 566 */       return "NO";
/*     */     }
/*     */     
/*     */ 
/* 570 */     return "";
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/CBColumnsMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */