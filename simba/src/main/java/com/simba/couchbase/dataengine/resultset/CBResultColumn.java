/*     */ package com.simba.couchbase.dataengine.resultset;
/*     */ 
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.dsi.dataengine.utilities.Nullable;
/*     */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBResultColumn
/*     */   extends CBColumnMetadata
/*     */ {
/*     */   private boolean m_queryStringIsAlias;
/*     */   private String m_queryString;
/*     */   private String m_rowDocumentAlias;
/*     */   private String m_selectAlias;
/*     */   
/*     */   public CBResultColumn(CBColumnMetadata inputColumnMeta, String inputSourceName, String inputDSIIName)
/*     */     throws ErrorException
/*     */   {
/*  47 */     super(inputSourceName, inputDSIIName, createTypeMetadata(inputColumnMeta));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  52 */     copyColumnMetadata(inputColumnMeta, inputSourceName, inputDSIIName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void copyColumnMetadata(CBColumnMetadata inputColumnMeta, String inputSourceNmae, String inputDSIIName)
/*     */     throws ErrorException
/*     */   {
/*     */     try
/*     */     {
/*  65 */       TypeMetadata typeMeta = createTypeMetadata(inputColumnMeta);
/*  66 */       setTypeMetadata(typeMeta);
/*     */       
/*  68 */       setCatalogName(inputColumnMeta.getCatalogName());
/*  69 */       setSchemaName(inputColumnMeta.getSchemaName());
/*  70 */       setTableName(inputColumnMeta.getTableName());
/*  71 */       setName(inputColumnMeta.getName());
/*  72 */       setLabel(inputColumnMeta.getLabel());
/*  73 */       setColumnLength(inputColumnMeta.getColumnLength());
/*  74 */       setNullable(Nullable.NULLABLE);
/*  75 */       setPKColumn(inputColumnMeta.isPKColumn());
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  79 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_EXE_COPY_COLUMN_METADATA_ERROR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/*  82 */       err.initCause(ex);
/*  83 */       throw err;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeMetadata createTypeMetadata(CBColumnMetadata inputColumnMeta)
/*     */     throws ErrorException
/*     */   {
/*  94 */     TypeMetadata typeMeta = TypeMetadata.createTypeMetadata(inputColumnMeta.getTypeMetadata().getType());
/*     */     
/*  96 */     typeMeta.setTypeName(inputColumnMeta.getTypeMetadata().getTypeName());
/*  97 */     return typeMeta;
/*     */   }
/*     */   
/*     */   public boolean isQueryStringAnAlias()
/*     */   {
/* 102 */     return this.m_queryStringIsAlias;
/*     */   }
/*     */   
/*     */   public boolean isTableColumn()
/*     */   {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isProjectedTableColumn()
/*     */   {
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public String getQueryString()
/*     */   {
/* 117 */     return this.m_queryString;
/*     */   }
/*     */   
/*     */   public String getRowDocumentAlias()
/*     */   {
/* 122 */     return this.m_rowDocumentAlias;
/*     */   }
/*     */   
/*     */   public String getSelectAlias()
/*     */   {
/* 127 */     if (null != this.m_selectAlias)
/*     */     {
/* 129 */       return this.m_selectAlias.substring(1, this.m_selectAlias.length() - 1);
/*     */     }
/*     */     
/*     */ 
/* 133 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueryString(String inputQueryString, boolean isQueryStringAlias)
/*     */   {
/* 141 */     this.m_queryString = inputQueryString;
/* 142 */     this.m_queryStringIsAlias = isQueryStringAlias;
/*     */   }
/*     */   
/*     */   public void setSelectAlias(String selectAlias)
/*     */   {
/* 147 */     this.m_selectAlias = selectAlias;
/* 148 */     this.m_rowDocumentAlias = selectAlias.substring(1, selectAlias.length() - 1);
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/resultset/CBResultColumn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */