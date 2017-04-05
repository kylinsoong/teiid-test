package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.conversions.ISqlConverter;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public class ETRelationalConvert
  extends ETUnaryRelationalExpr
  implements IWarningSource
{
  private List<ISqlConverter> m_converters;
  private List<IColumn> m_metadatas;
  private List<ETDataRequest> m_columnData;
  private IWarningListener m_warningListener;
  private final boolean m_errorOnTruncation;
  
  public ETRelationalConvert(List<IColumn> paramList, List<ISqlConverter> paramList1, boolean paramBoolean, ETRelationalExpr paramETRelationalExpr, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    super(paramETRelationalExpr, paramArrayOfBoolean);
    this.m_errorOnTruncation = paramBoolean;
    int i = paramETRelationalExpr.getColumnCount();
    if ((paramList1.size() != i) || (paramList.size() != i)) {
      throw new IllegalArgumentException("Column count mismatch");
    }
    this.m_metadatas = paramList;
    this.m_converters = new ArrayList(paramList1.size());
    this.m_converters.addAll(paramList1);
    this.m_columnData = new ArrayList();
    for (int j = 0; j < paramETRelationalExpr.getColumnCount(); j++)
    {
      ISqlConverter localISqlConverter = (ISqlConverter)this.m_converters.get(j);
      if (localISqlConverter != null) {
        this.m_columnData.add(new ETDataRequest(getOperand().getColumn(j)));
      } else {
        this.m_columnData.add(null);
      }
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public IColumn getColumn(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.m_metadatas.size())) {
      throw new IndexOutOfBoundsException("index: " + paramInt);
    }
    return (IColumn)this.m_metadatas.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_metadatas.size();
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return getOperand().getRowCount();
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ETDataRequest localETDataRequest = (ETDataRequest)this.m_columnData.get(paramInt);
    if (localETDataRequest == null) {
      return getOperand().retrieveData(paramInt, paramETDataRequest);
    }
    boolean bool = getOperand().retrieveData(paramInt, localETDataRequest);
    if (bool) {
      throw new IllegalStateException("Has more data after retrieve all data");
    }
    return ConversionUtil.doConvert(paramETDataRequest, localETDataRequest.getData(), (ISqlConverter)this.m_converters.get(paramInt), this.m_warningListener, this.m_errorOnTruncation);
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    return getOperand().doMove();
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    if (paramIWarningListener == null) {
      throw new IllegalArgumentException("listener is null.");
    }
    this.m_warningListener = paramIWarningListener;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/ETRelationalConvert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */