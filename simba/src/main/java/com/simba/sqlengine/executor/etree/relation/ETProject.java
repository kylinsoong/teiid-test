package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IETUnaryNode;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETProject
  extends ETRelationalExpr
  implements IETUnaryNode<ETRelationalExpr>
{
  private ETRelationalExpr m_operand;
  private ETValueExprList m_projectList;
  private List<IColumn> m_projectMetadata;
  
  public ETProject(ETRelationalExpr paramETRelationalExpr, ETValueExprList paramETValueExprList, List<IColumn> paramList, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    this.m_operand = paramETRelationalExpr;
    this.m_projectList = paramETValueExprList;
    this.m_projectMetadata = paramList;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    this.m_operand.close();
    this.m_projectList.close();
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_projectMetadata.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_projectList.getNumChildren();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_operand.getRowCount();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public ETRelationalExpr getOperand()
  {
    return this.m_operand;
  }
  
  public ETValueExprList getProjectionList()
  {
    return this.m_projectList;
  }
  
  public boolean isOpen()
  {
    return this.m_operand.isOpen();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_operand.open(paramCursorType);
    this.m_projectList.open();
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_operand.reset();
    this.m_projectList.reset();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_projectList.retrieveData(paramInt, paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
  {
    if (0 == paramInt) {
      return getOperand();
    }
    if (1 == paramInt) {
      return getProjectionList();
    }
    throw new IndexOutOfBoundsException();
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    return this.m_operand.move();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETProject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */