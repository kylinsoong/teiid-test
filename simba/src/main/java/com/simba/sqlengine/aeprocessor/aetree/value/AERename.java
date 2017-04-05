package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AERename
  extends AEUnaryValueExpr
{
  private ColumnMetadata m_columnMeta;
  
  public AERename(String paramString, AEValueExpr paramAEValueExpr)
    throws ErrorException
  {
    super(paramAEValueExpr);
    this.m_columnMeta = AEValueExpr.createColumnMetadata(paramAEValueExpr.getColumn());
    setName(paramString);
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
    if (!(paramIAENode instanceof AERename)) {
      return false;
    }
    AERename localAERename = (AERename)paramIAENode;
    return (getName().equals(localAERename.getName())) && (getOperand().isEquivalent(localAERename.getOperand()));
  }
  
  public AERename copy()
  {
    try
    {
      return new AERename(getName(), getOperand());
    }
    catch (ErrorException localErrorException)
    {
      throw new RuntimeException(localErrorException);
    }
  }
  
  public IColumn getColumn()
  {
    return this.m_columnMeta;
  }
  
  public String getLogString()
  {
    return super.getLogString() + ": " + getName();
  }
  
  public String toString()
  {
    return "AERename[" + getOperand().toString() + " AS " + getQColumnName() + "]";
  }
  
  public void setName(String paramString)
  {
    if ((null == paramString) || (0 == paramString.length())) {
      throw new IllegalArgumentException("name cannot be empty: " + paramString);
    }
    this.m_columnMeta.setName(paramString);
    this.m_columnMeta.setLabel(paramString);
  }
  
  public void updateColumn()
    throws ErrorException
  {
    String str = getName();
    this.m_columnMeta = AEValueExpr.createColumnMetadata(getOperand().getColumn());
    setName(str);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AERename.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */