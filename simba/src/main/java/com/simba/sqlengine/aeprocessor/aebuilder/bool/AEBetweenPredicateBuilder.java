package com.simba.sqlengine.aeprocessor.aebuilder.bool;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AERowValueListBuilder;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEBetweenPredicateBuilder
  extends AEBuilderBase<AEBooleanExpr>
{
  protected AEBetweenPredicateBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEBooleanExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (paramPTNonterminalNode.getNonterminalType() != PTNonterminalType.BETWEEN) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AERowValueListBuilder localAERowValueListBuilder = new AERowValueListBuilder(getQueryScope());
    AEValueExprList localAEValueExprList1 = (AEValueExprList)localAERowValueListBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.ROW_VALUE_CONSTRUCTOR));
    AEValueExprList localAEValueExprList2 = (AEValueExprList)localAERowValueListBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.ROW_VALUE_CONSTRUCTOR));
    AEValueExprList localAEValueExprList3 = (AEValueExprList)localAERowValueListBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.LOWER_LIMIT));
    AEValueExprList localAEValueExprList4 = (AEValueExprList)localAERowValueListBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.UPPER_LIMIT));
    IPTNode localIPTNode = paramPTNonterminalNode.getChild(PTPositionalType.IS_OR_IS_NOT);
    if (!(localIPTNode instanceof PTFlagNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    if (PTFlagType.NOT == ((PTFlagNode)localIPTNode).getFlagType()) {
      return doDemorgansTranslation(localAEValueExprList1, localAEValueExprList2, localAEValueExprList3, localAEValueExprList4);
    }
    return doTranslation(localAEValueExprList1, localAEValueExprList2, localAEValueExprList3, localAEValueExprList4);
  }
  
  private AEBooleanExpr doTranslation(AEValueExprList paramAEValueExprList1, AEValueExprList paramAEValueExprList2, AEValueExprList paramAEValueExprList3, AEValueExprList paramAEValueExprList4)
    throws ErrorException
  {
    AEComparison localAEComparison1 = new AEComparison(getQueryScope().getDataEngine().getContext(), AEComparisonType.GREATER_THAN_OR_EQUAL, paramAEValueExprList1, paramAEValueExprList3);
    AEComparison localAEComparison2 = new AEComparison(getQueryScope().getDataEngine().getContext(), AEComparisonType.LESS_THAN_OR_EQUAL, paramAEValueExprList2, paramAEValueExprList4);
    return new AEAnd(localAEComparison1, localAEComparison2);
  }
  
  private AEBooleanExpr doDemorgansTranslation(AEValueExprList paramAEValueExprList1, AEValueExprList paramAEValueExprList2, AEValueExprList paramAEValueExprList3, AEValueExprList paramAEValueExprList4)
    throws ErrorException
  {
    AEComparison localAEComparison1 = new AEComparison(getQueryScope().getDataEngine().getContext(), AEComparisonType.LESS_THAN, paramAEValueExprList1, paramAEValueExprList3);
    AEComparison localAEComparison2 = new AEComparison(getQueryScope().getDataEngine().getContext(), AEComparisonType.GREATER_THAN, paramAEValueExprList2, paramAEValueExprList4);
    return new AEOr(localAEComparison1, localAEComparison2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/bool/AEBetweenPredicateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */