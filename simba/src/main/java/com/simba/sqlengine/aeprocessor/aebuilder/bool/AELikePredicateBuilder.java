package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AELikePredicateBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AELikePredicateBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if ((null == paramPTNonterminalNode) || (PTNonterminalType.LIKE != paramPTNonterminalNode.getNonterminalType())) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_LEFT);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_RIGHT);
    if ((null == localIPTNode1) || (null == localIPTNode2) || (localIPTNode1.isEmptyNode()) || (localIPTNode2.isEmptyNode())) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExpr localAEValueExpr1 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode1);
    AEValueExpr localAEValueExpr2 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode2);
    IPTNode localIPTNode3 = paramPTNonterminalNode.getChild(PTPositionalType.ESCAPE_CHAR);
    AEValueExpr localAEValueExpr3 = localIPTNode3.isEmptyNode() ? null : (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode3);
    Object localObject = new AELikePredicate(getQueryScope().getDataEngine().getContext(), localAEValueExpr1, localAEValueExpr2, localAEValueExpr3);
    IPTNode localIPTNode4 = paramPTNonterminalNode.getChild(PTPositionalType.IS_OR_IS_NOT);
    if (!(localIPTNode4 instanceof PTFlagNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    if (PTFlagType.NOT == ((PTFlagNode)localIPTNode4).getFlagType()) {
      localObject = new AENot((AEBooleanExpr)localObject);
    }
    return (AEBooleanExpr)localObject;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AELikePredicateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */