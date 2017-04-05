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
/*     */ public class CBSchemaOnlyMetadataSource
/*     */   implements IMetadataSource
/*     */ {
/*     */   private final CBClient m_CBClient;
/*     */   
/*     */   public class CouchbaseSchemaMetadataSourceRow
/*     */   {
/*     */     private final String m_catalogName;
/*     */     private final String m_schemaName;
/*     */     
/*     */     public CouchbaseSchemaMetadataSourceRow(String catalogName, String schemaName)
/*     */     {
/*  46 */       this.m_catalogName = catalogName;
/*  47 */       this.m_schemaName = schemaName;
/*     */     }
/*     */     
/*     */     public String getCatalogName()
/*     */     {
/*  52 */       return this.m_catalogName;
/*     */     }
/*     */     
/*     */     public String getSchemaName()
/*     */     {
/*  57 */       return this.m_schemaName;
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
/*  73 */   private int m_currentIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private boolean m_hasStartedFetch = false;
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
/*  93 */   private ArrayList<CouchbaseSchemaMetadataSourceRow> m_schemas = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CBSchemaOnlyMetadataSource(ILogger logger, CBClient CBClient, Map<MetadataSourceColumnTag, String> restrictions)
/*     */     throws ErrorException
/*     */   {
/* 117 */     LogUtilities.logFunctionEntrance(logger, new Object[] { logger });
/*     */     
/* 119 */     this.m_logger = logger;
/* 120 */     this.m_CBClient = CBClient;
/* 121 */     this.m_restrictions = restrictions;
/* 122 */     initSchemas();
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
/* 134 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */     try
/*     */     {
/* 138 */       closeCursor();
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
/* 156 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 158 */     this.m_hasStartedFetch = false;
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
/* 181 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { columnTag, Long.valueOf(offset), Long.valueOf(maxSize) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */     switch (columnTag)
/*     */     {
/*     */ 
/*     */     case CATALOG_NAME: 
/* 191 */       return DSITypeUtilities.outputVarCharStringData(((CouchbaseSchemaMetadataSourceRow)this.m_schemas.get(this.m_currentIndex)).getCatalogName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case SCHEMA_NAME: 
/* 200 */       return DSITypeUtilities.outputVarCharStringData(((CouchbaseSchemaMetadataSourceRow)this.m_schemas.get(this.m_currentIndex)).getSchemaName(), data, offset, maxSize);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */     throw CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_GEN_ERR.name(), new String[] { columnTag.toString() });
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
/* 230 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 234 */     return !this.m_hasStartedFetch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initSchemas()
/*     */     throws ErrorException
/*     */   {
/* 245 */     if (null == this.m_schemas)
/*     */     {
/*     */       try
/*     */       {
/* 249 */         this.m_schemas = new ArrayList();
/*     */         
/* 251 */         catalogRestriction = MetadataUtils.GetRestriction(this.m_restrictions, MetadataSourceColumnTag.CATALOG_NAME);
/*     */         
/*     */         Iterator i$;
/*     */         
/*     */         String catalogName;
/* 256 */         if ((null == catalogRestriction) || (catalogRestriction.equalsIgnoreCase("")))
/*     */         {
/* 258 */           for (i$ = this.m_CBClient.getCatalogs().iterator(); i$.hasNext();) { catalogName = (String)i$.next();
/*     */             
/* 260 */             for (String currentSchema : this.m_CBClient.getSchemasFromSchemaMap(catalogName))
/*     */             {
/* 262 */               this.m_schemas.add(new CouchbaseSchemaMetadataSourceRow(catalogName, currentSchema));
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 270 */           for (String currentSchema : this.m_CBClient.getSchemasFromSchemaMap(catalogRestriction))
/*     */           {
/* 272 */             this.m_schemas.add(new CouchbaseSchemaMetadataSourceRow(catalogRestriction, currentSchema));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (ErrorException ex)
/*     */       {
/*     */         String catalogRestriction;
/*     */         
/* 280 */         throw ex;
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 284 */         ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_CATALOGSCHEMAONLY_FAIL_ERR.name(), e.getMessage());
/*     */         
/*     */ 
/* 287 */         err.initCause(e);
/*     */         
/* 289 */         throw err;
/*     */       }
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
/* 301 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 303 */     if (this.m_currentIndex >= this.m_schemas.size() - 1)
/*     */     {
/* 305 */       return false;
/*     */     }
/*     */     
/* 308 */     this.m_currentIndex += 1;
/*     */     
/* 310 */     if (!this.m_hasStartedFetch)
/*     */     {
/* 312 */       this.m_hasStartedFetch = true;
/*     */     }
/*     */     
/* 315 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/CBSchemaOnlyMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */