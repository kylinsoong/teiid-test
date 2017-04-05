package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

public abstract class AbstractTinyIntBinArithFunctor
  implements IBinaryArithmeticFunctor
{
  protected boolean m_isSigned;
  
  protected AbstractTinyIntBinArithFunctor(boolean paramBoolean)
  {
    this.m_isSigned = paramBoolean;
  }
  
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    short s1 = paramISqlDataWrapper1.getTinyInt();
    assert (((this.m_isSigned) && (s1 <= 127) && (s1 >= -128)) || ((!this.m_isSigned) && (s1 >= 0) && (s1 <= 255)));
    short s2 = paramISqlDataWrapper2.getTinyInt();
    assert (((this.m_isSigned) && (s2 <= 127) && (s2 >= -128)) || ((!this.m_isSigned) && (s2 >= 0) && (s2 <= 255)));
    short s3 = calculate(s1, s2);
    if (((this.m_isSigned) && (s3 <= 127) && (s3 >= -128)) || ((!this.m_isSigned) && (s3 >= 0) && (s3 <= 255)))
    {
      paramETDataRequest.getData().setTinyInt(s3);
      return false;
    }
    throw SQLEngineExceptionFactory.numArithOverflowException();
  }
  
  protected abstract short calculate(short paramShort1, short paramShort2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/AbstractTinyIntBinArithFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */