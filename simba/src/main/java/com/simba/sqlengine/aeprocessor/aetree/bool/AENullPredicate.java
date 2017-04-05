package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AENullPredicate
  extends AEBooleanExpr
  implements IAEUnaryNode<AEValueExprList>
{
  protected static final int NUM_CHILDREN = 1;
  private AEValueExprList m_operand;
  private IAENode m_parent;
  
  public AENullPredicate(AEValueExprList paramAEValueExprList)
  {
    this.m_operand = paramAEValueExprList;
    this.m_operand.setParent(this);
  }
  
  private AENullPredicate(AENullPredicate paramAENullPredicate)
  {
    setIsOptimized(paramAENullPredicate.isOptimized());
    this.m_operand = paramAENullPredicate.m_operand.copy();
    this.m_operand.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public int getNumChildren()
  {
    return 1;
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
    if (!(paramIAENode instanceof AENullPredicate)) {
      return false;
    }
    AENullPredicate localAENullPredicate = (AENullPredicate)paramIAENode;
    return localAENullPredicate.m_operand.isEquivalent(this.m_operand);
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.NULL_PRED;
  }
  
  public AENullPredicate copy()
  {
    return new AENullPredicate(this);
  }
  
  public AEValueExprList getOperand()
  {
    return this.m_operand;
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AENullPredicate.this.m_operand;
        }
        throw new IndexOutOfBoundsException("Index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return 1;
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AENullPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */