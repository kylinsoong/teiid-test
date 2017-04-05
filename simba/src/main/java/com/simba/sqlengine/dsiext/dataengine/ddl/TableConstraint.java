package com.simba.sqlengine.dsiext.dataengine.ddl;

public abstract class TableConstraint
{
  private String m_name;
  
  protected TableConstraint(String paramString)
  {
    this.m_name = paramString;
  }
  
  public String getName()
  {
    return this.m_name;
  }
  
  public abstract ConstraintType getType();
  
  public static enum ConstraintType
  {
    PRIMARY_KEY,  UNIQUE;
    
    private ConstraintType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ddl/TableConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */