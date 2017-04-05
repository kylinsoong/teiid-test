package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;

public class IntegerNegateFunctor
  implements IUnaryArithmeticFunctor
{
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper)
    throws ErrorException
  {
    if (paramISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    long l = -paramISqlDataWrapper.getInteger();
    if ((l <= 2147483647L) && (l >= -2147483648L))
    {
      paramETDataRequest.getData().setInteger(l);
      return false;
    }
    throw SQLEngineExceptionFactory.numArithOverflowException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/IntegerNegateFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */