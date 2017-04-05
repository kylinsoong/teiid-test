package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AEOr
  extends AEBinaryBooleanExpr
{
  public AEOr(AEBooleanExpr paramAEBooleanExpr1, AEBooleanExpr paramAEBooleanExpr2)
  {
    super(paramAEBooleanExpr1, paramAEBooleanExpr2);
  }
  
  private AEOr(AEOr paramAEOr)
  {
    super(paramAEOr.m_leftOp.copy(), paramAEOr.m_rightOp.copy());
    setIsOptimized(paramAEOr.isOptimized());
  }
  
  public AEOr copy()
  {
    return new AEOr(this);
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
    if (!(paramIAENode instanceof AEOr)) {
      return false;
    }
    AEOr localAEOr = (AEOr)paramIAENode;
    return ((localAEOr.m_leftOp.isEquivalent(this.m_leftOp)) && (localAEOr.m_rightOp.isEquivalent(this.m_rightOp))) || ((localAEOr.m_rightOp.isEquivalent(this.m_leftOp)) && (localAEOr.m_leftOp.isEquivalent(this.m_rightOp)));
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.OR;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEOr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */