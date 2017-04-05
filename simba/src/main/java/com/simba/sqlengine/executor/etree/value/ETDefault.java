package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class ETDefault
  extends ETValueExpr
{
  private boolean m_isOpen;
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    this.m_isOpen = false;
  }
  
  public String getLogString()
  {
    return "ETDefault";
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isOpen()
  {
    return this.m_isOpen;
  }
  
  public void open()
  {
    this.m_isOpen = true;
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    paramETDataRequest.setIsDefault(true);
    return false;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETDefault.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */