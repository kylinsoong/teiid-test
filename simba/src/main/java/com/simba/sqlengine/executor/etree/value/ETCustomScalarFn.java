package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.dsiext.dataengine.CustomScalarFunction;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public class ETCustomScalarFn
  extends ETValueExpr
{
  private CustomScalarFunction m_customScalarFn;
  private ETValueExprList m_parameters;
  private List<FunctionParameterWrapper> m_dataWrappers;
  private List<IColumn> m_inputMetadata;
  
  public ETCustomScalarFn(CustomScalarFunction paramCustomScalarFunction, ETValueExprList paramETValueExprList, List<IColumn> paramList)
  {
    this.m_customScalarFn = paramCustomScalarFunction;
    this.m_parameters = paramETValueExprList;
    this.m_inputMetadata = paramList;
    int i = this.m_parameters.getNumChildren();
    this.m_dataWrappers = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      this.m_dataWrappers.add(new FunctionParameterWrapper((ETValueExpr)this.m_parameters.getChild(j), (IColumn)this.m_inputMetadata.get(j)));
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    this.m_parameters.close();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isOpen()
  {
    ETValueExprList localETValueExprList = this.m_parameters;
    for (int i = 0; i < localETValueExprList.getNumChildren(); i++) {
      if (!localETValueExprList.isOpen(i)) {
        return false;
      }
    }
    return true;
  }
  
  public void open()
  {
    this.m_parameters.open();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (0L == paramETDataRequest.getOffset()) {
      this.m_customScalarFn.execute(this.m_dataWrappers);
    }
    return this.m_customScalarFn.retrieveData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return this.m_parameters;
    }
    throw new IndexOutOfBoundsException("" + paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETCustomScalarFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */