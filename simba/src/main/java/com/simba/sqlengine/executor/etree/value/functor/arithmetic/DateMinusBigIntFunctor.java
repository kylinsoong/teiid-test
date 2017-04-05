package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;
import java.sql.Date;

public class DateMinusBigIntFunctor
  implements IBinaryArithmeticFunctor
{
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    Date localDate = null;
    BigInteger localBigInteger = null;
    if (paramISqlDataWrapper1.getType() == 91)
    {
      localDate = paramISqlDataWrapper1.getDate();
      localBigInteger = paramISqlDataWrapper2.getBigInt();
    }
    else
    {
      localDate = paramISqlDataWrapper2.getDate();
      localBigInteger = paramISqlDataWrapper1.getBigInt();
    }
    paramETDataRequest.getData().setDate(DateTimeFunctorUtil.datePlusBigInteger(localDate, localBigInteger.negate()));
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/DateMinusBigIntFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */