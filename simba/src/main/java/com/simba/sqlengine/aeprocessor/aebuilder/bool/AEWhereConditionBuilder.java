package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEWhereConditionBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  public AEWhereConditionBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if ((PTNonterminalType.WHERE_CLAUSE != paramPTNonterminalNode.getNonterminalType()) || (1 != paramPTNonterminalNode.numChildren()) || (paramPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND).isEmptyNode())) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    return (AEBooleanExpr)new AEBooleanExprBuilder(getQueryScope()).build(paramPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEWhereConditionBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */