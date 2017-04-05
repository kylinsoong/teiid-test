package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;

public class AESimpleWhenClause
  implements IAENode
{
  private IAENode m_parent = null;
  private AEValueExpr m_whenExpression;
  private AEValueExpr m_thenExpression;
  private IColumn m_comparisonMetadata;
  
  public AESimpleWhenClause(AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2)
  {
    this.m_whenExpression = paramAEValueExpr1;
    this.m_thenExpression = paramAEValueExpr2;
    paramAEValueExpr1.setParent(this);
    paramAEValueExpr2.setParent(this);
  }
  
  public AEValueExpr getWhenExpression()
  {
    return this.m_whenExpression;
  }
  
  public AEValueExpr getThenExpression()
  {
    return this.m_thenExpression;
  }
  
  public void setComparisonMetadata(IColumn paramIColumn)
  {
    this.m_comparisonMetadata = paramIColumn;
  }
  
  public IColumn getComparisonMetadata()
  {
    return this.m_comparisonMetadata;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AESimpleWhenClause copy()
  {
    AESimpleWhenClause localAESimpleWhenClause = new AESimpleWhenClause(this.m_whenExpression.copy(), this.m_thenExpression.copy());
    localAESimpleWhenClause.setComparisonMetadata(ColumnMetadata.copyOf(this.m_comparisonMetadata));
    return localAESimpleWhenClause;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public AEValueExpr get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AESimpleWhenClause.this.getWhenExpression();
        }
        if (1 == paramAnonymousInt) {
          return AESimpleWhenClause.this.getThenExpression();
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int size()
      {
        return AESimpleWhenClause.this.getNumChildren();
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public IAENode getParent()
  {
    return this.m_parent;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    this.m_parent = paramIAENode;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AESimpleWhenClause)) {
      return false;
    }
    AESimpleWhenClause localAESimpleWhenClause = (AESimpleWhenClause)paramIAENode;
    return (this.m_whenExpression.isEquivalent(localAESimpleWhenClause.m_whenExpression)) && (this.m_thenExpression.isEquivalent(localAESimpleWhenClause.m_thenExpression));
  }
  
  public void setWhenExpression(AEValueExpr paramAEValueExpr)
  {
    if (null == paramAEValueExpr) {
      throw new NullPointerException("Can not set the 'when' expression to null");
    }
    this.m_whenExpression = paramAEValueExpr;
    this.m_whenExpression.setParent(this);
  }
  
  public void setThenExpression(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("Can not set the 'then' expression to null");
    }
    this.m_thenExpression = paramAEValueExpr;
    this.m_thenExpression.setParent(this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AESimpleWhenClause.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */