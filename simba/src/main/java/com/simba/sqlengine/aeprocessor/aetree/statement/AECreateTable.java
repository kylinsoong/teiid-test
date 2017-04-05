package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.dsiext.dataengine.ddl.TableSpecification;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AECreateTable
  extends AERowCountStatement
{
  private static final int NUM_CHILDREN = 0;
  private TableSpecification m_tableSpecification;
  private boolean m_isIdenCaseSensitive;
  
  public AECreateTable(TableSpecification paramTableSpecification, boolean paramBoolean)
  {
    this.m_tableSpecification = paramTableSpecification;
    this.m_isIdenCaseSensitive = paramBoolean;
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {}
  
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
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 0;
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return "AECreateTable: " + this.m_tableSpecification;
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
    if (!(paramIAENode instanceof AECreateTable)) {
      return false;
    }
    AECreateTable localAECreateTable = (AECreateTable)paramIAENode;
    if (this.m_isIdenCaseSensitive != localAECreateTable.m_isIdenCaseSensitive) {
      return false;
    }
    TableSpecification localTableSpecification = localAECreateTable.m_tableSpecification;
    return (isIdentifierEqual(this.m_tableSpecification.getTableName(), localTableSpecification.getTableName())) && (isIdentifierEqual(this.m_tableSpecification.getSchema(), localTableSpecification.getSchema())) && (isIdentifierEqual(this.m_tableSpecification.getCatalog(), localTableSpecification.getCatalog()));
  }
  
  public AECreateTable copy()
  {
    return new AECreateTable(this.m_tableSpecification, this.m_isIdenCaseSensitive);
  }
  
  private boolean isIdentifierEqual(String paramString1, String paramString2)
  {
    if (this.m_isIdenCaseSensitive) {
      return paramString1.equals(paramString2);
    }
    return paramString1.equalsIgnoreCase(paramString2);
  }
  
  public List<AEParameter> getDynamicParameters()
  {
    return Collections.emptyList();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AECreateTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */