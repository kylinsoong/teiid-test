package com.simba.sqlengine.executor.queryplan;

import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;

public class ETQueryPlan
  implements IQueryPlan
{
  private IAEStatement m_statement;
  private DefaultMaterializationInfo m_defaultSpec;
  
  public ETQueryPlan(IAEStatement paramIAEStatement)
  {
    this.m_statement = paramIAEStatement;
    this.m_defaultSpec = new DefaultMaterializationInfo();
  }
  
  public IAEStatement getAETree()
  {
    return this.m_statement;
  }
  
  public IMaterializationInfo getMaterializationInfo(AERelationalExpr paramAERelationalExpr)
  {
    return this.m_defaultSpec;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/queryplan/ETQueryPlan.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */