package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.support.exceptions.ErrorException;

public abstract interface IMasterJoinUnit
  extends IJoinUnit
{
  public abstract boolean moveToNextRow()
    throws ErrorException;
  
  public abstract void match()
    throws ErrorException;
  
  public abstract IRowView getRow();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/IMasterJoinUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */