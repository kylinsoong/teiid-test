package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEProject
  extends AEUnaryRelationalExpr
{
  private static final int NUM_CHILDREN = 2;
  private AEValueExprList m_projectList;
  
  public AEProject(AEValueExprList paramAEValueExprList, AERelationalExpr paramAERelationalExpr)
  {
    super(paramAERelationalExpr);
    this.m_projectList = paramAEValueExprList;
    this.m_projectList.setParent(this);
  }
  
  private AEProject(AEProject paramAEProject)
  {
    super(paramAEProject);
    this.m_projectList = paramAEProject.m_projectList.copy();
    this.m_projectList.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEProject copy()
  {
    return new AEProject(this);
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
    if (!(paramIAENode instanceof AEProject)) {
      return false;
    }
    AEProject localAEProject = (AEProject)paramIAENode;
    return (this.m_projectList.isEquivalent(localAEProject.m_projectList)) && (getOperand().isEquivalent(localAEProject.getOperand()));
  }
  
  public AEValueExprList getProjectionList()
  {
    return this.m_projectList;
  }
  
  public IColumn getColumn(int paramInt)
  {
    return ((AEValueExpr)this.m_projectList.getChild(paramInt)).getColumn();
  }
  
  public int getColumnCount()
  {
    return this.m_projectList.getNumChildren();
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[this.m_projectList.getNumChildren()];
      this.m_shouldUpdateDNTracker = false;
    }
    if (paramAERelationalExpr.equals(this))
    {
      this.m_dataNeeded[paramInt] = true;
      AEDefaultVisitor local1 = new AEDefaultVisitor()
      {
        public Void visit(AEColumnReference paramAnonymousAEColumnReference)
          throws ErrorException
        {
          if (!paramAnonymousAEColumnReference.isOuterReference()) {
            AEProject.this.getOperand().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
          } else {
            paramAnonymousAEColumnReference.getNamedRelationalExpr().setDataNeeded(paramAnonymousAEColumnReference.getNamedRelationalExpr(), paramAnonymousAEColumnReference.getColumnNum());
          }
          return null;
        }
        
        public Void visit(AEProxyColumn paramAnonymousAEProxyColumn)
          throws ErrorException
        {
          AEProject.this.getOperand().setDataNeeded(paramAnonymousAEProxyColumn.getRelationalExpr(), paramAnonymousAEProxyColumn.getColumnNumber());
          return null;
        }
        
        protected Void defaultVisit(IAENode paramAnonymousIAENode)
          throws ErrorException
        {
          Iterator localIterator = paramAnonymousIAENode.getChildItr();
          while (localIterator.hasNext()) {
            ((IAENode)localIterator.next()).acceptVisitor(this);
          }
          return null;
        }
      };
      ((AEValueExpr)this.m_projectList.getChild(paramInt)).acceptVisitor(local1);
      return paramInt;
    }
    getOperand().setDataNeeded(paramAERelationalExpr, paramInt);
    return -1;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    getOperand().setDataNeededOnChild();
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getOperand();
    }
    if (1 == paramInt) {
      return getProjectionList();
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEProject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */