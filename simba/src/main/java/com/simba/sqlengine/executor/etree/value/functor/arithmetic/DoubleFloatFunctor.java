package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.support.exceptions.ErrorException;

public class DoubleFloatFunctor
  extends AbstractDoubleBinArithFunctor
{
  protected double calculate(double paramDouble1, double paramDouble2)
    throws ErrorException
  {
    return paramDouble1 + paramDouble2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/DoubleFloatFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */