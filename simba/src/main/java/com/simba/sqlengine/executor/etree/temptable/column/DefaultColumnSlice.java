package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public abstract class DefaultColumnSlice
  implements IColumnSlice
{
  private static final long serialVersionUID = 8293211490240639000L;
  
  public boolean isNull(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public long getBigInt(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public byte[] getBytes(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public BigDecimal getExactNum(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public double getDouble(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public float getReal(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean getBoolean(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public String getString(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public Date getDate(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public Time getTime(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public UUID getGuid(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public int getInteger(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public short getSmallInt(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public byte getTinyInt(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setNull(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setBigInt(int paramInt, long paramLong)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setExactNum(int paramInt, BigDecimal paramBigDecimal)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setDouble(int paramInt, double paramDouble)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setReal(int paramInt, float paramFloat)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setString(int paramInt, String paramString)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setDate(int paramInt, Date paramDate)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setTime(int paramInt, Time paramTime)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setGuid(int paramInt, UUID paramUUID)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setInteger(int paramInt1, int paramInt2)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setSmallInt(int paramInt, short paramShort)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setTinyInt(int paramInt, byte paramByte)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setFileMarker(int paramInt, TemporaryFile.FileMarker paramFileMarker)
  {
    throw new UnsupportedOperationException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/DefaultColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */