package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public abstract interface IColumnSlice
  extends Serializable
{
  public abstract boolean isNull(int paramInt);
  
  public abstract long getBigInt(int paramInt);
  
  public abstract byte[] getBytes(int paramInt);
  
  public abstract BigDecimal getExactNum(int paramInt);
  
  public abstract double getDouble(int paramInt);
  
  public abstract float getReal(int paramInt);
  
  public abstract boolean getBoolean(int paramInt);
  
  public abstract String getString(int paramInt);
  
  public abstract Date getDate(int paramInt);
  
  public abstract Time getTime(int paramInt);
  
  public abstract Timestamp getTimestamp(int paramInt);
  
  public abstract ColumnSliceType getType();
  
  public abstract UUID getGuid(int paramInt);
  
  public abstract int getInteger(int paramInt);
  
  public abstract short getSmallInt(int paramInt);
  
  public abstract byte getTinyInt(int paramInt);
  
  public abstract TemporaryFile.FileMarker getFileMarker(int paramInt);
  
  public abstract void setNull(int paramInt);
  
  public abstract void setBigInt(int paramInt, long paramLong);
  
  public abstract void setBytes(int paramInt, byte[] paramArrayOfByte);
  
  public abstract void setExactNum(int paramInt, BigDecimal paramBigDecimal);
  
  public abstract void setDouble(int paramInt, double paramDouble);
  
  public abstract void setReal(int paramInt, float paramFloat);
  
  public abstract void setBoolean(int paramInt, boolean paramBoolean);
  
  public abstract void setString(int paramInt, String paramString);
  
  public abstract void setDate(int paramInt, Date paramDate);
  
  public abstract void setTime(int paramInt, Time paramTime);
  
  public abstract void setTimestamp(int paramInt, Timestamp paramTimestamp);
  
  public abstract void setGuid(int paramInt, UUID paramUUID);
  
  public abstract void setInteger(int paramInt1, int paramInt2);
  
  public abstract void setSmallInt(int paramInt, short paramShort);
  
  public abstract void setTinyInt(int paramInt, byte paramByte);
  
  public abstract void setFileMarker(int paramInt, TemporaryFile.FileMarker paramFileMarker);
  
  public abstract int size();
  
  public abstract void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract int columnNumber();
  
  public static enum ColumnSliceType
  {
    BIGINT,  BINARY,  BOOLEAN,  CHAR,  DATE,  DOUBLE,  EXACT_NUM,  FILE_MARKER,  GUID,  INTEGER,  REAL,  SMALLINT,  TIME,  TIMESTAMP,  TINYINT;
    
    private ColumnSliceType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/IColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */