package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;

public class SmallIntNegateFunctor
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
    int i = -paramISqlDataWrapper.getSmallInt();
    if ((i <= 32767) && (i >= 32768))
    {
      paramETDataRequest.getData().setSmallInt(i);
      return false;
    }
    throw SQLEngineExceptionFactory.numArithOverflowException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/SmallIntNegateFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */