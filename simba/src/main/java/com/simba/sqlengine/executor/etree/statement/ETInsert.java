package com.simba.sqlengine.executor.etree.statement;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet.DMLType;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.Pair;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ETInsert
  extends RowCountStatement
{
  private long m_rowcount = -1L;
  private ETTable m_table;
  private ArrayList<Pair<Integer, ETDataRequest>> m_insertColumnReqs;
  private ETRelationalExpr m_dataRelation;
  private int[] m_defaultCols;
  private short[] m_tgtTableTypes;
  
  public ETInsert(ETTable paramETTable, List<Pair<Integer, IColumn>> paramList, ETRelationalExpr paramETRelationalExpr, Map<Integer, ETParameter> paramMap)
    throws ErrorException
  {
    super(paramMap);
    if ((paramETTable == null) || (paramList == null) || (paramETRelationalExpr == null)) {
      throw new NullPointerException();
    }
    if (paramList.size() != paramETRelationalExpr.getColumnCount()) {
      throw new IllegalArgumentException("column count mismatch");
    }
    this.m_table = paramETTable;
    this.m_dataRelation = paramETRelationalExpr;
    this.m_tgtTableTypes = new short[this.m_table.getColumnCount()];
    HashSet localHashSet = new HashSet(this.m_table.getColumnCount());
    for (int i = 0; i < this.m_tgtTableTypes.length; i++)
    {
      this.m_tgtTableTypes[i] = this.m_table.getColumn(i).getTypeMetadata().getType();
      localHashSet.add(Integer.valueOf(i));
    }
    this.m_insertColumnReqs = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      ETDataRequest localETDataRequest = new ETDataRequest((IColumn)((Pair)localObject).value());
      this.m_insertColumnReqs.add(new Pair(((Pair)localObject).key(), localETDataRequest));
      localHashSet.remove(((Pair)localObject).key());
    }
    this.m_defaultCols = new int[localHashSet.size()];
    int j = 0;
    Object localObject = localHashSet.iterator();
    while (((Iterator)localObject).hasNext())
    {
      int k = ((Integer)((Iterator)localObject).next()).intValue();
      this.m_defaultCols[(j++)] = k;
    }
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
    if (null != this.m_dataRelation)
    {
      try
      {
        this.m_dataRelation.close();
      }
      catch (Exception localException1) {}
      this.m_dataRelation = null;
    }
    if (null != this.m_table)
    {
      try
      {
        this.m_table.close();
      }
      catch (Exception localException2) {}
      this.m_table = null;
    }
    this.m_insertColumnReqs = null;
    this.m_defaultCols = null;
    this.m_tgtTableTypes = null;
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_rowcount;
  }
  
  public void startBatch()
    throws ErrorException
  {
    this.m_table.open(CursorType.FORWARD_ONLY);
    this.m_table.onStartDMLBatch(DSIExtJResultSet.DMLType.INSERT, -1L);
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
    this.m_dataRelation.open(CursorType.FORWARD_ONLY);
    insertData();
    this.m_dataRelation.close();
  }
  
  public Iterator<? extends IETNode> getChildItr()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        return ETInsert.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return ETInsert.this.getNumChildren();
      }
    }.iterator();
  }
  
  private void insertData()
    throws ErrorException
  {
    DataWrapper localDataWrapper = new DataWrapper();
    for (this.m_rowcount = 0L; this.m_dataRelation.move(); this.m_rowcount += 1L)
    {
      this.m_table.appendRow();
      for (int m : this.m_defaultCols)
      {
        localDataWrapper.setNull(this.m_tgtTableTypes[m]);
        if (this.m_table.writeData(m, localDataWrapper, 0L, true)) {
          throw new ErrorException(DiagState.DIAG_INTERVAL_OVERFLOW, 7, SQLEngineMessageKey.RIGHT_DATA_TRUNCATION.name(), -1, m);
        }
      }
      for (int i = 0; i < this.m_insertColumnReqs.size(); i++)
      {
        Pair localPair = (Pair)this.m_insertColumnReqs.get(i);
        ??? = ((Integer)localPair.key()).intValue();
        ETDataRequest localETDataRequest = (ETDataRequest)localPair.value();
        localETDataRequest.setOffset(0L);
        localETDataRequest.setIsDefault(false);
        boolean bool1 = true;
        while (bool1)
        {
          bool1 = this.m_dataRelation.retrieveData(i, localETDataRequest);
          boolean bool2 = false;
          long l = 0L;
          if (localETDataRequest.isDefault())
          {
            bool2 = true;
            localDataWrapper.setNull(this.m_tgtTableTypes[???]);
          }
          else
          {
            localETDataRequest.getData().retrieveData(localDataWrapper);
            l = localETDataRequest.getOffset();
          }
          if (this.m_table.writeData(???, localDataWrapper, l, bool2)) {
            throw new ErrorException(DiagState.DIAG_STR_RIGHT_TRUNC_ERR, 7, SQLEngineMessageKey.RIGHT_DATA_TRUNCATION.name(), -1, ???);
          }
          if (bool1) {
            localETDataRequest.setOffset(localETDataRequest.getOffset() + localETDataRequest.getMaxSize());
          }
        }
      }
      this.m_table.onFinishRowUpdate();
    }
  }
  
  private IETNode getChild(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return this.m_table;
    case 1: 
      return this.m_dataRelation;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/ETInsert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */