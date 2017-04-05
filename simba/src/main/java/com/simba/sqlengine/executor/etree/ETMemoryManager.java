package com.simba.sqlengine.executor.etree;

import com.simba.sqlengine.dsiext.dataengine.mem.MemoryManager;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.util.ETWalker;
import com.simba.sqlengine.executor.etree.util.ETWalker.Action;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ETMemoryManager
  implements IETResource
{
  private ArrayList<IMemoryConsumer> m_consumers = null;
  private HashMap<IMemManagerAgent, IMemoryConsumer> m_agentMap = new HashMap();
  private HashSet<IMemoryConsumer> m_unregistered = new HashSet();
  private int m_allocationId = -1;
  private long m_available = 0L;
  private int m_nextAssignIndex = -1;
  
  public static ETMemoryManager createMemoryManager(IETNode paramIETNode)
  {
    ETMemoryManager localETMemoryManager = new ETMemoryManager();
    ETWalker.Action local1 = new ETWalker.Action()
    {
      public void act(IETNode paramAnonymousIETNode)
      {
        if ((paramAnonymousIETNode instanceof IMemoryConsumer))
        {
          IMemoryConsumer localIMemoryConsumer = (IMemoryConsumer)paramAnonymousIETNode;
          this.val$mm.addConsumer(localIMemoryConsumer);
        }
      }
      
      public Void getResult()
      {
        return null;
      }
    };
    ETWalker.walk(paramIETNode, local1);
    localETMemoryManager.m_nextAssignIndex = (localETMemoryManager.m_consumers.size() - 1);
    localETMemoryManager.m_allocationId = MemoryManager.getInstance().createUsageId();
    return localETMemoryManager;
  }
  
  private void addConsumer(IMemoryConsumer paramIMemoryConsumer)
  {
    MemManagerAgent localMemManagerAgent = new MemManagerAgent(null);
    paramIMemoryConsumer.registerManagerAgent(localMemManagerAgent);
    this.m_agentMap.put(localMemManagerAgent, paramIMemoryConsumer);
    this.m_consumers.add(paramIMemoryConsumer);
    this.m_unregistered.add(paramIMemoryConsumer);
  }
  
  public void allocate()
    throws ErrorException
  {
    if (this.m_unregistered.size() != this.m_consumers.size()) {
      throw new IllegalStateException("Memory consumer is not unregistered on re-allocation.");
    }
    if (this.m_consumers.size() == 0) {
      return;
    }
    long l1 = 0L;
    Iterator localIterator1 = this.m_consumers.iterator();
    while (localIterator1.hasNext())
    {
      IMemoryConsumer localIMemoryConsumer1 = (IMemoryConsumer)localIterator1.next();
      l1 += localIMemoryConsumer1.getRequiredMemory();
    }
    if (!MemoryManager.getInstance().reserve(this.m_allocationId, l1)) {
      throw SQLEngineExceptionFactory.failedToAllocateMemory("" + this.m_allocationId);
    }
    this.m_available = MemoryManager.getInstance().allocateMax(this.m_allocationId);
    long l2 = (this.m_available - l1) / this.m_consumers.size();
    Iterator localIterator2 = this.m_consumers.iterator();
    while (localIterator2.hasNext())
    {
      IMemoryConsumer localIMemoryConsumer2 = (IMemoryConsumer)localIterator2.next();
      long l3 = l2 + localIMemoryConsumer2.getRequiredMemory();
      long l4 = localIMemoryConsumer2.assign(l3);
      if (l4 > l3) {
        throw new IllegalArgumentException("Logic error from memory consumer implementation.");
      }
      this.m_available -= l4;
    }
    assignExtra(new IMemoryConsumer[0]);
    this.m_unregistered.clear();
  }
  
  public void free()
  {
    MemoryManager.getInstance().free(this.m_allocationId);
    this.m_allocationId = -1;
    this.m_available = 0L;
  }
  
  private void assignExtra(IMemoryConsumer... paramVarArgs)
  {
    if (this.m_available <= 0L) {
      return;
    }
    for (int i = 0; (i < this.m_consumers.size()) && (this.m_available > 0L); i++)
    {
      IMemoryConsumer localIMemoryConsumer1 = (IMemoryConsumer)this.m_consumers.get(this.m_nextAssignIndex);
      if (!this.m_unregistered.contains(localIMemoryConsumer1))
      {
        int j = 0;
        for (IMemoryConsumer localIMemoryConsumer2 : paramVarArgs) {
          if (localIMemoryConsumer2 == localIMemoryConsumer1)
          {
            j = 1;
            break;
          }
        }
        if (j == 0)
        {
          long l = localIMemoryConsumer1.assign(this.m_available);
          if (l > this.m_available) {
            throw new IllegalArgumentException("Logic error from memory consumer implementation.");
          }
          this.m_available -= l;
          this.m_nextAssignIndex -= 1;
          if (this.m_nextAssignIndex < 0) {
            this.m_nextAssignIndex = (this.m_consumers.size() - 1);
          }
        }
      }
    }
  }
  
  private class MemManagerAgent
    implements IMemManagerAgent
  {
    private MemManagerAgent() {}
    
    public void recycleMemory(long paramLong)
    {
      if (paramLong < 0L) {
        throw new IllegalArgumentException(" Invalid amount to recycle: " + paramLong);
      }
      if (paramLong != 0L)
      {
        ETMemoryManager.access$014(ETMemoryManager.this, paramLong);
        ETMemoryManager.this.assignExtra(new IMemoryConsumer[] { (IMemoryConsumer)ETMemoryManager.this.m_agentMap.get(this) });
      }
    }
    
    public void unregisterConsumer()
    {
      ETMemoryManager.this.m_unregistered.add(ETMemoryManager.this.m_agentMap.get(this));
    }
    
    public long require(long paramLong1, long paramLong2)
    {
      if (ETMemoryManager.this.m_unregistered.contains(ETMemoryManager.this.m_agentMap.get(this))) {
        throw new IllegalArgumentException("Memory required from unregistered consumer.");
      }
      if ((paramLong1 < 0L) || (paramLong1 > paramLong2)) {
        throw new IllegalArgumentException("Invalid request: (" + paramLong1 + ", " + paramLong2 + ").");
      }
      if (ETMemoryManager.this.m_available >= paramLong1)
      {
        if (ETMemoryManager.this.m_available > paramLong2)
        {
          ETMemoryManager.access$022(ETMemoryManager.this, paramLong2);
          return paramLong2;
        }
        l = ETMemoryManager.this.m_available;
        ETMemoryManager.this.m_available = 0L;
        return l;
      }
      long l = 0L;
      paramLong1 -= ETMemoryManager.this.m_available;
      paramLong2 -= ETMemoryManager.this.m_available;
      while (paramLong2 >= paramLong1)
      {
        if (MemoryManager.getInstance().allocate(ETMemoryManager.this.m_allocationId, paramLong2))
        {
          l = paramLong2;
          break;
        }
        if (paramLong2 == paramLong1) {
          break;
        }
        paramLong2 = (paramLong2 - paramLong1) / 2L + paramLong1;
        if (paramLong2 < paramLong1 + 256L) {
          paramLong2 = paramLong1;
        }
      }
      if (l == 0L) {
        return -1L;
      }
      l += ETMemoryManager.this.m_available;
      ETMemoryManager.this.m_available = 0L;
      return l;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETMemoryManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */