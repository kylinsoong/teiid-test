package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.support.exceptions.ErrorException;

public abstract interface ISlaveJoinUnit
  extends IJoinUnit
{
  public abstract void seek(IRowView paramIRowView);
  
  public abstract boolean moveToNextRow()
    throws ErrorException;
  
  public abstract boolean moveOuter();
  
  public abstract void setOutputOuter();
  
  public abstract boolean hasOuterRows();
  
  public abstract void match();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/ISlaveJoinUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */