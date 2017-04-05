package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AERelationalExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AERowValueListBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AEInPredicateBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AEInPredicateBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if ((paramPTNonterminalNode == null) || (paramPTNonterminalNode.getNonterminalType() != PTNonterminalType.IN)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExprList localAEValueExprList = (AEValueExprList)new AERowValueListBuilder(getQueryScope()).build(paramPTNonterminalNode.getChild(PTPositionalType.ROW_VALUE_CONSTRUCTOR));
    IPTNode localIPTNode = paramPTNonterminalNode.getChild(PTPositionalType.PREDICATE_VALUE);
    Object localObject3;
    Object localObject4;
    Object localObject1;
    if ((localIPTNode instanceof PTListNode))
    {
      localObject2 = (PTListNode)localIPTNode;
      if (((PTListNode)localObject2).getListType() != PTListType.VALUE_LIST) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localObject3 = new AEValueExprList();
      localObject4 = ((PTListNode)localObject2).getChildItr();
      while (((Iterator)localObject4).hasNext()) {
        ((AEValueExprList)localObject3).addNode(new AEValueExprBuilder(getQueryScope()).build((IPTNode)((Iterator)localObject4).next()));
      }
      localObject1 = new AEInPredicate(getQueryScope().getDataEngine().getContext(), localAEValueExprList, (AEValueExprList)localObject3);
    }
    else if ((localIPTNode instanceof PTNonterminalNode))
    {
      localObject2 = (PTNonterminalNode)localIPTNode;
      if (((PTNonterminalNode)localObject2).getNonterminalType() != PTNonterminalType.SUBQUERY) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localObject3 = new AERelationalExprBuilder(getQueryScope(), true);
      localObject4 = (AERelationalExpr)((AERelationalExprBuilder)localObject3).build(((PTNonterminalNode)localObject2).getChild(PTPositionalType.SINGLE_CHILD));
      localObject1 = new AEInPredicate(getQueryScope().getDataEngine().getContext(), localAEValueExprList, new AESubQuery((AERelationalExpr)localObject4, ((AERelationalExprBuilder)localObject3).isQueryCorrelated(), false));
    }
    else
    {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    Object localObject2 = paramPTNonterminalNode.getChild(PTPositionalType.IS_OR_IS_NOT);
    if (!(localObject2 instanceof PTFlagNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    if (PTFlagType.NOT == ((PTFlagNode)localObject2).getFlagType()) {
      localObject1 = new AENot((AEBooleanExpr)localObject1);
    }
    return (AEBooleanExpr)localObject1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEInPredicateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */