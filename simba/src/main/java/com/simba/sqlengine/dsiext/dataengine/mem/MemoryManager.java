package com.simba.sqlengine.dsiext.dataengine.mem;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.utilities.Variant;
import com.simba.support.ILogger;
import com.simba.support.LogLevel;
import com.simba.support.LogUtilities;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryManager
{
  private static final int MEM_CONSTANT = 3;
  private static final long DEFAULT_BYTES = 104857600L;
  private static final String PKG_NAME = "com.simba.sqlengine.dsiext.dataengine.mem";
  private static final String CLS_NAME = "MemoryManager";
  private final long m_totalPoolBytes;
  private final AtomicLong m_freePool;
  private final AtomicInteger m_nextUsageId = new AtomicInteger(0);
  private final ConcurrentHashMap<Integer, MemoryUsage> m_currentUsage = new ConcurrentHashMap();
  private final ILogger m_logger;
  
  MemoryManager(long paramLong)
  {
    this(paramLong, null);
  }
  
  MemoryManager(long paramLong, ILogger paramILogger)
  {
    if (0L >= paramLong) {
      throw new IllegalArgumentException("MemoryManager allocated with " + paramLong + " to use. Please check your JVM configuration.");
    }
    this.m_totalPoolBytes = paramLong;
    this.m_freePool = new AtomicLong(paramLong);
    this.m_logger = paramILogger;
  }
  
  public boolean allocate(int paramInt, long paramLong)
  {
    ILogger localILogger = this.m_logger;
    if (shouldLog(LogLevel.TRACE, localILogger)) {
      LogUtilities.logFunctionEntrance(localILogger, new Object[] { Long.valueOf(paramLong) });
    }
    if (this.m_totalPoolBytes < paramLong)
    {
      if (shouldLog(LogLevel.DEBUG, localILogger)) {
        logAllocate(localILogger, paramInt, paramLong, 0L, this.m_freePool.get());
      }
      return false;
    }
    MemoryUsage localMemoryUsage = getUsage(Integer.valueOf(paramInt));
    synchronized (localMemoryUsage)
    {
      long l1 = localMemoryUsage.getReservePool();
      if (l1 >= paramLong)
      {
        localMemoryUsage.allocate(paramLong);
        if (shouldLog(LogLevel.DEBUG, localILogger))
        {
          logAllocate(localILogger, paramInt, paramLong, paramLong, this.m_freePool.get());
          logUsage(localILogger, paramInt, "allocate", localMemoryUsage);
        }
        return true;
      }
      long l2 = paramLong - l1;
      long l3 = this.m_freePool.get();
      if (l3 / 3L < l2)
      {
        if (shouldLog(LogLevel.DEBUG, localILogger))
        {
          logAllocate(localILogger, paramInt, paramLong, 0L, l3);
          logUsage(localILogger, paramInt, "allocate", localMemoryUsage);
        }
        return false;
      }
      if (this.m_freePool.compareAndSet(l3, l3 - l2))
      {
        localMemoryUsage.allocate(paramLong);
        if (shouldLog(LogLevel.DEBUG, localILogger))
        {
          logAllocate(localILogger, paramInt, paramLong, paramLong, l3 - l2);
          logUsage(localILogger, paramInt, "allocate", localMemoryUsage);
        }
        return true;
      }
    }
  }
  
  public long allocateMax(int paramInt)
  {
    ILogger localILogger = this.m_logger;
    if (null != localILogger) {
      LogUtilities.logFunctionEntrance(localILogger, new Object[] { Integer.valueOf(paramInt) });
    }
    long l2;
    long l1;
    for (;;)
    {
      l2 = this.m_freePool.get();
      l1 = l2 / 3L;
      if (0L != l1) {
        if (this.m_freePool.compareAndSet(l2, l2 - l1)) {
          break;
        }
      }
    }
    MemoryUsage localMemoryUsage = getUsage(Integer.valueOf(paramInt));
    long l3;
    synchronized (localMemoryUsage)
    {
      l3 = localMemoryUsage.getReservePool() + l1;
      localMemoryUsage.allocate(l3);
    }
    if (shouldLog(LogLevel.DEBUG, localILogger))
    {
      logAllocateMax(localILogger, paramInt, l3, l2 - l1);
      logUsage(localILogger, paramInt, "allocateMax", localMemoryUsage);
    }
    return l3;
  }
  
  public int createUsageId()
  {
    ILogger localILogger = this.m_logger;
    if (shouldLog(LogLevel.TRACE, localILogger)) {
      LogUtilities.logFunctionEntrance(localILogger, new Object[0]);
    }
    int i = this.m_nextUsageId.getAndIncrement();
    assert (-1 != i);
    return i;
  }
  
  public void deallocate(int paramInt, long paramLong)
  {
    ILogger localILogger = this.m_logger;
    if (null != localILogger) {
      LogUtilities.logFunctionEntrance(localILogger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong) });
    }
    Integer localInteger = Integer.valueOf(paramInt);
    MemoryUsage localMemoryUsage = (MemoryUsage)this.m_currentUsage.get(localInteger);
    if (null == localMemoryUsage) {
      throw new NoSuchElementException("No usage for usageId: " + paramInt);
    }
    long l1;
    synchronized (localMemoryUsage)
    {
      if (paramLong > localMemoryUsage.getAllocated()) {
        throw new IllegalArgumentException("Dealloc failed.");
      }
      l1 = localMemoryUsage.deallocate(paramLong);
    }
    if (0L < l1)
    {
      long l2 = this.m_freePool.addAndGet(l1);
      if (shouldLog(LogLevel.DEBUG, localILogger))
      {
        logDeallocate(localILogger, paramInt, paramLong, l1, l2);
        logUsage(localILogger, paramInt, "deallocate", localMemoryUsage);
      }
    }
    else if (shouldLog(LogLevel.DEBUG, localILogger))
    {
      logDeallocate(localILogger, paramInt, paramLong, l1, this.m_freePool.get());
      logUsage(localILogger, paramInt, "deallocate", localMemoryUsage);
    }
  }
  
  public void free(int paramInt)
  {
    ILogger localILogger = this.m_logger;
    if (shouldLog(LogLevel.TRACE, localILogger)) {
      LogUtilities.logFunctionEntrance(localILogger, new Object[] { Integer.valueOf(paramInt) });
    }
    MemoryUsage localMemoryUsage = (MemoryUsage)this.m_currentUsage.remove(Integer.valueOf(paramInt));
    if (null == localMemoryUsage) {
      return;
    }
    long l1;
    synchronized (localMemoryUsage)
    {
      l1 = localMemoryUsage.free();
    }
    long l2 = -1L;
    if (0L < l1) {
      l2 = this.m_freePool.addAndGet(l1);
    }
    if (shouldLog(LogLevel.DEBUG, localILogger))
    {
      if (0L > l2) {
        l2 = this.m_freePool.get();
      }
      logFree(localILogger, paramInt, l1, l2);
    }
  }
  
  public long getAvailableMemory()
  {
    return this.m_freePool.get();
  }
  
  /* Error */
  public long getAllocated(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 8	com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager:m_currentUsage	Ljava/util/concurrent/ConcurrentHashMap;
    //   4: iload_1
    //   5: invokestatic 31	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   8: invokevirtual 46	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   11: checkcast 47	com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager$MemoryUsage
    //   14: astore_2
    //   15: aconst_null
    //   16: aload_2
    //   17: if_acmpne +5 -> 22
    //   20: lconst_0
    //   21: lreturn
    //   22: aload_2
    //   23: dup
    //   24: astore_3
    //   25: monitorenter
    //   26: aload_2
    //   27: invokevirtual 52	com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager$MemoryUsage:getAllocated	()J
    //   30: aload_3
    //   31: monitorexit
    //   32: lreturn
    //   33: astore 4
    //   35: aload_3
    //   36: monitorexit
    //   37: aload 4
    //   39: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	this	MemoryManager
    //   0	40	1	paramInt	int
    //   14	13	2	localMemoryUsage	MemoryUsage
    //   24	12	3	Ljava/lang/Object;	Object
    //   33	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   26	32	33	finally
    //   33	37	33	finally
  }
  
  /* Error */
  public long getReserved(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 8	com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager:m_currentUsage	Ljava/util/concurrent/ConcurrentHashMap;
    //   4: iload_1
    //   5: invokestatic 31	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   8: invokevirtual 46	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   11: checkcast 47	com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager$MemoryUsage
    //   14: astore_2
    //   15: aconst_null
    //   16: aload_2
    //   17: if_acmpne +5 -> 22
    //   20: lconst_0
    //   21: lreturn
    //   22: aload_2
    //   23: dup
    //   24: astore_3
    //   25: monitorenter
    //   26: aload_2
    //   27: invokevirtual 63	com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager$MemoryUsage:getReserveMax	()J
    //   30: aload_3
    //   31: monitorexit
    //   32: lreturn
    //   33: astore 4
    //   35: aload_3
    //   36: monitorexit
    //   37: aload 4
    //   39: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	this	MemoryManager
    //   0	40	1	paramInt	int
    //   14	13	2	localMemoryUsage	MemoryUsage
    //   24	12	3	Ljava/lang/Object;	Object
    //   33	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   26	32	33	finally
    //   33	37	33	finally
  }
  
  public long getTotalMemory()
  {
    return this.m_totalPoolBytes;
  }
  
  public boolean reserve(int paramInt, long paramLong)
  {
    ILogger localILogger = this.m_logger;
    if (null != localILogger) {
      LogUtilities.logFunctionEntrance(localILogger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong) });
    }
    if (0L > paramLong) {
      throw new IllegalArgumentException("Negative amount: " + paramLong);
    }
    if (this.m_totalPoolBytes < paramLong)
    {
      if (shouldLog(LogLevel.DEBUG, localILogger)) {
        logReserve(localILogger, paramInt, paramLong, 0L, this.m_freePool.get());
      }
      return false;
    }
    if (0L == paramLong)
    {
      this.m_currentUsage.putIfAbsent(Integer.valueOf(paramInt), new MemoryUsage(0L));
      if (shouldLog(LogLevel.DEBUG, localILogger))
      {
        logReserve(localILogger, paramInt, paramLong, 0L, this.m_freePool.get());
        logUsage(localILogger, paramInt, "reserve", (MemoryUsage)this.m_currentUsage.get(Integer.valueOf(paramInt)));
      }
      return true;
    }
    for (;;)
    {
      long l = this.m_freePool.get();
      if (paramLong > l)
      {
        if (shouldLog(LogLevel.DEBUG, localILogger)) {
          logReserve(localILogger, paramInt, paramLong, 0L, l);
        }
        return false;
      }
      if (this.m_freePool.compareAndSet(l, l - paramLong))
      {
        if (!shouldLog(LogLevel.DEBUG, localILogger)) {
          break;
        }
        logReserve(localILogger, paramInt, paramLong, paramLong, l - paramLong);
        break;
      }
    }
    Integer localInteger = Integer.valueOf(paramInt);
    MemoryUsage localMemoryUsage1 = (MemoryUsage)this.m_currentUsage.get(localInteger);
    if (null == localMemoryUsage1)
    {
      MemoryUsage localMemoryUsage2 = new MemoryUsage(paramLong);
      localMemoryUsage1 = (MemoryUsage)this.m_currentUsage.putIfAbsent(localInteger, localMemoryUsage2);
      if (null == localMemoryUsage1)
      {
        if (shouldLog(LogLevel.DEBUG, localILogger)) {
          logUsage(localILogger, paramInt, "reserve", localMemoryUsage2);
        }
        return true;
      }
    }
    synchronized (localMemoryUsage1)
    {
      localMemoryUsage1.reserve(paramLong);
      if (shouldLog(LogLevel.DEBUG, localILogger)) {
        logUsage(localILogger, paramInt, "reserve", localMemoryUsage1);
      }
    }
    return true;
  }
  
  public static MemoryManager getInstance()
  {
    return Holder.INSTANCE;
  }
  
  private void logAllocate(ILogger paramILogger, int paramInt, long paramLong1, long paramLong2, long paramLong3)
  {
    String str = String.format("[%d] ALLOC   %d -> %d (free: %d)", new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
    paramILogger.logDebug("com.simba.sqlengine.dsiext.dataengine.mem", "MemoryManager", "allocate", str);
  }
  
  private void logAllocateMax(ILogger paramILogger, int paramInt, long paramLong1, long paramLong2)
  {
    String str = String.format("[%d] ALOCMAX MAX -> %d (free: %d)", new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2) });
    paramILogger.logDebug("com.simba.sqlengine.dsiext.dataengine.mem", "MemoryManager", "allocateMax", str);
  }
  
  private void logDeallocate(ILogger paramILogger, int paramInt, long paramLong1, long paramLong2, long paramLong3)
  {
    String str = String.format("[%d] DEALLOC %d (free +%d : %d)", new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
    paramILogger.logDebug("com.simba.sqlengine.dsiext.dataengine.mem", "MemoryManager", "deallocate", str);
  }
  
  private void logFree(ILogger paramILogger, int paramInt, long paramLong1, long paramLong2)
  {
    String str = String.format("[%d] FREE    %d (free: %d)", new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2) });
    paramILogger.logDebug("com.simba.sqlengine.dsiext.dataengine.mem", "MemoryManager", "free", str);
  }
  
  private void logReserve(ILogger paramILogger, int paramInt, long paramLong1, long paramLong2, long paramLong3)
  {
    String str = String.format("[%d] RESERVE %d -> %d (free: %d)", new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
    paramILogger.logDebug("com.simba.sqlengine.dsiext.dataengine.mem", "MemoryManager", "reserve", str);
  }
  
  private void logUsage(ILogger paramILogger, int paramInt, String paramString, MemoryUsage paramMemoryUsage)
  {
    String str = String.format("[%d]         R: %d (MAX: %d) A: %d", new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramMemoryUsage.getReservePool()), Long.valueOf(paramMemoryUsage.getReserveMax()), Long.valueOf(paramMemoryUsage.getAllocated()) });
    paramILogger.logDebug("com.simba.sqlengine.dsiext.dataengine.mem", "MemoryManager", paramString, str);
  }
  
  private MemoryUsage getUsage(Integer paramInteger)
  {
    Object localObject = (MemoryUsage)this.m_currentUsage.get(paramInteger);
    if (null == localObject)
    {
      MemoryUsage localMemoryUsage = new MemoryUsage(0L);
      localObject = (MemoryUsage)this.m_currentUsage.putIfAbsent(paramInteger, localMemoryUsage);
      if (null == localObject) {
        localObject = localMemoryUsage;
      }
    }
    return (MemoryUsage)localObject;
  }
  
  private static boolean shouldLog(LogLevel paramLogLevel, ILogger paramILogger)
  {
    return (null != paramILogger) && (LogUtilities.shouldLogLevel(paramLogLevel, paramILogger));
  }
  
  private static final class Holder
  {
    public static final MemoryManager INSTANCE;
    
    static
    {
      long l1 = 0L;
      IDriver localIDriver = DSIDriverSingleton.getInstance();
      ILogger localILogger = null;
      if (null != localIDriver)
      {
        localILogger = localIDriver.getDriverLog();
        try
        {
          Variant localVariant = localIDriver.getProperty(19);
          l1 = localVariant.getLong();
        }
        catch (Exception localException) {}
      }
      if (0L >= l1) {
        l1 = 104857600L;
      }
      long l2 = Runtime.getRuntime().maxMemory() / 2L;
      l1 = Math.min(l2, l1);
      INSTANCE = new MemoryManager(l1, localILogger);
    }
  }
  
  private static final class MemoryUsage
  {
    private boolean m_isClosed = false;
    private long m_reserveMax;
    private long m_reservePool;
    private long m_allocated = 0L;
    
    public MemoryUsage(long paramLong)
    {
      this.m_reservePool = paramLong;
      this.m_reserveMax = paramLong;
    }
    
    public void allocate(long paramLong)
    {
      if (this.m_isClosed) {
        throw new IllegalStateException();
      }
      this.m_allocated += paramLong;
      this.m_reservePool = Math.max(this.m_reservePool - paramLong, 0L);
    }
    
    public long deallocate(long paramLong)
    {
      assert (!this.m_isClosed);
      assert (this.m_allocated >= paramLong);
      this.m_allocated -= paramLong;
      long l = Math.min(this.m_reserveMax - this.m_reservePool, paramLong);
      this.m_reservePool += l;
      return paramLong - l;
    }
    
    public long free()
    {
      this.m_isClosed = true;
      long l = this.m_reservePool + this.m_allocated;
      this.m_reservePool = 0L;
      this.m_allocated = 0L;
      return l;
    }
    
    public long getAllocated()
    {
      return this.m_allocated;
    }
    
    public long getReservePool()
    {
      return this.m_reservePool;
    }
    
    public long getReserveMax()
    {
      return this.m_reserveMax;
    }
    
    public void reserve(long paramLong)
    {
      if (this.m_isClosed) {
        throw new IllegalStateException();
      }
      this.m_reservePool += paramLong;
      this.m_reserveMax += paramLong;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/mem/MemoryManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */