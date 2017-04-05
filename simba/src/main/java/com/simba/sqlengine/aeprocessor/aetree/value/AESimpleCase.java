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

public class AESimpleCase
  extends AEValueExpr
{
  private AEValueExpr m_caseOperand;
  private AENodeList<AESimpleWhenClause> m_whenClauseList;
  private AEValueExpr m_elseOperand;
  private ColumnMetadata m_metadata = null;
  private final ICoercionHandler m_coercionHandler;
  
  public AESimpleCase(ICoercionHandler paramICoercionHandler, AEValueExpr paramAEValueExpr1, AENodeList<AESimpleWhenClause> paramAENodeList, AEValueExpr paramAEValueExpr2)
    throws ErrorException
  {
    this.m_coercionHandler = paramICoercionHandler;
    this.m_caseOperand = paramAEValueExpr1;
    this.m_whenClauseList = paramAENodeList;
    this.m_elseOperand = paramAEValueExpr2;
    paramAEValueExpr1.setParent(this);
    paramAENodeList.setParent(this);
    paramAEValueExpr2.setParent(this);
    this.m_metadata = initializeMetadata();
  }
  
  public AESimpleCase(AESimpleCase paramAESimpleCase)
  {
    this.m_coercionHandler = paramAESimpleCase.m_coercionHandler;
    this.m_caseOperand = paramAESimpleCase.m_caseOperand.copy();
    this.m_whenClauseList = paramAESimpleCase.m_whenClauseList.copy();
    this.m_elseOperand = paramAESimpleCase.m_elseOperand.copy();
    this.m_caseOperand.setParent(this);
    this.m_whenClauseList.setParent(this);
    this.m_elseOperand.setParent(this);
    this.m_metadata = ColumnMetadata.copyOf(paramAESimpleCase.m_metadata);
  }
  
  public AEValueExpr getCaseOperand()
  {
    return this.m_caseOperand;
  }
  
  public void setCaseOperand(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("Cannot set case operand to null");
    }
    this.m_caseOperand = paramAEValueExpr;
    this.m_caseOperand.setParent(this);
  }
  
  public AENodeList<AESimpleWhenClause> getWhenClauseList()
  {
    return this.m_whenClauseList;
  }
  
  public AEValueExpr getElseOperand()
  {
    return this.m_elseOperand;
  }
  
  public void setElseOperand(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("Cannot set else operand to null");
    }
    this.m_elseOperand = paramAEValueExpr;
    this.m_elseOperand.setParent(this);
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
          return AESimpleCase.this.getCaseOperand();
        }
        if (1 == paramAnonymousInt) {
          return AESimpleCase.this.getWhenClauseList();
        }
        if (2 == paramAnonymousInt) {
          return AESimpleCase.this.getElseOperand();
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int size()
      {
        return AESimpleCase.this.getNumChildren();
      }
    }.iterator();
  }
  
  public int getNumChildren()
  {
    return 3;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AESimpleCase)) {
      return false;
    }
    AESimpleCase localAESimpleCase = (AESimpleCase)paramIAENode;
    return (this.m_elseOperand.isEquivalent(localAESimpleCase.m_elseOperand)) && (this.m_caseOperand.isEquivalent(localAESimpleCase.m_caseOperand)) && (this.m_whenClauseList.isEquivalent(localAESimpleCase.m_whenClauseList));
  }
  
  public IColumn getColumn()
  {
    return this.m_metadata;
  }
  
  public AESimpleCase copy()
  {
    return new AESimpleCase(this);
  }
  
  public void updateColumn()
    throws ErrorException
  {
    this.m_metadata = initializeMetadata();
  }
  
  private ColumnMetadata initializeMetadata()
    throws ErrorException
  {
    AECoercionColumnInfo localAECoercionColumnInfo = new AECoercionColumnInfo(this.m_caseOperand);
    AEValueExprList localAEValueExprList1 = new AEValueExprList();
    AEValueExprList localAEValueExprList2 = new AEValueExprList();
    Object localObject1 = this.m_whenClauseList.getChildItr();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (AESimpleWhenClause)((Iterator)localObject1).next();
      localAEValueExprList1.addNode(((AESimpleWhenClause)localObject2).getWhenExpression());
      localAEValueExprList2.addNode(((AESimpleWhenClause)localObject2).getThenExpression());
    }
    localAEValueExprList1.addNode(this.m_caseOperand);
    localAEValueExprList2.addNode(this.m_elseOperand);
    localObject1 = new ArrayList();
    Object localObject2 = this.m_whenClauseList.getChildItr();
    Object localObject3;
    Object localObject4;
    Object localObject5;
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (AESimpleWhenClause)((Iterator)localObject2).next();
      localObject4 = this.m_coercionHandler.coerceComparisonColumns(localAECoercionColumnInfo, new AECoercionColumnInfo(((AESimpleWhenClause)localObject3).getWhenExpression()));
      ((AESimpleWhenClause)localObject3).setComparisonMetadata((IColumn)localObject4);
      localObject5 = ((AESimpleWhenClause)localObject3).getThenExpression();
      if (!(localObject5 instanceof AENull)) {
        ((ArrayList)localObject1).add(localObject5);
      }
    }
    if (!(this.m_elseOperand instanceof AENull)) {
      ((ArrayList)localObject1).add(this.m_elseOperand);
    }
    if (((ArrayList)localObject1).isEmpty())
    {
      localObject2 = ColumnMetadata.copyOf(new AENull().getColumn());
    }
    else
    {
      localObject3 = ((ArrayList)localObject1).iterator();
      localObject4 = (AEValueExpr)((Iterator)localObject3).next();
      localObject2 = ColumnMetadata.copyOf(((AEValueExpr)localObject4).getColumn());
      for (localObject5 = new AECoercionColumnInfo((AEValueExpr)localObject4); ((Iterator)localObject3).hasNext(); localObject5 = new MetadataColumnInfo((IColumn)localObject2, IColumnInfo.ColumnType.COLUMN)) {
        localObject2 = ColumnMetadata.copyOf(this.m_coercionHandler.coerceUnionColumns((IColumnInfo)localObject5, new AECoercionColumnInfo((AEValueExpr)((Iterator)localObject3).next())));
      }
    }
    for (int i = 0; i < this.m_whenClauseList.getNumChildren(); i++)
    {
      localObject4 = (AESimpleWhenClause)this.m_whenClauseList.getChild(i);
      ((AESimpleWhenClause)localObject4).setWhenExpression((AEValueExpr)localAEValueExprList1.getChild(i));
      ((AESimpleWhenClause)localObject4).setThenExpression((AEValueExpr)localAEValueExprList2.getChild(i));
    }
    ((ColumnMetadata)localObject2).setName(null);
    return (ColumnMetadata)localObject2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AESimpleCase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */