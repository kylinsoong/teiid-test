package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.CompressionUtil;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.conv.IntegralConverter;
import com.simba.support.exceptions.ErrorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

public class AvgAggregatorFactory
  extends AbstractAggregatorFactory
{
  private boolean m_isDistinct;
  
  public AvgAggregatorFactory(IColumn[] paramArrayOfIColumn, IColumn paramIColumn, boolean paramBoolean)
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
    int i = getInputMetadata(0).getTypeMetadata().getType();
    switch (i)
    {
    case 2: 
    case 3: 
      return new AvgExactNumAggregator(this);
    }
    assert ((TypeUtilities.isNumberType(i)) || (-7 == i));
    return new AvgDoubleAggregator(this);
  }
  
  private static class AvgExactNumAggregator
    extends AbstractAggregator
  {
    private BigDecimal m_sum = BigDecimal.ZERO;
    private long m_count;
    
    protected AvgExactNumAggregator(AvgAggregatorFactory paramAvgAggregatorFactory)
    {
      super();
    }
    
    public void load(byte[] paramArrayOfByte)
      throws ErrorException
    {
      try
      {
        ObjectInputStream localObjectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfByte));
        this.m_sum = ((BigDecimal)localObjectInputStream.readObject());
        this.m_count = localObjectInputStream.readLong();
      }
      catch (Exception localException)
      {
        throw SQLEngineExceptionFactory.failedToReadData(localException);
      }
    }
    
    public byte[] serialize()
      throws ErrorException
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(128);
      try
      {
        ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
        try
        {
          localObjectOutputStream.writeObject(this.m_sum);
          localObjectOutputStream.writeLong(this.m_count);
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
    
    public void reset()
    {
      this.m_sum = BigDecimal.ZERO;
      this.m_count = 0L;
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
      if (0L == this.m_count)
      {
        localISqlDataWrapper.setNull();
      }
      else if (0L < this.m_count)
      {
        localISqlDataWrapper.setExactNumber(this.m_sum.divide(BigDecimal.valueOf(this.m_count), RoundingMode.DOWN));
      }
      else
      {
        BigInteger localBigInteger = CompressionUtil.getlongAsBigInteger(this.m_count, false);
        localISqlDataWrapper.setExactNumber(this.m_sum.divide(new BigDecimal(localBigInteger), RoundingMode.DOWN));
      }
      return false;
    }
    
    public void close()
    {
      this.m_sum = null;
    }
    
    protected void update()
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
      if (!localISqlDataWrapper.isNull())
      {
        this.m_count += 1L;
        switch (localISqlDataWrapper.getType())
        {
        case 2: 
        case 3: 
          this.m_sum = this.m_sum.add(localISqlDataWrapper.getExactNumber());
          break;
        default: 
          throw new UnsupportedOperationException(getInputMetadata(0).getTypeMetadata().getTypeName());
        }
      }
    }
    
    protected long getMemoryUsage()
    {
      return AbstractAggregatorFactory.BIGDECIMAL_SIZE + 8L;
    }
  }
  
  private static class AvgDoubleAggregator
    extends AbstractAggregator
  {
    private double m_sum = 0.0D;
    private long m_count = 0L;
    
    protected AvgDoubleAggregator(AvgAggregatorFactory paramAvgAggregatorFactory)
    {
      super();
    }
    
    public void load(byte[] paramArrayOfByte)
      throws ErrorException
    {
      try
      {
        ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
        this.m_sum = localByteBuffer.getDouble();
        this.m_count = localByteBuffer.getLong();
      }
      catch (Exception localException)
      {
        throw SQLEngineExceptionFactory.failedToReadData(localException);
      }
    }
    
    public byte[] serialize()
    {
      return ByteBuffer.allocate(16).putDouble(this.m_sum).putLong(this.m_count).array();
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
      if (0L == this.m_count) {
        localISqlDataWrapper.setNull();
      } else {
        localISqlDataWrapper.setDouble(this.m_sum / this.m_count);
      }
      return false;
    }
    
    public void reset()
    {
      this.m_sum = 0.0D;
      this.m_count = 0L;
    }
    
    public void close() {}
    
    protected void update()
      throws ErrorException
    {
      ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
      if (!localISqlDataWrapper.isNull())
      {
        this.m_count += 1L;
        switch (localISqlDataWrapper.getType())
        {
        case -7: 
        case 16: 
          if (localISqlDataWrapper.getBoolean()) {
            this.m_sum += 1.0D;
          }
          break;
        case -6: 
          this.m_sum += localISqlDataWrapper.getTinyInt();
          break;
        case 5: 
          this.m_sum += localISqlDataWrapper.getSmallInt();
          break;
        case 4: 
          this.m_sum += localISqlDataWrapper.getInteger();
          break;
        case -5: 
          ConversionResult localConversionResult = new ConversionResult();
          double d = IntegralConverter.toDouble(localISqlDataWrapper.getBigInt(), localConversionResult);
          this.m_sum += d;
          if (ConversionResult.TypeConversionState.INTEGRAL_PRECISION_LOSS == localConversionResult.getState()) {
            this.m_listener.postWarning(new Warning(WarningCode.GENERAL_WARNING, 7, SQLEngineMessageKey.INTEGRAL_PRECISION_LOSS.name(), this.m_count > 2147483647L ? -1 : (int)this.m_count, -1));
          }
          break;
        case 7: 
          this.m_sum += localISqlDataWrapper.getReal();
          break;
        case 6: 
        case 8: 
          this.m_sum += localISqlDataWrapper.getDouble();
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
      }
    }
    
    protected long getMemoryUsage()
    {
      return 16L;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/AvgAggregatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */