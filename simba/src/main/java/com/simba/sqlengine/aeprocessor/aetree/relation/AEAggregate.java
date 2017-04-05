package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AEAggregate
  extends AEUnaryRelationalExpr
{
  private AEValueExprList m_groupingList = null;
  private Map<Integer, Integer> m_groupingListOrdinals;
  private AEValueExprList m_aggregateList;
  private AEQueryScope m_queryScope;
  
  public AEAggregate(AERelationalExpr paramAERelationalExpr, AEValueExprList paramAEValueExprList1, Map<Integer, Integer> paramMap, AEValueExprList paramAEValueExprList2, AEQueryScope paramAEQueryScope)
  {
    super(paramAERelationalExpr);
    assert (paramAEValueExprList2 != null);
    if (paramAEValueExprList1 != null)
    {
      this.m_groupingList = paramAEValueExprList1;
      this.m_groupingList.setParent(this);
    }
    this.m_aggregateList = paramAEValueExprList2;
    this.m_aggregateList.setParent(this);
    this.m_groupingListOrdinals = paramMap;
    this.m_queryScope = paramAEQueryScope;
  }
  
  private AEAggregate(AEAggregate paramAEAggregate)
  {
    super(paramAEAggregate);
    this.m_groupingList = (paramAEAggregate.hasGroupingList() ? paramAEAggregate.m_groupingList.copy() : null);
    this.m_aggregateList = paramAEAggregate.m_aggregateList.copy();
    if (null != paramAEAggregate.m_groupingListOrdinals) {
      this.m_groupingListOrdinals = new HashMap(paramAEAggregate.m_groupingListOrdinals);
    }
    this.m_queryScope = paramAEAggregate.m_queryScope;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public int getColumnCount()
  {
    return this.m_aggregateList.getNumChildren();
  }
  
  public IColumn getColumn(int paramInt)
  {
    return ((AEValueExpr)this.m_aggregateList.getChild(paramInt)).getColumn();
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AEAggregate)) {
      return false;
    }
    AEAggregate localAEAggregate = (AEAggregate)paramIAENode;
    if (hasGroupingList())
    {
      if ((!localAEAggregate.hasGroupingList()) || (!this.m_groupingList.isEquivalent(localAEAggregate.m_groupingList))) {
        return false;
      }
    }
    else if (localAEAggregate.hasGroupingList()) {
      return false;
    }
    return (getOperand().isEquivalent(localAEAggregate.getOperand())) && (this.m_aggregateList.isEquivalent(localAEAggregate.m_aggregateList));
  }
  
  public int getNumChildren()
  {
    if (hasGroupingList()) {
      return 3;
    }
    return 2;
  }
  
  public AEAggregate copy()
  {
    return new AEAggregate(this);
  }
  
  public boolean hasGroupingList()
  {
    return this.m_groupingList != null;
  }
  
  protected IAENode getChild(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return getOperand();
    case 1: 
      return this.m_aggregateList;
    case 2: 
      if (hasGroupingList()) {
        return this.m_groupingList;
      }
      throw new IndexOutOfBoundsException();
    }
    throw new IndexOutOfBoundsException();
  }
  
  public AEValueExprList getGroupingList()
  {
    return this.m_groupingList;
  }
  
  public AEValueExprList getAggregationList()
  {
    return this.m_aggregateList;
  }
  
  public AEQueryScope getQueryScope()
  {
    return this.m_queryScope;
  }
  
  public Map<Integer, Integer> getGroupingListOrdinalMap()
  {
    return this.m_groupingListOrdinals;
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[this.m_aggregateList.getNumChildren()];
      this.m_shouldUpdateDNTracker = false;
    }
    return this.m_dataNeeded[paramInt];
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[this.m_aggregateList.getNumChildren()];
      this.m_shouldUpdateDNTracker = false;
    }
    if (paramAERelationalExpr.equals(this))
    {
      this.m_dataNeeded[paramInt] = true;
      SetDataNeededVisitor localSetDataNeededVisitor = new SetDataNeededVisitor(null);
      ((AEValueExpr)this.m_aggregateList.getChild(paramInt)).acceptVisitor(localSetDataNeededVisitor);
      return paramInt;
    }
    getOperand().setDataNeeded(paramAERelationalExpr, paramInt);
    return -1;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    SetDataNeededVisitor localSetDataNeededVisitor = new SetDataNeededVisitor(null);
    if (null != this.m_groupingList) {
      this.m_groupingList.acceptVisitor(localSetDataNeededVisitor);
    }
    getOperand().setDataNeededOnChild();
  }
  
  private class SetDataNeededVisitor
    extends AEDefaultVisitor<Void>
  {
    private SetDataNeededVisitor() {}
    
    public Void visit(AEColumnReference paramAEColumnReference)
      throws ErrorException
    {
      if (!paramAEColumnReference.isOuterReference()) {
        AEAggregate.this.getOperand().setDataNeeded(paramAEColumnReference.getNamedRelationalExpr(), paramAEColumnReference.getColumnNum());
      } else {
        paramAEColumnReference.getNamedRelationalExpr().setDataNeeded(paramAEColumnReference.getNamedRelationalExpr(), paramAEColumnReference.getColumnNum());
      }
      return null;
    }
    
    public Void visit(AEProxyColumn paramAEProxyColumn)
      throws ErrorException
    {
      assert (AEAggregate.this.equals(paramAEProxyColumn.getRelationalExpr()));
      AEAggregate.this.setDataNeeded(paramAEProxyColumn.getRelationalExpr(), paramAEProxyColumn.getColumnNumber());
      return null;
    }
    
    protected Void defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      Iterator localIterator = paramIAENode.getChildItr();
      while (localIterator.hasNext()) {
        ((IAENode)localIterator.next()).acceptVisitor(this);
      }
      return null;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEAggregate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */