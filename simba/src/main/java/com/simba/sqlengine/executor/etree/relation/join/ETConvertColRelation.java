package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.conversions.ISqlConverter;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

class ETConvertColRelation
  extends ETRelationalExpr
  implements IWarningSource
{
  private final ETRelationalExpr m_wrapped;
  private final ISqlConverter[] m_converters;
  private final Integer[] m_extraColMap;
  private final int m_colCount;
  private final IColumn[] m_extraCols;
  private IWarningListener m_warningListener;
  private final ETDataRequest[] m_convertDR;
  
  public ETConvertColRelation(ETRelationalExpr paramETRelationalExpr, IColumn[] paramArrayOfIColumn, Integer[] paramArrayOfInteger, ISqlConverter[] paramArrayOfISqlConverter, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    assert (paramArrayOfBoolean.length == paramArrayOfIColumn.length + paramETRelationalExpr.getColumnCount());
    this.m_wrapped = paramETRelationalExpr;
    this.m_extraColMap = paramArrayOfInteger;
    this.m_converters = paramArrayOfISqlConverter;
    this.m_extraCols = paramArrayOfIColumn;
    this.m_colCount = (this.m_wrapped.getColumnCount() + this.m_extraColMap.length);
    this.m_convertDR = new ETDataRequest[this.m_extraColMap.length];
    for (int i = 0; i < this.m_convertDR.length; i++)
    {
      assert (null != this.m_converters[i]);
      this.m_convertDR[i] = new ETDataRequest(this.m_wrapped.getColumn(this.m_extraColMap[i].intValue()));
    }
  }
  
  public void close()
  {
    this.m_wrapped.close();
  }
  
  public boolean isOpen()
  {
    return this.m_wrapped.isOpen();
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_wrapped.reset();
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    throw new IllegalStateException("Cannot visit ETConvertColRelation because it is not part of the ETree.");
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public IColumn getColumn(int paramInt)
  {
    if (paramInt < this.m_wrapped.getColumnCount()) {
      return this.m_wrapped.getColumn(paramInt);
    }
    return this.m_extraCols[(paramInt - this.m_wrapped.getColumnCount())];
  }
  
  public int getColumnCount()
  {
    return this.m_colCount;
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    assert ((paramInt >= 0) && (paramInt < this.m_colCount));
    if (paramInt < this.m_wrapped.getColumnCount()) {
      return super.getDataNeeded(paramInt);
    }
    return true;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_wrapped.getRowCount();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    this.m_wrapped.open(paramCursorType);
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (paramInt < this.m_wrapped.getColumnCount()) {
      return this.m_wrapped.retrieveData(paramInt, paramETDataRequest);
    }
    int i = paramInt - this.m_wrapped.getColumnCount();
    ETDataRequest localETDataRequest = this.m_convertDR[i];
    boolean bool = this.m_wrapped.retrieveData(this.m_extraColMap[i].intValue(), localETDataRequest);
    if (bool) {
      throw SQLEngineExceptionFactory.joinOnLongData(paramInt);
    }
    return ConversionUtil.doConvert(paramETDataRequest, localETDataRequest.getData(), this.m_converters[i], this.m_warningListener, false);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    return this.m_wrapped.move();
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_warningListener = paramIWarningListener;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/ETConvertColRelation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */