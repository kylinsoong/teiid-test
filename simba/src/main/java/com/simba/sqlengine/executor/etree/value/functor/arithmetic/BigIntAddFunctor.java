package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import java.math.BigInteger;

public class BigIntAddFunctor
  extends AbstractBigIntBinArithFunctor
{
  protected BigInteger calculate(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    return paramBigInteger1.add(paramBigInteger2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/BigIntAddFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */