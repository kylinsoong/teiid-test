package com.simba.sqlengine.executor.etree.bool.functor.comp;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.support.exceptions.ErrorException;

public abstract interface IBooleanCompFunctor
{
  public abstract ETBoolean evaluate(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/IBooleanCompFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */