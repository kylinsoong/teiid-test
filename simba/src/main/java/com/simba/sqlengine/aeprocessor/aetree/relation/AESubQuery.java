package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AESubQuery
  extends AENamedRelationalExpr
  implements IAEUnaryNode<AERelationalExpr>
{
  private boolean m_isCorrelated;
  private boolean m_isInFrom;
  private AERelationalExpr m_operand;
  private boolean[] m_dataNeeded;
  protected boolean m_shouldUpdateDNTracker = true;
  
  public AESubQuery(AERelationalExpr paramAERelationalExpr, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.m_operand = paramAERelationalExpr;
    this.m_operand.setParent(this);
    this.m_isCorrelated = paramBoolean1;
    this.m_isInFrom = paramBoolean2;
  }
  
  protected AESubQuery(AESubQuery paramAESubQuery)
  {
    super(paramAESubQuery);
    this.m_shouldUpdateDNTracker = true;
    this.m_operand = paramAESubQuery.getOperand().copy();
    this.m_operand.setParent(this);
    this.m_isCorrelated = paramAESubQuery.m_isCorrelated;
    this.m_isInFrom = paramAESubQuery.m_isInFrom;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean hasCorrelationName()
  {
    String str = getCorrelationName();
    return (null != str) && (!"".equals(str));
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isCorrelated()
  {
    return this.m_isCorrelated;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AESubQuery)) {
      return false;
    }
    return ((AESubQuery)paramIAENode).getOperand().isEquivalent(getOperand());
  }
  
  public boolean isInFromClause()
  {
    return this.m_isInFrom;
  }
  
  public AESubQuery copy()
  {
    return new AESubQuery(this);
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getOperand();
    }
    throw new IndexOutOfBoundsException();
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public AERelationalExpr getOperand()
  {
    return this.m_operand;
  }
  
  public String getCatalogName()
  {
    return "";
  }
  
  public String getSchemaName()
  {
    return "";
  }
  
  public String getTableName()
  {
    return getCorrelationName();
  }
  
  public IColumn getBaseColumn(int paramInt)
  {
    return this.m_operand.getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_operand.getColumnCount();
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[this.m_operand.getColumnCount()];
      this.m_shouldUpdateDNTracker = false;
    }
    return this.m_dataNeeded[paramInt];
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[this.m_operand.getColumnCount()];
      this.m_shouldUpdateDNTracker = false;
    }
    if (paramAERelationalExpr.equals(this))
    {
      this.m_dataNeeded[paramInt] = true;
      getOperand().setDataNeeded(getOperand(), paramInt);
      return paramInt;
    }
    getOperand().setDataNeeded(getOperand(), paramInt);
    return -1;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    getOperand().setDataNeededOnChild();
  }
  
  public void setOperand(AERelationalExpr paramAERelationalExpr)
  {
    if (paramAERelationalExpr == null) {
      throw new NullPointerException("null is set for sub-query operand.");
    }
    this.m_operand = paramAERelationalExpr;
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        return AESubQuery.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return AESubQuery.this.getNumChildren();
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AESubQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */