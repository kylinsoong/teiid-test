package com.simba.sqlengine.parser.parsetree;

import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.support.exceptions.ErrorException;

public class PTFlagNode
  extends AbstractPTTerminalNode
{
  private final PTFlagType m_flagType;
  
  public PTFlagNode(PTFlagType paramPTFlagType)
  {
    if (null == paramPTFlagType) {
      throw new NullPointerException("PTFlagType cannot be null");
    }
    this.m_flagType = paramPTFlagType;
  }
  
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public PTFlagType getFlagType()
  {
    return this.m_flagType;
  }
  
  public String toString()
  {
    return "FLAG: " + this.m_flagType.name();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTFlagNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */