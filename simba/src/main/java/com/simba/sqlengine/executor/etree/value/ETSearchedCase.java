package com.simba.sqlengine.executor.etree.value;

import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class ETSearchedCase
  extends ETValueExpr
{
  private ETValueExprList m_whenExprs;
  private ETValueExpr m_elseResult;
  
  public ETSearchedCase(ETValueExprList paramETValueExprList, ETValueExpr paramETValueExpr)
  {
    this.m_whenExprs = paramETValueExprList;
    this.m_elseResult = paramETValueExpr;
  }
  
  public void close()
  {
    this.m_whenExprs.close();
    this.m_elseResult.close();
  }
  
  public boolean isOpen()
  {
    Iterator localIterator = this.m_whenExprs.iterator();
    while (localIterator.hasNext()) {
      if (!((ETValueExpr)localIterator.next()).isOpen()) {
        return false;
      }
    }
    return this.m_elseResult.isOpen();
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public void open()
  {
    Iterator localIterator = this.m_whenExprs.iterator();
    while (localIterator.hasNext()) {
      ((ETValueExpr)localIterator.next()).open();
    }
    this.m_elseResult.open();
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    Iterator localIterator = this.m_whenExprs.getChildItr();
    while (localIterator.hasNext())
    {
      ETSearchedWhenClause localETSearchedWhenClause = (ETSearchedWhenClause)localIterator.next();
      if (localETSearchedWhenClause.evaluateCondition()) {
        return localETSearchedWhenClause.retrieveData(paramETDataRequest);
      }
    }
    return this.m_elseResult.retrieveData(paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return this.m_whenExprs;
    }
    if (1 == paramInt) {
      return this.m_elseResult;
    }
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETSearchedCase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */