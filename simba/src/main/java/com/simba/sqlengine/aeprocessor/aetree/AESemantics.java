package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope.ClauseType;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;

public final class AESemantics
{
  private AESemantics()
  {
    throw new UnsupportedOperationException();
  }
  
  public static AEQueryScope findUniqueQueryScope(AEValueExpr paramAEValueExpr, boolean paramBoolean)
    throws ErrorException
  {
    return (AEQueryScope)AETreeWalker.walk(paramAEValueExpr, new FindUniqueQueryScopeAction(paramBoolean));
  }
  
  public static void checkAggrFnSemantics(AEValueExpr paramAEValueExpr, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = (AEQueryScope)AETreeWalker.walk(paramAEValueExpr, new CheckAggrFnAction());
    AEQueryScope.ClauseType localClauseType1 = paramAEQueryScope.getCurrentClause();
    switch (localClauseType1)
    {
    case FROM: 
      if ((localAEQueryScope == null) || (paramAEQueryScope == localAEQueryScope)) {
        throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_IN_ON);
      }
      break;
    case WHERE: 
      if ((localAEQueryScope == null) || (paramAEQueryScope == localAEQueryScope)) {
        throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_IN_WHERE);
      }
      break;
    case GROUP_BY: 
      throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_N_SUBQUERY_IN_GROUPBY);
    }
    if ((localAEQueryScope != null) && (paramAEQueryScope != localAEQueryScope))
    {
      AEQueryScope.ClauseType localClauseType2 = localAEQueryScope.getCurrentClause();
      if ((localClauseType2 != AEQueryScope.ClauseType.SELECT_LIST) && (localClauseType2 != AEQueryScope.ClauseType.HAVING))
      {
        if (localClauseType1 == AEQueryScope.ClauseType.FROM) {
          throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_IN_ON);
        }
        if (localClauseType1 == AEQueryScope.ClauseType.WHERE) {
          throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_IN_WHERE);
        }
        throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.AGGR_FN_NOT_IN_SEL_LIST_OR_HAVING);
      }
    }
  }
  
  private static class CheckAggrFnAction
    extends AESemantics.FindUniqueQueryScopeAction
  {
    public CheckAggrFnAction()
    {
      super();
    }
    
    public void act(IAENode paramIAENode)
      throws ErrorException
    {
      if (((paramIAENode instanceof AEAggrFn)) || ((paramIAENode instanceof AEValueSubQuery))) {
        throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.NESTED_AGGR_FN_NOT_ALLOWED);
      }
      super.act(paramIAENode);
    }
  }
  
  private static class FindUniqueQueryScopeAction
    extends AETreeWalker.Action<AEQueryScope>
  {
    private AEQueryScope m_queryScope = null;
    private boolean m_shouldThrow;
    
    public FindUniqueQueryScopeAction(boolean paramBoolean)
    {
      this.m_shouldThrow = paramBoolean;
    }
    
    public AEQueryScope getResult()
    {
      return this.m_queryScope;
    }
    
    public void act(IAENode paramIAENode)
      throws ErrorException
    {
      AEQueryScope localAEQueryScope = null;
      if ((paramIAENode instanceof AEProxyColumn)) {
        localAEQueryScope = ((AEProxyColumn)paramIAENode).getResolvedQueryScope();
      } else if ((paramIAENode instanceof AEColumnReference)) {
        localAEQueryScope = ((AEColumnReference)paramIAENode).getResolvedQueryScope();
      }
      if (localAEQueryScope != null) {
        if (this.m_queryScope == null)
        {
          this.m_queryScope = localAEQueryScope;
        }
        else if (this.m_queryScope != localAEQueryScope)
        {
          if (this.m_shouldThrow) {
            throw SQLEngineExceptionFactory.aggrFnSemanticsException(SQLEngineMessageKey.MULTI_COL_IN_AGGR_FN);
          }
          skipAll();
          this.m_queryScope = null;
        }
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AESemantics.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */