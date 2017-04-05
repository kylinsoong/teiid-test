package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.support.exceptions.ErrorException;

public class SqlIntegralConverterFactory
  implements ISqlConverterFactory
{
  public ISqlConverter createSqlConverter(IColumn paramIColumn1, IColumn paramIColumn2)
    throws ErrorException
  {
    return new SqlIntegralConverter(paramIColumn1, paramIColumn2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/SqlIntegralConverterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */