package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.support.exceptions.ErrorException;

public class AENegate
  extends AEUnaryValueExpr
{
  private IColumn m_columnMeta;
  private final ICoercionHandler m_coercionHandler;
  
  public AENegate(AEValueExpr paramAEValueExpr, SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    super(paramAEValueExpr);
    this.m_coercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    this.m_columnMeta = this.m_coercionHandler.coerceUnaryMinusColumn(new AECoercionColumnInfo(paramAEValueExpr));
  }
  
  public AENegate(AENegate paramAENegate)
  {
    super(paramAENegate.getOperand());
    this.m_coercionHandler = paramAENegate.m_coercionHandler;
    this.m_columnMeta = createColumnMetadata(paramAENegate.m_columnMeta);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AENegate)) {
      return false;
    }
    AENegate localAENegate = (AENegate)paramIAENode;
    return (this.m_columnMeta.equals(localAENegate.m_columnMeta)) && (getOperand().isEquivalent(localAENegate.getOperand()));
  }
  
  public AENegate copy()
  {
    return new AENegate(this);
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public IColumn getColumn()
  {
    return this.m_columnMeta;
  }
  
  public void updateColumn()
    throws ErrorException
  {
    this.m_columnMeta = this.m_coercionHandler.coerceUnaryMinusColumn(new AECoercionColumnInfo(getOperand()));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AENegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */