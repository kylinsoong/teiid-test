/*     */ package com.simba.couchbase.dataengine.metadata;
/*     */ 
/*     */ import com.simba.couchbase.core.CBClient;
/*     */ import com.simba.couchbase.core.CBJDBCDriver;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.dsi.dataengine.interfaces.IMetadataSource;
/*     */ import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
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
/*     */ public class CBTablesMetadataSource
/*     */   implements IMetadataSource
/*     */ {
/*     */   private final CBClient m_CBClient;
/*     */   
/*     */   private class CouchbaseTablesMetadataSourceRow
/*     */   {
/*     */     private final String m_catalogName;
/*     */     private final String m_schemaName;
/*     */     private final String m_tableName;
/*     */     
/*     */     public CouchbaseTablesMetadataSourceRow(String catalogName, String schemaName, String tableName)
/*     */     {
/*  48 */       this.m_catalogName = catalogName;
/*  49 */       this.m_schemaName = schemaName;
/*  50 */       this.m_tableName = tableName;
/*     */     }
/*     */     
/*     */     public String getCatalogName() throws ErrorException
/*     */     {
/*  55 */       return this.m_catalogName;
/*     */     }
/*     */     
/*     */     public String getSchemaName()
/*     */     {
/*  60 */       return this.m_schemaName;
/*     */     }
/*     */     
/*     */     public String getTableName()
/*     */     {
/*  65 */       return this.m_tableName;
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
/*  81 */   private int m_currentIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private boolean m_hasStartedFetch = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ILogger m_logger;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<MetadataSourceColumnTag, String> m_restrictions;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private ArrayList<CouchbaseTablesMetadataSourceRow> m_tables = null;
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
/*     */   public CBTablesMetadataSource(ILogger logger, CBClient CBClient, Map<MetadataSourceColumnTag, String> restrictions)
/*     */     throws ErrorException
/*     */   {
/* 121 */     LogUtilities.logFunctionEntrance(logger, new Object[] { logger });
/*     */     
/* 123 */     this.m_logger = logger;
/* 124 */     this.m_CBClient = CBClient;
/* 125 */     this.m_restrictions = restrictions;
/*     */     
/* 127 */     initTables();
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
/* 139 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */     try
/*     */     {
/* 143 */       closeCursor();
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
/*     */ 
/*     */   public void closeCursor()
/*     */     throws ErrorException
/*     */   {
/* 162 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 164 */     this.m_hasStartedFetch = false;
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
/*     */   public boolean getMetadata(MetadataSourceColumnTag columnTag, long offset, long maxSize, DataWrapper data)
/*     */     throws ErrorException
/*     */   {
/* 187 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { columnTag, Long.valueOf(offset), Long.valueOf(maxSize) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */     switch (columnTag)
/*     */     {
/*     */ 
/*     */     case CATALOG_NAME: 
/* 197 */       return DSITypeUtilities.outputVarCharStringData(((CouchbaseTablesMetadataSourceRow)this.m_tables.get(this.m_currentIndex)).getCatalogName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case SCHEMA_NAME: 
/* 207 */       return DSITypeUtilities.outputVarCharStringData(((CouchbaseTablesMetadataSourceRow)this.m_tables.get(this.m_currentIndex)).getSchemaName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case TABLE_NAME: 
/* 216 */       return DSITypeUtilities.outputVarCharStringData(((CouchbaseTablesMetadataSourceRow)this.m_tables.get(this.m_currentIndex)).getTableName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case TABLE_TYPE: 
/* 225 */       return DSITypeUtilities.outputVarCharStringData("TABLE", data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case REMARKS: 
/* 234 */       return DSITypeUtilities.outputVarCharStringData("", data, offset, maxSize);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 243 */     throw CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.METADATA_COLUMN_NOT_FOUND.name(), new String[] { columnTag.toString() });
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
/*     */   public boolean hasMoreRows()
/*     */     throws ErrorException
/*     */   {
/* 262 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */     return !this.m_hasStartedFetch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initTables()
/*     */     throws ErrorException
/*     */   {
/*     */     try
/*     */     {
/* 280 */       this.m_tables = new ArrayList();
/*     */       
/* 282 */       catalogRestriction = MetadataUtils.GetRestriction(this.m_restrictions, MetadataSourceColumnTag.CATALOG_NAME);
/*     */       
/*     */ 
/* 285 */       schemaRestriction = MetadataUtils.GetRestriction(this.m_restrictions, MetadataSourceColumnTag.SCHEMA_NAME);
/*     */       
/*     */       Iterator i$;
/*     */       
/*     */       String currentCatalog;
/* 290 */       if ((null == catalogRestriction) || (catalogRestriction.equalsIgnoreCase(""))) { Iterator i$;
/*     */         String currentCatalog;
/* 292 */         Iterator i$; String currentSchema; if ((null == schemaRestriction) || (schemaRestriction.equalsIgnoreCase("")))
/*     */         {
/* 294 */           for (i$ = this.m_CBClient.getCatalogs().iterator(); i$.hasNext();) { currentCatalog = (String)i$.next();
/*     */             
/* 296 */             for (i$ = this.m_CBClient.getSchemasFromSchemaMap(currentCatalog).iterator(); i$.hasNext();) { currentSchema = (String)i$.next();
/*     */               
/* 298 */               for (String currentTable : this.m_CBClient.getTablesFromSchemaMap(currentCatalog, currentSchema))
/*     */               {
/*     */ 
/*     */ 
/* 302 */                 this.m_tables.add(new CouchbaseTablesMetadataSourceRow(currentCatalog, currentSchema, currentTable));
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 312 */           for (i$ = this.m_CBClient.getCatalogs().iterator(); i$.hasNext();) { currentCatalog = (String)i$.next();
/*     */             
/* 314 */             for (String currentTable : this.m_CBClient.getTablesFromSchemaMap(currentCatalog, schemaRestriction))
/*     */             {
/*     */ 
/* 317 */               if (null != currentTable)
/*     */               {
/* 319 */                 this.m_tables.add(new CouchbaseTablesMetadataSourceRow(currentCatalog, schemaRestriction, currentTable));
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 330 */         for (String currentTable : this.m_CBClient.getTablesFromSchemaMap(catalogRestriction, schemaRestriction))
/*     */         {
/*     */ 
/* 333 */           this.m_tables.add(new CouchbaseTablesMetadataSourceRow(catalogRestriction, schemaRestriction, currentTable));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ErrorException ex)
/*     */     {
/*     */       String catalogRestriction;
/*     */       
/*     */       String schemaRestriction;
/*     */       
/* 343 */       throw ex;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 347 */       ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_TABLEONLY_FAIL_ERR.name(), e.getMessage());
/*     */       
/*     */ 
/* 350 */       err.initCause(e);
/*     */       
/* 352 */       throw err;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean moveToNextRow()
/*     */   {
/* 364 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 366 */     if (this.m_currentIndex >= this.m_tables.size() - 1)
/*     */     {
/* 368 */       return false;
/*     */     }
/*     */     
/* 371 */     this.m_currentIndex += 1;
/*     */     
/* 373 */     if (!this.m_hasStartedFetch)
/*     */     {
/* 375 */       this.m_hasStartedFetch = true;
/*     */     }
/*     */     
/* 378 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/CBTablesMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */