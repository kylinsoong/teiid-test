package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.ListTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.ParseTreeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope.ClauseType;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEColumnReferenceBuilder;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEValueExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESort;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.exceptions.InvalidQueryException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class AESortBuilder
  extends AEBuilderBase<AESort>
{
  private AEProject m_projectNode;
  
  public AESortBuilder(AEQueryScope paramAEQueryScope, AEProject paramAEProject)
  {
    super(paramAEQueryScope);
    if (null == paramAEProject) {
      throw new IllegalArgumentException("projectNode may not be null.");
    }
    this.m_projectNode = paramAEProject;
  }
  
  public AESort visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    if (null == this.m_projectNode) {
      throw new IllegalStateException("visit called more than once on AESortBuilder.");
    }
    checkProjectionListIsValid(paramPTListNode);
    getQueryScope().setCurrentClause(AEQueryScope.ClauseType.ORDER_BY);
    int i = this.m_projectNode.getColumnCount();
    List localList = buildSortSpecificationList(paramPTListNode);
    AESort localAESort = new AESort(this.m_projectNode, localList, i);
    this.m_projectNode = null;
    return localAESort;
  }
  
  private List<AESortSpec> buildSortSpecificationList(PTListNode paramPTListNode)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = this.m_projectNode.getProjectionList();
    int i = localAEValueExprList.getNumChildren();
    ArrayList localArrayList = new ArrayList(paramPTListNode.numChildren());
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    Iterator localIterator = paramPTListNode.getChildItr();
    while (localIterator.hasNext())
    {
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIterator.next();
      IPTNode localIPTNode = localPTNonterminalNode.getChild(PTPositionalType.SORT_KEY);
      boolean bool = isOrderSpecAscending(localPTNonterminalNode.getChild(PTPositionalType.ORDER_SPEC));
      int j = findColumnInSelectList(localAEValueExprList, localIPTNode);
      if (0 > j)
      {
        AEValueExpr localAEValueExpr = (AEValueExpr)localAEValueExprBuilder.build(localIPTNode);
        if ((localAEValueExpr instanceof AELiteral))
        {
          j = decodeColumnNumber((AELiteral)localAEValueExpr);
          if ((j < 0) || (j >= i)) {
            throw SQLEngineExceptionFactory.orderByPositionOutOfRangeException();
          }
        }
        else
        {
          if ((localAEValueExpr instanceof AEParameter)) {
            throw new SQLEngineException(SQLEngineMessageKey.DYN_PARAM_NOT_ALLOWED.name(), new String[] { "ORDER BY" });
          }
          validateSortKey(localAEValueExpr);
          j = resolveColumn(localAEValueExprList, localAEValueExpr);
        }
      }
      assert (j >= 0);
      localArrayList.add(new AESortSpec(j, bool));
    }
    return localArrayList;
  }
  
  private int findColumnInSelectList(AEValueExprList paramAEValueExprList, IPTNode paramIPTNode)
    throws ErrorException
  {
    int i = -1;
    AEQColumnName localAEQColumnName;
    try
    {
      localAEQColumnName = AEColumnReferenceBuilder.buildQualifiedColumnName(getQueryScope().getDataEngine().getContext(), paramIPTNode);
    }
    catch (ErrorException localErrorException)
    {
      return i;
    }
    int j = paramAEValueExprList.getNumChildren();
    for (int k = 0; k < j; k++)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)paramAEValueExprList.getChild(k);
      if (localAEValueExpr.matchesName(localAEQColumnName, getQueryScope().isCaseSensitive())) {
        if (localAEValueExpr.isSortable())
        {
          if (-1 != i) {
            throw SQLEngineExceptionFactory.columnReferenceNotUniqueException(localAEQColumnName.toString());
          }
          i = k;
        }
        else
        {
          throw SQLEngineExceptionFactory.invalidOrderByColumnException(localAEQColumnName.toString());
        }
      }
    }
    return i;
  }
  
  private int resolveColumn(AEValueExprList paramAEValueExprList, AEValueExpr paramAEValueExpr)
    throws InvalidQueryException
  {
    int i = paramAEValueExprList.findNode(paramAEValueExpr);
    if (0 > i)
    {
      if (getQueryScope().hasDistinct()) {
        throw new InvalidQueryException(SQLEngineMessageKey.ORDERBY_EXPR_NOT_IN_SELECT_DISTINCT.name());
      }
      if (getQueryScope().hasSetOperation()) {
        throw new InvalidQueryException(SQLEngineMessageKey.ORDERBY_EXPR_NOT_IN_SELECTLIST.name());
      }
      i = paramAEValueExprList.getNumChildren();
      paramAEValueExprList.addNode(paramAEValueExpr);
    }
    return i;
  }
  
  private void validateSortKey(AEValueExpr paramAEValueExpr)
    throws ErrorException
  {
    AEDefaultVisitor local1 = new AEDefaultVisitor()
    {
      public Boolean visit(AESearchedCase paramAnonymousAESearchedCase)
        throws ErrorException
      {
        throw SQLEngineExceptionFactory.invalidOrderByColumnException("CASE");
      }
      
      public Boolean visit(AESimpleCase paramAnonymousAESimpleCase)
        throws ErrorException
      {
        throw SQLEngineExceptionFactory.invalidOrderByColumnException("CASE");
      }
      
      public Boolean visit(AEColumnReference paramAnonymousAEColumnReference)
        throws ErrorException
      {
        if (!paramAnonymousAEColumnReference.isSortable()) {
          throw SQLEngineExceptionFactory.invalidOrderByColumnException(paramAnonymousAEColumnReference.getQColumnName().toString());
        }
        return Boolean.valueOf(true);
      }
      
      public Boolean visit(AEGeneralAggrFn paramAnonymousAEGeneralAggrFn)
        throws ErrorException
      {
        switch (AESortBuilder.2.$SwitchMap$com$simba$sqlengine$aeprocessor$aetree$value$AEAggrFn$AggrFnId[paramAnonymousAEGeneralAggrFn.getAggrFnId().ordinal()])
        {
        case 1: 
        case 2: 
          if (AESortBuilder.this.getQueryScope().hasGroupingExpression()) {
            return Boolean.valueOf(true);
          }
          break;
        }
        return (Boolean)paramAnonymousAEGeneralAggrFn.getOperand().acceptVisitor(this);
      }
      
      public Boolean visit(AECountStarAggrFn paramAnonymousAECountStarAggrFn)
        throws ErrorException
      {
        return Boolean.valueOf(AESortBuilder.this.getQueryScope().hasGroupingExpression());
      }
      
      protected Boolean defaultVisit(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        boolean bool = false;
        Iterator localIterator = paramAnonymousIAENode.getChildItr();
        while (localIterator.hasNext())
        {
          IAENode localIAENode = (IAENode)localIterator.next();
          bool = (bool) || (((Boolean)localIAENode.acceptVisitor(this)).booleanValue());
        }
        return Boolean.valueOf(bool);
      }
    };
    if (!((Boolean)paramAEValueExpr.acceptVisitor(local1)).booleanValue()) {
      throw SQLEngineExceptionFactory.invalidOrderByExprException();
    }
  }
  
  private static void checkProjectionListIsValid(PTListNode paramPTListNode)
    throws ErrorException
  {
    AEBuilderCheck.ParseTreeMatcher localParseTreeMatcher = AEBuilderCheck.nonTerminal(PTNonterminalType.SORT_SPECIFICATION).withExactChildren(PTPositionalType.SORT_KEY, AEBuilderCheck.anything(), PTPositionalType.ORDER_SPEC, AEBuilderCheck.optional(AEBuilderCheck.instanceOf(PTFlagNode.class)));
    AEBuilderCheck.checkThat(paramPTListNode, ((AEBuilderCheck.ListTypeMatcher)AEBuilderCheck.is(AEBuilderCheck.list(PTListType.SORT_SPECIFICATION_LIST))).withChildren(localParseTreeMatcher));
  }
  
  private static int decodeColumnNumber(AELiteral paramAELiteral)
    throws ErrorException
  {
    if (PTLiteralType.USINT != paramAELiteral.getLiteralType()) {
      throw SQLEngineExceptionFactory.invalidOrderByExprException();
    }
    try
    {
      int i = Integer.parseInt(paramAELiteral.getStringValue());
      return i - 1;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw SQLEngineExceptionFactory.orderByPositionOutOfRangeException();
    }
  }
  
  private static boolean isOrderSpecAscending(IPTNode paramIPTNode)
  {
    if (paramIPTNode.isEmptyNode()) {
      return true;
    }
    assert (EnumSet.of(PTFlagType.ASC, PTFlagType.DESC).contains(((PTFlagNode)paramIPTNode).getFlagType()));
    return PTFlagType.ASC == ((PTFlagNode)paramIPTNode).getFlagType();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AESortBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */