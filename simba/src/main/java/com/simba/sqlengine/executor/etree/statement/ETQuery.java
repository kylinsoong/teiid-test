package com.simba.sqlengine.executor.etree.statement;

import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IETUnaryNode;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.Map;

public class ETQuery
  extends AbstractETStatement
  implements IETUnaryNode<ETRelationalExpr>
{
  private ETRelationalExpr m_relationalExpr;
  
  public ETQuery(ETRelationalExpr paramETRelationalExpr, Map<Integer, ETParameter> paramMap)
  {
    super(paramMap);
    this.m_relationalExpr = paramETRelationalExpr;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public Iterator<? extends IETNode> getChildItr()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return ETQuery.this.getOperand();
        }
        throw new IndexOutOfBoundsException("Illegal index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return ETQuery.this.getNumChildren();
      }
    }.iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public ETRelationalExpr getOperand()
  {
    return this.m_relationalExpr;
  }
  
  public boolean isResultSet()
  {
    return true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/ETQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */