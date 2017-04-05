package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.temptable.ITemporaryTable;
import com.simba.support.exceptions.ErrorException;

public class ETRelationalCache
  extends ETUnaryRelationalExpr
  implements IMemoryConsumer
{
  private boolean m_isCursorOpened;
  private final ITemporaryTable m_cache;
  
  public ETRelationalCache(ETRelationalExpr paramETRelationalExpr, ITemporaryTable paramITemporaryTable)
  {
    super(paramETRelationalExpr, extractDataNeeded(paramETRelationalExpr));
    this.m_cache = paramITemporaryTable;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return getOperand().getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return getOperand().getColumnCount();
  }
  
  public String getLogString()
  {
    return "ETRelationalCache";
  }
  
  public long getRowCount()
    throws ErrorException
  {
    if (isOpen()) {
      return this.m_cache.getRowCount();
    }
    return getOperand().getRowCount();
  }
  
  public boolean isOpen()
  {
    return this.m_isCursorOpened;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    super.open(paramCursorType);
    this.m_cache.open();
    this.m_cache.writeFromRelation(getOperand());
    getOperand().close();
    this.m_cache.reset();
    this.m_isCursorOpened = true;
  }
  
  public void close()
  {
    super.close();
    this.m_cache.close();
    this.m_isCursorOpened = false;
  }
  
  public void reset()
    throws ErrorException
  {
    assert (isOpen());
    this.m_cache.reset();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_cache.retrieveData(paramInt, paramETDataRequest);
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    assert (isOpen());
    return this.m_cache.moveToNextRow();
  }
  
  private static boolean[] extractDataNeeded(ETRelationalExpr paramETRelationalExpr)
  {
    boolean[] arrayOfBoolean = new boolean[paramETRelationalExpr.getColumnCount()];
    for (int i = 0; i < paramETRelationalExpr.getColumnCount(); i++) {
      if (paramETRelationalExpr.getDataNeeded(i)) {
        arrayOfBoolean[i] = true;
      }
    }
    return arrayOfBoolean;
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_cache.registerManagerAgent(paramIMemManagerAgent);
  }
  
  public long assign(long paramLong)
  {
    return this.m_cache.assign(paramLong);
  }
  
  public long getRequiredMemory()
  {
    return this.m_cache.getRequiredMemory();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETRelationalCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */