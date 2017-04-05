package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class ETError
  extends ETConstant
{
  private ErrorException m_error;
  
  public ETError(ErrorException paramErrorException)
  {
    this.m_error = paramErrorException;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    throw this.m_error;
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    throw this.m_error;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */