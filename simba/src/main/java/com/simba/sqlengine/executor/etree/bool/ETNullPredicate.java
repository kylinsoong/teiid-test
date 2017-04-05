package com.simba.sqlengine.executor.etree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;

public class ETNullPredicate
  extends ETBooleanExpr
{
  private ETValueExpr m_operand;
  private ETDataRequest m_data;
  
  public ETNullPredicate(ETValueExpr paramETValueExpr, IColumn paramIColumn)
    throws ErrorException
  {
    if (paramETValueExpr == null) {
      throw new NullPointerException("ETArithmeticValueExpr does not take null input.");
    }
    this.m_data = new ETDataRequest(paramIColumn);
    this.m_operand = paramETValueExpr;
  }
  
  public void close()
  {
    this.m_operand.close();
  }
  
  public boolean isOpen()
  {
    return this.m_operand.isOpen();
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_operand.reset();
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public ETBoolean evaluate()
    throws ErrorException
  {
    this.m_operand.retrieveData(this.m_data);
    return this.m_data.getData().isNull() ? ETBoolean.SQL_BOOLEAN_TRUE : ETBoolean.SQL_BOOLEAN_FALSE;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (paramInt == 0) {
      return this.m_operand;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  public void open()
  {
    this.m_operand.open();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETNullPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */