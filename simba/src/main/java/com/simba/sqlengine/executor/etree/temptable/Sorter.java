package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.support.ILogger;
import com.simba.support.LogLevel;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

public class Sorter
  implements IMemoryConsumer
{
  private OnePassSorter m_onePassSorter = null;
  private IColumn[] m_columnMeta;
  private RowComparator m_comparator;
  private TemporaryTableBuilder.TemporaryTableProperties m_properties;
  private RowComparator.NullCollation m_nullCollation;
  private ILogger m_logger;
  private boolean[] m_dataNeeded;
  private BlockConverter m_blockConverter;
  private long m_memAssigned = 0L;
  private IMemManagerAgent m_memAgent = null;
  private Stage m_currentStage;
  private final ETCancelState m_cancelState;
  
  public Sorter(List<IColumn> paramList, List<AESortSpec> paramList1, TemporaryTableBuilder.TemporaryTableProperties paramTemporaryTableProperties, RowComparator.NullCollation paramNullCollation, ETCancelState paramETCancelState, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    this.m_properties = paramTemporaryTableProperties;
    this.m_nullCollation = paramNullCollation;
    this.m_columnMeta = ((IColumn[])paramList.toArray(new IColumn[paramList.size()]));
    this.m_comparator = RowComparator.createComparator(this.m_columnMeta, paramList1, this.m_nullCollation);
    this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
    this.m_memAssigned = 0L;
    this.m_logger = this.m_properties.m_logger;
    this.m_blockConverter = new BlockConverter(this.m_columnMeta, this.m_properties, this.m_logger, paramArrayOfBoolean);
    this.m_currentStage = Stage.FIRST_PASS;
    this.m_cancelState = paramETCancelState;
  }
  
  public void initialize()
    throws ErrorException
  {
    this.m_onePassSorter = new OnePassSorter(this.m_columnMeta, this.m_properties, this.m_comparator, this.m_dataNeeded);
    this.m_onePassSorter.increaseMemoryUsage(this.m_memAssigned);
  }
  
  public void addRow()
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.appendRow();
  }
  
  public DataStore getSorted()
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    List localList;
    try
    {
      localList = this.m_onePassSorter.getSorted();
    }
    finally
    {
      this.m_onePassSorter.destroy();
      this.m_onePassSorter = null;
    }
    DataStore localDataStore;
    if (localList.size() > 1)
    {
      this.m_currentStage = Stage.MERGING;
      localDataStore = merge(localList);
    }
    else if (localList.size() == 1)
    {
      localDataStore = (DataStore)localList.get(0);
    }
    else
    {
      localDataStore = null;
    }
    this.m_currentStage = Stage.FINISHED;
    this.m_blockConverter.reset();
    if (this.m_memAssigned != 0L)
    {
      this.m_memAgent.recycleMemory(this.m_memAssigned);
      this.m_memAssigned = 0L;
    }
    return localDataStore;
  }
  
  public void finishAppending()
  {
    this.m_onePassSorter.finishAppending();
  }
  
  private DataStore merge(List<DataStore> paramList)
    throws ErrorException
  {
    PriorityQueue localPriorityQueue1 = new PriorityQueue(paramList.size(), new Comparator()
    {
      public int compare(DataStore paramAnonymousDataStore1, DataStore paramAnonymousDataStore2)
      {
        return paramAnonymousDataStore1.getNumberBlocks() - paramAnonymousDataStore2.getNumberBlocks();
      }
    });
    localPriorityQueue1.addAll(paramList);
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    this.m_blockConverter.setMemLimit(this.m_properties.m_blockSize);
    PriorityQueue localPriorityQueue2 = new PriorityQueue(2, this.m_comparator);
    DataStore localDataStore1 = null;
    DataStore localDataStore2 = null;
    int i = 0;
    try
    {
      Object localObject1;
      for (int j = 1; localPriorityQueue1.size() > 1; j++)
      {
        int k = (int)((this.m_memAssigned - this.m_properties.m_blockSize) / this.m_properties.m_blockSize);
        if (k > this.m_properties.m_maxOpenFiles - 1) {
          k = this.m_properties.m_maxOpenFiles - 1;
        }
        localDataStore1 = new DataStore(this.m_properties, this.m_columnMeta);
        for (int m = 0; (m < k) && (localPriorityQueue1.size() > 0); m++)
        {
          localDataStore2 = (DataStore)localPriorityQueue1.poll();
          if (localDataStore2.numberOfBlocksHeld() < 1) {
            localDataStore2.giveBlock();
          }
          localDataStore2.moveToNextRow();
          localPriorityQueue2.add(localDataStore2);
        }
        localDataStore2 = null;
        if (localPriorityQueue2.size() < k)
        {
          long l = this.m_memAssigned - localPriorityQueue2.size() * this.m_properties.m_blockSize;
          assert (l > 0L);
          this.m_memAgent.recycleMemory(l);
          this.m_memAssigned -= l;
          this.m_currentStage = Stage.MERGE_LAST;
        }
        if ((null != this.m_logger) && (LogUtilities.shouldLogLevel(LogLevel.DEBUG, this.m_logger))) {
          LogUtilities.logDebug(String.format("Pass %d: Merging %d of %d chunks.", new Object[] { Integer.valueOf(j), Integer.valueOf(localPriorityQueue2.size()), Integer.valueOf(localPriorityQueue2.size() + localPriorityQueue1.size()) }), this.m_logger);
        }
        while (localPriorityQueue2.size() > 0)
        {
          localDataStore2 = (DataStore)localPriorityQueue2.poll();
          if (!this.m_blockConverter.appendRow())
          {
            this.m_cancelState.checkCancel();
            localDataStore1.put(this.m_blockConverter.toRowBlock());
            this.m_blockConverter.appendRow();
          }
          copyRow(localDataStore2, this.m_blockConverter);
          if (localDataStore2.moveToNextRow()) {
            localPriorityQueue2.add(localDataStore2);
          } else {
            localDataStore2.destroy();
          }
        }
        localObject1 = this.m_blockConverter.toRowBlock();
        if (localObject1 != null) {
          localDataStore1.put((RowBlock)localObject1);
        }
        localPriorityQueue1.add(localDataStore1);
        localDataStore1 = null;
      }
      i = 1;
      DataStore localDataStore3 = (DataStore)localPriorityQueue1.poll();
      if (i == 0)
      {
        Iterator localIterator1 = localPriorityQueue2.iterator();
        while (localIterator1.hasNext())
        {
          localObject1 = (DataStore)localIterator1.next();
          ((DataStore)localObject1).destroy();
        }
        localIterator1 = localPriorityQueue1.iterator();
        while (localIterator1.hasNext())
        {
          localObject1 = (DataStore)localIterator1.next();
          ((DataStore)localObject1).destroy();
        }
        if (localDataStore1 != null) {
          localDataStore1.destroy();
        }
        if (localDataStore2 != null) {
          localDataStore2.destroy();
        }
      }
      return localDataStore3;
    }
    finally
    {
      if (i == 0)
      {
        Iterator localIterator2 = localPriorityQueue2.iterator();
        DataStore localDataStore4;
        while (localIterator2.hasNext())
        {
          localDataStore4 = (DataStore)localIterator2.next();
          localDataStore4.destroy();
        }
        localIterator2 = localPriorityQueue1.iterator();
        while (localIterator2.hasNext())
        {
          localDataStore4 = (DataStore)localIterator2.next();
          localDataStore4.destroy();
        }
        if (localDataStore1 != null) {
          localDataStore1.destroy();
        }
        if (localDataStore2 != null) {
          localDataStore2.destroy();
        }
      }
    }
  }
  
  private void copyRow(DataStore paramDataStore, BlockConverter paramBlockConverter)
  {
    for (int i = 0; i < this.m_columnMeta.length; i++) {
      if (paramDataStore.isNull(i)) {
        paramBlockConverter.setNull(i);
      } else if (ColumnSizeCalculator.isLongData(this.m_columnMeta[i], this.m_properties.m_maxDataLen)) {
        paramBlockConverter.setFileMarker(i, paramDataStore.getFileMarker(i));
      } else {
        switch (this.m_columnMeta[i].getTypeMetadata().getType())
        {
        case -4: 
        case -3: 
        case -2: 
          paramBlockConverter.setBytes(i, paramDataStore.getBytes(i));
          break;
        case -10: 
        case -9: 
        case -8: 
        case -1: 
        case 1: 
        case 12: 
          paramBlockConverter.setString(i, paramDataStore.getString(i));
          break;
        case -7: 
        case 16: 
          paramBlockConverter.setBoolean(i, paramDataStore.getBoolean(i));
          break;
        case -6: 
          paramBlockConverter.setTinyInt(i, paramDataStore.getTinyInt(i));
          break;
        case 5: 
          paramBlockConverter.setSmallInt(i, paramDataStore.getSmallInt(i));
          break;
        case 4: 
          paramBlockConverter.setInteger(i, paramDataStore.getInteger(i));
          break;
        case -5: 
          paramBlockConverter.setBigInt(i, paramDataStore.getBigInt(i));
          break;
        case 7: 
          paramBlockConverter.setReal(i, paramDataStore.getReal(i));
          break;
        case 6: 
        case 8: 
          paramBlockConverter.setDouble(i, paramDataStore.getDouble(i));
          break;
        case 2: 
        case 3: 
          paramBlockConverter.setExactNum(i, paramDataStore.getExactNumber(i));
          break;
        case 91: 
          paramBlockConverter.setDate(i, paramDataStore.getDate(i));
          break;
        case 92: 
          paramBlockConverter.setTime(i, paramDataStore.getTime(i));
          break;
        case 93: 
          paramBlockConverter.setTimestamp(i, paramDataStore.getTimestamp(i));
          break;
        case -11: 
          paramBlockConverter.setGuid(i, paramDataStore.getGuid(i));
          break;
        case 0: 
        case 9: 
        case 10: 
        case 11: 
        case 13: 
        case 14: 
        case 15: 
        case 17: 
        case 18: 
        case 19: 
        case 20: 
        case 21: 
        case 22: 
        case 23: 
        case 24: 
        case 25: 
        case 26: 
        case 27: 
        case 28: 
        case 29: 
        case 30: 
        case 31: 
        case 32: 
        case 33: 
        case 34: 
        case 35: 
        case 36: 
        case 37: 
        case 38: 
        case 39: 
        case 40: 
        case 41: 
        case 42: 
        case 43: 
        case 44: 
        case 45: 
        case 46: 
        case 47: 
        case 48: 
        case 49: 
        case 50: 
        case 51: 
        case 52: 
        case 53: 
        case 54: 
        case 55: 
        case 56: 
        case 57: 
        case 58: 
        case 59: 
        case 60: 
        case 61: 
        case 62: 
        case 63: 
        case 64: 
        case 65: 
        case 66: 
        case 67: 
        case 68: 
        case 69: 
        case 70: 
        case 71: 
        case 72: 
        case 73: 
        case 74: 
        case 75: 
        case 76: 
        case 77: 
        case 78: 
        case 79: 
        case 80: 
        case 81: 
        case 82: 
        case 83: 
        case 84: 
        case 85: 
        case 86: 
        case 87: 
        case 88: 
        case 89: 
        case 90: 
        default: 
          throw new IllegalStateException("Unknown column type");
        }
      }
    }
  }
  
  public void setFileMarker(int paramInt, TemporaryFile.FileMarker paramFileMarker)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setFileMarker(paramInt, paramFileMarker);
  }
  
  public void setNull(int paramInt)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setNull(paramInt);
  }
  
  public void setBigInt(int paramInt, long paramLong)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setBigInt(paramInt, paramLong);
  }
  
  public void setExactNum(int paramInt, BigDecimal paramBigDecimal)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setExactNum(paramInt, paramBigDecimal);
  }
  
  public void setDouble(int paramInt, double paramDouble)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setDouble(paramInt, paramDouble);
  }
  
  public void setReal(int paramInt, float paramFloat)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setReal(paramInt, paramFloat);
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setBoolean(paramInt, paramBoolean);
  }
  
  public void setString(int paramInt, String paramString)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setString(paramInt, paramString);
  }
  
  public void setDate(int paramInt, Date paramDate)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setDate(paramInt, paramDate);
  }
  
  public void setTime(int paramInt, Time paramTime)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setTime(paramInt, paramTime);
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setTimestamp(paramInt, paramTimestamp);
  }
  
  public void setGuid(int paramInt, UUID paramUUID)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setGuid(paramInt, paramUUID);
  }
  
  public void setInteger(int paramInt1, int paramInt2)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setInteger(paramInt1, paramInt2);
  }
  
  public void setSmallInt(int paramInt, short paramShort)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setSmallInt(paramInt, paramShort);
  }
  
  public void setTinyInt(int paramInt, byte paramByte)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setTinyInt(paramInt, paramByte);
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
  {
    assert (this.m_currentStage == Stage.FIRST_PASS) : "Sorter call sequence error.";
    this.m_onePassSorter.setBytes(paramInt, paramArrayOfByte);
  }
  
  public void reset()
  {
    this.m_currentStage = Stage.FIRST_PASS;
    if (this.m_onePassSorter != null)
    {
      this.m_onePassSorter.destroy();
      this.m_onePassSorter = null;
    }
    this.m_blockConverter.reset();
    this.m_blockConverter.setMemLimit(0L);
    if (this.m_memAssigned != 0L)
    {
      this.m_memAgent.recycleMemory(this.m_memAssigned);
      this.m_memAssigned = 0L;
    }
  }
  
  public long getRequiredMemory()
  {
    return this.m_properties.m_blockSize * 3L + this.m_blockConverter.getMemOverhead();
  }
  
  RowComparator getRowComparator()
  {
    return this.m_comparator;
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_memAgent = paramIMemManagerAgent;
  }
  
  public long assign(long paramLong)
  {
    switch (this.m_currentStage)
    {
    case FIRST_PASS: 
      if (this.m_onePassSorter != null) {
        this.m_onePassSorter.increaseMemoryUsage(paramLong);
      }
      this.m_memAssigned += paramLong;
      return paramLong;
    case MERGING: 
      this.m_memAssigned += paramLong;
      return paramLong;
    case MERGE_LAST: 
    case FINISHED: 
      return 0L;
    }
    throw new IllegalStateException("Unknown state.");
  }
  
  private static enum Stage
  {
    FIRST_PASS,  MERGING,  MERGE_LAST,  FINISHED;
    
    private Stage() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/Sorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */