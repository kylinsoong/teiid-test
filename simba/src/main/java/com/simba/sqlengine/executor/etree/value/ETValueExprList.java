package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.AbstractETNodeList;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class ETValueExprList
  extends AbstractETNodeList<ETValueExpr>
  implements IETNode
{
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    Iterator localIterator = iterator();
    while (localIterator.hasNext())
    {
      ETValueExpr localETValueExpr = (ETValueExpr)localIterator.next();
      localETValueExpr.close();
    }
  }
  
  public boolean isOpen(int paramInt)
  {
    return ((ETValueExpr)getChild(paramInt)).isOpen();
  }
  
  public void open()
  {
    Iterator localIterator = iterator();
    while (localIterator.hasNext())
    {
      ETValueExpr localETValueExpr = (ETValueExpr)localIterator.next();
      localETValueExpr.open();
    }
  }
  
  public void reset()
  {
    Iterator localIterator = iterator();
    while (localIterator.hasNext())
    {
      ETValueExpr localETValueExpr = (ETValueExpr)localIterator.next();
      localETValueExpr.reset();
    }
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return ((ETValueExpr)getChild(paramInt)).retrieveData(paramETDataRequest);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETValueExprList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */