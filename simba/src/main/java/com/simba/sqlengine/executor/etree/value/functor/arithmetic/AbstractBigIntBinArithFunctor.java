package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public abstract class AbstractBigIntBinArithFunctor
  implements IBinaryArithmeticFunctor
{
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    BigInteger localBigInteger1 = paramISqlDataWrapper1.getBigInt();
    BigInteger localBigInteger2 = paramISqlDataWrapper2.getBigInt();
    if ((localBigInteger1 == null) || (localBigInteger2 == null)) {
      paramETDataRequest.getData().setBigInt(null);
    } else {
      paramETDataRequest.getData().setBigInt(calculate(localBigInteger1, localBigInteger2));
    }
    return false;
  }
  
  protected abstract BigInteger calculate(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/AbstractBigIntBinArithFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */