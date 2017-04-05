package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.conv.ConversionResult;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;

public abstract class AbstractDecimalBinArithFunctor
  implements IBinaryArithmeticFunctor
{
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    BigDecimal localBigDecimal1 = paramISqlDataWrapper1.getExactNumber();
    BigDecimal localBigDecimal2 = paramISqlDataWrapper2.getExactNumber();
    if ((localBigDecimal1 == null) || (localBigDecimal2 == null))
    {
      paramETDataRequest.getData().setExactNumber(null);
    }
    else
    {
      ConversionResult localConversionResult = new ConversionResult();
      TypeMetadata localTypeMetadata = paramETDataRequest.getMetadata();
      BigDecimal localBigDecimal3 = calculate(localBigDecimal1, localBigDecimal2, localTypeMetadata.getPrecision(), localTypeMetadata.getScale(), localConversionResult);
      switch (localConversionResult.getState())
      {
      case NUMERIC_OUT_OF_RANGE_TOO_LARGE: 
      case NUMERIC_OUT_OF_RANGE_TOO_SMALL: 
        throw SQLEngineExceptionFactory.numArithOverflowException();
      case SUCCESS: 
        break;
      case FRAC_TRUNCATION_ROUNDED_DOWN: 
      case FRAC_TRUNCATION_ROUNDED_UP: 
        break;
      default: 
        throw new IllegalStateException("Unexpected conversion result: " + localConversionResult.getState());
      }
      paramETDataRequest.getData().setExactNumber(localBigDecimal3);
    }
    return false;
  }
  
  protected abstract BigDecimal calculate(BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2, short paramShort1, short paramShort2, ConversionResult paramConversionResult)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/AbstractDecimalBinArithFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */