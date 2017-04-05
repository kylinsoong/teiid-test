/*     */ package com.simba.jdbc.utils;
/*     */ 
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.concurrent.Semaphore;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDoubleBufferManager
/*     */   implements Runnable
/*     */ {
/*     */   protected ILogger m_logger;
/*     */   protected BufferHolder[] m_cachedBuffers;
/*     */   private ErrorException m_exception;
/*     */   private int m_currentBufferIndex;
/*     */   private int m_bufferCacheIndex;
/*     */   private int m_bufferFetchIndex;
/*  99 */   private boolean m_isRunning = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Semaphore m_cacheBufferSemaphore;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Semaphore m_fetchBufferSemaphore;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int m_bufferSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class BufferHolder<BufferT>
/*     */   {
/* 128 */     public BufferT m_buffer = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 133 */     public boolean m_serverHasMoreRows = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int m_currentRow;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int m_numRows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractDoubleBufferManager(ILogger logger, int bufferSize)
/*     */   {
/* 151 */     this.m_logger = logger;
/* 152 */     this.m_cacheBufferSemaphore = new Semaphore(0);
/* 153 */     this.m_fetchBufferSemaphore = new Semaphore(1);
/* 154 */     this.m_bufferCacheIndex = 0;
/* 155 */     this.m_bufferFetchIndex = 0;
/* 156 */     this.m_exception = null;
/* 157 */     this.m_bufferSize = bufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void run()
/*     */   {
/* 168 */     this.m_cachedBuffers = new BufferHolder[2];
/* 169 */     this.m_cachedBuffers[0] = makeBuffer();
/* 170 */     this.m_cachedBuffers[1] = makeBuffer();
/*     */     
/* 172 */     while (this.m_isRunning)
/*     */     {
/*     */       try
/*     */       {
/* 176 */         this.m_fetchBufferSemaphore.acquire();
/*     */       }
/*     */       catch (InterruptedException ex) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */       if (!this.m_isRunning) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 192 */         fetchData(this.m_cachedBuffers[this.m_bufferCacheIndex]);
/*     */         
/* 194 */         if (!this.m_cachedBuffers[this.m_bufferCacheIndex].m_serverHasMoreRows)
/*     */         {
/*     */ 
/* 197 */           synchronized (this)
/*     */           {
/* 199 */             this.m_isRunning = false;
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 205 */           this.m_bufferCacheIndex = (1 - this.m_bufferCacheIndex);
/*     */         }
/*     */       }
/*     */       catch (ErrorException ex)
/*     */       {
/* 210 */         synchronized (this)
/*     */         {
/* 212 */           this.m_exception = ex;
/* 213 */           this.m_isRunning = false;
/*     */         }
/*     */         
/*     */       }
/*     */       finally
/*     */       {
/* 219 */         this.m_cacheBufferSemaphore.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean getNextBuffer()
/*     */     throws ErrorException
/*     */   {
/* 231 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/*     */     
/*     */ 
/* 234 */     if (null != this.m_exception)
/*     */     {
/* 236 */       throw this.m_exception;
/*     */     }
/*     */     
/*     */     boolean isRunning;
/*     */     
/* 241 */     synchronized (this)
/*     */     {
/* 243 */       isRunning = this.m_isRunning;
/*     */     }
/*     */     
/* 246 */     if (isRunning)
/*     */     {
/*     */       try
/*     */       {
/* 250 */         this.m_cacheBufferSemaphore.acquire();
/*     */       }
/*     */       catch (InterruptedException ex) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 259 */     if (null != this.m_exception)
/*     */     {
/* 261 */       throw this.m_exception;
/*     */     }
/*     */     
/* 264 */     this.m_currentBufferIndex = this.m_bufferFetchIndex;
/* 265 */     this.m_cachedBuffers[this.m_currentBufferIndex].m_currentRow = 0;
/* 266 */     if (!this.m_cachedBuffers[this.m_currentBufferIndex].m_serverHasMoreRows)
/*     */     {
/* 268 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 272 */     this.m_bufferFetchIndex = (1 - this.m_bufferFetchIndex);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 277 */     if (this.m_bufferFetchIndex == this.m_bufferCacheIndex)
/*     */     {
/* 279 */       this.m_fetchBufferSemaphore.release();
/*     */     }
/*     */     
/* 282 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void stop()
/*     */   {
/* 290 */     LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
/* 291 */     this.m_isRunning = false;
/* 292 */     this.m_fetchBufferSemaphore.release();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BufferHolder getFetchBuffer()
/*     */   {
/* 302 */     return this.m_cachedBuffers[this.m_currentBufferIndex];
/*     */   }
/*     */   
/*     */   protected abstract void fetchData(BufferHolder paramBufferHolder)
/*     */     throws ErrorException;
/*     */   
/*     */   protected abstract BufferHolder makeBuffer();
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/utils/AbstractDoubleBufferManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */