package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.hash.HashAggrPartition;
import com.simba.sqlengine.executor.etree.hash.HashAggrPartitionManager;
import com.simba.sqlengine.executor.etree.hash.HashPartitionProperties;
import com.simba.sqlengine.executor.etree.relation.join.RelationalRowBlock;
import com.simba.sqlengine.executor.etree.temptable.DataStore;
import com.simba.sqlengine.executor.etree.temptable.IRowBlock;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregator;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregatorFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ETHashAggregate
  extends ETAggregate
  implements IMemoryConsumer
{
  private static long PARTITION_SIZE_HINT = 8192L;
  private static int MAX_RECURSION = 1;
  private LinkedList<FetchState> m_fetchStates = new LinkedList();
  private HashPartitionProperties m_properties;
  private AggregateProjectionInfo m_aggregateProjection;
  private OperandProjectionInfo m_operandProjection;
  private IColumn[] m_scalarMeta;
  private TemporaryFile m_longDataStore;
  private boolean[] m_longDataColumns;
  private long m_allocatedMemory = 10000000L;
  
  public ETHashAggregate(ETRelationalExpr paramETRelationalExpr, HashPartitionProperties paramHashPartitionProperties)
  {
    super(extractDataNeeded(paramETRelationalExpr.getColumnCount()), paramETRelationalExpr);
    this.m_properties = paramHashPartitionProperties;
    this.m_aggregateProjection = paramHashPartitionProperties.getAggregateProjection();
    this.m_operandProjection = paramHashPartitionProperties.getOperandProjection();
    this.m_longDataColumns = new boolean[this.m_operandProjection.getNumColumns()];
    for (int i = 0; i < this.m_longDataColumns.length; i++) {
      this.m_longDataColumns[i] = ColumnSizeCalculator.isLongData(this.m_operandProjection.getMetadata()[i], paramHashPartitionProperties.getMaxDataLen());
    }
    int[] arrayOfInt = this.m_operandProjection.getScalarValueColumns();
    this.m_scalarMeta = new IColumn[arrayOfInt.length];
    for (int j = 0; j < arrayOfInt.length; j++) {
      this.m_scalarMeta[j] = this.m_operandProjection.getMetadata()[arrayOfInt[j]];
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_aggregateProjection.getColumnMetadata()[paramInt];
  }
  
  public int getColumnCount()
  {
    return this.m_aggregateProjection.getNumColumns();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return -1L;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    getOperand().open(paramCursorType);
    this.m_fetchStates.clear();
    if (null != this.m_longDataStore) {
      this.m_longDataStore.destroy();
    }
    for (int m : this.m_longDataColumns) {
      if (m != 0)
      {
        this.m_longDataStore = new TemporaryFile(this.m_properties.getStorageDir(), this.m_properties.getLogger());
        break;
      }
    }
    int i = calculateNumPartitions(this.m_allocatedMemory);
    FetchState localFetchState = new FetchState(new HashAggrPartitionManager(i, this.m_longDataStore, this.m_longDataColumns, this.m_properties, this.m_dataNeeded));
    localFetchState.m_inputRows = getOperandRows();
    this.m_fetchStates.add(localFetchState);
  }
  
  public void close()
  {
    getOperand().close();
    if (null != this.m_longDataStore)
    {
      this.m_longDataStore.destroy();
      this.m_longDataStore = null;
    }
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return getOperand();
    }
    throw new IndexOutOfBoundsException();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    FetchState localFetchState = (FetchState)this.m_fetchStates.peek();
    HashAggrPartition localHashAggrPartition = localFetchState.m_partitions.getPartition(localFetchState.m_currentPartition);
    if (this.m_aggregateProjection.isAggregateFnColumn(paramInt))
    {
      paramInt = this.m_aggregateProjection.mapColumnToAggregateIndex(paramInt);
      return localHashAggrPartition.retrieveAggregate(paramInt, paramETDataRequest);
    }
    paramInt = this.m_aggregateProjection.mapToOperandColumn(paramInt);
    return localHashAggrPartition.retrieveScalar(paramInt, paramETDataRequest);
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    while (!this.m_fetchStates.isEmpty())
    {
      if ((this.m_fetchStates.size() - 1 > MAX_RECURSION) && (!$assertionsDisabled)) {
        throw new AssertionError("Max recursion");
      }
      FetchState localFetchState1 = (FetchState)this.m_fetchStates.peek();
      if (null != localFetchState1.m_spilledPartials)
      {
        addPartials(localFetchState1, localFetchState1.m_spilledPartials);
        localFetchState1.m_spilledPartials = null;
      }
      if (null != localFetchState1.m_inputRows)
      {
        scanRows(localFetchState1, localFetchState1.m_inputRows);
        localFetchState1.m_inputRows = null;
      }
      HashAggrPartition localHashAggrPartition;
      if (localFetchState1.m_inMemoryFetch)
      {
        int i = localFetchState1.m_partitions.getNumPartitions();
        while (localFetchState1.m_currentPartition < i)
        {
          localHashAggrPartition = localFetchState1.m_partitions.getPartition(localFetchState1.m_currentPartition);
          if ((!localHashAggrPartition.isFinishedRetrieving()) && (localHashAggrPartition.moveToNextRow())) {
            return true;
          }
          localFetchState1.m_currentPartition += 1;
        }
        localFetchState1.m_currentPartition = 0;
        localFetchState1.m_inMemoryFetch = false;
        localFetchState1.m_memoryUsage = 0L;
        localFetchState1.m_spillCandidates.clear();
      }
      FetchState localFetchState2 = null;
      while (localFetchState1.m_currentPartition < localFetchState1.m_partitions.getNumPartitions())
      {
        localHashAggrPartition = localFetchState1.m_partitions.getPartition(localFetchState1.m_currentPartition++);
        if (localHashAggrPartition.hasFlushedRows())
        {
          localFetchState2 = transferResults(localHashAggrPartition);
          break;
        }
      }
      if (null != localFetchState2) {
        this.m_fetchStates.add(localFetchState2);
      } else {
        this.m_fetchStates.poll();
      }
    }
    return false;
  }
  
  private void addPartials(FetchState paramFetchState, DataStore paramDataStore)
    throws ErrorException
  {
    paramDataStore.giveBlock();
    while (paramDataStore.moveToNextRow())
    {
      int i = paramFetchState.m_partitions.partition(paramDataStore);
      HashAggrPartition localHashAggrPartition = paramFetchState.m_partitions.getPartition(i);
      long l1 = localHashAggrPartition.getMemoryUsage();
      localHashAggrPartition.addPartialAggregation(paramDataStore);
      long l2 = localHashAggrPartition.getMemoryUsage();
      if (l2 > this.m_allocatedMemory / paramFetchState.m_partitions.getNumPartitions()) {
        paramFetchState.m_spillCandidates.add(Integer.valueOf(i));
      }
      paramFetchState.m_memoryUsage += l2 - l1;
      long l3 = this.m_allocatedMemory / paramFetchState.m_partitions.getNumPartitions();
      while (paramFetchState.m_memoryUsage - this.m_allocatedMemory >= l3) {
        if (!spillPartition(paramFetchState)) {
          break;
        }
      }
    }
    paramDataStore.destroy();
  }
  
  private int calculateNumPartitions(long paramLong)
  {
    int i = (int)(paramLong / PARTITION_SIZE_HINT);
    i = Integer.highestOneBit(Math.max(i, 1));
    return i;
  }
  
  private IRowBlock getOperandRows()
    throws ErrorException
  {
    return new RelationalRowBlock(getOperand(), this.m_longDataStore, this.m_properties.getMaxDataLen(), this.m_dataNeeded);
  }
  
  private void scanRows(FetchState paramFetchState, IRowBlock paramIRowBlock)
    throws ErrorException
  {
    while (paramIRowBlock.moveToNextRow())
    {
      int i = paramFetchState.m_partitions.partition(paramIRowBlock);
      updatePartition(paramFetchState, i, paramIRowBlock);
    }
  }
  
  private boolean spillPartition(FetchState paramFetchState)
    throws ErrorException
  {
    Iterator localIterator = paramFetchState.m_spillCandidates.iterator();
    if (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      HashAggrPartition localHashAggrPartition = paramFetchState.m_partitions.getPartition(localInteger.intValue());
      long l1 = localHashAggrPartition.getMemoryUsage();
      int i = 1 == paramFetchState.m_spillCandidates.size() ? 1 : 0;
      localHashAggrPartition.spillPartialRows(Math.min(PARTITION_SIZE_HINT, this.m_allocatedMemory / paramFetchState.m_partitions.getNumPartitions()), i);
      long l2 = localHashAggrPartition.getMemoryUsage();
      if (l2 <= this.m_allocatedMemory / paramFetchState.m_partitions.getNumPartitions()) {
        localIterator.remove();
      }
      paramFetchState.m_memoryUsage -= l1 - l2;
      return true;
    }
    return false;
  }
  
  private FetchState transferResults(HashAggrPartition paramHashAggrPartition)
    throws ErrorException
  {
    HashAggrPartitionManager localHashAggrPartitionManager = new HashAggrPartitionManager(((FetchState)this.m_fetchStates.peek()).m_partitions.getNumPartitions(), this.m_longDataStore, this.m_longDataColumns, this.m_properties, this.m_dataNeeded);
    FetchState localFetchState = new FetchState(localHashAggrPartitionManager);
    localFetchState.m_spilledPartials = paramHashAggrPartition.unspillPartial();
    localFetchState.m_inputRows = paramHashAggrPartition.unspillInputRows();
    return localFetchState;
  }
  
  private void updatePartition(FetchState paramFetchState, int paramInt, IRowView paramIRowView)
    throws ErrorException
  {
    HashAggrPartition localHashAggrPartition = paramFetchState.m_partitions.getPartition(paramInt);
    long l1 = localHashAggrPartition.getMemoryUsage();
    localHashAggrPartition.update(paramIRowView);
    long l2 = localHashAggrPartition.getMemoryUsage();
    if (l2 > this.m_allocatedMemory / paramFetchState.m_partitions.getNumPartitions()) {
      paramFetchState.m_spillCandidates.add(Integer.valueOf(paramInt));
    }
    paramFetchState.m_memoryUsage += l2 - l1;
    long l3 = this.m_allocatedMemory / paramFetchState.m_partitions.getNumPartitions();
    while (paramFetchState.m_memoryUsage - this.m_allocatedMemory >= l3) {
      if (!spillPartition(paramFetchState)) {
        break;
      }
    }
  }
  
  private static boolean[] extractDataNeeded(int paramInt)
  {
    boolean[] arrayOfBoolean = new boolean[paramInt];
    for (int i = 0; i < paramInt; i++) {
      arrayOfBoolean[i] = true;
    }
    return arrayOfBoolean;
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent) {}
  
  public long assign(long paramLong)
  {
    return 0L;
  }
  
  public long getRequiredMemory()
  {
    return 0L;
  }
  
  public static class OperandProjectionInfo
  {
    private IColumn[] m_metadata;
    private int[] m_scalarValueColumns;
    private int[] m_groupingColumns;
    
    public OperandProjectionInfo(IColumn[] paramArrayOfIColumn, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      this.m_metadata = paramArrayOfIColumn;
      this.m_scalarValueColumns = paramArrayOfInt1;
      this.m_groupingColumns = paramArrayOfInt2;
    }
    
    public int getNumColumns()
    {
      return this.m_metadata.length;
    }
    
    public int[] getScalarValueColumns()
    {
      return this.m_scalarValueColumns;
    }
    
    public IColumn[] getMetadata()
    {
      return this.m_metadata;
    }
    
    public int[] getGroupingColumns()
    {
      return this.m_groupingColumns;
    }
  }
  
  private static class FetchState
  {
    public HashAggrPartitionManager m_partitions;
    public long m_memoryUsage;
    public int m_currentPartition;
    public boolean m_inMemoryFetch;
    public DataStore m_spilledPartials;
    public IRowBlock m_inputRows;
    public HashSet<Integer> m_spillCandidates;
    
    public FetchState(HashAggrPartitionManager paramHashAggrPartitionManager)
    {
      this.m_partitions = paramHashAggrPartitionManager;
      this.m_currentPartition = 0;
      this.m_inMemoryFetch = true;
      this.m_memoryUsage = 0L;
      this.m_spilledPartials = null;
      this.m_inputRows = null;
      this.m_spillCandidates = new HashSet();
    }
  }
  
  public static class AggregateProjectionInfo
  {
    private int m_numColumns;
    private IColumn[] m_columnMetadata;
    private int[] m_aggregateFnColumns;
    private int[] m_aggregateFnIndexMap;
    private List<int[]> m_aggregateFnArguments;
    private List<IColumn[]> m_aggregateFnArgumentMetadata;
    private IAggregatorFactory[] m_aggregatorFactories;
    private int[] m_operandColumnMap;
    
    public AggregateProjectionInfo(int paramInt, IColumn[] paramArrayOfIColumn, int[] paramArrayOfInt1, int[] paramArrayOfInt2, List<int[]> paramList, List<IColumn[]> paramList1, IAggregatorFactory[] paramArrayOfIAggregatorFactory)
    {
      this.m_numColumns = paramInt;
      this.m_columnMetadata = paramArrayOfIColumn;
      this.m_operandColumnMap = paramArrayOfInt1;
      this.m_aggregateFnColumns = paramArrayOfInt2;
      this.m_aggregateFnArguments = paramList;
      this.m_aggregateFnArgumentMetadata = paramList1;
      this.m_aggregatorFactories = paramArrayOfIAggregatorFactory;
      this.m_aggregateFnIndexMap = new int[paramInt];
      Arrays.fill(this.m_aggregateFnIndexMap, -1);
      for (int i = 0; i < paramArrayOfInt2.length; i++) {
        this.m_aggregateFnIndexMap[paramArrayOfInt2[i]] = i;
      }
    }
    
    public int getNumColumns()
    {
      return this.m_numColumns;
    }
    
    public IColumn[] getColumnMetadata()
    {
      return this.m_columnMetadata;
    }
    
    public int[] getAggregateFnColumns()
    {
      return this.m_aggregateFnColumns;
    }
    
    public boolean isAggregateFnColumn(int paramInt)
    {
      return 0 <= this.m_aggregateFnIndexMap[paramInt];
    }
    
    public int mapColumnToAggregateIndex(int paramInt)
    {
      assert (isAggregateFnColumn(paramInt)) : ("" + paramInt);
      return this.m_aggregateFnIndexMap[paramInt];
    }
    
    public IAggregator createAggregator(int paramInt)
      throws ErrorException
    {
      return this.m_aggregatorFactories[paramInt].createAggregator();
    }
    
    public IColumn getAggregateFnMetadata(int paramInt)
    {
      return this.m_columnMetadata[this.m_aggregateFnColumns[paramInt]];
    }
    
    public int[] getAggregateFnArguments(int paramInt)
    {
      return (int[])this.m_aggregateFnArguments.get(paramInt);
    }
    
    public IColumn[] getAggregateFnArgumentMetadata(int paramInt)
    {
      return (IColumn[])this.m_aggregateFnArgumentMetadata.get(paramInt);
    }
    
    public int mapToOperandColumn(int paramInt)
    {
      assert (!isAggregateFnColumn(paramInt)) : ("" + paramInt);
      return this.m_operandColumnMap[paramInt];
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETHashAggregate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */