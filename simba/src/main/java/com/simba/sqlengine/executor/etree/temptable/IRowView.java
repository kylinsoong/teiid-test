package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public abstract interface IRowView
{
  public abstract boolean isNull(int paramInt);
  
  public abstract long getBigInt(int paramInt);
  
  public abstract BigDecimal getExactNumber(int paramInt);
  
  public abstract double getDouble(int paramInt);
  
  public abstract TemporaryFile.FileMarker getFileMarker(int paramInt);
  
  public abstract float getReal(int paramInt);
  
  public abstract boolean getBoolean(int paramInt);
  
  public abstract String getString(int paramInt);
  
  public abstract Date getDate(int paramInt);
  
  public abstract Time getTime(int paramInt);
  
  public abstract Timestamp getTimestamp(int paramInt);
  
  public abstract UUID getGuid(int paramInt);
  
  public abstract int getInteger(int paramInt);
  
  public abstract short getSmallInt(int paramInt);
  
  public abstract byte getTinyInt(int paramInt);
  
  public abstract byte[] getBytes(int paramInt);
  
  public abstract IColumn getColumn(int paramInt);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/IRowView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */