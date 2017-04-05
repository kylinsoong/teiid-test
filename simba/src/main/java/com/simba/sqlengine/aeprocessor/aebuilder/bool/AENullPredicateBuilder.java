package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AERowValueListBuilder;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AENullPredicateBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AENullPredicateBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.IS_OR_IS_NOT);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.ROW_VALUE_CONSTRUCTOR);
    if ((2 != paramPTNonterminalNode.numChildren()) || (null == localIPTNode1) || (!(localIPTNode1 instanceof PTFlagNode)) || (null == localIPTNode2)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExprList localAEValueExprList = (AEValueExprList)new AERowValueListBuilder(getQueryScope()).build(localIPTNode2);
    AENullPredicate localAENullPredicate = new AENullPredicate(localAEValueExprList);
    switch (((PTFlagNode)localIPTNode1).getFlagType())
    {
    case IS: 
      return localAENullPredicate;
    case NOT: 
      return new AENot(localAENullPredicate);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AENullPredicateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */