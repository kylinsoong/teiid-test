package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDefault;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEUpdate
  extends AERowCountStatement
{
  private static final int NUM_CHILDREN = 3;
  private AETable m_table;
  private AESetClauseList m_setClauses;
  private AEBooleanExpr m_updateCond;
  private AEParameterContainer m_params = null;
  private final boolean m_doInsertOnRC0;
  
  public AEUpdate(AETable paramAETable, AESetClauseList paramAESetClauseList, AEBooleanExpr paramAEBooleanExpr, boolean paramBoolean)
    throws ErrorException
  {
    if ((null == paramAETable) || (null == paramAESetClauseList) || (null == paramAEBooleanExpr)) {
      throw new NullPointerException();
    }
    this.m_doInsertOnRC0 = paramBoolean;
    this.m_table = paramAETable;
    this.m_table.setParent(this);
    this.m_setClauses = paramAESetClauseList;
    this.m_setClauses.setParent(this);
    this.m_updateCond = paramAEBooleanExpr;
    this.m_updateCond.setParent(this);
    updateDefaultMetadata();
    validate();
  }
  
  private AEUpdate(AEUpdate paramAEUpdate)
  {
    this.m_table = paramAEUpdate.m_table.copy();
    this.m_table.setParent(this);
    this.m_setClauses = paramAEUpdate.m_setClauses.copy();
    this.m_setClauses.setParent(this);
    this.m_updateCond = paramAEUpdate.m_updateCond.copy();
    this.m_updateCond.setParent(this);
    this.m_doInsertOnRC0 = paramAEUpdate.m_doInsertOnRC0;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEUpdate copy()
  {
    AEUpdate localAEUpdate = new AEUpdate(this);
    AETreeCopyUtil.updateColumns(localAEUpdate);
    return localAEUpdate;
  }
  
  public boolean doInsertOnRc0()
  {
    return this.m_doInsertOnRC0;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEUpdate.this.m_table;
        }
        if (1 == paramAnonymousInt) {
          return AEUpdate.this.m_setClauses;
        }
        if (2 == paramAnonymousInt) {
          return AEUpdate.this.m_updateCond;
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 3;
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return this.m_doInsertOnRC0 ? "AEUpsert" : "AEUpdate";
  }
  
  public int getNumChildren()
  {
    return 3;
  }
  
  public AETable getTable()
  {
    return this.m_table;
  }
  
  public AESetClauseList getSetClauses()
  {
    return this.m_setClauses;
  }
  
  public AEBooleanExpr getUpdateCondition()
  {
    return this.m_updateCond;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    return (this == paramIAENode) || (((paramIAENode instanceof AEUpdate)) && (this.m_table.isEquivalent(((AEUpdate)paramIAENode).m_table)) && (this.m_setClauses.isEquivalent(((AEUpdate)paramIAENode).m_setClauses)) && (this.m_updateCond.isEquivalent(((AEUpdate)paramIAENode).m_updateCond)));
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {
    getMetadataProcessor().visit(this);
  }
  
  public void notifyDataNeeded()
    throws ErrorException
  {
    AEDefaultVisitor local2 = new AEDefaultVisitor()
    {
      public Void visit(AEColumnReference paramAnonymousAEColumnReference)
        throws ErrorException
      {
        AEUpdate.this.m_table.setDataNeeded(AEUpdate.this.m_table, paramAnonymousAEColumnReference.getColumnNum());
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
    this.m_updateCond.acceptVisitor(local2);
    Iterator localIterator = this.m_setClauses.getChildItr();
    while (localIterator.hasNext())
    {
      AESetClause localAESetClause = (AESetClause)localIterator.next();
      localAESetClause.getRightOperand().acceptVisitor(local2);
    }
    this.m_table.setDataNeededOnChild();
  }
  
  public void setTable(AETable paramAETable)
  {
    if (paramAETable == null) {
      throw new NullPointerException("Table to update can not be null.");
    }
    this.m_table = paramAETable;
  }
  
  public void setUpdateCondition(AEBooleanExpr paramAEBooleanExpr)
  {
    if (paramAEBooleanExpr == null) {
      throw new NullPointerException("Update condition can not be null.");
    }
    this.m_updateCond = paramAEBooleanExpr;
  }
  
  private void updateDefaultMetadata()
    throws ErrorException
  {
    Iterator localIterator = this.m_setClauses.getChildItr();
    while (localIterator.hasNext())
    {
      AESetClause localAESetClause = (AESetClause)localIterator.next();
      AEValueExpr localAEValueExpr = localAESetClause.getRightOperand();
      if ((localAEValueExpr instanceof AEDefault))
      {
        AEDefault localAEDefault = (AEDefault)localAEValueExpr;
        localAEDefault.setMetadata(localAESetClause.getLeftOperand().getColumn());
      }
    }
  }
  
  private void validate()
    throws ErrorException
  {
    int i = this.m_setClauses.getNumChildren();
    for (int j = 0; j < i - 1; j++)
    {
      AEColumnReference localAEColumnReference = ((AESetClause)this.m_setClauses.getChild(j)).getLeftOperand();
      for (int k = j + 1; k < i; k++) {
        if (localAEColumnReference.isEquivalent(((AESetClause)this.m_setClauses.getChild(k)).getLeftOperand())) {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.DUPLICATE_UPDATE_COLUMN.name(), new String[] { localAEColumnReference.getName() });
        }
      }
    }
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEUpdate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */