package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public class ETTop
  extends ETUnaryRelationalExpr
{
  private ETValueExpr m_selectLimit;
  private long m_rowCount;
  private long m_rowCountLimit;
  
  public ETTop(ETRelationalExpr paramETRelationalExpr, boolean[] paramArrayOfBoolean, ETValueExpr paramETValueExpr, boolean paramBoolean)
  {
    super(paramETRelationalExpr, paramArrayOfBoolean);
    if (paramBoolean) {
      throw new RuntimeException(SQLEngineExceptionFactory.featureNotImplementedException("TOP %"));
    }
    this.m_selectLimit = paramETValueExpr;
    this.m_rowCount = 0L;
    this.m_rowCountLimit = 0L;
  }
  
  public void close()
  {
    super.close();
    this.m_selectLimit.close();
  }
  
  public boolean isOpen()
  {
    return (super.isOpen()) && (this.m_selectLimit.isOpen());
  }
  
  public void reset()
    throws ErrorException
  {
    super.reset();
    this.m_selectLimit.reset();
    this.m_rowCount = 0L;
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
  
  public long getRowCount()
    throws ErrorException
  {
    assert (isOpen()) : "getRowCount() called when ETTop node was not open.";
    long l = getOperand().getRowCount();
    if ((-1L == l) || (l <= this.m_rowCountLimit)) {
      return l;
    }
    return this.m_rowCountLimit;
  }
  
  public ETValueExpr getSelectLimit()
  {
    return this.m_selectLimit;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    super.open(paramCursorType);
    this.m_selectLimit.open();
    ETDataRequest localETDataRequest = new ETDataRequest(0L, -1L, new ColumnMetadata(TypeMetadata.createTypeMetadata(-5)));
    BigInteger localBigInteger = null;
    try
    {
      this.m_selectLimit.retrieveData(localETDataRequest);
      localBigInteger = localETDataRequest.getData().getBigInt();
    }
    catch (ErrorException localErrorException)
    {
      throw SQLEngineExceptionFactory.incompatibleTypesException("TOP", "UNKNOWN");
    }
    if ((localBigInteger.compareTo(new BigInteger(String.valueOf(Long.MAX_VALUE))) > 0) || (localBigInteger.compareTo(BigInteger.ZERO) < 0)) {
      throw SQLEngineExceptionFactory.invalidTopLimitValue(localBigInteger.toString());
    }
    this.m_rowCountLimit = localBigInteger.longValue();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    return getOperand().retrieveData(paramInt, paramETDataRequest);
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    if ((this.m_rowCount < this.m_rowCountLimit) && (getOperand().move()))
    {
      this.m_rowCount += 1L;
      return true;
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETTop.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */