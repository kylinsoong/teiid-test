package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEInsert
  extends AERowCountStatement
{
  private static final int NUM_CHILDREN = 2;
  private AETable m_table;
  private AEValueExprList m_insertColumns;
  private AERelationalExpr m_relExpr;
  private boolean m_hasBeenValidated = false;
  private boolean m_isRecursive;
  private boolean m_hasCalculatedIfRecursive;
  private AEParameterContainer m_params = null;
  
  public AEInsert(AETable paramAETable, AEValueExprList paramAEValueExprList, AERelationalExpr paramAERelationalExpr, SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    this.m_table = paramAETable;
    this.m_table.setParent(this);
    this.m_insertColumns = paramAEValueExprList;
    this.m_insertColumns.setParent(this);
    this.m_relExpr = paramAERelationalExpr;
    this.m_relExpr.setParent(this);
    this.m_isRecursive = false;
    this.m_hasCalculatedIfRecursive = false;
    validate();
  }
  
  private AEInsert(AEInsert paramAEInsert)
  {
    this.m_table = paramAEInsert.m_table.copy();
    this.m_table.setParent(this);
    this.m_insertColumns = paramAEInsert.m_insertColumns.copy();
    this.m_insertColumns.setParent(this);
    this.m_relExpr = paramAEInsert.m_relExpr.copy();
    this.m_relExpr.setParent(this);
    this.m_isRecursive = paramAEInsert.m_isRecursive;
    this.m_hasCalculatedIfRecursive = paramAEInsert.m_hasCalculatedIfRecursive;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEInsert copy()
  {
    AEInsert localAEInsert = new AEInsert(this);
    AETreeCopyUtil.updateColumns(localAEInsert);
    return localAEInsert;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEInsert.this.m_table;
        }
        if (1 == paramAnonymousInt) {
          return AEInsert.this.m_relExpr;
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 2;
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return "AEInsert";
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public AEValueExprList getInsertColumns()
  {
    return this.m_insertColumns;
  }
  
  public AERelationalExpr getRelationalExpr()
  {
    return this.m_relExpr;
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
    if (!(paramIAENode instanceof AEInsert)) {
      return false;
    }
    AEInsert localAEInsert = (AEInsert)paramIAENode;
    return (this.m_table.isEquivalent(localAEInsert.m_table)) && (this.m_insertColumns.isEquivalent(localAEInsert.m_insertColumns)) && (this.m_relExpr.isEquivalent(localAEInsert.m_relExpr));
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {
    this.m_relExpr.acceptVisitor(getMetadataProcessor());
    validate();
  }
  
  public boolean isRecursive()
  {
    if (!this.m_hasCalculatedIfRecursive)
    {
      this.m_isRecursive = false;
      AETreeWalker localAETreeWalker = new AETreeWalker(this.m_relExpr);
      while (localAETreeWalker.hasNext())
      {
        IAENode localIAENode = localAETreeWalker.next();
        if ((localIAENode instanceof AETable))
        {
          AETable localAETable = (AETable)localIAENode;
          if ((localAETable.getTableName().equals(this.m_table.getTableName())) && (localAETable.getSchemaName().equals(this.m_table.getSchemaName())) && (localAETable.getCatalogName().equals(this.m_table.getCatalogName())))
          {
            this.m_isRecursive = true;
            break;
          }
        }
      }
    }
    return this.m_isRecursive;
  }
  
  public void notifyDataNeeded()
    throws ErrorException
  {
    for (int i = 0; i < this.m_relExpr.getColumnCount(); i++) {
      this.m_relExpr.setDataNeeded(this.m_relExpr, i);
    }
    this.m_relExpr.setDataNeededOnChild();
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
  
  private void checkDuplicateColumns()
    throws ErrorException
  {
    int i = this.m_insertColumns.getNumChildren();
    for (int j = 0; j < i - 1; j++)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)this.m_insertColumns.getChild(j);
      assert ((localAEValueExpr instanceof AEColumnReference));
      for (int k = j + 1; k < i; k++) {
        if (localAEValueExpr.isEquivalent(this.m_insertColumns.getChild(k))) {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.DUPLICATE_INSERT_COLUMN.name(), new String[] { localAEValueExpr.getName() });
        }
      }
    }
  }
  
  private void validate()
    throws ErrorException
  {
    if (!this.m_hasBeenValidated)
    {
      validateNumberOfColumns();
      checkDuplicateColumns();
    }
    this.m_hasBeenValidated = true;
  }
  
  private void validateNumberOfColumns()
    throws ErrorException
  {
    int i;
    if (0 == this.m_insertColumns.getNumChildren()) {
      i = this.m_table.getColumnCount();
    } else {
      i = this.m_insertColumns.getNumChildren();
    }
    if (this.m_relExpr.getColumnCount() != i) {
      throw new SQLEngineException(DiagState.DIAG_INSERT_VAL_LIST_COL_LIST_MISMATCH, SQLEngineMessageKey.INVALID_NUMBER_INSERT_VALUES.name(), new String[] { "" + this.m_relExpr.getColumnCount(), "" + i });
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEInsert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */