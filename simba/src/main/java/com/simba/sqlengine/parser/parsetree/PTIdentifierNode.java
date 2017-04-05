package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;

public class PTIdentifierNode
  extends AbstractPTTerminalNode
{
  private final String m_sqlString;
  
  public PTIdentifierNode(String paramString)
  {
    this.m_sqlString = paramString;
  }
  
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public String getIdentifier()
  {
    return this.m_sqlString;
  }
  
  public String toString()
  {
    return "IDENTIFIER: " + this.m_sqlString;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTIdentifierNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */