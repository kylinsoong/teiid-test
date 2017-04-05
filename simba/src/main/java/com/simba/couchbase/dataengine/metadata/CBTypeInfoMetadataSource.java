/*     */ package com.simba.couchbase.dataengine.metadata;
/*     */ 
/*     */ import com.simba.couchbase.core.CBJDBCDriver;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.dsi.dataengine.interfaces.IMetadataSource;
/*     */ import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
/*     */ import com.simba.dsi.dataengine.utilities.Nullable;
/*     */ import com.simba.dsi.dataengine.utilities.Searchable;
/*     */ import com.simba.dsi.dataengine.utilities.TypeUtilities;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBTypeInfoMetadataSource
/*     */   implements IMetadataSource
/*     */ {
/*     */   private static class TypeInfo
/*     */   {
/*     */     public String m_typeName;
/*     */     public int m_sqlType;
/*     */     public int m_columnSize;
/*     */     public String m_literalPrefix;
/*     */     public String m_literalSuffix;
/*     */     public String m_createParams;
/*     */     public Nullable m_nullable;
/*     */     public boolean m_caseSensitive;
/*     */     public Searchable m_searchable;
/*     */     public boolean m_unsignedAttr;
/*     */     public short m_fixedPrecScale;
/*     */     public short m_autoUnique;
/*     */     public short m_minScale;
/*     */     public short m_maxScale;
/*     */     
/*     */     public TypeInfo(int type, String typeName, int columnSize)
/*     */     {
/*  63 */       this.m_sqlType = type;
/*  64 */       this.m_typeName = typeName;
/*  65 */       this.m_columnSize = columnSize;
/*  66 */       this.m_literalPrefix = null;
/*  67 */       this.m_literalSuffix = null;
/*  68 */       this.m_createParams = null;
/*  69 */       this.m_nullable = Nullable.NULLABLE;
/*  70 */       this.m_caseSensitive = false;
/*  71 */       this.m_searchable = Searchable.SEARCHABLE;
/*  72 */       this.m_unsignedAttr = true;
/*  73 */       this.m_fixedPrecScale = 0;
/*  74 */       this.m_autoUnique = -1;
/*  75 */       this.m_minScale = 0;
/*  76 */       this.m_maxScale = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private boolean m_isFetching = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private int m_currentIndex = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  95 */   ArrayList<TypeInfo> m_dataTypes = new ArrayList();
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
/*     */   public CBTypeInfoMetadataSource(ILogger logger)
/*     */   {
/* 114 */     LogUtilities.logFunctionEntrance(logger, new Object[] { logger });
/*     */     
/* 116 */     this.m_logger = logger;
/* 117 */     initializeDataTypes();
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
/* 129 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */     try
/*     */     {
/* 133 */       closeCursor();
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
/*     */   public void closeCursor()
/*     */     throws ErrorException
/*     */   {
/* 150 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 152 */     this.m_isFetching = false;
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
/*     */   public boolean getMetadata(MetadataSourceColumnTag metadataSourceColumnTag, long offset, long maxSize, DataWrapper data)
/*     */     throws ErrorException
/*     */   {
/* 171 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { metadataSourceColumnTag, Long.valueOf(offset), Long.valueOf(maxSize) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */     switch (metadataSourceColumnTag)
/*     */     {
/*     */ 
/*     */     case DATA_TYPE_NAME: 
/* 181 */       return DSITypeUtilities.outputVarCharStringData(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_typeName, data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case DATA_TYPE: 
/* 190 */       data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_sqlType);
/*     */       
/* 192 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case COLUMN_SIZE: 
/* 197 */       data.setInteger(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_columnSize);
/*     */       
/* 199 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case LITERAL_PREFIX: 
/* 204 */       return DSITypeUtilities.outputVarCharStringData(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_literalPrefix, data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case LITERAL_SUFFIX: 
/* 213 */       return DSITypeUtilities.outputVarCharStringData(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_literalSuffix, data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case CREATE_PARAM: 
/* 223 */       return DSITypeUtilities.outputVarCharStringData(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_createParams, data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case NULLABLE: 
/* 232 */       data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_nullable.ordinal());
/* 233 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case CASE_SENSITIVE: 
/* 238 */       if (((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_caseSensitive)
/*     */       {
/* 240 */         data.setSmallInt(1);
/*     */       }
/*     */       else
/*     */       {
/* 244 */         data.setSmallInt(0);
/*     */       }
/*     */       
/* 247 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case SEARCHABLE: 
/* 252 */       data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_searchable.ordinal());
/*     */       
/* 254 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case UNSIGNED_ATTRIBUTE: 
/* 259 */       TypeInfo info = (TypeInfo)this.m_dataTypes.get(this.m_currentIndex);
/*     */       
/* 261 */       if ((!TypeUtilities.isApproximateNumericType(info.m_sqlType)) && (!TypeUtilities.isExactNumericType(info.m_sqlType)) && (!TypeUtilities.isIntegerType(info.m_sqlType)))
/*     */       {
/*     */ 
/*     */ 
/* 265 */         data.setNull(5);
/*     */       }
/* 267 */       else if (((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_unsignedAttr)
/*     */       {
/* 269 */         data.setSmallInt(1);
/*     */       }
/*     */       else
/*     */       {
/* 273 */         data.setSmallInt(0);
/*     */       }
/*     */       
/* 276 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case FIXED_PREC_SCALE: 
/* 281 */       data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_fixedPrecScale);
/*     */       
/* 283 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case AUTO_UNIQUE: 
/* 288 */       TypeInfo info = (TypeInfo)this.m_dataTypes.get(this.m_currentIndex);
/*     */       
/* 290 */       if ((!TypeUtilities.isApproximateNumericType(info.m_sqlType)) && (!TypeUtilities.isExactNumericType(info.m_sqlType)) && (!TypeUtilities.isIntegerType(info.m_sqlType)))
/*     */       {
/*     */ 
/*     */ 
/* 294 */         data.setNull(5);
/*     */       }
/* 296 */       else if (-1 == ((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_autoUnique)
/*     */       {
/* 298 */         data.setNull(5);
/*     */       }
/*     */       else
/*     */       {
/* 302 */         data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_autoUnique);
/*     */       }
/*     */       
/* 305 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case LOCAL_TYPE_NAME: 
/* 310 */       return DSITypeUtilities.outputVarCharStringData(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_typeName, data, offset, maxSize);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case MINIMUM_SCALE: 
/* 319 */       TypeInfo info = (TypeInfo)this.m_dataTypes.get(this.m_currentIndex);
/*     */       
/* 321 */       if ((TypeUtilities.isExactNumericType(info.m_sqlType)) || (TypeUtilities.isIntegerType(info.m_sqlType)) || (-7 == info.m_sqlType) || (92 == info.m_sqlType) || (93 == info.m_sqlType))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 329 */         data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_minScale);
/*     */       }
/*     */       else
/*     */       {
/* 333 */         data.setNull(5);
/*     */       }
/*     */       
/* 336 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case MAXIMUM_SCALE: 
/* 341 */       TypeInfo info = (TypeInfo)this.m_dataTypes.get(this.m_currentIndex);
/*     */       
/* 343 */       if ((TypeUtilities.isExactNumericType(info.m_sqlType)) || (TypeUtilities.isIntegerType(info.m_sqlType)) || (-7 == info.m_sqlType) || (92 == info.m_sqlType) || (93 == info.m_sqlType))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */         data.setSmallInt(((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_maxScale);
/*     */       }
/*     */       else
/*     */       {
/* 355 */         data.setNull(5);
/*     */       }
/*     */       
/* 358 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case SQL_DATA_TYPE: 
/* 363 */       int sqlType = ((TypeInfo)this.m_dataTypes.get(this.m_currentIndex)).m_sqlType;
/* 364 */       data.setSmallInt(TypeUtilities.getVerboseTypeFromConciseType(sqlType));
/* 365 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case SQL_DATETIME_SUB: 
/* 370 */       TypeInfo info = (TypeInfo)this.m_dataTypes.get(this.m_currentIndex);
/* 371 */       short datetimeSub = TypeUtilities.getIntervalCodeFromConciseType(info.m_sqlType);
/*     */       
/* 373 */       if (0 == datetimeSub)
/*     */       {
/* 375 */         data.setNull(5);
/*     */       }
/*     */       else
/*     */       {
/* 379 */         data.setSmallInt(datetimeSub);
/*     */       }
/*     */       
/* 382 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case NUM_PREC_RADIX: 
/* 387 */       TypeInfo info = (TypeInfo)this.m_dataTypes.get(this.m_currentIndex);
/*     */       
/* 389 */       if (TypeUtilities.isApproximateNumericType(info.m_sqlType))
/*     */       {
/* 391 */         data.setInteger(2L);
/*     */       }
/* 393 */       else if ((TypeUtilities.isExactNumericType(info.m_sqlType)) || (TypeUtilities.isIntegerType(info.m_sqlType)))
/*     */       {
/*     */ 
/* 396 */         data.setInteger(10L);
/*     */       }
/*     */       else
/*     */       {
/* 400 */         data.setNull(4);
/*     */       }
/*     */       
/* 403 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     case INTERVAL_PRECISION: 
/* 409 */       data.setNull(5);
/* 410 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */     case USER_DATA_TYPE: 
/* 415 */       data.setSmallInt(0);
/*     */       
/* 417 */       return false;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 422 */     throw CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.METADATA_COLUMN_NOT_FOUND.name(), new String[] { metadataSourceColumnTag.toString() });
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
/* 439 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */ 
/* 442 */     return this.m_currentIndex + 1 < this.m_dataTypes.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean moveToNextRow()
/*     */   {
/* 452 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/* 454 */     if (this.m_isFetching)
/*     */     {
/* 456 */       this.m_currentIndex += 1;
/*     */     }
/*     */     else
/*     */     {
/* 460 */       this.m_isFetching = true;
/* 461 */       this.m_currentIndex = 0;
/*     */     }
/*     */     
/* 464 */     return this.m_currentIndex < this.m_dataTypes.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeDataTypes()
/*     */   {
/* 476 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */ 
/* 479 */     TypeInfo typeInfo = new TypeInfo(12, "ARRAY", 65500);
/* 480 */     typeInfo.m_literalPrefix = "'";
/* 481 */     typeInfo.m_literalSuffix = "'";
/* 482 */     typeInfo.m_createParams = "max length";
/* 483 */     typeInfo.m_caseSensitive = true;
/* 484 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 487 */     typeInfo = new TypeInfo(4, "NUMERIC", 10);
/* 488 */     typeInfo.m_searchable = Searchable.PREDICATE_BASIC;
/* 489 */     typeInfo.m_unsignedAttr = false;
/* 490 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 493 */     typeInfo = new TypeInfo(-5, "NUMERIC", 19);
/* 494 */     typeInfo.m_searchable = Searchable.PREDICATE_BASIC;
/* 495 */     typeInfo.m_unsignedAttr = false;
/* 496 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 499 */     typeInfo = new TypeInfo(8, "NUMERIC", 15);
/* 500 */     typeInfo.m_searchable = Searchable.PREDICATE_BASIC;
/* 501 */     typeInfo.m_unsignedAttr = false;
/* 502 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 505 */     typeInfo = new TypeInfo(2, "NUMERIC", 38);
/* 506 */     typeInfo.m_createParams = "precision,scale";
/* 507 */     typeInfo.m_searchable = Searchable.PREDICATE_BASIC;
/* 508 */     typeInfo.m_unsignedAttr = false;
/* 509 */     typeInfo.m_minScale = 0;
/* 510 */     typeInfo.m_maxScale = 38;
/* 511 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 514 */     typeInfo = new TypeInfo(12, "STRING", 510);
/* 515 */     typeInfo.m_literalPrefix = "'";
/* 516 */     typeInfo.m_literalSuffix = "'";
/* 517 */     typeInfo.m_createParams = "max length";
/* 518 */     typeInfo.m_caseSensitive = true;
/* 519 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 522 */     typeInfo = new TypeInfo(16, "BOOLEAN", 1);
/* 523 */     typeInfo.m_searchable = Searchable.PREDICATE_BASIC;
/* 524 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 527 */     typeInfo = new TypeInfo(12, "OBJECT", 65500);
/* 528 */     typeInfo.m_literalPrefix = "'";
/* 529 */     typeInfo.m_literalSuffix = "'";
/* 530 */     typeInfo.m_createParams = "max length";
/* 531 */     typeInfo.m_caseSensitive = true;
/* 532 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 535 */     typeInfo = new TypeInfo(0, "NULL", 0);
/* 536 */     typeInfo.m_literalPrefix = "'";
/* 537 */     typeInfo.m_literalSuffix = "'";
/* 538 */     typeInfo.m_createParams = "LENGTH";
/* 539 */     typeInfo.m_caseSensitive = true;
/* 540 */     this.m_dataTypes.add(typeInfo);
/*     */     
/*     */ 
/* 543 */     typeInfo = new TypeInfo(12, "MISSING", 255);
/* 544 */     typeInfo.m_literalPrefix = "'";
/* 545 */     typeInfo.m_literalSuffix = "'";
/* 546 */     typeInfo.m_createParams = "max length";
/* 547 */     typeInfo.m_caseSensitive = true;
/* 548 */     this.m_dataTypes.add(typeInfo);
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/metadata/CBTypeInfoMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */