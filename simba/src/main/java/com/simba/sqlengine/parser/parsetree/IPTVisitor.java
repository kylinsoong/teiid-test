package com.simba.sqlengine.parser.parsetree;

import com.simba.support.exceptions.ErrorException;

public abstract interface IPTVisitor<T>
{
  public abstract T visit(PTEmptyNode paramPTEmptyNode)
    throws ErrorException;
  
  public abstract T visit(PTIdentifierNode paramPTIdentifierNode)
    throws ErrorException;
  
  public abstract T visit(PTFlagNode paramPTFlagNode)
    throws ErrorException;
  
  public abstract T visit(PTLiteralNode paramPTLiteralNode)
    throws ErrorException;
  
  public abstract T visit(PTDynamicParameterNode paramPTDynamicParameterNode)
    throws ErrorException;
  
  public abstract T visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException;
  
  public abstract T visit(PTListNode paramPTListNode)
    throws ErrorException;
  
  public abstract T visit(PTDefaultParameterNode paramPTDefaultParameterNode)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/IPTVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */