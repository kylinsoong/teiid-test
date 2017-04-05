package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class SumAggregatorFactory
  extends AbstractAggregatorFactory
{
  private final boolean m_isDistinct;
  
  public SumAggregatorFactory(IColumn[] paramArrayOfIColumn, IColumn paramIColumn, boolean paramBoolean)
  {
    super(paramArrayOfIColumn, paramIColumn);
    if (paramBoolean) {
      throw new UnsupportedOperationException("DISTINCT");
    }
    this.m_isDistinct = paramBoolean;
  }
  
  public boolean requiresDistinct()
  {
    return this.m_isDistinct;
  }
  
  public AbstractAggregator createAggregator()
  {
    switch (getInputMetadata(0).getTypeMetadata().getType())
    {
    case 2: 
    case 3: 
      return new SumExactNumAggregator(this);
    }
    return new SumDoubleAggregator(this);
  }
  
  private static class SumExactNumAggregator
    extends AbstractAggregator
  {
    private BigDecimal m_accumulator = BigDecimal.ZERO;
    private boolean m_hasSum = false;
    
    public SumExactNumAggregator(AbstractAggregatorFactory paramAbstractAggregatorFactory)
    {
      super();
    }
    
    public void load(byte[] paramArrayOfByte)
      throws ErrorException
    {
      try
      {
        ObjectInputStream localObjectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfByte));
        this.m_accumulator = ((BigDecimal)localObjectInputStream.readObject());
        this.m_hasSum = localObjectInputStream.readBoolean();
      }
      catch (Exception localException)
      {
        throw SQLEngineExceptionFactory.failedToReadData(localException);
      }
    }
    
    public byte[] serialize()
      throws ErrorException
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(32);
      try
      {
        ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
        try
        {
          localObjectOutputStream.writeObject(this.m_accumulator);
          localObjectOutputStream.writeBoolean(this.m_hasSum);
        }
        finally
        {
          localObjectOutputStream.close();
        }
      }
      catch (Exception localException)
      {
        throw SQLEngineExceptionFactory.failedToWriteData(localException);
      }
      return localByteArrayOutputStream.toByteArray();
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
      if (!this.m_hasSum) {
        localISqlDataWrapper.setNull();
      } else {
        localISqlDataWrapper.setExactNumber(this.m_accumulator);
      }
      return false;
    }
    
    public void reset()
    {
      this.m_accumulator = BigDecimal.ZERO;
      this.m_hasSum = false;
    }
    
    public void close()
    {
      this.m_accumulator = null;
    }
    
    protected void update()
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
      if (!localISqlDataWrapper.isNull())
      {
        this.m_accumulator = this.m_accumulator.add(localISqlDataWrapper.getExactNumber());
        this.m_hasSum = true;
      }
    }
    
    protected long getMemoryUsage()
    {
      return 1L + AbstractAggregatorFactory.BIGDECIMAL_SIZE;
    }
  }
  
  private static class SumDoubleAggregator
    extends AbstractAggregator
  {
    private double m_accumulator = 0.0D;
    private boolean m_hasSum = false;
    
    public SumDoubleAggregator(AbstractAggregatorFactory paramAbstractAggregatorFactory)
    {
      super();
    }
    
    public void load(byte[] paramArrayOfByte)
      throws ErrorException
    {
      try
      {
        ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
        this.m_accumulator = localByteBuffer.getDouble();
        this.m_hasSum = (0 != localByteBuffer.get());
      }
      catch (Exception localException)
      {
        throw SQLEngineExceptionFactory.failedToReadData(localException);
      }
    }
    
    public byte[] serialize()
    {
      ByteBuffer localByteBuffer = ByteBuffer.allocate(9);
      localByteBuffer.putDouble(this.m_accumulator);
      localByteBuffer.put((byte)(this.m_hasSum ? 1 : 0));
      return localByteBuffer.array();
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
      if (this.m_hasSum) {
        localISqlDataWrapper.setDouble(this.m_accumulator);
      } else {
        localISqlDataWrapper.setNull();
      }
      return false;
    }
    
    public void reset()
    {
      this.m_hasSum = false;
      this.m_accumulator = 0.0D;
    }
    
    public void close() {}
    
    protected void update()
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
      if (!localISqlDataWrapper.isNull())
      {
        switch (localISqlDataWrapper.getType())
        {
        case -7: 
        case 16: 
          if (localISqlDataWrapper.getBoolean()) {
            this.m_accumulator += 1.0D;
          }
          break;
        case -6: 
          this.m_accumulator += localISqlDataWrapper.getTinyInt();
          break;
        case 5: 
          this.m_accumulator += localISqlDataWrapper.getSmallInt();
          break;
        case 4: 
          this.m_accumulator += localISqlDataWrapper.getInteger();
          break;
        case -5: 
          this.m_accumulator += localISqlDataWrapper.getBigInt().doubleValue();
          break;
        case 7: 
          this.m_accumulator += localISqlDataWrapper.getReal();
          break;
        case 6: 
        case 8: 
          this.m_accumulator += localISqlDataWrapper.getDouble();
          break;
        case -4: 
        case -3: 
        case -2: 
        case -1: 
        case 0: 
        case 1: 
        case 2: 
        case 3: 
        case 9: 
        case 10: 
        case 11: 
        case 12: 
        case 13: 
        case 14: 
        case 15: 
        default: 
          throw new UnsupportedOperationException(getInputMetadata(0).getTypeMetadata().getTypeName());
        }
        this.m_hasSum = true;
      }
    }
    
    protected long getMemoryUsage()
    {
      return 9L;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/SumAggregatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */