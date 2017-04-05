package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AERelationalExpr
  implements IAENode
{
  private IAENode m_parent = null;
  private List<IColumn> m_asList = null;
  private AERelationalExpr m_origin;
  
  protected AERelationalExpr() {}
  
  protected AERelationalExpr(AERelationalExpr paramAERelationalExpr)
  {
    this.m_origin = paramAERelationalExpr;
  }
  
  public abstract AERelationalExpr copy();
  
  public ArrayList<IColumn> createResultSetColumns()
  {
    int i = getColumnCount();
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      localArrayList.add(getColumn(j));
    }
    return localArrayList;
  }
  
  public List<IColumn> createResultSetColumns(List<Integer> paramList)
  {
    ArrayList localArrayList = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      localArrayList.add(getColumn(localInteger.intValue()));
    }
    return localArrayList;
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public IAENode getParent()
  {
    return this.m_parent;
  }
  
  public List<IColumn> getResultSetColumns()
  {
    if (null == this.m_asList) {
      this.m_asList = new AbstractList()
      {
        public IColumn get(int paramAnonymousInt)
        {
          return AERelationalExpr.this.getColumn(paramAnonymousInt);
        }
        
        public int size()
        {
          return AERelationalExpr.this.getColumnCount();
        }
      };
    }
    return this.m_asList;
  }
  
  public abstract IColumn getColumn(int paramInt);
  
  public abstract int getColumnCount();
  
  public AERelationalExpr getOrigin()
  {
    return this.m_origin;
  }
  
  public int findColumn(String paramString, boolean paramBoolean)
  {
    throw new UnsupportedOperationException("find column is not supported for type: " + getLogString());
  }
  
  public AEColumnInfo findQColumn(AEQColumnName paramAEQColumnName, boolean paramBoolean)
    throws ErrorException
  {
    throw new UnsupportedOperationException("find column is not supported for type: " + getLogString());
  }
  
  public String toString()
  {
    return getLogString();
  }
  
  public abstract boolean getDataNeeded(int paramInt);
  
  public abstract int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException;
  
  public abstract void setDataNeededOnChild()
    throws ErrorException;
  
  public void setParent(IAENode paramIAENode)
  {
    this.m_parent = paramIAENode;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AERelationalExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */