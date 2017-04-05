package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn.AggrFnId;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn.AggrFnQuantifier;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.value.aggregatefn.AvgAggregatorFactory;
import com.simba.sqlengine.executor.etree.value.aggregatefn.CountAggregatorFactory;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregatorFactory;
import com.simba.sqlengine.executor.etree.value.aggregatefn.MinMaxAggregatorFactory;
import com.simba.sqlengine.executor.etree.value.aggregatefn.SumAggregatorFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class ETAggregateFnFactory
{
  public static IAggregatorFactory makeNewAggregatorFactory(AEAggrFn paramAEAggrFn, int paramInt)
    throws ErrorException
  {
    boolean bool = AEAggrFn.AggrFnQuantifier.DISTINCT == paramAEAggrFn.getSetQuantifier();
    if (bool) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("DISTINCT");
    }
    int i = paramAEAggrFn.getNumChildren();
    Iterator localIterator = paramAEAggrFn.getChildItr();
    IColumn[] arrayOfIColumn = new IColumn[i];
    for (int j = 0; j < i; j++) {
      arrayOfIColumn[j] = ((AEValueExpr)localIterator.next()).getColumn();
    }
    switch (paramAEAggrFn.getAggrFnId())
    {
    case AVG: 
      return new AvgAggregatorFactory(arrayOfIColumn, paramAEAggrFn.getColumn(), bool);
    case COUNT: 
    case COUNT_STAR: 
      return new CountAggregatorFactory(arrayOfIColumn, paramAEAggrFn.getColumn(), bool);
    case SUM: 
      return new SumAggregatorFactory(arrayOfIColumn, paramAEAggrFn.getColumn(), bool);
    case MIN: 
    case MAX: 
      assert (arrayOfIColumn.length == 1);
      if (ColumnSizeCalculator.isLongData(arrayOfIColumn[0], paramInt)) {
        throw SQLEngineExceptionFactory.aggregateOnLongData();
      }
      return new MinMaxAggregatorFactory(arrayOfIColumn, paramAEAggrFn.getColumn(), paramAEAggrFn.getAggrFnId() == AEAggrFn.AggrFnId.MAX, paramInt);
    }
    throw SQLEngineExceptionFactory.featureNotImplementedException("" + paramAEAggrFn.getAggrFnId());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETAggregateFnFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */