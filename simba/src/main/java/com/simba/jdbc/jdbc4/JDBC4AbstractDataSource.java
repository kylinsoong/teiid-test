package com.simba.jdbc.jdbc4;

import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.jdbc4.JDBC4ExceptionConverter;
import com.simba.jdbc.common.AbstractDataSource;
import com.simba.jdbc.common.JDBCObjectFactory;

public abstract class JDBC4AbstractDataSource
  extends AbstractDataSource
{
  public JDBC4AbstractDataSource()
  {
    ExceptionConverter.setInstance(new JDBC4ExceptionConverter());
  }
  
  protected JDBCObjectFactory createJDBCObjectFactory()
  {
    return new JDBC4ObjectFactory();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/JDBC4AbstractDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */