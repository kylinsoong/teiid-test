package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

public abstract class AbstractIntBinArithFunctor
  implements IBinaryArithmeticFunctor
{
  protected boolean m_isSigned;
  
  protected AbstractIntBinArithFunctor(boolean paramBoolean)
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
    long l1 = paramISqlDataWrapper1.getInteger();
    long l2 = paramISqlDataWrapper2.getInteger();
    assert (((this.m_isSigned) && (l1 <= 2147483647L) && (l1 >= -2147483648L)) || ((!this.m_isSigned) && (l1 >= 0L) && (l1 <= 4294967295L)));
    assert (((this.m_isSigned) && (l2 <= 2147483647L) && (l2 >= -2147483648L)) || ((!this.m_isSigned) && (l2 >= 0L) && (l2 <= 4294967295L)));
    long l3 = calculate(l1, l2);
    if (((this.m_isSigned) && (l3 <= 2147483647L) && (l3 >= -2147483648L)) || ((!this.m_isSigned) && (l3 >= 0L) && (l3 <= 4294967295L)))
    {
      paramETDataRequest.getData().setInteger(l3);
      return false;
    }
    throw SQLEngineExceptionFactory.numArithOverflowException();
  }
  
  protected abstract long calculate(long paramLong1, long paramLong2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/AbstractIntBinArithFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */