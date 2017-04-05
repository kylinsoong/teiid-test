package com.simba.sqlengine.executor.etree.statement;

import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import java.util.Map;

public abstract interface IETStatement
  extends IETNode
{
  public abstract boolean isResultSet();
  
  public abstract Map<Integer, ETParameter> getInputParameters();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/IETStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */