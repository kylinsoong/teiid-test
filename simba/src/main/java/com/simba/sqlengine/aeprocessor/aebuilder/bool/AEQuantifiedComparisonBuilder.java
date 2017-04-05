package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AERelationalExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison.QuantifierType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEQuantifiedComparisonBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AEQuantifiedComparisonBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal(PTNonterminalType.QUANTIFIED_COMPARISON_PREDICATE).withExactChildren(PTPositionalType.ROW_VALUE_CONSTRUCTOR, AEBuilderCheck.list(PTListType.ROW_VALUE_LIST), PTPositionalType.SUBQUERY, AEBuilderCheck.nonTerminal(PTNonterminalType.SUBQUERY), PTPositionalType.QUANTIFIER, AEBuilderCheck.instanceOf(PTFlagNode.class), PTPositionalType.COMPARISON_OP, AEBuilderCheck.nonTerminal()));
    PTListNode localPTListNode = (PTListNode)paramPTNonterminalNode.getChild(PTPositionalType.ROW_VALUE_CONSTRUCTOR);
    if (localPTListNode.numChildren() != 1) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Multiple expresions for quantified comparison is not supported.");
    }
    PTNonterminalNode localPTNonterminalNode1 = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.SUBQUERY);
    PTFlagNode localPTFlagNode = (PTFlagNode)paramPTNonterminalNode.getChild(PTPositionalType.QUANTIFIER);
    PTNonterminalNode localPTNonterminalNode2 = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.COMPARISON_OP);
    AEValueExprList localAEValueExprList = new AEValueExprList();
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    Object localObject = localPTListNode.getChildItr();
    while (((Iterator)localObject).hasNext()) {
      localAEValueExprList.addNode(localAEValueExprBuilder.build((IPTNode)((Iterator)localObject).next()));
    }
    localObject = new AERelationalExprBuilder(getQueryScope(), true);
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)((AERelationalExprBuilder)localObject).build(localPTNonterminalNode1.getChild(PTPositionalType.SINGLE_CHILD));
    if (localAERelationalExpr.getColumnCount() != 1) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Multiple expresions for quantified comparison is not supported.");
    }
    return new AEQuantifiedComparison(getQueryScope().getDataEngine().getContext(), localAEValueExprList, new AESubQuery(localAERelationalExpr, ((AERelationalExprBuilder)localObject).isQueryCorrelated(), false), AEComparisonType.getComparisonType(localPTNonterminalNode2.getNonterminalType()), getQuantifierType(localPTFlagNode));
  }
  
  private static AEQuantifiedComparison.QuantifierType getQuantifierType(PTFlagNode paramPTFlagNode)
    throws ErrorException
  {
    switch (paramPTFlagNode.getFlagType())
    {
    case ALL: 
      return AEQuantifiedComparison.QuantifierType.ALL;
    case ANY: 
      return AEQuantifiedComparison.QuantifierType.ANY;
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEQuantifiedComparisonBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */