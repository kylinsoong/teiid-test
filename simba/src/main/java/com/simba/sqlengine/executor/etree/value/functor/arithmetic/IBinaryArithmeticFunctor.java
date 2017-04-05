package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

public abstract interface IBinaryArithmeticFunctor
{
  public abstract boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/IBinaryArithmeticFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */