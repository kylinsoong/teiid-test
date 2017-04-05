/*     */ package com.simba.jdbc.common;
/*     */ 
/*     */ import com.simba.dsi.core.impl.DSIDriver;
/*     */ import com.simba.dsi.dataengine.interfaces.IResultSet;
/*     */ import com.simba.dsi.dataengine.utilities.CursorType;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.utilities.DSIMessageKey;
/*     */ import com.simba.support.IWarningListener;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import com.simba.support.exceptions.ExceptionBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CommonResultSet
/*     */   implements IResultSet
/*     */ {
/*     */   private int m_fetchSize;
/*  39 */   private boolean m_hasStartedFetch = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   private int m_currentRow = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private IWarningListener m_warningListener = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendRow()
/*     */     throws ErrorException
/*     */   {
/*  76 */     throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
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
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
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
/* 107 */     this.m_hasStartedFetch = false;
/* 108 */     this.m_currentRow = 0;
/*     */     
/*     */ 
/* 111 */     doCloseCursor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deleteRow()
/*     */     throws ErrorException
/*     */   {
/* 124 */     throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFetchSize()
/*     */     throws ErrorException
/*     */   {
/* 136 */     return this.m_fetchSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IWarningListener getWarningListener()
/*     */   {
/* 146 */     return this.m_warningListener;
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
/*     */   public boolean moveToNextRow()
/*     */     throws ErrorException
/*     */   {
/* 163 */     if (!this.m_hasStartedFetch)
/*     */     {
/*     */ 
/* 166 */       this.m_hasStartedFetch = true;
/* 167 */       this.m_currentRow = 0;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 172 */       this.m_currentRow += 1;
/*     */     }
/*     */     
/* 175 */     return doMoveToNextRow();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onFinishRowUpdate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onStartRowUpdate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerWarningListener(IWarningListener listener)
/*     */   {
/* 204 */     this.m_warningListener = listener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean rowDeleted()
/*     */   {
/* 214 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean rowInserted()
/*     */   {
/* 224 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean rowUpdated()
/*     */   {
/* 234 */     return false;
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
/*     */   public void setCursorType(CursorType cursorType)
/*     */     throws ErrorException
/*     */   {
/* 251 */     if (CursorType.FORWARD_ONLY != cursorType)
/*     */     {
/* 253 */       throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(cursorType));
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
/*     */   public void setFetchSize(int fetchSize)
/*     */     throws ErrorException
/*     */   {
/* 272 */     this.m_fetchSize = fetchSize;
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
/*     */   public boolean writeData(int column, DataWrapper data, long offset, boolean isDefault)
/*     */     throws ErrorException
/*     */   {
/* 294 */     throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name());
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
/*     */   protected abstract void doCloseCursor()
/*     */     throws ErrorException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean doMoveToNextRow()
/*     */     throws ErrorException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getCurrentRow()
/*     */   {
/* 328 */     return this.m_currentRow;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/CommonResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */