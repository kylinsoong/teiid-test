package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;

public class PTDefaultParameterNode
  extends AbstractPTTerminalNode
{
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null.");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public String toString()
  {
    return "DEFAULT_PARAMETER";
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTDefaultParameterNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */