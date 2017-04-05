package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.List;

public class AESelectListBuilder
  extends AEBuilderBase<AEValueExprList>
{
  public AESelectListBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
    if (paramAEQueryScope == null) {
      throw new NullPointerException("query scope for select list can not be null");
    }
  }
  
  public AEValueExprList visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    assert (paramPTListNode != null);
    if (paramPTListNode.getListType() != PTListType.SELECT_LIST) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExprList localAEValueExprList = new AEValueExprList();
    List localList = paramPTListNode.getImmutableChildList();
    for (int i = 0; i < localList.size(); i++)
    {
      IPTNode localIPTNode = (IPTNode)localList.get(i);
      if (!(localIPTNode instanceof PTNonterminalNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode;
      switch (localPTNonterminalNode.getNonterminalType())
      {
      case DERIVED_COLUMN: 
        localAEValueExprList.addNode(buildSelectListItem(localPTNonterminalNode));
        break;
      case COLUMN_REFERENCE: 
        buildStarColumn(localPTNonterminalNode, localAEValueExprList);
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
    }
    return localAEValueExprList;
  }
  
  private void buildStarColumn(PTNonterminalNode paramPTNonterminalNode, AEValueExprList paramAEValueExprList)
    throws ErrorException
  {
    assert (paramPTNonterminalNode.getNonterminalType() == PTNonterminalType.COLUMN_REFERENCE);
    AEQueryScope localAEQueryScope = getQueryScope();
    String str1 = AEUtils.getIdentifierString(paramPTNonterminalNode.getChild(PTPositionalType.TABLE_IDENT));
    String str2 = AEUtils.getIdentifierString(paramPTNonterminalNode.getChild(PTPositionalType.SCHEMA_IDENT));
    String str3 = AEUtils.getIdentifierString(paramPTNonterminalNode.getChild(PTPositionalType.CATALOG_IDENT));
    Object localObject;
    if ("".equals(str1))
    {
      if ((!str2.equals("")) || (!str3.equals(""))) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localObject = localAEQueryScope.getColumnItr();
      constructColumns((Iterator)localObject, paramAEValueExprList);
    }
    else
    {
      localObject = AEUtils.adjustCatalogAndSchema(new AEQTableName(str3, str2, str1), localAEQueryScope.getDataEngine().getContext(), false);
      Iterator localIterator = localAEQueryScope.getColumnItr((AEQTableName)localObject);
      constructColumns(localIterator, paramAEValueExprList);
    }
  }
  
  private void constructColumns(Iterator<AEColumnInfo> paramIterator, AEValueExprList paramAEValueExprList)
  {
    while (paramIterator.hasNext())
    {
      AEColumnInfo localAEColumnInfo = (AEColumnInfo)paramIterator.next();
      localAEColumnInfo.setResolvedQueryScope(getQueryScope());
      paramAEValueExprList.addNode(new AEColumnReference(localAEColumnInfo));
    }
  }
  
  private AEValueExpr buildSelectListItem(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    assert ((null != paramPTNonterminalNode) && (PTNonterminalType.DERIVED_COLUMN == paramPTNonterminalNode.getNonterminalType()));
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.VALUE_EXPRESSION);
    Object localObject = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode1);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.AS);
    if (!localIPTNode2.isEmptyNode())
    {
      if (!(localIPTNode2 instanceof PTIdentifierNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localObject = new AERename(((PTIdentifierNode)localIPTNode2).getIdentifier(), (AEValueExpr)localObject);
    }
    return (AEValueExpr)localObject;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AESelectListBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */