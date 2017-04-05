package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AETableRefBuilder
  extends AEBuilderBase<AERelationalExpr>
{
  protected AETableRefBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
    if (null == paramAEQueryScope) {
      throw new NullPointerException("Query scope cannot be null.");
    }
  }
  
  public AERelationalExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    Object localObject = null;
    if (PTNonterminalType.TABLE_REFERENCE == paramPTNonterminalNode.getNonterminalType())
    {
      if (3 != paramPTNonterminalNode.numChildren()) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.CORRELATION_IDENT);
      IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.DERIVED_COLUMN_LIST);
      if ((null == localIPTNode1) || (null == localIPTNode2)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      IPTNode localIPTNode3 = paramPTNonterminalNode.getChild(PTPositionalType.TABLE_NAME);
      if (null != localIPTNode3)
      {
        localObject = (AERelationalExpr)new AETableNameBuilder(getQueryScope()).build(localIPTNode3);
      }
      else
      {
        IPTNode localIPTNode4 = paramPTNonterminalNode.getChild(PTPositionalType.SUBQUERY);
        if (null == localIPTNode4) {
          throw SQLEngineExceptionFactory.invalidParseTreeException();
        }
        AERelationalExprBuilder localAERelationalExprBuilder = new AERelationalExprBuilder(getQueryScope(), true);
        AERelationalExpr localAERelationalExpr = (AERelationalExpr)localAERelationalExprBuilder.build(((PTNonterminalNode)localIPTNode4).getChild(PTPositionalType.SINGLE_CHILD));
        localObject = new AESubQuery(localAERelationalExpr, localAERelationalExprBuilder.isQueryCorrelated(), true);
      }
      if (!localIPTNode1.isEmptyNode()) {
        setCorrelationName(localIPTNode1, localIPTNode2, (AERelationalExpr)localObject);
      }
      getQueryScope().addTableSymbol((AENamedRelationalExpr)localObject);
    }
    else
    {
      localObject = (AERelationalExpr)new AEJoinedTableBuilder(getQueryScope()).build(paramPTNonterminalNode);
    }
    assert (null != localObject);
    return (AERelationalExpr)localObject;
  }
  
  private void setCorrelationName(IPTNode paramIPTNode1, IPTNode paramIPTNode2, AERelationalExpr paramAERelationalExpr)
    throws ErrorException
  {
    if (!(paramIPTNode1 instanceof PTIdentifierNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    ((AENamedRelationalExpr)paramAERelationalExpr).setCorrelationName(((PTIdentifierNode)paramIPTNode1).getIdentifier());
    if ((paramIPTNode2 instanceof PTListNode)) {
      ((AENamedRelationalExpr)paramAERelationalExpr).overrideColumnNames(getRenamedColumnNames((PTListNode)paramIPTNode2), getQueryScope().isCaseSensitive());
    }
  }
  
  private List<String> getRenamedColumnNames(PTListNode paramPTListNode)
    throws ErrorException
  {
    if (PTListType.COLUMN_NAME_LIST != paramPTListNode.getListType()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    ArrayList localArrayList = new ArrayList(paramPTListNode.numChildren());
    Iterator localIterator = paramPTListNode.getChildItr();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if (!(localIPTNode instanceof PTIdentifierNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localArrayList.add(((PTIdentifierNode)localIPTNode).getIdentifier());
    }
    return localArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AETableRefBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */