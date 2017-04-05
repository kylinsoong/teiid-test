package com.simba.sqlengine.executor;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.ETResourceManager;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.statement.ETQuery;
import com.simba.sqlengine.executor.etree.util.RegisterWarningListenerUtil;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.InvalidOperationException;
import java.util.ArrayList;

public class ETResultSet
  implements IResultSet
{
  private final ArrayList<IColumn> m_columns;
  private boolean m_isCursorSet;
  private int m_fetchSize = 0;
  private IWarningListener m_warningListener = null;
  private ETRelationalExpr m_etRelation;
  private ETResourceManager m_rscManager;
  private final ETCancelState m_cancelState;
  
  public ETResultSet(ETQuery paramETQuery, ETResourceManager paramETResourceManager, ETCancelState paramETCancelState)
  {
    this.m_etRelation = paramETQuery.getOperand();
    this.m_isCursorSet = false;
    this.m_rscManager = paramETResourceManager;
    this.m_columns = new ArrayList(this.m_etRelation.getColumnCount());
    this.m_cancelState = paramETCancelState;
    for (int i = 0; i < this.m_etRelation.getColumnCount(); i++) {
      this.m_columns.add(this.m_etRelation.getColumn(i));
    }
  }
  
  public void appendRow()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("Append row is not implemented");
  }
  
  public void close()
  {
    this.m_cancelState.cancel();
    synchronized (this)
    {
      try
      {
        this.m_etRelation.close();
        this.m_isCursorSet = false;
      }
      finally
      {
        this.m_rscManager.free();
      }
    }
  }
  
  public void closeCursor()
    throws ErrorException
  {
    close();
  }
  
  public void deleteRow()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("Delete row is not implemented");
  }
  
  public synchronized boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    if (!this.m_isCursorSet) {
      throw new InvalidOperationException(7, SQLEngineMessageKey.INVALID_OPERATION.name(), new String[] { "getData() called when the cursor is not set." });
    }
    if (this.m_cancelState.isCanceled()) {
      throw SQLEngineExceptionFactory.operationCanceledException();
    }
    ETDataRequest localETDataRequest = new ETDataRequest(paramLong1, paramLong2, (IColumn)this.m_columns.get(paramInt));
    boolean bool = this.m_etRelation.retrieveData(paramInt, localETDataRequest);
    localETDataRequest.getData().retrieveData(paramDataWrapper);
    return bool;
  }
  
  public int getFetchSize()
    throws ErrorException
  {
    return this.m_fetchSize;
  }
  
  public synchronized long getRowCount()
    throws ErrorException
  {
    return this.m_etRelation.getRowCount();
  }
  
  public ArrayList<? extends IColumn> getSelectColumns()
    throws ErrorException
  {
    return this.m_columns;
  }
  
  public IWarningListener getWarningListner()
  {
    return this.m_warningListener;
  }
  
  public final boolean hasMoreRows()
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("IResult.hasMoreRows()");
  }
  
  public boolean hasRowCount()
  {
    try
    {
      return -1L != getRowCount();
    }
    catch (ErrorException localErrorException) {}
    return false;
  }
  
  public synchronized boolean moveToNextRow()
    throws ErrorException
  {
    if (!this.m_isCursorSet) {
      throw new InvalidOperationException(7, SQLEngineMessageKey.INVALID_OPERATION.name(), new String[] { "moveToNextRow() called when the cursor is not set." });
    }
    if (this.m_cancelState.isCanceled()) {
      throw SQLEngineExceptionFactory.operationCanceledException();
    }
    return this.m_etRelation.move();
  }
  
  public void onFinishRowUpdate() {}
  
  public void onStartRowUpdate() {}
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    RegisterWarningListenerUtil.registerWarningListener(paramIWarningListener, this.m_etRelation);
    this.m_warningListener = paramIWarningListener;
  }
  
  public boolean rowDeleted()
  {
    return false;
  }
  
  public boolean rowInserted()
  {
    return false;
  }
  
  public boolean rowUpdated()
  {
    return false;
  }
  
  public synchronized void setCursorType(CursorType paramCursorType)
    throws ErrorException
  {
    if (CursorType.FORWARD_ONLY != paramCursorType) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.CURSOR_NOT_SUPPORTED.name(), String.valueOf(paramCursorType));
    }
    try
    {
      this.m_rscManager.allocate();
      this.m_etRelation.open(paramCursorType);
      this.m_isCursorSet = true;
    }
    catch (ErrorException localErrorException)
    {
      this.m_rscManager.free();
      throw localErrorException;
    }
    catch (RuntimeException localRuntimeException)
    {
      this.m_rscManager.free();
      throw localRuntimeException;
    }
    catch (Error localError)
    {
      this.m_rscManager.free();
      throw localError;
    }
  }
  
  public void setFetchSize(int paramInt)
    throws ErrorException
  {
    this.m_fetchSize = paramInt;
  }
  
  public boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("Updateable resultset");
  }
  
  public final boolean supportsHasMoreRows()
  {
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/ETResultSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */