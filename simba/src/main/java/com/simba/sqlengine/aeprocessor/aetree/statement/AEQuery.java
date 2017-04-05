package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEQuery
  implements IAEStatement, IAEUnaryNode<AERelationalExpr>
{
  private AERelationalExpr m_relationalExpr;
  private ArrayList<IColumn> m_metadata = null;
  private AEParameterContainer m_params;
  
  public AEQuery(AERelationalExpr paramAERelationalExpr)
  {
    assert (null != paramAERelationalExpr);
    this.m_relationalExpr = paramAERelationalExpr;
    this.m_relationalExpr.setParent(this);
  }
  
  private AEQuery(AEQuery paramAEQuery)
  {
    this.m_relationalExpr = paramAEQuery.m_relationalExpr.copy();
    this.m_relationalExpr.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEQuery copy()
  {
    AEQuery localAEQuery = new AEQuery(this);
    AETreeCopyUtil.updateColumns(localAEQuery);
    return localAEQuery;
  }
  
  public Iterator<IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEQuery.this.getOperand();
        }
        throw new IndexOutOfBoundsException("Illegal index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return AEQuery.this.getNumChildren();
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public String toString()
  {
    return getLogString() + ": " + getOperand().getLogString();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public IAENode getParent()
  {
    return null;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    throw new UnsupportedOperationException("AEQuery does not have parent.");
  }
  
  public void setOperand(AERelationalExpr paramAERelationalExpr)
  {
    if (paramAERelationalExpr == null) {
      throw new NullPointerException("Operand of AEQuery can not be null.");
    }
    this.m_relationalExpr = paramAERelationalExpr;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEQuery)) {
      return false;
    }
    AEQuery localAEQuery = (AEQuery)paramIAENode;
    return localAEQuery.m_relationalExpr.isEquivalent(this.m_relationalExpr);
  }
  
  public AERelationalExpr getOperand()
  {
    return this.m_relationalExpr;
  }
  
  public ArrayList<IColumn> createResultSetColumns()
  {
    if (null == this.m_metadata) {
      this.m_metadata = getOperand().createResultSetColumns();
    }
    return this.m_metadata;
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {
    getOperand().acceptVisitor(StatementMetadataProcessor.getInstance());
    this.m_metadata = null;
  }
  
  public void notifyDataNeeded()
    throws ErrorException
  {
    for (int i = 0; i < getOperand().getColumnCount(); i++) {
      getOperand().setDataNeeded(getOperand(), i);
    }
    getOperand().setDataNeededOnChild();
  }
  
  public List<AEParameter> getDynamicParameters()
  {
    if (this.m_params == null)
    {
      this.m_params = new AEParameterContainer();
      this.m_params.initialize(this);
    }
    return this.m_params.getParameters();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */