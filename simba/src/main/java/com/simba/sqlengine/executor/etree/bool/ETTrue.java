package com.simba.sqlengine.executor.etree.bool;

import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class ETTrue
  extends ETBooleanExpr
{
  public void close() {}
  
  public boolean isOpen()
  {
    return true;
  }
  
  public void reset()
    throws ErrorException
  {}
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public ETBoolean evaluate()
  {
    return ETBoolean.SQL_BOOLEAN_TRUE;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
  
  public void open() {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETTrue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */