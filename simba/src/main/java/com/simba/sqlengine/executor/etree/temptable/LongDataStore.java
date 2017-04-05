package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.io.File;

public class LongDataStore
{
  private final TemporaryFile m_storage;
  private final long m_memoryUsage;
  
  public LongDataStore(File paramFile, long paramLong, ILogger paramILogger)
    throws ErrorException
  {
    this.m_storage = new TemporaryFile(paramFile, paramILogger);
    this.m_memoryUsage = paramLong;
  }
  
  public TemporaryFile.FileMarker put(int paramInt, ETRelationalExpr paramETRelationalExpr)
    throws ErrorException
  {
    IColumn localIColumn = paramETRelationalExpr.getColumn(paramInt);
    if (!localIColumn.getTypeMetadata().isCharacterOrBinaryType()) {
      throw new IllegalArgumentException("Incompatible type for LongDataSotre");
    }
    ETDataRequest localETDataRequest = new ETDataRequest(localIColumn);
    localETDataRequest.setMaxBytes(this.m_memoryUsage);
    boolean bool = paramETRelationalExpr.retrieveData(paramInt, localETDataRequest);
    if (localETDataRequest.getData().isNull()) {
      return null;
    }
    long l = 0L;
    byte[] arrayOfByte = getBytes(localETDataRequest);
    this.m_storage.append(arrayOfByte);
    for (l += arrayOfByte.length; bool; l += arrayOfByte.length)
    {
      localETDataRequest.setOffset(l);
      bool = paramETRelationalExpr.retrieveData(paramInt, localETDataRequest);
      arrayOfByte = getBytes(localETDataRequest);
      this.m_storage.append(arrayOfByte);
    }
    return this.m_storage.generateFileMarker();
  }
  
  private byte[] getBytes(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (paramETDataRequest.getMetadata().isCharacterType()) {
      return DataRetrievalUtil.stringToBytes(paramETDataRequest.getData().getChar());
    }
    return paramETDataRequest.getData().getBinary();
  }
  
  public boolean retrieveData(TemporaryFile.FileMarker paramFileMarker, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (paramETDataRequest.getMetadata().isCharacterOrBinaryType());
    return DataRetrievalUtil.retrieveLongDataFromFile(this.m_storage, paramFileMarker, paramETDataRequest);
  }
  
  public void destroy()
  {
    if (this.m_storage != null) {
      this.m_storage.destroy();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/LongDataStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */