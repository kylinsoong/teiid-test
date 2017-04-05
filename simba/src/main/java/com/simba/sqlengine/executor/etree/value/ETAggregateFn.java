package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn.AggrFnId;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregator;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregator.IUpdateParameters;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETAggregateFn
  extends ETValueExpr
{
  private AEAggrFn.AggrFnId m_functionId;
  private IAggregator m_aggregator;
  private final ETValueUpdateParameters m_updateParameters;
  private ETValueExprList m_operands;
  private boolean m_isOpen = false;
  
  public ETAggregateFn(AEAggrFn.AggrFnId paramAggrFnId, ETValueExprList paramETValueExprList, List<? extends IColumn> paramList, IAggregator paramIAggregator)
  {
    this.m_functionId = paramAggrFnId;
    this.m_aggregator = paramIAggregator;
    this.m_updateParameters = new ETValueUpdateParameters(paramETValueExprList, (IColumn[])paramList.toArray(new IColumn[0]));
    this.m_operands = paramETValueExprList;
  }
  
  public void close()
  {
    this.m_operands.close();
    this.m_isOpen = false;
  }
  
  public boolean isOpen()
  {
    return this.m_isOpen;
  }
  
  public void reset()
  {
    this.m_operands.reset();
    this.m_aggregator.reset();
  }
  
  public String getLogString()
  {
    return "ETAggregateFn: " + this.m_functionId;
  }
  
  public void update()
    throws ErrorException
  {
    this.m_aggregator.update(this.m_updateParameters);
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return this.m_operands.getNumChildren();
  }
  
  public void open()
  {
    this.m_operands.open();
    this.m_aggregator.reset();
    this.m_isOpen = false;
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return this.m_aggregator.retrieveData(paramETDataRequest);
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    super.registerWarningListener(paramIWarningListener);
    this.m_aggregator.registerWarningListener(paramIWarningListener);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    return this.m_operands.getChild(paramInt);
  }
  
  private static class ETValueUpdateParameters
    implements IAggregator.IUpdateParameters
  {
    private boolean[] m_hasMoreData;
    private ETDataRequest[] m_dataRequest;
    private ETValueExprList m_valueExprs;
    private IColumn[] m_inputMetadata;
    
    public ETValueUpdateParameters(ETValueExprList paramETValueExprList, IColumn[] paramArrayOfIColumn)
    {
      this.m_valueExprs = paramETValueExprList;
      this.m_inputMetadata = paramArrayOfIColumn;
      this.m_dataRequest = new ETDataRequest[paramArrayOfIColumn.length];
      for (int i = 0; i < paramArrayOfIColumn.length; i++) {
        this.m_dataRequest[i] = new ETDataRequest(paramArrayOfIColumn[i]);
      }
      this.m_hasMoreData = new boolean[paramArrayOfIColumn.length];
    }
    
    public ISqlDataWrapper getData(int paramInt)
      throws ErrorException
    {
      ETDataRequest localETDataRequest = this.m_dataRequest[paramInt];
      localETDataRequest.setOffset(0L);
      localETDataRequest.setMaxBytes(-1L);
      localETDataRequest.getData().setNull();
      this.m_hasMoreData[paramInt] = this.m_valueExprs.retrieveData(paramInt, localETDataRequest);
      return localETDataRequest.getData();
    }
    
    public ISqlDataWrapper getData(int paramInt, long paramLong1, long paramLong2)
      throws ErrorException
    {
      ETDataRequest localETDataRequest = this.m_dataRequest[paramInt];
      localETDataRequest.setOffset(paramLong1);
      localETDataRequest.setMaxBytes(paramLong2);
      localETDataRequest.getData().setNull();
      this.m_hasMoreData[paramInt] = this.m_valueExprs.retrieveData(paramInt, localETDataRequest);
      return localETDataRequest.getData();
    }
    
    public IColumn getMetadata(int paramInt)
    {
      return this.m_inputMetadata[paramInt];
    }
    
    public boolean hasMoreData(int paramInt)
    {
      return this.m_hasMoreData[paramInt];
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETAggregateFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */