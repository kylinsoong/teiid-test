package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;

public class AEAdd
  extends AEBinaryValueExpr
{
  public AEAdd(ICoercionHandler paramICoercionHandler, AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2)
    throws ErrorException
  {
    super(paramAEValueExpr1, paramAEValueExpr2, paramICoercionHandler);
    initializeMetadata();
  }
  
  protected AEAdd(AEAdd paramAEAdd)
  {
    super(paramAEAdd.getLeftOperand().copy(), paramAEAdd.getRightOperand().copy(), paramAEAdd.m_coercionHandler);
    this.m_colMetadata = AEValueExpr.createColumnMetadata(paramAEAdd.getColumn());
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
    if (!(paramIAENode instanceof AEAdd)) {
      return false;
    }
    AEAdd localAEAdd = (AEAdd)paramIAENode;
    if (isAssociative()) {
      return ((getLeftOperand().isEquivalent(localAEAdd.getLeftOperand())) && (getRightOperand().isEquivalent(localAEAdd.getRightOperand()))) || ((getLeftOperand().isEquivalent(localAEAdd.getRightOperand())) && (getRightOperand().isEquivalent(localAEAdd.getLeftOperand())));
    }
    return (getLeftOperand().isEquivalent(localAEAdd.getLeftOperand())) && (getRightOperand().isEquivalent(localAEAdd.getRightOperand()));
  }
  
  public AEAdd copy()
  {
    return new AEAdd(this);
  }
  
  public boolean isAssociative()
  {
    return TypeUtilities.isNumberType(this.m_colMetadata.getTypeMetadata().getType());
  }
  
  protected void initializeMetadata()
    throws ErrorException
  {
    this.m_colMetadata = this.m_coercionHandler.coercePlusColumns(new AECoercionColumnInfo(getLeftOperand()), new AECoercionColumnInfo(getRightOperand()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEAdd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */