package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.support.exceptions.ErrorException;

public abstract interface IAEOptimizer
{
  public abstract void optimize(IAEStatement paramIAEStatement)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/IAEOptimizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */