package com.simba.sqlengine.aeprocessor.aebuilder.statement;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope.ClauseType;
import com.simba.sqlengine.aeprocessor.aebuilder.bool.AEBooleanExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AERelationalExprBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AETableNameBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEColumnReferenceBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDelete;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsert;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsertDefaults;
import com.simba.sqlengine.aeprocessor.aetree.statement.AERowCountStatement;
import com.simba.sqlengine.aeprocessor.aetree.statement.AESetClause;
import com.simba.sqlengine.aeprocessor.aetree.statement.AESetClauseList;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEUpdate;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.OpenTableType;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEDmlStatementBuilder
  extends AEBuilderBase<IAEStatement>
{
  public AEDmlStatementBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public IAEStatement visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case INSERT_STATEMENT: 
      return buildInsertStmt(paramPTNonterminalNode);
    case UPDATE_STATEMENT_SEARCHED: 
    case UPSERT_STATEMENT: 
      return buildUpdateStmt(paramPTNonterminalNode);
    case DELETE_STATEMENT_SEARCHED: 
      return buildDeleteStmt(paramPTNonterminalNode);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private AEDelete buildDeleteStmt(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.DELETE_STATEMENT_SEARCHED).withExactChildren(PTPositionalType.TABLE_NAME, AEBuilderCheck.nonTerminal(PTNonterminalType.TABLE_NAME), PTPositionalType.SEARCH_COND, AEBuilderCheck.optional(AEBuilderCheck.nonTerminal(PTNonterminalType.WHERE_CLAUSE)))));
    AEQueryScope localAEQueryScope = getQueryScope();
    AEUtils.checkReadOnly(localAEQueryScope.getDataEngine().getContext(), "DELETE");
    localAEQueryScope.setOpenTableType(OpenTableType.READ_WRITE);
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.NONE);
    AETable localAETable = new AETableNameBuilder(localAEQueryScope).visit((PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.TABLE_NAME));
    localAEQueryScope.addTableSymbol(localAETable);
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND);
    Object localObject;
    if (localIPTNode1.isEmptyNode())
    {
      localObject = new AEBooleanTrue();
    }
    else
    {
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode1;
      IPTNode localIPTNode2 = localPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND);
      if (null == localIPTNode2) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.WHERE);
      localObject = (AEBooleanExpr)new AEBooleanExprBuilder(localAEQueryScope).build(localIPTNode2);
    }
    return new AEDelete(localAETable, (AEBooleanExpr)localObject);
  }
  
  private AERowCountStatement buildInsertStmt(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.INSERT_STATEMENT).withExactChildren(PTPositionalType.TABLE_NAME, AEBuilderCheck.nonTerminal(PTNonterminalType.TABLE_NAME), PTPositionalType.INSERT_LIST, AEBuilderCheck.eitherOf(AEBuilderCheck.nonTerminal(PTNonterminalType.INSERT_LIST), AEBuilderCheck.flagNode(PTFlagType.DEFAULT, new PTFlagType[0])))));
    AEQueryScope localAEQueryScope = getQueryScope();
    AEUtils.checkReadOnly(localAEQueryScope.getDataEngine().getContext(), "INSERT");
    localAEQueryScope.setOpenTableType(OpenTableType.READ_WRITE);
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.NONE);
    AETable localAETable = new AETableNameBuilder(localAEQueryScope).visit((PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.TABLE_NAME));
    localAEQueryScope.addTableSymbol(localAETable);
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.INSERT_LIST);
    if ((localIPTNode1 instanceof PTFlagNode)) {
      return new AEInsertDefaults(localAETable);
    }
    PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode1;
    AEBuilderCheck.checkThat(localPTNonterminalNode, ((AEBuilderCheck.NonterminalTypeMatcher)AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.INSERT_LIST))).withExactChildren(PTPositionalType.COLUMN_LIST, AEBuilderCheck.optionalList(PTListType.COLUMN_NAME_LIST), PTPositionalType.QUERY_EXPRESSION, AEBuilderCheck.anything()));
    IPTNode localIPTNode2 = localPTNonterminalNode.getChild(PTPositionalType.COLUMN_LIST);
    AEValueExprList localAEValueExprList = buildColumnList(localIPTNode2, localAEQueryScope);
    ArrayList localArrayList = new ArrayList(localAEValueExprList.getNumChildren());
    Object localObject1 = localAEValueExprList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (AEValueExpr)((Iterator)localObject1).next();
      localArrayList.add(Integer.valueOf(((AEColumnReference)localObject2).getColumnNum()));
    }
    localObject1 = localArrayList.isEmpty() ? localAETable.createResultSetColumns() : localAETable.createResultSetColumns(localArrayList);
    Object localObject2 = buildInsertTableValues(localPTNonterminalNode.getChild(PTPositionalType.QUERY_EXPRESSION), (List)localObject1);
    SqlDataEngineContext localSqlDataEngineContext = localAEQueryScope.getDataEngine().getContext();
    return new AEInsert(localAETable, localAEValueExprList, (AERelationalExpr)localObject2, localSqlDataEngineContext);
  }
  
  private AERelationalExpr buildInsertTableValues(IPTNode paramIPTNode, List<IColumn> paramList)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = new AEQueryScope(getQueryScope().getDataEngine());
    localAEQueryScope.setOpenTableType(OpenTableType.READ_ONLY);
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.NONE);
    AERelationalExprBuilder localAERelationalExprBuilder = new AERelationalExprBuilder(localAEQueryScope, true);
    if ((paramIPTNode instanceof PTListNode))
    {
      PTListNode localPTListNode = (PTListNode)paramIPTNode;
      if (PTListType.TABLE_VALUE_LIST == localPTListNode.getListType()) {
        return localAERelationalExprBuilder.buildTableValueList(localPTListNode, paramList);
      }
    }
    return (AERelationalExpr)localAERelationalExprBuilder.build(paramIPTNode);
  }
  
  private AEUpdate buildUpdateStmt(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.eitherOf(AEBuilderCheck.nonTerminal(PTNonterminalType.UPDATE_STATEMENT_SEARCHED), AEBuilderCheck.nonTerminal(PTNonterminalType.UPSERT_STATEMENT)));
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.TABLE_NAME, AEBuilderCheck.nonTerminal(PTNonterminalType.TABLE_NAME), PTPositionalType.SET_CLAUSE_LIST, AEBuilderCheck.list(PTListType.SET_CLAUSE_LIST), PTPositionalType.SEARCH_COND, AEBuilderCheck.optional(AEBuilderCheck.nonTerminal(PTNonterminalType.WHERE_CLAUSE)))));
    AEQueryScope localAEQueryScope = getQueryScope();
    AEUtils.checkReadOnly(localAEQueryScope.getDataEngine().getContext(), "UPDATE");
    localAEQueryScope.setOpenTableType(OpenTableType.READ_WRITE);
    localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.NONE);
    AETable localAETable = new AETableNameBuilder(localAEQueryScope).visit((PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.TABLE_NAME));
    localAEQueryScope.addTableSymbol(localAETable);
    AESetClauseList localAESetClauseList = buildSetClauseList(paramPTNonterminalNode.getChild(PTPositionalType.SET_CLAUSE_LIST), localAEQueryScope);
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND);
    Object localObject;
    if (localIPTNode1.isEmptyNode())
    {
      localObject = new AEBooleanTrue();
    }
    else
    {
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode1;
      IPTNode localIPTNode2 = localPTNonterminalNode.getChild(PTPositionalType.SEARCH_COND);
      if (null == localIPTNode2) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localAEQueryScope.setCurrentClause(AEQueryScope.ClauseType.WHERE);
      localObject = (AEBooleanExpr)new AEBooleanExprBuilder(localAEQueryScope).build(localIPTNode2);
    }
    if (paramPTNonterminalNode.getNonterminalType() == PTNonterminalType.UPSERT_STATEMENT) {
      return new AEUpdate(localAETable, localAESetClauseList, (AEBooleanExpr)localObject, true);
    }
    return new AEUpdate(localAETable, localAESetClauseList, (AEBooleanExpr)localObject, false);
  }
  
  private static AEValueExprList buildColumnList(IPTNode paramIPTNode, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = new AEValueExprList();
    if (paramIPTNode.isEmptyNode()) {
      return localAEValueExprList;
    }
    if (!(paramIPTNode instanceof PTListNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    PTListNode localPTListNode = (PTListNode)paramIPTNode;
    assert (PTListType.COLUMN_NAME_LIST == localPTListNode.getListType());
    assert (0 < localPTListNode.numChildren());
    AEColumnReferenceBuilder localAEColumnReferenceBuilder = new AEColumnReferenceBuilder(paramAEQueryScope);
    Iterator localIterator = localPTListNode.getChildItr();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if (!(localIPTNode instanceof PTIdentifierNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localAEValueExprList.addNode(localAEColumnReferenceBuilder.visit((PTIdentifierNode)localIPTNode));
    }
    return localAEValueExprList;
  }
  
  private static AESetClause buildSetClause(PTNonterminalNode paramPTNonterminalNode, AEValueExprBuilder paramAEValueExprBuilder, AEColumnReferenceBuilder paramAEColumnReferenceBuilder)
    throws ErrorException
  {
    if (PTNonterminalType.SET_CLAUSE != paramPTNonterminalNode.getNonterminalType()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.COLUMN_NAME);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.VALUE_EXPRESSION);
    if ((!(localIPTNode1 instanceof PTIdentifierNode)) || (null == localIPTNode2)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEColumnReference localAEColumnReference = (AEColumnReference)paramAEColumnReferenceBuilder.build(localIPTNode1);
    AEValueExpr localAEValueExpr = (AEValueExpr)paramAEValueExprBuilder.build(localIPTNode2);
    return new AESetClause(localAEColumnReference, localAEValueExpr);
  }
  
  private static AESetClauseList buildSetClauseList(IPTNode paramIPTNode, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    AESetClauseList localAESetClauseList = new AESetClauseList();
    if (!paramIPTNode.isEmptyNode())
    {
      PTListNode localPTListNode = (PTListNode)paramIPTNode;
      AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(paramAEQueryScope);
      AEColumnReferenceBuilder localAEColumnReferenceBuilder = new AEColumnReferenceBuilder(paramAEQueryScope);
      Iterator localIterator = localPTListNode.getChildItr();
      while (localIterator.hasNext())
      {
        IPTNode localIPTNode = (IPTNode)localIterator.next();
        if (!(localIPTNode instanceof PTNonterminalNode)) {
          throw SQLEngineExceptionFactory.invalidParseTreeException();
        }
        localAESetClauseList.addNode(buildSetClause((PTNonterminalNode)localIPTNode, localAEValueExprBuilder, localAEColumnReferenceBuilder));
      }
    }
    return localAESetClauseList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/statement/AEDmlStatementBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */