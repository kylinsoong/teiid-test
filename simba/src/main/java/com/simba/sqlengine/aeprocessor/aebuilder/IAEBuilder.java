package com.simba.sqlengine.aeprocessor.aebuilder;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.support.exceptions.ErrorException;

public abstract interface IAEBuilder
{
  public abstract IAENode build(IPTNode paramIPTNode)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/IAEBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */