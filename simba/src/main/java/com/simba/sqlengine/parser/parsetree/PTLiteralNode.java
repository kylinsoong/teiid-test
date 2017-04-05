package com.simba.sqlengine.parser.parsetree;

import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.support.exceptions.ErrorException;

public class PTLiteralNode
  extends AbstractPTTerminalNode
{
  private final PTLiteralType m_literalType;
  private final String m_sqlString;
  
  public PTLiteralNode(PTLiteralType paramPTLiteralType, String paramString)
  {
    if ((null == paramPTLiteralType) || (null == paramString)) {
      throw new NullPointerException("Invalid input.");
    }
    this.m_literalType = paramPTLiteralType;
    this.m_sqlString = paramString;
  }
  
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null.");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public String getStringValue()
  {
    return this.m_sqlString;
  }
  
  public PTLiteralType getLiteralType()
  {
    return this.m_literalType;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(20);
    localStringBuffer.append(this.m_literalType.name());
    localStringBuffer.append(": ");
    localStringBuffer.append(this.m_sqlString);
    return localStringBuffer.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTLiteralNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */