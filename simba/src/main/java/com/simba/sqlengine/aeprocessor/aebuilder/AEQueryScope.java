package com.simba.sqlengine.aeprocessor.aebuilder;

import com.simba.dsi.core.utilities.Variant;
import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.OpenTableType;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEQueryScope
{
  private ClauseType m_currentClause;
  private AEQueryScope m_parent;
  private AESymbolTable m_symbolTable;
  private SqlDataEngine m_dataEngine;
  private OpenTableType m_openTableType;
  private boolean m_isCorrelated;
  private boolean m_hasOrderBy = false;
  private IPTNode m_ptSortSpecList = null;
  private AEJoin m_currentJoinExpr = null;
  private boolean m_hasDistinct = false;
  private boolean m_shouldCombineEquivGroupByColumns;
  private AEValueExprList m_aggregateList = new AEValueExprList();
  private ArrayList<AEProxyColumn> m_proxyCols = new ArrayList();
  private boolean m_hasImpliedGroupBy = false;
  private boolean m_hasGroupingExpression = false;
  
  public AEQueryScope(SqlDataEngine paramSqlDataEngine)
    throws ErrorException
  {
    if (null == paramSqlDataEngine) {
      throw new NullPointerException("DataEngine cannot be null.");
    }
    this.m_parent = null;
    this.m_dataEngine = paramSqlDataEngine;
    this.m_shouldCombineEquivGroupByColumns = paramSqlDataEngine.getProperty(5).getString().equals("Y");
    init();
  }
  
  private AEQueryScope(AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    if (null == paramAEQueryScope) {
      throw new NullPointerException("Parent scope cannot be null.");
    }
    this.m_parent = paramAEQueryScope;
    this.m_dataEngine = this.m_parent.getDataEngine();
    this.m_shouldCombineEquivGroupByColumns = paramAEQueryScope.m_shouldCombineEquivGroupByColumns;
    init();
  }
  
  private void init()
    throws ErrorException
  {
    this.m_currentClause = ClauseType.NONE;
    this.m_openTableType = OpenTableType.READ_ONLY;
    this.m_isCorrelated = false;
    this.m_symbolTable = new AESymbolTable(isCaseSensitive());
  }
  
  public AEQueryScope createChildScope()
    throws ErrorException
  {
    return new AEQueryScope(this);
  }
  
  public boolean isCaseSensitive()
  {
    return this.m_dataEngine.getContext().isSqlCaseSensitive();
  }
  
  public boolean isTopMost()
  {
    return null == this.m_parent;
  }
  
  public boolean hasOrderBy()
  {
    return this.m_hasOrderBy;
  }
  
  public void setHasOrderBy()
  {
    this.m_hasOrderBy = true;
  }
  
  public void addTableSymbol(AENamedRelationalExpr paramAENamedRelationalExpr)
    throws ErrorException
  {
    this.m_symbolTable.addTable(paramAENamedRelationalExpr);
  }
  
  public ClauseType getCurrentClause()
  {
    return this.m_currentClause;
  }
  
  public Iterator<AEColumnInfo> getColumnItr()
  {
    return this.m_symbolTable.getColumnItr();
  }
  
  public Iterator<AEColumnInfo> getColumnItr(AEQTableName paramAEQTableName)
    throws SQLEngineException
  {
    return this.m_symbolTable.getColumnItr(paramAEQTableName);
  }
  
  public SqlDataEngine getDataEngine()
  {
    return this.m_dataEngine;
  }
  
  public AEQueryScope getParentScope()
  {
    return this.m_parent;
  }
  
  public OpenTableType getOpenTableType()
  {
    return this.m_openTableType;
  }
  
  public IPTNode getPtSortSpecList()
  {
    return this.m_ptSortSpecList;
  }
  
  public void setPtSortSpecList(IPTNode paramIPTNode)
  {
    this.m_ptSortSpecList = paramIPTNode;
  }
  
  public boolean hasDistinct()
  {
    return this.m_hasDistinct;
  }
  
  public void setHasDistinct(boolean paramBoolean)
  {
    this.m_hasDistinct = paramBoolean;
  }
  
  public boolean hasSetOperation()
  {
    return false;
  }
  
  public AEColumnInfo resolveColumn(AEQColumnName paramAEQColumnName)
    throws ErrorException
  {
    return findColumn(paramAEQColumnName, false);
  }
  
  public void setCurrentClause(ClauseType paramClauseType)
  {
    this.m_currentClause = paramClauseType;
  }
  
  public void setCurrentJoinExpr(AEJoin paramAEJoin)
  {
    this.m_currentJoinExpr = paramAEJoin;
  }
  
  public boolean isQueryCorrelated()
  {
    return this.m_isCorrelated;
  }
  
  public AEValueExpr addAggregateFunction(AEAggrFn paramAEAggrFn)
  {
    if (this.m_currentClause == ClauseType.GROUP_BY) {
      throw new IllegalArgumentException("Aggregate functions are not added to the query in the correct order for execution.");
    }
    int i = this.m_aggregateList.findNode(paramAEAggrFn);
    if (-1 == i)
    {
      if ((this.m_currentClause != ClauseType.SELECT_LIST) && (this.m_currentClause != ClauseType.HAVING) && (this.m_currentClause != ClauseType.ORDER_BY)) {
        throw new IllegalArgumentException("Aggregate functions are not added to the query in the correct order for execution.");
      }
      this.m_aggregateList.addNode(paramAEAggrFn);
      i = this.m_aggregateList.getNumChildren() - 1;
    }
    AEProxyColumn localAEProxyColumn = new AEProxyColumn((AEValueExpr)this.m_aggregateList.getChild(i), this, i);
    registerProxyColumn(localAEProxyColumn);
    return localAEProxyColumn;
  }
  
  public AEValueExpr addGroupingExpr(AEValueExpr paramAEValueExpr)
  {
    assert (null != paramAEValueExpr);
    assert (ClauseType.GROUP_BY == this.m_currentClause);
    if ((this.m_shouldCombineEquivGroupByColumns) && (0 <= this.m_aggregateList.findNode(paramAEValueExpr))) {
      return null;
    }
    this.m_aggregateList.addNode(paramAEValueExpr);
    this.m_hasGroupingExpression = true;
    AEProxyColumn localAEProxyColumn = new AEProxyColumn(paramAEValueExpr, this, this.m_aggregateList.getNumChildren() - 1);
    registerProxyColumn(localAEProxyColumn);
    return localAEProxyColumn;
  }
  
  public AEValueExpr proxyToAggregateExpr(AEValueExpr paramAEValueExpr)
  {
    int i = this.m_aggregateList.findNode(paramAEValueExpr);
    if (-1 != i)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)this.m_aggregateList.getChild(i);
      AEProxyColumn localAEProxyColumn = new AEProxyColumn(localAEValueExpr, this, i);
      registerProxyColumn(localAEProxyColumn);
      return localAEProxyColumn;
    }
    return null;
  }
  
  private void registerProxyColumn(AEProxyColumn paramAEProxyColumn)
  {
    this.m_proxyCols.add(paramAEProxyColumn);
  }
  
  public AEValueExprList getAggregateList()
  {
    return this.m_aggregateList;
  }
  
  public List<? extends AEProxyColumn> getProxyColumns()
  {
    return this.m_proxyCols;
  }
  
  public boolean hasGroupingExpression()
  {
    return this.m_hasGroupingExpression;
  }
  
  private void setQueryIsCorrelated()
  {
    this.m_isCorrelated = true;
  }
  
  public boolean hasAggregate()
  {
    return (this.m_aggregateList.getNumChildren() > 0) || (this.m_hasImpliedGroupBy);
  }
  
  public void setImpliedGroupBy(boolean paramBoolean)
  {
    this.m_hasImpliedGroupBy = paramBoolean;
  }
  
  public void setOpenTableType(OpenTableType paramOpenTableType)
  {
    this.m_openTableType = paramOpenTableType;
  }
  
  private AEColumnInfo findColumn(AEQColumnName paramAEQColumnName, boolean paramBoolean)
    throws ErrorException
  {
    AEColumnInfo localAEColumnInfo1 = null;
    AEColumnInfo localAEColumnInfo2;
    if (ClauseType.FROM == this.m_currentClause)
    {
      if (null != this.m_currentJoinExpr)
      {
        localAEColumnInfo1 = this.m_currentJoinExpr.findQColumn(paramAEQColumnName, isCaseSensitive());
        if (null != localAEColumnInfo1)
        {
          localAEColumnInfo2 = new AEColumnInfo(localAEColumnInfo1.getNamedRelationalExpr(), localAEColumnInfo1.getColumnNum(), paramBoolean);
          localAEColumnInfo2.setResolvedQueryScope(this);
          return localAEColumnInfo2;
        }
      }
    }
    else
    {
      localAEColumnInfo1 = this.m_symbolTable.findColumn(paramAEQColumnName);
      if (null != localAEColumnInfo1)
      {
        localAEColumnInfo2 = new AEColumnInfo(localAEColumnInfo1.getNamedRelationalExpr(), localAEColumnInfo1.getColumnNum(), paramBoolean);
        localAEColumnInfo2.setResolvedQueryScope(this);
        return localAEColumnInfo2;
      }
    }
    if ((null == localAEColumnInfo1) && (shouldSearchParent()))
    {
      localAEColumnInfo1 = this.m_parent.findColumn(paramAEQColumnName, true);
      if (null != localAEColumnInfo1)
      {
        setQueryIsCorrelated();
        return localAEColumnInfo1;
      }
    }
    throw new SQLEngineException(DiagState.DIAG_COLUMN_MISSING, SQLEngineMessageKey.COLUMN_NOT_FOUND.name(), new String[] { paramAEQColumnName.toString() });
  }
  
  private boolean shouldSearchParent()
  {
    return (!isTopMost()) && (ClauseType.GROUP_BY != this.m_currentClause) && (ClauseType.ORDER_BY != this.m_currentClause);
  }
  
  public static enum ClauseType
  {
    NONE,  FROM,  WHERE,  GROUP_BY,  HAVING,  SELECT_LIST,  ORDER_BY;
    
    private ClauseType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/AEQueryScope.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */