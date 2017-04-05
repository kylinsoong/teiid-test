package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public abstract interface ISqlDataWrapper
{
  public abstract BigInteger getBigInt()
    throws ErrorException;
  
  public abstract byte[] getBinary()
    throws ErrorException;
  
  public abstract boolean getBoolean()
    throws ErrorException, NullPointerException;
  
  public abstract String getChar()
    throws ErrorException;
  
  public abstract Date getDate()
    throws ErrorException;
  
  public abstract BigDecimal getExactNumber()
    throws ErrorException;
  
  public abstract double getDouble()
    throws ErrorException, NullPointerException;
  
  public abstract UUID getGuid()
    throws ErrorException;
  
  public abstract long getInteger()
    throws ErrorException, NullPointerException;
  
  public abstract Object getInterval()
    throws ErrorException;
  
  public abstract float getReal()
    throws ErrorException, NullPointerException;
  
  public abstract int getSmallInt()
    throws ErrorException, NullPointerException;
  
  public abstract Time getTime()
    throws ErrorException;
  
  public abstract Timestamp getTimestamp()
    throws ErrorException;
  
  public abstract short getTinyInt()
    throws ErrorException, NullPointerException;
  
  public abstract int getType();
  
  public abstract boolean isNull()
    throws ErrorException;
  
  public abstract boolean isSet();
  
  public abstract void setBigInt(BigInteger paramBigInteger)
    throws ErrorException;
  
  public abstract void setBinary(byte[] paramArrayOfByte)
    throws ErrorException;
  
  public abstract void setBoolean(boolean paramBoolean)
    throws ErrorException;
  
  public abstract void setChar(String paramString)
    throws ErrorException;
  
  public abstract void setDate(Date paramDate)
    throws ErrorException;
  
  public abstract void setDouble(double paramDouble)
    throws ErrorException;
  
  public abstract void setExactNumber(BigDecimal paramBigDecimal)
    throws ErrorException;
  
  public abstract void setGuid(UUID paramUUID)
    throws ErrorException;
  
  public abstract void setInteger(long paramLong)
    throws ErrorException;
  
  public abstract void setInterval(Object paramObject)
    throws ErrorException;
  
  public abstract void setNull();
  
  public abstract void setReal(float paramFloat)
    throws ErrorException;
  
  public abstract void setSmallInt(int paramInt)
    throws ErrorException;
  
  public abstract void setTime(Time paramTime)
    throws ErrorException;
  
  public abstract void setTimestamp(Timestamp paramTimestamp)
    throws ErrorException;
  
  public abstract void setTinyInt(short paramShort)
    throws ErrorException;
  
  public abstract void setValue(DataWrapper paramDataWrapper)
    throws ErrorException;
  
  public abstract void retrieveData(DataWrapper paramDataWrapper)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/ISqlDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */