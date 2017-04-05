package com.simba.sqlengine.executor.etree.bool.functor.comp;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public class BigIntBoolLtFunctor
  implements IBooleanCompFunctor
{
  public ETBoolean evaluate(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull())) {
      return ETBoolean.SQL_BOOLEAN_UNKNOWN;
    }
    BigInteger localBigInteger1 = paramISqlDataWrapper1.getBigInt();
    BigInteger localBigInteger2 = paramISqlDataWrapper2.getBigInt();
    return localBigInteger1.compareTo(localBigInteger2) < 0 ? ETBoolean.SQL_BOOLEAN_TRUE : ETBoolean.SQL_BOOLEAN_FALSE;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/BigIntBoolLtFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */