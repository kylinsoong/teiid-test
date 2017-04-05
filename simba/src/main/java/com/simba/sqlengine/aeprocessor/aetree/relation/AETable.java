package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.AEQTableName.AEQTableNameBuilder;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AETable
  extends AENamedRelationalExpr
{
  private List<? extends IColumn> m_tableColumns;
  private DSIExtJResultSet m_table;
  
  public AETable(DSIExtJResultSet paramDSIExtJResultSet)
    throws ErrorException
  {
    if (null == paramDSIExtJResultSet) {
      throw new NullPointerException("table cannot be null.");
    }
    this.m_table = paramDSIExtJResultSet;
    this.m_tableColumns = new ArrayList(this.m_table.getSelectColumns());
  }
  
  private AETable(AETable paramAETable)
  {
    super(paramAETable);
    this.m_table = paramAETable.m_table;
    this.m_tableColumns = new ArrayList(paramAETable.m_tableColumns);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AETable copy()
  {
    return new AETable(this);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return Collections.emptyList().iterator();
  }
  
  public String getLogString()
  {
    StringBuilder localStringBuilder = new StringBuilder(20);
    localStringBuilder.append("AETable: ");
    localStringBuilder.append(new AEQTableName(this.m_table.getCatalogName(), this.m_table.getSchemaName(), this.m_table.getTableName()));
    if (hasCorrelationName()) {
      localStringBuilder.append(" AS ").append(getQTableName());
    }
    return localStringBuilder.toString();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AETable)) {
      return false;
    }
    AETable localAETable = (AETable)paramIAENode;
    return localAETable.m_table == this.m_table;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof AETable)) {
      return false;
    }
    return isEquivalent((AETable)paramObject);
  }
  
  public int hashCode()
  {
    return this.m_table.hashCode();
  }
  
  public IColumn getBaseColumn(int paramInt)
  {
    return (IColumn)this.m_tableColumns.get(paramInt);
  }
  
  public AEQTableName getBaseQTableName()
  {
    AEQTableName.AEQTableNameBuilder localAEQTableNameBuilder = new AEQTableName.AEQTableNameBuilder();
    String str1 = this.m_table.getCatalogName();
    if (null != str1) {
      localAEQTableNameBuilder.setCatalogName(str1);
    }
    String str2 = this.m_table.getSchemaName();
    if (null != str2) {
      localAEQTableNameBuilder.setSchemaName(str2);
    }
    localAEQTableNameBuilder.setTableName(this.m_table.getTableName());
    return localAEQTableNameBuilder.build();
  }
  
  public String getBaseTableName()
  {
    return this.m_table.getTableName();
  }
  
  public String getCatalogName()
  {
    if (hasCorrelationName()) {
      return "";
    }
    return this.m_table.getCatalogName();
  }
  
  public String getSchemaName()
  {
    if (hasCorrelationName()) {
      return "";
    }
    return this.m_table.getSchemaName();
  }
  
  public String getTableName()
  {
    if (hasCorrelationName()) {
      return getCorrelationName();
    }
    return this.m_table.getTableName();
  }
  
  public DSIExtJResultSet getTable()
  {
    return this.m_table;
  }
  
  public void setTable(DSIExtJResultSet paramDSIExtJResultSet)
  {
    if (paramDSIExtJResultSet == null) {
      throw new NullPointerException("null set on a AETable.");
    }
    this.m_table = paramDSIExtJResultSet;
  }
  
  public int getColumnCount()
  {
    return this.m_tableColumns.size();
  }
  
  public boolean hasCorrelationName()
  {
    return getCorrelationName().length() > 0;
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    if (paramAERelationalExpr.equals(this))
    {
      this.m_table.setDataNeeded(paramInt);
      return paramInt;
    }
    return -1;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {}
  
  public boolean getDataNeeded(int paramInt)
  {
    return this.m_table.getDataNeeded(paramInt);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AETable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */