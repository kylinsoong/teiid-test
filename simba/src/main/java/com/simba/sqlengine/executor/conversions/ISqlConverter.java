package com.simba.sqlengine.executor.conversions;

import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.conv.ConversionResult;
import com.simba.support.exceptions.ErrorException;

public abstract interface ISqlConverter
{
  public abstract ConversionResult convert(ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/conversions/ISqlConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */