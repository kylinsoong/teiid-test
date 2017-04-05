package com.simba.sqlengine.dsiext.dataengine.ddl;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.AEUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TableSpecification
{
  private String m_catalog;
  private String m_schema;
  private String m_name;
  private List<IColumn> m_columns;
  private List<TableConstraint> m_constraints;
  
  public TableSpecification(String paramString1, String paramString2, String paramString3, List<IColumn> paramList, List<TableConstraint> paramList1)
  {
    if ((paramString3 == null) || (paramString3.equals(""))) {
      throw new IllegalArgumentException("Table must have a valid name.");
    }
    if ((paramList == null) || (paramList.size() == 0)) {
      throw new IllegalArgumentException("A table must have at least one column");
    }
    this.m_catalog = (paramString1 == null ? "" : paramString1);
    this.m_schema = (paramString2 == null ? "" : paramString2);
    this.m_name = paramString3;
    this.m_columns = paramList;
    if (paramList1 == null) {
      this.m_constraints = new ArrayList();
    } else {
      this.m_constraints = paramList1;
    }
  }
  
  public String getCatalog()
  {
    return this.m_catalog;
  }
  
  public String getSchema()
  {
    return this.m_schema;
  }
  
  public String getTableName()
  {
    return this.m_name;
  }
  
  public List<IColumn> getColumns()
  {
    return Collections.unmodifiableList(this.m_columns);
  }
  
  public List<TableConstraint> getConstraints()
  {
    return Collections.unmodifiableList(this.m_constraints);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (0 < this.m_catalog.length())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_catalog)).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_schema)).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_name));
    }
    else if (0 < this.m_schema.length())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_schema)).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_name));
    }
    else
    {
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_name));
    }
    localStringBuilder.append("(");
    Iterator localIterator = this.m_columns.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (IColumn)localIterator.next();
      localStringBuilder.append(" ");
      localStringBuilder.append(((IColumn)localObject).getName());
      localStringBuilder.append(":");
      localStringBuilder.append(((IColumn)localObject).getTypeMetadata().getTypeName());
      localStringBuilder.append(",");
    }
    localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    localStringBuilder.append(" )");
    if (!this.m_constraints.isEmpty())
    {
      localStringBuilder.append(" => TABLE CONSTRAINT(S): ");
      localIterator = this.m_constraints.iterator();
      while (localIterator.hasNext())
      {
        localObject = (TableConstraint)localIterator.next();
        localStringBuilder.append(localObject);
        localStringBuilder.append("; ");
      }
      localStringBuilder.setLength(localStringBuilder.length() - 2);
    }
    return localStringBuilder.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ddl/TableSpecification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */