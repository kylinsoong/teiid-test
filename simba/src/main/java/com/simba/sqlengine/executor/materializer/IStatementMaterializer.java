package com.simba.sqlengine.executor.materializer;

import com.simba.sqlengine.executor.IStatementExecutor;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.support.exceptions.ErrorException;

public abstract interface IStatementMaterializer
{
  public abstract IStatementExecutor materialize(IQueryPlan paramIQueryPlan)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/IStatementMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */