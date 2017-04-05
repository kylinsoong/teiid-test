package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public abstract class AEBinaryBooleanExpr
  extends AEBooleanExpr
  implements IAEBinaryNode<AEBooleanExpr, AEBooleanExpr>
{
  protected static final int NUM_CHILDREN = 2;
  AEBooleanExpr m_leftOp;
  AEBooleanExpr m_rightOp;
  IAENode m_parent = null;
  
  public AEBinaryBooleanExpr(AEBooleanExpr paramAEBooleanExpr1, AEBooleanExpr paramAEBooleanExpr2)
  {
    this.m_leftOp = paramAEBooleanExpr1;
    this.m_leftOp.setParent(this);
    this.m_rightOp = paramAEBooleanExpr2;
    this.m_rightOp.setParent(this);
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
  
  public AEBooleanExpr getLeftOperand()
  {
    return this.m_leftOp;
  }
  
  public AEBooleanExpr getRightOperand()
  {
    return this.m_rightOp;
  }
  
  public AEBooleanExpr setLeftOperand(AEBooleanExpr paramAEBooleanExpr)
  {
    AEBooleanExpr localAEBooleanExpr = this.m_leftOp;
    this.m_leftOp = paramAEBooleanExpr;
    if (null != this.m_leftOp) {
      this.m_leftOp.setParent(this);
    }
    return localAEBooleanExpr;
  }
  
  public AEBooleanExpr setRightOperand(AEBooleanExpr paramAEBooleanExpr)
  {
    AEBooleanExpr localAEBooleanExpr = this.m_rightOp;
    this.m_rightOp = paramAEBooleanExpr;
    if (null != this.m_rightOp) {
      this.m_rightOp.setParent(this);
    }
    return localAEBooleanExpr;
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return this.m_leftOp;
    }
    if (1 == paramInt) {
      return this.m_rightOp;
    }
    throw new IndexOutOfBoundsException("index " + paramInt);
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        return AEBinaryBooleanExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return 2;
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEBinaryBooleanExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */