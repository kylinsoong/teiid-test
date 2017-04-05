package com.simba.dsi.dataengine.impl;

import com.simba.dsi.dataengine.interfaces.IErrorResult;
import com.simba.support.exceptions.ErrorException;

public class DSIErrorResult
  implements IErrorResult
{
  private ErrorException m_error = null;
  
  public DSIErrorResult(String paramString1, int paramInt, String paramString2)
  {
    this.m_error = new ErrorException(paramString2, paramString1, paramInt);
  }
  
  public DSIErrorResult(ErrorException paramErrorException)
  {
    this.m_error = paramErrorException;
  }
  
  public ErrorException getError()
  {
    return this.m_error;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIErrorResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */