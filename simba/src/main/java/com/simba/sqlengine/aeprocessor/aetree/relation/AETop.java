package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.support.exceptions.ErrorException;

public final class AETop
  extends AEUnaryRelationalExpr
{
  private AEValueExpr m_valueExpr;
  private boolean m_isPercent;
  
  public AETop(AERelationalExpr paramAERelationalExpr, AEValueExpr paramAEValueExpr, boolean paramBoolean)
  {
    super(paramAERelationalExpr);
    this.m_valueExpr = paramAEValueExpr;
    this.m_valueExpr.setParent(this);
    this.m_isPercent = paramBoolean;
  }
  
  private AETop(AETop paramAETop)
  {
    super(paramAETop);
    this.m_valueExpr = paramAETop.m_valueExpr.copy();
    this.m_isPercent = paramAETop.m_isPercent;
  }
  
  public boolean isPercent()
  {
    return this.m_isPercent;
  }
  
  public AEValueExpr getSelectLimitExpr()
  {
    return this.m_valueExpr;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AETop)) {
      return false;
    }
    AETop localAETop = (AETop)paramIAENode;
    return (this.m_isPercent == localAETop.m_isPercent) && (getOperand().isEquivalent(localAETop.getOperand())) && (this.m_valueExpr.isEquivalent(localAETop.m_valueExpr));
  }
  
  public AETop copy()
  {
    return new AETop(this);
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    getOperand().setDataNeededOnChild();
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getOperand();
    }
    if (1 == paramInt) {
      return getSelectLimitExpr();
    }
    throw new IndexOutOfBoundsException("There is no child at index: " + paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AETop.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */