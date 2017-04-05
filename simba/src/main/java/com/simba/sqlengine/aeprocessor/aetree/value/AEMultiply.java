package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;

public class AEMultiply
  extends AEBinaryValueExpr
{
  public AEMultiply(ICoercionHandler paramICoercionHandler, AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2)
    throws ErrorException
  {
    super(paramAEValueExpr1, paramAEValueExpr2, paramICoercionHandler);
    initializeMetadata();
  }
  
  protected AEMultiply(AEMultiply paramAEMultiply)
  {
    super(paramAEMultiply.getLeftOperand().copy(), paramAEMultiply.getRightOperand().copy(), paramAEMultiply.m_coercionHandler);
    this.m_colMetadata = AEValueExpr.createColumnMetadata(paramAEMultiply.getColumn());
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AEMultiply)) {
      return false;
    }
    AEMultiply localAEMultiply = (AEMultiply)paramIAENode;
    return ((getLeftOperand().isEquivalent(localAEMultiply.getLeftOperand())) && (getRightOperand().isEquivalent(localAEMultiply.getRightOperand()))) || ((getLeftOperand().isEquivalent(localAEMultiply.getRightOperand())) && (getRightOperand().isEquivalent(localAEMultiply.getLeftOperand())));
  }
  
  public AEMultiply copy()
  {
    return new AEMultiply(this);
  }
  
  protected void initializeMetadata()
    throws ErrorException
  {
    this.m_colMetadata = this.m_coercionHandler.coerceMultiplicationColumns(new AECoercionColumnInfo(getLeftOperand()), new AECoercionColumnInfo(getRightOperand()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEMultiply.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */