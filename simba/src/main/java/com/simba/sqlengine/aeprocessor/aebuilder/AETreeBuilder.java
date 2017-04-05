package com.simba.sqlengine.aeprocessor.aebuilder;

import com.simba.sqlengine.aeprocessor.aebuilder.statement.AEStatementBuilder;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.support.exceptions.ErrorException;

public class AETreeBuilder
{
  public static IAEStatement build(IPTNode paramIPTNode, SqlDataEngine paramSqlDataEngine)
    throws ErrorException
  {
    return (IAEStatement)new AEStatementBuilder(paramSqlDataEngine).build(paramIPTNode);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/AETreeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */