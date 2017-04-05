package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.metadatautil.AEAbstractColumnInfo;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo.ColumnTypeFinder;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AEProxyColumn
  extends AEValueExpr
{
  private static final int NUM_CHILDREN = 0;
  private AEQueryScope m_resolvedQueryScope;
  private int m_columnNumber;
  private AERelationalExpr m_relExpr;
  private IColumnInfo m_columnInfo;
  private final String m_refToLogString;
  
  public AEProxyColumn(AEValueExpr paramAEValueExpr, AEQueryScope paramAEQueryScope, int paramInt)
  {
    assert ((null != paramAEQueryScope) && (null != paramAEValueExpr));
    AECoercionColumnInfo.ColumnTypeFinder localColumnTypeFinder = new AECoercionColumnInfo.ColumnTypeFinder();
    IColumnInfo.ColumnType localColumnType = null;
    try
    {
      localColumnType = (IColumnInfo.ColumnType)paramAEValueExpr.acceptVisitor(localColumnTypeFinder);
    }
    catch (ErrorException localErrorException)
    {
      throw SQLEngineExceptionFactory.runtimeException(localErrorException);
    }
    String str = localColumnTypeFinder.getLiterString();
    this.m_columnInfo = new ProxyColumnInfo(localColumnType, str, paramAEValueExpr.getColumn());
    this.m_resolvedQueryScope = paramAEQueryScope;
    this.m_columnNumber = paramInt;
    this.m_refToLogString = paramAEValueExpr.getLogString();
  }
  
  public AEProxyColumn(AEProxyColumn paramAEProxyColumn)
  {
    this.m_columnNumber = paramAEProxyColumn.m_columnNumber;
    this.m_resolvedQueryScope = paramAEProxyColumn.m_resolvedQueryScope;
    this.m_relExpr = paramAEProxyColumn.m_relExpr;
    this.m_refToLogString = paramAEProxyColumn.m_refToLogString;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return Collections.emptyList().iterator();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public IColumnInfo getProxiedColumnInfo()
  {
    return this.m_columnInfo;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AEProxyColumn)) {
      return false;
    }
    AEProxyColumn localAEProxyColumn = (AEProxyColumn)paramIAENode;
    return (localAEProxyColumn.m_relExpr == this.m_relExpr) && (localAEProxyColumn.m_columnNumber == this.m_columnNumber);
  }
  
  public IColumn getColumn()
  {
    if (null != this.m_relExpr) {
      return this.m_relExpr.getColumn(this.m_columnNumber);
    }
    return null;
  }
  
  public AEQueryScope getResolvedQueryScope()
  {
    return this.m_resolvedQueryScope;
  }
  
  public int getColumnNumber()
  {
    return this.m_columnNumber;
  }
  
  public AEProxyColumn copy()
  {
    return new AEProxyColumn(this);
  }
  
  public AERelationalExpr getRelationalExpr()
  {
    return this.m_relExpr;
  }
  
  public String getLogString()
  {
    String str1 = super.getLogString() + ": " + this.m_refToLogString;
    IColumn localIColumn = getColumn();
    if ((null == localIColumn) || (localIColumn.isUnnamed()))
    {
      str1 = str1 + ": column #" + Integer.toString(getColumnNumber());
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder(30);
      localStringBuilder.append(str1);
      localStringBuilder.append(": ");
      String str2 = localIColumn.getCatalogName();
      if ((null != str2) && (str2.length() > 0))
      {
        localStringBuilder.append(str2);
        localStringBuilder.append(".");
      }
      String str3 = localIColumn.getSchemaName();
      if ((null != str3) && (str3.length() > 0))
      {
        localStringBuilder.append(str3);
        localStringBuilder.append(".");
      }
      String str4 = localIColumn.getTableName();
      if ((null != str4) && (str4.length() > 0))
      {
        localStringBuilder.append(str4);
        localStringBuilder.append(".");
      }
      localStringBuilder.append(localIColumn.getName());
      str1 = localStringBuilder.toString();
    }
    return str1;
  }
  
  public void setRelationalExpr(AERelationalExpr paramAERelationalExpr)
  {
    this.m_relExpr = paramAERelationalExpr;
  }
  
  public void updateColumn()
    throws ErrorException
  {}
  
  private static class ProxyColumnInfo
    extends AEAbstractColumnInfo
  {
    private IColumnInfo.ColumnType m_type;
    private String m_literalString;
    private IColumn m_columnMeta;
    
    public ProxyColumnInfo(IColumnInfo.ColumnType paramColumnType, String paramString, IColumn paramIColumn)
    {
      this.m_type = paramColumnType;
      this.m_literalString = paramString;
      this.m_columnMeta = paramIColumn;
    }
    
    public IColumnInfo.ColumnType getColumnType()
    {
      return this.m_type;
    }
    
    public String getLiteralString()
    {
      return this.m_literalString;
    }
    
    protected IColumn getColumnMetadata()
    {
      return this.m_columnMeta;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEProxyColumn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */