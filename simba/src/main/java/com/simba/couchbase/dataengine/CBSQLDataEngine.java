/*     */ package com.simba.couchbase.dataengine;
/*     */ 
/*     */ import com.simba.couchbase.core.CBClient;
/*     */ import com.simba.couchbase.core.CBConnectionSettings;
/*     */ import com.simba.couchbase.core.CBJDBCDriver;
/*     */ import com.simba.couchbase.core.CBStatement;
/*     */ import com.simba.couchbase.dataengine.metadata.CBCatalogOnlyMetadataSource;
/*     */ import com.simba.couchbase.dataengine.metadata.CBColumnsMetadataSource;
/*     */ import com.simba.couchbase.dataengine.metadata.CBPrimaryKeyMetadataSource;
/*     */ import com.simba.couchbase.dataengine.metadata.CBSchemaOnlyMetadataSource;
/*     */ import com.simba.couchbase.dataengine.metadata.CBTablesMetadataSource;
/*     */ import com.simba.couchbase.dataengine.metadata.CBTypeInfoMetadataSource;
/*     */ import com.simba.couchbase.dataengine.operationhandler.CBOperationHandlerFactory;
/*     */ import com.simba.couchbase.dataengine.querygeneration.CBAliasGenerator;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.dsi.core.impl.DSIDriverSingleton;
/*     */ import com.simba.dsi.dataengine.impl.DSIEmptyMetadataSource;
/*     */ import com.simba.dsi.dataengine.interfaces.IMetadataSource;
/*     */ import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
/*     */ import com.simba.dsi.dataengine.utilities.ColumnMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceID;
/*     */ import com.simba.jdbc.utils.ParseQueryUtils;
/*     */ import com.simba.sqlengine.SQLEngineGenericContext;
/*     */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
/*     */ import com.simba.sqlengine.dsiext.dataengine.DSIExtOperationHandlerFactory;
/*     */ import com.simba.sqlengine.dsiext.dataengine.OpenTableType;
/*     */ import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
/*     */ import com.simba.sqlengine.dsiext.dataengine.StoredProcedure;
/*     */ import com.simba.sqlengine.exceptions.SQLEngineException;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.MessageSourceImpl;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBSQLDataEngine
/*     */   extends SqlDataEngine
/*     */ {
/*     */   private CBConnectionSettings m_settings;
/*     */   private CBClient m_client;
/*     */   private CBAliasGenerator m_columnAliasGenerator;
/*     */   private boolean m_isDirectExecution;
/*     */   private String m_query;
/*     */   private ArrayList<ColumnMetadata> m_selectColumnListForTranslationCache;
/*     */   private CBAliasGenerator m_tableAliasGenerator;
/*  96 */   private static boolean s_isMessageSourceSet = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CBSQLDataEngine(CBStatement statement, CBConnectionSettings settings, CBClient client)
/*     */     throws ErrorException
/*     */   {
/* 115 */     super(statement);
/* 116 */     LogUtilities.logFunctionEntrance(getLog(), new Object[] { statement, this.m_client });
/* 117 */     initMessageSource();
/*     */     
/* 119 */     this.m_settings = settings;
/* 120 */     this.m_client = client;
/* 121 */     this.m_selectColumnListForTranslationCache = new ArrayList();
/*     */     
/*     */ 
/* 124 */     this.m_tableAliasGenerator = new CBAliasGenerator("$sb_t");
/* 125 */     this.m_columnAliasGenerator = new CBAliasGenerator("$sb_c");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public DSIExtOperationHandlerFactory createOperationHandlerFactory()
/*     */   {
/* 156 */     return new CBOperationHandlerFactory(this, getLog());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CBAliasGenerator getColumnAliasGenerator()
/*     */   {
/* 165 */     return this.m_columnAliasGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CBAliasGenerator getTableAliasGenerator()
/*     */   {
/* 174 */     return this.m_tableAliasGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StoredProcedure openProcedure(String catalogName, String schemaName, String procName)
/*     */     throws ErrorException
/*     */   {
/* 207 */     LogUtilities.logFunctionEntrance(getLog(), new Object[] { catalogName, schemaName, procName });
/*     */     
/* 209 */     if (null == schemaName)
/*     */     {
/* 211 */       schemaName = "";
/*     */     }
/*     */     
/* 214 */     if (null == catalogName)
/*     */     {
/* 216 */       catalogName = "";
/*     */     }
/*     */     
/* 219 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DSIExtJResultSet openTable(String catalogName, String schemaName, String tableName, OpenTableType openType)
/*     */     throws ErrorException
/*     */   {
/* 259 */     LogUtilities.logFunctionEntrance(getLog(), new Object[] { catalogName, schemaName, tableName, openType });
/*     */     
/* 261 */     DSIExtJResultSet extTable = this.m_client.openTable(catalogName, schemaName, tableName, this.m_selectColumnListForTranslationCache, openType);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */     return extTable;
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
/*     */   public IQueryExecutor prepare(String query)
/*     */     throws com.simba.dsi.exceptions.ParsingException, ErrorException
/*     */   {
/* 289 */     LogUtilities.logFunctionEntrance(getLog(), new Object[] { query });
/*     */     
/* 291 */     if (this.m_settings.m_queryMode)
/*     */     {
/* 293 */       return new CBNativeQueryExecutor(query, this.m_settings, this.m_client, this.m_isDirectExecution, getLog());
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
/*     */     try
/*     */     {
/* 307 */       List<String> queryList = ParseQueryUtils.splitQueries(query);
/* 308 */       this.m_query = ((String)queryList.get(0));
/* 309 */       return super.prepare(this.m_query);
/*     */     }
/*     */     catch (com.simba.jdbc.utils.ParsingException ex)
/*     */     {
/* 313 */       throw ex;
/*     */     }
/*     */     catch (SQLEngineException engineException)
/*     */     {
/* 317 */       throw engineException;
/*     */ 
/*     */     }
/*     */     catch (SQLEngineException engineException)
/*     */     {
/*     */ 
/* 323 */       return new CBNativeQueryExecutor(this.m_query, this.m_settings, this.m_client, this.m_isDirectExecution, getLog());
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (com.simba.jdbc.utils.ParsingException ex)
/*     */     {
/*     */ 
/*     */ 
/* 332 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_INFO_FAIL_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 335 */       err.initCause(ex);
/* 336 */       throw err;
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
/*     */   public void setDirectExecute()
/*     */   {
/* 352 */     this.m_isDirectExecution = true;
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
/*     */   protected IMetadataSource makeNewMetadataSource(MetadataSourceID metadataSourceId, Map<MetadataSourceColumnTag, String> restrictions, String escapeChar, String identifierQuoteChar, boolean filterAsIdentifier)
/*     */     throws ErrorException
/*     */   {
/* 379 */     LogUtilities.logFunctionEntrance(getLog(), new Object[] { restrictions });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 406 */     IMetadataSource result = null;
/*     */     
/* 408 */     switch (metadataSourceId)
/*     */     {
/*     */ 
/*     */     case TYPE_INFO: 
/* 412 */       return new CBTypeInfoMetadataSource(getLog());
/*     */     
/*     */ 
/*     */ 
/*     */     case CATALOG_SCHEMA_ONLY: 
/* 417 */       return new CBSchemaOnlyMetadataSource(getLog(), this.m_client, restrictions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case SCHEMA_ONLY: 
/* 425 */       return new CBSchemaOnlyMetadataSource(getLog(), this.m_client, restrictions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case CATALOG_ONLY: 
/* 433 */       return new CBCatalogOnlyMetadataSource(getLog(), this.m_client);
/*     */     
/*     */ 
/*     */ 
/*     */     case COLUMNS: 
/* 438 */       return new CBColumnsMetadataSource(getLog(), restrictions, this.m_client, this.m_settings);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case TABLES: 
/* 447 */       return new CBTablesMetadataSource(getLog(), this.m_client, restrictions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case TABLETYPE_ONLY: 
/*     */       break;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case PRIMARY_KEYS: 
/* 461 */       return new CBPrimaryKeyMetadataSource(getLog(), this.m_client, restrictions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     case FOREIGN_KEYS: 
/*     */       break;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     default: 
/* 474 */       result = new DSIEmptyMetadataSource(getLog());
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 479 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized void initMessageSource()
/*     */   {
/* 491 */     if (!s_isMessageSourceSet)
/*     */     {
/* 493 */       SQLEngineGenericContext.setDefaultMsgSource((MessageSourceImpl)((CBJDBCDriver)DSIDriverSingleton.getInstance()).getMessageSource());
/*     */       
/*     */ 
/* 496 */       s_isMessageSourceSet = true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/CBSQLDataEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */