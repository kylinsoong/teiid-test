package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.support.exceptions.ErrorException;

public class IntegerSubtractFunctor
  extends AbstractIntBinArithFunctor
{
  public IntegerSubtractFunctor(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  protected long calculate(long paramLong1, long paramLong2)
    throws ErrorException
  {
    return paramLong1 - paramLong2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/IntegerSubtractFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */