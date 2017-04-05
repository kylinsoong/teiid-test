package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.exceptions.UtilsException;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.util.List;

public class TemporaryTableBuilder
{
  private List<AESortSpec> m_sortSpecList = null;
  private final List<IColumn> m_metadata;
  private SqlDataEngine m_dataEngine;
  private boolean[] m_dataNeeded;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extProperty;
  private final ETCancelState m_cancelState;
  
  public TemporaryTableBuilder(List<IColumn> paramList, SqlDataEngine paramSqlDataEngine, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, ETCancelState paramETCancelState, boolean[] paramArrayOfBoolean)
  {
    this.m_metadata = paramList;
    this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
    this.m_dataEngine = paramSqlDataEngine;
    this.m_extProperty = paramExternalAlgorithmProperties;
    this.m_cancelState = paramETCancelState;
  }
  
  public TemporaryTableBuilder sortSpec(List<AESortSpec> paramList)
  {
    this.m_sortSpecList = paramList;
    return this;
  }
  
  public SortedTemporaryTable buildSorted()
    throws ErrorException
  {
    RowComparator.NullCollation localNullCollation = null;
    try
    {
      int i = this.m_dataEngine.getContext().getConnProperty(89).getInt();
      switch (i)
      {
      case 4: 
        localNullCollation = RowComparator.NullCollation.NULLS_END;
        break;
      case 0: 
        localNullCollation = RowComparator.NullCollation.NULLS_HI;
        break;
      case 1: 
        localNullCollation = RowComparator.NullCollation.NULLS_LO;
        break;
      case 2: 
        localNullCollation = RowComparator.NullCollation.NULLS_START;
      }
    }
    catch (UtilsException localUtilsException)
    {
      throw SQLEngineExceptionFactory.invalidOperationException(localUtilsException.getMessage());
    }
    int j = this.m_extProperty.getCellMemoryLimit();
    long l1 = this.m_extProperty.getBlockSize();
    long l2 = ExternalAlgorithmUtil.calculateRowSize(this.m_metadata, this.m_dataNeeded, j);
    l1 = Math.max(l1, l2);
    TemporaryTableProperties localTemporaryTableProperties = new TemporaryTableProperties(this.m_extProperty.getStorageDir(), j, l1, l2, this.m_extProperty.getMaxNumOpenFiles(), this.m_extProperty.getLogger(), "Sort");
    assert (null != this.m_sortSpecList);
    SortedTemporaryTable localSortedTemporaryTable = new SortedTemporaryTable(this.m_metadata, localTemporaryTableProperties, this.m_sortSpecList, localNullCollation, this.m_cancelState, this.m_dataNeeded);
    return localSortedTemporaryTable;
  }
  
  public static final class TemporaryTableProperties
  {
    final int m_maxDataLen;
    final long m_blockSize;
    final long m_rowSize;
    final int m_maxOpenFiles;
    final File m_storageDir;
    final ILogger m_logger;
    final String m_operationName;
    
    public TemporaryTableProperties(File paramFile, int paramInt1, long paramLong1, long paramLong2, int paramInt2, ILogger paramILogger, String paramString)
    {
      this.m_storageDir = paramFile;
      this.m_blockSize = paramLong1;
      this.m_maxDataLen = paramInt1;
      this.m_rowSize = paramLong2;
      this.m_maxOpenFiles = paramInt2;
      this.m_logger = paramILogger;
      this.m_operationName = paramString;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/TemporaryTableBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */