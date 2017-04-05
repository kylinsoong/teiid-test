package com.simba.sqlengine.parser;

import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.type.PTCountLimit;
import com.simba.sqlengine.parser.type.PTStringConstraint;

public abstract interface IPTLimitChecker
{
  public abstract void checkString(PTStringConstraint paramPTStringConstraint, String paramString)
    throws SQLEngineException;
  
  public abstract void checkCount(PTCountLimit paramPTCountLimit, int paramInt)
    throws SQLEngineException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/IPTLimitChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */