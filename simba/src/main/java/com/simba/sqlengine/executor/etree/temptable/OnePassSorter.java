package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator.JavaSize;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

public class OnePassSorter
{
  private BlockConverter m_block;
  private InMemTable m_table;
  private PriorityQueue<Integer> m_curHeap;
  private PriorityQueue<Integer> m_backupHeap;
  private DataStore m_curDataStore;
  private List<DataStore> m_sorted;
  private int m_curRow = -1;
  private ILogger m_logger;
  private boolean m_pollStarted;
  private TemporaryTableBuilder.TemporaryTableProperties m_properties;
  private IColumn[] m_columnMetadata;
  private long m_memLimitForSorting;
  private final IndexComparator m_idxCmp;
  
  public OnePassSorter(IColumn[] paramArrayOfIColumn, TemporaryTableBuilder.TemporaryTableProperties paramTemporaryTableProperties, RowComparator paramRowComparator, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    this.m_columnMetadata = ((IColumn[])paramArrayOfIColumn.clone());
    this.m_properties = paramTemporaryTableProperties;
    this.m_logger = paramTemporaryTableProperties.m_logger;
    this.m_block = new BlockConverter(paramArrayOfIColumn, paramTemporaryTableProperties, this.m_logger, paramArrayOfBoolean);
    int i = (int)(paramTemporaryTableProperties.m_blockSize / paramTemporaryTableProperties.m_rowSize / 10L) + 1;
    this.m_table = new InMemTable(paramArrayOfIColumn, paramTemporaryTableProperties.m_maxDataLen, i, paramArrayOfBoolean, this.m_logger);
    IndexComparator localIndexComparator = new IndexComparator(paramRowComparator, this.m_table);
    this.m_idxCmp = localIndexComparator;
    this.m_memLimitForSorting = (-1 * (this.m_table.getMemOverhead() + this.m_block.getMemOverhead()));
    this.m_curHeap = new PriorityQueue(100, this.m_idxCmp);
    this.m_backupHeap = new PriorityQueue(100, this.m_idxCmp);
    this.m_sorted = new ArrayList();
    this.m_curDataStore = new DataStore(this.m_properties, this.m_columnMetadata);
    if (null != paramTemporaryTableProperties.m_logger)
    {
      long l = this.m_properties.m_blockSize;
      String str = l + "B";
      LogUtilities.logDebug("Block size: " + str, paramTemporaryTableProperties.m_logger);
    }
  }
  
  public void appendRow()
    throws ErrorException
  {
    int i;
    for (this.m_curRow = tryAppend(); (this.m_curRow < 0) && (this.m_curHeap.size() > 0); this.m_curRow = tryAppend())
    {
      this.m_pollStarted = true;
      i = ((Integer)this.m_curHeap.poll()).intValue();
      copyAndRemoveRow(i);
    }
    if (this.m_curRow < 0) {
      flushCurrentHeap();
    }
    while ((this.m_curRow < 0) && (this.m_curHeap.size() > 0))
    {
      this.m_pollStarted = true;
      i = ((Integer)this.m_curHeap.poll()).intValue();
      copyAndRemoveRow(i);
      this.m_curRow = tryAppend();
    }
    assert (this.m_curRow >= 0);
  }
  
  public void destroy()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    this.m_block = null;
    this.m_table.clear();
    this.m_table = null;
    this.m_curDataStore.destroy();
    this.m_curDataStore = null;
    if (this.m_sorted != null)
    {
      Iterator localIterator = this.m_sorted.iterator();
      while (localIterator.hasNext())
      {
        DataStore localDataStore = (DataStore)localIterator.next();
        localDataStore.destroy();
      }
    }
  }
  
  private int tryAppend()
  {
    if (this.m_table.getMemUsage() + getIndexOverhead(this.m_table.getNumRows()) > this.m_memLimitForSorting) {
      return -1;
    }
    return this.m_table.appendRow();
  }
  
  public void finishAppending()
  {
    assert (this.m_curRow >= 0);
    if ((this.m_pollStarted) && ((this.m_curHeap.isEmpty()) || (this.m_idxCmp.compare(Integer.valueOf(this.m_curRow), (Integer)this.m_curHeap.peek()) < 0))) {
      this.m_backupHeap.offer(Integer.valueOf(this.m_curRow));
    } else {
      this.m_curHeap.offer(Integer.valueOf(this.m_curRow));
    }
    this.m_curRow = -1;
  }
  
  public List<DataStore> getSorted()
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    flushCurrentHeap();
    flushCurrentHeap();
    List localList = this.m_sorted;
    this.m_sorted = null;
    return localList;
  }
  
  private void flushCurrentHeap()
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    int i = (this.m_curHeap.size() > 0) || (this.m_block.getNumRows() > 0) || (this.m_curDataStore.getNumberBlocks() > 0) ? 1 : 0;
    for (int j = this.m_curHeap.size(); j > 0; j--)
    {
      int k = ((Integer)this.m_curHeap.poll()).intValue();
      copyAndRemoveRow(k);
    }
    if (this.m_block.getNumRows() != 0) {
      this.m_curDataStore.put(this.m_block.toRowBlock());
    }
    if (i != 0)
    {
      this.m_sorted.add(this.m_curDataStore);
      this.m_curDataStore = new DataStore(this.m_properties, this.m_columnMetadata);
    }
    swapHeap();
  }
  
  private void copyAndRemoveRow(int paramInt)
    throws ErrorException
  {
    boolean bool = this.m_block.appendRow();
    if (!bool)
    {
      this.m_curDataStore.put(this.m_block.toRowBlock());
      this.m_block.appendRow();
    }
    for (int i = 0; i < this.m_columnMetadata.length; i++) {
      if (this.m_table.isNull(paramInt, i)) {
        this.m_block.setNull(i);
      } else if (ColumnSizeCalculator.isLongData(this.m_columnMetadata[i], this.m_properties.m_maxDataLen)) {
        this.m_block.setFileMarker(i, this.m_table.getFileMarker(paramInt, i));
      } else {
        switch (this.m_columnMetadata[i].getTypeMetadata().getType())
        {
        case -4: 
        case -3: 
        case -2: 
          this.m_block.setBytes(i, this.m_table.getBytes(paramInt, i));
          break;
        case -10: 
        case -9: 
        case -8: 
        case -1: 
        case 1: 
        case 12: 
          this.m_block.setString(i, this.m_table.getString(paramInt, i));
          break;
        case -7: 
        case 16: 
          this.m_block.setBoolean(i, this.m_table.getBoolean(paramInt, i));
          break;
        case -6: 
          this.m_block.setTinyInt(i, this.m_table.getTinyInt(paramInt, i));
          break;
        case 5: 
          this.m_block.setSmallInt(i, this.m_table.getSmallInt(paramInt, i));
          break;
        case 4: 
          this.m_block.setInteger(i, this.m_table.getInteger(paramInt, i));
          break;
        case -5: 
          this.m_block.setBigInt(i, this.m_table.getBigInt(paramInt, i));
          break;
        case 7: 
          this.m_block.setReal(i, this.m_table.getReal(paramInt, i));
          break;
        case 6: 
        case 8: 
          this.m_block.setDouble(i, this.m_table.getDouble(paramInt, i));
          break;
        case 2: 
        case 3: 
          this.m_block.setExactNum(i, this.m_table.getExactNum(paramInt, i));
          break;
        case 91: 
          this.m_block.setDate(i, this.m_table.getDate(paramInt, i));
          break;
        case 92: 
          this.m_block.setTime(i, this.m_table.getTime(paramInt, i));
          break;
        case 93: 
          this.m_block.setTimestamp(i, this.m_table.getTimestamp(paramInt, i));
          break;
        case -11: 
          this.m_block.setGuid(i, this.m_table.getGuid(paramInt, i));
          break;
        case 0: 
        case 9: 
        case 10: 
        case 11: 
        case 13: 
        case 14: 
        case 15: 
        case 17: 
        case 18: 
        case 19: 
        case 20: 
        case 21: 
        case 22: 
        case 23: 
        case 24: 
        case 25: 
        case 26: 
        case 27: 
        case 28: 
        case 29: 
        case 30: 
        case 31: 
        case 32: 
        case 33: 
        case 34: 
        case 35: 
        case 36: 
        case 37: 
        case 38: 
        case 39: 
        case 40: 
        case 41: 
        case 42: 
        case 43: 
        case 44: 
        case 45: 
        case 46: 
        case 47: 
        case 48: 
        case 49: 
        case 50: 
        case 51: 
        case 52: 
        case 53: 
        case 54: 
        case 55: 
        case 56: 
        case 57: 
        case 58: 
        case 59: 
        case 60: 
        case 61: 
        case 62: 
        case 63: 
        case 64: 
        case 65: 
        case 66: 
        case 67: 
        case 68: 
        case 69: 
        case 70: 
        case 71: 
        case 72: 
        case 73: 
        case 74: 
        case 75: 
        case 76: 
        case 77: 
        case 78: 
        case 79: 
        case 80: 
        case 81: 
        case 82: 
        case 83: 
        case 84: 
        case 85: 
        case 86: 
        case 87: 
        case 88: 
        case 89: 
        case 90: 
        default: 
          throw new IllegalStateException("Unknow column type");
        }
      }
    }
    this.m_table.removeRow(paramInt);
  }
  
  public void swapHeap()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    PriorityQueue localPriorityQueue = this.m_curHeap;
    this.m_curHeap = this.m_backupHeap;
    this.m_backupHeap = localPriorityQueue;
    this.m_pollStarted = false;
  }
  
  public void setNull(int paramInt)
  {
    this.m_table.setNull(this.m_curRow, paramInt);
  }
  
  public void setBigInt(int paramInt, long paramLong)
  {
    this.m_table.setBigInt(this.m_curRow, paramInt, paramLong);
  }
  
  public void setExactNum(int paramInt, BigDecimal paramBigDecimal)
  {
    this.m_table.setExactNum(this.m_curRow, paramInt, paramBigDecimal);
  }
  
  public void setDouble(int paramInt, double paramDouble)
  {
    this.m_table.setDouble(this.m_curRow, paramInt, paramDouble);
  }
  
  public void setReal(int paramInt, float paramFloat)
  {
    this.m_table.setReal(this.m_curRow, paramInt, paramFloat);
  }
  
  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    this.m_table.setBoolean(this.m_curRow, paramInt, paramBoolean);
  }
  
  public void setString(int paramInt, String paramString)
  {
    this.m_table.setString(this.m_curRow, paramInt, paramString);
  }
  
  public void setDate(int paramInt, Date paramDate)
  {
    this.m_table.setDate(this.m_curRow, paramInt, paramDate);
  }
  
  public void setTime(int paramInt, Time paramTime)
  {
    this.m_table.setTime(this.m_curRow, paramInt, paramTime);
  }
  
  public void setTimestamp(int paramInt, Timestamp paramTimestamp)
  {
    this.m_table.setTimestamp(this.m_curRow, paramInt, paramTimestamp);
  }
  
  public void setFileMarker(int paramInt, TemporaryFile.FileMarker paramFileMarker)
  {
    this.m_table.setFileMarker(this.m_curRow, paramInt, paramFileMarker);
  }
  
  public void setGuid(int paramInt, UUID paramUUID)
  {
    this.m_table.setGuid(this.m_curRow, paramInt, paramUUID);
  }
  
  public void setInteger(int paramInt1, int paramInt2)
  {
    this.m_table.setInteger(this.m_curRow, paramInt1, paramInt2);
  }
  
  public void setSmallInt(int paramInt, short paramShort)
  {
    this.m_table.setSmallInt(this.m_curRow, paramInt, paramShort);
  }
  
  public void setTinyInt(int paramInt, byte paramByte)
  {
    this.m_table.setTinyInt(this.m_curRow, paramInt, paramByte);
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
  {
    this.m_table.setBytes(this.m_curRow, paramInt, paramArrayOfByte);
  }
  
  private int getIndexOverhead(int paramInt)
  {
    return paramInt * (ColumnSizeCalculator.JAVA_SIZE.getIntSize() + ColumnSizeCalculator.JAVA_SIZE.getObjectShellSize() + ColumnSizeCalculator.JAVA_SIZE.getObjectRefSize());
  }
  
  public void increaseMemoryUsage(long paramLong)
  {
    if (this.m_memLimitForSorting < 0L)
    {
      this.m_memLimitForSorting += paramLong;
      if (this.m_memLimitForSorting > 0L) {
        this.m_table.increaseMemLimit(this.m_memLimitForSorting);
      }
    }
    else
    {
      this.m_table.increaseMemLimit(paramLong);
    }
  }
  
  private static final class IndexComparator
    implements Comparator<Integer>
  {
    private final InMemTable m_table;
    private final RowComparator m_cmp;
    
    public IndexComparator(RowComparator paramRowComparator, InMemTable paramInMemTable)
    {
      this.m_table = paramInMemTable;
      this.m_cmp = paramRowComparator;
    }
    
    public int compare(Integer paramInteger1, Integer paramInteger2)
    {
      IndexRowView localIndexRowView1 = new IndexRowView(this.m_table);
      localIndexRowView1.setRowNum(paramInteger1.intValue());
      IndexRowView localIndexRowView2 = new IndexRowView(this.m_table);
      localIndexRowView2.setRowNum(paramInteger2.intValue());
      return this.m_cmp.compare(localIndexRowView1, localIndexRowView2);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/OnePassSorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */