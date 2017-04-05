package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.exceptions.ErrorException;
import java.sql.Timestamp;

public class TimestampNegateFunctor
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
    Timestamp localTimestamp = paramISqlDataWrapper.getTimestamp();
    paramETDataRequest.getData().setTimestamp(DateTimeFunctorUtil.negateTimestamp(localTimestamp));
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/TimestampNegateFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */