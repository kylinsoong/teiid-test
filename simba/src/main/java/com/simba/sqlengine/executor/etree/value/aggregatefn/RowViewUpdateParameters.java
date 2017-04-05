package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.exceptions.ErrorException;

public class RowViewUpdateParameters
  implements IAggregator.IUpdateParameters
{
  private IRowView m_rowView;
  private TemporaryFile m_longDataStore;
  private IColumn[] m_columnMeta;
  private boolean[] m_isLongData;
  private boolean[] m_hasMoreData;
  private ETDataRequest[] m_dataRequests;
  
  public RowViewUpdateParameters(IRowView paramIRowView, IColumn[] paramArrayOfIColumn, boolean[] paramArrayOfBoolean)
  {
    this.m_rowView = paramIRowView;
    this.m_columnMeta = paramArrayOfIColumn;
    this.m_isLongData = paramArrayOfBoolean;
    int i = paramArrayOfIColumn.length;
    if (0 < i)
    {
      this.m_hasMoreData = new boolean[i];
      this.m_dataRequests = new ETDataRequest[i];
    }
  }
  
  public ISqlDataWrapper getData(int paramInt)
    throws ErrorException
  {
    return getData(paramInt, 0L, -1L);
  }
  
  public ISqlDataWrapper getData(int paramInt, long paramLong1, long paramLong2)
    throws ErrorException
  {
    ETDataRequest localETDataRequest = this.m_dataRequests[paramInt];
    if (null == localETDataRequest)
    {
      this.m_dataRequests[paramInt] = (localETDataRequest = new ETDataRequest(paramLong1, paramLong2, this.m_columnMeta[paramInt]));
    }
    else
    {
      localETDataRequest.setMaxBytes(paramLong2);
      localETDataRequest.setOffset(paramLong1);
    }
    this.m_hasMoreData[paramInt] = DataRetrievalUtil.retrieveFromRowView(paramInt, this.m_isLongData[paramInt], localETDataRequest, this.m_rowView, this.m_longDataStore);
    return localETDataRequest.getData();
  }
  
  public IColumn getMetadata(int paramInt)
  {
    return this.m_columnMeta[paramInt];
  }
  
  public boolean hasMoreData(int paramInt)
  {
    return this.m_hasMoreData[paramInt];
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/RowViewUpdateParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */