package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEJoin
  extends AEBinaryRelationalExpr
{
  private AEJoinType m_joinType;
  private AEBooleanExpr m_joinCondition;
  
  public AEJoin(AEJoinType paramAEJoinType, AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2)
  {
    this(paramAEJoinType, paramAERelationalExpr1, paramAERelationalExpr2, null);
  }
  
  public AEJoin(AEJoinType paramAEJoinType, AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, AEBooleanExpr paramAEBooleanExpr)
  {
    super(paramAERelationalExpr1, paramAERelationalExpr2);
    if (null == paramAEJoinType) {
      throw new IllegalArgumentException("Arguments to AEJoin may not be null.");
    }
    this.m_joinType = paramAEJoinType;
    this.m_joinCondition = paramAEBooleanExpr;
    if (null != paramAEBooleanExpr) {
      this.m_joinCondition.setParent(this);
    }
  }
  
  private AEJoin(AEJoin paramAEJoin)
  {
    super(paramAEJoin);
    this.m_joinType = paramAEJoin.m_joinType;
    this.m_joinCondition = (null == paramAEJoin.m_joinCondition ? null : paramAEJoin.m_joinCondition.copy());
  }
  
  public AEBooleanExpr getJoinCondition()
  {
    return this.m_joinCondition;
  }
  
  public void setJoinCondition(AEBooleanExpr paramAEBooleanExpr)
  {
    paramAEBooleanExpr.setParent(this);
    this.m_joinCondition = paramAEBooleanExpr;
  }
  
  public AEJoinType getJoinType()
  {
    return this.m_joinType;
  }
  
  public boolean isOuterJoin()
  {
    return this.m_joinType != AEJoinType.INNER_JOIN;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 3;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEJoin)) {
      return false;
    }
    AEJoin localAEJoin = (AEJoin)paramIAENode;
    return (localAEJoin.m_joinType == this.m_joinType) && (localAEJoin.m_joinCondition.isEquivalent(this.m_joinCondition)) && (localAEJoin.getLeftOperand().isEquivalent(getLeftOperand())) && (localAEJoin.getRightOperand().isEquivalent(getRightOperand()));
  }
  
  public AEJoin copy()
  {
    return new AEJoin(this);
  }
  
  public String getLogString()
  {
    return super.getLogString() + ": " + this.m_joinType.name();
  }
  
  protected IAENode getChild(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return getLeftOperand();
    case 1: 
      return getRightOperand();
    case 2: 
      return this.m_joinCondition;
    }
    throw new IndexOutOfBoundsException("" + paramInt);
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    AEDefaultVisitor local1 = new AEDefaultVisitor()
    {
      public Void visit(AEColumnReference paramAnonymousAEColumnReference)
        throws ErrorException
      {
        if (!paramAnonymousAEColumnReference.isOuterReference())
        {
          AEJoin.this.getLeftOperand().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
          AEJoin.this.getRightOperand().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
        }
        else
        {
          paramAnonymousAEColumnReference.getNamedRelationalExpr().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
        }
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
    this.m_joinCondition.acceptVisitor(local1);
    super.setDataNeededOnChild();
  }
  
  public static enum AEJoinType
  {
    INNER_JOIN("INNER JOIN"),  LEFT_OUTER_JOIN("LEFT JOIN"),  RIGHT_OUTER_JOIN("RIGHT JOIN"),  FULL_OUTER_JOIN("FULL JOIN");
    
    private String m_stringVal;
    
    private AEJoinType(String paramString)
    {
      this.m_stringVal = paramString;
    }
    
    public String toString()
    {
      return this.m_stringVal;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEJoin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */