package com.simba.sqlengine.aeprocessor.aebuilder.statement;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.ParseTreeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AERelationalExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEQuery;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEQueryBuilder
  extends AEBuilderBase<AEQuery>
{
  protected AEQueryBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEQuery visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (PTNonterminalType.TOP_LEVEL_SELECT_STATEMENT == paramPTNonterminalNode.getNonterminalType()) {
      return buildTopLevelSelectStatement(paramPTNonterminalNode);
    }
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)new AERelationalExprBuilder(getQueryScope(), false).build(paramPTNonterminalNode);
    return new AEQuery(localAERelationalExpr);
  }
  
  private AEQuery buildTopLevelSelectStatement(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    Object localObject = AEBuilderCheck.nonTerminal(PTNonterminalType.ORDER_BY).withExactChildren(PTPositionalType.SORT_SPEC_LIST, AEBuilderCheck.list(PTListType.SORT_SPECIFICATION_LIST));
    AEBuilderCheck.checkThat(paramPTNonterminalNode, ((AEBuilderCheck.NonterminalTypeMatcher)AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.TOP_LEVEL_SELECT_STATEMENT))).withExactChildren(PTPositionalType.SELECT, AEBuilderCheck.nonTerminal(PTNonterminalType.SELECT_STATEMENT), PTPositionalType.ORDER_BY, (AEBuilderCheck.ParseTreeMatcher)localObject));
    localObject = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.SELECT);
    PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.ORDER_BY);
    AEQueryScope localAEQueryScope = getQueryScope();
    localAEQueryScope.setPtSortSpecList(localPTNonterminalNode.getChild(PTPositionalType.SORT_SPEC_LIST));
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)new AERelationalExprBuilder(localAEQueryScope, false).build((IPTNode)localObject);
    return new AEQuery(localAERelationalExpr);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/statement/AEQueryBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */