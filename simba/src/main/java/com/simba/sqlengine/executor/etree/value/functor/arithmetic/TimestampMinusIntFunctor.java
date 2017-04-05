package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.DateTimeConverter;
import com.simba.support.exceptions.ErrorException;
import java.sql.Timestamp;

public class TimestampMinusIntFunctor
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
    Timestamp localTimestamp = null;
    long l = 0L;
    if (paramISqlDataWrapper1.getType() == 93)
    {
      localTimestamp = paramISqlDataWrapper1.getTimestamp();
      l = paramISqlDataWrapper2.getInteger();
    }
    else
    {
      localTimestamp = paramISqlDataWrapper2.getTimestamp();
      l = paramISqlDataWrapper1.getInteger();
    }
    localTimestamp = DateTimeFunctorUtil.timestampPlusNum(localTimestamp, -l);
    ConversionResult localConversionResult = new ConversionResult();
    localTimestamp = DateTimeConverter.toTimestamp(localTimestamp, localConversionResult, paramETDataRequest.getColumn().getTypeMetadata().getPrecision());
    ConversionUtil.checkResult(localConversionResult, paramIWarningListener, -1, -1);
    paramETDataRequest.getData().setTimestamp(localTimestamp);
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/TimestampMinusIntFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */