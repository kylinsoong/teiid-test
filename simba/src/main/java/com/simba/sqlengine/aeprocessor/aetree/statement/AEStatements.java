package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;

public class AEStatements
{
  private ArrayList<IAEStatement> m_statements = new ArrayList();
  
  public void addStatement(IAEStatement paramIAEStatement)
  {
    this.m_statements.add(paramIAEStatement);
  }
  
  public Iterator<IAEStatement> getStatementItr()
  {
    return this.m_statements.iterator();
  }
  
  public int size()
  {
    return this.m_statements.size();
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {
    Iterator localIterator = this.m_statements.iterator();
    while (localIterator.hasNext())
    {
      IAEStatement localIAEStatement = (IAEStatement)localIterator.next();
      localIAEStatement.reprocessMetadata();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEStatements.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */