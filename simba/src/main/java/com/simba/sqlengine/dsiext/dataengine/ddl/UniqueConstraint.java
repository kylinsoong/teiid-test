package com.simba.sqlengine.dsiext.dataengine.ddl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UniqueConstraint
  extends TableConstraint
{
  private static final TableConstraint.ConstraintType CONSTRAINT_TYPE = TableConstraint.ConstraintType.UNIQUE;
  private Set<String> m_uniqueColumns;
  
  public UniqueConstraint(String paramString, Collection<String> paramCollection)
  {
    super(paramString);
    if ((paramCollection == null) || (paramCollection.size() == 0)) {
      throw new IllegalArgumentException("Unique constraint requires at least one column");
    }
    this.m_uniqueColumns = new HashSet();
    this.m_uniqueColumns.addAll(paramCollection);
  }
  
  public Set<String> getUniqueColumns()
  {
    return this.m_uniqueColumns;
  }
  
  public TableConstraint.ConstraintType getType()
  {
    return CONSTRAINT_TYPE;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UNIQUE COLUMN(");
    String[] arrayOfString1 = (String[])this.m_uniqueColumns.toArray(new String[this.m_uniqueColumns.size()]);
    Arrays.sort(arrayOfString1);
    for (String str : arrayOfString1)
    {
      localStringBuilder.append(str);
      localStringBuilder.append(",");
    }
    localStringBuilder.setLength(localStringBuilder.length() - 1);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ddl/UniqueConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */