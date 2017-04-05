package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;

public class AESearchedWhenClause
  implements IAENode
{
  private IAENode m_parent = null;
  private AEBooleanExpr m_whenCond;
  private AEValueExpr m_thenExpr;
  
  public AESearchedWhenClause(AEBooleanExpr paramAEBooleanExpr, AEValueExpr paramAEValueExpr)
  {
    this.m_whenCond = paramAEBooleanExpr;
    this.m_thenExpr = paramAEValueExpr;
    this.m_whenCond.setParent(this);
    this.m_thenExpr.setParent(this);
  }
  
  public AEBooleanExpr getWhenCondition()
  {
    return this.m_whenCond;
  }
  
  public AEValueExpr getThenExpression()
  {
    return this.m_thenExpr;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public IAENode copy()
  {
    return new AESearchedWhenClause(this.m_whenCond.copy(), this.m_thenExpr.copy());
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AESearchedWhenClause.this.getWhenCondition();
        }
        if (1 == paramAnonymousInt) {
          return AESearchedWhenClause.this.getThenExpression();
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int size()
      {
        return AESearchedWhenClause.this.getNumChildren();
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
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AESearchedWhenClause)) {
      return false;
    }
    AESearchedWhenClause localAESearchedWhenClause = (AESearchedWhenClause)paramIAENode;
    return (this.m_whenCond.isEquivalent(localAESearchedWhenClause.m_whenCond)) && (this.m_thenExpr.isEquivalent(localAESearchedWhenClause.m_thenExpr));
  }
  
  public void setThenExpression(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("Can not set the when expression to null");
    }
    this.m_thenExpr = paramAEValueExpr;
    this.m_thenExpr.setParent(this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AESearchedWhenClause.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */