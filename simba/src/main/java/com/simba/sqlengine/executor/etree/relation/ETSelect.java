package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.support.exceptions.ErrorException;

public class ETSelect
  extends ETRelationalExpr
{
  private ETRelationalExpr m_relationExpr;
  private ETBooleanExpr m_booleanExpr;
  
  public ETSelect(ETRelationalExpr paramETRelationalExpr, ETBooleanExpr paramETBooleanExpr, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    this.m_relationExpr = paramETRelationalExpr;
    this.m_booleanExpr = paramETBooleanExpr;
  }
  
  public void close()
  {
    this.m_relationExpr.close();
    this.m_booleanExpr.close();
  }
  
  public boolean isOpen()
  {
    return (this.m_relationExpr.isOpen()) && (this.m_booleanExpr.isOpen());
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_relationExpr.reset();
    this.m_booleanExpr.reset();
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
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_relationExpr.getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_relationExpr.getColumnCount();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return -1L;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_relationExpr.open(paramCursorType);
    this.m_booleanExpr.open();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_relationExpr.retrieveData(paramInt, paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    switch (paramInt)
    {
    case 0: 
      return this.m_relationExpr;
    case 1: 
      return this.m_booleanExpr;
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    do
    {
      if (!this.m_relationExpr.move()) {
        return false;
      }
    } while (this.m_booleanExpr.evaluate() != ETBoolean.SQL_BOOLEAN_TRUE);
    return true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETSelect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */