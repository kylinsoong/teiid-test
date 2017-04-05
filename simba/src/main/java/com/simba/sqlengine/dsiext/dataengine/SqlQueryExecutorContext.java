package com.simba.sqlengine.dsiext.dataengine;

import com.simba.support.IWarningListener;

public class SqlQueryExecutorContext
{
  private SqlDataEngineContext m_dataEngineContext;
  private volatile boolean m_isCanceled = false;
  private PassdownInformation m_pdInfo;
  private SqlQueryExecutor m_executor;
  private IWarningListener m_warningListener = null;
  
  public SqlQueryExecutorContext(SqlDataEngineContext paramSqlDataEngineContext, SqlQueryExecutor paramSqlQueryExecutor)
  {
    this.m_executor = paramSqlQueryExecutor;
    this.m_dataEngineContext = paramSqlDataEngineContext;
    this.m_pdInfo = new PassdownInformation();
  }
  
  public PassdownInformation getPassdownInformation()
  {
    return this.m_pdInfo;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public void setPassdownInformation(PassdownInformation paramPassdownInformation)
  {
    if (null == paramPassdownInformation) {
      throw new IllegalArgumentException("Cannot set PassdownInformation to null.");
    }
    this.m_pdInfo = paramPassdownInformation;
  }
  
  public void setIsCanceled(boolean paramBoolean)
  {
    this.m_isCanceled = paramBoolean;
  }
  
  public boolean isCanceled()
  {
    return this.m_isCanceled;
  }
  
  public SqlDataEngineContext getDataEngineContext()
  {
    return this.m_dataEngineContext;
  }
  
  public SqlQueryExecutor getExecutor()
  {
    return this.m_executor;
  }
  
  public void setWarningListener(IWarningListener paramIWarningListener)
  {
    assert (null != paramIWarningListener);
    this.m_warningListener = paramIWarningListener;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/SqlQueryExecutorContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */