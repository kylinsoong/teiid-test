package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AENodeList;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

public class AESearchedCase
  extends AEValueExpr
{
  private AENodeList<AESearchedWhenClause> m_whenClauseList;
  private AEValueExpr m_elseClause;
  private ColumnMetadata m_metadata = null;
  private final ICoercionHandler m_coercionHandler;
  
  public AESearchedCase(ICoercionHandler paramICoercionHandler, AENodeList<AESearchedWhenClause> paramAENodeList, AEValueExpr paramAEValueExpr)
    throws ErrorException
  {
    this.m_coercionHandler = paramICoercionHandler;
    this.m_whenClauseList = paramAENodeList;
    this.m_elseClause = paramAEValueExpr;
    paramAENodeList.setParent(this);
    paramAEValueExpr.setParent(this);
    this.m_metadata = initializeMetadata();
  }
  
  private AESearchedCase(AESearchedCase paramAESearchedCase)
  {
    this.m_coercionHandler = paramAESearchedCase.m_coercionHandler;
    this.m_whenClauseList = paramAESearchedCase.m_whenClauseList.copy();
    this.m_elseClause = paramAESearchedCase.m_elseClause.copy();
    this.m_whenClauseList.setParent(this);
    this.m_elseClause.setParent(this);
    ColumnMetadata.copyOf(paramAESearchedCase.m_metadata);
  }
  
  public AENodeList<AESearchedWhenClause> getWhenClauseList()
  {
    return this.m_whenClauseList;
  }
  
  public AEValueExpr getElseClause()
  {
    return this.m_elseClause;
  }
  
  public void setElseClause(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("Cannot set else clause to null");
    }
    this.m_elseClause = paramAEValueExpr;
    this.m_elseClause.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AESearchedCase.this.getWhenClauseList();
        }
        if (1 == paramAnonymousInt) {
          return AESearchedCase.this.getElseClause();
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int size()
      {
        return AESearchedCase.this.getNumChildren();
      }
    }.iterator();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AESearchedCase)) {
      return false;
    }
    AESearchedCase localAESearchedCase = (AESearchedCase)paramIAENode;
    if (!this.m_whenClauseList.isEquivalent(localAESearchedCase.m_whenClauseList)) {
      return false;
    }
    return this.m_elseClause.isEquivalent(localAESearchedCase.m_elseClause);
  }
  
  public IColumn getColumn()
  {
    return this.m_metadata;
  }
  
  public AESearchedCase copy()
  {
    return new AESearchedCase(this);
  }
  
  public void updateColumn()
    throws ErrorException
  {
    this.m_metadata = initializeMetadata();
  }
  
  private ColumnMetadata initializeMetadata()
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    AEValueExprList localAEValueExprList = new AEValueExprList();
    Object localObject1 = this.m_whenClauseList.getChildItr();
    while (((Iterator)localObject1).hasNext()) {
      localAEValueExprList.addNode(((AESearchedWhenClause)((Iterator)localObject1).next()).getThenExpression());
    }
    localAEValueExprList.addNode(this.m_elseClause);
    localObject1 = this.m_whenClauseList.getChildItr();
    Object localObject2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = ((AESearchedWhenClause)((Iterator)localObject1).next()).getThenExpression();
      if (!(localObject2 instanceof AENull)) {
        localArrayList.add(localObject2);
      }
    }
    if (!(this.m_elseClause instanceof AENull)) {
      localArrayList.add(this.m_elseClause);
    }
    if (localArrayList.isEmpty())
    {
      localObject1 = ColumnMetadata.copyOf(new AENull().getColumn());
    }
    else
    {
      localObject2 = localArrayList.iterator();
      AEValueExpr localAEValueExpr = (AEValueExpr)((Iterator)localObject2).next();
      localObject1 = ColumnMetadata.copyOf(localAEValueExpr.getColumn());
      for (Object localObject3 = new AECoercionColumnInfo(localAEValueExpr); ((Iterator)localObject2).hasNext(); localObject3 = new MetadataColumnInfo((IColumn)localObject1, IColumnInfo.ColumnType.COLUMN)) {
        localObject1 = ColumnMetadata.copyOf(this.m_coercionHandler.coerceUnionColumns((IColumnInfo)localObject3, new AECoercionColumnInfo((AEValueExpr)((Iterator)localObject2).next())));
      }
    }
    for (int i = 0; i < this.m_whenClauseList.getNumChildren(); i++) {
      ((AESearchedWhenClause)this.m_whenClauseList.getChild(i)).setThenExpression((AEValueExpr)localAEValueExprList.getChild(i));
    }
    ((ColumnMetadata)localObject1).setName(null);
    return (ColumnMetadata)localObject1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AESearchedCase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */