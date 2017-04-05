package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;

public class AEConcat
  extends AEBinaryValueExpr
{
  public AEConcat(ICoercionHandler paramICoercionHandler, AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2)
    throws ErrorException
  {
    super(paramAEValueExpr1, paramAEValueExpr2, paramICoercionHandler);
    initializeMetadata();
  }
  
  protected AEConcat(AEConcat paramAEConcat)
  {
    super(paramAEConcat.getLeftOperand().copy(), paramAEConcat.getRightOperand().copy(), paramAEConcat.m_coercionHandler);
    this.m_colMetadata = AEValueExpr.createColumnMetadata(paramAEConcat.getColumn());
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
    if (!(paramIAENode instanceof AEConcat)) {
      return false;
    }
    AEConcat localAEConcat = (AEConcat)paramIAENode;
    return (getLeftOperand().isEquivalent(localAEConcat.getLeftOperand())) && (getRightOperand().isEquivalent(localAEConcat.getRightOperand()));
  }
  
  public AEConcat copy()
  {
    return new AEConcat(this);
  }
  
  protected void initializeMetadata()
    throws ErrorException
  {
    this.m_colMetadata = this.m_coercionHandler.coerceConcatColumns(new AECoercionColumnInfo(getLeftOperand()), new AECoercionColumnInfo(getRightOperand()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEConcat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */