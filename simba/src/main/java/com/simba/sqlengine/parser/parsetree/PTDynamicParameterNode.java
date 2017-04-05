package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;

public class PTDynamicParameterNode
  extends AbstractPTTerminalNode
{
  private final int m_index;
  
  public PTDynamicParameterNode(int paramInt)
  {
    this.m_index = paramInt;
  }
  
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null.");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public int getIndex()
  {
    return this.m_index;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(60);
    localStringBuilder.append("DYNAMIC_PARAMETER: index=");
    localStringBuilder.append(this.m_index);
    return localStringBuilder.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTDynamicParameterNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */