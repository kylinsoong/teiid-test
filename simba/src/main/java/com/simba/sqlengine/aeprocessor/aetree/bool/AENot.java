package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AENot
  extends AEBooleanExpr
  implements IAEUnaryNode<AEBooleanExpr>
{
  AEBooleanExpr m_operand;
  IAENode m_parent = null;
  private static final int NUM_CHILDREN = 1;
  
  public AENot(AEBooleanExpr paramAEBooleanExpr)
  {
    if (null == paramAEBooleanExpr) {
      throw new NullPointerException("Argument cannot be null.");
    }
    this.m_operand = paramAEBooleanExpr;
    this.m_operand.setParent(this);
  }
  
  private AENot(AENot paramAENot)
  {
    setIsOptimized(paramAENot.isOptimized());
    this.m_operand = paramAENot.m_operand.copy();
    this.m_operand.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public String toString()
  {
    return getLogString();
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
    if (!(paramIAENode instanceof AENot)) {
      return false;
    }
    AENot localAENot = (AENot)paramIAENode;
    return localAENot.m_operand.isEquivalent(this.m_operand);
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.NOT;
  }
  
  public AENot copy()
  {
    return new AENot(this);
  }
  
  public AEBooleanExpr getOperand()
  {
    return this.m_operand;
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
          return AENot.this.m_operand;
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AENot.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */