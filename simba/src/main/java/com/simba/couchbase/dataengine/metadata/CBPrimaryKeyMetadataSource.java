/*     */ package com.simba.couchbase.dataengine.metadata;
/*     */ 
/*     */ import com.simba.couchbase.core.CBClient;
/*     */ import com.simba.couchbase.core.CBJDBCDriver;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.schemamap.metadata.CBCatalogMetadata;
/*     */ import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
/*     */ import com.simba.couchbase.schemamap.metadata.CBSMMetadata;
/*     */ import com.simba.couchbase.schemamap.metadata.CBSchemaMetadata;
/*     */ import com.simba.couchbase.schemamap.metadata.CBTableMetadata;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.dsi.dataengine.interfaces.IMetadataSource;
/*     */ import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import com.simba.support.exceptions.ExceptionBuilder;
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
/*     */ public class CBPrimaryKeyMetadataSource
/*     */   implements IMetadataSource
/*     */ {
/*     */   private ILogger m_logger;
/*     */   private List<CBColumnMetadata> m_primaryKeys;
/*  40 */   private int m_currentRow = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<MetadataSourceColumnTag, String> m_restrictions;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CBPrimaryKeyMetadataSource(ILogger logger, CBClient client, Map<MetadataSourceColumnTag, String> restrictions)
/*     */     throws ErrorException
/*     */   {
/*  62 */     this.m_logger = logger;
/*  63 */     this.m_restrictions = restrictions;
/*  64 */     this.m_primaryKeys = new ArrayList();
/*  65 */     initPrimaryKeys(client);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*  74 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */   }
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
/*  87 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
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
/*     */   public boolean getMetadata(MetadataSourceColumnTag metadataSourceColumnTag, long offset, long maxSize, DataWrapper data)
/*     */     throws ErrorException
/*     */   {
/* 110 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { metadataSourceColumnTag, Long.valueOf(offset), Long.valueOf(maxSize) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */     switch (metadataSourceColumnTag)
/*     */     {
/*     */ 
/*     */     case PRIMARY_KEY_CATALOG_NAME: 
/* 120 */       return DSITypeUtilities.outputVarCharStringData(((CBColumnMetadata)this.m_primaryKeys.get(this.m_currentRow)).getCatalogName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case PRIMARY_KEY_SCHEMA_NAME: 
/* 128 */       return DSITypeUtilities.outputVarCharStringData(((CBColumnMetadata)this.m_primaryKeys.get(this.m_currentRow)).getSchemaName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case PRIMARY_KEY_TABLE_NAME: 
/* 136 */       return DSITypeUtilities.outputVarCharStringData(((CBColumnMetadata)this.m_primaryKeys.get(this.m_currentRow)).getTableName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case PRIMARY_KEY_COLUMN_NAME: 
/* 144 */       return DSITypeUtilities.outputVarCharStringData(((CBColumnMetadata)this.m_primaryKeys.get(this.m_currentRow)).getName(), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case KEY_SEQ: 
/* 152 */       return DSITypeUtilities.outputVarCharStringData(Integer.toString(this.m_currentRow + 1), data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case PRIMARY_KEY_NAME: 
/* 160 */       data.setNull(12);
/* 161 */       return false;
/*     */     }
/*     */     
/*     */     
/* 165 */     ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CATALOGFUN_PRIMARYKEY_FAIL_GEN_ERR.name(), new String[0]);
/*     */     
/*     */ 
/* 168 */     throw err;
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
/*     */   public boolean hasMoreRows()
/*     */     throws ErrorException
/*     */   {
/* 185 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/* 186 */     return this.m_currentRow < this.m_primaryKeys.size() - 1;
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
/* 197 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/* 198 */     if (this.m_currentRow < this.m_primaryKeys.size())
/*     */     {
/* 200 */       this.m_currentRow += 1;
/*     */     }
/*     */     
/* 203 */     return this.m_currentRow < this.m_primaryKeys.size();
/*     */   }
/*     */   
/*     */   public void initPrimaryKeys(CBClient client) throws ErrorException
/*     */   {
/* 208 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 210 */     String catalogName = (String)this.m_restrictions.get(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME);
/* 211 */     String schemaName = (String)this.m_restrictions.get(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME);
/* 212 */     String tableName = (String)this.m_restrictions.get(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME);
/*     */     
/* 214 */     CBSMMetadata metadata = client.getSchemaMapMeta();
/* 215 */     ArrayList<CBCatalogMetadata> catalogList = new ArrayList();
/* 216 */     ArrayList<CBSchemaMetadata> schemaList = new ArrayList();
/* 217 */     ArrayList<CBTableMetadata> tableList = new ArrayList();
/* 218 */     ArrayList<CBColumnMetadata> columnList = new ArrayList();
/*     */     
/* 220 */     if ((null == catalogName) || (catalogName.equalsIgnoreCase("")))
/*     */     {
/* 222 */       for (String currCatalog : client.getCatalogs())
/*     */       {
/* 224 */         catalogList.add(metadata.getCatalogMeta(currCatalog));
/*     */       }
/* 226 */       if (catalogList.isEmpty())
/*     */       {
/*     */ 
/* 229 */         ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_PRIMARYKEY_NO_CATALOG_FOUND_ERR.name(), "No catalogs found");
/*     */         
/*     */ 
/* 232 */         throw err;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 239 */         catalogList.add(metadata.getCatalogMeta(catalogName));
/*     */ 
/*     */       }
/*     */       catch (NullPointerException ex)
/*     */       {
/* 244 */         ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_PRIMARYKEY_NO_CATALOG_FOUND_ERR.name(), "No specified catalog found");
/*     */         
/*     */ 
/* 247 */         throw err;
/*     */       }
/*     */     }
/* 250 */     for (CBCatalogMetadata currCatalog : catalogList)
/*     */     {
/* 252 */       if ((null == schemaName) || (schemaName.equalsIgnoreCase("")))
/*     */       {
/* 254 */         for (String currSchema : currCatalog.getSchemaNameList())
/*     */         {
/* 256 */           schemaList.add(currCatalog.getSchemaMeta(currSchema));
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/*     */         try
/*     */         {
/* 263 */           if (null != currCatalog.getSchemaMeta(schemaName))
/*     */           {
/* 265 */             schemaList.add(currCatalog.getSchemaMeta(schemaName));
/*     */           }
/*     */         }
/*     */         catch (NullPointerException ex) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 274 */     if (schemaList.isEmpty())
/*     */     {
/*     */ 
/* 277 */       ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_PRIMARYKEY_NO_SCHEMA_FOUND_ERR.name(), "No schemas found");
/*     */       
/*     */ 
/* 280 */       throw err;
/*     */     }
/* 282 */     for (CBSchemaMetadata currSchema : schemaList)
/*     */     {
/*     */       try
/*     */       {
/* 286 */         if (null != currSchema.getTableMeta(tableName))
/*     */         {
/* 288 */           tableList.add(currSchema.getTableMeta(tableName));
/*     */         }
/*     */       }
/*     */       catch (NullPointerException ex) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 296 */     if (tableList.isEmpty())
/*     */     {
/*     */ 
/* 299 */       ErrorException err = CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CATALOGFUN_PRIMARYKEY_TABLE_NOT_FOUND_ERR.name(), "No tables found");
/*     */       
/*     */ 
/* 302 */       throw err;
/*     */     }
/* 304 */     for (CBTableMetadata currTable : tableList)
/*     */     {
/* 306 */       columnList = currTable.getColumnMetadataList();
/* 307 */       for (CBColumnMetadata currColumn : columnList)
/*     */       {
/* 309 */         if (currColumn.isPKColumn())
/*     */         {
/* 311 */           this.m_primaryKeys.add(currColumn);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/CBPrimaryKeyMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */