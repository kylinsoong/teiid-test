package com.simba.sqlengine.executor.etree.bool.functor.comp;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.support.exceptions.ErrorException;

public class RealBoolGtFunctor
  implements IBooleanCompFunctor
{
  public ETBoolean evaluate(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull())) {
      return ETBoolean.SQL_BOOLEAN_UNKNOWN;
    }
    float f1 = paramISqlDataWrapper1.getReal();
    float f2 = paramISqlDataWrapper2.getReal();
    return f1 > f2 ? ETBoolean.SQL_BOOLEAN_TRUE : ETBoolean.SQL_BOOLEAN_FALSE;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/RealBoolGtFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */