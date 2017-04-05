/*     */ package com.simba.couchbase.dataengine.metadata;
/*     */ 
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
/*     */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.TypeUtilities;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
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
/*     */ public class MetadataUtils
/*     */ {
/*     */   public static String GetRestriction(Map<MetadataSourceColumnTag, String> in_restrictions, MetadataSourceColumnTag in_restrictionTag)
/*     */   {
/*  42 */     String restriction = "";
/*  43 */     if (in_restrictions != null)
/*     */     {
/*  45 */       restriction = (String)in_restrictions.get(in_restrictionTag);
/*     */     }
/*  47 */     if (restriction != null)
/*     */     {
/*     */ 
/*  50 */       restriction = restriction.trim();
/*     */     }
/*  52 */     return restriction;
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
/*     */   public static int getBufferSize(TypeMetadata type, ILogger m_logger)
/*     */   {
/*  67 */     int resultType = 0;
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  73 */       if (12 == type.getType())
/*     */       {
/*     */ 
/*  76 */         return 2048;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  81 */       if (0 == type.getType())
/*     */       {
/*     */ 
/*  84 */         return 0;
/*     */       }
/*     */       
/*  87 */       return TypeUtilities.getSizeInBytes(type.getType());
/*     */ 
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  92 */       LogUtilities.logError(ex, m_logger); }
/*  93 */     return resultType;
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
/*     */   public static short getNumPrecRadix(TypeMetadata type)
/*     */   {
/* 106 */     if (TypeUtilities.isNumberType(type.getType()))
/*     */     {
/* 108 */       return 10;
/*     */     }
/* 110 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/MetadataUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */