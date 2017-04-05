package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SortedTemporaryTable
  implements ITemporaryTable
{
  private static final int CHECK_CANCEL_FREQ = 1000;
  private static final long EIGHT_KB = 8192L;
  private DataStore m_dataStore = null;
  private LongDataStore m_longDataStore = null;
  private final List<AESortSpec> m_sortSpecs;
  private final TemporaryTableBuilder.TemporaryTableProperties m_properties;
  private final List<IColumn> m_columnMetadata;
  private final boolean[] m_longDataCol;
  private final RowComparator.NullCollation m_nullCollation;
  private boolean m_moveSuccess = true;
  private boolean[] m_dataNeeded;
  private long m_memAssigned = 0L;
  private final Sorter m_sorter;
  private IMemManagerAgent m_memAgent;
  private long m_rowCount = -1L;
  private final ETCancelState m_cancelState;
  
  public SortedTemporaryTable(List<IColumn> paramList, TemporaryTableBuilder.TemporaryTableProperties paramTemporaryTableProperties, List<AESortSpec> paramList1, RowComparator.NullCollation paramNullCollation, ETCancelState paramETCancelState, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    this.m_properties = paramTemporaryTableProperties;
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { paramList, paramTemporaryTableProperties, paramList1, paramNullCollation });
    }
    this.m_columnMetadata = paramList;
    this.m_sortSpecs = paramList1;
    this.m_longDataCol = new boolean[paramList.size()];
    this.m_nullCollation = paramNullCollation;
    this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
    this.m_cancelState = paramETCancelState;
    prepareForLongDataColumns(this.m_columnMetadata);
    Iterator localIterator = this.m_sortSpecs.iterator();
    while (localIterator.hasNext())
    {
      AESortSpec localAESortSpec = (AESortSpec)localIterator.next();
      if ((((IColumn)this.m_columnMetadata.get(localAESortSpec.getColumnNumber())).getTypeMetadata().isBinaryType()) || (this.m_longDataCol[localAESortSpec.getColumnNumber()] != 0)) {
        throw SQLEngineExceptionFactory.sortOnLongData(localAESortSpec.getColumnNumber() + 1);
      }
    }
    this.m_sorter = new Sorter(this.m_columnMetadata, this.m_sortSpecs, this.m_properties, this.m_nullCollation, paramETCancelState, this.m_dataNeeded);
  }
  
  public void close()
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if (this.m_dataStore != null) {
      this.m_dataStore.destroy();
    }
    if (this.m_longDataStore != null)
    {
      this.m_longDataStore.destroy();
      this.m_longDataStore = null;
    }
    this.m_sorter.reset();
    this.m_memAgent.recycleMemory(this.m_memAssigned);
    this.m_memAssigned = 0L;
    this.m_memAgent.unregisterConsumer();
  }
  
  public void open()
    throws ErrorException
  {}
  
  public long getRowCount()
  {
    return this.m_rowCount;
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    if (!this.m_moveSuccess) {
      return false;
    }
    if (null == this.m_dataStore)
    {
      this.m_moveSuccess = false;
      return false;
    }
    if (1 > this.m_dataStore.numberOfBlocksHeld()) {
      this.m_dataStore.giveBlock();
    }
    boolean bool = this.m_dataStore.moveToNextRow();
    if (!bool) {
      this.m_moveSuccess = false;
    }
    return bool;
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (null != this.m_dataStore);
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { Integer.valueOf(paramInt), paramETDataRequest });
    }
    return DataRetrievalUtil.retrieveFromRowView(paramInt, this.m_longDataCol[paramInt], paramETDataRequest, this.m_dataStore, this.m_longDataStore);
  }
  
  public void writeFromRelation(ETRelationalExpr paramETRelationalExpr)
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[0]);
    }
    paramETRelationalExpr.open(CursorType.FORWARD_ONLY);
    this.m_sorter.initialize();
    int i = 0;
    while (paramETRelationalExpr.move())
    {
      i++;
      if (i == 1000)
      {
        i = 0;
        this.m_cancelState.checkCancel();
      }
      this.m_sorter.addRow();
      for (int j = 0; j < this.m_columnMetadata.size(); j++) {
        if (this.m_dataNeeded[j] != 0) {
          if (this.m_longDataCol[j] != 0)
          {
            this.m_sorter.setFileMarker(j, this.m_longDataStore.put(j, paramETRelationalExpr));
          }
          else
          {
            ETDataRequest localETDataRequest = new ETDataRequest((IColumn)this.m_columnMetadata.get(j));
            paramETRelationalExpr.retrieveData(j, localETDataRequest);
            writeFromDataWrapper(this.m_sorter, j, localETDataRequest.getData());
          }
        }
      }
      this.m_sorter.finishAppending();
    }
    this.m_dataStore = this.m_sorter.getSorted();
  }
  
  public Comparator<IRowView> getRowComparator()
  {
    return this.m_sorter.getRowComparator();
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_memAgent = paramIMemManagerAgent;
    this.m_sorter.registerManagerAgent(paramIMemManagerAgent);
  }
  
  public long assign(long paramLong)
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("Invalid amount assigned.");
    }
    long l = 0L;
    if (this.m_memAssigned < this.m_properties.m_blockSize)
    {
      l = Math.min(paramLong, this.m_properties.m_blockSize - this.m_memAssigned);
      this.m_memAssigned += l;
      paramLong -= l;
    }
    return l + this.m_sorter.assign(paramLong);
  }
  
  public long getRequiredMemory()
  {
    return this.m_properties.m_blockSize + this.m_sorter.getRequiredMemory() + (this.m_longDataStore == null ? 0L : 8192L);
  }
  
  public void reset()
  {
    if (this.m_rowCount == -1L) {
      throw new IllegalStateException("Resetting temporary table before data has been set.");
    }
    if (null != this.m_dataStore) {
      this.m_dataStore.reset();
    }
  }
  
  private void prepareForLongDataColumns(List<IColumn> paramList)
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { paramList });
    }
    int i = 0;
    for (int j = 0; j < paramList.size(); j++)
    {
      IColumn localIColumn = (IColumn)paramList.get(j);
      if (ColumnSizeCalculator.isLongData(localIColumn, this.m_properties.m_maxDataLen))
      {
        if (i == 0)
        {
          i = 1;
          this.m_longDataStore = new LongDataStore(this.m_properties.m_storageDir, 8192L, this.m_properties.m_logger);
        }
        this.m_longDataCol[j] = true;
      }
    }
  }
  
  private void writeFromDataWrapper(Sorter paramSorter, int paramInt, ISqlDataWrapper paramISqlDataWrapper)
    throws ErrorException
  {
    if (null != this.m_properties.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_properties.m_logger, new Object[] { paramSorter, Integer.valueOf(paramInt), paramISqlDataWrapper });
    }
    if (paramISqlDataWrapper.isNull())
    {
      paramSorter.setNull(paramInt);
      return;
    }
    switch (paramISqlDataWrapper.getType())
    {
    case -5: 
      paramSorter.setBigInt(paramInt, paramISqlDataWrapper.getBigInt().longValue());
      break;
    case 2: 
    case 3: 
      paramSorter.setExactNum(paramInt, paramISqlDataWrapper.getExactNumber());
      break;
    case 6: 
    case 8: 
      paramSorter.setDouble(paramInt, paramISqlDataWrapper.getDouble());
      break;
    case 7: 
      paramSorter.setReal(paramInt, paramISqlDataWrapper.getReal());
      break;
    case -7: 
    case 16: 
      paramSorter.setBoolean(paramInt, paramISqlDataWrapper.getBoolean());
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      paramSorter.setString(paramInt, paramISqlDataWrapper.getChar());
      break;
    case -4: 
    case -3: 
    case -2: 
      paramSorter.setBytes(paramInt, paramISqlDataWrapper.getBinary());
      break;
    case 91: 
      paramSorter.setDate(paramInt, paramISqlDataWrapper.getDate());
      break;
    case 92: 
      paramSorter.setTime(paramInt, paramISqlDataWrapper.getTime());
      break;
    case 93: 
      paramSorter.setTimestamp(paramInt, paramISqlDataWrapper.getTimestamp());
      break;
    case -11: 
      paramSorter.setGuid(paramInt, paramISqlDataWrapper.getGuid());
      break;
    case 4: 
      paramSorter.setInteger(paramInt, (int)paramISqlDataWrapper.getInteger());
      break;
    case 5: 
      paramSorter.setSmallInt(paramInt, (short)paramISqlDataWrapper.getSmallInt());
      break;
    case -6: 
      paramSorter.setTinyInt(paramInt, (byte)paramISqlDataWrapper.getTinyInt());
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
      throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + paramISqlDataWrapper.getType());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/SortedTemporaryTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */