package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public class AEUnion
  extends AESetOperation
{
  private List<ColumnMetadata> m_columnMetadataList;
  
  public AEUnion(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, boolean paramBoolean, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    this(paramAERelationalExpr1, paramAERelationalExpr2, paramBoolean, calculateMetadata(paramAERelationalExpr1, paramAERelationalExpr2, paramICoercionHandler));
  }
  
  private AEUnion(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, boolean paramBoolean, List<ColumnMetadata> paramList)
  {
    super(paramAERelationalExpr1, paramAERelationalExpr2, paramBoolean);
    this.m_columnMetadataList = paramList;
  }
  
  private AEUnion(AEUnion paramAEUnion)
  {
    super(paramAEUnion);
    this.m_columnMetadataList = new ArrayList(paramAEUnion.m_columnMetadataList);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEUnion copy()
  {
    return new AEUnion(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_columnMetadataList.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_columnMetadataList.size();
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
    if (!(paramIAENode instanceof AEUnion)) {
      return false;
    }
    AEUnion localAEUnion = (AEUnion)paramIAENode;
    return (isAllOptPresent() == localAEUnion.isAllOptPresent()) && (getLeftOperand().isEquivalent(localAEUnion.getLeftOperand())) && (getRightOperand().isEquivalent(localAEUnion.getRightOperand()));
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getLeftOperand();
    }
    if (1 == paramInt) {
      return getRightOperand();
    }
    throw new IndexOutOfBoundsException("" + paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEUnion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */