package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEExistsPredicate
  extends AEBooleanExpr
  implements IAEUnaryNode<AERelationalExpr>
{
  protected static final int NUM_CHILDREN = 1;
  private AERelationalExpr m_operand;
  private IAENode m_parent;
  
  public AEExistsPredicate(AERelationalExpr paramAERelationalExpr)
  {
    if (paramAERelationalExpr == null) {
      throw new NullPointerException("Null operand is not accepted as operand of AEExistsPredicate");
    }
    this.m_operand = paramAERelationalExpr;
    paramAERelationalExpr.setParent(this);
  }
  
  public AEExistsPredicate(AEExistsPredicate paramAEExistsPredicate)
  {
    setIsOptimized(paramAEExistsPredicate.isOptimized());
    this.m_operand = paramAEExistsPredicate.m_operand.copy();
    this.m_operand.setParent(this);
  }
  
  public AERelationalExpr getOperand()
  {
    return this.m_operand;
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
    if (!(paramIAENode instanceof AEExistsPredicate)) {
      return false;
    }
    return this.m_operand.isEquivalent(((AEExistsPredicate)paramIAENode).getOperand());
  }
  
  public AEExistsPredicate copy()
  {
    return new AEExistsPredicate(this);
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.EXISTS_PRED;
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        switch (paramAnonymousInt)
        {
        case 0: 
          return AEExistsPredicate.this.m_operand;
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEExistsPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */