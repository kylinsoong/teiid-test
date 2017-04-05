package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBinaryBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEBooleanExprBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  public AEBooleanExprBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    IPTNode localIPTNode1;
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case OR: 
    case AND: 
      localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_LEFT);
      IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_RIGHT);
      if ((2 != paramPTNonterminalNode.numChildren()) || (!(localIPTNode1 instanceof PTNonterminalNode)) || (!(localIPTNode2 instanceof PTNonterminalNode))) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      return buildBinaryNode(paramPTNonterminalNode, (PTNonterminalNode)localIPTNode1, (PTNonterminalNode)localIPTNode2);
    case NOT: 
      localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.PREDICATE);
      if ((1 != paramPTNonterminalNode.numChildren()) || (null == localIPTNode1) || (localIPTNode1.isEmptyNode())) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      return new AENot((AEBooleanExpr)build(localIPTNode1));
    case EQUALS_OP: 
    case NOT_EQUALS_OP: 
    case LESS_THAN_OP: 
    case GREATER_THAN_OP: 
    case LESS_THAN_OR_EQUALS_OP: 
    case GREATER_THAN_OR_EQUALS_OP: 
      return (AEBooleanExpr)new AEComparisonPredicateBuilder(getQueryScope()).build(paramPTNonterminalNode);
    case LIKE: 
      return (AEBooleanExpr)new AELikePredicateBuilder(getQueryScope()).build(paramPTNonterminalNode);
    case IN: 
      return (AEBooleanExpr)new AEInPredicateBuilder(getQueryScope()).build(paramPTNonterminalNode);
    case NULL: 
      return (AEBooleanExpr)new AENullPredicateBuilder(getQueryScope()).build(paramPTNonterminalNode);
    case EXISTS: 
      return (AEBooleanExpr)new AEExistsPredicateBuilder(getQueryScope()).build(paramPTNonterminalNode);
    case QUANTIFIED_COMPARISON_PREDICATE: 
      return (AEBooleanExpr)new AEQuantifiedComparisonBuilder(getQueryScope()).build(paramPTNonterminalNode);
    case BETWEEN: 
      return (AEBooleanExpr)new AEBetweenPredicateBuilder(getQueryScope()).build(paramPTNonterminalNode);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private AEBooleanExpr buildBinaryNode(PTNonterminalNode paramPTNonterminalNode1, PTNonterminalNode paramPTNonterminalNode2, PTNonterminalNode paramPTNonterminalNode3)
    throws ErrorException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if ((PTNonterminalType.AND == paramPTNonterminalNode2.getNonterminalType()) && (PTNonterminalType.AND == paramPTNonterminalNode3.getNonterminalType()))
    {
      AEBooleanExpr localAEBooleanExpr1 = (AEBooleanExpr)build(paramPTNonterminalNode2);
      AEBooleanExpr localAEBooleanExpr2 = (AEBooleanExpr)build(paramPTNonterminalNode3);
      if (((localAEBooleanExpr1 instanceof AEAnd)) && (!(localAEBooleanExpr2 instanceof AEAnd)))
      {
        localObject2 = localAEBooleanExpr1;
        localObject1 = localAEBooleanExpr2;
      }
      else if (((localAEBooleanExpr1 instanceof AEAnd)) && ((localAEBooleanExpr2 instanceof AEAnd)))
      {
        if (PTNonterminalType.AND == paramPTNonterminalNode1.getNonterminalType())
        {
          AEAnd localAEAnd = new AEAnd(((AEBinaryBooleanExpr)localAEBooleanExpr1).getLeftOperand(), localAEBooleanExpr2);
          localObject1 = ((AEBinaryBooleanExpr)localAEBooleanExpr1).getRightOperand();
          localObject2 = localAEAnd;
        }
        else
        {
          localObject1 = localAEBooleanExpr1;
          localObject2 = localAEBooleanExpr2;
        }
      }
      else
      {
        localObject1 = localAEBooleanExpr1;
        localObject2 = localAEBooleanExpr2;
      }
    }
    else if ((PTNonterminalType.AND == paramPTNonterminalNode2.getNonterminalType()) && (PTNonterminalType.AND != paramPTNonterminalNode3.getNonterminalType()))
    {
      localObject1 = (AEBooleanExpr)build(paramPTNonterminalNode3);
      localObject2 = (AEBooleanExpr)build(paramPTNonterminalNode2);
    }
    else
    {
      localObject1 = (AEBooleanExpr)build(paramPTNonterminalNode2);
      localObject2 = (AEBooleanExpr)build(paramPTNonterminalNode3);
    }
    if (PTNonterminalType.AND == paramPTNonterminalNode1.getNonterminalType()) {
      return new AEAnd((AEBooleanExpr)localObject1, (AEBooleanExpr)localObject2);
    }
    return new AEOr((AEBooleanExpr)localObject1, (AEBooleanExpr)localObject2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEBooleanExprBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */