package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.support.exceptions.ErrorException;

public class ETColumnRef
  extends ETValueExpr
{
  private ETRelationalExpr m_rel;
  private int m_colNum;
  private final boolean m_isOuterRef;
  
  public ETColumnRef(ETRelationalExpr paramETRelationalExpr, int paramInt, boolean paramBoolean)
  {
    this.m_rel = paramETRelationalExpr;
    this.m_colNum = paramInt;
    this.m_isOuterRef = paramBoolean;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close() {}
  
  public int getColumnNumber()
  {
    return this.m_colNum;
  }
  
  public String getLogString()
  {
    StringBuilder localStringBuilder = new StringBuilder(100);
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append(": ");
    IColumn localIColumn = this.m_rel.getColumn(this.m_colNum);
    AEQColumnName localAEQColumnName = new AEQColumnName(new AEQTableName(localIColumn.getCatalogName(), localIColumn.getSchemaName(), localIColumn.getTableName()), localIColumn.getName());
    localStringBuilder.append(localAEQColumnName.toString());
    return localStringBuilder.toString();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public ETRelationalExpr getRelationalExpression()
  {
    return this.m_rel;
  }
  
  public boolean isOpen()
  {
    return true;
  }
  
  public boolean isOuterReference()
  {
    return this.m_isOuterRef;
  }
  
  public void open() {}
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_rel.retrieveData(this.m_colNum, paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETColumnRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */