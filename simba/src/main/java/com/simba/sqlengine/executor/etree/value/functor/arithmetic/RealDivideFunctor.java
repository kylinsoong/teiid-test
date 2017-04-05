package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class RealDivideFunctor
  extends AbstractRealBinArithFunctor
{
  protected float calculate(float paramFloat1, float paramFloat2)
    throws ErrorException
  {
    if (paramFloat2 == 0.0F) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramFloat1 / paramFloat2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/RealDivideFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */