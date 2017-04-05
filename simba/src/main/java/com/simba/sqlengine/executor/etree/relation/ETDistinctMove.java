package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.IndexRowView;
import com.simba.sqlengine.executor.etree.temptable.LongDataStore;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.exceptions.ErrorException;
import java.util.Comparator;
import java.util.List;

public class ETDistinctMove
  extends ETUnaryRelationalExpr
  implements IMemoryConsumer
{
  private static final int EIGHT_KB = 8192;
  private ETRowListener m_rowListener;
  private Comparator<IRowView> m_rowComparator;
  private IColumn[] m_metadata;
  private InMemTable m_rowCache;
  private boolean[] m_isLongData;
  private LongDataStore m_longDataStore;
  private int m_currentCacheRow;
  private int m_nextCacheRow;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extProps;
  private long m_memoryAllocated = 0L;
  private ETDataRequest[] m_dataRequests;
  private boolean m_isCacheInitialized = false;
  private boolean m_isFirstMove;
  private boolean m_hasMoreRows;
  private IMemManagerAgent m_memAgent;
  
  public ETDistinctMove(ETRelationalExpr paramETRelationalExpr, List<IColumn> paramList, Comparator<IRowView> paramComparator, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, boolean[] paramArrayOfBoolean)
  {
    super(paramETRelationalExpr, paramArrayOfBoolean);
    this.m_metadata = ((IColumn[])paramList.toArray(new IColumn[0]));
    this.m_extProps = paramExternalAlgorithmProperties;
    this.m_rowComparator = paramComparator;
    this.m_rowCache = new InMemTable(this.m_metadata, paramExternalAlgorithmProperties.getCellMemoryLimit(), 2, paramArrayOfBoolean, null);
    this.m_isLongData = new boolean[this.m_metadata.length];
    for (int i = 0; i < this.m_metadata.length; i++)
    {
      boolean bool = ColumnSizeCalculator.isLongData(this.m_metadata[i], paramExternalAlgorithmProperties.getCellMemoryLimit());
      this.m_isLongData[i] = bool;
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return getOperand().getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return getOperand().getColumnCount();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return getOperand().getRowCount();
  }
  
  public String getLogString()
  {
    return "ETDistinctMove";
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    super.open(paramCursorType);
    if (this.m_memoryAllocated < getRequiredMemory()) {
      throw new IllegalStateException("Not enough memory allocated.");
    }
    for (int i = 0; i < this.m_isLongData.length; i++) {
      if ((this.m_dataNeeded[i] != 0) && (this.m_isLongData[i] != 0) && (null == this.m_longDataStore)) {
        this.m_longDataStore = new LongDataStore(this.m_extProps.getStorageDir(), 8192L, null);
      }
    }
    if (null == this.m_dataRequests)
    {
      this.m_dataRequests = new ETDataRequest[this.m_metadata.length];
      for (i = 0; i < this.m_metadata.length; i++) {
        if (this.m_dataNeeded[i] != 0) {
          this.m_dataRequests[i] = new ETDataRequest(this.m_metadata[i]);
        }
      }
    }
    if (!this.m_isCacheInitialized)
    {
      this.m_rowCache.setMemLimit(Long.MAX_VALUE);
      this.m_currentCacheRow = this.m_rowCache.appendRow();
      this.m_nextCacheRow = this.m_rowCache.appendRow();
      this.m_rowCache.reduceMemoryUsage();
      this.m_isCacheInitialized = true;
    }
    this.m_isFirstMove = true;
    this.m_hasMoreRows = true;
  }
  
  public void close()
  {
    super.close();
    this.m_rowCache.setMemLimit(0L);
    this.m_rowCache.clear();
    this.m_isCacheInitialized = false;
    this.m_memAgent.recycleMemory(this.m_memoryAllocated);
    this.m_memAgent.unregisterConsumer();
    this.m_memoryAllocated = 0L;
  }
  
  public void reset()
    throws ErrorException
  {
    super.reset();
    this.m_isFirstMove = true;
    this.m_hasMoreRows = true;
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (this.m_dataNeeded[paramInt] != 0) : ("data not needed " + paramInt);
    IndexRowView localIndexRowView = new IndexRowView(this.m_rowCache);
    localIndexRowView.setRowNum(this.m_currentCacheRow);
    return DataRetrievalUtil.retrieveFromRowView(paramInt, this.m_isLongData[paramInt], paramETDataRequest, localIndexRowView, this.m_longDataStore);
  }
  
  public void registerRowLister(ETRowListener paramETRowListener)
  {
    this.m_rowListener = paramETRowListener;
  }
  
  public long assign(long paramLong)
  {
    long l = getRequiredMemory();
    if (this.m_memoryAllocated >= l) {
      return 0L;
    }
    paramLong = Math.min(paramLong, l - this.m_memoryAllocated);
    this.m_memoryAllocated += paramLong;
    return paramLong;
  }
  
  public long getRequiredMemory()
  {
    return 2L * this.m_rowCache.getRowSize() + this.m_rowCache.getMemOverhead() + 8192L;
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_memAgent = paramIMemManagerAgent;
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    if (this.m_hasMoreRows)
    {
      boolean bool1 = false;
      boolean bool2;
      if (this.m_isFirstMove)
      {
        this.m_isFirstMove = false;
        bool2 = moveAndFetchToCache();
      }
      else
      {
        flipCache();
        bool2 = true;
      }
      if (bool2) {
        updateRowListener();
      }
      while (moveAndFetchToCache())
      {
        if (!isDuplicateRow())
        {
          flipCache();
          bool1 = true;
          break;
        }
        updateRowListener();
      }
      this.m_hasMoreRows = bool1;
      return bool2;
    }
    return false;
  }
  
  private void updateRowListener()
    throws ErrorException
  {
    if (null != this.m_rowListener) {
      this.m_rowListener.onNewRow();
    }
  }
  
  private void flipCache()
  {
    int i = this.m_currentCacheRow;
    this.m_currentCacheRow = this.m_nextCacheRow;
    this.m_nextCacheRow = i;
  }
  
  private boolean isDuplicateRow()
  {
    IndexRowView localIndexRowView1 = new IndexRowView(this.m_rowCache);
    localIndexRowView1.setRowNum(this.m_currentCacheRow);
    IndexRowView localIndexRowView2 = new IndexRowView(this.m_rowCache);
    localIndexRowView2.setRowNum(this.m_nextCacheRow);
    return 0 == this.m_rowComparator.compare(localIndexRowView1, localIndexRowView2);
  }
  
  private boolean moveAndFetchToCache()
    throws ErrorException
  {
    if (getOperand().move())
    {
      flipCache();
      for (int i = 0; i < this.m_metadata.length; i++) {
        if (this.m_dataNeeded[i] != 0)
        {
          Object localObject;
          if (this.m_isLongData[i] != 0)
          {
            localObject = this.m_longDataStore.put(i, getOperand());
            if (null == localObject) {
              this.m_rowCache.setNull(this.m_currentCacheRow, i);
            } else {
              this.m_rowCache.setFileMarker(this.m_currentCacheRow, i, (TemporaryFile.FileMarker)localObject);
            }
          }
          else
          {
            localObject = this.m_dataRequests[i];
            ((ETDataRequest)localObject).getData().setNull();
            getOperand().retrieveData(i, (ETDataRequest)localObject);
            ISqlDataWrapper localISqlDataWrapper = ((ETDataRequest)localObject).getData();
            InMemTable.setColumn(this.m_rowCache, localISqlDataWrapper, this.m_metadata[i], i, this.m_currentCacheRow);
          }
        }
      }
      return true;
    }
    return false;
  }
  
  static abstract interface ETRowListener
  {
    public abstract void onNewRow()
      throws ErrorException;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETDistinctMove.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */