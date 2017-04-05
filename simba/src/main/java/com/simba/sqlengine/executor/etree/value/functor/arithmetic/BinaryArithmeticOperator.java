package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.dsi.dataengine.interfaces.IColumn;

public final class BinaryArithmeticOperator
{
  private final IBinaryArithmeticFunctor m_functor;
  private final IColumn m_leftMetadata;
  private final IColumn m_rightMetadata;
  
  public BinaryArithmeticOperator(IBinaryArithmeticFunctor paramIBinaryArithmeticFunctor, IColumn paramIColumn1, IColumn paramIColumn2)
  {
    this.m_functor = paramIBinaryArithmeticFunctor;
    this.m_leftMetadata = paramIColumn1;
    this.m_rightMetadata = paramIColumn2;
  }
  
  public IBinaryArithmeticFunctor getFunctor()
  {
    return this.m_functor;
  }
  
  public IColumn getLeftMetadata()
  {
    return this.m_leftMetadata;
  }
  
  public IColumn getRightMetadata()
  {
    return this.m_rightMetadata;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/BinaryArithmeticOperator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */