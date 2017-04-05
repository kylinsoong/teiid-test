package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.LongDataStore;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.exceptions.ErrorException;
import java.util.BitSet;

class InMemJoinUnit
  implements ISlaveJoinUnit
{
  private static final int LONG_DATA_BUFFER_SIZE = 4096;
  private InMemTable m_table;
  private int m_currentRow;
  private BitSet m_rowTracker;
  private LongDataStore m_longDataStore;
  private boolean[] m_dataNeeded;
  private long m_memoryBalance;
  private boolean m_hasLongData;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_property;
  
  public InMemJoinUnit(IColumn[] paramArrayOfIColumn, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    this.m_property = paramExternalAlgorithmProperties;
    this.m_table = new InMemTable(paramArrayOfIColumn, paramExternalAlgorithmProperties.getCellMemoryLimit(), 100, paramArrayOfBoolean, null);
    this.m_table.setMemLimit(0L);
    this.m_hasLongData = false;
    for (int i = 0; i < paramArrayOfIColumn.length; i++) {
      if (this.m_table.isLongDataColumn(i))
      {
        this.m_hasLongData = true;
        break;
      }
    }
    this.m_memoryBalance = (-this.m_table.getMemOverhead());
    this.m_memoryBalance += (this.m_hasLongData ? -paramExternalAlgorithmProperties.getBlockSize() : 0L);
    if (this.m_hasLongData) {
      this.m_longDataStore = createLongDataStore();
    }
    this.m_rowTracker = new BitSet();
    this.m_currentRow = -1;
    this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_table.clear();
    this.m_rowTracker.clear();
    this.m_currentRow = -1;
    if (this.m_longDataStore != null)
    {
      this.m_longDataStore.destroy();
      this.m_longDataStore = null;
    }
    if (this.m_hasLongData)
    {
      assert (this.m_memoryBalance >= 0L);
      this.m_longDataStore = createLongDataStore();
    }
  }
  
  private LongDataStore createLongDataStore()
    throws ErrorException
  {
    return new LongDataStore(this.m_property.getStorageDir(), 4096L, null);
  }
  
  public void appendRow(ETRelationalExpr paramETRelationalExpr)
    throws ErrorException
  {
    int i = this.m_table.appendRow();
    if (i < 0) {
      throw new IllegalStateException("Can not append more rows.");
    }
    for (int j = 0; j < paramETRelationalExpr.getColumnCount(); j++) {
      if (this.m_dataNeeded[j] != 0) {
        if (this.m_table.isLongDataColumn(j))
        {
          TemporaryFile.FileMarker localFileMarker = this.m_longDataStore.put(j, paramETRelationalExpr);
          if (localFileMarker == null) {
            this.m_table.setNull(i, j);
          } else {
            this.m_table.setFileMarker(i, j, localFileMarker);
          }
        }
        else
        {
          this.m_table.writeData(i, j, paramETRelationalExpr);
        }
      }
    }
  }
  
  public boolean canAppendRow()
  {
    return this.m_table.canAppendRow();
  }
  
  public void seek(IRowView paramIRowView)
  {
    this.m_currentRow = -1;
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert ((this.m_currentRow >= 0) && (this.m_currentRow < this.m_table.getNumRows()));
    if (this.m_table.isLongDataColumn(paramInt))
    {
      if (this.m_table.isNull(this.m_currentRow, paramInt))
      {
        paramETDataRequest.getData().setNull();
        return false;
      }
      return this.m_longDataStore.retrieveData(this.m_table.getFileMarker(this.m_currentRow, paramInt), paramETDataRequest);
    }
    return this.m_table.retrieveData(this.m_currentRow, paramInt, paramETDataRequest);
  }
  
  public boolean moveToNextRow()
  {
    if (this.m_currentRow < this.m_table.getNumRows() - 1)
    {
      this.m_currentRow += 1;
      return true;
    }
    return false;
  }
  
  public boolean moveOuter()
  {
    this.m_currentRow = this.m_rowTracker.nextClearBit(this.m_currentRow + 1);
    return this.m_currentRow < this.m_table.getNumRows();
  }
  
  public void setOutputOuter()
  {
    this.m_currentRow = -1;
  }
  
  public boolean hasOuterRows()
  {
    return this.m_rowTracker.nextClearBit(0) < this.m_table.getNumRows();
  }
  
  public void match()
  {
    this.m_rowTracker.set(this.m_currentRow);
  }
  
  public void close()
  {
    this.m_table.clear();
    if (this.m_longDataStore != null)
    {
      this.m_longDataStore.destroy();
      this.m_longDataStore = null;
    }
  }
  
  public long getRequiredMemory()
  {
    return this.m_table.getRowSize() + this.m_table.getMemOverhead() + (this.m_hasLongData ? 4096 : 0);
  }
  
  public long reduceMemoryUsage()
  {
    return this.m_table.reduceMemoryUsage();
  }
  
  public void assignMemory(long paramLong)
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("Invalid amount assigned");
    }
    if (this.m_memoryBalance < 0L)
    {
      this.m_memoryBalance += paramLong;
      paramLong = this.m_memoryBalance;
      if (this.m_memoryBalance > 0L) {
        this.m_memoryBalance = 0L;
      }
    }
    if (paramLong > 0L) {
      this.m_table.increaseMemLimit(paramLong);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/InMemJoinUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */