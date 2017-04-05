package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;

public abstract interface IUnaryArithmeticFunctor
{
  public abstract boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/IUnaryArithmeticFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */