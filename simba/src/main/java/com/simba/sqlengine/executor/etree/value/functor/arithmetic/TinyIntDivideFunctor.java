package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

public class TinyIntDivideFunctor
  extends AbstractTinyIntBinArithFunctor
{
  protected TinyIntDivideFunctor(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  protected short calculate(short paramShort1, short paramShort2)
    throws ErrorException
  {
    if (paramShort2 == 0) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    return (short)(paramShort1 / paramShort2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/TinyIntDivideFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */