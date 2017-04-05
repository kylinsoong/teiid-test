package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public class BigIntDivideFunctor
  extends AbstractBigIntBinArithFunctor
{
  protected BigInteger calculate(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws ErrorException
  {
    if (paramBigInteger2.equals(BigInteger.ZERO)) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramBigInteger1.divide(paramBigInteger2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/BigIntDivideFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */