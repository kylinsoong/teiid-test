package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;

public class AEDivide
  extends AEBinaryValueExpr
{
  public AEDivide(ICoercionHandler paramICoercionHandler, AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2)
    throws ErrorException
  {
    super(paramAEValueExpr1, paramAEValueExpr2, paramICoercionHandler);
    initializeMetadata();
  }
  
  protected AEDivide(AEDivide paramAEDivide)
  {
    super(paramAEDivide.getLeftOperand().copy(), paramAEDivide.getRightOperand().copy(), paramAEDivide.m_coercionHandler);
    this.m_colMetadata = AEValueExpr.createColumnMetadata(paramAEDivide.getColumn());
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
    if (!(paramIAENode instanceof AEDivide)) {
      return false;
    }
    AEDivide localAEDivide = (AEDivide)paramIAENode;
    return (getLeftOperand().isEquivalent(localAEDivide.getLeftOperand())) && (getRightOperand().isEquivalent(localAEDivide.getRightOperand()));
  }
  
  public AEDivide copy()
  {
    return new AEDivide(this);
  }
  
  protected void initializeMetadata()
    throws ErrorException
  {
    this.m_colMetadata = this.m_coercionHandler.coerceDivisionColumns(new AECoercionColumnInfo(getLeftOperand()), new AECoercionColumnInfo(getRightOperand()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEDivide.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */