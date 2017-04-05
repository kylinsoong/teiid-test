package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEDelete
  extends AERowCountStatement
{
  private static final int NUM_CHILDREN = 2;
  private AETable m_table;
  private AEBooleanExpr m_cond;
  private AEParameterContainer m_params = null;
  
  public AEDelete(AETable paramAETable, AEBooleanExpr paramAEBooleanExpr)
  {
    if (null == paramAETable) {
      throw new NullPointerException("table may not be null.");
    }
    if (null == paramAEBooleanExpr) {
      throw new NullPointerException("cond may not be null");
    }
    this.m_table = paramAETable;
    paramAETable.setParent(this);
    this.m_cond = paramAEBooleanExpr;
    paramAEBooleanExpr.setParent(this);
  }
  
  private AEDelete(AEDelete paramAEDelete)
  {
    this.m_table = paramAEDelete.m_table.copy();
    this.m_cond = paramAEDelete.m_cond.copy();
    this.m_table.setParent(this);
    this.m_cond.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEDelete copy()
  {
    AEDelete localAEDelete = new AEDelete(this);
    AETreeCopyUtil.updateColumns(localAEDelete);
    return localAEDelete;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEDelete.this.m_table;
        }
        if (1 == paramAnonymousInt) {
          return AEDelete.this.m_cond;
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 2;
      }
    }.iterator();
  }
  
  public AEBooleanExpr getCondition()
  {
    return this.m_cond;
  }
  
  public String getLogString()
  {
    return "AEDelete";
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public AETable getTable()
  {
    return this.m_table;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEDelete)) {
      return false;
    }
    AEDelete localAEDelete = (AEDelete)paramIAENode;
    return (this.m_table.isEquivalent(localAEDelete.m_table)) && (this.m_cond.isEquivalent(localAEDelete.m_cond));
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {
    this.m_cond.acceptVisitor(getMetadataProcessor());
  }
  
  public void notifyDataNeeded()
    throws ErrorException
  {
    AEDefaultVisitor local2 = new AEDefaultVisitor()
    {
      public Void visit(AEColumnReference paramAnonymousAEColumnReference)
        throws ErrorException
      {
        AEDelete.this.m_table.setDataNeeded(AEDelete.this.m_table, paramAnonymousAEColumnReference.getColumnNum());
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
    this.m_cond.acceptVisitor(local2);
    this.m_table.setDataNeededOnChild();
  }
  
  public void setTable(AETable paramAETable)
  {
    if (paramAETable == null) {
      throw new NullPointerException("Table to update can not be null.");
    }
    this.m_table = paramAETable;
  }
  
  public void setCondition(AEBooleanExpr paramAEBooleanExpr)
  {
    if (paramAEBooleanExpr == null) {
      throw new NullPointerException("Update condition can not be null.");
    }
    this.m_cond = paramAEBooleanExpr;
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEDelete.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */