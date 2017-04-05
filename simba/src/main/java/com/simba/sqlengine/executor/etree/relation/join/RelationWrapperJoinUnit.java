package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.ITemporaryTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTableBuilder.TemporaryTableProperties;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;

class RelationWrapperJoinUnit
  implements IMasterJoinUnit
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree.relation.join";
  private static final String CLASS_NAME = "RelationWrapperJoinUnit";
  private static final int ROW_TRACKER_BUFFER_SIZE = 4096;
  private ETRelationalExpr m_relation;
  private long m_currentRowNum;
  private FileRowTracker m_tracker = null;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_externProperty;
  private boolean m_trackOuterRows;
  private ITemporaryTable m_cache;
  private long m_memoryAssigned;
  private long m_requiredMemory;
  
  public RelationWrapperJoinUnit(ETRelationalExpr paramETRelationalExpr, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, boolean paramBoolean)
    throws ErrorException
  {
    if (null != paramExternalAlgorithmProperties.getLogger()) {
      LogUtilities.logFunctionEntrance(paramExternalAlgorithmProperties.getLogger(), new Object[0]);
    }
    this.m_relation = paramETRelationalExpr;
    this.m_currentRowNum = -1L;
    this.m_externProperty = paramExternalAlgorithmProperties;
    this.m_trackOuterRows = paramBoolean;
    this.m_memoryAssigned = 0L;
    if (this.m_trackOuterRows)
    {
      this.m_cache = createCache();
      this.m_requiredMemory = (this.m_cache.getRequiredMemory() + 4096L);
    }
    else
    {
      this.m_requiredMemory = 0L;
    }
  }
  
  public IRowView getRow()
  {
    return null;
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_trackOuterRows ? this.m_cache.retrieveData(paramInt, paramETDataRequest) : this.m_relation.retrieveData(paramInt, paramETDataRequest);
  }
  
  public void reset()
    throws ErrorException
  {
    resetRelation();
    if (this.m_trackOuterRows)
    {
      assert (this.m_tracker != null);
      this.m_tracker.reset();
    }
  }
  
  public void resetRelation()
    throws ErrorException
  {
    if (this.m_trackOuterRows)
    {
      assert (this.m_cache != null);
      this.m_cache.reset();
    }
    else
    {
      assert (this.m_relation.isOpen());
      this.m_relation.reset();
    }
    this.m_currentRowNum = -1L;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    boolean bool;
    if (this.m_trackOuterRows)
    {
      assert (this.m_cache != null);
      bool = this.m_cache.moveToNextRow();
    }
    else
    {
      assert (this.m_relation.isOpen());
      bool = this.m_relation.move();
    }
    if (bool) {
      this.m_currentRowNum += 1L;
    }
    return bool;
  }
  
  public void match()
    throws ErrorException
  {
    if (this.m_tracker != null) {
      this.m_tracker.set(this.m_currentRowNum);
    }
  }
  
  public boolean isCurRowUnmatched()
    throws ErrorException
  {
    if (!this.m_trackOuterRows) {
      throw new IllegalStateException("Outer rows is not tracked.");
    }
    return !this.m_tracker.isSet(this.m_currentRowNum);
  }
  
  public void close()
  {
    if (null != this.m_externProperty.getLogger()) {
      this.m_externProperty.getLogger().logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "RelationWrapperJoinUnit", "close");
    }
  }
  
  public void closeRelation()
  {
    if (null != this.m_externProperty.getLogger()) {
      this.m_externProperty.getLogger().logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "RelationWrapperJoinUnit", "closeRelation");
    }
    if (this.m_relation.isOpen()) {
      this.m_relation.close();
    }
    if (this.m_tracker != null)
    {
      this.m_tracker.close();
      this.m_tracker = null;
    }
    if (this.m_cache != null)
    {
      this.m_cache.close();
      this.m_cache = null;
    }
  }
  
  public long getRequiredMemory()
  {
    return this.m_requiredMemory;
  }
  
  public void openRelation()
    throws ErrorException
  {
    if (null != this.m_externProperty.getLogger()) {
      this.m_externProperty.getLogger().logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "RelationWrapperJoinUnit", "openRelation");
    }
    this.m_relation.open(CursorType.FORWARD_ONLY);
    if (this.m_trackOuterRows)
    {
      if (this.m_tracker != null)
      {
        this.m_tracker.close();
        this.m_tracker = null;
      }
      this.m_tracker = new FileRowTracker(this.m_externProperty.getStorageDir(), 4096L, this.m_externProperty.getLogger());
      if (this.m_cache == null) {
        this.m_cache = createCache();
      }
      this.m_cache.assign(this.m_memoryAssigned - 4096L);
      this.m_cache.open();
      this.m_cache.writeFromRelation(this.m_relation);
      this.m_cache.reset();
      this.m_relation.close();
    }
  }
  
  public long assignMemory(long paramLong)
  {
    if (null != this.m_externProperty.getLogger()) {
      this.m_externProperty.getLogger().logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "RelationWrapperJoinUnit", "assignMemory");
    }
    if (this.m_memoryAssigned < this.m_requiredMemory)
    {
      long l = this.m_requiredMemory - this.m_memoryAssigned;
      if (l <= paramLong)
      {
        this.m_memoryAssigned = this.m_requiredMemory;
        assert (this.m_cache != null);
        this.m_cache.assign(this.m_memoryAssigned);
        if (null != this.m_externProperty.getLogger()) {
          this.m_externProperty.getLogger().logTrace("com.simba.sqlengine.executor.etree.relation.join", "RelationWrapperJoinUnit", "assignMemory", "Assigned : " + l);
        }
        return l;
      }
      this.m_memoryAssigned += paramLong;
      if (null != this.m_externProperty.getLogger()) {
        this.m_externProperty.getLogger().logTrace("com.simba.sqlengine.executor.etree.relation.join", "RelationWrapperJoinUnit", "assignMemory", "Assigned : " + paramLong);
      }
      return paramLong;
    }
    return 0L;
  }
  
  private ITemporaryTable createCache()
    throws ErrorException
  {
    int i = this.m_relation.getColumnCount();
    boolean[] arrayOfBoolean = new boolean[i];
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++)
    {
      localArrayList.add(this.m_relation.getColumn(j));
      arrayOfBoolean[j] = this.m_relation.getDataNeeded(j);
    }
    TemporaryTableBuilder.TemporaryTableProperties localTemporaryTableProperties = new TemporaryTableBuilder.TemporaryTableProperties(this.m_externProperty.getStorageDir(), this.m_externProperty.getCellMemoryLimit(), this.m_externProperty.getBlockSize(), ExternalAlgorithmUtil.calculateRowSize(localArrayList, arrayOfBoolean, this.m_externProperty.getCellMemoryLimit()), this.m_externProperty.getMaxNumOpenFiles(), this.m_externProperty.getLogger(), "Join");
    TemporaryTable localTemporaryTable = new TemporaryTable(localArrayList, localTemporaryTableProperties, arrayOfBoolean);
    localTemporaryTable.registerManagerAgent(new IMemManagerAgent()
    {
      public void unregisterConsumer() {}
      
      public void recycleMemory(long paramAnonymousLong) {}
      
      public long require(long paramAnonymousLong1, long paramAnonymousLong2)
      {
        return -1L;
      }
    });
    return localTemporaryTable;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/RelationWrapperJoinUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */