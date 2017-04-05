package com.simba.sqlengine.aeprocessor.aebuilder.statement;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.support.exceptions.ErrorException;

public class AEStatementBuilder
  extends AEBuilderBase<IAEStatement>
{
  public AEStatementBuilder(SqlDataEngine paramSqlDataEngine)
    throws ErrorException
  {
    super(new AEQueryScope(paramSqlDataEngine));
  }
  
  public IAEStatement visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case SELECT_STATEMENT: 
    case TOP_LEVEL_SELECT_STATEMENT: 
    case UNION: 
    case UNION_ALL: 
    case EXCEPT: 
    case EXCEPT_ALL: 
      return buildQuery(paramPTNonterminalNode);
    case CREATE_TABLE_STATEMENT: 
    case DROP_TABLE_STATEMENT: 
      return buildDDLStatement(paramPTNonterminalNode);
    case INSERT_STATEMENT: 
    case UPDATE_STATEMENT_SEARCHED: 
    case DELETE_STATEMENT_SEARCHED: 
    case UPSERT_STATEMENT: 
      return buildDmlStatement(paramPTNonterminalNode);
    }
    throw SQLEngineExceptionFactory.featureNotImplementedException(String.valueOf(paramPTNonterminalNode.getNonterminalType()));
  }
  
  private IAEStatement buildDDLStatement(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    return (IAEStatement)new AEDdlStatementBuilder(getQueryScope()).build(paramPTNonterminalNode);
  }
  
  private IAEStatement buildQuery(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    return (IAEStatement)new AEQueryBuilder(getQueryScope()).build(paramPTNonterminalNode);
  }
  
  private IAEStatement buildDmlStatement(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    return (IAEStatement)new AEDmlStatementBuilder(getQueryScope()).build(paramPTNonterminalNode);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/statement/AEStatementBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */