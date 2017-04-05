package com.simba.sqlengine.aeprocessor;

public final class AEQColumnName
{
  private AEQTableName m_qTable;
  private String m_colName;
  
  public AEQColumnName(AEQTableName paramAEQTableName, String paramString)
  {
    if ((null == paramString) || (null == paramAEQTableName)) {
      throw new IllegalArgumentException("null parameters are not allowed.");
    }
    this.m_qTable = paramAEQTableName;
    this.m_colName = paramString;
  }
  
  public String getColName()
  {
    return this.m_colName;
  }
  
  public String getCatalogName()
  {
    return this.m_qTable.getCatalogName();
  }
  
  public String getSchemaName()
  {
    return this.m_qTable.getSchemaName();
  }
  
  public String getTableName()
  {
    return this.m_qTable.getTableName();
  }
  
  public AEQTableName getQTable()
  {
    return this.m_qTable;
  }
  
  public boolean hasQualifier()
  {
    return this.m_qTable.hasTableName();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (0 < getCatalogName().length())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(getCatalogName())).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(getSchemaName())).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(getTableName())).append(".");
    }
    else if (0 < getSchemaName().length())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(getSchemaName())).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(getTableName())).append(".");
    }
    else if (0 < getTableName().length())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(getTableName())).append(".");
    }
    localStringBuilder.append(AEUtils.sqlQuoted(getColName()));
    return localStringBuilder.toString();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/AEQColumnName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */