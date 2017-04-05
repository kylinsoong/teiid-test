package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public final class FunctionParameterWrapper
  implements ISqlDataWrapper
{
  private final ETValueExpr m_valueExpr;
  private final IColumn m_columnMetadata;
  private ETDataRequest m_dataRequest = null;
  
  FunctionParameterWrapper(ETValueExpr paramETValueExpr, IColumn paramIColumn)
  {
    if ((null == paramETValueExpr) || (null == paramIColumn)) {
      throw new NullPointerException();
    }
    if (!paramETValueExpr.isOpen()) {
      throw new IllegalArgumentException("valueExpr is not open.");
    }
    this.m_valueExpr = paramETValueExpr;
    this.m_columnMetadata = paramIColumn;
  }
  
  public BigInteger getBigInt()
    throws ErrorException
  {
    return retrieveData().getBigInt();
  }
  
  public byte[] getBinary()
    throws ErrorException
  {
    return retrieveData().getBinary();
  }
  
  public boolean getBoolean()
    throws ErrorException, NullPointerException
  {
    return retrieveData().getBoolean();
  }
  
  public String getChar()
    throws ErrorException
  {
    return retrieveData().getChar();
  }
  
  public Date getDate()
    throws ErrorException
  {
    return retrieveData().getDate();
  }
  
  public BigDecimal getExactNumber()
    throws ErrorException
  {
    return retrieveData().getExactNumber();
  }
  
  public double getDouble()
    throws ErrorException, NullPointerException
  {
    return retrieveData().getDouble();
  }
  
  public UUID getGuid()
    throws ErrorException
  {
    return retrieveData().getGuid();
  }
  
  public long getInteger()
    throws ErrorException, NullPointerException
  {
    return retrieveData().getInteger();
  }
  
  public Object getInterval()
    throws ErrorException
  {
    return retrieveData().getInterval();
  }
  
  public float getReal()
    throws ErrorException, NullPointerException
  {
    return retrieveData().getReal();
  }
  
  public int getSmallInt()
    throws ErrorException, NullPointerException
  {
    return retrieveData().getSmallInt();
  }
  
  public Time getTime()
    throws ErrorException
  {
    return retrieveData().getTime();
  }
  
  public Timestamp getTimestamp()
    throws ErrorException
  {
    return retrieveData().getTimestamp();
  }
  
  public short getTinyInt()
    throws ErrorException, NullPointerException
  {
    return retrieveData().getTinyInt();
  }
  
  public int getType()
  {
    return this.m_columnMetadata.getTypeMetadata().getType();
  }
  
  public boolean isNull()
    throws ErrorException
  {
    return retrieveData().isNull();
  }
  
  public boolean isSet()
  {
    return null != this.m_dataRequest;
  }
  
  public void retrieveData(DataWrapper paramDataWrapper)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setBigInt(BigInteger paramBigInteger)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setBinary(byte[] paramArrayOfByte)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setBoolean(boolean paramBoolean)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setChar(String paramString)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setDate(Date paramDate)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setDouble(double paramDouble)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setExactNumber(BigDecimal paramBigDecimal)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setGuid(UUID paramUUID)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setInteger(long paramLong)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setInterval(Object paramObject)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setNull()
  {
    throw new UnsupportedOperationException();
  }
  
  public void setReal(float paramFloat)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setSmallInt(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setTime(Time paramTime)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setTimestamp(Timestamp paramTimestamp)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setTinyInt(short paramShort)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setValue(DataWrapper paramDataWrapper)
  {
    throw new UnsupportedOperationException();
  }
  
  private ISqlDataWrapper retrieveData()
    throws ErrorException
  {
    ETValueExpr localETValueExpr = this.m_valueExpr;
    assert (localETValueExpr.isOpen()) : "m_valueExpr was not open.";
    ETDataRequest localETDataRequest = this.m_dataRequest;
    if (null == localETDataRequest)
    {
      localETDataRequest = this.m_dataRequest = new ETDataRequest(this.m_columnMetadata);
      if (!localETValueExpr.retrieveData(localETDataRequest)) {
        throw new IllegalStateException();
      }
    }
    return localETDataRequest.getData();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/FunctionParameterWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */