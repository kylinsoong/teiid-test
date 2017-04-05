package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.bool.ETComparison;
import com.simba.sqlengine.executor.etree.bool.functor.comp.BooleanFunctorFactory;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.value.ETColumnRef;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.LinkedList;

class NBFallBackJoinAlgorithm
  implements IJoinAlgorithmAdapter
{
  private IJoinAlgorithmAdapter m_fallBack;
  private ETJoinedUnitWrapper m_leftJoinedUnitWrapper;
  private ETJoinedUnitWrapper m_rightJoinedUnitWrapper;
  private LinkedList<ETBooleanExpr> m_filters;
  
  public NBFallBackJoinAlgorithm(ETRelationalExpr paramETRelationalExpr1, ETRelationalExpr paramETRelationalExpr2, AEJoin.AEJoinType paramAEJoinType, ETCancelState paramETCancelState, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties)
    throws ErrorException
  {
    boolean[] arrayOfBoolean1 = extractDataNeeded(paramETRelationalExpr1);
    boolean[] arrayOfBoolean2 = extractDataNeeded(paramETRelationalExpr2);
    this.m_leftJoinedUnitWrapper = new ETJoinedUnitWrapper(paramETRelationalExpr1, arrayOfBoolean1);
    this.m_rightJoinedUnitWrapper = new ETJoinedUnitWrapper(paramETRelationalExpr2, arrayOfBoolean2);
    this.m_filters = new LinkedList();
    this.m_fallBack = new NBJoinAlgorithm(paramETRelationalExpr1, paramETRelationalExpr2, paramAEJoinType, paramExternalAlgorithmProperties, paramETCancelState, paramExternalAlgorithmProperties.getLogger());
  }
  
  public void addFilter(int paramInt1, int paramInt2, AEComparisonType paramAEComparisonType)
    throws ErrorException
  {
    ETColumnRef localETColumnRef1 = new ETColumnRef(this.m_leftJoinedUnitWrapper, paramInt1, false);
    ETColumnRef localETColumnRef2 = new ETColumnRef(this.m_rightJoinedUnitWrapper, paramInt2, false);
    ETComparison localETComparison;
    switch (paramAEComparisonType)
    {
    case EQUAL: 
      IColumn localIColumn = this.m_leftJoinedUnitWrapper.getColumn(paramInt1);
      localETComparison = new ETComparison(localIColumn, localETColumnRef1, localETColumnRef2, BooleanFunctorFactory.getBoolCompFunctor(AEComparisonType.EQUAL, localIColumn.getTypeMetadata()));
      break;
    default: 
      throw new IllegalArgumentException("Only equal filter is supported.");
    }
    assert (localETComparison != null);
    this.m_filters.add(localETComparison);
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_fallBack.registerManagerAgent(paramIMemManagerAgent);
  }
  
  public long assign(long paramLong)
  {
    return this.m_fallBack.assign(paramLong);
  }
  
  public long getRequiredMemory()
  {
    return this.m_fallBack.getRequiredMemory();
  }
  
  public Pair<? extends IJoinUnit, ? extends IJoinUnit> loadNextJoinUnit()
    throws ErrorException
  {
    Pair localPair = this.m_fallBack.loadNextJoinUnit();
    if (localPair != null)
    {
      this.m_leftJoinedUnitWrapper.setJoinUnit((IJoinUnit)localPair.key());
      this.m_rightJoinedUnitWrapper.setJoinUnit((IJoinUnit)localPair.value());
    }
    return localPair;
  }
  
  public boolean isMasterJoinUnitOnLeft()
  {
    return this.m_fallBack.isMasterJoinUnitOnLeft();
  }
  
  public void match()
    throws ErrorException
  {
    this.m_fallBack.match();
  }
  
  public boolean isOuterRow()
  {
    return this.m_fallBack.isOuterRow();
  }
  
  public boolean moveMaster()
    throws ErrorException
  {
    return this.m_fallBack.moveMaster();
  }
  
  public boolean moveSlave()
    throws ErrorException
  {
    boolean bool;
    while (((bool = this.m_fallBack.moveSlave())) && (!this.m_fallBack.isOuterRow()) && (!evaluate())) {}
    return bool;
  }
  
  public void seekSlave()
  {
    this.m_fallBack.seekSlave();
  }
  
  public void closeRelations()
  {
    this.m_fallBack.closeRelations();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_fallBack.open(paramCursorType);
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_fallBack.reset();
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_fallBack.registerWarningListener(paramIWarningListener);
  }
  
  private boolean evaluate()
    throws ErrorException
  {
    Iterator localIterator = this.m_filters.iterator();
    while (localIterator.hasNext())
    {
      ETBooleanExpr localETBooleanExpr = (ETBooleanExpr)localIterator.next();
      if (localETBooleanExpr.evaluate() != ETBoolean.SQL_BOOLEAN_TRUE) {
        return false;
      }
    }
    return true;
  }
  
  private static boolean[] extractDataNeeded(ETRelationalExpr paramETRelationalExpr)
  {
    boolean[] arrayOfBoolean = new boolean[paramETRelationalExpr.getColumnCount()];
    for (int i = 0; i < paramETRelationalExpr.getColumnCount(); i++) {
      if (paramETRelationalExpr.getDataNeeded(i)) {
        arrayOfBoolean[i] = true;
      }
    }
    return arrayOfBoolean;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/NBFallBackJoinAlgorithm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */