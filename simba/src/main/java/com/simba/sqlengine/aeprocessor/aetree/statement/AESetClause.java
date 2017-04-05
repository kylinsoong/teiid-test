package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;

public class AESetClause
  implements IAENode, IAEBinaryNode<AEColumnReference, AEValueExpr>
{
  private static final int NUM_CHILDREN = 2;
  private IAENode m_parent = null;
  private AEColumnReference m_column;
  private AEValueExpr m_value;
  
  public AESetClause(AEColumnReference paramAEColumnReference, AEValueExpr paramAEValueExpr)
  {
    if (null == paramAEColumnReference) {
      throw new NullPointerException("column cannot be null.");
    }
    if (null == paramAEValueExpr) {
      throw new NullPointerException("value cannot be null");
    }
    this.m_column = paramAEColumnReference;
    this.m_value = paramAEValueExpr;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AESetClause copy()
  {
    return new AESetClause(this.m_column.copy(), this.m_value.copy());
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AESetClause.this.getLeftOperand();
        }
        if (1 == paramAnonymousInt) {
          return AESetClause.this.getRightOperand();
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 2;
      }
    }.iterator();
  }
  
  public AEColumnReference getLeftOperand()
  {
    return this.m_column;
  }
  
  public String getLogString()
  {
    return "AESetClause";
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
  
  public AEValueExpr getRightOperand()
  {
    return this.m_value;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AESetClause)) {
      return false;
    }
    AESetClause localAESetClause = (AESetClause)paramIAENode;
    return (this.m_column.isEquivalent(localAESetClause.m_column)) && (this.m_value.isEquivalent(localAESetClause.m_value));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AESetClause.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */