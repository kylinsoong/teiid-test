package com.simba.sqlengine.executor.etree.bool.functor.comp;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;

public class DecimalBoolNeFunctor
  implements IBooleanCompFunctor
{
  public ETBoolean evaluate(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull())) {
      return ETBoolean.SQL_BOOLEAN_UNKNOWN;
    }
    BigDecimal localBigDecimal1 = paramISqlDataWrapper1.getExactNumber();
    BigDecimal localBigDecimal2 = paramISqlDataWrapper2.getExactNumber();
    return localBigDecimal1.compareTo(localBigDecimal2) != 0 ? ETBoolean.SQL_BOOLEAN_TRUE : ETBoolean.SQL_BOOLEAN_FALSE;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/DecimalBoolNeFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */