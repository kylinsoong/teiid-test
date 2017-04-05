package com.simba.sqlengine.executor.conversions;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.support.exceptions.ErrorException;

public abstract interface ISqlConverterFactory
{
  public abstract ISqlConverter createSqlConverter(IColumn paramIColumn1, IColumn paramIColumn2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/ISqlConverterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */