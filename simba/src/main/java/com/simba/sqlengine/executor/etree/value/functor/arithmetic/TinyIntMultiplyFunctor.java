package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.support.exceptions.ErrorException;

public class TinyIntMultiplyFunctor
  extends AbstractTinyIntBinArithFunctor
{
  protected TinyIntMultiplyFunctor(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  protected short calculate(short paramShort1, short paramShort2)
    throws ErrorException
  {
    return (short)(paramShort1 * paramShort2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/TinyIntMultiplyFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */