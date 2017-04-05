package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AEAnd
  extends AEBinaryBooleanExpr
{
  public AEAnd(AEBooleanExpr paramAEBooleanExpr1, AEBooleanExpr paramAEBooleanExpr2)
  {
    super(paramAEBooleanExpr1, paramAEBooleanExpr2);
  }
  
  private AEAnd(AEAnd paramAEAnd)
  {
    super(paramAEAnd.m_leftOp.copy(), paramAEAnd.m_rightOp.copy());
    setIsOptimized(paramAEAnd.isOptimized());
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
    if (!(paramIAENode instanceof AEAnd)) {
      return false;
    }
    AEAnd localAEAnd = (AEAnd)paramIAENode;
    return ((localAEAnd.m_leftOp.isEquivalent(this.m_leftOp)) && (localAEAnd.m_rightOp.isEquivalent(this.m_rightOp))) || ((localAEAnd.m_rightOp.isEquivalent(this.m_leftOp)) && (localAEAnd.m_leftOp.isEquivalent(this.m_rightOp)));
  }
  
  public AEAnd copy()
  {
    return new AEAnd(this);
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.AND;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEAnd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */