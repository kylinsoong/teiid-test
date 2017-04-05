package com.simba.sqlengine.dsiext.dataengine.ddl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PrimaryKeyConstraint
  extends TableConstraint
{
  private static final TableConstraint.ConstraintType CONSTRAINT_TYPE = TableConstraint.ConstraintType.PRIMARY_KEY;
  private Set<String> m_primaryKeyColumns;
  
  public PrimaryKeyConstraint(String paramString, Collection<String> paramCollection)
  {
    super(paramString);
    if ((paramCollection == null) || (paramCollection.size() == 0)) {
      throw new IllegalArgumentException("Primary key constraint requires at least one column");
    }
    this.m_primaryKeyColumns = new HashSet();
    this.m_primaryKeyColumns.addAll(paramCollection);
  }
  
  public Set<String> getPrimaryKeyColumns()
  {
    return Collections.unmodifiableSet(this.m_primaryKeyColumns);
  }
  
  public TableConstraint.ConstraintType getType()
  {
    return CONSTRAINT_TYPE;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PRIMARY KEY(");
    String[] arrayOfString1 = (String[])this.m_primaryKeyColumns.toArray(new String[this.m_primaryKeyColumns.size()]);
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ddl/PrimaryKeyConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */