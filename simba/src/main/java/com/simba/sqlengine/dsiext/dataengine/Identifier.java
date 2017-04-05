package com.simba.sqlengine.dsiext.dataengine;

public class Identifier
{
  private String m_catalog;
  private String m_schema;
  private String m_name;
  
  public String getCatalog()
  {
    return this.m_catalog;
  }
  
  public String getName()
  {
    return this.m_name;
  }
  
  public String getSchema()
  {
    return this.m_schema;
  }
  
  public void setCatalog(String paramString)
  {
    this.m_catalog = paramString;
  }
  
  public void setName(String paramString)
  {
    this.m_name = paramString;
  }
  
  public void setSchema(String paramString)
  {
    this.m_schema = paramString;
  }
  
  public String toString()
  {
    String str = sqlQuoted(this.m_name);
    if (null != this.m_schema) {
      str = sqlQuoted(this.m_schema) + "." + str;
    }
    if (null != this.m_catalog) {
      str = sqlQuoted(this.m_catalog) + "." + str;
    }
    return str;
  }
  
  private static String sqlQuoted(String paramString)
  {
    return "\"" + paramString.replace("\"", "\"\"") + "\"";
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/Identifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */