package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprOuterRefProcessor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEJoinedTableBuilder
  extends AEBuilderBase<AERelationalExpr>
{
  protected AEJoinedTableBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AERelationalExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (null == paramPTNonterminalNode) {
      throw new NullPointerException("node may not be null.");
    }
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case CROSS_JOIN: 
      return buildCrossJoin(paramPTNonterminalNode, getQueryScope());
    case INNER_JOIN: 
    case LEFT_OUTER_JOIN: 
    case RIGHT_OUTER_JOIN: 
    case FULL_OUTER_JOIN: 
      return buildJoin(paramPTNonterminalNode, getQueryScope());
    case JOIN_ESCAPE: 
      return (AERelationalExpr)paramPTNonterminalNode.getChild(PTPositionalType.JOIN).acceptVisitor(this);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private static AECrossJoin buildCrossJoin(PTNonterminalNode paramPTNonterminalNode, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal(PTNonterminalType.CROSS_JOIN).withExactChildren(PTPositionalType.TABLE_REF_LEFT, AEBuilderCheck.nonEmpty(), PTPositionalType.TABLE_REF_RIGHT, AEBuilderCheck.nonEmpty()));
    AETableRefBuilder localAETableRefBuilder = new AETableRefBuilder(paramAEQueryScope);
    AERelationalExpr localAERelationalExpr1 = (AERelationalExpr)localAETableRefBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.TABLE_REF_LEFT));
    AERelationalExpr localAERelationalExpr2 = (AERelationalExpr)localAETableRefBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.TABLE_REF_RIGHT));
    return new AECrossJoin(localAERelationalExpr1, localAERelationalExpr2);
  }
  
  private static AEJoin buildJoin(PTNonterminalNode paramPTNonterminalNode, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.TABLE_REF_LEFT, AEBuilderCheck.nonEmpty(), PTPositionalType.TABLE_REF_RIGHT, AEBuilderCheck.nonEmpty(), PTPositionalType.SEARCH_COND, AEBuilderCheck.nonEmpty()));
    AEJoin.AEJoinType localAEJoinType = translateJoinType(paramPTNonterminalNode.getNonterminalType());
    AETableRefBuilder localAETableRefBuilder = new AETableRefBuilder(paramAEQueryScope);
    AERelationalExpr localAERelationalExpr1 = (AERelationalExpr)localAETableRefBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.TABLE_REF_LEFT));
    AERelationalExpr localAERelationalExpr2 = (AERelationalExpr)localAETableRefBuilder.build(paramPTNonterminalNode.getChild(PTPositionalType.TABLE_REF_RIGHT));
    AEJoin localAEJoin = new AEJoin(localAEJoinType, localAERelationalExpr1, localAERelationalExpr2);
    paramAEQueryScope.setCurrentJoinExpr(localAEJoin);
    try
    {
      AEBooleanExpr localAEBooleanExpr = (AEBooleanExpr)new AEBooleanExprBuilder(paramAEQueryScope).build(paramPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND));
      if (!paramAEQueryScope.isTopMost()) {
        AEBooleanExprOuterRefProcessor.process(localAEBooleanExpr, paramAEQueryScope);
      }
      localAEJoin.setJoinCondition(localAEBooleanExpr);
    }
    finally
    {
      paramAEQueryScope.setCurrentJoinExpr(null);
    }
    return localAEJoin;
  }
  
  private static AEJoin.AEJoinType translateJoinType(PTNonterminalType paramPTNonterminalType)
    throws ErrorException
  {
    switch (paramPTNonterminalType)
    {
    case INNER_JOIN: 
      return AEJoin.AEJoinType.INNER_JOIN;
    case LEFT_OUTER_JOIN: 
      return AEJoin.AEJoinType.LEFT_OUTER_JOIN;
    case RIGHT_OUTER_JOIN: 
      return AEJoin.AEJoinType.RIGHT_OUTER_JOIN;
    case FULL_OUTER_JOIN: 
      return AEJoin.AEJoinType.FULL_OUTER_JOIN;
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AEJoinedTableBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */