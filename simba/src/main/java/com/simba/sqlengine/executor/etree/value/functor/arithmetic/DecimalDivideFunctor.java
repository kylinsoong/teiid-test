package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ExactNumConverter;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalDivideFunctor
  extends AbstractDecimalBinArithFunctor
{
  protected BigDecimal calculate(BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
    throws ErrorException
  {
    if (paramBigDecimal2.equals(BigDecimal.ZERO)) {
      throw SQLEngineExceptionFactory.divByZeroException();
    }
    BigDecimal localBigDecimal = paramBigDecimal1.divide(paramBigDecimal2, paramShort1 * 2, RoundingMode.DOWN);
    return ExactNumConverter.setPrecScale(localBigDecimal, paramShort1, paramShort2, paramConversionResult);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/DecimalDivideFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */