package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AEDropTable
  extends AERowCountStatement
{
  private static final int NUM_CHILDREN = 0;
  private String m_catalogName;
  private String m_schemaName;
  private String m_tableName;
  private boolean m_isIdenCaseSensitive;
  
  public AEDropTable(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    if (null == paramString1) {
      this.m_catalogName = "";
    } else {
      this.m_catalogName = paramString1;
    }
    if (null == paramString2) {
      this.m_schemaName = "";
    } else {
      this.m_schemaName = paramString2;
    }
    this.m_tableName = paramString3;
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
    return "AEDropTable";
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
    if (!(paramIAENode instanceof AEDropTable)) {
      return false;
    }
    AEDropTable localAEDropTable = (AEDropTable)paramIAENode;
    if (this.m_isIdenCaseSensitive != localAEDropTable.m_isIdenCaseSensitive) {
      return false;
    }
    return (isIdentifierEqual(this.m_catalogName, localAEDropTable.m_catalogName)) && (isIdentifierEqual(this.m_schemaName, localAEDropTable.m_schemaName)) && (isIdentifierEqual(this.m_tableName, localAEDropTable.m_tableName));
  }
  
  public AEDropTable copy()
  {
    return new AEDropTable(this.m_catalogName, this.m_schemaName, this.m_tableName, this.m_isIdenCaseSensitive);
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEDropTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */