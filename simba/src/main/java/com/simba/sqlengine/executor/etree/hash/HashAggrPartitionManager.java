package com.simba.sqlengine.executor.etree.hash;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.OperandProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.join.HasherFactory;
import com.simba.sqlengine.executor.etree.relation.join.IHasher;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashAggrPartitionManager
{
  private List<HashAggrPartition> m_partitions;
  private IHasher m_hash;
  private HashPartitionProperties m_properties;
  private TemporaryFile m_longDataStore;
  private boolean[] m_longDataColumns;
  private IColumn[] m_scalarMetadata;
  
  public HashAggrPartitionManager(int paramInt, TemporaryFile paramTemporaryFile, boolean[] paramArrayOfBoolean1, HashPartitionProperties paramHashPartitionProperties, boolean[] paramArrayOfBoolean2)
    throws ErrorException
  {
    assert (paramInt >= 1);
    this.m_hash = paramHashPartitionProperties.getHashFactory().nextHasher(Long.MAX_VALUE);
    this.m_longDataStore = paramTemporaryFile;
    this.m_longDataColumns = paramArrayOfBoolean1;
    this.m_properties = paramHashPartitionProperties;
    int[] arrayOfInt = paramHashPartitionProperties.getOperandProjection().getScalarValueColumns();
    this.m_scalarMetadata = new IColumn[arrayOfInt.length];
    for (int i = 0; i < arrayOfInt.length; i++) {
      this.m_scalarMetadata[i] = paramHashPartitionProperties.getOperandProjection().getMetadata()[i];
    }
    createPartitions(paramInt);
  }
  
  private void createPartitions(int paramInt)
    throws ErrorException
  {
    IHasher localIHasher = this.m_properties.getHashFactory().nextHasher(Long.MAX_VALUE);
    boolean[] arrayOfBoolean = new boolean[this.m_scalarMetadata.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = true;
    }
    long l = Math.max(ExternalAlgorithmUtil.calculateRowSize(Arrays.asList(this.m_scalarMetadata), arrayOfBoolean, this.m_properties.getMaxDataLen()), 1L);
    ExternalAlgorithmUtil.ExternalAlgorithmProperties localExternalAlgorithmProperties = this.m_properties.getExternalAlgorithmProperties();
    int j = 1 + (int)(0.1D * localExternalAlgorithmProperties.getBlockSize() / l);
    ArrayList localArrayList = new ArrayList(paramInt);
    for (int k = 0; k < paramInt; k++)
    {
      InMemTable localInMemTable = new InMemTable(this.m_scalarMetadata, this.m_properties.getMaxDataLen(), j, arrayOfBoolean, this.m_properties.getLogger());
      localInMemTable.setMemLimit(Long.MAX_VALUE);
      localArrayList.add(new HashAggrPartition(localIHasher, this.m_longDataColumns, arrayOfBoolean, this.m_longDataStore, this.m_scalarMetadata, localInMemTable, this.m_properties));
    }
    this.m_partitions = localArrayList;
  }
  
  public int getNumPartitions()
  {
    return this.m_partitions.size();
  }
  
  public int partition(IRowView paramIRowView)
  {
    long l = this.m_hash.hash(paramIRowView, this.m_properties.getOperandProjection().getGroupingColumns());
    return (int)(l % this.m_partitions.size());
  }
  
  public HashAggrPartition getPartition(IRowView paramIRowView)
  {
    return getPartition(partition(paramIRowView));
  }
  
  public HashAggrPartition getPartition(int paramInt)
  {
    return (HashAggrPartition)this.m_partitions.get(paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/hash/HashAggrPartitionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */