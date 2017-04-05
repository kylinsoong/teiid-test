package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator.JavaSize;
import com.simba.support.exceptions.ErrorException;

public abstract interface IAggregator
  extends IWarningSource
{
  public abstract long getMemorySize(ColumnSizeCalculator.JavaSize paramJavaSize);
  
  public abstract void load(byte[] paramArrayOfByte)
    throws ErrorException;
  
  public abstract byte[] serialize()
    throws ErrorException;
  
  public abstract void update(IUpdateParameters paramIUpdateParameters)
    throws ErrorException;
  
  public abstract boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException;
  
  public abstract void reset();
  
  public abstract void close();
  
  public static abstract interface IUpdateParameters
  {
    public abstract ISqlDataWrapper getData(int paramInt)
      throws ErrorException;
    
    public abstract ISqlDataWrapper getData(int paramInt, long paramLong1, long paramLong2)
      throws ErrorException;
    
    public abstract IColumn getMetadata(int paramInt);
    
    public abstract boolean hasMoreData(int paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/IAggregator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */