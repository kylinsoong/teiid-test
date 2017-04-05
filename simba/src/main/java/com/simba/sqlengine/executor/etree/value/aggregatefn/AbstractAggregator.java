package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator.JavaSize;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

abstract class AbstractAggregator
  implements IAggregator
{
  protected IWarningListener m_listener = null;
  private final AbstractAggregatorFactory m_factory;
  private IAggregator.IUpdateParameters m_params;
  
  protected AbstractAggregator(AbstractAggregatorFactory paramAbstractAggregatorFactory)
  {
    this.m_factory = paramAbstractAggregatorFactory;
  }
  
  public final void update(IAggregator.IUpdateParameters paramIUpdateParameters)
    throws ErrorException
  {
    this.m_params = paramIUpdateParameters;
    update();
  }
  
  public final long getMemorySize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectShellSize() + 2 * paramJavaSize.getObjectRefSize() + getMemoryUsage();
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_listener = paramIWarningListener;
  }
  
  protected abstract void update()
    throws ErrorException;
  
  protected abstract long getMemoryUsage();
  
  protected ISqlDataWrapper getArgumentData(int paramInt)
    throws ErrorException
  {
    return this.m_params.getData(paramInt);
  }
  
  protected ISqlDataWrapper getArgumentData(int paramInt, long paramLong1, long paramLong2)
    throws ErrorException
  {
    return this.m_params.getData(paramInt, paramLong1, paramLong2);
  }
  
  protected IColumn getInputMetadata(int paramInt)
  {
    return this.m_factory.getInputMetadata(paramInt);
  }
  
  protected IColumn getResultMetadata()
  {
    return this.m_factory.getResultMetadata();
  }
  
  protected AbstractAggregatorFactory getFactory()
  {
    return this.m_factory;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/AbstractAggregator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */