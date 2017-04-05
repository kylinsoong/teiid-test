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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBCatalogOnlyMetadataSource
/*     */   implements IMetadataSource
/*     */ {
/*  39 */   private ArrayList<String> m_catalog = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final CBClient m_CBClient;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private int m_currentIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private boolean m_hasStartedFetch = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ILogger m_logger;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CBCatalogOnlyMetadataSource(ILogger logger, CBClient in_CBClient)
/*     */     throws ErrorException
/*     */   {
/*  76 */     LogUtilities.logFunctionEntrance(logger, new Object[] { logger });
/*     */     
/*  78 */     this.m_logger = logger;
/*  79 */     this.m_CBClient = in_CBClient;
/*     */     
/*  81 */     initCatalog();
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
/*  93 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */     try
/*     */     {
/*  97 */       closeCursor();
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
/* 115 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Boolean.valueOf(this.m_hasStartedFetch) });
/*     */     
/* 117 */     this.m_hasStartedFetch = false;
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
/* 140 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { columnTag, Long.valueOf(offset), Long.valueOf(maxSize) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     switch (columnTag)
/*     */     {
/*     */ 
/*     */     case CATALOG_NAME: 
/* 150 */       return DSITypeUtilities.outputVarCharStringData((String)this.m_catalog.get(this.m_currentIndex), data, offset, maxSize);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */     throw CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_CATALOGCOLUMN_FAIL_GEN_ERR.name(), new String[] { columnTag.toString() });
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
/*     */   public boolean hasMoreRows()
/*     */     throws ErrorException
/*     */   {
/* 178 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Boolean.valueOf(this.m_hasStartedFetch) });
/*     */     
/* 180 */     return !this.m_hasStartedFetch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initCatalog()
/*     */     throws ErrorException
/*     */   {
/* 190 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/* 191 */     if (null == this.m_catalog)
/*     */     {
/*     */       try
/*     */       {
/* 195 */         this.m_catalog = this.m_CBClient.getCatalogs();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 199 */         ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_CATALOGONLY_FAIL_ERR.name(), e.getMessage());
/*     */         
/*     */ 
/* 202 */         err.initCause(e);
/*     */         
/* 204 */         throw err;
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
/* 216 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 218 */     if (this.m_currentIndex >= this.m_catalog.size() - 1)
/*     */     {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     this.m_currentIndex += 1;
/*     */     
/* 225 */     if (!this.m_hasStartedFetch)
/*     */     {
/* 227 */       this.m_hasStartedFetch = true;
/*     */     }
/*     */     
/* 230 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/CBCatalogOnlyMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */