package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.temptable.column.ExactNumColumnSlice;
import com.simba.support.exceptions.ErrorException;

public abstract class AbstractAggregatorFactory
  implements IAggregatorFactory
{
  protected static final long BIGDECIMAL_SIZE = new Double(ExactNumColumnSlice.estimateRowSize(ColumnSizeCalculator.JAVA_SIZE)).longValue();
  private final IColumn[] m_inputMetadata;
  private final IColumn m_resultMetadata;
  
  protected AbstractAggregatorFactory(IColumn[] paramArrayOfIColumn, IColumn paramIColumn)
  {
    this.m_inputMetadata = paramArrayOfIColumn;
    this.m_resultMetadata = paramIColumn;
  }
  
  public abstract AbstractAggregator createAggregator()
    throws ErrorException;
  
  protected final IColumn getInputMetadata(int paramInt)
  {
    return this.m_inputMetadata[paramInt];
  }
  
  protected final IColumn getResultMetadata()
  {
    return this.m_resultMetadata;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/AbstractAggregatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */