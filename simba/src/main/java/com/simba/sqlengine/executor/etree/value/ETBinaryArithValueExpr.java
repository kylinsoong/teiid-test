package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETBinaryNode;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.functor.arithmetic.IBinaryArithmeticFunctor;
import com.simba.support.exceptions.ErrorException;

public class ETBinaryArithValueExpr
  extends ETValueExpr
  implements IETBinaryNode<ETValueExpr, ETValueExpr>
{
  private ETValueExpr m_leftOperand;
  private ETValueExpr m_rightOperand;
  private ETDataRequest m_leftData;
  private ETDataRequest m_rightData;
  private IBinaryArithmeticFunctor m_functor;
  
  public ETBinaryArithValueExpr(IColumn paramIColumn1, ETValueExpr paramETValueExpr1, IColumn paramIColumn2, ETValueExpr paramETValueExpr2, IBinaryArithmeticFunctor paramIBinaryArithmeticFunctor)
    throws ErrorException
  {
    if ((paramETValueExpr1 == null) || (paramIColumn1 == null) || (paramIColumn2 == null) || (paramETValueExpr2 == null) || (paramIBinaryArithmeticFunctor == null)) {
      throw new NullPointerException("ETBinaryArithValueExpr does not take null input.");
    }
    this.m_leftOperand = paramETValueExpr1;
    this.m_rightOperand = paramETValueExpr2;
    this.m_leftData = new ETDataRequest(paramIColumn1);
    this.m_rightData = new ETDataRequest(paramIColumn2);
    this.m_functor = paramIBinaryArithmeticFunctor;
  }
  
  public void close()
  {
    this.m_leftOperand.close();
    this.m_rightOperand.close();
  }
  
  public boolean isOpen()
  {
    return (this.m_leftOperand.isOpen()) && (this.m_rightOperand.isOpen());
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public void open()
  {
    this.m_leftOperand.open();
    this.m_rightOperand.open();
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (paramInt == 0) {
      return this.m_leftOperand;
    }
    if (paramInt == 1) {
      return this.m_rightOperand;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  public ETValueExpr getLeftOperand()
  {
    return this.m_leftOperand;
  }
  
  public ETValueExpr getRightOperand()
  {
    return this.m_rightOperand;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    this.m_leftOperand.retrieveData(this.m_leftData);
    this.m_rightOperand.retrieveData(this.m_rightData);
    return this.m_functor.execute(paramETDataRequest, this.m_leftData.getData(), this.m_rightData.getData(), getWarningListener());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETBinaryArithValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */