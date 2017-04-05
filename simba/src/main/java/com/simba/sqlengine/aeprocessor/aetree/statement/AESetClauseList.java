package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AbstractAENodeList;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AESetClauseList
  extends AbstractAENodeList<AESetClause>
{
  public AESetClauseList() {}
  
  public AESetClauseList(AESetClauseList paramAESetClauseList)
  {
    super(paramAESetClauseList);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AESetClauseList copy()
  {
    return new AESetClauseList(this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AESetClauseList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */