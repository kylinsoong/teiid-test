package com.simba.sqlengine.executor.etree;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.DefaultSqlDataWrapper;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;

public final class ETDataRequest
{
  private final ISqlDataWrapper m_data;
  private long m_offset;
  private long m_maxBytes;
  private boolean m_isDefault;
  private IColumn m_metadata = null;
  
  public ETDataRequest(IColumn paramIColumn)
  {
    this(0L, -1L, paramIColumn);
  }
  
  public ETDataRequest(long paramLong1, long paramLong2, IColumn paramIColumn)
  {
    this.m_offset = paramLong1;
    this.m_maxBytes = paramLong2;
    this.m_isDefault = false;
    this.m_metadata = paramIColumn;
    this.m_data = DefaultSqlDataWrapper.initializeFromSqlType(paramIColumn.getTypeMetadata().getType());
  }
  
  public ETDataRequest(ETDataRequest paramETDataRequest)
  {
    this.m_data = paramETDataRequest.m_data;
    this.m_offset = paramETDataRequest.m_offset;
    this.m_maxBytes = paramETDataRequest.m_maxBytes;
    this.m_isDefault = paramETDataRequest.m_isDefault;
    this.m_metadata = paramETDataRequest.m_metadata;
  }
  
  public ISqlDataWrapper getData()
  {
    return this.m_data;
  }
  
  public TypeMetadata getMetadata()
  {
    return this.m_metadata.getTypeMetadata();
  }
  
  public IColumn getColumn()
  {
    return this.m_metadata;
  }
  
  public long getOffset()
  {
    return this.m_offset;
  }
  
  public long getMaxSize()
  {
    return this.m_maxBytes;
  }
  
  public boolean isDefault()
  {
    return this.m_isDefault;
  }
  
  public void setIsDefault(boolean paramBoolean)
  {
    this.m_isDefault = paramBoolean;
  }
  
  public void setMaxBytes(long paramLong)
  {
    if (-1L != paramLong)
    {
      if (0L > paramLong) {
        throw new IllegalArgumentException("Invalid max retrieval size: " + paramLong);
      }
      if (this.m_metadata.getTypeMetadata().isCharacterType())
      {
        paramLong &= 0xFFFFFFFFFFFFFFFE;
        assert (0L <= paramLong);
      }
    }
    this.m_maxBytes = paramLong;
  }
  
  public void setOffset(long paramLong)
  {
    if ((paramLong < 0L) || ((this.m_metadata.getTypeMetadata().isCharacterType()) && (paramLong % 2L != 0L))) {
      throw new IllegalArgumentException("invalid offset: " + paramLong);
    }
    this.m_offset = paramLong;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETDataRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */