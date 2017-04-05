package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AEColumnReference
  extends AEValueExpr
{
  private final AEColumnInfo m_columnInfo;
  
  public AEColumnReference(AEColumnInfo paramAEColumnInfo)
  {
    this.m_columnInfo = new AEColumnInfo(paramAEColumnInfo);
  }
  
  public AEColumnReference(AEColumnReference paramAEColumnReference)
  {
    super(paramAEColumnReference);
    this.m_columnInfo = new AEColumnInfo(paramAEColumnReference.m_columnInfo);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEColumnReference copy()
  {
    return new AEColumnReference(this);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    List localList = Collections.emptyList();
    return localList.iterator();
  }
  
  public IColumn getColumn()
  {
    return this.m_columnInfo.getColumn();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEColumnReference)) {
      return false;
    }
    AEColumnReference localAEColumnReference = (AEColumnReference)paramIAENode;
    return (getNamedRelationalExpr() == localAEColumnReference.getNamedRelationalExpr()) && (getColumnNum() == localAEColumnReference.getColumnNum());
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof AEColumnReference)) {
      return false;
    }
    return this.m_columnInfo.equals(((AEColumnReference)paramObject).m_columnInfo);
  }
  
  public int hashCode()
  {
    return this.m_columnInfo.hashCode();
  }
  
  public int getColumnNum()
  {
    return this.m_columnInfo.getColumnNum();
  }
  
  public void setColumnNum(int paramInt)
  {
    this.m_columnInfo.setColumnNum(paramInt);
  }
  
  public AENamedRelationalExpr getNamedRelationalExpr()
  {
    return this.m_columnInfo.getNamedRelationalExpr();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName() + ": " + getQColumnName();
  }
  
  public AEQueryScope getResolvedQueryScope()
  {
    return this.m_columnInfo.getResolvedQueryScope();
  }
  
  public boolean isOuterReference()
  {
    return this.m_columnInfo.isOuterReference();
  }
  
  public void updateColumn()
    throws ErrorException
  {}
  
  public void setNamedRelationalExpr(AENamedRelationalExpr paramAENamedRelationalExpr)
  {
    this.m_columnInfo.setNamedRelationalExpr(paramAENamedRelationalExpr);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEColumnReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */