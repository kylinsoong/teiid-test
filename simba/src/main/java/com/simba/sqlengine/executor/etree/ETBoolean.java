package com.simba.sqlengine.executor.etree;

public enum ETBoolean
{
  SQL_BOOLEAN_TRUE,  SQL_BOOLEAN_FALSE,  SQL_BOOLEAN_UNKNOWN;
  
  private ETBoolean() {}
  
  public abstract ETBoolean not();
  
  public abstract ETBoolean and(ETBoolean paramETBoolean);
  
  public abstract ETBoolean or(ETBoolean paramETBoolean);
  
  public static final ETBoolean fromBoolean(boolean paramBoolean)
  {
    return paramBoolean ? SQL_BOOLEAN_TRUE : SQL_BOOLEAN_FALSE;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETBoolean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */