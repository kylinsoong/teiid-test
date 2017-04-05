package com.simba.sqlengine.executor.etree.temptable;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.support.exceptions.ErrorException;

public abstract interface ITemporaryTable
  extends IMemoryConsumer
{
  public abstract void open()
    throws ErrorException;
  
  public abstract void close();
  
  public abstract void reset()
    throws ErrorException;
  
  public abstract long getRowCount();
  
  public abstract boolean moveToNextRow()
    throws ErrorException;
  
  public abstract boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException;
  
  public abstract void writeFromRelation(ETRelationalExpr paramETRelationalExpr)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/ITemporaryTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */