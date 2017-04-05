package com.simba.sqlengine.aeprocessor;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;

public final class AEColumnInfo
{
  private AENamedRelationalExpr m_namedRelExpr;
  private int m_columnNum;
  private boolean m_isOuterReference;
  private AEQueryScope m_queryScope;
  
  public AEColumnInfo(AENamedRelationalExpr paramAENamedRelationalExpr, int paramInt, boolean paramBoolean)
  {
    this.m_namedRelExpr = paramAENamedRelationalExpr;
    this.m_columnNum = paramInt;
    this.m_isOuterReference = paramBoolean;
  }
  
  public AEColumnInfo(AEColumnInfo paramAEColumnInfo)
  {
    this.m_namedRelExpr = paramAEColumnInfo.m_namedRelExpr;
    this.m_columnNum = paramAEColumnInfo.m_columnNum;
    this.m_isOuterReference = paramAEColumnInfo.m_isOuterReference;
    this.m_queryScope = paramAEColumnInfo.m_queryScope;
  }
  
  public AENamedRelationalExpr getNamedRelationalExpr()
  {
    return this.m_namedRelExpr;
  }
  
  public int getColumnNum()
  {
    return this.m_columnNum;
  }
  
  public void setColumnNum(int paramInt)
  {
    this.m_columnNum = paramInt;
  }
  
  public boolean isOuterReference()
  {
    return this.m_isOuterReference;
  }
  
  public AEQueryScope getResolvedQueryScope()
  {
    return this.m_queryScope;
  }
  
  public void setResolvedQueryScope(AEQueryScope paramAEQueryScope)
  {
    this.m_queryScope = paramAEQueryScope;
  }
  
  public IColumn getColumn()
  {
    int i = getColumnNum();
    return getNamedRelationalExpr().getColumn(i);
  }
  
  public void setNamedRelationalExpr(AENamedRelationalExpr paramAENamedRelationalExpr)
  {
    if (paramAENamedRelationalExpr == null) {
      throw new NullPointerException("Set null as relation of column reference");
    }
    this.m_namedRelExpr = paramAENamedRelationalExpr;
  }
  
  public final boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof AEColumnInfo)) {
      return false;
    }
    AEColumnInfo localAEColumnInfo = (AEColumnInfo)paramObject;
    return (this.m_namedRelExpr == localAEColumnInfo.m_namedRelExpr) && (this.m_columnNum == localAEColumnInfo.m_columnNum) && (this.m_isOuterReference == localAEColumnInfo.m_isOuterReference);
  }
  
  public int hashCode()
  {
    int i = 17;
    i = 31 * i + (this.m_isOuterReference ? 1 : 0);
    i = 31 * i + (this.m_namedRelExpr != null ? this.m_namedRelExpr.hashCode() : 0);
    i = 31 * i + this.m_columnNum;
    return i;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/AEColumnInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */