package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

public abstract class AbstractRealBinArithFunctor
  implements IBinaryArithmeticFunctor
{
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull())) {
      paramETDataRequest.getData().setNull();
    } else {
      paramETDataRequest.getData().setReal(calculate(paramISqlDataWrapper1.getReal(), paramISqlDataWrapper2.getReal()));
    }
    return false;
  }
  
  protected abstract float calculate(float paramFloat1, float paramFloat2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/AbstractRealBinArithFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */