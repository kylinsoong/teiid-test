package com.simba.sqlengine.executor.etree.bool.functor.comp;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.support.exceptions.ErrorException;
import java.util.UUID;

public class GuidBoolNeFunctor
  implements IBooleanCompFunctor
{
  public ETBoolean evaluate(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull())) {
      return ETBoolean.SQL_BOOLEAN_UNKNOWN;
    }
    UUID localUUID1 = paramISqlDataWrapper1.getGuid();
    UUID localUUID2 = paramISqlDataWrapper2.getGuid();
    return ETBoolean.fromBoolean((localUUID1.getLeastSignificantBits() != localUUID2.getLeastSignificantBits()) || (localUUID1.getMostSignificantBits() != localUUID2.getMostSignificantBits()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/functor/comp/GuidBoolNeFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */