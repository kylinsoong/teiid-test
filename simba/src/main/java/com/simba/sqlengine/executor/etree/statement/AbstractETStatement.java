package com.simba.sqlengine.executor.etree.statement;

import com.simba.sqlengine.executor.etree.value.ETParameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractETStatement
  implements IETStatement
{
  private Map<Integer, ETParameter> m_parameters;
  
  protected AbstractETStatement(Map<Integer, ETParameter> paramMap)
  {
    this.m_parameters = new HashMap(paramMap);
  }
  
  public Map<Integer, ETParameter> getInputParameters()
  {
    return Collections.unmodifiableMap(this.m_parameters);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/AbstractETStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */