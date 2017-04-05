package com.simba.sqlengine.dsiext.dataengine;

public class SqlCustomBehaviourProvider
{
  protected IColumnFactory m_columnFactory;
  
  public IColumnFactory getColumnFactory()
  {
    return this.m_columnFactory;
  }
  
  protected void initColumnFactory(SqlDataEngine paramSqlDataEngine)
  {
    this.m_columnFactory = new DSIExtColumnFactory(paramSqlDataEngine);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/SqlCustomBehaviourProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */