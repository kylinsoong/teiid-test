package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public class BigIntSubtractFunctor
  extends AbstractBigIntBinArithFunctor
{
  protected BigInteger calculate(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws ErrorException
  {
    return paramBigInteger1.subtract(paramBigInteger2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/BigIntSubtractFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */