package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.CompressionUtil;
import com.simba.support.exceptions.ErrorException;
import java.nio.ByteBuffer;

public class CountAggregatorFactory
  extends AbstractAggregatorFactory
{
  private final boolean m_isCountStar;
  private final boolean m_isDistinct;
  
  public CountAggregatorFactory(IColumn[] paramArrayOfIColumn, IColumn paramIColumn, boolean paramBoolean)
  {
    super(paramArrayOfIColumn, paramIColumn);
    if (paramBoolean) {
      throw new UnsupportedOperationException("DISTINCT");
    }
    this.m_isCountStar = (0 == paramArrayOfIColumn.length);
    this.m_isDistinct = paramBoolean;
  }
  
  public boolean requiresDistinct()
  {
    return (!this.m_isCountStar) && (this.m_isDistinct);
  }
  
  public CountAggregator createAggregator()
  {
    return this.m_isCountStar ? new CountStarAggregator(this) : new CountAggregator(this);
  }
  
  private static final class CountStarAggregator
    extends CountAggregatorFactory.CountAggregator
  {
    public CountStarAggregator(CountAggregatorFactory paramCountAggregatorFactory)
    {
      super();
    }
    
    protected boolean shouldUpdate()
      throws ErrorException
    {
      return true;
    }
  }
  
  private static class CountAggregator
    extends AbstractAggregator
  {
    private long m_count = 0L;
    
    public CountAggregator(CountAggregatorFactory paramCountAggregatorFactory)
    {
      super();
    }
    
    public void load(byte[] paramArrayOfByte)
      throws ErrorException
    {
      try
      {
        this.m_count = ByteBuffer.wrap(paramArrayOfByte).getLong();
      }
      catch (Exception localException)
      {
        throw SQLEngineExceptionFactory.failedToReadData(localException);
      }
    }
    
    public byte[] serialize()
    {
      return ByteBuffer.allocate(8).putLong(this.m_count).array();
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      paramETDataRequest.getData().setBigInt(CompressionUtil.getlongAsBigInteger(this.m_count, false));
      return false;
    }
    
    protected void update()
      throws ErrorException
    {
      if (shouldUpdate()) {
        this.m_count += 1L;
      }
    }
    
    protected boolean shouldUpdate()
      throws ErrorException
    {
      return !getArgumentData(0, 0L, 0L).isNull();
    }
    
    protected long getMemoryUsage()
    {
      return 8L;
    }
    
    public void reset()
    {
      this.m_count = 0L;
    }
    
    public void close() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/CountAggregatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */