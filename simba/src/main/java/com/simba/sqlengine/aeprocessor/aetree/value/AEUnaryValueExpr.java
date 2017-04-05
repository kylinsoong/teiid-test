package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public abstract class AEUnaryValueExpr
  extends AEValueExpr
  implements IAEUnaryNode<AEValueExpr>
{
  private AEValueExpr m_operand;
  
  protected AEUnaryValueExpr(AEValueExpr paramAEValueExpr)
  {
    this.m_operand = paramAEValueExpr;
    paramAEValueExpr.setParent(this);
  }
  
  public final AEValueExpr getOperand()
  {
    return this.m_operand;
  }
  
  public final void setOperand(AEValueExpr paramAEValueExpr)
  {
    this.m_operand = paramAEValueExpr;
    this.m_operand.setParent(this);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public abstract AEUnaryValueExpr copy();
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return this.m_operand;
    }
    throw new IndexOutOfBoundsException("index " + paramInt);
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        return AEUnaryValueExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return AEUnaryValueExpr.this.getNumChildren();
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEUnaryValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */