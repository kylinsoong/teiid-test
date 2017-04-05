package com.simba.sqlengine.executor.etree.statement;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet.DMLType;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ETSearchedUpdate
  extends RowCountStatement
{
  private ETTable m_table;
  private ETBooleanExpr m_searchCondition;
  private ETSetClauseList m_setClauseList;
  private long m_updateCount = -1L;
  private ETDataRequest[] m_dataRequests;
  private int[] m_colTypes;
  private boolean m_doInsertOnRC0;
  private int[] m_defaultColToIns;
  
  public ETSearchedUpdate(ETTable paramETTable, ETSetClauseList paramETSetClauseList, ETBooleanExpr paramETBooleanExpr, boolean paramBoolean, Map<Integer, ETParameter> paramMap)
    throws ErrorException
  {
    super(paramMap);
    this.m_table = paramETTable;
    this.m_setClauseList = paramETSetClauseList;
    this.m_searchCondition = paramETBooleanExpr;
    this.m_doInsertOnRC0 = paramBoolean;
    initializeDataRequests();
  }
  
  public boolean isResultSet()
  {
    return false;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    if (null != this.m_table)
    {
      try
      {
        this.m_table.close();
      }
      catch (Exception localException1) {}
      this.m_table = null;
    }
    if (null != this.m_searchCondition)
    {
      try
      {
        this.m_searchCondition.close();
      }
      catch (Exception localException2) {}
      this.m_searchCondition = null;
    }
    this.m_setClauseList = null;
    this.m_dataRequests = null;
    this.m_colTypes = null;
    this.m_defaultColToIns = null;
  }
  
  public void startBatch()
    throws ErrorException
  {
    this.m_table.open(CursorType.FORWARD_ONLY);
    if (this.m_doInsertOnRC0) {
      this.m_table.onStartDMLBatch(DSIExtJResultSet.DMLType.UPSERT, -1L);
    } else {
      this.m_table.onStartDMLBatch(DSIExtJResultSet.DMLType.UPDATE, -1L);
    }
  }
  
  public void endBatch()
    throws ErrorException
  {
    this.m_table.onFinishDMLBatch();
    this.m_table.close();
  }
  
  public void execute()
    throws ErrorException
  {
    this.m_updateCount = 0L;
    this.m_table.reset();
    this.m_searchCondition.open();
    while (this.m_table.move()) {
      if (ETBoolean.SQL_BOOLEAN_TRUE == this.m_searchCondition.evaluate())
      {
        this.m_table.onStartRowUpdate();
        updateRow();
        this.m_table.onFinishRowUpdate();
        this.m_updateCount += 1L;
      }
    }
    if ((this.m_updateCount == 0L) && (this.m_doInsertOnRC0))
    {
      this.m_table.appendRow();
      updateRow();
      writeDefaultColumns();
      this.m_table.onFinishRowUpdate();
      this.m_updateCount += 1L;
    }
    this.m_searchCondition.close();
  }
  
  public Iterator<? extends IETNode> getChildItr()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        return ETSearchedUpdate.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return ETSearchedUpdate.this.getNumChildren();
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return "ETSearchedUpdate";
  }
  
  public int getNumChildren()
  {
    return 3;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_updateCount;
  }
  
  private IETNode getChild(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return this.m_table;
    case 1: 
      return this.m_setClauseList;
    case 2: 
      return this.m_searchCondition;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  private void initializeDataRequests()
    throws ErrorException
  {
    this.m_colTypes = new int[this.m_table.getColumnCount()];
    int i = this.m_setClauseList.getNumChildren();
    this.m_dataRequests = new ETDataRequest[i];
    HashSet localHashSet = new HashSet();
    for (int j = 0; j < i; j++)
    {
      ETSetClause localETSetClause = this.m_setClauseList.getChild(j);
      int m = localETSetClause.getColumnIndex();
      this.m_dataRequests[j] = new ETDataRequest(this.m_table.getColumn(m));
      localHashSet.add(Integer.valueOf(m));
    }
    this.m_defaultColToIns = new int[this.m_colTypes.length - localHashSet.size()];
    j = 0;
    for (int k = 0; k < this.m_colTypes.length; k++)
    {
      this.m_colTypes[k] = this.m_table.getColumn(k).getTypeMetadata().getType();
      if (!localHashSet.contains(Integer.valueOf(k))) {
        this.m_defaultColToIns[(j++)] = k;
      }
    }
    assert (j == this.m_defaultColToIns.length);
  }
  
  private void updateRow()
    throws ErrorException
  {
    DataWrapper localDataWrapper = new DataWrapper();
    for (int i = 0; i < this.m_setClauseList.getNumChildren(); i++)
    {
      ETSetClause localETSetClause = this.m_setClauseList.getChild(i);
      ETDataRequest localETDataRequest = this.m_dataRequests[i];
      int j = localETSetClause.getColumnIndex();
      localETDataRequest.getData().setNull();
      localETDataRequest.setOffset(0L);
      boolean bool1 = localETSetClause.retrieveData(localETDataRequest);
      do
      {
        bool1 = localETSetClause.retrieveData(localETDataRequest);
        long l;
        if (localETDataRequest.isDefault())
        {
          localDataWrapper.setNull(this.m_colTypes[j]);
          l = 0L;
        }
        else
        {
          localETDataRequest.getData().retrieveData(localDataWrapper);
          l = localETDataRequest.getOffset();
        }
        boolean bool2 = this.m_table.writeData(j, localDataWrapper, l, localETDataRequest.isDefault());
        if (bool2) {
          throw new ErrorException(DiagState.DIAG_STR_RIGHT_TRUNC_ERR, 7, SQLEngineMessageKey.RIGHT_DATA_TRUNCATION.name(), -1, i + 1);
        }
        if (bool1) {
          localETDataRequest.setOffset(localETDataRequest.getOffset() + localETDataRequest.getMaxSize());
        }
      } while (bool1);
    }
  }
  
  private void writeDefaultColumns()
    throws ErrorException
  {
    if (this.m_defaultColToIns.length == 0) {
      return;
    }
    DataWrapper localDataWrapper = new DataWrapper();
    for (int k : this.m_defaultColToIns)
    {
      localDataWrapper.setNull(this.m_colTypes[k]);
      if (this.m_table.writeData(k, localDataWrapper, 0L, true)) {
        throw new ErrorException(DiagState.DIAG_INTERVAL_OVERFLOW, 7, SQLEngineMessageKey.RIGHT_DATA_TRUNCATION.name(), -1, k);
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/ETSearchedUpdate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */