package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public abstract class AEBinaryValueExpr
  extends AEValueExpr
  implements IAEBinaryNode<AEValueExpr, AEValueExpr>
{
  private AEValueExpr m_left;
  private AEValueExpr m_right;
  protected IColumn m_colMetadata;
  protected final ICoercionHandler m_coercionHandler;
  
  protected AEBinaryValueExpr(AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2, ICoercionHandler paramICoercionHandler)
  {
    assert ((paramAEValueExpr1 != null) && (paramAEValueExpr2 != null) && (paramICoercionHandler != null));
    this.m_left = paramAEValueExpr1;
    this.m_left.setParent(this);
    this.m_right = paramAEValueExpr2;
    this.m_right.setParent(this);
    this.m_coercionHandler = paramICoercionHandler;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public IColumn getColumn()
  {
    return this.m_colMetadata;
  }
  
  public void updateColumn()
    throws ErrorException
  {
    initializeMetadata();
  }
  
  protected abstract void initializeMetadata()
    throws ErrorException;
  
  public abstract AEBinaryValueExpr copy();
  
  public AEValueExpr getLeftOperand()
  {
    return this.m_left;
  }
  
  public final void setLeftOperand(AEValueExpr paramAEValueExpr)
  {
    this.m_left = paramAEValueExpr;
    this.m_left.setParent(this);
  }
  
  public AEValueExpr getRightOperand()
  {
    return this.m_right;
  }
  
  public final void setRightOperand(AEValueExpr paramAEValueExpr)
  {
    this.m_right = paramAEValueExpr;
    this.m_right.setParent(this);
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return this.m_left;
    }
    if (1 == paramInt) {
      return this.m_right;
    }
    throw new IndexOutOfBoundsException("index " + paramInt);
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        return AEBinaryValueExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return AEBinaryValueExpr.this.getNumChildren();
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEBinaryValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */