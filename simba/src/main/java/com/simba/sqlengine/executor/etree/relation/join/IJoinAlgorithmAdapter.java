package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

public abstract interface IJoinAlgorithmAdapter
  extends IMemoryConsumer, IWarningSource
{
  public abstract Pair<? extends IJoinUnit, ? extends IJoinUnit> loadNextJoinUnit()
    throws ErrorException;
  
  public abstract boolean isMasterJoinUnitOnLeft();
  
  public abstract void match()
    throws ErrorException;
  
  public abstract boolean isOuterRow();
  
  public abstract boolean moveMaster()
    throws ErrorException;
  
  public abstract boolean moveSlave()
    throws ErrorException;
  
  public abstract void seekSlave();
  
  public abstract void closeRelations();
  
  public abstract void open(CursorType paramCursorType)
    throws ErrorException;
  
  public abstract void reset()
    throws ErrorException;
  
  public abstract void registerWarningListener(IWarningListener paramIWarningListener);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/IJoinAlgorithmAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */