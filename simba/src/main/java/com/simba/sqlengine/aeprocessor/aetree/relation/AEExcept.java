package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AEExcept
  extends AESetOperation
{
  private final List<ColumnMetadata> m_colMeta;
  
  public AEExcept(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, boolean paramBoolean, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    super(paramAERelationalExpr1, paramAERelationalExpr2, paramBoolean);
    this.m_colMeta = calculateMetadata(paramAERelationalExpr1, paramAERelationalExpr2, paramICoercionHandler);
  }
  
  public AEExcept(AEExcept paramAEExcept)
  {
    super(paramAEExcept);
    this.m_colMeta = new ArrayList(paramAEExcept.m_colMeta.size());
    Collections.copy(this.m_colMeta, paramAEExcept.m_colMeta);
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
    if (!(paramIAENode instanceof AEExcept)) {
      return false;
    }
    AEExcept localAEExcept = (AEExcept)paramIAENode;
    return (isAllOptPresent() == localAEExcept.isAllOptPresent()) && (getLeftOperand().isEquivalent(localAEExcept.getLeftOperand())) && (getRightOperand().isEquivalent(localAEExcept.getRightOperand()));
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_colMeta.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_colMeta.size();
  }
  
  public AEExcept copy()
  {
    return new AEExcept(this);
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getLeftOperand();
    }
    if (1 == paramInt) {
      return getRightOperand();
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEExcept.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */