package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.IndexRowView;
import com.simba.sqlengine.executor.etree.temptable.LongDataStore;
import com.simba.sqlengine.executor.etree.temptable.RowComparator;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class Partition
  implements ISlaveJoinUnit, IMasterJoinUnit
{
  private static final int BUFFER_SIZE = 8192;
  private static final int TRACKER_SIZE = 10;
  private HHJoinDataSource m_dataSource = null;
  private RepartitionState m_repState = null;
  private InMemTable m_data = null;
  private boolean m_isSlave = false;
  private int[] m_hashColumns = null;
  private long m_partitionSize = -1L;
  private HasherFactory m_hsFactory = null;
  private boolean[] m_dataNeeded = null;
  private IColumn[] m_columns = null;
  private Partition m_pairedPartion = null;
  private long m_memoryAssigned = 0L;
  private int m_inMemColSizeLimit = -1;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extProp;
  private final IColumn[] m_hashColMeta;
  private Map<Long, List<Integer>> m_hashedData;
  private List<Integer> m_seekedRows;
  private int m_cursor;
  private IHasher m_fineHasher;
  private RowComparator m_comparator;
  private IRowView m_matchingRow;
  private IndexRowView m_dataRow;
  private BitSet m_rowTracker;
  private int m_outerRowCursor;
  private HashRowView m_masterCompRow;
  private HashRowView m_compRow;
  private boolean m_outputOuter;
  private boolean m_saveOuterRows;
  private long[] m_rePartTracker;
  private IHasher m_rePartHasher;
  private LongDataStore m_longDataStore;
  
  public Partition(HHJoinDataSource paramHHJoinDataSource, IColumn[] paramArrayOfIColumn1, int[] paramArrayOfInt, IColumn[] paramArrayOfIColumn2, HasherFactory paramHasherFactory, boolean[] paramArrayOfBoolean, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, LongDataStore paramLongDataStore)
  {
    this.m_extProp = paramExternalAlgorithmProperties;
    this.m_dataSource = paramHHJoinDataSource;
    this.m_hsFactory = paramHasherFactory;
    this.m_dataNeeded = paramArrayOfBoolean;
    this.m_columns = paramArrayOfIColumn1;
    this.m_partitionSize = Long.MAX_VALUE;
    this.m_rePartTracker = null;
    this.m_hashColumns = paramArrayOfInt;
    this.m_memoryAssigned = 0L;
    this.m_inMemColSizeLimit = paramExternalAlgorithmProperties.getCellMemoryLimit();
    this.m_repState = null;
    this.m_longDataStore = paramLongDataStore;
    this.m_isSlave = false;
    this.m_hashColMeta = ((IColumn[])paramArrayOfIColumn2.clone());
  }
  
  public void finishExecution()
    throws ErrorException
  {
    while ((this.m_repState != null) && (!this.m_isSlave) && (this.m_repState.moveToNextRow())) {}
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (this.m_isSlave)
    {
      int i;
      if (this.m_outputOuter)
      {
        i = this.m_outerRowCursor;
      }
      else
      {
        assert (this.m_seekedRows != null);
        assert ((this.m_cursor >= 0) && (this.m_cursor < this.m_seekedRows.size()));
        i = ((Integer)this.m_seekedRows.get(this.m_cursor)).intValue();
      }
      if (this.m_data.isLongDataColumn(paramInt))
      {
        if (this.m_data.isNull(i, paramInt))
        {
          paramETDataRequest.getData().setNull();
          return false;
        }
        assert (this.m_longDataStore != null);
        return this.m_longDataStore.retrieveData(this.m_data.getFileMarker(i, paramInt), paramETDataRequest);
      }
      return this.m_data.retrieveData(i, paramInt, paramETDataRequest);
    }
    return this.m_dataSource.retrieveData(paramInt, paramETDataRequest);
  }
  
  public void close()
  {
    this.m_dataSource.close();
    if (this.m_data != null)
    {
      this.m_data.clear();
      this.m_data = null;
    }
  }
  
  public IRowView getRow()
  {
    assert (!this.m_isSlave);
    return this.m_dataSource;
  }
  
  public void seek(IRowView paramIRowView)
  {
    assert (this.m_isSlave);
    this.m_masterCompRow.setRow(paramIRowView);
    long l = this.m_fineHasher.hash(this.m_masterCompRow, this.m_masterCompRow.getHashColumns());
    this.m_seekedRows = ((List)this.m_hashedData.get(Long.valueOf(l)));
    this.m_matchingRow = paramIRowView;
    this.m_cursor = -1;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    if (this.m_isSlave)
    {
      this.m_outputOuter = false;
      if (this.m_seekedRows == null) {
        return false;
      }
      while (++this.m_cursor < this.m_seekedRows.size())
      {
        this.m_dataRow.setRowNum(((Integer)this.m_seekedRows.get(this.m_cursor)).intValue());
        if (compareRow()) {
          return true;
        }
      }
      return false;
    }
    if (this.m_repState != null) {
      return this.m_repState.moveToNextRow();
    }
    return this.m_dataSource.move();
  }
  
  public boolean moveOuter()
  {
    assert (this.m_isSlave);
    while ((this.m_outerRowCursor = this.m_rowTracker.nextClearBit(this.m_outerRowCursor + 1)) <= this.m_data.getMaxRowNumber()) {
      if (this.m_data.isRowInTable(this.m_outerRowCursor))
      {
        this.m_outputOuter = true;
        return true;
      }
    }
    return false;
  }
  
  public void setOutputOuter()
  {
    assert (this.m_isSlave);
    this.m_outerRowCursor = -1;
  }
  
  public boolean hasOuterRows()
  {
    assert (this.m_isSlave);
    int i = -1;
    while ((i = this.m_rowTracker.nextClearBit(++i)) <= this.m_data.getMaxRowNumber()) {
      if (this.m_data.isRowInTable(i)) {
        return true;
      }
    }
    return false;
  }
  
  public void match()
  {
    if (this.m_isSlave) {
      this.m_rowTracker.set(((Integer)this.m_seekedRows.get(this.m_cursor)).intValue());
    }
  }
  
  public long getMemUsage()
  {
    return this.m_memoryAssigned;
  }
  
  public long takeExtraMem()
  {
    assert ((this.m_isSlave) && (this.m_repState != null));
    long l1 = this.m_memoryAssigned;
    long l2 = this.m_data.getMemUsage();
    this.m_memoryAssigned = l2;
    return l1 - l2;
  }
  
  public long getSize()
  {
    return this.m_partitionSize;
  }
  
  public void initAsSlave(boolean paramBoolean1, boolean paramBoolean2, long paramLong, Partition paramPartition, RowComparator paramRowComparator, ETCancelState paramETCancelState)
    throws ErrorException
  {
    if (null != this.m_extProp.getLogger()) {
      LogUtilities.logFunctionEntrance(this.m_extProp.getLogger(), new Object[0]);
    }
    this.m_comparator = paramRowComparator;
    this.m_isSlave = true;
    this.m_pairedPartion = paramPartition;
    if (paramLong < 0L) {
      throw new IllegalArgumentException("Setting a negative memory limit.");
    }
    this.m_memoryAssigned = paramLong;
    this.m_data = new InMemTable(this.m_columns, this.m_inMemColSizeLimit, -1, this.m_dataNeeded, this.m_extProp.getLogger());
    this.m_data.setMemLimit(paramLong);
    this.m_hashedData = new HashMap();
    this.m_seekedRows = null;
    this.m_cursor = -1;
    this.m_fineHasher = this.m_hsFactory.nextHasher(Long.MAX_VALUE);
    this.m_matchingRow = null;
    this.m_dataRow = new IndexRowView(this.m_data);
    this.m_compRow = new HashRowView(this.m_hashColMeta, this.m_hashColumns, this.m_dataRow);
    this.m_masterCompRow = new HashRowView(this.m_hashColMeta, paramPartition.m_hashColumns, null);
    this.m_rowTracker = new BitSet();
    this.m_outerRowCursor = -1;
    this.m_dataSource.reset();
    if (paramBoolean1)
    {
      long l = this.m_memoryAssigned / 8192L;
      if (l < 2L) {
        l = 2L;
      }
      int i = this.m_extProp.getMaxNumOpenFiles() - 1;
      if (this.m_longDataStore != null) {
        i--;
      }
      if (l > i)
      {
        if (i < 2) {
          throw new IllegalStateException("number of sub-partitions is too small: " + i);
        }
        l = i;
      }
      this.m_repState = new RepartitionState(paramBoolean2, this.m_hsFactory.nextHasher(l), paramETCancelState);
      this.m_repState.repartitionSlave();
    }
    else
    {
      loadData();
    }
  }
  
  public void initAsMaster(boolean paramBoolean1, boolean paramBoolean2, long paramLong, Partition paramPartition, boolean paramBoolean3, ETCancelState paramETCancelState)
    throws ErrorException
  {
    if (null != this.m_extProp.getLogger()) {
      LogUtilities.logFunctionEntrance(this.m_extProp.getLogger(), new Object[0]);
    }
    this.m_isSlave = false;
    this.m_saveOuterRows = paramBoolean3;
    this.m_pairedPartion = paramPartition;
    this.m_dataSource.reset();
    if (paramBoolean1)
    {
      this.m_repState = new RepartitionState(paramBoolean2, null, paramETCancelState);
      this.m_data = new InMemTable(this.m_columns, this.m_inMemColSizeLimit, -1, this.m_dataNeeded, this.m_extProp.getLogger());
      this.m_data.setMemLimit(paramLong);
    }
  }
  
  public boolean canRepartition()
  {
    if (this.m_rePartTracker == null) {
      return true;
    }
    long l1 = -1L;
    for (long l2 : this.m_rePartTracker) {
      if (l2 > l1) {
        l1 = l2;
      }
    }
    return l1 / getSize() < 0.75D;
  }
  
  public HHJoinDataSource getDataSource()
  {
    return this.m_dataSource;
  }
  
  public boolean isRepartion()
  {
    return this.m_repState != null;
  }
  
  public Map<Long, Partition> takeSubPartition()
    throws ErrorException
  {
    if (null != this.m_extProp.getLogger()) {
      LogUtilities.logFunctionEntrance(this.m_extProp.getLogger(), new Object[0]);
    }
    assert (isRepartion());
    HashMap localHashMap = this.m_repState.m_subPartition;
    this.m_repState.m_subPartition = null;
    Iterator localIterator = localHashMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Long localLong = (Long)localIterator.next();
      ((Partition)localHashMap.get(localLong)).m_dataSource.reset();
    }
    return localHashMap;
  }
  
  public Map<Long, Partition> takeProcessedPartition()
    throws ErrorException
  {
    if (null != this.m_extProp.getLogger()) {
      LogUtilities.logFunctionEntrance(this.m_extProp.getLogger(), new Object[0]);
    }
    assert (isRepartion());
    HashMap localHashMap = this.m_repState.m_processedPartitions;
    this.m_repState.m_processedPartitions = null;
    Iterator localIterator = localHashMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Long localLong = (Long)localIterator.next();
      ((Partition)localHashMap.get(localLong)).m_dataSource.reset();
    }
    return localHashMap;
  }
  
  public void destroy()
  {
    if (null != this.m_extProp.getLogger()) {
      LogUtilities.logFunctionEntrance(this.m_extProp.getLogger(), new Object[0]);
    }
    this.m_dataSource.destroy();
    if (this.m_repState != null)
    {
      this.m_repState.cleanPartitions();
      this.m_repState = null;
    }
  }
  
  private void buildIndex(List<Integer> paramList)
  {
    HashRowView localHashRowView = new HashRowView(this.m_hashColMeta, this.m_hashColumns, this.m_dataRow);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      this.m_dataRow.setRowNum(i);
      long l = this.m_fineHasher.hash(localHashRowView, localHashRowView.getHashColumns());
      if (this.m_hashedData.containsKey(Long.valueOf(l)))
      {
        ((List)this.m_hashedData.get(Long.valueOf(l))).add(Integer.valueOf(i));
      }
      else
      {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(Integer.valueOf(i));
        this.m_hashedData.put(Long.valueOf(l), localArrayList);
      }
    }
  }
  
  private boolean compareRow()
  {
    int i = this.m_hashColumns.length;
    this.m_compRow.setRow(this.m_dataRow);
    this.m_masterCompRow.setRow(this.m_matchingRow);
    int j = 0 == this.m_comparator.compare(this.m_compRow, this.m_masterCompRow) ? 1 : 0;
    if (j == 0) {
      return false;
    }
    for (int k = 0; k < i; k++)
    {
      if (this.m_compRow.isNull(k)) {
        return false;
      }
      int m = this.m_compRow.getColumn(k).getTypeMetadata().getType();
      if ((8 == m) || (6 == m))
      {
        if (Double.isNaN(this.m_compRow.getDouble(k))) {
          return false;
        }
      }
      else if ((7 == m) && (Float.isNaN(this.m_compRow.getReal(k)))) {
        return false;
      }
    }
    return true;
  }
  
  private void initAsWriting()
  {
    this.m_rePartTracker = new long[10];
    this.m_rePartHasher = this.m_hsFactory.nextHasher(10L);
    this.m_partitionSize = 0L;
  }
  
  private void loadData()
    throws ErrorException
  {
    LinkedList localLinkedList = new LinkedList();
    while (this.m_dataSource.move())
    {
      int i = this.m_data.appendRow();
      assert (i >= 0);
      this.m_data.writeRow(i, this.m_dataSource);
      localLinkedList.add(Integer.valueOf(i));
    }
    buildIndex(localLinkedList);
  }
  
  private void writeRows(InMemTable paramInMemTable, List<Integer> paramList, boolean paramBoolean)
    throws ErrorException
  {
    IndexRowView localIndexRowView = new IndexRowView(paramInMemTable);
    HashRowView localHashRowView = new HashRowView(this.m_hashColMeta, this.m_hashColumns, localIndexRowView);
    Iterator localIterator = paramList.iterator();
    int i;
    while (localIterator.hasNext())
    {
      i = ((Integer)localIterator.next()).intValue();
      int j = paramInMemTable.getRowSize(i);
      this.m_partitionSize += j;
      localIndexRowView.setRowNum(i);
      long l = this.m_rePartHasher.hash(localHashRowView, localHashRowView.getHashColumns());
      assert ((l >= 0L) && (l < this.m_rePartTracker.length));
      this.m_rePartTracker[((int)l)] += j;
    }
    this.m_dataSource.writeRows(paramInMemTable, paramList, 8192);
    if (paramBoolean)
    {
      localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        i = ((Integer)localIterator.next()).intValue();
        paramInMemTable.removeRow(i);
      }
    }
  }
  
  private class RepartitionState
  {
    private IHasher m_courseHasher;
    HashMap<Long, Partition> m_subPartition;
    HashMap<Long, List<Integer>> m_partialPartitions;
    HashMap<Long, Long> m_partSizes;
    HashMap<Long, List<Integer>> m_inMemPartitions;
    HashMap<Long, Partition> m_processedPartitions;
    private boolean m_maintainAllRows;
    private final ETCancelState m_cancelState;
    
    public RepartitionState(boolean paramBoolean, IHasher paramIHasher, ETCancelState paramETCancelState)
    {
      if (null != Partition.this.m_extProp.getLogger()) {
        LogUtilities.logFunctionEntrance(Partition.this.m_extProp.getLogger(), new Object[0]);
      }
      this.m_courseHasher = paramIHasher;
      this.m_subPartition = new HashMap();
      this.m_partialPartitions = new HashMap();
      this.m_partSizes = new HashMap();
      this.m_inMemPartitions = new HashMap();
      this.m_maintainAllRows = paramBoolean;
      this.m_processedPartitions = new HashMap();
      this.m_cancelState = paramETCancelState;
    }
    
    public void repartitionSlave()
      throws ErrorException
    {
      try
      {
        if (null != Partition.this.m_extProp.getLogger()) {
          LogUtilities.logFunctionEntrance(Partition.this.m_extProp.getLogger(), new Object[0]);
        }
        HashRowView localHashRowView = new HashRowView(Partition.this.m_hashColMeta, Partition.this.m_hashColumns, Partition.this.m_dataSource);
        while (Partition.this.m_dataSource.move()) {
          repartRow(localHashRowView);
        }
        flushPartitions(this.m_partialPartitions, true);
        this.m_partialPartitions.clear();
        Partition.this.m_data.reduceMemoryUsage();
        if (this.m_maintainAllRows)
        {
          flushPartitions(this.m_inMemPartitions, false);
          localIterator = this.m_inMemPartitions.keySet().iterator();
          while (localIterator.hasNext())
          {
            long l = ((Long)localIterator.next()).longValue();
            assert (this.m_subPartition.containsKey(Long.valueOf(l)));
            this.m_processedPartitions.put(Long.valueOf(l), this.m_subPartition.remove(Long.valueOf(l)));
          }
        }
        closePartitions();
        Iterator localIterator = this.m_inMemPartitions.values().iterator();
        while (localIterator.hasNext())
        {
          List localList = (List)localIterator.next();
          Partition.this.buildIndex(localList);
        }
      }
      catch (ErrorException localErrorException)
      {
        cleanPartitions();
        throw localErrorException;
      }
      catch (RuntimeException localRuntimeException)
      {
        cleanPartitions();
        throw localRuntimeException;
      }
      catch (Error localError)
      {
        cleanPartitions();
        throw localError;
      }
    }
    
    public boolean moveToNextRow()
      throws ErrorException
    {
      try
      {
        if (null != Partition.this.m_extProp.getLogger()) {
          LogUtilities.logFunctionEntrance(Partition.this.m_extProp.getLogger(), new Object[0]);
        }
        HashRowView localHashRowView = new HashRowView(Partition.this.m_hashColMeta, Partition.this.m_hashColumns, Partition.this.m_dataSource);
        Object localObject;
        while (Partition.this.m_dataSource.move())
        {
          localObject = Partition.access$600(Partition.this).m_repState.m_courseHasher;
          long l = ((IHasher)localObject).hash(localHashRowView, localHashRowView.getHashColumns());
          boolean bool1 = Partition.access$600(Partition.this).m_repState.m_inMemPartitions.containsKey(Long.valueOf(l));
          boolean bool2 = Partition.access$600(Partition.this).m_repState.m_subPartition.containsKey(Long.valueOf(l));
          if ((Partition.this.m_saveOuterRows) || (bool1) || (bool2))
          {
            if ((this.m_maintainAllRows) || (bool2)) {
              repartRow(localHashRowView);
            }
            if ((bool1) || (!bool2)) {
              return true;
            }
          }
        }
        flushPartitions(this.m_inMemPartitions, true);
        this.m_inMemPartitions.clear();
        flushPartitions(this.m_partialPartitions, true);
        this.m_partialPartitions.clear();
        if (this.m_maintainAllRows)
        {
          localObject = this.m_subPartition.entrySet().iterator();
          while (((Iterator)localObject).hasNext())
          {
            Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
            if (Partition.access$600(Partition.this).m_repState.m_processedPartitions.containsKey(localEntry.getKey()))
            {
              ((Iterator)localObject).remove();
              this.m_processedPartitions.put(localEntry.getKey(), localEntry.getValue());
            }
          }
        }
        closePartitions();
        return false;
      }
      catch (ErrorException localErrorException)
      {
        cleanPartitions();
        throw localErrorException;
      }
      catch (RuntimeException localRuntimeException)
      {
        cleanPartitions();
        throw localRuntimeException;
      }
      catch (Error localError)
      {
        cleanPartitions();
        throw localError;
      }
    }
    
    public void cleanPartitions()
    {
      Iterator localIterator = this.m_subPartition.values().iterator();
      Partition localPartition;
      while (localIterator.hasNext())
      {
        localPartition = (Partition)localIterator.next();
        localPartition.destroy();
      }
      this.m_subPartition.clear();
      localIterator = this.m_processedPartitions.values().iterator();
      while (localIterator.hasNext())
      {
        localPartition = (Partition)localIterator.next();
        localPartition.destroy();
      }
      this.m_partialPartitions.clear();
    }
    
    private void closePartitions()
    {
      Iterator localIterator = this.m_subPartition.values().iterator();
      Partition localPartition;
      while (localIterator.hasNext())
      {
        localPartition = (Partition)localIterator.next();
        localPartition.close();
      }
      localIterator = this.m_processedPartitions.values().iterator();
      while (localIterator.hasNext())
      {
        localPartition = (Partition)localIterator.next();
        localPartition.close();
      }
    }
    
    private void flushPartitions(HashMap<Long, List<Integer>> paramHashMap, boolean paramBoolean)
      throws ErrorException
    {
      Iterator localIterator = paramHashMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        getSubpartition(((Long)localEntry.getKey()).longValue()).writeRows(Partition.this.m_data, (List)localEntry.getValue(), paramBoolean);
        this.m_partSizes.remove(localEntry.getKey());
      }
    }
    
    private Partition getSubpartition(long paramLong)
      throws ErrorException
    {
      if (this.m_subPartition.containsKey(Long.valueOf(paramLong))) {
        return (Partition)this.m_subPartition.get(Long.valueOf(paramLong));
      }
      Partition localPartition = new Partition(new HHJoinDataSource(Partition.this.m_longDataStore, Partition.this.m_dataNeeded, Partition.this.m_columns, Partition.this.m_extProp.getCellMemoryLimit(), Partition.this.m_extProp.getStorageDir(), Partition.this.m_extProp.getLogger()), Partition.this.m_columns, Partition.this.m_hashColumns, Partition.this.m_hashColMeta, Partition.this.m_hsFactory, Partition.this.m_dataNeeded, Partition.this.m_extProp, Partition.this.m_longDataStore);
      localPartition.initAsWriting();
      this.m_subPartition.put(Long.valueOf(paramLong), localPartition);
      return localPartition;
    }
    
    private long maxSizePartition(Iterable<Long> paramIterable)
    {
      long l1 = -1L;
      long l2 = -1L;
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
      {
        long l3 = ((Long)localIterator.next()).longValue();
        long l4 = ((Long)this.m_partSizes.get(Long.valueOf(l3))).longValue();
        if (l4 > l1)
        {
          l1 = l4;
          l2 = l3;
        }
      }
      return l2;
    }
    
    private long getPartToFlush_slave()
    {
      if (!this.m_partialPartitions.isEmpty())
      {
        long l1 = maxSizePartition(this.m_partialPartitions.keySet());
        if ((((Long)this.m_partSizes.get(Long.valueOf(l1))).longValue() >= 8192L) || (this.m_inMemPartitions.size() == 0)) {
          return l1;
        }
        long l2 = maxSizePartition(this.m_inMemPartitions.keySet());
        return ((Long)this.m_partSizes.get(Long.valueOf(l1))).longValue() < ((Long)this.m_partSizes.get(Long.valueOf(l2))).longValue() ? l2 : l1;
      }
      assert (!this.m_inMemPartitions.isEmpty());
      return maxSizePartition(this.m_inMemPartitions.keySet());
    }
    
    private long getPartToFlush_master()
    {
      if ((!this.m_partialPartitions.isEmpty()) && (!this.m_inMemPartitions.isEmpty()))
      {
        long l1 = maxSizePartition(this.m_partialPartitions.keySet());
        long l2 = maxSizePartition(this.m_inMemPartitions.keySet());
        return ((Long)this.m_partSizes.get(Long.valueOf(l1))).longValue() < ((Long)this.m_partSizes.get(Long.valueOf(l2))).longValue() ? l2 : l1;
      }
      if (!this.m_partialPartitions.isEmpty()) {
        return maxSizePartition(this.m_partialPartitions.keySet());
      }
      assert (!this.m_inMemPartitions.isEmpty());
      return maxSizePartition(this.m_inMemPartitions.keySet());
    }
    
    private void repartRow(HashRowView paramHashRowView)
      throws ErrorException
    {
      int i;
      long l1;
      while ((i = Partition.this.m_data.appendRow()) < 0)
      {
        this.m_cancelState.checkCancel();
        l1 = Partition.this.m_isSlave ? getPartToFlush_slave() : getPartToFlush_master();
        List localList = (List)this.m_partialPartitions.remove(Long.valueOf(l1));
        if (localList == null) {
          localList = (List)this.m_inMemPartitions.remove(Long.valueOf(l1));
        }
        getSubpartition(l1).writeRows(Partition.this.m_data, localList, true);
        this.m_partSizes.remove(Long.valueOf(l1));
      }
      if (Partition.this.m_isSlave) {
        l1 = this.m_courseHasher.hash(paramHashRowView, paramHashRowView.getHashColumns());
      } else {
        l1 = Partition.access$600(Partition.this).m_repState.m_courseHasher.hash(paramHashRowView, paramHashRowView.getHashColumns());
      }
      Partition.this.m_data.writeRow(i, paramHashRowView.getWrapped());
      long l2 = Partition.this.m_data.getRowSize(i);
      if (this.m_partSizes.containsKey(Long.valueOf(l1))) {
        this.m_partSizes.put(Long.valueOf(l1), Long.valueOf(((Long)this.m_partSizes.get(Long.valueOf(l1))).longValue() + l2));
      } else {
        this.m_partSizes.put(Long.valueOf(l1), Long.valueOf(l2));
      }
      HashMap localHashMap = this.m_subPartition.containsKey(Long.valueOf(l1)) ? this.m_partialPartitions : this.m_inMemPartitions;
      if (localHashMap.containsKey(Long.valueOf(l1)))
      {
        ((List)localHashMap.get(Long.valueOf(l1))).add(Integer.valueOf(i));
      }
      else
      {
        ArrayList localArrayList = new ArrayList();
        localHashMap.put(Long.valueOf(l1), localArrayList);
        localArrayList.add(Integer.valueOf(i));
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/Partition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */