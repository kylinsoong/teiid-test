package com.simba.sqlengine.executor.etree.hash;

import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.AggregateProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.OperandProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.join.HasherFactory;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.io.File;

public class HashPartitionProperties
{
  private ILogger m_logger;
  private ETHashAggregate.AggregateProjectionInfo m_aggregateProjection;
  private ETHashAggregate.OperandProjectionInfo m_operandProjection;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extAlgProps;
  private IRowBinaryPredicate m_predicate;
  private HasherFactory m_hashFactory;
  
  public HashPartitionProperties(HasherFactory paramHasherFactory, IRowBinaryPredicate paramIRowBinaryPredicate, ETHashAggregate.AggregateProjectionInfo paramAggregateProjectionInfo, ETHashAggregate.OperandProjectionInfo paramOperandProjectionInfo, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, ILogger paramILogger)
  {
    this.m_hashFactory = paramHasherFactory;
    this.m_aggregateProjection = paramAggregateProjectionInfo;
    this.m_operandProjection = paramOperandProjectionInfo;
    this.m_extAlgProps = paramExternalAlgorithmProperties;
    this.m_predicate = paramIRowBinaryPredicate;
    this.m_logger = paramILogger;
  }
  
  public IRowBinaryPredicate getEqualityPredicate()
  {
    return this.m_predicate;
  }
  
  public HasherFactory getHashFactory()
  {
    return this.m_hashFactory;
  }
  
  public ETHashAggregate.AggregateProjectionInfo getAggregateProjection()
  {
    return this.m_aggregateProjection;
  }
  
  public ETHashAggregate.OperandProjectionInfo getOperandProjection()
  {
    return this.m_operandProjection;
  }
  
  public ExternalAlgorithmUtil.ExternalAlgorithmProperties getExternalAlgorithmProperties()
  {
    return this.m_extAlgProps;
  }
  
  public ILogger getLogger()
  {
    return this.m_logger;
  }
  
  public File getStorageDir()
    throws ErrorException
  {
    return getExternalAlgorithmProperties().getStorageDir();
  }
  
  public int getMaxDataLen()
  {
    return getExternalAlgorithmProperties().getCellMemoryLimit();
  }
  
  public static Builder builder()
  {
    return new Builder(null);
  }
  
  public static class Builder
  {
    private ILogger m_logger;
    private ETHashAggregate.AggregateProjectionInfo m_aggregateProjection;
    private ETHashAggregate.OperandProjectionInfo m_operandProjection;
    private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extAlgProp;
    private IRowBinaryPredicate m_predicate;
    
    public Builder logger(ILogger paramILogger)
    {
      this.m_logger = paramILogger;
      return this;
    }
    
    public Builder aggregateProjection(ETHashAggregate.AggregateProjectionInfo paramAggregateProjectionInfo)
    {
      this.m_aggregateProjection = paramAggregateProjectionInfo;
      return this;
    }
    
    public Builder equalityPredicate(IRowBinaryPredicate paramIRowBinaryPredicate)
    {
      this.m_predicate = paramIRowBinaryPredicate;
      return this;
    }
    
    public Builder extAlgorithmProperties(ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties)
    {
      this.m_extAlgProp = paramExternalAlgorithmProperties;
      return this;
    }
    
    public Builder operandProjection(ETHashAggregate.OperandProjectionInfo paramOperandProjectionInfo)
    {
      this.m_operandProjection = paramOperandProjectionInfo;
      return this;
    }
    
    public HashPartitionProperties build()
      throws ErrorException
    {
      return new HashPartitionProperties(new HasherFactory(), this.m_predicate, this.m_aggregateProjection, this.m_operandProjection, this.m_extAlgProp, this.m_logger);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/hash/HashPartitionProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */