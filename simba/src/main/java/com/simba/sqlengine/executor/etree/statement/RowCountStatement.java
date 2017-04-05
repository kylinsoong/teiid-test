package com.simba.sqlengine.executor.etree.statement;

import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class RowCountStatement
  implements IETStatement
{
  private Map<Integer, ETParameter> m_parameters;
  
  protected RowCountStatement(Map<Integer, ETParameter> paramMap)
  {
    this.m_parameters = new HashMap(paramMap);
  }
  
  public Map<Integer, ETParameter> getInputParameters()
  {
    return Collections.unmodifiableMap(this.m_parameters);
  }
  
  public abstract void close();
  
  public abstract long getRowCount()
    throws ErrorException;
  
  public abstract void execute()
    throws ErrorException;
  
  public abstract void startBatch()
    throws ErrorException;
  
  public abstract void endBatch()
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/RowCountStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */