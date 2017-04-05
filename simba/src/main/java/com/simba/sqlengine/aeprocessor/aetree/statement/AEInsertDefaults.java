package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AEInsertDefaults
  extends AERowCountStatement
{
  private static final int NUM_CHILDREN = 1;
  private AETable m_table;
  
  public AEInsertDefaults(AETable paramAETable)
  {
    this.m_table = paramAETable;
    this.m_table.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEInsertDefaults copy()
  {
    return new AEInsertDefaults(this.m_table);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEInsertDefaults.this.m_table;
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 1;
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return "AEInsertDefaults";
  }
  
  public AETable getTable()
  {
    return this.m_table;
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    return (this == paramIAENode) || (((paramIAENode instanceof AEInsertDefaults)) && (this.m_table.isEquivalent(((AEInsertDefaults)paramIAENode).m_table)));
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {}
  
  public List<AEParameter> getDynamicParameters()
  {
    return Collections.emptyList();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEInsertDefaults.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */