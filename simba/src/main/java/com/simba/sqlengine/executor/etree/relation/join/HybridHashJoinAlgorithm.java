package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.conversions.ISqlConverter;
import com.simba.sqlengine.executor.datawrapper.DefaultSqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.LongDataStore;
import com.simba.sqlengine.executor.etree.temptable.RowComparator;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HybridHashJoinAlgorithm
  extends AbstractJoinAlgorithmAdaper
{
  private static final int BUFFER_SIZE = 8192;
  private LinkedList<Pair<Partition, Partition>> m_toJoin;
  private LinkedList<Pair<Partition, Partition>> m_joined;
  private Pair<Partition, Partition> m_repartionPair;
  private boolean m_isMasterOnLeft;
  private NBFallBackJoinAlgorithm m_fallback;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extAlgoProperties;
  private boolean m_needsReset;
  private ETConvertColRelation m_leftRelation;
  private ETConvertColRelation m_rightRelation;
  private HashJoinProperties m_property;
  private LongDataStore m_longDataStore;
  private long m_memoryAvailable;
  private IMemManagerAgent m_memAgent;
  private HasherFactory m_hsFactory;
  private RowComparator m_comparator;
  private int m_leftOverHead;
  private int m_rightOverHead;
  private boolean m_needMemory;
  private final ETCancelState m_cancelState;
  private boolean m_doneFirstPass = false;
  
  public HybridHashJoinAlgorithm(ETRelationalExpr paramETRelationalExpr1, ETRelationalExpr paramETRelationalExpr2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, AEJoin.AEJoinType paramAEJoinType, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, List<IColumn> paramList, List<ISqlConverter> paramList1, List<ISqlConverter> paramList2, ETCancelState paramETCancelState, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    super(paramAEJoinType);
    if (null != paramExternalAlgorithmProperties.getLogger()) {
      LogUtilities.logFunctionEntrance(paramExternalAlgorithmProperties.getLogger(), new Object[0]);
    }
    assert (paramArrayOfInt1.length == paramArrayOfInt2.length);
    for (int i = 0; i < paramArrayOfInt1.length; i++)
    {
      localObject1 = paramETRelationalExpr1.getColumn(paramArrayOfInt1[i]);
      localObject2 = paramETRelationalExpr2.getColumn(paramArrayOfInt2[i]);
      if ((ColumnSizeCalculator.isLongData((IColumn)localObject1, paramExternalAlgorithmProperties.getCellMemoryLimit())) || (ColumnSizeCalculator.isLongData((IColumn)localObject2, paramExternalAlgorithmProperties.getCellMemoryLimit()))) {
        throw SQLEngineExceptionFactory.joinOnLongData(paramArrayOfInt1[i]);
      }
    }
    this.m_hsFactory = new HasherFactory();
    this.m_memoryAvailable = 0L;
    this.m_extAlgoProperties = paramExternalAlgorithmProperties;
    this.m_needsReset = false;
    this.m_cancelState = paramETCancelState;
    this.m_needMemory = true;
    this.m_joined = new LinkedList();
    this.m_toJoin = new LinkedList();
    this.m_leftRelation = wrapInputRelation(paramETRelationalExpr1, paramArrayOfInt1, paramList, paramList1, Arrays.copyOfRange(paramArrayOfBoolean, 0, paramETRelationalExpr1.getColumnCount()));
    this.m_rightRelation = wrapInputRelation(paramETRelationalExpr2, paramArrayOfInt2, paramList, paramList2, Arrays.copyOfRange(paramArrayOfBoolean, paramETRelationalExpr1.getColumnCount(), paramArrayOfBoolean.length));
    IColumn[] arrayOfIColumn = new IColumn[this.m_leftRelation.getColumnCount()];
    Object localObject1 = new boolean[this.m_leftRelation.getColumnCount()];
    Object localObject2 = new IColumn[this.m_rightRelation.getColumnCount()];
    boolean[] arrayOfBoolean = new boolean[this.m_rightRelation.getColumnCount()];
    int j = 0;
    for (int k = 0; k < arrayOfIColumn.length; k++)
    {
      arrayOfIColumn[k] = this.m_leftRelation.getColumn(k);
      if ((j == 0) && (ColumnSizeCalculator.isLongData(arrayOfIColumn[k], paramExternalAlgorithmProperties.getCellMemoryLimit()))) {
        j = 1;
      }
      localObject1[k] = this.m_leftRelation.getDataNeeded(k);
    }
    for (k = 0; k < localObject2.length; k++)
    {
      localObject2[k] = this.m_rightRelation.getColumn(k);
      if ((j == 0) && (ColumnSizeCalculator.isLongData(localObject2[k], paramExternalAlgorithmProperties.getCellMemoryLimit()))) {
        j = 1;
      }
      arrayOfBoolean[k] = this.m_rightRelation.getDataNeeded(k);
    }
    this.m_longDataStore = null;
    if (j != 0) {
      this.m_longDataStore = new LongDataStore(paramExternalAlgorithmProperties.getStorageDir(), 8192L, paramExternalAlgorithmProperties.getLogger());
    }
    this.m_comparator = createHashColComparator(paramList);
    this.m_leftOverHead = new InMemTable(arrayOfIColumn, paramExternalAlgorithmProperties.getCellMemoryLimit(), -1, (boolean[])localObject1, paramExternalAlgorithmProperties.getLogger()).getMemOverhead();
    this.m_rightOverHead = new InMemTable((IColumn[])localObject2, paramExternalAlgorithmProperties.getCellMemoryLimit(), -1, arrayOfBoolean, paramExternalAlgorithmProperties.getLogger()).getMemOverhead();
    this.m_property = new HashJoinProperties(paramExternalAlgorithmProperties.getStorageDir(), paramExternalAlgorithmProperties.getCellMemoryLimit(), ExternalAlgorithmUtil.calculateRowSize(Arrays.asList((Object[])localObject2), arrayOfBoolean, paramExternalAlgorithmProperties.getCellMemoryLimit()), ExternalAlgorithmUtil.calculateRowSize(Arrays.asList(arrayOfIColumn), (boolean[])localObject1, paramExternalAlgorithmProperties.getCellMemoryLimit()), paramExternalAlgorithmProperties.getLogger(), this.m_joinType, new HasherFactory(), (boolean[])localObject1, arrayOfBoolean, arrayOfIColumn, (IColumn[])localObject2, paramArrayOfInt1, paramArrayOfInt2, (IColumn[])paramList.toArray(new IColumn[paramList.size()]));
  }
  
  public boolean isMasterJoinUnitOnLeft()
  {
    if (null != this.m_fallback) {
      return this.m_fallback.isMasterJoinUnitOnLeft();
    }
    return this.m_isMasterOnLeft;
  }
  
  public void closeRelations()
  {
    if (null != this.m_property.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_property.m_logger, new Object[0]);
    }
    if (this.m_fallback != null)
    {
      this.m_fallback.closeRelations();
      this.m_fallback = null;
    }
    this.m_leftRelation.close();
    this.m_rightRelation.close();
    destroyPartitions(this.m_joined);
    destroyPartitions(this.m_toJoin);
    if (null != this.m_longDataStore)
    {
      this.m_longDataStore.destroy();
      this.m_longDataStore = null;
    }
    this.m_memAgent.recycleMemory(this.m_memoryAvailable);
    this.m_memAgent.unregisterConsumer();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    if (null != this.m_property.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_property.m_logger, new Object[0]);
    }
    assert (CursorType.FORWARD_ONLY == paramCursorType);
    this.m_leftRelation.open(paramCursorType);
    this.m_rightRelation.open(paramCursorType);
    this.m_toJoin.clear();
    loadFirstPartition();
  }
  
  public void reset()
    throws ErrorException
  {
    if (null != this.m_property.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_property.m_logger, new Object[0]);
    }
    if (!this.m_needsReset)
    {
      this.m_needsReset = true;
      this.m_leftRelation.reset();
      this.m_rightRelation.reset();
      destroyPartitions(this.m_toJoin);
      this.m_toJoin.clear();
      loadFirstPartition();
    }
    else
    {
      if (this.m_repartionPair != null)
      {
        ((Partition)this.m_repartionPair.key()).finishExecution();
        ((Partition)this.m_repartionPair.value()).finishExecution();
      }
      disposeCurrentPartion();
      this.m_toJoin.addAll(this.m_joined);
      this.m_joined.clear();
      if (this.m_leftRelation.isOpen()) {
        this.m_leftRelation.close();
      }
      if (this.m_rightRelation.isOpen()) {
        this.m_rightRelation.close();
      }
    }
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_memAgent = paramIMemManagerAgent;
  }
  
  public long assign(long paramLong)
  {
    if (this.m_needMemory)
    {
      this.m_memoryAvailable += paramLong;
      return paramLong;
    }
    return 0L;
  }
  
  public long getRequiredMemory()
  {
    long l1 = this.m_property.m_leftRowSize < 8192L ? 8192L : this.m_property.m_leftRowSize;
    long l2 = this.m_property.m_rightRowSize < 8192L ? 8192L : this.m_property.m_rightRowSize;
    return this.m_leftOverHead + this.m_rightOverHead + 2L * (l1 + l2) + 8192L;
  }
  
  public Pair<? extends IJoinUnit, ? extends IJoinUnit> loadNextJoinUnit()
    throws ErrorException
  {
    if (null != this.m_fallback)
    {
      Pair localPair = this.m_fallback.loadNextJoinUnit();
      if (null != localPair) {
        return localPair;
      }
      this.m_fallback.closeRelations();
      this.m_fallback = null;
    }
    return super.loadNextJoinUnit();
  }
  
  public void match()
    throws ErrorException
  {
    if (null != this.m_fallback)
    {
      this.m_fallback.match();
      return;
    }
    super.match();
  }
  
  public boolean isOuterRow()
  {
    if (null != this.m_fallback) {
      return this.m_fallback.isOuterRow();
    }
    return super.isOuterRow();
  }
  
  public boolean moveMaster()
    throws ErrorException
  {
    if (null != this.m_fallback) {
      return this.m_fallback.moveMaster();
    }
    return super.moveMaster();
  }
  
  public boolean moveSlave()
    throws ErrorException
  {
    if (null != this.m_fallback) {
      return this.m_fallback.moveSlave();
    }
    return super.moveSlave();
  }
  
  public void seekSlave()
  {
    if (null != this.m_fallback) {
      this.m_fallback.seekSlave();
    } else {
      super.seekSlave();
    }
  }
  
  public Pair<ISlaveJoinUnit, IMasterJoinUnit> loadMasterSlave()
    throws ErrorException
  {
    disposeCurrentPartion();
    if (this.m_toJoin.size() == 0) {
      return null;
    }
    this.m_repartionPair = ((Pair)this.m_toJoin.poll());
    Partition localPartition1 = (Partition)this.m_repartionPair.key();
    Partition localPartition2 = (Partition)this.m_repartionPair.value();
    if ((localPartition1 == null) || (localPartition2 == null))
    {
      this.m_isMasterOnLeft = (localPartition2 == null);
      Partition localPartition3 = this.m_isMasterOnLeft ? localPartition1 : localPartition2;
      this.m_joined.add(this.m_repartionPair);
      this.m_repartionPair = null;
      localPartition3.initAsMaster(false, this.m_needsReset, this.m_memoryAvailable, null, computeSaveOuter(this.m_isMasterOnLeft), this.m_cancelState);
      return new Pair(new EmptySlaveUnit(null), localPartition3);
    }
    int i = requiredFitInMem(localPartition1, true) <= this.m_memoryAvailable ? 1 : 0;
    int j = requiredFitInMem(localPartition2, false) <= this.m_memoryAvailable ? 1 : 0;
    if (((i != 0) && (j == 0)) || ((i != 0) && (localPartition1.getSize() >= localPartition2.getSize())))
    {
      this.m_isMasterOnLeft = false;
      localPartition1.initAsSlave(false, this.m_needsReset, getSlaveMemory(true), localPartition2, this.m_comparator, this.m_cancelState);
      localPartition2.initAsMaster(false, this.m_needsReset, 0L, localPartition1, computeSaveOuter(this.m_isMasterOnLeft), this.m_cancelState);
      this.m_joined.add(this.m_repartionPair);
      this.m_repartionPair = null;
      return new Pair(localPartition1, localPartition2);
    }
    if (((j != 0) && (i == 0)) || ((j != 0) && (localPartition1.getSize() < localPartition2.getSize())))
    {
      this.m_isMasterOnLeft = true;
      localPartition2.initAsSlave(false, this.m_needsReset, getSlaveMemory(false), localPartition1, this.m_comparator, this.m_cancelState);
      localPartition1.initAsMaster(false, this.m_needsReset, 0L, localPartition2, computeSaveOuter(this.m_isMasterOnLeft), this.m_cancelState);
      this.m_joined.add(this.m_repartionPair);
      this.m_repartionPair = null;
      return new Pair(localPartition2, localPartition1);
    }
    long l = this.m_memoryAvailable - this.m_leftOverHead - this.m_rightOverHead - 8192L;
    if (localPartition1.canRepartition())
    {
      this.m_isMasterOnLeft = false;
      localPartition1.initAsSlave(true, this.m_needsReset, l - this.m_property.m_rightRowSize, localPartition2, this.m_comparator, this.m_cancelState);
      localPartition2.initAsMaster(true, this.m_needsReset, this.m_property.m_rightRowSize + localPartition1.takeExtraMem(), localPartition1, computeSaveOuter(this.m_isMasterOnLeft), this.m_cancelState);
      return new Pair(localPartition1, localPartition2);
    }
    if (localPartition2.canRepartition())
    {
      this.m_isMasterOnLeft = true;
      localPartition2.initAsSlave(true, this.m_needsReset, l - this.m_property.m_leftRowSize, localPartition1, this.m_comparator, this.m_cancelState);
      localPartition1.initAsMaster(true, this.m_needsReset, this.m_property.m_leftRowSize + localPartition2.takeExtraMem(), localPartition2, computeSaveOuter(this.m_isMasterOnLeft), this.m_cancelState);
      return new Pair(localPartition2, localPartition1);
    }
    this.m_fallback = createFallback(localPartition1, localPartition2);
    if (this.m_needsReset) {
      this.m_joined.add(this.m_repartionPair);
    }
    this.m_repartionPair = null;
    Pair localPair = this.m_fallback.loadNextJoinUnit();
    if (this.m_fallback.isMasterJoinUnitOnLeft()) {
      return new Pair((ISlaveJoinUnit)localPair.value(), (IMasterJoinUnit)localPair.key());
    }
    return new Pair((ISlaveJoinUnit)localPair.key(), (IMasterJoinUnit)localPair.value());
  }
  
  private void loadFirstPartition()
  {
    Partition localPartition1 = new Partition(new HHJoinDataSource(this.m_leftRelation, this.m_longDataStore, this.m_property.m_leftDataNeeded, this.m_property.m_leftMetadata, this.m_property.m_maxInMemDataLen), this.m_property.m_leftMetadata, this.m_property.m_leftHashColumns, this.m_property.m_hashColMeta, this.m_hsFactory, this.m_property.m_leftDataNeeded, this.m_extAlgoProperties, this.m_longDataStore);
    Partition localPartition2 = new Partition(new HHJoinDataSource(this.m_rightRelation, this.m_longDataStore, this.m_property.m_rightDataNeeded, this.m_property.m_rightMetadata, this.m_property.m_maxInMemDataLen), this.m_property.m_rightMetadata, this.m_property.m_rightHashColumns, this.m_property.m_hashColMeta, this.m_hsFactory, this.m_property.m_rightDataNeeded, this.m_extAlgoProperties, this.m_longDataStore);
    this.m_toJoin.push(new Pair(localPartition1, localPartition2));
  }
  
  private void destroyPartitions(List<Pair<Partition, Partition>> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Pair localPair = (Pair)localIterator.next();
      Partition localPartition1 = (Partition)localPair.key();
      Partition localPartition2 = (Partition)localPair.value();
      if (localPartition1 != null) {
        localPartition1.destroy();
      }
      if (localPartition2 != null) {
        localPartition2.destroy();
      }
    }
    paramList.clear();
  }
  
  private boolean computeSaveOuter(boolean paramBoolean)
  {
    return ((paramBoolean) && ((this.m_property.m_type == AEJoin.AEJoinType.FULL_OUTER_JOIN) || (this.m_property.m_type == AEJoin.AEJoinType.LEFT_OUTER_JOIN))) || ((!paramBoolean) && ((this.m_property.m_type == AEJoin.AEJoinType.FULL_OUTER_JOIN) || (this.m_property.m_type == AEJoin.AEJoinType.RIGHT_OUTER_JOIN)));
  }
  
  private void disposeCurrentPartion()
    throws ErrorException
  {
    if (this.m_repartionPair == null) {
      return;
    }
    Partition localPartition1 = (Partition)this.m_repartionPair.key();
    Partition localPartition2 = (Partition)this.m_repartionPair.value();
    if (localPartition1.isRepartion())
    {
      Map localMap = localPartition1.takeSubPartition();
      if (!this.m_doneFirstPass)
      {
        if ((null == localMap) || (localMap.isEmpty()))
        {
          long l2 = this.m_property.m_rightRowSize + 8192L + localPartition1.getMemUsage();
          recycleMemory(l2);
        }
        this.m_doneFirstPass = true;
      }
      mergePartition(localMap, localPartition2.takeSubPartition(), this.m_toJoin);
      if (this.m_needsReset) {
        mergePartition(localPartition1.takeProcessedPartition(), localPartition2.takeProcessedPartition(), this.m_joined);
      }
    }
    else if (this.m_needsReset)
    {
      this.m_joined.add(this.m_repartionPair);
    }
    if (this.m_needsReset)
    {
      long l1 = calculateRequiredMem();
      recycleMemory(l1);
    }
    this.m_repartionPair = null;
  }
  
  private void recycleMemory(long paramLong)
  {
    this.m_needMemory = (paramLong > this.m_memoryAvailable);
    if (paramLong < this.m_memoryAvailable)
    {
      this.m_memAgent.recycleMemory(this.m_memoryAvailable - paramLong);
      this.m_memoryAvailable = paramLong;
    }
  }
  
  private long calculateRequiredMem()
  {
    long l1 = 0L;
    Iterator localIterator = this.m_toJoin.iterator();
    Pair localPair;
    long l2;
    while (localIterator.hasNext())
    {
      localPair = (Pair)localIterator.next();
      l2 = Math.min(requiredFitInMem((Partition)localPair.key(), true), requiredFitInMem((Partition)localPair.value(), false));
      if (l2 > l1) {
        l1 = l2;
      }
    }
    localIterator = this.m_joined.iterator();
    while (localIterator.hasNext())
    {
      localPair = (Pair)localIterator.next();
      l2 = Math.min(requiredFitInMem((Partition)localPair.key(), true), requiredFitInMem((Partition)localPair.value(), false));
      if (l2 > l1) {
        l1 = l2;
      }
    }
    assert (l1 >= 0L);
    return l1;
  }
  
  private void mergePartition(Map<Long, Partition> paramMap1, Map<Long, Partition> paramMap2, LinkedList<Pair<Partition, Partition>> paramLinkedList)
  {
    boolean bool1 = computeSaveOuter(true);
    boolean bool2 = computeSaveOuter(false);
    Iterator localIterator = paramMap1.entrySet().iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      if (paramMap2.containsKey(((Map.Entry)localObject).getKey()))
      {
        paramLinkedList.add(new Pair(((Map.Entry)localObject).getValue(), paramMap2.get(((Map.Entry)localObject).getKey())));
        paramMap2.remove(((Map.Entry)localObject).getKey());
      }
      else if (bool1)
      {
        paramLinkedList.add(new Pair(((Map.Entry)localObject).getValue(), null));
      }
    }
    if (bool2)
    {
      localObject = paramMap2.values().iterator();
      while (((Iterator)localObject).hasNext())
      {
        Partition localPartition = (Partition)((Iterator)localObject).next();
        paramLinkedList.add(new Pair(null, localPartition));
      }
    }
  }
  
  private long requiredFitInMem(Partition paramPartition, boolean paramBoolean)
  {
    if (null == paramPartition)
    {
      l1 = paramBoolean ? this.m_property.m_rightRowSize : this.m_property.m_leftRowSize;
      return l1 + 8192L;
    }
    long l1 = this.m_property.m_leftRowSize + this.m_property.m_rightRowSize;
    long l2 = paramBoolean ? this.m_leftOverHead : this.m_rightOverHead;
    long l3 = paramPartition.getSize() + l2 + l1 + 8192L;
    return l3 >= 0L ? l3 : Long.MAX_VALUE;
  }
  
  private long getSlaveMemory(boolean paramBoolean)
  {
    long l1 = paramBoolean ? this.m_property.m_rightRowSize : this.m_property.m_leftRowSize;
    long l2 = paramBoolean ? this.m_leftOverHead : this.m_rightOverHead;
    return this.m_memoryAvailable - l2 - l1 - 8192L;
  }
  
  private NBFallBackJoinAlgorithm createFallback(Partition paramPartition1, Partition paramPartition2)
    throws ErrorException
  {
    if (null != this.m_property.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_property.m_logger, new Object[0]);
    }
    NBFallBackJoinAlgorithm localNBFallBackJoinAlgorithm = new NBFallBackJoinAlgorithm(paramPartition1.getDataSource(), paramPartition2.getDataSource(), this.m_joinType, this.m_cancelState, this.m_extAlgoProperties);
    for (int i = 0; i < this.m_property.m_leftHashColumns.length; i++) {
      localNBFallBackJoinAlgorithm.addFilter(this.m_property.m_leftHashColumns[i], this.m_property.m_rightHashColumns[i], AEComparisonType.EQUAL);
    }
    IMemManagerAgent local1 = new IMemManagerAgent()
    {
      public void recycleMemory(long paramAnonymousLong) {}
      
      public void unregisterConsumer() {}
      
      public long require(long paramAnonymousLong1, long paramAnonymousLong2)
      {
        return -1L;
      }
    };
    localNBFallBackJoinAlgorithm.registerManagerAgent(local1);
    long l1 = localNBFallBackJoinAlgorithm.getRequiredMemory();
    if (l1 > this.m_memoryAvailable)
    {
      long l2 = l1 - this.m_memoryAvailable;
      long l3 = this.m_memAgent.require(l2, l2);
      if (l3 <= 0L) {
        throw SQLEngineExceptionFactory.failedToAllocateMemory("Unknown :: Hybrid Hash Join fall back");
      }
      assert (l3 >= l2);
      this.m_memoryAvailable += l3;
    }
    localNBFallBackJoinAlgorithm.assign(this.m_memoryAvailable);
    localNBFallBackJoinAlgorithm.open(CursorType.FORWARD_ONLY);
    return localNBFallBackJoinAlgorithm;
  }
  
  private ETConvertColRelation wrapInputRelation(ETRelationalExpr paramETRelationalExpr, int[] paramArrayOfInt, List<IColumn> paramList, List<ISqlConverter> paramList1, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    ArrayList localArrayList1 = new ArrayList(paramETRelationalExpr.getColumnCount());
    for (int i = 0; i < paramETRelationalExpr.getColumnCount(); i++) {
      localArrayList1.add(Boolean.valueOf(paramETRelationalExpr.getDataNeeded(i)));
    }
    ArrayList localArrayList2 = new ArrayList(paramArrayOfInt.length);
    ArrayList localArrayList3 = new ArrayList(paramArrayOfInt.length);
    ArrayList localArrayList4 = new ArrayList(paramArrayOfInt.length);
    for (int j = 0; j < paramArrayOfInt.length; j++) {
      if (!DefaultSqlDataWrapper.isImplicitConvSupported(paramETRelationalExpr.getColumn(paramArrayOfInt[j]), (IColumn)paramList.get(j)))
      {
        localArrayList2.add(Integer.valueOf(paramArrayOfInt[j]));
        paramArrayOfInt[j] = (paramETRelationalExpr.getColumnCount() + localArrayList2.size() - 1);
        localArrayList3.add(paramList.get(j));
        ISqlConverter localISqlConverter = (ISqlConverter)paramList1.get(j);
        assert (localISqlConverter != null);
        localArrayList4.add(localISqlConverter);
        localArrayList1.add(Boolean.valueOf(true));
        if ((paramArrayOfInt[j] < paramETRelationalExpr.getColumnCount()) && (paramArrayOfBoolean[paramArrayOfInt[j]] == 0)) {
          localArrayList1.set(paramArrayOfInt[j], Boolean.valueOf(false));
        }
      }
    }
    boolean[] arrayOfBoolean = new boolean[localArrayList1.size()];
    for (int k = 0; k < arrayOfBoolean.length; k++) {
      arrayOfBoolean[k] = ((Boolean)localArrayList1.get(k)).booleanValue();
    }
    return new ETConvertColRelation(paramETRelationalExpr, (IColumn[])localArrayList3.toArray(new IColumn[localArrayList3.size()]), (Integer[])localArrayList2.toArray(new Integer[localArrayList2.size()]), (ISqlConverter[])localArrayList4.toArray(new ISqlConverter[localArrayList4.size()]), arrayOfBoolean);
  }
  
  private RowComparator createHashColComparator(List<IColumn> paramList)
    throws ErrorException
  {
    IColumn[] arrayOfIColumn = new IColumn[paramList.size()];
    ArrayList localArrayList = new ArrayList(paramList.size());
    for (int i = 0; i < arrayOfIColumn.length; i++)
    {
      arrayOfIColumn[i] = ((IColumn)paramList.get(i));
      localArrayList.add(RowComparator.createDefaultSortSpec(i));
    }
    return RowComparator.createComparator(arrayOfIColumn, localArrayList, RowComparator.getDefaultNullCollation());
  }
  
  static final class HashJoinProperties
  {
    public final IColumn[] m_hashColMeta;
    public final int m_maxInMemDataLen;
    public final long m_rightRowSize;
    public final long m_leftRowSize;
    public final File m_storageDir;
    public final ILogger m_logger;
    public final AEJoin.AEJoinType m_type;
    public final HasherFactory m_hasherFact;
    public final IColumn[] m_leftMetadata;
    public final IColumn[] m_rightMetadata;
    public final boolean[] m_leftDataNeeded;
    public final boolean[] m_rightDataNeeded;
    public final int[] m_leftHashColumns;
    public final int[] m_rightHashColumns;
    
    public HashJoinProperties(File paramFile, int paramInt, long paramLong1, long paramLong2, ILogger paramILogger, AEJoin.AEJoinType paramAEJoinType, HasherFactory paramHasherFactory, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, IColumn[] paramArrayOfIColumn1, IColumn[] paramArrayOfIColumn2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, IColumn[] paramArrayOfIColumn3)
    {
      this.m_storageDir = paramFile;
      this.m_maxInMemDataLen = paramInt;
      this.m_rightRowSize = paramLong1;
      this.m_leftRowSize = paramLong2;
      this.m_logger = paramILogger;
      this.m_type = paramAEJoinType;
      this.m_hasherFact = paramHasherFactory;
      this.m_leftDataNeeded = paramArrayOfBoolean1;
      this.m_rightDataNeeded = paramArrayOfBoolean2;
      this.m_leftMetadata = paramArrayOfIColumn1;
      this.m_rightMetadata = paramArrayOfIColumn2;
      this.m_leftHashColumns = paramArrayOfInt1;
      this.m_rightHashColumns = paramArrayOfInt2;
      this.m_hashColMeta = paramArrayOfIColumn3;
    }
  }
  
  private static final class EmptySlaveUnit
    implements ISlaveJoinUnit
  {
    public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      throw new IllegalStateException();
    }
    
    public boolean moveToNextRow()
    {
      return false;
    }
    
    public void close() {}
    
    public void seek(IRowView paramIRowView) {}
    
    public boolean moveOuter()
    {
      return false;
    }
    
    public void setOutputOuter() {}
    
    public boolean hasOuterRows()
    {
      return false;
    }
    
    public void match() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/HybridHashJoinAlgorithm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */