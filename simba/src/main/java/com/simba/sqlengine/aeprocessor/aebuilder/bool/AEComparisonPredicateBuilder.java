package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEComparisonPredicateBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AEComparisonPredicateBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.BINARY_LEFT, AEBuilderCheck.list(PTListType.ROW_VALUE_LIST), PTPositionalType.BINARY_RIGHT, AEBuilderCheck.list(PTListType.ROW_VALUE_LIST)));
    PTListNode localPTListNode1 = (PTListNode)paramPTNonterminalNode.getChild(PTPositionalType.BINARY_LEFT);
    PTListNode localPTListNode2 = (PTListNode)paramPTNonterminalNode.getChild(PTPositionalType.BINARY_RIGHT);
    AEValueExprList localAEValueExprList1 = new AEValueExprList();
    AEValueExprList localAEValueExprList2 = new AEValueExprList();
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    Iterator localIterator = localPTListNode1.getChildItr();
    while (localIterator.hasNext()) {
      localAEValueExprList1.addNode(localAEValueExprBuilder.build((IPTNode)localIterator.next()));
    }
    localIterator = localPTListNode2.getChildItr();
    while (localIterator.hasNext()) {
      localAEValueExprList2.addNode(localAEValueExprBuilder.build((IPTNode)localIterator.next()));
    }
    return new AEComparison(getQueryScope().getDataEngine().getContext(), AEComparisonType.getComparisonType(paramPTNonterminalNode.getNonterminalType()), localAEValueExprList1, localAEValueExprList2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEComparisonPredicateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */