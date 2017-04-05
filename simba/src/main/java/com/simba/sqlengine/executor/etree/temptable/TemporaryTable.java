package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public class TemporaryTable
  implements ITemporaryTable
{
  private static final long EIGHT_KB = 8192L;
  private List<IColumn> m_metadata;
  private DataStore m_dataStore;
  private InMemTable m_inMemTable;
  private LongDataStore m_longDataStore;
  private boolean[] m_isLongData;
  private TemporaryTableBuilder.TemporaryTableProperties m_properties;
  private long m_allocatedMemory;
  private boolean m_startedFetch;
  private int m_rowInCurrentBlock = -1;
  private boolean m_inMemTableHasData;
  private boolean[] m_needsData;
  private IMemManagerAgent m_memManAgent = null;
  private long m_rowCount = -1L;
  
  public TemporaryTable(List<? extends IColumn> paramList, TemporaryTableBuilder.TemporaryTableProperties paramTemporaryTableProperties, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    this.m_metadata = new ArrayList(paramList);
    this.m_properties = paramTemporaryTableProperties;
    this.m_needsData = ((boolean[])paramArrayOfBoolean.clone());
    long l = ExternalAlgorithmUtil.calculateRowSize(this.m_metadata, this.m_needsData, this.m_properties.m_maxDataLen);
    int i = 0;
    if (0L < l) {
      i = (int)(this.m_properties.m_blockSize * 0.1D / l) + 1;
    }
    this.m_inMemTable = new InMemTable((IColumn[])this.m_metadata.toArray(new IColumn[0]), this.m_properties.m_maxDataLen, i, this.m_needsData, this.m_properties.m_logger);
    prepareForLongData();
  }
  
  public void open()
    throws ErrorException
  {
    if (0L == this.m_allocatedMemory) {
      throw new IllegalStateException("Memory unassigned.");
    }
    this.m_startedFetch = false;
    try
    {
      this.m_dataStore = new DataStore(this.m_properties, (IColumn[])this.m_metadata.toArray(new IColumn[0]));
      this.m_inMemTableHasData = false;
    }
    catch (Exception localException)
    {
      if (null != this.m_dataStore)
      {
        this.m_dataStore.destroy();
        this.m_dataStore = null;
      }
      if ((localException instanceof ErrorException)) {
        throw ((ErrorException)localException);
      }
      throw SQLEngineExceptionFactory.runtimeException(localException);
    }
  }
  
  public void close()
  {
    if (null != this.m_dataStore)
    {
      this.m_dataStore.destroy();
      this.m_dataStore = null;
    }
    if (null != this.m_longDataStore)
    {
      this.m_longDataStore.destroy();
      this.m_longDataStore = null;
    }
    this.m_inMemTable.clear();
    this.m_inMemTable.setMemLimit(0L);
    this.m_memManAgent.recycleMemory(this.m_allocatedMemory);
    this.m_memManAgent.unregisterConsumer();
  }
  
  public long getRowCount()
  {
    return this.m_rowCount;
  }
  
  public void reset()
    throws ErrorException
  {
    if (this.m_rowCount == -1L) {
      throw new IllegalStateException("Resetting temporary table before data has been set.");
    }
    prepareForFetch();
    this.m_dataStore.reset();
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    prepareForFetch();
    return this.m_dataStore.moveToNextRow();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (!this.m_startedFetch) {
      throw new UnsupportedOperationException("Cannot retrieveData without move()");
    }
    return DataRetrievalUtil.retrieveFromRowView(paramInt, this.m_isLongData[paramInt], paramETDataRequest, this.m_dataStore, this.m_longDataStore);
  }
  
  public void writeFromRelation(ETRelationalExpr paramETRelationalExpr)
    throws ErrorException
  {
    int i = this.m_metadata.size();
    short[] arrayOfShort = new short[i];
    ETDataRequest[] arrayOfETDataRequest = new ETDataRequest[i];
    for (int j = 0; j < arrayOfETDataRequest.length; j++)
    {
      arrayOfETDataRequest[j] = new ETDataRequest((IColumn)this.m_metadata.get(j));
      arrayOfShort[j] = ((IColumn)this.m_metadata.get(j)).getTypeMetadata().getType();
    }
    this.m_rowCount = 0L;
    while (paramETRelationalExpr.move())
    {
      this.m_rowCount += 1L;
      appendRow();
      j = this.m_rowInCurrentBlock;
      for (int k = 0; k < i; k++) {
        if (paramETRelationalExpr.getDataNeeded(k)) {
          switch (arrayOfShort[k])
          {
          case -4: 
          case -3: 
          case -2: 
            if (this.m_isLongData[k] != 0) {
              this.m_inMemTable.setFileMarker(j, k, this.m_longDataStore.put(k, paramETRelationalExpr));
            } else {
              this.m_inMemTable.writeData(j, k, paramETRelationalExpr);
            }
            break;
          case -10: 
          case -9: 
          case -8: 
          case -1: 
          case 1: 
          case 12: 
            if (this.m_isLongData[k] != 0) {
              this.m_inMemTable.setFileMarker(j, k, this.m_longDataStore.put(k, paramETRelationalExpr));
            } else {
              this.m_inMemTable.writeData(j, k, paramETRelationalExpr);
            }
            break;
          case -7: 
          case -6: 
          case -5: 
          case 0: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
          case 7: 
          case 8: 
          case 9: 
          case 10: 
          case 11: 
          default: 
            this.m_inMemTable.writeData(j, k, paramETRelationalExpr);
          }
        }
      }
    }
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_memManAgent = paramIMemManagerAgent;
  }
  
  public long assign(long paramLong)
  {
    long l1 = getRequiredMemory();
    if (this.m_allocatedMemory > l1) {
      return 0L;
    }
    long l2 = l1 - this.m_allocatedMemory;
    if (l2 >= paramLong)
    {
      this.m_allocatedMemory += paramLong;
      this.m_inMemTable.setMemLimit(this.m_properties.m_blockSize);
      return paramLong;
    }
    this.m_allocatedMemory += l2;
    return l2;
  }
  
  public long getRequiredMemory()
  {
    return this.m_properties.m_blockSize + this.m_inMemTable.getMemOverhead() + (this.m_longDataStore == null ? 0L : 8192L);
  }
  
  private void appendCurrentBlockToDataStore()
    throws ErrorException
  {
    assert (this.m_inMemTableHasData);
    this.m_dataStore.put(this.m_inMemTable.toRowBlock());
    this.m_inMemTable.clear();
    this.m_inMemTableHasData = false;
    this.m_rowInCurrentBlock = -1;
  }
  
  private void prepareForFetch()
    throws ErrorException
  {
    if (!this.m_startedFetch)
    {
      this.m_dataStore.giveBlock();
      if (this.m_inMemTableHasData) {
        appendCurrentBlockToDataStore();
      }
      this.m_startedFetch = true;
    }
  }
  
  private void prepareForLongData()
    throws ErrorException
  {
    this.m_isLongData = new boolean[this.m_metadata.size()];
    for (int i = 0; i < this.m_metadata.size(); i++)
    {
      this.m_isLongData[i] = ColumnSizeCalculator.isLongData((IColumn)this.m_metadata.get(i), this.m_properties.m_maxDataLen);
      if ((this.m_isLongData[i] != 0) && (null == this.m_longDataStore)) {
        this.m_longDataStore = new LongDataStore(this.m_properties.m_storageDir, 8192L, this.m_properties.m_logger);
      }
    }
  }
  
  private void appendRow()
    throws ErrorException
  {
    if (this.m_startedFetch) {
      throw new UnsupportedOperationException("Cannot appendRow after fetch started.");
    }
    int i = this.m_inMemTable.appendRow();
    if (0 > i)
    {
      appendCurrentBlockToDataStore();
      i = this.m_inMemTable.appendRow();
    }
    this.m_rowInCurrentBlock = i;
    this.m_inMemTableHasData = true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/TemporaryTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */