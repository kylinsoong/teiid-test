package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AESelect
  extends AEUnaryRelationalExpr
{
  AEBooleanExpr m_condition;
  private static final int NUM_CHILDREN = 2;
  
  public AESelect(AERelationalExpr paramAERelationalExpr, AEBooleanExpr paramAEBooleanExpr)
  {
    super(paramAERelationalExpr);
    if (null == paramAEBooleanExpr) {
      throw new NullPointerException("condition cannot be null.");
    }
    this.m_condition = paramAEBooleanExpr;
    this.m_condition.setParent(this);
  }
  
  private AESelect(AESelect paramAESelect)
  {
    super(paramAESelect);
    this.m_condition = paramAESelect.m_condition.copy();
    this.m_condition.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AESelect)) {
      return false;
    }
    AESelect localAESelect = (AESelect)paramIAENode;
    return (getOperand().isEquivalent(localAESelect.getOperand())) && (this.m_condition.isEquivalent(localAESelect.m_condition));
  }
  
  public AESelect copy()
  {
    return new AESelect(this);
  }
  
  public AEBooleanExpr getCondition()
  {
    return this.m_condition;
  }
  
  public AEBooleanExpr setSelectCond(AEBooleanExpr paramAEBooleanExpr)
  {
    AEBooleanExpr localAEBooleanExpr = this.m_condition;
    this.m_condition = paramAEBooleanExpr;
    this.m_condition.setParent(this);
    return localAEBooleanExpr;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    AEDefaultVisitor local1 = new AEDefaultVisitor()
    {
      public Void visit(AEColumnReference paramAnonymousAEColumnReference)
        throws ErrorException
      {
        if (!paramAnonymousAEColumnReference.isOuterReference()) {
          AESelect.this.getOperand().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
        } else {
          paramAnonymousAEColumnReference.getNamedRelationalExpr().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
        }
        return null;
      }
      
      public Void visit(AEProxyColumn paramAnonymousAEProxyColumn)
        throws ErrorException
      {
        paramAnonymousAEProxyColumn.getRelationalExpr().setDataNeeded(paramAnonymousAEProxyColumn.getRelationalExpr(), paramAnonymousAEProxyColumn.getColumnNumber());
        return null;
      }
      
      protected Void defaultVisit(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        Iterator localIterator = paramAnonymousIAENode.getChildItr();
        while (localIterator.hasNext()) {
          ((IAENode)localIterator.next()).acceptVisitor(this);
        }
        return null;
      }
    };
    this.m_condition.acceptVisitor(local1);
    getOperand().setDataNeededOnChild();
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getOperand();
    }
    if (1 == paramInt) {
      return getCondition();
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AESelect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */