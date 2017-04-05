package com.simba.sqlengine.dsiext.dataengine;

import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;

public class PassdownInformation
{
  public boolean canHandlePassdown(AEBooleanExpr paramAEBooleanExpr)
  {
    return true;
  }
  
  public boolean canHandlePassdown(AERelationalExpr paramAERelationalExpr)
  {
    return true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/PassdownInformation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */