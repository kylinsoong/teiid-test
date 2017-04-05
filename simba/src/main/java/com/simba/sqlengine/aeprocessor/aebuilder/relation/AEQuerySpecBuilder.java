package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope.ClauseType;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprOuterRefProcessor;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprProcessor;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEWhereConditionBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEGroupByListBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AESelectListBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprComposer;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprOuterRefProcessor;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEAggregate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEDistinct;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESort;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETop;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AEQuerySpecBuilder
  extends AEBuilderBase<AERelationalExpr>
{
  private static final String EXPR_COL_NAME = "EXPR_";
  private AEValueExprList m_groupByExprList = null;
  private AEBooleanExpr m_havingExpr = null;
  private Map<Integer, Integer> m_groupingListOrdinalReferenceMap;
  private AEValueExprList m_selectList;
  private int m_numSelectedCols = -1;
  private boolean m_hasAggregate;
  private AEAggregate m_aggregate;
  
  protected AEQuerySpecBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AERelationalExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (paramPTNonterminalNode.getNonterminalType() != PTNonterminalType.SELECT_STATEMENT) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.TABLE_REF_LIST);
    Object localObject = processFrom(localIPTNode1);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.WHERE);
    localObject = processWhere((AERelationalExpr)localObject, localIPTNode2);
    IPTNode localIPTNode3 = paramPTNonterminalNode.getChild(PTPositionalType.GROUP_BY);
    IPTNode localIPTNode4 = paramPTNonterminalNode.getChild(PTPositionalType.SELECT_LIST);
    AEBuilderCheck.checkThat(localIPTNode4, AEBuilderCheck.list(PTListType.SELECT_LIST));
    processGroupBy((AERelationalExpr)localObject, localIPTNode3, (PTListNode)localIPTNode4);
    IPTNode localIPTNode5 = paramPTNonterminalNode.getChild(PTPositionalType.HAVING);
    if ((localIPTNode5 != null) && (!localIPTNode5.isEmptyNode())) {
      processHaving(localIPTNode5);
    }
    IPTNode localIPTNode6 = paramPTNonterminalNode.getChild(PTPositionalType.SET_QUANTIFIER);
    if ((null != localIPTNode6) && (!localIPTNode6.isEmptyNode()))
    {
      if (!(localIPTNode6 instanceof PTFlagNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      if (PTFlagType.DISTINCT == ((PTFlagNode)localIPTNode6).getFlagType()) {
        getQueryScope().setHasDistinct(true);
      }
    }
    localObject = processSelectList((AERelationalExpr)localObject, localIPTNode4);
    AEProject localAEProject = createProject((AERelationalExpr)localObject);
    localObject = processOrderBy(localAEProject);
    composeAggregatesInSelList();
    nameUnnamedExpressions(localAEProject);
    if (getQueryScope().hasDistinct()) {
      localObject = new AEDistinct((AERelationalExpr)localObject);
    }
    localObject = processSelectLimit((AERelationalExpr)localObject, paramPTNonterminalNode.getChild(PTPositionalType.SELECT_LIMIT));
    return (AERelationalExpr)localObject;
  }
  
  private void nameUnnamedExpressions(AEProject paramAEProject)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = paramAEProject.getProjectionList();
    Iterator localIterator = localAEValueExprList.getChildItr();
    HashSet localHashSet = new HashSet();
    while (localIterator.hasNext())
    {
      AEValueExpr localAEValueExpr1 = (AEValueExpr)localIterator.next();
      if (!localAEValueExpr1.isUnnamed()) {
        localHashSet.add(localAEValueExpr1.getName().toUpperCase());
      }
    }
    if (0 > this.m_numSelectedCols) {
      this.m_numSelectedCols = localAEValueExprList.getNumChildren();
    }
    localIterator = localAEValueExprList.getChildItr();
    int i = 0;
    for (int j = 0; j < this.m_numSelectedCols; j++)
    {
      AEValueExpr localAEValueExpr2 = (AEValueExpr)localIterator.next();
      if (localAEValueExpr2.isUnnamed())
      {
        for (String str = "EXPR_" + i; localHashSet.contains(str); str = "EXPR_" + i) {
          i++;
        }
        i++;
        AERename localAERename = new AERename(str, localAEValueExpr2);
        localAEValueExprList.replaceNode(localAERename, j);
      }
    }
  }
  
  private AEProject createProject(AERelationalExpr paramAERelationalExpr)
  {
    if (this.m_hasAggregate)
    {
      AEAggregate localAEAggregate = new AEAggregate(paramAERelationalExpr, this.m_groupByExprList, this.m_groupingListOrdinalReferenceMap, getQueryScope().getAggregateList(), getQueryScope());
      this.m_aggregate = localAEAggregate;
      if (this.m_havingExpr != null) {
        paramAERelationalExpr = new AESelect(localAEAggregate, this.m_havingExpr);
      } else {
        paramAERelationalExpr = localAEAggregate;
      }
    }
    return new AEProject(this.m_selectList, paramAERelationalExpr);
  }
  
  private void updateProxyColumns(AERelationalExpr paramAERelationalExpr)
  {
    Iterator localIterator = getQueryScope().getProxyColumns().iterator();
    while (localIterator.hasNext())
    {
      AEProxyColumn localAEProxyColumn = (AEProxyColumn)localIterator.next();
      assert (localAEProxyColumn.getResolvedQueryScope() == getQueryScope());
      localAEProxyColumn.setRelationalExpr(paramAERelationalExpr);
    }
  }
  
  private AERelationalExpr processSelectList(AERelationalExpr paramAERelationalExpr, IPTNode paramIPTNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = getQueryScope();
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.SELECT_LIST);
    this.m_selectList = ((AEValueExprList)new AESelectListBuilder(localAEQueryScope).build(paramIPTNode));
    if (!getQueryScope().hasAggregate()) {
      this.m_hasAggregate = checkAggrFnInSelectList();
    } else {
      this.m_hasAggregate = true;
    }
    return paramAERelationalExpr;
  }
  
  private void composeAggregatesInSelList()
    throws ErrorException
  {
    if (!getQueryScope().isTopMost()) {
      AEValueExprOuterRefProcessor.process(this.m_selectList, getQueryScope());
    }
    if ((getQueryScope().hasAggregate()) || (this.m_hasAggregate))
    {
      AEValueExprComposer.compose(this.m_selectList, getQueryScope());
      if (null != this.m_aggregate) {
        updateProxyColumns(this.m_aggregate);
      }
    }
  }
  
  private AERelationalExpr processOrderBy(AEProject paramAEProject)
    throws ErrorException
  {
    IPTNode localIPTNode = getQueryScope().getPtSortSpecList();
    if (null != localIPTNode)
    {
      AESortBuilder localAESortBuilder = new AESortBuilder(getQueryScope(), paramAEProject);
      AESort localAESort = (AESort)localAESortBuilder.build(localIPTNode);
      this.m_numSelectedCols = localAESort.getColumnCount();
      return localAESort;
    }
    return paramAEProject;
  }
  
  private void processGroupBy(AERelationalExpr paramAERelationalExpr, IPTNode paramIPTNode, PTListNode paramPTListNode)
    throws ErrorException
  {
    if (!paramIPTNode.isEmptyNode())
    {
      AEBuilderCheck.checkThat(paramIPTNode, AEBuilderCheck.nonTerminal(PTNonterminalType.GROUP_BY).withExactChildren(PTPositionalType.GROUP_BY_EXPRESSION_LIST, AEBuilderCheck.instanceOf(IPTNode.class)));
      getQueryScope().setCurrentClause(AEQueryScope.ClauseType.GROUP_BY);
      AEGroupByListBuilder localAEGroupByListBuilder = new AEGroupByListBuilder(getQueryScope(), paramPTListNode);
      this.m_groupByExprList = ((AEValueExprList)localAEGroupByListBuilder.build(((PTNonterminalNode)paramIPTNode).getChild(PTPositionalType.GROUP_BY_EXPRESSION_LIST)));
      this.m_groupingListOrdinalReferenceMap = localAEGroupByListBuilder.getGroupingListOrdinalReferenceMap();
    }
  }
  
  private void processHaving(IPTNode paramIPTNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramIPTNode, AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.SEARCH_COND, AEBuilderCheck.nonEmpty()));
    AEQueryScope localAEQueryScope = getQueryScope();
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.HAVING);
    localAEQueryScope.setImpliedGroupBy(true);
    this.m_havingExpr = ((AEBooleanExpr)new AEBooleanExprBuilder(localAEQueryScope).build(((PTNonterminalNode)paramIPTNode).getChild(PTPositionalType.SEARCH_COND)));
    if (!localAEQueryScope.isTopMost()) {
      AEBooleanExprOuterRefProcessor.process(this.m_havingExpr, localAEQueryScope);
    }
    AEBooleanExprProcessor.process(this.m_havingExpr, localAEQueryScope);
  }
  
  private AERelationalExpr processFrom(IPTNode paramIPTNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = getQueryScope();
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.FROM);
    if (paramIPTNode.isEmptyNode()) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Empty table list in select statement is not supported.");
    }
    return (AERelationalExpr)new AETableRefListBuilder(localAEQueryScope).build(paramIPTNode);
  }
  
  private AERelationalExpr processWhere(AERelationalExpr paramAERelationalExpr, IPTNode paramIPTNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = getQueryScope();
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.WHERE);
    if (paramIPTNode.isEmptyNode()) {
      return paramAERelationalExpr;
    }
    AEBooleanExpr localAEBooleanExpr = (AEBooleanExpr)new AEWhereConditionBuilder(getQueryScope()).build(paramIPTNode);
    if (!getQueryScope().isTopMost()) {
      AEBooleanExprOuterRefProcessor.process(localAEBooleanExpr, localAEQueryScope);
    }
    return new AESelect(paramAERelationalExpr, localAEBooleanExpr);
  }
  
  private AERelationalExpr processSelectLimit(AERelationalExpr paramAERelationalExpr, IPTNode paramIPTNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = getQueryScope();
    if (paramIPTNode.isEmptyNode()) {
      return paramAERelationalExpr;
    }
    AEBuilderCheck.checkThat(paramIPTNode, AEBuilderCheck.nonTerminal(PTNonterminalType.SELECT_LIMIT).withExactChildren(PTPositionalType.TOP_VALUE_SPEC, AEBuilderCheck.instanceOf(IPTNode.class)));
    PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)paramIPTNode;
    IPTNode localIPTNode = localPTNonterminalNode.getChild(PTPositionalType.TOP_VALUE_SPEC);
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.NONE);
    AEValueExpr localAEValueExpr = (AEValueExpr)new AEValueExprBuilder(localAEQueryScope).build(localIPTNode);
    return new AETop(paramAERelationalExpr, localAEValueExpr, false);
  }
  
  private boolean checkAggrFnInSelectList()
    throws ErrorException
  {
    AEDefaultVisitor local1 = new AEDefaultVisitor()
    {
      boolean m_hasRowExpr = false;
      boolean m_hasAggr = false;
      String m_exceptionCol = null;
      
      public Boolean visit(AEColumnReference paramAnonymousAEColumnReference)
        throws ErrorException
      {
        if ((!this.m_hasRowExpr) && (paramAnonymousAEColumnReference.getResolvedQueryScope() == AEQuerySpecBuilder.this.getQueryScope()))
        {
          this.m_hasRowExpr = true;
          this.m_exceptionCol = paramAnonymousAEColumnReference.getLogString();
        }
        return Boolean.valueOf(this.m_hasAggr);
      }
      
      public Boolean visit(AEProxyColumn paramAnonymousAEProxyColumn)
        throws ErrorException
      {
        if ((!this.m_hasRowExpr) && (paramAnonymousAEProxyColumn.getResolvedQueryScope() == AEQuerySpecBuilder.this.getQueryScope()))
        {
          this.m_hasRowExpr = true;
          this.m_exceptionCol = paramAnonymousAEProxyColumn.getLogString();
        }
        return Boolean.valueOf(this.m_hasAggr);
      }
      
      public Boolean visit(AECountStarAggrFn paramAnonymousAECountStarAggrFn)
      {
        this.m_hasAggr = true;
        return Boolean.valueOf(this.m_hasAggr);
      }
      
      public Boolean visit(AEGeneralAggrFn paramAnonymousAEGeneralAggrFn)
      {
        this.m_hasAggr = true;
        return Boolean.valueOf(this.m_hasAggr);
      }
      
      public Boolean visit(AEValueSubQuery paramAnonymousAEValueSubQuery)
        throws ErrorException
      {
        if (paramAnonymousAEValueSubQuery.isCorrelated()) {
          paramAnonymousAEValueSubQuery.getQueryExpression().acceptVisitor(this);
        }
        return Boolean.valueOf(this.m_hasAggr);
      }
      
      protected Boolean defaultVisit(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        Iterator localIterator = paramAnonymousIAENode.getChildItr();
        while (localIterator.hasNext())
        {
          ((IAENode)localIterator.next()).acceptVisitor(this);
          if ((this.m_hasRowExpr) && (this.m_hasAggr))
          {
            assert (this.m_exceptionCol != null);
            throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.NON_GROUPING_COLUMN_IN_SEL_LIST.name(), new String[] { this.m_exceptionCol });
          }
        }
        return Boolean.valueOf(this.m_hasAggr);
      }
    };
    return ((Boolean)this.m_selectList.acceptVisitor(local1)).booleanValue();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AEQuerySpecBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */