package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;

public class AEValueSubQuery
  extends AEValueExpr
{
  private static final int NUM_CHILDREN = 1;
  private AERelationalExpr m_queryExpr;
  private boolean m_isCorrelated;
  private ColumnMetadata m_columnMetadata;
  
  public AEValueSubQuery(AERelationalExpr paramAERelationalExpr, boolean paramBoolean)
  {
    this.m_queryExpr = paramAERelationalExpr;
    this.m_isCorrelated = paramBoolean;
    if (1 != paramAERelationalExpr.getColumnCount()) {
      throw new IllegalArgumentException("Illegal number of columns: " + paramAERelationalExpr.getColumnCount());
    }
    this.m_columnMetadata = ColumnMetadata.copyOf(this.m_queryExpr.getColumn(0));
    this.m_columnMetadata.setName(null);
  }
  
  public boolean isCorrelated()
  {
    return this.m_isCorrelated;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<AERelationalExpr> getChildItr()
  {
    new AbstractList()
    {
      public AERelationalExpr get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEValueSubQuery.this.m_queryExpr;
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 1;
      }
    }.iterator();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEValueSubQuery)) {
      return false;
    }
    AEValueSubQuery localAEValueSubQuery = (AEValueSubQuery)paramIAENode;
    return (this.m_isCorrelated == localAEValueSubQuery.m_isCorrelated) && (this.m_queryExpr.isEquivalent(localAEValueSubQuery.m_queryExpr));
  }
  
  public IColumn getColumn()
  {
    return this.m_columnMetadata;
  }
  
  public AEValueSubQuery copy()
  {
    return new AEValueSubQuery(this.m_queryExpr.copy(), this.m_isCorrelated);
  }
  
  public AERelationalExpr getQueryExpression()
  {
    return this.m_queryExpr;
  }
  
  public void updateColumn()
    throws ErrorException
  {
    this.m_columnMetadata = ColumnMetadata.copyOf(this.m_queryExpr.getColumn(0));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEValueSubQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */