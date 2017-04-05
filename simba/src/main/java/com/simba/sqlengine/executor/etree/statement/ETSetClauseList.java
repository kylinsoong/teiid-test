package com.simba.sqlengine.executor.etree.statement;

import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ETSetClauseList
  implements IETNode
{
  private List<ETSetClause> m_setClauses = new ArrayList();
  
  public void add(ETSetClause paramETSetClause)
  {
    this.m_setClauses.add(paramETSetClause);
  }
  
  public ETSetClause getChild(int paramInt)
  {
    return (ETSetClause)this.m_setClauses.get(paramInt);
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public Iterator<ETSetClause> getChildItr()
  {
    return this.m_setClauses.iterator();
  }
  
  public String getLogString()
  {
    return "ETSetClauseList";
  }
  
  public int getNumChildren()
  {
    return this.m_setClauses.size();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/ETSetClauseList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */