package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.support.exceptions.ErrorException;

public class RealMultiplyFunctor
  extends AbstractRealBinArithFunctor
{
  protected float calculate(float paramFloat1, float paramFloat2)
    throws ErrorException
  {
    return paramFloat1 * paramFloat2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/RealMultiplyFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */