package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public abstract class AEUnaryRelationalExpr
  extends AERelationalExpr
  implements IAEUnaryNode<AERelationalExpr>
{
  private AERelationalExpr m_operand;
  protected boolean[] m_dataNeeded;
  protected boolean m_shouldUpdateDNTracker = true;
  
  public AEUnaryRelationalExpr(AERelationalExpr paramAERelationalExpr)
  {
    if (paramAERelationalExpr == null) {
      throw new NullPointerException();
    }
    this.m_operand = paramAERelationalExpr;
    paramAERelationalExpr.setParent(this);
  }
  
  protected AEUnaryRelationalExpr(AEUnaryRelationalExpr paramAEUnaryRelationalExpr)
  {
    super(paramAEUnaryRelationalExpr);
    this.m_shouldUpdateDNTracker = true;
    this.m_operand = paramAEUnaryRelationalExpr.m_operand.copy();
    this.m_operand.setParent(this);
  }
  
  public abstract AEUnaryRelationalExpr copy();
  
  public Iterator<IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public abstract int getNumChildren();
  
  public AERelationalExpr getOperand()
  {
    return this.m_operand;
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_operand.getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_operand.getColumnCount();
  }
  
  protected abstract IAENode getChild(int paramInt);
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        return AEUnaryRelationalExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return AEUnaryRelationalExpr.this.getNumChildren();
      }
    };
  }
  
  public void setOperand(AERelationalExpr paramAERelationalExpr)
  {
    paramAERelationalExpr.setParent(this);
    this.m_operand = paramAERelationalExpr;
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[this.m_operand.getColumnCount()];
      this.m_shouldUpdateDNTracker = false;
    }
    return this.m_dataNeeded[paramInt];
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[getOperand().getColumnCount()];
      this.m_shouldUpdateDNTracker = false;
    }
    int i;
    if (paramAERelationalExpr.equals(this))
    {
      this.m_dataNeeded[paramInt] = true;
      i = paramInt;
      getOperand().setDataNeeded(getOperand(), i);
    }
    else
    {
      i = getOperand().setDataNeeded(paramAERelationalExpr, paramInt);
      if (i != -1) {
        this.m_dataNeeded[i] = true;
      }
    }
    return i;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEUnaryRelationalExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */