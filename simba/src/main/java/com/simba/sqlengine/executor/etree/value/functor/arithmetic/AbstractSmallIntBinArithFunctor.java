package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

public abstract class AbstractSmallIntBinArithFunctor
  implements IBinaryArithmeticFunctor
{
  protected boolean m_isSigned;
  
  protected AbstractSmallIntBinArithFunctor(boolean paramBoolean)
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
    int i = paramISqlDataWrapper1.getSmallInt();
    assert (((this.m_isSigned) && (i <= 32767) && (i >= 32768)) || ((!this.m_isSigned) && (i >= 0) && (i <= 65535)));
    int j = paramISqlDataWrapper2.getSmallInt();
    assert (((this.m_isSigned) && (j <= 32767) && (j >= 32768)) || ((!this.m_isSigned) && (j >= 0) && (j <= 65535)));
    int k = calculate(i, j);
    if (((this.m_isSigned) && (k <= 32767) && (k >= 32768)) || ((!this.m_isSigned) && (k >= 0) && (k <= 65535)))
    {
      paramETDataRequest.getData().setSmallInt(k);
      return false;
    }
    throw SQLEngineExceptionFactory.numArithOverflowException();
  }
  
  protected abstract int calculate(int paramInt1, int paramInt2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/AbstractSmallIntBinArithFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */