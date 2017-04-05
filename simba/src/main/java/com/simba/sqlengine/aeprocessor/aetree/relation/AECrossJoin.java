package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AECrossJoin
  extends AEBinaryRelationalExpr
{
  public AECrossJoin(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2)
  {
    super(paramAERelationalExpr1, paramAERelationalExpr2);
  }
  
  private AECrossJoin(AECrossJoin paramAECrossJoin)
  {
    super(paramAECrossJoin);
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
    if (!(paramIAENode instanceof AECrossJoin)) {
      return false;
    }
    AECrossJoin localAECrossJoin = (AECrossJoin)paramIAENode;
    return (getLeftOperand().isEquivalent(localAECrossJoin.getLeftOperand())) && (getRightOperand().isEquivalent(localAECrossJoin.getRightOperand()));
  }
  
  public AECrossJoin copy()
  {
    return new AECrossJoin(this);
  }
  
  protected IAENode getChild(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return getLeftOperand();
    case 1: 
      return getRightOperand();
    }
    throw new IndexOutOfBoundsException("" + paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AECrossJoin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */