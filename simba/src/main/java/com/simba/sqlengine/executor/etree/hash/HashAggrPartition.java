package com.simba.sqlengine.executor.etree.hash;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.AggregateProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.OperandProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.join.IHasher;
import com.simba.sqlengine.executor.etree.relation.join.RowFile;
import com.simba.sqlengine.executor.etree.temptable.DataStore;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.IndexRowView;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTableBuilder.TemporaryTableProperties;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregator;
import com.simba.sqlengine.executor.etree.value.aggregatefn.RowViewUpdateParameters;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HashAggrPartition
{
  private IHasher m_hash;
  private ETHashAggregate.AggregateProjectionInfo m_aggregateProjection;
  private ETHashAggregate.OperandProjectionInfo m_operandProjection;
  private boolean m_hasFlushedRows = false;
  private boolean m_isFinishedRetrieving = false;
  private OperationMode m_operationMode = OperationMode.WRITE;
  private int m_lastRow = -1;
  private int m_currentRow = -1;
  private InMemTable m_memTable;
  private IColumn[] m_memTableMeta;
  private TemporaryFile m_longDataStore;
  private boolean[] m_longDataColumns;
  private DataStore m_spilledPartial;
  private RowFile m_spilledInput;
  private long m_memorySize;
  private HashMap<HashedRowView, PartialAggregationRow> m_hashMap;
  private ArrayList<PartialAggregationRow> m_partialList;
  private IRowBinaryPredicate m_predicate;
  private HashPartitionProperties m_properties;
  private boolean[] m_dataNeeded;
  
  HashAggrPartition(IHasher paramIHasher, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, TemporaryFile paramTemporaryFile, IColumn[] paramArrayOfIColumn, InMemTable paramInMemTable, HashPartitionProperties paramHashPartitionProperties)
  {
    this.m_hash = paramIHasher;
    this.m_predicate = paramHashPartitionProperties.getEqualityPredicate();
    this.m_hashMap = new HashMap();
    this.m_partialList = new ArrayList();
    this.m_aggregateProjection = paramHashPartitionProperties.getAggregateProjection();
    this.m_operandProjection = paramHashPartitionProperties.getOperandProjection();
    this.m_memTable = paramInMemTable;
    this.m_memTableMeta = paramArrayOfIColumn;
    this.m_longDataColumns = paramArrayOfBoolean1;
    this.m_longDataStore = paramTemporaryFile;
    this.m_properties = paramHashPartitionProperties;
    this.m_dataNeeded = paramArrayOfBoolean2;
  }
  
  public void addPartialAggregation(IRowView paramIRowView)
    throws ErrorException
  {
    if (OperationMode.WRITE != this.m_operationMode) {
      throw new IllegalStateException();
    }
    int i = appendScalarValues(paramIRowView);
    int j = this.m_memTableMeta.length;
    int k = this.m_aggregateProjection.getAggregateFnColumns().length;
    Object localObject;
    if (0 < k)
    {
      localObject = new ArrayList(k);
      for (int m = 0; m < k; m++)
      {
        IAggregator localIAggregator = this.m_aggregateProjection.createAggregator(m);
        localIAggregator.load(paramIRowView.getBytes(j + m));
        ((List)localObject).add(localIAggregator);
        this.m_memorySize += localIAggregator.getMemorySize(ColumnSizeCalculator.JAVA_SIZE);
      }
    }
    else
    {
      localObject = Collections.emptyList();
    }
    PartialAggregationRow localPartialAggregationRow = new PartialAggregationRow(this.m_memTable, i, (List)localObject);
    this.m_hashMap.put(new HashedRowView(hash(localPartialAggregationRow.getRow()), localPartialAggregationRow.getRow(), this.m_predicate), localPartialAggregationRow);
    this.m_partialList.add(localPartialAggregationRow);
  }
  
  public long getMemoryUsage()
  {
    return this.m_memTable.getMemUsage() + this.m_memorySize;
  }
  
  public boolean hasFlushedRows()
  {
    return this.m_hasFlushedRows;
  }
  
  public boolean isFinishedRetrieving()
  {
    return this.m_isFinishedRetrieving;
  }
  
  public boolean moveToNextRow()
  {
    if ((OperationMode.RETRIEVE != this.m_operationMode) && (OperationMode.WRITE != this.m_operationMode)) {
      throw new IllegalStateException("" + this.m_operationMode);
    }
    this.m_operationMode = OperationMode.RETRIEVE;
    if (!this.m_isFinishedRetrieving)
    {
      this.m_currentRow += 1;
      if (0 <= this.m_lastRow)
      {
        PartialAggregationRow localPartialAggregationRow = (PartialAggregationRow)this.m_partialList.get(this.m_lastRow);
        this.m_memTable.removeRow(localPartialAggregationRow.getIndex());
      }
      this.m_lastRow = this.m_currentRow;
      if (this.m_currentRow < this.m_partialList.size()) {
        return true;
      }
      this.m_currentRow = -1;
    }
    this.m_isFinishedRetrieving = true;
    return false;
  }
  
  public boolean retrieveAggregate(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (OperationMode.RETRIEVE == this.m_operationMode);
    IAggregator localIAggregator = (IAggregator)((PartialAggregationRow)this.m_partialList.get(this.m_currentRow)).getAggregators().get(paramInt);
    return localIAggregator.retrieveData(paramETDataRequest);
  }
  
  public boolean retrieveScalar(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (OperationMode.RETRIEVE == this.m_operationMode);
    IRowView localIRowView = ((PartialAggregationRow)this.m_partialList.get(this.m_currentRow)).getRow();
    return DataRetrievalUtil.retrieveFromRowView(paramInt, this.m_longDataColumns[paramInt], paramETDataRequest, localIRowView, this.m_longDataStore);
  }
  
  public void spillAllRows(long paramLong)
    throws ErrorException
  {
    while (!this.m_partialList.isEmpty()) {
      spillPartialRows(paramLong, 0);
    }
  }
  
  public void spillPartialRows(long paramLong, int paramInt)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList(Arrays.asList(this.m_memTableMeta));
    int i = this.m_aggregateProjection.getAggregateFnColumns().length;
    if (0 < i) {
      localArrayList.addAll(Collections.nCopies(i, new ColumnMetadata(TypeMetadata.createTypeMetadata(-2))));
    }
    boolean[] arrayOfBoolean = new boolean[localArrayList.size()];
    for (int j = 0; j < arrayOfBoolean.length; j++) {
      arrayOfBoolean[j] = true;
    }
    long l1 = ExternalAlgorithmUtil.calculateRowSize(localArrayList, arrayOfBoolean, this.m_properties.getMaxDataLen());
    l1 = Math.max(l1, 1L);
    int k = 1 + (int)(paramLong * 0.5D / l1);
    if (null == this.m_spilledPartial)
    {
      localObject = new TemporaryTableBuilder.TemporaryTableProperties(this.m_properties.getStorageDir(), this.m_properties.getMaxDataLen(), this.m_properties.getExternalAlgorithmProperties().getBlockSize(), k, this.m_properties.getExternalAlgorithmProperties().getMaxNumOpenFiles(), this.m_properties.getLogger(), "ETHashPartition");
      this.m_spilledPartial = new DataStore((TemporaryTableBuilder.TemporaryTableProperties)localObject, (IColumn[])localArrayList.toArray(new IColumn[0]));
    }
    Object localObject = new InMemTable((IColumn[])localArrayList.toArray(new IColumn[0]), this.m_properties.getMaxDataLen(), k, arrayOfBoolean, null);
    ((InMemTable)localObject).setMemLimit(Long.MAX_VALUE);
    long l2 = Math.max(getMemoryUsage() - paramLong, 0L);
    int m;
    while ((this.m_partialList.size() > paramInt) && (getMemoryUsage() > l2) && (0 <= (m = ((InMemTable)localObject).appendRow())))
    {
      PartialAggregationRow localPartialAggregationRow = (PartialAggregationRow)this.m_partialList.remove(this.m_partialList.size() - 1);
      this.m_hashMap.remove(new HashedRowView(hash(localPartialAggregationRow.getRow()), localPartialAggregationRow.getRow(), this.m_predicate));
      copyRow(this.m_memTable, localPartialAggregationRow.getIndex(), (InMemTable)localObject, m, this.m_memTableMeta, this.m_longDataColumns);
      for (int n = 0; n < i; n++)
      {
        IAggregator localIAggregator = (IAggregator)localPartialAggregationRow.getAggregators().get(n);
        this.m_memorySize -= localIAggregator.getMemorySize(ColumnSizeCalculator.JAVA_SIZE);
        ((InMemTable)localObject).setBytes(m, this.m_memTableMeta.length + n, localIAggregator.serialize());
      }
      this.m_memTable.removeRow(localPartialAggregationRow.getIndex());
    }
    this.m_spilledPartial.put(((InMemTable)localObject).toRowBlock());
    this.m_hasFlushedRows = true;
  }
  
  public DataStore unspillPartial()
  {
    this.m_operationMode = OperationMode.UNSPILL;
    DataStore localDataStore = this.m_spilledPartial;
    this.m_spilledPartial = null;
    return localDataStore;
  }
  
  public RowFile unspillInputRows()
  {
    this.m_operationMode = OperationMode.UNSPILL;
    RowFile localRowFile = this.m_spilledInput;
    this.m_spilledInput = null;
    return localRowFile;
  }
  
  public void update(IRowView paramIRowView)
    throws ErrorException
  {
    if (OperationMode.WRITE != this.m_operationMode) {
      throw new IllegalStateException("" + this.m_operationMode);
    }
    HashedRowView localHashedRowView = new HashedRowView(hash(paramIRowView), paramIRowView, this.m_predicate);
    if (!findAndUpdate(localHashedRowView)) {
      if (!hasFlushedRows())
      {
        PartialAggregationRow localPartialAggregationRow = createPartialAggregation(paramIRowView);
        this.m_hashMap.put(new HashedRowView(localHashedRowView.hashCode(), localPartialAggregationRow.getRow(), this.m_predicate), localPartialAggregationRow);
        this.m_partialList.add(localPartialAggregationRow);
      }
      else
      {
        spillInputRow(paramIRowView);
      }
    }
  }
  
  private int appendScalarValues(IRowView paramIRowView)
  {
    int i = this.m_memTable.appendRow();
    assert (0 <= i);
    int[] arrayOfInt = this.m_operandProjection.getScalarValueColumns();
    for (int j = 0; j < this.m_memTableMeta.length; j++)
    {
      TypeMetadata localTypeMetadata = this.m_memTableMeta[j].getTypeMetadata();
      int k = arrayOfInt[j];
      if (paramIRowView.isNull(k)) {
        this.m_memTable.setNull(i, j);
      } else {
        switch (localTypeMetadata.getType())
        {
        case -7: 
        case 16: 
          this.m_memTable.setBoolean(i, j, paramIRowView.getBoolean(k));
          break;
        case 4: 
          this.m_memTable.setInteger(i, j, paramIRowView.getInteger(k));
          break;
        case -10: 
        case -9: 
        case -8: 
        case -1: 
        case 1: 
        case 12: 
          if (this.m_longDataColumns[k] != 0) {
            this.m_memTable.setFileMarker(i, j, paramIRowView.getFileMarker(k));
          } else {
            this.m_memTable.setString(i, j, paramIRowView.getString(k));
          }
          break;
        case -6: 
        case -5: 
        case -4: 
        case -3: 
        case -2: 
        case 0: 
        case 2: 
        case 3: 
        case 5: 
        case 6: 
        case 7: 
        case 8: 
        case 9: 
        case 10: 
        case 11: 
        case 13: 
        case 14: 
        case 15: 
        default: 
          throw new UnsupportedOperationException(localTypeMetadata.getTypeName());
        }
      }
    }
    return i;
  }
  
  private PartialAggregationRow createPartialAggregation(IRowView paramIRowView)
    throws ErrorException
  {
    int i = this.m_aggregateProjection.getAggregateFnColumns().length;
    Object localObject;
    if (0 < i)
    {
      localObject = new ArrayList(i);
      for (int j = 0; j < i; j++)
      {
        IAggregator localIAggregator = this.m_aggregateProjection.createAggregator(j);
        localIAggregator.update(createUpdateParams(j, paramIRowView));
        ((List)localObject).add(localIAggregator);
        this.m_memorySize += localIAggregator.getMemorySize(ColumnSizeCalculator.JAVA_SIZE);
      }
    }
    else
    {
      localObject = Collections.emptyList();
    }
    return new PartialAggregationRow(this.m_memTable, appendScalarValues(paramIRowView), (List)localObject);
  }
  
  private RowViewUpdateParameters createUpdateParams(int paramInt, IRowView paramIRowView)
  {
    IColumn[] arrayOfIColumn = this.m_aggregateProjection.getAggregateFnArgumentMetadata(paramInt);
    int[] arrayOfInt = this.m_aggregateProjection.getAggregateFnArguments(paramInt);
    boolean[] arrayOfBoolean = new boolean[arrayOfInt.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = this.m_longDataColumns[arrayOfInt[i]];
    }
    return new RowViewUpdateParameters(new RenumberedRowView(paramIRowView, arrayOfInt), arrayOfIColumn, arrayOfBoolean);
  }
  
  private boolean findAndUpdate(HashedRowView paramHashedRowView)
    throws ErrorException
  {
    PartialAggregationRow localPartialAggregationRow = (PartialAggregationRow)this.m_hashMap.get(paramHashedRowView);
    if (null != localPartialAggregationRow)
    {
      List localList = localPartialAggregationRow.getAggregators();
      int i = 0;
      int j = localList.size();
      while (i < j)
      {
        ((IAggregator)localList.get(i)).update(createUpdateParams(i, paramHashedRowView.getRowView()));
        i++;
      }
      return true;
    }
    return false;
  }
  
  private long hash(IRowView paramIRowView)
  {
    return this.m_hash.hash(paramIRowView, this.m_operandProjection.getGroupingColumns());
  }
  
  private void spillInputRow(IRowView paramIRowView)
    throws ErrorException
  {
    if (null == this.m_spilledInput) {
      this.m_spilledInput = new RowFile(this.m_operandProjection.getMetadata(), this.m_properties.getStorageDir(), this.m_properties.getLogger(), this.m_properties.getMaxDataLen(), this.m_dataNeeded);
    }
  }
  
  private static void copyRow(InMemTable paramInMemTable1, int paramInt1, InMemTable paramInMemTable2, int paramInt2, IColumn[] paramArrayOfIColumn, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    for (int i = 0; i < paramArrayOfIColumn.length; i++) {
      if (paramInMemTable1.isNull(paramInt1, i)) {
        paramInMemTable2.setNull(paramInt2, i);
      } else {
        switch (paramArrayOfIColumn[i].getTypeMetadata().getType())
        {
        case 4: 
          break;
        case -10: 
        case -9: 
        case -8: 
        case -1: 
        case 1: 
        case 12: 
          if (paramArrayOfBoolean[i] != 0) {
            paramInMemTable2.setFileMarker(paramInt2, i, paramInMemTable1.getFileMarker(paramInt1, i));
          } else {
            paramInMemTable2.setString(paramInt2, i, paramInMemTable1.getString(paramInt1, i));
          }
          break;
        case -7: 
        case -6: 
        case -5: 
        case -4: 
        case -3: 
        case -2: 
        case 0: 
        case 2: 
        case 3: 
        case 5: 
        case 6: 
        case 7: 
        case 8: 
        case 9: 
        case 10: 
        case 11: 
        default: 
          throw SQLEngineExceptionFactory.featureNotImplementedException(paramArrayOfIColumn[i].getTypeMetadata().getTypeName());
        }
      }
    }
  }
  
  private static final class RenumberedRowView
    implements IRowView
  {
    private final IRowView m_rowView;
    private final int[] m_indices;
    
    public RenumberedRowView(IRowView paramIRowView, int[] paramArrayOfInt)
    {
      this.m_rowView = paramIRowView;
      this.m_indices = paramArrayOfInt;
    }
    
    public boolean isNull(int paramInt)
    {
      return this.m_rowView.isNull(this.m_indices[paramInt]);
    }
    
    public long getBigInt(int paramInt)
    {
      return this.m_rowView.getBigInt(this.m_indices[paramInt]);
    }
    
    public BigDecimal getExactNumber(int paramInt)
    {
      return this.m_rowView.getExactNumber(this.m_indices[paramInt]);
    }
    
    public double getDouble(int paramInt)
    {
      return this.m_rowView.getDouble(this.m_indices[paramInt]);
    }
    
    public TemporaryFile.FileMarker getFileMarker(int paramInt)
    {
      return this.m_rowView.getFileMarker(this.m_indices[paramInt]);
    }
    
    public float getReal(int paramInt)
    {
      return this.m_rowView.getReal(this.m_indices[paramInt]);
    }
    
    public boolean getBoolean(int paramInt)
    {
      return this.m_rowView.getBoolean(this.m_indices[paramInt]);
    }
    
    public String getString(int paramInt)
    {
      return this.m_rowView.getString(this.m_indices[paramInt]);
    }
    
    public Date getDate(int paramInt)
    {
      return this.m_rowView.getDate(this.m_indices[paramInt]);
    }
    
    public Time getTime(int paramInt)
    {
      return this.m_rowView.getTime(this.m_indices[paramInt]);
    }
    
    public Timestamp getTimestamp(int paramInt)
    {
      return this.m_rowView.getTimestamp(this.m_indices[paramInt]);
    }
    
    public UUID getGuid(int paramInt)
    {
      return this.m_rowView.getGuid(this.m_indices[paramInt]);
    }
    
    public int getInteger(int paramInt)
    {
      return this.m_rowView.getInteger(this.m_indices[paramInt]);
    }
    
    public short getSmallInt(int paramInt)
    {
      return this.m_rowView.getSmallInt(this.m_indices[paramInt]);
    }
    
    public byte getTinyInt(int paramInt)
    {
      return this.m_rowView.getTinyInt(this.m_indices[paramInt]);
    }
    
    public byte[] getBytes(int paramInt)
    {
      return this.m_rowView.getBytes(this.m_indices[paramInt]);
    }
    
    public IColumn getColumn(int paramInt)
    {
      return this.m_rowView.getColumn(this.m_indices[paramInt]);
    }
  }
  
  private static final class PartialAggregationRow
  {
    private int m_rowIndex;
    private IndexRowView m_rowView;
    private List<IAggregator> m_aggregators;
    
    public PartialAggregationRow(InMemTable paramInMemTable, int paramInt, List<IAggregator> paramList)
    {
      this.m_rowIndex = paramInt;
      this.m_rowView = new IndexRowView(paramInMemTable);
      this.m_rowView.setRowNum(paramInt);
      this.m_aggregators = paramList;
    }
    
    public int getIndex()
    {
      return this.m_rowIndex;
    }
    
    public IRowView getRow()
    {
      return this.m_rowView;
    }
    
    public List<IAggregator> getAggregators()
    {
      return this.m_aggregators;
    }
  }
  
  private static final class HashedRowView
  {
    private final long m_hashCode;
    private IRowView m_rowView;
    private final IRowBinaryPredicate m_predicate;
    
    public HashedRowView(long paramLong, IRowView paramIRowView, IRowBinaryPredicate paramIRowBinaryPredicate)
    {
      this.m_hashCode = paramLong;
      this.m_rowView = paramIRowView;
      this.m_predicate = paramIRowBinaryPredicate;
    }
    
    public IRowView getRowView()
    {
      return this.m_rowView;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof HashedRowView)) {
        return false;
      }
      HashedRowView localHashedRowView = (HashedRowView)paramObject;
      return (this.m_hashCode == localHashedRowView.m_hashCode) && (this.m_predicate.apply(this.m_rowView, localHashedRowView.m_rowView));
    }
    
    public int hashCode()
    {
      return (int)(this.m_hashCode ^ this.m_hashCode >>> 32);
    }
  }
  
  private static enum OperationMode
  {
    WRITE,  RETRIEVE,  UNSPILL;
    
    private OperationMode() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/hash/HashAggrPartition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */