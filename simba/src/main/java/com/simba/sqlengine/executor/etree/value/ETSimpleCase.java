package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.executor.datawrapper.DefaultSqlDataWrapper;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.bool.ETComparison;
import com.simba.sqlengine.executor.etree.bool.functor.comp.BooleanFunctorFactory;
import com.simba.sqlengine.executor.etree.bool.functor.comp.IBooleanCompFunctor;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.materializer.ConvMaterializeUtil;
import com.simba.sqlengine.executor.materializer.MaterializerContext;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.List;

public class ETSimpleCase
  extends ETValueExpr
{
  private ETCachedValueExpr m_caseOperand;
  private ETValueExprList m_whenClauses;
  private ETValueExpr m_elseExpr;
  private ETBooleanExpr[] m_conditionsList;
  
  public ETSimpleCase(ETValueExpr paramETValueExpr1, IColumn paramIColumn, ETValueExprList paramETValueExprList, ETValueExpr paramETValueExpr2, List<IColumn> paramList, MaterializerContext paramMaterializerContext)
    throws ErrorException
  {
    this.m_caseOperand = new ETCachedValueExpr(paramETValueExpr1, paramIColumn);
    this.m_whenClauses = paramETValueExprList;
    this.m_elseExpr = paramETValueExpr2;
    initConditions(paramMaterializerContext, paramList, paramIColumn);
  }
  
  public void close()
  {
    this.m_caseOperand.close();
    this.m_whenClauses.close();
    this.m_elseExpr.close();
    for (int i = 0; i < this.m_conditionsList.length; i++) {
      this.m_conditionsList[i].close();
    }
  }
  
  public boolean isOpen()
  {
    if ((!this.m_caseOperand.isOpen()) || (!this.m_elseExpr.isOpen())) {
      return false;
    }
    Iterator localIterator = this.m_whenClauses.getChildItr();
    for (int i = 0; localIterator.hasNext(); i++) {
      if ((!((ETValueExpr)localIterator.next()).isOpen()) || (!this.m_conditionsList[i].isOpen())) {
        return false;
      }
    }
    return true;
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
  
  public void open()
  {
    this.m_caseOperand.open();
    this.m_whenClauses.open();
    this.m_elseExpr.open();
    for (int i = 0; i < this.m_conditionsList.length; i++) {
      this.m_conditionsList[i].close();
    }
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    Iterator localIterator = this.m_whenClauses.getChildItr();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      ETSimpleWhenClause localETSimpleWhenClause = (ETSimpleWhenClause)localIterator.next();
      if (ETBoolean.SQL_BOOLEAN_TRUE == this.m_conditionsList[i].evaluate())
      {
        this.m_caseOperand.clearCache();
        return localETSimpleWhenClause.retrieveData(paramETDataRequest);
      }
    }
    this.m_caseOperand.clearCache();
    return this.m_elseExpr.retrieveData(paramETDataRequest);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if (0 == paramInt) {
      return this.m_whenClauses;
    }
    if (1 == paramInt) {
      return this.m_elseExpr;
    }
    throw new IndexOutOfBoundsException();
  }
  
  private void initConditions(MaterializerContext paramMaterializerContext, List<IColumn> paramList, IColumn paramIColumn)
    throws ErrorException
  {
    int i = this.m_whenClauses.getNumChildren();
    this.m_conditionsList = new ETBooleanExpr[i];
    Iterator localIterator = this.m_whenClauses.getChildItr();
    for (int j = 0; localIterator.hasNext(); j++)
    {
      ETSimpleWhenClause localETSimpleWhenClause = (ETSimpleWhenClause)localIterator.next();
      ETValueExpr localETValueExpr1 = ConvMaterializeUtil.addConversionNodeWhenNeeded(this.m_caseOperand, paramIColumn, (IColumn)paramList.get(j), paramMaterializerContext);
      ETValueExpr localETValueExpr2 = localETSimpleWhenClause.getWhenOperand();
      IBooleanCompFunctor localIBooleanCompFunctor = BooleanFunctorFactory.getBoolCompFunctor(AEComparisonType.EQUAL, ((IColumn)paramList.get(j)).getTypeMetadata());
      this.m_conditionsList[j] = new ETComparison((IColumn)paramList.get(j), localETValueExpr1, localETValueExpr2, localIBooleanCompFunctor);
    }
  }
  
  private static class ETCachedValueExpr
    extends ETValueExpr
  {
    private ETValueExpr m_cachedNode;
    private boolean m_hasCache;
    private boolean m_hasMoreData;
    private long m_lastRetrievedOffset;
    private ETDataRequest m_dataCache;
    
    public ETCachedValueExpr(ETValueExpr paramETValueExpr, IColumn paramIColumn)
      throws ErrorException
    {
      this.m_cachedNode = paramETValueExpr;
      this.m_hasCache = false;
      this.m_hasMoreData = false;
      this.m_lastRetrievedOffset = 0L;
      this.m_dataCache = new ETDataRequest(paramIColumn);
    }
    
    public void close()
    {
      this.m_cachedNode.close();
    }
    
    public boolean isOpen()
    {
      return this.m_cachedNode.isOpen();
    }
    
    public void reset()
    {
      clearCache();
      this.m_cachedNode.reset();
    }
    
    public void clearCache()
    {
      this.m_hasCache = false;
    }
    
    public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
      throws ErrorException
    {
      return (T)this.m_cachedNode.acceptVisitor(paramIETNodeVisitor);
    }
    
    public int getNumChildren()
    {
      return 1;
    }
    
    public void open()
    {
      this.m_cachedNode.open();
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      if (needRefreshCache(paramETDataRequest))
      {
        this.m_dataCache.setMaxBytes(paramETDataRequest.getMaxSize());
        this.m_dataCache.setOffset(paramETDataRequest.getOffset());
        this.m_hasMoreData = this.m_cachedNode.retrieveData(this.m_dataCache);
        this.m_lastRetrievedOffset = paramETDataRequest.getOffset();
        this.m_hasCache = true;
      }
      DefaultSqlDataWrapper.setDataWrapperFromDataWrapper(this.m_dataCache.getData(), paramETDataRequest.getData());
      boolean bool = false;
      if ((!paramETDataRequest.getData().isNull()) && (TypeUtilities.isCharacterType(paramETDataRequest.getData().getType()))) {
        bool = DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
      } else if ((!paramETDataRequest.getData().isNull()) && (TypeUtilities.isBinaryType(paramETDataRequest.getData().getType()))) {
        bool = DataRetrievalUtil.retrieveBinaryData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
      }
      return (this.m_hasMoreData) || (bool);
    }
    
    protected IETNode getChild(int paramInt)
      throws IndexOutOfBoundsException
    {
      if (0 == paramInt) {
        return this.m_cachedNode;
      }
      throw new IndexOutOfBoundsException();
    }
    
    private boolean needRefreshCache(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      if ((!this.m_hasCache) || (this.m_lastRetrievedOffset > paramETDataRequest.getOffset())) {
        return true;
      }
      if (-1L == paramETDataRequest.getMaxSize()) {
        return this.m_hasMoreData;
      }
      if (!this.m_hasMoreData) {
        return false;
      }
      int i;
      long l;
      if (TypeUtilities.isCharacterType(paramETDataRequest.getData().getType()))
      {
        i = paramETDataRequest.getData().getChar().length();
        l = i - (paramETDataRequest.getOffset() - this.m_lastRetrievedOffset) / 2L;
        return l < paramETDataRequest.getMaxSize();
      }
      if (TypeUtilities.isBinaryType(paramETDataRequest.getData().getType()))
      {
        i = paramETDataRequest.getData().getBinary().length;
        l = i - (paramETDataRequest.getOffset() - this.m_lastRetrievedOffset);
        return l < paramETDataRequest.getMaxSize();
      }
      return false;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETSimpleCase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */