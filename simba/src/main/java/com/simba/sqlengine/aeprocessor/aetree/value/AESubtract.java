package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;

public class AESubtract
  extends AEBinaryValueExpr
{
  public AESubtract(ICoercionHandler paramICoercionHandler, AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2)
    throws ErrorException
  {
    super(paramAEValueExpr1, paramAEValueExpr2, paramICoercionHandler);
    initializeMetadata();
  }
  
  protected AESubtract(AESubtract paramAESubtract)
  {
    super(paramAESubtract.getLeftOperand().copy(), paramAESubtract.getRightOperand().copy(), paramAESubtract.m_coercionHandler);
    this.m_colMetadata = AEValueExpr.createColumnMetadata(paramAESubtract.getColumn());
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
    if (!(paramIAENode instanceof AESubtract)) {
      return false;
    }
    AESubtract localAESubtract = (AESubtract)paramIAENode;
    return (getLeftOperand().isEquivalent(localAESubtract.getLeftOperand())) && (getRightOperand().isEquivalent(localAESubtract.getRightOperand()));
  }
  
  public AESubtract copy()
  {
    return new AESubtract(this);
  }
  
  protected void initializeMetadata()
    throws ErrorException
  {
    this.m_colMetadata = this.m_coercionHandler.coerceMinusColumns(new AECoercionColumnInfo(getLeftOperand()), new AECoercionColumnInfo(getRightOperand()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AESubtract.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */