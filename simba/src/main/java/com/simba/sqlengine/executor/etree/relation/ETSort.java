package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.temptable.SortedTemporaryTable;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public final class ETSort
  extends ETUnaryRelationalExpr
  implements IMemoryConsumer
{
  private final List<? extends IColumn> m_columns;
  private final SortedTemporaryTable m_tempTable;
  
  public ETSort(ETRelationalExpr paramETRelationalExpr, List<? extends IColumn> paramList, SortedTemporaryTable paramSortedTemporaryTable, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    super(paramETRelationalExpr, paramArrayOfBoolean);
    this.m_columns = paramList;
    this.m_tempTable = paramSortedTemporaryTable;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_columns.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_columns.size();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    try
    {
      return getOperand().getRowCount();
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    try
    {
      this.m_tempTable.open();
      this.m_tempTable.writeFromRelation(getOperand());
      getOperand().close();
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
    catch (RuntimeException localRuntimeException)
    {
      close();
      throw SQLEngineExceptionFactory.convertRuntimeException(localRuntimeException);
    }
  }
  
  public void close()
  {
    this.m_tempTable.close();
  }
  
  public void reset()
  {
    if (!isOpen())
    {
      close();
      throw new IllegalStateException("Reset called before relation is opened.");
    }
    this.m_tempTable.reset();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    try
    {
      return this.m_tempTable.retrieveData(paramInt, paramETDataRequest);
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
    catch (RuntimeException localRuntimeException)
    {
      close();
      throw SQLEngineExceptionFactory.convertRuntimeException(localRuntimeException);
    }
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    try
    {
      return this.m_tempTable.moveToNextRow();
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
    catch (RuntimeException localRuntimeException)
    {
      close();
      throw SQLEngineExceptionFactory.convertRuntimeException(localRuntimeException);
    }
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_tempTable.registerManagerAgent(paramIMemManagerAgent);
  }
  
  public long assign(long paramLong)
  {
    return this.m_tempTable.assign(paramLong);
  }
  
  public long getRequiredMemory()
  {
    return this.m_tempTable.getRequiredMemory();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETSort.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */