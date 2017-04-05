package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AESemantics;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AEGroupByListBuilder
  extends AEBuilderBase<AEValueExprList>
{
  private static final int INVALID_COLUMN_NUMBER = -1;
  boolean m_hasStarInSelectList = false;
  private ArrayList<IPTNode> m_selectListValExprs = new ArrayList();
  private HashMap<Integer, Integer> m_refMap = new HashMap();
  
  public AEGroupByListBuilder(AEQueryScope paramAEQueryScope, PTListNode paramPTListNode)
    throws ErrorException
  {
    super(paramAEQueryScope);
    preProcessSelectList(paramPTListNode);
  }
  
  public AEValueExprList visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    assert (paramPTListNode != null);
    if (paramPTListNode.getListType() != PTListType.GROUPBY_EXPRESSION_LIST) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    this.m_refMap.clear();
    AEValueExprList localAEValueExprList = new AEValueExprList();
    Iterator localIterator = paramPTListNode.getImmutableChildList().iterator();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      AEValueExpr localAEValueExpr1 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(localIPTNode);
      int i = getColumnNumber(localAEValueExpr1);
      if (-1 != i)
      {
        if (this.m_hasStarInSelectList) {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.GROUP_BY_POS_NUM_WITH_STAR.name());
        }
        if (i >= this.m_selectListValExprs.size()) {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.GROUP_BY_POS_NUM_OUT_OF_RANGE.name(), new String[] { Integer.toString(i) });
        }
        localObject = (IPTNode)this.m_selectListValExprs.get(i);
        localAEValueExpr1 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build((IPTNode)localObject);
      }
      if ((localAEValueExpr1 instanceof AEParameter)) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.DYN_PARAM_NOT_ALLOWED.name(), new String[] { "GROUP BY" });
      }
      Object localObject = new AEDefaultVisitor()
      {
        public Void visit(AECountStarAggrFn paramAnonymousAECountStarAggrFn)
          throws ErrorException
        {
          throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_N_SUBQUERY_IN_GROUPBY);
        }
        
        public Void visit(AEGeneralAggrFn paramAnonymousAEGeneralAggrFn)
          throws ErrorException
        {
          throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_N_SUBQUERY_IN_GROUPBY);
        }
        
        public Void visit(AEValueSubQuery paramAnonymousAEValueSubQuery)
          throws ErrorException
        {
          throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_N_SUBQUERY_IN_GROUPBY);
        }
        
        protected Void defaultVisit(IAENode paramAnonymousIAENode)
          throws ErrorException
        {
          Iterator localIterator = paramAnonymousIAENode.getChildItr();
          while (localIterator.hasNext()) {
            ((IAENode)localIterator.next()).acceptVisitor(this);
          }
          return null;
        }
      };
      localAEValueExpr1.acceptVisitor((IAENodeVisitor)localObject);
      if (AESemantics.findUniqueQueryScope(localAEValueExpr1, true) != getQueryScope()) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.EXPR_IN_GROUP_BY_NOT_ROW_BASED.name());
      }
      AEValueExpr localAEValueExpr2 = getQueryScope().addGroupingExpr(localAEValueExpr1);
      if (null != localAEValueExpr2)
      {
        localAEValueExprList.addNode(localAEValueExpr2);
        if (-1 != i) {
          this.m_refMap.put(Integer.valueOf(localAEValueExprList.getNumChildren() - 1), Integer.valueOf(i));
        }
      }
    }
    assert (0 < localAEValueExprList.getNumChildren());
    return localAEValueExprList;
  }
  
  private int getColumnNumber(AEValueExpr paramAEValueExpr)
    throws ErrorException
  {
    int i = -1;
    if ((paramAEValueExpr instanceof AELiteral))
    {
      AELiteral localAELiteral = (AELiteral)paramAEValueExpr;
      if (PTLiteralType.USINT == localAELiteral.getLiteralType()) {
        try
        {
          i = Integer.parseInt(localAELiteral.getStringValue()) - 1;
          if (0 > i) {
            throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.GROUP_BY_POS_NUM_OUT_OF_RANGE.name(), new String[] { Integer.toString(i) });
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.GROUP_BY_POS_NUM_OUT_OF_RANGE.name(), new String[] { "UNKNOWN" });
        }
      }
    }
    return i;
  }
  
  private void preProcessSelectList(PTListNode paramPTListNode)
    throws ErrorException
  {
    Iterator localIterator = paramPTListNode.getChildItr();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if (!localIPTNode.isTerminalNode())
      {
        PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode;
        switch (localPTNonterminalNode.getNonterminalType())
        {
        case COLUMN_REFERENCE: 
          this.m_hasStarInSelectList = true;
          return;
        case DERIVED_COLUMN: 
          this.m_selectListValExprs.add(localPTNonterminalNode.getChild(PTPositionalType.VALUE_EXPRESSION));
          break;
        default: 
          throw SQLEngineExceptionFactory.invalidParseTreeException();
        }
      }
    }
  }
  
  public Map<Integer, Integer> getGroupingListOrdinalReferenceMap()
  {
    return this.m_refMap;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEGroupByListBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */