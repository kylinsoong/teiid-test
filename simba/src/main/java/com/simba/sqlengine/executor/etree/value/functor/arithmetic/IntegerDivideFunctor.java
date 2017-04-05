package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class IntegerDivideFunctor
  extends AbstractIntBinArithFunctor
{
  protected IntegerDivideFunctor(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  protected long calculate(long paramLong1, long paramLong2)
    throws ErrorException
  {
    if (paramLong2 == 0L) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramLong1 / paramLong2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/IntegerDivideFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */