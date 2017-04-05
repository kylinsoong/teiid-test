package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;

public abstract class PTDefaultVisitor<T>
  implements IPTVisitor<T>
{
  public T visit(PTEmptyNode paramPTEmptyNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTEmptyNode);
  }
  
  public T visit(PTIdentifierNode paramPTIdentifierNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTIdentifierNode);
  }
  
  public T visit(PTFlagNode paramPTFlagNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTFlagNode);
  }
  
  public T visit(PTLiteralNode paramPTLiteralNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTLiteralNode);
  }
  
  public T visit(PTDynamicParameterNode paramPTDynamicParameterNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTDynamicParameterNode);
  }
  
  public T visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTNonterminalNode);
  }
  
  public T visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTListNode);
  }
  
  public T visit(PTDefaultParameterNode paramPTDefaultParameterNode)
    throws ErrorException
  {
    return (T)defaultVisit(paramPTDefaultParameterNode);
  }
  
  protected abstract T defaultVisit(IPTNode paramIPTNode)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTDefaultVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */