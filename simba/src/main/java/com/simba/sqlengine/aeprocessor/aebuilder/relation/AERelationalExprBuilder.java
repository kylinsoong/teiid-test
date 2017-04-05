package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.ParseTreeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEExcept;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETableConstructor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnion;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDefault;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AERelationalExprBuilder
  extends AEBuilderBase<AERelationalExpr>
{
  private boolean m_isQueryCorrelated;
  private boolean m_startNewQS;
  
  public AERelationalExprBuilder(AEQueryScope paramAEQueryScope, boolean paramBoolean)
  {
    super(paramAEQueryScope);
    if (null == paramAEQueryScope) {
      throw new NullPointerException("Query scope cannot be null.");
    }
    this.m_isQueryCorrelated = false;
    this.m_startNewQS = paramBoolean;
  }
  
  public boolean isQueryCorrelated()
  {
    return this.m_isQueryCorrelated;
  }
  
  public AERelationalExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case SELECT_STATEMENT: 
      return buildSelectStatement(paramPTNonterminalNode);
    case SORTED_SELECT_STATEMENT: 
      return buildSortedSelectStatement(paramPTNonterminalNode);
    case UNION: 
    case UNION_ALL: 
      return buildUnion(paramPTNonterminalNode);
    case EXCEPT: 
    case EXCEPT_ALL: 
      return buildExcept(paramPTNonterminalNode);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  public AERelationalExpr visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    switch (paramPTListNode.getListType())
    {
    case TABLE_VALUE_LIST: 
      return buildTableValueList(paramPTListNode, null);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  public AERelationalExpr buildTableValueList(PTListNode paramPTListNode, List<IColumn> paramList)
    throws ErrorException
  {
    assert (PTListType.TABLE_VALUE_LIST == paramPTListNode.getListType());
    if (paramPTListNode.numChildren() < 1) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    ArrayList localArrayList = new ArrayList();
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    Iterator localIterator = paramPTListNode.getChildItr();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      PTListNode localPTListNode = (localIPTNode instanceof PTListNode) ? (PTListNode)localIPTNode : null;
      if (null == localPTListNode) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      AEValueExprList localAEValueExprList = buildRowValueConstructor(localPTListNode, localAEValueExprBuilder);
      if (null != paramList)
      {
        if (localAEValueExprList.getNumChildren() != paramList.size()) {
          throw new SQLEngineException(DiagState.DIAG_INSERT_VAL_LIST_COL_LIST_MISMATCH, SQLEngineMessageKey.INVALID_NUMBER_INSERT_VALUES.name(), new String[] { "" + localAEValueExprList.getNumChildren(), "" + paramList.size() });
        }
        for (int i = 0; i < localAEValueExprList.getNumChildren(); i++)
        {
          AEValueExpr localAEValueExpr = (AEValueExpr)localAEValueExprList.getChild(i);
          if ((localAEValueExpr instanceof AEDefault))
          {
            AEDefault localAEDefault = (AEDefault)localAEValueExpr;
            localAEDefault.setMetadata((IColumn)paramList.get(i));
          }
        }
      }
      localArrayList.add(localAEValueExprList);
    }
    return new AETableConstructor(localArrayList, paramList, getQueryScope().getDataEngine().getContext().getCoercionHandler());
  }
  
  private AERelationalExpr buildSelectStatement(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope;
    if (this.m_startNewQS) {
      localAEQueryScope = getQueryScope().createChildScope();
    } else {
      localAEQueryScope = getQueryScope();
    }
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)new AEQuerySpecBuilder(localAEQueryScope).build(paramPTNonterminalNode);
    this.m_isQueryCorrelated = localAEQueryScope.isQueryCorrelated();
    return localAERelationalExpr;
  }
  
  private AERelationalExpr buildSortedSelectStatement(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    Object localObject = AEBuilderCheck.nonTerminal(PTNonterminalType.ORDER_BY).withExactChildren(PTPositionalType.SORT_SPEC_LIST, AEBuilderCheck.list(PTListType.SORT_SPECIFICATION_LIST));
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.SELECT, AEBuilderCheck.nonEmpty(), PTPositionalType.ORDER_BY, (AEBuilderCheck.ParseTreeMatcher)localObject));
    if (this.m_startNewQS) {
      localObject = getQueryScope().createChildScope();
    } else {
      localObject = getQueryScope();
    }
    PTNonterminalNode localPTNonterminalNode1 = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.SELECT);
    IPTNode localIPTNode = localPTNonterminalNode1.getChild(PTPositionalType.SELECT_LIMIT);
    if ((null == localIPTNode) || (localIPTNode.isEmptyNode())) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.ORDER_BY_IN_SUBQUERY_WITHOUT_TOP.name());
    }
    PTNonterminalNode localPTNonterminalNode2 = (PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.ORDER_BY);
    PTListNode localPTListNode = (PTListNode)localPTNonterminalNode2.getChild(PTPositionalType.SORT_SPEC_LIST);
    ((AEQueryScope)localObject).setPtSortSpecList(localPTListNode);
    ((AEQueryScope)localObject).setHasOrderBy();
    this.m_isQueryCorrelated = ((AEQueryScope)localObject).isQueryCorrelated();
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)new AERelationalExprBuilder((AEQueryScope)localObject, false).build(localPTNonterminalNode1);
    return localAERelationalExpr;
  }
  
  private AEExcept buildExcept(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    assert (paramPTNonterminalNode.numChildren() == 2);
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_LEFT);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_RIGHT);
    PTNonterminalType localPTNonterminalType = paramPTNonterminalNode.getNonterminalType();
    if (((PTNonterminalType.EXCEPT_ALL != localPTNonterminalType) && (PTNonterminalType.EXCEPT != localPTNonterminalType)) || (null == localIPTNode1) || (null == localIPTNode2)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEQueryScope localAEQueryScope1;
    AEQueryScope localAEQueryScope2;
    if (this.m_startNewQS)
    {
      localAEQueryScope1 = getQueryScope().createChildScope();
      localAEQueryScope2 = getQueryScope().createChildScope();
    }
    else
    {
      localAEQueryScope1 = getQueryScope();
      if (localAEQueryScope1.isTopMost()) {
        localAEQueryScope2 = new AEQueryScope(localAEQueryScope1.getDataEngine());
      } else {
        localAEQueryScope2 = localAEQueryScope1.getParentScope().createChildScope();
      }
    }
    boolean bool = PTNonterminalType.EXCEPT_ALL == localPTNonterminalType;
    AERelationalExprBuilder localAERelationalExprBuilder1 = new AERelationalExprBuilder(localAEQueryScope1, false);
    AERelationalExprBuilder localAERelationalExprBuilder2 = new AERelationalExprBuilder(localAEQueryScope2, false);
    AERelationalExpr localAERelationalExpr1 = (AERelationalExpr)localAERelationalExprBuilder1.build(localIPTNode1);
    AERelationalExpr localAERelationalExpr2 = (AERelationalExpr)localAERelationalExprBuilder2.build(localIPTNode2);
    validateSetOperands(localAERelationalExpr1, localAERelationalExpr2, bool ? "EXCEPT ALL" : "EXCEPT");
    this.m_isQueryCorrelated = ((localAERelationalExprBuilder1.isQueryCorrelated()) || (localAERelationalExprBuilder2.isQueryCorrelated()));
    return new AEExcept(localAERelationalExpr1, localAERelationalExpr2, bool, getQueryScope().getDataEngine().getContext().getCoercionHandler());
  }
  
  private AEUnion buildUnion(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (2 == paramPTNonterminalNode.numChildren())
    {
      boolean bool1;
      switch (paramPTNonterminalNode.getNonterminalType())
      {
      case UNION: 
        bool1 = false;
        break;
      case UNION_ALL: 
        bool1 = true;
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_LEFT);
      IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.BINARY_RIGHT);
      if ((null != localIPTNode1) && (null != localIPTNode2))
      {
        AEQueryScope localAEQueryScope1;
        AEQueryScope localAEQueryScope2;
        if (this.m_startNewQS)
        {
          localAEQueryScope1 = getQueryScope().createChildScope();
          localAEQueryScope2 = getQueryScope().createChildScope();
        }
        else
        {
          localAEQueryScope1 = getQueryScope();
          if (localAEQueryScope1.isTopMost()) {
            localAEQueryScope2 = new AEQueryScope(localAEQueryScope1.getDataEngine());
          } else {
            localAEQueryScope2 = localAEQueryScope1.getParentScope().createChildScope();
          }
        }
        AERelationalExprBuilder localAERelationalExprBuilder1 = new AERelationalExprBuilder(localAEQueryScope1, false);
        AERelationalExprBuilder localAERelationalExprBuilder2 = new AERelationalExprBuilder(localAEQueryScope2, false);
        AERelationalExpr localAERelationalExpr1 = (AERelationalExpr)localAERelationalExprBuilder1.build(localIPTNode1);
        AERelationalExpr localAERelationalExpr2 = (AERelationalExpr)localAERelationalExprBuilder2.build(localIPTNode2);
        boolean bool2 = (localAERelationalExprBuilder1.isQueryCorrelated()) || (localAERelationalExprBuilder2.isQueryCorrelated());
        validateSetOperands(localAERelationalExpr1, localAERelationalExpr2, bool1 ? "UNION ALL" : "UNION");
        this.m_isQueryCorrelated = bool2;
        return new AEUnion(localAERelationalExpr1, localAERelationalExpr2, bool1, getQueryScope().getDataEngine().getContext().getCoercionHandler());
      }
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private static AEValueExprList buildRowValueConstructor(PTListNode paramPTListNode, AEValueExprBuilder paramAEValueExprBuilder)
    throws ErrorException
  {
    if (PTListType.ROW_VALUE_LIST != paramPTListNode.getListType()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExprList localAEValueExprList = new AEValueExprList();
    for (int i = 0; i < paramPTListNode.numChildren(); i++) {
      localAEValueExprList.addNode(paramAEValueExprBuilder.build(paramPTListNode.getChild(i)));
    }
    return localAEValueExprList;
  }
  
  private static void validateSetOperands(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, String paramString)
    throws ErrorException
  {
    if (paramAERelationalExpr1.getColumnCount() != paramAERelationalExpr2.getColumnCount()) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.SET_OP_INVALID_NUM_COLUMNS.name(), new String[] { paramString });
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AERelationalExprBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */