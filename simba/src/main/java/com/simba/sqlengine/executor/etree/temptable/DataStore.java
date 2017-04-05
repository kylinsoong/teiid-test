package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class DataStore
  implements IRowView
{
  private final TemporaryFile m_file;
  private RowBlock m_currentBlock = null;
  private final ArrayList<BlockMarker> m_blockList;
  private final TemporaryTableBuilder.TemporaryTableProperties m_properties;
  private boolean m_closed = false;
  private boolean m_fetching = false;
  private boolean m_blockAllocated = false;
  private int m_currentBlockIndex = 0;
  private int m_rowNumOfCurrBlock = -1;
  private IColumn[] m_metadata;
  
  public DataStore(TemporaryTableBuilder.TemporaryTableProperties paramTemporaryTableProperties, IColumn[] paramArrayOfIColumn)
    throws ErrorException
  {
    this.m_properties = paramTemporaryTableProperties;
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    this.m_metadata = ((IColumn[])paramArrayOfIColumn.clone());
    this.m_file = new TemporaryFile(this.m_properties.m_storageDir, this.m_properties.m_logger);
    this.m_blockList = new ArrayList();
  }
  
  public void destroy()
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if (!this.m_closed)
    {
      this.m_file.destroy();
      this.m_currentBlock = null;
      this.m_blockAllocated = false;
      this.m_closed = true;
      this.m_fetching = false;
    }
  }
  
  public void giveBlock()
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if (this.m_blockAllocated) {
      throw new IllegalStateException("Allocating more blocks than the DataStore can use.");
    }
    this.m_blockAllocated = true;
  }
  
  public boolean moveToFirstRow()
    throws ErrorException
  {
    reset();
    return moveToNextRow();
  }
  
  public void reset()
  {
    if (0 != this.m_currentBlockIndex)
    {
      this.m_currentBlockIndex = 0;
      this.m_currentBlock = null;
    }
    this.m_rowNumOfCurrBlock = -1;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if (!this.m_blockAllocated) {
      throw new IllegalStateException("Attempt to retrieve data without allocating memory for the DataStore.");
    }
    this.m_fetching = true;
    ensureCurrentBlockLoaded();
    while (null != this.m_currentBlock)
    {
      if (this.m_currentBlock.moveToRow(++this.m_rowNumOfCurrBlock)) {
        return true;
      }
      if ((0 == this.m_currentBlockIndex) && (null == ((BlockMarker)this.m_blockList.get(0)).m_fileMarker)) {
        ((BlockMarker)this.m_blockList.get(0)).m_fileMarker = this.m_file.put(this.m_currentBlock.serialize());
      }
      this.m_rowNumOfCurrBlock = -1;
      this.m_currentBlockIndex += 1;
      this.m_currentBlock = null;
      ensureCurrentBlockLoaded();
    }
    return false;
  }
  
  public int numberOfBlocksHeld()
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if (this.m_blockAllocated) {
      return 1;
    }
    return 0;
  }
  
  public void put(RowBlock paramRowBlock)
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { paramRowBlock });
    }
    TemporaryFile.FileMarker localFileMarker = null;
    long l = 0L;
    if ((!this.m_fetching) && (this.m_blockAllocated) && (this.m_blockList.isEmpty()))
    {
      this.m_currentBlock = paramRowBlock;
    }
    else
    {
      byte[] arrayOfByte = paramRowBlock.serialize();
      localFileMarker = this.m_file.put(arrayOfByte);
      if (!this.m_blockList.isEmpty())
      {
        BlockMarker localBlockMarker = (BlockMarker)this.m_blockList.get(this.m_blockList.size() - 1);
        l = localBlockMarker.m_startIndex + localBlockMarker.m_numRows;
      }
    }
    this.m_blockList.add(new BlockMarker(localFileMarker, l, paramRowBlock.getNumRows()));
  }
  
  public void takeBlock()
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if ((this.m_blockAllocated) && (0 == this.m_currentBlockIndex) && (null == ((BlockMarker)this.m_blockList.get(0)).m_fileMarker))
    {
      byte[] arrayOfByte = this.m_currentBlock.serialize();
      ((BlockMarker)this.m_blockList.get(0)).m_fileMarker = this.m_file.put(arrayOfByte);
    }
    this.m_currentBlock = null;
    this.m_blockAllocated = false;
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getFileMarker(paramInt);
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getExactNumber(paramInt);
  }
  
  public long getBigInt(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getBigInt(paramInt);
  }
  
  public boolean isNull(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.isNull(paramInt);
  }
  
  public double getDouble(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getDouble(paramInt);
  }
  
  public float getReal(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getReal(paramInt);
  }
  
  public boolean getBoolean(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getBoolean(paramInt);
  }
  
  public String getString(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getString(paramInt);
  }
  
  public Date getDate(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getDate(paramInt);
  }
  
  public Time getTime(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getTime(paramInt);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getTimestamp(paramInt);
  }
  
  public UUID getGuid(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getGuid(paramInt);
  }
  
  public int getInteger(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getInteger(paramInt);
  }
  
  public short getSmallInt(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getSmallInt(paramInt);
  }
  
  public byte getTinyInt(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getTinyInt(paramInt);
  }
  
  public byte[] getBytes(int paramInt)
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_currentBlock.getBytes(paramInt);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_metadata[paramInt];
  }
  
  public int getNumberBlocks()
  {
    return this.m_blockList.size();
  }
  
  private void ensureCurrentBlockLoaded()
    throws ErrorException
  {
    assert (this.m_blockAllocated);
    if ((null == this.m_currentBlock) && (this.m_currentBlockIndex < this.m_blockList.size())) {
      this.m_currentBlock = RowBlock.loadData(this.m_file.get(((BlockMarker)this.m_blockList.get(this.m_currentBlockIndex)).m_fileMarker), this.m_properties.m_logger, this.m_metadata);
    }
  }
  
  private static final class BlockMarker
  {
    public TemporaryFile.FileMarker m_fileMarker;
    public long m_startIndex;
    public int m_numRows;
    
    public BlockMarker(TemporaryFile.FileMarker paramFileMarker, long paramLong, int paramInt)
    {
      this.m_fileMarker = paramFileMarker;
      this.m_startIndex = paramLong;
      this.m_numRows = paramInt;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/DataStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */