package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.AENodeList;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.List;

public class AECaseExprBuilder
  extends AEBuilderBase<AEValueExpr>
{
  protected AECaseExprBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEValueExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case SEARCHED_CASE: 
      return buildSearchedCase(paramPTNonterminalNode);
    case SIMPLE_CASE: 
      return buildSimpleCase(paramPTNonterminalNode);
    case NULLIF: 
      return buildNullIf(paramPTNonterminalNode);
    case COALESCE: 
      return buildCoalesce(paramPTNonterminalNode);
    }
    return (AEValueExpr)super.visit(paramPTNonterminalNode);
  }
  
  private AEValueExpr buildCoalesce(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (1 != paramPTNonterminalNode.numChildren()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    PTListNode localPTListNode = (PTListNode)paramPTNonterminalNode.getChild(PTPositionalType.COALESCE_LIST);
    Iterator localIterator = localPTListNode.getChildItr();
    AENodeList localAENodeList = new AENodeList();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      AEValueExpr localAEValueExpr = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode);
      AEValueExprList localAEValueExprList = new AEValueExprList();
      localAEValueExprList.addNode(localAEValueExpr);
      AENullPredicate localAENullPredicate = new AENullPredicate(localAEValueExprList);
      AENot localAENot = new AENot(localAENullPredicate);
      AESearchedWhenClause localAESearchedWhenClause = new AESearchedWhenClause(localAENot, localAEValueExpr.copy());
      localAENodeList.addNode(localAESearchedWhenClause);
    }
    return new AESearchedCase(getQueryScope().getDataEngine().getContext().getCoercionHandler(), localAENodeList, new AENull());
  }
  
  private AEValueExpr buildNullIf(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (2 != paramPTNonterminalNode.numChildren()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExpr localAEValueExpr1 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(paramPTNonterminalNode.getChild(PTPositionalType.EXPRESSION_FST));
    AEValueExpr localAEValueExpr2 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(paramPTNonterminalNode.getChild(PTPositionalType.EXPRESSION_SND));
    AEValueExprList localAEValueExprList1 = new AEValueExprList();
    localAEValueExprList1.addNode(localAEValueExpr1);
    AEValueExprList localAEValueExprList2 = new AEValueExprList();
    localAEValueExprList2.addNode(localAEValueExpr2);
    AEComparison localAEComparison = new AEComparison(getQueryScope().getDataEngine().getContext(), AEComparisonType.EQUAL, localAEValueExprList1, localAEValueExprList2);
    AESearchedWhenClause localAESearchedWhenClause = new AESearchedWhenClause(localAEComparison, new AENull());
    AENodeList localAENodeList = new AENodeList();
    localAENodeList.addNode(localAESearchedWhenClause);
    return new AESearchedCase(getQueryScope().getDataEngine().getContext().getCoercionHandler(), localAENodeList, localAEValueExpr1.copy());
  }
  
  private AESearchedCase buildSearchedCase(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (2 != paramPTNonterminalNode.numChildren()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.WHEN_CLAUSE_LIST);
    if (!(localIPTNode1 instanceof PTListNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AENodeList localAENodeList = buildSearchedWhenClauseList((PTListNode)localIPTNode1);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.ELSE);
    Object localObject;
    if (((localIPTNode2 instanceof PTNonterminalNode)) && (PTNonterminalType.ELSE_CLAUSE == ((PTNonterminalNode)localIPTNode2).getNonterminalType()))
    {
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode2;
      IPTNode localIPTNode3 = localPTNonterminalNode.getChild(PTPositionalType.RESULT);
      localObject = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode3);
    }
    else if ((null != localIPTNode2) && (localIPTNode2.isEmptyNode()))
    {
      localObject = new AENull();
    }
    else
    {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    return new AESearchedCase(getQueryScope().getDataEngine().getContext().getCoercionHandler(), localAENodeList, (AEValueExpr)localObject);
  }
  
  private AENodeList<AESearchedWhenClause> buildSearchedWhenClauseList(PTListNode paramPTListNode)
    throws ErrorException
  {
    if ((PTListType.SEARCHED_WHEN_CLAUSE_LIST != paramPTListNode.getListType()) || (0 == paramPTListNode.numChildren())) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AENodeList localAENodeList = new AENodeList();
    AEBooleanExprBuilder localAEBooleanExprBuilder = new AEBooleanExprBuilder(getQueryScope());
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    Iterator localIterator = paramPTListNode.getImmutableChildList().iterator();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if (!(localIPTNode instanceof PTNonterminalNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localAENodeList.addNode(buildSearchedWhenClause((PTNonterminalNode)localIPTNode, localAEBooleanExprBuilder, localAEValueExprBuilder));
    }
    return localAENodeList;
  }
  
  private AESearchedWhenClause buildSearchedWhenClause(PTNonterminalNode paramPTNonterminalNode, AEBooleanExprBuilder paramAEBooleanExprBuilder, AEValueExprBuilder paramAEValueExprBuilder)
    throws ErrorException
  {
    if (PTNonterminalType.SEARCHED_WHEN_CLAUSE != paramPTNonterminalNode.getNonterminalType()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    return new AESearchedWhenClause((AEBooleanExpr)paramAEBooleanExprBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND)), (AEValueExpr)paramAEValueExprBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.RESULT)));
  }
  
  private AESimpleCase buildSimpleCase(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (3 != paramPTNonterminalNode.numChildren()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.VALUE_EXPRESSION);
    if (null == localIPTNode1) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExpr localAEValueExpr = (AEValueExpr)localAEValueExprBuilder.build(localIPTNode1);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.WHEN_CLAUSE_LIST);
    if (!(localIPTNode2 instanceof PTListNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AENodeList localAENodeList = buildSimpleWhenClauseList((PTListNode)localIPTNode2, localAEValueExprBuilder);
    IPTNode localIPTNode3 = paramPTNonterminalNode.getChild(PTPositionalType.ELSE);
    Object localObject;
    if (((localIPTNode3 instanceof PTNonterminalNode)) && (PTNonterminalType.ELSE_CLAUSE == ((PTNonterminalNode)localIPTNode3).getNonterminalType()))
    {
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode3;
      IPTNode localIPTNode4 = localPTNonterminalNode.getChild(PTPositionalType.RESULT);
      localObject = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode4);
    }
    else if ((null != localIPTNode3) && (localIPTNode3.isEmptyNode()))
    {
      localObject = new AENull();
    }
    else
    {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    return new AESimpleCase(getQueryScope().getDataEngine().getContext().getCoercionHandler(), localAEValueExpr, localAENodeList, (AEValueExpr)localObject);
  }
  
  private AENodeList<AESimpleWhenClause> buildSimpleWhenClauseList(PTListNode paramPTListNode, AEValueExprBuilder paramAEValueExprBuilder)
    throws ErrorException
  {
    if ((PTListType.SIMPLE_WHEN_CLAUSE_LIST != paramPTListNode.getListType()) || (0 == paramPTListNode.numChildren())) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AENodeList localAENodeList = new AENodeList();
    Iterator localIterator = paramPTListNode.getImmutableChildList().iterator();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if (!(localIPTNode instanceof PTNonterminalNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode;
      if (PTNonterminalType.SIMPLE_WHEN_CLAUSE != localPTNonterminalNode.getNonterminalType()) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localAENodeList.addNode(new AESimpleWhenClause((AEValueExpr)paramAEValueExprBuilder.build(localPTNonterminalNode.getChild(PTPositionalType.VALUE_EXPRESSION)), (AEValueExpr)paramAEValueExprBuilder.build(localPTNonterminalNode.getChild(PTPositionalType.RESULT))));
    }
    return localAENodeList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AECaseExprBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */