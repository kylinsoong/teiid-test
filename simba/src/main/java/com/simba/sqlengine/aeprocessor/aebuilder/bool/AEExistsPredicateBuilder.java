package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AERelationalExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEExistsPredicateBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AEExistsPredicateBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal(PTNonterminalType.EXISTS).withExactChildren(PTPositionalType.SUBQUERY, AEBuilderCheck.nonTerminal(PTNonterminalType.SUBQUERY)));
    PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.SUBQUERY);
    AERelationalExprBuilder localAERelationalExprBuilder = new AERelationalExprBuilder(getQueryScope(), true);
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)localAERelationalExprBuilder.build(localPTNonterminalNode.getChild(PTPositionalType.SINGLE_CHILD));
    return new AEExistsPredicate(new AESubQuery(localAERelationalExpr, localAERelationalExprBuilder.isQueryCorrelated(), false));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEExistsPredicateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */