package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class SmallIntDivideFunctor
  extends AbstractSmallIntBinArithFunctor
{
  public SmallIntDivideFunctor(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  protected int calculate(int paramInt1, int paramInt2)
    throws ErrorException
  {
    if (paramInt2 == 0) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return paramInt1 / paramInt2;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/SmallIntDivideFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */