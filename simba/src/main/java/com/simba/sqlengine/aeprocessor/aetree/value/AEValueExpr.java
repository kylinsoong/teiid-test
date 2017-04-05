package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataUtilities;
import com.simba.support.exceptions.ErrorException;

public abstract class AEValueExpr
  implements IAENode
{
  private IAENode m_parent = null;
  
  protected AEValueExpr() {}
  
  protected AEValueExpr(AEValueExpr paramAEValueExpr) {}
  
  public abstract IColumn getColumn();
  
  public IAENode getParent()
  {
    return this.m_parent;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    this.m_parent = paramIAENode;
  }
  
  public abstract AEValueExpr copy();
  
  public String getCatalogName()
  {
    return getColumn().getCatalogName();
  }
  
  public String getLabel()
  {
    return getColumn().getLabel();
  }
  
  public String getName()
  {
    return getColumn().getName();
  }
  
  public Nullable getNullable()
  {
    return getColumn().getNullable();
  }
  
  public AEQColumnName getQColumnName()
  {
    return new AEQColumnName(new AEQTableName(getCatalogName(), getSchemaName(), getTableName()), getName());
  }
  
  public String getSchemaName()
  {
    return getColumn().getSchemaName();
  }
  
  public String getTableName()
  {
    return getColumn().getTableName();
  }
  
  public TypeMetadata getTypeMetadata()
  {
    return getColumn().getTypeMetadata();
  }
  
  public boolean isSortable()
  {
    return getColumn().getTypeMetadata().isSortable();
  }
  
  public boolean isUnnamed()
  {
    return getColumn().isUnnamed();
  }
  
  public boolean matchesName(AEQColumnName paramAEQColumnName, boolean paramBoolean)
  {
    if ((null == getName()) || ("".equals(getName())) || (!stringEquals(getName(), paramAEQColumnName.getColName(), paramBoolean))) {
      return false;
    }
    if ((!"".equals(paramAEQColumnName.getTableName())) && (!stringEquals(getTableName(), paramAEQColumnName.getTableName(), paramBoolean))) {
      return false;
    }
    if ((!"".equals(paramAEQColumnName.getSchemaName())) && (!stringEquals(getSchemaName(), paramAEQColumnName.getSchemaName(), paramBoolean))) {
      return false;
    }
    return ("".equals(paramAEQColumnName.getCatalogName())) || (stringEquals(getCatalogName(), paramAEQColumnName.getCatalogName(), paramBoolean));
  }
  
  public boolean matchesNameCaseInsensitive(AEQColumnName paramAEQColumnName)
  {
    return matchesName(paramAEQColumnName, false);
  }
  
  public boolean matchesNameCaseSensitive(AEQColumnName paramAEQColumnName)
  {
    return matchesName(paramAEQColumnName, true);
  }
  
  public abstract void updateColumn()
    throws ErrorException;
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public String toString()
  {
    return getLogString();
  }
  
  protected static ColumnMetadata createColumnMetadata()
  {
    return MetadataUtilities.defaultColumnMetadata();
  }
  
  protected static ColumnMetadata createColumnMetadata(IColumn paramIColumn)
  {
    return ColumnMetadata.copyOf(paramIColumn);
  }
  
  protected static TypeMetadata createTypeMetadata(TypeMetadata paramTypeMetadata)
  {
    return TypeMetadata.copyOf(paramTypeMetadata);
  }
  
  private static boolean stringEquals(String paramString1, String paramString2, boolean paramBoolean)
  {
    return paramBoolean ? paramString1.equals(paramString2) : paramString1.equalsIgnoreCase(paramString2);
  }
  
  public static enum AEValueExprOperator
  {
    BINARY_PLUS,  BINARY_MINUS,  MULTIPLY,  DIVIDE,  CONCAT,  INSERT,  UNARY_PLUS,  UNARY_MINUS,  UNION,  EXCEPT,  INTERSECT,  IN,  NO_OP;
    
    private AEValueExprOperator() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEValueExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */