package com.simba.sqlengine.executor.queryplan;

import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;

public abstract interface IQueryPlan
{
  public abstract IAEStatement getAETree();
  
  public abstract IMaterializationInfo getMaterializationInfo(AERelationalExpr paramAERelationalExpr);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/queryplan/IQueryPlan.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */