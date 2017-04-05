package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.support.exceptions.ErrorException;

public class SmallIntSubtractFunctor
  extends AbstractSmallIntBinArithFunctor
{
  public SmallIntSubtractFunctor(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  protected int calculate(int paramInt1, int paramInt2)
    throws ErrorException
  {
    return paramInt1 - paramInt2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/SmallIntSubtractFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */